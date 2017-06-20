/*     */ package com.avrgaming.civcraft.cache;
/*     */ 
/*     */ import com.avrgaming.civcraft.components.PlayerProximityComponent;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.threading.tasks.FireWorkTask;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Queue;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.FireworkEffect;
/*     */ import org.bukkit.FireworkEffect.Builder;
/*     */ import org.bukkit.FireworkEffect.Type;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CannonExplosionProjectile
/*     */ {
/*     */   Location loc;
/*     */   Location target;
/*  45 */   int speed = 6;
/*  46 */   int damage = 40;
/*  47 */   int splash = 30;
/*     */   Buildable buildable;
/*     */   PlayerProximityComponent proximityComponent;
/*     */   
/*     */   public CannonExplosionProjectile(Buildable buildable, Location target) {
/*  52 */     this.proximityComponent = new PlayerProximityComponent();
/*  53 */     this.proximityComponent.createComponent(buildable);
/*  54 */     this.proximityComponent.setCenter(new BlockCoord(target));
/*     */   }
/*     */   
/*     */   public Vector getVectorBetween(Location to, Location from) {
/*  58 */     Vector dir = new Vector();
/*     */     
/*  60 */     dir.setX(to.getX() - from.getX());
/*  61 */     dir.setY(to.getY() - from.getY());
/*  62 */     dir.setZ(to.getZ() - from.getZ());
/*     */     
/*  64 */     return dir;
/*     */   }
/*     */   
/*     */   public boolean advance() {
/*  68 */     Vector dir = getVectorBetween(this.target, this.loc).normalize();
/*  69 */     double distance = this.loc.distanceSquared(this.target);
/*  70 */     dir.multiply(this.speed);
/*     */     
/*  72 */     this.loc.add(dir);
/*  73 */     this.loc.getWorld().createExplosion(this.loc, 0.0F, false);
/*  74 */     distance = this.loc.distanceSquared(this.target);
/*  75 */     BlockCoord center = this.proximityComponent.getCenter();
/*  76 */     center.setFromLocation(this.loc);
/*     */     
/*  78 */     if (distance < this.speed * 1.5D) {
/*  79 */       this.loc.setX(this.target.getX());
/*  80 */       this.loc.setY(this.target.getY());
/*  81 */       this.loc.setZ(this.target.getZ());
/*  82 */       onHit();
/*  83 */       return true;
/*     */     }
/*     */     
/*  86 */     return false;
/*     */   }
/*     */   
/*     */   public void onHit()
/*     */   {
/*  91 */     int spread = 3;
/*  92 */     int[][] offset = { { -1 }, { 1 }, { 0, -1 }, { 0, 1 } };
/*  93 */     for (int i = 0; i < 4; i++) {
/*  94 */       int x = offset[i][0] * spread;
/*  95 */       int y = 0;
/*  96 */       int z = offset[i][1] * spread;
/*     */       
/*  98 */       Location location = new Location(this.loc.getWorld(), this.loc.getX(), this.loc.getY(), this.loc.getZ());
/*  99 */       location = location.add(x, y, z);
/*     */       
/* 101 */       launchExplodeFirework(location);
/*     */       
/* 103 */       setFireAt(location, spread);
/*     */     }
/*     */     
/* 106 */     launchExplodeFirework(this.loc);
/*     */     
/* 108 */     damagePlayers(this.loc, this.splash);
/* 109 */     setFireAt(this.loc, spread);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void damagePlayers(Location loc, int radius)
/*     */   {
/* 172 */     TaskMaster.asyncTask(new Runnable()
/*     */     {
/*     */       int radius;
/*     */       
/*     */       public void run()
/*     */       {
/* 146 */         Queue<Player> playerList = new LinkedList();
/* 147 */         Queue<Double> damageList = new LinkedList();
/*     */         
/*     */ 
/*     */ 
/* 151 */         for (PlayerLocationCache pc : PlayerLocationCache.getCache()) {
/* 152 */           if (pc.getCoord().distanceSquared(new BlockCoord(CannonExplosionProjectile.this.target)) < this.radius) {
/*     */             try {
/* 154 */               Player player = CivGlobal.getPlayer(pc.getName());
/* 155 */               playerList.add(player);
/* 156 */               damageList.add(Double.valueOf(CannonExplosionProjectile.this.damage));
/*     */             }
/*     */             catch (CivException localCivException) {}
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 164 */         TaskMaster.syncTask(new CannonExplosionProjectile.1SyncTask(CannonExplosionProjectile.this, playerList, damageList));
/*     */ 
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 172 */     }, 0L);
/*     */   }
/*     */   
/*     */   private void setFireAt(Location loc, int radius)
/*     */   {
/* 177 */     for (int x = -radius; x < radius; x++) {
/* 178 */       for (int y = -3; y < 3; y++) {
/* 179 */         for (int z = -radius; z < radius; z++) {
/* 180 */           Block block = loc.getWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z);
/* 181 */           if (ItemManager.getId(block) == 0) {
/* 182 */             ItemManager.setTypeId(block, 51);
/* 183 */             ItemManager.setData(block, 0, true);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void launchExplodeFirework(Location loc) {
/* 191 */     FireworkEffect fe = FireworkEffect.builder().withColor(Color.ORANGE).withColor(Color.YELLOW).flicker(true).with(FireworkEffect.Type.BURST).build();
/* 192 */     TaskMaster.syncTask(new FireWorkTask(fe, loc.getWorld(), loc, 3), 0L);
/*     */   }
/*     */   
/*     */   public void setLocation(Location turretLoc)
/*     */   {
/* 197 */     this.loc = turretLoc;
/*     */   }
/*     */   
/*     */   public void setTargetLocation(Location location) {
/* 201 */     this.target = location;
/*     */   }
/*     */   
/*     */   public void setSpeed(int speed) {
/* 205 */     this.speed = speed;
/*     */   }
/*     */   
/*     */   public void setDamage(int damage) {
/* 209 */     this.damage = damage;
/*     */   }
/*     */   
/*     */   public void setSplash(int splash) {
/* 213 */     this.splash = splash;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\cache\CannonExplosionProjectile.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.avrgaming.civcraft.siege;
/*     */ 
/*     */ import com.avrgaming.civcraft.camp.CampBlock;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.DiplomacyManager;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.StructureBlock;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.structure.TownHall;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.threading.tasks.FireWorkTask;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.war.WarRegen;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import net.minecraft.server.v1_7_R2.EntityPlayer;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.FireworkEffect;
/*     */ import org.bukkit.FireworkEffect.Builder;
/*     */ import org.bukkit.FireworkEffect.Type;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ public class CannonProjectile
/*     */ {
/*     */   public Cannon cannon;
/*     */   public Location loc;
/*     */   private Location startLoc;
/*     */   public Resident whoFired;
/*  41 */   public double speed = 1.0D;
/*     */   public static double yield;
/*     */   public static double playerDamage;
/*     */   public static double maxRange;
/*     */   
/*     */   static {
/*     */     try {
/*  48 */       yield = CivSettings.getDouble(CivSettings.warConfig, "cannon.yield");
/*  49 */       playerDamage = CivSettings.getDouble(CivSettings.warConfig, "cannon.player_damage");
/*  50 */       maxRange = CivSettings.getDouble(CivSettings.warConfig, "cannon.max_range");
/*     */     } catch (InvalidConfiguration e) {
/*  52 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public CannonProjectile(Cannon cannon, Location loc, Resident whoFired) {
/*  57 */     this.cannon = cannon;
/*  58 */     this.loc = loc;
/*  59 */     this.startLoc = loc.clone();
/*  60 */     this.whoFired = whoFired;
/*     */   }
/*     */   
/*     */   private void explodeBlock(Block b) {
/*  64 */     WarRegen.saveBlock(b, "special:Cannons", false);
/*  65 */     ItemManager.setTypeId(b, 0);
/*  66 */     launchExplodeFirework(b.getLocation());
/*     */   }
/*     */   
/*  69 */   public static BlockCoord bcoord = new BlockCoord();
/*     */   
/*     */   public void onHit()
/*     */   {
/*  73 */     int radius = (int)yield;
/*  74 */     HashSet<Buildable> structuresHit = new HashSet();
/*     */     int y;
/*  76 */     for (int x = -radius; x < radius; x++) {
/*  77 */       for (int z = -radius; z < radius; z++) {
/*  78 */         for (y = -radius; y < radius; y++)
/*     */         {
/*  80 */           Block b = this.loc.getBlock().getRelative(x, y, z);
/*  81 */           if (ItemManager.getId(b) != 7)
/*     */           {
/*     */ 
/*     */ 
/*  85 */             if (this.loc.distance(b.getLocation()) <= yield) {
/*  86 */               bcoord.setFromLocation(b.getLocation());
/*  87 */               StructureBlock sb = CivGlobal.getStructureBlock(bcoord);
/*  88 */               CampBlock cb = CivGlobal.getCampBlock(bcoord);
/*     */               
/*  90 */               if ((sb == null) && (cb == null)) {
/*  91 */                 explodeBlock(b);
/*     */ 
/*     */ 
/*     */               }
/*  95 */               else if (sb != null)
/*     */               {
/*  97 */                 if (sb.isDamageable())
/*     */                 {
/*     */ 
/*     */ 
/* 101 */                   if ((sb.getOwner() instanceof TownHall)) {
/* 102 */                     TownHall th = (TownHall)sb.getOwner();
/* 103 */                     if (th.getControlPoints().containsKey(bcoord)) {}
/*     */ 
/*     */ 
/*     */ 
/*     */                   }
/* 108 */                   else if (!sb.getOwner().isDestroyed()) {
/* 109 */                     if (!structuresHit.contains(sb.getOwner()))
/*     */                     {
/* 111 */                       structuresHit.add(sb.getOwner());
/*     */                       
/* 113 */                       if ((sb.getOwner() instanceof TownHall)) {
/* 114 */                         TownHall th = (TownHall)sb.getOwner();
/*     */                         
/* 116 */                         if (th.getHitpoints() == 0) {
/* 117 */                           explodeBlock(b);
/*     */                         } else {
/* 119 */                           th.onCannonDamage(this.cannon.getDamage());
/*     */                         }
/*     */                       } else {
/* 122 */                         Player player = null;
/*     */                         try {
/* 124 */                           player = CivGlobal.getPlayer(this.whoFired);
/*     */                         }
/*     */                         catch (CivException localCivException) {}
/*     */                         
/* 128 */                         if ((!sb.getCiv().getDiplomacyManager().atWarWith(this.whoFired.getCiv())) && 
/* 129 */                           (player != null)) {
/* 130 */                           CivMessage.sendError(player, "Cannot damage structures in civilizations we're not at war with.");
/* 131 */                           return;
/*     */                         }
/*     */                         
/*     */ 
/* 135 */                         sb.getOwner().onDamage(this.cannon.getDamage(), b.getWorld(), player, sb.getCoord(), sb);
/* 136 */                         CivMessage.sendCiv(sb.getCiv(), "§eOur " + sb.getOwner().getDisplayName() + " at (" + 
/* 137 */                           sb.getOwner().getCenterLocation().getX() + "," + 
/* 138 */                           sb.getOwner().getCenterLocation().getY() + "," + 
/* 139 */                           sb.getOwner().getCenterLocation().getZ() + ")" + 
/* 140 */                           " was hit by a cannon! (" + sb.getOwner().getHitpoints() + "/" + sb.getOwner().getMaxHitPoints() + ")");
/*     */                       }
/*     */                       
/* 143 */                       CivMessage.sendCiv(this.whoFired.getCiv(), "§aWe've hit " + sb.getOwner().getTown().getName() + "'s " + 
/* 144 */                         sb.getOwner().getDisplayName() + " with a cannon!" + 
/* 145 */                         " (" + sb.getOwner().getHitpoints() + "/" + sb.getOwner().getMaxHitPoints() + ")");
/*     */                     }
/*     */                     
/*     */                   }
/* 149 */                   else if (!Cannon.cannonBlocks.containsKey(bcoord)) {
/* 150 */                     explodeBlock(b);
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 161 */     LinkedList<Entity> players = com.avrgaming.civcraft.util.EntityProximity.getNearbyEntities(null, this.loc, yield, EntityPlayer.class);
/* 162 */     for (Entity e : players) {
/* 163 */       Player player = (Player)e;
/* 164 */       player.damage(playerDamage);
/* 165 */       if (player.isDead()) {
/* 166 */         CivMessage.global("§7" + this.whoFired.getName() + " obliterated " + player.getName() + " with a cannon blast!");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void launchExplodeFirework(Location loc) {
/* 172 */     FireworkEffect fe = FireworkEffect.builder().withColor(Color.ORANGE).withColor(Color.YELLOW).flicker(true).with(FireworkEffect.Type.BURST).build();
/* 173 */     TaskMaster.syncTask(new FireWorkTask(fe, loc.getWorld(), loc, 3), 0L);
/*     */   }
/*     */   
/*     */   public boolean advance() {
/* 177 */     Vector dir = this.loc.getDirection();
/* 178 */     dir.add(new Vector(0.0D, -0.008D, 0.0D));
/* 179 */     this.loc.setDirection(dir);
/*     */     
/* 181 */     this.loc.add(dir.multiply(this.speed));
/* 182 */     this.loc.getWorld().createExplosion(this.loc, 0.0F, false);
/*     */     
/* 184 */     if (ItemManager.getId(this.loc.getBlock()) != 0) {
/* 185 */       return true;
/*     */     }
/*     */     
/* 188 */     if (this.loc.distance(this.startLoc) > maxRange) {
/* 189 */       return true;
/*     */     }
/*     */     
/* 192 */     return false;
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
/*     */   public void fire()
/*     */   {
/* 213 */     TaskMaster.syncTask(new Runnable()
/*     */     {
/*     */       CannonProjectile proj;
/*     */       
/*     */       public void run()
/*     */       {
/* 205 */         if (this.proj.advance()) {
/* 206 */           CannonProjectile.this.onHit();
/* 207 */           return;
/*     */         }
/* 209 */         TaskMaster.syncTask(this, 1L);
/*     */       }
/*     */     });
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\siege\CannonProjectile.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
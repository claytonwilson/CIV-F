/*     */ package com.avrgaming.civcraft.components;
/*     */ 
/*     */ import com.avrgaming.civcraft.cache.PlayerLocationCache;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.object.BuffManager;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.DiplomacyManager;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import java.util.HashSet;
/*     */ import net.minecraft.server.v1_7_R2.Vec3D;
/*     */ import net.minecraft.server.v1_7_R2.WorldServer;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
/*     */ import org.bukkit.entity.Entity;
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
/*     */ public abstract class ProjectileComponent
/*     */   extends Component
/*     */ {
/*     */   protected int damage;
/*     */   protected double range;
/*     */   protected double min_range;
/*     */   protected Buildable buildable;
/*     */   protected PlayerProximityComponent proximityComponent;
/*     */   private Location turretCenter;
/*  49 */   private HashSet<BlockCoord> turrets = new HashSet();
/*     */   
/*     */   public ProjectileComponent(Buildable buildable, Location turretCenter) {
/*  52 */     this.buildable = buildable;
/*  53 */     this.proximityComponent = new PlayerProximityComponent();
/*  54 */     this.proximityComponent.createComponent(buildable);
/*  55 */     this.turretCenter = turretCenter;
/*  56 */     loadSettings();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLoad() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onSave() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void createComponent(Buildable buildable, boolean async)
/*     */   {
/*  75 */     if (async) {
/*  76 */       TaskMaster.asyncTask(new RegisterComponentAsync(buildable, this, ProjectileComponent.class.getName(), true), 0L);
/*     */     } else {
/*  78 */       new RegisterComponentAsync(buildable, this, ProjectileComponent.class.getName(), true).run();
/*     */     }
/*     */   }
/*     */   
/*     */   public void destroyComponent()
/*     */   {
/*  84 */     TaskMaster.asyncTask(new RegisterComponentAsync(null, this, ProjectileComponent.class.getName(), false), 0L);
/*     */   }
/*     */   
/*     */   public void setTurretLocation(BlockCoord absCoord) {
/*  88 */     this.turrets.add(absCoord);
/*     */   }
/*     */   
/*     */   public Vector getVectorBetween(Location to, Location from) {
/*  92 */     Vector dir = new Vector();
/*     */     
/*  94 */     dir.setX(to.getX() - from.getX());
/*  95 */     dir.setY(to.getY() - from.getY());
/*  96 */     dir.setZ(to.getZ() - from.getZ());
/*     */     
/*  98 */     return dir;
/*     */   }
/*     */   
/*     */   public int getDamage() {
/* 102 */     double rate = 1.0D;
/* 103 */     rate += getBuildable().getTown().getBuffManager().getEffectiveDouble("buff_fire_bomb");
/* 104 */     return (int)(this.damage * rate);
/*     */   }
/*     */   
/*     */   public void setDamage(int damage) {
/* 108 */     this.damage = damage;
/*     */   }
/*     */   
/*     */   private Location getNearestTurret(Location playerLoc)
/*     */   {
/* 113 */     double distance = Double.MAX_VALUE;
/* 114 */     BlockCoord nearest = null;
/* 115 */     for (BlockCoord turretCoord : this.turrets) {
/* 116 */       Location turretLoc = turretCoord.getLocation();
/* 117 */       if (playerLoc.getWorld() != turretLoc.getWorld()) {
/* 118 */         return null;
/*     */       }
/*     */       
/* 121 */       double tmp = turretLoc.distance(playerLoc);
/* 122 */       if (tmp < distance) {
/* 123 */         distance = tmp;
/* 124 */         nearest = turretCoord;
/*     */       }
/*     */     }
/*     */     
/* 128 */     if (nearest == null) {
/* 129 */       return null;
/*     */     }
/* 131 */     return nearest.getLocation();
/*     */   }
/*     */   
/*     */   private boolean isWithinRange(Location residentLocation, double range)
/*     */   {
/* 136 */     if (residentLocation.getWorld() != this.turretCenter.getWorld()) {
/* 137 */       return false;
/*     */     }
/*     */     
/* 140 */     if (residentLocation.distance(this.turretCenter) <= range) {
/* 141 */       return true;
/*     */     }
/* 143 */     return false;
/*     */   }
/*     */   
/*     */   private boolean canSee(Player player, Location loc2) {
/* 147 */     Location loc1 = player.getLocation();
/* 148 */     return ((CraftWorld)loc1.getWorld()).getHandle().a(Vec3D.a(loc1.getX(), loc1.getY() + player.getEyeHeight(), loc1.getZ()), Vec3D.a(loc2.getX(), loc2.getY(), loc2.getZ())) == null;
/*     */   }
/*     */   
/*     */   protected Location adjustTurretLocation(Location turretLoc, Location playerLoc)
/*     */   {
/* 153 */     int diff = 2;
/*     */     
/* 155 */     int xdiff = 0;
/* 156 */     int zdiff = 0;
/* 157 */     if (playerLoc.getBlockX() > turretLoc.getBlockX()) {
/* 158 */       xdiff = diff;
/* 159 */     } else if (playerLoc.getBlockX() < turretLoc.getBlockX()) {
/* 160 */       xdiff = -diff;
/*     */     }
/*     */     
/* 163 */     if (playerLoc.getBlockZ() > turretLoc.getBlockZ()) {
/* 164 */       zdiff = diff;
/* 165 */     } else if (playerLoc.getBlockZ() < turretLoc.getBlockZ()) {
/* 166 */       zdiff = -diff;
/*     */     }
/*     */     
/* 169 */     return turretLoc.getBlock().getRelative(xdiff, 0, zdiff).getLocation();
/*     */   }
/*     */   
/*     */   public void process()
/*     */   {
/* 174 */     if (!this.buildable.isActive()) {
/* 175 */       return;
/*     */     }
/*     */     
/* 178 */     Player nearestPlayer = null;
/* 179 */     double nearestDistance = Double.MAX_VALUE;
/*     */     
/* 181 */     Location turretLoc = null;
/* 182 */     for (PlayerLocationCache pc : this.proximityComponent.tryGetNearbyPlayers(false)) {
/* 183 */       if ((pc != null) && (!pc.isDead()))
/*     */       {
/*     */ 
/*     */ 
/* 187 */         if (!this.buildable.getTown().isOutlaw(pc.getName())) {
/* 188 */           Resident resident = pc.getResident();
/*     */           
/* 190 */           if ((resident != null) && (resident.hasTown()))
/*     */           {
/*     */ 
/*     */ 
/* 194 */             if (!this.buildable.getCiv().getDiplomacyManager().isHostileWith(resident)) {}
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 199 */           Location playerLoc = pc.getCoord().getLocation();
/* 200 */           turretLoc = getNearestTurret(playerLoc);
/* 201 */           if (turretLoc == null)
/*     */           {
/* 203 */             return;
/*     */           }
/*     */           
/*     */           try
/*     */           {
/* 208 */             player = CivGlobal.getPlayer(pc.getName());
/*     */           } catch (CivException e) {
/*     */             Player player;
/*     */             return;
/*     */           }
/*     */           Player player;
/* 214 */           if (canSee(player, turretLoc))
/*     */           {
/*     */ 
/*     */ 
/* 218 */             if ((isWithinRange(player.getLocation(), this.range)) && 
/* 219 */               (!isWithinRange(player.getLocation(), this.min_range)))
/*     */             {
/*     */ 
/*     */ 
/* 223 */               double distance = player.getLocation().distance(this.turretCenter);
/* 224 */               if (distance < nearestDistance) {
/* 225 */                 nearestPlayer = player;
/* 226 */                 nearestDistance = distance;
/*     */               }
/*     */             } }
/*     */         } }
/*     */     }
/* 231 */     if ((nearestPlayer == null) || (turretLoc == null)) {
/* 232 */       return;
/*     */     }
/*     */     
/* 235 */     fire(turretLoc, nearestPlayer);
/*     */   }
/*     */   
/*     */   public abstract void fire(Location paramLocation, Entity paramEntity);
/*     */   
/*     */   public abstract void loadSettings();
/*     */   
/* 242 */   public Buildable getBuildable() { return this.buildable; }
/*     */   
/*     */   public void setBuildable(Buildable buildable)
/*     */   {
/* 246 */     this.buildable = buildable;
/*     */   }
/*     */   
/*     */   public Location getTurretCenter() {
/* 250 */     return this.turretCenter;
/*     */   }
/*     */   
/*     */   public void setTurretCenter(Location turretCenter) {
/* 254 */     this.turretCenter = turretCenter;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\components\ProjectileComponent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
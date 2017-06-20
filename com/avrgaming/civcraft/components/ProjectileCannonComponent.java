/*     */ package com.avrgaming.civcraft.components;
/*     */ 
/*     */ import com.avrgaming.civcraft.cache.CannonExplosionProjectile;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import java.util.Random;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Entity;
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
/*     */ public class ProjectileCannonComponent
/*     */   extends ProjectileComponent
/*     */ {
/*     */   private int speed;
/*     */   private int splash;
/*     */   private int fireRate;
/*  39 */   private int halfSecondCount = 0;
/*     */   
/*     */   public ProjectileCannonComponent(Buildable buildable, Location turretCenter) {
/*  42 */     super(buildable, turretCenter);
/*     */   }
/*     */   
/*     */   public void fire(Location turretLoc, Entity targetEntity)
/*     */   {
/*  47 */     if (this.halfSecondCount < this.fireRate) {
/*  48 */       this.halfSecondCount += 1;
/*  49 */       return;
/*     */     }
/*  51 */     this.halfSecondCount = 0;
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
/*  92 */     Runnable follow = new Runnable()
/*     */     {
/*     */       public CannonExplosionProjectile proj;
/*     */       
/*     */       public void run()
/*     */       {
/*  84 */         if (this.proj.advance()) {
/*  85 */           this.proj = null;
/*  86 */           return;
/*     */         }
/*  88 */         TaskMaster.syncTask(this, 1L);
/*     */       }
/*     */       
/*     */ 
/*  92 */     };
/*  93 */     CannonExplosionProjectile proj = new CannonExplosionProjectile(this.buildable, targetEntity.getLocation());
/*  94 */     proj.setLocation(new Location(turretLoc.getWorld(), turretLoc.getX(), turretLoc.getY(), turretLoc.getZ()));
/*  95 */     proj.setTargetLocation(targetEntity.getLocation());
/*  96 */     proj.setSpeed(this.speed);
/*  97 */     proj.setDamage(this.damage);
/*  98 */     proj.setSplash(this.splash);
/*  99 */     follow.proj = proj;
/* 100 */     TaskMaster.syncTask(follow);
/*     */     
/* 102 */     World world = turretLoc.getWorld();
/* 103 */     Random rand = new Random();
/* 104 */     for (int i = 0; i < 10; i++) {
/* 105 */       Runnable task = new Runnable()
/*     */       {
/*     */         public double x;
/*     */         public double y;
/*     */         public double z;
/*     */         public World world;
/*     */         
/*     */         public void run()
/*     */         {
/*  62 */           this.world.createExplosion(this.x, this.y, this.z, 0.0F, false);
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
/*     */         }
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
/* 105 */       };
/* 106 */       task.world = world;
/* 107 */       task.x = (turretLoc.getX() + (rand.nextInt(5) - 2.5D));
/* 108 */       task.y = (turretLoc.getY() + (rand.nextInt(5) - 2.5D));
/* 109 */       task.z = (turretLoc.getZ() + (rand.nextInt(5) - 2.5D));
/* 110 */       TaskMaster.syncTask(task, rand.nextInt(7));
/*     */     }
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
/*     */   public void loadSettings()
/*     */   {
/*     */     try
/*     */     {
/* 127 */       setDamage(CivSettings.getInteger(CivSettings.warConfig, "cannon_tower.damage").intValue());
/* 128 */       this.speed = CivSettings.getInteger(CivSettings.warConfig, "cannon_tower.speed").intValue();
/* 129 */       this.range = CivSettings.getDouble(CivSettings.warConfig, "cannon_tower.range");
/* 130 */       this.min_range = CivSettings.getDouble(CivSettings.warConfig, "cannon_tower.min_range");
/* 131 */       this.splash = CivSettings.getInteger(CivSettings.warConfig, "cannon_tower.splash").intValue();
/* 132 */       this.fireRate = CivSettings.getInteger(CivSettings.warConfig, "cannon_tower.fire_rate").intValue();
/*     */       
/*     */ 
/* 135 */       this.proximityComponent.setBuildable(this.buildable);
/* 136 */       this.proximityComponent.setCenter(new BlockCoord(getTurretCenter()));
/* 137 */       this.proximityComponent.setRadius(this.range);
/*     */     } catch (InvalidConfiguration e) {
/* 139 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public int getHalfSecondCount() {
/* 144 */     return this.halfSecondCount;
/*     */   }
/*     */   
/*     */   public void setHalfSecondCount(int halfSecondCount) {
/* 148 */     this.halfSecondCount = halfSecondCount;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\components\ProjectileCannonComponent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
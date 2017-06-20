/*    */ package com.avrgaming.civcraft.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.cache.ArrowFiredCache;
/*    */ import com.avrgaming.civcraft.cache.CivCache;
/*    */ import com.avrgaming.civcraft.config.CivSettings;
/*    */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*    */ import com.avrgaming.civcraft.object.BuffManager;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.structure.Buildable;
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
/*    */ import java.util.Map;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.entity.Arrow;
/*    */ import org.bukkit.entity.Entity;
/*    */ import org.bukkit.util.Vector;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ProjectileArrowComponent
/*    */   extends ProjectileComponent
/*    */ {
/*    */   private double power;
/*    */   
/*    */   public ProjectileArrowComponent(Buildable buildable, Location turretCenter)
/*    */   {
/* 38 */     super(buildable, turretCenter);
/*    */   }
/*    */   
/*    */ 
/*    */   public void loadSettings()
/*    */   {
/*    */     try
/*    */     {
/* 46 */       setDamage(CivSettings.getInteger(CivSettings.warConfig, "arrow_tower.damage").intValue());
/* 47 */       this.power = CivSettings.getDouble(CivSettings.warConfig, "arrow_tower.power");
/* 48 */       this.range = CivSettings.getDouble(CivSettings.warConfig, "arrow_tower.range");
/* 49 */       this.min_range = CivSettings.getDouble(CivSettings.warConfig, "arrow_tower.min_range");
/*    */       
/* 51 */       this.proximityComponent.setBuildable(this.buildable);
/* 52 */       this.proximityComponent.setCenter(new BlockCoord(getTurretCenter()));
/* 53 */       this.proximityComponent.setRadius(this.range);
/*    */     }
/*    */     catch (InvalidConfiguration e) {
/* 56 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */   
/*    */   public void fire(Location turretLoc, Entity targetEntity)
/*    */   {
/* 62 */     if (!this.buildable.isValid()) {
/* 63 */       return;
/*    */     }
/*    */     
/* 66 */     Location playerLoc = targetEntity.getLocation();
/* 67 */     playerLoc.setY(playerLoc.getY() + 1.0D);
/*    */     
/* 69 */     turretLoc = adjustTurretLocation(turretLoc, playerLoc);
/* 70 */     Vector dir = getVectorBetween(playerLoc, turretLoc).normalize();
/* 71 */     Arrow arrow = this.buildable.getCorner().getLocation().getWorld().spawnArrow(turretLoc, dir, (float)this.power, 0.0F);
/* 72 */     arrow.setVelocity(dir.multiply(this.power));
/*    */     
/* 74 */     if (this.buildable.getTown().getBuffManager().hasBuff("buff_fire_bomb")) {
/* 75 */       arrow.setFireTicks(1000);
/*    */     }
/*    */     
/* 78 */     CivCache.arrowsFired.put(arrow.getUniqueId(), new ArrowFiredCache(this, targetEntity, arrow));
/*    */   }
/*    */   
/*    */   public double getPower() {
/* 82 */     return this.power;
/*    */   }
/*    */   
/*    */   public void setPower(double power) {
/* 86 */     this.power = power;
/*    */   }
/*    */   
/*    */   public Town getTown() {
/* 90 */     return this.buildable.getTown();
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\components\ProjectileArrowComponent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
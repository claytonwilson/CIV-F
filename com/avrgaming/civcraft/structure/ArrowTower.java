/*    */ package com.avrgaming.civcraft.structure;
/*    */ 
/*    */ import com.avrgaming.civcraft.components.ProjectileArrowComponent;
/*    */ import com.avrgaming.civcraft.config.ConfigBuildableInfo;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.object.BuffManager;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ import org.bukkit.Location;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ArrowTower
/*    */   extends Structure
/*    */ {
/*    */   ProjectileArrowComponent arrowComponent;
/*    */   
/*    */   protected ArrowTower(Location center, String id, Town town)
/*    */     throws CivException
/*    */   {
/* 38 */     super(center, id, town);
/* 39 */     this.hitpoints = getMaxHitPoints();
/*    */   }
/*    */   
/*    */   protected ArrowTower(ResultSet rs) throws SQLException, CivException {
/* 43 */     super(rs);
/*    */   }
/*    */   
/*    */   public void loadSettings()
/*    */   {
/* 48 */     super.loadSettings();
/* 49 */     this.arrowComponent = new ProjectileArrowComponent(this, getCenterLocation().getLocation());
/* 50 */     this.arrowComponent.createComponent(this);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public int getDamage()
/*    */   {
/* 57 */     double rate = 1.0D;
/* 58 */     rate += getTown().getBuffManager().getEffectiveDouble("buff_fire_bomb");
/* 59 */     return (int)(this.arrowComponent.getDamage() * rate);
/*    */   }
/*    */   
/*    */   public int getMaxHitPoints()
/*    */   {
/* 64 */     double rate = 1.0D;
/* 65 */     if (getTown().getBuffManager().hasBuff("buff_chichen_itza_tower_hp")) {
/* 66 */       rate += getTown().getBuffManager().getEffectiveDouble("buff_chichen_itza_tower_hp");
/* 67 */       rate += getTown().getBuffManager().getEffectiveDouble("buff_barricade");
/*    */     }
/* 69 */     return (int)(this.info.max_hitpoints * rate);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void setDamage(int damage)
/*    */   {
/* 76 */     this.arrowComponent.setDamage(damage);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public double getPower()
/*    */   {
/* 83 */     return this.arrowComponent.getPower();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void setPower(double power)
/*    */   {
/* 90 */     this.arrowComponent.setPower(power);
/*    */   }
/*    */   
/*    */   public void setTurretLocation(BlockCoord absCoord) {
/* 94 */     this.arrowComponent.setTurretLocation(absCoord);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\ArrowTower.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
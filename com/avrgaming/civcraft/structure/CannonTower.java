/*     */ package com.avrgaming.civcraft.structure;
/*     */ 
/*     */ import com.avrgaming.civcraft.components.ProjectileCannonComponent;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigBuildableInfo;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.object.BuffManager;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import org.bukkit.Location;
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
/*     */ public class CannonTower
/*     */   extends Structure
/*     */ {
/*     */   ProjectileCannonComponent cannonComponent;
/*     */   
/*     */   protected CannonTower(Location center, String id, Town town)
/*     */     throws CivException
/*     */   {
/*  40 */     super(center, id, town);
/*  41 */     this.hitpoints = getMaxHitPoints();
/*     */   }
/*     */   
/*     */   protected CannonTower(ResultSet rs) throws SQLException, CivException {
/*  45 */     super(rs);
/*     */   }
/*     */   
/*     */   public void loadSettings()
/*     */   {
/*  50 */     super.loadSettings();
/*  51 */     this.cannonComponent = new ProjectileCannonComponent(this, getCenterLocation().getLocation());
/*  52 */     this.cannonComponent.createComponent(this);
/*     */   }
/*     */   
/*     */   public int getDamage() {
/*  56 */     double rate = 1.0D;
/*  57 */     rate += getTown().getBuffManager().getEffectiveDouble("buff_fire_bomb");
/*  58 */     return (int)(this.cannonComponent.getDamage() * rate);
/*     */   }
/*     */   
/*     */   public int getMaxHitPoints()
/*     */   {
/*  63 */     double rate = 1.0D;
/*  64 */     if (getTown().getBuffManager().hasBuff("buff_chichen_itza_tower_hp")) {
/*  65 */       rate += getTown().getBuffManager().getEffectiveDouble("buff_chichen_itza_tower_hp");
/*  66 */       rate += getTown().getBuffManager().getEffectiveDouble("buff_barricade");
/*     */     }
/*  68 */     return (int)(this.info.max_hitpoints * rate);
/*     */   }
/*     */   
/*     */   public void setDamage(int damage) {
/*  72 */     this.cannonComponent.setDamage(damage);
/*     */   }
/*     */   
/*     */   public void setTurretLocation(BlockCoord absCoord)
/*     */   {
/*  77 */     this.cannonComponent.setTurretLocation(absCoord);
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
/*     */   public void onCheck()
/*     */     throws CivException
/*     */   {
/*     */     try
/*     */     {
/*  96 */       double build_distance = CivSettings.getDouble(CivSettings.warConfig, "cannon_tower.build_distance");
/*     */       Iterator localIterator2;
/*  98 */       for (Iterator localIterator1 = getTown().getCiv().getTowns().iterator(); localIterator1.hasNext(); 
/*  99 */           localIterator2.hasNext())
/*     */       {
/*  98 */         Town town = (Town)localIterator1.next();
/*  99 */         localIterator2 = town.getStructures().iterator(); continue;Structure struct = (Structure)localIterator2.next();
/* 100 */         if ((struct instanceof CannonTower)) {
/* 101 */           BlockCoord center = struct.getCenterLocation();
/* 102 */           double distance = center.distance(getCenterLocation());
/* 103 */           if (distance <= build_distance) {
/* 104 */             throw new CivException("Cannot build here. Too close to another Cannon Tower at (" + center.getX() + "," + center.getY() + "," + center.getZ() + ")");
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (InvalidConfiguration e) {
/* 110 */       e.printStackTrace();
/* 111 */       throw new CivException(e.getMessage());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\CannonTower.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
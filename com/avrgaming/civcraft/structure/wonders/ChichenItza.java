/*    */ package com.avrgaming.civcraft.structure.wonders;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.object.BuffManager;
/*    */ import com.avrgaming.civcraft.object.ControlPoint;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.structure.TownHall;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ import java.util.HashMap;
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
/*    */ public class ChichenItza
/*    */   extends Wonder
/*    */ {
/*    */   public ChichenItza(Location center, String id, Town town)
/*    */     throws CivException
/*    */   {
/* 35 */     super(center, id, town);
/*    */   }
/*    */   
/*    */   public ChichenItza(ResultSet rs) throws SQLException, CivException {
/* 39 */     super(rs);
/*    */   }
/*    */   
/*    */   protected void removeBuffs()
/*    */   {
/* 44 */     removeBuffFromCiv(getCiv(), "buff_chichen_itza_tower_hp");
/* 45 */     removeBuffFromCiv(getCiv(), "buff_chichen_itza_regen_rate");
/* 46 */     removeBuffFromTown(getTown(), "buff_chichen_itza_cp_bonus_hp");
/*    */     
/* 48 */     for (ControlPoint cp : getTown().getTownHall().getControlPoints().values())
/*    */     {
/* 50 */       cp.setMaxHitpoints(cp.getMaxHitpoints() - (int)getTown().getBuffManager().getEffectiveDouble("buff_chichen_itza_cp_bonus_hp"));
/*    */     }
/*    */   }
/*    */   
/*    */   protected void addBuffs()
/*    */   {
/* 56 */     addBuffToCiv(getCiv(), "buff_chichen_itza_tower_hp");
/* 57 */     addBuffToCiv(getCiv(), "buff_chichen_itza_regen_rate");
/* 58 */     addBuffToTown(getTown(), "buff_chichen_itza_cp_bonus_hp");
/*    */     
/* 60 */     for (ControlPoint cp : getTown().getTownHall().getControlPoints().values())
/*    */     {
/* 62 */       cp.setMaxHitpoints(cp.getMaxHitpoints() + (int)getTown().getBuffManager().getEffectiveDouble("buff_chichen_itza_cp_bonus_hp"));
/*    */     }
/*    */   }
/*    */   
/*    */   public void onLoad()
/*    */   {
/* 68 */     if (isActive()) {
/* 69 */       addBuffs();
/*    */     }
/*    */   }
/*    */   
/*    */   public void onComplete()
/*    */   {
/* 75 */     addBuffs();
/*    */   }
/*    */   
/*    */   public void onDestroy()
/*    */   {
/* 80 */     super.onDestroy();
/* 81 */     removeBuffs();
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\wonders\ChichenItza.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
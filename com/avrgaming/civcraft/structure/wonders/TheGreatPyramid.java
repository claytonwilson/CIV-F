/*    */ package com.avrgaming.civcraft.structure.wonders;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.TreeMap;
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
/*    */ 
/*    */ public class TheGreatPyramid
/*    */   extends Wonder
/*    */ {
/*    */   public TheGreatPyramid(Location center, String id, Town town)
/*    */     throws CivException
/*    */   {
/* 37 */     super(center, id, town);
/*    */   }
/*    */   
/*    */   public TheGreatPyramid(ResultSet rs) throws SQLException, CivException {
/* 41 */     super(rs);
/*    */   }
/*    */   
/*    */   private Civilization calculateNearestCivilization() {
/* 45 */     TreeMap<Double, Civilization> civMaps = CivGlobal.findNearestCivilizations(getTown());
/* 46 */     Civilization nearestCiv = null;
/* 47 */     if (civMaps.size() > 0) {
/* 48 */       nearestCiv = (Civilization)civMaps.firstEntry().getValue();
/*    */     }
/* 50 */     return nearestCiv;
/*    */   }
/*    */   
/*    */   protected void addBuffs()
/*    */   {
/* 55 */     addBuffToTown(getTown(), "buff_pyramid_cottage_consume");
/* 56 */     addBuffToTown(getTown(), "buff_pyramid_cottage_bonus");
/* 57 */     addBuffToCiv(getCiv(), "buff_pyramid_culture");
/* 58 */     addBuffToTown(getTown(), "buff_pyramid_leech");
/* 59 */     Civilization nearest = calculateNearestCivilization();
/* 60 */     if (nearest != null) {
/* 61 */       addBuffToCiv(nearest, "debuff_pyramid_leech");
/*    */     }
/*    */   }
/*    */   
/*    */   protected void removeBuffs()
/*    */   {
/* 67 */     removeBuffFromTown(getTown(), "buff_pyramid_cottage_consume");
/* 68 */     removeBuffFromTown(getTown(), "buff_pyramid_cottage_bonus");
/* 69 */     removeBuffFromCiv(getCiv(), "buff_pyramid_culture");
/* 70 */     removeBuffFromTown(getTown(), "buff_pyramid_leech");
/* 71 */     Civilization nearest = calculateNearestCivilization();
/* 72 */     if (nearest != null) {
/* 73 */       removeBuffFromCiv(nearest, "debuff_pyramid_leech");
/*    */     }
/*    */   }
/*    */   
/*    */   public void onLoad()
/*    */   {
/* 79 */     if (isActive()) {
/* 80 */       addBuffs();
/*    */     }
/*    */   }
/*    */   
/*    */   public void onDestroy()
/*    */   {
/* 86 */     super.onDestroy();
/* 87 */     removeBuffs();
/*    */   }
/*    */   
/*    */   public void onComplete()
/*    */   {
/* 92 */     addBuffs();
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\wonders\TheGreatPyramid.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
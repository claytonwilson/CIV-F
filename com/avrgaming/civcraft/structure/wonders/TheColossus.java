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
/*    */ public class TheColossus
/*    */   extends Wonder
/*    */ {
/*    */   private TreeMap<Double, Civilization> nearestCivs;
/*    */   
/*    */   public TheColossus(Location center, String id, Town town)
/*    */     throws CivException
/*    */   {
/* 37 */     super(center, id, town);
/*    */   }
/*    */   
/*    */   public TheColossus(ResultSet rs) throws SQLException, CivException {
/* 41 */     super(rs);
/*    */   }
/*    */   
/*    */ 
/*    */   private void calculateNearestCivilizations()
/*    */   {
/* 47 */     this.nearestCivs = CivGlobal.findNearestCivilizations(getTown());
/*    */   }
/*    */   
/*    */   public void onLoad()
/*    */   {
/* 52 */     if (isActive()) {
/* 53 */       addBuffs();
/*    */     }
/*    */   }
/*    */   
/*    */   public void onComplete()
/*    */   {
/* 59 */     addBuffs();
/*    */   }
/*    */   
/*    */   public void onDestroy()
/*    */   {
/* 64 */     super.onDestroy();
/* 65 */     removeBuffs();
/*    */   }
/*    */   
/*    */   protected void removeBuffs()
/*    */   {
/* 70 */     int i = 0;
/* 71 */     for (Map.Entry<Double, Civilization> entry : this.nearestCivs.entrySet()) {
/* 72 */       removeBuffFromCiv((Civilization)entry.getValue(), "debuff_colossus_leech_upkeep");
/* 73 */       i++;
/*    */       
/* 75 */       if (i > 3) {
/*    */         break;
/*    */       }
/*    */     }
/*    */     
/* 80 */     removeBuffFromTown(getTown(), "buff_colossus_reduce_upkeep");
/* 81 */     removeBuffFromTown(getTown(), "buff_colossus_coins_from_culture");
/*    */   }
/*    */   
/*    */   protected void addBuffs()
/*    */   {
/* 86 */     calculateNearestCivilizations();
/*    */     
/* 88 */     int i = 0;
/* 89 */     for (Map.Entry<Double, Civilization> entry : this.nearestCivs.entrySet()) {
/* 90 */       addBuffToCiv((Civilization)entry.getValue(), "debuff_colossus_leech_upkeep");
/* 91 */       i++;
/*    */       
/* 93 */       if (i > 3) {
/*    */         break;
/*    */       }
/*    */     }
/*    */     
/* 98 */     addBuffToTown(getTown(), "buff_colossus_reduce_upkeep");
/* 99 */     addBuffToTown(getTown(), "buff_colossus_coins_from_culture");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\wonders\TheColossus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
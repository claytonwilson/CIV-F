/*    */ package com.avrgaming.civcraft.randomevents.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.randomevents.RandomEventComponent;
/*    */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*    */ import java.text.DecimalFormat;
/*    */ 
/*    */ public class HammerRate extends RandomEventComponent
/*    */ {
/*    */   public void process()
/*    */   {
/* 13 */     double rate = getDouble("value");
/* 14 */     int duration = Integer.valueOf(getString("duration")).intValue();
/*    */     
/* 16 */     com.avrgaming.civcraft.main.CivGlobal.getSessionDB().add(getKey(getParentTown()), rate + ":" + duration, getParentTown().getCiv().getId(), getParentTown().getId(), 0);
/* 17 */     DecimalFormat df = new DecimalFormat();
/*    */     
/* 19 */     if (rate > 1.0D) {
/* 20 */       sendMessage("Our production rate has increased by " + df.format((rate - 1.0D) * 100.0D) + "% due to an unforseen event!");
/*    */     } else {
/* 22 */       sendMessage("Our production rate has decreased by " + df.format((1.0D - rate) * 100.0D) + "% due to an unforseen event!");
/*    */     }
/*    */   }
/*    */   
/*    */   public static String getKey(Town town) {
/* 27 */     return "randomevent:hammerrate" + town.getId();
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\randomevents\components\HammerRate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
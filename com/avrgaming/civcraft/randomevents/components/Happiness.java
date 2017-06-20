/*    */ package com.avrgaming.civcraft.randomevents.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*    */ 
/*    */ public class Happiness extends com.avrgaming.civcraft.randomevents.RandomEventComponent
/*    */ {
/*    */   public void process()
/*    */   {
/* 11 */     int happiness = Integer.valueOf(getString("value")).intValue();
/* 12 */     int duration = Integer.valueOf(getString("duration")).intValue();
/*    */     
/* 14 */     com.avrgaming.civcraft.main.CivGlobal.getSessionDB().add(getKey(getParentTown()), happiness + ":" + duration, getParentTown().getCiv().getId(), getParentTown().getId(), 0);
/* 15 */     sendMessage("We're now enjoying a happiness bonus  of " + happiness + " happiness for " + duration + " hours!");
/*    */   }
/*    */   
/*    */   public static String getKey(Town town) {
/* 19 */     return "randomevent:happiness:" + town.getId();
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\randomevents\components\Happiness.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
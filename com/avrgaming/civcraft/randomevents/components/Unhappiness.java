/*    */ package com.avrgaming.civcraft.randomevents.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*    */ 
/*    */ public class Unhappiness extends com.avrgaming.civcraft.randomevents.RandomEventComponent
/*    */ {
/*    */   public static String getKey(Town town)
/*    */   {
/* 10 */     return "randomevent:unhappiness:" + town.getId();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void process()
/*    */   {
/* 17 */     int unhappiness = Integer.valueOf(getString("value")).intValue();
/* 18 */     int duration = Integer.valueOf(getString("duration")).intValue();
/*    */     
/* 20 */     com.avrgaming.civcraft.main.CivGlobal.getSessionDB().add(getKey(getParentTown()), unhappiness + ":" + duration, getParentTown().getCiv().getId(), getParentTown().getId(), 0);
/* 21 */     sendMessage("Blast! We're now suffering a happiness penalty of " + unhappiness + " unhappiness for " + duration + " hours!");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\randomevents\components\Unhappiness.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
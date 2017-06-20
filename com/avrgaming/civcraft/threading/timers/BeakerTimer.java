/*    */ package com.avrgaming.civcraft.threading.timers;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.structure.TownHall;
/*    */ import com.avrgaming.civcraft.threading.CivAsyncTask;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BeakerTimer
/*    */   extends CivAsyncTask
/*    */ {
/*    */   public static final int BEAKER_PERIOD = 60;
/*    */   
/*    */   public BeakerTimer(int periodInSeconds) {}
/*    */   
/*    */   public void run()
/*    */   {
/* 42 */     for (Civilization civ : )
/*    */     {
/* 44 */       if (civ.getCapitolName() == null) {
/* 45 */         CivMessage.sendCiv(civ, "ERROR: your capitol name is not set right! No research is progressing. Contact an admin.");
/*    */       }
/*    */       else
/*    */       {
/* 49 */         Town town = CivGlobal.getTown(civ.getCapitolName());
/* 50 */         if (town == null) {
/* 51 */           CivMessage.sendCiv(civ, "ERROR: Couldn't find your capitol town named " + civ.getCapitolName() + "! No research is progressing. Contact an admin.");
/*    */         }
/*    */         else
/*    */         {
/* 55 */           TownHall townhall = town.getTownHall();
/* 56 */           if (townhall == null) {
/* 57 */             CivMessage.sendCiv(civ, "Your captial doesn't have a town hall! You are not generating any beakers.");
/*    */           }
/*    */           
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           try
/*    */           {
/* 66 */             if (civ.getResearchTech() != null) {
/* 67 */               civ.addBeakers(civ.getBeakers() / 60.0D);
/*    */             } else {
/* 69 */               civ.processUnusedBeakers();
/*    */             }
/*    */           } catch (Exception e) {
/* 72 */             e.printStackTrace();
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\timers\BeakerTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
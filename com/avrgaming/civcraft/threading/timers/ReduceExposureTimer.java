/*    */ package com.avrgaming.civcraft.threading.timers;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import java.util.LinkedList;
/*    */ 
/*    */ 
/*    */ public class ReduceExposureTimer
/*    */   implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/* 14 */     LinkedList<String> playersToReduce = new LinkedList();
/*    */     
/* 16 */     for (Resident resident : CivGlobal.getResidents()) {
/* 17 */       if ((!resident.isPerformingMission()) && (resident.getSpyExposure() > 0.0D)) {
/* 18 */         playersToReduce.add(resident.getName());
/*    */       }
/*    */     }
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
/*    */ 
/* 46 */     TaskMaster.syncTask(new Runnable()
/*    */     {
/*    */       public LinkedList<String> playersToReduce;
/*    */       
/*    */       public void run()
/*    */       {
/* 33 */         for (String name : this.playersToReduce) {
/* 34 */           Resident resident = CivGlobal.getResident(name);
/* 35 */           if (resident.getSpyExposure() <= 5.0D) {
/* 36 */             resident.setSpyExposure(0.0D);
/*    */           } else {
/* 38 */             resident.setSpyExposure(resident.getSpyExposure() - 5.0D);
/*    */           }
/*    */         }
/*    */       }
/*    */     });
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\timers\ReduceExposureTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
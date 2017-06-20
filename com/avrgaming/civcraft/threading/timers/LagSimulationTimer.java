/*    */ package com.avrgaming.civcraft.threading.timers;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LagSimulationTimer
/*    */   implements Runnable
/*    */ {
/*    */   int targetTPS;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public LagSimulationTimer(int targetTPS)
/*    */   {
/* 26 */     this.targetTPS = targetTPS;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void run()
/*    */   {
/* 34 */     int waitTime = 20 - this.targetTPS;
/*    */     
/* 36 */     if (waitTime < 0) {
/* 37 */       return;
/*    */     }
/*    */     
/* 40 */     double secondsPerTick = 0.05D;
/* 41 */     long millis = (waitTime * secondsPerTick * 1000.0D);
/* 42 */     synchronized (this) {
/*    */       try {
/* 44 */         wait(millis);
/*    */       }
/*    */       catch (InterruptedException localInterruptedException) {}
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\timers\LagSimulationTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
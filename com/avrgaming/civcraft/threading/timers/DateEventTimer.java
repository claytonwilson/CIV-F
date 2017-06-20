/*    */ package com.avrgaming.civcraft.threading.timers;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import java.util.Date;
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
/*    */ public class DateEventTimer
/*    */   implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/* 35 */     Date now = new Date();
/*    */     
/*    */ 
/* 38 */     now.after(CivGlobal.getTodaysSpawnRegenDate());
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\timers\DateEventTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
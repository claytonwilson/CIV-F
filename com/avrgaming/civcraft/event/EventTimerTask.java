/*    */ package com.avrgaming.civcraft.event;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import java.util.Calendar;
/*    */ import java.util.HashMap;
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
/*    */ public class EventTimerTask
/*    */   implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/* 32 */     Calendar cal = EventTimer.getCalendarInServerTimeZone();
/*    */     
/* 34 */     for (EventTimer timer : EventTimer.timers.values())
/*    */     {
/* 36 */       if (cal.after(timer.getNext())) {
/* 37 */         timer.setLast(cal);
/*    */         
/*    */         try
/*    */         {
/* 41 */           next = timer.getEventFunction().getNextDate();
/*    */         } catch (InvalidConfiguration e) { Calendar next;
/* 43 */           e.printStackTrace();
/* 44 */           continue;
/*    */         }
/*    */         Calendar next;
/* 47 */         if (next == null) {
/* 48 */           CivLog.warning("WARNING timer:" + timer.getName() + " did not return a next time.");
/*    */         }
/*    */         else
/*    */         {
/* 52 */           timer.setNext(next);
/* 53 */           timer.save();
/*    */           
/* 55 */           timer.getEventFunction().process();
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\event\EventTimerTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
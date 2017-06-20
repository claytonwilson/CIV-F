/*    */ package com.avrgaming.civcraft.event;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.CivSettings;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import com.avrgaming.civcraft.threading.tasks.CultureProcessAsyncTask;
/*    */ import com.avrgaming.civcraft.threading.timers.DailyTimer;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Calendar;
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
/*    */ public class DailyEvent
/*    */   implements EventInterface
/*    */ {
/* 34 */   public static Boolean dailyTimerFinished = Boolean.valueOf(true);
/*    */   
/* 36 */   public static int dayExecuted = 0;
/*    */   
/*    */ 
/*    */   public void process()
/*    */   {
/* 41 */     CivLog.info("TimerEvent: Daily -------------------------------------");
/*    */     
/* 43 */     while (!CultureProcessAsyncTask.cultureProcessedSinceStartup) {
/* 44 */       CivLog.info("DailyTimer: Waiting for culture to finish processing.");
/*    */       try {
/* 46 */         Thread.sleep(10000L);
/*    */       } catch (InterruptedException e) {
/* 48 */         e.printStackTrace();
/* 49 */         break;
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 54 */     if (dailyTimerFinished.booleanValue()) {
/* 55 */       CivLog.info("Daily timer was finished, starting a new timer.");
/* 56 */       dailyTimerFinished = Boolean.valueOf(false);
/* 57 */       if (dayExecuted == 0) {
/* 58 */         Calendar cal = Calendar.getInstance();
/* 59 */         dayExecuted = cal.get(5);
/* 60 */         TaskMaster.syncTask(new DailyTimer(), 0L);
/*    */       }
/*    */       else {
/*    */         try {
/* 64 */           throw new CivException("TRIED TO EXECUTE DAILY EVENT TWICE");
/*    */         } catch (CivException e) {
/* 66 */           e.printStackTrace();
/*    */         }
/*    */       }
/*    */     } else {
/* 70 */       CivLog.info("Daily timer was NOT finished. skipped.");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public Calendar getNextDate()
/*    */     throws InvalidConfiguration
/*    */   {
/* 78 */     Calendar cal = EventTimer.getCalendarInServerTimeZone();
/*    */     
/* 80 */     int daily_upkeep_hour = CivSettings.getInteger(CivSettings.civConfig, "global.daily_upkeep_hour").intValue();
/* 81 */     cal.set(12, 0);
/* 82 */     cal.set(13, 0);
/* 83 */     cal.set(11, daily_upkeep_hour);
/*    */     
/* 85 */     Calendar now = Calendar.getInstance();
/* 86 */     if (now.after(cal)) {
/* 87 */       cal.add(5, 1);
/* 88 */       cal.set(11, daily_upkeep_hour);
/* 89 */       cal.set(12, 0);
/* 90 */       cal.set(13, 0);
/*    */     }
/* 92 */     SimpleDateFormat sdf = new SimpleDateFormat("M/dd h:mm:ss a z");
/* 93 */     CivLog.info("Setting Next Daily Event:" + sdf.format(cal.getTime()));
/*    */     
/* 95 */     return cal;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\event\DailyEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
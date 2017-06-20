/*    */ package com.avrgaming.civcraft.event;
/*    */ 
/*    */ import com.avrgaming.civcraft.camp.CampHourlyTick;
/*    */ import com.avrgaming.civcraft.config.CivSettings;
/*    */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import com.avrgaming.civcraft.threading.tasks.CultureProcessAsyncTask;
/*    */ import com.avrgaming.civcraft.threading.timers.EffectEventTimer;
/*    */ import com.avrgaming.civcraft.threading.timers.SyncTradeTimer;
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
/*    */ public class HourlyTickEvent
/*    */   implements EventInterface
/*    */ {
/*    */   public void process()
/*    */   {
/* 37 */     CivLog.info("TimerEvent: Hourly -------------------------------------");
/* 38 */     TaskMaster.asyncTask("cultureProcess", new CultureProcessAsyncTask(), 0L);
/* 39 */     TaskMaster.asyncTask("EffectEventTimer", new EffectEventTimer(), 0L);
/* 40 */     TaskMaster.syncTask(new SyncTradeTimer(), 0L);
/* 41 */     TaskMaster.syncTask(new CampHourlyTick(), 0L);
/* 42 */     CivLog.info("TimerEvent: Hourly Finished -----------------------------");
/*    */   }
/*    */   
/*    */   public Calendar getNextDate() throws InvalidConfiguration
/*    */   {
/* 47 */     SimpleDateFormat sdf = new SimpleDateFormat("M/dd h:mm:ss a z");
/* 48 */     Calendar cal = EventTimer.getCalendarInServerTimeZone();
/*    */     
/* 50 */     int hourly_peroid = CivSettings.getInteger(CivSettings.civConfig, "global.hourly_tick").intValue();
/* 51 */     cal.set(13, 0);
/* 52 */     cal.set(12, 0);
/* 53 */     cal.add(13, hourly_peroid);
/* 54 */     sdf.setTimeZone(cal.getTimeZone());
/* 55 */     return cal;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\event\HourlyTickEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
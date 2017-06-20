/*    */ package com.avrgaming.civcraft.event;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.CivSettings;
/*    */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import com.avrgaming.civcraft.threading.timers.WarEndCheckTask;
/*    */ import com.avrgaming.civcraft.util.TimeTools;
/*    */ import com.avrgaming.civcraft.war.War;
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
/*    */ public class WarEvent
/*    */   implements EventInterface
/*    */ {
/*    */   public void process()
/*    */   {
/* 35 */     CivLog.info("TimerEvent: WarEvent -------------------------------------");
/*    */     try
/*    */     {
/* 38 */       War.setWarTime(true);
/*    */     } catch (Exception e) {
/* 40 */       CivLog.error("WarStartException:" + e.getMessage());
/* 41 */       e.printStackTrace();
/*    */     }
/*    */     
/*    */ 
/* 45 */     TaskMaster.syncTask(new WarEndCheckTask(), TimeTools.toTicks(1L));
/*    */   }
/*    */   
/*    */   public Calendar getNextDate() throws InvalidConfiguration
/*    */   {
/* 50 */     Calendar cal = EventTimer.getCalendarInServerTimeZone();
/*    */     
/* 52 */     int dayOfWeek = CivSettings.getInteger(CivSettings.warConfig, "war.time_day").intValue();
/* 53 */     int hourOfWar = CivSettings.getInteger(CivSettings.warConfig, "war.time_hour").intValue();
/*    */     
/* 55 */     cal.set(7, dayOfWeek);
/* 56 */     cal.set(11, hourOfWar);
/* 57 */     cal.set(12, 0);
/* 58 */     cal.set(13, 0);
/*    */     
/* 60 */     Calendar now = Calendar.getInstance();
/* 61 */     if (now.after(cal)) {
/* 62 */       cal.add(4, 1);
/* 63 */       cal.set(7, dayOfWeek);
/* 64 */       cal.set(11, hourOfWar);
/* 65 */       cal.set(12, 0);
/* 66 */       cal.set(13, 0);
/*    */     }
/*    */     
/* 69 */     return cal;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\event\WarEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
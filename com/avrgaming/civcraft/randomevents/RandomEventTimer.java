/*    */ package com.avrgaming.civcraft.randomevents;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.CivSettings;
/*    */ import com.avrgaming.civcraft.event.EventInterface;
/*    */ import com.avrgaming.civcraft.event.EventTimer;
/*    */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import java.util.Calendar;
/*    */ import java.util.Date;
/*    */ import java.util.HashMap;
/*    */ import java.util.Random;
/*    */ 
/*    */ public class RandomEventTimer
/*    */   implements EventInterface
/*    */ {
/*    */   public void process()
/*    */   {
/* 19 */     for (Town town : ) {
/* 20 */       if (town.getActiveEvent() == null)
/*    */       {
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
/* 33 */         ConfigRandomEvent selectedEvent = null;
/* 34 */         Random rand = new Random();
/*    */         
/* 36 */         for (ConfigRandomEvent event : CivSettings.randomEvents.values()) {
/* 37 */           int r = rand.nextInt(1000);
/*    */           
/* 39 */           if (r <= event.chance) {
/* 40 */             if (selectedEvent == null) {
/* 41 */               selectedEvent = event;
/*    */ 
/*    */ 
/*    */             }
/* 45 */             else if (selectedEvent.chance == event.chance)
/*    */             {
/* 47 */               if (rand.nextInt(1) == 0)
/*    */               {
/* 49 */                 selectedEvent = event;
/*    */               }
/*    */               
/*    */ 
/*    */             }
/* 54 */             else if (event.chance < selectedEvent.chance) {
/* 55 */               selectedEvent = event;
/*    */             }
/*    */           }
/*    */         }
/*    */         
/*    */ 
/*    */ 
/* 62 */         if (selectedEvent != null)
/*    */         {
/*    */ 
/*    */ 
/*    */ 
/* 67 */           RandomEvent randEvent = new RandomEvent(selectedEvent);
/* 68 */           randEvent.start(town);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public Calendar getNextDate() throws InvalidConfiguration {
/* 75 */     Calendar now = EventTimer.getCalendarInServerTimeZone();
/*    */     
/* 77 */     Random rand = new Random();
/* 78 */     int hours = rand.nextInt(12) + 12;
/* 79 */     now.setTimeInMillis(now.getTime().getTime() + hours * 3600000);
/* 80 */     return now;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\randomevents\RandomEventTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
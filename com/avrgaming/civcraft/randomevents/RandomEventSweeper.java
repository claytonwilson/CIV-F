/*    */ package com.avrgaming.civcraft.randomevents;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.LinkedList;
/*    */ 
/*    */ public class RandomEventSweeper implements Runnable
/*    */ {
/*  8 */   private static LinkedList<RandomEvent> events = new LinkedList();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int MILLISECONDS_PER_HOUR = 3600000;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void run()
/*    */   {
/* 25 */     LinkedList<RandomEvent> removed = new LinkedList();
/* 26 */     for (RandomEvent event : events) {
/* 27 */       boolean allPass = false;
/* 28 */       if (event.requirements.size() > 0) {
/* 29 */         allPass = true;
/* 30 */         for (RandomEventComponent comp : event.requirements.values()) {
/* 31 */           if (!comp.onCheck()) {
/* 32 */             allPass = false;
/*    */           }
/*    */         }
/*    */       }
/*    */       
/* 37 */       if (allPass)
/*    */       {
/* 39 */         for (RandomEventComponent comp : event.success.values()) {
/* 40 */           comp.process();
/*    */         }
/* 42 */         event.cleanup();
/* 43 */         removed.add(event);
/*    */       }
/*    */       else {
/* 46 */         java.util.Date now = new java.util.Date();
/*    */         
/* 48 */         long expireTime = event.getStartDate().getTime() + event.getLength() * 3600000;
/* 49 */         if (now.getTime() > expireTime)
/*    */         {
/* 51 */           for (RandomEventComponent comp : event.failure.values()) {
/* 52 */             comp.process();
/*    */           }
/* 54 */           event.cleanup();
/* 55 */           removed.add(event);
/*    */         }
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 61 */     events.removeAll(removed);
/*    */   }
/*    */   
/*    */   public static void register(RandomEvent randomEvent)
/*    */   {
/* 66 */     events.add(randomEvent);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\randomevents\RandomEventSweeper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
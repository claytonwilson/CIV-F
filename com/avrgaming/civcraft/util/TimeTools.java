/*    */ package com.avrgaming.civcraft.util;
/*    */ 
/*    */ import java.util.Calendar;
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
/*    */ public class TimeTools
/*    */ {
/*    */   public static long toTicks(long seconds)
/*    */   {
/* 27 */     return 20L * seconds;
/*    */   }
/*    */   
/*    */   public static long getTicksUnitl(Date next) {
/* 31 */     Calendar c = Calendar.getInstance();
/* 32 */     Date now = c.getTime();
/*    */     
/* 34 */     long seconds = Math.abs((now.getTime() - next.getTime()) / 1000L);
/*    */     
/* 36 */     return seconds * 20L;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\TimeTools.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
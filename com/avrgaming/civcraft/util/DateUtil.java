/*    */ package com.avrgaming.civcraft.util;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import java.util.Date;
/*    */ 
/*    */ public class DateUtil
/*    */ {
/*    */   private static final int MILLISECONDS_PER_SECOND = 1000;
/*    */   private static final int SECONDS_PER_MIN = 60;
/*    */   private static final int MINS_PER_HOUR = 60;
/*    */   private static final int HOURS_PER_DAY = 24;
/*    */   
/*    */   public static boolean isAfterSeconds(Date d, int value)
/*    */   {
/* 15 */     return isAfter(d, value, 1000);
/*    */   }
/*    */   
/*    */   public static boolean isAfterMins(Date d, int value) {
/* 19 */     return isAfter(d, value, 60000);
/*    */   }
/*    */   
/*    */   public static boolean isAfterHours(Date d, int value) {
/* 23 */     return isAfter(d, value, 3600000);
/*    */   }
/*    */   
/*    */   public static boolean isAfterDays(Date d, int value) {
/* 27 */     return isAfter(d, value, 86400000);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private static boolean isAfter(Date d, int value, int m)
/*    */   {
/* 34 */     Date now = new Date();
/*    */     
/* 36 */     if (CivGlobal.debugDateBypass) {
/* 37 */       return true;
/*    */     }
/*    */     
/* 40 */     if (d == null) {
/* 41 */       return true;
/*    */     }
/*    */     
/* 44 */     if (now.getTime() > d.getTime() + value * m) {
/* 45 */       return true;
/*    */     }
/*    */     
/* 48 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\DateUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
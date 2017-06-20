/*    */ package com.avrgaming.civcraft.util;
/*    */ 
/*    */ import java.text.DecimalFormat;
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
/*    */ public class DecimalHelper
/*    */ {
/*    */   public static String formatPercentage(double d)
/*    */   {
/* 27 */     DecimalFormat df = new DecimalFormat();
/* 28 */     return df.format(d * 100.0D) + "%";
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\DecimalHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
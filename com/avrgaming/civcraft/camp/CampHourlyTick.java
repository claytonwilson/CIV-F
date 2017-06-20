/*    */ package com.avrgaming.civcraft.camp;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
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
/*    */ public class CampHourlyTick
/*    */   implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/* 27 */     for (Camp camp : ) {
/*    */       try {
/* 29 */         camp.processFirepoints();
/* 30 */         if (camp.isLonghouseEnabled()) {
/* 31 */           camp.processLonghouse();
/*    */         }
/*    */       } catch (Exception e) {
/* 34 */         e.printStackTrace();
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\camp\CampHourlyTick.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
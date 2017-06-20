/*    */ package com.avrgaming.civcraft.event;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
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
/*    */ public class TestEvent
/*    */   implements EventInterface
/*    */ {
/*    */   public void process()
/*    */   {
/* 30 */     CivMessage.global("This is a test event firing!");
/*    */   }
/*    */   
/*    */   public Calendar getNextDate() throws InvalidConfiguration
/*    */   {
/* 35 */     Calendar cal = Calendar.getInstance();
/* 36 */     cal.add(13, 60);
/* 37 */     return cal;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\event\TestEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
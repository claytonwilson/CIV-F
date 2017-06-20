/*    */ package com.avrgaming.civcraft.randomevents.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.randomevents.RandomEventComponent;
/*    */ 
/*    */ public class MessageTown
/*    */   extends RandomEventComponent
/*    */ {
/*    */   public void process()
/*    */   {
/* 10 */     String message = getString("message");
/* 11 */     sendMessage(message);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\randomevents\components\MessageTown.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
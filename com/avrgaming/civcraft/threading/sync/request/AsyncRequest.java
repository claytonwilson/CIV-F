/*    */ package com.avrgaming.civcraft.threading.sync.request;
/*    */ 
/*    */ import java.util.concurrent.locks.Condition;
/*    */ import java.util.concurrent.locks.ReentrantLock;
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
/*    */ public class AsyncRequest
/*    */ {
/*    */   public Condition condition;
/*    */   
/*    */   public AsyncRequest(ReentrantLock lock)
/*    */   {
/* 27 */     this.condition = lock.newCondition();
/*    */   }
/*    */   
/*    */ 
/* 31 */   public Boolean finished = Boolean.valueOf(false);
/* 32 */   public Object result = null;
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\sync\request\AsyncRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
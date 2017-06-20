/*    */ package com.avrgaming.civcraft.threading.sync.request;
/*    */ 
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
/*    */ public class LoadChunkRequest
/*    */   extends AsyncRequest
/*    */ {
/*    */   public String worldName;
/*    */   public int x;
/*    */   public int z;
/*    */   
/*    */   public LoadChunkRequest(ReentrantLock lock)
/*    */   {
/* 26 */     super(lock);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\sync\request\LoadChunkRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
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
/*    */ public class GetChestRequest
/*    */   extends AsyncRequest
/*    */ {
/*    */   public int block_x;
/*    */   public int block_y;
/*    */   public int block_z;
/*    */   public String worldName;
/*    */   
/*    */   public GetChestRequest(ReentrantLock lock)
/*    */   {
/* 26 */     super(lock);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\sync\request\GetChestRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
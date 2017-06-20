/*    */ package com.avrgaming.civcraft.threading.sync.request;
/*    */ 
/*    */ import com.avrgaming.civcraft.structure.farm.FarmChunk;
/*    */ import com.avrgaming.civcraft.structure.farm.GrowBlock;
/*    */ import java.util.LinkedList;
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
/*    */ public class GrowRequest
/*    */   extends AsyncRequest
/*    */ {
/*    */   public LinkedList<GrowBlock> growBlocks;
/*    */   public FarmChunk farmChunk;
/*    */   
/*    */   public GrowRequest(ReentrantLock lock)
/*    */   {
/* 30 */     super(lock);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\sync\request\GrowRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
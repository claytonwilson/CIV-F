/*    */ package com.avrgaming.civcraft.threading.sync;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.threading.sync.request.UpdateInventoryRequest;
/*    */ import com.avrgaming.civcraft.util.MultiInventory;
/*    */ import java.util.LinkedList;
/*    */ import java.util.Queue;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SyncUpdateInventory
/*    */   implements Runnable
/*    */ {
/*    */   public static final int UPDATE_LIMIT = 200;
/* 36 */   public static Queue<UpdateInventoryRequest> requestQueue = new LinkedList();
/*    */   public static ReentrantLock lock;
/*    */   
/*    */   public SyncUpdateInventory() {
/* 40 */     lock = new ReentrantLock();
/*    */   }
/*    */   
/*    */ 
/*    */   public void run()
/*    */   {
/* 46 */     Boolean retBool = Boolean.valueOf(false);
/* 47 */     if (lock.tryLock()) {
/*    */       try {
/* 49 */         for (int i = 0; i < 200; i++) {
/* 50 */           UpdateInventoryRequest request = (UpdateInventoryRequest)requestQueue.poll();
/* 51 */           if (request == null) {
/* 52 */             return;
/*    */           }
/*    */           
/*    */ 
/* 56 */           switch (request.action) {
/*    */           case ADD: 
/* 58 */             int leftovers = request.inv.addItem(request.stack);
/* 59 */             retBool = Boolean.valueOf(leftovers <= 0);
/* 60 */             break;
/*    */           case REMOVE: 
/*    */             try {
/* 63 */               retBool = Boolean.valueOf(request.inv.removeItem(request.stack));
/*    */             } catch (CivException e) {
/* 65 */               e.printStackTrace();
/*    */             }
/*    */           }
/*    */           
/*    */           
/* 70 */           request.result = retBool;
/* 71 */           request.finished = Boolean.valueOf(true);
/* 72 */           request.condition.signalAll();
/*    */         }
/*    */       } finally {
/* 75 */         lock.unlock(); } lock.unlock();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\sync\SyncUpdateInventory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
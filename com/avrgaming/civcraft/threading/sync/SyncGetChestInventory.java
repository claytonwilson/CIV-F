/*    */ package com.avrgaming.civcraft.threading.sync;
/*    */ 
/*    */ import com.avrgaming.civcraft.threading.sync.request.GetChestRequest;
/*    */ import com.avrgaming.civcraft.util.ItemManager;
/*    */ import java.util.LinkedList;
/*    */ import java.util.Queue;
/*    */ import java.util.concurrent.locks.Condition;
/*    */ import java.util.concurrent.locks.ReentrantLock;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.Chunk;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.block.BlockState;
/*    */ import org.bukkit.block.Chest;
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
/*    */ public class SyncGetChestInventory
/*    */   implements Runnable
/*    */ {
/*    */   public static final int TIMEOUT_SECONDS = 2;
/*    */   public static final int UPDATE_LIMIT = 20;
/*    */   public static ReentrantLock lock;
/* 40 */   public static Queue<GetChestRequest> requestQueue = new LinkedList();
/*    */   
/*    */ 
/*    */   public static boolean add(GetChestRequest request)
/*    */   {
/* 45 */     return requestQueue.offer(request);
/*    */   }
/*    */   
/*    */   public SyncGetChestInventory() {
/* 49 */     lock = new ReentrantLock();
/*    */   }
/*    */   
/*    */ 
/*    */   public void run()
/*    */   {
/* 55 */     if (lock.tryLock()) {
/*    */       try {
/* 57 */         for (int i = 0; i < 20; i++) {
/* 58 */           GetChestRequest request = (GetChestRequest)requestQueue.poll();
/* 59 */           if (request == null) {
/* 60 */             return;
/*    */           }
/*    */           
/* 63 */           Block b = Bukkit.getWorld(request.worldName).getBlockAt(request.block_x, request.block_y, request.block_z);
/* 64 */           Chest chest = null;
/*    */           
/*    */ 
/* 67 */           if (b.getChunk().isLoaded()) {
/*    */             try {
/* 69 */               chest = (Chest)b.getState();
/*    */             }
/*    */             catch (ClassCastException e) {
/* 72 */               ItemManager.setTypeId(b, 54);
/* 73 */               ItemManager.setTypeId(b.getState(), 54);
/* 74 */               b.getState().update();
/* 75 */               chest = (Chest)b.getState();
/*    */             }
/*    */           }
/*    */           
/*    */ 
/*    */ 
/* 81 */           request.result = chest.getBlockInventory();
/* 82 */           request.finished = Boolean.valueOf(true);
/* 83 */           request.condition.signalAll();
/*    */         }
/*    */       }
/*    */       finally {
/* 87 */         lock.unlock(); } lock.unlock();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\sync\SyncGetChestInventory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package com.avrgaming.civcraft.threading.sync;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import com.avrgaming.civcraft.threading.sync.request.LoadChunkRequest;
/*    */ import java.util.LinkedList;
/*    */ import java.util.Queue;
/*    */ import java.util.concurrent.locks.Condition;
/*    */ import java.util.concurrent.locks.ReentrantLock;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.Chunk;
/*    */ import org.bukkit.World;
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
/*    */ public class SyncLoadChunk
/*    */   implements Runnable
/*    */ {
/*    */   public static final int UPDATE_LIMIT = 2048;
/* 37 */   public static Queue<LoadChunkRequest> requestQueue = new LinkedList();
/*    */   public static ReentrantLock lock;
/*    */   
/*    */   public SyncLoadChunk() {
/* 41 */     lock = new ReentrantLock();
/*    */   }
/*    */   
/*    */ 
/*    */   public void run()
/*    */   {
/* 47 */     if (lock.tryLock()) {
/*    */       try {
/* 49 */         for (int i = 0; i < 2048; i++) {
/* 50 */           LoadChunkRequest request = (LoadChunkRequest)requestQueue.poll();
/* 51 */           if (request == null) {
/* 52 */             return;
/*    */           }
/*    */           
/* 55 */           Chunk chunk = Bukkit.getWorld(request.worldName).getChunkAt(request.x, request.z);
/* 56 */           if ((!chunk.isLoaded()) && 
/* 57 */             (!chunk.load())) {
/* 58 */             CivLog.error("Couldn't load chunk at " + request.x + "," + request.z);
/*    */ 
/*    */           }
/*    */           else
/*    */           {
/* 63 */             request.finished = Boolean.valueOf(true);
/* 64 */             request.condition.signalAll();
/*    */           }
/*    */         }
/* 67 */       } finally { lock.unlock(); } lock.unlock();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\sync\SyncLoadChunk.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
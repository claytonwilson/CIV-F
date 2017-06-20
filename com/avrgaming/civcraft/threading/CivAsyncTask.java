/*     */ package com.avrgaming.civcraft.threading;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivTaskAbortException;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.structure.farm.FarmChunk;
/*     */ import com.avrgaming.civcraft.structure.farm.GrowBlock;
/*     */ import com.avrgaming.civcraft.threading.sync.SyncBuildUpdateTask;
/*     */ import com.avrgaming.civcraft.threading.sync.SyncGetChestInventory;
/*     */ import com.avrgaming.civcraft.threading.sync.SyncGrowTask;
/*     */ import com.avrgaming.civcraft.threading.sync.SyncUpdateInventory;
/*     */ import com.avrgaming.civcraft.threading.sync.request.GetChestRequest;
/*     */ import com.avrgaming.civcraft.threading.sync.request.GrowRequest;
/*     */ import com.avrgaming.civcraft.threading.sync.request.UpdateInventoryRequest;
/*     */ import com.avrgaming.civcraft.threading.sync.request.UpdateInventoryRequest.Action;
/*     */ import com.avrgaming.civcraft.util.MultiInventory;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CivAsyncTask
/*     */   implements Runnable
/*     */ {
/*     */   public static final long TIMEOUT = 5000L;
/*  68 */   protected boolean finished = true;
/*     */   
/*     */   public boolean isFinished()
/*     */   {
/*  72 */     return this.finished;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void syncLoadChunk(String worldname, int x, int z)
/*     */     throws InterruptedException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 32	com/avrgaming/civcraft/threading/sync/request/LoadChunkRequest
/*     */     //   3: dup
/*     */     //   4: getstatic 34	com/avrgaming/civcraft/threading/sync/SyncLoadChunk:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   7: invokespecial 40	com/avrgaming/civcraft/threading/sync/request/LoadChunkRequest:<init>	(Ljava/util/concurrent/locks/ReentrantLock;)V
/*     */     //   10: astore 4
/*     */     //   12: aload 4
/*     */     //   14: aload_1
/*     */     //   15: putfield 43	com/avrgaming/civcraft/threading/sync/request/LoadChunkRequest:worldName	Ljava/lang/String;
/*     */     //   18: aload 4
/*     */     //   20: iload_2
/*     */     //   21: putfield 47	com/avrgaming/civcraft/threading/sync/request/LoadChunkRequest:x	I
/*     */     //   24: aload 4
/*     */     //   26: iload_3
/*     */     //   27: putfield 51	com/avrgaming/civcraft/threading/sync/request/LoadChunkRequest:z	I
/*     */     //   30: aload_0
/*     */     //   31: iconst_0
/*     */     //   32: putfield 19	com/avrgaming/civcraft/threading/CivAsyncTask:finished	Z
/*     */     //   35: getstatic 34	com/avrgaming/civcraft/threading/sync/SyncLoadChunk:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   38: invokevirtual 54	java/util/concurrent/locks/ReentrantLock:lock	()V
/*     */     //   41: getstatic 58	com/avrgaming/civcraft/threading/sync/SyncLoadChunk:requestQueue	Ljava/util/Queue;
/*     */     //   44: aload 4
/*     */     //   46: invokeinterface 62 2 0
/*     */     //   51: pop
/*     */     //   52: goto +36 -> 88
/*     */     //   55: aload 4
/*     */     //   57: getfield 68	com/avrgaming/civcraft/threading/sync/request/LoadChunkRequest:condition	Ljava/util/concurrent/locks/Condition;
/*     */     //   60: ldc2_w 10
/*     */     //   63: getstatic 72	java/util/concurrent/TimeUnit:MILLISECONDS	Ljava/util/concurrent/TimeUnit;
/*     */     //   66: invokeinterface 78 4 0
/*     */     //   71: pop
/*     */     //   72: aload 4
/*     */     //   74: getfield 84	com/avrgaming/civcraft/threading/sync/request/LoadChunkRequest:finished	Ljava/lang/Boolean;
/*     */     //   77: invokevirtual 87	java/lang/Boolean:booleanValue	()Z
/*     */     //   80: ifne +8 -> 88
/*     */     //   83: ldc 92
/*     */     //   85: invokestatic 94	com/avrgaming/civcraft/main/CivLog:warning	(Ljava/lang/String;)V
/*     */     //   88: aload 4
/*     */     //   90: getfield 84	com/avrgaming/civcraft/threading/sync/request/LoadChunkRequest:finished	Ljava/lang/Boolean;
/*     */     //   93: invokevirtual 87	java/lang/Boolean:booleanValue	()Z
/*     */     //   96: ifeq -41 -> 55
/*     */     //   99: goto +19 -> 118
/*     */     //   102: astore 5
/*     */     //   104: aload_0
/*     */     //   105: iconst_1
/*     */     //   106: putfield 19	com/avrgaming/civcraft/threading/CivAsyncTask:finished	Z
/*     */     //   109: getstatic 34	com/avrgaming/civcraft/threading/sync/SyncLoadChunk:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   112: invokevirtual 100	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   115: aload 5
/*     */     //   117: athrow
/*     */     //   118: aload_0
/*     */     //   119: iconst_1
/*     */     //   120: putfield 19	com/avrgaming/civcraft/threading/CivAsyncTask:finished	Z
/*     */     //   123: getstatic 34	com/avrgaming/civcraft/threading/sync/SyncLoadChunk:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   126: invokevirtual 100	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   129: return
/*     */     // Line number table:
/*     */     //   Java source line #77	-> byte code offset #0
/*     */     //   Java source line #78	-> byte code offset #12
/*     */     //   Java source line #79	-> byte code offset #18
/*     */     //   Java source line #80	-> byte code offset #24
/*     */     //   Java source line #82	-> byte code offset #30
/*     */     //   Java source line #84	-> byte code offset #35
/*     */     //   Java source line #86	-> byte code offset #41
/*     */     //   Java source line #87	-> byte code offset #52
/*     */     //   Java source line #93	-> byte code offset #55
/*     */     //   Java source line #94	-> byte code offset #72
/*     */     //   Java source line #95	-> byte code offset #83
/*     */     //   Java source line #87	-> byte code offset #88
/*     */     //   Java source line #99	-> byte code offset #99
/*     */     //   Java source line #100	-> byte code offset #104
/*     */     //   Java source line #101	-> byte code offset #109
/*     */     //   Java source line #102	-> byte code offset #115
/*     */     //   Java source line #100	-> byte code offset #118
/*     */     //   Java source line #101	-> byte code offset #123
/*     */     //   Java source line #104	-> byte code offset #129
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	130	0	this	CivAsyncTask
/*     */     //   0	130	1	worldname	String
/*     */     //   0	130	2	x	int
/*     */     //   0	130	3	z	int
/*     */     //   10	79	4	request	com.avrgaming.civcraft.threading.sync.request.LoadChunkRequest
/*     */     //   102	14	5	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   41	102	102	finally
/*     */   }
/*     */   
/*     */   public Inventory getChestInventory(String worldname, int x, int y, int z, boolean retry)
/*     */     throws InterruptedException, CivTaskAbortException
/*     */   {
/* 108 */     GetChestRequest request = new GetChestRequest(SyncGetChestInventory.lock);
/* 109 */     request.worldName = worldname;
/* 110 */     request.block_x = x;
/* 111 */     request.block_y = y;
/* 112 */     request.block_z = z;
/*     */     
/* 114 */     this.finished = false;
/*     */     
/* 116 */     SyncGetChestInventory.lock.lock();
/*     */     try {
/* 118 */       SyncGetChestInventory.requestQueue.add(request);
/* 119 */       while (!request.finished.booleanValue())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 125 */         request.condition.await(5000L, TimeUnit.MILLISECONDS);
/* 126 */         if (!request.finished.booleanValue()) {
/* 127 */           if (!retry) {
/* 128 */             throw new CivTaskAbortException("Couldn't get chest in 5000 milliseconds, aborting.");
/*     */           }
/* 130 */           CivLog.warning("Couldn't get chest in 5000 milliseconds! Retrying.");
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 135 */       return (Inventory)request.result;
/*     */     } finally {
/* 137 */       this.finished = true;
/* 138 */       SyncGetChestInventory.lock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateBlocksQueue(Queue<SimpleBlock> sbs)
/*     */   {
/* 144 */     SyncBuildUpdateTask.queueSimpleBlock(sbs);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Boolean updateInventory(UpdateInventoryRequest.Action action, MultiInventory inv, ItemStack itemStack)
/*     */     throws InterruptedException
/*     */   {
/* 162 */     UpdateInventoryRequest request = new UpdateInventoryRequest(SyncUpdateInventory.lock);
/* 163 */     request.action = action;
/* 164 */     request.stack = itemStack;
/* 165 */     request.inv = inv;
/*     */     
/* 167 */     this.finished = false;
/*     */     
/* 169 */     SyncUpdateInventory.lock.lock();
/*     */     try {
/* 171 */       SyncUpdateInventory.requestQueue.add(request);
/* 172 */       while (!request.finished.booleanValue())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 178 */         request.condition.await(5000L, TimeUnit.MILLISECONDS);
/* 179 */         if (!request.finished.booleanValue()) {
/* 180 */           CivLog.warning("Couldn't update inventory in 5000 milliseconds! Retrying.");
/*     */         }
/*     */       }
/*     */       
/* 184 */       return (Boolean)request.result;
/*     */     } finally {
/* 186 */       this.finished = true;
/* 187 */       SyncUpdateInventory.lock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public Boolean growBlocks(LinkedList<GrowBlock> growBlocks, FarmChunk farmChunk) throws InterruptedException
/*     */   {
/* 193 */     GrowRequest request = new GrowRequest(SyncGrowTask.lock);
/* 194 */     request.growBlocks = growBlocks;
/* 195 */     request.farmChunk = farmChunk;
/*     */     
/* 197 */     this.finished = false;
/* 198 */     SyncGrowTask.lock.lock();
/*     */     try {
/* 200 */       SyncGrowTask.requestQueue.add(request);
/* 201 */       while (!request.finished.booleanValue()) {
/* 202 */         request.condition.await(5000L, TimeUnit.MILLISECONDS);
/* 203 */         if (!request.finished.booleanValue()) {
/* 204 */           CivLog.warning("Couldn't grow block in 5000 milliseconds! retrying.");
/*     */         }
/*     */       }
/*     */       
/* 208 */       return (Boolean)request.result;
/*     */     } finally {
/* 210 */       this.finished = true;
/* 211 */       SyncGrowTask.lock.unlock();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\CivAsyncTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
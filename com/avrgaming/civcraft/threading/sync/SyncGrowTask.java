/*     */ package com.avrgaming.civcraft.threading.sync;
/*     */ 
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.structure.Farm;
/*     */ import com.avrgaming.civcraft.structure.farm.FarmChunk;
/*     */ import com.avrgaming.civcraft.structure.farm.GrowBlock;
/*     */ import com.avrgaming.civcraft.threading.sync.request.GrowRequest;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.bukkit.Chunk;
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
/*     */ public class SyncGrowTask
/*     */   implements Runnable
/*     */ {
/*  37 */   public static Queue<GrowRequest> requestQueue = new LinkedList();
/*     */   public static ReentrantLock lock;
/*     */   public static final int UPDATE_LIMIT = 200;
/*     */   
/*     */   public SyncGrowTask()
/*     */   {
/*  43 */     lock = new ReentrantLock();
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*  48 */     if (!CivGlobal.growthEnabled) {
/*  49 */       return;
/*     */     }
/*     */     
/*  52 */     HashSet<FarmChunk> unloadedFarms = new HashSet();
/*     */     
/*  54 */     if (lock.tryLock()) {
/*     */       try { GrowRequest request;
/*  56 */         for (int i = 0; i < 200; i++) {
/*  57 */           request = (GrowRequest)requestQueue.poll();
/*  58 */           if (request == null) {
/*  59 */             return;
/*     */           }
/*     */           
/*  62 */           if (request.farmChunk == null) {
/*  63 */             request.result = Boolean.valueOf(false);
/*  64 */           } else if (!request.farmChunk.getChunk().isLoaded())
/*     */           {
/*     */ 
/*     */ 
/*  68 */             unloadedFarms.add(request.farmChunk);
/*  69 */             request.result = Boolean.valueOf(false);
/*     */           }
/*     */           else
/*     */           {
/*  73 */             for (GrowBlock growBlock : request.growBlocks) {
/*  74 */               switch (growBlock.typeId) {
/*     */               case 59: 
/*     */               case 141: 
/*     */               case 142: 
/*  78 */                 if (growBlock.data - 1 != ItemManager.getData(growBlock.bcoord.getBlock())) {
/*     */                   break;
/*     */                 }
/*     */               
/*     */ 
/*     */ 
/*     */               default: 
/*  85 */                 if ((growBlock.spawn) || (ItemManager.getId(growBlock.bcoord.getBlock()) == growBlock.typeId))
/*     */                 {
/*     */ 
/*  88 */                   if (growBlock.spawn)
/*     */                   {
/*  90 */                     ItemManager.setTypeId(growBlock.bcoord.getBlock(), growBlock.typeId);
/*     */                   }
/*  92 */                   ItemManager.setData(growBlock.bcoord.getBlock(), growBlock.data);
/*  93 */                   request.result = Boolean.valueOf(true);
/*     */                 }
/*     */                 break;
/*     */               }
/*     */             }
/*     */           }
/*  99 */           request.finished = Boolean.valueOf(true);
/* 100 */           request.condition.signalAll();
/*     */         }
/*     */         
/*     */ 
/* 104 */         for (FarmChunk fc : unloadedFarms) {
/* 105 */           fc.incrementMissedGrowthTicks();
/* 106 */           Farm farm = (Farm)fc.getStruct();
/* 107 */           farm.saveMissedGrowths();
/*     */         }
/*     */       }
/*     */       finally
/*     */       {
/* 112 */         lock.unlock(); } lock.unlock();
/*     */     }
/*     */     else {
/* 115 */       CivLog.warning("SyncGrowTask: lock busy, retrying next tick.");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\sync\SyncGrowTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
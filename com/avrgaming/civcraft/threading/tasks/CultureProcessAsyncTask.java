/*     */ package com.avrgaming.civcraft.threading.tasks;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigCultureLevel;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.CultureChunk;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.object.TownChunk;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import com.avrgaming.civcraft.threading.CivAsyncTask;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.locks.ReentrantLock;
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
/*     */ public class CultureProcessAsyncTask
/*     */   extends CivAsyncTask
/*     */ {
/*  40 */   public ReentrantLock lock = new ReentrantLock();
/*  41 */   public static boolean cultureProcessedSinceStartup = false;
/*     */   
/*     */   private void processTownCulture(Town town) {
/*  44 */     ChunkCoord origin = town.getTownCultureOrigin();
/*     */     
/*     */ 
/*  47 */     HashSet<ChunkCoord> expanded = new HashSet();
/*  48 */     CultureChunk starting = CivGlobal.getCultureChunk(origin);
/*  49 */     if (starting == null) {
/*  50 */       starting = new CultureChunk(town, origin);
/*  51 */       town.addCultureChunk(starting);
/*  52 */       CivGlobal.addCultureChunk(starting);
/*     */     }
/*     */     
/*  55 */     _processCultureBreadthFirst(town, origin, starting, expanded);
/*     */     
/*  57 */     town.trimCultureChunks(expanded);
/*  58 */     int expandedAmount = expanded.size() - town.getCultureChunks().size();
/*  59 */     if (expandedAmount > 0) {
/*  60 */       CivMessage.sendCiv(town.getCiv(), "Town of " + town.getName() + " expanded " + expandedAmount + " chunks!");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void _processCultureBreadthFirst(Town town, ChunkCoord origin, CultureChunk starting, HashSet<ChunkCoord> expanded)
/*     */   {
/*  68 */     Queue<CultureChunk> openList = new LinkedBlockingQueue();
/*  69 */     HashMap<ChunkCoord, CultureChunk> closedChunks = new HashMap();
/*  70 */     ConfigCultureLevel clc = (ConfigCultureLevel)CivSettings.cultureLevels.get(Integer.valueOf(town.getCultureLevel()));
/*     */     
/*  72 */     openList.add(starting);
/*  73 */     if (starting.getTown() != town)
/*     */     {
/*     */ 
/*  76 */       starting.getTown().removeCultureChunk(starting);
/*  77 */       starting.setTown(town);
/*  78 */       starting.getTown().addCultureChunk(starting);
/*     */     }
/*     */     
/*  81 */     while (!openList.isEmpty())
/*     */     {
/*  83 */       CultureChunk node = (CultureChunk)openList.poll();
/*     */       
/*     */ 
/*  86 */       if (!closedChunks.containsKey(node.getChunkCoord()))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  93 */         if (node.getChunkCoord().manhattanDistance(origin) > clc.chunks) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*  99 */         closedChunks.put(node.getChunkCoord(), node);
/*     */         
/*     */ 
/* 102 */         int[][] offset = { { -1 }, { 1 }, { 0, -1 }, { 0, 1 } };
/* 103 */         for (int i = 0; i < 4; i++) {
/* 104 */           ChunkCoord nextCoord = new ChunkCoord(node.getChunkCoord().getWorldname(), 
/* 105 */             node.getChunkCoord().getX() + offset[i][0], 
/* 106 */             node.getChunkCoord().getZ() + offset[i][1]);
/*     */           
/* 108 */           TownChunk townChunk = CivGlobal.getTownChunk(nextCoord);
/*     */           
/* 110 */           if ((townChunk == null) || 
/* 111 */             (townChunk.getTown() == town))
/*     */           {
/*     */ 
/*     */ 
/* 115 */             CultureChunk neighbor = CivGlobal.getCultureChunk(nextCoord);
/* 116 */             if (neighbor == null)
/*     */             {
/*     */ 
/* 119 */               if (node.getChunkCoord().manhattanDistance(origin) + 1 >= clc.chunks) continue;
/* 120 */               neighbor = new CultureChunk(town, nextCoord);
/* 121 */               town.addCultureChunk(neighbor);
/* 122 */               CivGlobal.addCultureChunk(neighbor);
/* 123 */               expanded.add(neighbor.getChunkCoord());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             }
/* 129 */             else if (neighbor.getTown() != node.getTown())
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/* 134 */               double nodePower = node.getPower();
/* 135 */               double neighborPower = neighbor.getPower();
/* 136 */               boolean switchOwners = false;
/*     */               
/* 138 */               if (nodePower > neighborPower) {
/* 139 */                 switchOwners = true;
/*     */               }
/* 141 */               else if (nodePower == neighborPower)
/*     */               {
/*     */ 
/* 144 */                 if (node.getTown().getAccumulatedCulture() > neighbor.getTown().getAccumulatedCulture()) {
/* 145 */                   switchOwners = true;
/*     */                 }
/*     */               }
/*     */               
/* 149 */               if (!switchOwners) continue;
/* 150 */               neighbor.getTown().removeCultureChunk(neighbor);
/* 151 */               node.getTown().addCultureChunk(neighbor);
/* 152 */               expanded.add(neighbor.getChunkCoord());
/* 153 */               neighbor.setTown(node.getTown());
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
/*     */             }
/* 165 */             else if (neighbor.getChunkCoord().manhattanDistance(origin) < clc.chunks) {
/* 166 */               expanded.add(neighbor.getChunkCoord());
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 172 */             neighbor.setDistance(neighbor.getChunkCoord().manhattanDistance(origin));
/*     */             
/*     */ 
/*     */ 
/* 176 */             if ((townChunk != null) && 
/* 177 */               (townChunk.getTown() == town)) {
/* 178 */               neighbor.setDistance(0);
/*     */             }
/*     */             
/*     */ 
/* 182 */             if (!closedChunks.containsKey(neighbor.getChunkCoord()))
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/* 187 */               openList.add(neighbor);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void run()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 22	com/avrgaming/civcraft/threading/tasks/CultureProcessAsyncTask:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   4: invokevirtual 249	java/util/concurrent/locks/ReentrantLock:lock	()V
/*     */     //   7: invokestatic 251	com/avrgaming/civcraft/main/CivGlobal:getTowns	()Ljava/util/Collection;
/*     */     //   10: invokeinterface 254 1 0
/*     */     //   15: astore_2
/*     */     //   16: goto +18 -> 34
/*     */     //   19: aload_2
/*     */     //   20: invokeinterface 258 1 0
/*     */     //   25: checkcast 29	com/avrgaming/civcraft/object/Town
/*     */     //   28: astore_1
/*     */     //   29: aload_0
/*     */     //   30: aload_1
/*     */     //   31: invokespecial 263	com/avrgaming/civcraft/threading/tasks/CultureProcessAsyncTask:processTownCulture	(Lcom/avrgaming/civcraft/object/Town;)V
/*     */     //   34: aload_2
/*     */     //   35: invokeinterface 265 1 0
/*     */     //   40: ifne -21 -> 19
/*     */     //   43: invokestatic 268	com/avrgaming/civcraft/threading/tasks/CultureProcessAsyncTask:recalculateTouchingCultures	()V
/*     */     //   46: invokestatic 271	com/avrgaming/civcraft/threading/tasks/CultureProcessAsyncTask:processStructureFlipping	()V
/*     */     //   49: iconst_1
/*     */     //   50: putstatic 12	com/avrgaming/civcraft/threading/tasks/CultureProcessAsyncTask:cultureProcessedSinceStartup	Z
/*     */     //   53: goto +13 -> 66
/*     */     //   56: astore_3
/*     */     //   57: aload_0
/*     */     //   58: getfield 22	com/avrgaming/civcraft/threading/tasks/CultureProcessAsyncTask:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   61: invokevirtual 274	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   64: aload_3
/*     */     //   65: athrow
/*     */     //   66: aload_0
/*     */     //   67: getfield 22	com/avrgaming/civcraft/threading/tasks/CultureProcessAsyncTask:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   70: invokevirtual 274	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   73: return
/*     */     // Line number table:
/*     */     //   Java source line #215	-> byte code offset #0
/*     */     //   Java source line #217	-> byte code offset #7
/*     */     //   Java source line #218	-> byte code offset #29
/*     */     //   Java source line #217	-> byte code offset #34
/*     */     //   Java source line #221	-> byte code offset #43
/*     */     //   Java source line #222	-> byte code offset #46
/*     */     //   Java source line #224	-> byte code offset #49
/*     */     //   Java source line #225	-> byte code offset #53
/*     */     //   Java source line #226	-> byte code offset #57
/*     */     //   Java source line #227	-> byte code offset #64
/*     */     //   Java source line #226	-> byte code offset #66
/*     */     //   Java source line #228	-> byte code offset #73
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	74	0	this	CultureProcessAsyncTask
/*     */     //   28	3	1	t	Town
/*     */     //   15	20	2	localIterator	Iterator
/*     */     //   56	9	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	56	56	finally
/*     */   }
/*     */   
/*     */   private static void processStructureFlipping()
/*     */   {
/* 236 */     HashMap<ChunkCoord, Structure> centerCoords = new HashMap();
/*     */     
/* 238 */     for (Structure struct : CivGlobal.getStructures()) {
/* 239 */       ChunkCoord coord = new ChunkCoord(struct.getCenterLocation());
/* 240 */       centerCoords.put(coord, struct);
/*     */     }
/*     */     
/* 243 */     for (Town t : CivGlobal.getTowns())
/* 244 */       t.processStructureFlipping(centerCoords);
/*     */   }
/*     */   
/*     */   private static void recalculateTouchingCultures() {
/*     */     Iterator localIterator2;
/* 249 */     for (Iterator localIterator1 = CivGlobal.getTowns().iterator(); localIterator1.hasNext(); 
/*     */         
/*     */ 
/* 252 */         localIterator2.hasNext())
/*     */     {
/* 249 */       Town t = (Town)localIterator1.next();
/*     */       
/* 251 */       t.townTouchList.clear();
/* 252 */       localIterator2 = t.getCultureChunks().iterator(); continue;CultureChunk cc = (CultureChunk)localIterator2.next();
/*     */       
/* 254 */       int[][] offset = { { -1 }, { 1 }, { 0, -1 }, { 0, 1 } };
/* 255 */       for (int i = 0; i < 4; i++) {
/* 256 */         ChunkCoord nextCoord = new ChunkCoord(cc.getChunkCoord().getWorldname(), 
/* 257 */           cc.getChunkCoord().getX() + offset[i][0], 
/* 258 */           cc.getChunkCoord().getZ() + offset[i][1]);
/*     */         
/* 260 */         CultureChunk next = CivGlobal.getCultureChunk(nextCoord);
/* 261 */         if (next != null)
/*     */         {
/*     */ 
/*     */ 
/* 265 */           if (next.getTown() != cc.getTown())
/*     */           {
/*     */ 
/*     */ 
/* 269 */             if (!t.townTouchList.contains(next.getTown()))
/*     */             {
/*     */ 
/*     */ 
/* 273 */               t.townTouchList.add(next.getTown());
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\CultureProcessAsyncTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
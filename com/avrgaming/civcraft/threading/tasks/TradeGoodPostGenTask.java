/*     */ package com.avrgaming.civcraft.threading.tasks;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.ConfigTradeGood;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.populators.TradeGoodPick;
/*     */ import com.avrgaming.civcraft.populators.TradeGoodPopulator;
/*     */ import com.avrgaming.civcraft.populators.TradeGoodPreGenerate;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import java.io.PrintStream;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Chunk;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockFace;
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
/*     */ public class TradeGoodPostGenTask
/*     */   implements Runnable
/*     */ {
/*     */   String playerName;
/*     */   int start;
/*     */   
/*     */   public TradeGoodPostGenTask(String playerName, int start)
/*     */   {
/*  52 */     this.playerName = playerName;
/*  53 */     this.start = 0;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void deleteAllTradeGoodiesFromDB()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_1
/*     */     //   2: aconst_null
/*     */     //   3: astore_2
/*     */     //   4: invokestatic 26	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*     */     //   7: astore_1
/*     */     //   8: ldc 32
/*     */     //   10: astore_3
/*     */     //   11: aload_1
/*     */     //   12: aload_3
/*     */     //   13: invokeinterface 34 2 0
/*     */     //   18: astore_2
/*     */     //   19: aload_2
/*     */     //   20: invokeinterface 40 1 0
/*     */     //   25: pop
/*     */     //   26: goto +56 -> 82
/*     */     //   29: astore_3
/*     */     //   30: aload_3
/*     */     //   31: invokevirtual 46	java/sql/SQLException:printStackTrace	()V
/*     */     //   34: goto +48 -> 82
/*     */     //   37: astore 4
/*     */     //   39: aload_1
/*     */     //   40: ifnull +19 -> 59
/*     */     //   43: aload_1
/*     */     //   44: invokeinterface 51 1 0
/*     */     //   49: goto +10 -> 59
/*     */     //   52: astore 5
/*     */     //   54: aload 5
/*     */     //   56: invokevirtual 46	java/sql/SQLException:printStackTrace	()V
/*     */     //   59: aload_2
/*     */     //   60: ifnull +19 -> 79
/*     */     //   63: aload_2
/*     */     //   64: invokeinterface 54 1 0
/*     */     //   69: goto +10 -> 79
/*     */     //   72: astore 5
/*     */     //   74: aload 5
/*     */     //   76: invokevirtual 46	java/sql/SQLException:printStackTrace	()V
/*     */     //   79: aload 4
/*     */     //   81: athrow
/*     */     //   82: aload_1
/*     */     //   83: ifnull +19 -> 102
/*     */     //   86: aload_1
/*     */     //   87: invokeinterface 51 1 0
/*     */     //   92: goto +10 -> 102
/*     */     //   95: astore 5
/*     */     //   97: aload 5
/*     */     //   99: invokevirtual 46	java/sql/SQLException:printStackTrace	()V
/*     */     //   102: aload_2
/*     */     //   103: ifnull +19 -> 122
/*     */     //   106: aload_2
/*     */     //   107: invokeinterface 54 1 0
/*     */     //   112: goto +10 -> 122
/*     */     //   115: astore 5
/*     */     //   117: aload 5
/*     */     //   119: invokevirtual 46	java/sql/SQLException:printStackTrace	()V
/*     */     //   122: return
/*     */     // Line number table:
/*     */     //   Java source line #58	-> byte code offset #0
/*     */     //   Java source line #59	-> byte code offset #2
/*     */     //   Java source line #62	-> byte code offset #4
/*     */     //   Java source line #63	-> byte code offset #8
/*     */     //   Java source line #64	-> byte code offset #11
/*     */     //   Java source line #65	-> byte code offset #19
/*     */     //   Java source line #66	-> byte code offset #26
/*     */     //   Java source line #67	-> byte code offset #30
/*     */     //   Java source line #69	-> byte code offset #34
/*     */     //   Java source line #70	-> byte code offset #39
/*     */     //   Java source line #72	-> byte code offset #43
/*     */     //   Java source line #73	-> byte code offset #49
/*     */     //   Java source line #74	-> byte code offset #54
/*     */     //   Java source line #78	-> byte code offset #59
/*     */     //   Java source line #80	-> byte code offset #63
/*     */     //   Java source line #81	-> byte code offset #69
/*     */     //   Java source line #82	-> byte code offset #74
/*     */     //   Java source line #85	-> byte code offset #79
/*     */     //   Java source line #70	-> byte code offset #82
/*     */     //   Java source line #72	-> byte code offset #86
/*     */     //   Java source line #73	-> byte code offset #92
/*     */     //   Java source line #74	-> byte code offset #97
/*     */     //   Java source line #78	-> byte code offset #102
/*     */     //   Java source line #80	-> byte code offset #106
/*     */     //   Java source line #81	-> byte code offset #112
/*     */     //   Java source line #82	-> byte code offset #117
/*     */     //   Java source line #87	-> byte code offset #122
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	123	0	this	TradeGoodPostGenTask
/*     */     //   1	86	1	conn	java.sql.Connection
/*     */     //   3	104	2	ps	java.sql.PreparedStatement
/*     */     //   10	3	3	code	String
/*     */     //   29	2	3	e1	java.sql.SQLException
/*     */     //   37	43	4	localObject	Object
/*     */     //   52	3	5	e	java.sql.SQLException
/*     */     //   72	3	5	e	java.sql.SQLException
/*     */     //   95	3	5	e	java.sql.SQLException
/*     */     //   115	3	5	e	java.sql.SQLException
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	26	29	java/sql/SQLException
/*     */     //   4	37	37	finally
/*     */     //   43	49	52	java/sql/SQLException
/*     */     //   63	69	72	java/sql/SQLException
/*     */     //   86	92	95	java/sql/SQLException
/*     */     //   106	112	115	java/sql/SQLException
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*  91 */     CivMessage.console(this.playerName, "Generating/Clearing Trade goods...");
/*  92 */     CivMessage.console(this.playerName, "|- Organizing trade picks into a Queue.");
/*     */     
/*  94 */     deleteAllTradeGoodiesFromDB();
/*     */     
/*     */ 
/*  97 */     Queue<TradeGoodPick> picksQueue = new LinkedList();
/*  98 */     for (TradeGoodPick pick : CivGlobal.preGenerator.goodPicks.values()) {
/*  99 */       picksQueue.add(pick);
/*     */     }
/*     */     
/* 102 */     int count = 0;
/* 103 */     int amount = 20;
/* 104 */     int totalSize = picksQueue.size();
/* 105 */     while (picksQueue.peek() != null) {
/* 106 */       CivMessage.console(this.playerName, "|- Placing/Picking Goods:" + count + "/" + totalSize + " current size:" + picksQueue.size());
/* 107 */       TaskMaster.syncTask(new SyncTradeGenTask(picksQueue, amount));
/* 108 */       count += amount;
/*     */       try {
/* 110 */         Thread.sleep(500L);
/*     */       } catch (InterruptedException e) {
/* 112 */         return;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 117 */     CivMessage.console(this.playerName, "Finished!");
/*     */   }
/*     */   
/*     */   class SyncTradeGenTask implements Runnable {
/*     */     public Queue<TradeGoodPick> picksQueue;
/*     */     public int amount;
/*     */     
/*     */     public SyncTradeGenTask(int picksQueue) {
/* 125 */       this.picksQueue = picksQueue;
/* 126 */       this.amount = amount;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 131 */       World world = Bukkit.getWorld("world");
/* 132 */       BlockCoord bcoord2 = new BlockCoord();
/*     */       
/* 134 */       for (int i = 0; i < this.amount; i++) {
/* 135 */         TradeGoodPick pick = (TradeGoodPick)this.picksQueue.poll();
/* 136 */         if (pick == null) {
/* 137 */           return;
/*     */         }
/*     */         
/* 140 */         ChunkCoord coord = pick.chunkCoord;
/* 141 */         Chunk chunk = world.getChunkAt(coord.getX(), coord.getZ());
/*     */         
/* 143 */         int centerX = (chunk.getX() << 4) + 8;
/* 144 */         int centerZ = (chunk.getZ() << 4) + 8;
/* 145 */         int centerY = world.getHighestBlockYAt(centerX, centerZ);
/*     */         
/*     */ 
/*     */ 
/* 149 */         bcoord2.setWorldname("world");
/* 150 */         bcoord2.setX(centerX);
/* 151 */         bcoord2.setY(centerY - 1);
/* 152 */         bcoord2.setZ(centerZ);
/*     */         
/*     */         for (;;)
/*     */         {
/* 156 */           Block top = world.getBlockAt(bcoord2.getX(), bcoord2.getY(), bcoord2.getZ());
/*     */           
/* 158 */           if (!top.getChunk().isLoaded()) {
/* 159 */             top.getChunk().load();
/*     */           }
/*     */           
/* 162 */           if (ItemManager.getId(top) != 7) break;
/* 163 */           ItemManager.setTypeId(top, 0);
/* 164 */           ItemManager.setData(top, 0, true);
/* 165 */           bcoord2.setY(bcoord2.getY() - 1);
/*     */           
/* 167 */           top = top.getRelative(BlockFace.NORTH);
/* 168 */           if (ItemManager.getId(top) == 68) {
/* 169 */             ItemManager.setTypeId(top, 0);
/* 170 */             ItemManager.setData(top, 0, true);
/*     */           }
/*     */           
/* 173 */           top = top.getRelative(BlockFace.SOUTH);
/* 174 */           if (ItemManager.getId(top) == 68) {
/* 175 */             ItemManager.setTypeId(top, 0);
/* 176 */             ItemManager.setData(top, 0, true);
/*     */           }
/*     */           
/* 179 */           top = top.getRelative(BlockFace.EAST);
/* 180 */           if (ItemManager.getId(top) == 68) {
/* 181 */             ItemManager.setTypeId(top, 0);
/* 182 */             ItemManager.setData(top, 0, true);
/*     */           }
/*     */           
/* 185 */           top = top.getRelative(BlockFace.WEST);
/* 186 */           if (ItemManager.getId(top) == 68) {
/* 187 */             ItemManager.setTypeId(top, 0);
/* 188 */             ItemManager.setData(top, 0, true);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 196 */         centerY = world.getHighestBlockYAt(centerX, centerZ);
/*     */         
/*     */         ConfigTradeGood good;
/*     */         ConfigTradeGood good;
/* 200 */         if ((ItemManager.getBlockTypeIdAt(world, centerX, centerY - 1, centerZ) == 9) || 
/* 201 */           (ItemManager.getBlockTypeIdAt(world, centerX, centerY - 1, centerZ) == 8)) {
/* 202 */           good = pick.waterPick;
/*     */         } else {
/* 204 */           good = pick.landPick;
/*     */         }
/*     */         
/*     */ 
/* 208 */         if (good == null) {
/* 209 */           System.out.println("Could not find suitable good type during populate! aborting.");
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 214 */           BlockCoord bcoord = new BlockCoord(world.getName(), centerX, centerY, centerZ);
/* 215 */           TradeGoodPopulator.buildTradeGoodie(good, bcoord, world, true);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\TradeGoodPostGenTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
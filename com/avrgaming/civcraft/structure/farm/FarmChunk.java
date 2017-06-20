/*     */ package com.avrgaming.civcraft.structure.farm;
/*     */ 
/*     */ import com.avrgaming.civcraft.components.ActivateOnBiome;
/*     */ import com.avrgaming.civcraft.components.Component;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.InvalidBlockLocation;
/*     */ import com.avrgaming.civcraft.main.CivData;
/*     */ import com.avrgaming.civcraft.object.AttrSource;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.Farm;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import com.avrgaming.civcraft.threading.CivAsyncTask;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.BlockSnapshot;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.bukkit.Chunk;
/*     */ import org.bukkit.ChunkSnapshot;
/*     */ import org.bukkit.block.Biome;
/*     */ import org.bukkit.block.Block;
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
/*     */ public class FarmChunk
/*     */ {
/*     */   private Town town;
/*     */   private Structure struct;
/*     */   private ChunkCoord coord;
/*     */   public ChunkSnapshot snapshot;
/*  53 */   public ArrayList<BlockCoord> cropLocationCache = new ArrayList();
/*  54 */   public ReentrantLock lock = new ReentrantLock();
/*     */   
/*  56 */   private ArrayList<BlockCoord> lastGrownCrops = new ArrayList();
/*     */   
/*     */   private LinkedList<GrowBlock> growBlocks;
/*     */   private Date lastGrowDate;
/*     */   private int lastGrowTickCount;
/*     */   private double lastChanceForLast;
/*     */   private int lastRandomInt;
/*     */   private int missedGrowthTicks;
/*     */   private int missedGrowthTicksStat;
/*  65 */   String biomeName = "none";
/*     */   
/*     */   public FarmChunk(Chunk c, Town t, Structure struct) {
/*  68 */     this.town = t;
/*  69 */     this.struct = struct;
/*  70 */     this.coord = new ChunkCoord(c);
/*  71 */     this.biomeName = this.coord.getChunk().getBlock(8, 64, 8).getBiome().name();
/*     */   }
/*     */   
/*     */   public Chunk getChunk() {
/*  75 */     return this.coord.getChunk();
/*     */   }
/*     */   
/*     */   public Town getTown() {
/*  79 */     return this.town;
/*     */   }
/*     */   
/*  82 */   public void setTown(Town town) { this.town = town; }
/*     */   
/*     */   public Structure getStruct() {
/*  85 */     return this.struct;
/*     */   }
/*     */   
/*     */   public Farm getFarm() {
/*  89 */     return (Farm)this.struct;
/*     */   }
/*     */   
/*     */   public void setStruct(Structure struct) {
/*  93 */     this.struct = struct;
/*     */   }
/*     */   
/*     */   public boolean isHydrated(Block block) {
/*  97 */     Block beneath = block.getRelative(0, -1, 0);
/*     */     
/*  99 */     if ((beneath != null) && 
/* 100 */       (ItemManager.getId(beneath) == 60) && 
/* 101 */       (ItemManager.getData(beneath) != 0)) {
/* 102 */       return true;
/*     */     }
/*     */     
/* 105 */     return false;
/*     */   }
/*     */   
/*     */   public int getLightLevel(Block block) {
/* 109 */     return block.getLightLevel();
/*     */   }
/*     */   
/*     */   public void spawnMelonOrPumpkin(BlockSnapshot bs, CivAsyncTask task) throws InterruptedException
/*     */   {
/* 114 */     int[][] offset = { { -1 }, { 1 }, { 0, -1 }, { 0, 1 } };
/* 115 */     BlockSnapshot freeBlock = null;
/* 116 */     for (int i = 0; i < 4; i++)
/*     */     {
/*     */       try {
/* 119 */         nextBlock = bs.getRelative(offset[i][0], 0, offset[i][1]);
/*     */       }
/*     */       catch (InvalidBlockLocation e) {
/*     */         BlockSnapshot nextBlock;
/* 123 */         continue;
/*     */       }
/*     */       BlockSnapshot nextBlock;
/* 126 */       if (nextBlock.getTypeId() == 0) {
/* 127 */         freeBlock = nextBlock;
/*     */       }
/*     */       
/* 130 */       if (((nextBlock.getTypeId() == 103) && 
/* 131 */         (bs.getTypeId() == 105)) || (
/* 132 */         (nextBlock.getTypeId() == 86) && 
/* 133 */         (bs.getTypeId() == 104))) {
/* 134 */         return;
/*     */       }
/*     */     }
/*     */     
/* 138 */     if (freeBlock == null) {
/* 139 */       return;
/*     */     }
/*     */     
/* 142 */     if (bs.getTypeId() == 105) {
/* 143 */       addGrowBlock("world", freeBlock.getX(), freeBlock.getY(), freeBlock.getZ(), 103, 0, true);
/*     */     } else {
/* 145 */       addGrowBlock("world", freeBlock.getX(), freeBlock.getY(), freeBlock.getZ(), 86, 0, true);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addGrowBlock(String world, int x, int y, int z, int typeid, int data, boolean spawn)
/*     */   {
/* 151 */     this.growBlocks.add(new GrowBlock(world, x, y, z, typeid, data, spawn));
/*     */   }
/*     */   
/*     */ 
/*     */   public void growBlock(BlockSnapshot bs, BlockCoord growMe, CivAsyncTask task)
/*     */     throws InterruptedException
/*     */   {
/* 158 */     switch (bs.getTypeId()) {
/*     */     case 59: 
/*     */     case 141: 
/*     */     case 142: 
/* 162 */       if (bs.getData() < 7) {
/* 163 */         addGrowBlock("world", growMe.getX(), growMe.getY(), growMe.getZ(), bs.getTypeId(), bs.getData() + 1, false);
/*     */       }
/* 165 */       break;
/*     */     case 115: 
/* 167 */       if (bs.getData() < 3) {
/* 168 */         addGrowBlock("world", growMe.getX(), growMe.getY(), growMe.getZ(), bs.getTypeId(), bs.getData() + 1, false);
/*     */       }
/* 170 */       break;
/*     */     case 104: 
/*     */     case 105: 
/* 173 */       if (bs.getData() < 7) {
/* 174 */         addGrowBlock("world", growMe.getX(), growMe.getY(), growMe.getZ(), bs.getTypeId(), bs.getData() + 1, false);
/* 175 */       } else if (bs.getData() == 7) {
/* 176 */         spawnMelonOrPumpkin(bs, task);
/*     */       }
/* 178 */       break;
/*     */     case 127: 
/* 180 */       if (CivData.canCocoaGrow(bs)) {
/* 181 */         addGrowBlock("world", growMe.getX(), growMe.getY(), growMe.getZ(), bs.getTypeId(), CivData.getNextCocoaValue(bs), false);
/*     */       }
/*     */       break;
/*     */     }
/*     */   }
/*     */   
/*     */   public void processGrowth(CivAsyncTask task) throws InterruptedException
/*     */   {
/* 189 */     if (!getStruct().isActive()) {
/* 190 */       return;
/*     */     }
/*     */     
/* 193 */     if (this.snapshot == null) {
/* 194 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 202 */     double effectiveGrowthRate = this.town.getGrowth().total / 100.0D;
/*     */     
/* 204 */     for (Component comp : getFarm().attachedComponents) {
/* 205 */       if ((comp instanceof ActivateOnBiome)) {
/* 206 */         ActivateOnBiome ab = (ActivateOnBiome)comp;
/* 207 */         if (ab.isValidBiome(this.biomeName)) {
/* 208 */           effectiveGrowthRate += ab.getValue();
/*     */         }
/*     */       }
/*     */     }
/* 212 */     getFarm().setLastEffectiveGrowth(effectiveGrowthRate);
/*     */     
/* 214 */     int crops_per_growth_tick = CivSettings.getIntegerStructure("farm.grows_per_tick");
/* 215 */     int numberOfCropsToGrow = (int)(effectiveGrowthRate * crops_per_growth_tick);
/* 216 */     int chanceForLast = (int)(this.town.getGrowth().total % 100.0D);
/*     */     
/* 218 */     this.lastGrownCrops.clear();
/* 219 */     this.lastGrowTickCount = numberOfCropsToGrow;
/* 220 */     this.lastChanceForLast = chanceForLast;
/* 221 */     Calendar c = Calendar.getInstance();
/* 222 */     this.lastGrowDate = c.getTime();
/* 223 */     this.growBlocks = new LinkedList();
/*     */     
/* 225 */     if (this.cropLocationCache.size() == 0) {
/* 226 */       return;
/*     */     }
/*     */     
/*     */ 
/* 230 */     Random rand = new Random();
/* 231 */     for (int i = 0; i < numberOfCropsToGrow; i++) {
/* 232 */       BlockCoord growMe = (BlockCoord)this.cropLocationCache.get(rand.nextInt(this.cropLocationCache.size()));
/*     */       
/* 234 */       int bsx = growMe.getX() % 16;
/* 235 */       int bsy = growMe.getY();
/* 236 */       int bsz = growMe.getZ() % 16;
/*     */       
/* 238 */       BlockSnapshot bs = new BlockSnapshot(bsx, bsy, bsz, this.snapshot);
/*     */       
/* 240 */       this.lastGrownCrops.add(growMe);
/* 241 */       growBlock(bs, growMe, task);
/*     */     }
/* 243 */     if (chanceForLast != 0) {
/* 244 */       int randInt = rand.nextInt(100);
/* 245 */       this.lastRandomInt = randInt;
/* 246 */       if (randInt < chanceForLast) {
/* 247 */         BlockCoord growMe = (BlockCoord)this.cropLocationCache.get(rand.nextInt(this.cropLocationCache.size()));
/* 248 */         BlockSnapshot bs = new BlockSnapshot(growMe.getX() % 16, growMe.getY(), growMe.getZ() % 16, this.snapshot);
/*     */         
/* 250 */         this.lastGrownCrops.add(growMe);
/* 251 */         growBlock(bs, growMe, task);
/*     */       }
/*     */     }
/*     */     
/* 255 */     task.growBlocks(this.growBlocks, this);
/*     */   }
/*     */   
/*     */   public void processMissedGrowths(boolean populate, CivAsyncTask task) {
/* 259 */     if (this.missedGrowthTicks > 0)
/*     */     {
/* 261 */       if (populate) {
/* 262 */         if (this.snapshot == null) {
/* 263 */           this.snapshot = getChunk().getChunkSnapshot();
/*     */         }
/* 265 */         populateCropLocationCache();
/*     */       }
/*     */       
/* 268 */       for (int i = 0; i < this.missedGrowthTicks; i++) {
/*     */         try {
/* 270 */           processGrowth(task);
/*     */         } catch (InterruptedException e) {
/* 272 */           e.printStackTrace();
/* 273 */           return;
/*     */         }
/*     */       }
/* 276 */       this.missedGrowthTicks = 0;
/*     */     }
/*     */   }
/*     */   
/*     */   public ChunkCoord getCoord() {
/* 281 */     return this.coord;
/*     */   }
/*     */   
/*     */   public void setCoord(ChunkCoord coord) {
/* 285 */     this.coord = coord;
/*     */   }
/*     */   
/*     */   public int getLastGrowTickCount() {
/* 289 */     return this.lastGrowTickCount;
/*     */   }
/*     */   
/*     */   public void setLastGrowTickCount(int lastGrowTickCount) {
/* 293 */     this.lastGrowTickCount = lastGrowTickCount;
/*     */   }
/*     */   
/*     */   public Date getLastGrowDate() {
/* 297 */     return this.lastGrowDate;
/*     */   }
/*     */   
/*     */   public void setLastGrowDate(Date lastGrowDate) {
/* 301 */     this.lastGrowDate = lastGrowDate;
/*     */   }
/*     */   
/*     */   public ArrayList<BlockCoord> getLastGrownCrops() {
/* 305 */     return this.lastGrownCrops;
/*     */   }
/*     */   
/*     */   public void setLastGrownCrops(ArrayList<BlockCoord> lastGrownCrops) {
/* 309 */     this.lastGrownCrops = lastGrownCrops;
/*     */   }
/*     */   
/*     */   public double getLastChanceForLast() {
/* 313 */     return this.lastChanceForLast;
/*     */   }
/*     */   
/*     */   public void setLastChanceForLast(double lastChanceForLast) {
/* 317 */     this.lastChanceForLast = lastChanceForLast;
/*     */   }
/*     */   
/*     */   public int getLastRandomInt() {
/* 321 */     return this.lastRandomInt;
/*     */   }
/*     */   
/*     */   public void setLastRandomInt(int lastRandomInt) {
/* 325 */     this.lastRandomInt = lastRandomInt;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void populateCropLocationCache()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 48	com/avrgaming/civcraft/structure/farm/FarmChunk:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   4: invokevirtual 387	java/util/concurrent/locks/ReentrantLock:lock	()V
/*     */     //   7: aload_0
/*     */     //   8: getfield 43	com/avrgaming/civcraft/structure/farm/FarmChunk:cropLocationCache	Ljava/util/ArrayList;
/*     */     //   11: invokevirtual 285	java/util/ArrayList:clear	()V
/*     */     //   14: new 139	com/avrgaming/civcraft/util/BlockSnapshot
/*     */     //   17: dup
/*     */     //   18: invokespecial 389	com/avrgaming/civcraft/util/BlockSnapshot:<init>	()V
/*     */     //   21: astore_1
/*     */     //   22: iconst_0
/*     */     //   23: istore_2
/*     */     //   24: goto +116 -> 140
/*     */     //   27: iconst_0
/*     */     //   28: istore_3
/*     */     //   29: goto +102 -> 131
/*     */     //   32: iconst_0
/*     */     //   33: istore 4
/*     */     //   35: goto +85 -> 120
/*     */     //   38: aload_1
/*     */     //   39: iload_2
/*     */     //   40: iload 4
/*     */     //   42: iload_3
/*     */     //   43: aload_0
/*     */     //   44: getfield 228	com/avrgaming/civcraft/structure/farm/FarmChunk:snapshot	Lorg/bukkit/ChunkSnapshot;
/*     */     //   47: invokevirtual 390	com/avrgaming/civcraft/util/BlockSnapshot:setFromSnapshotLocation	(IIILorg/bukkit/ChunkSnapshot;)V
/*     */     //   50: aload_1
/*     */     //   51: invokestatic 393	com/avrgaming/civcraft/main/CivData:canGrow	(Lcom/avrgaming/civcraft/util/BlockSnapshot;)Z
/*     */     //   54: ifeq +63 -> 117
/*     */     //   57: aload_0
/*     */     //   58: getfield 43	com/avrgaming/civcraft/structure/farm/FarmChunk:cropLocationCache	Ljava/util/ArrayList;
/*     */     //   61: new 200	com/avrgaming/civcraft/util/BlockCoord
/*     */     //   64: dup
/*     */     //   65: aload_0
/*     */     //   66: getfield 228	com/avrgaming/civcraft/structure/farm/FarmChunk:snapshot	Lorg/bukkit/ChunkSnapshot;
/*     */     //   69: invokeinterface 396 1 0
/*     */     //   74: aload_0
/*     */     //   75: getfield 228	com/avrgaming/civcraft/structure/farm/FarmChunk:snapshot	Lorg/bukkit/ChunkSnapshot;
/*     */     //   78: invokeinterface 401 1 0
/*     */     //   83: iconst_4
/*     */     //   84: ishl
/*     */     //   85: aload_1
/*     */     //   86: invokevirtual 149	com/avrgaming/civcraft/util/BlockSnapshot:getX	()I
/*     */     //   89: iadd
/*     */     //   90: aload_1
/*     */     //   91: invokevirtual 152	com/avrgaming/civcraft/util/BlockSnapshot:getY	()I
/*     */     //   94: aload_0
/*     */     //   95: getfield 228	com/avrgaming/civcraft/structure/farm/FarmChunk:snapshot	Lorg/bukkit/ChunkSnapshot;
/*     */     //   98: invokeinterface 402 1 0
/*     */     //   103: iconst_4
/*     */     //   104: ishl
/*     */     //   105: aload_1
/*     */     //   106: invokevirtual 155	com/avrgaming/civcraft/util/BlockSnapshot:getZ	()I
/*     */     //   109: iadd
/*     */     //   110: invokespecial 403	com/avrgaming/civcraft/util/BlockCoord:<init>	(Ljava/lang/String;III)V
/*     */     //   113: invokevirtual 322	java/util/ArrayList:add	(Ljava/lang/Object;)Z
/*     */     //   116: pop
/*     */     //   117: iinc 4 1
/*     */     //   120: iload 4
/*     */     //   122: sipush 256
/*     */     //   125: if_icmplt -87 -> 38
/*     */     //   128: iinc 3 1
/*     */     //   131: iload_3
/*     */     //   132: bipush 16
/*     */     //   134: if_icmplt -102 -> 32
/*     */     //   137: iinc 2 1
/*     */     //   140: iload_2
/*     */     //   141: bipush 16
/*     */     //   143: if_icmplt -116 -> 27
/*     */     //   146: goto +15 -> 161
/*     */     //   149: astore 5
/*     */     //   151: aload_0
/*     */     //   152: getfield 48	com/avrgaming/civcraft/structure/farm/FarmChunk:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   155: invokevirtual 406	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   158: aload 5
/*     */     //   160: athrow
/*     */     //   161: aload_0
/*     */     //   162: getfield 48	com/avrgaming/civcraft/structure/farm/FarmChunk:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   165: invokevirtual 406	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   168: return
/*     */     // Line number table:
/*     */     //   Java source line #333	-> byte code offset #0
/*     */     //   Java source line #335	-> byte code offset #7
/*     */     //   Java source line #336	-> byte code offset #14
/*     */     //   Java source line #338	-> byte code offset #22
/*     */     //   Java source line #339	-> byte code offset #27
/*     */     //   Java source line #340	-> byte code offset #32
/*     */     //   Java source line #344	-> byte code offset #38
/*     */     //   Java source line #346	-> byte code offset #50
/*     */     //   Java source line #347	-> byte code offset #57
/*     */     //   Java source line #348	-> byte code offset #74
/*     */     //   Java source line #349	-> byte code offset #90
/*     */     //   Java source line #350	-> byte code offset #94
/*     */     //   Java source line #347	-> byte code offset #113
/*     */     //   Java source line #340	-> byte code offset #117
/*     */     //   Java source line #339	-> byte code offset #128
/*     */     //   Java source line #338	-> byte code offset #137
/*     */     //   Java source line #355	-> byte code offset #146
/*     */     //   Java source line #356	-> byte code offset #151
/*     */     //   Java source line #357	-> byte code offset #158
/*     */     //   Java source line #356	-> byte code offset #161
/*     */     //   Java source line #358	-> byte code offset #168
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	169	0	this	FarmChunk
/*     */     //   21	85	1	bs	BlockSnapshot
/*     */     //   23	118	2	x	int
/*     */     //   28	104	3	z	int
/*     */     //   33	88	4	y	int
/*     */     //   149	10	5	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	149	149	finally
/*     */   }
/*     */   
/*     */   public int getMissedGrowthTicks()
/*     */   {
/* 361 */     return this.missedGrowthTicks;
/*     */   }
/*     */   
/*     */   public void setMissedGrowthTicks(int missedGrowthTicks) {
/* 365 */     this.missedGrowthTicks = missedGrowthTicks;
/*     */   }
/*     */   
/*     */   public void incrementMissedGrowthTicks() {
/* 369 */     this.missedGrowthTicks += 1;
/* 370 */     this.missedGrowthTicksStat += 1;
/*     */   }
/*     */   
/*     */   public int getMissedGrowthTicksStat() {
/* 374 */     return this.missedGrowthTicksStat;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\farm\FarmChunk.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
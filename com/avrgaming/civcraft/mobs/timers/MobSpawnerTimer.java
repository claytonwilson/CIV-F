/*    */ package com.avrgaming.civcraft.mobs.timers;
/*    */ 
/*    */ import java.util.LinkedList;
/*    */ import java.util.Queue;
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
/*    */ public class MobSpawnerTimer
/*    */   implements Runnable
/*    */ {
/* 26 */   public static int UPDATE_LIMIT = 40;
/* 27 */   public static int MOB_AREA_LIMIT = 5;
/* 28 */   public static int MOB_AREA = 32;
/*    */   
/* 30 */   public static int MIN_SPAWN_DISTANCE = 20;
/* 31 */   public static int MAX_SPAWN_DISTANCE = 50;
/* 32 */   public static int MIN_SPAWN_AMOUNT = 5;
/*    */   
/* 34 */   public static int Y_SHIFT = 3;
/*    */   
/* 36 */   public static Queue<String> playerQueue = new LinkedList();
/*    */   
/*    */   /* Error */
/*    */   public void run()
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aconst_null
/*    */     //   1: astore_1
/*    */     //   2: iconst_0
/*    */     //   3: istore_2
/*    */     //   4: goto +383 -> 387
/*    */     //   7: getstatic 41	com/avrgaming/civcraft/mobs/timers/MobSpawnerTimer:playerQueue	Ljava/util/Queue;
/*    */     //   10: invokeinterface 49 1 0
/*    */     //   15: checkcast 55	java/lang/String
/*    */     //   18: astore_1
/*    */     //   19: aload_1
/*    */     //   20: ifnonnull +18 -> 38
/*    */     //   23: aload_1
/*    */     //   24: ifnull +13 -> 37
/*    */     //   27: getstatic 41	com/avrgaming/civcraft/mobs/timers/MobSpawnerTimer:playerQueue	Ljava/util/Queue;
/*    */     //   30: aload_1
/*    */     //   31: invokeinterface 57 2 0
/*    */     //   36: pop
/*    */     //   37: return
/*    */     //   38: aload_1
/*    */     //   39: invokestatic 61	com/avrgaming/civcraft/main/CivGlobal:getPlayer	(Ljava/lang/String;)Lorg/bukkit/entity/Player;
/*    */     //   42: astore_3
/*    */     //   43: aload_3
/*    */     //   44: invokeinterface 67 1 0
/*    */     //   49: astore 4
/*    */     //   51: aload 4
/*    */     //   53: invokeinterface 73 1 0
/*    */     //   58: ifne +20 -> 78
/*    */     //   61: aload_1
/*    */     //   62: ifnull +322 -> 384
/*    */     //   65: getstatic 41	com/avrgaming/civcraft/mobs/timers/MobSpawnerTimer:playerQueue	Ljava/util/Queue;
/*    */     //   68: aload_1
/*    */     //   69: invokeinterface 57 2 0
/*    */     //   74: pop
/*    */     //   75: goto +309 -> 384
/*    */     //   78: iconst_0
/*    */     //   79: istore 5
/*    */     //   81: goto +241 -> 322
/*    */     //   84: new 79	java/util/Random
/*    */     //   87: dup
/*    */     //   88: invokespecial 81	java/util/Random:<init>	()V
/*    */     //   91: astore 6
/*    */     //   93: aload 6
/*    */     //   95: getstatic 30	com/avrgaming/civcraft/mobs/timers/MobSpawnerTimer:MAX_SPAWN_DISTANCE	I
/*    */     //   98: invokevirtual 82	java/util/Random:nextInt	(I)I
/*    */     //   101: getstatic 28	com/avrgaming/civcraft/mobs/timers/MobSpawnerTimer:MIN_SPAWN_DISTANCE	I
/*    */     //   104: iadd
/*    */     //   105: istore 7
/*    */     //   107: aload 6
/*    */     //   109: invokevirtual 86	java/util/Random:nextBoolean	()Z
/*    */     //   112: ifeq +9 -> 121
/*    */     //   115: iload 7
/*    */     //   117: iconst_m1
/*    */     //   118: imul
/*    */     //   119: istore 7
/*    */     //   121: aload 6
/*    */     //   123: getstatic 30	com/avrgaming/civcraft/mobs/timers/MobSpawnerTimer:MAX_SPAWN_DISTANCE	I
/*    */     //   126: invokevirtual 82	java/util/Random:nextInt	(I)I
/*    */     //   129: getstatic 28	com/avrgaming/civcraft/mobs/timers/MobSpawnerTimer:MIN_SPAWN_DISTANCE	I
/*    */     //   132: iadd
/*    */     //   133: istore 8
/*    */     //   135: aload 6
/*    */     //   137: invokevirtual 86	java/util/Random:nextBoolean	()Z
/*    */     //   140: ifeq +9 -> 149
/*    */     //   143: iload 8
/*    */     //   145: iconst_m1
/*    */     //   146: imul
/*    */     //   147: istore 8
/*    */     //   149: aload 4
/*    */     //   151: iload 7
/*    */     //   153: iload 8
/*    */     //   155: invokeinterface 89 3 0
/*    */     //   160: istore 9
/*    */     //   162: new 93	org/bukkit/Location
/*    */     //   165: dup
/*    */     //   166: aload_3
/*    */     //   167: invokeinterface 95 1 0
/*    */     //   172: invokevirtual 99	org/bukkit/Location:getWorld	()Lorg/bukkit/World;
/*    */     //   175: aload_3
/*    */     //   176: invokeinterface 95 1 0
/*    */     //   181: invokevirtual 100	org/bukkit/Location:getX	()D
/*    */     //   184: iload 7
/*    */     //   186: i2d
/*    */     //   187: dadd
/*    */     //   188: iload 9
/*    */     //   190: i2d
/*    */     //   191: aload_3
/*    */     //   192: invokeinterface 95 1 0
/*    */     //   197: invokevirtual 104	org/bukkit/Location:getZ	()D
/*    */     //   200: iload 8
/*    */     //   202: i2d
/*    */     //   203: dadd
/*    */     //   204: invokespecial 107	org/bukkit/Location:<init>	(Lorg/bukkit/World;DDD)V
/*    */     //   207: astore 10
/*    */     //   209: aload 10
/*    */     //   211: invokevirtual 110	org/bukkit/Location:getChunk	()Lorg/bukkit/Chunk;
/*    */     //   214: invokeinterface 114 1 0
/*    */     //   219: ifne +6 -> 225
/*    */     //   222: goto +97 -> 319
/*    */     //   225: new 119	com/avrgaming/civcraft/util/ChunkCoord
/*    */     //   228: dup
/*    */     //   229: aload 10
/*    */     //   231: invokespecial 121	com/avrgaming/civcraft/util/ChunkCoord:<init>	(Lorg/bukkit/Location;)V
/*    */     //   234: invokestatic 124	com/avrgaming/civcraft/main/CivGlobal:getTownChunk	(Lcom/avrgaming/civcraft/util/ChunkCoord;)Lcom/avrgaming/civcraft/object/TownChunk;
/*    */     //   237: astore 11
/*    */     //   239: aload 11
/*    */     //   241: ifnull +6 -> 247
/*    */     //   244: goto +75 -> 319
/*    */     //   247: aload 10
/*    */     //   249: invokevirtual 128	org/bukkit/Location:getBlock	()Lorg/bukkit/block/Block;
/*    */     //   252: getstatic 132	org/bukkit/block/BlockFace:DOWN	Lorg/bukkit/block/BlockFace;
/*    */     //   255: invokeinterface 138 2 0
/*    */     //   260: invokestatic 144	com/avrgaming/civcraft/util/ItemManager:getId	(Lorg/bukkit/block/Block;)I
/*    */     //   263: bipush 9
/*    */     //   265: if_icmpne +6 -> 271
/*    */     //   268: goto +51 -> 319
/*    */     //   271: aload 10
/*    */     //   273: aload 10
/*    */     //   275: invokevirtual 150	org/bukkit/Location:getY	()D
/*    */     //   278: getstatic 34	com/avrgaming/civcraft/mobs/timers/MobSpawnerTimer:Y_SHIFT	I
/*    */     //   281: i2d
/*    */     //   282: dadd
/*    */     //   283: invokevirtual 153	org/bukkit/Location:setY	(D)V
/*    */     //   286: aconst_null
/*    */     //   287: aload 10
/*    */     //   289: getstatic 26	com/avrgaming/civcraft/mobs/timers/MobSpawnerTimer:MOB_AREA	I
/*    */     //   292: i2d
/*    */     //   293: ldc -99
/*    */     //   295: invokestatic 159	com/avrgaming/civcraft/util/EntityProximity:getNearbyEntities	(Lorg/bukkit/entity/Entity;Lorg/bukkit/Location;DLjava/lang/Class;)Ljava/util/LinkedList;
/*    */     //   298: astore 12
/*    */     //   300: aload 12
/*    */     //   302: invokevirtual 165	java/util/LinkedList:size	()I
/*    */     //   305: getstatic 24	com/avrgaming/civcraft/mobs/timers/MobSpawnerTimer:MOB_AREA_LIMIT	I
/*    */     //   308: if_icmple +6 -> 314
/*    */     //   311: goto +8 -> 319
/*    */     //   314: aload 10
/*    */     //   316: invokestatic 169	com/avrgaming/civcraft/mobs/MobSpawner:spawnRandomCustomMob	(Lorg/bukkit/Location;)V
/*    */     //   319: iinc 5 1
/*    */     //   322: iload 5
/*    */     //   324: getstatic 32	com/avrgaming/civcraft/mobs/timers/MobSpawnerTimer:MIN_SPAWN_AMOUNT	I
/*    */     //   327: if_icmplt -243 -> 84
/*    */     //   330: aload_1
/*    */     //   331: ifnull +63 -> 394
/*    */     //   334: getstatic 41	com/avrgaming/civcraft/mobs/timers/MobSpawnerTimer:playerQueue	Ljava/util/Queue;
/*    */     //   337: aload_1
/*    */     //   338: invokeinterface 57 2 0
/*    */     //   343: pop
/*    */     //   344: goto +50 -> 394
/*    */     //   347: astore_3
/*    */     //   348: aload_1
/*    */     //   349: ifnull +35 -> 384
/*    */     //   352: getstatic 41	com/avrgaming/civcraft/mobs/timers/MobSpawnerTimer:playerQueue	Ljava/util/Queue;
/*    */     //   355: aload_1
/*    */     //   356: invokeinterface 57 2 0
/*    */     //   361: pop
/*    */     //   362: goto +22 -> 384
/*    */     //   365: astore 13
/*    */     //   367: aload_1
/*    */     //   368: ifnull +13 -> 381
/*    */     //   371: getstatic 41	com/avrgaming/civcraft/mobs/timers/MobSpawnerTimer:playerQueue	Ljava/util/Queue;
/*    */     //   374: aload_1
/*    */     //   375: invokeinterface 57 2 0
/*    */     //   380: pop
/*    */     //   381: aload 13
/*    */     //   383: athrow
/*    */     //   384: iinc 2 1
/*    */     //   387: iload_2
/*    */     //   388: getstatic 22	com/avrgaming/civcraft/mobs/timers/MobSpawnerTimer:UPDATE_LIMIT	I
/*    */     //   391: if_icmplt -384 -> 7
/*    */     //   394: return
/*    */     // Line number table:
/*    */     //   Java source line #40	-> byte code offset #0
/*    */     //   Java source line #42	-> byte code offset #2
/*    */     //   Java source line #45	-> byte code offset #7
/*    */     //   Java source line #46	-> byte code offset #19
/*    */     //   Java source line #101	-> byte code offset #23
/*    */     //   Java source line #103	-> byte code offset #27
/*    */     //   Java source line #48	-> byte code offset #37
/*    */     //   Java source line #51	-> byte code offset #38
/*    */     //   Java source line #52	-> byte code offset #43
/*    */     //   Java source line #53	-> byte code offset #51
/*    */     //   Java source line #101	-> byte code offset #61
/*    */     //   Java source line #103	-> byte code offset #65
/*    */     //   Java source line #54	-> byte code offset #75
/*    */     //   Java source line #57	-> byte code offset #78
/*    */     //   Java source line #58	-> byte code offset #84
/*    */     //   Java source line #59	-> byte code offset #93
/*    */     //   Java source line #60	-> byte code offset #107
/*    */     //   Java source line #61	-> byte code offset #115
/*    */     //   Java source line #64	-> byte code offset #121
/*    */     //   Java source line #65	-> byte code offset #135
/*    */     //   Java source line #66	-> byte code offset #143
/*    */     //   Java source line #69	-> byte code offset #149
/*    */     //   Java source line #70	-> byte code offset #162
/*    */     //   Java source line #71	-> byte code offset #188
/*    */     //   Java source line #70	-> byte code offset #204
/*    */     //   Java source line #72	-> byte code offset #209
/*    */     //   Java source line #73	-> byte code offset #222
/*    */     //   Java source line #77	-> byte code offset #225
/*    */     //   Java source line #78	-> byte code offset #239
/*    */     //   Java source line #80	-> byte code offset #244
/*    */     //   Java source line #83	-> byte code offset #247
/*    */     //   Java source line #85	-> byte code offset #268
/*    */     //   Java source line #88	-> byte code offset #271
/*    */     //   Java source line #89	-> byte code offset #286
/*    */     //   Java source line #90	-> byte code offset #300
/*    */     //   Java source line #92	-> byte code offset #311
/*    */     //   Java source line #95	-> byte code offset #314
/*    */     //   Java source line #57	-> byte code offset #319
/*    */     //   Java source line #101	-> byte code offset #330
/*    */     //   Java source line #103	-> byte code offset #334
/*    */     //   Java source line #97	-> byte code offset #344
/*    */     //   Java source line #98	-> byte code offset #347
/*    */     //   Java source line #101	-> byte code offset #348
/*    */     //   Java source line #103	-> byte code offset #352
/*    */     //   Java source line #100	-> byte code offset #365
/*    */     //   Java source line #101	-> byte code offset #367
/*    */     //   Java source line #103	-> byte code offset #371
/*    */     //   Java source line #105	-> byte code offset #381
/*    */     //   Java source line #42	-> byte code offset #384
/*    */     //   Java source line #107	-> byte code offset #394
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	395	0	this	MobSpawnerTimer
/*    */     //   1	374	1	name	String
/*    */     //   3	385	2	i	int
/*    */     //   42	150	3	player	org.bukkit.entity.Player
/*    */     //   347	1	3	localCivException	com.avrgaming.civcraft.exception.CivException
/*    */     //   49	101	4	world	org.bukkit.World
/*    */     //   79	244	5	j	int
/*    */     //   91	45	6	random	java.util.Random
/*    */     //   105	80	7	x	int
/*    */     //   133	68	8	z	int
/*    */     //   160	29	9	y	int
/*    */     //   207	108	10	loc	org.bukkit.Location
/*    */     //   237	3	11	tc	com.avrgaming.civcraft.object.TownChunk
/*    */     //   298	3	12	entities	LinkedList<org.bukkit.entity.Entity>
/*    */     //   365	17	13	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   7	23	347	com/avrgaming/civcraft/exception/CivException
/*    */     //   38	61	347	com/avrgaming/civcraft/exception/CivException
/*    */     //   78	330	347	com/avrgaming/civcraft/exception/CivException
/*    */     //   7	23	365	finally
/*    */     //   38	61	365	finally
/*    */     //   78	330	365	finally
/*    */     //   347	348	365	finally
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\mobs\timers\MobSpawnerTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
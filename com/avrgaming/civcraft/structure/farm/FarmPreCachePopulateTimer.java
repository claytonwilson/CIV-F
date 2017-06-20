/*    */ package com.avrgaming.civcraft.structure.farm;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FarmPreCachePopulateTimer
/*    */   implements Runnable
/*    */ {
/* 32 */   public static int updateLimit = 50;
/* 33 */   public static ReentrantLock lock = new ReentrantLock();
/*    */   
/*    */   /* Error */
/*    */   public void run()
/*    */   {
/*    */     // Byte code:
/*    */     //   0: getstatic 29	com/avrgaming/civcraft/main/CivGlobal:growthEnabled	Z
/*    */     //   3: ifne +4 -> 7
/*    */     //   6: return
/*    */     //   7: getstatic 21	com/avrgaming/civcraft/structure/farm/FarmPreCachePopulateTimer:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   10: invokevirtual 35	java/util/concurrent/locks/ReentrantLock:tryLock	()Z
/*    */     //   13: ifeq +152 -> 165
/*    */     //   16: new 39	java/util/LinkedList
/*    */     //   19: dup
/*    */     //   20: invokespecial 41	java/util/LinkedList:<init>	()V
/*    */     //   23: astore_1
/*    */     //   24: iconst_0
/*    */     //   25: istore_2
/*    */     //   26: goto +59 -> 85
/*    */     //   29: invokestatic 42	com/avrgaming/civcraft/main/CivGlobal:pollFarmChunk	()Lcom/avrgaming/civcraft/structure/farm/FarmChunk;
/*    */     //   32: astore_3
/*    */     //   33: aload_3
/*    */     //   34: ifnonnull +6 -> 40
/*    */     //   37: goto +55 -> 92
/*    */     //   40: aload_3
/*    */     //   41: invokevirtual 46	com/avrgaming/civcraft/structure/farm/FarmChunk:getChunk	()Lorg/bukkit/Chunk;
/*    */     //   44: astore 4
/*    */     //   46: aload_3
/*    */     //   47: invokestatic 52	com/avrgaming/civcraft/main/CivGlobal:farmChunkValid	(Lcom/avrgaming/civcraft/structure/farm/FarmChunk;)Z
/*    */     //   50: ifeq +32 -> 82
/*    */     //   53: aload 4
/*    */     //   55: invokeinterface 56 1 0
/*    */     //   60: ifeq +22 -> 82
/*    */     //   63: aload_1
/*    */     //   64: aload_3
/*    */     //   65: invokevirtual 61	java/util/LinkedList:add	(Ljava/lang/Object;)Z
/*    */     //   68: pop
/*    */     //   69: aload_3
/*    */     //   70: aload_3
/*    */     //   71: invokevirtual 46	com/avrgaming/civcraft/structure/farm/FarmChunk:getChunk	()Lorg/bukkit/Chunk;
/*    */     //   74: invokeinterface 65 1 0
/*    */     //   79: putfield 69	com/avrgaming/civcraft/structure/farm/FarmChunk:snapshot	Lorg/bukkit/ChunkSnapshot;
/*    */     //   82: iinc 2 1
/*    */     //   85: iload_2
/*    */     //   86: getstatic 14	com/avrgaming/civcraft/structure/farm/FarmPreCachePopulateTimer:updateLimit	I
/*    */     //   89: if_icmplt -60 -> 29
/*    */     //   92: aload_1
/*    */     //   93: invokevirtual 73	java/util/LinkedList:iterator	()Ljava/util/Iterator;
/*    */     //   96: astore_3
/*    */     //   97: goto +17 -> 114
/*    */     //   100: aload_3
/*    */     //   101: invokeinterface 77 1 0
/*    */     //   106: checkcast 47	com/avrgaming/civcraft/structure/farm/FarmChunk
/*    */     //   109: astore_2
/*    */     //   110: aload_2
/*    */     //   111: invokestatic 83	com/avrgaming/civcraft/main/CivGlobal:queueFarmChunk	(Lcom/avrgaming/civcraft/structure/farm/FarmChunk;)V
/*    */     //   114: aload_3
/*    */     //   115: invokeinterface 87 1 0
/*    */     //   120: ifne -20 -> 100
/*    */     //   123: aload_1
/*    */     //   124: invokevirtual 90	java/util/LinkedList:size	()I
/*    */     //   127: ifle +29 -> 156
/*    */     //   130: new 94	com/avrgaming/civcraft/structure/farm/FarmCachePopulateTask
/*    */     //   133: dup
/*    */     //   134: aload_1
/*    */     //   135: invokespecial 96	com/avrgaming/civcraft/structure/farm/FarmCachePopulateTask:<init>	(Ljava/util/LinkedList;)V
/*    */     //   138: lconst_0
/*    */     //   139: invokestatic 99	com/avrgaming/civcraft/threading/TaskMaster:asyncTask	(Ljava/lang/Runnable;J)V
/*    */     //   142: goto +14 -> 156
/*    */     //   145: astore 5
/*    */     //   147: getstatic 21	com/avrgaming/civcraft/structure/farm/FarmPreCachePopulateTimer:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   150: invokevirtual 105	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*    */     //   153: aload 5
/*    */     //   155: athrow
/*    */     //   156: getstatic 21	com/avrgaming/civcraft/structure/farm/FarmPreCachePopulateTimer:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   159: invokevirtual 105	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*    */     //   162: goto +8 -> 170
/*    */     //   165: ldc 108
/*    */     //   167: invokestatic 110	com/avrgaming/civcraft/main/CivLog:warning	(Ljava/lang/String;)V
/*    */     //   170: return
/*    */     // Line number table:
/*    */     //   Java source line #44	-> byte code offset #0
/*    */     //   Java source line #45	-> byte code offset #6
/*    */     //   Java source line #48	-> byte code offset #7
/*    */     //   Java source line #50	-> byte code offset #16
/*    */     //   Java source line #52	-> byte code offset #24
/*    */     //   Java source line #53	-> byte code offset #29
/*    */     //   Java source line #54	-> byte code offset #33
/*    */     //   Java source line #55	-> byte code offset #37
/*    */     //   Java source line #62	-> byte code offset #40
/*    */     //   Java source line #63	-> byte code offset #46
/*    */     //   Java source line #64	-> byte code offset #63
/*    */     //   Java source line #65	-> byte code offset #69
/*    */     //   Java source line #52	-> byte code offset #82
/*    */     //   Java source line #69	-> byte code offset #92
/*    */     //   Java source line #73	-> byte code offset #110
/*    */     //   Java source line #69	-> byte code offset #114
/*    */     //   Java source line #76	-> byte code offset #123
/*    */     //   Java source line #77	-> byte code offset #130
/*    */     //   Java source line #79	-> byte code offset #142
/*    */     //   Java source line #80	-> byte code offset #147
/*    */     //   Java source line #81	-> byte code offset #153
/*    */     //   Java source line #80	-> byte code offset #156
/*    */     //   Java source line #82	-> byte code offset #162
/*    */     //   Java source line #83	-> byte code offset #165
/*    */     //   Java source line #85	-> byte code offset #170
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	171	0	this	FarmPreCachePopulateTimer
/*    */     //   23	112	1	farms	java.util.LinkedList<FarmChunk>
/*    */     //   25	61	2	i	int
/*    */     //   109	2	2	fc	FarmChunk
/*    */     //   32	83	3	fc	FarmChunk
/*    */     //   44	10	4	chunk	org.bukkit.Chunk
/*    */     //   145	9	5	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   16	145	145	finally
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\farm\FarmPreCachePopulateTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
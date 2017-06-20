/*    */ package com.avrgaming.civcraft.threading.sync;
/*    */ 
/*    */ import com.avrgaming.civcraft.util.SimpleBlock;
/*    */ import java.util.LinkedList;
/*    */ import java.util.Queue;
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
/*    */ 
/*    */ 
/*    */ public class SyncBuildUpdateTask
/*    */   implements Runnable
/*    */ {
/* 37 */   public static int UPDATE_LIMIT = Integer.MAX_VALUE;
/*    */   
/*    */   public static final int QUEUE_SIZE = 4096;
/*    */   
/* 41 */   private static Queue<SimpleBlock> updateBlocks = new LinkedList();
/*    */   
/*    */ 
/* 44 */   public static ReentrantLock buildBlockLock = new ReentrantLock();
/*    */   
/*    */   /* Error */
/*    */   public static void queueSimpleBlock(Queue<SimpleBlock> sbList)
/*    */   {
/*    */     // Byte code:
/*    */     //   0: getstatic 36	com/avrgaming/civcraft/threading/sync/SyncBuildUpdateTask:buildBlockLock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   3: invokevirtual 43	java/util/concurrent/locks/ReentrantLock:lock	()V
/*    */     //   6: getstatic 31	com/avrgaming/civcraft/threading/sync/SyncBuildUpdateTask:updateBlocks	Ljava/util/Queue;
/*    */     //   9: aload_0
/*    */     //   10: invokeinterface 46 2 0
/*    */     //   15: pop
/*    */     //   16: goto +12 -> 28
/*    */     //   19: astore_1
/*    */     //   20: getstatic 36	com/avrgaming/civcraft/threading/sync/SyncBuildUpdateTask:buildBlockLock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   23: invokevirtual 52	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*    */     //   26: aload_1
/*    */     //   27: athrow
/*    */     //   28: getstatic 36	com/avrgaming/civcraft/threading/sync/SyncBuildUpdateTask:buildBlockLock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   31: invokevirtual 52	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*    */     //   34: return
/*    */     // Line number table:
/*    */     //   Java source line #47	-> byte code offset #0
/*    */     //   Java source line #49	-> byte code offset #6
/*    */     //   Java source line #50	-> byte code offset #16
/*    */     //   Java source line #51	-> byte code offset #20
/*    */     //   Java source line #52	-> byte code offset #26
/*    */     //   Java source line #51	-> byte code offset #28
/*    */     //   Java source line #53	-> byte code offset #34
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	35	0	sbList	Queue<SimpleBlock>
/*    */     //   19	8	1	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   6	19	19	finally
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public void run()
/*    */   {
/*    */     // Byte code:
/*    */     //   0: getstatic 36	com/avrgaming/civcraft/threading/sync/SyncBuildUpdateTask:buildBlockLock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   3: invokevirtual 64	java/util/concurrent/locks/ReentrantLock:tryLock	()Z
/*    */     //   6: ifeq +225 -> 231
/*    */     //   9: iconst_0
/*    */     //   10: istore_1
/*    */     //   11: iconst_0
/*    */     //   12: istore_1
/*    */     //   13: goto +188 -> 201
/*    */     //   16: getstatic 31	com/avrgaming/civcraft/threading/sync/SyncBuildUpdateTask:updateBlocks	Ljava/util/Queue;
/*    */     //   19: invokeinterface 68 1 0
/*    */     //   24: checkcast 72	com/avrgaming/civcraft/util/SimpleBlock
/*    */     //   27: astore_2
/*    */     //   28: aload_2
/*    */     //   29: ifnonnull +6 -> 35
/*    */     //   32: goto +190 -> 222
/*    */     //   35: aload_2
/*    */     //   36: getfield 74	com/avrgaming/civcraft/util/SimpleBlock:worldname	Ljava/lang/String;
/*    */     //   39: invokestatic 78	org/bukkit/Bukkit:getWorld	(Ljava/lang/String;)Lorg/bukkit/World;
/*    */     //   42: aload_2
/*    */     //   43: getfield 84	com/avrgaming/civcraft/util/SimpleBlock:x	I
/*    */     //   46: aload_2
/*    */     //   47: getfield 87	com/avrgaming/civcraft/util/SimpleBlock:y	I
/*    */     //   50: aload_2
/*    */     //   51: getfield 90	com/avrgaming/civcraft/util/SimpleBlock:z	I
/*    */     //   54: invokeinterface 93 4 0
/*    */     //   59: astore_3
/*    */     //   60: aload_3
/*    */     //   61: aload_2
/*    */     //   62: invokevirtual 99	com/avrgaming/civcraft/util/SimpleBlock:getType	()I
/*    */     //   65: invokestatic 103	com/avrgaming/civcraft/util/ItemManager:setTypeId	(Lorg/bukkit/block/Block;I)V
/*    */     //   68: aload_3
/*    */     //   69: aload_2
/*    */     //   70: invokevirtual 109	com/avrgaming/civcraft/util/SimpleBlock:getData	()I
/*    */     //   73: invokestatic 112	com/avrgaming/civcraft/util/ItemManager:setData	(Lorg/bukkit/block/Block;I)V
/*    */     //   76: invokestatic 115	com/avrgaming/civcraft/threading/sync/SyncBuildUpdateTask:$SWITCH_TABLE$com$avrgaming$civcraft$util$SimpleBlock$Type	()[I
/*    */     //   79: aload_2
/*    */     //   80: getfield 118	com/avrgaming/civcraft/util/SimpleBlock:specialType	Lcom/avrgaming/civcraft/util/SimpleBlock$Type;
/*    */     //   83: invokevirtual 122	com/avrgaming/civcraft/util/SimpleBlock$Type:ordinal	()I
/*    */     //   86: iaload
/*    */     //   87: tableswitch	default:+91->178, 1:+91->178, 2:+25->112, 3:+38->125
/*    */     //   112: aload_3
/*    */     //   113: iconst_0
/*    */     //   114: invokestatic 103	com/avrgaming/civcraft/util/ItemManager:setTypeId	(Lorg/bukkit/block/Block;I)V
/*    */     //   117: aload_3
/*    */     //   118: iconst_0
/*    */     //   119: invokestatic 112	com/avrgaming/civcraft/util/ItemManager:setData	(Lorg/bukkit/block/Block;I)V
/*    */     //   122: goto +56 -> 178
/*    */     //   125: aload_3
/*    */     //   126: invokeinterface 127 1 0
/*    */     //   131: checkcast 133	org/bukkit/block/Sign
/*    */     //   134: astore 4
/*    */     //   136: iconst_0
/*    */     //   137: istore 5
/*    */     //   139: goto +22 -> 161
/*    */     //   142: aload 4
/*    */     //   144: iload 5
/*    */     //   146: aload_2
/*    */     //   147: getfield 135	com/avrgaming/civcraft/util/SimpleBlock:message	[Ljava/lang/String;
/*    */     //   150: iload 5
/*    */     //   152: aaload
/*    */     //   153: invokeinterface 139 3 0
/*    */     //   158: iinc 5 1
/*    */     //   161: iload 5
/*    */     //   163: iconst_4
/*    */     //   164: if_icmplt -22 -> 142
/*    */     //   167: aload 4
/*    */     //   169: invokeinterface 143 1 0
/*    */     //   174: pop
/*    */     //   175: goto +3 -> 178
/*    */     //   178: aload_2
/*    */     //   179: getfield 146	com/avrgaming/civcraft/util/SimpleBlock:buildable	Lcom/avrgaming/civcraft/structure/Buildable;
/*    */     //   182: ifnull +16 -> 198
/*    */     //   185: aload_2
/*    */     //   186: getfield 146	com/avrgaming/civcraft/util/SimpleBlock:buildable	Lcom/avrgaming/civcraft/structure/Buildable;
/*    */     //   189: dup
/*    */     //   190: getfield 150	com/avrgaming/civcraft/structure/Buildable:savedBlockCount	I
/*    */     //   193: iconst_1
/*    */     //   194: iadd
/*    */     //   195: putfield 150	com/avrgaming/civcraft/structure/Buildable:savedBlockCount	I
/*    */     //   198: iinc 1 1
/*    */     //   201: iload_1
/*    */     //   202: getstatic 24	com/avrgaming/civcraft/threading/sync/SyncBuildUpdateTask:UPDATE_LIMIT	I
/*    */     //   205: if_icmplt -189 -> 16
/*    */     //   208: goto +14 -> 222
/*    */     //   211: astore 6
/*    */     //   213: getstatic 36	com/avrgaming/civcraft/threading/sync/SyncBuildUpdateTask:buildBlockLock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   216: invokevirtual 52	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*    */     //   219: aload 6
/*    */     //   221: athrow
/*    */     //   222: getstatic 36	com/avrgaming/civcraft/threading/sync/SyncBuildUpdateTask:buildBlockLock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   225: invokevirtual 52	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*    */     //   228: goto +8 -> 236
/*    */     //   231: ldc -101
/*    */     //   233: invokestatic 157	com/avrgaming/civcraft/main/CivLog:warning	(Ljava/lang/String;)V
/*    */     //   236: return
/*    */     // Line number table:
/*    */     //   Java source line #65	-> byte code offset #0
/*    */     //   Java source line #68	-> byte code offset #9
/*    */     //   Java source line #69	-> byte code offset #11
/*    */     //   Java source line #70	-> byte code offset #16
/*    */     //   Java source line #71	-> byte code offset #28
/*    */     //   Java source line #72	-> byte code offset #32
/*    */     //   Java source line #75	-> byte code offset #35
/*    */     //   Java source line #76	-> byte code offset #60
/*    */     //   Java source line #77	-> byte code offset #68
/*    */     //   Java source line #81	-> byte code offset #76
/*    */     //   Java source line #81	-> byte code offset #79
/*    */     //   Java source line #83	-> byte code offset #112
/*    */     //   Java source line #84	-> byte code offset #117
/*    */     //   Java source line #85	-> byte code offset #122
/*    */     //   Java source line #87	-> byte code offset #125
/*    */     //   Java source line #88	-> byte code offset #136
/*    */     //   Java source line #89	-> byte code offset #142
/*    */     //   Java source line #88	-> byte code offset #158
/*    */     //   Java source line #92	-> byte code offset #167
/*    */     //   Java source line #93	-> byte code offset #175
/*    */     //   Java source line #96	-> byte code offset #178
/*    */     //   Java source line #98	-> byte code offset #179
/*    */     //   Java source line #99	-> byte code offset #185
/*    */     //   Java source line #69	-> byte code offset #198
/*    */     //   Java source line #102	-> byte code offset #208
/*    */     //   Java source line #103	-> byte code offset #213
/*    */     //   Java source line #104	-> byte code offset #219
/*    */     //   Java source line #103	-> byte code offset #222
/*    */     //   Java source line #105	-> byte code offset #228
/*    */     //   Java source line #106	-> byte code offset #231
/*    */     //   Java source line #108	-> byte code offset #236
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	237	0	this	SyncBuildUpdateTask
/*    */     //   10	192	1	i	int
/*    */     //   27	159	2	next	SimpleBlock
/*    */     //   59	67	3	block	org.bukkit.block.Block
/*    */     //   134	34	4	s	org.bukkit.block.Sign
/*    */     //   137	25	5	j	int
/*    */     //   211	9	6	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   9	211	211	finally
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\sync\SyncBuildUpdateTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
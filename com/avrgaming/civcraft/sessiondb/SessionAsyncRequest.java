/*    */ package com.avrgaming.civcraft.sessiondb;
/*    */ 
/*    */ public class SessionAsyncRequest
/*    */ {
/*    */   String tb_prefix;
/*    */   public SessionEntry entry;
/*    */   public Operation op;
/*    */   public Database database;
/*    */   
/*    */   public static enum Operation
/*    */   {
/* 12 */     ADD, 
/* 13 */     DELETE, 
/* 14 */     DELETE_ALL, 
/* 15 */     UPDATE, 
/* 16 */     UPDATE_INSERT;
/*    */   }
/*    */   
/*    */   public static enum Database {
/* 20 */     GAME, 
/* 21 */     GLOBAL;
/*    */   }
/*    */   
/*    */   public SessionAsyncRequest(Operation op, Database data, String prefix, SessionEntry entry) {
/* 25 */     this.op = op;
/* 26 */     this.database = data;
/* 27 */     this.tb_prefix = prefix;
/* 28 */     this.entry = entry;
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public void queue()
/*    */   {
/*    */     // Byte code:
/*    */     //   0: getstatic 34	com/avrgaming/civcraft/sessiondb/SessionDBAsyncTimer:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   3: invokevirtual 40	java/util/concurrent/locks/ReentrantLock:tryLock	()Z
/*    */     //   6: ifeq +34 -> 40
/*    */     //   9: getstatic 46	com/avrgaming/civcraft/sessiondb/SessionDBAsyncTimer:requestQueue	Ljava/util/Queue;
/*    */     //   12: aload_0
/*    */     //   13: invokeinterface 50 2 0
/*    */     //   18: pop
/*    */     //   19: goto +12 -> 31
/*    */     //   22: astore_1
/*    */     //   23: getstatic 34	com/avrgaming/civcraft/sessiondb/SessionDBAsyncTimer:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   26: invokevirtual 56	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*    */     //   29: aload_1
/*    */     //   30: athrow
/*    */     //   31: getstatic 34	com/avrgaming/civcraft/sessiondb/SessionDBAsyncTimer:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   34: invokevirtual 56	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*    */     //   37: goto +16 -> 53
/*    */     //   40: new 59	com/avrgaming/civcraft/sessiondb/SessionAsyncRequest$1AsyncSessionDBRequestWaitTask
/*    */     //   43: dup
/*    */     //   44: aload_0
/*    */     //   45: aload_0
/*    */     //   46: invokespecial 61	com/avrgaming/civcraft/sessiondb/SessionAsyncRequest$1AsyncSessionDBRequestWaitTask:<init>	(Lcom/avrgaming/civcraft/sessiondb/SessionAsyncRequest;Lcom/avrgaming/civcraft/sessiondb/SessionAsyncRequest;)V
/*    */     //   49: lconst_0
/*    */     //   50: invokestatic 64	com/avrgaming/civcraft/threading/TaskMaster:asyncTask	(Ljava/lang/Runnable;J)V
/*    */     //   53: return
/*    */     // Line number table:
/*    */     //   Java source line #32	-> byte code offset #0
/*    */     //   Java source line #34	-> byte code offset #9
/*    */     //   Java source line #35	-> byte code offset #19
/*    */     //   Java source line #36	-> byte code offset #23
/*    */     //   Java source line #37	-> byte code offset #29
/*    */     //   Java source line #36	-> byte code offset #31
/*    */     //   Java source line #38	-> byte code offset #37
/*    */     //   Java source line #58	-> byte code offset #40
/*    */     //   Java source line #60	-> byte code offset #53
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	54	0	this	SessionAsyncRequest
/*    */     //   22	8	1	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   9	22	22	finally
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\sessiondb\SessionAsyncRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
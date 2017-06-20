/*    */ package com.avrgaming.civcraft.database;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import com.avrgaming.civcraft.object.SQLObject;
/*    */ import java.util.LinkedList;
/*    */ import java.util.Queue;
/*    */ import java.util.concurrent.TimeUnit;
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
/*    */ public class SQLUpdate
/*    */   implements Runnable
/*    */ {
/*    */   public static final int UPDATE_LIMIT = 50;
/* 35 */   public static ReentrantLock lock = new ReentrantLock();
/*    */   
/* 37 */   private static Queue<SQLObject> saveObjects = new LinkedList();
/*    */   
/*    */   /* Error */
/*    */   public static void add(SQLObject obj)
/*    */   {
/*    */     // Byte code:
/*    */     //   0: getstatic 25	com/avrgaming/civcraft/database/SQLUpdate:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   3: invokevirtual 39	java/util/concurrent/locks/ReentrantLock:tryLock	()Z
/*    */     //   6: ifeq +34 -> 40
/*    */     //   9: getstatic 30	com/avrgaming/civcraft/database/SQLUpdate:saveObjects	Ljava/util/Queue;
/*    */     //   12: aload_0
/*    */     //   13: invokeinterface 43 2 0
/*    */     //   18: pop
/*    */     //   19: goto +12 -> 31
/*    */     //   22: astore_1
/*    */     //   23: getstatic 25	com/avrgaming/civcraft/database/SQLUpdate:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   26: invokevirtual 48	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*    */     //   29: aload_1
/*    */     //   30: athrow
/*    */     //   31: getstatic 25	com/avrgaming/civcraft/database/SQLUpdate:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   34: invokevirtual 48	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*    */     //   37: goto +15 -> 52
/*    */     //   40: new 51	com/avrgaming/civcraft/database/SQLUpdate$1AsyncRetrySQLUpdateTask
/*    */     //   43: dup
/*    */     //   44: aload_0
/*    */     //   45: invokespecial 53	com/avrgaming/civcraft/database/SQLUpdate$1AsyncRetrySQLUpdateTask:<init>	(Lcom/avrgaming/civcraft/object/SQLObject;)V
/*    */     //   48: lconst_0
/*    */     //   49: invokestatic 55	com/avrgaming/civcraft/threading/TaskMaster:asyncTask	(Ljava/lang/Runnable;J)V
/*    */     //   52: return
/*    */     // Line number table:
/*    */     //   Java source line #41	-> byte code offset #0
/*    */     //   Java source line #43	-> byte code offset #9
/*    */     //   Java source line #44	-> byte code offset #19
/*    */     //   Java source line #45	-> byte code offset #23
/*    */     //   Java source line #46	-> byte code offset #29
/*    */     //   Java source line #45	-> byte code offset #31
/*    */     //   Java source line #47	-> byte code offset #37
/*    */     //   Java source line #73	-> byte code offset #40
/*    */     //   Java source line #75	-> byte code offset #52
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	53	0	obj	SQLObject
/*    */     //   22	8	1	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   9	22	22	finally
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public void run()
/*    */   {
/*    */     // Byte code:
/*    */     //   0: getstatic 25	com/avrgaming/civcraft/database/SQLUpdate:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   3: invokevirtual 67	java/util/concurrent/locks/ReentrantLock:lock	()V
/*    */     //   6: iconst_0
/*    */     //   7: istore_1
/*    */     //   8: goto +37 -> 45
/*    */     //   11: getstatic 30	com/avrgaming/civcraft/database/SQLUpdate:saveObjects	Ljava/util/Queue;
/*    */     //   14: invokeinterface 69 1 0
/*    */     //   19: checkcast 73	com/avrgaming/civcraft/object/SQLObject
/*    */     //   22: astore_2
/*    */     //   23: aload_2
/*    */     //   24: ifnonnull +6 -> 30
/*    */     //   27: goto +38 -> 65
/*    */     //   30: aload_2
/*    */     //   31: invokevirtual 75	com/avrgaming/civcraft/object/SQLObject:saveNow	()V
/*    */     //   34: goto +8 -> 42
/*    */     //   37: astore_3
/*    */     //   38: aload_3
/*    */     //   39: invokevirtual 78	java/sql/SQLException:printStackTrace	()V
/*    */     //   42: iinc 1 1
/*    */     //   45: iload_1
/*    */     //   46: bipush 50
/*    */     //   48: if_icmplt -37 -> 11
/*    */     //   51: goto +14 -> 65
/*    */     //   54: astore 4
/*    */     //   56: getstatic 25	com/avrgaming/civcraft/database/SQLUpdate:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   59: invokevirtual 48	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*    */     //   62: aload 4
/*    */     //   64: athrow
/*    */     //   65: getstatic 25	com/avrgaming/civcraft/database/SQLUpdate:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   68: invokevirtual 48	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*    */     //   71: return
/*    */     // Line number table:
/*    */     //   Java source line #79	-> byte code offset #0
/*    */     //   Java source line #81	-> byte code offset #6
/*    */     //   Java source line #82	-> byte code offset #11
/*    */     //   Java source line #83	-> byte code offset #23
/*    */     //   Java source line #84	-> byte code offset #27
/*    */     //   Java source line #88	-> byte code offset #30
/*    */     //   Java source line #89	-> byte code offset #34
/*    */     //   Java source line #90	-> byte code offset #38
/*    */     //   Java source line #81	-> byte code offset #42
/*    */     //   Java source line #93	-> byte code offset #51
/*    */     //   Java source line #94	-> byte code offset #56
/*    */     //   Java source line #95	-> byte code offset #62
/*    */     //   Java source line #94	-> byte code offset #65
/*    */     //   Java source line #96	-> byte code offset #71
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	72	0	this	SQLUpdate
/*    */     //   7	39	1	i	int
/*    */     //   22	9	2	obj	SQLObject
/*    */     //   37	2	3	e	java.sql.SQLException
/*    */     //   54	9	4	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   30	34	37	java/sql/SQLException
/*    */     //   6	54	54	finally
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\database\SQLUpdate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
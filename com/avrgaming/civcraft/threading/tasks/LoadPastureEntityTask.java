/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*    */ import com.avrgaming.civcraft.structure.Pasture;
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
/*    */ public class LoadPastureEntityTask
/*    */   implements Runnable
/*    */ {
/*    */   public Queue<SessionEntry> entriesToLoad;
/*    */   public Pasture pasture;
/*    */   
/*    */   public LoadPastureEntityTask(Queue<SessionEntry> entriesToLoad, Pasture pasture)
/*    */   {
/* 23 */     this.entriesToLoad = entriesToLoad;
/* 24 */     this.pasture = pasture;
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public void run()
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield 22	com/avrgaming/civcraft/threading/tasks/LoadPastureEntityTask:pasture	Lcom/avrgaming/civcraft/structure/Pasture;
/*    */     //   4: invokevirtual 30	com/avrgaming/civcraft/structure/Pasture:getMobMax	()I
/*    */     //   7: istore_1
/*    */     //   8: aload_0
/*    */     //   9: getfield 22	com/avrgaming/civcraft/threading/tasks/LoadPastureEntityTask:pasture	Lcom/avrgaming/civcraft/structure/Pasture;
/*    */     //   12: getfield 36	com/avrgaming/civcraft/structure/Pasture:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   15: invokevirtual 40	java/util/concurrent/locks/ReentrantLock:tryLock	()Z
/*    */     //   18: ifeq +163 -> 181
/*    */     //   21: ldc 46
/*    */     //   23: invokestatic 48	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*    */     //   26: iconst_0
/*    */     //   27: istore_2
/*    */     //   28: goto +117 -> 145
/*    */     //   31: aload_0
/*    */     //   32: getfield 20	com/avrgaming/civcraft/threading/tasks/LoadPastureEntityTask:entriesToLoad	Ljava/util/Queue;
/*    */     //   35: invokeinterface 54 1 0
/*    */     //   40: checkcast 60	com/avrgaming/civcraft/sessiondb/SessionEntry
/*    */     //   43: astore_3
/*    */     //   44: aload_3
/*    */     //   45: ifnonnull +6 -> 51
/*    */     //   48: goto +120 -> 168
/*    */     //   51: aload_3
/*    */     //   52: getfield 62	com/avrgaming/civcraft/sessiondb/SessionEntry:value	Ljava/lang/String;
/*    */     //   55: ldc 66
/*    */     //   57: invokevirtual 68	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
/*    */     //   60: astore 4
/*    */     //   62: aload 4
/*    */     //   64: iconst_0
/*    */     //   65: aaload
/*    */     //   66: invokestatic 74	org/bukkit/Bukkit:getWorld	(Ljava/lang/String;)Lorg/bukkit/World;
/*    */     //   69: aload 4
/*    */     //   71: iconst_1
/*    */     //   72: aaload
/*    */     //   73: invokestatic 80	java/util/UUID:fromString	(Ljava/lang/String;)Ljava/util/UUID;
/*    */     //   76: invokestatic 86	com/avrgaming/civcraft/util/EntityUtil:getEntity	(Lorg/bukkit/World;Ljava/util/UUID;)Lorg/bukkit/entity/Entity;
/*    */     //   79: astore 5
/*    */     //   81: aload 5
/*    */     //   83: ifnull +44 -> 127
/*    */     //   86: aload_0
/*    */     //   87: getfield 22	com/avrgaming/civcraft/threading/tasks/LoadPastureEntityTask:pasture	Lcom/avrgaming/civcraft/structure/Pasture;
/*    */     //   90: getfield 92	com/avrgaming/civcraft/structure/Pasture:entities	Ljava/util/HashSet;
/*    */     //   93: aload 5
/*    */     //   95: invokeinterface 96 1 0
/*    */     //   100: invokevirtual 102	java/util/HashSet:add	(Ljava/lang/Object;)Z
/*    */     //   103: pop
/*    */     //   104: getstatic 108	com/avrgaming/civcraft/structure/Pasture:pastureEntities	Ljava/util/Map;
/*    */     //   107: aload 5
/*    */     //   109: invokeinterface 96 1 0
/*    */     //   114: aload_0
/*    */     //   115: getfield 22	com/avrgaming/civcraft/threading/tasks/LoadPastureEntityTask:pasture	Lcom/avrgaming/civcraft/structure/Pasture;
/*    */     //   118: invokeinterface 112 3 0
/*    */     //   123: pop
/*    */     //   124: goto +18 -> 142
/*    */     //   127: invokestatic 118	com/avrgaming/civcraft/main/CivGlobal:getSessionDB	()Lcom/avrgaming/civcraft/sessiondb/SessionDatabase;
/*    */     //   130: aload_3
/*    */     //   131: getfield 124	com/avrgaming/civcraft/sessiondb/SessionEntry:request_id	I
/*    */     //   134: aload_3
/*    */     //   135: getfield 128	com/avrgaming/civcraft/sessiondb/SessionEntry:key	Ljava/lang/String;
/*    */     //   138: invokevirtual 131	com/avrgaming/civcraft/sessiondb/SessionDatabase:delete	(ILjava/lang/String;)Z
/*    */     //   141: pop
/*    */     //   142: iinc 2 1
/*    */     //   145: iload_2
/*    */     //   146: iload_1
/*    */     //   147: if_icmplt -116 -> 31
/*    */     //   150: goto +18 -> 168
/*    */     //   153: astore 6
/*    */     //   155: aload_0
/*    */     //   156: getfield 22	com/avrgaming/civcraft/threading/tasks/LoadPastureEntityTask:pasture	Lcom/avrgaming/civcraft/structure/Pasture;
/*    */     //   159: getfield 36	com/avrgaming/civcraft/structure/Pasture:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   162: invokevirtual 137	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*    */     //   165: aload 6
/*    */     //   167: athrow
/*    */     //   168: aload_0
/*    */     //   169: getfield 22	com/avrgaming/civcraft/threading/tasks/LoadPastureEntityTask:pasture	Lcom/avrgaming/civcraft/structure/Pasture;
/*    */     //   172: getfield 36	com/avrgaming/civcraft/structure/Pasture:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   175: invokevirtual 137	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*    */     //   178: goto +18 -> 196
/*    */     //   181: ldc -116
/*    */     //   183: invokestatic 142	com/avrgaming/civcraft/main/CivLog:warning	(Ljava/lang/String;)V
/*    */     //   186: aload_0
/*    */     //   187: ldc2_w 145
/*    */     //   190: invokestatic 147	com/avrgaming/civcraft/util/TimeTools:toTicks	(J)J
/*    */     //   193: invokestatic 153	com/avrgaming/civcraft/threading/TaskMaster:syncTask	(Ljava/lang/Runnable;J)V
/*    */     //   196: aload_0
/*    */     //   197: getfield 20	com/avrgaming/civcraft/threading/tasks/LoadPastureEntityTask:entriesToLoad	Ljava/util/Queue;
/*    */     //   200: invokeinterface 54 1 0
/*    */     //   205: checkcast 60	com/avrgaming/civcraft/sessiondb/SessionEntry
/*    */     //   208: astore_2
/*    */     //   209: goto +31 -> 240
/*    */     //   212: invokestatic 118	com/avrgaming/civcraft/main/CivGlobal:getSessionDB	()Lcom/avrgaming/civcraft/sessiondb/SessionDatabase;
/*    */     //   215: aload_2
/*    */     //   216: getfield 124	com/avrgaming/civcraft/sessiondb/SessionEntry:request_id	I
/*    */     //   219: aload_2
/*    */     //   220: getfield 128	com/avrgaming/civcraft/sessiondb/SessionEntry:key	Ljava/lang/String;
/*    */     //   223: invokevirtual 131	com/avrgaming/civcraft/sessiondb/SessionDatabase:delete	(ILjava/lang/String;)Z
/*    */     //   226: pop
/*    */     //   227: aload_0
/*    */     //   228: getfield 20	com/avrgaming/civcraft/threading/tasks/LoadPastureEntityTask:entriesToLoad	Ljava/util/Queue;
/*    */     //   231: invokeinterface 54 1 0
/*    */     //   236: checkcast 60	com/avrgaming/civcraft/sessiondb/SessionEntry
/*    */     //   239: astore_2
/*    */     //   240: aload_2
/*    */     //   241: ifnonnull -29 -> 212
/*    */     //   244: ldc -97
/*    */     //   246: invokestatic 48	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*    */     //   249: return
/*    */     // Line number table:
/*    */     //   Java source line #29	-> byte code offset #0
/*    */     //   Java source line #31	-> byte code offset #8
/*    */     //   Java source line #32	-> byte code offset #21
/*    */     //   Java source line #34	-> byte code offset #26
/*    */     //   Java source line #35	-> byte code offset #31
/*    */     //   Java source line #36	-> byte code offset #44
/*    */     //   Java source line #37	-> byte code offset #48
/*    */     //   Java source line #40	-> byte code offset #51
/*    */     //   Java source line #41	-> byte code offset #62
/*    */     //   Java source line #43	-> byte code offset #81
/*    */     //   Java source line #44	-> byte code offset #86
/*    */     //   Java source line #45	-> byte code offset #104
/*    */     //   Java source line #46	-> byte code offset #124
/*    */     //   Java source line #47	-> byte code offset #127
/*    */     //   Java source line #34	-> byte code offset #142
/*    */     //   Java source line #50	-> byte code offset #150
/*    */     //   Java source line #51	-> byte code offset #155
/*    */     //   Java source line #52	-> byte code offset #165
/*    */     //   Java source line #51	-> byte code offset #168
/*    */     //   Java source line #53	-> byte code offset #178
/*    */     //   Java source line #55	-> byte code offset #181
/*    */     //   Java source line #56	-> byte code offset #186
/*    */     //   Java source line #60	-> byte code offset #196
/*    */     //   Java source line #61	-> byte code offset #209
/*    */     //   Java source line #62	-> byte code offset #212
/*    */     //   Java source line #63	-> byte code offset #227
/*    */     //   Java source line #61	-> byte code offset #240
/*    */     //   Java source line #65	-> byte code offset #244
/*    */     //   Java source line #66	-> byte code offset #249
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	250	0	this	LoadPastureEntityTask
/*    */     //   7	140	1	max	int
/*    */     //   27	119	2	i	int
/*    */     //   208	33	2	entry	SessionEntry
/*    */     //   43	92	3	entry	SessionEntry
/*    */     //   60	10	4	split	String[]
/*    */     //   79	29	5	entity	org.bukkit.entity.Entity
/*    */     //   153	13	6	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   26	153	153	finally
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\LoadPastureEntityTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
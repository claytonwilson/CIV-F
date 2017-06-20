/*     */ package com.avrgaming.civcraft.sessiondb;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SessionDBAsyncTimer
/*     */   implements Runnable
/*     */ {
/*     */   private static final int UPDATE_AMOUNT = 30;
/*  18 */   public static ReentrantLock lock = new ReentrantLock();
/*  19 */   public static Queue<SessionAsyncRequest> requestQueue = new LinkedList();
/*     */   
/*     */   /* Error */
/*     */   public void run()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_1
/*     */     //   2: aconst_null
/*     */     //   3: astore_2
/*     */     //   4: iconst_0
/*     */     //   5: istore_3
/*     */     //   6: goto +433 -> 439
/*     */     //   9: getstatic 28	com/avrgaming/civcraft/sessiondb/SessionDBAsyncTimer:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   12: invokevirtual 41	java/util/concurrent/locks/ReentrantLock:lock	()V
/*     */     //   15: getstatic 33	com/avrgaming/civcraft/sessiondb/SessionDBAsyncTimer:requestQueue	Ljava/util/Queue;
/*     */     //   18: invokeinterface 43 1 0
/*     */     //   23: checkcast 49	com/avrgaming/civcraft/sessiondb/SessionAsyncRequest
/*     */     //   26: astore 4
/*     */     //   28: aload 4
/*     */     //   30: ifnonnull +50 -> 80
/*     */     //   33: getstatic 28	com/avrgaming/civcraft/sessiondb/SessionDBAsyncTimer:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   36: invokevirtual 51	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   39: aload_1
/*     */     //   40: ifnull +19 -> 59
/*     */     //   43: aload_1
/*     */     //   44: invokeinterface 54 1 0
/*     */     //   49: goto +10 -> 59
/*     */     //   52: astore 8
/*     */     //   54: aload 8
/*     */     //   56: invokevirtual 59	java/sql/SQLException:printStackTrace	()V
/*     */     //   59: aload_2
/*     */     //   60: ifnull +19 -> 79
/*     */     //   63: aload_2
/*     */     //   64: invokeinterface 54 1 0
/*     */     //   69: goto +10 -> 79
/*     */     //   72: astore 8
/*     */     //   74: aload 8
/*     */     //   76: invokevirtual 59	java/sql/SQLException:printStackTrace	()V
/*     */     //   79: return
/*     */     //   80: aload 4
/*     */     //   82: ifnull +260 -> 342
/*     */     //   85: invokestatic 64	com/avrgaming/civcraft/sessiondb/SessionDBAsyncTimer:$SWITCH_TABLE$com$avrgaming$civcraft$sessiondb$SessionAsyncRequest$Database	()[I
/*     */     //   88: aload 4
/*     */     //   90: getfield 67	com/avrgaming/civcraft/sessiondb/SessionAsyncRequest:database	Lcom/avrgaming/civcraft/sessiondb/SessionAsyncRequest$Database;
/*     */     //   93: invokevirtual 71	com/avrgaming/civcraft/sessiondb/SessionAsyncRequest$Database:ordinal	()I
/*     */     //   96: iaload
/*     */     //   97: tableswitch	default:+69->166, 1:+23->120, 2:+46->143
/*     */     //   120: aload_1
/*     */     //   121: ifnull +12 -> 133
/*     */     //   124: aload_1
/*     */     //   125: invokeinterface 77 1 0
/*     */     //   130: ifeq +7 -> 137
/*     */     //   133: invokestatic 81	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*     */     //   136: astore_1
/*     */     //   137: aload_1
/*     */     //   138: astore 5
/*     */     //   140: goto +73 -> 213
/*     */     //   143: aload_2
/*     */     //   144: ifnull +12 -> 156
/*     */     //   147: aload_2
/*     */     //   148: invokeinterface 77 1 0
/*     */     //   153: ifeq +7 -> 160
/*     */     //   156: invokestatic 87	com/avrgaming/civcraft/database/SQL:getGlobalConnection	()Ljava/sql/Connection;
/*     */     //   159: astore_2
/*     */     //   160: aload_2
/*     */     //   161: astore 5
/*     */     //   163: goto +50 -> 213
/*     */     //   166: getstatic 28	com/avrgaming/civcraft/sessiondb/SessionDBAsyncTimer:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   169: invokevirtual 51	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   172: aload_1
/*     */     //   173: ifnull +19 -> 192
/*     */     //   176: aload_1
/*     */     //   177: invokeinterface 54 1 0
/*     */     //   182: goto +10 -> 192
/*     */     //   185: astore 8
/*     */     //   187: aload 8
/*     */     //   189: invokevirtual 59	java/sql/SQLException:printStackTrace	()V
/*     */     //   192: aload_2
/*     */     //   193: ifnull +19 -> 212
/*     */     //   196: aload_2
/*     */     //   197: invokeinterface 54 1 0
/*     */     //   202: goto +10 -> 212
/*     */     //   205: astore 8
/*     */     //   207: aload 8
/*     */     //   209: invokevirtual 59	java/sql/SQLException:printStackTrace	()V
/*     */     //   212: return
/*     */     //   213: invokestatic 90	com/avrgaming/civcraft/sessiondb/SessionDBAsyncTimer:$SWITCH_TABLE$com$avrgaming$civcraft$sessiondb$SessionAsyncRequest$Operation	()[I
/*     */     //   216: aload 4
/*     */     //   218: getfield 92	com/avrgaming/civcraft/sessiondb/SessionAsyncRequest:op	Lcom/avrgaming/civcraft/sessiondb/SessionAsyncRequest$Operation;
/*     */     //   221: invokevirtual 96	com/avrgaming/civcraft/sessiondb/SessionAsyncRequest$Operation:ordinal	()I
/*     */     //   224: iaload
/*     */     //   225: tableswitch	default:+87->312, 1:+35->260, 2:+46->271, 3:+57->282, 4:+68->293, 5:+79->304
/*     */     //   260: aload_0
/*     */     //   261: aload 4
/*     */     //   263: aload 5
/*     */     //   265: invokevirtual 99	com/avrgaming/civcraft/sessiondb/SessionDBAsyncTimer:performAdd	(Lcom/avrgaming/civcraft/sessiondb/SessionAsyncRequest;Ljava/sql/Connection;)V
/*     */     //   268: goto +74 -> 342
/*     */     //   271: aload_0
/*     */     //   272: aload 4
/*     */     //   274: aload 5
/*     */     //   276: invokespecial 103	com/avrgaming/civcraft/sessiondb/SessionDBAsyncTimer:performDelete	(Lcom/avrgaming/civcraft/sessiondb/SessionAsyncRequest;Ljava/sql/Connection;)V
/*     */     //   279: goto +63 -> 342
/*     */     //   282: aload_0
/*     */     //   283: aload 4
/*     */     //   285: aload 5
/*     */     //   287: invokespecial 106	com/avrgaming/civcraft/sessiondb/SessionDBAsyncTimer:performDeleteAll	(Lcom/avrgaming/civcraft/sessiondb/SessionAsyncRequest;Ljava/sql/Connection;)V
/*     */     //   290: goto +52 -> 342
/*     */     //   293: aload_0
/*     */     //   294: aload 4
/*     */     //   296: aload 5
/*     */     //   298: invokespecial 109	com/avrgaming/civcraft/sessiondb/SessionDBAsyncTimer:performUpdate	(Lcom/avrgaming/civcraft/sessiondb/SessionAsyncRequest;Ljava/sql/Connection;)V
/*     */     //   301: goto +41 -> 342
/*     */     //   304: aload_0
/*     */     //   305: aload 4
/*     */     //   307: aload 5
/*     */     //   309: invokespecial 112	com/avrgaming/civcraft/sessiondb/SessionDBAsyncTimer:performUpdateInsert	(Lcom/avrgaming/civcraft/sessiondb/SessionAsyncRequest;Ljava/sql/Connection;)V
/*     */     //   312: goto +30 -> 342
/*     */     //   315: astore 4
/*     */     //   317: aload 4
/*     */     //   319: invokevirtual 115	java/lang/Exception:printStackTrace	()V
/*     */     //   322: getstatic 28	com/avrgaming/civcraft/sessiondb/SessionDBAsyncTimer:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   325: invokevirtual 51	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   328: goto +68 -> 396
/*     */     //   331: astore 6
/*     */     //   333: getstatic 28	com/avrgaming/civcraft/sessiondb/SessionDBAsyncTimer:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   336: invokevirtual 51	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   339: aload 6
/*     */     //   341: athrow
/*     */     //   342: getstatic 28	com/avrgaming/civcraft/sessiondb/SessionDBAsyncTimer:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   345: invokevirtual 51	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   348: goto +48 -> 396
/*     */     //   351: astore 7
/*     */     //   353: aload_1
/*     */     //   354: ifnull +19 -> 373
/*     */     //   357: aload_1
/*     */     //   358: invokeinterface 54 1 0
/*     */     //   363: goto +10 -> 373
/*     */     //   366: astore 8
/*     */     //   368: aload 8
/*     */     //   370: invokevirtual 59	java/sql/SQLException:printStackTrace	()V
/*     */     //   373: aload_2
/*     */     //   374: ifnull +19 -> 393
/*     */     //   377: aload_2
/*     */     //   378: invokeinterface 54 1 0
/*     */     //   383: goto +10 -> 393
/*     */     //   386: astore 8
/*     */     //   388: aload 8
/*     */     //   390: invokevirtual 59	java/sql/SQLException:printStackTrace	()V
/*     */     //   393: aload 7
/*     */     //   395: athrow
/*     */     //   396: aload_1
/*     */     //   397: ifnull +19 -> 416
/*     */     //   400: aload_1
/*     */     //   401: invokeinterface 54 1 0
/*     */     //   406: goto +10 -> 416
/*     */     //   409: astore 8
/*     */     //   411: aload 8
/*     */     //   413: invokevirtual 59	java/sql/SQLException:printStackTrace	()V
/*     */     //   416: aload_2
/*     */     //   417: ifnull +19 -> 436
/*     */     //   420: aload_2
/*     */     //   421: invokeinterface 54 1 0
/*     */     //   426: goto +10 -> 436
/*     */     //   429: astore 8
/*     */     //   431: aload 8
/*     */     //   433: invokevirtual 59	java/sql/SQLException:printStackTrace	()V
/*     */     //   436: iinc 3 1
/*     */     //   439: iload_3
/*     */     //   440: bipush 30
/*     */     //   442: if_icmplt -433 -> 9
/*     */     //   445: return
/*     */     // Line number table:
/*     */     //   Java source line #26	-> byte code offset #0
/*     */     //   Java source line #27	-> byte code offset #2
/*     */     //   Java source line #29	-> byte code offset #4
/*     */     //   Java source line #31	-> byte code offset #9
/*     */     //   Java source line #33	-> byte code offset #15
/*     */     //   Java source line #34	-> byte code offset #28
/*     */     //   Java source line #78	-> byte code offset #33
/*     */     //   Java source line #82	-> byte code offset #39
/*     */     //   Java source line #83	-> byte code offset #43
/*     */     //   Java source line #85	-> byte code offset #49
/*     */     //   Java source line #86	-> byte code offset #54
/*     */     //   Java source line #90	-> byte code offset #59
/*     */     //   Java source line #91	-> byte code offset #63
/*     */     //   Java source line #93	-> byte code offset #69
/*     */     //   Java source line #94	-> byte code offset #74
/*     */     //   Java source line #35	-> byte code offset #79
/*     */     //   Java source line #38	-> byte code offset #80
/*     */     //   Java source line #40	-> byte code offset #85
/*     */     //   Java source line #40	-> byte code offset #88
/*     */     //   Java source line #42	-> byte code offset #120
/*     */     //   Java source line #43	-> byte code offset #133
/*     */     //   Java source line #45	-> byte code offset #137
/*     */     //   Java source line #46	-> byte code offset #140
/*     */     //   Java source line #48	-> byte code offset #143
/*     */     //   Java source line #49	-> byte code offset #156
/*     */     //   Java source line #51	-> byte code offset #160
/*     */     //   Java source line #52	-> byte code offset #163
/*     */     //   Java source line #78	-> byte code offset #166
/*     */     //   Java source line #82	-> byte code offset #172
/*     */     //   Java source line #83	-> byte code offset #176
/*     */     //   Java source line #85	-> byte code offset #182
/*     */     //   Java source line #86	-> byte code offset #187
/*     */     //   Java source line #90	-> byte code offset #192
/*     */     //   Java source line #91	-> byte code offset #196
/*     */     //   Java source line #93	-> byte code offset #202
/*     */     //   Java source line #94	-> byte code offset #207
/*     */     //   Java source line #54	-> byte code offset #212
/*     */     //   Java source line #57	-> byte code offset #213
/*     */     //   Java source line #57	-> byte code offset #216
/*     */     //   Java source line #59	-> byte code offset #260
/*     */     //   Java source line #60	-> byte code offset #268
/*     */     //   Java source line #62	-> byte code offset #271
/*     */     //   Java source line #63	-> byte code offset #279
/*     */     //   Java source line #65	-> byte code offset #282
/*     */     //   Java source line #66	-> byte code offset #290
/*     */     //   Java source line #68	-> byte code offset #293
/*     */     //   Java source line #69	-> byte code offset #301
/*     */     //   Java source line #71	-> byte code offset #304
/*     */     //   Java source line #75	-> byte code offset #312
/*     */     //   Java source line #76	-> byte code offset #317
/*     */     //   Java source line #78	-> byte code offset #322
/*     */     //   Java source line #77	-> byte code offset #331
/*     */     //   Java source line #78	-> byte code offset #333
/*     */     //   Java source line #79	-> byte code offset #339
/*     */     //   Java source line #78	-> byte code offset #342
/*     */     //   Java source line #80	-> byte code offset #348
/*     */     //   Java source line #82	-> byte code offset #353
/*     */     //   Java source line #83	-> byte code offset #357
/*     */     //   Java source line #85	-> byte code offset #363
/*     */     //   Java source line #86	-> byte code offset #368
/*     */     //   Java source line #90	-> byte code offset #373
/*     */     //   Java source line #91	-> byte code offset #377
/*     */     //   Java source line #93	-> byte code offset #383
/*     */     //   Java source line #94	-> byte code offset #388
/*     */     //   Java source line #96	-> byte code offset #393
/*     */     //   Java source line #82	-> byte code offset #396
/*     */     //   Java source line #83	-> byte code offset #400
/*     */     //   Java source line #85	-> byte code offset #406
/*     */     //   Java source line #86	-> byte code offset #411
/*     */     //   Java source line #90	-> byte code offset #416
/*     */     //   Java source line #91	-> byte code offset #420
/*     */     //   Java source line #93	-> byte code offset #426
/*     */     //   Java source line #94	-> byte code offset #431
/*     */     //   Java source line #29	-> byte code offset #436
/*     */     //   Java source line #99	-> byte code offset #445
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	446	0	this	SessionDBAsyncTimer
/*     */     //   1	400	1	gameConnection	Connection
/*     */     //   3	418	2	globalConnection	Connection
/*     */     //   5	435	3	i	int
/*     */     //   26	280	4	request	SessionAsyncRequest
/*     */     //   315	3	4	e	Exception
/*     */     //   138	3	5	cntx	Connection
/*     */     //   161	3	5	cntx	Connection
/*     */     //   213	95	5	cntx	Connection
/*     */     //   331	9	6	localObject1	Object
/*     */     //   351	43	7	localObject2	Object
/*     */     //   52	3	8	e	java.sql.SQLException
/*     */     //   72	3	8	e	java.sql.SQLException
/*     */     //   185	3	8	e	java.sql.SQLException
/*     */     //   205	3	8	e	java.sql.SQLException
/*     */     //   366	3	8	e	java.sql.SQLException
/*     */     //   386	3	8	e	java.sql.SQLException
/*     */     //   409	3	8	e	java.sql.SQLException
/*     */     //   429	3	8	e	java.sql.SQLException
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   39	49	52	java/sql/SQLException
/*     */     //   59	69	72	java/sql/SQLException
/*     */     //   172	182	185	java/sql/SQLException
/*     */     //   192	202	205	java/sql/SQLException
/*     */     //   15	33	315	java/lang/Exception
/*     */     //   80	166	315	java/lang/Exception
/*     */     //   213	312	315	java/lang/Exception
/*     */     //   15	33	331	finally
/*     */     //   80	166	331	finally
/*     */     //   213	322	331	finally
/*     */     //   9	39	351	finally
/*     */     //   80	172	351	finally
/*     */     //   213	351	351	finally
/*     */     //   353	363	366	java/sql/SQLException
/*     */     //   373	383	386	java/sql/SQLException
/*     */     //   396	406	409	java/sql/SQLException
/*     */     //   416	426	429	java/sql/SQLException
/*     */   }
/*     */   
/*     */   public void performAdd(SessionAsyncRequest request, Connection cntx)
/*     */     throws Exception
/*     */   {
/* 104 */     String code = "INSERT INTO `" + request.tb_prefix + "SESSIONS` (`request_id`, `key`, `value`, `time`, `civ_id`, `town_id`, `struct_id`) VALUES (?, ?, ?, ?, ?, ?, ?)";
/* 105 */     PreparedStatement s = cntx.prepareStatement(code, 1);
/* 106 */     s.setNull(1, 4);
/* 107 */     s.setString(2, request.entry.key);
/* 108 */     s.setString(3, request.entry.value);
/* 109 */     s.setLong(4, request.entry.time);
/* 110 */     s.setInt(5, request.entry.civ_id);
/* 111 */     s.setInt(6, request.entry.town_id);
/* 112 */     s.setInt(7, request.entry.struct_id);
/*     */     
/*     */ 
/* 115 */     int rs = s.executeUpdate();
/* 116 */     if (rs == 0) {
/* 117 */       throw new Exception("Could not execute SQL code:" + code);
/*     */     }
/*     */     
/* 120 */     ResultSet res = s.getGeneratedKeys();
/* 121 */     while (res.next())
/*     */     {
/* 123 */       request.entry.request_id = res.getInt(1);
/*     */     }
/* 125 */     res.close();
/* 126 */     s.close();
/*     */   }
/*     */   
/*     */ 
/*     */   private void performUpdate(SessionAsyncRequest request, Connection cntx)
/*     */     throws Exception
/*     */   {
/* 133 */     String code = "UPDATE `" + request.tb_prefix + "SESSIONS` SET `value`= ? WHERE `request_id` = ?";
/* 134 */     PreparedStatement s = cntx.prepareStatement(code);
/* 135 */     s.setString(1, request.entry.value);
/* 136 */     s.setInt(2, request.entry.request_id);
/*     */     
/* 138 */     int rs = s.executeUpdate();
/* 139 */     s.close();
/* 140 */     if (rs == 0) {
/* 141 */       throw new Exception("Could not execute SQL code:" + code + " value=" + request.entry.value + " reqid=" + request.entry.request_id);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void performUpdateInsert(SessionAsyncRequest request, Connection cntx)
/*     */     throws Exception
/*     */   {
/* 149 */     String code = "UPDATE `" + request.tb_prefix + "SESSIONS` SET `value`= ? WHERE `request_id` = ?";
/* 150 */     PreparedStatement s = cntx.prepareStatement(code);
/* 151 */     s.setString(1, request.entry.value);
/* 152 */     s.setInt(2, request.entry.request_id);
/*     */     
/* 154 */     int rs = s.executeUpdate();
/* 155 */     s.close();
/* 156 */     if (rs == 0) {
/* 157 */       throw new Exception("Could not execute SQL code:" + code);
/*     */     }
/*     */   }
/*     */   
/*     */   private void performDeleteAll(SessionAsyncRequest request, Connection cntx)
/*     */     throws Exception
/*     */   {
/* 164 */     String code = "DELETE FROM `" + request.tb_prefix + "SESSIONS` WHERE `key` = ?";
/* 165 */     PreparedStatement s = cntx.prepareStatement(code);
/* 166 */     s.setString(1, request.entry.key);
/* 167 */     s.executeUpdate();
/* 168 */     s.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void performDelete(SessionAsyncRequest request, Connection cntx)
/*     */     throws Exception
/*     */   {
/* 177 */     String code = "DELETE FROM `" + request.tb_prefix + "SESSIONS` WHERE `request_id` = ?";
/* 178 */     PreparedStatement s = cntx.prepareStatement(code);
/* 179 */     s.setInt(1, request.entry.request_id);
/*     */     
/* 181 */     int rs = s.executeUpdate();
/* 182 */     s.close();
/* 183 */     if (rs == 0) {
/* 184 */       throw new Exception("Could not execute SQL code:" + code + " where entry id:" + request.entry.request_id);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\sessiondb\SessionDBAsyncTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
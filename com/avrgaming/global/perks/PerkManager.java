/*     */ package com.avrgaming.global.perks;
/*     */ 
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PerkManager
/*     */ {
/*  18 */   public static String hostname = "";
/*  19 */   public static String port = "";
/*  20 */   public static String db_name = "";
/*  21 */   public static String username = "";
/*  22 */   public static String password = "";
/*  23 */   public static String dsn = "";
/*     */   
/*     */ 
/*  26 */   public static HashMap<String, Integer> identPlatinumRewards = new HashMap();
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
/*  45 */   private static HashMap<String, Integer> userIdCache = new HashMap();
/*     */   
/*  47 */   public static Integer getUserWebsiteId(Resident resident) throws SQLException, NotVerifiedException { Connection context = null;
/*  48 */     ResultSet rs = null;
/*  49 */     PreparedStatement s = null;
/*     */     try
/*     */     {
/*  52 */       context = SQL.getPerkConnection();
/*     */       
/*  54 */       Integer userId = (Integer)userIdCache.get(resident.getName());
/*  55 */       Integer localInteger1; if (userId != null) {
/*  56 */         return userId;
/*     */       }
/*     */       
/*  59 */       String sql = "SELECT `id`, `game_name`, `verified` FROM `users` WHERE `game_name` = ?";
/*  60 */       s = context.prepareStatement(sql);
/*  61 */       s.setString(1, resident.getName());
/*     */       
/*  63 */       rs = s.executeQuery();
/*  64 */       if (!rs.next()) {
/*  65 */         throw new NotVerifiedException();
/*     */       }
/*     */       
/*     */ 
/*  69 */       Boolean verified = Boolean.valueOf(rs.getBoolean("verified"));
/*  70 */       if (!verified.booleanValue()) {
/*  71 */         throw new NotVerifiedException();
/*     */       }
/*     */       
/*  74 */       userId = Integer.valueOf(rs.getInt("id"));
/*  75 */       userIdCache.put(resident.getName(), userId);
/*  76 */       return userId;
/*     */     } finally {
/*  78 */       SQL.close(rs, s, context);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static java.util.LinkedList<String> loadPerksForResident(Resident resident)
/*     */     throws SQLException, NotVerifiedException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 150	java/util/LinkedList
/*     */     //   3: dup
/*     */     //   4: invokespecial 152	java/util/LinkedList:<init>	()V
/*     */     //   7: astore_1
/*     */     //   8: aconst_null
/*     */     //   9: astore_3
/*     */     //   10: aconst_null
/*     */     //   11: astore 4
/*     */     //   13: aconst_null
/*     */     //   14: astore 5
/*     */     //   16: new 153	java/util/HashSet
/*     */     //   19: dup
/*     */     //   20: invokespecial 155	java/util/HashSet:<init>	()V
/*     */     //   23: astore 6
/*     */     //   25: new 34	java/util/HashMap
/*     */     //   28: dup
/*     */     //   29: invokespecial 36	java/util/HashMap:<init>	()V
/*     */     //   32: astore 7
/*     */     //   34: invokestatic 55	com/avrgaming/civcraft/database/SQL:getPerkConnection	()Ljava/sql/Connection;
/*     */     //   37: astore_3
/*     */     //   38: aload_0
/*     */     //   39: invokestatic 156	com/avrgaming/global/perks/PerkManager:getUserWebsiteId	(Lcom/avrgaming/civcraft/object/Resident;)Ljava/lang/Integer;
/*     */     //   42: astore 8
/*     */     //   44: ldc -98
/*     */     //   46: astore_2
/*     */     //   47: aload_3
/*     */     //   48: aload_2
/*     */     //   49: invokeinterface 79 2 0
/*     */     //   54: astore 5
/*     */     //   56: aload 5
/*     */     //   58: iconst_1
/*     */     //   59: aload 8
/*     */     //   61: invokevirtual 160	java/lang/Integer:intValue	()I
/*     */     //   64: invokeinterface 164 3 0
/*     */     //   69: aload 5
/*     */     //   71: invokeinterface 91 1 0
/*     */     //   76: astore 4
/*     */     //   78: goto +115 -> 193
/*     */     //   81: aload 4
/*     */     //   83: ldc -88
/*     */     //   85: invokeinterface 170 2 0
/*     */     //   90: astore 9
/*     */     //   92: aload 9
/*     */     //   94: ifnonnull +7 -> 101
/*     */     //   97: ldc -82
/*     */     //   99: astore 9
/*     */     //   101: aload 4
/*     */     //   103: ldc -80
/*     */     //   105: invokeinterface 119 2 0
/*     */     //   110: istore 10
/*     */     //   112: aload 9
/*     */     //   114: invokestatic 178	com/avrgaming/civcraft/main/CivGlobal:getPhase	()Ljava/lang/String;
/*     */     //   117: invokevirtual 183	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   120: ifne +73 -> 193
/*     */     //   123: aload 7
/*     */     //   125: iload 10
/*     */     //   127: invokestatic 123	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   130: invokevirtual 67	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   133: checkcast 71	java/lang/Integer
/*     */     //   136: astore 11
/*     */     //   138: aload 11
/*     */     //   140: ifnonnull +21 -> 161
/*     */     //   143: aload 7
/*     */     //   145: iload 10
/*     */     //   147: invokestatic 123	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   150: iconst_1
/*     */     //   151: invokestatic 123	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   154: invokevirtual 126	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   157: pop
/*     */     //   158: goto +24 -> 182
/*     */     //   161: aload 7
/*     */     //   163: iload 10
/*     */     //   165: invokestatic 123	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   168: aload 11
/*     */     //   170: invokevirtual 160	java/lang/Integer:intValue	()I
/*     */     //   173: iconst_1
/*     */     //   174: iadd
/*     */     //   175: invokestatic 123	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   178: invokevirtual 126	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   181: pop
/*     */     //   182: aload 6
/*     */     //   184: iload 10
/*     */     //   186: invokestatic 123	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   189: invokevirtual 187	java/util/HashSet:add	(Ljava/lang/Object;)Z
/*     */     //   192: pop
/*     */     //   193: aload 4
/*     */     //   195: invokeinterface 95 1 0
/*     */     //   200: ifne -119 -> 81
/*     */     //   203: goto +16 -> 219
/*     */     //   206: astore 12
/*     */     //   208: aload 4
/*     */     //   210: aload 5
/*     */     //   212: aconst_null
/*     */     //   213: invokestatic 73	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   216: aload 12
/*     */     //   218: athrow
/*     */     //   219: aload 4
/*     */     //   221: aload 5
/*     */     //   223: aconst_null
/*     */     //   224: invokestatic 73	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   227: aload 6
/*     */     //   229: invokevirtual 190	java/util/HashSet:size	()I
/*     */     //   232: ifle +206 -> 438
/*     */     //   235: new 193	java/lang/StringBuilder
/*     */     //   238: dup
/*     */     //   239: ldc -61
/*     */     //   241: invokespecial 197	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   244: astore 9
/*     */     //   246: aload 6
/*     */     //   248: invokevirtual 200	java/util/HashSet:iterator	()Ljava/util/Iterator;
/*     */     //   251: astore 11
/*     */     //   253: goto +41 -> 294
/*     */     //   256: aload 11
/*     */     //   258: invokeinterface 204 1 0
/*     */     //   263: checkcast 71	java/lang/Integer
/*     */     //   266: astore 10
/*     */     //   268: aload 9
/*     */     //   270: new 193	java/lang/StringBuilder
/*     */     //   273: dup
/*     */     //   274: invokespecial 209	java/lang/StringBuilder:<init>	()V
/*     */     //   277: aload 10
/*     */     //   279: invokevirtual 210	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   282: ldc -42
/*     */     //   284: invokevirtual 216	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   287: invokevirtual 219	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   290: invokevirtual 216	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   293: pop
/*     */     //   294: aload 11
/*     */     //   296: invokeinterface 222 1 0
/*     */     //   301: ifne -45 -> 256
/*     */     //   304: aload 9
/*     */     //   306: aload 9
/*     */     //   308: invokevirtual 225	java/lang/StringBuilder:length	()I
/*     */     //   311: iconst_1
/*     */     //   312: isub
/*     */     //   313: bipush 41
/*     */     //   315: invokevirtual 228	java/lang/StringBuilder:setCharAt	(IC)V
/*     */     //   318: aload_3
/*     */     //   319: aload 9
/*     */     //   321: invokevirtual 219	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   324: invokeinterface 79 2 0
/*     */     //   329: astore 5
/*     */     //   331: aload 5
/*     */     //   333: invokeinterface 91 1 0
/*     */     //   338: astore 4
/*     */     //   340: goto +58 -> 398
/*     */     //   343: aload 7
/*     */     //   345: aload 4
/*     */     //   347: ldc 117
/*     */     //   349: invokeinterface 119 2 0
/*     */     //   354: invokestatic 123	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   357: invokevirtual 67	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   360: checkcast 71	java/lang/Integer
/*     */     //   363: astore 10
/*     */     //   365: iconst_0
/*     */     //   366: istore 11
/*     */     //   368: goto +20 -> 388
/*     */     //   371: aload_1
/*     */     //   372: aload 4
/*     */     //   374: ldc -24
/*     */     //   376: invokeinterface 170 2 0
/*     */     //   381: invokevirtual 234	java/util/LinkedList:add	(Ljava/lang/Object;)Z
/*     */     //   384: pop
/*     */     //   385: iinc 11 1
/*     */     //   388: iload 11
/*     */     //   390: aload 10
/*     */     //   392: invokevirtual 160	java/lang/Integer:intValue	()I
/*     */     //   395: if_icmplt -24 -> 371
/*     */     //   398: aload 4
/*     */     //   400: invokeinterface 95 1 0
/*     */     //   405: ifne -62 -> 343
/*     */     //   408: aload 5
/*     */     //   410: invokeinterface 235 1 0
/*     */     //   415: aload 4
/*     */     //   417: invokeinterface 237 1 0
/*     */     //   422: goto +16 -> 438
/*     */     //   425: astore 12
/*     */     //   427: aload 4
/*     */     //   429: aload 5
/*     */     //   431: aconst_null
/*     */     //   432: invokestatic 73	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   435: aload 12
/*     */     //   437: athrow
/*     */     //   438: aload 4
/*     */     //   440: aload 5
/*     */     //   442: aconst_null
/*     */     //   443: invokestatic 73	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   446: aload_1
/*     */     //   447: astore 14
/*     */     //   449: aload 4
/*     */     //   451: aload 5
/*     */     //   453: aload_3
/*     */     //   454: invokestatic 73	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   457: aload 14
/*     */     //   459: areturn
/*     */     //   460: astore 13
/*     */     //   462: aload 4
/*     */     //   464: aload 5
/*     */     //   466: aload_3
/*     */     //   467: invokestatic 73	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   470: aload 13
/*     */     //   472: athrow
/*     */     // Line number table:
/*     */     //   Java source line #83	-> byte code offset #0
/*     */     //   Java source line #85	-> byte code offset #8
/*     */     //   Java source line #86	-> byte code offset #10
/*     */     //   Java source line #87	-> byte code offset #13
/*     */     //   Java source line #88	-> byte code offset #16
/*     */     //   Java source line #89	-> byte code offset #25
/*     */     //   Java source line #92	-> byte code offset #34
/*     */     //   Java source line #95	-> byte code offset #38
/*     */     //   Java source line #99	-> byte code offset #44
/*     */     //   Java source line #100	-> byte code offset #47
/*     */     //   Java source line #101	-> byte code offset #56
/*     */     //   Java source line #103	-> byte code offset #69
/*     */     //   Java source line #105	-> byte code offset #78
/*     */     //   Java source line #108	-> byte code offset #81
/*     */     //   Java source line #109	-> byte code offset #92
/*     */     //   Java source line #110	-> byte code offset #97
/*     */     //   Java source line #113	-> byte code offset #101
/*     */     //   Java source line #114	-> byte code offset #112
/*     */     //   Java source line #115	-> byte code offset #123
/*     */     //   Java source line #116	-> byte code offset #138
/*     */     //   Java source line #117	-> byte code offset #143
/*     */     //   Java source line #118	-> byte code offset #158
/*     */     //   Java source line #119	-> byte code offset #161
/*     */     //   Java source line #122	-> byte code offset #182
/*     */     //   Java source line #105	-> byte code offset #193
/*     */     //   Java source line #125	-> byte code offset #203
/*     */     //   Java source line #126	-> byte code offset #208
/*     */     //   Java source line #127	-> byte code offset #216
/*     */     //   Java source line #126	-> byte code offset #219
/*     */     //   Java source line #130	-> byte code offset #227
/*     */     //   Java source line #132	-> byte code offset #235
/*     */     //   Java source line #133	-> byte code offset #246
/*     */     //   Java source line #134	-> byte code offset #268
/*     */     //   Java source line #133	-> byte code offset #294
/*     */     //   Java source line #136	-> byte code offset #304
/*     */     //   Java source line #137	-> byte code offset #318
/*     */     //   Java source line #138	-> byte code offset #331
/*     */     //   Java source line #141	-> byte code offset #340
/*     */     //   Java source line #142	-> byte code offset #343
/*     */     //   Java source line #143	-> byte code offset #365
/*     */     //   Java source line #144	-> byte code offset #371
/*     */     //   Java source line #143	-> byte code offset #385
/*     */     //   Java source line #141	-> byte code offset #398
/*     */     //   Java source line #148	-> byte code offset #408
/*     */     //   Java source line #149	-> byte code offset #415
/*     */     //   Java source line #151	-> byte code offset #422
/*     */     //   Java source line #152	-> byte code offset #427
/*     */     //   Java source line #153	-> byte code offset #435
/*     */     //   Java source line #152	-> byte code offset #438
/*     */     //   Java source line #154	-> byte code offset #446
/*     */     //   Java source line #156	-> byte code offset #449
/*     */     //   Java source line #154	-> byte code offset #457
/*     */     //   Java source line #155	-> byte code offset #460
/*     */     //   Java source line #156	-> byte code offset #462
/*     */     //   Java source line #157	-> byte code offset #470
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	473	0	resident	Resident
/*     */     //   7	440	1	perkIdents	java.util.LinkedList<String>
/*     */     //   46	3	2	sql	String
/*     */     //   219	1	2	sql	String
/*     */     //   9	458	3	context	Connection
/*     */     //   11	452	4	rs	ResultSet
/*     */     //   14	451	5	s	PreparedStatement
/*     */     //   23	224	6	perkIDs	java.util.HashSet<Integer>
/*     */     //   32	312	7	perkCounts	HashMap<Integer, Integer>
/*     */     //   42	18	8	userID	Integer
/*     */     //   90	23	9	usedPhase	String
/*     */     //   244	76	9	sqlBuild	StringBuilder
/*     */     //   110	75	10	id	int
/*     */     //   266	12	10	id	Integer
/*     */     //   363	28	10	count	Integer
/*     */     //   136	159	11	count	Integer
/*     */     //   366	23	11	i	int
/*     */     //   206	11	12	localObject1	Object
/*     */     //   425	11	12	localObject2	Object
/*     */     //   460	11	13	localObject3	Object
/*     */     //   447	11	14	localLinkedList1	java.util.LinkedList<String>
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   44	206	206	finally
/*     */     //   227	425	425	finally
/*     */     //   34	449	460	finally
/*     */   }
/*     */   
/*     */   public static void markAsUsed(Resident resident, Perk parent)
/*     */     throws SQLException, NotVerifiedException
/*     */   {
/* 161 */     Connection context = null;
/* 162 */     PreparedStatement s = null;
/*     */     try
/*     */     {
/* 165 */       context = SQL.getPerkConnection();
/* 166 */       Integer userID = getUserWebsiteId(resident);
/* 167 */       Integer perkID = getPerkWebsiteId(parent);
/*     */       
/* 169 */       if (perkID.intValue() == 0) {
/* 170 */         CivLog.error("Couldn't find perk id in website DB. Looking for ident:" + parent.getIdent());
/* 171 */         return;
/*     */       }
/*     */       
/* 174 */       String sql = "UPDATE `userperks` SET `used_phase` = ? WHERE `user_id` = ? AND `perk_id` = ? AND (`used_phase` IS NULL OR `used_phase` NOT LIKE ?) LIMIT 1";
/* 175 */       s = context.prepareStatement(sql);
/* 176 */       s.setString(1, CivGlobal.getPhase());
/* 177 */       s.setInt(2, userID.intValue());
/* 178 */       s.setInt(3, perkID.intValue());
/* 179 */       s.setString(4, CivGlobal.getPhase());
/*     */       
/* 181 */       int update = s.executeUpdate();
/* 182 */       if (update != 1) {
/* 183 */         CivLog.error("Marked an unexpected number of perks as used. Marked " + update + " should have been 1");
/*     */       }
/* 185 */       return;
/*     */     } finally {
/* 187 */       SQL.close(null, s, context);
/*     */     }
/*     */   }
/*     */   
/*     */   private static Integer getPerkWebsiteId(Perk parent) throws SQLException {
/* 192 */     Connection context = null;
/* 193 */     ResultSet rs = null;
/* 194 */     PreparedStatement s = null;
/*     */     try
/*     */     {
/* 197 */       context = SQL.getPerkConnection();
/* 198 */       String sql = "SELECT `id` FROM `perks` WHERE `ident` = ?";
/* 199 */       s = context.prepareStatement(sql);
/* 200 */       s.setString(1, parent.getIdent());
/*     */       
/* 202 */       rs = s.executeQuery();
/* 203 */       Integer perkID = Integer.valueOf(0);
/* 204 */       if (rs.next()) {
/* 205 */         perkID = Integer.valueOf(rs.getInt("id"));
/*     */       }
/* 207 */       return perkID;
/*     */     } finally {
/* 209 */       SQL.close(rs, s, context);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void updatePlatinum(Integer userID, Integer plat) throws SQLException {
/* 214 */     Connection context = null;
/* 215 */     PreparedStatement s = null;
/*     */     try
/*     */     {
/* 218 */       context = SQL.getPerkConnection();
/* 219 */       String sql = "UPDATE `users` SET `platinum` = `platinum` + ? WHERE `id` = ?";
/* 220 */       s = context.prepareStatement(sql);
/* 221 */       s.setInt(1, plat.intValue());
/* 222 */       s.setInt(2, userID.intValue());
/*     */       
/* 224 */       CivLog.info("Updated Platinum, user:" + userID + " with:" + plat);
/* 225 */       int update = s.executeUpdate();
/* 226 */       if (update != 1) {
/* 227 */         CivLog.error("Failed to update platinum. Updated " + update + " rows when it should have been 1");
/*     */       }
/*     */       
/* 230 */       return;
/*     */     } finally {
/* 232 */       SQL.close(null, s, context);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\global\perks\PerkManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
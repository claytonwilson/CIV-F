/*     */ package com.avrgaming.civcraft.database;
/*     */ 
/*     */ import com.avrgaming.civcraft.arena.ArenaTeam;
/*     */ import com.avrgaming.civcraft.camp.Camp;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigMarketItem;
/*     */ import com.avrgaming.civcraft.event.EventTimer;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.items.BonusGoodie;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.MissionLogger;
/*     */ import com.avrgaming.civcraft.object.NamedObject;
/*     */ import com.avrgaming.civcraft.object.ProtectedBlock;
/*     */ import com.avrgaming.civcraft.object.Relation;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.SQLObject;
/*     */ import com.avrgaming.civcraft.object.StructureChest;
/*     */ import com.avrgaming.civcraft.object.StructureSign;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.object.TownChunk;
/*     */ import com.avrgaming.civcraft.object.TradeGood;
/*     */ import com.avrgaming.civcraft.object.WallBlock;
/*     */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*     */ import com.avrgaming.civcraft.randomevents.RandomEvent;
/*     */ import com.avrgaming.civcraft.road.RoadBlock;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import com.avrgaming.civcraft.structure.wonders.Wonder;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.BiomeCache;
/*     */ import com.avrgaming.global.perks.PerkManager;
/*     */ import com.avrgaming.global.perks.PlatinumManager;
/*     */ import com.avrgaming.global.reports.ReportManager;
/*     */ import com.avrgaming.global.scores.ScoreManager;
/*     */ import com.jolbox.bonecp.Statistics;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
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
/*     */ public class SQL
/*     */ {
/*  69 */   public static String hostname = "";
/*  70 */   public static String port = "";
/*  71 */   public static String db_name = "";
/*  72 */   public static String username = "";
/*  73 */   public static String password = "";
/*  74 */   public static String tb_prefix = "";
/*     */   
/*  76 */   private static String dsn = "";
/*     */   
/*     */   public static Integer min_conns;
/*     */   
/*     */   public static Integer max_conns;
/*     */   
/*     */   public static Integer parts;
/*     */   
/*  84 */   public static String global_dsn = "";
/*  85 */   public static String global_hostname = "";
/*  86 */   public static String global_port = "";
/*  87 */   public static String global_username = "";
/*  88 */   public static String global_password = "";
/*  89 */   public static String global_db = "";
/*     */   public static Integer global_min_conns;
/*     */   public static Integer global_max_conns;
/*     */   public static Integer global_parts;
/*     */   public static ConnectionPool gameDatabase;
/*     */   public static ConnectionPool globalDatabase;
/*     */   public static ConnectionPool perkDatabase;
/*     */   
/*     */   public static void initialize() throws InvalidConfiguration, SQLException, ClassNotFoundException
/*     */   {
/*  99 */     CivLog.heading("Initializing SQL");
/*     */     
/* 101 */     hostname = CivSettings.getStringBase("mysql.hostname");
/* 102 */     port = CivSettings.getStringBase("mysql.port");
/* 103 */     db_name = CivSettings.getStringBase("mysql.database");
/* 104 */     username = CivSettings.getStringBase("mysql.username");
/* 105 */     password = CivSettings.getStringBase("mysql.password");
/* 106 */     tb_prefix = CivSettings.getStringBase("mysql.table_prefix");
/* 107 */     dsn = "jdbc:mysql://" + hostname + ":" + port + "/" + tb_prefix + db_name;
/* 108 */     min_conns = Integer.valueOf(CivSettings.getStringBase("mysql.min_conns"));
/* 109 */     max_conns = Integer.valueOf(CivSettings.getStringBase("mysql.max_conns"));
/* 110 */     parts = Integer.valueOf(CivSettings.getStringBase("mysql.parts"));
/*     */     
/*     */ 
/* 113 */     CivLog.info("\t Using " + tb_prefix + db_name + " as database.");
/* 114 */     CivLog.info("\t Building Connection Pool for GAME database.");
/* 115 */     gameDatabase = new ConnectionPool(dsn, username, password, min_conns.intValue(), max_conns.intValue(), parts.intValue());
/* 116 */     CivLog.info("\t Connected to GAME database");
/*     */     
/* 118 */     CivLog.heading("Initializing Global SQL Database");
/* 119 */     global_hostname = CivSettings.getStringBase("global_database.hostname");
/* 120 */     global_port = CivSettings.getStringBase("global_database.port");
/* 121 */     global_username = CivSettings.getStringBase("global_database.username");
/* 122 */     global_password = CivSettings.getStringBase("global_database.password");
/* 123 */     global_db = CivSettings.getStringBase("global_database.database");
/* 124 */     global_min_conns = Integer.valueOf(CivSettings.getStringBase("global_database.min_conns"));
/* 125 */     global_max_conns = Integer.valueOf(CivSettings.getStringBase("global_database.max_conns"));
/* 126 */     global_parts = Integer.valueOf(CivSettings.getStringBase("global_database.parts"));
/*     */     
/* 128 */     global_dsn = "jdbc:mysql://" + global_hostname + ":" + global_port + "/" + global_db;
/* 129 */     CivLog.info("\t Using GLOBAL db at:" + global_hostname + ":" + global_port + " user:" + global_username + " DB:" + global_db);
/* 130 */     CivLog.info("\t Building Connection Pool for GAME database.");
/* 131 */     globalDatabase = new ConnectionPool(global_dsn, global_username, global_password, global_min_conns.intValue(), global_max_conns.intValue(), global_parts.intValue());
/* 132 */     CivLog.info("\t Connected to GLOBAL database");
/*     */     
/* 134 */     if (PlatinumManager.isEnabled()) {
/* 135 */       CivLog.heading("Initializing Perk/Web Database");
/* 136 */       PerkManager.hostname = CivSettings.getStringBase("perk_database.hostname");
/* 137 */       PerkManager.port = CivSettings.getStringBase("perk_database.port");
/* 138 */       PerkManager.db_name = CivSettings.getStringBase("perk_database.database");
/* 139 */       PerkManager.username = CivSettings.getStringBase("perk_database.username");
/* 140 */       PerkManager.password = CivSettings.getStringBase("perk_database.password");
/* 141 */       PerkManager.dsn = "jdbc:mysql://" + PerkManager.hostname + ":" + PerkManager.port + "/" + PerkManager.db_name;
/* 142 */       CivLog.info("\t Using " + PerkManager.dsn + " as PERK database.");
/* 143 */       perkDatabase = new ConnectionPool(PerkManager.dsn, PerkManager.username, PerkManager.password, global_min_conns.intValue(), global_max_conns.intValue(), global_parts.intValue());
/* 144 */       CivLog.info("\t Connected to PERK database.");
/*     */     }
/*     */     
/*     */ 
/* 148 */     CivLog.heading("Initializing SQL Finished");
/*     */   }
/*     */   
/*     */   public static void initCivObjectTables() throws SQLException
/*     */   {
/* 153 */     CivLog.heading("Building Civ Object Tables.");
/*     */     
/* 155 */     SessionDatabase.init();
/* 156 */     BiomeCache.init();
/* 157 */     Civilization.init();
/* 158 */     Town.init();
/* 159 */     Resident.init();
/* 160 */     Relation.init();
/* 161 */     TownChunk.init();
/* 162 */     Structure.init();
/* 163 */     Wonder.init();
/* 164 */     WallBlock.init();
/* 165 */     RoadBlock.init();
/* 166 */     PermissionGroup.init();
/* 167 */     StructureSign.init();
/* 168 */     StructureChest.init();
/* 169 */     TradeGood.init();
/* 170 */     ProtectedBlock.init();
/* 171 */     BonusGoodie.init();
/* 172 */     MissionLogger.init();
/* 173 */     EventTimer.init();
/* 174 */     Camp.init();
/* 175 */     ConfigMarketItem.init();
/* 176 */     RandomEvent.init();
/* 177 */     ArenaTeam.init();
/*     */     
/* 179 */     CivLog.heading("Building Global Tables!!");
/* 180 */     ReportManager.init();
/* 181 */     ScoreManager.init();
/*     */     
/* 183 */     CivLog.info("----- Done Building Tables ----");
/*     */   }
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
/*     */   public static Connection getGameConnection()
/*     */     throws SQLException
/*     */   {
/* 229 */     return gameDatabase.getConnection();
/*     */   }
/*     */   
/*     */   public static Statistics getGameDatabaseStats() {
/* 233 */     return gameDatabase.getStats();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Connection getGlobalConnection()
/*     */     throws SQLException
/*     */   {
/* 246 */     return globalDatabase.getConnection();
/*     */   }
/*     */   
/*     */   public static Statistics getGlobalDatabaseStats() {
/* 250 */     return globalDatabase.getStats();
/*     */   }
/*     */   
/*     */   public static Connection getPerkConnection() throws SQLException
/*     */   {
/* 255 */     if (getPerkDatabaseStats().getTotalFree() == 0) {
/*     */       try {
/* 257 */         throw new CivException("No more free connections! Possible connection leak!");
/*     */       } catch (CivException e) {
/* 259 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/* 263 */     return perkDatabase.getConnection();
/*     */   }
/*     */   
/* 266 */   public static Statistics getPerkDatabaseStats() { return perkDatabase.getStats(); }
/*     */   
/*     */   public static boolean hasTable(String name) throws SQLException
/*     */   {
/* 270 */     Connection context = null;
/* 271 */     ResultSet result = null;
/*     */     try {
/* 273 */       context = getGameConnection();
/* 274 */       DatabaseMetaData dbm = context.getMetaData();
/* 275 */       String[] types = { "TABLE" };
/*     */       
/* 277 */       result = dbm.getTables(null, null, tb_prefix + name, types);
/* 278 */       if (result.next()) {
/* 279 */         return true;
/*     */       }
/* 281 */       return false;
/*     */     } finally {
/* 283 */       close(result, null, context);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean hasGlobalTable(String name) throws SQLException {
/* 288 */     Connection global_context = null;
/* 289 */     ResultSet rs = null;
/*     */     try
/*     */     {
/* 292 */       global_context = getGlobalConnection();
/* 293 */       DatabaseMetaData dbm = global_context.getMetaData();
/* 294 */       String[] types = { "TABLE" };
/* 295 */       rs = dbm.getTables(null, null, name, types);
/* 296 */       if (rs.next()) {
/* 297 */         return true;
/*     */       }
/* 299 */       return false;
/*     */     }
/*     */     finally {
/* 302 */       close(rs, null, global_context);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean hasColumn(String tablename, String columnName) throws SQLException {
/* 307 */     Connection context = null;
/* 308 */     ResultSet result = null;
/*     */     try
/*     */     {
/* 311 */       context = getGameConnection();
/* 312 */       DatabaseMetaData dbm = context.getMetaData();
/* 313 */       result = dbm.getColumns(null, null, tb_prefix + tablename, columnName);
/* 314 */       boolean found = result.next();
/* 315 */       return found;
/*     */     } finally {
/* 317 */       close(result, null, context);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static void addColumn(String tablename, String columnDef)
/*     */     throws SQLException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_2
/*     */     //   2: aconst_null
/*     */     //   3: astore_3
/*     */     //   4: new 102	java/lang/StringBuilder
/*     */     //   7: dup
/*     */     //   8: ldc_w 405
/*     */     //   11: invokespecial 106	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   14: getstatic 45	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*     */     //   17: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   20: aload_0
/*     */     //   21: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   24: ldc_w 407
/*     */     //   27: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   30: aload_1
/*     */     //   31: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   34: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   37: astore 4
/*     */     //   39: invokestatic 346	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*     */     //   42: astore_2
/*     */     //   43: aload_2
/*     */     //   44: aload 4
/*     */     //   46: invokeinterface 409 2 0
/*     */     //   51: astore_3
/*     */     //   52: aload_3
/*     */     //   53: invokeinterface 413 1 0
/*     */     //   58: pop
/*     */     //   59: new 102	java/lang/StringBuilder
/*     */     //   62: dup
/*     */     //   63: ldc_w 418
/*     */     //   66: invokespecial 106	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   69: aload_1
/*     */     //   70: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   73: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   76: invokestatic 142	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*     */     //   79: goto +14 -> 93
/*     */     //   82: astore 5
/*     */     //   84: aconst_null
/*     */     //   85: aload_3
/*     */     //   86: aload_2
/*     */     //   87: invokestatic 372	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   90: aload 5
/*     */     //   92: athrow
/*     */     //   93: aconst_null
/*     */     //   94: aload_3
/*     */     //   95: aload_2
/*     */     //   96: invokestatic 372	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   99: return
/*     */     // Line number table:
/*     */     //   Java source line #322	-> byte code offset #0
/*     */     //   Java source line #323	-> byte code offset #2
/*     */     //   Java source line #326	-> byte code offset #4
/*     */     //   Java source line #327	-> byte code offset #39
/*     */     //   Java source line #328	-> byte code offset #43
/*     */     //   Java source line #329	-> byte code offset #52
/*     */     //   Java source line #330	-> byte code offset #59
/*     */     //   Java source line #331	-> byte code offset #79
/*     */     //   Java source line #332	-> byte code offset #84
/*     */     //   Java source line #333	-> byte code offset #90
/*     */     //   Java source line #332	-> byte code offset #93
/*     */     //   Java source line #335	-> byte code offset #99
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	100	0	tablename	String
/*     */     //   0	100	1	columnDef	String
/*     */     //   1	95	2	context	Connection
/*     */     //   3	92	3	ps	PreparedStatement
/*     */     //   37	8	4	table_alter	String
/*     */     //   82	9	5	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	82	82	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static boolean hasGlobalColumn(String tablename, String columnName)
/*     */     throws SQLException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_2
/*     */     //   2: aconst_null
/*     */     //   3: astore_3
/*     */     //   4: invokestatic 389	com/avrgaming/civcraft/database/SQL:getGlobalConnection	()Ljava/sql/Connection;
/*     */     //   7: astore_2
/*     */     //   8: aload_2
/*     */     //   9: invokeinterface 348 1 0
/*     */     //   14: astore 4
/*     */     //   16: aload 4
/*     */     //   18: aconst_null
/*     */     //   19: aconst_null
/*     */     //   20: aload_0
/*     */     //   21: aload_1
/*     */     //   22: invokeinterface 395 5 0
/*     */     //   27: astore_3
/*     */     //   28: aload_3
/*     */     //   29: invokeinterface 367 1 0
/*     */     //   34: istore 5
/*     */     //   36: iload 5
/*     */     //   38: istore 7
/*     */     //   40: aload_3
/*     */     //   41: invokeinterface 425 1 0
/*     */     //   46: aload_3
/*     */     //   47: aconst_null
/*     */     //   48: aload_2
/*     */     //   49: invokestatic 372	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   52: iload 7
/*     */     //   54: ireturn
/*     */     //   55: astore 6
/*     */     //   57: aload_3
/*     */     //   58: invokeinterface 425 1 0
/*     */     //   63: aload 6
/*     */     //   65: athrow
/*     */     //   66: astore 8
/*     */     //   68: aload_3
/*     */     //   69: aconst_null
/*     */     //   70: aload_2
/*     */     //   71: invokestatic 372	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   74: aload 8
/*     */     //   76: athrow
/*     */     // Line number table:
/*     */     //   Java source line #338	-> byte code offset #0
/*     */     //   Java source line #339	-> byte code offset #2
/*     */     //   Java source line #342	-> byte code offset #4
/*     */     //   Java source line #343	-> byte code offset #8
/*     */     //   Java source line #344	-> byte code offset #16
/*     */     //   Java source line #347	-> byte code offset #28
/*     */     //   Java source line #348	-> byte code offset #36
/*     */     //   Java source line #350	-> byte code offset #40
/*     */     //   Java source line #354	-> byte code offset #46
/*     */     //   Java source line #348	-> byte code offset #52
/*     */     //   Java source line #349	-> byte code offset #55
/*     */     //   Java source line #350	-> byte code offset #57
/*     */     //   Java source line #351	-> byte code offset #63
/*     */     //   Java source line #353	-> byte code offset #66
/*     */     //   Java source line #354	-> byte code offset #68
/*     */     //   Java source line #355	-> byte code offset #74
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	77	0	tablename	String
/*     */     //   0	77	1	columnName	String
/*     */     //   1	70	2	global_context	Connection
/*     */     //   3	66	3	rs	ResultSet
/*     */     //   14	3	4	dbm	DatabaseMetaData
/*     */     //   34	3	5	found	boolean
/*     */     //   55	9	6	localObject1	Object
/*     */     //   38	15	7	bool1	boolean
/*     */     //   66	9	8	localObject2	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   28	40	55	finally
/*     */     //   4	46	66	finally
/*     */     //   55	66	66	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static void addGlobalColumn(String tablename, String columnDef)
/*     */     throws SQLException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_2
/*     */     //   2: aconst_null
/*     */     //   3: astore_3
/*     */     //   4: invokestatic 389	com/avrgaming/civcraft/database/SQL:getGlobalConnection	()Ljava/sql/Connection;
/*     */     //   7: astore_2
/*     */     //   8: new 102	java/lang/StringBuilder
/*     */     //   11: dup
/*     */     //   12: ldc_w 405
/*     */     //   15: invokespecial 106	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   18: aload_0
/*     */     //   19: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   22: ldc_w 407
/*     */     //   25: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   28: aload_1
/*     */     //   29: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   32: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   35: astore 4
/*     */     //   37: aload_2
/*     */     //   38: aload 4
/*     */     //   40: invokeinterface 409 2 0
/*     */     //   45: astore_3
/*     */     //   46: aload_3
/*     */     //   47: invokeinterface 413 1 0
/*     */     //   52: pop
/*     */     //   53: new 102	java/lang/StringBuilder
/*     */     //   56: dup
/*     */     //   57: ldc_w 428
/*     */     //   60: invokespecial 106	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   63: aload_1
/*     */     //   64: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   67: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   70: invokestatic 142	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*     */     //   73: goto +14 -> 87
/*     */     //   76: astore 5
/*     */     //   78: aconst_null
/*     */     //   79: aload_3
/*     */     //   80: aload_2
/*     */     //   81: invokestatic 372	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   84: aload 5
/*     */     //   86: athrow
/*     */     //   87: aconst_null
/*     */     //   88: aload_3
/*     */     //   89: aload_2
/*     */     //   90: invokestatic 372	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   93: return
/*     */     // Line number table:
/*     */     //   Java source line #358	-> byte code offset #0
/*     */     //   Java source line #359	-> byte code offset #2
/*     */     //   Java source line #362	-> byte code offset #4
/*     */     //   Java source line #363	-> byte code offset #8
/*     */     //   Java source line #364	-> byte code offset #28
/*     */     //   Java source line #363	-> byte code offset #32
/*     */     //   Java source line #366	-> byte code offset #37
/*     */     //   Java source line #367	-> byte code offset #46
/*     */     //   Java source line #368	-> byte code offset #53
/*     */     //   Java source line #369	-> byte code offset #73
/*     */     //   Java source line #370	-> byte code offset #78
/*     */     //   Java source line #371	-> byte code offset #84
/*     */     //   Java source line #370	-> byte code offset #87
/*     */     //   Java source line #372	-> byte code offset #93
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	94	0	tablename	String
/*     */     //   0	94	1	columnDef	String
/*     */     //   1	89	2	global_context	Connection
/*     */     //   3	86	3	ps	PreparedStatement
/*     */     //   35	4	4	table_alter	String
/*     */     //   76	9	5	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	76	76	finally
/*     */   }
/*     */   
/*     */   public static void updateNamedObjectAsync(NamedObject obj, HashMap<String, Object> hashmap, String tablename)
/*     */     throws SQLException
/*     */   {
/* 375 */     TaskMaster.asyncTask("", new SQLUpdateNamedObjectTask(obj, hashmap, tablename), 0L);
/*     */   }
/*     */   
/*     */   public static void updateNamedObject(SQLObject obj, HashMap<String, Object> hashmap, String tablename) throws SQLException {
/* 379 */     if (obj.isDeleted()) {
/* 380 */       return;
/*     */     }
/*     */     
/* 383 */     if (obj.getId() == 0) {
/* 384 */       obj.setId(insertNow(hashmap, tablename));
/*     */     } else {
/* 386 */       update(obj.getId(), hashmap, tablename);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void update(int id, HashMap<String, Object> hashmap, String tablename) throws SQLException {
/* 391 */     hashmap.put("id", Integer.valueOf(id));
/* 392 */     update(hashmap, "id", tablename);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static void update(HashMap<String, Object> hashmap, String keyname, String tablename)
/*     */     throws SQLException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_3
/*     */     //   2: aconst_null
/*     */     //   3: astore 4
/*     */     //   5: new 102	java/lang/StringBuilder
/*     */     //   8: dup
/*     */     //   9: ldc_w 491
/*     */     //   12: invokespecial 106	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   15: getstatic 45	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*     */     //   18: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   21: aload_2
/*     */     //   22: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   25: ldc_w 493
/*     */     //   28: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   31: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   34: astore 5
/*     */     //   36: new 102	java/lang/StringBuilder
/*     */     //   39: dup
/*     */     //   40: ldc_w 495
/*     */     //   43: invokespecial 106	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   46: aload_1
/*     */     //   47: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   50: ldc_w 497
/*     */     //   53: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   56: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   59: astore 6
/*     */     //   61: new 499	java/util/ArrayList
/*     */     //   64: dup
/*     */     //   65: invokespecial 501	java/util/ArrayList:<init>	()V
/*     */     //   68: astore 7
/*     */     //   70: aload_0
/*     */     //   71: invokevirtual 502	java/util/HashMap:keySet	()Ljava/util/Set;
/*     */     //   74: invokeinterface 506 1 0
/*     */     //   79: astore 8
/*     */     //   81: goto +112 -> 193
/*     */     //   84: aload 8
/*     */     //   86: invokeinterface 512 1 0
/*     */     //   91: checkcast 354	java/lang/String
/*     */     //   94: astore 9
/*     */     //   96: aload 9
/*     */     //   98: aload_1
/*     */     //   99: invokevirtual 517	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   102: ifeq +6 -> 108
/*     */     //   105: goto +88 -> 193
/*     */     //   108: new 102	java/lang/StringBuilder
/*     */     //   111: dup
/*     */     //   112: aload 5
/*     */     //   114: invokestatic 358	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   117: invokespecial 106	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   120: ldc_w 521
/*     */     //   123: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   126: aload 9
/*     */     //   128: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   131: ldc_w 523
/*     */     //   134: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   137: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   140: astore 5
/*     */     //   142: new 102	java/lang/StringBuilder
/*     */     //   145: dup
/*     */     //   146: aload 5
/*     */     //   148: invokestatic 358	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   151: invokespecial 106	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   154: aload 8
/*     */     //   156: invokeinterface 525 1 0
/*     */     //   161: ifeq +9 -> 170
/*     */     //   164: ldc_w 528
/*     */     //   167: goto +6 -> 173
/*     */     //   170: ldc_w 530
/*     */     //   173: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   176: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   179: astore 5
/*     */     //   181: aload 7
/*     */     //   183: aload_0
/*     */     //   184: aload 9
/*     */     //   186: invokevirtual 532	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   189: invokevirtual 536	java/util/ArrayList:add	(Ljava/lang/Object;)Z
/*     */     //   192: pop
/*     */     //   193: aload 8
/*     */     //   195: invokeinterface 525 1 0
/*     */     //   200: ifne -116 -> 84
/*     */     //   203: new 102	java/lang/StringBuilder
/*     */     //   206: dup
/*     */     //   207: aload 5
/*     */     //   209: invokestatic 358	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   212: invokespecial 106	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   215: aload 6
/*     */     //   217: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   220: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   223: astore 5
/*     */     //   225: invokestatic 346	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*     */     //   228: astore_3
/*     */     //   229: aload_3
/*     */     //   230: aload 5
/*     */     //   232: invokeinterface 409 2 0
/*     */     //   237: astore 4
/*     */     //   239: iconst_1
/*     */     //   240: istore 9
/*     */     //   242: aload 7
/*     */     //   244: invokevirtual 539	java/util/ArrayList:iterator	()Ljava/util/Iterator;
/*     */     //   247: astore 11
/*     */     //   249: goto +191 -> 440
/*     */     //   252: aload 11
/*     */     //   254: invokeinterface 512 1 0
/*     */     //   259: astore 10
/*     */     //   261: aload 10
/*     */     //   263: instanceof 354
/*     */     //   266: ifeq +20 -> 286
/*     */     //   269: aload 4
/*     */     //   271: iload 9
/*     */     //   273: aload 10
/*     */     //   275: checkcast 354	java/lang/String
/*     */     //   278: invokeinterface 540 3 0
/*     */     //   283: goto +154 -> 437
/*     */     //   286: aload 10
/*     */     //   288: instanceof 123
/*     */     //   291: ifeq +23 -> 314
/*     */     //   294: aload 4
/*     */     //   296: iload 9
/*     */     //   298: aload 10
/*     */     //   300: checkcast 123	java/lang/Integer
/*     */     //   303: invokevirtual 149	java/lang/Integer:intValue	()I
/*     */     //   306: invokeinterface 544 3 0
/*     */     //   311: goto +126 -> 437
/*     */     //   314: aload 10
/*     */     //   316: instanceof 548
/*     */     //   319: ifeq +23 -> 342
/*     */     //   322: aload 4
/*     */     //   324: iload 9
/*     */     //   326: aload 10
/*     */     //   328: checkcast 548	java/lang/Boolean
/*     */     //   331: invokevirtual 550	java/lang/Boolean:booleanValue	()Z
/*     */     //   334: invokeinterface 553 3 0
/*     */     //   339: goto +98 -> 437
/*     */     //   342: aload 10
/*     */     //   344: instanceof 557
/*     */     //   347: ifeq +23 -> 370
/*     */     //   350: aload 4
/*     */     //   352: iload 9
/*     */     //   354: aload 10
/*     */     //   356: checkcast 557	java/lang/Double
/*     */     //   359: invokevirtual 559	java/lang/Double:doubleValue	()D
/*     */     //   362: invokeinterface 563 4 0
/*     */     //   367: goto +70 -> 437
/*     */     //   370: aload 10
/*     */     //   372: instanceof 567
/*     */     //   375: ifeq +23 -> 398
/*     */     //   378: aload 4
/*     */     //   380: iload 9
/*     */     //   382: aload 10
/*     */     //   384: checkcast 567	java/lang/Float
/*     */     //   387: invokevirtual 569	java/lang/Float:floatValue	()F
/*     */     //   390: invokeinterface 573 3 0
/*     */     //   395: goto +42 -> 437
/*     */     //   398: aload 10
/*     */     //   400: instanceof 577
/*     */     //   403: ifeq +23 -> 426
/*     */     //   406: aload 4
/*     */     //   408: iload 9
/*     */     //   410: aload 10
/*     */     //   412: checkcast 577	java/lang/Long
/*     */     //   415: invokevirtual 579	java/lang/Long:longValue	()J
/*     */     //   418: invokeinterface 583 4 0
/*     */     //   423: goto +14 -> 437
/*     */     //   426: aload 4
/*     */     //   428: iload 9
/*     */     //   430: aload 10
/*     */     //   432: invokeinterface 587 3 0
/*     */     //   437: iinc 9 1
/*     */     //   440: aload 11
/*     */     //   442: invokeinterface 525 1 0
/*     */     //   447: ifne -195 -> 252
/*     */     //   450: aload 4
/*     */     //   452: iload 9
/*     */     //   454: aload_0
/*     */     //   455: aload_1
/*     */     //   456: invokevirtual 532	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   459: invokeinterface 587 3 0
/*     */     //   464: aload 4
/*     */     //   466: invokeinterface 591 1 0
/*     */     //   471: ifne +24 -> 495
/*     */     //   474: aload_0
/*     */     //   475: aload_2
/*     */     //   476: invokestatic 461	com/avrgaming/civcraft/database/SQL:insertNow	(Ljava/util/HashMap;Ljava/lang/String;)I
/*     */     //   479: pop
/*     */     //   480: goto +15 -> 495
/*     */     //   483: astore 12
/*     */     //   485: aconst_null
/*     */     //   486: aload 4
/*     */     //   488: aload_3
/*     */     //   489: invokestatic 372	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   492: aload 12
/*     */     //   494: athrow
/*     */     //   495: aconst_null
/*     */     //   496: aload 4
/*     */     //   498: aload_3
/*     */     //   499: invokestatic 372	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   502: return
/*     */     // Line number table:
/*     */     //   Java source line #397	-> byte code offset #0
/*     */     //   Java source line #398	-> byte code offset #2
/*     */     //   Java source line #401	-> byte code offset #5
/*     */     //   Java source line #402	-> byte code offset #36
/*     */     //   Java source line #403	-> byte code offset #61
/*     */     //   Java source line #405	-> byte code offset #70
/*     */     //   Java source line #406	-> byte code offset #81
/*     */     //   Java source line #407	-> byte code offset #84
/*     */     //   Java source line #408	-> byte code offset #96
/*     */     //   Java source line #409	-> byte code offset #105
/*     */     //   Java source line #412	-> byte code offset #108
/*     */     //   Java source line #413	-> byte code offset #142
/*     */     //   Java source line #414	-> byte code offset #181
/*     */     //   Java source line #406	-> byte code offset #193
/*     */     //   Java source line #417	-> byte code offset #203
/*     */     //   Java source line #419	-> byte code offset #225
/*     */     //   Java source line #420	-> byte code offset #229
/*     */     //   Java source line #422	-> byte code offset #239
/*     */     //   Java source line #423	-> byte code offset #242
/*     */     //   Java source line #424	-> byte code offset #261
/*     */     //   Java source line #425	-> byte code offset #269
/*     */     //   Java source line #426	-> byte code offset #283
/*     */     //   Java source line #427	-> byte code offset #294
/*     */     //   Java source line #428	-> byte code offset #311
/*     */     //   Java source line #429	-> byte code offset #322
/*     */     //   Java source line #430	-> byte code offset #339
/*     */     //   Java source line #431	-> byte code offset #350
/*     */     //   Java source line #432	-> byte code offset #367
/*     */     //   Java source line #433	-> byte code offset #378
/*     */     //   Java source line #434	-> byte code offset #395
/*     */     //   Java source line #435	-> byte code offset #406
/*     */     //   Java source line #436	-> byte code offset #423
/*     */     //   Java source line #437	-> byte code offset #426
/*     */     //   Java source line #439	-> byte code offset #437
/*     */     //   Java source line #423	-> byte code offset #440
/*     */     //   Java source line #442	-> byte code offset #450
/*     */     //   Java source line #444	-> byte code offset #464
/*     */     //   Java source line #445	-> byte code offset #474
/*     */     //   Java source line #447	-> byte code offset #480
/*     */     //   Java source line #448	-> byte code offset #485
/*     */     //   Java source line #449	-> byte code offset #492
/*     */     //   Java source line #448	-> byte code offset #495
/*     */     //   Java source line #450	-> byte code offset #502
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	503	0	hashmap	HashMap<String, Object>
/*     */     //   0	503	1	keyname	String
/*     */     //   0	503	2	tablename	String
/*     */     //   1	498	3	context	Connection
/*     */     //   3	494	4	ps	PreparedStatement
/*     */     //   34	197	5	sql	String
/*     */     //   59	157	6	where	String
/*     */     //   68	175	7	values	ArrayList<Object>
/*     */     //   79	115	8	keyIter	Iterator<String>
/*     */     //   94	91	9	key	String
/*     */     //   240	213	9	i	int
/*     */     //   259	172	10	value	Object
/*     */     //   247	194	11	localIterator	Iterator
/*     */     //   483	10	12	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   5	483	483	finally
/*     */   }
/*     */   
/*     */   public static void insert(HashMap<String, Object> hashmap, String tablename)
/*     */   {
/* 453 */     TaskMaster.asyncTask(new SQLInsertTask(hashmap, tablename), 0L);
/*     */   }
/*     */   
/*     */   public static int insertNow(HashMap<String, Object> hashmap, String tablename) throws SQLException {
/* 457 */     Connection context = null;
/* 458 */     ResultSet rs = null;
/* 459 */     PreparedStatement ps = null;
/*     */     try
/*     */     {
/* 462 */       String sql = "INSERT INTO " + tb_prefix + tablename + " ";
/* 463 */       String keycodes = "(";
/* 464 */       String valuecodes = " VALUES ( ";
/* 465 */       ArrayList<Object> values = new ArrayList();
/*     */       
/* 467 */       Iterator<String> keyIter = hashmap.keySet().iterator();
/* 468 */       while (keyIter.hasNext()) {
/* 469 */         String key = (String)keyIter.next();
/*     */         
/* 471 */         keycodes = keycodes + key;
/* 472 */         keycodes = keycodes + (keyIter.hasNext() ? "," : ")");
/*     */         
/* 474 */         valuecodes = valuecodes + "?";
/* 475 */         valuecodes = valuecodes + (keyIter.hasNext() ? "," : ")");
/*     */         
/* 477 */         values.add(hashmap.get(key));
/*     */       }
/*     */       
/* 480 */       sql = sql + keycodes;
/* 481 */       sql = sql + valuecodes;
/*     */       
/* 483 */       context = getGameConnection();
/* 484 */       ps = context.prepareStatement(sql, 1);
/*     */       
/* 486 */       int i = 1;
/* 487 */       for (Object value : values) {
/* 488 */         if ((value instanceof String)) {
/* 489 */           ps.setString(i, (String)value);
/* 490 */         } else if ((value instanceof Integer)) {
/* 491 */           ps.setInt(i, ((Integer)value).intValue());
/* 492 */         } else if ((value instanceof Boolean)) {
/* 493 */           ps.setBoolean(i, ((Boolean)value).booleanValue());
/* 494 */         } else if ((value instanceof Double)) {
/* 495 */           ps.setDouble(i, ((Double)value).doubleValue());
/* 496 */         } else if ((value instanceof Float)) {
/* 497 */           ps.setFloat(i, ((Float)value).floatValue());
/* 498 */         } else if ((value instanceof Long)) {
/* 499 */           ps.setLong(i, ((Long)value).longValue());
/*     */         } else {
/* 501 */           ps.setObject(i, value);
/*     */         }
/* 503 */         i++;
/*     */       }
/*     */       
/* 506 */       ps.execute();
/* 507 */       int id = 0;
/* 508 */       rs = ps.getGeneratedKeys();
/*     */       
/* 510 */       if (rs.next()) {
/* 511 */         id = rs.getInt(1);
/*     */       }
/*     */       
/*     */ 
/* 515 */       if (id == 0) {
/* 516 */         String name = (String)hashmap.get("name");
/* 517 */         if (name == null) {
/* 518 */           name = "Unknown";
/*     */         }
/*     */         
/* 521 */         CivLog.error("SQL ERROR: Saving an SQLObject returned a 0 ID! Name:" + name + " Table:" + tablename);
/*     */       }
/* 523 */       return id;
/*     */     }
/*     */     finally {
/* 526 */       close(rs, ps, context);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static void deleteNamedObject(SQLObject obj, String tablename)
/*     */     throws SQLException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_2
/*     */     //   2: aconst_null
/*     */     //   3: astore_3
/*     */     //   4: new 102	java/lang/StringBuilder
/*     */     //   7: dup
/*     */     //   8: ldc_w 655
/*     */     //   11: invokespecial 106	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   14: getstatic 45	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*     */     //   17: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   20: aload_1
/*     */     //   21: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   24: ldc_w 657
/*     */     //   27: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   30: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   33: astore 4
/*     */     //   35: invokestatic 346	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*     */     //   38: astore_2
/*     */     //   39: aload_2
/*     */     //   40: aload 4
/*     */     //   42: iconst_1
/*     */     //   43: invokeinterface 630 3 0
/*     */     //   48: astore_3
/*     */     //   49: aload_3
/*     */     //   50: iconst_1
/*     */     //   51: aload_0
/*     */     //   52: invokevirtual 458	com/avrgaming/civcraft/object/SQLObject:getId	()I
/*     */     //   55: invokeinterface 544 3 0
/*     */     //   60: aload_3
/*     */     //   61: invokeinterface 413 1 0
/*     */     //   66: pop
/*     */     //   67: aload_3
/*     */     //   68: invokeinterface 659 1 0
/*     */     //   73: aload_0
/*     */     //   74: iconst_1
/*     */     //   75: invokevirtual 660	com/avrgaming/civcraft/object/SQLObject:setDeleted	(Z)V
/*     */     //   78: goto +14 -> 92
/*     */     //   81: astore 5
/*     */     //   83: aconst_null
/*     */     //   84: aload_3
/*     */     //   85: aload_2
/*     */     //   86: invokestatic 372	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   89: aload 5
/*     */     //   91: athrow
/*     */     //   92: aconst_null
/*     */     //   93: aload_3
/*     */     //   94: aload_2
/*     */     //   95: invokestatic 372	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   98: return
/*     */     // Line number table:
/*     */     //   Java source line #532	-> byte code offset #0
/*     */     //   Java source line #533	-> byte code offset #2
/*     */     //   Java source line #536	-> byte code offset #4
/*     */     //   Java source line #537	-> byte code offset #35
/*     */     //   Java source line #538	-> byte code offset #39
/*     */     //   Java source line #539	-> byte code offset #49
/*     */     //   Java source line #540	-> byte code offset #60
/*     */     //   Java source line #541	-> byte code offset #67
/*     */     //   Java source line #542	-> byte code offset #73
/*     */     //   Java source line #543	-> byte code offset #78
/*     */     //   Java source line #544	-> byte code offset #83
/*     */     //   Java source line #545	-> byte code offset #89
/*     */     //   Java source line #544	-> byte code offset #92
/*     */     //   Java source line #546	-> byte code offset #98
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	99	0	obj	SQLObject
/*     */     //   0	99	1	tablename	String
/*     */     //   1	94	2	context	Connection
/*     */     //   3	91	3	ps	PreparedStatement
/*     */     //   33	8	4	sql	String
/*     */     //   81	9	5	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	81	81	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static void deleteByName(String name, String tablename)
/*     */     throws SQLException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_2
/*     */     //   2: aconst_null
/*     */     //   3: astore_3
/*     */     //   4: new 102	java/lang/StringBuilder
/*     */     //   7: dup
/*     */     //   8: ldc_w 655
/*     */     //   11: invokespecial 106	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   14: getstatic 45	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*     */     //   17: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   20: aload_1
/*     */     //   21: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   24: ldc_w 665
/*     */     //   27: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   30: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   33: astore 4
/*     */     //   35: invokestatic 346	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*     */     //   38: astore_2
/*     */     //   39: aload_2
/*     */     //   40: aload 4
/*     */     //   42: iconst_1
/*     */     //   43: invokeinterface 630 3 0
/*     */     //   48: astore_3
/*     */     //   49: aload_3
/*     */     //   50: iconst_1
/*     */     //   51: aload_0
/*     */     //   52: invokeinterface 540 3 0
/*     */     //   57: aload_3
/*     */     //   58: invokeinterface 413 1 0
/*     */     //   63: pop
/*     */     //   64: aload_3
/*     */     //   65: invokeinterface 659 1 0
/*     */     //   70: goto +14 -> 84
/*     */     //   73: astore 5
/*     */     //   75: aconst_null
/*     */     //   76: aload_3
/*     */     //   77: aload_2
/*     */     //   78: invokestatic 372	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   81: aload 5
/*     */     //   83: athrow
/*     */     //   84: aconst_null
/*     */     //   85: aload_3
/*     */     //   86: aload_2
/*     */     //   87: invokestatic 372	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   90: return
/*     */     // Line number table:
/*     */     //   Java source line #549	-> byte code offset #0
/*     */     //   Java source line #550	-> byte code offset #2
/*     */     //   Java source line #553	-> byte code offset #4
/*     */     //   Java source line #554	-> byte code offset #35
/*     */     //   Java source line #555	-> byte code offset #39
/*     */     //   Java source line #556	-> byte code offset #49
/*     */     //   Java source line #557	-> byte code offset #57
/*     */     //   Java source line #558	-> byte code offset #64
/*     */     //   Java source line #559	-> byte code offset #70
/*     */     //   Java source line #560	-> byte code offset #75
/*     */     //   Java source line #561	-> byte code offset #81
/*     */     //   Java source line #560	-> byte code offset #84
/*     */     //   Java source line #562	-> byte code offset #90
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	91	0	name	String
/*     */     //   0	91	1	tablename	String
/*     */     //   1	86	2	context	Connection
/*     */     //   3	83	3	ps	PreparedStatement
/*     */     //   33	8	4	sql	String
/*     */     //   73	9	5	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	73	73	finally
/*     */   }
/*     */   
/*     */   public static void makeCol(String colname, String type, String TABLE_NAME)
/*     */     throws SQLException
/*     */   {
/* 564 */     if (!hasColumn(TABLE_NAME, colname)) {
/* 565 */       CivLog.info("\tCouldn't find " + colname + " column for " + TABLE_NAME);
/* 566 */       addColumn(TABLE_NAME, "`" + colname + "` " + type);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static void makeTable(String table_create)
/*     */     throws SQLException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_1
/*     */     //   2: aconst_null
/*     */     //   3: astore_2
/*     */     //   4: invokestatic 346	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*     */     //   7: astore_1
/*     */     //   8: aload_1
/*     */     //   9: aload_0
/*     */     //   10: invokeinterface 409 2 0
/*     */     //   15: astore_2
/*     */     //   16: aload_2
/*     */     //   17: invokeinterface 413 1 0
/*     */     //   22: pop
/*     */     //   23: goto +12 -> 35
/*     */     //   26: astore_3
/*     */     //   27: aconst_null
/*     */     //   28: aload_2
/*     */     //   29: aload_1
/*     */     //   30: invokestatic 372	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   33: aload_3
/*     */     //   34: athrow
/*     */     //   35: aconst_null
/*     */     //   36: aload_2
/*     */     //   37: aload_1
/*     */     //   38: invokestatic 372	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   41: return
/*     */     // Line number table:
/*     */     //   Java source line #571	-> byte code offset #0
/*     */     //   Java source line #572	-> byte code offset #2
/*     */     //   Java source line #575	-> byte code offset #4
/*     */     //   Java source line #576	-> byte code offset #8
/*     */     //   Java source line #577	-> byte code offset #16
/*     */     //   Java source line #578	-> byte code offset #23
/*     */     //   Java source line #579	-> byte code offset #27
/*     */     //   Java source line #580	-> byte code offset #33
/*     */     //   Java source line #579	-> byte code offset #35
/*     */     //   Java source line #582	-> byte code offset #41
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	42	0	table_create	String
/*     */     //   1	37	1	context	Connection
/*     */     //   3	34	2	ps	PreparedStatement
/*     */     //   26	8	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	26	26	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static void makeGlobalTable(String table_create)
/*     */     throws SQLException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_1
/*     */     //   2: aconst_null
/*     */     //   3: astore_2
/*     */     //   4: invokestatic 389	com/avrgaming/civcraft/database/SQL:getGlobalConnection	()Ljava/sql/Connection;
/*     */     //   7: astore_1
/*     */     //   8: aload_1
/*     */     //   9: aload_0
/*     */     //   10: invokeinterface 409 2 0
/*     */     //   15: astore_2
/*     */     //   16: aload_2
/*     */     //   17: invokeinterface 413 1 0
/*     */     //   22: pop
/*     */     //   23: goto +12 -> 35
/*     */     //   26: astore_3
/*     */     //   27: aconst_null
/*     */     //   28: aload_2
/*     */     //   29: aload_1
/*     */     //   30: invokestatic 372	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   33: aload_3
/*     */     //   34: athrow
/*     */     //   35: aconst_null
/*     */     //   36: aload_2
/*     */     //   37: aload_1
/*     */     //   38: invokestatic 372	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   41: return
/*     */     // Line number table:
/*     */     //   Java source line #585	-> byte code offset #0
/*     */     //   Java source line #586	-> byte code offset #2
/*     */     //   Java source line #589	-> byte code offset #4
/*     */     //   Java source line #590	-> byte code offset #8
/*     */     //   Java source line #591	-> byte code offset #16
/*     */     //   Java source line #592	-> byte code offset #23
/*     */     //   Java source line #593	-> byte code offset #27
/*     */     //   Java source line #594	-> byte code offset #33
/*     */     //   Java source line #593	-> byte code offset #35
/*     */     //   Java source line #595	-> byte code offset #41
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	42	0	table_create	String
/*     */     //   1	37	1	context	Connection
/*     */     //   3	34	2	ps	PreparedStatement
/*     */     //   26	8	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	26	26	finally
/*     */   }
/*     */   
/*     */   public static void close(ResultSet rs, PreparedStatement ps, Connection context)
/*     */   {
/* 598 */     if (rs != null) {
/*     */       try {
/* 600 */         rs.close();
/*     */       } catch (SQLException e) {
/* 602 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/* 606 */     if (ps != null) {
/*     */       try {
/* 608 */         ps.close();
/*     */       } catch (SQLException e) {
/* 610 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/* 614 */     if (context != null) {
/*     */       try {
/* 616 */         context.close();
/*     */       } catch (SQLException e) {
/* 618 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\database\SQL.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
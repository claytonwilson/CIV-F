/*     */ package com.avrgaming.civcraft.sessiondb;
/*     */ 
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import java.io.PrintStream;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public class SessionDatabase
/*     */ {
/*     */   private boolean initalized;
/*     */   String tb_prefix;
/*  45 */   private ConcurrentHashMap<String, ArrayList<SessionEntry>> cache = new ConcurrentHashMap();
/*     */   
/*     */   public SessionDatabase() {
/*  48 */     this.tb_prefix = SQL.tb_prefix;
/*     */   }
/*     */   
/*  51 */   public static String TABLE_NAME = "SESSIONS";
/*  52 */   public static String GLOBAL_TABLE_NAME = "GLOBAL_SESSIONS";
/*     */   
/*  54 */   public static void init() throws SQLException { System.out.println("================= SESSION DB INIT ======================");
/*     */     
/*  56 */     if (!SQL.hasTable(TABLE_NAME)) {
/*  57 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + TABLE_NAME + " (" + 
/*  58 */         "`request_id` int(11) unsigned NOT NULL auto_increment," + 
/*  59 */         "`key` mediumtext," + 
/*  60 */         "`value` mediumtext," + 
/*  61 */         "`town_id` int(11)," + 
/*  62 */         "`civ_id` int(11)," + 
/*  63 */         "`struct_id` int(11)," + 
/*  64 */         "`time` long," + 
/*  65 */         "PRIMARY KEY (`request_id`)" + ")";
/*     */       
/*  67 */       SQL.makeTable(table_create);
/*  68 */       CivLog.info("Created " + TABLE_NAME + " table");
/*     */     } else {
/*  70 */       CivLog.info(TABLE_NAME + " table OK!");
/*     */     }
/*  72 */     System.out.println("==================================================");
/*     */     
/*  74 */     System.out.println("================= GLOBAL SESSION DB INIT ======================");
/*     */     
/*  76 */     if (!SQL.hasGlobalTable(GLOBAL_TABLE_NAME)) {
/*  77 */       String table_create = "CREATE TABLE " + GLOBAL_TABLE_NAME + " (" + 
/*  78 */         "`request_id` int(11) unsigned NOT NULL auto_increment," + 
/*  79 */         "`key` mediumtext," + 
/*  80 */         "`value` mediumtext," + 
/*  81 */         "`town_id` int(11)," + 
/*  82 */         "`civ_id` int(11)," + 
/*  83 */         "`struct_id` int(11)," + 
/*  84 */         "`time` long," + 
/*  85 */         "PRIMARY KEY (`request_id`)" + ")";
/*     */       
/*  87 */       SQL.makeGlobalTable(table_create);
/*  88 */       CivLog.info("Created " + GLOBAL_TABLE_NAME + " table");
/*     */     } else {
/*  90 */       CivLog.info(GLOBAL_TABLE_NAME + " table OK!");
/*     */     }
/*  92 */     System.out.println("==================================================");
/*     */   }
/*     */   
/*     */   public boolean add(String key, String value, int civ_id, int town_id, int struct_id) {
/*  96 */     SessionEntry se = new SessionEntry();
/*  97 */     se.key = key;
/*  98 */     se.value = value;
/*  99 */     se.civ_id = civ_id;
/* 100 */     se.town_id = town_id;
/* 101 */     se.struct_id = struct_id;
/* 102 */     se.time = System.currentTimeMillis();
/* 103 */     se.request_id = -1;
/*     */     
/*     */ 
/* 106 */     ArrayList<SessionEntry> entries = (ArrayList)this.cache.get(key);
/* 107 */     if (entries == null) {
/* 108 */       entries = new ArrayList();
/*     */     }
/* 110 */     entries.add(se);
/*     */     
/*     */ 
/*     */ 
/* 114 */     SessionAsyncRequest request = new SessionAsyncRequest(SessionAsyncRequest.Operation.ADD, SessionAsyncRequest.Database.GAME, this.tb_prefix, se);
/* 115 */     request.queue();
/* 116 */     return true;
/*     */   }
/*     */   
/*     */   public boolean global_add(String key, String value) {
/* 120 */     SessionEntry se = new SessionEntry();
/* 121 */     se.key = key;
/* 122 */     se.value = value;
/* 123 */     se.time = System.currentTimeMillis();
/* 124 */     se.request_id = -1;
/*     */     
/*     */ 
/* 127 */     SessionAsyncRequest request = new SessionAsyncRequest(SessionAsyncRequest.Operation.ADD, SessionAsyncRequest.Database.GLOBAL, "GLOBAL_", se);
/* 128 */     request.queue();
/* 129 */     return true;
/*     */   }
/*     */   
/*     */   public ArrayList<SessionEntry> lookup(String key) {
/* 133 */     Connection cntx = null;
/* 134 */     ResultSet rs = null;
/* 135 */     PreparedStatement ps = null;
/*     */     
/* 137 */     ArrayList<SessionEntry> retList = null;
/*     */     
/*     */     try
/*     */     {
/* 141 */       retList = (ArrayList)this.cache.get(key);
/* 142 */       ArrayList<SessionEntry> localArrayList1; if (retList != null) {
/* 143 */         return retList;
/*     */       }
/*     */       
/*     */ 
/* 147 */       retList = new ArrayList();
/* 148 */       String code = "SELECT * FROM `" + this.tb_prefix + "SESSIONS` WHERE `key` = ?";
/*     */       try
/*     */       {
/* 151 */         cntx = SQL.getGameConnection();
/* 152 */         ps = cntx.prepareStatement(code);
/* 153 */         ps.setString(1, key);
/*     */         
/* 155 */         rs = ps.executeQuery();
/*     */         
/* 157 */         while (rs.next()) {
/* 158 */           SessionEntry se = new SessionEntry();
/*     */           
/*     */ 
/* 161 */           se.request_id = rs.getInt("request_id");
/*     */           
/* 163 */           String line = rs.getString("key");
/*     */           
/* 165 */           if (line == null) {
/*     */             break;
/*     */           }
/* 168 */           se.key = line;
/*     */           
/* 170 */           line = rs.getString("value");
/* 171 */           if (line == null) {
/*     */             break;
/*     */           }
/* 174 */           se.value = line;
/*     */           
/* 176 */           se.civ_id = rs.getInt("civ_id");
/* 177 */           se.town_id = rs.getInt("town_id");
/* 178 */           se.struct_id = rs.getInt("struct_id");
/*     */           
/* 180 */           long time = rs.getLong("time");
/* 181 */           se.time = time;
/*     */           
/* 183 */           retList.add(se);
/*     */         }
/*     */       }
/*     */       catch (SQLException e) {
/* 187 */         CivLog.error("SQL: select sql error " + e.getMessage() + " --> " + code);
/*     */       }
/*     */       
/*     */ 
/* 191 */       this.cache.put(key, retList);
/*     */       
/* 193 */       return retList;
/*     */     } finally {
/* 195 */       SQL.close(rs, ps, cntx);
/*     */     }
/*     */   }
/*     */   
/*     */   public ArrayList<SessionEntry> global_lookup(String key) {
/* 200 */     Connection global_context = null;
/* 201 */     ResultSet rs = null;
/* 202 */     PreparedStatement s = null;
/* 203 */     ArrayList<SessionEntry> retList = new ArrayList();
/*     */     try
/*     */     {
/*     */       try {
/* 207 */         global_context = SQL.getGlobalConnection();
/*     */         
/* 209 */         String code = "SELECT * FROM `GLOBAL_SESSIONS` WHERE `key` = ?";
/* 210 */         s = global_context.prepareStatement(code);
/* 211 */         s.setString(1, key);
/*     */         
/* 213 */         rs = s.executeQuery();
/*     */         
/* 215 */         while (rs.next()) {
/* 216 */           SessionEntry se = new SessionEntry();
/*     */           
/*     */ 
/* 219 */           se.request_id = rs.getInt("request_id");
/*     */           
/* 221 */           String line = rs.getString("key");
/*     */           
/* 223 */           if (line == null) {
/*     */             break;
/*     */           }
/* 226 */           se.key = line;
/*     */           
/* 228 */           line = rs.getString("value");
/* 229 */           if (line == null) {
/*     */             break;
/*     */           }
/* 232 */           se.value = line;
/*     */           
/* 234 */           long time = rs.getLong("time");
/* 235 */           se.time = time;
/*     */           
/* 237 */           retList.add(se);
/*     */         }
/*     */       } catch (SQLException e) {
/* 240 */         CivLog.error("SQL: select sql error " + e.getMessage());
/*     */       }
/*     */       
/*     */ 
/* 244 */       this.cache.put(key, retList);
/* 245 */       return retList;
/*     */     } finally {
/* 247 */       SQL.close(rs, s, global_context);
/*     */     }
/*     */   }
/*     */   
/*     */   public void test()
/*     */   {
/* 253 */     add("ThisTestKey", "ThisTestData", 0, 0, 0);
/*     */     
/* 255 */     for (SessionEntry se : lookup("ThisTestKey")) {
/* 256 */       System.out.println("GOT ME SOME:" + se.value);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isInitialized()
/*     */   {
/* 262 */     return this.initalized;
/*     */   }
/*     */   
/*     */   public boolean delete_all(String key) {
/* 266 */     SessionEntry se = new SessionEntry();
/* 267 */     se.key = key;
/*     */     
/*     */ 
/* 270 */     this.cache.remove(key);
/*     */     
/*     */ 
/* 273 */     SessionAsyncRequest request = new SessionAsyncRequest(SessionAsyncRequest.Operation.DELETE_ALL, SessionAsyncRequest.Database.GAME, this.tb_prefix, se);
/* 274 */     request.queue();
/* 275 */     return true;
/*     */   }
/*     */   
/*     */   public boolean delete(int request_id, String key) {
/* 279 */     SessionEntry se = new SessionEntry();
/* 280 */     se.request_id = request_id;
/*     */     
/*     */ 
/* 283 */     ArrayList<SessionEntry> entries = (ArrayList)this.cache.get(key);
/* 284 */     if (entries != null) {
/* 285 */       for (SessionEntry e : entries) {
/* 286 */         if (e.request_id == request_id) {
/* 287 */           entries.remove(e);
/* 288 */           break;
/*     */         }
/*     */       }
/*     */       
/* 292 */       if (entries.size() == 0) {
/* 293 */         this.cache.remove(key);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 299 */     SessionAsyncRequest request = new SessionAsyncRequest(SessionAsyncRequest.Operation.DELETE, SessionAsyncRequest.Database.GAME, this.tb_prefix, se);
/* 300 */     request.queue();
/* 301 */     return true;
/*     */   }
/*     */   
/*     */   public boolean global_update(int request_id, String key, String newValue) {
/* 305 */     SessionEntry se = new SessionEntry();
/* 306 */     se.request_id = request_id;
/* 307 */     se.value = newValue;
/* 308 */     se.key = key;
/*     */     
/*     */ 
/* 311 */     SessionAsyncRequest request = new SessionAsyncRequest(SessionAsyncRequest.Operation.UPDATE, SessionAsyncRequest.Database.GLOBAL, "GLOBAL_", se);
/* 312 */     request.queue();
/* 313 */     return true;
/*     */   }
/*     */   
/*     */   public boolean update(int request_id, String key, String newValue)
/*     */   {
/* 318 */     SessionEntry se = new SessionEntry();
/* 319 */     se.request_id = request_id;
/* 320 */     se.value = newValue;
/* 321 */     se.key = key;
/*     */     
/*     */ 
/* 324 */     ArrayList<SessionEntry> entries = (ArrayList)this.cache.get(key);
/* 325 */     if (entries != null) {
/* 326 */       for (SessionEntry e : entries) {
/* 327 */         if (e.request_id == request_id) {
/* 328 */           e.value = newValue;
/*     */         }
/*     */       }
/*     */     } else {
/* 332 */       entries = new ArrayList();
/* 333 */       entries.add(se);
/* 334 */       this.cache.put(se.key, entries);
/*     */     }
/*     */     
/*     */ 
/* 338 */     SessionAsyncRequest request = new SessionAsyncRequest(SessionAsyncRequest.Operation.UPDATE, SessionAsyncRequest.Database.GAME, this.tb_prefix, se);
/* 339 */     request.queue();
/* 340 */     return true;
/*     */   }
/*     */   
/*     */   public void deleteAllForTown(Town town) {}
/*     */   
/*     */   public void deleteAllForBuildable(Buildable buildable) {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\sessiondb\SessionDatabase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
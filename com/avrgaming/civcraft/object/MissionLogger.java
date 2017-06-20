/*     */ package com.avrgaming.civcraft.object;
/*     */ 
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
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
/*     */ public class MissionLogger
/*     */ {
/*  37 */   public static String TABLE_NAME = "MISSION_LOGS";
/*     */   
/*  39 */   public static void init() throws SQLException { if (!SQL.hasTable(TABLE_NAME)) {
/*  40 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + TABLE_NAME + " (" + 
/*  41 */         "`id` int(11) unsigned NOT NULL auto_increment," + 
/*  42 */         "`town_id` int(11) unsigned DEFAULT 0," + 
/*  43 */         "`target_id` int(11) unsigned DEFAULT 0," + 
/*  44 */         "`time` long," + 
/*  45 */         "`playerName` mediumtext," + 
/*  46 */         "`missionName` mediumtext," + 
/*  47 */         "`result` mediumtext," + 
/*  48 */         "PRIMARY KEY (`id`)" + ")";
/*     */       
/*  50 */       SQL.makeTable(table_create);
/*  51 */       CivLog.info("Created " + TABLE_NAME + " table");
/*     */     } else {
/*  53 */       CivLog.info(TABLE_NAME + " table OK!");
/*     */     }
/*     */   }
/*     */   
/*     */   public static void logMission(Town town, Town target, String playerName, String missionName, String result)
/*     */   {
/*  59 */     HashMap<String, Object> hashmap = new HashMap();
/*     */     
/*  61 */     hashmap.put("town_id", Integer.valueOf(town.getId()));
/*  62 */     hashmap.put("target_id", Integer.valueOf(target.getId()));
/*  63 */     hashmap.put("time", new Date());
/*  64 */     hashmap.put("playerName", playerName);
/*  65 */     hashmap.put("missionName", missionName);
/*  66 */     hashmap.put("result", result);
/*     */     try
/*     */     {
/*  69 */       SQL.insertNow(hashmap, TABLE_NAME);
/*     */     } catch (SQLException e) {
/*  71 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public static ArrayList<String> getMissionLogs(Town town) {
/*  76 */     Connection context = null;
/*  77 */     ResultSet rs = null;
/*  78 */     PreparedStatement ps = null;
/*     */     try
/*     */     {
/*  81 */       ArrayList<String> out = new ArrayList();
/*     */       try {
/*  83 */         context = SQL.getGameConnection();
/*  84 */         ps = context.prepareStatement("SELECT * FROM " + SQL.tb_prefix + TABLE_NAME + " WHERE `town_id` = ?");
/*  85 */         ps.setInt(1, town.getId());
/*  86 */         rs = ps.executeQuery();
/*     */         
/*  88 */         SimpleDateFormat sdf = new SimpleDateFormat("M/dd h:mm:ss a z");
/*  89 */         while (rs.next()) {
/*  90 */           Date date = new Date(rs.getLong("time"));
/*  91 */           Town target = CivGlobal.getTownFromId(rs.getInt("target_id"));
/*  92 */           if (target != null)
/*     */           {
/*     */ 
/*     */ 
/*  96 */             String str = sdf.format(date) + " - " + rs.getString("playerName") + ":" + target.getName() + ":" + rs.getString("missionName") + " -- " + rs.getString("result");
/*  97 */             out.add(str);
/*     */           }
/*     */         }
/* 100 */       } catch (SQLException e) { e.printStackTrace();
/*     */       }
/*     */       
/* 103 */       return out;
/*     */     } finally {
/* 105 */       SQL.close(rs, ps, context);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\MissionLogger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
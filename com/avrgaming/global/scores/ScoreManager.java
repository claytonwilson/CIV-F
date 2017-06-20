/*     */ package com.avrgaming.global.scores;
/*     */ 
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import java.io.PrintStream;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import org.bukkit.Bukkit;
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
/*     */ public class ScoreManager
/*     */ {
/*  34 */   public static String TOWN_TABLE_NAME = "SCORES_TOWNS";
/*  35 */   public static String CIV_TABLE_NAME = "SCORES_CIVS";
/*     */   
/*     */   public static String getCivKey(Civilization civ) {
/*  38 */     return Bukkit.getServerName() + ":" + civ.getName();
/*     */   }
/*     */   
/*     */   public static String getTownKey(Town town) {
/*  42 */     return Bukkit.getServerName() + ":" + town.getName();
/*     */   }
/*     */   
/*     */   public static void init() throws SQLException {
/*  46 */     System.out.println("================= SCORE_TOWN INIT ======================");
/*     */     
/*     */ 
/*  49 */     if (!SQL.hasGlobalTable(TOWN_TABLE_NAME)) {
/*  50 */       String table_create = "CREATE TABLE " + TOWN_TABLE_NAME + " (" + 
/*  51 */         "`key` VARCHAR(128)," + 
/*  52 */         "`server` VARCHAR(64)," + 
/*  53 */         "`local_id` int(11)," + 
/*  54 */         "`local_name` mediumtext," + 
/*  55 */         "`local_civ_name` mediumtext," + 
/*  56 */         "`points` int(11)," + 
/*  57 */         "INDEX (`server`)," + 
/*  58 */         "PRIMARY KEY (`key`)" + ")";
/*     */       
/*  60 */       SQL.makeGlobalTable(table_create);
/*  61 */       CivLog.info("Created " + TOWN_TABLE_NAME + " table");
/*     */     } else {
/*  63 */       CivLog.info(TOWN_TABLE_NAME + " table OK!");
/*     */     }
/*     */     
/*  66 */     System.out.println("==================================================");
/*     */     
/*  68 */     System.out.println("================= SCORE_CIV INIT ======================");
/*     */     
/*     */ 
/*  71 */     if (!SQL.hasGlobalTable(CIV_TABLE_NAME)) {
/*  72 */       String table_create = "CREATE TABLE " + CIV_TABLE_NAME + " (" + 
/*  73 */         "`key` VARCHAR(128)," + 
/*  74 */         "`server` VARCHAR(64)," + 
/*  75 */         "`local_id` int(11)," + 
/*  76 */         "`local_name` mediumtext," + 
/*  77 */         "`local_capitol_name` mediumtext," + 
/*  78 */         "`points` int(11)," + 
/*  79 */         "INDEX (`server`)," + 
/*  80 */         "PRIMARY KEY (`key`)" + ")";
/*     */       
/*  82 */       SQL.makeGlobalTable(table_create);
/*  83 */       CivLog.info("Created " + CIV_TABLE_NAME + " table");
/*     */     } else {
/*  85 */       CivLog.info(CIV_TABLE_NAME + " table OK!");
/*     */     }
/*     */     
/*  88 */     System.out.println("==================================================");
/*     */   }
/*     */   
/*     */   public static void UpdateScore(Civilization civ, int points) throws SQLException {
/*  92 */     Connection global_context = null;
/*  93 */     PreparedStatement s = null;
/*     */     try
/*     */     {
/*  96 */       global_context = SQL.getGlobalConnection();
/*  97 */       String query = "INSERT INTO `" + CIV_TABLE_NAME + "` (`key`, `server`, `local_id`, `local_name`, `local_capitol_name`, `points`) " + 
/*  98 */         "VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE `local_name`=?, `local_capitol_name`=?, `points`=?";
/*  99 */       s = global_context.prepareStatement(query);
/*     */       
/* 101 */       s.setString(1, getCivKey(civ));
/* 102 */       s.setString(2, Bukkit.getServerName());
/* 103 */       s.setInt(3, civ.getId());
/* 104 */       s.setString(4, civ.getName());
/* 105 */       s.setString(5, civ.getCapitolName());
/* 106 */       s.setInt(6, points);
/*     */       
/* 108 */       s.setString(7, civ.getName());
/* 109 */       s.setString(8, civ.getCapitolName());
/* 110 */       s.setInt(9, points);
/*     */       
/* 112 */       int rs = s.executeUpdate();
/* 113 */       if (rs == 0) {
/* 114 */         throw new SQLException("Could not execute SQL code:" + query);
/*     */       }
/*     */     }
/*     */     finally {
/* 118 */       SQL.close(null, s, global_context); } SQL.close(null, s, global_context);
/*     */   }
/*     */   
/*     */   public static void UpdateScore(Town town, int points) throws SQLException
/*     */   {
/* 123 */     Connection global_context = null;
/* 124 */     PreparedStatement s = null;
/*     */     try
/*     */     {
/* 127 */       global_context = SQL.getGlobalConnection();
/* 128 */       String query = "INSERT INTO `" + TOWN_TABLE_NAME + "` (`key`, `server`, `local_id`, `local_name`, `local_civ_name`, `points`) " + 
/* 129 */         "VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE `local_name`=?, `local_civ_name`=?, `points`=?";
/* 130 */       s = global_context.prepareStatement(query);
/*     */       
/* 132 */       s.setString(1, getTownKey(town));
/* 133 */       s.setString(2, Bukkit.getServerName());
/* 134 */       s.setInt(3, town.getId());
/* 135 */       s.setString(4, town.getName());
/* 136 */       s.setString(5, town.getCiv().getName());
/* 137 */       s.setInt(6, points);
/*     */       
/* 139 */       s.setString(7, town.getName());
/* 140 */       s.setString(8, town.getCiv().getName());
/* 141 */       s.setInt(9, points);
/*     */       
/* 143 */       int rs = s.executeUpdate();
/* 144 */       if (rs == 0) {
/* 145 */         throw new SQLException("Could not execute SQL code:" + query);
/*     */       }
/*     */     }
/*     */     finally {
/* 149 */       SQL.close(null, s, global_context); } SQL.close(null, s, global_context);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\global\scores\ScoreManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
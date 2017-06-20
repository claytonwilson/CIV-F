/*     */ package com.avrgaming.global.reports;
/*     */ 
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import java.io.PrintStream;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Date;
/*     */ import org.bukkit.Bukkit;
/*     */ 
/*     */ public class ReportManager
/*     */ {
/*     */   public static enum ReportType
/*     */   {
/*  17 */     LANGUAGE, 
/*  18 */     EXPLOITING, 
/*  19 */     HARASSMENT, 
/*  20 */     CHEATING, 
/*  21 */     OTHER;
/*     */   }
/*     */   
/*  24 */   public static String TABLE_NAME = "REPORTS";
/*     */   
/*  26 */   public static void init() throws SQLException { System.out.println("================= REPORTS INIT ======================");
/*     */     
/*     */ 
/*  29 */     if (!SQL.hasGlobalTable(TABLE_NAME)) {
/*  30 */       String table_create = "CREATE TABLE " + TABLE_NAME + " (" + 
/*  31 */         "`id` int(11) unsigned NOT NULL auto_increment," + 
/*  32 */         "`name` mediumtext," + 
/*  33 */         "`server` mediumtext," + 
/*  34 */         "`report_type` mediumtext," + 
/*  35 */         "`message` mediumtext," + 
/*  36 */         "`reported_by` mediumtext," + 
/*  37 */         "`time` long," + 
/*  38 */         "PRIMARY KEY (`id`)" + ")";
/*     */       
/*  40 */       SQL.makeGlobalTable(table_create);
/*  41 */       CivLog.info("Created " + TABLE_NAME + " table");
/*     */     } else {
/*  43 */       CivLog.info(TABLE_NAME + " table OK!");
/*     */     }
/*     */     
/*  46 */     System.out.println("==================================================");
/*     */   }
/*     */   
/*     */   public static String getReportTypes() {
/*  50 */     String out = "";
/*  51 */     ReportType[] arrayOfReportType; int j = (arrayOfReportType = ReportType.values()).length; for (int i = 0; i < j; i++) { ReportType type = arrayOfReportType[i];
/*  52 */       out = out + type.name().toLowerCase() + ", ";
/*     */     }
/*  54 */     return out;
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
/*     */   public static void reportPlayer(String name, ReportType type, String message, String reportedBy)
/*     */   {
/*  82 */     TaskMaster.asyncTask(new Runnable()
/*     */     {
/*     */       String reportType;
/*     */       String message;
/*     */       String reportedBy;
/*     */       
/*     */       public void run()
/*     */       {
/*     */         try
/*     */         {
/*  75 */           ReportManager.reportPlayerSync(ReportManager.this, this.reportType, this.message, this.reportedBy);
/*     */         } catch (SQLException e) {
/*  77 */           e.printStackTrace();
/*     */         }
/*     */         
/*     */       }
/*     */       
/*  82 */     }, 0L);
/*     */   }
/*     */   
/*     */   private static void reportPlayerSync(String name, String reportType, String message, String reportedBy) throws SQLException
/*     */   {
/*  87 */     Connection global_context = null;
/*  88 */     PreparedStatement s = null;
/*     */     try
/*     */     {
/*  91 */       global_context = SQL.getGlobalConnection();
/*  92 */       String query = "INSERT INTO `" + TABLE_NAME + "` (`name`, `server`, `report_type`, `message`, `reported_by`, `time`) " + 
/*  93 */         "VALUES (?, ?, ?, ?, ?, ?)";
/*  94 */       s = global_context.prepareStatement(query);
/*     */       
/*  96 */       Date now = new Date();
/*     */       
/*  98 */       s.setString(1, name);
/*  99 */       s.setString(2, Bukkit.getServerName());
/* 100 */       s.setString(3, reportType);
/* 101 */       s.setString(4, message);
/* 102 */       s.setString(5, reportedBy);
/* 103 */       s.setLong(6, now.getTime());
/*     */       
/* 105 */       int rs = s.executeUpdate();
/* 106 */       if (rs == 0) {
/* 107 */         throw new SQLException("Could not execute SQL code:" + query);
/*     */       }
/*     */     } finally {
/* 110 */       SQL.close(null, s, global_context); } SQL.close(null, s, global_context);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\global\reports\ReportManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
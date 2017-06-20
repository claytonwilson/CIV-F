/*    */ package com.avrgaming.civcraft.main;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import java.io.IOException;
/*    */ import java.util.logging.FileHandler;
/*    */ import java.util.logging.Logger;
/*    */ import java.util.logging.SimpleFormatter;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
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
/*    */ public class CivLog
/*    */ {
/*    */   public static JavaPlugin plugin;
/*    */   private static Logger cleanupLogger;
/*    */   
/*    */   public static void init(JavaPlugin plugin)
/*    */   {
/* 36 */     plugin = plugin;
/*    */     
/* 38 */     cleanupLogger = Logger.getLogger("cleanUp");
/*    */     
/*    */     try
/*    */     {
/* 42 */       FileHandler fh = new FileHandler("cleanUp.log");
/* 43 */       cleanupLogger.addHandler(fh);
/* 44 */       SimpleFormatter formatter = new SimpleFormatter();
/* 45 */       fh.setFormatter(formatter);
/*    */     } catch (IOException e) {
/* 47 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */   
/*    */   public static void heading(String title) {
/* 52 */     plugin.getLogger().info("========= " + title + " =========");
/*    */   }
/*    */   
/*    */   public static void info(String message) {
/* 56 */     plugin.getLogger().info(message);
/*    */   }
/*    */   
/*    */   public static void debug(String message) {
/* 60 */     plugin.getLogger().info("[DEBUG] " + message);
/*    */   }
/*    */   
/*    */   public static void warning(String message) {
/* 64 */     if (message == null) {
/*    */       try {
/* 66 */         throw new CivException("Null warning message!");
/*    */       } catch (CivException e) {
/* 68 */         e.printStackTrace();
/*    */       }
/*    */     }
/* 71 */     if (CivGlobal.warningsEnabled) {
/* 72 */       plugin.getLogger().info("[WARNING] " + message);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void error(String message) {
/* 77 */     plugin.getLogger().severe(message);
/*    */   }
/*    */   
/*    */   public static void adminlog(String name, String message) {
/* 81 */     plugin.getLogger().info("[ADMIN:" + name + "] " + message);
/*    */   }
/*    */   
/*    */   public static void cleanupLog(String message) {
/* 85 */     info(message);
/* 86 */     cleanupLogger.info(message);
/*    */   }
/*    */   
/*    */   public static void exception(String string, Exception e)
/*    */   {
/* 91 */     e.printStackTrace();
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\main\CivLog.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
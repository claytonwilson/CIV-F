/*    */ package com.avrgaming.civcraft.config;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
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
/*    */ public class ConfigCultureLevel
/*    */ {
/*    */   public int level;
/*    */   public int amount;
/*    */   public int chunks;
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Map<Integer, ConfigCultureLevel> levels)
/*    */   {
/* 34 */     levels.clear();
/* 35 */     List<Map<?, ?>> culture_levels = cfg.getMapList("culture_levels");
/* 36 */     for (Map<?, ?> level : culture_levels) {
/* 37 */       ConfigCultureLevel culture_level = new ConfigCultureLevel();
/* 38 */       culture_level.level = ((Integer)level.get("level")).intValue();
/* 39 */       culture_level.amount = ((Integer)level.get("amount")).intValue();
/* 40 */       culture_level.chunks = ((Integer)level.get("chunks")).intValue();
/* 41 */       levels.put(Integer.valueOf(culture_level.level), culture_level);
/*    */     }
/* 43 */     CivLog.info("Loaded " + levels.size() + " culture levels.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigCultureLevel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
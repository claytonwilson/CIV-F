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
/*    */ 
/*    */ public class ConfigTownLevel
/*    */ {
/*    */   public int level;
/*    */   public String title;
/*    */   public double upkeep;
/*    */   public int plots;
/*    */   public double plot_cost;
/*    */   public int tile_improvements;
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Map<Integer, ConfigTownLevel> levels)
/*    */   {
/* 38 */     levels.clear();
/* 39 */     List<Map<?, ?>> culture_levels = cfg.getMapList("town_levels");
/* 40 */     for (Map<?, ?> level : culture_levels) {
/* 41 */       ConfigTownLevel town_level = new ConfigTownLevel();
/* 42 */       town_level.level = ((Integer)level.get("level")).intValue();
/* 43 */       town_level.title = ((String)level.get("title"));
/* 44 */       town_level.upkeep = ((Double)level.get("upkeep")).doubleValue();
/* 45 */       town_level.plots = ((Integer)level.get("plots")).intValue();
/* 46 */       town_level.plot_cost = ((Double)level.get("plot_cost")).doubleValue();
/* 47 */       town_level.tile_improvements = ((Integer)level.get("tile_improvements")).intValue();
/*    */       
/* 49 */       levels.put(Integer.valueOf(town_level.level), town_level);
/*    */     }
/* 51 */     CivLog.info("Loaded " + levels.size() + " town levels.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigTownLevel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
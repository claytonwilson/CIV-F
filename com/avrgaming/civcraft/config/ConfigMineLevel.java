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
/*    */ public class ConfigMineLevel
/*    */ {
/*    */   public int level;
/*    */   public int amount;
/*    */   public int count;
/*    */   public double hammers;
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Map<Integer, ConfigMineLevel> levels)
/*    */   {
/* 35 */     levels.clear();
/* 36 */     List<Map<?, ?>> mine_levels = cfg.getMapList("mine_levels");
/* 37 */     for (Map<?, ?> level : mine_levels) {
/* 38 */       ConfigMineLevel mine_level = new ConfigMineLevel();
/* 39 */       mine_level.level = ((Integer)level.get("level")).intValue();
/* 40 */       mine_level.amount = ((Integer)level.get("amount")).intValue();
/* 41 */       mine_level.hammers = ((Double)level.get("hammers")).doubleValue();
/* 42 */       mine_level.count = ((Integer)level.get("count")).intValue();
/* 43 */       levels.put(Integer.valueOf(mine_level.level), mine_level);
/*    */     }
/* 45 */     CivLog.info("Loaded " + levels.size() + " mine levels.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigMineLevel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
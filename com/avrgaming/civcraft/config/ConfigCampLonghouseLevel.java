/*    */ package com.avrgaming.civcraft.config;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
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
/*    */ 
/*    */ public class ConfigCampLonghouseLevel
/*    */ {
/*    */   public int level;
/*    */   public Map<Integer, Integer> consumes;
/*    */   public int count;
/*    */   public double coins;
/*    */   
/*    */   public ConfigCampLonghouseLevel() {}
/*    */   
/*    */   public ConfigCampLonghouseLevel(ConfigCampLonghouseLevel currentlvl)
/*    */   {
/* 41 */     this.level = currentlvl.level;
/* 42 */     this.count = currentlvl.count;
/* 43 */     this.coins = currentlvl.coins;
/*    */     
/* 45 */     this.consumes = new HashMap();
/* 46 */     for (Map.Entry<Integer, Integer> entry : currentlvl.consumes.entrySet()) {
/* 47 */       this.consumes.put((Integer)entry.getKey(), (Integer)entry.getValue());
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public static void loadConfig(FileConfiguration cfg, Map<Integer, ConfigCampLonghouseLevel> longhouse_levels)
/*    */   {
/* 54 */     longhouse_levels.clear();
/* 55 */     List<Map<?, ?>> list = cfg.getMapList("longhouse_levels");
/* 56 */     Map<Integer, Integer> consumes_list = null;
/* 57 */     for (Map<?, ?> cl : list) {
/* 58 */       List<?> consumes = (List)cl.get("consumes");
/* 59 */       if (consumes != null) {
/* 60 */         consumes_list = new HashMap();
/* 61 */         for (int i = 0; i < consumes.size(); i++) {
/* 62 */           String line = (String)consumes.get(i);
/*    */           
/* 64 */           String[] split = line.split(",");
/* 65 */           consumes_list.put(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
/*    */         }
/*    */       }
/*    */       
/*    */ 
/* 70 */       ConfigCampLonghouseLevel level = new ConfigCampLonghouseLevel();
/* 71 */       level.level = ((Integer)cl.get("level")).intValue();
/* 72 */       level.consumes = consumes_list;
/* 73 */       level.count = ((Integer)cl.get("count")).intValue();
/* 74 */       level.coins = ((Double)cl.get("coins")).doubleValue();
/*    */       
/* 76 */       longhouse_levels.put(Integer.valueOf(level.level), level);
/*    */     }
/*    */     
/* 79 */     CivLog.info("Loaded " + longhouse_levels.size() + " longhouse levels.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigCampLonghouseLevel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
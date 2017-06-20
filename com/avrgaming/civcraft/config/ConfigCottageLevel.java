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
/*    */ public class ConfigCottageLevel
/*    */ {
/*    */   public int level;
/*    */   public Map<Integer, Integer> consumes;
/*    */   public int count;
/*    */   public double coins;
/*    */   
/*    */   public ConfigCottageLevel() {}
/*    */   
/*    */   public ConfigCottageLevel(ConfigCottageLevel currentlvl)
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
/*    */   public static void loadConfig(FileConfiguration cfg, Map<Integer, ConfigCottageLevel> cottage_levels)
/*    */   {
/* 54 */     cottage_levels.clear();
/* 55 */     List<Map<?, ?>> cottage_list = cfg.getMapList("cottage_levels");
/* 56 */     Map<Integer, Integer> consumes_list = null;
/* 57 */     for (Map<?, ?> cl : cottage_list) {
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
/* 70 */       ConfigCottageLevel cottagelevel = new ConfigCottageLevel();
/* 71 */       cottagelevel.level = ((Integer)cl.get("level")).intValue();
/* 72 */       cottagelevel.consumes = consumes_list;
/* 73 */       cottagelevel.count = ((Integer)cl.get("count")).intValue();
/* 74 */       cottagelevel.coins = ((Double)cl.get("coins")).doubleValue();
/*    */       
/* 76 */       cottage_levels.put(Integer.valueOf(cottagelevel.level), cottagelevel);
/*    */     }
/*    */     
/* 79 */     CivLog.info("Loaded " + cottage_levels.size() + " cottage levels.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigCottageLevel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
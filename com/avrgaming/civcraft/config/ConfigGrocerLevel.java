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
/*    */ public class ConfigGrocerLevel
/*    */ {
/*    */   public int level;
/*    */   public String itemName;
/*    */   public int itemId;
/*    */   public int itemData;
/*    */   public int amount;
/*    */   public double price;
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Map<Integer, ConfigGrocerLevel> levels)
/*    */   {
/* 37 */     levels.clear();
/* 38 */     List<Map<?, ?>> culture_levels = cfg.getMapList("grocer_levels");
/* 39 */     for (Map<?, ?> level : culture_levels) {
/* 40 */       ConfigGrocerLevel grocer_level = new ConfigGrocerLevel();
/* 41 */       grocer_level.level = ((Integer)level.get("level")).intValue();
/* 42 */       grocer_level.itemName = ((String)level.get("itemName"));
/* 43 */       grocer_level.itemId = ((Integer)level.get("itemId")).intValue();
/* 44 */       grocer_level.itemData = ((Integer)level.get("itemData")).intValue();
/* 45 */       grocer_level.amount = ((Integer)level.get("amount")).intValue();
/* 46 */       grocer_level.price = ((Double)level.get("price")).doubleValue();
/*    */       
/* 48 */       levels.put(Integer.valueOf(grocer_level.level), grocer_level);
/*    */     }
/* 50 */     CivLog.info("Loaded " + levels.size() + " grocer levels.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigGrocerLevel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
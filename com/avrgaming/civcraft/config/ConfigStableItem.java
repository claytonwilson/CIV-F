/*    */ package com.avrgaming.civcraft.config;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
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
/*    */ public class ConfigStableItem
/*    */ {
/*    */   public String name;
/*    */   public double cost;
/*    */   public int store_id;
/*    */   public int item_id;
/*    */   public int horse_id;
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Set<ConfigStableItem> items)
/*    */   {
/* 37 */     items.clear();
/* 38 */     List<Map<?, ?>> cfg_items = cfg.getMapList("stable_items");
/* 39 */     for (Map<?, ?> level : cfg_items) {
/* 40 */       ConfigStableItem itm = new ConfigStableItem();
/*    */       
/* 42 */       itm.name = ((String)level.get("name"));
/* 43 */       itm.cost = ((Double)level.get("cost")).doubleValue();
/* 44 */       itm.store_id = ((Integer)level.get("store_id")).intValue();
/* 45 */       itm.item_id = ((Integer)level.get("item_id")).intValue();
/* 46 */       itm.horse_id = ((Integer)level.get("horse_id")).intValue();
/*    */       
/* 48 */       items.add(itm);
/*    */     }
/* 50 */     CivLog.info("Loaded " + items.size() + " stable items.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigStableItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
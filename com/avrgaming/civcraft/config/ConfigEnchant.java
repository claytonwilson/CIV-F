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
/*    */ public class ConfigEnchant
/*    */ {
/*    */   public String id;
/*    */   public String name;
/*    */   public String description;
/*    */   public double cost;
/*    */   public String enchant_id;
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Map<String, ConfigEnchant> enchant_map)
/*    */   {
/* 37 */     enchant_map.clear();
/* 38 */     List<Map<?, ?>> techs = cfg.getMapList("enchants");
/* 39 */     for (Map<?, ?> level : techs) {
/* 40 */       ConfigEnchant enchant = new ConfigEnchant();
/*    */       
/* 42 */       enchant.id = ((String)level.get("id"));
/* 43 */       enchant.name = ((String)level.get("name"));
/* 44 */       enchant.description = ((String)level.get("description"));
/* 45 */       enchant.cost = ((Double)level.get("cost")).doubleValue();
/* 46 */       enchant.enchant_id = ((String)level.get("enchant_id"));
/* 47 */       enchant_map.put(enchant.id, enchant);
/*    */     }
/* 49 */     CivLog.info("Loaded " + enchant_map.size() + " enchantments.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigEnchant.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
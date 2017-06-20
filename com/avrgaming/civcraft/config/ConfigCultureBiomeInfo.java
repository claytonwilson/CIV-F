/*    */ package com.avrgaming.civcraft.config;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ 
/*    */ public class ConfigCultureBiomeInfo
/*    */ {
/*    */   public String name;
/*    */   public double coins;
/*    */   public double hammers;
/*    */   public double growth;
/*    */   public double happiness;
/*    */   public double beakers;
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Map<String, ConfigCultureBiomeInfo> culture_biomes)
/*    */   {
/* 19 */     culture_biomes.clear();
/* 20 */     List<Map<?, ?>> list = cfg.getMapList("culture_biomes");
/* 21 */     for (Map<?, ?> cl : list)
/*    */     {
/* 23 */       ConfigCultureBiomeInfo biome = new ConfigCultureBiomeInfo();
/* 24 */       biome.name = ((String)cl.get("name"));
/* 25 */       biome.coins = ((Double)cl.get("coins")).doubleValue();
/* 26 */       biome.hammers = ((Double)cl.get("hammers")).doubleValue();
/* 27 */       biome.growth = ((Double)cl.get("growth")).doubleValue();
/* 28 */       biome.happiness = ((Double)cl.get("happiness")).doubleValue();
/* 29 */       biome.beakers = ((Double)cl.get("beakers")).doubleValue();
/*    */       
/* 31 */       culture_biomes.put(biome.name, biome);
/*    */     }
/* 33 */     CivLog.info("Loaded " + culture_biomes.size() + " Culture Biomes.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigCultureBiomeInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
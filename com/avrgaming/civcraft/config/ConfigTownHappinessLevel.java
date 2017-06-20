/*    */ package com.avrgaming.civcraft.config;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ 
/*    */ public class ConfigTownHappinessLevel
/*    */ {
/*    */   public int level;
/*    */   public double happiness;
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Map<Integer, ConfigTownHappinessLevel> town_happiness_levels)
/*    */   {
/* 15 */     town_happiness_levels.clear();
/* 16 */     List<Map<?, ?>> list = cfg.getMapList("happiness.town_levels");
/* 17 */     for (Map<?, ?> cl : list)
/*    */     {
/* 19 */       ConfigTownHappinessLevel happy_level = new ConfigTownHappinessLevel();
/* 20 */       happy_level.level = ((Integer)cl.get("level")).intValue();
/* 21 */       happy_level.happiness = ((Double)cl.get("happiness")).doubleValue();
/*    */       
/* 23 */       town_happiness_levels.put(Integer.valueOf(happy_level.level), happy_level);
/*    */     }
/*    */     
/* 26 */     CivLog.info("Loaded " + town_happiness_levels.size() + " Town Happiness levels.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigTownHappinessLevel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
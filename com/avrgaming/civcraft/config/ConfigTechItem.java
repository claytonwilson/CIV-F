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
/*    */ public class ConfigTechItem
/*    */ {
/*    */   public int id;
/*    */   public String name;
/*    */   public String require_tech;
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Map<Integer, ConfigTechItem> tech_maps)
/*    */   {
/* 35 */     tech_maps.clear();
/* 36 */     List<Map<?, ?>> techs = cfg.getMapList("items");
/* 37 */     for (Map<?, ?> confTech : techs) {
/* 38 */       ConfigTechItem tech = new ConfigTechItem();
/*    */       
/* 40 */       tech.id = ((Integer)confTech.get("id")).intValue();
/* 41 */       tech.name = ((String)confTech.get("name"));
/* 42 */       tech.require_tech = ((String)confTech.get("require_tech"));
/* 43 */       tech_maps.put(Integer.valueOf(tech.id), tech);
/*    */     }
/* 45 */     CivLog.info("Loaded " + tech_maps.size() + " technologies.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigTechItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
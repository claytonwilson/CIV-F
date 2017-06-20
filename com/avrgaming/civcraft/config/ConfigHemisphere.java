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
/*    */ public class ConfigHemisphere
/*    */ {
/*    */   public String id;
/*    */   public int x_min;
/*    */   public int x_max;
/*    */   public int z_min;
/*    */   public int z_max;
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Map<String, ConfigHemisphere> hemis)
/*    */   {
/* 37 */     hemis.clear();
/* 38 */     List<Map<?, ?>> configHemis = cfg.getMapList("hemispheres");
/* 39 */     for (Map<?, ?> b : configHemis) {
/* 40 */       ConfigHemisphere buff = new ConfigHemisphere();
/* 41 */       buff.id = ((String)b.get("id"));
/* 42 */       buff.x_min = ((Integer)b.get("x_min")).intValue();
/* 43 */       buff.x_max = ((Integer)b.get("x_max")).intValue();
/* 44 */       buff.z_min = ((Integer)b.get("z_min")).intValue();
/* 45 */       buff.z_max = ((Integer)b.get("z_max")).intValue();
/* 46 */       hemis.put(buff.id, buff);
/*    */     }
/*    */     
/* 49 */     CivLog.info("Loaded " + hemis.size() + " Hemispheres.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigHemisphere.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
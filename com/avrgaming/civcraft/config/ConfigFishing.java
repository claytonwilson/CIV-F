/*    */ package com.avrgaming.civcraft.config;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import java.util.ArrayList;
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
/*    */ public class ConfigFishing
/*    */ {
/*    */   public String craftMatId;
/*    */   public int type_id;
/*    */   public double drop_chance;
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, ArrayList<ConfigFishing> configList)
/*    */   {
/* 36 */     configList.clear();
/* 37 */     List<Map<?, ?>> drops = cfg.getMapList("fishing_drops");
/* 38 */     for (Map<?, ?> item : drops) {
/* 39 */       ConfigFishing g = new ConfigFishing();
/*    */       
/* 41 */       g.craftMatId = ((String)item.get("craftMatId"));
/* 42 */       g.type_id = ((Integer)item.get("type_id")).intValue();
/* 43 */       g.drop_chance = ((Double)item.get("drop_chance")).doubleValue();
/*    */       
/* 45 */       configList.add(g);
/*    */     }
/*    */     
/* 48 */     CivLog.info("Loaded " + configList.size() + " fishing drops.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigFishing.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package com.avrgaming.civcraft.config;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import java.util.HashMap;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ 
/*    */ 
/*    */ public class ConfigValidMod
/*    */ {
/*    */   public String name;
/* 14 */   public LinkedList<Long> checksums = new LinkedList();
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, HashMap<String, ConfigValidMod> mods) {
/* 17 */     mods.clear();
/* 18 */     List<Map<?, ?>> cfg_items = cfg.getMapList("valid_mods");
/* 19 */     for (Map<?, ?> level : cfg_items) {
/* 20 */       ConfigValidMod itm = new ConfigValidMod();
/* 21 */       itm.name = ((String)level.get("name"));
/*    */       
/* 23 */       List<?> checksums = (List)level.get("checksums");
/* 24 */       for (Object chkObj : checksums) {
/* 25 */         itm.checksums.add(Long.valueOf((String)chkObj));
/*    */       }
/*    */       
/* 28 */       mods.put(itm.name, itm);
/*    */     }
/* 30 */     CivLog.info("Loaded " + mods.size() + " valid mods.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigValidMod.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
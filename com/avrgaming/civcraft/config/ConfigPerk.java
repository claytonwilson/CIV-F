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
/*    */ public class ConfigPerk
/*    */ {
/*    */   public String id;
/*    */   public LinkedList<HashMap<String, String>> components;
/*    */   public String display_name;
/*    */   public Integer type_id;
/*    */   public Integer data;
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Map<String, ConfigPerk> perk_map)
/*    */   {
/* 38 */     perk_map.clear();
/* 39 */     List<Map<?, ?>> perks = cfg.getMapList("perks");
/* 40 */     for (Map<?, ?> obj : perks) {
/* 41 */       ConfigPerk p = new ConfigPerk();
/*    */       
/* 43 */       p.id = ((String)obj.get("id"));
/* 44 */       p.display_name = ((String)obj.get("display_name"));
/* 45 */       p.type_id = ((Integer)obj.get("item_id"));
/* 46 */       p.data = ((Integer)obj.get("data"));
/*    */       
/* 48 */       p.components = new LinkedList();
/*    */       
/*    */ 
/* 51 */       List<Map<?, ?>> comps = (List)obj.get("components");
/* 52 */       if (comps != null) {
/* 53 */         for (Map<?, ?> compObj : comps)
/*    */         {
/* 55 */           HashMap<String, String> compMap = new HashMap();
/* 56 */           for (Object key : compObj.keySet()) {
/* 57 */             compMap.put((String)key, (String)compObj.get(key));
/*    */           }
/*    */           
/* 60 */           p.components.add(compMap);
/*    */         }
/*    */       }
/*    */       
/* 64 */       perk_map.put(p.id, p);
/*    */     }
/* 66 */     CivLog.info("Loaded " + perk_map.size() + " Perks.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigPerk.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
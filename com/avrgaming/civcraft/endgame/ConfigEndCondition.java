/*    */ package com.avrgaming.civcraft.endgame;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ 
/*    */ 
/*    */ public class ConfigEndCondition
/*    */ {
/*    */   public String id;
/*    */   public String className;
/* 15 */   public HashMap<String, String> attributes = new HashMap();
/*    */   public String victoryName;
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Map<String, ConfigEndCondition> endconditionMap) {
/* 19 */     endconditionMap.clear();
/* 20 */     List<Map<?, ?>> perks = cfg.getMapList("end_conditions");
/* 21 */     for (Map<?, ?> obj : perks) {
/* 22 */       ConfigEndCondition p = new ConfigEndCondition();
/*    */       
/* 24 */       p.id = ((String)obj.get("id"));
/* 25 */       p.className = ((String)obj.get("class"));
/* 26 */       p.victoryName = ((String)obj.get("name"));
/*    */       
/* 28 */       for (Map.Entry<?, ?> entry : obj.entrySet()) {
/* 29 */         if (((entry.getKey() instanceof String)) && ((entry.getValue() instanceof String))) {
/* 30 */           p.attributes.put((String)entry.getKey(), (String)entry.getValue());
/*    */         }
/*    */       }
/*    */       
/* 34 */       endconditionMap.put(p.id, p);
/*    */     }
/* 36 */     CivLog.info("Loaded " + endconditionMap.size() + " Perks.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\endgame\ConfigEndCondition.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
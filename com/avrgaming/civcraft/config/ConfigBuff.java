/*    */ package com.avrgaming.civcraft.config;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import com.avrgaming.civcraft.util.CivColor;
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
/*    */ public class ConfigBuff
/*    */ {
/*    */   public String id;
/*    */   public String name;
/*    */   public String description;
/*    */   public String value;
/*    */   public boolean stackable;
/*    */   public String parent;
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Map<String, ConfigBuff> buffs)
/*    */   {
/* 38 */     buffs.clear();
/* 39 */     List<Map<?, ?>> configBuffs = cfg.getMapList("buffs");
/* 40 */     for (Map<?, ?> b : configBuffs) {
/* 41 */       ConfigBuff buff = new ConfigBuff();
/* 42 */       buff.id = ((String)b.get("id"));
/* 43 */       buff.name = ((String)b.get("name"));
/*    */       
/* 45 */       buff.description = ((String)b.get("description"));
/* 46 */       buff.description = CivColor.colorize(buff.description);
/*    */       
/* 48 */       buff.value = ((String)b.get("value"));
/* 49 */       buff.stackable = ((Boolean)b.get("stackable")).booleanValue();
/* 50 */       buff.parent = ((String)b.get("parent"));
/*    */       
/* 52 */       if (buff.parent == null) {
/* 53 */         buff.parent = buff.id;
/*    */       }
/*    */       
/* 56 */       buffs.put(buff.id, buff);
/*    */     }
/*    */     
/* 59 */     CivLog.info("Loaded " + buffs.size() + " Buffs.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigBuff.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
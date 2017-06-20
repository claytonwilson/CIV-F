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
/*    */ 
/*    */ public class ConfigWonderBuff
/*    */ {
/*    */   public String id;
/* 32 */   public ArrayList<ConfigBuff> buffs = new ArrayList();
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Map<String, ConfigWonderBuff> wbuffs) {
/* 35 */     wbuffs.clear();
/* 36 */     List<Map<?, ?>> ConfigWonderBuff = cfg.getMapList("wonder_buffs");
/* 37 */     for (Map<?, ?> b : ConfigWonderBuff) {
/* 38 */       ConfigWonderBuff buff = new ConfigWonderBuff();
/* 39 */       buff.id = ((String)b.get("id"));
/*    */       
/* 41 */       List<?> buffStrings = (List)b.get("buffs");
/* 42 */       for (Object obj : buffStrings) {
/* 43 */         if ((obj instanceof String)) {
/* 44 */           String str = (String)obj;
/*    */           
/* 46 */           ConfigBuff cfgBuff = (ConfigBuff)CivSettings.buffs.get(str);
/*    */           
/* 48 */           if (cfgBuff != null) {
/* 49 */             buff.buffs.add(cfgBuff);
/*    */           } else {
/* 51 */             CivLog.warning("Unknown buff id:" + str);
/*    */           }
/*    */         }
/*    */       }
/*    */       
/*    */ 
/* 57 */       wbuffs.put(buff.id, buff);
/*    */     }
/*    */     
/* 60 */     CivLog.info("Loaded " + wbuffs.size() + " Wonder Buffs.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigWonderBuff.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package com.avrgaming.civcraft.config;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import org.bukkit.entity.EntityType;
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
/*    */ public class ConfigTempleSacrifice
/*    */ {
/*    */   public List<String> entites;
/*    */   public int reward;
/* 35 */   public static HashSet<String> validEntities = new HashSet();
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, ArrayList<ConfigTempleSacrifice> temple_sacrifices) {
/* 38 */     List<Map<?, ?>> ts_list = cfg.getMapList("temple.sacrifices");
/* 39 */     for (Map<?, ?> cl : ts_list) {
/* 40 */       ConfigTempleSacrifice config_ts = new ConfigTempleSacrifice();
/*    */       
/* 42 */       List<?> entitiesList = (List)cl.get("entities");
/* 43 */       if (entitiesList != null) {
/* 44 */         ArrayList<String> entities = new ArrayList();
/*    */         
/* 46 */         for (Object obj : entitiesList) {
/* 47 */           if ((obj instanceof String)) {
/* 48 */             entities.add((String)obj);
/*    */             
/* 50 */             String[] split = ((String)obj).split(":");
/* 51 */             validEntities.add(split[0].toUpperCase());
/*    */           }
/*    */         }
/* 54 */         config_ts.entites = entities;
/*    */       }
/*    */       
/* 57 */       config_ts.reward = ((Integer)cl.get("reward")).intValue();
/* 58 */       temple_sacrifices.add(config_ts);
/*    */     }
/* 60 */     CivLog.info("Loaded " + temple_sacrifices.size() + " temple sacrifices.");
/*    */   }
/*    */   
/*    */   public static boolean isValidEntity(EntityType entityType) {
/* 64 */     if (validEntities.contains(entityType.toString())) {
/* 65 */       return true;
/*    */     }
/*    */     
/* 68 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigTempleSacrifice.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
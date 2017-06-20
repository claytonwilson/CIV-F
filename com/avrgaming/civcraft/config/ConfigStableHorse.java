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
/*    */ public class ConfigStableHorse
/*    */ {
/*    */   public int id;
/*    */   public double speed;
/*    */   public double jump;
/*    */   public double health;
/*    */   public boolean mule;
/*    */   public String variant;
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Map<Integer, ConfigStableHorse> horses)
/*    */   {
/* 37 */     horses.clear();
/* 38 */     List<Map<?, ?>> config_horses = cfg.getMapList("stable_horses");
/* 39 */     for (Map<?, ?> level : config_horses) {
/* 40 */       ConfigStableHorse horse = new ConfigStableHorse();
/* 41 */       horse.id = ((Integer)level.get("id")).intValue();
/* 42 */       horse.speed = ((Double)level.get("speed")).doubleValue();
/* 43 */       horse.jump = ((Double)level.get("jump")).doubleValue();
/* 44 */       horse.health = ((Double)level.get("health")).doubleValue();
/* 45 */       horse.variant = ((String)level.get("variant"));
/*    */       
/* 47 */       Boolean mule = (Boolean)level.get("mule");
/* 48 */       if ((mule == null) || (!mule.booleanValue())) {
/* 49 */         horse.mule = false;
/*    */       } else {
/* 51 */         horse.mule = true;
/*    */       }
/*    */       
/* 54 */       horses.put(Integer.valueOf(horse.id), horse);
/*    */     }
/* 56 */     CivLog.info("Loaded " + horses.size() + " Horses.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigStableHorse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
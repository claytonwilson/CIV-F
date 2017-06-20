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
/*    */ 
/*    */ public class ConfigMission
/*    */ {
/*    */   public String id;
/*    */   public String name;
/*    */   public List<String> description;
/*    */   public Double cost;
/*    */   public Double range;
/*    */   public Double cooldown;
/*    */   public Integer intel;
/*    */   public Integer slot;
/*    */   public Double fail_chance;
/*    */   public Integer length;
/*    */   public Double compromise_chance;
/*    */   
/*    */   public ConfigMission() {}
/*    */   
/*    */   public ConfigMission(ConfigMission mission)
/*    */   {
/* 46 */     this.id = mission.id;
/* 47 */     this.name = mission.name;
/* 48 */     this.description = mission.description;
/* 49 */     this.cost = mission.cost;
/* 50 */     this.range = mission.range;
/* 51 */     this.cooldown = mission.cooldown;
/* 52 */     this.intel = mission.intel;
/*    */   }
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Map<String, ConfigMission> missions)
/*    */   {
/* 57 */     missions.clear();
/* 58 */     List<Map<?, ?>> configMissions = cfg.getMapList("missions");
/* 59 */     for (Map<?, ?> b : configMissions) {
/* 60 */       ConfigMission mission = new ConfigMission();
/* 61 */       mission.id = ((String)b.get("id"));
/* 62 */       mission.name = ((String)b.get("name"));
/* 63 */       mission.cost = ((Double)b.get("cost"));
/* 64 */       mission.range = ((Double)b.get("range"));
/* 65 */       mission.cooldown = ((Double)b.get("cooldown"));
/* 66 */       mission.intel = ((Integer)b.get("intel"));
/* 67 */       mission.length = ((Integer)b.get("length"));
/* 68 */       mission.fail_chance = ((Double)b.get("fail_chance"));
/* 69 */       mission.compromise_chance = ((Double)b.get("compromise_chance"));
/* 70 */       mission.slot = ((Integer)b.get("slot"));
/* 71 */       mission.description = ((List)b.get("description"));
/*    */       
/* 73 */       missions.put(mission.id.toLowerCase(), mission);
/*    */     }
/*    */     
/* 76 */     CivLog.info("Loaded " + missions.size() + " Espionage Missions.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigMission.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
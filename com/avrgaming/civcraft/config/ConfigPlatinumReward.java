/*    */ package com.avrgaming.civcraft.config;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ 
/*    */ public class ConfigPlatinumReward
/*    */ {
/*    */   public String name;
/*    */   public int amount;
/*    */   public String occurs;
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Map<String, ConfigPlatinumReward> rewards)
/*    */   {
/* 16 */     rewards.clear();
/* 17 */     List<Map<?, ?>> culture_levels = cfg.getMapList("platinum");
/* 18 */     for (Map<?, ?> level : culture_levels) {
/* 19 */       ConfigPlatinumReward reward = new ConfigPlatinumReward();
/* 20 */       reward.name = ((String)level.get("name"));
/* 21 */       reward.amount = ((Integer)level.get("amount")).intValue();
/* 22 */       reward.occurs = ((String)level.get("occurs"));
/* 23 */       rewards.put(reward.name, reward);
/*    */     }
/* 25 */     CivLog.info("Loaded " + rewards.size() + " platinum rewards..");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigPlatinumReward.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
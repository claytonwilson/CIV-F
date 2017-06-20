/*    */ package com.avrgaming.civcraft.config;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ 
/*    */ public class ConfigHappinessState
/*    */ {
/*    */   public int level;
/*    */   public String name;
/*    */   public String color;
/*    */   public double amount;
/*    */   public double beaker_rate;
/*    */   public double coin_rate;
/*    */   public double culture_rate;
/*    */   public double hammer_rate;
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Map<Integer, ConfigHappinessState> happiness_states)
/*    */   {
/* 21 */     happiness_states.clear();
/* 22 */     List<Map<?, ?>> list = cfg.getMapList("happiness.states");
/* 23 */     for (Map<?, ?> cl : list)
/*    */     {
/* 25 */       ConfigHappinessState happy_level = new ConfigHappinessState();
/* 26 */       happy_level.level = ((Integer)cl.get("level")).intValue();
/* 27 */       happy_level.name = ((String)cl.get("name"));
/* 28 */       happy_level.color = ((String)cl.get("color"));
/* 29 */       happy_level.amount = ((Double)cl.get("amount")).doubleValue();
/* 30 */       happy_level.beaker_rate = ((Double)cl.get("beaker_rate")).doubleValue();
/* 31 */       happy_level.coin_rate = ((Double)cl.get("coin_rate")).doubleValue();
/* 32 */       happy_level.culture_rate = ((Double)cl.get("culture_rate")).doubleValue();
/* 33 */       happy_level.hammer_rate = ((Double)cl.get("hammer_rate")).doubleValue();
/*    */       
/*    */ 
/* 36 */       happiness_states.put(Integer.valueOf(happy_level.level), happy_level);
/*    */     }
/*    */     
/* 39 */     CivLog.info("Loaded " + happiness_states.size() + " Happiness States.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigHappinessState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
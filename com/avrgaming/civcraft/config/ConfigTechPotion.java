/*    */ package com.avrgaming.civcraft.config;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class ConfigTechPotion
/*    */ {
/*    */   public String name;
/*    */   public int data;
/*    */   public String require_tech;
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Map<Integer, ConfigTechPotion> techPotions)
/*    */   {
/* 19 */     techPotions.clear();
/* 20 */     List<Map<?, ?>> techs = cfg.getMapList("potions");
/* 21 */     for (Map<?, ?> confTech : techs) {
/* 22 */       ConfigTechPotion tech = new ConfigTechPotion();
/*    */       
/* 24 */       tech.name = ((String)confTech.get("name"));
/* 25 */       tech.data = ((Integer)confTech.get("data")).intValue();
/* 26 */       tech.require_tech = ((String)confTech.get("require_tech"));
/* 27 */       techPotions.put(Integer.valueOf(tech.data), tech);
/*    */     }
/* 29 */     CivLog.info("Loaded " + techPotions.size() + " tech potions.");
/*    */   }
/*    */   
/*    */   public boolean hasTechnology(Player player) {
/* 33 */     Resident resident = com.avrgaming.civcraft.main.CivGlobal.getResident(player);
/* 34 */     if ((resident == null) || (!resident.hasTown())) {
/* 35 */       return false;
/*    */     }
/*    */     
/* 38 */     if (!resident.getCiv().hasTechnology(this.require_tech)) {
/* 39 */       return false;
/*    */     }
/*    */     
/* 42 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigTechPotion.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
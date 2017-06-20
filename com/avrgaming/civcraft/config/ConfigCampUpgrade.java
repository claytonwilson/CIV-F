/*    */ package com.avrgaming.civcraft.config;
/*    */ 
/*    */ import com.avrgaming.civcraft.camp.Camp;
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import java.util.HashMap;
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
/*    */ public class ConfigCampUpgrade
/*    */ {
/*    */   public String id;
/*    */   public String name;
/*    */   public double cost;
/*    */   public String action;
/* 37 */   public String require_upgrade = null;
/*    */   
/* 39 */   public static HashMap<String, Integer> categories = new HashMap();
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Map<String, ConfigCampUpgrade> upgrades) {
/* 42 */     upgrades.clear();
/* 43 */     List<Map<?, ?>> culture_levels = cfg.getMapList("upgrades");
/* 44 */     for (Map<?, ?> level : culture_levels) {
/* 45 */       ConfigCampUpgrade upgrade = new ConfigCampUpgrade();
/*    */       
/* 47 */       upgrade.id = ((String)level.get("id"));
/* 48 */       upgrade.name = ((String)level.get("name"));
/* 49 */       upgrade.cost = ((Double)level.get("cost")).doubleValue();
/* 50 */       upgrade.action = ((String)level.get("action"));
/* 51 */       upgrade.require_upgrade = ((String)level.get("require_upgrade"));
/* 52 */       upgrades.put(upgrade.id, upgrade);
/*    */     }
/* 54 */     CivLog.info("Loaded " + upgrades.size() + " camp upgrades.");
/*    */   }
/*    */   
/*    */   public boolean isAvailable(Camp camp) {
/* 58 */     if (camp.hasUpgrade(this.id)) {
/* 59 */       return false;
/*    */     }
/*    */     
/* 62 */     if ((this.require_upgrade == null) || (this.require_upgrade.equals(""))) {
/* 63 */       return true;
/*    */     }
/*    */     
/* 66 */     if (camp.hasUpgrade(this.require_upgrade)) {
/* 67 */       return true;
/*    */     }
/* 69 */     return false;
/*    */   }
/*    */   
/*    */   public void processAction(Camp camp)
/*    */   {
/* 74 */     if (this.action == null) {
/* 75 */       CivLog.warning("No action found for upgrade:" + this.id); return;
/*    */     }
/*    */     
/*    */     String str;
/* 79 */     switch ((str = this.action.toLowerCase()).hashCode()) {case 714278504:  if (str.equals("enable_longhouse")) break; break; case 910201969:  if (str.equals("enable_garden")) {} case 1260797837:  if ((goto 154) && (str.equals("enable_sifter")))
/*    */       {
/* 81 */         camp.setSifterEnabled(true);
/* 82 */         CivMessage.sendCamp(camp, "Our Sifter has been enabled!");
/* 83 */         return;
/*    */         
/* 85 */         camp.setLonghouseEnabled(true);
/* 86 */         CivMessage.sendCamp(camp, "Our longhouse has been enabled!");
/* 87 */         return;
/*    */         
/* 89 */         camp.setGardenEnabled(true);
/* 90 */         CivMessage.sendCamp(camp, "Our garden has been enabled!"); }
/* 91 */       break;
/*    */     }
/* 93 */     CivLog.warning("Unknown action:" + this.action + " processed for upgrade:" + this.id);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigCampUpgrade.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
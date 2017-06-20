/*    */ package com.avrgaming.civcraft.config;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Town;
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
/*    */ public class ConfigUnit
/*    */ {
/*    */   public String id;
/*    */   public String name;
/*    */   public String class_name;
/*    */   public String require_tech;
/*    */   public String require_struct;
/*    */   public String require_upgrade;
/*    */   public double cost;
/*    */   public double hammer_cost;
/*    */   public int limit;
/*    */   public int item_id;
/*    */   public int item_data;
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Map<String, ConfigUnit> units)
/*    */   {
/* 45 */     units.clear();
/* 46 */     List<Map<?, ?>> configUnits = cfg.getMapList("units");
/* 47 */     for (Map<?, ?> b : configUnits)
/*    */     {
/* 49 */       ConfigUnit unit = new ConfigUnit();
/*    */       
/* 51 */       unit.id = ((String)b.get("id"));
/* 52 */       unit.name = ((String)b.get("name"));
/* 53 */       unit.class_name = ((String)b.get("class_name"));
/* 54 */       unit.require_tech = ((String)b.get("require_tech"));
/* 55 */       unit.require_struct = ((String)b.get("require_struct"));
/* 56 */       unit.require_upgrade = ((String)b.get("require_upgrade"));
/* 57 */       unit.cost = ((Double)b.get("cost")).doubleValue();
/* 58 */       unit.hammer_cost = ((Double)b.get("hammer_cost")).doubleValue();
/* 59 */       unit.limit = ((Integer)b.get("limit")).intValue();
/* 60 */       unit.item_id = ((Integer)b.get("item_id")).intValue();
/* 61 */       unit.item_data = ((Integer)b.get("item_data")).intValue();
/*    */       
/* 63 */       units.put(unit.id, unit);
/*    */     }
/*    */     
/* 66 */     CivLog.info("Loaded " + units.size() + " Units.");
/*    */   }
/*    */   
/*    */   public boolean isAvailable(Town town) {
/* 70 */     if (CivGlobal.testFileFlag("debug-norequire")) {
/* 71 */       CivMessage.global("Ignoring requirements! debug-norequire found.");
/* 72 */       return true;
/*    */     }
/*    */     
/* 75 */     if ((town.hasTechnology(this.require_tech)) && 
/* 76 */       (town.hasUpgrade(this.require_upgrade)) && 
/* 77 */       (town.hasStructure(this.require_struct)) && (
/* 78 */       (this.limit == 0) || (town.getUnitTypeCount(this.id) < this.limit))) {
/* 79 */       return true;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 84 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigUnit.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.avrgaming.civcraft.config;
/*     */ 
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConfigTech
/*     */ {
/*     */   public String id;
/*     */   public String name;
/*     */   public double beaker_cost;
/*     */   public double cost;
/*     */   public String require_techs;
/*     */   public Integer points;
/*     */   
/*     */   public static void loadConfig(FileConfiguration cfg, Map<String, ConfigTech> tech_maps)
/*     */   {
/*  41 */     tech_maps.clear();
/*  42 */     List<Map<?, ?>> techs = cfg.getMapList("techs");
/*  43 */     for (Map<?, ?> confTech : techs) {
/*  44 */       ConfigTech tech = new ConfigTech();
/*     */       
/*  46 */       tech.id = ((String)confTech.get("id"));
/*  47 */       tech.name = ((String)confTech.get("name"));
/*  48 */       tech.beaker_cost = ((Double)confTech.get("beaker_cost")).doubleValue();
/*  49 */       tech.cost = ((Double)confTech.get("cost")).doubleValue();
/*  50 */       tech.require_techs = ((String)confTech.get("require_techs"));
/*  51 */       tech.points = ((Integer)confTech.get("points"));
/*     */       
/*  53 */       tech_maps.put(tech.id, tech);
/*     */     }
/*  55 */     CivLog.info("Loaded " + tech_maps.size() + " technologies.");
/*     */   }
/*     */   
/*     */   public static ArrayList<ConfigTech> getAvailableTechs(Civilization civ)
/*     */   {
/*  60 */     ArrayList<ConfigTech> returnTechs = new ArrayList();
/*     */     
/*  62 */     for (ConfigTech tech : CivSettings.techs.values()) {
/*  63 */       if ((!civ.hasTechnology(tech.id)) && 
/*  64 */         (tech.isAvailable(civ))) {
/*  65 */         returnTechs.add(tech);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */     return returnTechs;
/*     */   }
/*     */   
/*     */   public boolean isAvailable(Civilization civ) {
/*  92 */     if (CivGlobal.testFileFlag("debug-norequire")) {
/*  93 */       CivMessage.global("Ignoring requirements! debug-norequire found.");
/*  94 */       return true;
/*     */     }
/*     */     
/*  97 */     if ((this.require_techs == null) || (this.require_techs.equals(""))) {
/*  98 */       return true;
/*     */     }
/*     */     
/* 101 */     String[] requireTechs = this.require_techs.split(":");
/*     */     String[] arrayOfString1;
/* 103 */     int j = (arrayOfString1 = requireTechs).length; for (int i = 0; i < j; i++) { String reqTech = arrayOfString1[i];
/* 104 */       if (!civ.hasTechnology(reqTech)) {
/* 105 */         return false;
/*     */       }
/*     */     }
/* 108 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigTech.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
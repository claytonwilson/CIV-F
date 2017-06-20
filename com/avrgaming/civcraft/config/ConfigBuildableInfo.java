/*     */ package com.avrgaming.civcraft.config;
/*     */ 
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
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
/*     */ 
/*     */ public class ConfigBuildableInfo
/*     */ {
/*  33 */   public String id = "";
/*  34 */   public String template_base_name = "";
/*  35 */   public int templateYShift = 0;
/*  36 */   public String displayName = "";
/*  37 */   public String require_tech = "";
/*  38 */   public String require_upgrade = "";
/*  39 */   public String require_structure = "";
/*  40 */   public String check_event = "";
/*  41 */   public String effect_event = "";
/*  42 */   public String update_event = "";
/*  43 */   public String onBuild_event = "";
/*  44 */   public int limit = 0;
/*  45 */   public ArrayList<String> signs = new ArrayList();
/*  46 */   public double cost = 0.0D;
/*  47 */   public double upkeep = 0.0D;
/*  48 */   public double hammer_cost = 0.0D;
/*  49 */   public int max_hitpoints = 0;
/*  50 */   public Boolean destroyable = Boolean.valueOf(false);
/*  51 */   public Boolean allow_outside_town = Boolean.valueOf(false);
/*  52 */   public Boolean isWonder = Boolean.valueOf(false);
/*  53 */   public Integer regenRate = Integer.valueOf(0);
/*  54 */   public Boolean tile_improvement = Boolean.valueOf(false);
/*  55 */   public Integer points = Integer.valueOf(0);
/*  56 */   public boolean allow_demolish = false;
/*  57 */   public boolean strategic = false;
/*  58 */   public boolean ignore_floating = false;
/*  59 */   public List<HashMap<String, String>> components = new LinkedList();
/*  60 */   public boolean has_template = true;
/*     */   
/*     */   public boolean isAvailable(Town town) {
/*  63 */     if ((town.hasTechnology(this.require_tech)) && 
/*  64 */       (town.hasUpgrade(this.require_upgrade)) && 
/*  65 */       (town.hasStructure(this.require_structure)) && (
/*  66 */       (this.limit == 0) || (town.getStructureTypeCount(this.id) < this.limit))) {
/*  67 */       boolean capitol = town.isCapitol();
/*     */       
/*  69 */       if ((this.id.equals("s_townhall")) && (capitol)) {
/*  70 */         return false;
/*     */       }
/*     */       
/*  73 */       if ((this.id.equals("s_capitol")) && (!capitol)) {
/*  74 */         return false;
/*     */       }
/*     */       
/*  77 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  82 */     return false;
/*     */   }
/*     */   
/*     */   public static void loadConfig(FileConfiguration cfg, String path, Map<String, ConfigBuildableInfo> structureMap, boolean isWonder) {
/*  86 */     structureMap.clear();
/*  87 */     List<Map<?, ?>> structures = cfg.getMapList(path);
/*  88 */     for (Map<?, ?> obj : structures) {
/*  89 */       ConfigBuildableInfo sinfo = new ConfigBuildableInfo();
/*     */       
/*  91 */       sinfo.id = ((String)obj.get("id"));
/*  92 */       sinfo.template_base_name = ((String)obj.get("template"));
/*  93 */       sinfo.templateYShift = ((Integer)obj.get("template_y_shift")).intValue();
/*  94 */       sinfo.displayName = ((String)obj.get("displayName"));
/*  95 */       sinfo.require_tech = ((String)obj.get("require_tech"));
/*  96 */       sinfo.require_upgrade = ((String)obj.get("require_upgrade"));
/*  97 */       sinfo.require_structure = ((String)obj.get("require_structure"));
/*  98 */       sinfo.check_event = ((String)obj.get("check_event"));
/*  99 */       sinfo.effect_event = ((String)obj.get("effect_event"));
/* 100 */       sinfo.update_event = ((String)obj.get("update_event"));
/* 101 */       sinfo.onBuild_event = ((String)obj.get("onBuild_event"));
/* 102 */       sinfo.limit = ((Integer)obj.get("limit")).intValue();
/*     */       
/* 104 */       sinfo.cost = ((Double)obj.get("cost")).doubleValue();
/* 105 */       sinfo.upkeep = ((Double)obj.get("upkeep")).doubleValue();
/* 106 */       sinfo.hammer_cost = ((Double)obj.get("hammer_cost")).doubleValue();
/* 107 */       sinfo.max_hitpoints = ((Integer)obj.get("max_hitpoints")).intValue();
/* 108 */       sinfo.destroyable = ((Boolean)obj.get("destroyable"));
/* 109 */       sinfo.allow_outside_town = ((Boolean)obj.get("allow_outside_town"));
/* 110 */       sinfo.regenRate = ((Integer)obj.get("regen_rate"));
/* 111 */       sinfo.isWonder = Boolean.valueOf(isWonder);
/* 112 */       sinfo.points = ((Integer)obj.get("points"));
/*     */       
/*     */ 
/* 115 */       List<Map<?, ?>> comps = (List)obj.get("components");
/* 116 */       if (comps != null) {
/* 117 */         for (Map<?, ?> compObj : comps)
/*     */         {
/* 119 */           HashMap<String, String> compMap = new HashMap();
/* 120 */           for (Object key : compObj.keySet()) {
/* 121 */             compMap.put((String)key, (String)compObj.get(key));
/*     */           }
/*     */           
/* 124 */           sinfo.components.add(compMap);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 129 */       Boolean tileImprovement = (Boolean)obj.get("tile_improvement");
/* 130 */       if ((tileImprovement != null) && (tileImprovement.booleanValue())) {
/* 131 */         sinfo.tile_improvement = Boolean.valueOf(true);
/*     */       } else {
/* 133 */         sinfo.tile_improvement = Boolean.valueOf(false);
/*     */       }
/*     */       
/* 136 */       Boolean allowDemolish = (Boolean)obj.get("allow_demolish");
/* 137 */       if ((allowDemolish == null) || (allowDemolish.booleanValue())) {
/* 138 */         sinfo.allow_demolish = true;
/*     */       } else {
/* 140 */         sinfo.allow_demolish = false;
/*     */       }
/*     */       
/* 143 */       Boolean strategic = (Boolean)obj.get("strategic");
/* 144 */       if ((strategic == null) || (!strategic.booleanValue())) {
/* 145 */         sinfo.strategic = false;
/*     */       } else {
/* 147 */         sinfo.strategic = true;
/*     */       }
/*     */       
/* 150 */       Boolean ignore_floating = (Boolean)obj.get("ignore_floating");
/* 151 */       if (ignore_floating != null) {
/* 152 */         sinfo.ignore_floating = ignore_floating.booleanValue();
/*     */       }
/*     */       
/* 155 */       Boolean has_template = (Boolean)obj.get("has_template");
/* 156 */       if (has_template != null) {
/* 157 */         sinfo.has_template = has_template.booleanValue();
/*     */       }
/*     */       
/*     */ 
/* 161 */       if (isWonder) {
/* 162 */         sinfo.strategic = true;
/*     */       }
/*     */       
/* 165 */       structureMap.put(sinfo.id, sinfo);
/*     */     }
/* 167 */     CivLog.info("Loaded " + structureMap.size() + " structures.");
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigBuildableInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
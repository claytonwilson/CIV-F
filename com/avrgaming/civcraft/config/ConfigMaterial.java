/*     */ package com.avrgaming.civcraft.config;
/*     */ 
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.util.CivColor;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConfigMaterial
/*     */ {
/*     */   public String id;
/*     */   public int item_id;
/*     */   public int item_data;
/*     */   public String name;
/*  23 */   public String category = "Misc";
/*  24 */   public String categoryCivColortripped = this.category;
/*     */   
/*     */   public int tier;
/*     */   
/*  28 */   public String[] lore = null;
/*  29 */   public boolean craftable = false;
/*  30 */   public String required_tech = null;
/*  31 */   public boolean shaped = false;
/*     */   public HashMap<String, ConfigIngredient> incredients;
/*     */   public String[] shape;
/*  34 */   public List<HashMap<String, String>> components = new LinkedList();
/*  35 */   public boolean vanilla = false;
/*  36 */   public int amount = 1;
/*     */   
/*     */   public static void loadConfig(FileConfiguration cfg, Map<String, ConfigMaterial> materials)
/*     */   {
/*  40 */     materials.clear();
/*  41 */     List<Map<?, ?>> configMaterials = cfg.getMapList("materials");
/*  42 */     for (Map<?, ?> b : configMaterials) {
/*  43 */       ConfigMaterial mat = new ConfigMaterial();
/*     */       
/*     */ 
/*  46 */       mat.id = ((String)b.get("id"));
/*  47 */       mat.item_id = ((Integer)b.get("item_id")).intValue();
/*  48 */       mat.item_data = ((Integer)b.get("item_data")).intValue();
/*  49 */       mat.name = ((String)b.get("name"));
/*  50 */       mat.name = CivColor.colorize(mat.name);
/*     */       
/*  52 */       String category = (String)b.get("category");
/*  53 */       if (category != null) {
/*  54 */         mat.category = CivColor.colorize(category);
/*  55 */         mat.categoryCivColortripped = CivColor.stripTags(category);
/*     */         
/*  57 */         if (mat.category.toLowerCase().contains("tier 1")) {
/*  58 */           mat.tier = 1;
/*  59 */         } else if (mat.category.toLowerCase().contains("tier 2")) {
/*  60 */           mat.tier = 2;
/*  61 */         } else if (mat.category.toLowerCase().contains("tier 3")) {
/*  62 */           mat.tier = 3;
/*  63 */         } else if (mat.category.toLowerCase().contains("tier 4")) {
/*  64 */           mat.tier = 4;
/*     */         } else {
/*  66 */           mat.tier = 0;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  72 */       List<?> configLore = (List)b.get("lore");
/*  73 */       if (configLore != null) {
/*  74 */         String[] lore = new String[configLore.size()];
/*     */         
/*  76 */         int i = 0;
/*  77 */         for (Object obj : configLore) {
/*  78 */           if ((obj instanceof String)) {
/*  79 */             lore[i] = ((String)obj);
/*  80 */             i++;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  85 */       Boolean craftable = (Boolean)b.get("craftable");
/*  86 */       if (craftable != null) {
/*  87 */         mat.craftable = craftable.booleanValue();
/*     */       }
/*     */       
/*  90 */       Boolean shaped = (Boolean)b.get("shaped");
/*  91 */       if (shaped != null) {
/*  92 */         mat.shaped = shaped.booleanValue();
/*     */       }
/*     */       
/*  95 */       Boolean vanilla = (Boolean)b.get("vanilla");
/*  96 */       if (vanilla != null) {
/*  97 */         mat.vanilla = vanilla.booleanValue();
/*     */       }
/*     */       
/* 100 */       Integer amount = (Integer)b.get("amount");
/* 101 */       if (amount != null) {
/* 102 */         mat.amount = amount.intValue();
/*     */       }
/*     */       
/* 105 */       String required_tech = (String)b.get("required_techs");
/* 106 */       if (required_tech != null) {
/* 107 */         mat.required_tech = required_tech;
/*     */       }
/*     */       
/* 110 */       List<Map<?, ?>> comps = (List)b.get("components");
/* 111 */       HashMap<String, String> compMap; if (comps != null) {
/* 112 */         for (Map<?, ?> compObj : comps)
/*     */         {
/* 114 */           compMap = new HashMap();
/* 115 */           for (Object key : compObj.keySet()) {
/* 116 */             compMap.put((String)key, (String)compObj.get(key));
/*     */           }
/* 118 */           mat.components.add(compMap);
/*     */         }
/*     */       }
/*     */       
/* 122 */       List<Map<?, ?>> configIngredients = (List)b.get("ingredients");
/* 123 */       String custom_id; if (configIngredients != null) {
/* 124 */         mat.incredients = new HashMap();
/*     */         
/* 126 */         for (Object ingred : configIngredients) {
/* 127 */           ConfigIngredient ingredient = new ConfigIngredient();
/* 128 */           ingredient.type_id = ((Integer)((Map)ingred).get("type_id")).intValue();
/* 129 */           ingredient.data = ((Integer)((Map)ingred).get("data")).intValue();
/* 130 */           String key = null;
/*     */           
/* 132 */           custom_id = (String)((Map)ingred).get("custom_id");
/* 133 */           if (custom_id != null) {
/* 134 */             ingredient.custom_id = custom_id;
/* 135 */             key = custom_id;
/*     */           } else {
/* 137 */             ingredient.custom_id = null;
/* 138 */             key = "mc_" + ingredient.type_id;
/*     */           }
/*     */           
/* 141 */           Boolean ignore_data = (Boolean)((Map)ingred).get("ignore_data");
/* 142 */           if ((ignore_data == null) || (!ignore_data.booleanValue())) {
/* 143 */             ingredient.ignore_data = false;
/*     */           } else {
/* 145 */             ingredient.ignore_data = true;
/*     */           }
/*     */           
/* 148 */           Integer count = (Integer)((Map)ingred).get("count");
/* 149 */           if (count != null) {
/* 150 */             ingredient.count = count.intValue();
/*     */           }
/*     */           
/* 153 */           String letter = (String)((Map)ingred).get("letter");
/* 154 */           if (letter != null) {
/* 155 */             ingredient.letter = letter;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 160 */           mat.incredients.put(key, ingredient);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 165 */       if (shaped.booleanValue())
/*     */       {
/* 167 */         Object configShape = (List)b.get("shape");
/*     */         
/* 169 */         if (configShape != null) {
/* 170 */           String[] shape = new String[((List)configShape).size()];
/*     */           
/* 172 */           int i = 0;
/* 173 */           for (Object obj : (List)configShape) {
/* 174 */             if ((obj instanceof String)) {
/* 175 */               shape[i] = ((String)obj);
/* 176 */               i++;
/*     */             }
/*     */           }
/* 179 */           mat.shape = shape;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 185 */       ConfigMaterialCategory.addMaterial(mat);
/* 186 */       materials.put(mat.id, mat);
/*     */     }
/*     */     
/* 189 */     CivLog.info("Loaded " + materials.size() + " Materials.");
/*     */   }
/*     */   
/*     */   public boolean playerHasTechnology(Player player) {
/* 193 */     if (this.required_tech == null) {
/* 194 */       return true;
/*     */     }
/*     */     
/* 197 */     Resident resident = CivGlobal.getResident(player);
/* 198 */     if ((resident == null) || (!resident.hasTown())) {
/* 199 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 203 */     String[] split = this.required_tech.split(",");
/* 204 */     String[] arrayOfString1; int j = (arrayOfString1 = split).length; for (int i = 0; i < j; i++) { String tech = arrayOfString1[i];
/* 205 */       tech = tech.replace(" ", "");
/* 206 */       if (!resident.getCiv().hasTechnology(tech)) {
/* 207 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 211 */     return true;
/*     */   }
/*     */   
/*     */   public String getRequireString() {
/* 215 */     String out = "";
/* 216 */     if (this.required_tech == null) {
/* 217 */       return out;
/*     */     }
/*     */     
/*     */ 
/* 221 */     String[] split = this.required_tech.split(",");
/* 222 */     String[] arrayOfString1; int j = (arrayOfString1 = split).length; for (int i = 0; i < j; i++) { String tech = arrayOfString1[i];
/* 223 */       tech = tech.replace(" ", "");
/* 224 */       ConfigTech technology = (ConfigTech)CivSettings.techs.get(tech);
/* 225 */       if (technology != null) {
/* 226 */         out = out + technology.name + ", ";
/*     */       }
/*     */     }
/*     */     
/* 230 */     return out;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigMaterial.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
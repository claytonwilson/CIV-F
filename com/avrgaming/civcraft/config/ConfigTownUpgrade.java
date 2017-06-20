/*     */ package com.avrgaming.civcraft.config;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.LibraryEnchantment;
/*     */ import com.avrgaming.civcraft.object.StoreMaterial;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.Bank;
/*     */ import com.avrgaming.civcraft.structure.Grocer;
/*     */ import com.avrgaming.civcraft.structure.Library;
/*     */ import com.avrgaming.civcraft.structure.Store;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.HashMap;
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
/*     */ public class ConfigTownUpgrade
/*     */ {
/*     */   public String id;
/*     */   public String name;
/*     */   public double cost;
/*     */   public String action;
/*  46 */   public String require_upgrade = null;
/*  47 */   public String require_tech = null;
/*  48 */   public String require_structure = null;
/*  49 */   public String category = null;
/*     */   
/*  51 */   public static HashMap<String, Integer> categories = new HashMap();
/*     */   
/*     */   public static void loadConfig(FileConfiguration cfg, Map<String, ConfigTownUpgrade> upgrades) {
/*  54 */     upgrades.clear();
/*  55 */     List<Map<?, ?>> culture_levels = cfg.getMapList("upgrades");
/*  56 */     for (Map<?, ?> level : culture_levels) {
/*  57 */       ConfigTownUpgrade town_upgrade = new ConfigTownUpgrade();
/*     */       
/*  59 */       town_upgrade.id = ((String)level.get("id"));
/*  60 */       town_upgrade.name = ((String)level.get("name"));
/*  61 */       town_upgrade.cost = ((Double)level.get("cost")).doubleValue();
/*  62 */       town_upgrade.action = ((String)level.get("action"));
/*  63 */       town_upgrade.require_upgrade = ((String)level.get("require_upgrade"));
/*  64 */       town_upgrade.require_tech = ((String)level.get("require_tech"));
/*  65 */       town_upgrade.require_structure = ((String)level.get("require_structure"));
/*  66 */       town_upgrade.category = ((String)level.get("category"));
/*     */       
/*  68 */       Integer categoryCount = (Integer)categories.get(town_upgrade.category);
/*  69 */       if (categoryCount == null) {
/*  70 */         categories.put(town_upgrade.category.toLowerCase(), Integer.valueOf(1));
/*     */       } else {
/*  72 */         categories.put(town_upgrade.category.toLowerCase(), Integer.valueOf(categoryCount.intValue() + 1));
/*     */       }
/*     */       
/*  75 */       upgrades.put(town_upgrade.id, town_upgrade);
/*     */     }
/*  77 */     CivLog.info("Loaded " + upgrades.size() + " town upgrades.");
/*     */   }
/*     */   
/*     */   public void processAction(Town town) throws CivException {
/*  81 */     if (this.action == null)
/*  82 */       return;
/*  83 */     String[] args = this.action.split(",");
/*     */     
/*     */     String str;
/*     */     
/*  87 */     switch ((str = args[0]).hashCode()) {case -176481388:  if (str.equals("set_town_level")) break; break; case 258318921:  if (str.equals("set_store_level")) {} break; case 362455799:  if (str.equals("enable_library_enchantment")) {} break; case 946426032:  if (str.equals("set_bank_interest")) {} break; case 1428368450:  if (str.equals("set_store_material")) {} break; case 1727385534:  if (str.equals("set_bank_level")) {} break; case 1815979491:  if (str.equals("set_library_level")) {} break; case 2121371438:  if (!str.equals("set_grocer_level")) {
/*     */         return;
/*  89 */         if (town.getLevel() < Integer.valueOf(args[1].trim()).intValue()) {
/*  90 */           town.setLevel(Integer.valueOf(args[1].trim()).intValue());
/*  91 */           CivMessage.global(town.getName() + " is now a " + town.getLevelTitle() + "!");
/*     */           
/*  93 */           return;
/*     */           
/*     */ 
/*  96 */           Structure struct = town.getStructureByType("s_bank");
/*  97 */           if ((struct != null) && ((struct instanceof Bank))) {
/*  98 */             Bank bank = (Bank)struct;
/*  99 */             if (bank.getLevel() < Integer.valueOf(args[1].trim()).intValue()) {
/* 100 */               bank.setLevel(Integer.valueOf(args[1].trim()).intValue());
/* 101 */               bank.updateSignText();
/* 102 */               town.saved_bank_level = bank.getLevel();
/* 103 */               CivMessage.sendTown(town, "The bank is now level " + bank.getLevel());
/*     */               
/*     */ 
/* 106 */               return;
/*     */               
/* 108 */               Structure struct = town.getStructureByType("s_bank");
/* 109 */               if ((struct != null) && ((struct instanceof Bank))) {
/* 110 */                 Bank bank = (Bank)struct;
/* 111 */                 if (bank.getInterestRate() < Double.valueOf(args[1].trim()).doubleValue()) {
/* 112 */                   bank.setInterestRate(Double.valueOf(args[1].trim()).doubleValue());
/* 113 */                   town.saved_bank_interest_amount = bank.getInterestRate();
/* 114 */                   DecimalFormat df = new DecimalFormat();
/* 115 */                   CivMessage.sendTown(town, "The bank is now provides a " + df.format(bank.getInterestRate() * 100.0D) + "% interest rate.");
/*     */                   
/*     */ 
/* 118 */                   return;
/*     */                   
/* 120 */                   Structure struct = town.getStructureByType("s_store");
/* 121 */                   if ((struct != null) && ((struct instanceof Store))) {
/* 122 */                     Store store = (Store)struct;
/* 123 */                     if (store.getLevel() < Integer.valueOf(args[1].trim()).intValue()) {
/* 124 */                       store.setLevel(Integer.valueOf(args[1].trim()).intValue());
/* 125 */                       store.updateSignText();
/* 126 */                       CivMessage.sendTown(town, "The store is now level " + store.getLevel());
/*     */                       
/*     */ 
/* 129 */                       return;
/*     */                       
/* 131 */                       Structure struct = town.getStructureByType("s_store");
/* 132 */                       if ((struct != null) && ((struct instanceof Store))) {
/* 133 */                         Store store = (Store)struct;
/* 134 */                         StoreMaterial mat = new StoreMaterial(args[1].trim(), args[2].trim(), args[3].trim(), args[4].trim());
/* 135 */                         store.addStoreMaterial(mat);
/* 136 */                         store.updateSignText();
/*     */                         
/* 138 */                         return;
/*     */                         
/* 140 */                         Structure struct = town.getStructureByType("s_library");
/* 141 */                         if ((struct != null) && ((struct instanceof Library))) {
/* 142 */                           Library library = (Library)struct;
/* 143 */                           if (library.getLevel() < Integer.valueOf(args[1].trim()).intValue()) {
/* 144 */                             library.setLevel(Integer.valueOf(args[1].trim()).intValue());
/* 145 */                             library.updateSignText();
/* 146 */                             CivMessage.sendTown(town, "The library is now level " + library.getLevel());
/*     */                             
/*     */ 
/* 149 */                             return;
/*     */                             
/* 151 */                             Structure struct = town.getStructureByType("s_library");
/* 152 */                             if ((struct != null) && ((struct instanceof Library))) {
/* 153 */                               Library library = (Library)struct;
/* 154 */                               LibraryEnchantment enchant = new LibraryEnchantment(args[1].trim(), Integer.valueOf(args[2].trim()).intValue(), Double.valueOf(args[3].trim()).doubleValue());
/* 155 */                               library.addEnchant(enchant);
/* 156 */                               library.updateSignText();
/* 157 */                               CivMessage.sendTown(town, "The library now offers the " + args[1].trim() + " enchantment at level " + args[2] + "!");
/*     */                             }
/*     */                           }
/*     */                         }
/* 161 */                       } } } } } } } } } else { Structure struct = town.getStructureByType("s_grocer");
/* 162 */         if ((struct != null) && ((struct instanceof Grocer))) {
/* 163 */           Grocer grocer = (Grocer)struct;
/* 164 */           if (grocer.getLevel() < Integer.valueOf(args[1].trim()).intValue()) {
/* 165 */             grocer.setLevel(Integer.valueOf(args[1].trim()).intValue());
/* 166 */             grocer.updateSignText();
/* 167 */             CivMessage.sendTown(town, "The grocer is now level " + grocer.getLevel());
/*     */           }
/*     */         }
/*     */       }
/*     */       break; }
/*     */   }
/*     */   
/*     */   public boolean isAvailable(Town town) {
/* 175 */     if (CivGlobal.testFileFlag("debug-norequire")) {
/* 176 */       CivMessage.global("Ignoring requirements! debug-norequire found.");
/* 177 */       return true;
/*     */     }
/*     */     
/* 180 */     if ((town.hasUpgrade(this.require_upgrade)) && 
/* 181 */       (town.getCiv().hasTechnology(this.require_tech)) && 
/* 182 */       (town.hasStructure(this.require_structure)) && 
/* 183 */       (!town.hasUpgrade(this.id))) {
/* 184 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 189 */     return false;
/*     */   }
/*     */   
/*     */   public static int getAvailableCategoryCount(String category, Town town) {
/* 193 */     int count = 0;
/*     */     
/* 195 */     for (ConfigTownUpgrade upgrade : CivSettings.townUpgrades.values()) {
/* 196 */       if (((upgrade.category.equalsIgnoreCase(category)) || (category.equalsIgnoreCase("all"))) && 
/* 197 */         (upgrade.isAvailable(town))) {
/* 198 */         count++;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 203 */     return count;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigTownUpgrade.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
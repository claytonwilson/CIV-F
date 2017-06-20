/*     */ package com.avrgaming.civcraft.config;
/*     */ 
/*     */ import com.avrgaming.civcraft.camp.Camp;
/*     */ import com.avrgaming.civcraft.endgame.ConfigEndCondition;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.items.units.Unit;
/*     */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*     */ import com.avrgaming.civcraft.main.CivCraft;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.populators.TradeGoodPreGenerate;
/*     */ import com.avrgaming.civcraft.randomevents.ConfigRandomEvent;
/*     */ import com.avrgaming.civcraft.structure.Wall;
/*     */ import com.avrgaming.civcraft.template.Template;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.global.perks.Perk;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.configuration.InvalidConfigurationException;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.entity.EntityType;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
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
/*     */ public class CivSettings
/*     */ {
/*     */   public static CivCraft plugin;
/*     */   public static final long MOB_REMOVE_INTERVAL = 5000L;
/*     */   public static final int GRACE_DAYS = 3;
/*     */   public static final int CIV_DEBT_GRACE_DAYS = 7;
/*     */   public static final int CIV_DEBT_SELL_DAYS = 14;
/*     */   public static final int CIV_DEBT_TOWN_SELL_DAYS = 21;
/*     */   public static final int TOWN_DEBT_GRACE_DAYS = 7;
/*     */   public static final int TOWN_DEBT_SELL_DAYS = 14;
/*     */   public static float T1_leather_speed;
/*     */   public static float T2_leather_speed;
/*     */   public static float T3_leather_speed;
/*     */   public static float T4_leather_speed;
/*     */   public static float T1_metal_speed;
/*     */   public static float T2_metal_speed;
/*     */   public static float T3_metal_speed;
/*     */   public static float T4_metal_speed;
/*     */   public static float normal_speed;
/*     */   public static double highjump;
/*     */   public static FileConfiguration townConfig;
/*  93 */   public static Map<Integer, ConfigTownLevel> townLevels = new HashMap();
/*  94 */   public static Map<String, ConfigTownUpgrade> townUpgrades = new TreeMap();
/*     */   
/*     */   public static FileConfiguration civConfig;
/*  97 */   public static Map<String, ConfigEndCondition> endConditions = new HashMap();
/*  98 */   public static Map<String, ConfigPlatinumReward> platinumRewards = new HashMap();
/*     */   
/*     */   public static FileConfiguration cultureConfig;
/* 101 */   public static Map<Integer, ConfigCultureLevel> cultureLevels = new HashMap();
/* 102 */   private static Map<String, ConfigCultureBiomeInfo> cultureBiomes = new HashMap();
/*     */   
/*     */   public static FileConfiguration structureConfig;
/* 105 */   public static Map<String, ConfigBuildableInfo> structures = new HashMap();
/* 106 */   public static Map<Integer, ConfigGrocerLevel> grocerLevels = new HashMap();
/* 107 */   public static Map<Integer, ConfigCottageLevel> cottageLevels = new HashMap();
/* 108 */   public static ArrayList<ConfigTempleSacrifice> templeSacrifices = new ArrayList();
/* 109 */   public static Map<Integer, ConfigMineLevel> mineLevels = new HashMap();
/*     */   
/*     */   public static FileConfiguration wonderConfig;
/* 112 */   public static Map<String, ConfigBuildableInfo> wonders = new HashMap();
/* 113 */   public static Map<String, ConfigWonderBuff> wonderBuffs = new HashMap();
/*     */   
/*     */   public static FileConfiguration techsConfig;
/* 116 */   public static Map<String, ConfigTech> techs = new HashMap();
/* 117 */   public static Map<Integer, ConfigTechItem> techItems = new HashMap();
/* 118 */   public static Map<Integer, ConfigTechPotion> techPotions = new HashMap();
/*     */   
/*     */   public static FileConfiguration goodsConfig;
/* 121 */   public static Map<String, ConfigTradeGood> goods = new HashMap();
/* 122 */   public static Map<String, ConfigTradeGood> landGoods = new HashMap();
/* 123 */   public static Map<String, ConfigTradeGood> waterGoods = new HashMap();
/* 124 */   public static Map<String, ConfigHemisphere> hemispheres = new HashMap();
/*     */   
/*     */   public static FileConfiguration buffConfig;
/* 127 */   public static Map<String, ConfigBuff> buffs = new HashMap();
/*     */   
/*     */   public static FileConfiguration unitConfig;
/* 130 */   public static Map<String, ConfigUnit> units = new HashMap();
/*     */   
/*     */   public static FileConfiguration espionageConfig;
/* 133 */   public static Map<String, ConfigMission> missions = new HashMap();
/*     */   
/*     */   public static FileConfiguration governmentConfig;
/* 136 */   public static Map<String, ConfigGovernment> governments = new HashMap();
/*     */   
/* 138 */   public static HashSet<Material> switchItems = new HashSet();
/* 139 */   public static Map<Material, Integer> restrictedItems = new HashMap();
/* 140 */   public static Map<Material, Integer> blockPlaceExceptions = new HashMap();
/* 141 */   public static Map<EntityType, Integer> restrictedSpawns = new HashMap();
/* 142 */   public static HashSet<EntityType> playerEntityWeapons = new HashSet();
/* 143 */   public static HashSet<Integer> alwaysCrumble = new HashSet();
/*     */   
/*     */   public static FileConfiguration warConfig;
/*     */   
/*     */   public static FileConfiguration scoreConfig;
/*     */   
/*     */   public static FileConfiguration perkConfig;
/* 150 */   public static Map<String, ConfigPerk> perks = new HashMap();
/*     */   
/*     */   public static FileConfiguration enchantConfig;
/* 153 */   public static Map<String, ConfigEnchant> enchants = new HashMap();
/*     */   
/*     */   public static float speedtoe_speed;
/*     */   public static double speedtoe_consume;
/*     */   public static int thorhammerchance;
/*     */   public static int punchoutchance;
/*     */   public static FileConfiguration campConfig;
/* 160 */   public static Map<Integer, ConfigCampLonghouseLevel> longhouseLevels = new HashMap();
/* 161 */   public static Map<String, ConfigCampUpgrade> campUpgrades = new HashMap();
/*     */   
/*     */   public static FileConfiguration marketConfig;
/* 164 */   public static Map<Integer, ConfigMarketItem> marketItems = new HashMap();
/*     */   
/* 166 */   public static Set<ConfigStableItem> stableItems = new HashSet();
/* 167 */   public static HashMap<Integer, ConfigStableHorse> horses = new HashMap();
/*     */   
/*     */   public static FileConfiguration happinessConfig;
/* 170 */   public static HashMap<Integer, ConfigTownHappinessLevel> townHappinessLevels = new HashMap();
/* 171 */   public static HashMap<Integer, ConfigHappinessState> happinessStates = new HashMap();
/*     */   
/*     */   public static FileConfiguration materialsConfig;
/* 174 */   public static HashMap<String, ConfigMaterial> materials = new HashMap();
/*     */   
/*     */   public static FileConfiguration randomEventsConfig;
/* 177 */   public static HashMap<String, ConfigRandomEvent> randomEvents = new HashMap();
/* 178 */   public static ArrayList<String> randomEventIDs = new ArrayList();
/*     */   
/*     */   public static FileConfiguration nocheatConfig;
/* 181 */   public static HashMap<String, ConfigValidMod> validMods = new HashMap();
/*     */   
/*     */   public static FileConfiguration arenaConfig;
/* 184 */   public static HashMap<String, ConfigArena> arenas = new HashMap();
/*     */   
/*     */   public static FileConfiguration fishingConfig;
/* 187 */   public static ArrayList<ConfigFishing> fishingDrops = new ArrayList();
/*     */   
/*     */   public static double iron_rate;
/*     */   
/*     */   public static double gold_rate;
/*     */   public static double diamond_rate;
/*     */   public static double emerald_rate;
/*     */   public static double startingCoins;
/* 195 */   public static ArrayList<String> kitItems = new ArrayList();
/* 196 */   public static HashMap<Integer, ConfigRemovedRecipes> removedRecipies = new HashMap();
/* 197 */   public static HashSet<Material> restrictedUndoBlocks = new HashSet();
/* 198 */   public static boolean hasVanishNoPacket = false;
/*     */   public static final String MINI_ADMIN = "civ.admin";
/*     */   public static final String MODERATOR = "civ.moderator";
/*     */   public static final String ECON = "civ.econ";
/*     */   public static final int MARKET_COIN_STEP = 5;
/*     */   public static final int MARKET_BUYSELL_COIN_DIFF = 30;
/*     */   public static final int MARKET_STEP_THRESHOLD = 2;
/*     */   
/*     */   public static void init(JavaPlugin plugin) throws FileNotFoundException, IOException, InvalidConfigurationException, InvalidConfiguration
/*     */   {
/* 208 */     plugin = (CivCraft)plugin;
/*     */     
/* 210 */     initRestrictedItems();
/* 211 */     initRestrictedUndoBlocks();
/* 212 */     initSwitchItems();
/* 213 */     initRestrictedSpawns();
/* 214 */     initBlockPlaceExceptions();
/* 215 */     initPlayerEntityWeapons();
/*     */     
/* 217 */     loadConfigFiles();
/* 218 */     loadConfigObjects();
/*     */     
/* 220 */     Perk.init();
/* 221 */     Unit.init();
/*     */     
/*     */ 
/*     */ 
/* 225 */     T1_leather_speed = (float)getDouble(unitConfig, "base.T1_leather_speed");
/* 226 */     T2_leather_speed = (float)getDouble(unitConfig, "base.T2_leather_speed");
/* 227 */     T3_leather_speed = (float)getDouble(unitConfig, "base.T3_leather_speed");
/* 228 */     T4_leather_speed = (float)getDouble(unitConfig, "base.T4_leather_speed");
/* 229 */     T1_metal_speed = (float)getDouble(unitConfig, "base.T1_metal_speed");
/* 230 */     T2_metal_speed = (float)getDouble(unitConfig, "base.T2_metal_speed");
/* 231 */     T3_metal_speed = (float)getDouble(unitConfig, "base.T3_metal_speed");
/* 232 */     T4_metal_speed = (float)getDouble(unitConfig, "base.T4_metal_speed");
/* 233 */     normal_speed = 0.2F;
/*     */     
/* 235 */     for (Object obj : civConfig.getList("global.start_kit")) {
/* 236 */       if ((obj instanceof String)) {
/* 237 */         kitItems.add((String)obj);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 242 */     CivGlobal.banWords.add("fuck");
/* 243 */     CivGlobal.banWords.add("shit");
/* 244 */     CivGlobal.banWords.add("nigger");
/* 245 */     CivGlobal.banWords.add("faggot");
/* 246 */     CivGlobal.banWords.add("gay");
/* 247 */     CivGlobal.banWords.add("rape");
/* 248 */     CivGlobal.banWords.add("http");
/* 249 */     CivGlobal.banWords.add("cunt");
/*     */     
/* 251 */     iron_rate = getDouble(civConfig, "ore_rates.iron");
/* 252 */     gold_rate = getDouble(civConfig, "ore_rates.gold");
/* 253 */     diamond_rate = getDouble(civConfig, "ore_rates.diamond");
/* 254 */     emerald_rate = getDouble(civConfig, "ore_rates.emerald");
/* 255 */     startingCoins = getDouble(civConfig, "global.starting_coins");
/*     */     
/* 257 */     alwaysCrumble.add(Integer.valueOf(7));
/* 258 */     alwaysCrumble.add(Integer.valueOf(ItemManager.getId(Material.GOLD_BLOCK)));
/* 259 */     alwaysCrumble.add(Integer.valueOf(ItemManager.getId(Material.DIAMOND_BLOCK)));
/* 260 */     alwaysCrumble.add(Integer.valueOf(ItemManager.getId(Material.IRON_BLOCK)));
/* 261 */     alwaysCrumble.add(Integer.valueOf(ItemManager.getId(Material.REDSTONE_BLOCK)));
/*     */     
/* 263 */     LoreEnhancement.init();
/* 264 */     LoreCraftableMaterial.buildStaticMaterials();
/* 265 */     LoreCraftableMaterial.buildRecipes();
/* 266 */     Template.initAttachableTypes();
/*     */     
/* 268 */     if (plugin.hasPlugin("VanishNoPacket")) {
/* 269 */       hasVanishNoPacket = true;
/*     */     }
/*     */   }
/*     */   
/*     */   private static void initRestrictedUndoBlocks()
/*     */   {
/* 275 */     restrictedUndoBlocks.add(Material.CROPS);
/* 276 */     restrictedUndoBlocks.add(Material.CARROT);
/* 277 */     restrictedUndoBlocks.add(Material.POTATO);
/* 278 */     restrictedUndoBlocks.add(Material.REDSTONE);
/* 279 */     restrictedUndoBlocks.add(Material.REDSTONE_TORCH_OFF);
/* 280 */     restrictedUndoBlocks.add(Material.REDSTONE_TORCH_ON);
/*     */   }
/*     */   
/*     */   private static void initPlayerEntityWeapons() {
/* 284 */     playerEntityWeapons.add(EntityType.PLAYER);
/* 285 */     playerEntityWeapons.add(EntityType.ARROW);
/* 286 */     playerEntityWeapons.add(EntityType.EGG);
/* 287 */     playerEntityWeapons.add(EntityType.SNOWBALL);
/* 288 */     playerEntityWeapons.add(EntityType.SPLASH_POTION);
/* 289 */     playerEntityWeapons.add(EntityType.FISHING_HOOK);
/*     */   }
/*     */   
/*     */   public static FileConfiguration loadCivConfig(String filepath) throws FileNotFoundException, IOException, InvalidConfigurationException
/*     */   {
/* 294 */     File file = new File(filepath);
/* 295 */     if (file.exists()) {
/* 296 */       CivLog.info("Loading Configuration:" + filepath);
/*     */       
/*     */ 
/* 299 */       YamlConfiguration cfg = new YamlConfiguration();
/* 300 */       cfg.load(file);
/* 301 */       return cfg;
/*     */     }
/* 303 */     CivLog.error("Configuration file:" + filepath + " is missing!");
/*     */     
/* 305 */     return null;
/*     */   }
/*     */   
/*     */   private static void loadConfigFiles() throws FileNotFoundException, IOException, InvalidConfigurationException {
/* 309 */     townConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/town.yml");
/* 310 */     civConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/civ.yml");
/* 311 */     cultureConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/culture.yml");
/* 312 */     structureConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/structures.yml");
/* 313 */     techsConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/techs.yml");
/* 314 */     goodsConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/goods.yml");
/* 315 */     buffConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/buffs.yml");
/* 316 */     governmentConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/governments.yml");
/* 317 */     warConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/war.yml");
/* 318 */     wonderConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/wonders.yml");
/* 319 */     unitConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/units.yml");
/* 320 */     espionageConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/espionage.yml");
/* 321 */     scoreConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/score.yml");
/* 322 */     perkConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/perks.yml");
/* 323 */     enchantConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/enchantments.yml");
/* 324 */     campConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/camp.yml");
/* 325 */     marketConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/market.yml");
/* 326 */     happinessConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/happiness.yml");
/* 327 */     materialsConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/materials.yml");
/* 328 */     randomEventsConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/randomevents.yml");
/* 329 */     nocheatConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/nocheat.yml");
/* 330 */     arenaConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/arena.yml");
/* 331 */     fishingConfig = loadCivConfig(plugin.getDataFolder().getPath() + "/data/fishing.yml");
/*     */   }
/*     */   
/*     */   private static void loadConfigObjects() throws InvalidConfiguration {
/* 335 */     ConfigTownLevel.loadConfig(townConfig, townLevels);
/* 336 */     ConfigTownUpgrade.loadConfig(townConfig, townUpgrades);
/* 337 */     ConfigCultureLevel.loadConfig(cultureConfig, cultureLevels);
/* 338 */     ConfigBuildableInfo.loadConfig(structureConfig, "structures", structures, false);
/* 339 */     ConfigBuildableInfo.loadConfig(wonderConfig, "wonders", wonders, true);
/* 340 */     ConfigTech.loadConfig(techsConfig, techs);
/* 341 */     ConfigTechItem.loadConfig(techsConfig, techItems);
/* 342 */     ConfigTechPotion.loadConfig(techsConfig, techPotions);
/* 343 */     ConfigHemisphere.loadConfig(goodsConfig, hemispheres);
/* 344 */     ConfigBuff.loadConfig(buffConfig, buffs);
/* 345 */     ConfigWonderBuff.loadConfig(wonderConfig, wonderBuffs);
/* 346 */     ConfigTradeGood.loadConfig(goodsConfig, goods, landGoods, waterGoods);
/* 347 */     ConfigGrocerLevel.loadConfig(structureConfig, grocerLevels);
/* 348 */     ConfigCottageLevel.loadConfig(structureConfig, cottageLevels);
/* 349 */     ConfigTempleSacrifice.loadConfig(structureConfig, templeSacrifices);
/* 350 */     ConfigMineLevel.loadConfig(structureConfig, mineLevels);
/* 351 */     ConfigGovernment.loadConfig(governmentConfig, governments);
/* 352 */     ConfigEnchant.loadConfig(enchantConfig, enchants);
/* 353 */     ConfigUnit.loadConfig(unitConfig, units);
/* 354 */     ConfigMission.loadConfig(espionageConfig, missions);
/* 355 */     ConfigPerk.loadConfig(perkConfig, perks);
/* 356 */     ConfigCampLonghouseLevel.loadConfig(campConfig, longhouseLevels);
/* 357 */     ConfigCampUpgrade.loadConfig(campConfig, campUpgrades);
/* 358 */     ConfigMarketItem.loadConfig(marketConfig, marketItems);
/* 359 */     ConfigStableItem.loadConfig(structureConfig, stableItems);
/* 360 */     ConfigStableHorse.loadConfig(structureConfig, horses);
/* 361 */     ConfigTownHappinessLevel.loadConfig(happinessConfig, townHappinessLevels);
/* 362 */     ConfigHappinessState.loadConfig(happinessConfig, happinessStates);
/* 363 */     ConfigCultureBiomeInfo.loadConfig(cultureConfig, cultureBiomes);
/* 364 */     ConfigMaterial.loadConfig(materialsConfig, materials);
/* 365 */     ConfigRandomEvent.loadConfig(randomEventsConfig, randomEvents, randomEventIDs);
/* 366 */     ConfigEndCondition.loadConfig(civConfig, endConditions);
/* 367 */     ConfigPlatinumReward.loadConfig(civConfig, platinumRewards);
/* 368 */     ConfigValidMod.loadConfig(nocheatConfig, validMods);
/* 369 */     ConfigArena.loadConfig(arenaConfig, arenas);
/* 370 */     ConfigFishing.loadConfig(fishingConfig, fishingDrops);
/*     */     
/* 372 */     ConfigRemovedRecipes.removeRecipes(materialsConfig, removedRecipies);
/* 373 */     CivGlobal.preGenerator.preGenerate();
/* 374 */     Wall.init_settings();
/*     */   }
/*     */   
/*     */   private static void initRestrictedSpawns() {
/* 378 */     restrictedSpawns.put(EntityType.BLAZE, Integer.valueOf(0));
/* 379 */     restrictedSpawns.put(EntityType.CAVE_SPIDER, Integer.valueOf(0));
/* 380 */     restrictedSpawns.put(EntityType.CREEPER, Integer.valueOf(0));
/* 381 */     restrictedSpawns.put(EntityType.ENDER_DRAGON, Integer.valueOf(0));
/* 382 */     restrictedSpawns.put(EntityType.ENDERMAN, Integer.valueOf(0));
/* 383 */     restrictedSpawns.put(EntityType.GHAST, Integer.valueOf(0));
/* 384 */     restrictedSpawns.put(EntityType.GIANT, Integer.valueOf(0));
/* 385 */     restrictedSpawns.put(EntityType.PIG_ZOMBIE, Integer.valueOf(0));
/* 386 */     restrictedSpawns.put(EntityType.SILVERFISH, Integer.valueOf(0));
/* 387 */     restrictedSpawns.put(EntityType.SKELETON, Integer.valueOf(0));
/* 388 */     restrictedSpawns.put(EntityType.SLIME, Integer.valueOf(0));
/* 389 */     restrictedSpawns.put(EntityType.SPIDER, Integer.valueOf(0));
/* 390 */     restrictedSpawns.put(EntityType.WITCH, Integer.valueOf(0));
/* 391 */     restrictedSpawns.put(EntityType.WITHER, Integer.valueOf(0));
/* 392 */     restrictedSpawns.put(EntityType.ZOMBIE, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   private static void initRestrictedItems()
/*     */   {
/* 397 */     restrictedItems.put(Material.FLINT_AND_STEEL, Integer.valueOf(0));
/* 398 */     restrictedItems.put(Material.BUCKET, Integer.valueOf(0));
/* 399 */     restrictedItems.put(Material.WATER_BUCKET, Integer.valueOf(0));
/* 400 */     restrictedItems.put(Material.LAVA_BUCKET, Integer.valueOf(0));
/* 401 */     restrictedItems.put(Material.CAKE_BLOCK, Integer.valueOf(0));
/* 402 */     restrictedItems.put(Material.CAULDRON, Integer.valueOf(0));
/* 403 */     restrictedItems.put(Material.DIODE, Integer.valueOf(0));
/* 404 */     restrictedItems.put(Material.INK_SACK, Integer.valueOf(0));
/* 405 */     restrictedItems.put(Material.ITEM_FRAME, Integer.valueOf(0));
/* 406 */     restrictedItems.put(Material.PAINTING, Integer.valueOf(0));
/* 407 */     restrictedItems.put(Material.SHEARS, Integer.valueOf(0));
/* 408 */     restrictedItems.put(Material.STATIONARY_LAVA, Integer.valueOf(0));
/* 409 */     restrictedItems.put(Material.STATIONARY_WATER, Integer.valueOf(0));
/* 410 */     restrictedItems.put(Material.TNT, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   private static void initSwitchItems()
/*     */   {
/* 415 */     switchItems.add(Material.ANVIL);
/* 416 */     switchItems.add(Material.BEACON);
/* 417 */     switchItems.add(Material.BREWING_STAND);
/* 418 */     switchItems.add(Material.BURNING_FURNACE);
/* 419 */     switchItems.add(Material.CAKE_BLOCK);
/* 420 */     switchItems.add(Material.CAULDRON);
/* 421 */     switchItems.add(Material.CHEST);
/* 422 */     switchItems.add(Material.COMMAND);
/* 423 */     switchItems.add(Material.DIODE);
/* 424 */     switchItems.add(Material.DIODE_BLOCK_OFF);
/* 425 */     switchItems.add(Material.DIODE_BLOCK_ON);
/* 426 */     switchItems.add(Material.DISPENSER);
/* 427 */     switchItems.add(Material.FENCE_GATE);
/* 428 */     switchItems.add(Material.FURNACE);
/* 429 */     switchItems.add(Material.JUKEBOX);
/* 430 */     switchItems.add(Material.LEVER);
/*     */     
/* 432 */     switchItems.add(Material.STONE_BUTTON);
/* 433 */     switchItems.add(Material.STONE_PLATE);
/* 434 */     switchItems.add(Material.IRON_DOOR);
/* 435 */     switchItems.add(Material.TNT);
/* 436 */     switchItems.add(Material.TRAP_DOOR);
/* 437 */     switchItems.add(Material.WOOD_DOOR);
/* 438 */     switchItems.add(Material.WOODEN_DOOR);
/* 439 */     switchItems.add(Material.WOOD_PLATE);
/*     */     
/*     */ 
/*     */ 
/* 443 */     switchItems.add(Material.HOPPER);
/* 444 */     switchItems.add(Material.HOPPER_MINECART);
/* 445 */     switchItems.add(Material.DROPPER);
/* 446 */     switchItems.add(Material.REDSTONE_COMPARATOR);
/* 447 */     switchItems.add(Material.REDSTONE_COMPARATOR_ON);
/* 448 */     switchItems.add(Material.REDSTONE_COMPARATOR_OFF);
/* 449 */     switchItems.add(Material.TRAPPED_CHEST);
/* 450 */     switchItems.add(Material.GOLD_PLATE);
/* 451 */     switchItems.add(Material.IRON_PLATE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void initBlockPlaceExceptions()
/*     */   {
/* 461 */     blockPlaceExceptions.put(Material.FIRE, Integer.valueOf(0));
/* 462 */     blockPlaceExceptions.put(Material.PORTAL, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   public static String getStringBase(String path) throws InvalidConfiguration {
/* 466 */     return getString(plugin.getConfig(), path);
/*     */   }
/*     */   
/*     */   public static double getDoubleTown(String path) throws InvalidConfiguration {
/* 470 */     return getDouble(townConfig, path);
/*     */   }
/*     */   
/*     */   public static double getDoubleCiv(String path) throws InvalidConfiguration {
/* 474 */     return getDouble(civConfig, path);
/*     */   }
/*     */   
/*     */   public static void saveGenID(String gen_id) {
/*     */     try {
/* 479 */       Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("plugins/CivCraft/genid.data")));
/* 480 */       writer.write(gen_id);
/* 481 */       writer.close();
/*     */     } catch (IOException e) {
/* 483 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public static String getGenID() {
/* 488 */     String genid = null;
/*     */     try {
/* 490 */       BufferedReader br = new BufferedReader(new FileReader("plugins/CivCraft/genid.data"));
/* 491 */       genid = br.readLine();
/* 492 */       br.close();
/*     */     }
/*     */     catch (IOException localIOException) {}
/* 495 */     return genid;
/*     */   }
/*     */   
/*     */   public static Double getDoubleStructure(String path) {
/*     */     Double ret;
/*     */     try {
/* 501 */       ret = Double.valueOf(getDouble(structureConfig, path));
/*     */     } catch (InvalidConfiguration e) { Double ret;
/* 503 */       ret = Double.valueOf(0.0D);
/* 504 */       e.printStackTrace();
/*     */     }
/* 506 */     return ret;
/*     */   }
/*     */   
/*     */   public static int getIntegerStructure(String path) {
/*     */     Integer ret;
/*     */     try {
/* 512 */       ret = getInteger(structureConfig, path);
/*     */     } catch (InvalidConfiguration e) { Integer ret;
/* 514 */       ret = Integer.valueOf(0);
/* 515 */       e.printStackTrace();
/*     */     }
/* 517 */     return ret.intValue();
/*     */   }
/*     */   
/*     */   public static Integer getIntegerGovernment(String path) {
/*     */     Integer ret;
/*     */     try {
/* 523 */       ret = getInteger(governmentConfig, path);
/*     */     } catch (InvalidConfiguration e) { Integer ret;
/* 525 */       ret = Integer.valueOf(0);
/* 526 */       e.printStackTrace();
/*     */     }
/* 528 */     return ret;
/*     */   }
/*     */   
/*     */   public static Integer getInteger(FileConfiguration cfg, String path) throws InvalidConfiguration {
/* 532 */     if (!cfg.contains(path)) {
/* 533 */       throw new InvalidConfiguration("Could not get configuration integer " + path);
/*     */     }
/*     */     
/* 536 */     int data = cfg.getInt(path);
/* 537 */     return Integer.valueOf(data);
/*     */   }
/*     */   
/*     */   public static String getString(FileConfiguration cfg, String path) throws InvalidConfiguration {
/* 541 */     String data = cfg.getString(path);
/* 542 */     if (data == null) {
/* 543 */       throw new InvalidConfiguration("Could not get configuration string " + path);
/*     */     }
/* 545 */     return data;
/*     */   }
/*     */   
/*     */   public static double getDouble(FileConfiguration cfg, String path) throws InvalidConfiguration {
/* 549 */     if (!cfg.contains(path)) {
/* 550 */       throw new InvalidConfiguration("Could not get configuration double " + path);
/*     */     }
/*     */     
/* 553 */     double data = cfg.getDouble(path);
/* 554 */     return data;
/*     */   }
/*     */   
/*     */   public static int getMaxNameLength()
/*     */   {
/* 559 */     return 32;
/*     */   }
/*     */   
/*     */   public static String getNameCheckRegex() throws InvalidConfiguration {
/* 563 */     return getStringBase("regex.name_check_regex");
/*     */   }
/*     */   
/*     */   public static String getNameFilterRegex() throws InvalidConfiguration {
/* 567 */     return getStringBase("regex.name_filter_regex");
/*     */   }
/*     */   
/*     */   public static String getNameRemoveRegex() throws InvalidConfiguration {
/* 571 */     return getStringBase("regex.name_remove_regex");
/*     */   }
/*     */   
/*     */   public static ConfigTownUpgrade getUpgradeByName(String name) {
/* 575 */     for (ConfigTownUpgrade upgrade : townUpgrades.values()) {
/* 576 */       if (upgrade.name.equalsIgnoreCase(name)) {
/* 577 */         return upgrade;
/*     */       }
/*     */     }
/* 580 */     return null;
/*     */   }
/*     */   
/*     */   public static ConfigHappinessState getHappinessState(double amount) {
/* 584 */     ConfigHappinessState closestState = (ConfigHappinessState)happinessStates.get(Integer.valueOf(0));
/*     */     
/* 586 */     for (int i = 0; i < happinessStates.size(); i++) {
/* 587 */       ConfigHappinessState state = (ConfigHappinessState)happinessStates.get(Integer.valueOf(i));
/* 588 */       amount = Math.round(amount * 100.0D) / 100.0D;
/* 589 */       if (amount >= state.amount) {
/* 590 */         closestState = state;
/*     */       }
/*     */     }
/*     */     
/* 594 */     return closestState;
/*     */   }
/*     */   
/*     */   public static ConfigTownUpgrade getUpgradeByNameRegex(Town town, String name) throws CivException {
/* 598 */     ConfigTownUpgrade returnUpgrade = null;
/* 599 */     for (ConfigTownUpgrade upgrade : townUpgrades.values()) {
/* 600 */       if (upgrade.isAvailable(town))
/*     */       {
/*     */ 
/*     */ 
/* 604 */         if (name.equalsIgnoreCase(upgrade.name)) {
/* 605 */           return upgrade;
/*     */         }
/*     */         
/* 608 */         String loweredUpgradeName = upgrade.name.toLowerCase();
/* 609 */         String loweredName = name.toLowerCase();
/*     */         
/* 611 */         if (loweredUpgradeName.contains(loweredName)) {
/* 612 */           if (returnUpgrade == null) {
/* 613 */             returnUpgrade = upgrade;
/*     */           } else
/* 615 */             throw new CivException(name + " is not specific enough to single out only one upgrade.");
/*     */         }
/*     */       }
/*     */     }
/* 619 */     return returnUpgrade;
/*     */   }
/*     */   
/*     */   public static ConfigCampUpgrade getCampUpgradeByNameRegex(Camp camp, String name) throws CivException {
/* 623 */     ConfigCampUpgrade returnUpgrade = null;
/* 624 */     for (ConfigCampUpgrade upgrade : campUpgrades.values()) {
/* 625 */       if (upgrade.isAvailable(camp))
/*     */       {
/*     */ 
/*     */ 
/* 629 */         if (name.equalsIgnoreCase(upgrade.name)) {
/* 630 */           return upgrade;
/*     */         }
/*     */         
/* 633 */         String loweredUpgradeName = upgrade.name.toLowerCase();
/* 634 */         String loweredName = name.toLowerCase();
/*     */         
/* 636 */         if (loweredUpgradeName.contains(loweredName)) {
/* 637 */           if (returnUpgrade == null) {
/* 638 */             returnUpgrade = upgrade;
/*     */           } else
/* 640 */             throw new CivException(name + " is not specific enough to single out only one upgrade.");
/*     */         }
/*     */       }
/*     */     }
/* 644 */     return returnUpgrade;
/*     */   }
/*     */   
/*     */   public static ConfigBuildableInfo getBuildableInfoByName(String fullArgs) {
/* 648 */     for (ConfigBuildableInfo sinfo : structures.values()) {
/* 649 */       if (sinfo.displayName.equalsIgnoreCase(fullArgs)) {
/* 650 */         return sinfo;
/*     */       }
/*     */     }
/*     */     
/* 654 */     for (ConfigBuildableInfo sinfo : wonders.values()) {
/* 655 */       if (sinfo.displayName.equalsIgnoreCase(fullArgs)) {
/* 656 */         return sinfo;
/*     */       }
/*     */     }
/*     */     
/* 660 */     return null;
/*     */   }
/*     */   
/*     */   public static ConfigTech getTechByName(String techname) {
/* 664 */     for (ConfigTech tech : techs.values()) {
/* 665 */       if (tech.name.equalsIgnoreCase(techname)) {
/* 666 */         return tech;
/*     */       }
/*     */     }
/* 669 */     return null;
/*     */   }
/*     */   
/*     */   public static int getCottageMaxLevel() {
/* 673 */     int returnLevel = 0;
/* 674 */     for (Integer level : cottageLevels.keySet()) {
/* 675 */       if (returnLevel < level.intValue()) {
/* 676 */         returnLevel = level.intValue();
/*     */       }
/*     */     }
/*     */     
/* 680 */     return returnLevel;
/*     */   }
/*     */   
/*     */   public static int getMineMaxLevel() {
/* 684 */     int returnLevel = 0;
/* 685 */     for (Integer level : mineLevels.keySet()) {
/* 686 */       if (returnLevel < level.intValue()) {
/* 687 */         returnLevel = level.intValue();
/*     */       }
/*     */     }
/*     */     
/* 691 */     return returnLevel;
/*     */   }
/*     */   
/*     */   public static int getMaxCultureLevel() {
/* 695 */     int returnLevel = 0;
/* 696 */     for (Integer level : cultureLevels.keySet()) {
/* 697 */       if (returnLevel < level.intValue()) {
/* 698 */         returnLevel = level.intValue();
/*     */       }
/*     */     }
/*     */     
/* 702 */     return returnLevel;
/*     */   }
/*     */   
/*     */ 
/*     */   public static ConfigCultureBiomeInfo getCultureBiome(String name)
/*     */   {
/* 708 */     ConfigCultureBiomeInfo biomeInfo = (ConfigCultureBiomeInfo)cultureBiomes.get(name);
/* 709 */     if (biomeInfo == null) {
/* 710 */       biomeInfo = (ConfigCultureBiomeInfo)cultureBiomes.get("UNKNOWN");
/*     */     }
/*     */     
/* 713 */     return biomeInfo;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\CivSettings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*      */ package com.avrgaming.civcraft.object;
/*      */ 
/*      */ import com.avrgaming.civcraft.camp.WarCamp;
/*      */ import com.avrgaming.civcraft.config.CivSettings;
/*      */ import com.avrgaming.civcraft.config.ConfigGovernment;
/*      */ import com.avrgaming.civcraft.config.ConfigTech;
/*      */ import com.avrgaming.civcraft.database.SQL;
/*      */ import com.avrgaming.civcraft.database.SQLUpdate;
/*      */ import com.avrgaming.civcraft.endgame.EndConditionScience;
/*      */ import com.avrgaming.civcraft.endgame.EndGameCondition;
/*      */ import com.avrgaming.civcraft.exception.CivException;
/*      */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*      */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*      */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*      */ import com.avrgaming.civcraft.main.CivGlobal;
/*      */ import com.avrgaming.civcraft.main.CivLog;
/*      */ import com.avrgaming.civcraft.main.CivMessage;
/*      */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*      */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*      */ import com.avrgaming.civcraft.structure.Capitol;
/*      */ import com.avrgaming.civcraft.structure.RespawnLocationHolder;
/*      */ import com.avrgaming.civcraft.structure.Structure;
/*      */ import com.avrgaming.civcraft.structure.TownHall;
/*      */ import com.avrgaming.civcraft.threading.TaskMaster;
/*      */ import com.avrgaming.civcraft.threading.tasks.UpdateTechBar;
/*      */ import com.avrgaming.civcraft.util.BlockCoord;
/*      */ import com.avrgaming.civcraft.util.ChunkCoord;
/*      */ import com.avrgaming.civcraft.util.DateUtil;
/*      */ import com.avrgaming.civcraft.util.ItemManager;
/*      */ import java.io.PrintStream;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.text.DecimalFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import org.bukkit.Location;
/*      */ import org.bukkit.Material;
/*      */ import org.bukkit.entity.Player;
/*      */ import org.bukkit.inventory.ItemStack;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Civilization
/*      */   extends SQLObject
/*      */ {
/*   71 */   private Map<String, ConfigTech> techs = new ConcurrentHashMap();
/*      */   
/*      */   private int color;
/*   74 */   private int daysInDebt = 0;
/*      */   private double incomeTaxRate;
/*      */   private double sciencePercentage;
/*   77 */   private ConfigTech researchTech = null;
/*   78 */   private double researchProgress = 0.0D;
/*      */   
/*      */   private EconObject treasury;
/*      */   
/*      */   private PermissionGroup leaderGroup;
/*      */   
/*      */   private PermissionGroup adviserGroup;
/*      */   
/*      */   private String leaderName;
/*      */   private String leaderGroupName;
/*      */   private String advisersGroupName;
/*      */   private String capitolName;
/*   90 */   private ConcurrentHashMap<String, Town> towns = new ConcurrentHashMap();
/*      */   
/*      */   private ConfigGovernment government;
/*   93 */   private double baseBeakers = 1.0D;
/*      */   
/*      */   public static final int HEX_COLOR_MAX = 16777215;
/*      */   
/*      */   public static final int HEX_COLOR_TOLERANCE = 40;
/*      */   
/*   99 */   public HashMap<String, Double> lastUpkeepPaidMap = new HashMap();
/*      */   
/*      */ 
/*  102 */   public HashMap<String, Double> lastTaxesPaidMap = new HashMap();
/*      */   
/*      */ 
/*  105 */   private int lastTechPercentage = 0;
/*      */   
/*  107 */   private DiplomacyManager diplomacyManager = new DiplomacyManager(this);
/*      */   
/*  109 */   private boolean adminCiv = false;
/*  110 */   private boolean conquered = false;
/*      */   
/*  112 */   private Date conquer_date = null;
/*  113 */   private Date created_date = null;
/*      */   
/*  115 */   public boolean scoutDebug = false;
/*  116 */   public String scoutDebugPlayer = null;
/*      */   
/*  118 */   private LinkedList<WarCamp> warCamps = new LinkedList();
/*      */   
/*      */   public Civilization(String name, String capitolName, Resident leader) throws InvalidNameException {
/*  121 */     setName(name);
/*  122 */     this.leaderName = leader.getName();
/*  123 */     setCapitolName(capitolName);
/*      */     
/*  125 */     this.government = ((ConfigGovernment)CivSettings.governments.get("gov_tribalism"));
/*  126 */     this.color = pickCivColor();
/*  127 */     setTreasury(new EconObject(this));
/*  128 */     getTreasury().setBalance(0.0D, false);
/*  129 */     this.created_date = new Date();
/*  130 */     loadSettings();
/*      */   }
/*      */   
/*      */   public Civilization(ResultSet rs) throws SQLException, InvalidNameException {
/*  134 */     load(rs);
/*  135 */     loadSettings();
/*      */   }
/*      */   
/*      */   public void loadSettings() {
/*      */     try {
/*  140 */       this.baseBeakers = CivSettings.getDouble(CivSettings.civConfig, "civ.base_beaker_rate");
/*      */     } catch (InvalidConfiguration e) {
/*  142 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*  146 */   public static String TABLE_NAME = "CIVILIZATIONS";
/*      */   
/*  148 */   public static void init() throws SQLException { if (!SQL.hasTable(TABLE_NAME)) {
/*  149 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + TABLE_NAME + " (" + 
/*  150 */         "`id` int(11) unsigned NOT NULL auto_increment," + 
/*  151 */         "`name` VARCHAR(64) NOT NULL," + 
/*  152 */         "`leaderName` mediumtext," + 
/*  153 */         "`capitolName` mediumtext," + 
/*  154 */         "`debt` float NOT NULL DEFAULT '0'," + 
/*  155 */         "`coins` double DEFAULT 0," + 
/*  156 */         "`daysInDebt` int NOT NULL DEFAULT '0'," + 
/*  157 */         "`techs` mediumtext DEFAULT NULL," + 
/*  158 */         "`researchTech` mediumtext DEFAULT NULL," + 
/*  159 */         "`researchProgress` float NOT NULL DEFAULT 0," + 
/*  160 */         "`researched` mediumtext DEFAULT NULL, " + 
/*  161 */         "`government_id` mediumtext DEFAULT NULL," + 
/*  162 */         "`color` int(11) DEFAULT 0," + 
/*  163 */         "`income_tax_rate` float NOT NULL DEFAULT 0," + 
/*  164 */         "`science_percentage` float NOT NULL DEFAULT 0," + 
/*  165 */         "`leaderGroupName` mediumtext DEFAULT NULL," + 
/*  166 */         "`advisersGroupName` mediumtext DEFAULT NULL," + 
/*  167 */         "`lastUpkeepTick` mediumtext DEFAULT NULL," + 
/*  168 */         "`lastTaxesTick` mediumtext DEFAULT NULL," + 
/*  169 */         "`adminCiv` boolean DEFAULT false," + 
/*  170 */         "`conquered` boolean DEFAULT false," + 
/*  171 */         "`conquered_date` long," + 
/*  172 */         "`created_date` long," + 
/*  173 */         "UNIQUE KEY (`name`), " + 
/*  174 */         "PRIMARY KEY (`id`)" + ")";
/*      */       
/*  176 */       SQL.makeTable(table_create);
/*  177 */       CivLog.info("Created " + TABLE_NAME + " table");
/*      */     } else {
/*  179 */       CivLog.info(TABLE_NAME + " table OK!");
/*  180 */       SQL.makeCol("conquered", "booelan", TABLE_NAME);
/*  181 */       SQL.makeCol("conquered_date", "long", TABLE_NAME);
/*  182 */       SQL.makeCol("created_date", "long", TABLE_NAME);
/*      */     }
/*      */   }
/*      */   
/*      */   public void load(ResultSet rs) throws SQLException, InvalidNameException
/*      */   {
/*  188 */     setId(rs.getInt("id"));
/*  189 */     setName(rs.getString("name"));
/*      */     
/*  191 */     this.leaderName = rs.getString("leaderName");
/*  192 */     this.capitolName = rs.getString("capitolName");
/*  193 */     setLeaderGroupName(rs.getString("leaderGroupName"));
/*  194 */     setAdvisersGroupName(rs.getString("advisersGroupName"));
/*  195 */     this.daysInDebt = rs.getInt("daysInDebt");
/*  196 */     this.color = rs.getInt("color");
/*  197 */     setResearchTech((ConfigTech)CivSettings.techs.get(rs.getString("researchTech")));
/*  198 */     setResearchProgress(rs.getDouble("researchProgress"));
/*  199 */     setGovernment(rs.getString("government_id"));
/*  200 */     loadKeyValueString(rs.getString("lastUpkeepTick"), this.lastUpkeepPaidMap);
/*  201 */     loadKeyValueString(rs.getString("lastTaxesTick"), this.lastTaxesPaidMap);
/*  202 */     setSciencePercentage(rs.getDouble("science_percentage"));
/*      */     
/*  204 */     double taxes = rs.getDouble("income_tax_rate");
/*  205 */     if (taxes > this.government.maximum_tax_rate) {
/*  206 */       taxes = this.government.maximum_tax_rate;
/*      */     }
/*      */     
/*  209 */     setIncomeTaxRate(taxes);
/*  210 */     loadResearchedTechs(rs.getString("researched"));
/*  211 */     this.adminCiv = rs.getBoolean("adminCiv");
/*  212 */     this.conquered = rs.getBoolean("conquered");
/*  213 */     Long ctime = Long.valueOf(rs.getLong("conquered_date"));
/*  214 */     if ((ctime == null) || (ctime.longValue() == 0L)) {
/*  215 */       this.conquer_date = null;
/*      */     } else {
/*  217 */       this.conquer_date = new Date(ctime.longValue());
/*      */     }
/*      */     
/*  220 */     ctime = Long.valueOf(rs.getLong("created_date"));
/*  221 */     if ((ctime == null) || (ctime.longValue() == 0L)) {
/*  222 */       this.created_date = new Date(0L);
/*      */     } else {
/*  224 */       this.created_date = new Date(ctime.longValue());
/*      */     }
/*      */     
/*  227 */     setTreasury(new EconObject(this));
/*  228 */     getTreasury().setBalance(rs.getDouble("coins"), false);
/*  229 */     getTreasury().setDebt(rs.getDouble("debt"));
/*      */   }
/*      */   
/*      */   public void save()
/*      */   {
/*  234 */     SQLUpdate.add(this);
/*      */   }
/*      */   
/*      */   public void saveNow() throws SQLException
/*      */   {
/*  239 */     HashMap<String, Object> hashmap = new HashMap();
/*  240 */     hashmap.put("name", getName());
/*  241 */     hashmap.put("leaderName", this.leaderName);
/*  242 */     hashmap.put("capitolName", this.capitolName);
/*  243 */     hashmap.put("leaderGroupName", getLeaderGroupName());
/*  244 */     hashmap.put("advisersGroupName", getAdvisersGroupName());
/*  245 */     hashmap.put("debt", Double.valueOf(getTreasury().getDebt()));
/*  246 */     hashmap.put("coins", Double.valueOf(getTreasury().getBalance()));
/*  247 */     hashmap.put("daysInDebt", Integer.valueOf(this.daysInDebt));
/*  248 */     hashmap.put("income_tax_rate", Double.valueOf(getIncomeTaxRate()));
/*  249 */     hashmap.put("science_percentage", Double.valueOf(getSciencePercentage()));
/*  250 */     hashmap.put("color", Integer.valueOf(getColor()));
/*  251 */     if (getResearchTech() != null) {
/*  252 */       hashmap.put("researchTech", getResearchTech().id);
/*      */     } else {
/*  254 */       hashmap.put("researchTech", null);
/*      */     }
/*  256 */     hashmap.put("researchProgress", Double.valueOf(getResearchProgress()));
/*  257 */     hashmap.put("government_id", getGovernment().id);
/*  258 */     hashmap.put("lastUpkeepTick", saveKeyValueString(this.lastUpkeepPaidMap));
/*  259 */     hashmap.put("lastTaxesTick", saveKeyValueString(this.lastTaxesPaidMap));
/*  260 */     hashmap.put("researched", saveResearchedTechs());
/*  261 */     hashmap.put("adminCiv", Boolean.valueOf(this.adminCiv));
/*  262 */     hashmap.put("conquered", Boolean.valueOf(this.conquered));
/*  263 */     if (this.conquer_date != null) {
/*  264 */       hashmap.put("conquered_date", Long.valueOf(this.conquer_date.getTime()));
/*      */     } else {
/*  266 */       hashmap.put("conquered_date", null);
/*      */     }
/*      */     
/*  269 */     if (this.created_date != null) {
/*  270 */       hashmap.put("created_date", Long.valueOf(this.created_date.getTime()));
/*      */     } else {
/*  272 */       hashmap.put("created_date", null);
/*      */     }
/*      */     
/*  275 */     SQL.updateNamedObject(this, hashmap, TABLE_NAME);
/*      */   }
/*      */   
/*      */   private void loadResearchedTechs(String techstring) {
/*  279 */     if ((techstring == null) || (techstring.equals(""))) {
/*  280 */       return;
/*      */     }
/*      */     
/*  283 */     String[] techs = techstring.split(",");
/*      */     String[] arrayOfString1;
/*  285 */     int j = (arrayOfString1 = techs).length; for (int i = 0; i < j; i++) { String tech = arrayOfString1[i];
/*  286 */       ConfigTech t = (ConfigTech)CivSettings.techs.get(tech);
/*  287 */       if (t != null) {
/*  288 */         CivGlobal.researchedTechs.add(t.id.toLowerCase());
/*  289 */         this.techs.put(tech, t);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private Object saveResearchedTechs() {
/*  295 */     String out = "";
/*      */     
/*  297 */     for (ConfigTech tech : this.techs.values()) {
/*  298 */       out = out + tech.id + ",";
/*      */     }
/*      */     
/*  301 */     return out;
/*      */   }
/*      */   
/*      */   private void loadKeyValueString(String string, HashMap<String, Double> map)
/*      */   {
/*  306 */     String[] keyvalues = string.split(";");
/*      */     String[] arrayOfString1;
/*  308 */     int j = (arrayOfString1 = keyvalues).length; for (int i = 0; i < j; i++) { String keyvalue = arrayOfString1[i];
/*      */       try {
/*  310 */         String key = keyvalue.split(":")[0];
/*  311 */         String value = keyvalue.split(":")[1];
/*      */         
/*  313 */         map.put(key, Double.valueOf(value));
/*      */       }
/*      */       catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {}
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private String saveKeyValueString(HashMap<String, Double> map)
/*      */   {
/*  322 */     String out = "";
/*      */     
/*  324 */     for (String key : map.keySet()) {
/*  325 */       double value = ((Double)map.get(key)).doubleValue();
/*  326 */       out = out + key + ":" + value + ";";
/*      */     }
/*  328 */     return out;
/*      */   }
/*      */   
/*      */   public boolean hasTechnology(String require_tech)
/*      */   {
/*  333 */     if (require_tech != null) {
/*  334 */       String[] split = require_tech.split(":");
/*  335 */       String[] arrayOfString1; int j = (arrayOfString1 = split).length; for (int i = 0; i < j; i++) { String str = arrayOfString1[i];
/*  336 */         if (!hasTech(str)) {
/*  337 */           return false;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  342 */     return true;
/*      */   }
/*      */   
/*      */   private boolean hasTech(String configId) {
/*  346 */     if ((configId == null) || (configId.equals(""))) {
/*  347 */       return true;
/*      */     }
/*      */     
/*  350 */     return this.techs.containsKey(configId);
/*      */   }
/*      */   
/*      */   public void addTech(ConfigTech t) {
/*  354 */     CivGlobal.researchedTechs.add(t.id.toLowerCase());
/*  355 */     this.techs.put(t.id, t);
/*      */     
/*  357 */     for (Town town : getTowns()) {
/*  358 */       town.onTechUpdate();
/*      */     }
/*      */   }
/*      */   
/*      */   public void removeTech(ConfigTech t)
/*      */   {
/*  364 */     removeTech(t.id);
/*      */   }
/*      */   
/*      */   public void removeTech(String configId) {
/*  368 */     this.techs.remove(configId);
/*      */     
/*  370 */     for (Town town : getTowns()) {
/*  371 */       town.onTechUpdate();
/*      */     }
/*      */   }
/*      */   
/*      */   public ConfigGovernment getGovernment() {
/*  376 */     return this.government;
/*      */   }
/*      */   
/*      */   public void setGovernment(String gov_id) {
/*  380 */     this.government = ((ConfigGovernment)CivSettings.governments.get(gov_id));
/*      */     
/*  382 */     if (getSciencePercentage() > this.government.maximum_tax_rate) {
/*  383 */       setSciencePercentage(this.government.maximum_tax_rate);
/*      */     }
/*      */   }
/*      */   
/*      */   public int getColor()
/*      */   {
/*  389 */     return this.color;
/*      */   }
/*      */   
/*      */   public void setColor(int color) {
/*  393 */     this.color = color;
/*      */   }
/*      */   
/*      */   public Resident getLeader() {
/*  397 */     return CivGlobal.getResident(this.leaderName);
/*      */   }
/*      */   
/*      */   public void setLeader(Resident leader) {
/*  401 */     this.leaderName = leader.getName();
/*      */   }
/*      */   
/*      */ 
/*      */   public void delete()
/*      */     throws SQLException
/*      */   {
/*  408 */     if (this.leaderGroup != null) {
/*  409 */       this.leaderGroup.delete();
/*      */     }
/*      */     
/*  412 */     if (this.adviserGroup != null) {
/*  413 */       this.adviserGroup.delete();
/*      */     }
/*      */     
/*      */ 
/*  417 */     for (Town t : getTowns()) {
/*  418 */       t.delete();
/*      */     }
/*      */     
/*      */ 
/*  422 */     this.diplomacyManager.deleteAllRelations();
/*      */     
/*  424 */     SQL.deleteNamedObject(this, TABLE_NAME);
/*  425 */     CivGlobal.removeCiv(this);
/*  426 */     if (isConquered()) {
/*  427 */       CivGlobal.removeConqueredCiv(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public EconObject getTreasury() {
/*  432 */     return this.treasury;
/*      */   }
/*      */   
/*      */   public void setTreasury(EconObject treasury) {
/*  436 */     this.treasury = treasury;
/*      */   }
/*      */   
/*      */   public String getLeaderGroupName() {
/*  440 */     return "leaders";
/*      */   }
/*      */   
/*      */   public void setLeaderGroupName(String leaderGroupName) {
/*  444 */     this.leaderGroupName = "leaders";
/*      */   }
/*      */   
/*      */   public String getAdvisersGroupName() {
/*  448 */     return "advisers";
/*      */   }
/*      */   
/*      */   public void setAdvisersGroupName(String advisersGroupName) {
/*  452 */     this.advisersGroupName = "advisers";
/*      */   }
/*      */   
/*      */   public double getIncomeTaxRate() {
/*  456 */     return this.incomeTaxRate;
/*      */   }
/*      */   
/*      */   public void setIncomeTaxRate(double taxRate) {
/*  460 */     this.incomeTaxRate = taxRate;
/*      */   }
/*      */   
/*      */   public Town getTown(String name) {
/*  464 */     return (Town)this.towns.get(name.toLowerCase());
/*      */   }
/*      */   
/*      */   public void addTown(Town town) {
/*  468 */     this.towns.put(town.getName().toLowerCase(), town);
/*      */   }
/*      */   
/*      */   public int getTownCount() {
/*  472 */     return this.towns.size();
/*      */   }
/*      */   
/*      */   public String getIncomeTaxRateString() {
/*  476 */     return this.incomeTaxRate * 100.0D + "%";
/*      */   }
/*      */   
/*      */   public static void newCiv(String name, String capitolName, Resident resident, Player player, Location loc)
/*      */     throws CivException
/*      */   {
/*  482 */     ItemStack stack = player.getItemInHand();
/*      */     
/*      */ 
/*      */ 
/*  486 */     LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/*  487 */     if ((craftMat == null) || (!craftMat.hasComponent("FoundCivilization"))) {
/*  488 */       throw new CivException("You must be holding an item that can found a Civilization.");
/*      */     }
/*      */     
/*  491 */     Civilization existCiv = CivGlobal.getCiv(name);
/*  492 */     if (existCiv != null) {
/*  493 */       throw new CivException("A Civilization named " + name + " already exists!");
/*      */     }
/*      */     
/*  496 */     Town existTown = CivGlobal.getTown(capitolName);
/*  497 */     if (existTown != null) {
/*  498 */       throw new CivException("A town named " + capitolName + " already exists!");
/*      */     }
/*      */     
/*  501 */     if (resident.hasCamp()) {
/*  502 */       throw new CivException("You must first leave your camp before founding a civilization.");
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  507 */       int min_distance = CivSettings.getInteger(CivSettings.civConfig, "civ.min_distance").intValue();
/*  508 */       ChunkCoord foundLocation = new ChunkCoord(loc);
/*      */       
/*  510 */       for (CultureChunk cc : CivGlobal.getCultureChunks()) {
/*  511 */         if (foundLocation.distance(cc.getChunkCoord()) <= min_distance) {
/*  512 */           throw new CivException("Too close to the culture of " + cc.getCiv().getName() + ", cannot found civilization here.");
/*      */         }
/*      */       }
/*      */     } catch (InvalidConfiguration e1) {
/*  516 */       e1.printStackTrace();
/*  517 */       throw new CivException("Internal configuration exception.");
/*      */     }
/*      */     try
/*      */     {
/*  521 */       Civilization civ = new Civilization(name, capitolName, resident);
/*      */       try {
/*  523 */         civ.saveNow();
/*      */       } catch (SQLException e) {
/*  525 */         CivLog.error("Caught exception:" + e.getMessage() + " error code:" + e.getErrorCode());
/*  526 */         if (e.getMessage().contains("Duplicate entry")) {
/*  527 */           SQL.deleteByName(name, TABLE_NAME);
/*  528 */           throw new CivException("We detected and internal inconsistency with the database. Try founding your civ again,if the problem persists, contact an admin.");
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  534 */       PermissionGroup leadersGroup = new PermissionGroup(civ, "leaders");
/*  535 */       leadersGroup.addMember(resident);
/*  536 */       leadersGroup.saveNow();
/*  537 */       civ.setLeaderGroup(leadersGroup);
/*      */       
/*  539 */       PermissionGroup adviserGroup = new PermissionGroup(civ, "advisers");
/*  540 */       adviserGroup.saveNow();
/*  541 */       civ.setAdviserGroup(adviserGroup);
/*      */       
/*      */       try
/*      */       {
/*  545 */         Town.newTown(resident, capitolName, civ, true, true, loc);
/*      */       } catch (CivException e) {
/*  547 */         e.printStackTrace();
/*  548 */         civ.delete();
/*  549 */         leadersGroup.delete();
/*  550 */         adviserGroup.delete();
/*  551 */         throw e;
/*      */       }
/*      */       
/*  554 */       CivGlobal.addCiv(civ);
/*  555 */       ItemStack newStack = new ItemStack(Material.AIR);
/*  556 */       player.setItemInHand(newStack);
/*  557 */       CivMessage.global("The Civilization of " + civ.getName() + " has been founded! " + civ.getCapitolName() + " is it's capitol!");
/*      */     }
/*      */     catch (InvalidNameException e) {
/*  560 */       throw new CivException("The name of " + name + " is invalid, please choose another.");
/*      */     } catch (SQLException e) {
/*  562 */       e.printStackTrace();
/*  563 */       throw new CivException("Internal SQL Error.");
/*      */     }
/*      */   }
/*      */   
/*      */   public String getCapitolName()
/*      */   {
/*  569 */     return this.capitolName;
/*      */   }
/*      */   
/*      */   public void setCapitolName(String capitolName) {
/*  573 */     this.capitolName = capitolName;
/*      */   }
/*      */   
/*      */   public void addGroup(PermissionGroup grp)
/*      */   {
/*  578 */     if (grp.getName().equalsIgnoreCase(this.leaderGroupName)) {
/*  579 */       setLeaderGroup(grp);
/*  580 */     } else if (grp.getName().equalsIgnoreCase(this.advisersGroupName)) {
/*  581 */       setAdviserGroup(grp);
/*      */     }
/*      */   }
/*      */   
/*      */   public PermissionGroup getLeaderGroup() {
/*  586 */     return this.leaderGroup;
/*      */   }
/*      */   
/*      */   public void setLeaderGroup(PermissionGroup leaderGroup) {
/*  590 */     this.leaderGroup = leaderGroup;
/*      */   }
/*      */   
/*      */   public PermissionGroup getAdviserGroup() {
/*  594 */     return this.adviserGroup;
/*      */   }
/*      */   
/*      */   public void setAdviserGroup(PermissionGroup adviserGroup) {
/*  598 */     this.adviserGroup = adviserGroup;
/*      */   }
/*      */   
/*      */   public Collection<Town> getTowns() {
/*  602 */     return this.towns.values();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getWarUpkeep()
/*      */   {
/*  632 */     double upkeep = 0.0D;
/*  633 */     boolean doublePenalty = false;
/*      */     
/*      */ 
/*  636 */     for (Relation relation : getDiplomacyManager().getRelations()) {
/*  637 */       if ((relation.getStatus() == Relation.Status.WAR) && 
/*  638 */         (relation.getAggressor() == this)) {
/*  639 */         double thisWarUpkeep = 0.0D;
/*  640 */         int ourScore = CivGlobal.getScoreForCiv(this).intValue();
/*  641 */         int theirScore = CivGlobal.getScoreForCiv(relation.getOtherCiv()).intValue();
/*  642 */         int scoreDiff = ourScore - theirScore;
/*      */         try {
/*  644 */           thisWarUpkeep += CivSettings.getDouble(CivSettings.warConfig, "war.upkeep_per_war");
/*      */         } catch (InvalidConfiguration e) {
/*  646 */           e.printStackTrace();
/*  647 */           return 0.0D;
/*      */         }
/*  649 */         if (scoreDiff > 0)
/*      */         {
/*      */           try {
/*  652 */             war_penalty = CivSettings.getDouble(CivSettings.warConfig, "war.upkeep_per_war_multiplier");
/*      */           } catch (InvalidConfiguration e) {
/*      */             double war_penalty;
/*  655 */             e.printStackTrace();
/*  656 */             return 0.0D; }
/*      */           double war_penalty;
/*  658 */           thisWarUpkeep += scoreDiff * war_penalty;
/*      */         }
/*      */         
/*      */ 
/*  662 */         ArrayList<Civilization> allies = new ArrayList();
/*  663 */         allies.add(relation.getOtherCiv());
/*  664 */         for (Relation relation2 : relation.getOtherCiv().getDiplomacyManager().getRelations()) {
/*  665 */           if (relation2.getStatus() == Relation.Status.ALLY) {
/*  666 */             allies.add(relation2.getOtherCiv());
/*      */           }
/*      */         }
/*      */         
/*  670 */         for (Civilization civ : allies) {
/*  671 */           for (Town t : civ.getTowns()) {
/*  672 */             if (t.getBuffManager().hasBuff("buff_notre_dame_extra_war_penalty")) {
/*  673 */               doublePenalty = true;
/*  674 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  679 */         if (doublePenalty) {
/*  680 */           thisWarUpkeep *= 2.0D;
/*      */         }
/*      */         
/*  683 */         upkeep += thisWarUpkeep;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  688 */     return upkeep;
/*      */   }
/*      */   
/*      */   public double getWarUnhappiness() {
/*  692 */     double happy = 0.0D;
/*      */     
/*      */ 
/*  695 */     for (Relation relation : getDiplomacyManager().getRelations()) {
/*  696 */       if ((relation.getStatus() == Relation.Status.WAR) && 
/*  697 */         (relation.getAggressor() == this)) {
/*  698 */         double thisWarUpkeep = 0.0D;
/*  699 */         int ourScore = CivGlobal.getScoreForCiv(this).intValue();
/*  700 */         int theirScore = CivGlobal.getScoreForCiv(relation.getOtherCiv()).intValue();
/*  701 */         int scoreDiff = ourScore - theirScore;
/*      */         try {
/*  703 */           thisWarUpkeep += CivSettings.getDouble(CivSettings.happinessConfig, "happiness.per_war");
/*      */         } catch (InvalidConfiguration e) {
/*  705 */           e.printStackTrace();
/*  706 */           return 0.0D;
/*      */         }
/*  708 */         if (scoreDiff > 0) {
/*      */           try
/*      */           {
/*  711 */             double war_penalty = CivSettings.getDouble(CivSettings.happinessConfig, "happiness.per_war_score");
/*  712 */             double addedFromPoints = scoreDiff * war_penalty;
/*  713 */             addedFromPoints = Math.min(CivSettings.getDouble(CivSettings.happinessConfig, "happiness.per_war_score_max"), addedFromPoints);
/*      */             
/*  715 */             thisWarUpkeep += addedFromPoints;
/*      */           } catch (InvalidConfiguration e) {
/*  717 */             e.printStackTrace();
/*  718 */             return 0.0D;
/*      */           }
/*      */         }
/*      */         
/*  722 */         happy += thisWarUpkeep;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  727 */     return happy;
/*      */   }
/*      */   
/*      */   public double getDistanceUpkeepAtLocation(Location capitolTownHallLoc, Location townHallLoc, boolean touching) throws InvalidConfiguration
/*      */   {
/*  732 */     double town_distance_base_upkeep = CivSettings.getDoubleCiv("civ.town_distance_base_upkeep");
/*  733 */     double distance_multiplier_touching = CivSettings.getDoubleCiv("civ.town_distance_multiplier");
/*  734 */     double distance_multiplier_not_touching = CivSettings.getDoubleCiv("civ.town_distance_multiplier_outside_culture");
/*  735 */     double maxDistanceUpkeep = CivSettings.getDoubleCiv("civ.town_distance_upkeep_max");
/*      */     
/*  737 */     double distance = capitolTownHallLoc.distance(townHallLoc);
/*  738 */     double distanceUpkeep = 0.0D;
/*  739 */     if (touching) {
/*  740 */       distanceUpkeep = town_distance_base_upkeep * Math.pow(distance, distance_multiplier_touching);
/*      */     } else {
/*  742 */       distanceUpkeep = town_distance_base_upkeep * Math.pow(distance, distance_multiplier_not_touching);
/*      */     }
/*      */     
/*  745 */     if (distanceUpkeep > maxDistanceUpkeep) {
/*  746 */       distanceUpkeep = maxDistanceUpkeep;
/*      */     }
/*      */     
/*  749 */     distanceUpkeep = Math.round(distanceUpkeep);
/*  750 */     return distanceUpkeep;
/*      */   }
/*      */   
/*      */   public double getDistanceHappiness(Location capitolTownHallLoc, Location townHallLoc, boolean touching) throws InvalidConfiguration {
/*  754 */     double town_distance_base_happy = CivSettings.getDouble(CivSettings.happinessConfig, "happiness.distance_base");
/*  755 */     double distance_multiplier_touching = CivSettings.getDouble(CivSettings.happinessConfig, "happiness.distance_multiplier");
/*  756 */     double distance_multiplier_not_touching = CivSettings.getDouble(CivSettings.happinessConfig, "happiness.distance_multiplier_outside_culture");
/*  757 */     double maxDistanceHappiness = CivSettings.getDouble(CivSettings.happinessConfig, "happiness.distance_max");
/*  758 */     double distance = capitolTownHallLoc.distance(townHallLoc);
/*  759 */     double distance_happy = 0.0D;
/*      */     
/*  761 */     if (touching) {
/*  762 */       distance_happy = town_distance_base_happy * Math.pow(distance, distance_multiplier_touching);
/*      */     } else {
/*  764 */       distance_happy = town_distance_base_happy * Math.pow(distance, distance_multiplier_not_touching);
/*      */     }
/*      */     
/*  767 */     if (distance_happy > maxDistanceHappiness) {
/*  768 */       distance_happy = maxDistanceHappiness;
/*      */     }
/*      */     
/*  771 */     distance_happy = Math.round(distance_happy);
/*  772 */     return distance_happy;
/*      */   }
/*      */   
/*      */   public Location getCapitolTownHallLocation() {
/*  776 */     Town capitol = getTown(this.capitolName);
/*  777 */     if (capitol == null) {
/*  778 */       return null;
/*      */     }
/*      */     
/*  781 */     for (Structure struct : capitol.getStructures()) {
/*  782 */       if ((struct instanceof Capitol)) {
/*  783 */         return struct.getCorner().getLocation();
/*      */       }
/*      */     }
/*      */     
/*  787 */     return null;
/*      */   }
/*      */   
/*      */   public Capitol getCapitolStructure() {
/*  791 */     Town capitol = getTown(this.capitolName);
/*  792 */     if (capitol == null) {
/*  793 */       return null;
/*      */     }
/*      */     
/*  796 */     for (Structure struct : capitol.getStructures()) {
/*  797 */       if ((struct instanceof Capitol)) {
/*  798 */         return (Capitol)struct;
/*      */       }
/*      */     }
/*      */     
/*  802 */     return null;
/*      */   }
/*      */   
/*      */   public double payUpkeep() throws InvalidConfiguration, CivException {
/*  806 */     double upkeep = 0.0D;
/*  807 */     this.lastUpkeepPaidMap.clear();
/*      */     
/*  809 */     Town capitol = getTown(this.capitolName);
/*  810 */     if (capitol == null) {
/*  811 */       throw new CivException("Civilization found with no capitol!");
/*      */     }
/*      */     
/*  814 */     for (Town t : getTowns())
/*      */     {
/*      */ 
/*  817 */       if (!getCapitolName().equals(t.getName())) {
/*      */         try
/*      */         {
/*  820 */           upkeep += CivSettings.getDoubleCiv("civ.town_upkeep");
/*  821 */           this.lastUpkeepPaidMap.put(t.getName() + ",base", Double.valueOf(upkeep));
/*      */         }
/*      */         catch (InvalidConfiguration e) {
/*  824 */           e.printStackTrace();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  829 */     upkeep += getWarUpkeep();
/*      */     
/*  831 */     if (getTreasury().hasEnough(upkeep))
/*      */     {
/*  833 */       getTreasury().withdraw(upkeep);
/*      */     }
/*      */     else {
/*  836 */       double diff = upkeep - getTreasury().getBalance();
/*  837 */       getTreasury().setDebt(getTreasury().getDebt() + diff);
/*  838 */       getTreasury().withdraw(getTreasury().getBalance());
/*      */     }
/*      */     
/*  841 */     return upkeep;
/*      */   }
/*      */   
/*      */   public int getDaysInDebt()
/*      */   {
/*  846 */     return this.daysInDebt;
/*      */   }
/*      */   
/*      */   public void setDaysInDebt(int daysInDebt) {
/*  850 */     this.daysInDebt = daysInDebt;
/*      */   }
/*      */   
/*      */   public void warnDebt() {
/*  854 */     CivMessage.global(getName() + " is in " + getTreasury().getDebt() + " coins of debt!");
/*      */   }
/*      */   
/*      */   public void incrementDaysInDebt()
/*      */   {
/*  859 */     this.daysInDebt += 1;
/*      */     
/*  861 */     if ((this.daysInDebt >= 7) && 
/*  862 */       (this.daysInDebt >= 14) && 
/*  863 */       (this.daysInDebt >= 21)) {
/*  864 */       CivMessage.global(getName() + " and its towns have fell into ruin!");
/*      */       try {
/*  866 */         delete();
/*  867 */         return;
/*      */       } catch (SQLException e) {
/*  869 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  876 */     CivMessage.global(getName() + " is in debt! " + getDaysLeftWarning());
/*  877 */     save();
/*      */   }
/*      */   
/*      */ 
/*      */   public String getDaysLeftWarning()
/*      */   {
/*  883 */     if (this.daysInDebt < 7) {
/*  884 */       return 7 - this.daysInDebt + " days until civ goes up for sale.";
/*      */     }
/*      */     
/*  887 */     if (this.daysInDebt < 14) {
/*  888 */       return getName() + " is up for sale, " + (14 - this.daysInDebt) + " days until it's towns go up for sale.";
/*      */     }
/*      */     
/*      */ 
/*  892 */     if (this.daysInDebt < 21) {
/*  893 */       return getName() + " is up for sale, " + (21 - this.daysInDebt) + " days until the civ is deleted.";
/*      */     }
/*      */     
/*  896 */     return "";
/*      */   }
/*      */   
/*      */   private int pickCivColor() {
/*  900 */     int max_retries = 10;
/*  901 */     Random rand = new Random();
/*  902 */     boolean found = false;
/*  903 */     int c = 0;
/*      */     
/*  905 */     for (int i = 0; i < max_retries; i++) {
/*  906 */       c = rand.nextInt(16777215);
/*  907 */       if (testColorForCloseness(c))
/*      */       {
/*      */ 
/*  910 */         found = true;
/*  911 */         break;
/*      */       }
/*      */     }
/*      */     
/*  915 */     if (!found) {
/*  916 */       c = rand.nextInt();
/*  917 */       System.out.println("WARNING: color exhaustion? couldn't find a free color within tolerance");
/*      */     }
/*      */     
/*  920 */     return c;
/*      */   }
/*      */   
/*      */   private boolean testColorForCloseness(int c) {
/*  924 */     int tolerance = 40;
/*      */     
/*  926 */     if (simpleColorDistance(c, 16711680) < tolerance) {
/*  927 */       return false;
/*      */     }
/*      */     
/*  930 */     if (simpleColorDistance(c, 16777215) < tolerance) {
/*  931 */       return false;
/*      */     }
/*      */     
/*  934 */     if (simpleColorDistance(c, 0) < tolerance) {
/*  935 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  939 */     for (Iterator localIterator = CivGlobal.CivColorInUse.keySet().iterator(); localIterator.hasNext();) { int c2 = ((Integer)localIterator.next()).intValue();
/*  940 */       if (simpleColorDistance(c, c2) < tolerance) {
/*  941 */         return false;
/*      */       }
/*      */     }
/*  944 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   private int simpleColorDistance(int color1, int color2)
/*      */   {
/*  950 */     int red1 = color1 & 0xFF0000;
/*  951 */     int red2 = color2 & 0xFF0000;
/*  952 */     int green1 = color1 & 0xFF00;
/*  953 */     int green2 = color2 & 0xFF00;
/*  954 */     int blue1 = color1 & 0xFF;
/*  955 */     int blue2 = color2 & 0xFF;
/*      */     
/*  957 */     double redPower = Math.pow(red1 - red2, 2.0D);
/*  958 */     double greenPower = Math.pow(green1 - green2, 2.0D);
/*  959 */     double bluePower = Math.pow(blue1 - blue2, 2.0D);
/*      */     
/*  961 */     return (int)Math.sqrt(redPower + greenPower + bluePower);
/*      */   }
/*      */   
/*      */   public String getCultureDescriptionString() {
/*  965 */     String out = "";
/*      */     
/*  967 */     out = out + "<b>" + getName() + "</b>";
/*      */     
/*  969 */     return out;
/*      */   }
/*      */   
/*      */   public double getBaseBeakers() {
/*  973 */     return this.baseBeakers;
/*      */   }
/*      */   
/*      */   public double getBeakers() {
/*  977 */     double total = 0.0D;
/*      */     
/*  979 */     for (Town town : getTowns()) {
/*  980 */       total += town.getBeakers().total;
/*      */     }
/*      */     
/*  983 */     total += this.baseBeakers;
/*      */     
/*  985 */     return total;
/*      */   }
/*      */   
/*      */   public void setBaseBeakers(double beakerRate) {
/*  989 */     this.baseBeakers = beakerRate;
/*      */   }
/*      */   
/*      */   public void addBeakers(double beakers)
/*      */   {
/*  994 */     if (beakers == 0.0D) {
/*  995 */       return;
/*      */     }
/*      */     
/*  998 */     TaskMaster.asyncTask(new UpdateTechBar(this), 0L);
/*  999 */     setResearchProgress(getResearchProgress() + beakers);
/*      */     
/* 1001 */     if (getResearchProgress() >= getResearchTech().beaker_cost) {
/* 1002 */       CivMessage.sendCiv(this, "Our civilization has discovered " + getResearchTech().name + "!");
/* 1003 */       addTech(getResearchTech());
/* 1004 */       setResearchProgress(0.0D);
/* 1005 */       setResearchTech(null);
/*      */       
/* 1007 */       save();
/*      */       
/* 1009 */       return;
/*      */     }
/*      */     
/* 1012 */     int percentageComplete = (int)(getResearchProgress() / getResearchTech().beaker_cost * 100.0D);
/* 1013 */     if (percentageComplete % 10 == 0)
/*      */     {
/* 1015 */       if (percentageComplete != this.lastTechPercentage) {
/* 1016 */         CivMessage.sendCiv(this, "Our civilizations research progress on " + getResearchTech().name + " is now " + percentageComplete + "% completed!");
/* 1017 */         this.lastTechPercentage = percentageComplete;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1022 */     save();
/*      */   }
/*      */   
/*      */   public void startTechnologyResearch(ConfigTech tech) throws CivException
/*      */   {
/* 1027 */     if (getResearchTech() != null) {
/* 1028 */       throw new CivException("Current researching " + getResearchTech().name + ". " + 
/* 1029 */         "If you want to change your focus, use /civ research switch instead.");
/*      */     }
/*      */     
/* 1032 */     if (!getTreasury().hasEnough(tech.cost)) {
/* 1033 */       throw new CivException("Our Civilization's treasury does have the required " + tech.cost + " coins to start this research.");
/*      */     }
/*      */     
/* 1036 */     if (hasTech(tech.id)) {
/* 1037 */       throw new CivException("You already have this technology.");
/*      */     }
/*      */     
/* 1040 */     if (!tech.isAvailable(this)) {
/* 1041 */       throw new CivException("You do not have the required technology to research this technology.");
/*      */     }
/*      */     
/* 1044 */     setResearchTech(tech);
/* 1045 */     setResearchProgress(0.0D);
/*      */     
/* 1047 */     getTreasury().withdraw(tech.cost);
/* 1048 */     TaskMaster.asyncTask(new UpdateTechBar(this), 0L);
/*      */   }
/*      */   
/*      */   public ConfigTech getResearchTech() {
/* 1052 */     return this.researchTech;
/*      */   }
/*      */   
/*      */   public void setResearchTech(ConfigTech researchTech) {
/* 1056 */     this.researchTech = researchTech;
/*      */   }
/*      */   
/*      */   public double getResearchProgress() {
/* 1060 */     return this.researchProgress;
/*      */   }
/*      */   
/*      */   public void setResearchProgress(double researchProgress) {
/* 1064 */     this.researchProgress = researchProgress;
/*      */   }
/*      */   
/*      */   public void changeGovernment(Civilization civ, ConfigGovernment gov, boolean force) throws CivException {
/* 1068 */     changeGovernment(civ, gov, force, 24);
/*      */   }
/*      */   
/*      */   public void changeGovernment(Civilization civ, ConfigGovernment gov, boolean force, int hours) throws CivException
/*      */   {
/* 1073 */     if ((civ.getGovernment() == gov) && (!force)) {
/* 1074 */       throw new CivException("You are already a " + gov.displayName);
/*      */     }
/*      */     
/* 1077 */     if (civ.getGovernment().id.equals("gov_anarchy")) {
/* 1078 */       throw new CivException("You are already in anarchy, you cannot switch governments.");
/*      */     }
/*      */     
/* 1081 */     boolean noanarchy = false;
/* 1082 */     for (Town t : getTowns()) {
/* 1083 */       if (t.getBuffManager().hasBuff("buff_noanarchy")) {
/* 1084 */         noanarchy = true;
/* 1085 */         break;
/*      */       }
/*      */     }
/*      */     
/* 1089 */     if (!noanarchy) {
/* 1090 */       String key = "changegov_" + getId();
/* 1091 */       String value = gov.id;
/*      */       
/* 1093 */       sessionAdd(key, value);
/*      */       
/*      */ 
/* 1096 */       civ.setGovernment("gov_anarchy");
/* 1097 */       CivMessage.global(getName() + " has fallen into anarchy!");
/*      */     } else {
/* 1099 */       civ.setGovernment(gov.id);
/* 1100 */       CivMessage.global(civ.getName() + " has emerged from anarchy and has adopted " + ((ConfigGovernment)CivSettings.governments.get(gov.id)).displayName);
/*      */     }
/*      */     
/*      */ 
/* 1104 */     civ.save();
/*      */   }
/*      */   
/*      */   public String getUpkeepPaid(Town town, String type)
/*      */   {
/* 1109 */     String out = "";
/*      */     
/* 1111 */     if (this.lastUpkeepPaidMap.containsKey(town.getName() + "," + type)) {
/* 1112 */       out = out + this.lastUpkeepPaidMap.get(new StringBuilder(String.valueOf(town.getName())).append(",").append(type).toString());
/*      */     } else {
/* 1114 */       out = out + "0";
/*      */     }
/*      */     
/* 1117 */     return out;
/*      */   }
/*      */   
/*      */   public void taxPayment(Town town, double amount)
/*      */   {
/* 1122 */     Double townPaid = (Double)this.lastTaxesPaidMap.get(town.getName());
/* 1123 */     if (townPaid == null) {
/* 1124 */       townPaid = Double.valueOf(amount);
/*      */     } else {
/* 1126 */       townPaid = Double.valueOf(townPaid.doubleValue() + amount);
/*      */     }
/* 1128 */     this.lastTaxesPaidMap.put(town.getName(), townPaid);
/* 1129 */     double beakerAmount = amount * this.sciencePercentage;
/* 1130 */     amount -= beakerAmount;
/* 1131 */     getTreasury().deposit(amount);
/* 1132 */     save();
/*      */     
/*      */     try
/*      */     {
/* 1136 */       double coins_per_beaker = CivSettings.getDouble(CivSettings.civConfig, "civ.coins_per_beaker");
/*      */       
/* 1138 */       for (Town t : getTowns()) {
/* 1139 */         if (t.getBuffManager().hasBuff("buff_greatlibrary_double_tax_beakers")) {
/* 1140 */           coins_per_beaker /= 2.0D;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (InvalidConfiguration e) {
/* 1145 */       e.printStackTrace(); return;
/*      */     }
/*      */     
/*      */     double coins_per_beaker;
/* 1149 */     DecimalFormat df = new DecimalFormat("#.#");
/* 1150 */     double totalBeakers = Double.valueOf(df.format(beakerAmount / coins_per_beaker)).doubleValue();
/* 1151 */     if (totalBeakers == 0.0D) {
/* 1152 */       return;
/*      */     }
/*      */     
/* 1155 */     if (this.researchTech != null) {
/* 1156 */       addBeakers(totalBeakers);
/*      */     } else {
/* 1158 */       EndGameCondition scienceVictory = EndGameCondition.getEndCondition("end_science");
/* 1159 */       if (scienceVictory == null) {
/* 1160 */         CivLog.error("Couldn't find science victory, not configured?");
/*      */       }
/* 1162 */       else if (scienceVictory.isActive(this))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1167 */         double beakerTotal = totalBeakers;
/* 1168 */         ((EndConditionScience)scienceVictory).addExtraBeakersToCiv(this, beakerTotal);
/* 1169 */         return;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public double getSciencePercentage()
/*      */   {
/* 1176 */     return this.sciencePercentage;
/*      */   }
/*      */   
/*      */   public void setSciencePercentage(double sciencePercentage) {
/* 1180 */     if (sciencePercentage > 1.0D) {
/* 1181 */       sciencePercentage = 1.0D;
/*      */     }
/*      */     
/* 1184 */     this.sciencePercentage = sciencePercentage;
/*      */   }
/*      */   
/*      */   public Collection<ConfigTech> getTechs() {
/* 1188 */     return this.techs.values();
/*      */   }
/*      */   
/*      */   public void depositFromResident(Resident resident, Double amount) throws CivException, SQLException
/*      */   {
/* 1193 */     if (!resident.getTreasury().hasEnough(amount.doubleValue())) {
/* 1194 */       throw new CivException("You do not have enough.");
/*      */     }
/*      */     
/* 1197 */     if (getTreasury().inDebt()) {
/* 1198 */       if (getTreasury().getDebt() >= amount.doubleValue()) {
/* 1199 */         getTreasury().setDebt(getTreasury().getDebt() - amount.doubleValue());
/* 1200 */         resident.getTreasury().withdraw(amount.doubleValue());
/* 1201 */       } else if (getTreasury().getDebt() < amount.doubleValue()) {
/* 1202 */         double leftAmount = amount.doubleValue() - getTreasury().getDebt();
/* 1203 */         getTreasury().setDebt(0.0D);
/* 1204 */         getTreasury().deposit(leftAmount);
/* 1205 */         resident.getTreasury().withdraw(amount.doubleValue());
/*      */       }
/*      */       
/* 1208 */       if (!getTreasury().inDebt()) {
/* 1209 */         this.daysInDebt = 0;
/* 1210 */         CivMessage.global(getName() + " is no longer in debt.");
/*      */       }
/*      */     } else {
/* 1213 */       getTreasury().deposit(amount.doubleValue());
/* 1214 */       resident.getTreasury().withdraw(amount.doubleValue());
/*      */     }
/* 1216 */     save();
/*      */   }
/*      */   
/*      */   public void sessionAdd(String key, String value) {
/* 1220 */     CivGlobal.getSessionDB().add(key, value, getId(), 0, 0);
/*      */   }
/*      */   
/*      */   public void sessionDeleteAll(String key) {
/* 1224 */     CivGlobal.getSessionDB().delete_all(key);
/*      */   }
/*      */   
/*      */ 
/*      */   public void sessionUpdateInsert(String key, String value) {}
/*      */   
/*      */   public DiplomacyManager getDiplomacyManager()
/*      */   {
/* 1232 */     return this.diplomacyManager;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onDefeat(Civilization attackingCiv)
/*      */   {
/* 1245 */     for (Town town : getTowns()) {
/* 1246 */       town.onDefeat(attackingCiv);
/*      */     }
/*      */     
/*      */ 
/* 1250 */     LinkedList<Relation> deletedRelations = new LinkedList();
/* 1251 */     for (Relation relation : getDiplomacyManager().getRelations()) {
/* 1252 */       deletedRelations.add(relation);
/*      */     }
/* 1254 */     for (Relation relation : deletedRelations) {
/*      */       try {
/* 1256 */         relation.delete();
/*      */       } catch (SQLException e) {
/* 1258 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1264 */     CivGlobal.removeCiv(this);
/* 1265 */     CivGlobal.addConqueredCiv(this);
/* 1266 */     this.conquered = true;
/* 1267 */     this.conquer_date = new Date();
/* 1268 */     save();
/*      */   }
/*      */   
/*      */   public boolean isConquered() {
/* 1272 */     return this.conquered;
/*      */   }
/*      */   
/*      */   public void regenControlBlocks()
/*      */   {
/* 1277 */     for (Town t : getTowns()) {
/* 1278 */       t.getTownHall().regenControlBlocks();
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isAdminCiv() {
/* 1283 */     return this.adminCiv;
/*      */   }
/*      */   
/*      */   public void setAdminCiv(boolean bool) {
/* 1287 */     this.adminCiv = bool;
/* 1288 */     if (this.adminCiv) {
/* 1289 */       CivGlobal.addAdminCiv(this);
/*      */     } else {
/* 1291 */       CivGlobal.removeAdminCiv(this);
/*      */     }
/* 1293 */     save();
/*      */   }
/*      */   
/*      */   public void repositionPlayers(String reason) {
/* 1297 */     if (!getDiplomacyManager().isAtWar()) {
/* 1298 */       return;
/*      */     }
/*      */     
/* 1301 */     for (Town t : getTowns()) {
/* 1302 */       TownHall townhall = t.getTownHall();
/* 1303 */       if (townhall == null) {
/* 1304 */         CivLog.error("Town hall was null for " + t.getName() + " when trying to reposition players.");
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*      */ 
/* 1313 */         for (Resident resident : t.getResidents())
/*      */         {
/* 1315 */           BlockCoord revive = townhall.getRandomRevivePoint();
/*      */           try
/*      */           {
/* 1318 */             Player player = CivGlobal.getPlayer(resident);
/* 1319 */             ChunkCoord coord = new ChunkCoord(player.getLocation());
/* 1320 */             CultureChunk cc = CivGlobal.getCultureChunk(coord);
/* 1321 */             if ((cc != null) && (cc.getCiv() != this) && 
/* 1322 */               (cc.getCiv().getDiplomacyManager().atWarWith(this))) {
/* 1323 */               CivMessage.send(player, "§5" + reason);
/* 1324 */               player.teleport(revive.getLocation());
/*      */             }
/*      */           }
/*      */           catch (CivException localCivException) {}
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isTownsForSale()
/*      */   {
/* 1352 */     if (this.daysInDebt >= 14) {
/* 1353 */       return true;
/*      */     }
/* 1355 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isForSale() {
/* 1359 */     if (getTownCount() == 0) {
/* 1360 */       return false;
/*      */     }
/*      */     
/* 1363 */     if (this.daysInDebt >= 7) {
/* 1364 */       return true;
/*      */     }
/* 1366 */     return false;
/*      */   }
/*      */   
/*      */   public double getForSalePriceFromCivOnly() {
/* 1370 */     int effectivePoints = 0;
/* 1371 */     effectivePoints = getTechScore();
/*      */     try
/*      */     {
/* 1374 */       coins_per_point = CivSettings.getDouble(CivSettings.scoreConfig, "coins_per_point");
/*      */     } catch (InvalidConfiguration e) { double coins_per_point;
/* 1376 */       e.printStackTrace();
/* 1377 */       return 0.0D; }
/*      */     double coins_per_point;
/* 1379 */     return coins_per_point * effectivePoints;
/*      */   }
/*      */   
/*      */   public double getTotalSalePrice() {
/* 1383 */     double price = getForSalePriceFromCivOnly();
/* 1384 */     for (Town town : getTowns()) {
/* 1385 */       price += town.getForSalePrice();
/*      */     }
/* 1387 */     return price;
/*      */   }
/*      */   
/*      */   public void buyCiv(Civilization civ) throws CivException
/*      */   {
/* 1392 */     if (!getTreasury().hasEnough(civ.getTotalSalePrice())) {
/* 1393 */       throw new CivException("Your civilization treasury does not have enough money.");
/*      */     }
/*      */     
/* 1396 */     getTreasury().withdraw(civ.getTotalSalePrice());
/* 1397 */     mergeInCiv(civ);
/*      */   }
/*      */   
/*      */   public int getTechScore() {
/* 1401 */     int points = 0;
/*      */     
/* 1403 */     for (ConfigTech t : getTechs()) {
/* 1404 */       points += t.points.intValue();
/*      */     }
/* 1406 */     return points;
/*      */   }
/*      */   
/*      */   public int getScore() {
/* 1410 */     int points = 0;
/* 1411 */     for (Town t : getTowns()) {
/* 1412 */       points += t.getScore();
/*      */     }
/*      */     
/* 1415 */     points += getTechScore();
/*      */     
/* 1417 */     return points;
/*      */   }
/*      */   
/*      */   public boolean hasResident(Resident resident) {
/* 1421 */     if (resident == null) {
/* 1422 */       return false;
/*      */     }
/*      */     
/* 1425 */     for (Town t : getTowns()) {
/* 1426 */       if (t.hasResident(resident)) {
/* 1427 */         return true;
/*      */       }
/*      */     }
/*      */     
/* 1431 */     return false;
/*      */   }
/*      */   
/*      */   public void removeTown(Town town) {
/* 1435 */     this.towns.remove(town.getName().toLowerCase());
/*      */   }
/*      */   
/*      */   public void mergeInCiv(Civilization oldciv) {
/* 1439 */     if (oldciv == this) {
/* 1440 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1444 */     for (Town town : oldciv.getTowns()) {
/* 1445 */       town.changeCiv(this);
/* 1446 */       town.setDebt(0.0D);
/* 1447 */       town.setDaysInDebt(0);
/* 1448 */       town.save();
/*      */     }
/*      */     
/* 1451 */     if (oldciv.towns.size() > 0) {
/* 1452 */       CivLog.error("CIV SOMEHOW STILL HAS TOWNS AFTER WE GAVE THEM ALL AWAY WTFWTFWTFWTF.");
/* 1453 */       this.towns.clear();
/*      */     }
/*      */     try
/*      */     {
/* 1457 */       oldciv.delete();
/*      */     } catch (SQLException e) {
/* 1459 */       e.printStackTrace();
/*      */     }
/*      */     
/* 1462 */     CivGlobal.processCulture();
/*      */   }
/*      */   
/*      */   public void buyTown(Town town) throws CivException
/*      */   {
/* 1467 */     if (!getTreasury().hasEnough(town.getForSalePrice())) {
/* 1468 */       throw new CivException("Your civilization treasury does not have enough money.");
/*      */     }
/*      */     
/* 1471 */     getTreasury().withdraw(town.getForSalePrice());
/* 1472 */     town.changeCiv(this);
/* 1473 */     town.setMotherCiv(null);
/* 1474 */     town.setDebt(0.0D);
/* 1475 */     town.setDaysInDebt(0);
/* 1476 */     town.save();
/* 1477 */     CivGlobal.processCulture();
/* 1478 */     CivMessage.global("The town of " + getName() + " has been bought by " + getName());
/*      */   }
/*      */   
/*      */   public double getRevolutionFee()
/*      */   {
/*      */     try
/*      */     {
/* 1485 */       double base_coins = CivSettings.getDouble(CivSettings.warConfig, "revolution.base_cost");
/* 1486 */       double coins_per_town = CivSettings.getDouble(CivSettings.warConfig, "revolution.coins_per_town");
/* 1487 */       double coins_per_point = CivSettings.getDouble(CivSettings.warConfig, "revolution.coins_per_point");
/* 1488 */       double max_fee = CivSettings.getDouble(CivSettings.warConfig, "revolution.maximum_fee");
/*      */       
/* 1490 */       double total_coins = base_coins;
/*      */       
/* 1492 */       double motherCivPoints = getTechScore();
/* 1493 */       for (Town t : CivGlobal.getTowns()) {
/* 1494 */         if (t.getMotherCiv() == this) {
/* 1495 */           motherCivPoints += t.getScore();
/* 1496 */           total_coins += coins_per_town;
/*      */         }
/*      */       }
/*      */       
/* 1500 */       total_coins += motherCivPoints * coins_per_point;
/*      */       
/* 1502 */       if (total_coins > max_fee) {}
/* 1503 */       return max_fee;
/*      */ 
/*      */     }
/*      */     catch (InvalidConfiguration e)
/*      */     {
/*      */ 
/* 1509 */       e.printStackTrace(); }
/* 1510 */     return Double.MAX_VALUE;
/*      */   }
/*      */   
/*      */   public void setConquered(boolean b)
/*      */   {
/* 1515 */     this.conquered = b;
/*      */   }
/*      */   
/*      */   public Collection<Resident> getOnlineResidents()
/*      */   {
/* 1520 */     LinkedList<Resident> residents = new LinkedList();
/* 1521 */     for (Town t : getTowns()) {
/* 1522 */       residents.addAll(t.getOnlineResidents());
/*      */     }
/*      */     
/* 1525 */     return residents;
/*      */   }
/*      */   
/*      */   public Date getConqueredDate() {
/* 1529 */     return this.conquer_date;
/*      */   }
/*      */   
/*      */   public void capitulate() {
/* 1533 */     for (Town t : ) {
/* 1534 */       if (t.getMotherCiv() == this) {
/* 1535 */         t.setMotherCiv(null);
/* 1536 */         t.save();
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/* 1541 */       delete();
/*      */     } catch (SQLException e) {
/* 1543 */       e.printStackTrace();
/*      */     }
/*      */     
/* 1546 */     CivMessage.global("The Civilization of " + getName() + " has capitualted all of its old towns can no longer revolt.");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public double getCivWideUnhappiness(HashMap<String, Double> sources)
/*      */   {
/* 1553 */     double total = 0.0D;
/*      */     
/*      */     try
/*      */     {
/* 1557 */       double per_town = CivSettings.getDouble(CivSettings.happinessConfig, "happiness.per_town");
/* 1558 */       double per_captured_town = CivSettings.getDouble(CivSettings.happinessConfig, "happiness.per_captured_town");
/*      */       
/* 1560 */       double happy_town = 0.0D;
/* 1561 */       double happy_captured_town = 0.0D;
/* 1562 */       for (Town town : getTowns()) {
/* 1563 */         if (town.getMotherCiv() == null) {
/* 1564 */           if (!town.isCapitol()) {
/* 1565 */             happy_town += per_town;
/*      */           }
/*      */         } else {
/* 1568 */           happy_captured_town += per_captured_town;
/*      */         }
/*      */       }
/*      */       
/* 1572 */       total += happy_town;
/* 1573 */       sources.put("Towns", Double.valueOf(happy_town));
/*      */       
/* 1575 */       total += happy_captured_town;
/* 1576 */       sources.put("Captured Towns", Double.valueOf(happy_captured_town));
/*      */       
/*      */ 
/* 1579 */       double war_happy = getWarUnhappiness();
/* 1580 */       total += war_happy;
/* 1581 */       sources.put("War", Double.valueOf(war_happy));
/*      */     }
/*      */     catch (InvalidConfiguration e) {
/* 1584 */       e.printStackTrace();
/*      */     }
/*      */     
/* 1587 */     return total;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public double getDistanceHappiness(Town town)
/*      */   {
/* 1594 */     Structure capitolTownHall = getCapitolStructure();
/* 1595 */     Structure townHall = town.getTownHall();
/* 1596 */     if ((capitolTownHall != null) && (townHall != null)) {
/* 1597 */       Location loc_cap = capitolTownHall.getCorner().getLocation();
/* 1598 */       Location loc_town = townHall.getCorner().getLocation();
/*      */       double distanceHappy;
/* 1600 */       if ((town.getMotherCiv() == null) || (town.getMotherCiv() == this)) {
/*      */         try {
/* 1602 */           distanceHappy = getDistanceHappiness(loc_cap, loc_town, town.touchesCapitolCulture(new HashSet()));
/*      */         } catch (InvalidConfiguration e) { double distanceHappy;
/* 1604 */           e.printStackTrace();
/* 1605 */           return 0.0D;
/*      */         }
/*      */       } else {
/* 1608 */         distanceHappy = 0.0D;
/*      */       }
/* 1610 */       return distanceHappy;
/*      */     }
/* 1612 */     return 0.0D;
/*      */   }
/*      */   
/*      */   public void declareAsWinner(EndGameCondition end) {
/* 1616 */     String out = "The Civilization of " + getName() + " has acheived a " + end.getVictoryName() + " victory!";
/* 1617 */     CivGlobal.getSessionDB().add("endgame:winningCiv", out, 0, 0, 0);
/* 1618 */     CivMessage.global(out);
/*      */   }
/*      */   
/*      */   public void winConditionWarning(EndGameCondition end, int daysLeft) {
/* 1622 */     CivMessage.global("The Civilization of " + getName() + " is close to achieving a " + end.getVictoryName() + " victory! Only " + daysLeft + " days left.");
/*      */   }
/*      */   
/*      */   public double getPercentageConquered()
/*      */   {
/* 1627 */     int totalCivs = CivGlobal.getCivs().size() + CivGlobal.getConqueredCivs().size();
/* 1628 */     int conqueredCivs = 1;
/*      */     
/* 1630 */     for (Civilization civ : CivGlobal.getConqueredCivs()) {
/* 1631 */       Town capital = CivGlobal.getTown(civ.getCapitolName());
/* 1632 */       if (capital == null)
/*      */       {
/* 1634 */         totalCivs--;
/*      */ 
/*      */ 
/*      */       }
/* 1638 */       else if (capital.getCiv() == this) {
/* 1639 */         conqueredCivs++;
/*      */       }
/*      */     }
/*      */     
/* 1643 */     double percent = conqueredCivs / totalCivs;
/* 1644 */     return percent;
/*      */   }
/*      */   
/*      */   public void processUnusedBeakers()
/*      */   {
/* 1649 */     EndGameCondition scienceVictory = EndGameCondition.getEndCondition("end_science");
/* 1650 */     if (scienceVictory == null) {
/* 1651 */       CivLog.error("Couldn't find science victory, not configured?");
/*      */     }
/* 1653 */     else if (scienceVictory.isActive(this))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1658 */       double beakerTotal = getBeakers() / 60.0D;
/* 1659 */       ((EndConditionScience)scienceVictory).addExtraBeakersToCiv(this, beakerTotal);
/* 1660 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1664 */     for (Town town : this.towns.values()) {
/* 1665 */       town.addUnusedBeakers(town.getBeakers().total / 60.0D);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean areLeadersInactive() {
/*      */     try {
/* 1671 */       int leader_inactive_days = CivSettings.getInteger(CivSettings.civConfig, "civ.leader_inactive_days").intValue();
/*      */       
/* 1673 */       for (Resident resident : getLeaderGroup().getMemberList()) {
/* 1674 */         if (!resident.isInactiveForDays(leader_inactive_days)) {
/* 1675 */           return false;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (InvalidConfiguration e) {
/* 1680 */       e.printStackTrace();
/* 1681 */       return false;
/*      */     }
/*      */     
/* 1684 */     return true;
/*      */   }
/*      */   
/*      */   public void rename(String name) throws CivException, InvalidNameException
/*      */   {
/* 1689 */     Civilization other = CivGlobal.getCiv(name);
/* 1690 */     if (other != null) {
/* 1691 */       throw new CivException("Already another civ with this name");
/*      */     }
/*      */     
/* 1694 */     other = CivGlobal.getConqueredCiv(name);
/* 1695 */     if (other != null) {
/* 1696 */       throw new CivException("Already another civ with this name");
/*      */     }
/*      */     
/* 1699 */     if (this.conquered) {
/* 1700 */       CivGlobal.removeConqueredCiv(this);
/*      */     } else {
/* 1702 */       CivGlobal.removeCiv(this);
/*      */     }
/*      */     
/* 1705 */     String oldName = getName();
/* 1706 */     setName(name);
/* 1707 */     save();
/*      */     
/* 1709 */     if (this.conquered) {
/* 1710 */       CivGlobal.addConqueredCiv(this);
/*      */     } else {
/* 1712 */       CivGlobal.addCiv(this);
/*      */     }
/*      */     
/* 1715 */     CivMessage.global("The civilization " + oldName + " is now called " + getName());
/*      */   }
/*      */   
/*      */   public ArrayList<RespawnLocationHolder> getAvailableRespawnables() {
/* 1719 */     ArrayList<RespawnLocationHolder> respawns = new ArrayList();
/*      */     
/* 1721 */     for (Town town : getTowns()) {
/* 1722 */       TownHall townhall = town.getTownHall();
/* 1723 */       if ((townhall != null) && (townhall.isActive()) && (
/* 1724 */         (townhall.getTown().isCapitol()) || (!town.defeated)))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1729 */         respawns.add(townhall);
/*      */       }
/*      */     }
/*      */     
/* 1733 */     for (WarCamp camp : this.warCamps) {
/* 1734 */       respawns.add(camp);
/*      */     }
/*      */     
/* 1737 */     return respawns;
/*      */   }
/*      */   
/*      */   public void addWarCamp(WarCamp camp)
/*      */   {
/* 1742 */     this.warCamps.add(camp);
/*      */   }
/*      */   
/*      */   public LinkedList<WarCamp> getWarCamps() {
/* 1746 */     return this.warCamps;
/*      */   }
/*      */   
/*      */   public void onWarEnd() {
/* 1750 */     for (WarCamp camp : this.warCamps) {
/* 1751 */       camp.onWarEnd();
/*      */     }
/*      */     
/* 1754 */     for (Town town : this.towns.values()) {
/* 1755 */       TownHall th = town.getTownHall();
/* 1756 */       if (th != null) {
/* 1757 */         th.setHitpoints(th.getMaxHitPoints());
/* 1758 */         th.save();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public Date getCreated() {
/* 1764 */     return this.created_date;
/*      */   }
/*      */   
/*      */   public void setCreated(Date created_date) {
/* 1768 */     this.created_date = created_date;
/*      */   }
/*      */   
/*      */   public void validateGift() throws CivException {
/*      */     try {
/* 1773 */       int min_gift_age = CivSettings.getInteger(CivSettings.civConfig, "civ.min_gift_age").intValue();
/*      */       
/* 1775 */       if (!DateUtil.isAfterDays(this.created_date, min_gift_age)) {
/* 1776 */         throw new CivException(getName() + " cannot participate in gifting/merging civs or towns until it is " + min_gift_age + " days old.");
/*      */       }
/*      */     } catch (InvalidConfiguration e) {
/* 1779 */       throw new CivException("Configuration error.");
/*      */     }
/*      */   }
/*      */   
/*      */   public void clearAggressiveWars()
/*      */   {
/* 1785 */     LinkedList<Relation> removeUs = new LinkedList();
/* 1786 */     for (Relation relation : getDiplomacyManager().getRelations()) {
/* 1787 */       if ((relation.getStatus().equals(Relation.Status.WAR)) && 
/* 1788 */         (relation.getAggressor() == this)) {
/* 1789 */         removeUs.add(relation);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1794 */     for (Relation relation : removeUs) {
/* 1795 */       getDiplomacyManager().deleteRelation(relation);
/* 1796 */       CivMessage.global(getName() + " was in debt too long and can no longer maintain it's aggressive war with " + relation.getOtherCiv().getName() + ".");
/*      */     }
/*      */   }
/*      */   
/*      */   public int getMergeCost()
/*      */   {
/* 1802 */     int total = 0;
/* 1803 */     for (Town town : this.towns.values()) {
/* 1804 */       total += town.getGiftCost();
/*      */     }
/*      */     
/* 1807 */     return total;
/*      */   }
/*      */   
/*      */   public Structure getNearestStructureInTowns(Location loc) {
/* 1811 */     Structure nearest = null;
/* 1812 */     double lowest_distance = Double.MAX_VALUE;
/*      */     Iterator localIterator2;
/* 1814 */     for (Iterator localIterator1 = this.towns.values().iterator(); localIterator1.hasNext(); 
/* 1815 */         localIterator2.hasNext())
/*      */     {
/* 1814 */       Town town = (Town)localIterator1.next();
/* 1815 */       localIterator2 = town.getStructures().iterator(); continue;Structure struct = (Structure)localIterator2.next();
/* 1816 */       double distance = struct.getCenterLocation().getLocation().distance(loc);
/* 1817 */       if (distance < lowest_distance) {
/* 1818 */         lowest_distance = distance;
/* 1819 */         nearest = struct;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1824 */     return nearest;
/*      */   }
/*      */   
/*      */   public ItemStack getRandomLeaderSkull(String message) {
/* 1828 */     Random rand = new Random();
/* 1829 */     int i = rand.nextInt(getLeaderGroup().getMemberCount());
/* 1830 */     int count = 0;
/* 1831 */     Resident resident = CivGlobal.getResident(getLeader());
/*      */     
/* 1833 */     for (Resident res : getLeaderGroup().getMemberList()) {
/* 1834 */       if (count == i) {
/* 1835 */         resident = res;
/* 1836 */         break;
/*      */       }
/*      */     }
/*      */     
/* 1840 */     String leader = "";
/* 1841 */     if (resident != null) {
/* 1842 */       leader = resident.getName();
/*      */     }
/*      */     
/* 1845 */     ItemStack stack = ItemManager.spawnPlayerHead(leader, message + " (" + leader + ")");
/* 1846 */     return stack;
/*      */   }
/*      */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\Civilization.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
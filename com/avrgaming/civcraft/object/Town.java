/*      */ package com.avrgaming.civcraft.object;
/*      */ 
/*      */ import com.avrgaming.civcraft.components.AttributeBase;
/*      */ import com.avrgaming.civcraft.components.AttributeRate;
/*      */ import com.avrgaming.civcraft.components.AttributeWarUnhappiness;
/*      */ import com.avrgaming.civcraft.components.Component;
/*      */ import com.avrgaming.civcraft.config.CivSettings;
/*      */ import com.avrgaming.civcraft.config.ConfigBuff;
/*      */ import com.avrgaming.civcraft.config.ConfigBuildableInfo;
/*      */ import com.avrgaming.civcraft.config.ConfigCultureLevel;
/*      */ import com.avrgaming.civcraft.config.ConfigGovernment;
/*      */ import com.avrgaming.civcraft.config.ConfigHappinessState;
/*      */ import com.avrgaming.civcraft.config.ConfigTownHappinessLevel;
/*      */ import com.avrgaming.civcraft.config.ConfigTownLevel;
/*      */ import com.avrgaming.civcraft.config.ConfigTownUpgrade;
/*      */ import com.avrgaming.civcraft.config.ConfigTradeGood;
/*      */ import com.avrgaming.civcraft.config.ConfigUnit;
/*      */ import com.avrgaming.civcraft.database.SQL;
/*      */ import com.avrgaming.civcraft.database.SQLUpdate;
/*      */ import com.avrgaming.civcraft.exception.AlreadyRegisteredException;
/*      */ import com.avrgaming.civcraft.exception.CivException;
/*      */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*      */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*      */ import com.avrgaming.civcraft.interactive.InteractiveBuildableRefresh;
/*      */ import com.avrgaming.civcraft.items.BonusGoodie;
/*      */ import com.avrgaming.civcraft.items.units.Unit;
/*      */ import com.avrgaming.civcraft.main.CivGlobal;
/*      */ import com.avrgaming.civcraft.main.CivLog;
/*      */ import com.avrgaming.civcraft.main.CivMessage;
/*      */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*      */ import com.avrgaming.civcraft.permission.PlotPermissions;
/*      */ import com.avrgaming.civcraft.randomevents.RandomEvent;
/*      */ import com.avrgaming.civcraft.road.Road;
/*      */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*      */ import com.avrgaming.civcraft.structure.Buildable;
/*      */ import com.avrgaming.civcraft.structure.Structure;
/*      */ import com.avrgaming.civcraft.structure.TownHall;
/*      */ import com.avrgaming.civcraft.structure.TradeOutpost;
/*      */ import com.avrgaming.civcraft.structure.Wall;
/*      */ import com.avrgaming.civcraft.structure.wonders.Wonder;
/*      */ import com.avrgaming.civcraft.template.Template;
/*      */ import com.avrgaming.civcraft.threading.TaskMaster;
/*      */ import com.avrgaming.civcraft.threading.sync.SyncUpdateTags;
/*      */ import com.avrgaming.civcraft.threading.tasks.BuildAsyncTask;
/*      */ import com.avrgaming.civcraft.util.BlockCoord;
/*      */ import com.avrgaming.civcraft.util.ChunkCoord;
/*      */ import com.avrgaming.civcraft.util.CivColor;
/*      */ import com.avrgaming.civcraft.util.DateUtil;
/*      */ import com.avrgaming.civcraft.util.ItemFrameStorage;
/*      */ import com.avrgaming.civcraft.util.WorldCord;
/*      */ import com.avrgaming.civcraft.war.War;
/*      */ import com.avrgaming.global.perks.Perk;
/*      */ import com.avrgaming.global.perks.components.CustomTemplate;
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
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import org.bukkit.Bukkit;
/*      */ import org.bukkit.Location;
/*      */ import org.bukkit.Material;
/*      */ import org.bukkit.World;
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
/*      */ public class Town
/*      */   extends SQLObject
/*      */ {
/*   88 */   private ConcurrentHashMap<String, Resident> residents = new ConcurrentHashMap();
/*   89 */   private ConcurrentHashMap<String, Resident> fakeResidents = new ConcurrentHashMap();
/*      */   
/*   91 */   private ConcurrentHashMap<ChunkCoord, TownChunk> townChunks = new ConcurrentHashMap();
/*   92 */   private ConcurrentHashMap<ChunkCoord, TownChunk> outposts = new ConcurrentHashMap();
/*   93 */   private ConcurrentHashMap<ChunkCoord, CultureChunk> cultureChunks = new ConcurrentHashMap();
/*      */   
/*   95 */   private ConcurrentHashMap<BlockCoord, Wonder> wonders = new ConcurrentHashMap();
/*   96 */   private ConcurrentHashMap<BlockCoord, Structure> structures = new ConcurrentHashMap();
/*   97 */   private ConcurrentHashMap<BlockCoord, Buildable> disabledBuildables = new ConcurrentHashMap();
/*      */   
/*      */   private int level;
/*      */   
/*      */   private double taxRate;
/*      */   
/*      */   private double flatTax;
/*      */   private Civilization civ;
/*      */   private Civilization motherCiv;
/*      */   private int daysInDebt;
/*  107 */   private double baseHammers = 1.0D;
/*      */   
/*      */   private double extraHammers;
/*      */   
/*      */   public Buildable currentStructureInProgress;
/*      */   
/*      */   public Buildable currentWonderInProgress;
/*      */   
/*      */   private int culture;
/*      */   
/*      */   private PermissionGroup defaultGroup;
/*      */   
/*      */   private PermissionGroup mayorGroup;
/*      */   
/*      */   private PermissionGroup assistantGroup;
/*      */   
/*      */   private double unusedBeakers;
/*      */   private String defaultGroupName;
/*      */   private String mayorGroupName;
/*      */   private String assistantGroupName;
/*  127 */   public ArrayList<TownChunk> savedEdgeBlocks = new ArrayList();
/*  128 */   public HashSet<Town> townTouchList = new HashSet();
/*      */   
/*  130 */   private ConcurrentHashMap<String, PermissionGroup> groups = new ConcurrentHashMap();
/*      */   private EconObject treasury;
/*  132 */   private ConcurrentHashMap<String, ConfigTownUpgrade> upgrades = new ConcurrentHashMap();
/*      */   
/*      */ 
/*  135 */   private ConcurrentHashMap<String, BonusGoodie> bonusGoodies = new ConcurrentHashMap();
/*      */   
/*  137 */   private BuffManager buffManager = new BuffManager();
/*      */   
/*  139 */   private boolean pvp = false;
/*      */   
/*  141 */   public ArrayList<BuildAsyncTask> build_tasks = new ArrayList();
/*  142 */   public Buildable lastBuildableBuilt = null;
/*      */   
/*  144 */   public boolean leaderWantsToDisband = false;
/*  145 */   public boolean mayorWantsToDisband = false;
/*  146 */   public HashSet<String> outlaws = new HashSet();
/*      */   
/*  148 */   public boolean claimed = false;
/*  149 */   public boolean defeated = false;
/*  150 */   public LinkedList<Buildable> invalidStructures = new LinkedList();
/*      */   
/*      */ 
/*  153 */   public int saved_bank_level = 1;
/*  154 */   public double saved_bank_interest_amount = 0.0D;
/*      */   
/*      */ 
/*  157 */   private double baseHappy = 0.0D;
/*  158 */   private double baseUnhappy = 0.0D;
/*      */   
/*      */ 
/*      */   private RandomEvent activeEvent;
/*      */   
/*  163 */   private Date lastBuildableRefresh = null;
/*      */   
/*      */ 
/*      */ 
/*      */   private Date created_date;
/*      */   
/*      */ 
/*      */ 
/*      */   public static final int ATTR_TIMEOUT_SECONDS = 5;
/*      */   
/*      */ 
/*      */ 
/*  175 */   public HashMap<String, AttrCache> attributeCache = new HashMap();
/*      */   
/*  177 */   private double baseGrowth = 0.0D;
/*      */   public static final String TABLE_NAME = "TOWNS";
/*      */   
/*      */   public static void init() throws SQLException {
/*  181 */     if (!SQL.hasTable("TOWNS")) {
/*  182 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + "TOWNS" + " (" + 
/*  183 */         "`id` int(11) unsigned NOT NULL auto_increment," + 
/*  184 */         "`name` VARCHAR(64) NOT NULL," + 
/*  185 */         "`civ_id` int(11) NOT NULL DEFAULT 0," + 
/*  186 */         "`master_civ_id` int(11) NOT NULL DEFAULT 0," + 
/*  187 */         "`mother_civ_id` int(11) NOT NULL DEFAULT 0," + 
/*  188 */         "`defaultGroupName` mediumtext DEFAULT NULL," + 
/*  189 */         "`mayorGroupName` mediumtext DEFAULT NULL," + 
/*  190 */         "`assistantGroupName` mediumtext DEFAULT NULL," + 
/*  191 */         "`upgrades` mediumtext DEFAULT NULL," + 
/*  192 */         "`level` int(11) DEFAULT 1," + 
/*  193 */         "`debt` double DEFAULT 0," + 
/*  194 */         "`coins` double DEFAULT 0," + 
/*  195 */         "`daysInDebt` int(11) DEFAULT 0," + 
/*  196 */         "`flat_tax` double NOT NULL DEFAULT '0'," + 
/*  197 */         "`tax_rate` double DEFAULT 0," + 
/*  198 */         "`extra_hammers` double DEFAULT 0," + 
/*  199 */         "`culture` int(11) DEFAULT 0," + 
/*  200 */         "`created_date` long," + 
/*  201 */         "`outlaws` mediumtext DEFAULT NULL," + 
/*  202 */         "`dbg_civ_name` mediumtext DEFAULT NULL," + 
/*  203 */         "UNIQUE KEY (`name`), " + 
/*  204 */         "PRIMARY KEY (`id`)" + ")";
/*      */       
/*  206 */       SQL.makeTable(table_create);
/*  207 */       CivLog.info("Created TOWNS table");
/*      */     } else {
/*  209 */       CivLog.info("TOWNS table OK!");
/*      */       
/*      */ 
/*  212 */       SQL.makeCol("outlaws", "mediumtext", "TOWNS");
/*  213 */       SQL.makeCol("daysInDebt", "int(11)", "TOWNS");
/*  214 */       SQL.makeCol("mother_civ_id", "int(11)", "TOWNS");
/*  215 */       SQL.makeCol("dbg_civ_name", "mediumtext", "TOWNS");
/*  216 */       SQL.makeCol("created_date", "long", "TOWNS");
/*      */     }
/*      */   }
/*      */   
/*      */   public void load(ResultSet rs) throws SQLException, InvalidNameException, CivException
/*      */   {
/*  222 */     setId(rs.getInt("id"));
/*  223 */     setName(rs.getString("name"));
/*  224 */     setLevel(rs.getInt("level"));
/*  225 */     setCiv(CivGlobal.getCivFromId(rs.getInt("civ_id")));
/*      */     
/*  227 */     Integer motherCivId = Integer.valueOf(rs.getInt("mother_civ_id"));
/*  228 */     if ((motherCivId != null) && (motherCivId.intValue() != 0)) {
/*  229 */       Civilization mother = CivGlobal.getConqueredCivFromId(motherCivId.intValue());
/*  230 */       if (mother == null) {
/*  231 */         mother = CivGlobal.getCivFromId(motherCivId.intValue());
/*      */       }
/*      */       
/*  234 */       if (mother == null) {
/*  235 */         CivLog.warning("Unable to find a mother civ with ID:" + motherCivId + "!");
/*      */       } else {
/*  237 */         setMotherCiv(mother);
/*      */       }
/*      */     }
/*      */     
/*  241 */     if (getCiv() == null) {
/*  242 */       CivLog.error("TOWN:" + getName() + " WITHOUT A CIV, id was:" + rs.getInt("civ_id"));
/*      */       
/*  244 */       CivGlobal.orphanTowns.add(this);
/*  245 */       throw new CivException("Failed to load town, bad data.");
/*      */     }
/*  247 */     setDaysInDebt(rs.getInt("daysInDebt"));
/*  248 */     setFlatTax(rs.getDouble("flat_tax"));
/*  249 */     setTaxRate(rs.getDouble("tax_rate"));
/*  250 */     setUpgradesFromString(rs.getString("upgrades"));
/*      */     
/*      */ 
/*  253 */     setExtraHammers(rs.getDouble("extra_hammers"));
/*  254 */     setAccumulatedCulture(rs.getInt("culture"));
/*      */     
/*  256 */     this.defaultGroupName = "residents";
/*  257 */     this.mayorGroupName = "mayors";
/*  258 */     this.assistantGroupName = "assistants";
/*      */     
/*  260 */     setTreasury(new EconObject(this));
/*  261 */     getTreasury().setBalance(rs.getDouble("coins"), false);
/*  262 */     setDebt(rs.getDouble("debt"));
/*      */     
/*  264 */     String outlawRaw = rs.getString("outlaws");
/*  265 */     if (outlawRaw != null) {
/*  266 */       String[] outlaws = outlawRaw.split(",");
/*      */       String[] arrayOfString1;
/*  268 */       int j = (arrayOfString1 = outlaws).length; for (int i = 0; i < j; i++) { String outlaw = arrayOfString1[i];
/*  269 */         this.outlaws.add(outlaw);
/*      */       }
/*      */     }
/*      */     
/*  273 */     Long ctime = Long.valueOf(rs.getLong("created_date"));
/*  274 */     if ((ctime == null) || (ctime.longValue() == 0L)) {
/*  275 */       setCreated(new Date(0L));
/*      */     } else {
/*  277 */       setCreated(new Date(ctime.longValue()));
/*      */     }
/*      */     
/*  280 */     getCiv().addTown(this);
/*      */   }
/*      */   
/*      */   public void save()
/*      */   {
/*  285 */     SQLUpdate.add(this);
/*      */   }
/*      */   
/*      */   public void saveNow() throws SQLException
/*      */   {
/*  290 */     HashMap<String, Object> hashmap = new HashMap();
/*      */     
/*  292 */     hashmap.put("name", getName());
/*  293 */     hashmap.put("civ_id", Integer.valueOf(getCiv().getId()));
/*      */     
/*  295 */     if (this.motherCiv != null) {
/*  296 */       hashmap.put("mother_civ_id", Integer.valueOf(this.motherCiv.getId()));
/*      */     } else {
/*  298 */       hashmap.put("mother_civ_id", Integer.valueOf(0));
/*      */     }
/*      */     
/*  301 */     hashmap.put("defaultGroupName", getDefaultGroupName());
/*  302 */     hashmap.put("mayorGroupName", getMayorGroupName());
/*  303 */     hashmap.put("assistantGroupName", getAssistantGroupName());
/*  304 */     hashmap.put("level", Integer.valueOf(getLevel()));
/*  305 */     hashmap.put("debt", Double.valueOf(getTreasury().getDebt()));
/*  306 */     hashmap.put("daysInDebt", Integer.valueOf(getDaysInDebt()));
/*  307 */     hashmap.put("flat_tax", Double.valueOf(getFlatTax()));
/*  308 */     hashmap.put("tax_rate", Double.valueOf(getTaxRate()));
/*  309 */     hashmap.put("extra_hammers", Double.valueOf(getExtraHammers()));
/*  310 */     hashmap.put("culture", Integer.valueOf(getAccumulatedCulture()));
/*  311 */     hashmap.put("upgrades", getUpgradesString());
/*  312 */     hashmap.put("coins", Double.valueOf(getTreasury().getBalance()));
/*  313 */     hashmap.put("dbg_civ_name", getCiv().getName());
/*      */     
/*  315 */     if (this.created_date != null) {
/*  316 */       hashmap.put("created_date", Long.valueOf(this.created_date.getTime()));
/*      */     } else {
/*  318 */       hashmap.put("created_date", null);
/*      */     }
/*      */     
/*  321 */     String outlaws = "";
/*  322 */     for (String outlaw : this.outlaws) {
/*  323 */       outlaws = outlaws + outlaw + ",";
/*      */     }
/*  325 */     hashmap.put("outlaws", outlaws);
/*      */     
/*  327 */     SQL.updateNamedObject(this, hashmap, "TOWNS");
/*      */   }
/*      */   
/*      */ 
/*      */   public void delete()
/*      */     throws SQLException
/*      */   {
/*  334 */     for (PermissionGroup grp : this.groups.values()) {
/*  335 */       grp.delete();
/*      */     }
/*      */     
/*      */ 
/*  339 */     for (Resident resident : this.residents.values()) {
/*  340 */       resident.setTown(null);
/*      */       
/*  342 */       resident.getTreasury().setDebt(0.0D);
/*  343 */       resident.saveNow();
/*      */     }
/*      */     
/*      */ 
/*  347 */     if (this.structures != null) {
/*  348 */       for (Structure struct : this.structures.values()) {
/*  349 */         struct.delete();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  354 */     if (getTownChunks() != null) {
/*  355 */       for (TownChunk tc : getTownChunks()) {
/*  356 */         tc.delete();
/*      */       }
/*      */     }
/*      */     
/*  360 */     if (this.wonders != null) {
/*  361 */       for (Wonder wonder : this.wonders.values()) {
/*  362 */         wonder.unbindStructureBlocks();
/*  363 */         wonder.fancyDestroyStructureBlocks();
/*  364 */         wonder.delete();
/*      */       }
/*      */     }
/*      */     
/*  368 */     if (this.cultureChunks != null) {
/*  369 */       for (CultureChunk cc : this.cultureChunks.values()) {
/*  370 */         CivGlobal.removeCultureChunk(cc);
/*      */       }
/*      */     }
/*  373 */     this.cultureChunks = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  378 */     CivGlobal.getSessionDB().deleteAllForTown(this);
/*      */     
/*  380 */     SQL.deleteNamedObject(this, "TOWNS");
/*  381 */     CivGlobal.removeTown(this);
/*      */   }
/*      */   
/*      */   public Town(String name, Resident mayor, Civilization civ) throws InvalidNameException
/*      */   {
/*  386 */     setName(name);
/*  387 */     setLevel(1);
/*  388 */     setTaxRate(0.0D);
/*  389 */     setFlatTax(0.0D);
/*  390 */     setCiv(civ);
/*      */     
/*  392 */     setDaysInDebt(0);
/*  393 */     setHammerRate(1.0D);
/*  394 */     setExtraHammers(0.0D);
/*  395 */     addAccumulatedCulture(0.0D);
/*  396 */     setTreasury(new EconObject(this));
/*  397 */     getTreasury().setBalance(0.0D, false);
/*  398 */     this.created_date = new Date();
/*      */     
/*  400 */     loadSettings();
/*      */   }
/*      */   
/*      */   public Town(ResultSet rs) throws SQLException, InvalidNameException, CivException {
/*  404 */     load(rs);
/*  405 */     loadSettings();
/*      */   }
/*      */   
/*      */   public void loadSettings() {
/*      */     try {
/*  410 */       this.baseHammers = CivSettings.getDouble(CivSettings.townConfig, "town.base_hammer_rate");
/*  411 */       setBaseGrowth(CivSettings.getDouble(CivSettings.townConfig, "town.base_growth_rate"));
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     catch (InvalidConfiguration e)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  421 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   private void setUpgradesFromString(String upgradeString) {
/*  426 */     String[] split = upgradeString.split(",");
/*      */     String[] arrayOfString1;
/*  428 */     int j = (arrayOfString1 = split).length; for (int i = 0; i < j; i++) { String str = arrayOfString1[i];
/*  429 */       if ((str != null) && (!str.equals("")))
/*      */       {
/*      */ 
/*      */ 
/*  433 */         ConfigTownUpgrade upgrade = (ConfigTownUpgrade)CivSettings.townUpgrades.get(str);
/*  434 */         if (upgrade == null) {
/*  435 */           CivLog.warning("Unknown town upgrade:" + str + " in town " + getName());
/*      */         }
/*      */         else
/*      */         {
/*  439 */           this.upgrades.put(str, upgrade); }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*  444 */   private String getUpgradesString() { String out = "";
/*      */     
/*  446 */     for (ConfigTownUpgrade upgrade : this.upgrades.values()) {
/*  447 */       out = out + upgrade.id + ",";
/*      */     }
/*      */     
/*  450 */     return out;
/*      */   }
/*      */   
/*      */   public ConfigTownUpgrade getUpgrade(String id) {
/*  454 */     return (ConfigTownUpgrade)this.upgrades.get(id);
/*      */   }
/*      */   
/*      */   public boolean isMayor(Resident res) {
/*  458 */     if (getMayorGroup().hasMember(res)) {
/*  459 */       return true;
/*      */     }
/*  461 */     return false;
/*      */   }
/*      */   
/*      */   public int getResidentCount() {
/*  465 */     return this.residents.size();
/*      */   }
/*      */   
/*      */   public Collection<Resident> getResidents() {
/*  469 */     return this.residents.values();
/*      */   }
/*      */   
/*      */   public boolean hasResident(String name) {
/*  473 */     return this.residents.containsKey(name.toLowerCase());
/*      */   }
/*      */   
/*      */   public boolean hasResident(Resident res) {
/*  477 */     return hasResident(res.getName());
/*      */   }
/*      */   
/*      */   public void addResident(Resident res) throws AlreadyRegisteredException {
/*  481 */     String key = res.getName().toLowerCase();
/*      */     
/*  483 */     if (this.residents.containsKey(key)) {
/*  484 */       throw new AlreadyRegisteredException(res.getName() + " already a member of town " + getName());
/*      */     }
/*      */     
/*  487 */     res.setTown(this);
/*      */     
/*  489 */     this.residents.put(key, res);
/*  490 */     if ((this.defaultGroup != null) && (!this.defaultGroup.hasMember(res))) {
/*  491 */       this.defaultGroup.addMember(res);
/*  492 */       this.defaultGroup.save();
/*      */     }
/*      */   }
/*      */   
/*      */   public void addTownChunk(TownChunk tc) throws AlreadyRegisteredException
/*      */   {
/*  498 */     if (this.townChunks.containsKey(tc.getChunkCoord())) {
/*  499 */       throw new AlreadyRegisteredException("TownChunk at " + tc.getChunkCoord() + " already registered to town " + getName());
/*      */     }
/*  501 */     this.townChunks.put(tc.getChunkCoord(), tc);
/*      */   }
/*      */   
/*      */   public Structure findStructureByName(String name) {
/*  505 */     for (Structure struct : this.structures.values()) {
/*  506 */       if (struct.getDisplayName().equalsIgnoreCase(name)) {
/*  507 */         return struct;
/*      */       }
/*      */     }
/*  510 */     return null;
/*      */   }
/*      */   
/*      */   public Structure findStructureByLocation(WorldCord wc) {
/*  514 */     return (Structure)this.structures.get(wc);
/*      */   }
/*      */   
/*      */   public int getLevel() {
/*  518 */     return this.level;
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
/*      */   public void setLevel(int level)
/*      */   {
/*  532 */     this.level = level;
/*      */   }
/*      */   
/*      */   public double getTaxRate() {
/*  536 */     return this.taxRate;
/*      */   }
/*      */   
/*      */   public void setTaxRate(double taxRate) {
/*  540 */     this.taxRate = taxRate;
/*      */   }
/*      */   
/*      */   public String getTaxRateString() {
/*  544 */     long rounded = Math.round(this.taxRate * 100.0D);
/*  545 */     return rounded + "%";
/*      */   }
/*      */   
/*      */   public double getFlatTax() {
/*  549 */     return this.flatTax;
/*      */   }
/*      */   
/*      */   public void setFlatTax(double flatTax) {
/*  553 */     this.flatTax = flatTax;
/*      */   }
/*      */   
/*      */   public Civilization getCiv() {
/*  557 */     return this.civ;
/*      */   }
/*      */   
/*      */   public void setCiv(Civilization civ) {
/*  561 */     this.civ = civ;
/*      */   }
/*      */   
/*      */   public int getAccumulatedCulture() {
/*  565 */     return this.culture;
/*      */   }
/*      */   
/*      */   public void setAccumulatedCulture(int culture) {
/*  569 */     this.culture = culture;
/*      */   }
/*      */   
/*      */   public AttrSource getCultureRate() {
/*  573 */     double rate = 1.0D;
/*  574 */     HashMap<String, Double> rates = new HashMap();
/*      */     
/*  576 */     double newRate = getGovernment().culture_rate;
/*  577 */     rates.put("Government", Double.valueOf(newRate - rate));
/*  578 */     rate = newRate;
/*      */     
/*  580 */     ConfigHappinessState state = CivSettings.getHappinessState(getHappinessPercentage());
/*  581 */     newRate = rate * state.culture_rate;
/*  582 */     rates.put("Happiness", Double.valueOf(newRate - rate));
/*  583 */     rate = newRate;
/*      */     
/*  585 */     double additional = getBuffManager().getEffectiveDouble("buff_fine_art");
/*      */     
/*  587 */     if (getBuffManager().hasBuff("buff_pyramid_culture")) {
/*  588 */       additional += getBuffManager().getEffectiveDouble("buff_pyramid_culture");
/*      */     }
/*      */     
/*  591 */     rates.put("Wonders/Goodies", Double.valueOf(additional));
/*  592 */     rate += additional;
/*      */     
/*  594 */     return new AttrSource(rates, rate, null);
/*      */   }
/*      */   
/*      */   public AttrSource getCulture()
/*      */   {
/*  599 */     AttrCache cache = (AttrCache)this.attributeCache.get("CULTURE");
/*  600 */     if (cache == null) {
/*  601 */       cache = new AttrCache();
/*  602 */       cache.lastUpdate = new Date();
/*      */     } else {
/*  604 */       Date now = new Date();
/*  605 */       if (now.getTime() > cache.lastUpdate.getTime() + 5000L) {
/*  606 */         cache.lastUpdate = now;
/*      */       } else {
/*  608 */         return cache.sources;
/*      */       }
/*      */     }
/*      */     
/*  612 */     double total = 0.0D;
/*  613 */     HashMap<String, Double> sources = new HashMap();
/*      */     
/*      */ 
/*  616 */     double goodieCulture = getBuffManager().getEffectiveInt("buff_doesnotexist");
/*  617 */     sources.put("Goodies", Double.valueOf(goodieCulture));
/*  618 */     total += goodieCulture;
/*      */     
/*      */ 
/*  621 */     double fromStructures = 0.0D;
/*  622 */     Iterator localIterator2; for (Iterator localIterator1 = this.structures.values().iterator(); localIterator1.hasNext(); 
/*  623 */         localIterator2.hasNext())
/*      */     {
/*  622 */       Structure struct = (Structure)localIterator1.next();
/*  623 */       localIterator2 = struct.attachedComponents.iterator(); continue;Component comp = (Component)localIterator2.next();
/*  624 */       if ((comp instanceof AttributeBase)) {
/*  625 */         AttributeBase as = (AttributeBase)comp;
/*  626 */         if (as.getString("attribute").equalsIgnoreCase("CULTURE")) {
/*  627 */           fromStructures += as.getGenerated();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  633 */     total += fromStructures;
/*  634 */     sources.put("Structures", Double.valueOf(fromStructures));
/*      */     
/*  636 */     AttrSource rate = getCultureRate();
/*  637 */     total *= rate.total;
/*      */     
/*  639 */     if (total < 0.0D) {
/*  640 */       total = 0.0D;
/*      */     }
/*      */     
/*  643 */     AttrSource as = new AttrSource(sources, total, rate);
/*  644 */     cache.sources = as;
/*  645 */     this.attributeCache.put("CULTURE", cache);
/*  646 */     return as;
/*      */   }
/*      */   
/*      */   public void addAccumulatedCulture(double generated)
/*      */   {
/*  651 */     ConfigCultureLevel clc = (ConfigCultureLevel)CivSettings.cultureLevels.get(Integer.valueOf(getCultureLevel()));
/*      */     
/*  653 */     this.culture = ((int)(this.culture + generated));
/*  654 */     if ((getCultureLevel() != CivSettings.getMaxCultureLevel()) && 
/*  655 */       (this.culture >= clc.amount)) {
/*  656 */       CivGlobal.processCulture();
/*  657 */       CivMessage.sendCiv(this.civ, "The borders of " + getName() + " have expanded!");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public double getExtraHammers()
/*      */   {
/*  665 */     return this.extraHammers;
/*      */   }
/*      */   
/*      */   public void setExtraHammers(double extraHammers)
/*      */   {
/*  670 */     this.extraHammers = extraHammers;
/*      */   }
/*      */   
/*      */   public AttrSource getHammerRate() {
/*  674 */     double rate = 1.0D;
/*  675 */     HashMap<String, Double> rates = new HashMap();
/*  676 */     ConfigHappinessState state = CivSettings.getHappinessState(getHappinessPercentage());
/*      */     
/*      */ 
/*  679 */     double newRate = rate * state.hammer_rate;
/*  680 */     rates.put("Happiness", Double.valueOf(newRate - rate));
/*  681 */     rate = newRate;
/*      */     
/*      */ 
/*  684 */     newRate = rate * getGovernment().hammer_rate;
/*  685 */     rates.put("Government", Double.valueOf(newRate - rate));
/*  686 */     rate = newRate;
/*      */     
/*  688 */     double randomRate = RandomEvent.getHammerRate(this);
/*  689 */     newRate = rate * randomRate;
/*  690 */     rates.put("Random Events", Double.valueOf(newRate - rate));
/*  691 */     rate = newRate;
/*      */     
/*      */ 
/*  694 */     if (this.motherCiv != null) {
/*      */       try {
/*  696 */         newRate = rate * CivSettings.getDouble(CivSettings.warConfig, "war.captured_penalty");
/*  697 */         rates.put("Captured Penalty", Double.valueOf(newRate - rate));
/*  698 */         rate = newRate;
/*      */       }
/*      */       catch (InvalidConfiguration e) {
/*  701 */         e.printStackTrace();
/*      */       }
/*      */     }
/*  704 */     return new AttrSource(rates, rate, null);
/*      */   }
/*      */   
/*      */   public AttrSource getHammers() {
/*  708 */     double total = 0.0D;
/*      */     
/*  710 */     AttrCache cache = (AttrCache)this.attributeCache.get("HAMMERS");
/*  711 */     if (cache == null) {
/*  712 */       cache = new AttrCache();
/*  713 */       cache.lastUpdate = new Date();
/*      */     } else {
/*  715 */       Date now = new Date();
/*  716 */       if (now.getTime() > cache.lastUpdate.getTime() + 5000L) {
/*  717 */         cache.lastUpdate = now;
/*      */       } else {
/*  719 */         return cache.sources;
/*      */       }
/*      */     }
/*      */     
/*  723 */     HashMap<String, Double> sources = new HashMap();
/*      */     
/*      */ 
/*  726 */     double wonderGoodies = getBuffManager().getEffectiveInt("buff_construction");
/*  727 */     sources.put("Wonders/Goodies", Double.valueOf(wonderGoodies));
/*  728 */     total += wonderGoodies;
/*      */     
/*  730 */     double cultureHammers = getHammersFromCulture().doubleValue();
/*  731 */     sources.put("Culture Biomes", Double.valueOf(cultureHammers));
/*  732 */     total += cultureHammers;
/*      */     
/*      */ 
/*  735 */     double structures = 0.0D;
/*  736 */     Iterator localIterator2; for (Iterator localIterator1 = this.structures.values().iterator(); localIterator1.hasNext(); 
/*  737 */         localIterator2.hasNext())
/*      */     {
/*  736 */       Structure struct = (Structure)localIterator1.next();
/*  737 */       localIterator2 = struct.attachedComponents.iterator(); continue;Component comp = (Component)localIterator2.next();
/*  738 */       if ((comp instanceof AttributeBase)) {
/*  739 */         AttributeBase as = (AttributeBase)comp;
/*  740 */         if (as.getString("attribute").equalsIgnoreCase("HAMMERS")) {
/*  741 */           structures += as.getGenerated();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  747 */     total += structures;
/*  748 */     sources.put("Structures", Double.valueOf(structures));
/*      */     
/*      */ 
/*  751 */     sources.put("Base Hammers", Double.valueOf(this.baseHammers));
/*  752 */     total += this.baseHammers;
/*      */     
/*  754 */     AttrSource rate = getHammerRate();
/*  755 */     total *= rate.total;
/*      */     
/*  757 */     if (total < this.baseHammers) {
/*  758 */       total = this.baseHammers;
/*      */     }
/*      */     
/*  761 */     AttrSource as = new AttrSource(sources, total, rate);
/*  762 */     cache.sources = as;
/*  763 */     this.attributeCache.put("HAMMERS", cache);
/*  764 */     return as;
/*      */   }
/*      */   
/*      */   public void setHammerRate(double hammerRate) {
/*  768 */     this.baseHammers = hammerRate;
/*      */   }
/*      */   
/*      */   public static Town newTown(Resident resident, String name, Civilization civ, boolean free, boolean capitol, Location loc) throws CivException
/*      */   {
/*      */     try
/*      */     {
/*  775 */       if ((War.isWarTime()) && (!free) && (civ.getDiplomacyManager().isAtWar())) {
/*  776 */         throw new CivException("Cannot start towns during WarTime if you're at war.");
/*      */       }
/*      */       
/*  779 */       if (civ == null) {
/*  780 */         throw new CivException("Towns must be founded inside a Civilization.");
/*      */       }
/*      */       
/*  783 */       if ((resident.getTown() != null) && (resident.getTown().isMayor(resident))) {
/*  784 */         throw new CivException("You cannot start another town since you are the mayor of " + resident.getTown().getName());
/*      */       }
/*      */       
/*  787 */       if (resident.hasCamp()) {
/*  788 */         throw new CivException("You must first leave your camp before starting a town.");
/*      */       }
/*      */       
/*  791 */       Town existTown = CivGlobal.getTown(name);
/*  792 */       if (existTown != null) {
/*  793 */         throw new CivException("A town named " + name + " already exists!");
/*      */       }
/*      */       
/*      */       try
/*      */       {
/*  798 */         newTown = new Town(name, resident, civ);
/*      */       } catch (InvalidNameException e) { Town newTown;
/*  800 */         throw new CivException("The town name of " + name + " is invalid, choose another.");
/*      */       }
/*      */       Town newTown;
/*  803 */       Player player = Bukkit.getPlayer(resident.getName());
/*  804 */       if (player == null) {
/*  805 */         throw new CivException("Couldn't find you? Are you online? wat?");
/*      */       }
/*      */       
/*  808 */       if (CivGlobal.getTownChunk(loc) != null) {
/*  809 */         throw new CivException("Cannot start town here, chunk already registered to a town.");
/*      */       }
/*      */       
/*  812 */       CultureChunk cultrueChunk = CivGlobal.getCultureChunk(loc);
/*  813 */       if ((cultrueChunk != null) && (cultrueChunk.getCiv() != resident.getCiv())) {
/*  814 */         throw new CivException("Cannot start a town inside another civ's cultural borders.");
/*      */       }
/*      */       
/*      */ 
/*      */       try
/*      */       {
/*  820 */         double minDistanceFriend = CivSettings.getDouble(CivSettings.townConfig, "town.min_town_distance");
/*  821 */         minDistanceEnemy = CivSettings.getDouble(CivSettings.townConfig, "town.min_town_distance_enemy");
/*      */       } catch (InvalidConfiguration e) { double minDistanceEnemy;
/*  823 */         e.printStackTrace();
/*  824 */         throw new CivException("Internal configuration error."); }
/*      */       double minDistanceEnemy;
/*      */       double minDistanceFriend;
/*  827 */       double dist; for (Town town : CivGlobal.getTowns()) {
/*  828 */         TownHall townhall = town.getTownHall();
/*  829 */         if (townhall != null)
/*      */         {
/*      */ 
/*      */ 
/*  833 */           dist = townhall.getCenterLocation().distance(new BlockCoord(player.getLocation()));
/*  834 */           double minDistance = minDistanceFriend;
/*  835 */           if (townhall.getCiv().getDiplomacyManager().atWarWith(civ)) {
/*  836 */             minDistance = minDistanceEnemy;
/*      */           }
/*      */           
/*  839 */           if (dist < minDistance) {
/*  840 */             DecimalFormat df = new DecimalFormat();
/*  841 */             throw new CivException("Cannot build town here. Too close to the town of " + town.getName() + ". Distance is " + df.format(dist) + " and needs to be " + minDistance);
/*      */           }
/*      */         }
/*      */       }
/*      */       try
/*      */       {
/*  847 */         int min_distance = CivSettings.getInteger(CivSettings.civConfig, "civ.min_distance").intValue();
/*  848 */         ChunkCoord foundLocation = new ChunkCoord(loc);
/*      */         
/*  850 */         for (TownChunk cc : CivGlobal.getTownChunks()) {
/*  851 */           if (cc.getTown().getCiv() != newTown.getCiv())
/*      */           {
/*      */ 
/*      */ 
/*  855 */             if (foundLocation.distance(cc.getChunkCoord()) <= min_distance)
/*  856 */               throw new CivException("The town borders of " + cc.getTown().getName() + " are too close, cannot found town here.");
/*      */           }
/*      */         }
/*      */       } catch (InvalidConfiguration e1) {
/*  860 */         e1.printStackTrace();
/*  861 */         throw new CivException("Internal configuration exception.");
/*      */       }
/*      */       
/*      */ 
/*  865 */       if (!free) {
/*  866 */         ConfigUnit unit = Unit.getPlayerUnit(player);
/*  867 */         if ((unit == null) || (!unit.id.equals("u_settler"))) {
/*  868 */           throw new CivException("You must be a settler in order to found a town.");
/*      */         }
/*      */       }
/*  871 */       newTown.saveNow();
/*      */       
/*  873 */       CivGlobal.addTown(newTown);
/*      */       
/*      */ 
/*      */       try
/*      */       {
/*  878 */         PermissionGroup residentsGroup = new PermissionGroup(newTown, "residents");
/*  879 */         residentsGroup.addMember(resident);
/*  880 */         residentsGroup.saveNow();
/*  881 */         newTown.setDefaultGroup(residentsGroup);
/*      */         
/*      */ 
/*  884 */         PermissionGroup mayorGroup = new PermissionGroup(newTown, "mayors");
/*  885 */         mayorGroup.addMember(resident);
/*  886 */         mayorGroup.saveNow();
/*  887 */         newTown.setMayorGroup(mayorGroup);
/*      */         
/*  889 */         PermissionGroup assistantGroup = new PermissionGroup(newTown, "assistants");
/*  890 */         assistantGroup.saveNow();
/*  891 */         newTown.setAssistantGroup(assistantGroup);
/*      */       } catch (InvalidNameException e2) {
/*  893 */         e2.printStackTrace();
/*  894 */         throw new CivException("Internal naming error.");
/*      */       }
/*      */       PermissionGroup residentsGroup;
/*  897 */       ChunkCoord cl = new ChunkCoord(loc);
/*  898 */       TownChunk tc = new TownChunk(newTown, cl);
/*  899 */       tc.perms.addGroup(residentsGroup);
/*      */       try {
/*  901 */         newTown.addTownChunk(tc);
/*      */       } catch (AlreadyRegisteredException e1) {
/*  903 */         throw new CivException("Town already has this town chunk?");
/*      */       }
/*      */       
/*  906 */       tc.save();
/*  907 */       CivGlobal.addTownChunk(tc);
/*  908 */       civ.addTown(newTown);
/*      */       
/*      */       try
/*      */       {
/*  912 */         Location centerLoc = loc;
/*  913 */         if (capitol) {
/*  914 */           ConfigBuildableInfo buildableInfo = (ConfigBuildableInfo)CivSettings.structures.get("s_capitol");
/*  915 */           newTown.getTreasury().deposit(buildableInfo.cost);
/*  916 */           newTown.buildStructure(player, buildableInfo.id, centerLoc, resident.desiredTemplate);
/*      */         } else {
/*  918 */           ConfigBuildableInfo buildableInfo = (ConfigBuildableInfo)CivSettings.structures.get("s_townhall");
/*  919 */           newTown.getTreasury().deposit(buildableInfo.cost);
/*  920 */           newTown.buildStructure(player, buildableInfo.id, centerLoc, resident.desiredTemplate);
/*      */         }
/*      */       } catch (CivException e) {
/*  923 */         civ.removeTown(newTown);
/*  924 */         newTown.delete();
/*  925 */         throw e;
/*      */       }
/*      */       
/*  928 */       if (!free) {
/*  929 */         ItemStack newStack = new ItemStack(Material.AIR);
/*  930 */         player.setItemInHand(newStack);
/*  931 */         Unit.removeUnit(player);
/*      */       }
/*      */       try
/*      */       {
/*  935 */         if (resident.getTown() != null) {
/*  936 */           CivMessage.sendTown(resident.getTown(), resident.getName() + " has left the town.");
/*  937 */           resident.getTown().removeResident(resident);
/*      */         }
/*  939 */         newTown.addResident(resident);
/*      */       } catch (AlreadyRegisteredException e) {
/*  941 */         e.printStackTrace();
/*  942 */         throw new CivException("Internal error, resident already registered to this town but creating it?");
/*      */       }
/*  944 */       resident.saveNow();
/*      */       
/*  946 */       CivGlobal.processCulture();
/*  947 */       newTown.saveNow();
/*  948 */       return newTown;
/*      */     } catch (SQLException e2) {
/*  950 */       e2.printStackTrace();
/*  951 */       throw new CivException("Internal SQL Error.");
/*      */     }
/*      */   }
/*      */   
/*      */   public PermissionGroup getDefaultGroup() {
/*  956 */     return this.defaultGroup;
/*      */   }
/*      */   
/*      */   public void setDefaultGroup(PermissionGroup defaultGroup) {
/*  960 */     this.defaultGroup = defaultGroup;
/*  961 */     this.groups.put(defaultGroup.getName(), defaultGroup);
/*      */   }
/*      */   
/*      */   public Collection<PermissionGroup> getGroups() {
/*  965 */     return this.groups.values();
/*      */   }
/*      */   
/*      */   public PermissionGroup getGroup(String name) {
/*  969 */     return (PermissionGroup)this.groups.get(name);
/*      */   }
/*      */   
/*      */   public PermissionGroup getGroupFromId(Integer id) {
/*  973 */     for (PermissionGroup grp : this.groups.values()) {
/*  974 */       if (grp.getId() == id.intValue()) {
/*  975 */         return grp;
/*      */       }
/*      */     }
/*  978 */     return null;
/*      */   }
/*      */   
/*      */   public void addGroup(PermissionGroup grp)
/*      */   {
/*  983 */     if (grp.getName().equalsIgnoreCase(this.defaultGroupName)) {
/*  984 */       this.defaultGroup = grp;
/*  985 */     } else if (grp.getName().equalsIgnoreCase(this.mayorGroupName)) {
/*  986 */       this.mayorGroup = grp;
/*  987 */     } else if (grp.getName().equalsIgnoreCase(this.assistantGroupName)) {
/*  988 */       this.assistantGroup = grp;
/*      */     }
/*      */     
/*  991 */     this.groups.put(grp.getName(), grp);
/*      */   }
/*      */   
/*      */   public void removeGroup(PermissionGroup grp)
/*      */   {
/*  996 */     this.groups.remove(grp.getName());
/*      */   }
/*      */   
/*      */   public boolean hasGroupNamed(String name) {
/* 1000 */     for (PermissionGroup grp : this.groups.values()) {
/* 1001 */       if (grp.getName().equalsIgnoreCase(name)) {
/* 1002 */         return true;
/*      */       }
/*      */     }
/* 1005 */     return false;
/*      */   }
/*      */   
/*      */   public PermissionGroup getGroupByName(String name) {
/* 1009 */     for (PermissionGroup grp : this.groups.values()) {
/* 1010 */       if (grp.getName().equalsIgnoreCase(name)) {
/* 1011 */         return grp;
/*      */       }
/*      */     }
/* 1014 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDefaultGroupName()
/*      */   {
/* 1022 */     return "residents";
/*      */   }
/*      */   
/*      */   public PermissionGroup getMayorGroup() {
/* 1026 */     return this.mayorGroup;
/*      */   }
/*      */   
/*      */   public void setMayorGroup(PermissionGroup mayorGroup) {
/* 1030 */     this.mayorGroup = mayorGroup;
/* 1031 */     this.groups.put(mayorGroup.getName(), mayorGroup);
/*      */   }
/*      */   
/*      */   public PermissionGroup getAssistantGroup()
/*      */   {
/* 1036 */     return this.assistantGroup;
/*      */   }
/*      */   
/*      */   public void setAssistantGroup(PermissionGroup assistantGroup) {
/* 1040 */     this.assistantGroup = assistantGroup;
/* 1041 */     this.groups.put(assistantGroup.getName(), assistantGroup);
/*      */   }
/*      */   
/*      */   public String getMayorGroupName()
/*      */   {
/* 1046 */     return "mayors";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getAssistantGroupName()
/*      */   {
/* 1058 */     return "assistants";
/*      */   }
/*      */   
/*      */   public boolean isProtectedGroup(PermissionGroup grp) {
/* 1062 */     return grp.isProtectedGroup();
/*      */   }
/*      */   
/*      */   public boolean playerIsInGroupName(String groupName, Player player) {
/* 1066 */     PermissionGroup grp = getGroupByName(groupName);
/* 1067 */     if (grp == null) {
/* 1068 */       return false;
/*      */     }
/*      */     
/* 1071 */     return grp.hasMember(player.getName());
/*      */   }
/*      */   
/*      */   public EconObject getTreasury() {
/* 1075 */     return this.treasury;
/*      */   }
/*      */   
/*      */   public void depositDirect(double amount) {
/* 1079 */     this.treasury.deposit(amount);
/*      */   }
/*      */   
/*      */   public void depositTaxed(double amount)
/*      */   {
/* 1084 */     double taxAmount = amount * getDepositCiv().getIncomeTaxRate();
/* 1085 */     amount -= taxAmount;
/*      */     
/* 1087 */     if (getMotherCiv() != null)
/*      */     {
/*      */       try {
/* 1090 */         capturedPenalty = CivSettings.getDouble(CivSettings.warConfig, "war.captured_penalty");
/*      */       } catch (InvalidConfiguration e) { double capturedPenalty;
/* 1092 */         e.printStackTrace(); return;
/*      */       }
/*      */       
/*      */       double capturedPenalty;
/* 1096 */       double capturePayment = amount * capturedPenalty;
/* 1097 */       CivMessage.sendTown(this, "§eYour town paid " + (amount - capturePayment) + " coins due to being captured by " + getCiv().getName());
/* 1098 */       amount = capturePayment;
/*      */     }
/*      */     
/* 1101 */     this.treasury.deposit(amount);
/* 1102 */     getDepositCiv().taxPayment(this, taxAmount);
/*      */   }
/*      */   
/*      */   public void withdraw(double amount) {
/* 1106 */     this.treasury.withdraw(amount);
/*      */   }
/*      */   
/*      */   public boolean inDebt() {
/* 1110 */     return this.treasury.inDebt();
/*      */   }
/*      */   
/*      */   public double getDebt() {
/* 1114 */     return this.treasury.getDebt();
/*      */   }
/*      */   
/*      */   public void setDebt(double amount) {
/* 1118 */     this.treasury.setDebt(amount);
/*      */   }
/*      */   
/*      */   public double getBalance() {
/* 1122 */     return this.treasury.getBalance();
/*      */   }
/*      */   
/*      */   public boolean hasEnough(double amount) {
/* 1126 */     return this.treasury.hasEnough(amount);
/*      */   }
/*      */   
/*      */   public void setTreasury(EconObject treasury) {
/* 1130 */     this.treasury = treasury;
/*      */   }
/*      */   
/*      */   public String getLevelTitle() {
/* 1134 */     ConfigTownLevel clevel = (ConfigTownLevel)CivSettings.townLevels.get(Integer.valueOf(this.level));
/* 1135 */     if (clevel == null) {
/* 1136 */       return "Unknown";
/*      */     }
/* 1138 */     return clevel.title;
/*      */   }
/*      */   
/*      */   public void purchaseUpgrade(ConfigTownUpgrade upgrade) throws CivException
/*      */   {
/* 1143 */     if (!hasUpgrade(upgrade.require_upgrade)) {
/* 1144 */       throw new CivException("Town does not have the required upgrades to purchase this upgrade.");
/*      */     }
/*      */     
/* 1147 */     if (!getTreasury().hasEnough(upgrade.cost)) {
/* 1148 */       throw new CivException("The town does not have the required " + upgrade.cost + " coins.");
/*      */     }
/*      */     
/* 1151 */     if (!hasStructure(upgrade.require_structure)) {
/* 1152 */       throw new CivException("The town does not have the required structures to buy this upgrade.");
/*      */     }
/*      */     
/* 1155 */     getTreasury().withdraw(upgrade.cost);
/*      */     try
/*      */     {
/* 1158 */       upgrade.processAction(this);
/*      */     }
/*      */     catch (CivException e) {
/* 1161 */       getTreasury().deposit(upgrade.cost);
/* 1162 */       throw e;
/*      */     }
/*      */     
/* 1165 */     this.upgrades.put(upgrade.id, upgrade);
/* 1166 */     save();
/*      */   }
/*      */   
/*      */   public Structure findStructureByConfigId(String require_structure)
/*      */   {
/* 1171 */     for (Structure struct : this.structures.values()) {
/* 1172 */       if (struct.getConfigId().equals(require_structure)) {
/* 1173 */         return struct;
/*      */       }
/*      */     }
/*      */     
/* 1177 */     return null;
/*      */   }
/*      */   
/*      */   public ConcurrentHashMap<String, ConfigTownUpgrade> getUpgrades() {
/* 1181 */     return this.upgrades;
/*      */   }
/*      */   
/*      */   public boolean isPvp() {
/* 1185 */     return this.pvp;
/*      */   }
/*      */   
/*      */   public void setPvp(boolean pvp) {
/* 1189 */     this.pvp = pvp;
/*      */   }
/*      */   
/*      */   public String getPvpString() {
/* 1193 */     if (!getCiv().getDiplomacyManager().isAtWar()) {
/* 1194 */       if (this.pvp) {
/* 1195 */         return "§4[PvP]";
/*      */       }
/* 1197 */       return "§2[No PvP]";
/*      */     }
/*      */     
/* 1200 */     return "§4[WAR-PvP]";
/*      */   }
/*      */   
/*      */ 
/*      */   private void kickResident(Resident resident)
/*      */   {
/* 1206 */     for (TownChunk tc : this.townChunks.values()) {
/* 1207 */       if (tc.perms.getOwner() == resident) {
/* 1208 */         tc.perms.setOwner(null);
/* 1209 */         tc.perms.replaceGroups(this.defaultGroup);
/* 1210 */         tc.perms.resetPerms();
/* 1211 */         tc.save();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1216 */     resident.getTreasury().setDebt(0.0D);
/* 1217 */     resident.setDaysTilEvict(0);
/* 1218 */     resident.setTown(null);
/* 1219 */     resident.setRejoinCooldown(this);
/*      */     
/* 1221 */     this.residents.remove(resident.getName().toLowerCase());
/*      */     
/* 1223 */     resident.save();
/* 1224 */     save();
/*      */   }
/*      */   
/*      */   public double collectPlotTax() {
/* 1228 */     double total = 0.0D;
/* 1229 */     for (Resident resident : this.residents.values()) {
/* 1230 */       if (!resident.hasTown()) {
/* 1231 */         CivLog.warning("Resident in town list but doesnt have a town! Resident:" + resident.getName() + " town:" + getName());
/*      */ 
/*      */ 
/*      */       }
/* 1235 */       else if (!resident.isTaxExempt())
/*      */       {
/*      */ 
/* 1238 */         double tax = resident.getPropertyTaxOwed();
/* 1239 */         boolean wasInDebt = resident.getTreasury().inDebt();
/*      */         
/* 1241 */         total += resident.getTreasury().payToCreditor(getTreasury(), tax);
/*      */         
/* 1243 */         if ((resident.getTreasury().inDebt()) && (!wasInDebt)) {
/* 1244 */           resident.onEnterDebt();
/*      */         }
/*      */       }
/*      */     }
/* 1248 */     return total;
/*      */   }
/*      */   
/*      */   public double collectFlatTax() {
/* 1252 */     double total = 0.0D;
/* 1253 */     for (Resident resident : this.residents.values()) {
/* 1254 */       if (!resident.hasTown()) {
/* 1255 */         CivLog.warning("Resident in town list but doesnt have a town! Resident:" + resident.getName() + " town:" + getName());
/*      */ 
/*      */ 
/*      */       }
/* 1259 */       else if (!resident.isTaxExempt())
/*      */       {
/*      */ 
/* 1262 */         boolean wasInDebt = resident.getTreasury().inDebt();
/*      */         
/* 1264 */         total += resident.getTreasury().payToCreditor(getTreasury(), getFlatTax());
/*      */         
/* 1266 */         if ((resident.getTreasury().inDebt()) && (!wasInDebt)) {
/* 1267 */           resident.onEnterDebt();
/*      */         }
/*      */       }
/*      */     }
/* 1271 */     return total;
/*      */   }
/*      */   
/* 1274 */   public Collection<TownChunk> getTownChunks() { return this.townChunks.values(); }
/*      */   
/*      */   public void quicksave() throws CivException
/*      */   {
/* 1278 */     save();
/*      */   }
/*      */   
/*      */   public boolean isInGroup(String name, Resident resident) {
/* 1282 */     PermissionGroup grp = getGroupByName(name);
/* 1283 */     if ((grp != null) && 
/* 1284 */       (grp.hasMember(resident))) {
/* 1285 */       return true;
/*      */     }
/*      */     
/* 1288 */     return false;
/*      */   }
/*      */   
/*      */   public TownHall getTownHall() {
/* 1292 */     for (Structure struct : this.structures.values()) {
/* 1293 */       if ((struct instanceof TownHall)) {
/* 1294 */         return (TownHall)struct;
/*      */       }
/*      */     }
/* 1297 */     return null;
/*      */   }
/*      */   
/*      */   public double payUpkeep() throws InvalidConfiguration {
/* 1301 */     double upkeep = 0.0D;
/* 1302 */     upkeep += getBaseUpkeep();
/*      */     
/* 1304 */     upkeep += getStructureUpkeep();
/* 1305 */     upkeep += getOutpostUpkeep();
/*      */     
/* 1307 */     upkeep *= getGovernment().upkeep_rate;
/*      */     
/* 1309 */     if (getBuffManager().hasBuff("buff_colossus_reduce_upkeep")) {
/* 1310 */       upkeep -= upkeep * getBuffManager().getEffectiveDouble("buff_colossus_reduce_upkeep");
/*      */     }
/*      */     
/* 1313 */     if (getBuffManager().hasBuff("debuff_colossus_leech_upkeep")) {
/* 1314 */       double rate = getBuffManager().getEffectiveDouble("debuff_colossus_leech_upkeep");
/* 1315 */       double amount = upkeep * rate;
/*      */       
/* 1317 */       Wonder colossus = CivGlobal.getWonderByConfigId("w_colossus");
/* 1318 */       if (colossus != null) {
/* 1319 */         colossus.getTown().getTreasury().deposit(amount);
/*      */       } else {
/* 1321 */         CivLog.warning("Unable to find Colossus wonder but debuff for leech upkeep was present!");
/*      */         
/* 1323 */         getBuffManager().removeBuff("debuff_colossus_leech_upkeep");
/*      */       }
/*      */     }
/*      */     
/* 1327 */     if (getTreasury().hasEnough(upkeep)) {
/* 1328 */       getTreasury().withdraw(upkeep);
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1333 */       double diff = upkeep - getTreasury().getBalance();
/*      */       
/* 1335 */       if (isCapitol())
/*      */       {
/* 1337 */         if (getCiv().getTreasury().hasEnough(diff)) {
/* 1338 */           getCiv().getTreasury().withdraw(diff);
/*      */         } else {
/* 1340 */           diff -= getCiv().getTreasury().getBalance();
/* 1341 */           getCiv().getTreasury().setBalance(0.0D);
/* 1342 */           getCiv().getTreasury().setDebt(getCiv().getTreasury().getDebt() + diff);
/* 1343 */           getCiv().save();
/*      */         }
/*      */       } else {
/* 1346 */         getTreasury().setDebt(getTreasury().getDebt() + diff);
/*      */       }
/* 1348 */       getTreasury().withdraw(getTreasury().getBalance());
/*      */     }
/*      */     
/*      */ 
/* 1352 */     return upkeep;
/*      */   }
/*      */   
/*      */   public double getBaseUpkeep() {
/* 1356 */     ConfigTownLevel level = (ConfigTownLevel)CivSettings.townLevels.get(Integer.valueOf(this.level));
/* 1357 */     return level.upkeep;
/*      */   }
/*      */   
/*      */   public double getStructureUpkeep() {
/* 1361 */     double upkeep = 0.0D;
/*      */     
/* 1363 */     for (Structure struct : getStructures()) {
/* 1364 */       upkeep += struct.getUpkeepCost();
/*      */     }
/* 1366 */     return upkeep;
/*      */   }
/*      */   
/*      */   public void removeResident(Resident resident) {
/* 1370 */     this.residents.remove(resident.getName().toLowerCase());
/*      */     
/*      */ 
/* 1373 */     for (PermissionGroup group : this.groups.values()) {
/* 1374 */       if (group.hasMember(resident)) {
/* 1375 */         group.removeMember(resident);
/* 1376 */         group.save();
/*      */       }
/*      */     }
/*      */     
/* 1380 */     kickResident(resident);
/*      */   }
/*      */   
/*      */   public boolean canClaim()
/*      */   {
/* 1385 */     if (getMaxPlots() <= this.townChunks.size()) {
/* 1386 */       return false;
/*      */     }
/*      */     
/* 1389 */     return true;
/*      */   }
/*      */   
/*      */   public int getMaxPlots() {
/* 1393 */     ConfigTownLevel lvl = (ConfigTownLevel)CivSettings.townLevels.get(Integer.valueOf(this.level));
/* 1394 */     return lvl.plots;
/*      */   }
/*      */   
/*      */   public boolean hasUpgrade(String require_upgrade) {
/* 1398 */     if ((require_upgrade == null) || (require_upgrade.equals(""))) {
/* 1399 */       return true;
/*      */     }
/* 1401 */     return this.upgrades.containsKey(require_upgrade);
/*      */   }
/*      */   
/*      */   public boolean hasTechnology(String require_tech) {
/* 1405 */     return getCiv().hasTechnology(require_tech);
/*      */   }
/*      */   
/*      */   public String getDynmapDescription() {
/* 1409 */     String out = "";
/*      */     try {
/* 1411 */       out = out + "<h3><b>" + getName() + "</b> (<i>" + getCiv().getName() + "</i>)</h3>";
/* 1412 */       out = out + "<b>Mayors: " + getMayorGroup().getMembersString() + "</b>";
/*      */     } catch (Exception e) {
/* 1414 */       e.printStackTrace();
/*      */     }
/*      */     
/* 1417 */     return out;
/*      */   }
/*      */   
/*      */   public void removeCultureChunk(ChunkCoord coord) {
/* 1421 */     this.cultureChunks.remove(coord);
/*      */   }
/*      */   
/*      */   public void removeCultureChunk(CultureChunk cc) {
/* 1425 */     this.cultureChunks.remove(cc.getChunkCoord());
/*      */   }
/*      */   
/*      */   public void addCultureChunk(CultureChunk cc) {
/* 1429 */     this.cultureChunks.put(cc.getChunkCoord(), cc);
/*      */   }
/*      */   
/*      */ 
/*      */   public int getCultureLevel()
/*      */   {
/* 1435 */     int bestLevel = 0;
/* 1436 */     ConfigCultureLevel level = (ConfigCultureLevel)CivSettings.cultureLevels.get(Integer.valueOf(0));
/*      */     
/* 1438 */     while (this.culture >= level.amount) {
/* 1439 */       level = (ConfigCultureLevel)CivSettings.cultureLevels.get(Integer.valueOf(bestLevel + 1));
/* 1440 */       if (level == null) {
/* 1441 */         level = (ConfigCultureLevel)CivSettings.cultureLevels.get(Integer.valueOf(bestLevel));
/* 1442 */         break;
/*      */       }
/* 1444 */       bestLevel++;
/*      */     }
/*      */     
/* 1447 */     return level.level;
/*      */   }
/*      */   
/*      */   public Collection<CultureChunk> getCultureChunks() {
/* 1451 */     return this.cultureChunks.values();
/*      */   }
/*      */   
/*      */   public Object getCultureChunk(ChunkCoord coord) {
/* 1455 */     return this.cultureChunks.get(coord);
/*      */   }
/*      */   
/*      */   public void removeWonder(Buildable buildable) {
/* 1459 */     if (!buildable.isComplete()) {
/* 1460 */       removeBuildTask(buildable);
/*      */     }
/*      */     
/* 1463 */     if (this.currentWonderInProgress == buildable) {
/* 1464 */       this.currentWonderInProgress = null;
/*      */     }
/*      */     
/* 1467 */     this.wonders.remove(buildable.getCorner());
/*      */   }
/*      */   
/*      */   public void addWonder(Buildable buildable) {
/* 1471 */     if ((buildable instanceof Wonder)) {
/* 1472 */       this.wonders.put(buildable.getCorner(), (Wonder)buildable);
/*      */     }
/*      */   }
/*      */   
/*      */   public int getStructureTypeCount(String id) {
/* 1477 */     int count = 0;
/* 1478 */     for (Structure struct : this.structures.values()) {
/* 1479 */       if (struct.getConfigId().equalsIgnoreCase(id)) {
/* 1480 */         count++;
/*      */       }
/*      */     }
/* 1483 */     return count;
/*      */   }
/*      */   
/*      */   public void giveExtraHammers(double extra) {
/* 1487 */     if (this.build_tasks.size() == 0)
/*      */     {
/* 1489 */       this.extraHammers = extra;
/*      */     }
/*      */     else {
/* 1492 */       double hammers_per_task = extra / this.build_tasks.size();
/* 1493 */       double leftovers = 0.0D;
/*      */       
/* 1495 */       for (BuildAsyncTask task : this.build_tasks) {
/* 1496 */         leftovers += task.setExtraHammers(hammers_per_task);
/*      */       }
/*      */       
/* 1499 */       this.extraHammers = leftovers;
/*      */     }
/* 1501 */     save();
/*      */   }
/*      */   
/*      */   public void buildWonder(Player player, String id, Location center, Template tpl) throws CivException
/*      */   {
/* 1506 */     if (!center.getWorld().getName().equals("world")) {
/* 1507 */       throw new CivException("Can only build wonders in the overworld ... for now.");
/*      */     }
/*      */     
/* 1510 */     Wonder wonder = Wonder.newWonder(center, id, this);
/*      */     
/* 1512 */     if (!hasUpgrade(wonder.getRequiredUpgrade())) {
/* 1513 */       throw new CivException("We require an upgrade we do not have yet.");
/*      */     }
/*      */     
/* 1516 */     if (!hasTechnology(wonder.getRequiredTechnology())) {
/* 1517 */       throw new CivException("We don't have the technology yet.");
/*      */     }
/*      */     
/* 1520 */     if (!wonder.isAvailable()) {
/* 1521 */       throw new CivException("This wonder is not currently available.");
/*      */     }
/*      */     
/* 1524 */     wonder.canBuildHere(center, 7.0D);
/*      */     
/* 1526 */     if (!Wonder.isWonderAvailable(id)) {
/* 1527 */       throw new CivException("This wonder is already built somewhere else.");
/*      */     }
/*      */     
/* 1530 */     if (CivGlobal.isCasualMode()) {
/*      */       Iterator localIterator2;
/* 1532 */       for (Iterator localIterator1 = getCiv().getTowns().iterator(); localIterator1.hasNext(); 
/* 1533 */           localIterator2.hasNext())
/*      */       {
/* 1532 */         Town town = (Town)localIterator1.next();
/* 1533 */         localIterator2 = town.getWonders().iterator(); continue;Wonder w = (Wonder)localIterator2.next();
/* 1534 */         if (w.getConfigId().equals(id)) {
/* 1535 */           throw new CivException("Can only have one wonder of each type in your civilization in casual mode.");
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1541 */     double cost = wonder.getCost();
/* 1542 */     if (!getTreasury().hasEnough(cost)) {
/* 1543 */       throw new CivException("Your town cannot not afford the " + cost + " coins to build " + wonder.getDisplayName());
/*      */     }
/*      */     
/* 1546 */     wonder.runCheck(center);
/*      */     
/* 1548 */     Buildable inProgress = getCurrentStructureInProgress();
/* 1549 */     if (inProgress != null) {
/* 1550 */       throw new CivException("Your town is currently building a " + inProgress.getDisplayName() + " structure. Can only build one structure at a time.");
/*      */     }
/* 1552 */     inProgress = getCurrentWonderInProgress();
/* 1553 */     if (inProgress != null) {
/* 1554 */       throw new CivException("Your town is currently building " + inProgress.getDisplayName() + " and can only build one wonder at a time.");
/*      */     }
/*      */     
/*      */     try
/*      */     {
/* 1559 */       wonder.build(player, center, tpl);
/* 1560 */       if (getExtraHammers() > 0.0D) {
/* 1561 */         giveExtraHammers(getExtraHammers());
/*      */       }
/*      */     } catch (Exception e) {
/* 1564 */       if (CivGlobal.testFileFlag("debug")) {
/* 1565 */         e.printStackTrace();
/*      */       }
/* 1567 */       throw new CivException("Failed to build: " + e.getMessage());
/*      */     }
/*      */     
/* 1570 */     this.wonders.put(wonder.getCorner(), wonder);
/*      */     
/* 1572 */     getTreasury().withdraw(cost);
/* 1573 */     CivMessage.sendTown(this, "§eThe town has started construction on  " + wonder.getDisplayName());
/* 1574 */     save();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void buildStructure(Player player, String id, Location center, Template tpl)
/*      */     throws CivException
/*      */   {
/* 1583 */     Structure struct = Structure.newStructure(center, id, this);
/*      */     
/* 1585 */     if (!hasUpgrade(struct.getRequiredUpgrade())) {
/* 1586 */       throw new CivException("We require an upgrade we do not have yet.");
/*      */     }
/*      */     
/* 1589 */     if (!hasTechnology(struct.getRequiredTechnology())) {
/* 1590 */       throw new CivException("We don't have the technology yet.");
/*      */     }
/*      */     
/* 1593 */     if (!struct.isAvailable()) {
/* 1594 */       throw new CivException("This structure is not currently available.");
/*      */     }
/*      */     
/* 1597 */     struct.canBuildHere(center, 7.0D);
/*      */     
/* 1599 */     if ((struct.getLimit() != 0) && 
/* 1600 */       (getStructureTypeCount(id) >= struct.getLimit())) {
/* 1601 */       throw new CivException("Your town can only have " + struct.getLimit() + " " + struct.getDisplayName() + " structures.");
/*      */     }
/*      */     
/*      */ 
/* 1605 */     double cost = struct.getCost();
/* 1606 */     if (!getTreasury().hasEnough(cost)) {
/* 1607 */       throw new CivException("Your town cannot not afford the " + cost + " coins to build a " + struct.getDisplayName());
/*      */     }
/*      */     
/* 1610 */     struct.runCheck(center);
/*      */     
/* 1612 */     Buildable inProgress = getCurrentStructureInProgress();
/* 1613 */     if (inProgress != null) {
/* 1614 */       throw new CivException("Your town is currently building a " + inProgress.getDisplayName() + " and can only build one structure at a time.");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 1623 */       if (tpl == null) {
/*      */         try {
/* 1625 */           tpl = new Template();
/* 1626 */           tpl.initTemplate(center, struct);
/*      */         } catch (Exception e) {
/* 1628 */           throw e;
/*      */         }
/*      */       }
/*      */       
/* 1632 */       struct.build(player, center, tpl);
/* 1633 */       struct.save();
/*      */       
/*      */ 
/*      */ 
/* 1637 */       for (TownChunk tc : struct.townChunksToSave) {
/* 1638 */         tc.save();
/*      */       }
/* 1640 */       struct.townChunksToSave.clear();
/*      */       
/* 1642 */       if (getExtraHammers() > 0.0D) {
/* 1643 */         giveExtraHammers(getExtraHammers());
/*      */       }
/*      */     } catch (CivException e) {
/* 1646 */       throw new CivException("Failed to build: " + e.getMessage());
/*      */     } catch (Exception e) {
/* 1648 */       e.printStackTrace();
/* 1649 */       throw new CivException("Internal Error.");
/*      */     }
/*      */     
/* 1652 */     getTreasury().withdraw(cost);
/* 1653 */     CivMessage.sendTown(this, "§eThe town has started construction on a " + struct.getDisplayName());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1659 */     if ((struct instanceof TradeOutpost)) {
/* 1660 */       TradeOutpost outpost = (TradeOutpost)struct;
/* 1661 */       if (outpost.getGood() != null) {
/* 1662 */         outpost.getGood().save();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isStructureAddable(Structure struct)
/*      */   {
/* 1674 */     int count = getStructureTypeCount(struct.getConfigId());
/*      */     
/* 1676 */     if (struct.isTileImprovement()) {
/* 1677 */       ConfigTownLevel level = (ConfigTownLevel)CivSettings.townLevels.get(Integer.valueOf(getLevel()));
/* 1678 */       if (getTileImprovementCount() > level.tile_improvements) {
/* 1679 */         return false;
/*      */       }
/* 1681 */     } else if ((struct.getLimit() != 0) && (count > struct.getLimit())) {
/* 1682 */       return false;
/*      */     }
/*      */     
/* 1685 */     return true;
/*      */   }
/*      */   
/*      */   public void addStructure(Structure struct) {
/* 1689 */     this.structures.put(struct.getCorner(), struct);
/*      */     
/* 1691 */     if (!isStructureAddable(struct)) {
/* 1692 */       this.disabledBuildables.put(struct.getCorner(), struct);
/* 1693 */       struct.setEnabled(false);
/*      */     } else {
/* 1695 */       this.disabledBuildables.remove(struct.getCorner());
/* 1696 */       struct.setEnabled(true);
/*      */     }
/*      */   }
/*      */   
/*      */   public Structure getStructureByType(String id)
/*      */   {
/* 1702 */     for (Structure struct : this.structures.values()) {
/* 1703 */       if (struct.getConfigId().equalsIgnoreCase(id)) {
/* 1704 */         return struct;
/*      */       }
/*      */     }
/* 1707 */     return null;
/*      */   }
/*      */   
/*      */   public void loadUpgrades() throws CivException
/*      */   {
/* 1712 */     for (ConfigTownUpgrade upgrade : this.upgrades.values()) {
/*      */       try {
/* 1714 */         upgrade.processAction(this);
/*      */       }
/*      */       catch (CivException e) {
/* 1717 */         CivLog.warning("Loading upgrade generated exception:" + e.getMessage());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public Collection<Structure> getStructures()
/*      */   {
/* 1724 */     return this.structures.values();
/*      */   }
/*      */   
/*      */   public void processUndo() throws CivException {
/* 1728 */     if (this.lastBuildableBuilt == null) {
/* 1729 */       throw new CivException("Cannot undo, cannot find the last thing built.");
/*      */     }
/*      */     
/* 1732 */     if ((!(this.lastBuildableBuilt instanceof Wall)) && 
/* 1733 */       (!(this.lastBuildableBuilt instanceof Road))) {
/* 1734 */       throw new CivException("Only wall and road structures can be use build undo.");
/*      */     }
/*      */     
/* 1737 */     this.lastBuildableBuilt.processUndo();
/* 1738 */     this.structures.remove(this.lastBuildableBuilt.getCorner());
/* 1739 */     removeBuildTask(this.lastBuildableBuilt);
/* 1740 */     this.lastBuildableBuilt = null;
/*      */   }
/*      */   
/*      */   private void removeBuildTask(Buildable lastBuildableBuilt) {
/* 1744 */     for (BuildAsyncTask task : this.build_tasks) {
/* 1745 */       if (task.buildable == lastBuildableBuilt) {
/* 1746 */         this.build_tasks.remove(task);
/* 1747 */         task.abort();
/* 1748 */         return;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public Structure getStructure(BlockCoord coord) {
/* 1754 */     return (Structure)this.structures.get(coord);
/*      */   }
/*      */   
/*      */   public void demolish(Structure struct, boolean isAdmin) throws CivException
/*      */   {
/* 1759 */     if ((!struct.allowDemolish()) && (!isAdmin)) {
/* 1760 */       throw new CivException("Cannot demolish this structure. Please re-build it instead.");
/*      */     }
/*      */     try
/*      */     {
/* 1764 */       struct.onDemolish();
/* 1765 */       struct.unbindStructureBlocks();
/* 1766 */       removeStructure(struct);
/* 1767 */       struct.delete();
/*      */     } catch (SQLException e) {
/* 1769 */       e.printStackTrace();
/* 1770 */       throw new CivException("Internal database error.");
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean hasStructure(String require_structure) {
/* 1775 */     if ((require_structure == null) || (require_structure.equals(""))) {
/* 1776 */       return true;
/*      */     }
/*      */     
/* 1779 */     Structure struct = findStructureByConfigId(require_structure);
/* 1780 */     if ((struct != null) && (struct.isActive())) {
/* 1781 */       return true;
/*      */     }
/*      */     
/* 1784 */     return false;
/*      */   }
/*      */   
/*      */   public AttrSource getGrowthRate() {
/* 1788 */     double rate = 1.0D;
/* 1789 */     HashMap<String, Double> rates = new HashMap();
/*      */     
/* 1791 */     double newRate = rate * getGovernment().growth_rate;
/* 1792 */     rates.put("Government", Double.valueOf(newRate - rate));
/* 1793 */     rate = newRate;
/*      */     
/*      */ 
/* 1796 */     double additional = getBuffManager().getEffectiveDouble("buff_year_of_plenty");
/* 1797 */     additional += getBuffManager().getEffectiveDouble("buff_hanging_gardens_growth");
/*      */     
/* 1799 */     double additionalGrapes = getBuffManager().getEffectiveDouble("buff_hanging_gardens_additional_growth");
/* 1800 */     int grapeCount = 0;
/* 1801 */     for (BonusGoodie goodie : getBonusGoodies()) {
/* 1802 */       if (goodie.getDisplayName().equalsIgnoreCase("grapes")) {
/* 1803 */         grapeCount++;
/*      */       }
/*      */     }
/*      */     
/* 1807 */     additional += additionalGrapes * grapeCount;
/* 1808 */     rates.put("Wonders/Goodies", Double.valueOf(additional));
/* 1809 */     rate += additional;
/*      */     
/* 1811 */     return new AttrSource(rates, rate, null);
/*      */   }
/*      */   
/*      */   public AttrSource getGrowth() {
/* 1815 */     AttrCache cache = (AttrCache)this.attributeCache.get("GROWTH");
/* 1816 */     if (cache == null) {
/* 1817 */       cache = new AttrCache();
/* 1818 */       cache.lastUpdate = new Date();
/*      */     } else {
/* 1820 */       Date now = new Date();
/* 1821 */       if (now.getTime() > cache.lastUpdate.getTime() + 5000L) {
/* 1822 */         cache.lastUpdate = now;
/*      */       } else {
/* 1824 */         return cache.sources;
/*      */       }
/*      */     }
/*      */     
/* 1828 */     double total = 0.0D;
/* 1829 */     HashMap<String, Double> sources = new HashMap();
/*      */     
/*      */ 
/* 1832 */     double cultureSource = 0.0D;
/* 1833 */     for (CultureChunk cc : this.cultureChunks.values()) {
/* 1834 */       cultureSource += cc.getGrowth();
/*      */     }
/* 1836 */     sources.put("Culture Biomes", Double.valueOf(cultureSource));
/* 1837 */     total += cultureSource;
/*      */     
/*      */ 
/* 1840 */     double structures = 0.0D;
/* 1841 */     Iterator localIterator3; for (Iterator localIterator2 = this.structures.values().iterator(); localIterator2.hasNext(); 
/* 1842 */         localIterator3.hasNext())
/*      */     {
/* 1841 */       Structure struct = (Structure)localIterator2.next();
/* 1842 */       localIterator3 = struct.attachedComponents.iterator(); continue;Component comp = (Component)localIterator3.next();
/* 1843 */       if ((comp instanceof AttributeBase)) {
/* 1844 */         AttributeBase as = (AttributeBase)comp;
/* 1845 */         if (as.getString("attribute").equalsIgnoreCase("GROWTH")) {
/* 1846 */           double h = as.getGenerated();
/* 1847 */           structures += h;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1854 */     total += structures;
/* 1855 */     sources.put("Structures", Double.valueOf(structures));
/*      */     
/* 1857 */     sources.put("Base Growth", Double.valueOf(this.baseGrowth));
/* 1858 */     total += this.baseGrowth;
/*      */     
/* 1860 */     AttrSource rate = getGrowthRate();
/* 1861 */     total *= rate.total;
/*      */     
/* 1863 */     if (total < 0.0D) {
/* 1864 */       total = 0.0D;
/*      */     }
/*      */     
/* 1867 */     AttrSource as = new AttrSource(sources, total, rate);
/* 1868 */     cache.sources = as;
/* 1869 */     this.attributeCache.put("GROWTH", cache);
/* 1870 */     return as;
/*      */   }
/*      */   
/*      */   public double getCottageRate() {
/* 1874 */     double rate = getGovernment().cottage_rate;
/*      */     
/* 1876 */     double additional = rate * getBuffManager().getEffectiveDouble("buff_doesnotexist");
/* 1877 */     rate += additional;
/*      */     
/*      */ 
/* 1880 */     rate *= getHappinessState().coin_rate;
/* 1881 */     return rate;
/*      */   }
/*      */   
/*      */   public double getSpreadUpkeep() throws InvalidConfiguration {
/* 1885 */     double total = 0.0D;
/* 1886 */     double grace_distance = CivSettings.getDoubleTown("town.upkeep_town_block_grace_distance");
/* 1887 */     double base = CivSettings.getDoubleTown("town.upkeep_town_block_base");
/* 1888 */     double falloff = CivSettings.getDoubleTown("town.upkeep_town_block_falloff");
/*      */     
/* 1890 */     Structure townHall = getTownHall();
/* 1891 */     if (townHall == null) {
/* 1892 */       CivLog.error("No town hall for " + getName() + " while getting spread upkeep.");
/* 1893 */       return 0.0D;
/*      */     }
/*      */     
/* 1896 */     ChunkCoord townHallChunk = new ChunkCoord(townHall.getCorner().getLocation());
/*      */     
/* 1898 */     for (TownChunk tc : getTownChunks()) {
/* 1899 */       if (!tc.isOutpost())
/*      */       {
/*      */ 
/*      */ 
/* 1903 */         if (!tc.getChunkCoord().equals(townHallChunk))
/*      */         {
/*      */ 
/* 1906 */           double distance = tc.getChunkCoord().distance(townHallChunk);
/* 1907 */           if (distance > grace_distance) {
/* 1908 */             distance -= grace_distance;
/* 1909 */             double upkeep = base * Math.pow(distance, falloff);
/*      */             
/* 1911 */             total += upkeep;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1916 */     return Math.floor(total);
/*      */   }
/*      */   
/*      */   public double getTotalUpkeep() throws InvalidConfiguration {
/* 1920 */     return getBaseUpkeep() + getStructureUpkeep() + getSpreadUpkeep() + getOutpostUpkeep();
/*      */   }
/*      */   
/*      */   public double getTradeRate() {
/* 1924 */     double rate = getGovernment().trade_rate;
/*      */     
/*      */ 
/* 1927 */     double fromStructures = 0.0D;
/* 1928 */     Iterator localIterator2; for (Iterator localIterator1 = this.structures.values().iterator(); localIterator1.hasNext(); 
/* 1929 */         localIterator2.hasNext())
/*      */     {
/* 1928 */       Structure struct = (Structure)localIterator1.next();
/* 1929 */       localIterator2 = struct.attachedComponents.iterator(); continue;Component comp = (Component)localIterator2.next();
/* 1930 */       if ((comp instanceof AttributeRate)) {
/* 1931 */         AttributeRate as = (AttributeRate)comp;
/* 1932 */         if (as.getString("attribute").equalsIgnoreCase("TRADE")) {
/* 1933 */           fromStructures += as.getGenerated();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1939 */     rate += fromStructures;
/*      */     
/* 1941 */     double additional = rate * getBuffManager().getEffectiveDouble("buff_monopoly");
/* 1942 */     rate += additional;
/*      */     
/*      */ 
/* 1945 */     rate *= getHappinessState().coin_rate;
/* 1946 */     return rate;
/*      */   }
/*      */   
/*      */   public int getTileImprovementCount() {
/* 1950 */     int count = 0;
/* 1951 */     for (Structure struct : getStructures()) {
/* 1952 */       if (struct.isTileImprovement()) {
/* 1953 */         count++;
/*      */       }
/*      */     }
/* 1956 */     return count;
/*      */   }
/*      */   
/*      */   public void removeTownChunk(TownChunk tc) {
/* 1960 */     if (tc.isOutpost()) {
/* 1961 */       this.outposts.remove(tc.getChunkCoord());
/*      */     } else {
/* 1963 */       this.townChunks.remove(tc.getChunkCoord());
/*      */     }
/*      */   }
/*      */   
/*      */   public Double getHammersFromCulture() {
/* 1968 */     double hammers = 0.0D;
/* 1969 */     for (CultureChunk cc : this.cultureChunks.values()) {
/* 1970 */       hammers += cc.getHammers();
/*      */     }
/* 1972 */     return Double.valueOf(hammers);
/*      */   }
/*      */   
/*      */   public void setBonusGoodies(ConcurrentHashMap<String, BonusGoodie> bonusGoodies) {
/* 1976 */     this.bonusGoodies = bonusGoodies;
/*      */   }
/*      */   
/*      */   public Collection<BonusGoodie> getBonusGoodies() {
/* 1980 */     return this.bonusGoodies.values();
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
/*      */   public void removeUpgrade(ConfigTownUpgrade upgrade)
/*      */   {
/* 2007 */     this.upgrades.remove(upgrade.id);
/*      */   }
/*      */   
/*      */   public Structure getNearestStrucutre(Location location) {
/* 2011 */     Structure nearest = null;
/* 2012 */     double lowest_distance = Double.MAX_VALUE;
/*      */     
/* 2014 */     for (Structure struct : getStructures()) {
/* 2015 */       double distance = struct.getCenterLocation().getLocation().distance(location);
/* 2016 */       if (distance < lowest_distance) {
/* 2017 */         lowest_distance = distance;
/* 2018 */         nearest = struct;
/*      */       }
/*      */     }
/*      */     
/* 2022 */     return nearest;
/*      */   }
/*      */   
/*      */   public Buildable getNearestStrucutreOrWonderInprogress(Location location) {
/* 2026 */     Buildable nearest = null;
/* 2027 */     double lowest_distance = Double.MAX_VALUE;
/*      */     
/* 2029 */     for (Structure struct : getStructures()) {
/* 2030 */       double distance = struct.getCenterLocation().getLocation().distance(location);
/* 2031 */       if (distance < lowest_distance) {
/* 2032 */         lowest_distance = distance;
/* 2033 */         nearest = struct;
/*      */       }
/*      */     }
/*      */     
/* 2037 */     for (Wonder wonder : getWonders()) {
/* 2038 */       if (!wonder.isComplete())
/*      */       {
/*      */ 
/*      */ 
/* 2042 */         double distance = wonder.getCenterLocation().getLocation().distance(location);
/* 2043 */         if (distance < lowest_distance) {
/* 2044 */           lowest_distance = distance;
/* 2045 */           nearest = wonder;
/*      */         }
/*      */       }
/*      */     }
/* 2049 */     return nearest;
/*      */   }
/*      */   
/*      */   public void removeStructure(Structure structure) {
/* 2053 */     if (!structure.isComplete()) {
/* 2054 */       removeBuildTask(structure);
/*      */     }
/*      */     
/* 2057 */     if (this.currentStructureInProgress == structure) {
/* 2058 */       this.currentStructureInProgress = null;
/*      */     }
/*      */     
/* 2061 */     this.structures.remove(structure.getCorner());
/* 2062 */     this.invalidStructures.remove(structure);
/* 2063 */     this.disabledBuildables.remove(structure.getCorner());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public BuffManager getBuffManager()
/*      */   {
/* 2070 */     return this.buffManager;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setBuffManager(BuffManager buffManager)
/*      */   {
/* 2077 */     this.buffManager = buffManager;
/*      */   }
/*      */   
/*      */   public void repairStructure(Structure struct) throws CivException {
/* 2081 */     struct.repairStructure();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onDefeat(Civilization attackingCiv)
/*      */   {
/* 2089 */     if (getMotherCiv() == null)
/*      */     {
/* 2091 */       setMotherCiv(this.civ);
/*      */ 
/*      */     }
/* 2094 */     else if (getMotherCiv() == attackingCiv) {
/* 2095 */       setMotherCiv(null);
/*      */     }
/*      */     
/*      */ 
/* 2099 */     changeCiv(attackingCiv);
/* 2100 */     save();
/*      */   }
/*      */   
/*      */   public Civilization getDepositCiv()
/*      */   {
/* 2105 */     return getCiv();
/*      */   }
/*      */   
/*      */   public Collection<Wonder> getWonders() {
/* 2109 */     return this.wonders.values();
/*      */   }
/*      */   
/*      */   public void onGoodiePlaceIntoFrame(ItemFrameStorage framestore, BonusGoodie goodie) {
/* 2113 */     TownHall townhall = getTownHall();
/*      */     
/* 2115 */     if (townhall == null) {
/* 2116 */       return;
/*      */     }
/*      */     
/* 2119 */     for (ItemFrameStorage fs : townhall.getGoodieFrames()) {
/* 2120 */       if (fs == framestore) {
/* 2121 */         this.bonusGoodies.put(goodie.getOutpost().getCorner().toString(), goodie);
/* 2122 */         for (ConfigBuff cBuff : goodie.getConfigTradeGood().buffs.values()) {
/* 2123 */           String key = "tradegood:" + goodie.getOutpost().getCorner() + ":" + cBuff.id;
/*      */           
/* 2125 */           if (!this.buffManager.hasBuffKey(key))
/*      */           {
/*      */             try
/*      */             {
/*      */ 
/* 2130 */               this.buffManager.addBuff(key, cBuff.id, goodie.getDisplayName());
/*      */             } catch (CivException e) {
/* 2132 */               e.printStackTrace();
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2138 */     for (Structure struct : this.structures.values()) {
/* 2139 */       struct.onGoodieToFrame();
/*      */     }
/*      */     
/* 2142 */     for (Wonder wonder : this.wonders.values()) {
/* 2143 */       wonder.onGoodieToFrame();
/*      */     }
/*      */   }
/*      */   
/*      */   public void loadGoodiePlaceIntoFrame(TownHall townhall, BonusGoodie goodie)
/*      */   {
/* 2149 */     this.bonusGoodies.put(goodie.getOutpost().getCorner().toString(), goodie);
/* 2150 */     for (ConfigBuff cBuff : goodie.getConfigTradeGood().buffs.values()) {
/* 2151 */       String key = "tradegood:" + goodie.getOutpost().getCorner() + ":" + cBuff.id;
/*      */       
/* 2153 */       if (!this.buffManager.hasBuffKey(key))
/*      */       {
/*      */         try
/*      */         {
/*      */ 
/* 2158 */           this.buffManager.addBuff(key, cBuff.id, goodie.getDisplayName());
/*      */         } catch (CivException e) {
/* 2160 */           e.printStackTrace();
/*      */         } }
/*      */     }
/*      */   }
/*      */   
/*      */   public void removeGoodie(BonusGoodie goodie) {
/* 2166 */     this.bonusGoodies.remove(goodie.getOutpost().getCorner().toString());
/* 2167 */     for (ConfigBuff cBuff : goodie.getConfigTradeGood().buffs.values()) {
/* 2168 */       String key = "tradegood:" + goodie.getOutpost().getCorner() + ":" + cBuff.id;
/* 2169 */       this.buffManager.removeBuff(key);
/*      */     }
/* 2171 */     if (goodie.getFrame() != null) {
/* 2172 */       goodie.getFrame().clearItem();
/*      */     }
/*      */   }
/*      */   
/*      */   public void onGoodieRemoveFromFrame(ItemFrameStorage framestore, BonusGoodie goodie) {
/* 2177 */     TownHall townhall = getTownHall();
/*      */     
/* 2179 */     if (townhall == null) {
/* 2180 */       return;
/*      */     }
/*      */     
/* 2183 */     for (ItemFrameStorage fs : townhall.getGoodieFrames()) {
/* 2184 */       if (fs == framestore) {
/* 2185 */         removeGoodie(goodie);
/*      */       }
/*      */     }
/*      */     
/* 2189 */     for (Structure struct : this.structures.values()) {
/* 2190 */       struct.onGoodieFromFrame();
/*      */     }
/*      */     
/* 2193 */     for (Wonder wonder : this.wonders.values()) {
/* 2194 */       wonder.onGoodieToFrame();
/*      */     }
/*      */   }
/*      */   
/*      */   public int getUnitTypeCount(String id)
/*      */   {
/* 2200 */     return 0;
/*      */   }
/*      */   
/*      */   public ArrayList<ConfigUnit> getAvailableUnits() {
/* 2204 */     ArrayList<ConfigUnit> unitList = new ArrayList();
/*      */     
/* 2206 */     for (ConfigUnit unit : CivSettings.units.values()) {
/* 2207 */       if (unit.isAvailable(this)) {
/* 2208 */         unitList.add(unit);
/*      */       }
/*      */     }
/* 2211 */     return unitList;
/*      */   }
/*      */   
/*      */   public void onTechUpdate() {
/*      */     try {
/* 2216 */       for (Structure struct : this.structures.values()) {
/* 2217 */         if (struct.isActive()) {
/* 2218 */           struct.onTechUpdate();
/*      */         }
/*      */       }
/*      */       
/* 2222 */       for (Wonder wonder : this.wonders.values()) {
/* 2223 */         if (wonder.isActive()) {
/* 2224 */           wonder.onTechUpdate();
/*      */         }
/*      */       }
/*      */     } catch (Exception e) {
/* 2228 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   public Buildable getNearestBuildable(Location location)
/*      */   {
/* 2234 */     Buildable nearest = null;
/* 2235 */     double distance = Double.MAX_VALUE;
/*      */     
/* 2237 */     for (Structure struct : this.structures.values()) {
/* 2238 */       double tmp = location.distance(struct.getCenterLocation().getLocation());
/* 2239 */       if (tmp < distance) {
/* 2240 */         nearest = struct;
/* 2241 */         distance = tmp;
/*      */       }
/*      */     }
/*      */     
/* 2245 */     for (Wonder wonder : this.wonders.values()) {
/* 2246 */       double tmp = location.distance(wonder.getCenterLocation().getLocation());
/* 2247 */       if (tmp < distance) {
/* 2248 */         nearest = wonder;
/* 2249 */         distance = tmp;
/*      */       }
/*      */     }
/*      */     
/* 2253 */     return nearest;
/*      */   }
/*      */   
/*      */   public boolean isCapitol() {
/* 2257 */     if (getCiv().getCapitolName().equals(getName())) {
/* 2258 */       return true;
/*      */     }
/* 2260 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isForSale() {
/* 2264 */     if (getCiv().isTownsForSale()) {
/* 2265 */       return true;
/*      */     }
/*      */     
/* 2268 */     if (!inDebt()) {
/* 2269 */       return false;
/*      */     }
/*      */     
/* 2272 */     if (this.daysInDebt >= 7) {
/* 2273 */       return true;
/*      */     }
/*      */     
/* 2276 */     return false;
/*      */   }
/*      */   
/*      */   public double getForSalePrice() {
/* 2280 */     int points = getScore();
/*      */     try {
/* 2282 */       double coins_per_point = CivSettings.getDouble(CivSettings.scoreConfig, "coins_per_point");
/* 2283 */       return coins_per_point * points;
/*      */     } catch (InvalidConfiguration e) {
/* 2285 */       e.printStackTrace(); }
/* 2286 */     return 0.0D;
/*      */   }
/*      */   
/*      */   public int getScore()
/*      */   {
/* 2291 */     int points = 0;
/*      */     
/*      */ 
/* 2294 */     for (Structure struct : getStructures()) {
/* 2295 */       points += struct.getPoints();
/*      */     }
/*      */     
/*      */ 
/* 2299 */     for (Wonder wonder : getWonders()) {
/* 2300 */       points += wonder.getPoints();
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 2306 */       double perResident = CivSettings.getInteger(CivSettings.scoreConfig, "town_scores.resident").intValue();
/* 2307 */       points = (int)(points + perResident * getResidents().size());
/*      */       
/* 2309 */       double perTownChunk = CivSettings.getInteger(CivSettings.scoreConfig, "town_scores.town_chunk").intValue();
/* 2310 */       points = (int)(points + perTownChunk * getTownChunks().size());
/*      */       
/* 2312 */       double perCultureChunk = CivSettings.getInteger(CivSettings.scoreConfig, "town_scores.culture_chunk").intValue();
/* 2313 */       if (this.cultureChunks != null) {
/* 2314 */         points = (int)(points + perCultureChunk * this.cultureChunks.size());
/*      */       } else {
/* 2316 */         CivLog.warning("Town " + getName() + " has no culture chunks??");
/*      */       }
/*      */       
/* 2319 */       double coins_per_point = CivSettings.getInteger(CivSettings.scoreConfig, "coins_per_point").intValue();
/* 2320 */       points += (int)(getTreasury().getBalance() / coins_per_point);
/*      */     }
/*      */     catch (InvalidConfiguration e) {
/* 2323 */       e.printStackTrace();
/*      */     }
/*      */     
/* 2326 */     return points;
/*      */   }
/*      */   
/*      */   public void addOutpostChunk(TownChunk tc) throws AlreadyRegisteredException {
/* 2330 */     if (this.outposts.containsKey(tc.getChunkCoord())) {
/* 2331 */       throw new AlreadyRegisteredException("Outpost at " + tc.getChunkCoord() + " already registered to town " + getName());
/*      */     }
/* 2333 */     this.outposts.put(tc.getChunkCoord(), tc);
/*      */   }
/*      */   
/*      */   public Collection<TownChunk> getOutpostChunks() {
/* 2337 */     return this.outposts.values();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getOutpostUpkeep()
/*      */   {
/* 2349 */     return 0.0D;
/*      */   }
/*      */   
/*      */   public boolean isOutlaw(String name) {
/* 2353 */     return this.outlaws.contains(name);
/*      */   }
/*      */   
/*      */   public void addOutlaw(String name) {
/* 2357 */     this.outlaws.add(name);
/* 2358 */     TaskMaster.syncTask(new SyncUpdateTags(name, this.residents.values()));
/*      */   }
/*      */   
/*      */   public void removeOutlaw(String name) {
/* 2362 */     this.outlaws.remove(name);
/* 2363 */     TaskMaster.syncTask(new SyncUpdateTags(name, this.residents.values()));
/*      */   }
/*      */   
/*      */ 
/*      */   public void changeCiv(Civilization newCiv)
/*      */   {
/* 2369 */     Civilization oldCiv = this.civ;
/* 2370 */     oldCiv.removeTown(this);
/* 2371 */     oldCiv.save();
/*      */     
/*      */ 
/* 2374 */     newCiv.addTown(this);
/* 2375 */     newCiv.save();
/*      */     
/*      */ 
/* 2378 */     LinkedList<String> removeUs = new LinkedList();
/* 2379 */     for (String outlaw : this.outlaws) {
/* 2380 */       Resident resident = CivGlobal.getResident(outlaw);
/* 2381 */       if (newCiv.hasResident(resident)) {
/* 2382 */         removeUs.add(outlaw);
/*      */       }
/*      */     }
/*      */     
/* 2386 */     for (String outlaw : removeUs) {
/* 2387 */       this.outlaws.remove(outlaw);
/*      */     }
/*      */     
/* 2390 */     setCiv(newCiv);
/* 2391 */     CivGlobal.processCulture();
/*      */     
/* 2393 */     save();
/*      */   }
/*      */   
/*      */   public void validateResidentSelect(Resident resident) throws CivException {
/* 2397 */     if ((getMayorGroup() == null) || (getAssistantGroup() == null) || (getDefaultGroup() == null) || 
/* 2398 */       (getCiv().getLeaderGroup() == null) || (getAssistantGroup() == null)) {
/* 2399 */       throw new CivException("You cannot switch to this town, one of its protected groups could not be found. Contact an admin.");
/*      */     }
/*      */     
/* 2402 */     if ((!getMayorGroup().hasMember(resident)) && (!getAssistantGroup().hasMember(resident)) && (!getDefaultGroup().hasMember(resident)) && 
/* 2403 */       (!getCiv().getLeaderGroup().hasMember(resident)) && (!getCiv().getAdviserGroup().hasMember(resident))) {
/* 2404 */       throw new CivException("You do not have permission to select this town.");
/*      */     }
/*      */   }
/*      */   
/*      */   public void disband() {
/* 2409 */     getCiv().removeTown(this);
/*      */     try {
/* 2411 */       delete();
/*      */     } catch (SQLException e) {
/* 2413 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean touchesCapitolCulture(HashSet<Town> closedSet) {
/* 2418 */     if (isCapitol()) {
/* 2419 */       return true;
/*      */     }
/*      */     
/* 2422 */     closedSet.add(this);
/*      */     
/* 2424 */     for (Town t : this.townTouchList) {
/* 2425 */       if (!closedSet.contains(t))
/*      */       {
/*      */ 
/*      */ 
/* 2429 */         if (t.getCiv() == getCiv())
/*      */         {
/*      */ 
/*      */ 
/* 2433 */           boolean touches = t.touchesCapitolCulture(closedSet);
/* 2434 */           if (touches)
/* 2435 */             return true;
/*      */         }
/*      */       }
/*      */     }
/* 2439 */     return false;
/*      */   }
/*      */   
/*      */   public void incrementDaysInDebt() {
/* 2443 */     this.daysInDebt += 1;
/*      */     
/* 2445 */     if ((this.daysInDebt >= 7) && 
/* 2446 */       (this.daysInDebt >= 14)) {
/* 2447 */       disband();
/* 2448 */       CivMessage.global("The town of " + getName() + " could not pay its debts and has fell into ruin!");
/* 2449 */       return;
/*      */     }
/*      */     
/*      */ 
/* 2453 */     CivMessage.global("Town of " + getName() + " is in debt! " + getDaysLeftWarning());
/*      */   }
/*      */   
/*      */   public String getDaysLeftWarning()
/*      */   {
/* 2458 */     if (this.daysInDebt < 7) {
/* 2459 */       return 7 - this.daysInDebt + " days until town goes up for sale.";
/*      */     }
/*      */     
/* 2462 */     if (this.daysInDebt < 14) {
/* 2463 */       return getName() + " is up for sale, " + (14 - this.daysInDebt) + " days until the town is deleted!";
/*      */     }
/*      */     
/* 2466 */     return "";
/*      */   }
/*      */   
/*      */   public int getDaysInDebt() {
/* 2470 */     return this.daysInDebt;
/*      */   }
/*      */   
/*      */   public void setDaysInDebt(int daysInDebt) {
/* 2474 */     this.daysInDebt = daysInDebt;
/*      */   }
/*      */   
/*      */   public void depositFromResident(Double amount, Resident resident) throws CivException {
/* 2478 */     if (!resident.getTreasury().hasEnough(amount.doubleValue())) {
/* 2479 */       throw new CivException("You do not have enough coins for that.");
/*      */     }
/*      */     
/* 2482 */     if (inDebt()) {
/* 2483 */       if (getDebt() > amount.doubleValue()) {
/* 2484 */         getTreasury().setDebt(getTreasury().getDebt() - amount.doubleValue());
/* 2485 */         resident.getTreasury().withdraw(amount.doubleValue());
/*      */       } else {
/* 2487 */         double leftAmount = amount.doubleValue() - getTreasury().getDebt();
/* 2488 */         getTreasury().setDebt(0.0D);
/* 2489 */         getTreasury().deposit(leftAmount);
/* 2490 */         resident.getTreasury().withdraw(amount.doubleValue());
/*      */       }
/*      */       
/* 2493 */       if (!getTreasury().inDebt()) {
/* 2494 */         this.daysInDebt = 0;
/* 2495 */         CivMessage.global("Town of " + getName() + " is no longer in debt.");
/*      */       }
/*      */     } else {
/* 2498 */       getTreasury().deposit(amount.doubleValue());
/* 2499 */       resident.getTreasury().withdraw(amount.doubleValue());
/*      */     }
/*      */     
/* 2502 */     save();
/*      */   }
/*      */   
/*      */   public Civilization getMotherCiv() {
/* 2506 */     return this.motherCiv;
/*      */   }
/*      */   
/*      */   public void setMotherCiv(Civilization motherCiv) {
/* 2510 */     this.motherCiv = motherCiv;
/*      */   }
/*      */   
/*      */   public Collection<Resident> getOnlineResidents()
/*      */   {
/* 2515 */     LinkedList<Resident> residents = new LinkedList();
/* 2516 */     for (Resident resident : getResidents()) {
/*      */       try {
/* 2518 */         CivGlobal.getPlayer(resident);
/* 2519 */         residents.add(resident);
/*      */       }
/*      */       catch (CivException localCivException) {}
/*      */     }
/*      */     
/*      */ 
/* 2525 */     for (Resident resident : this.fakeResidents.values()) {
/* 2526 */       residents.add(resident);
/*      */     }
/*      */     
/* 2529 */     return residents;
/*      */   }
/*      */   
/*      */   public void capitulate() {
/* 2533 */     if (getMotherCiv() == null) {
/* 2534 */       return;
/*      */     }
/*      */     
/* 2537 */     if (getMotherCiv().getCapitolName().equals(getName())) {
/* 2538 */       getMotherCiv().capitulate();
/* 2539 */       return;
/*      */     }
/*      */     
/*      */ 
/* 2543 */     setMotherCiv(null);
/* 2544 */     save();
/* 2545 */     CivMessage.global("The conquered town of " + getName() + " has capitualted to " + getCiv().getName() + " and can no longer revolt.");
/*      */   }
/*      */   
/*      */   public ConfigGovernment getGovernment() {
/* 2549 */     if (getCiv().getGovernment().id.equals("gov_anarchy")) {
/* 2550 */       if ((this.motherCiv != null) && (!this.motherCiv.getGovernment().id.equals("gov_anarchy"))) {
/* 2551 */         return this.motherCiv.getGovernment();
/*      */       }
/*      */       
/* 2554 */       if (this.motherCiv != null) {
/* 2555 */         return (ConfigGovernment)CivSettings.governments.get("gov_tribalism");
/*      */       }
/*      */     }
/*      */     
/* 2559 */     return getCiv().getGovernment();
/*      */   }
/*      */   
/*      */   public AttrSource getBeakerRate()
/*      */   {
/* 2564 */     double rate = 1.0D;
/* 2565 */     HashMap<String, Double> rates = new HashMap();
/*      */     
/* 2567 */     ConfigHappinessState state = getHappinessState();
/* 2568 */     double newRate = rate * state.beaker_rate;
/* 2569 */     rates.put("Happiness", Double.valueOf(newRate - rate));
/* 2570 */     rate = newRate;
/*      */     
/* 2572 */     newRate = rate * getGovernment().beaker_rate;
/* 2573 */     rates.put("Government", Double.valueOf(newRate - rate));
/* 2574 */     rate = newRate;
/*      */     
/*      */ 
/*      */ 
/* 2578 */     double additional = rate * getBuffManager().getEffectiveDouble("buff_innovation");
/* 2579 */     additional += rate * getBuffManager().getEffectiveDouble("buff_greatlibrary_extra_beakers");
/* 2580 */     rate += additional;
/* 2581 */     rates.put("Goodies/Wonders", Double.valueOf(additional));
/*      */     
/* 2583 */     return new AttrSource(rates, rate, null);
/*      */   }
/*      */   
/*      */   public AttrSource getBeakers() {
/* 2587 */     AttrCache cache = (AttrCache)this.attributeCache.get("BEAKERS");
/* 2588 */     if (cache == null) {
/* 2589 */       cache = new AttrCache();
/* 2590 */       cache.lastUpdate = new Date();
/*      */     } else {
/* 2592 */       Date now = new Date();
/* 2593 */       if (now.getTime() > cache.lastUpdate.getTime() + 5000L) {
/* 2594 */         cache.lastUpdate = now;
/*      */       } else {
/* 2596 */         return cache.sources;
/*      */       }
/*      */     }
/*      */     
/* 2600 */     double beakers = 0.0D;
/* 2601 */     HashMap<String, Double> sources = new HashMap();
/*      */     
/*      */ 
/* 2604 */     double fromCulture = 0.0D;
/* 2605 */     for (CultureChunk cc : this.cultureChunks.values()) {
/* 2606 */       fromCulture += cc.getBeakers();
/*      */     }
/* 2608 */     sources.put("Culture Biomes", Double.valueOf(fromCulture));
/* 2609 */     beakers += fromCulture;
/*      */     
/*      */ 
/* 2612 */     double fromStructures = 0.0D;
/* 2613 */     Iterator localIterator3; for (Iterator localIterator2 = this.structures.values().iterator(); localIterator2.hasNext(); 
/* 2614 */         localIterator3.hasNext())
/*      */     {
/* 2613 */       Structure struct = (Structure)localIterator2.next();
/* 2614 */       localIterator3 = struct.attachedComponents.iterator(); continue;Component comp = (Component)localIterator3.next();
/* 2615 */       if ((comp instanceof AttributeBase)) {
/* 2616 */         AttributeBase as = (AttributeBase)comp;
/* 2617 */         if (as.getString("attribute").equalsIgnoreCase("BEAKERS")) {
/* 2618 */           fromStructures += as.getGenerated();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2624 */     beakers += fromStructures;
/* 2625 */     sources.put("Structures", Double.valueOf(fromStructures));
/*      */     
/*      */ 
/* 2628 */     double wondersTrade = 0.0D;
/*      */     
/*      */ 
/*      */ 
/* 2632 */     beakers += wondersTrade;
/* 2633 */     sources.put("Goodies/Wonders", Double.valueOf(wondersTrade));
/*      */     
/*      */ 
/* 2636 */     beakers = Math.max(beakers, 0.0D);
/* 2637 */     AttrSource rates = getBeakerRate();
/* 2638 */     beakers *= rates.total;
/*      */     
/* 2640 */     if (beakers < 0.0D) {
/* 2641 */       beakers = 0.0D;
/*      */     }
/*      */     
/* 2644 */     AttrSource as = new AttrSource(sources, beakers, null);
/* 2645 */     cache.sources = as;
/* 2646 */     this.attributeCache.put("BEAKERS", cache);
/* 2647 */     return as;
/*      */   }
/*      */   
/*      */ 
/*      */   public AttrSource getHappiness()
/*      */   {
/* 2653 */     HashMap<String, Double> sources = new HashMap();
/* 2654 */     double total = 0.0D;
/*      */     
/* 2656 */     AttrCache cache = (AttrCache)this.attributeCache.get("HAPPINESS");
/* 2657 */     if (cache == null) {
/* 2658 */       cache = new AttrCache();
/* 2659 */       cache.lastUpdate = new Date();
/*      */     } else {
/* 2661 */       Date now = new Date();
/* 2662 */       if (now.getTime() > cache.lastUpdate.getTime() + 5000L) {
/* 2663 */         cache.lastUpdate = now;
/*      */       } else {
/* 2665 */         return cache.sources;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2670 */     double townlevel = ((ConfigTownHappinessLevel)CivSettings.townHappinessLevels.get(Integer.valueOf(getLevel()))).happiness;
/* 2671 */     total += townlevel;
/* 2672 */     sources.put("Base Happiness", Double.valueOf(townlevel));
/*      */     
/*      */ 
/* 2675 */     double goodiesWonders = this.buffManager.getEffectiveDouble("buff_hedonism");
/* 2676 */     sources.put("Goodies/Wonders", Double.valueOf(goodiesWonders));
/* 2677 */     total += goodiesWonders;
/*      */     
/*      */ 
/* 2680 */     int tradeGoods = this.bonusGoodies.size();
/* 2681 */     if (tradeGoods > 0) {
/* 2682 */       sources.put("Trade Goods", Double.valueOf(tradeGoods));
/*      */     }
/* 2684 */     total += tradeGoods;
/*      */     
/*      */ 
/* 2687 */     if (this.baseHappy != 0.0D) {
/* 2688 */       sources.put("Base Happiness", Double.valueOf(this.baseHappy));
/* 2689 */       total += this.baseHappy;
/*      */     }
/*      */     
/*      */ 
/* 2693 */     double fromCulture = 0.0D;
/* 2694 */     for (CultureChunk cc : this.cultureChunks.values()) {
/* 2695 */       fromCulture += cc.getHappiness();
/*      */     }
/* 2697 */     sources.put("Culture Biomes", Double.valueOf(fromCulture));
/* 2698 */     total += fromCulture;
/*      */     
/*      */ 
/* 2701 */     double structures = 0.0D;
/* 2702 */     Iterator localIterator3; for (Iterator localIterator2 = this.structures.values().iterator(); localIterator2.hasNext(); 
/* 2703 */         localIterator3.hasNext())
/*      */     {
/* 2702 */       Structure struct = (Structure)localIterator2.next();
/* 2703 */       localIterator3 = struct.attachedComponents.iterator(); continue;Component comp = (Component)localIterator3.next();
/* 2704 */       if ((comp instanceof AttributeBase)) {
/* 2705 */         AttributeBase as = (AttributeBase)comp;
/* 2706 */         if (as.getString("attribute").equalsIgnoreCase("HAPPINESS")) {
/* 2707 */           double h = as.getGenerated();
/* 2708 */           if (h > 0.0D) {
/* 2709 */             structures += h;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2715 */     total += structures;
/* 2716 */     sources.put("Structures", Double.valueOf(structures));
/*      */     
/* 2718 */     if (total < 0.0D) {
/* 2719 */       total = 0.0D;
/*      */     }
/*      */     
/* 2722 */     double randomEvent = RandomEvent.getHappiness(this);
/* 2723 */     total += randomEvent;
/* 2724 */     sources.put("Random Events", Double.valueOf(randomEvent));
/*      */     
/*      */ 
/*      */ 
/* 2728 */     AttrSource as = new AttrSource(sources, total, null);
/* 2729 */     cache.sources = as;
/* 2730 */     this.attributeCache.put("HAPPINESS", cache);
/* 2731 */     return as;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public AttrSource getUnhappiness()
/*      */   {
/* 2739 */     AttrCache cache = (AttrCache)this.attributeCache.get("UNHAPPINESS");
/* 2740 */     if (cache == null) {
/* 2741 */       cache = new AttrCache();
/* 2742 */       cache.lastUpdate = new Date();
/*      */     } else {
/* 2744 */       Date now = new Date();
/* 2745 */       if (now.getTime() > cache.lastUpdate.getTime() + 5000L) {
/* 2746 */         cache.lastUpdate = now;
/*      */       } else {
/* 2748 */         return cache.sources;
/*      */       }
/*      */     }
/*      */     
/* 2752 */     HashMap<String, Double> sources = new HashMap();
/*      */     
/*      */ 
/* 2755 */     double total = getCiv().getCivWideUnhappiness(sources);
/*      */     
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 2761 */       per_resident = CivSettings.getDouble(CivSettings.happinessConfig, "happiness.per_resident");
/*      */     } catch (InvalidConfiguration e) { double per_resident;
/* 2763 */       e.printStackTrace();
/* 2764 */       return null; }
/*      */     double per_resident;
/* 2766 */     double happy_resident = per_resident * getResidents().size();
/* 2767 */     sources.put("Residents", Double.valueOf(happy_resident));
/* 2768 */     total += happy_resident;
/*      */     
/*      */     double value;
/* 2771 */     if (sources.containsKey("War")) {
/* 2772 */       for (Iterator localIterator1 = this.structures.values().iterator(); localIterator1.hasNext(); 
/* 2773 */           localIterator2.hasNext())
/*      */       {
/* 2772 */         Structure struct = (Structure)localIterator1.next();
/* 2773 */         localIterator2 = struct.attachedComponents.iterator(); continue;Component comp = (Component)localIterator2.next();
/* 2774 */         if (comp.isActive())
/*      */         {
/*      */ 
/*      */ 
/* 2778 */           if ((comp instanceof AttributeWarUnhappiness)) {
/* 2779 */             AttributeWarUnhappiness warunhappyComp = (AttributeWarUnhappiness)comp;
/* 2780 */             value = ((Double)sources.get("War")).doubleValue();
/* 2781 */             value += warunhappyComp.value;
/*      */             
/* 2783 */             if (value < 0.0D) {
/* 2784 */               value = 0.0D;
/*      */             }
/*      */             
/* 2787 */             sources.put("War", Double.valueOf(value));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2794 */     if ((getMotherCiv() == null) && (!isCapitol())) {
/* 2795 */       double distance_unhappy = getCiv().getDistanceHappiness(this);
/* 2796 */       total += distance_unhappy;
/* 2797 */       sources.put("Distance To Capitol", Double.valueOf(distance_unhappy));
/*      */     }
/*      */     
/*      */ 
/* 2801 */     if (this.baseUnhappy != 0.0D) {
/* 2802 */       sources.put("Base Unhappiness", Double.valueOf(this.baseUnhappy));
/* 2803 */       total += this.baseUnhappy;
/*      */     }
/*      */     
/*      */ 
/* 2807 */     double structures = 0.0D;
/* 2808 */     for (Iterator localIterator2 = this.structures.values().iterator(); localIterator2.hasNext(); 
/* 2809 */         value.hasNext())
/*      */     {
/* 2808 */       Structure struct = (Structure)localIterator2.next();
/* 2809 */       value = struct.attachedComponents.iterator(); continue;Component comp = (Component)value.next();
/* 2810 */       if ((comp instanceof AttributeBase)) {
/* 2811 */         AttributeBase as = (AttributeBase)comp;
/* 2812 */         if (as.getString("attribute").equalsIgnoreCase("HAPPINESS")) {
/* 2813 */           double h = as.getGenerated();
/* 2814 */           if (h < 0.0D) {
/* 2815 */             structures += h * -1.0D;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2821 */     total += structures;
/* 2822 */     sources.put("Structures", Double.valueOf(structures));
/*      */     
/*      */ 
/* 2825 */     double randomEvent = RandomEvent.getUnhappiness(this);
/* 2826 */     total += randomEvent;
/* 2827 */     if (randomEvent > 0.0D) {
/* 2828 */       sources.put("Random Events", Double.valueOf(randomEvent));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2835 */     if (total < 0.0D) {
/* 2836 */       total = 0.0D;
/*      */     }
/*      */     
/* 2839 */     AttrSource as = new AttrSource(sources, total, null);
/* 2840 */     cache.sources = as;
/* 2841 */     this.attributeCache.put("UNHAPPINESS", cache);
/* 2842 */     return as;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public double getHappinessModifier()
/*      */   {
/* 2849 */     return 1.0D;
/*      */   }
/*      */   
/*      */   public double getHappinessPercentage() {
/* 2853 */     double total_happiness = getHappiness().total;
/* 2854 */     double total_unhappiness = getUnhappiness().total;
/*      */     
/* 2856 */     double total = total_happiness + total_unhappiness;
/* 2857 */     return total_happiness / total;
/*      */   }
/*      */   
/*      */   public ConfigHappinessState getHappinessState() {
/* 2861 */     return CivSettings.getHappinessState(getHappinessPercentage());
/*      */   }
/*      */   
/*      */   public void setBaseHappiness(double happy) {
/* 2865 */     this.baseHappy = happy;
/*      */   }
/*      */   
/*      */   public void setBaseUnhappy(double happy) {
/* 2869 */     this.baseUnhappy = happy;
/*      */   }
/*      */   
/*      */   public double getBaseGrowth() {
/* 2873 */     return this.baseGrowth;
/*      */   }
/*      */   
/*      */   public void setBaseGrowth(double baseGrowth) {
/* 2877 */     this.baseGrowth = baseGrowth;
/*      */   }
/*      */   
/*      */   public Buildable getCurrentStructureInProgress() {
/* 2881 */     return this.currentStructureInProgress;
/*      */   }
/*      */   
/*      */   public void setCurrentStructureInProgress(Buildable currentStructureInProgress) {
/* 2885 */     this.currentStructureInProgress = currentStructureInProgress;
/*      */   }
/*      */   
/*      */   public Buildable getCurrentWonderInProgress() {
/* 2889 */     return this.currentWonderInProgress;
/*      */   }
/*      */   
/*      */   public void setCurrentWonderInProgress(Buildable currentWonderInProgress) {
/* 2893 */     this.currentWonderInProgress = currentWonderInProgress;
/*      */   }
/*      */   
/*      */   public void addFakeResident(Resident fake) {
/* 2897 */     this.fakeResidents.put(fake.getName(), fake);
/*      */   }
/*      */   
/* 2900 */   private static String lastMessage = null;
/*      */   
/* 2902 */   public boolean processSpyExposure(Resident resident) { double exposure = resident.getSpyExposure();
/* 2903 */     double percent = exposure / Resident.MAX_SPY_EXPOSURE;
/* 2904 */     boolean failed = false;
/*      */     
/*      */     try
/*      */     {
/* 2908 */       player = CivGlobal.getPlayer(resident);
/*      */     } catch (CivException e1) { Player player;
/* 2910 */       e1.printStackTrace();
/* 2911 */       return failed;
/*      */     }
/*      */     Player player;
/* 2914 */     String message = "";
/*      */     try
/*      */     {
/* 2917 */       if (percent >= CivSettings.getDouble(CivSettings.espionageConfig, "espionage.town_exposure_failure")) {
/* 2918 */         failed = true;
/* 2919 */         CivMessage.sendTown(this, "§e" + CivColor.BOLD + "The enemy spy mission has been thwarted by our defenses.");
/* 2920 */         return failed;
/*      */       }
/*      */       
/* 2923 */       if (percent > CivSettings.getDouble(CivSettings.espionageConfig, "espionage.town_exposure_warning")) {
/* 2924 */         message = message + "We're being spied on! ";
/*      */       }
/*      */       
/* 2927 */       if (percent > CivSettings.getDouble(CivSettings.espionageConfig, "espionage.town_exposure_location")) {
/* 2928 */         message = message + "Scouts report activity around " + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + " ";
/*      */       }
/*      */       
/* 2931 */       if (percent > CivSettings.getDouble(CivSettings.espionageConfig, "espionage.town_exposure_name")) {
/* 2932 */         message = message + "The spy is " + resident.getName() + "!";
/*      */       }
/*      */       
/* 2935 */       if ((message.length() > 0) && (
/* 2936 */         (lastMessage == null) || (!lastMessage.equals(message)))) {
/* 2937 */         CivMessage.sendTown(this, "§e" + CivColor.BOLD + message);
/* 2938 */         lastMessage = message;
/*      */       }
/*      */     }
/*      */     catch (InvalidConfiguration e) {
/* 2942 */       e.printStackTrace();
/*      */     }
/*      */     
/* 2945 */     return failed;
/*      */   }
/*      */   
/*      */   public ArrayList<Perk> getTemplatePerks(Buildable buildable, Resident resident, ConfigBuildableInfo info) {
/* 2949 */     ArrayList<Perk> perks = CustomTemplate.getTemplatePerksForBuildable(this, buildable.getTemplateBaseName());
/*      */     
/* 2951 */     for (Perk perk : resident.getPersonalTemplatePerks(info)) {
/* 2952 */       perks.add(perk);
/*      */     }
/*      */     
/* 2955 */     return perks;
/*      */   }
/*      */   
/*      */   public RandomEvent getActiveEvent() {
/* 2959 */     return this.activeEvent;
/*      */   }
/*      */   
/*      */   public void setActiveEvent(RandomEvent activeEvent) {
/* 2963 */     this.activeEvent = activeEvent;
/*      */   }
/*      */   
/*      */   public double getUnusedBeakers() {
/* 2967 */     return this.unusedBeakers;
/*      */   }
/*      */   
/*      */   public void setUnusedBeakers(double unusedBeakers) {
/* 2971 */     this.unusedBeakers = unusedBeakers;
/*      */   }
/*      */   
/*      */   public void addUnusedBeakers(double more) {
/* 2975 */     this.unusedBeakers += more;
/*      */   }
/*      */   
/*      */   public void markLastBuildableRefeshAsNow() {
/* 2979 */     this.lastBuildableRefresh = new Date();
/*      */   }
/*      */   
/*      */   public void refreshNearestBuildable(Resident resident) throws CivException {
/* 2983 */     if (!getMayorGroup().hasMember(resident)) {
/* 2984 */       throw new CivException("You must be the mayor in order to refresh a building.");
/*      */     }
/*      */     
/* 2987 */     if (this.lastBuildableRefresh != null) {
/* 2988 */       Date now = new Date();
/*      */       try
/*      */       {
/* 2991 */         buildable_refresh_cooldown = CivSettings.getInteger(CivSettings.townConfig, "town.buildable_refresh_cooldown").intValue();
/*      */       } catch (InvalidConfiguration e) { int buildable_refresh_cooldown;
/* 2993 */         e.printStackTrace();
/* 2994 */         throw new CivException("Internal error");
/*      */       }
/*      */       int buildable_refresh_cooldown;
/* 2997 */       if (now.getTime() < this.lastBuildableRefresh.getTime() + buildable_refresh_cooldown * 60 * 1000) {
/* 2998 */         throw new CivException("You must wait " + buildable_refresh_cooldown + " mins before you can refresh another building.");
/*      */       }
/*      */     }
/*      */     
/* 3002 */     Player player = CivGlobal.getPlayer(resident);
/* 3003 */     Buildable buildable = CivGlobal.getNearestBuildable(player.getLocation());
/* 3004 */     if (buildable == null) {
/* 3005 */       throw new CivException("Couldnt find a nearby structure or wonder.");
/*      */     }
/*      */     
/* 3008 */     if (!buildable.isActive()) {
/* 3009 */       throw new CivException("You cannot refresh buildings that are in progress, disabled, or destroyed.");
/*      */     }
/*      */     
/* 3012 */     if (War.isWarTime()) {
/* 3013 */       throw new CivException("You cannot refresh buildings during WarTime.");
/*      */     }
/*      */     
/* 3016 */     if (buildable.getTown() != this) {
/* 3017 */       throw new CivException("You cannot refresh buildings in towns that are not your own.");
/*      */     }
/*      */     
/* 3020 */     resident.setInteractiveMode(new InteractiveBuildableRefresh(buildable, resident.getName()));
/*      */   }
/*      */   
/*      */   public boolean areMayorsInactive()
/*      */   {
/*      */     try
/*      */     {
/* 3027 */       int mayor_inactive_days = CivSettings.getInteger(CivSettings.townConfig, "town.mayor_inactive_days").intValue();
/* 3028 */       for (Resident resident : getMayorGroup().getMemberList()) {
/* 3029 */         if (!resident.isInactiveForDays(mayor_inactive_days)) {
/* 3030 */           return false;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (InvalidConfiguration e) {
/* 3035 */       e.printStackTrace();
/* 3036 */       return false;
/*      */     }
/*      */     int mayor_inactive_days;
/* 3039 */     return true;
/*      */   }
/*      */   
/*      */   public void rename(String name) throws CivException, InvalidNameException
/*      */   {
/* 3044 */     Town other = CivGlobal.getTown(name);
/* 3045 */     if (other != null) {
/* 3046 */       throw new CivException("Already another town with this name");
/*      */     }
/*      */     
/* 3049 */     if (isCapitol()) {
/* 3050 */       getCiv().setCapitolName(name);
/* 3051 */       getCiv().save();
/*      */     }
/*      */     
/* 3054 */     String oldName = getName();
/*      */     
/* 3056 */     CivGlobal.removeTown(this);
/*      */     
/* 3058 */     setName(name);
/* 3059 */     save();
/*      */     
/* 3061 */     CivGlobal.addTown(this);
/*      */     
/* 3063 */     CivMessage.global("The town of " + oldName + " is now called " + getName());
/*      */   }
/*      */   
/*      */   public void trimCultureChunks(HashSet<ChunkCoord> expanded)
/*      */   {
/* 3068 */     LinkedList<ChunkCoord> removedKeys = new LinkedList();
/* 3069 */     for (ChunkCoord coord : this.cultureChunks.keySet()) {
/* 3070 */       if (!expanded.contains(coord)) {
/* 3071 */         removedKeys.add(coord);
/*      */       }
/*      */     }
/*      */     
/* 3075 */     for (ChunkCoord coord : removedKeys) {
/* 3076 */       CultureChunk cc = CivGlobal.getCultureChunk(coord);
/* 3077 */       CivGlobal.removeCultureChunk(cc);
/* 3078 */       this.cultureChunks.remove(coord);
/*      */     }
/*      */   }
/*      */   
/*      */   public ChunkCoord getTownCultureOrigin()
/*      */   {
/* 3084 */     TownHall townhall = getTownHall();
/*      */     ChunkCoord coord;
/* 3086 */     ChunkCoord coord; if (townhall == null)
/*      */     {
/* 3088 */       coord = ((TownChunk)getTownChunks().iterator().next()).getChunkCoord();
/*      */     }
/*      */     else {
/* 3091 */       coord = new ChunkCoord(townhall.getCenterLocation());
/*      */     }
/*      */     
/* 3094 */     return coord;
/*      */   }
/*      */   
/*      */   public Date getCreated() {
/* 3098 */     return this.created_date;
/*      */   }
/*      */   
/*      */   public void setCreated(Date created_date) {
/* 3102 */     this.created_date = created_date;
/*      */   }
/*      */   
/*      */   public void validateGift() throws CivException {
/*      */     try {
/* 3107 */       int min_gift_age = CivSettings.getInteger(CivSettings.civConfig, "civ.min_gift_age").intValue();
/*      */       
/* 3109 */       if (!DateUtil.isAfterDays(this.created_date, min_gift_age)) {
/* 3110 */         throw new CivException(getName() + " cannot be gifted until it is " + min_gift_age + " days old.");
/*      */       }
/*      */     } catch (InvalidConfiguration e) {
/* 3113 */       throw new CivException("Configuration error.");
/*      */     }
/*      */   }
/*      */   
/*      */   public int getGiftCost()
/*      */   {
/*      */     try {
/* 3120 */       gift_cost = CivSettings.getInteger(CivSettings.civConfig, "civ.gift_cost_per_town").intValue();
/*      */     } catch (InvalidConfiguration e) { int gift_cost;
/* 3122 */       e.printStackTrace();
/* 3123 */       return 0;
/*      */     }
/*      */     int gift_cost;
/* 3126 */     return gift_cost;
/*      */   }
/*      */   
/*      */   public void clearBonusGoods() {
/* 3130 */     this.bonusGoodies.clear();
/*      */   }
/*      */   
/*      */   public void processStructureFlipping(HashMap<ChunkCoord, Structure> centerCoords)
/*      */   {
/* 3135 */     for (CultureChunk cc : this.cultureChunks.values()) {
/* 3136 */       Structure struct = (Structure)centerCoords.get(cc.getChunkCoord());
/* 3137 */       if (struct != null)
/*      */       {
/*      */ 
/*      */ 
/* 3141 */         if (struct.getCiv() != cc.getCiv())
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3149 */           struct.getTown().removeStructure(struct);
/* 3150 */           struct.getTown().addStructure(struct);
/* 3151 */           struct.setTown(this);
/* 3152 */           struct.save();
/*      */         } }
/*      */     }
/*      */   }
/*      */   
/* 3157 */   public boolean hasDisabledStructures() { if (this.disabledBuildables.size() == 0) {
/* 3158 */       return false;
/*      */     }
/* 3160 */     return true;
/*      */   }
/*      */   
/*      */   public Collection<Buildable> getDisabledBuildables() {
/* 3164 */     return this.disabledBuildables.values();
/*      */   }
/*      */   
/*      */   class AttrCache
/*      */   {
/*      */     public Date lastUpdate;
/*      */     public AttrSource sources;
/*      */     
/*      */     AttrCache() {}
/*      */   }
/*      */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\Town.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
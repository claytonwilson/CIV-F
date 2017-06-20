/*      */ package com.avrgaming.civcraft.object;
/*      */ 
/*      */ import com.avrgaming.civcraft.arena.Arena;
/*      */ import com.avrgaming.civcraft.arena.ArenaTeam;
/*      */ import com.avrgaming.civcraft.camp.Camp;
/*      */ import com.avrgaming.civcraft.config.CivSettings;
/*      */ import com.avrgaming.civcraft.config.ConfigBuildableInfo;
/*      */ import com.avrgaming.civcraft.config.ConfigMaterial;
/*      */ import com.avrgaming.civcraft.config.ConfigPerk;
/*      */ import com.avrgaming.civcraft.config.ConfigPlatinumReward;
/*      */ import com.avrgaming.civcraft.database.SQL;
/*      */ import com.avrgaming.civcraft.database.SQLUpdate;
/*      */ import com.avrgaming.civcraft.event.EventTimer;
/*      */ import com.avrgaming.civcraft.exception.AlreadyRegisteredException;
/*      */ import com.avrgaming.civcraft.exception.CivException;
/*      */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*      */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*      */ import com.avrgaming.civcraft.interactive.InteractiveResponse;
/*      */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*      */ import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
/*      */ import com.avrgaming.civcraft.main.CivGlobal;
/*      */ import com.avrgaming.civcraft.main.CivLog;
/*      */ import com.avrgaming.civcraft.main.CivMessage;
/*      */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*      */ import com.avrgaming.civcraft.permission.PlotPermissions;
/*      */ import com.avrgaming.civcraft.randomevents.ConfigRandomEvent;
/*      */ import com.avrgaming.civcraft.randomevents.RandomEvent;
/*      */ import com.avrgaming.civcraft.road.RoadBlock;
/*      */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*      */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*      */ import com.avrgaming.civcraft.structure.Buildable;
/*      */ import com.avrgaming.civcraft.structure.Capitol;
/*      */ import com.avrgaming.civcraft.structure.TownHall;
/*      */ import com.avrgaming.civcraft.template.Template;
/*      */ import com.avrgaming.civcraft.threading.TaskMaster;
/*      */ import com.avrgaming.civcraft.util.BlockCoord;
/*      */ import com.avrgaming.civcraft.util.CallbackInterface;
/*      */ import com.avrgaming.civcraft.util.CivColor;
/*      */ import com.avrgaming.civcraft.util.ItemManager;
/*      */ import com.avrgaming.civcraft.util.Paginator;
/*      */ import com.avrgaming.civcraft.util.PlayerBlockChangeUtil;
/*      */ import com.avrgaming.civcraft.util.SimpleBlock;
/*      */ import com.avrgaming.global.perks.NotVerifiedException;
/*      */ import com.avrgaming.global.perks.Perk;
/*      */ import com.avrgaming.global.perks.PerkManager;
/*      */ import com.avrgaming.global.perks.PlatinumManager;
/*      */ import com.avrgaming.global.perks.components.CustomPersonalTemplate;
/*      */ import com.avrgaming.global.perks.components.CustomTemplate;
/*      */ import gpl.InventorySerializer;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.FileWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintWriter;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.Map;
/*      */ import java.util.TimeZone;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import org.bukkit.Bukkit;
/*      */ import org.bukkit.ChatColor;
/*      */ import org.bukkit.Location;
/*      */ import org.bukkit.Material;
/*      */ import org.bukkit.Server;
/*      */ import org.bukkit.World;
/*      */ import org.bukkit.entity.Player;
/*      */ import org.bukkit.inventory.Inventory;
/*      */ import org.bukkit.inventory.ItemStack;
/*      */ import org.bukkit.inventory.PlayerInventory;
/*      */ import org.bukkit.material.MaterialData;
/*      */ import org.bukkit.scoreboard.DisplaySlot;
/*      */ import org.bukkit.scoreboard.Objective;
/*      */ import org.bukkit.scoreboard.Score;
/*      */ import org.bukkit.scoreboard.Scoreboard;
/*      */ import org.bukkit.scoreboard.ScoreboardManager;
/*      */ import org.bukkit.scoreboard.Team;
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
/*      */ public class Resident
/*      */   extends SQLObject
/*      */ {
/*   96 */   private Town town = null;
/*   97 */   private Camp camp = null;
/*   98 */   private boolean townChat = false;
/*   99 */   private boolean civChat = false;
/*  100 */   private boolean adminChat = false;
/*  101 */   private boolean combatInfo = false;
/*      */   
/*  103 */   private boolean usesAntiCheat = false;
/*      */   
/*  105 */   public static HashSet<String> allchatters = new HashSet();
/*      */   
/*      */ 
/*  108 */   private Town townChatOverride = null;
/*  109 */   private Civilization civChatOverride = null;
/*  110 */   private boolean permOverride = false;
/*  111 */   private boolean sbperm = false;
/*  112 */   private boolean controlBlockInstantBreak = false;
/*  113 */   private int townID = 0;
/*  114 */   private int campID = 0;
/*  115 */   private boolean dontSaveTown = false;
/*      */   
/*      */   private String timezone;
/*  118 */   private boolean banned = false;
/*      */   
/*      */   private long registered;
/*      */   private long lastOnline;
/*      */   private int daysTilEvict;
/*      */   private boolean givenKit;
/*  124 */   private ConcurrentHashMap<String, Integer> friends = new ConcurrentHashMap();
/*      */   private EconObject treasury;
/*      */   private boolean muted;
/*  127 */   private Date muteExpires = null;
/*      */   
/*  129 */   private boolean interactiveMode = false;
/*  130 */   private InteractiveResponse interactiveResponse = null;
/*      */   
/*  132 */   private double spyExposure = 0.0D;
/*  133 */   public static int MAX_SPY_EXPOSURE = 1000;
/*  134 */   private boolean performingMission = false;
/*      */   
/*  136 */   private Town selectedTown = null;
/*      */   
/*  138 */   private Scoreboard scoreboard = null;
/*      */   public String desiredCivName;
/*      */   public String desiredCapitolName;
/*      */   public String desiredTownName;
/*  142 */   public Location desiredTownLocation = null;
/*  143 */   public Template desiredTemplate = null;
/*      */   
/*  145 */   public boolean allchat = false;
/*      */   
/*      */ 
/*      */   public Buildable pendingBuildable;
/*      */   
/*      */ 
/*      */   public ConfigBuildableInfo pendingBuildableInfo;
/*      */   
/*      */ 
/*      */   public CallbackInterface pendingCallback;
/*      */   
/*      */ 
/*  157 */   private boolean showScout = true;
/*  158 */   private boolean showTown = true;
/*  159 */   private boolean showCiv = true;
/*  160 */   private boolean showMap = false;
/*  161 */   private boolean showInfo = false;
/*  162 */   private String itemMode = "all";
/*  163 */   private String savedInventory = null;
/*  164 */   private boolean insideArena = false;
/*  165 */   private boolean isProtected = false;
/*      */   
/*  167 */   public HashMap<BlockCoord, SimpleBlock> previewUndo = null;
/*  168 */   public HashMap<String, Perk> perks = new HashMap();
/*  169 */   private Date lastKilledTime = null;
/*  170 */   private String lastIP = "";
/*      */   
/*  172 */   private boolean onRoad = false;
/*      */   public static final String TABLE_NAME = "RESIDENTS";
/*      */   
/*  175 */   public Resident(String name) throws InvalidNameException { setName(name);
/*  176 */     this.treasury = new EconObject(this);
/*  177 */     setTimezoneToServerDefault();
/*  178 */     loadSettings();
/*      */   }
/*      */   
/*      */   public Resident(ResultSet rs) throws SQLException, InvalidNameException {
/*  182 */     load(rs);
/*  183 */     loadSettings();
/*      */   }
/*      */   
/*      */   public void loadSettings() {
/*  187 */     this.spyExposure = 0.0D;
/*      */   }
/*      */   
/*      */   public static void init() throws SQLException
/*      */   {
/*  192 */     if (!SQL.hasTable("RESIDENTS")) {
/*  193 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + "RESIDENTS" + " (" + 
/*  194 */         "`id` int(11) unsigned NOT NULL auto_increment," + 
/*  195 */         "`name` VARCHAR(64) NOT NULL," + 
/*  196 */         "`town_id` int(11)," + 
/*  197 */         "`lastOnline` BIGINT NOT NULL," + 
/*  198 */         "`registered` BIGINT NOT NULL," + 
/*  199 */         "`friends` mediumtext," + 
/*  200 */         "`debt` double DEFAULT 0," + 
/*  201 */         "`coins` double DEFAULT 0," + 
/*  202 */         "`daysTilEvict` mediumint DEFAULT NULL," + 
/*  203 */         "`givenKit` bool NOT NULL DEFAULT '0'," + 
/*  204 */         "`camp_id` int(11)," + 
/*  205 */         "`timezone` mediumtext," + 
/*  206 */         "`banned` bool NOT NULL DEFAULT '0'," + 
/*  207 */         "`bannedMessage` mediumtext DEFAULT NULL," + 
/*  208 */         "`savedInventory` mediumtext DEFAULT NULL," + 
/*  209 */         "`insideArena` bool NOT NULL DEFAULT '0'," + 
/*  210 */         "`isProtected` bool NOT NULL DEFAULT '0'," + 
/*  211 */         "`flags` mediumtext DEFAULT NULL," + 
/*  212 */         "`last_ip` mediumtext DEFAULT NULL," + 
/*  213 */         "`debug_town` mediumtext DEFAULT NULL," + 
/*  214 */         "`debug_civ` mediumtext DEFAULT NuLL," + 
/*  215 */         "UNIQUE KEY (`name`), " + 
/*  216 */         "PRIMARY KEY (`id`)" + ")";
/*      */       
/*  218 */       SQL.makeTable(table_create);
/*  219 */       CivLog.info("Created RESIDENTS table");
/*      */     } else {
/*  221 */       CivLog.info("RESIDENTS table OK!");
/*      */       
/*  223 */       if (!SQL.hasColumn("RESIDENTS", "banned")) {
/*  224 */         CivLog.info("\tCouldn't find `banned` for resident.");
/*  225 */         SQL.addColumn("RESIDENTS", "`banned` bool default 0");
/*      */       }
/*      */       
/*  228 */       if (!SQL.hasColumn("RESIDENTS", "bannedMessage")) {
/*  229 */         CivLog.info("\tCouldn't find `bannedMessage` for resident.");
/*  230 */         SQL.addColumn("RESIDENTS", "`bannedMessage` mediumtext default null");
/*      */       }
/*      */       
/*  233 */       if (!SQL.hasColumn("RESIDENTS", "last_ip")) {
/*  234 */         CivLog.info("\tCouldn't find `last_ip` for resident.");
/*  235 */         SQL.addColumn("RESIDENTS", "`last_ip` mediumtext default null");
/*      */       }
/*      */       
/*  238 */       if (!SQL.hasColumn("RESIDENTS", "camp_id")) {
/*  239 */         CivLog.info("\tCouldn't find `camp_id` for resident.");
/*  240 */         SQL.addColumn("RESIDENTS", "`camp_id` int(11) default 0");
/*      */       }
/*      */       
/*  243 */       if (!SQL.hasColumn("RESIDENTS", "timezone")) {
/*  244 */         CivLog.info("\tCouldn't find `timezone` for resident.");
/*  245 */         SQL.addColumn("RESIDENTS", "`timezone` mediumtext default null");
/*      */       }
/*      */       
/*  248 */       if (!SQL.hasColumn("RESIDENTS", "debug_civ")) {
/*  249 */         CivLog.info("\tCouldn't find `debug_civ` for resident.");
/*  250 */         SQL.addColumn("RESIDENTS", "`debug_civ` mediumtext default null");
/*      */       }
/*      */       
/*  253 */       if (!SQL.hasColumn("RESIDENTS", "debug_town")) {
/*  254 */         CivLog.info("\tCouldn't find `debug_town` for resident.");
/*  255 */         SQL.addColumn("RESIDENTS", "`debug_town` mediumtext default null");
/*      */       }
/*      */       
/*  258 */       SQL.makeCol("flags", "mediumtext", "RESIDENTS");
/*  259 */       SQL.makeCol("savedInventory", "mediumtext", "RESIDENTS");
/*  260 */       SQL.makeCol("insideArena", "bool NOT NULL DEFAULT '0'", "RESIDENTS");
/*  261 */       SQL.makeCol("isProtected", "bool NOT NULL DEFAULT '0'", "RESIDENTS");
/*      */     }
/*      */   }
/*      */   
/*      */   public void load(ResultSet rs) throws SQLException, InvalidNameException
/*      */   {
/*  267 */     setId(rs.getInt("id"));
/*  268 */     setName(rs.getString("name"));
/*  269 */     this.townID = rs.getInt("town_id");
/*  270 */     this.campID = rs.getInt("camp_id");
/*  271 */     this.lastIP = rs.getString("last_ip");
/*      */     
/*  273 */     this.treasury = new EconObject(this);
/*  274 */     getTreasury().setBalance(rs.getDouble("coins"), false);
/*  275 */     setGivenKit(rs.getBoolean("givenKit"));
/*  276 */     setTimezone(rs.getString("timezone"));
/*  277 */     loadFlagSaveString(rs.getString("flags"));
/*  278 */     this.savedInventory = rs.getString("savedInventory");
/*  279 */     this.insideArena = rs.getBoolean("insideArena");
/*  280 */     this.isProtected = rs.getBoolean("isProtected");
/*      */     
/*  282 */     if (getTimezone() == null) {
/*  283 */       setTimezoneToServerDefault();
/*      */     }
/*      */     
/*  286 */     if (this.townID != 0) {
/*  287 */       setTown(CivGlobal.getTownFromId(this.townID));
/*  288 */       if (this.town == null) {
/*  289 */         CivLog.error("COULD NOT FIND TOWN(" + this.townID + ") FOR RESIDENT(" + getId() + ") Name:" + getName());
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  298 */         if (CivGlobal.testFileFlag("cleanupDatabase")) {
/*  299 */           saveNow();
/*      */         } else {
/*  301 */           this.dontSaveTown = true;
/*      */         }
/*  303 */         return;
/*      */       }
/*      */     }
/*      */     
/*  307 */     if (this.campID != 0) {
/*  308 */       setCamp(CivGlobal.getCampFromId(this.campID));
/*  309 */       if (this.camp == null) {
/*  310 */         CivLog.error("COULD NOT FIND CAMP(" + this.campID + ") FOR RESIDENT(" + getId() + ") Name:" + getName());
/*      */       } else {
/*  312 */         this.camp.addMember(this);
/*      */       }
/*      */     }
/*      */     
/*  316 */     if (getTown() != null) {
/*      */       try {
/*  318 */         getTown().addResident(this);
/*      */       } catch (AlreadyRegisteredException e) {
/*  320 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */     
/*  324 */     setLastOnline(rs.getLong("lastOnline"));
/*  325 */     setRegistered(rs.getLong("registered"));
/*  326 */     setDaysTilEvict(rs.getInt("daysTilEvict"));
/*  327 */     getTreasury().setDebt(rs.getDouble("debt"));
/*  328 */     loadFriendsFromSaveString(rs.getString("friends"));
/*      */   }
/*      */   
/*      */   private void setTimezoneToServerDefault()
/*      */   {
/*  333 */     this.timezone = EventTimer.getCalendarInServerTimeZone().getTimeZone().getID();
/*      */   }
/*      */   
/*      */   public String getFlagSaveString() {
/*  337 */     String flagString = "";
/*      */     
/*  339 */     if (isShowMap()) {
/*  340 */       flagString = flagString + "map,";
/*      */     }
/*      */     
/*  343 */     if (isShowTown()) {
/*  344 */       flagString = flagString + "showtown,";
/*      */     }
/*      */     
/*  347 */     if (isShowCiv()) {
/*  348 */       flagString = flagString + "showciv,";
/*      */     }
/*      */     
/*  351 */     if (isShowScout()) {
/*  352 */       flagString = flagString + "showscout,";
/*      */     }
/*      */     
/*  355 */     if (isShowInfo()) {
/*  356 */       flagString = flagString + "info,";
/*      */     }
/*      */     
/*  359 */     if (this.combatInfo) {
/*  360 */       flagString = flagString + "combatinfo,";
/*      */     }
/*      */     
/*  363 */     if (this.itemMode.equals("rare")) {
/*  364 */       flagString = flagString + "itemModeRare,";
/*  365 */     } else if (this.itemMode.equals("none")) {
/*  366 */       flagString = flagString + "itemModeNone,";
/*      */     }
/*      */     
/*  369 */     return flagString;
/*      */   }
/*      */   
/*      */   public void loadFlagSaveString(String str) {
/*  373 */     if (str == null) {
/*  374 */       return;
/*      */     }
/*      */     
/*  377 */     String[] split = str.split(",");
/*      */     String[] arrayOfString1;
/*  379 */     int j = (arrayOfString1 = split).length; for (int i = 0; i < j; i++) { String s = arrayOfString1[i];
/*  380 */       String str1; switch ((str1 = s.toLowerCase()).hashCode()) {case -1885357535:  if (str1.equals("showscout")) {} break; case -382744158:  if (str1.equals("combatinfo")) {} break; case -337871089:  if (str1.equals("showtown")) {} break; case -55175314:  if (str1.equals("itemmodenone")) {} break; case -55069480:  if (str1.equals("itemmoderare")) {} break; case 107868:  if (str1.equals("map")) break; break; case 3237038:  if (str1.equals("info")) {} break; case 2067294387:  if (!str1.equals("showciv"))
/*      */         {
/*  382 */           continue;setShowMap(true);
/*  383 */           continue;
/*      */           
/*  385 */           setShowTown(true);
/*      */         }
/*      */         else {
/*  388 */           setShowCiv(true);
/*  389 */           continue;
/*      */           
/*  391 */           setShowScout(true);
/*  392 */           continue;
/*      */           
/*  394 */           setShowInfo(true);
/*  395 */           continue;
/*      */           
/*  397 */           setCombatInfo(true);
/*  398 */           continue;
/*      */           
/*  400 */           this.itemMode = "rare";
/*  401 */           continue;
/*      */           
/*  403 */           this.itemMode = "none";
/*      */         }
/*      */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void save() {
/*  411 */     SQLUpdate.add(this);
/*      */   }
/*      */   
/*      */   public void saveNow()
/*      */     throws SQLException
/*      */   {
/*  417 */     HashMap<String, Object> hashmap = new HashMap();
/*      */     
/*  419 */     hashmap.put("name", getName());
/*  420 */     if (getTown() != null) {
/*  421 */       hashmap.put("town_id", Integer.valueOf(getTown().getId()));
/*      */     }
/*  423 */     else if (!this.dontSaveTown) {
/*  424 */       hashmap.put("town_id", null);
/*      */     }
/*      */     
/*      */ 
/*  428 */     if (getCamp() != null) {
/*  429 */       hashmap.put("camp_id", Integer.valueOf(getCamp().getId()));
/*      */     } else {
/*  431 */       hashmap.put("camp_id", null);
/*      */     }
/*      */     
/*  434 */     hashmap.put("lastOnline", Long.valueOf(getLastOnline()));
/*  435 */     hashmap.put("registered", Long.valueOf(getRegistered()));
/*  436 */     hashmap.put("debt", Double.valueOf(getTreasury().getDebt()));
/*  437 */     hashmap.put("daysTilEvict", Integer.valueOf(getDaysTilEvict()));
/*  438 */     hashmap.put("friends", getFriendsSaveString());
/*  439 */     hashmap.put("givenKit", Boolean.valueOf(isGivenKit()));
/*  440 */     hashmap.put("coins", Double.valueOf(getTreasury().getBalance()));
/*  441 */     hashmap.put("timezone", getTimezone());
/*  442 */     hashmap.put("flags", getFlagSaveString());
/*  443 */     hashmap.put("last_ip", getLastIP());
/*  444 */     hashmap.put("savedInventory", this.savedInventory);
/*  445 */     hashmap.put("insideArena", Boolean.valueOf(this.insideArena));
/*  446 */     hashmap.put("isProtected", Boolean.valueOf(this.isProtected));
/*      */     
/*  448 */     if (getTown() != null) {
/*  449 */       hashmap.put("debug_town", getTown().getName());
/*      */       
/*  451 */       if (getTown().getCiv() != null) {
/*  452 */         hashmap.put("debug_civ", getCiv().getName());
/*      */       }
/*      */     }
/*      */     
/*  456 */     SQL.updateNamedObject(this, hashmap, "RESIDENTS");
/*      */   }
/*      */   
/*      */   public String getTownString() {
/*  460 */     if (this.town == null) {
/*  461 */       return "none";
/*      */     }
/*  463 */     return getTown().getName();
/*      */   }
/*      */   
/*      */   public Town getTown() {
/*  467 */     return this.town;
/*      */   }
/*      */   
/*      */   public void setTown(Town town) {
/*  471 */     this.town = town;
/*      */   }
/*      */   
/*      */   public boolean hasTown() {
/*  475 */     return this.town != null;
/*      */   }
/*      */   
/*      */   public long getRegistered() {
/*  479 */     return this.registered;
/*      */   }
/*      */   
/*      */   public void setRegistered(long registered) {
/*  483 */     this.registered = registered;
/*      */   }
/*      */   
/*      */   public long getLastOnline() {
/*  487 */     return this.lastOnline;
/*      */   }
/*      */   
/*      */   public void setLastOnline(long lastOnline) {
/*  491 */     this.lastOnline = lastOnline;
/*      */   }
/*      */   
/*      */ 
/*      */   public void delete() {}
/*      */   
/*      */   public EconObject getTreasury()
/*      */   {
/*  499 */     return this.treasury;
/*      */   }
/*      */   
/*      */   public void setTreasury(EconObject treasury) {
/*  503 */     this.treasury = treasury;
/*      */   }
/*      */   
/*      */   public void onEnterDebt() {
/*  507 */     this.daysTilEvict = 3;
/*      */   }
/*      */   
/*      */   public void warnDebt()
/*      */   {
/*      */     try {
/*  513 */       Player player = CivGlobal.getPlayer(this);
/*  514 */       CivMessage.send(player, "§eYou are in " + getTreasury().getDebt() + " coins of debt!");
/*  515 */       CivMessage.send(player, "§7If you do not pay your debt within " + this.daysTilEvict + " days you will be evicted from town.");
/*      */     }
/*      */     catch (CivException localCivException) {}
/*      */   }
/*      */   
/*      */   public int getDaysTilEvict()
/*      */   {
/*  522 */     return this.daysTilEvict;
/*      */   }
/*      */   
/*      */   public void setDaysTilEvict(int daysTilEvict) {
/*  526 */     this.daysTilEvict = daysTilEvict;
/*      */   }
/*      */   
/*      */   public void decrementGraceCounters() {
/*  530 */     this.daysTilEvict -= 1;
/*  531 */     if (this.daysTilEvict == 0) {
/*  532 */       getTown().removeResident(this);
/*      */       try
/*      */       {
/*  535 */         CivMessage.send(CivGlobal.getPlayer(this), "§eYou have been evicted from town!");
/*      */       }
/*      */       catch (CivException localCivException) {}
/*      */       
/*  539 */       return;
/*      */     }
/*      */     
/*  542 */     if (getTreasury().inDebt()) {
/*  543 */       warnDebt();
/*      */     } else {
/*  545 */       warnEvict();
/*      */     }
/*      */     
/*  548 */     save();
/*      */   }
/*      */   
/*      */   public double getPropertyTaxOwed() {
/*  552 */     double total = 0.0D;
/*      */     
/*  554 */     if (getTown() == null) {
/*  555 */       return total;
/*      */     }
/*      */     
/*  558 */     for (TownChunk tc : getTown().getTownChunks()) {
/*  559 */       if (tc.perms.getOwner() == this) {
/*  560 */         double tax = tc.getValue() * getTown().getTaxRate();
/*  561 */         total += tax;
/*      */       }
/*      */     }
/*  564 */     return total;
/*      */   }
/*      */   
/*      */   public boolean isLandOwner() {
/*  568 */     if (getTown() == null) {
/*  569 */       return false;
/*      */     }
/*  571 */     for (TownChunk tc : getTown().getTownChunks()) {
/*  572 */       if (tc.perms.getOwner() == this) {
/*  573 */         return true;
/*      */       }
/*      */     }
/*      */     
/*  577 */     return false;
/*      */   }
/*      */   
/*      */   public double getFlatTaxOwed()
/*      */   {
/*  582 */     if (getTown() == null) {
/*  583 */       return 0.0D;
/*      */     }
/*  585 */     return getTown().getFlatTax();
/*      */   }
/*      */   
/*      */   public boolean isTaxExempt() {
/*  589 */     return (getTown().isInGroup("mayors", this)) || (getTown().isInGroup("assistants", this));
/*      */   }
/*      */   
/*      */   public void payOffDebt() {
/*  593 */     getTreasury().payTo(getTown().getTreasury(), getTreasury().getDebt());
/*  594 */     getTreasury().setDebt(0.0D);
/*  595 */     this.daysTilEvict = -1;
/*  596 */     save();
/*      */   }
/*      */   
/*      */   public void addFriend(Resident resident) {
/*  600 */     this.friends.put(resident.getName(), Integer.valueOf(1));
/*      */   }
/*      */   
/*      */   public boolean isFriend(Resident resident) {
/*  604 */     return this.friends.containsKey(resident.getName());
/*      */   }
/*      */   
/*      */   public Collection<String> getFriends() {
/*  608 */     return this.friends.keySet();
/*      */   }
/*      */   
/*      */   private String getFriendsSaveString() {
/*  612 */     String out = "";
/*  613 */     for (String name : this.friends.keySet()) {
/*  614 */       out = out + name + ",";
/*      */     }
/*  616 */     return out;
/*      */   }
/*      */   
/*      */   private void loadFriendsFromSaveString(String string) {
/*  620 */     String[] split = string.split(",");
/*      */     String[] arrayOfString1;
/*  622 */     int j = (arrayOfString1 = split).length; for (int i = 0; i < j; i++) { String str = arrayOfString1[i];
/*  623 */       this.friends.put(str, Integer.valueOf(1));
/*      */     }
/*      */   }
/*      */   
/*      */   public void removeFriend(Resident friendToAdd) {
/*  628 */     this.friends.remove(friendToAdd.getName());
/*      */   }
/*      */   
/*      */   public String getGroupsString() {
/*  632 */     String out = "";
/*      */     
/*  634 */     for (PermissionGroup grp : CivGlobal.getGroups()) {
/*  635 */       if (grp.hasMember(this)) {
/*  636 */         if (grp.getTown() != null) {
/*  637 */           if (grp.isProtectedGroup()) {
/*  638 */             out = out + "§d";
/*      */           } else {
/*  640 */             out = out + "§f";
/*      */           }
/*  642 */           out = out + grp.getName() + "(" + grp.getTown().getName() + ")";
/*      */         }
/*  644 */         else if (grp.getCiv() != null) {
/*  645 */           out = out + "§6" + grp.getName() + "(" + grp.getCiv().getName() + ")";
/*      */         }
/*      */         
/*  648 */         out = out + ", ";
/*      */       }
/*      */     }
/*      */     
/*  652 */     return out;
/*      */   }
/*      */   
/*      */   public void warnEvict() {
/*      */     try {
/*  657 */       CivMessage.send(CivGlobal.getPlayer(this), "§eYou are being evicted from town! You have " + 
/*  658 */         getDaysTilEvict() + " days left to pack your stuff and leave.");
/*      */     }
/*      */     catch (CivException localCivException) {}
/*      */   }
/*      */   
/*      */   public boolean isTownChat()
/*      */   {
/*  665 */     return this.townChat;
/*      */   }
/*      */   
/*      */   public void setTownChat(boolean townChat) {
/*  669 */     this.townChat = townChat;
/*      */   }
/*      */   
/*      */   public boolean isCivChat() {
/*  673 */     return this.civChat;
/*      */   }
/*      */   
/*      */   public void setCivChat(boolean civChat) {
/*  677 */     this.civChat = civChat;
/*      */   }
/*      */   
/*      */   public boolean isAdminChat() {
/*  681 */     return this.adminChat;
/*      */   }
/*      */   
/*      */   public void setAdminChat(boolean adminChat) {
/*  685 */     this.adminChat = adminChat;
/*      */   }
/*      */   
/*      */   public Town getTownChatOverride() {
/*  689 */     return this.townChatOverride;
/*      */   }
/*      */   
/*      */   public void setTownChatOverride(Town townChatOverride) {
/*  693 */     this.townChatOverride = townChatOverride;
/*      */   }
/*      */   
/*      */   public Civilization getCivChatOverride() {
/*  697 */     return this.civChatOverride;
/*      */   }
/*      */   
/*      */   public void setCivChatOverride(Civilization civChatOverride) {
/*  701 */     this.civChatOverride = civChatOverride;
/*      */   }
/*      */   
/*      */   public boolean isPermOverride() {
/*  705 */     return this.permOverride;
/*      */   }
/*      */   
/*      */   public void setPermOverride(boolean permOverride) {
/*  709 */     this.permOverride = permOverride;
/*      */   }
/*      */   
/*      */   public boolean takeItemInHand(int itemId, int itemData, int amount) throws CivException
/*      */   {
/*  714 */     Player player = CivGlobal.getPlayer(this);
/*  715 */     Inventory inv = player.getInventory();
/*      */     
/*  717 */     if (!inv.contains(itemId)) {
/*  718 */       return false;
/*      */     }
/*      */     
/*  721 */     if ((player.getItemInHand().getTypeId() != itemId) && 
/*  722 */       (player.getItemInHand().getTypeId() != itemData)) {
/*  723 */       return false;
/*      */     }
/*      */     
/*  726 */     ItemStack stack = player.getItemInHand();
/*      */     
/*  728 */     if (stack.getAmount() < amount)
/*  729 */       return false;
/*  730 */     if (stack.getAmount() == amount) {
/*  731 */       inv.removeItem(new ItemStack[] { stack });
/*      */     } else {
/*  733 */       stack.setAmount(stack.getAmount() - amount);
/*      */     }
/*      */     
/*  736 */     player.updateInventory();
/*  737 */     return true;
/*      */   }
/*      */   
/*      */   public boolean takeItem(int itemId, int itemData, int amount) throws CivException
/*      */   {
/*  742 */     Player player = CivGlobal.getPlayer(this);
/*  743 */     Inventory inv = player.getInventory();
/*      */     
/*  745 */     if (!inv.contains(itemId)) {
/*  746 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  750 */     HashMap<Integer, ? extends ItemStack> stacks = inv.all(itemId);
/*      */     
/*  752 */     for (ItemStack stack : stacks.values()) {
/*  753 */       if (stack.getData().getData() == (byte)itemData)
/*      */       {
/*      */ 
/*      */ 
/*  757 */         if (stack.getAmount() > 0)
/*      */         {
/*      */ 
/*  760 */           if (stack.getAmount() < amount) {
/*  761 */             amount -= stack.getAmount();
/*  762 */             stack.setAmount(0);
/*  763 */             inv.removeItem(new ItemStack[] { stack });
/*      */           }
/*      */           else
/*      */           {
/*  767 */             stack.setAmount(stack.getAmount() - amount);
/*  768 */             break;
/*      */           } }
/*      */       }
/*      */     }
/*  772 */     player.updateInventory();
/*  773 */     return true;
/*      */   }
/*      */   
/*      */   public int giveItem(int itemId, short damage, int amount) throws CivException
/*      */   {
/*  778 */     Player player = CivGlobal.getPlayer(this);
/*  779 */     Inventory inv = player.getInventory();
/*  780 */     ItemStack stack = new ItemStack(itemId, amount, damage);
/*  781 */     HashMap<Integer, ItemStack> leftovers = null;
/*  782 */     leftovers = inv.addItem(new ItemStack[] { stack });
/*      */     
/*  784 */     int leftoverAmount = 0;
/*  785 */     for (ItemStack i : leftovers.values()) {
/*  786 */       leftoverAmount += i.getAmount();
/*      */     }
/*  788 */     player.updateInventory();
/*  789 */     return amount - leftoverAmount;
/*      */   }
/*      */   
/*      */   public boolean buyItem(String itemName, int id, byte data, double price, int amount) throws CivException
/*      */   {
/*  794 */     if (!getTreasury().hasEnough(price)) {
/*  795 */       throw new CivException("Not enough coins.");
/*      */     }
/*      */     
/*  798 */     boolean completed = true;
/*  799 */     int bought = 0;
/*  800 */     bought = giveItem(id, data, amount);
/*  801 */     if (bought != amount) {
/*  802 */       getTreasury().withdraw(price);
/*  803 */       takeItem(id, data, bought);
/*  804 */       completed = false;
/*      */     } else {
/*  806 */       getTreasury().withdraw(price);
/*      */     }
/*      */     
/*  809 */     if (completed) {
/*  810 */       return true;
/*      */     }
/*      */     
/*  813 */     throw new CivException("Inventory full.");
/*      */   }
/*      */   
/*      */   public Civilization getCiv()
/*      */   {
/*  818 */     if (getTown() == null) {
/*  819 */       return null;
/*      */     }
/*  821 */     return getTown().getCiv();
/*      */   }
/*      */   
/*      */   public void setScoreboardName(String name, String key) {
/*  825 */     if (this.scoreboard == null) {
/*  826 */       this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
/*  827 */       Team team = this.scoreboard.registerNewTeam("team");
/*  828 */       team.addPlayer(CivGlobal.getFakeOfflinePlayer(key));
/*  829 */       team.setDisplayName(name);
/*      */     } else {
/*  831 */       Team team = this.scoreboard.getTeam("team");
/*  832 */       team.setDisplayName(name);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setScoreboardValue(String name, String key, int value)
/*      */   {
/*  838 */     if (this.scoreboard == null) {
/*  839 */       return;
/*      */     }
/*      */     
/*  842 */     Objective obj = this.scoreboard.getObjective("obj:" + key);
/*  843 */     if (obj == null) {
/*  844 */       obj = this.scoreboard.registerNewObjective(name, "dummy");
/*  845 */       obj.setDisplaySlot(DisplaySlot.SIDEBAR);
/*  846 */       Score score = obj.getScore(CivGlobal.getFakeOfflinePlayer(key));
/*  847 */       score.setScore(value);
/*      */     } else {
/*  849 */       Score score = obj.getScore(CivGlobal.getFakeOfflinePlayer(key));
/*  850 */       score.setScore(value);
/*      */     }
/*      */   }
/*      */   
/*      */   public void showScoreboard() {
/*  855 */     if (this.scoreboard != null) {
/*      */       try
/*      */       {
/*  858 */         Player player = CivGlobal.getPlayer(this);
/*  859 */         player.setScoreboard(this.scoreboard);
/*      */       } catch (CivException e) {
/*  861 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void hideScoreboard()
/*      */   {
/*      */     try {
/*  869 */       Player player = CivGlobal.getPlayer(this);
/*  870 */       player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
/*      */     } catch (CivException e) {
/*  872 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isGivenKit() {
/*  877 */     return this.givenKit;
/*      */   }
/*      */   
/*      */   public void setGivenKit(boolean givenKit) {
/*  881 */     this.givenKit = givenKit;
/*      */   }
/*      */   
/*      */   public boolean isSBPermOverride() {
/*  885 */     return this.sbperm;
/*      */   }
/*      */   
/*      */   public void setSBPermOverride(boolean b) {
/*  889 */     this.sbperm = b;
/*      */   }
/*      */   
/*      */   public void setInteractiveMode(InteractiveResponse interactive) {
/*  893 */     this.interactiveMode = true;
/*  894 */     this.interactiveResponse = interactive;
/*      */   }
/*      */   
/*      */   public void clearInteractiveMode() {
/*  898 */     this.interactiveMode = false;
/*  899 */     this.interactiveResponse = null;
/*      */   }
/*      */   
/*      */   public InteractiveResponse getInteractiveResponse() {
/*  903 */     return this.interactiveResponse;
/*      */   }
/*      */   
/*      */   public boolean isInteractiveMode() {
/*  907 */     return this.interactiveMode;
/*      */   }
/*      */   
/*      */   public Town getSelectedTown() {
/*  911 */     return this.selectedTown;
/*      */   }
/*      */   
/*      */   public void setSelectedTown(Town selectedTown) {
/*  915 */     this.selectedTown = selectedTown;
/*      */   }
/*      */   
/*      */   public Camp getCamp() {
/*  919 */     return this.camp;
/*      */   }
/*      */   
/*      */   public void setCamp(Camp camp) {
/*  923 */     this.camp = camp;
/*      */   }
/*      */   
/*      */   public boolean hasCamp() {
/*  927 */     return this.camp != null;
/*      */   }
/*      */   
/*      */   public String getCampString() {
/*  931 */     if (this.camp == null) {
/*  932 */       return "none";
/*      */     }
/*  934 */     return this.camp.getName();
/*      */   }
/*      */   
/*      */   public void showWarnings(Player player)
/*      */   {
/*  939 */     if (getTown() != null) {
/*  940 */       for (Buildable struct : getTown().invalidStructures) {
/*  941 */         CivMessage.send(player, "§e" + ChatColor.BOLD + 
/*  942 */           "WARNING: Your town's " + struct.getDisplayName() + " at " + struct.getCenterLocation() + 
/*  943 */           " is invalid! Reason:" + struct.getInvalidReason());
/*      */       }
/*      */       
/*      */ 
/*  947 */       if (getTown().getActiveEvent() != null) {
/*  948 */         CivMessage.send(player, "§eThe is a " + getTown().getActiveEvent().configRandomEvent.name + " going on in your town! Use /town event for details.");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean isShowScout()
/*      */   {
/*  956 */     return this.showScout;
/*      */   }
/*      */   
/*      */   public void setShowScout(boolean showScout) {
/*  960 */     this.showScout = showScout;
/*      */   }
/*      */   
/*      */   public boolean isShowTown() {
/*  964 */     return this.showTown;
/*      */   }
/*      */   
/*      */   public void setShowTown(boolean showTown) {
/*  968 */     this.showTown = showTown;
/*      */   }
/*      */   
/*      */   public boolean isShowCiv() {
/*  972 */     return this.showCiv;
/*      */   }
/*      */   
/*      */   public void setShowCiv(boolean showCiv) {
/*  976 */     this.showCiv = showCiv;
/*      */   }
/*      */   
/*      */   public boolean isShowMap() {
/*  980 */     return this.showMap;
/*      */   }
/*      */   
/*      */   public void setShowMap(boolean showMap) {
/*  984 */     this.showMap = showMap;
/*      */   }
/*      */   
/*      */   public void undoPreview() {
/*  988 */     if (this.previewUndo == null) {
/*  989 */       this.previewUndo = new HashMap();
/*  990 */       return;
/*      */     }
/*      */     try
/*      */     {
/*  994 */       Player player = CivGlobal.getPlayer(this);
/*  995 */       PlayerBlockChangeUtil util = new PlayerBlockChangeUtil();
/*  996 */       for (BlockCoord coord : this.previewUndo.keySet()) {
/*  997 */         SimpleBlock sb = (SimpleBlock)this.previewUndo.get(coord);
/*  998 */         util.addUpdateBlock(player.getName(), coord, sb.getType(), sb.getData());
/*      */       }
/*      */       
/* 1001 */       util.sendUpdate(player.getName());
/*      */     }
/*      */     catch (CivException localCivException) {}
/*      */     
/*      */ 
/* 1006 */     this.previewUndo.clear();
/* 1007 */     this.previewUndo = new HashMap();
/*      */   }
/*      */   
/*      */   public boolean isShowInfo() {
/* 1011 */     return this.showInfo;
/*      */   }
/*      */   
/*      */   public void setShowInfo(boolean showInfo) {
/* 1015 */     this.showInfo = showInfo;
/*      */   }
/*      */   
/*      */   public boolean isBanned() {
/* 1019 */     return this.banned;
/*      */   }
/*      */   
/*      */   public void setBanned(boolean banned) {
/* 1023 */     this.banned = banned;
/*      */   }
/*      */   
/*      */   public double getSpyExposure() {
/* 1027 */     return this.spyExposure;
/*      */   }
/*      */   
/*      */   public void setSpyExposure(double spyExposure) {
/* 1031 */     this.spyExposure = spyExposure;
/*      */     try
/*      */     {
/* 1034 */       Player player = CivGlobal.getPlayer(this);
/* 1035 */       double percentage = spyExposure / MAX_SPY_EXPOSURE;
/* 1036 */       player.setExp((float)percentage);
/*      */     }
/*      */     catch (CivException localCivException) {}
/*      */   }
/*      */   
/*      */   public boolean isPerformingMission()
/*      */   {
/* 1043 */     return this.performingMission;
/*      */   }
/*      */   
/*      */   public void setPerformingMission(boolean performingMission) {
/* 1047 */     this.performingMission = performingMission;
/*      */   }
/*      */   
/*      */   public void onRoadTest(BlockCoord coord, Player player)
/*      */   {
/* 1052 */     BlockCoord feet = new BlockCoord(coord);
/* 1053 */     feet.setY(feet.getY() - 1);
/* 1054 */     RoadBlock rb = CivGlobal.getRoadBlock(feet);
/*      */     
/* 1056 */     if (rb == null) {
/* 1057 */       this.onRoad = false;
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1062 */       this.onRoad = true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isOnRoad()
/*      */   {
/* 1072 */     return this.onRoad;
/*      */   }
/*      */   
/*      */   public void setOnRoad(boolean onRoad) {
/* 1076 */     this.onRoad = onRoad;
/*      */   }
/*      */   
/*      */   public void giveAllFreePerks()
/*      */   {
/*      */     try {
/* 1082 */       perkCount = CivSettings.getInteger(CivSettings.perkConfig, "system.free_perk_count").intValue();
/*      */     } catch (InvalidConfiguration e) { int perkCount;
/* 1084 */       e.printStackTrace(); return;
/*      */     }
/*      */     
/*      */     int perkCount;
/* 1088 */     for (ConfigPerk p : CivSettings.perks.values()) {
/* 1089 */       Perk perk = new Perk(p);
/* 1090 */       perk.count = perkCount;
/* 1091 */       this.perks.put(perk.getIdent(), perk);
/*      */     }
/*      */     
/* 1094 */     CivMessage.send(this, "§aYou've got free perks! Use /resident perks to see them.");
/*      */   }
/*      */   
/*      */   public void loadPerks() {
/* 1098 */     if (!PlatinumManager.isEnabled()) {
/* 1099 */       return;
/*      */     }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1161 */     TaskMaster.asyncTask(new Runnable()
/*      */     {
/*      */       Resident resident;
/*      */       
/*      */       public void run()
/*      */       {
/*      */         try
/*      */         {
/* 1112 */           this.resident.perks.clear();
/* 1113 */           Player player = CivGlobal.getPlayer(this.resident);
/*      */           try {
/* 1115 */             LinkedList<String> perkIDs = PerkManager.loadPerksForResident(this.resident);
/* 1116 */             for (String perkID : perkIDs) {
/* 1117 */               ConfigPerk configPerk = (ConfigPerk)CivSettings.perks.get(perkID);
/* 1118 */               if (configPerk != null)
/*      */               {
/*      */ 
/*      */ 
/* 1122 */                 Perk p2 = (Perk)this.resident.perks.get(configPerk.id);
/* 1123 */                 if (p2 != null) {
/* 1124 */                   p2.count += 1;
/* 1125 */                   this.resident.perks.put(p2.getIdent(), p2);
/*      */                 } else {
/* 1127 */                   Perk p = new Perk(configPerk);
/* 1128 */                   this.resident.perks.put(p.getIdent(), p);
/*      */                 }
/*      */               }
/*      */             }
/*      */           } catch (SQLException e) {
/* 1133 */             CivMessage.sendError(player, "Unable to load perks from perk database. Contact an admin.");
/* 1134 */             e.printStackTrace();
/* 1135 */             return;
/*      */           }
/*      */           catch (NotVerifiedException e) {
/*      */             try {
/* 1139 */               url = CivSettings.getString(CivSettings.perkConfig, "system.register_url");
/*      */             } catch (InvalidConfiguration e1) { String url;
/* 1141 */               e1.printStackTrace(); return;
/*      */             }
/*      */             
/*      */             String url;
/* 1145 */             CivMessage.send(player, "§e" + CivColor.BOLD + "Hey! You're in-game account is not registered! Register it at " + url);
/* 1146 */             CivMessage.send(player, "§e" + CivColor.BOLD + "You'll be unable to earn Platinum until you register.");
/* 1147 */             return;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1154 */           PlatinumManager.givePlatinumOnce(this.resident, 
/* 1155 */             ((ConfigPlatinumReward)CivSettings.platinumRewards.get("loginFirstVerified")).name, 
/* 1156 */             Integer.valueOf(((ConfigPlatinumReward)CivSettings.platinumRewards.get("loginFirstVerified")).amount), 
/* 1157 */             "Achievement! First time you've logged in while verified! %d");
/*      */         }
/*      */         catch (CivException e1) {}
/*      */       }
/* 1161 */     }, 0L);
/*      */   }
/*      */   
/*      */   public void setRejoinCooldown(Town town) {
/* 1165 */     String value = town.getCiv().getId();
/* 1166 */     String key = getCooldownKey();
/* 1167 */     CivGlobal.getSessionDB().add(key, value, 0, 0, 0);
/*      */   }
/*      */   
/*      */   public String getCooldownKey() {
/* 1171 */     return "cooldown:" + getName();
/*      */   }
/*      */   
/*      */   public void cleanupCooldown() {
/* 1175 */     CivGlobal.getSessionDB().delete_all(getCooldownKey());
/*      */   }
/*      */   
/*      */   public void validateJoinTown(Town town) throws CivException {
/* 1179 */     if ((hasTown()) && (getCiv() == town.getCiv()))
/*      */     {
/* 1181 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 1187 */       cooldownHours = CivSettings.getInteger(CivSettings.civConfig, "global.join_civ_cooldown").intValue();
/*      */     } catch (InvalidConfiguration e) { int cooldownHours;
/* 1189 */       e.printStackTrace(); return;
/*      */     }
/*      */     
/*      */     int cooldownHours;
/* 1193 */     long cooldownTime = cooldownHours * 60 * 60 * 1000;
/*      */     
/* 1195 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(getCooldownKey());
/* 1196 */     if (entries.size() > 0) {
/* 1197 */       Civilization oldCiv = CivGlobal.getCivFromId(Integer.valueOf(((SessionEntry)entries.get(0)).value).intValue());
/* 1198 */       if (oldCiv == null)
/*      */       {
/* 1200 */         cleanupCooldown();
/* 1201 */         return;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1210 */       Date now = new Date();
/* 1211 */       if (now.getTime() > ((SessionEntry)entries.get(0)).time + cooldownTime)
/*      */       {
/* 1213 */         cleanupCooldown();
/* 1214 */         return;
/*      */       }
/*      */       
/* 1217 */       throw new CivException("Cannot invite a player who has left a different civilization within " + cooldownHours + " hours after leaving.");
/*      */     }
/*      */   }
/*      */   
/*      */   public LinkedList<Perk> getPersonalTemplatePerks(ConfigBuildableInfo info) {
/* 1222 */     LinkedList<Perk> templates = new LinkedList();
/*      */     
/* 1224 */     for (Perk perk : this.perks.values()) {
/* 1225 */       CustomPersonalTemplate customTemplate = (CustomPersonalTemplate)perk.getComponent("CustomPersonalTemplate");
/* 1226 */       if (customTemplate != null)
/*      */       {
/*      */ 
/*      */ 
/* 1230 */         if (customTemplate.getString("id").equals(info.id))
/* 1231 */           templates.add(perk);
/*      */       }
/*      */     }
/* 1234 */     return templates;
/*      */   }
/*      */   
/*      */   public ArrayList<Perk> getUnboundTemplatePerks(ArrayList<Perk> alreadyBoundPerkList, ConfigBuildableInfo info) {
/* 1238 */     ArrayList<Perk> unboundPerks = new ArrayList();
/* 1239 */     for (Perk ourPerk : this.perks.values()) {
/* 1240 */       CustomTemplate customTemplate = (CustomTemplate)ourPerk.getComponent("CustomTemplate");
/* 1241 */       if (customTemplate != null)
/*      */       {
/*      */ 
/*      */ 
/* 1245 */         if (customTemplate.getString("template").equals(info.template_base_name))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1250 */           for (Perk perk : alreadyBoundPerkList) {
/* 1251 */             if (perk.getIdent().equals(ourPerk.getIdent())) {
/*      */               break;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 1257 */           unboundPerks.add(ourPerk);
/*      */         } }
/*      */     }
/* 1260 */     return unboundPerks;
/*      */   }
/*      */   
/*      */   public boolean isControlBlockInstantBreak() {
/* 1264 */     return this.controlBlockInstantBreak;
/*      */   }
/*      */   
/*      */   public void setControlBlockInstantBreak(boolean controlBlockInstantBreak) {
/* 1268 */     this.controlBlockInstantBreak = controlBlockInstantBreak;
/*      */   }
/*      */   
/*      */   public boolean isMuted()
/*      */   {
/* 1273 */     return this.muted;
/*      */   }
/*      */   
/*      */   public void setMuted(boolean muted) {
/* 1277 */     this.muted = muted;
/*      */   }
/*      */   
/*      */   public boolean isCombatInfo() {
/* 1281 */     return this.combatInfo;
/*      */   }
/*      */   
/*      */   public void setCombatInfo(boolean combatInfo) {
/* 1285 */     this.combatInfo = combatInfo;
/*      */   }
/*      */   
/*      */   public boolean isInactiveForDays(int days) {
/* 1289 */     Calendar now = Calendar.getInstance();
/* 1290 */     Calendar expire = Calendar.getInstance();
/* 1291 */     expire.setTimeInMillis(getLastOnline());
/*      */     
/* 1293 */     expire.add(5, days);
/*      */     
/* 1295 */     if (now.after(expire)) {
/* 1296 */       return true;
/*      */     }
/*      */     
/* 1299 */     return false;
/*      */   }
/*      */   
/*      */   public String getTimezone() {
/* 1303 */     return this.timezone;
/*      */   }
/*      */   
/*      */   public void setTimezone(String timezone) {
/* 1307 */     this.timezone = timezone;
/*      */   }
/*      */   
/*      */   public Inventory startTradeWith(Resident resident) {
/*      */     try {
/* 1312 */       Player player = CivGlobal.getPlayer(this);
/* 1313 */       if (player.isDead()) {
/* 1314 */         throw new CivException("Cannot trade with dead players.");
/*      */       }
/* 1316 */       inv = Bukkit.createInventory(player, 45, getName() + " : " + resident.getName());
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1323 */       signStack = LoreGuiItem.build("", 35, 0, new String[] { "" });
/* 1324 */       int start = 0;
/* 1325 */       for (int i = start; i < 9 + start; i++) {
/* 1326 */         if (i - start == 8) {
/* 1327 */           ItemStack guiStack = LoreGuiItem.build(resident.getName() + " Confirm", 
/* 1328 */             35, 14, new String[] {
/* 1329 */             "§aWaiting for §b" + resident.getName(), 
/* 1330 */             "§7to confirm this trade." });
/* 1331 */           inv.setItem(i, guiStack);
/* 1332 */         } else if (i - start == 7) {
/* 1333 */           ItemStack guiStack = LoreGuiItem.build("Coins Offered", 
/* 1334 */             ItemManager.getId(Material.NETHER_BRICK_ITEM), 0, new String[] {
/* 1335 */             "§e0 Coins" });
/* 1336 */           inv.setItem(i, guiStack);
/*      */         } else {
/* 1338 */           inv.setItem(i, signStack);
/*      */         }
/*      */       }
/*      */       
/* 1342 */       start = 36;
/* 1343 */       for (int i = start; i < 9 + start; i++) {
/* 1344 */         if (i - start == 8) {
/* 1345 */           ItemStack guiStack = LoreGuiItem.build("Your Confirm", 
/* 1346 */             35, 14, new String[] {
/* 1347 */             "§6<Click to Confirm Trade>" });
/* 1348 */           inv.setItem(i, guiStack);
/*      */         }
/* 1350 */         else if (i - start == 0) {
/* 1351 */           ItemStack guiStack = LoreGuiItem.build("Remove Coins", 
/* 1352 */             ItemManager.getId(Material.NETHER_BRICK_ITEM), 0, new String[] {
/* 1353 */             "§6Click to Remove 100 coins.", 
/* 1354 */             "§6Shift-Click to Remove 1000 coins." });
/* 1355 */           inv.setItem(i, guiStack);
/* 1356 */         } else if (i - start == 1) {
/* 1357 */           ItemStack guiStack = LoreGuiItem.build("Add Coins", 
/* 1358 */             ItemManager.getId(Material.GOLD_INGOT), 0, new String[] {
/* 1359 */             "§6Click to Add 100 coins.", 
/* 1360 */             "§6Shift-Click to Add 1000 coins." });
/* 1361 */           inv.setItem(i, guiStack);
/* 1362 */         } else if (i - start == 7) {
/* 1363 */           ItemStack guiStack = LoreGuiItem.build("Coins Offered", 
/* 1364 */             ItemManager.getId(Material.NETHER_BRICK_ITEM), 0, new String[] {
/* 1365 */             "§e0 Coins" });
/* 1366 */           inv.setItem(i, guiStack);
/*      */         }
/*      */         else {
/* 1369 */           inv.setItem(i, signStack);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1376 */       start = 18;
/* 1377 */       for (int i = start; i < 9 + start; i++) {
/* 1378 */         inv.setItem(i, signStack);
/*      */       }
/*      */       
/* 1381 */       player.openInventory(inv);
/* 1382 */       return inv;
/*      */     } catch (CivException e) {
/* 1384 */       try { inv = null;signStack = null; label653: try { out = new PrintWriter(new BufferedWriter(new FileWriter("possibleCheaters.txt", true)));
/*      */         } finally { PrintWriter out;
/* 1386 */           inv = signStack; break label653; if (inv != signStack) inv.addSuppressed(signStack);
/*      */         }
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/* 1391 */         CivMessage.sendError(this, "Couldn't trade: " + e.getMessage());
/* 1392 */         CivMessage.sendError(resident, "Couldn't trade: " + e.getMessage()); } }
/* 1393 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean hasTechForItem(ItemStack stack)
/*      */   {
/* 1399 */     if (isInsideArena()) {
/* 1400 */       return true;
/*      */     }
/*      */     
/* 1403 */     LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/* 1404 */     if (craftMat == null) {
/* 1405 */       return true;
/*      */     }
/*      */     
/* 1408 */     if (craftMat.getConfigMaterial().required_tech == null) {
/* 1409 */       return true;
/*      */     }
/*      */     
/* 1412 */     if (!hasTown()) {
/* 1413 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1417 */     String[] split = craftMat.getConfigMaterial().required_tech.split(",");
/* 1418 */     String[] arrayOfString1; int j = (arrayOfString1 = split).length; for (int i = 0; i < j; i++) { String tech = arrayOfString1[i];
/* 1419 */       tech = tech.replace(" ", "");
/* 1420 */       if (!getCiv().hasTechnology(tech)) {
/* 1421 */         return false;
/*      */       }
/*      */     }
/*      */     
/* 1425 */     return true;
/*      */   }
/*      */   
/*      */   public Date getLastKilledTime() {
/* 1429 */     return this.lastKilledTime;
/*      */   }
/*      */   
/*      */   public void setLastKilledTime(Date lastKilledTime) {
/* 1433 */     this.lastKilledTime = lastKilledTime;
/*      */   }
/*      */   
/*      */   public Date getMuteExpires() {
/* 1437 */     return this.muteExpires;
/*      */   }
/*      */   
/*      */   public void setMuteExpires(Date muteExpires) {
/* 1441 */     this.muteExpires = muteExpires;
/*      */   }
/*      */   
/*      */   public String getItemMode() {
/* 1445 */     return this.itemMode;
/*      */   }
/*      */   
/*      */   public void setItemMode(String itemMode) {
/* 1449 */     this.itemMode = itemMode;
/*      */   }
/*      */   
/*      */   public void toggleItemMode() {
/* 1453 */     if (this.itemMode.equals("all")) {
/* 1454 */       this.itemMode = "rare";
/* 1455 */       CivMessage.send(this, "§aOnly displaying rare item drops.");
/* 1456 */     } else if (this.itemMode.equals("rare")) {
/* 1457 */       this.itemMode = "none";
/* 1458 */       CivMessage.send(this, "§aNo no longer displaying item drops.");
/*      */     } else {
/* 1460 */       this.itemMode = "all";
/* 1461 */       CivMessage.send(this, "§aDisplaying all item drops.");
/*      */     }
/* 1463 */     save();
/*      */   }
/*      */   
/*      */   public void setLastIP(String hostAddress) {
/* 1467 */     this.lastIP = hostAddress;
/*      */   }
/*      */   
/*      */   public String getLastIP() {
/* 1471 */     return this.lastIP;
/*      */   }
/*      */   
/*      */   public void teleportHome()
/*      */   {
/*      */     try {
/* 1477 */       Player player = CivGlobal.getPlayer(this);
/* 1478 */       teleportHome(player);
/*      */     }
/*      */     catch (CivException e) {}
/*      */     Player player;
/*      */   }
/*      */   
/*      */   public void teleportHome(Player player) {
/* 1485 */     if (hasTown()) {
/* 1486 */       TownHall townhall = getTown().getTownHall();
/* 1487 */       if (townhall != null) {
/* 1488 */         BlockCoord coord = townhall.getRandomRevivePoint();
/* 1489 */         player.teleport(coord.getLocation());
/*      */       }
/*      */     } else {
/* 1492 */       World world = Bukkit.getWorld("world");
/* 1493 */       player.teleport(world.getSpawnLocation());
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean canDamageControlBlock() {
/* 1498 */     if ((hasTown()) && 
/* 1499 */       (!getCiv().getCapitolStructure().isValid())) {
/* 1500 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1504 */     return true;
/*      */   }
/*      */   
/*      */   public boolean isUsesAntiCheat() throws CivException {
/* 1508 */     CivGlobal.getPlayer(this);
/* 1509 */     return this.usesAntiCheat;
/*      */   }
/*      */   
/*      */   public void setUsesAntiCheat(boolean usesAntiCheat) {
/* 1513 */     this.usesAntiCheat = usesAntiCheat;
/*      */   }
/*      */   
/*      */   public boolean hasTeam() {
/* 1517 */     ArenaTeam team = ArenaTeam.getTeamForResident(this);
/* 1518 */     if (team == null) {
/* 1519 */       return false;
/*      */     }
/* 1521 */     return true;
/*      */   }
/*      */   
/*      */   public ArenaTeam getTeam() {
/* 1525 */     ArenaTeam team = ArenaTeam.getTeamForResident(this);
/* 1526 */     if (team == null) {
/* 1527 */       return null;
/*      */     }
/* 1529 */     return team;
/*      */   }
/*      */   
/*      */   public boolean isTeamLeader() {
/* 1533 */     ArenaTeam team = ArenaTeam.getTeamForResident(this);
/* 1534 */     if (team == null) {
/* 1535 */       return false;
/*      */     }
/*      */     
/* 1538 */     if (team.getLeader() == this) {
/* 1539 */       return true;
/*      */     }
/*      */     
/* 1542 */     return false;
/*      */   }
/*      */   
/*      */   public void saveInventory() {
/*      */     try {
/* 1547 */       Player player = CivGlobal.getPlayer(this);
/* 1548 */       String serial = InventorySerializer.InventoryToString(player.getInventory());
/* 1549 */       setSavedInventory(serial);
/* 1550 */       save();
/*      */     }
/*      */     catch (CivException localCivException) {}
/*      */   }
/*      */   
/*      */   public void clearInventory() {
/*      */     try {
/* 1557 */       Player player = CivGlobal.getPlayer(this);
/* 1558 */       player.getInventory().clear();
/* 1559 */       player.getInventory().setArmorContents(new ItemStack[4]);
/*      */     }
/*      */     catch (CivException localCivException) {}
/*      */   }
/*      */   
/*      */   public void restoreInventory() {
/* 1565 */     if (this.savedInventory == null) {
/* 1566 */       return;
/*      */     }
/*      */     try
/*      */     {
/* 1570 */       Player player = CivGlobal.getPlayer(this);
/* 1571 */       clearInventory();
/* 1572 */       InventorySerializer.StringToInventory(player.getInventory(), this.savedInventory);
/* 1573 */       setSavedInventory(null);
/* 1574 */       save();
/*      */     }
/*      */     catch (CivException e) {
/* 1577 */       e.printStackTrace();
/* 1578 */       setSavedInventory(null);
/* 1579 */       save();
/*      */     }
/*      */   }
/*      */   
/*      */   public String getSavedInventory() {
/* 1584 */     return this.savedInventory;
/*      */   }
/*      */   
/*      */   public void setSavedInventory(String savedInventory) {
/* 1588 */     this.savedInventory = savedInventory;
/*      */   }
/*      */   
/*      */   public Arena getCurrentArena() {
/* 1592 */     if (getTeam() == null) {
/* 1593 */       return null;
/*      */     }
/*      */     
/* 1596 */     return getTeam().getCurrentArena();
/*      */   }
/*      */   
/*      */   public boolean isInsideArena()
/*      */   {
/* 1601 */     if (!hasTeam()) {
/* 1602 */       this.insideArena = false;
/* 1603 */       return false;
/*      */     }
/*      */     try
/*      */     {
/* 1607 */       Player player = CivGlobal.getPlayer(this);
/*      */       
/* 1609 */       if (player.getWorld().getName().equals("world")) {
/* 1610 */         this.insideArena = false;
/* 1611 */         return false;
/*      */       }
/*      */     }
/*      */     catch (CivException e) {
/* 1615 */       return false;
/*      */     }
/*      */     
/* 1618 */     return this.insideArena;
/*      */   }
/*      */   
/*      */   public void setInsideArena(boolean inside) {
/* 1622 */     this.insideArena = inside;
/*      */   }
/*      */   
/*      */   public boolean isProtected() {
/* 1626 */     return this.isProtected;
/*      */   }
/*      */   
/*      */   public void setisProtected(boolean prot) {
/* 1630 */     this.isProtected = prot;
/*      */   }
/*      */   
/*      */   public void showPerkPage(int pageNumber)
/*      */   {
/*      */     try {
/* 1636 */       player = CivGlobal.getPlayer(this);
/*      */     } catch (CivException e) { Player player;
/*      */       return;
/*      */     }
/*      */     Player player;
/* 1641 */     Inventory inv = Bukkit.getServer().createInventory(player, 54, "Perks");
/* 1642 */     Paginator paginator = new Paginator();
/* 1643 */     paginator.paginate(this.perks.values(), pageNumber);
/*      */     
/* 1645 */     for (Object obj : paginator.page) {
/* 1646 */       Perk p = (Perk)obj;
/* 1647 */       ItemStack stack = LoreGuiItem.build(p.configPerk.display_name, 
/* 1648 */         p.configPerk.type_id.intValue(), 
/* 1649 */         p.configPerk.data.intValue(), new String[] { "§6<Click To Activate>", 
/* 1650 */         "§bCount: " + p.count });
/* 1651 */       stack = LoreGuiItem.setAction(stack, "activatePerk:" + p.configPerk.id);
/*      */       
/* 1653 */       inv.addItem(new ItemStack[] { stack });
/*      */     }
/*      */     
/* 1656 */     if (paginator.hasPrevPage) {
/* 1657 */       ItemStack stack = LoreGuiItem.build("Prev Page", ItemManager.getId(Material.PAPER), 0, new String[] { "" });
/* 1658 */       stack = LoreGuiItem.setAction(stack, "showperkpage:" + (pageNumber - 1));
/* 1659 */       inv.setItem(45, stack);
/*      */     }
/*      */     
/* 1662 */     if (paginator.hasNextPage) {
/* 1663 */       ItemStack stack = LoreGuiItem.build("Next Page", ItemManager.getId(Material.PAPER), 0, new String[] { "" });
/* 1664 */       stack = LoreGuiItem.setAction(stack, "showperkpage:" + (pageNumber + 1));
/* 1665 */       inv.setItem(53, stack);
/*      */     }
/*      */     
/* 1668 */     player.openInventory(inv);
/*      */   }
/*      */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\Resident.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
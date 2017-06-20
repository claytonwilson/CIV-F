/*      */ package com.avrgaming.civcraft.camp;
/*      */ 
/*      */ import com.avrgaming.civcraft.components.ConsumeLevelComponent;
/*      */ import com.avrgaming.civcraft.components.ConsumeLevelComponent.Result;
/*      */ import com.avrgaming.civcraft.components.SifterComponent;
/*      */ import com.avrgaming.civcraft.config.CivSettings;
/*      */ import com.avrgaming.civcraft.config.ConfigCampLonghouseLevel;
/*      */ import com.avrgaming.civcraft.config.ConfigCampUpgrade;
/*      */ import com.avrgaming.civcraft.database.SQL;
/*      */ import com.avrgaming.civcraft.database.SQLUpdate;
/*      */ import com.avrgaming.civcraft.exception.CivException;
/*      */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*      */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*      */ import com.avrgaming.civcraft.exception.InvalidObjectException;
/*      */ import com.avrgaming.civcraft.items.components.Tagged;
/*      */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*      */ import com.avrgaming.civcraft.main.CivData;
/*      */ import com.avrgaming.civcraft.main.CivGlobal;
/*      */ import com.avrgaming.civcraft.main.CivLog;
/*      */ import com.avrgaming.civcraft.main.CivMessage;
/*      */ import com.avrgaming.civcraft.object.BuildableDamageBlock;
/*      */ import com.avrgaming.civcraft.object.ControlPoint;
/*      */ import com.avrgaming.civcraft.object.CultureChunk;
/*      */ import com.avrgaming.civcraft.object.EconObject;
/*      */ import com.avrgaming.civcraft.object.Resident;
/*      */ import com.avrgaming.civcraft.object.StructureBlock;
/*      */ import com.avrgaming.civcraft.object.TownChunk;
/*      */ import com.avrgaming.civcraft.permission.PlotPermissions;
/*      */ import com.avrgaming.civcraft.permission.PlotPermissions.Type;
/*      */ import com.avrgaming.civcraft.road.Road;
/*      */ import com.avrgaming.civcraft.road.RoadBlock;
/*      */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*      */ import com.avrgaming.civcraft.structure.Buildable;
/*      */ import com.avrgaming.civcraft.template.Template;
/*      */ import com.avrgaming.civcraft.template.Template.TemplateType;
/*      */ import com.avrgaming.civcraft.threading.TaskMaster;
/*      */ import com.avrgaming.civcraft.util.BlockCoord;
/*      */ import com.avrgaming.civcraft.util.ChunkCoord;
/*      */ import com.avrgaming.civcraft.util.FireworkEffectPlayer;
/*      */ import com.avrgaming.civcraft.util.ItemManager;
/*      */ import com.avrgaming.civcraft.util.MultiInventory;
/*      */ import com.avrgaming.civcraft.util.SimpleBlock;
/*      */ import com.avrgaming.civcraft.util.SimpleBlock.Type;
/*      */ import gpl.AttributeUtil;
/*      */ import java.io.IOException;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import org.bukkit.ChatColor;
/*      */ import org.bukkit.Color;
/*      */ import org.bukkit.Effect;
/*      */ import org.bukkit.FireworkEffect;
/*      */ import org.bukkit.FireworkEffect.Builder;
/*      */ import org.bukkit.FireworkEffect.Type;
/*      */ import org.bukkit.Location;
/*      */ import org.bukkit.Material;
/*      */ import org.bukkit.Sound;
/*      */ import org.bukkit.World;
/*      */ import org.bukkit.block.Block;
/*      */ import org.bukkit.block.Chest;
/*      */ import org.bukkit.block.Furnace;
/*      */ import org.bukkit.block.Sign;
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
/*      */ public class Camp
/*      */   extends Buildable
/*      */ {
/*      */   private String ownerName;
/*      */   private int hitpoints;
/*      */   private int firepoints;
/*      */   private BlockCoord corner;
/*   96 */   private HashMap<String, Resident> members = new HashMap();
/*      */   public static final double SHIFT_OUT = 2.0D;
/*      */   public static final String SUBDIR = "camp";
/*   99 */   private boolean undoable = false;
/*      */   
/*      */ 
/*  102 */   public HashSet<BlockCoord> growthLocations = new HashSet();
/*  103 */   private boolean gardenEnabled = false;
/*      */   
/*      */ 
/*  106 */   public HashMap<BlockCoord, CampBlock> campBlocks = new HashMap();
/*      */   
/*      */ 
/*  109 */   public HashMap<Integer, BlockCoord> firepitBlocks = new HashMap();
/*  110 */   public HashSet<BlockCoord> fireFurnaceBlocks = new HashSet();
/*      */   
/*      */   private Integer coal_per_firepoint;
/*      */   
/*      */   private Integer maxFirePoints;
/*  115 */   public SifterComponent sifter = new SifterComponent();
/*  116 */   public ReentrantLock sifterLock = new ReentrantLock();
/*  117 */   private boolean sifterEnabled = false;
/*      */   
/*      */ 
/*  120 */   public HashSet<BlockCoord> foodDepositPoints = new HashSet();
/*      */   public ConsumeLevelComponent consumeComponent;
/*  122 */   private boolean longhouseEnabled = false;
/*      */   
/*      */ 
/*  125 */   public HashSet<BlockCoord> doors = new HashSet();
/*      */   
/*      */ 
/*      */ 
/*  129 */   public HashMap<BlockCoord, ControlPoint> controlBlocks = new HashMap();
/*      */   
/*      */   private Date nextRaidDate;
/*      */   
/*      */   private int raidLength;
/*  134 */   private HashMap<String, ConfigCampUpgrade> upgrades = new HashMap();
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
/*      */   public static final String TABLE_NAME = "CAMPS";
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
/*      */   public static void newCamp(Resident resident, Player player, String name)
/*      */   {
/*  180 */     TaskMaster.syncTask(new Runnable()
/*      */     {
/*      */       String name;
/*      */       Player player;
/*      */       
/*      */       public void run()
/*      */       {
/*      */         try
/*      */         {
/*  153 */           Camp existCamp = CivGlobal.getCamp(this.name);
/*  154 */           if (existCamp != null) {
/*  155 */             throw new CivException("A camp named " + this.name + " already exists!");
/*      */           }
/*      */           
/*  158 */           ItemStack stack = this.player.getItemInHand();
/*  159 */           LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/*  160 */           if ((craftMat == null) || (!craftMat.hasComponent("FoundCamp"))) {
/*  161 */             throw new CivException("You must be holding an item that can found a camp.");
/*      */           }
/*      */           
/*  164 */           Camp camp = new Camp(Camp.this, this.name, this.player.getLocation());
/*  165 */           camp.buildCamp(this.player, this.player.getLocation());
/*  166 */           camp.setUndoable(true);
/*  167 */           CivGlobal.addCamp(camp);
/*  168 */           camp.save();
/*      */           
/*  170 */           CivMessage.sendSuccess(this.player, "You have set up camp!");
/*  171 */           ItemStack newStack = new ItemStack(Material.AIR);
/*  172 */           this.player.setItemInHand(newStack);
/*  173 */           Camp.this.clearInteractiveMode();
/*      */         } catch (CivException e) {
/*  175 */           CivMessage.sendError(this.player, e.getMessage());
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   public Camp(Resident owner, String name, Location corner)
/*      */     throws CivException
/*      */   {
/*  184 */     this.ownerName = owner.getName();
/*  185 */     this.corner = new BlockCoord(corner);
/*      */     try {
/*  187 */       setName(name);
/*      */     }
/*      */     catch (InvalidNameException e1) {
/*  190 */       throw new CivException("Invalid name, please choose another.");
/*      */     }
/*  192 */     this.nextRaidDate = new Date();
/*  193 */     this.nextRaidDate.setTime(this.nextRaidDate.getTime() + 86400000L);
/*      */     try
/*      */     {
/*  196 */       this.firepoints = CivSettings.getInteger(CivSettings.campConfig, "camp.firepoints").intValue();
/*  197 */       this.hitpoints = CivSettings.getInteger(CivSettings.campConfig, "camp.hitpoints").intValue();
/*      */     } catch (InvalidConfiguration e) {
/*  199 */       e.printStackTrace();
/*      */     }
/*  201 */     loadSettings();
/*      */   }
/*      */   
/*      */   public Camp(ResultSet rs) throws SQLException, InvalidNameException, InvalidObjectException, CivException {
/*  205 */     load(rs);
/*  206 */     loadSettings();
/*      */   }
/*      */   
/*      */   public void loadSettings()
/*      */   {
/*      */     try {
/*  212 */       this.coal_per_firepoint = CivSettings.getInteger(CivSettings.campConfig, "camp.coal_per_firepoint");
/*  213 */       this.maxFirePoints = CivSettings.getInteger(CivSettings.campConfig, "camp.firepoints");
/*      */       
/*      */ 
/*  216 */       double gold_nugget_chance = CivSettings.getDouble(CivSettings.campConfig, "camp.sifter_gold_nugget_chance");
/*  217 */       double iron_ignot_chance = CivSettings.getDouble(CivSettings.campConfig, "camp.sifter_iron_ingot_chance");
/*      */       
/*  219 */       this.raidLength = CivSettings.getInteger(CivSettings.campConfig, "camp.raid_length").intValue();
/*      */       
/*  221 */       this.sifter.addSiftItem(ItemManager.getId(Material.COBBLESTONE), (short)0, gold_nugget_chance, ItemManager.getId(Material.GOLD_NUGGET), (short)0, 1);
/*  222 */       this.sifter.addSiftItem(ItemManager.getId(Material.COBBLESTONE), (short)0, iron_ignot_chance, ItemManager.getId(Material.IRON_INGOT), (short)0, 1);
/*  223 */       this.sifter.addSiftItem(ItemManager.getId(Material.COBBLESTONE), (short)0, 1.0D, ItemManager.getId(Material.GRAVEL), (short)0, 1);
/*      */       
/*  225 */       this.consumeComponent = new ConsumeLevelComponent();
/*  226 */       this.consumeComponent.setBuildable(this);
/*  227 */       for (ConfigCampLonghouseLevel lvl : CivSettings.longhouseLevels.values()) {
/*  228 */         this.consumeComponent.addLevel(lvl.level, lvl.count);
/*  229 */         this.consumeComponent.setConsumes(lvl.level, lvl.consumes);
/*      */       }
/*  231 */       this.consumeComponent.onLoad();
/*      */     }
/*      */     catch (InvalidConfiguration e)
/*      */     {
/*  235 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void init()
/*      */     throws SQLException
/*      */   {
/*  242 */     if (!SQL.hasTable("CAMPS")) {
/*  243 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + "CAMPS" + " (" + 
/*  244 */         "`id` int(11) unsigned NOT NULL auto_increment," + 
/*  245 */         "`name` VARCHAR(64) NOT NULL," + 
/*  246 */         "`owner_name` mediumtext NOT NULL," + 
/*  247 */         "`firepoints` int(11) DEFAULT 0," + 
/*  248 */         "`next_raid_date` long," + 
/*  249 */         "`corner` mediumtext," + 
/*  250 */         "`upgrades` mediumtext," + 
/*  251 */         "`template_name` mediumtext," + 
/*  252 */         "PRIMARY KEY (`id`)" + ")";
/*      */       
/*  254 */       SQL.makeTable(table_create);
/*  255 */       CivLog.info("Created CAMPS table");
/*      */     } else {
/*  257 */       CivLog.info("CAMPS table OK!");
/*  258 */       SQL.makeCol("name", "VARCHAR(64) NOT NULL", "CAMPS");
/*  259 */       SQL.makeCol("upgrades", "mediumtext", "CAMPS");
/*  260 */       SQL.makeCol("template_name", "mediumtext", "CAMPS");
/*  261 */       SQL.makeCol("next_raid_date", "long", "CAMPS");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void load(ResultSet rs)
/*      */     throws SQLException, InvalidNameException, InvalidObjectException, CivException
/*      */   {
/*  269 */     setId(rs.getInt("id"));
/*  270 */     setName(rs.getString("name"));
/*  271 */     this.ownerName = rs.getString("owner_name");
/*  272 */     this.corner = new BlockCoord(rs.getString("corner"));
/*  273 */     this.nextRaidDate = new Date(rs.getLong("next_raid_date"));
/*  274 */     setTemplateName(rs.getString("template_name"));
/*      */     try
/*      */     {
/*  277 */       this.hitpoints = CivSettings.getInteger(CivSettings.campConfig, "camp.hitpoints").intValue();
/*      */     } catch (InvalidConfiguration e) {
/*  279 */       e.printStackTrace();
/*      */     }
/*      */     
/*  282 */     this.firepoints = rs.getInt("firepoints");
/*      */     
/*  284 */     if (this.ownerName == null) {
/*  285 */       CivLog.error("COULD NOT FIND OWNER FOR CAMP ID:" + getId());
/*  286 */       return;
/*      */     }
/*      */     
/*  289 */     loadUpgradeString(rs.getString("upgrades"));
/*  290 */     bindCampBlocks();
/*      */   }
/*      */   
/*      */   public void save()
/*      */   {
/*  295 */     SQLUpdate.add(this);
/*      */   }
/*      */   
/*      */   public void saveNow()
/*      */     throws SQLException
/*      */   {
/*  301 */     HashMap<String, Object> hashmap = new HashMap();
/*  302 */     hashmap.put("name", getName());
/*  303 */     hashmap.put("owner_name", getOwner().getName());
/*  304 */     hashmap.put("firepoints", Integer.valueOf(this.firepoints));
/*  305 */     hashmap.put("corner", this.corner.toString());
/*  306 */     hashmap.put("next_raid_date", Long.valueOf(this.nextRaidDate.getTime()));
/*  307 */     hashmap.put("upgrades", getUpgradeSaveString());
/*  308 */     hashmap.put("template_name", getSavedTemplatePath());
/*      */     
/*  310 */     SQL.updateNamedObject(this, hashmap, "CAMPS");
/*      */   }
/*      */   
/*      */   public void delete()
/*      */     throws SQLException
/*      */   {
/*  316 */     for (Resident resident : this.members.values()) {
/*  317 */       resident.setCamp(null);
/*  318 */       resident.save();
/*      */     }
/*      */     
/*  321 */     unbindCampBlocks();
/*  322 */     SQL.deleteNamedObject(this, "CAMPS");
/*  323 */     CivGlobal.removeCamp(getName());
/*      */   }
/*      */   
/*      */   public void loadUpgradeString(String upgrades) {
/*  327 */     String[] split = upgrades.split(",");
/*  328 */     String[] arrayOfString1; int j = (arrayOfString1 = split).length; for (int i = 0; i < j; i++) { String id = arrayOfString1[i];
/*      */       
/*  330 */       if ((id != null) && (!id.equalsIgnoreCase("")))
/*      */       {
/*      */ 
/*  333 */         id = id.trim();
/*  334 */         ConfigCampUpgrade upgrade = (ConfigCampUpgrade)CivSettings.campUpgrades.get(id);
/*  335 */         if (upgrade == null) {
/*  336 */           CivLog.warning("Unknown upgrade id " + id + " during load.");
/*      */         }
/*      */         else
/*      */         {
/*  340 */           this.upgrades.put(id, upgrade);
/*  341 */           upgrade.processAction(this);
/*      */         }
/*      */       }
/*      */     } }
/*      */   
/*  346 */   public String getUpgradeSaveString() { String out = "";
/*  347 */     for (ConfigCampUpgrade upgrade : this.upgrades.values()) {
/*  348 */       out = out + upgrade.id + ",";
/*      */     }
/*      */     
/*  351 */     return out;
/*      */   }
/*      */   
/*      */   public void destroy() {
/*  355 */     fancyCampBlockDestory();
/*      */     try {
/*  357 */       delete();
/*      */     } catch (SQLException e) {
/*  359 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   public void disband() {
/*  364 */     undoFromTemplate();
/*      */     try
/*      */     {
/*  367 */       delete();
/*      */     } catch (SQLException e) {
/*  369 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   public void undo() {
/*  374 */     undoFromTemplate();
/*      */     try
/*      */     {
/*  377 */       delete();
/*      */     } catch (SQLException e) {
/*  379 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   public void undoFromTemplate()
/*      */   {
/*  385 */     Template undo_tpl = new Template();
/*      */     try {
/*  387 */       undo_tpl.initUndoTemplate(getCorner().toString(), "camp");
/*  388 */       undo_tpl.buildUndoTemplate(undo_tpl, getCorner().getBlock());
/*  389 */       undo_tpl.deleteUndoTemplate(getCorner().toString(), "camp");
/*      */     }
/*      */     catch (IOException|CivException e1) {
/*  392 */       e1.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   public void buildCamp(Player player, Location center) throws CivException
/*      */   {
/*      */     try
/*      */     {
/*  400 */       templateFile = CivSettings.getString(CivSettings.campConfig, "camp.template");
/*      */     } catch (InvalidConfiguration e) { String templateFile;
/*  402 */       e.printStackTrace(); return;
/*      */     }
/*      */     String templateFile;
/*  405 */     Resident resident = CivGlobal.getResident(player);
/*      */     
/*      */     Template tpl;
/*      */     
/*  409 */     if (resident.desiredTemplate == null)
/*      */     {
/*      */       try {
/*  412 */         String templatePath = Template.getTemplateFilePath(templateFile, Template.getDirection(center), Template.TemplateType.STRUCTURE, "default");
/*  413 */         setTemplateName(templatePath);
/*      */         
/*  415 */         tpl = Template.getTemplate(templatePath, center);
/*      */       } catch (IOException e) { Template tpl;
/*  417 */         e.printStackTrace();
/*  418 */         return;
/*      */       } catch (CivException e) {
/*  420 */         e.printStackTrace();
/*  421 */         return;
/*      */       }
/*      */     } else {
/*  424 */       tpl = resident.desiredTemplate;
/*  425 */       resident.desiredTemplate = null;
/*  426 */       setTemplateName(tpl.getFilepath());
/*      */     }
/*      */     
/*  429 */     this.corner.setFromLocation(repositionCenter(center, tpl.dir(), tpl.size_x, tpl.size_z));
/*  430 */     checkBlockPermissionsAndRestrictions(player, this.corner.getBlock(), tpl.size_x, tpl.size_y, tpl.size_z);
/*      */     try {
/*  432 */       tpl.saveUndoTemplate(getCorner().toString(), "camp", getCorner().getLocation());
/*      */     } catch (IOException e1) {
/*  434 */       e1.printStackTrace();
/*      */     }
/*  436 */     buildCampFromTemplate(tpl, this.corner);
/*  437 */     processCommandSigns(tpl, this.corner);
/*      */     try {
/*  439 */       saveNow();
/*      */     } catch (SQLException e) {
/*  441 */       e.printStackTrace();
/*  442 */       throw new CivException("Internal SQL Error.");
/*      */     }
/*      */     
/*  445 */     addMember(resident);
/*  446 */     resident.save();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void reprocessCommandSigns()
/*      */   {
/*      */     try
/*      */     {
/*  456 */       tpl = Template.getTemplate(getSavedTemplatePath(), null);
/*      */     } catch (IOException|CivException e) { Template tpl;
/*  458 */       e.printStackTrace(); return;
/*      */     }
/*      */     
/*      */     Template tpl;
/*  462 */     processCommandSigns(tpl, this.corner);
/*      */   }
/*      */   
/*      */   private void processCommandSigns(Template tpl, BlockCoord corner) {
/*  466 */     for (BlockCoord relativeCoord : tpl.commandBlockRelativeLocations) {
/*  467 */       SimpleBlock sb = tpl.blocks[relativeCoord.getX()][relativeCoord.getY()][relativeCoord.getZ()];
/*  468 */       BlockCoord absCoord = new BlockCoord(corner.getBlock().getRelative(relativeCoord.getX(), relativeCoord.getY(), relativeCoord.getZ()));
/*      */       String str;
/*  470 */       switch ((str = sb.command).hashCode()) {case -2139491776:  if (str.equals("/sifter")) {} break; case -1747576099:  if (str.equals("/foodinput")) {} break; case -754406701:  if (str.equals("/firefurnace")) {} break; case -541735786:  if (str.equals("/firepit")) {} break; case 46494813:  if (str.equals("/door")) {} break; case 46548709:  if (str.equals("/fire")) {} break; case 490167712:  if (str.equals("/literal")) {} break; case 1259250254:  if (str.equals("/control")) {} break; case 1816785633:  if (str.equals("/gardensign")) break; break; case 1820508854:  if (!str.equals("/growth")) {
/*      */           continue;
/*  472 */           if (!this.gardenEnabled) {
/*  473 */             ItemManager.setTypeId(absCoord.getBlock(), ItemManager.getId(Material.SIGN_POST));
/*  474 */             ItemManager.setData(absCoord.getBlock(), sb.getData());
/*      */             
/*  476 */             Sign sign = (Sign)absCoord.getBlock().getState();
/*  477 */             sign.setLine(0, "Garden Disabled");
/*  478 */             sign.setLine(1, "Upgrade using");
/*  479 */             sign.setLine(2, "/camp upgrade");
/*  480 */             sign.setLine(3, "command");
/*  481 */             sign.update();
/*  482 */             addCampBlock(absCoord);
/*      */           } else {
/*  484 */             ItemManager.setTypeId(absCoord.getBlock(), ItemManager.getId(Material.AIR));
/*  485 */             removeCampBlock(absCoord);
/*      */           }
/*      */           
/*      */         }
/*  489 */         else if (this.gardenEnabled) {
/*  490 */           this.growthLocations.add(absCoord);
/*  491 */           CivGlobal.vanillaGrowthLocations.add(absCoord);
/*      */           
/*  493 */           Block b = absCoord.getBlock();
/*  494 */           if (ItemManager.getId(b) != 60) {
/*  495 */             ItemManager.setTypeId(b, 60);
/*      */           }
/*      */           
/*  498 */           addCampBlock(absCoord, true);
/*  499 */           addCampBlock(new BlockCoord(absCoord.getBlock().getRelative(0, 1, 0)), true);
/*      */         } else {
/*  501 */           addCampBlock(absCoord);
/*  502 */           addCampBlock(new BlockCoord(absCoord.getBlock().getRelative(0, 1, 0)));
/*      */           
/*  504 */           continue;
/*      */           
/*  506 */           this.firepitBlocks.put(Integer.valueOf((String)sb.keyvalues.get("id")), absCoord);
/*  507 */           addCampBlock(absCoord);
/*  508 */           continue;
/*      */           
/*  510 */           ItemManager.setTypeId(absCoord.getBlock(), ItemManager.getId(Material.FIRE));
/*  511 */           continue;
/*      */           
/*  513 */           this.fireFurnaceBlocks.add(absCoord);
/*  514 */           byte data = CivData.convertSignDataToChestData((byte)sb.getData());
/*  515 */           ItemManager.setTypeId(absCoord.getBlock(), ItemManager.getId(Material.FURNACE));
/*  516 */           ItemManager.setData(absCoord.getBlock(), data);
/*  517 */           addCampBlock(absCoord);
/*      */           
/*  519 */           continue;
/*      */           
/*  521 */           Integer id = Integer.valueOf((String)sb.keyvalues.get("id"));
/*  522 */           switch (id.intValue()) {
/*      */           case 0: 
/*  524 */             this.sifter.setSourceCoord(absCoord);
/*  525 */             break;
/*      */           case 1: 
/*  527 */             this.sifter.setDestCoord(absCoord);
/*  528 */             break;
/*      */           default: 
/*  530 */             CivLog.warning("Unknown ID for sifter in camp:" + id);
/*      */           }
/*      */           
/*      */           
/*  534 */           if (this.sifterEnabled) {
/*  535 */             ItemManager.setTypeId(absCoord.getBlock(), ItemManager.getId(Material.CHEST));
/*  536 */             byte data2 = CivData.convertSignDataToChestData((byte)sb.getData());
/*  537 */             ItemManager.setData(absCoord.getBlock(), data2);
/*      */           } else {
/*      */             try {
/*  540 */               ItemManager.setTypeId(absCoord.getBlock(), ItemManager.getId(Material.SIGN_POST));
/*  541 */               ItemManager.setData(absCoord.getBlock(), sb.getData());
/*      */               
/*  543 */               Sign sign = (Sign)absCoord.getBlock().getState();
/*  544 */               sign.setLine(0, "Sifter Disabled");
/*  545 */               sign.setLine(1, "Upgrade using");
/*  546 */               sign.setLine(2, "/camp upgrade");
/*  547 */               sign.setLine(3, "command");
/*  548 */               sign.update();
/*      */             } catch (Exception e) {
/*  550 */               e.printStackTrace();
/*      */             }
/*      */           }
/*  553 */           addCampBlock(absCoord);
/*  554 */           continue;
/*      */           
/*  556 */           if (this.longhouseEnabled) {
/*  557 */             this.foodDepositPoints.add(absCoord);
/*  558 */             ItemManager.setTypeId(absCoord.getBlock(), ItemManager.getId(Material.CHEST));
/*  559 */             byte data3 = CivData.convertSignDataToChestData((byte)sb.getData());
/*  560 */             ItemManager.setData(absCoord.getBlock(), data3);
/*      */           } else {
/*  562 */             ItemManager.setTypeId(absCoord.getBlock(), ItemManager.getId(Material.SIGN_POST));
/*  563 */             ItemManager.setData(absCoord.getBlock(), sb.getData());
/*      */             
/*  565 */             Sign sign = (Sign)absCoord.getBlock().getState();
/*  566 */             sign.setLine(0, "Longhouse");
/*  567 */             sign.setLine(1, "Disabled");
/*  568 */             sign.setLine(2, "Upgrade using");
/*  569 */             sign.setLine(3, "/camp upgrade");
/*  570 */             sign.update();
/*      */           }
/*  572 */           addCampBlock(absCoord);
/*  573 */           continue;
/*      */           
/*  575 */           this.doors.add(absCoord);
/*  576 */           Block doorBlock = absCoord.getBlock();
/*  577 */           Block doorBlock2 = absCoord.getBlock().getRelative(0, 1, 0);
/*      */           
/*      */ 
/*  580 */           byte topData = 8;
/*  581 */           byte bottomData = 0;
/*  582 */           byte doorDirection = CivData.convertSignDataToDoorDirectionData((byte)sb.getData());
/*  583 */           bottomData = (byte)(bottomData | doorDirection);
/*      */           
/*      */ 
/*  586 */           ItemManager.setTypeIdAndData(doorBlock, ItemManager.getId(Material.WOODEN_DOOR), bottomData, false);
/*  587 */           ItemManager.setTypeIdAndData(doorBlock2, ItemManager.getId(Material.WOODEN_DOOR), topData, false);
/*      */           
/*  589 */           addCampBlock(new BlockCoord(doorBlock));
/*  590 */           addCampBlock(new BlockCoord(doorBlock2));
/*  591 */           continue;
/*      */           
/*  593 */           createControlPoint(absCoord);
/*  594 */           continue;
/*      */           
/*      */ 
/*  597 */           ItemManager.setTypeId(absCoord.getBlock(), ItemManager.getId(Material.WALL_SIGN));
/*  598 */           ItemManager.setData(absCoord.getBlock(), sb.getData());
/*      */           
/*  600 */           Sign sign = (Sign)absCoord.getBlock().getState();
/*  601 */           sign.setLine(0, sb.message[0]);
/*  602 */           sign.setLine(1, sb.message[1]);
/*  603 */           sign.setLine(2, sb.message[2]);
/*  604 */           sign.setLine(3, sb.message[3]);
/*  605 */           sign.update();
/*      */         }
/*      */         break;
/*      */       }
/*      */     }
/*  610 */     updateFirepit();
/*      */   }
/*      */   
/*      */   private void removeCampBlock(BlockCoord absCoord) {
/*  614 */     this.campBlocks.remove(absCoord);
/*  615 */     CivGlobal.removeCampBlock(absCoord);
/*      */   }
/*      */   
/*      */   private void updateFirepit() {
/*      */     try {
/*  620 */       int maxFirePoints = CivSettings.getInteger(CivSettings.campConfig, "camp.firepoints").intValue();
/*  621 */       int totalFireBlocks = this.firepitBlocks.size();
/*      */       
/*  623 */       double percentLeft = this.firepoints / maxFirePoints;
/*      */       
/*      */ 
/*  626 */       int litFires = (int)(percentLeft * totalFireBlocks);
/*      */       
/*  628 */       for (int i = 0; i < totalFireBlocks; i++) {
/*  629 */         BlockCoord next = (BlockCoord)this.firepitBlocks.get(Integer.valueOf(i));
/*  630 */         if (next == null) {
/*  631 */           CivLog.warning("Couldn't find firepit id:" + i);
/*      */ 
/*      */ 
/*      */         }
/*  635 */         else if (i < litFires) {
/*  636 */           ItemManager.setTypeId(next.getBlock(), 51);
/*      */         } else {
/*  638 */           ItemManager.setTypeId(next.getBlock(), 0);
/*      */         }
/*      */       }
/*      */     } catch (InvalidConfiguration e) {
/*  642 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   public void processFirepoints()
/*      */   {
/*  648 */     MultiInventory mInv = new MultiInventory();
/*  649 */     for (BlockCoord bcoord : this.fireFurnaceBlocks) {
/*  650 */       Furnace furnace = (Furnace)bcoord.getBlock().getState();
/*  651 */       mInv.addInventory(furnace.getInventory());
/*      */     }
/*      */     
/*  654 */     if (mInv.contains(null, CivData.COAL, (short)0, this.coal_per_firepoint.intValue())) {
/*      */       try {
/*  656 */         mInv.removeItem(CivData.COAL, this.coal_per_firepoint.intValue());
/*      */       } catch (CivException e) {
/*  658 */         e.printStackTrace();
/*      */       }
/*      */       
/*  661 */       this.firepoints += 1;
/*  662 */       if (this.firepoints > this.maxFirePoints.intValue()) {
/*  663 */         this.firepoints = this.maxFirePoints.intValue();
/*      */       }
/*      */     } else {
/*  666 */       this.firepoints -= 1;
/*  667 */       CivMessage.sendCamp(this, "§eOur campfire doesn't have enough coal to keep burning, its starting to go out! " + this.firepoints + " hours left.");
/*      */       
/*  669 */       double percentLeft = this.firepoints / this.maxFirePoints.intValue();
/*  670 */       if (percentLeft < 0.3D) {
/*  671 */         CivMessage.sendCamp(this, "§e" + ChatColor.BOLD + "Warning! Our campfire is less than 30% out! We need to stock it with more coal or our camp will be destroyed!");
/*      */       }
/*      */       
/*  674 */       if (this.firepoints < 0) {
/*  675 */         destroy();
/*      */       }
/*      */     }
/*      */     
/*  679 */     save();
/*  680 */     updateFirepit();
/*      */   }
/*      */   
/*      */   public void processLonghouse() {
/*  684 */     MultiInventory mInv = new MultiInventory();
/*      */     
/*  686 */     for (BlockCoord bcoord : this.foodDepositPoints) {
/*  687 */       Block b = bcoord.getBlock();
/*  688 */       if ((b.getState() instanceof Chest)) {
/*      */         try {
/*  690 */           mInv.addInventory(((Chest)b.getState()).getInventory());
/*      */         } catch (Exception e) {
/*  692 */           e.printStackTrace();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  697 */     if (mInv.getInventoryCount() == 0) {
/*  698 */       CivMessage.sendCamp(this, "§cYour camp's longhouse could not find an input chest for food! Nothing happens.");
/*  699 */       return;
/*      */     }
/*      */     
/*  702 */     this.consumeComponent.setSource(mInv);
/*  703 */     ConsumeLevelComponent.Result result = this.consumeComponent.processConsumption();
/*  704 */     this.consumeComponent.onSave();
/*      */     
/*  706 */     switch (result) {
/*      */     case LEVELUP: 
/*  708 */       CivMessage.sendCamp(this, "§aYour camp's longhouse §cstarved" + this.consumeComponent.getCountString() + "§a" + " and generated no coins.");
/*  709 */       return;
/*      */     case STAGNATE: 
/*  711 */       CivMessage.sendCamp(this, "§aYour camp's longhouse §cstarved and leveled-down§a and generated no coins.");
/*  712 */       return;
/*      */     case GROW: 
/*  714 */       CivMessage.sendCamp(this, "§aYour camp's longhouse §estagnated§a and generated no coins.");
/*  715 */       return;
/*      */     case UNKNOWN: 
/*  717 */       CivMessage.sendCamp(this, "§aYour camp's longhouse has done §5something unknown§a and generated no coins.");
/*  718 */       return;
/*      */     }
/*      */     
/*      */     
/*      */ 
/*  723 */     ConfigCampLonghouseLevel lvl = null;
/*  724 */     if (result == ConsumeLevelComponent.Result.LEVELUP) {
/*  725 */       lvl = (ConfigCampLonghouseLevel)CivSettings.longhouseLevels.get(Integer.valueOf(this.consumeComponent.getLevel() - 1));
/*      */     } else {
/*  727 */       lvl = (ConfigCampLonghouseLevel)CivSettings.longhouseLevels.get(Integer.valueOf(this.consumeComponent.getLevel()));
/*      */     }
/*      */     
/*  730 */     double total_coins = lvl.coins;
/*  731 */     getOwner().getTreasury().deposit(total_coins);
/*      */     
/*  733 */     LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterialFromId("mat_token_of_leadership");
/*  734 */     if (craftMat != null) {
/*  735 */       ItemStack token = LoreCraftableMaterial.spawn(craftMat);
/*      */       
/*  737 */       Tagged tag = (Tagged)craftMat.getComponent("Tagged");
/*  738 */       token = tag.addTag(token, getOwnerName());
/*      */       
/*  740 */       AttributeUtil attrs = new AttributeUtil(token);
/*  741 */       attrs.addLore("§7" + getOwnerName());
/*  742 */       token = attrs.getStack();
/*      */       
/*  744 */       mInv.addItem(token);
/*      */     }
/*      */     
/*  747 */     String stateMessage = "";
/*  748 */     switch (result) {
/*      */     case LEVELDOWN: 
/*  750 */       stateMessage = "§2grew" + this.consumeComponent.getCountString() + "§a";
/*  751 */       break;
/*      */     case MAXED: 
/*  753 */       stateMessage = "§2leveled up§a";
/*  754 */       break;
/*      */     case STARVE: 
/*  756 */       stateMessage = "§2is maxed" + this.consumeComponent.getCountString() + "§a";
/*  757 */       break;
/*      */     }
/*      */     
/*      */     
/*      */ 
/*  762 */     CivMessage.sendCamp(this, "§aYour camp's longhouse " + stateMessage + " and generated " + total_coins + " coins. Coins were given to the camp's owner.");
/*      */   }
/*      */   
/*      */   private void buildCampFromTemplate(Template tpl, BlockCoord corner)
/*      */   {
/*  767 */     Block cornerBlock = corner.getBlock();
/*  768 */     for (int x = 0; x < tpl.size_x; x++) {
/*  769 */       for (int y = 0; y < tpl.size_y; y++) {
/*  770 */         for (int z = 0; z < tpl.size_z; z++) {
/*  771 */           Block nextBlock = cornerBlock.getRelative(x, y, z);
/*      */           
/*  773 */           if (tpl.blocks[x][y][z].specialType != SimpleBlock.Type.COMMAND)
/*      */           {
/*      */ 
/*      */ 
/*  777 */             if (tpl.blocks[x][y][z].specialType == SimpleBlock.Type.LITERAL)
/*      */             {
/*  779 */               tpl.blocks[x][y][z].command = "/literal";
/*  780 */               tpl.commandBlockRelativeLocations.add(new BlockCoord(cornerBlock.getWorld().getName(), x, y, z));
/*      */             }
/*      */             else
/*      */             {
/*      */               try {
/*  785 */                 if (ItemManager.getId(nextBlock) != tpl.blocks[x][y][z].getType()) {
/*  786 */                   ItemManager.setTypeId(nextBlock, tpl.blocks[x][y][z].getType());
/*  787 */                   ItemManager.setData(nextBlock, tpl.blocks[x][y][z].getData());
/*      */                 }
/*      */                 
/*      */ 
/*  791 */                 if (ItemManager.getId(nextBlock) != 0) {
/*  792 */                   addCampBlock(new BlockCoord(nextBlock.getLocation()));
/*      */                 }
/*      */               } catch (Exception e) {
/*  795 */                 CivLog.error(e.getMessage());
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void bindCampBlocks()
/*      */   {
/*      */     try
/*      */     {
/*  811 */       tpl = Template.getTemplate(getSavedTemplatePath(), null);
/*      */     } catch (IOException e) { Template tpl;
/*  813 */       e.printStackTrace();
/*  814 */       return;
/*      */     } catch (CivException e) {
/*  816 */       e.printStackTrace(); return;
/*      */     }
/*      */     
/*      */     Template tpl;
/*  820 */     for (int y = 0; y < tpl.size_y; y++) {
/*  821 */       for (int z = 0; z < tpl.size_z; z++) {
/*  822 */         for (int x = 0; x < tpl.size_x; x++) {
/*  823 */           int relx = getCorner().getX() + x;
/*  824 */           int rely = getCorner().getY() + y;
/*  825 */           int relz = getCorner().getZ() + z;
/*      */           
/*  827 */           BlockCoord coord = new BlockCoord(getCorner().getWorldname(), relx, rely, relz);
/*      */           
/*  829 */           if (tpl.blocks[x][y][z].getType() != 0)
/*      */           {
/*      */ 
/*      */ 
/*  833 */             if (tpl.blocks[x][y][z].specialType != SimpleBlock.Type.COMMAND)
/*      */             {
/*      */ 
/*      */ 
/*  837 */               addCampBlock(coord); }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  842 */     processCommandSigns(tpl, this.corner);
/*      */   }
/*      */   
/*      */   protected Location repositionCenter(Location center, String dir, double x_size, double z_size) throws CivException
/*      */   {
/*  847 */     Location loc = new Location(center.getWorld(), 
/*  848 */       center.getX(), center.getY(), center.getZ(), 
/*  849 */       center.getYaw(), center.getPitch());
/*      */     
/*      */ 
/*  852 */     if (dir.equalsIgnoreCase("east")) {
/*  853 */       loc.setZ(loc.getZ() - z_size / 2.0D);
/*  854 */       loc.setX(loc.getX() + 2.0D);
/*      */     }
/*  856 */     else if (dir.equalsIgnoreCase("west")) {
/*  857 */       loc.setZ(loc.getZ() - z_size / 2.0D);
/*  858 */       loc.setX(loc.getX() - (2.0D + x_size));
/*      */ 
/*      */     }
/*  861 */     else if (dir.equalsIgnoreCase("north")) {
/*  862 */       loc.setX(loc.getX() - x_size / 2.0D);
/*  863 */       loc.setZ(loc.getZ() - (2.0D + z_size));
/*      */     }
/*  865 */     else if (dir.equalsIgnoreCase("south")) {
/*  866 */       loc.setX(loc.getX() - x_size / 2.0D);
/*  867 */       loc.setZ(loc.getZ() + 2.0D);
/*      */     }
/*      */     
/*      */ 
/*  871 */     return loc;
/*      */   }
/*      */   
/*      */   protected void checkBlockPermissionsAndRestrictions(Player player, Block centerBlock, int regionX, int regionY, int regionZ) throws CivException
/*      */   {
/*  876 */     ChunkCoord ccoord = new ChunkCoord(centerBlock.getLocation());
/*  877 */     CultureChunk cc = CivGlobal.getCultureChunk(ccoord);
/*  878 */     if (cc != null) {
/*  879 */       throw new CivException("You cannot build a camp inside a civilization's culture.");
/*      */     }
/*      */     
/*  882 */     if (player.getLocation().getY() >= 200.0D) {
/*  883 */       throw new CivException("You're too high to build camps.");
/*      */     }
/*      */     
/*  886 */     if (regionY + centerBlock.getLocation().getBlockY() >= 255) {
/*  887 */       throw new CivException("Cannot build camp here, would go over the minecraft height limit.");
/*      */     }
/*      */     
/*  890 */     if (!player.isOp()) {
/*  891 */       Buildable.validateDistanceFromSpawn(centerBlock.getLocation());
/*      */     }
/*      */     
/*  894 */     int yTotal = 0;
/*  895 */     int yCount = 0;
/*      */     
/*      */ 
/*  898 */     LinkedList<RoadBlock> deletedRoadBlocks = new LinkedList();
/*  899 */     int y; for (int x = 0; x < regionX; x++) {
/*  900 */       for (y = 0; y < regionY; y++) {
/*  901 */         for (int z = 0; z < regionZ; z++) {
/*  902 */           Block b = centerBlock.getRelative(x, y, z);
/*      */           
/*  904 */           if (ItemManager.getId(b) == 54) {
/*  905 */             throw new CivException("Cannot build here, would destroy chest.");
/*      */           }
/*      */           
/*  908 */           BlockCoord coord = new BlockCoord(b);
/*  909 */           ChunkCoord chunkCoord = new ChunkCoord(coord.getLocation());
/*      */           
/*  911 */           TownChunk tc = CivGlobal.getTownChunk(chunkCoord);
/*  912 */           if ((tc != null) && (!tc.perms.hasPermission(PlotPermissions.Type.DESTROY, CivGlobal.getResident(player))))
/*      */           {
/*  914 */             throw new CivException("Cannot build here, you need DESTROY permissions to the block at " + b.getX() + "," + b.getY() + "," + b.getZ());
/*      */           }
/*      */           
/*  917 */           if (CivGlobal.getProtectedBlock(coord) != null) {
/*  918 */             throw new CivException("Cannot build here, protected blocks in the way.");
/*      */           }
/*      */           
/*  921 */           if (CivGlobal.getStructureBlock(coord) != null) {
/*  922 */             throw new CivException("Cannot build here, structure blocks in the way.");
/*      */           }
/*      */           
/*  925 */           if (CivGlobal.getFarmChunk(chunkCoord) != null) {
/*  926 */             throw new CivException("Cannot build here, in the same chunk as a farm improvement.");
/*      */           }
/*      */           
/*  929 */           if (CivGlobal.getWallChunk(chunkCoord) != null) {
/*  930 */             throw new CivException("Cannot build here, in the same chunk as a wall improvement.");
/*      */           }
/*      */           
/*  933 */           if (CivGlobal.getCampBlock(coord) != null) {
/*  934 */             throw new CivException("Cannot build here, a camp is in the way.");
/*      */           }
/*      */           
/*  937 */           yTotal += b.getWorld().getHighestBlockYAt(centerBlock.getX() + x, centerBlock.getZ() + z);
/*  938 */           yCount++;
/*      */           
/*  940 */           RoadBlock rb = CivGlobal.getRoadBlock(coord);
/*  941 */           if (CivGlobal.getRoadBlock(coord) != null)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  947 */             deletedRoadBlocks.add(rb);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  954 */     for (RoadBlock roadBlock : deletedRoadBlocks) {
/*  955 */       roadBlock.getRoad().deleteRoadBlock(roadBlock);
/*      */     }
/*      */     
/*  958 */     double highestAverageBlock = yTotal / yCount;
/*      */     
/*  960 */     if ((centerBlock.getY() > highestAverageBlock + 10.0D) || 
/*  961 */       (centerBlock.getY() < highestAverageBlock - 10.0D)) {
/*  962 */       throw new CivException("Cannot build here, you must be closer to the surface.");
/*      */     }
/*      */   }
/*      */   
/*      */   public void unbindCampBlocks()
/*      */   {
/*  968 */     for (BlockCoord bcoord : this.campBlocks.keySet()) {
/*  969 */       CivGlobal.removeCampBlock(bcoord);
/*  970 */       ChunkCoord coord = new ChunkCoord(bcoord);
/*  971 */       CivGlobal.removeCampChunk(coord);
/*      */     }
/*      */   }
/*      */   
/*      */   private void addCampBlock(BlockCoord coord) {
/*  976 */     addCampBlock(coord, false);
/*      */   }
/*      */   
/*      */   private void addCampBlock(BlockCoord coord, boolean friendlyBreakable) {
/*  980 */     CampBlock cb = new CampBlock(coord, this, friendlyBreakable);
/*      */     
/*  982 */     this.campBlocks.put(coord, cb);
/*  983 */     CivGlobal.addCampBlock(cb);
/*      */   }
/*      */   
/*      */   public void addMember(Resident resident) {
/*  987 */     this.members.put(resident.getName(), resident);
/*  988 */     resident.setCamp(this);
/*  989 */     resident.save();
/*      */   }
/*      */   
/*      */   public void removeMember(Resident resident) {
/*  993 */     this.members.remove(resident.getName());
/*  994 */     resident.setCamp(null);
/*  995 */     resident.save();
/*      */   }
/*      */   
/*      */   public Resident getMember(String name) {
/*  999 */     return (Resident)this.members.get(name);
/*      */   }
/*      */   
/*      */   public boolean hasMember(String name) {
/* 1003 */     return this.members.containsKey(name);
/*      */   }
/*      */   
/*      */   public Resident getOwner() {
/* 1007 */     return CivGlobal.getResident(this.ownerName);
/*      */   }
/*      */   
/*      */   public void setOwner(Resident owner)
/*      */   {
/* 1012 */     this.ownerName = owner.getName();
/*      */   }
/*      */   
/*      */   public int getHitpoints()
/*      */   {
/* 1017 */     return this.hitpoints;
/*      */   }
/*      */   
/*      */   public void setHitpoints(int hitpoints)
/*      */   {
/* 1022 */     this.hitpoints = hitpoints;
/*      */   }
/*      */   
/*      */   public int getFirepoints()
/*      */   {
/* 1027 */     return this.firepoints;
/*      */   }
/*      */   
/*      */   public void setFirepoints(int firepoints)
/*      */   {
/* 1032 */     this.firepoints = firepoints;
/*      */   }
/*      */   
/*      */   public BlockCoord getCorner()
/*      */   {
/* 1037 */     return this.corner;
/*      */   }
/*      */   
/*      */   public void setCorner(BlockCoord corner)
/*      */   {
/* 1042 */     this.corner = corner;
/*      */   }
/*      */   
/*      */   public void fancyCampBlockDestory() {
/* 1046 */     for (BlockCoord coord : this.campBlocks.keySet())
/*      */     {
/* 1048 */       if (CivGlobal.getStructureChest(coord) == null)
/*      */       {
/*      */ 
/*      */ 
/* 1052 */         if (CivGlobal.getStructureSign(coord) == null)
/*      */         {
/*      */ 
/*      */ 
/* 1056 */           if (ItemManager.getId(coord.getBlock()) != 54)
/*      */           {
/*      */ 
/*      */ 
/* 1060 */             if (ItemManager.getId(coord.getBlock()) != 63)
/*      */             {
/*      */ 
/*      */ 
/* 1064 */               if (ItemManager.getId(coord.getBlock()) != 68)
/*      */               {
/*      */ 
/*      */ 
/* 1068 */                 if (CivSettings.alwaysCrumble.contains(Integer.valueOf(ItemManager.getId(coord.getBlock())))) {
/* 1069 */                   ItemManager.setTypeId(coord.getBlock(), 13);
/*      */                 }
/*      */                 else
/*      */                 {
/* 1073 */                   Random rand = new Random();
/*      */                   
/*      */ 
/* 1076 */                   if (rand.nextInt(100) <= 10) {
/* 1077 */                     ItemManager.setTypeId(coord.getBlock(), 13);
/*      */ 
/*      */ 
/*      */ 
/*      */                   }
/* 1082 */                   else if (rand.nextInt(100) <= 50) {
/* 1083 */                     ItemManager.setTypeId(coord.getBlock(), 51);
/*      */ 
/*      */ 
/*      */ 
/*      */                   }
/* 1088 */                   else if (rand.nextInt(100) <= 1) {
/* 1089 */                     FireworkEffect effect = FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.ORANGE).withColor(Color.RED).withTrail().withFlicker().build();
/* 1090 */                     FireworkEffectPlayer fePlayer = new FireworkEffectPlayer();
/* 1091 */                     for (int i = 0; i < 3; i++)
/*      */                       try {
/* 1093 */                         fePlayer.playFirework(coord.getBlock().getWorld(), coord.getLocation(), effect);
/*      */                       } catch (Exception e) {
/* 1095 */                         e.printStackTrace();
/*      */                       }
/*      */                   }
/*      */                 } } } } }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void createControlPoint(BlockCoord absCoord) {
/* 1104 */     Location centerLoc = absCoord.getLocation();
/*      */     
/*      */ 
/* 1107 */     Block b = centerLoc.getBlock();
/* 1108 */     ItemManager.setTypeId(b, 85);ItemManager.setData(b, 0);
/*      */     
/* 1110 */     StructureBlock sb = new StructureBlock(new BlockCoord(b), this);
/* 1111 */     addCampBlock(sb.getCoord());
/*      */     
/*      */ 
/* 1114 */     b = centerLoc.getBlock().getRelative(0, 1, 0);
/* 1115 */     ItemManager.setTypeId(b, 49);
/* 1116 */     sb = new StructureBlock(new BlockCoord(b), this);
/* 1117 */     addCampBlock(sb.getCoord());
/*      */     int campControlHitpoints;
/*      */     try
/*      */     {
/* 1121 */       campControlHitpoints = CivSettings.getInteger(CivSettings.warConfig, "war.control_block_hitpoints_camp").intValue();
/*      */     } catch (InvalidConfiguration e) { int campControlHitpoints;
/* 1123 */       e.printStackTrace();
/* 1124 */       campControlHitpoints = 100;
/*      */     }
/*      */     
/* 1127 */     BlockCoord coord = new BlockCoord(b);
/* 1128 */     this.controlBlocks.put(coord, new ControlPoint(coord, this, campControlHitpoints));
/*      */   }
/*      */   
/*      */   public boolean isUndoable()
/*      */   {
/* 1133 */     return this.undoable;
/*      */   }
/*      */   
/*      */   public void setUndoable(boolean undoable) {
/* 1137 */     this.undoable = undoable;
/*      */   }
/*      */   
/*      */   public String getDisplayName()
/*      */   {
/* 1142 */     return "Camp";
/*      */   }
/*      */   
/*      */   public void sessionAdd(String key, String value)
/*      */   {
/* 1147 */     CivGlobal.getSessionDB().add(key, value, 0, 0, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void processUndo()
/*      */     throws CivException
/*      */   {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void updateBuildProgess() {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void build(Player player, Location centerLoc, Template tpl)
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */ 
/*      */   protected void runOnBuild(Location centerLoc, Template tpl)
/*      */     throws CivException
/*      */   {}
/*      */   
/*      */ 
/*      */   public String getDynmapDescription()
/*      */   {
/* 1175 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public String getMarkerIconName()
/*      */   {
/* 1181 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onComplete() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onLoad() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onUnload() {}
/*      */   
/*      */ 
/*      */ 
/*      */   public Collection<Resident> getMembers()
/*      */   {
/* 1203 */     return this.members.values();
/*      */   }
/*      */   
/*      */   public String getOwnerName() {
/* 1207 */     return this.ownerName;
/*      */   }
/*      */   
/*      */   public void setOwnerName(String ownerName) {
/* 1211 */     this.ownerName = ownerName;
/*      */   }
/*      */   
/*      */   public int getLonghouseLevel() {
/* 1215 */     return this.consumeComponent.getLevel();
/*      */   }
/*      */   
/*      */   public String getLonghouseCountString() {
/* 1219 */     return this.consumeComponent.getCountString();
/*      */   }
/*      */   
/*      */   public String getMembersString() {
/* 1223 */     String out = "";
/* 1224 */     for (Resident resident : this.members.values()) {
/* 1225 */       out = out + resident.getName() + " ";
/*      */     }
/* 1227 */     return out;
/*      */   }
/*      */   
/*      */   public void onControlBlockHit(ControlPoint cp, World world, Player player) {
/* 1231 */     world.playSound(cp.getCoord().getLocation(), Sound.ANVIL_USE, 0.2F, 1.0F);
/* 1232 */     world.playEffect(cp.getCoord().getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
/*      */     
/* 1234 */     CivMessage.send(player, "§7Damaged Control Block (" + cp.getHitpoints() + " / " + cp.getMaxHitpoints() + ")");
/* 1235 */     CivMessage.sendCamp(this, "§eOne of our camp's Control Points is under attack!");
/*      */   }
/*      */   
/*      */   public void onControlBlockDestroy(ControlPoint cp, World world, Player player)
/*      */   {
/* 1240 */     ItemManager.setTypeId(cp.getCoord().getLocation().getBlock(), 0);
/* 1241 */     world.playSound(cp.getCoord().getLocation(), Sound.ANVIL_BREAK, 1.0F, -1.0F);
/* 1242 */     world.playSound(cp.getCoord().getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
/*      */     
/* 1244 */     FireworkEffect effect = FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.YELLOW).withColor(Color.RED).withTrail().withFlicker().build();
/* 1245 */     FireworkEffectPlayer fePlayer = new FireworkEffectPlayer();
/* 1246 */     for (int i = 0; i < 3; i++) {
/*      */       try {
/* 1248 */         fePlayer.playFirework(world, cp.getCoord().getLocation(), effect);
/*      */       } catch (Exception e) {
/* 1250 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */     
/* 1254 */     boolean allDestroyed = true;
/* 1255 */     for (ControlPoint c : this.controlBlocks.values()) {
/* 1256 */       if (!c.isDestroyed()) {
/* 1257 */         allDestroyed = false;
/* 1258 */         break;
/*      */       }
/*      */     }
/*      */     
/* 1262 */     if (allDestroyed) {
/* 1263 */       CivMessage.sendCamp(this, "§cOur camp has been destroyed!");
/* 1264 */       destroy();
/*      */     } else {
/* 1266 */       CivMessage.sendCamp(this, "§cOne of camps's Control Points has been destroyed!");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void onDamage(int amount, World world, Player player, BlockCoord hit, BuildableDamageBlock hit2)
/*      */   {
/* 1274 */     ControlPoint cp = (ControlPoint)this.controlBlocks.get(hit);
/* 1275 */     if (cp != null) {
/* 1276 */       Date now = new Date();
/* 1277 */       Resident resident = CivGlobal.getResident(player);
/*      */       
/* 1279 */       if (resident.isProtected()) {
/* 1280 */         CivMessage.sendError(player, "You are unable to damage camps while protected.");
/* 1281 */         return;
/*      */       }
/*      */       
/* 1284 */       if (now.after(getNextRaidDate())) {
/* 1285 */         if (!cp.isDestroyed()) {
/* 1286 */           cp.damage(amount);
/* 1287 */           if (cp.isDestroyed()) {
/* 1288 */             onControlBlockDestroy(cp, world, player);
/*      */           } else {
/* 1290 */             onControlBlockHit(cp, world, player);
/*      */           }
/*      */         } else {
/* 1293 */           CivMessage.send(player, "§cControl Block already destroyed.");
/*      */         }
/*      */       } else {
/* 1296 */         SimpleDateFormat sdf = new SimpleDateFormat("M/dd h:mm:ss a z");
/* 1297 */         CivMessage.send(player, "§cCannot damage control blocks for this camp until " + sdf.format(getNextRaidDate()));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void setNextRaidDate(Date next)
/*      */   {
/* 1304 */     this.nextRaidDate = next;
/* 1305 */     save();
/*      */   }
/*      */   
/*      */   public Date getNextRaidDate() {
/* 1309 */     Date raidEnd = new Date(this.nextRaidDate.getTime());
/* 1310 */     raidEnd.setTime(this.nextRaidDate.getTime() + 3600000 * this.raidLength);
/*      */     
/* 1312 */     Date now = new Date();
/* 1313 */     if (now.getTime() > raidEnd.getTime()) {
/* 1314 */       this.nextRaidDate.setTime(this.nextRaidDate.getTime() + 86400000L);
/*      */     }
/*      */     
/* 1317 */     return this.nextRaidDate;
/*      */   }
/*      */   
/*      */   public boolean isSifterEnabled() {
/* 1321 */     return this.sifterEnabled;
/*      */   }
/*      */   
/*      */   public void setSifterEnabled(boolean sifterEnabled) {
/* 1325 */     this.sifterEnabled = sifterEnabled;
/*      */   }
/*      */   
/*      */   public Collection<ConfigCampUpgrade> getUpgrades() {
/* 1329 */     return this.upgrades.values();
/*      */   }
/*      */   
/*      */   public boolean hasUpgrade(String require_upgrade) {
/* 1333 */     return this.upgrades.containsKey(require_upgrade);
/*      */   }
/*      */   
/*      */   public void purchaseUpgrade(ConfigCampUpgrade upgrade) throws CivException {
/* 1337 */     Resident owner = getOwner();
/*      */     
/* 1339 */     if (!owner.getTreasury().hasEnough(upgrade.cost)) {
/* 1340 */       throw new CivException("The owner does not have the required " + upgrade.cost + " coins to purchase this upgrade.");
/*      */     }
/*      */     
/* 1343 */     this.upgrades.put(upgrade.id, upgrade);
/* 1344 */     upgrade.processAction(this);
/*      */     
/*      */ 
/* 1347 */     reprocessCommandSigns();
/* 1348 */     owner.getTreasury().withdraw(upgrade.cost);
/* 1349 */     save();
/*      */   }
/*      */   
/*      */   public boolean isLonghouseEnabled()
/*      */   {
/* 1354 */     return this.longhouseEnabled;
/*      */   }
/*      */   
/*      */   public void setLonghouseEnabled(boolean longhouseEnabled) {
/* 1358 */     this.longhouseEnabled = longhouseEnabled;
/*      */   }
/*      */   
/*      */   public boolean isGardenEnabled() {
/* 1362 */     return this.gardenEnabled;
/*      */   }
/*      */   
/*      */   public void setGardenEnabled(boolean gardenEnabled) {
/* 1366 */     this.gardenEnabled = gardenEnabled;
/*      */   }
/*      */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\camp\Camp.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
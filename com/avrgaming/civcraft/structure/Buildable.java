/*      */ package com.avrgaming.civcraft.structure;
/*      */ 
/*      */ import com.avrgaming.civcraft.components.Component;
/*      */ import com.avrgaming.civcraft.config.CivSettings;
/*      */ import com.avrgaming.civcraft.config.ConfigBuildableInfo;
/*      */ import com.avrgaming.civcraft.config.ConfigPerk;
/*      */ import com.avrgaming.civcraft.config.ConfigTownLevel;
/*      */ import com.avrgaming.civcraft.exception.CivException;
/*      */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*      */ import com.avrgaming.civcraft.interactive.InteractiveBuildCommand;
/*      */ import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
/*      */ import com.avrgaming.civcraft.main.CivGlobal;
/*      */ import com.avrgaming.civcraft.main.CivLog;
/*      */ import com.avrgaming.civcraft.main.CivMessage;
/*      */ import com.avrgaming.civcraft.object.AttrSource;
/*      */ import com.avrgaming.civcraft.object.BuffManager;
/*      */ import com.avrgaming.civcraft.object.BuildableDamageBlock;
/*      */ import com.avrgaming.civcraft.object.Civilization;
/*      */ import com.avrgaming.civcraft.object.CultureChunk;
/*      */ import com.avrgaming.civcraft.object.Resident;
/*      */ import com.avrgaming.civcraft.object.SQLObject;
/*      */ import com.avrgaming.civcraft.object.StructureChest;
/*      */ import com.avrgaming.civcraft.object.StructureSign;
/*      */ import com.avrgaming.civcraft.object.Town;
/*      */ import com.avrgaming.civcraft.object.TownChunk;
/*      */ import com.avrgaming.civcraft.permission.PlotPermissions;
/*      */ import com.avrgaming.civcraft.permission.PlotPermissions.Type;
/*      */ import com.avrgaming.civcraft.road.Road;
/*      */ import com.avrgaming.civcraft.road.RoadBlock;
/*      */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*      */ import com.avrgaming.civcraft.structure.wonders.Wonder;
/*      */ import com.avrgaming.civcraft.structurevalidation.StructureValidator;
/*      */ import com.avrgaming.civcraft.template.Template;
/*      */ import com.avrgaming.civcraft.template.Template.TemplateType;
/*      */ import com.avrgaming.civcraft.threading.TaskMaster;
/*      */ import com.avrgaming.civcraft.threading.tasks.BuildAsyncTask;
/*      */ import com.avrgaming.civcraft.threading.tasks.PostBuildSyncTask;
/*      */ import com.avrgaming.civcraft.util.AABB;
/*      */ import com.avrgaming.civcraft.util.BlockCoord;
/*      */ import com.avrgaming.civcraft.util.BukkitObjects;
/*      */ import com.avrgaming.civcraft.util.CallbackInterface;
/*      */ import com.avrgaming.civcraft.util.ChunkCoord;
/*      */ import com.avrgaming.civcraft.util.FireworkEffectPlayer;
/*      */ import com.avrgaming.civcraft.util.ItemManager;
/*      */ import com.avrgaming.civcraft.util.SimpleBlock;
/*      */ import com.avrgaming.civcraft.util.SimpleBlock.Type;
/*      */ import com.avrgaming.global.perks.Perk;
/*      */ import com.wimbli.WorldBorder.BorderData;
/*      */ import com.wimbli.WorldBorder.Config;
/*      */ import java.io.IOException;
/*      */ import java.sql.SQLException;
/*      */ import java.text.DecimalFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import org.bukkit.Bukkit;
/*      */ import org.bukkit.ChatColor;
/*      */ import org.bukkit.Chunk;
/*      */ import org.bukkit.ChunkSnapshot;
/*      */ import org.bukkit.Color;
/*      */ import org.bukkit.Effect;
/*      */ import org.bukkit.FireworkEffect;
/*      */ import org.bukkit.FireworkEffect.Builder;
/*      */ import org.bukkit.FireworkEffect.Type;
/*      */ import org.bukkit.Location;
/*      */ import org.bukkit.Material;
/*      */ import org.bukkit.Server;
/*      */ import org.bukkit.Sound;
/*      */ import org.bukkit.World;
/*      */ import org.bukkit.block.Biome;
/*      */ import org.bukkit.block.Block;
/*      */ import org.bukkit.block.Sign;
/*      */ import org.bukkit.entity.Player;
/*      */ import org.bukkit.event.player.PlayerInteractEvent;
/*      */ import org.bukkit.inventory.Inventory;
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
/*      */ public abstract class Buildable
/*      */   extends SQLObject
/*      */ {
/*      */   private Town town;
/*      */   protected BlockCoord corner;
/*  103 */   public ConfigBuildableInfo info = new ConfigBuildableInfo();
/*      */   
/*      */   protected int hitpoints;
/*  106 */   public int builtBlockCount = 0;
/*  107 */   public int savedBlockCount = 0;
/*  108 */   private int totalBlockCount = 0;
/*  109 */   private boolean complete = false;
/*  110 */   protected boolean autoClaim = false;
/*  111 */   private boolean enabled = true;
/*      */   
/*      */   private String templateName;
/*      */   
/*      */   private int templateX;
/*      */   
/*      */   private int templateY;
/*      */   
/*      */   private int templateZ;
/*      */   public static final double SHIFT_OUT = 0.0D;
/*      */   public static final int MIN_DISTANCE = 7;
/*  122 */   private Map<BlockCoord, StructureSign> structureSigns = new ConcurrentHashMap();
/*  123 */   private Map<BlockCoord, StructureChest> structureChests = new ConcurrentHashMap();
/*      */   
/*      */ 
/*  126 */   protected Map<BlockCoord, Boolean> structureBlocks = new ConcurrentHashMap();
/*      */   
/*      */   private BlockCoord centerLocation;
/*      */   
/*  130 */   public ArrayList<TownChunk> townChunksToSave = new ArrayList();
/*  131 */   public ArrayList<Component> attachedComponents = new ArrayList();
/*      */   
/*  133 */   private boolean valid = true;
/*  134 */   public static double validPercentRequirement = 0.8D;
/*  135 */   public static HashSet<Buildable> invalidBuildables = new HashSet();
/*  136 */   public HashMap<Integer, BuildableLayer> layerValidPercentages = new HashMap();
/*  137 */   public boolean validated = false;
/*      */   
/*  139 */   private String invalidReason = "";
/*      */   
/*      */   public static final double DEFAULT_HAMMERRATE = 1.0D;
/*  142 */   public AABB templateBoundingBox = null;
/*  143 */   public String invalidLayerMessage = "";
/*      */   
/*      */   public Town getTown()
/*      */   {
/*  147 */     return this.town;
/*      */   }
/*      */   
/*  150 */   public void setTown(Town town) { this.town = town; }
/*      */   
/*      */   public Civilization getCiv()
/*      */   {
/*  154 */     if (getTown() == null) {
/*  155 */       return null;
/*      */     }
/*  157 */     return getTown().getCiv();
/*      */   }
/*      */   
/*      */   public String getHash() {
/*  161 */     return this.corner.toString();
/*      */   }
/*      */   
/*      */   public String getConfigId()
/*      */   {
/*  166 */     return this.info.id;
/*      */   }
/*      */   
/*      */   public String getTemplateBaseName() {
/*  170 */     return this.info.template_base_name;
/*      */   }
/*      */   
/*      */   public String getDisplayName() {
/*  174 */     return this.info.displayName;
/*      */   }
/*      */   
/*      */   public int getMaxHitPoints()
/*      */   {
/*  179 */     return this.info.max_hitpoints;
/*      */   }
/*      */   
/*      */   public double getCost()
/*      */   {
/*  184 */     return this.info.cost;
/*      */   }
/*      */   
/*      */   public int getRegenRate() {
/*  188 */     if (this.info.regenRate == null) {
/*  189 */       return 0;
/*      */     }
/*      */     
/*  192 */     return this.info.regenRate.intValue();
/*      */   }
/*      */   
/*      */   public double getHammerCost()
/*      */   {
/*  197 */     double rate = 1.0D;
/*  198 */     if (getTown().getBuffManager().hasBuff("buff_rush")) {
/*  199 */       rate -= getTown().getBuffManager().getEffectiveDouble("buff_rush");
/*      */     }
/*  201 */     return rate * this.info.hammer_cost;
/*      */   }
/*      */   
/*      */   public double getUpkeepCost()
/*      */   {
/*  206 */     return this.info.upkeep;
/*      */   }
/*      */   
/*      */   public int getTemplateYShift()
/*      */   {
/*  211 */     return this.info.templateYShift;
/*      */   }
/*      */   
/*      */   public String getRequiredUpgrade()
/*      */   {
/*  216 */     return this.info.require_upgrade;
/*      */   }
/*      */   
/*      */   public String getRequiredTechnology()
/*      */   {
/*  221 */     return this.info.require_tech;
/*      */   }
/*      */   
/*      */   public String getUpdateEvent()
/*      */   {
/*  226 */     return this.info.update_event;
/*      */   }
/*      */   
/*      */   public int getPoints() {
/*  230 */     if (this.info.points != null) {
/*  231 */       return this.info.points.intValue();
/*      */     }
/*  233 */     return 0;
/*      */   }
/*      */   
/*      */   public String getEffectEvent() {
/*  237 */     return this.info.effect_event;
/*      */   }
/*      */   
/*      */   public void onEffectEvent() {}
/*      */   
/*      */   public String getOnBuildEvent()
/*      */   {
/*  244 */     return this.info.onBuild_event;
/*      */   }
/*      */   
/*      */   public boolean allowDemolish() {
/*  248 */     return this.info.allow_demolish;
/*      */   }
/*      */   
/*      */   public boolean isTileImprovement() {
/*  252 */     return this.info.tile_improvement.booleanValue();
/*      */   }
/*      */   
/*      */   public boolean isActive() {
/*  256 */     return (isComplete()) && (!isDestroyed()) && (isEnabled());
/*      */   }
/*      */   
/*      */ 
/*      */   public abstract void processUndo() throws CivException;
/*      */   
/*  262 */   public int getBuiltBlockCount() { return this.builtBlockCount; }
/*      */   
/*      */   public void setBuiltBlockCount(int builtBlockCount) {
/*  265 */     this.builtBlockCount = builtBlockCount;
/*  266 */     this.savedBlockCount = builtBlockCount;
/*      */   }
/*      */   
/*  269 */   public int getTotalBlockCount() { return this.totalBlockCount; }
/*      */   
/*      */   public void setTotalBlockCount(int totalBlockCount) {
/*  272 */     this.totalBlockCount = totalBlockCount;
/*      */   }
/*      */   
/*      */   public boolean isDestroyed() {
/*  276 */     if ((this.hitpoints == 0) && (getMaxHitPoints() != 0)) {
/*  277 */       return true;
/*      */     }
/*  279 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isDestroyable() {
/*  283 */     return (this.info.destroyable != null) && (this.info.destroyable.booleanValue());
/*      */   }
/*      */   
/*      */   public boolean isComplete() {
/*  287 */     return this.complete;
/*      */   }
/*      */   
/*  290 */   public void setComplete(boolean complete) { this.complete = complete; }
/*      */   
/*      */   public abstract void updateBuildProgess();
/*      */   
/*      */   public BlockCoord getCorner()
/*      */   {
/*  296 */     return this.corner;
/*      */   }
/*      */   
/*      */   public void setCorner(BlockCoord center) {
/*  300 */     this.corner = center;
/*      */   }
/*      */   
/*      */   public BlockCoord getCenterLocation() {
/*  304 */     if (this.centerLocation == null) {
/*  305 */       int centerX = getCorner().getX() + getTemplateX() / 2;
/*  306 */       int centerY = getCorner().getY() + getTemplateY() / 2;
/*  307 */       int centerZ = getCorner().getZ() + getTemplateZ() / 2;
/*      */       
/*  309 */       this.centerLocation = new BlockCoord(getCorner().getWorldname(), centerX, centerY, centerZ);
/*      */     }
/*      */     
/*  312 */     return this.centerLocation;
/*      */   }
/*      */   
/*      */   public double getBlocksPerHammer()
/*      */   {
/*  317 */     if (getHammerCost() == 0.0D) {
/*  318 */       return this.totalBlockCount;
/*      */     }
/*  320 */     return this.totalBlockCount / getHammerCost();
/*      */   }
/*      */   
/*      */   public int getHitpoints() {
/*  324 */     return this.hitpoints;
/*      */   }
/*      */   
/*      */   public void setHitpoints(int hitpoints) {
/*  328 */     this.hitpoints = hitpoints;
/*      */   }
/*      */   
/*      */   public abstract void build(Player paramPlayer, Location paramLocation, Template paramTemplate)
/*      */     throws Exception;
/*      */   
/*      */   protected abstract void runOnBuild(Location paramLocation, Template paramTemplate) throws CivException;
/*      */   
/*      */   public void bindStructureBlocks()
/*      */   {
/*  338 */     if (isDestroyable()) {
/*  339 */       return;
/*      */     }
/*      */     try
/*      */     {
/*  343 */       tpl = Template.getTemplate(this.templateName, null);
/*      */     } catch (IOException e) { Template tpl;
/*  345 */       e.printStackTrace();
/*  346 */       return;
/*      */     } catch (CivException e) {
/*  348 */       e.printStackTrace(); return;
/*      */     }
/*      */     
/*      */     Template tpl;
/*  352 */     setTemplateX(tpl.size_x);
/*  353 */     setTemplateY(tpl.size_y);
/*  354 */     setTemplateZ(tpl.size_z);
/*  355 */     setTemplateAABB(getCorner(), tpl);
/*      */     
/*  357 */     for (int y = 0; y < getTemplateY(); y++) {
/*  358 */       for (int z = 0; z < getTemplateZ(); z++) {
/*  359 */         for (int x = 0; x < getTemplateX(); x++) {
/*  360 */           int relx = getCorner().getX() + x;
/*  361 */           int rely = getCorner().getY() + y;
/*  362 */           int relz = getCorner().getZ() + z;
/*      */           
/*  364 */           BlockCoord coord = new BlockCoord(getCorner().getWorldname(), relx, rely, relz);
/*      */           
/*  366 */           if (tpl.blocks[x][y][z].getType() != 0)
/*      */           {
/*      */ 
/*      */ 
/*  370 */             if (tpl.blocks[x][y][z].specialType != SimpleBlock.Type.COMMAND)
/*      */             {
/*      */ 
/*      */ 
/*  374 */               if (y == 0) {
/*  375 */                 addStructureBlock(coord, false);
/*      */               } else {
/*  377 */                 addStructureBlock(coord, true);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void buildPlayerPreview(Player player, Location centerLoc) throws CivException, IOException {
/*  387 */     Resident resident = CivGlobal.getResident(player);
/*  388 */     ArrayList<Perk> perkList = getTown().getTemplatePerks(this, resident, this.info);
/*  389 */     ArrayList<Perk> personalUnboundPerks = resident.getUnboundTemplatePerks(perkList, this.info);
/*  390 */     if ((perkList.size() != 0) || (personalUnboundPerks.size() != 0))
/*      */     {
/*  392 */       resident.pendingBuildable = this;
/*      */       
/*      */ 
/*  395 */       Inventory inv = Bukkit.getServer().createInventory(player, 54);
/*  396 */       ItemStack infoRec = LoreGuiItem.build("Default " + getDisplayName(), 
/*  397 */         ItemManager.getId(Material.WRITTEN_BOOK), 
/*  398 */         0, new String[] { "§6<Click To Build>" });
/*  399 */       infoRec = LoreGuiItem.setAction(infoRec, "buildWithDefaultTemplate");
/*  400 */       inv.addItem(new ItemStack[] { infoRec });
/*      */       
/*  402 */       for (Perk perk : perkList) {
/*  403 */         infoRec = LoreGuiItem.build(perk.getDisplayName(), 
/*  404 */           perk.configPerk.type_id.intValue(), 
/*  405 */           perk.configPerk.data.intValue(), new String[] { "§6<Click To Build>", 
/*  406 */           "§8Provided by: §b" + perk.provider });
/*  407 */         infoRec = LoreGuiItem.setAction(infoRec, "buildWithTemplate:" + perk.getIdent());
/*  408 */         inv.addItem(new ItemStack[] { infoRec });
/*      */       }
/*      */       
/*  411 */       for (Perk perk : personalUnboundPerks) {
/*  412 */         infoRec = LoreGuiItem.build(perk.getDisplayName(), 
/*  413 */           7, 
/*  414 */           perk.configPerk.data.intValue(), new String[] { "§6<Click To Bind>", 
/*  415 */           "§8Unbound Temple", 
/*  416 */           "§8You own this template.", 
/*  417 */           "§8The town is missing it.", 
/*  418 */           "§8Click to bind to town first.", 
/*  419 */           "§8Then build again." });
/*  420 */         infoRec = LoreGuiItem.setAction(infoRec, "activateperk:" + perk.getIdent());
/*  421 */         inv.addItem(new ItemStack[] { infoRec });
/*      */       }
/*      */       
/*      */ 
/*  425 */       player.openInventory(inv);
/*  426 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  433 */     Template tpl = new Template();
/*      */     try {
/*  435 */       tpl.initTemplate(centerLoc, this);
/*      */     } catch (CivException e) {
/*  437 */       e.printStackTrace();
/*  438 */       throw e;
/*      */     } catch (IOException e) {
/*  440 */       e.printStackTrace();
/*  441 */       throw e;
/*      */     }
/*      */     
/*  444 */     buildPlayerPreview(player, centerLoc, tpl);
/*      */   }
/*      */   
/*      */   public void buildPlayerPreview(Player player, Location centerLoc, Template tpl) throws CivException, IOException {
/*  448 */     centerLoc = repositionCenter(centerLoc, tpl.dir(), tpl.size_x, tpl.size_z);
/*  449 */     tpl.buildPreviewScaffolding(centerLoc, player);
/*  450 */     setCorner(new BlockCoord(centerLoc));
/*      */     
/*  452 */     CivMessage.sendHeading(player, "Building a Structure");
/*  453 */     CivMessage.send(player, "§e" + ChatColor.BOLD + "We've placed a bedrock outline, only visible to you which outlines " + 
/*  454 */       " the structure's location.");
/*  455 */     CivMessage.send(player, "§a" + ChatColor.BOLD + "If this location looks good, type 'yes'. Otherwise, type anything else to cancel building.");
/*  456 */     Resident resident = CivGlobal.getResident(player);
/*      */     
/*      */ 
/*      */ 
/*  460 */     this.templateName = tpl.getFilepath();
/*  461 */     TaskMaster.asyncTask(new StructureValidator(player, this), 0L);
/*  462 */     resident.setInteractiveMode(new InteractiveBuildCommand(getTown(), this, player.getLocation(), tpl));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void buildVerifyStatic(Player player, ConfigBuildableInfo info, Location centerLoc, CallbackInterface callback)
/*      */     throws CivException
/*      */   {
/*  472 */     Resident resident = CivGlobal.getResident(player);
/*      */     
/*  474 */     LinkedList<Perk> perkList = resident.getPersonalTemplatePerks(info);
/*  475 */     if (perkList.size() != 0)
/*      */     {
/*      */ 
/*  478 */       resident.pendingBuildableInfo = info;
/*  479 */       resident.pendingCallback = callback;
/*      */       
/*      */ 
/*  482 */       Inventory inv = Bukkit.getServer().createInventory(player, 54);
/*  483 */       ItemStack infoRec = LoreGuiItem.build("Default " + info.displayName, 
/*  484 */         ItemManager.getId(Material.WRITTEN_BOOK), 
/*  485 */         0, new String[] { "§6<Click To Build>" });
/*  486 */       infoRec = LoreGuiItem.setAction(infoRec, "buildWithDefaultPersonalTemplate");
/*  487 */       inv.addItem(new ItemStack[] { infoRec });
/*      */       
/*  489 */       for (Perk perk : perkList) {
/*  490 */         infoRec = LoreGuiItem.build(perk.getDisplayName(), 
/*  491 */           perk.configPerk.type_id.intValue(), 
/*  492 */           perk.configPerk.data.intValue(), new String[] { "§6<Click To Build>", 
/*  493 */           "§8Provided by: §bYourself :)" });
/*  494 */         infoRec = LoreGuiItem.setAction(infoRec, "buildWithPersonalTemplate:" + perk.getIdent());
/*  495 */         inv.addItem(new ItemStack[] { infoRec });
/*  496 */         player.openInventory(inv);
/*      */       }
/*      */       
/*  499 */       return;
/*      */     }
/*      */     
/*  502 */     String path = Template.getTemplateFilePath(info.template_base_name, 
/*  503 */       Template.getDirection(player.getLocation()), Template.TemplateType.STRUCTURE, "default");
/*      */     
/*      */     try
/*      */     {
/*  507 */       tpl = Template.getTemplate(path, player.getLocation());
/*      */     } catch (IOException e) { Template tpl;
/*  509 */       e.printStackTrace(); return;
/*      */     }
/*      */     
/*      */     Template tpl;
/*  513 */     centerLoc = repositionCenterStatic(centerLoc, info, tpl.dir(), tpl.size_x, tpl.size_z);
/*      */     
/*  515 */     TaskMaster.asyncTask(new StructureValidator(player, tpl.getFilepath(), centerLoc, callback), 0L);
/*      */   }
/*      */   
/*      */   public void undoFromTemplate() throws IOException, CivException {
/*  519 */     Template undo_tpl = new Template();
/*  520 */     undo_tpl.initUndoTemplate(getCorner().toString(), getTown().getName());
/*  521 */     undo_tpl.buildUndoTemplate(undo_tpl, getCorner().getBlock());
/*      */     
/*  523 */     for (BuildAsyncTask task : getTown().build_tasks) {
/*  524 */       if (task.buildable == this) {
/*  525 */         task.abort();
/*      */       }
/*      */     }
/*  528 */     undo_tpl.deleteUndoTemplate(getCorner().toString(), getTown().getName());
/*      */   }
/*      */   
/*      */   public void unbindStructureBlocks() {
/*  532 */     for (BlockCoord coord : this.structureBlocks.keySet()) {
/*  533 */       CivGlobal.removeStructureBlock(coord);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static Location repositionCenterStatic(Location center, ConfigBuildableInfo info, String dir, double x_size, double z_size)
/*      */     throws CivException
/*      */   {
/*  542 */     Location loc = new Location(center.getWorld(), 
/*  543 */       center.getX(), center.getY(), center.getZ(), 
/*  544 */       center.getYaw(), center.getPitch());
/*      */     
/*      */ 
/*      */ 
/*  548 */     if (info.tile_improvement.booleanValue())
/*      */     {
/*  550 */       loc = center.getChunk().getBlock(0, center.getBlockY(), 0).getLocation();
/*      */     }
/*  552 */     else if (dir.equalsIgnoreCase("east")) {
/*  553 */       loc.setZ(loc.getZ() - z_size / 2.0D);
/*  554 */       loc = center.getChunk().getBlock(0, center.getBlockY(), 0).getLocation();
/*  555 */       loc.setX(loc.getX() + 0.0D);
/*      */     }
/*  557 */     else if (dir.equalsIgnoreCase("west")) {
/*  558 */       loc.setZ(loc.getZ() - z_size / 2.0D);
/*  559 */       loc = center.getChunk().getBlock(0, center.getBlockY(), 0).getLocation();
/*  560 */       loc.setX(loc.getX() - (0.0D + x_size));
/*      */     }
/*  562 */     else if (dir.equalsIgnoreCase("north")) {
/*  563 */       loc.setX(loc.getX() - x_size / 2.0D);
/*  564 */       loc = center.getChunk().getBlock(0, center.getBlockY(), 0).getLocation();
/*  565 */       loc.setZ(loc.getZ() - (0.0D + z_size));
/*      */     }
/*  567 */     else if (dir.equalsIgnoreCase("south")) {
/*  568 */       loc.setX(loc.getX() - x_size / 2.0D);
/*  569 */       loc = center.getChunk().getBlock(0, center.getBlockY(), 0).getLocation();
/*  570 */       loc.setZ(loc.getZ() + 0.0D);
/*      */     }
/*      */     
/*  573 */     if (info.templateYShift != 0)
/*      */     {
/*  575 */       loc.setY(loc.getY() + info.templateYShift);
/*      */       
/*  577 */       if (loc.getY() < 1.0D) {
/*  578 */         throw new CivException("Cannot build here, too close to bedrock.");
/*      */       }
/*      */     }
/*      */     
/*  582 */     return loc;
/*      */   }
/*      */   
/*      */   protected Location repositionCenter(Location center, String dir, double x_size, double z_size) throws CivException {
/*  586 */     Location loc = new Location(center.getWorld(), 
/*  587 */       center.getX(), center.getY(), center.getZ(), 
/*  588 */       center.getYaw(), center.getPitch());
/*      */     
/*      */ 
/*      */ 
/*  592 */     if (isTileImprovement())
/*      */     {
/*  594 */       loc = center.getChunk().getBlock(0, center.getBlockY(), 0).getLocation();
/*      */     }
/*  596 */     else if (dir.equalsIgnoreCase("east")) {
/*  597 */       loc.setZ(loc.getZ() - z_size / 2.0D);
/*  598 */       loc = center.getChunk().getBlock(0, center.getBlockY(), 0).getLocation();
/*  599 */       loc.setX(loc.getX() + 0.0D);
/*      */     }
/*  601 */     else if (dir.equalsIgnoreCase("west")) {
/*  602 */       loc.setZ(loc.getZ() - z_size / 2.0D);
/*  603 */       loc = center.getChunk().getBlock(0, center.getBlockY(), 0).getLocation();
/*  604 */       loc.setX(loc.getX() - (0.0D + x_size));
/*      */     }
/*  606 */     else if (dir.equalsIgnoreCase("north")) {
/*  607 */       loc.setX(loc.getX() - x_size / 2.0D);
/*  608 */       loc = center.getChunk().getBlock(0, center.getBlockY(), 0).getLocation();
/*  609 */       loc.setZ(loc.getZ() - (0.0D + z_size));
/*      */     }
/*  611 */     else if (dir.equalsIgnoreCase("south")) {
/*  612 */       loc.setX(loc.getX() - x_size / 2.0D);
/*  613 */       loc = center.getChunk().getBlock(0, center.getBlockY(), 0).getLocation();
/*  614 */       loc.setZ(loc.getZ() + 0.0D);
/*      */     }
/*      */     
/*  617 */     if (getTemplateYShift() != 0)
/*      */     {
/*  619 */       loc.setY(loc.getY() + getTemplateYShift());
/*      */       
/*  621 */       if (loc.getY() < 1.0D) {
/*  622 */         throw new CivException("Cannot build here, too close to bedrock.");
/*      */       }
/*      */     }
/*      */     
/*  626 */     return loc;
/*      */   }
/*      */   
/*      */ 
/*      */   public void resumeBuildFromTemplate()
/*      */     throws Exception
/*      */   {
/*  633 */     Location corner = getCorner().getLocation();
/*      */     try
/*      */     {
/*  636 */       Template tpl = new Template();
/*  637 */       tpl.resumeTemplate(getSavedTemplatePath(), this);
/*      */     } catch (Exception e) {
/*  639 */       throw e;
/*      */     }
/*      */     Template tpl;
/*  642 */     this.totalBlockCount = (tpl.size_x * tpl.size_y * tpl.size_z);
/*      */     
/*  644 */     if ((this instanceof Wonder)) {
/*  645 */       getTown().setCurrentWonderInProgress(this);
/*      */     } else {
/*  647 */       getTown().setCurrentStructureInProgress(this);
/*      */     }
/*      */     
/*  650 */     startBuildTask(tpl, corner);
/*      */   }
/*      */   
/*      */ 
/*      */   protected void setTemplateAABB(BlockCoord corner, Template tpl)
/*      */   {
/*  656 */     setCorner(corner);
/*  657 */     this.templateBoundingBox = new AABB();
/*  658 */     this.templateBoundingBox.setPosition(getCenterLocation());
/*  659 */     this.templateBoundingBox.setExtents(new BlockCoord(getCorner().getWorldname(), 
/*  660 */       getTemplateX() / 2, 
/*  661 */       getTemplateY() / 2, 
/*  662 */       getTemplateZ() / 2));
/*      */   }
/*      */   
/*      */   public static void validateDistanceFromSpawn(Location loc) throws CivException
/*      */   {
/*      */     try
/*      */     {
/*  669 */       requiredDistance = CivSettings.getDouble(CivSettings.civConfig, "global.distance_from_spawn");
/*      */     } catch (InvalidConfiguration e) { double requiredDistance;
/*  671 */       e.printStackTrace(); return;
/*      */     }
/*      */     
/*      */     double requiredDistance;
/*  675 */     for (Civilization civ : CivGlobal.getAdminCivs()) {
/*  676 */       Location townHallLoc = civ.getCapitolTownHallLocation();
/*  677 */       if (townHallLoc != null)
/*      */       {
/*      */ 
/*      */ 
/*  681 */         double distance = townHallLoc.distance(loc);
/*  682 */         if (distance < requiredDistance) {
/*  683 */           throw new CivException("You must build at least " + requiredDistance + " blocks away from spawn.");
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void checkBlockPermissionsAndRestrictions(Player player, Block centerBlock, int regionX, int regionY, int regionZ, Location origin) throws CivException
/*      */   {
/*  691 */     boolean foundTradeGood = false;
/*  692 */     TradeOutpost tradeOutpost = null;
/*  693 */     boolean ignoreBorders = false;
/*  694 */     boolean autoClaim = this.autoClaim;
/*      */     
/*  696 */     if ((this instanceof TradeOutpost)) {
/*  697 */       tradeOutpost = (TradeOutpost)this;
/*      */     }
/*      */     
/*      */ 
/*  701 */     if (!isTownHall()) {
/*  702 */       CultureChunk cc = CivGlobal.getCultureChunk(centerBlock.getLocation());
/*  703 */       if ((cc == null) || (cc.getTown().getCiv() != this.town.getCiv())) {
/*  704 */         throw new CivException("Cannot build here, you need to build inside your culture.");
/*      */       }
/*      */     }
/*      */     else {
/*  708 */       ignoreBorders = true;
/*      */     }
/*      */     TownHall townhall;
/*  711 */     if (isTownHall())
/*      */     {
/*      */       try {
/*  714 */         minDistance = CivSettings.getDouble(CivSettings.townConfig, "town.min_town_distance");
/*      */       } catch (InvalidConfiguration e) { double minDistance;
/*  716 */         CivMessage.sendError(player, "Internal configuration error.");
/*  717 */         e.printStackTrace(); return;
/*      */       }
/*      */       
/*      */       double minDistance;
/*  721 */       for (Town town : CivGlobal.getTowns()) {
/*  722 */         townhall = town.getTownHall();
/*  723 */         if (townhall != null)
/*      */         {
/*      */ 
/*      */ 
/*  727 */           double dist = townhall.getCenterLocation().distance(new BlockCoord(centerBlock));
/*  728 */           if (dist < minDistance) {
/*  729 */             DecimalFormat df = new DecimalFormat();
/*  730 */             CivMessage.sendError(player, "Cannot build town here. Too close to the town of " + town.getName() + ". Distance is " + df.format(dist) + " and needs to be " + minDistance);
/*  731 */             return;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  736 */     if ((getConfigId().equals("s_shipyard")) && 
/*  737 */       (!centerBlock.getBiome().equals(Biome.OCEAN)) && 
/*  738 */       (!centerBlock.getBiome().equals(Biome.BEACH)) && 
/*  739 */       (!centerBlock.getBiome().equals(Biome.DEEP_OCEAN)) && 
/*  740 */       (!centerBlock.getBiome().equals(Biome.RIVER)) && 
/*  741 */       (!centerBlock.getBiome().equals(Biome.FROZEN_OCEAN)) && 
/*  742 */       (!centerBlock.getBiome().equals(Biome.FROZEN_RIVER))) {
/*  743 */       throw new CivException("Cannot build shipyard here, you need to be in a majority of ocean, river, or beach biome. Try repositioning it if you are.");
/*      */     }
/*      */     
/*      */ 
/*  747 */     Structure struct = CivGlobal.getStructure(new BlockCoord(centerBlock));
/*  748 */     if (struct != null) {
/*  749 */       throw new CivException("Cannot build here, structure already at this location.");
/*      */     }
/*      */     
/*  752 */     ignoreBorders = isAllowOutsideTown();
/*      */     
/*  754 */     if (!player.isOp()) {
/*  755 */       validateDistanceFromSpawn(centerBlock.getLocation());
/*      */     }
/*      */     
/*  758 */     if (isTileImprovement()) {
/*  759 */       ignoreBorders = true;
/*  760 */       ConfigTownLevel level = (ConfigTownLevel)CivSettings.townLevels.get(Integer.valueOf(getTown().getLevel()));
/*      */       
/*  762 */       if (getTown().getTileImprovementCount() >= level.tile_improvements) {
/*  763 */         throw new CivException("Cannot build tile improvement. Already at tile improvement limit.");
/*      */       }
/*      */       
/*  766 */       ChunkCoord coord = new ChunkCoord(centerBlock.getLocation());
/*  767 */       for (Structure s : getTown().getStructures()) {
/*  768 */         if (s.isTileImprovement())
/*      */         {
/*      */ 
/*  771 */           ChunkCoord sCoord = new ChunkCoord(s.getCorner());
/*  772 */           if (sCoord.equals(coord)) {
/*  773 */             throw new CivException("Cannot build a tile improvement on the same chunk as another tile improvement.");
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  779 */     TownChunk centertc = CivGlobal.getTownChunk(origin);
/*  780 */     if ((centertc == null) && (!ignoreBorders)) {
/*  781 */       throw new CivException("You must build this structure while inside town borders.");
/*      */     }
/*      */     
/*  784 */     if (centerBlock.getLocation().getY() >= 255.0D) {
/*  785 */       throw new CivException("You're too high to build structures.");
/*      */     }
/*      */     
/*  788 */     if (centerBlock.getLocation().getY() <= 7.0D) {
/*  789 */       throw new CivException("You can not place structures this close to bedrock!");
/*      */     }
/*      */     
/*      */ 
/*  793 */     if (regionY + centerBlock.getLocation().getBlockY() >= 255) {
/*  794 */       throw new CivException("Cannot build structure here, would go over the minecraft height limit.");
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
/*  811 */     onCheck();
/*      */     
/*  813 */     LinkedList<RoadBlock> deletedRoadBlocks = new LinkedList();
/*  814 */     Object claimCoords = new ArrayList();
/*  815 */     int y; for (int x = 0; x < regionX; x++) {
/*  816 */       for (y = 0; y < regionY; y++) {
/*  817 */         for (int z = 0; z < regionZ; z++) {
/*  818 */           Block b = centerBlock.getRelative(x, y, z);
/*      */           
/*  820 */           if (ItemManager.getId(b) == 54) {
/*  821 */             throw new CivException("Cannot build here, would destroy chest.");
/*      */           }
/*      */           
/*  824 */           TownChunk tc = CivGlobal.getTownChunk(b.getLocation());
/*  825 */           if ((tc == null) && (autoClaim)) {
/*  826 */             ((ArrayList)claimCoords).add(new ChunkCoord(b.getLocation()));
/*      */           }
/*      */           
/*  829 */           if ((tc != null) && (!tc.perms.hasPermission(PlotPermissions.Type.DESTROY, CivGlobal.getResident(player))))
/*      */           {
/*  831 */             throw new CivException("Cannot build here, you need DESTROY permissions to the block at " + b.getX() + "," + b.getY() + "," + b.getZ());
/*      */           }
/*      */           
/*  834 */           BlockCoord coord = new BlockCoord(b);
/*  835 */           ChunkCoord chunkCoord = new ChunkCoord(coord.getLocation());
/*      */           
/*  837 */           if (tradeOutpost == null)
/*      */           {
/*  839 */             if (CivGlobal.getProtectedBlock(coord) != null) {
/*  840 */               throw new CivException("Cannot build here, protected blocks in the way.");
/*      */             }
/*      */           }
/*  843 */           else if (CivGlobal.getTradeGood(coord) != null)
/*      */           {
/*  845 */             if (y + 3 < regionY) {
/*  846 */               foundTradeGood = true;
/*  847 */               tradeOutpost.setTradeGoodCoord(coord);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*  852 */           if (CivGlobal.getStructureBlock(coord) != null) {
/*  853 */             throw new CivException("Cannot build here, structure blocks in the way.");
/*      */           }
/*      */           
/*  856 */           if (CivGlobal.getFarmChunk(new ChunkCoord(coord.getLocation())) != null) {
/*  857 */             throw new CivException("Cannot build here, in the same chunk as a farm improvement.");
/*      */           }
/*      */           
/*  860 */           if (CivGlobal.getWallChunk(chunkCoord) != null) {
/*  861 */             throw new CivException("Cannot build here, in the same chunk as a wall improvement.");
/*      */           }
/*      */           
/*  864 */           if (CivGlobal.getCampBlock(coord) != null) {
/*  865 */             throw new CivException("Cannot build here, structure blocks in the way.");
/*      */           }
/*      */           
/*  868 */           if (CivGlobal.getBuildablesAt(coord) != null) {
/*  869 */             throw new CivException("Cannot build here, there is already a structure here.");
/*      */           }
/*      */           
/*  872 */           RoadBlock rb = CivGlobal.getRoadBlock(coord);
/*  873 */           if (rb != null) {
/*  874 */             deletedRoadBlocks.add(rb);
/*      */           }
/*      */           
/*  877 */           BorderData border = Config.Border(b.getWorld().getName());
/*  878 */           if ((border != null) && 
/*  879 */             (!border.insideBorder(b.getLocation().getX(), b.getLocation().getZ(), Config.ShapeRound()))) {
/*  880 */             throw new CivException("Cannot build here. Part of the structure would sit beyond the world border.");
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  887 */     if ((tradeOutpost != null) && 
/*  888 */       (!foundTradeGood)) {
/*  889 */       throw new CivException("Must be built on top of a trade good.");
/*      */     }
/*      */     
/*      */ 
/*  893 */     for (ChunkCoord c : (ArrayList)claimCoords) {
/*      */       try
/*      */       {
/*  896 */         this.townChunksToSave.add(TownChunk.townHallClaim(getTown(), c));
/*      */       }
/*      */       catch (Exception localException) {}
/*      */     }
/*      */     
/*      */ 
/*  902 */     for (RoadBlock rb : deletedRoadBlocks) {
/*  903 */       rb.getRoad().deleteRoadBlock(rb);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void onCheck()
/*      */     throws CivException
/*      */   {}
/*      */   
/*      */   public synchronized void buildRepairTemplate(Template tpl, Block centerBlock)
/*      */   {
/*  914 */     HashMap<Chunk, Chunk> chunkUpdates = new HashMap();
/*      */     
/*  916 */     for (int x = 0; x < tpl.size_x; x++) {
/*  917 */       for (int y = 0; y < tpl.size_y; y++) {
/*  918 */         for (int z = 0; z < tpl.size_z; z++) {
/*  919 */           Block b = centerBlock.getRelative(x, y, z);
/*      */           
/*  921 */           if (tpl.blocks[x][y][z].specialType == SimpleBlock.Type.COMMAND) {
/*  922 */             ItemManager.setTypeIdAndData(b, 0, 0, false);
/*      */           } else {
/*  924 */             ItemManager.setTypeIdAndData(b, tpl.blocks[x][y][z].getType(), (byte)tpl.blocks[x][y][z].getData(), false);
/*      */           }
/*      */           
/*  927 */           chunkUpdates.put(b.getChunk(), b.getChunk());
/*      */           
/*  929 */           if ((ItemManager.getId(b) == 68) || (ItemManager.getId(b) == 63)) {
/*  930 */             Sign s2 = (Sign)b.getState();
/*  931 */             s2.setLine(0, tpl.blocks[x][y][z].message[0]);
/*  932 */             s2.setLine(1, tpl.blocks[x][y][z].message[1]);
/*  933 */             s2.setLine(2, tpl.blocks[x][y][z].message[2]);
/*  934 */             s2.setLine(3, tpl.blocks[x][y][z].message[3]);
/*  935 */             s2.update();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void startBuildTask(Template tpl, Location center)
/*      */   {
/*  945 */     if ((this instanceof Structure)) {
/*  946 */       getTown().setCurrentStructureInProgress(this);
/*      */     } else {
/*  948 */       getTown().setCurrentWonderInProgress(this);
/*      */     }
/*  950 */     BuildAsyncTask task = new BuildAsyncTask(this, tpl, getBuildSpeed(), getBlocksPerTick(), center.getBlock());
/*      */     
/*  952 */     this.town.build_tasks.add(task);
/*  953 */     BukkitObjects.scheduleAsyncDelayedTask(task, 0L);
/*      */   }
/*      */   
/*      */ 
/*      */   public int getBuildSpeed()
/*      */   {
/*  959 */     double hoursPerBlock = getHammerCost() / this.town.getHammers().total / this.totalBlockCount;
/*  960 */     double millisecondsPerBlock = hoursPerBlock * 60.0D * 60.0D * 1000.0D;
/*      */     
/*  962 */     if (millisecondsPerBlock < 500.0D) {
/*  963 */       millisecondsPerBlock = 500.0D;
/*      */     }
/*      */     
/*  966 */     return (int)millisecondsPerBlock;
/*      */   }
/*      */   
/*      */   public double getBuiltHammers() {
/*  970 */     double hoursPerBlock = getHammerCost() / 1.0D / this.totalBlockCount;
/*  971 */     return this.builtBlockCount * hoursPerBlock;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getBlocksPerTick()
/*      */   {
/*  979 */     double hoursPerBlock = getHammerCost() / this.town.getHammers().total / this.totalBlockCount;
/*  980 */     double millisecondsPerBlock = hoursPerBlock * 60.0D * 60.0D * 1000.0D;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  986 */     double blocks = 500.0D / millisecondsPerBlock;
/*      */     
/*  988 */     if (blocks < 1.0D) {
/*  989 */       blocks = 1.0D;
/*      */     }
/*      */     
/*  992 */     return (int)blocks;
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
/*      */   public void canBuildHere(Location center, double distance)
/*      */     throws CivException
/*      */   {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getTemplateX()
/*      */   {
/* 1017 */     return this.templateX;
/*      */   }
/*      */   
/* 1020 */   public void setTemplateX(int templateX) { this.templateX = templateX; }
/*      */   
/*      */   public int getTemplateY() {
/* 1023 */     return this.templateY;
/*      */   }
/*      */   
/* 1026 */   public void setTemplateY(int templateY) { this.templateY = templateY; }
/*      */   
/*      */   public int getTemplateZ() {
/* 1029 */     return this.templateZ;
/*      */   }
/*      */   
/* 1032 */   public void setTemplateZ(int templateZ) { this.templateZ = templateZ; }
/*      */   
/*      */   public void addStructureSign(StructureSign s)
/*      */   {
/* 1036 */     this.structureSigns.put(s.getCoord(), s);
/*      */   }
/*      */   
/*      */   public Collection<StructureSign> getSigns() {
/* 1040 */     return this.structureSigns.values();
/*      */   }
/*      */   
/*      */   public StructureSign getSign(BlockCoord coord) {
/* 1044 */     return (StructureSign)this.structureSigns.get(coord);
/*      */   }
/*      */   
/*      */   public void processSignAction(Player player, StructureSign sign, PlayerInteractEvent event) throws CivException {
/* 1048 */     CivLog.info("No Sign action for this buildable?:" + getDisplayName());
/*      */   }
/*      */   
/*      */   public String getSavedTemplatePath() {
/* 1052 */     return this.templateName;
/*      */   }
/*      */   
/* 1055 */   public void setTemplateName(String templateName) { this.templateName = templateName; }
/*      */   
/*      */   public void addStructureChest(StructureChest chest)
/*      */   {
/* 1059 */     this.structureChests.put(chest.getCoord(), chest);
/*      */   }
/*      */   
/*      */   public ArrayList<StructureChest> getAllChestsById(int id) {
/* 1063 */     ArrayList<StructureChest> chests = new ArrayList();
/*      */     
/* 1065 */     for (StructureChest chest : this.structureChests.values()) {
/* 1066 */       if (chest.getChestId() == id) {
/* 1067 */         chests.add(chest);
/*      */       }
/*      */     }
/*      */     
/* 1071 */     return chests;
/*      */   }
/*      */   
/*      */   public Collection<StructureChest> getChests() {
/* 1075 */     return this.structureChests.values();
/*      */   }
/*      */   
/*      */   public void addStructureBlock(BlockCoord coord, boolean damageable)
/*      */   {
/* 1080 */     CivGlobal.addStructureBlock(coord, this, damageable);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1085 */     this.structureBlocks.put(coord, Boolean.valueOf(true));
/*      */   }
/*      */   
/*      */ 
/*      */   public abstract String getDynmapDescription();
/*      */   
/*      */   public abstract String getMarkerIconName();
/*      */   
/*      */   public void sessionAdd(String key, String value)
/*      */   {
/* 1095 */     CivGlobal.getSessionDB().add(key, value, getCiv().getId(), getTown().getId(), getId());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getHitPoints()
/*      */   {
/* 1102 */     return this.hitpoints;
/*      */   }
/*      */   
/*      */   public int getDamagePercentage() {
/* 1106 */     double percentage = this.hitpoints / getMaxHitPoints();
/* 1107 */     percentage *= 100.0D;
/* 1108 */     return (int)percentage;
/*      */   }
/*      */   
/*      */   public void damage(int amount) {
/* 1112 */     if (this.hitpoints == 0)
/* 1113 */       return;
/* 1114 */     this.hitpoints -= amount;
/*      */     
/* 1116 */     if (this.hitpoints <= 0) {
/* 1117 */       this.hitpoints = 0;
/* 1118 */       onDestroy();
/*      */     }
/*      */   }
/*      */   
/*      */   public void onDestroy()
/*      */   {
/* 1124 */     CivMessage.global("A " + getDisplayName() + " in " + getTown().getName() + " has been destroyed!");
/* 1125 */     this.hitpoints = 0;
/* 1126 */     fancyDestroyStructureBlocks();
/* 1127 */     save();
/*      */   }
/*      */   
/*      */   public void onDamage(int amount, World world, Player player, BlockCoord coord, BuildableDamageBlock hit) {
/* 1131 */     boolean wasTenPercent = false;
/*      */     
/* 1133 */     if (hit.getOwner().isDestroyed()) {
/* 1134 */       if (player != null) {
/* 1135 */         CivMessage.sendError(player, hit.getOwner().getDisplayName() + " is already destroyed.");
/*      */       }
/* 1137 */       return;
/*      */     }
/*      */     
/* 1140 */     if ((!hit.getOwner().isComplete()) && (!(hit.getOwner() instanceof Wonder))) {
/* 1141 */       if (player != null) {
/* 1142 */         CivMessage.sendError(player, hit.getOwner().getDisplayName() + " is still being built, cannot be destroyed.");
/*      */       }
/* 1144 */       return;
/*      */     }
/*      */     
/* 1147 */     if (hit.getOwner().getDamagePercentage() % 10 == 0) {
/* 1148 */       wasTenPercent = true;
/*      */     }
/*      */     
/* 1151 */     damage(amount);
/*      */     
/* 1153 */     world.playSound(hit.getCoord().getLocation(), Sound.ANVIL_USE, 0.2F, 1.0F);
/* 1154 */     world.playEffect(hit.getCoord().getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
/*      */     
/* 1156 */     if ((hit.getOwner().getDamagePercentage() % 10 == 0) && (!wasTenPercent) && 
/* 1157 */       (player != null)) {
/* 1158 */       onDamageNotification(player, hit);
/*      */     }
/*      */     
/*      */ 
/* 1162 */     if (player != null) {
/* 1163 */       Resident resident = CivGlobal.getResident(player);
/* 1164 */       if (resident.isCombatInfo()) {
/* 1165 */         CivMessage.send(player, "§7" + hit.getOwner().getDisplayName() + " has been damaged (" + 
/* 1166 */           hit.getOwner().hitpoints + "/" + hit.getOwner().getMaxHitPoints() + ")");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void onDamageNotification(Player player, BuildableDamageBlock hit)
/*      */   {
/* 1173 */     CivMessage.send(player, "§7" + hit.getOwner().getDisplayName() + " has been damaged " + 
/* 1174 */       hit.getOwner().getDamagePercentage() + "%!");
/* 1175 */     CivMessage.sendTown(hit.getTown(), "§eOur " + hit.getOwner().getDisplayName() + " at (" + hit.getOwner().getCorner() + 
/* 1176 */       ") is under attack! Damage is " + hit.getOwner().getDamagePercentage() + "%!");
/*      */   }
/*      */   
/*      */   public Map<BlockCoord, Boolean> getStructureBlocks() {
/* 1180 */     return this.structureBlocks;
/*      */   }
/*      */   
/*      */   public boolean isAvailable()
/*      */   {
/* 1185 */     return this.info.isAvailable(getTown());
/*      */   }
/*      */   
/*      */   public int getLimit() {
/* 1189 */     return this.info.limit;
/*      */   }
/*      */   
/*      */   public boolean isAllowOutsideTown() {
/* 1193 */     return (this.info.allow_outside_town != null) && (this.info.allow_outside_town.booleanValue());
/*      */   }
/*      */   
/*      */   public boolean isStrategic() {
/* 1197 */     return this.info.strategic;
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
/*      */   public void runCheck(Location center)
/*      */     throws CivException
/*      */   {}
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
/*      */   public void fancyDestroyStructureBlocks()
/*      */   {
/* 1281 */     TaskMaster.syncTask(new Runnable()
/*      */     {
/*      */       public void run()
/*      */       {
/* 1218 */         for (BlockCoord coord : Buildable.this.structureBlocks.keySet())
/*      */         {
/* 1220 */           if (CivGlobal.getStructureChest(coord) == null)
/*      */           {
/*      */ 
/*      */ 
/* 1224 */             if (CivGlobal.getStructureSign(coord) == null)
/*      */             {
/*      */ 
/*      */ 
/* 1228 */               if (ItemManager.getId(coord.getBlock()) != 0)
/*      */               {
/*      */ 
/*      */ 
/* 1232 */                 if (ItemManager.getId(coord.getBlock()) != 54)
/*      */                 {
/*      */ 
/*      */ 
/* 1236 */                   if (ItemManager.getId(coord.getBlock()) != 63)
/*      */                   {
/*      */ 
/*      */ 
/* 1240 */                     if (ItemManager.getId(coord.getBlock()) != 68)
/*      */                     {
/*      */ 
/*      */ 
/* 1244 */                       if (CivSettings.alwaysCrumble.contains(Integer.valueOf(ItemManager.getId(coord.getBlock())))) {
/* 1245 */                         ItemManager.setTypeId(coord.getBlock(), 13);
/*      */                       }
/*      */                       else
/*      */                       {
/* 1249 */                         Random rand = new Random();
/*      */                         
/*      */ 
/* 1252 */                         if (rand.nextInt(100) <= 10) {
/* 1253 */                           ItemManager.setTypeId(coord.getBlock(), 13);
/* 1254 */                           ItemManager.setData(coord.getBlock(), 0, true);
/*      */ 
/*      */ 
/*      */ 
/*      */                         }
/* 1259 */                         else if (rand.nextInt(100) <= 50) {
/* 1260 */                           ItemManager.setTypeId(coord.getBlock(), 51);
/* 1261 */                           ItemManager.setData(coord.getBlock(), 0, true);
/*      */ 
/*      */ 
/*      */ 
/*      */                         }
/* 1266 */                         else if (rand.nextInt(100) <= 1) {
/* 1267 */                           FireworkEffect effect = FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.ORANGE).withColor(Color.RED).withTrail().withFlicker().build();
/* 1268 */                           FireworkEffectPlayer fePlayer = new FireworkEffectPlayer();
/* 1269 */                           for (int i = 0; i < 3; i++)
/*      */                             try {
/* 1271 */                               fePlayer.playFirework(coord.getBlock().getWorld(), coord.getLocation(), effect);
/*      */                             } catch (Exception e) {
/* 1273 */                               e.printStackTrace();
/*      */                             }
/*      */                         }
/*      */                       } } } } }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   public abstract void onComplete();
/*      */   
/*      */   public abstract void onLoad() throws CivException;
/*      */   
/*      */   public abstract void onUnload();
/*      */   
/*      */   public void onPostBuild(BlockCoord absCoord, SimpleBlock commandBlock) {}
/*      */   
/*      */   public void onTechUpdate() {}
/*      */   
/*      */   public void processRegen() {
/* 1295 */     if ((this.validated) && (!isValid()))
/*      */     {
/* 1297 */       return;
/*      */     }
/*      */     
/* 1300 */     int regenRate = getRegenRate();
/* 1301 */     regenRate += getTown().getBuffManager().getEffectiveInt("buff_chichen_itza_regen_rate");
/*      */     
/* 1303 */     if ((regenRate != 0) && 
/* 1304 */       (getHitpoints() != getMaxHitPoints()) && 
/* 1305 */       (getHitpoints() != 0)) {
/* 1306 */       setHitpoints(getHitpoints() + regenRate);
/*      */       
/* 1308 */       if (getHitpoints() > getMaxHitPoints()) {
/* 1309 */         setHitpoints(getMaxHitPoints());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void onUpdate() {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void flashStructureBlocks()
/*      */   {
/* 1322 */     World world = null;
/* 1323 */     for (BlockCoord coord : this.structureBlocks.keySet()) {
/* 1324 */       if (world == null) {
/* 1325 */         world = coord.getLocation().getWorld();
/*      */       }
/*      */       
/* 1328 */       world.playEffect(coord.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean showOnDynmap() {
/* 1333 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void onDailyEvent() {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void onPreBuild(Location centerLoc)
/*      */     throws CivException
/*      */   {}
/*      */   
/*      */ 
/*      */   public void updateSignText() {}
/*      */   
/*      */ 
/*      */   public void repairFromTemplate()
/*      */     throws IOException, CivException
/*      */   {
/* 1353 */     Template tpl = Template.getTemplate(getSavedTemplatePath(), null);
/* 1354 */     buildRepairTemplate(tpl, getCorner().getBlock());
/* 1355 */     TaskMaster.syncTask(new PostBuildSyncTask(tpl, this));
/*      */   }
/*      */   
/*      */   public boolean isTownHall() {
/* 1359 */     return this instanceof TownHall;
/*      */   }
/*      */   
/*      */   public void markInvalid() {
/* 1363 */     if (getCiv().isAdminCiv()) {
/* 1364 */       this.valid = true;
/*      */     } else {
/* 1366 */       this.valid = false;
/* 1367 */       getTown().invalidStructures.add(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isValid() {
/* 1372 */     if (getCiv().isAdminCiv()) {
/* 1373 */       return true;
/*      */     }
/*      */     
/* 1376 */     return this.valid;
/*      */   }
/*      */   
/*      */   public String getInvalidReason() {
/* 1380 */     return this.invalidReason;
/*      */   }
/*      */   
/*      */   public void setInvalidReason(String invalidReason) {
/* 1384 */     this.invalidReason = invalidReason;
/*      */   }
/*      */   
/*      */   public static int getBlockIDFromSnapshotMap(HashMap<ChunkCoord, ChunkSnapshot> snapshots, int absX, int absY, int absZ, String worldName) throws CivException
/*      */   {
/* 1389 */     int chunkX = ChunkCoord.castToChunkX(absX);
/* 1390 */     int chunkZ = ChunkCoord.castToChunkZ(absZ);
/*      */     
/* 1392 */     int blockChunkX = absX % 16;
/* 1393 */     int blockChunkZ = absZ % 16;
/*      */     
/* 1395 */     if (blockChunkX < 0) {
/* 1396 */       blockChunkX += 16;
/*      */     }
/*      */     
/* 1399 */     if (blockChunkZ < 0) {
/* 1400 */       blockChunkZ += 16;
/*      */     }
/*      */     
/* 1403 */     ChunkCoord coord = new ChunkCoord(worldName, chunkX, chunkZ);
/*      */     
/* 1405 */     ChunkSnapshot snapshot = (ChunkSnapshot)snapshots.get(coord);
/* 1406 */     if (snapshot == null) {
/* 1407 */       throw new CivException("Snapshot for chunk " + chunkX + ", " + chunkZ + " in " + worldName + " not found for abs:" + absX + "," + absZ);
/*      */     }
/*      */     
/* 1410 */     return ItemManager.getBlockTypeId(snapshot, blockChunkX, absY, blockChunkZ);
/*      */   }
/*      */   
/*      */   public static double getReinforcementRequirementForLevel(int level) {
/* 1414 */     if (level > 10) {
/* 1415 */       return validPercentRequirement * 0.3D;
/*      */     }
/*      */     
/* 1418 */     if (level > 40) {
/* 1419 */       return validPercentRequirement * 0.1D;
/*      */     }
/*      */     
/* 1422 */     return validPercentRequirement;
/*      */   }
/*      */   
/*      */   public boolean isIgnoreFloating()
/*      */   {
/* 1427 */     return this.info.ignore_floating;
/*      */   }
/*      */   
/*      */   public void validate(Player player) throws CivException
/*      */   {
/* 1432 */     TaskMaster.asyncTask(new StructureValidator(player, this), 0L);
/*      */   }
/*      */   
/*      */   public void setValid(boolean b) {
/* 1436 */     if (getCiv().isAdminCiv()) {
/* 1437 */       this.valid = true;
/*      */     } else {
/* 1439 */       this.valid = b;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void onGoodieFromFrame() {}
/*      */   
/*      */   public void onGoodieToFrame() {}
/*      */   
/*      */   public void delete()
/*      */     throws SQLException
/*      */   {
/* 1451 */     for (Component comp : this.attachedComponents) {
/* 1452 */       comp.destroyComponent();
/*      */     }
/*      */   }
/*      */   
/*      */   protected List<HashMap<String, String>> getComponentInfoList() {
/* 1457 */     return this.info.components;
/*      */   }
/*      */   
/*      */   public Component getComponent(String name)
/*      */   {
/* 1462 */     for (Component comp : this.attachedComponents) {
/* 1463 */       if (comp.getName().equals(name)) {
/* 1464 */         return comp;
/*      */       }
/*      */     }
/* 1467 */     return null;
/*      */   }
/*      */   
/*      */   public void loadSettings() {}
/*      */   
/*      */   public void onDemolish() throws CivException
/*      */   {}
/*      */   
/*      */   public static int getReinforcementValue(int typeId)
/*      */   {
/* 1477 */     switch (typeId) {
/*      */     case 0: 
/*      */     case 8: 
/*      */     case 9: 
/*      */     case 10: 
/*      */     case 11: 
/*      */     case 30: 
/* 1484 */       return 0;
/*      */     case 42: 
/* 1486 */       return 4;
/*      */     case 98: 
/* 1488 */       return 3;
/*      */     case 1: 
/* 1490 */       return 2;
/*      */     }
/* 1492 */     return 1;
/*      */   }
/*      */   
/*      */ 
/* 1496 */   public boolean hasTemplate() { return this.info.has_template; }
/* 1497 */   public boolean canRestoreFromTemplate() { return true; }
/*      */   
/*      */   public void onInvalidPunish()
/*      */   {
/* 1501 */     BlockCoord center = getCenterLocation();
/*      */     try
/*      */     {
/* 1504 */       invalid_hourly_penalty = CivSettings.getDouble(CivSettings.warConfig, "war.invalid_hourly_penalty");
/*      */     } catch (InvalidConfiguration e) { double invalid_hourly_penalty;
/* 1506 */       e.printStackTrace(); return;
/*      */     }
/*      */     
/*      */     double invalid_hourly_penalty;
/* 1510 */     int damage = (int)(getMaxHitPoints() * invalid_hourly_penalty);
/* 1511 */     if (damage <= 0) {
/* 1512 */       damage = 10;
/*      */     }
/*      */     
/* 1515 */     damage(damage);
/*      */     
/* 1517 */     DecimalFormat df = new DecimalFormat("###");
/* 1518 */     CivMessage.sendTown(getTown(), "§cOur town's " + getDisplayName() + " at (" + 
/* 1519 */       center.getX() + "," + center.getY() + "," + center.getZ() + ") cannot be supported by the blocks underneath!");
/* 1520 */     CivMessage.sendTown(getTown(), "§cIt's lost " + df.format(invalid_hourly_penalty * 100.0D) + "% of it's hitpoints! HP is now (" + this.hitpoints + "/" + getMaxHitPoints() + ")");
/* 1521 */     CivMessage.sendTown(getTown(), "§c" + this.invalidLayerMessage);
/* 1522 */     CivMessage.sendTown(getTown(), "§cFix the blocks on this layer then run '/build validatenearest' to fix it.");
/* 1523 */     save();
/*      */   }
/*      */   
/*      */   public boolean isEnabled() {
/* 1527 */     return this.enabled;
/*      */   }
/*      */   
/* 1530 */   public void setEnabled(boolean enabled) { this.enabled = enabled; }
/*      */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\Buildable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
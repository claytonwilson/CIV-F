/*     */ package com.avrgaming.civcraft.structure;
/*     */ 
/*     */ import com.avrgaming.civcraft.components.Component;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigBuildableInfo;
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.database.SQLUpdate;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.StructureChest;
/*     */ import com.avrgaming.civcraft.object.StructureSign;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.object.TradeGood;
/*     */ import com.avrgaming.civcraft.road.Road;
/*     */ import com.avrgaming.civcraft.template.Template;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import java.io.IOException;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.entity.Player;
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
/*     */ public class Structure
/*     */   extends Buildable
/*     */ {
/*  51 */   public static String TABLE_NAME = "STRUCTURES";
/*     */   
/*     */   public Structure(Location center, String id, Town town) throws CivException {
/*  54 */     this.info = ((ConfigBuildableInfo)CivSettings.structures.get(id));
/*  55 */     setTown(town);
/*  56 */     setCorner(new BlockCoord(center));
/*  57 */     this.hitpoints = this.info.max_hitpoints;
/*     */     
/*     */ 
/*  60 */     Structure struct = CivGlobal.getStructure(getCorner());
/*  61 */     if (struct != null) {
/*  62 */       throw new CivException("There is a structure already here.");
/*     */     }
/*     */   }
/*     */   
/*     */   public Structure(ResultSet rs) throws SQLException, CivException {
/*  67 */     load(rs);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onCheck()
/*     */     throws CivException
/*     */   {}
/*     */   
/*     */ 
/*     */   private static Structure _newStructure(Location center, String id, Town town, ResultSet rs)
/*     */     throws CivException, SQLException
/*     */   {
/*     */     String str;
/*     */     
/*     */     Structure struct;
/*     */     
/*  84 */     switch ((str = id).hashCode()) {case -1785818089:  if (str.equals("s_townhall")) {} break; case -1549674046:  if (str.equals("s_shipyard")) {} break; case -1351633389:  if (str.equals("s_monument")) {} break; case -1326824512:  if (str.equals("ti_farm")) {} break; case -1326608419:  if (str.equals("ti_mine")) {} break; case -1326454102:  if (str.equals("ti_road")) {} break; case -1326318252:  if (str.equals("ti_wall")) {} break; case -911864184:  if (str.equals("s_bank")) break; break; case -846924265:  if (str.equals("s_scouttower")) {} break; case -558661556:  if (str.equals("ti_pasture")) {} break; case -394774612:  if (str.equals("s_trommel")) {} break; case 30718520:  if (str.equals("s_grocer")) {} break; case 64541897:  if (str.equals("ti_trade_outpost")) {} break; case 81287007:  if (str.equals("ti_fishing_boat")) {} break; case 186890632:  if (str.equals("s_market")) {} break; case 375697543:  if (str.equals("s_stable")) {} break; case 390844825:  if (str.equals("s_temple")) {} break; case 777383520:  if (str.equals("s_cannontower")) {} break; case 825600239:  if (str.equals("s_library")) {} break; case 939668804:  if (str.equals("s_granary")) {} break; case 1190404553:  if (str.equals("ti_cottage")) {} break; case 1211647822:  if (str.equals("s_capitol")) {} break; case 1337602028:  if (str.equals("s_arrowtower")) {} break; case 1428220592:  if (str.equals("s_blacksmith")) {} break; case 1523526293:  if (str.equals("s_barracks")) {} break; case 1813248533:  if (str.equals("s_store")) {} break; case 1823024814:  if (!str.equals("ti_windmill")) {
/*     */         break label1447;
/*  86 */         if (rs == null) {
/*  87 */           Structure struct = new Bank(center, id, town);
/*     */           break label1476; }
/*  89 */         Structure struct = new Bank(rs);
/*     */         
/*     */ 
/*     */         break label1476;
/*     */         
/*  94 */         if (rs == null) {
/*  95 */           Structure struct = new Trommel(center, id, town);
/*     */           break label1476; }
/*  97 */         Structure struct = new Trommel(rs);
/*     */         
/*     */ 
/*     */         break label1476;
/*     */         
/* 102 */         if (rs == null) {
/* 103 */           Structure struct = new Store(center, id, town);
/*     */           break label1476; }
/* 105 */         Structure struct = new Store(rs);
/*     */         
/*     */ 
/*     */         break label1476;
/*     */         
/* 110 */         if (rs == null) {
/* 111 */           Structure struct = new Grocer(center, id, town);
/*     */           break label1476; }
/* 113 */         Structure struct = new Grocer(rs);
/*     */         
/*     */ 
/*     */         break label1476;
/*     */         
/* 118 */         if (rs == null) {
/* 119 */           Structure struct = new Library(center, id, town);
/*     */           break label1476; }
/* 121 */         Structure struct = new Library(rs);
/*     */         
/*     */ 
/*     */         break label1476;
/*     */         
/* 126 */         if (rs == null) {
/* 127 */           Structure struct = new Blacksmith(center, id, town);
/*     */           break label1476; }
/* 129 */         Structure struct = new Blacksmith(rs);
/*     */         
/*     */ 
/*     */         break label1476;
/*     */         
/* 134 */         if (rs == null) {
/* 135 */           Structure struct = new Granary(center, id, town);
/*     */           break label1476; }
/* 137 */         Structure struct = new Granary(rs);
/*     */         
/*     */ 
/*     */         break label1476;
/*     */         
/* 142 */         if (rs == null) {
/* 143 */           Structure struct = new Cottage(center, id, town);
/*     */           break label1476; }
/* 145 */         Structure struct = new Cottage(rs);
/*     */         
/*     */         break label1476;
/*     */         
/* 149 */         if (rs == null) {
/* 150 */           Structure struct = new Monument(center, id, town);
/*     */           break label1476; }
/* 152 */         Structure struct = new Monument(rs);
/*     */         
/*     */         break label1476;
/*     */         
/* 156 */         if (rs == null) {
/* 157 */           Structure struct = new Temple(center, id, town);
/*     */           break label1476; }
/* 159 */         Structure struct = new Temple(rs);
/*     */         
/*     */         break label1476;
/*     */         
/* 163 */         if (rs == null) {
/* 164 */           Structure struct = new Mine(center, id, town);
/*     */           break label1476; }
/* 166 */         Structure struct = new Mine(rs);
/*     */         
/*     */         break label1476;
/*     */         
/* 170 */         if (rs == null) {
/* 171 */           Structure struct = new Farm(center, id, town);
/*     */           break label1476; }
/* 173 */         Structure struct = new Farm(rs);
/*     */         
/*     */         break label1476;
/*     */         
/* 177 */         if (rs == null) {
/* 178 */           Structure struct = new TradeOutpost(center, id, town);
/*     */           break label1476; }
/* 180 */         Structure struct = new TradeOutpost(rs);
/*     */         
/*     */         break label1476;
/*     */         
/* 184 */         if (rs == null) {
/* 185 */           Structure struct = new FishingBoat(center, id, town);
/*     */           break label1476; }
/* 187 */         Structure struct = new FishingBoat(rs);
/*     */         
/*     */         break label1476;
/*     */         
/* 191 */         if (rs == null) {
/* 192 */           Structure struct = new TownHall(center, id, town);
/*     */           break label1476; }
/* 194 */         Structure struct = new TownHall(rs);
/*     */         
/*     */         break label1476;
/*     */         
/* 198 */         if (rs == null) {
/* 199 */           Structure struct = new Capitol(center, id, town);
/*     */           break label1476; }
/* 201 */         Structure struct = new Capitol(rs);
/*     */         
/*     */         break label1476;
/*     */         
/* 205 */         if (rs == null) {
/* 206 */           Structure struct = new ArrowTower(center, id, town);
/*     */           break label1476; }
/* 208 */         Structure struct = new ArrowTower(rs);
/*     */         
/*     */         break label1476;
/*     */         
/* 212 */         if (rs == null) {
/* 213 */           Structure struct = new CannonTower(center, id, town);
/*     */           break label1476; }
/* 215 */         Structure struct = new CannonTower(rs);
/*     */         
/*     */         break label1476;
/*     */         
/* 219 */         if (rs == null) {
/* 220 */           Structure struct = new ScoutTower(center, id, town);
/*     */           break label1476; }
/* 222 */         Structure struct = new ScoutTower(rs);
/*     */         
/*     */         break label1476;
/*     */         
/* 226 */         if (rs == null) {
/* 227 */           Structure struct = new WaterStructure(center, id, town);
/*     */           break label1476; }
/* 229 */         Structure struct = new WaterStructure(rs);
/*     */         
/*     */         break label1476;
/*     */         
/* 233 */         if (rs == null) {
/* 234 */           Structure struct = new Wall(center, id, town);
/*     */           break label1476; }
/* 236 */         Structure struct = new Wall(rs);
/*     */         
/*     */         break label1476;
/*     */         
/* 240 */         if (rs == null) {
/* 241 */           Structure struct = new Road(center, id, town);
/*     */           break label1476; }
/* 243 */         Structure struct = new Road(rs);
/*     */         
/*     */         break label1476;
/*     */         
/* 247 */         if (rs == null) {
/* 248 */           Structure struct = new Barracks(center, id, town);
/*     */           break label1476; }
/* 250 */         Structure struct = new Barracks(rs);
/*     */         break label1476;
/*     */       } else {
/*     */         Structure struct;
/* 254 */         if (rs == null) {
/* 255 */           struct = new Windmill(center, id, town);
/*     */         } else {
/* 257 */           Structure struct = new Windmill(rs);
/*     */           
/*     */           break label1476;
/*     */           Structure struct;
/* 261 */           if (rs == null) {
/* 262 */             struct = new Market(center, id, town);
/*     */           } else {
/* 264 */             Structure struct = new Market(rs);
/*     */             
/*     */             break label1476;
/*     */             Structure struct;
/* 268 */             if (rs == null) {
/* 269 */               struct = new Stable(center, id, town);
/*     */             } else {
/* 271 */               Structure struct = new Stable(rs);
/*     */               
/*     */               break label1476;
/*     */               Structure struct;
/* 275 */               if (rs == null) {
/* 276 */                 struct = new Pasture(center, id, town);
/*     */               } else
/* 278 */                 struct = new Pasture(rs);
/*     */             } } } }
/* 280 */       break; }
/*     */     label1447:
/*     */     Structure struct;
/*     */     Structure struct;
/* 284 */     if (rs == null) {
/* 285 */       struct = new Structure(center, id, town);
/*     */     } else {
/* 287 */       struct = new Structure(rs);
/*     */     }
/*     */     
/*     */     label1476:
/*     */     
/* 292 */     struct.loadSettings();
/*     */     
/* 294 */     if (rs == null) {
/* 295 */       struct.saveComponents();
/*     */     } else {
/* 297 */       struct.loadComponents();
/*     */     }
/*     */     
/* 300 */     return struct;
/*     */   }
/*     */   
/*     */   private void loadComponents() {
/* 304 */     for (Component comp : this.attachedComponents) {
/* 305 */       comp.onLoad();
/*     */     }
/*     */   }
/*     */   
/*     */   private void saveComponents() {
/* 310 */     for (Component comp : this.attachedComponents) {
/* 311 */       comp.onSave();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static Structure newStructure(ResultSet rs)
/*     */     throws CivException, SQLException
/*     */   {
/* 319 */     return _newStructure(null, rs.getString("type_id"), null, rs);
/*     */   }
/*     */   
/*     */   public static Structure newStructure(Location center, String id, Town town) throws CivException {
/*     */     try {
/* 324 */       return _newStructure(center, id, town, null);
/*     */     }
/*     */     catch (SQLException e) {
/* 327 */       e.printStackTrace(); }
/* 328 */     return null;
/*     */   }
/*     */   
/*     */   public static void init()
/*     */     throws SQLException
/*     */   {
/* 334 */     if (!SQL.hasTable(TABLE_NAME)) {
/* 335 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + TABLE_NAME + " (" + 
/* 336 */         "`id` int(11) unsigned NOT NULL auto_increment," + 
/* 337 */         "`type_id` mediumtext NOT NULL," + 
/* 338 */         "`town_id` int(11) DEFAULT NULL," + 
/* 339 */         "`complete` bool NOT NULL DEFAULT '0'," + 
/* 340 */         "`builtBlockCount` int(11) DEFAULT NULL, " + 
/* 341 */         "`cornerBlockHash` mediumtext DEFAULT NULL," + 
/* 342 */         "`template_name` mediumtext DEFAULT NULL, " + 
/* 343 */         "`template_x` int(11) DEFAULT NULL, " + 
/* 344 */         "`template_y` int(11) DEFAULT NULL, " + 
/* 345 */         "`template_z` int(11) DEFAULT NULL, " + 
/* 346 */         "`hitpoints` int(11) DEFAULT '100'," + 
/* 347 */         "PRIMARY KEY (`id`)" + ")";
/*     */       
/* 349 */       SQL.makeTable(table_create);
/* 350 */       CivLog.info("Created " + TABLE_NAME + " table");
/*     */     } else {
/* 352 */       CivLog.info(TABLE_NAME + " table OK!");
/*     */     }
/*     */   }
/*     */   
/*     */   public void load(ResultSet rs) throws SQLException, CivException
/*     */   {
/* 358 */     setId(rs.getInt("id"));
/* 359 */     this.info = ((ConfigBuildableInfo)CivSettings.structures.get(rs.getString("type_id")));
/* 360 */     setTown(CivGlobal.getTownFromId(rs.getInt("town_id")));
/*     */     
/* 362 */     if (getTown() == null)
/*     */     {
/*     */ 
/* 365 */       delete();
/*     */       
/*     */ 
/* 368 */       throw new CivException("Coudln't find town ID:" + rs.getInt("town_id") + " for structure " + getDisplayName() + " ID:" + getId());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 373 */     setCorner(new BlockCoord(rs.getString("cornerBlockHash")));
/* 374 */     this.hitpoints = rs.getInt("hitpoints");
/* 375 */     setTemplateName(rs.getString("template_name"));
/* 376 */     setTemplateX(rs.getInt("template_x"));
/* 377 */     setTemplateY(rs.getInt("template_y"));
/* 378 */     setTemplateZ(rs.getInt("template_z"));
/* 379 */     setComplete(rs.getBoolean("complete"));
/* 380 */     setBuiltBlockCount(rs.getInt("builtBlockCount"));
/*     */     
/*     */ 
/* 383 */     getTown().addStructure(this);
/* 384 */     bindStructureBlocks();
/*     */     
/* 386 */     if (!isComplete()) {
/*     */       try {
/* 388 */         resumeBuildFromTemplate();
/*     */       } catch (Exception e) {
/* 390 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void save()
/*     */   {
/* 397 */     SQLUpdate.add(this);
/*     */   }
/*     */   
/*     */   public void saveNow() throws SQLException
/*     */   {
/* 402 */     HashMap<String, Object> hashmap = new HashMap();
/* 403 */     hashmap.put("type_id", getConfigId());
/* 404 */     hashmap.put("town_id", Integer.valueOf(getTown().getId()));
/* 405 */     hashmap.put("complete", Boolean.valueOf(isComplete()));
/* 406 */     hashmap.put("builtBlockCount", Integer.valueOf(getBuiltBlockCount()));
/* 407 */     hashmap.put("cornerBlockHash", getCorner().toString());
/* 408 */     hashmap.put("hitpoints", Integer.valueOf(getHitpoints()));
/* 409 */     hashmap.put("template_name", getSavedTemplatePath());
/* 410 */     hashmap.put("template_x", Integer.valueOf(getTemplateX()));
/* 411 */     hashmap.put("template_y", Integer.valueOf(getTemplateY()));
/* 412 */     hashmap.put("template_z", Integer.valueOf(getTemplateZ()));
/* 413 */     SQL.updateNamedObject(this, hashmap, TABLE_NAME);
/*     */   }
/*     */   
/*     */   public void delete() throws SQLException
/*     */   {
/* 418 */     super.delete();
/*     */     
/* 420 */     if (getTown() != null)
/*     */     {
/* 422 */       if ((this instanceof TradeOutpost))
/*     */       {
/* 424 */         TradeOutpost outpost = (TradeOutpost)this;
/*     */         
/* 426 */         if (outpost.getGood() != null) {
/* 427 */           outpost.getGood().setStruct(null);
/* 428 */           outpost.getGood().setTown(null);
/* 429 */           outpost.getGood().setCiv(null);
/* 430 */           outpost.getGood().save();
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 435 */       for (StructureSign sign : getSigns()) {
/* 436 */         sign.delete();
/*     */       }
/*     */       
/*     */ 
/* 440 */       for (StructureChest chest : getChests()) {
/* 441 */         chest.delete();
/*     */       }
/*     */       try
/*     */       {
/* 445 */         undoFromTemplate();
/*     */       } catch (IOException|CivException e1) {
/* 447 */         e1.printStackTrace();
/* 448 */         fancyDestroyStructureBlocks();
/*     */       }
/*     */       
/* 451 */       CivGlobal.removeStructure(this);
/* 452 */       getTown().removeStructure(this);
/* 453 */       unbindStructureBlocks();
/*     */     }
/*     */     
/* 456 */     SQL.deleteNamedObject(this, TABLE_NAME);
/*     */   }
/*     */   
/*     */   public void updateBuildProgess()
/*     */   {
/* 461 */     if (getId() != 0) {
/* 462 */       HashMap<String, Object> struct_hm = new HashMap();
/* 463 */       struct_hm.put("id", Integer.valueOf(getId()));
/* 464 */       struct_hm.put("type_id", getConfigId());
/* 465 */       struct_hm.put("complete", Boolean.valueOf(isComplete()));
/* 466 */       struct_hm.put("builtBlockCount", Integer.valueOf(this.savedBlockCount));
/*     */       try
/*     */       {
/* 469 */         SQL.updateNamedObjectAsync(this, struct_hm, TABLE_NAME);
/*     */       } catch (SQLException e) {
/* 471 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateSignText() {}
/*     */   
/*     */ 
/*     */   public void build(Player player, Location centerLoc, Template tpl)
/*     */     throws Exception
/*     */   {
/* 483 */     onPreBuild(centerLoc);
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
/* 495 */     doBuild(player, centerLoc, tpl);
/*     */   }
/*     */   
/*     */   public void doBuild(Player player, Location centerLoc, Template tpl)
/*     */     throws CivException, IOException, SQLException
/*     */   {
/* 501 */     Location savedLocation = centerLoc.clone();
/* 502 */     centerLoc = repositionCenter(centerLoc, tpl.dir(), tpl.size_x, tpl.size_z);
/* 503 */     Block centerBlock = centerLoc.getBlock();
/*     */     
/* 505 */     setTotalBlockCount(tpl.size_x * tpl.size_y * tpl.size_z);
/*     */     
/*     */ 
/* 508 */     setTemplateName(tpl.getFilepath());
/* 509 */     setTemplateX(tpl.size_x);
/* 510 */     setTemplateY(tpl.size_y);
/* 511 */     setTemplateZ(tpl.size_z);
/* 512 */     setTemplateAABB(new BlockCoord(centerLoc), tpl);
/*     */     
/* 514 */     checkBlockPermissionsAndRestrictions(player, centerBlock, tpl.size_x, tpl.size_y, tpl.size_z, savedLocation);
/*     */     
/* 516 */     runOnBuild(centerLoc, tpl);
/*     */     
/*     */ 
/* 519 */     getTown().lastBuildableBuilt = this;
/* 520 */     tpl.saveUndoTemplate(getCorner().toString(), getTown().getName(), centerLoc);
/* 521 */     tpl.buildScaffolding(centerLoc);
/*     */     
/*     */ 
/* 524 */     Resident resident = CivGlobal.getResident(player);
/* 525 */     resident.undoPreview();
/* 526 */     startBuildTask(tpl, centerLoc);
/*     */     
/*     */ 
/* 529 */     bind();
/* 530 */     getTown().addStructure(this);
/*     */   }
/*     */   
/*     */   protected void runOnBuild(Location centerLoc, Template tpl)
/*     */     throws CivException
/*     */   {
/* 536 */     if ((getOnBuildEvent() == null) || (getOnBuildEvent().equals(""))) {
/* 537 */       return;
/*     */     }
/*     */     
/* 540 */     if ((getOnBuildEvent().equals("build_farm")) && 
/* 541 */       ((this instanceof Farm))) {
/* 542 */       Farm farm = (Farm)this;
/* 543 */       farm.build_farm(centerLoc);
/*     */     }
/*     */     
/*     */ 
/* 547 */     if ((getOnBuildEvent().equals("build_trade_outpost")) && 
/* 548 */       ((this instanceof TradeOutpost))) {
/* 549 */       TradeOutpost tradeoutpost = (TradeOutpost)this;
/* 550 */       tradeoutpost.build_trade_outpost(centerLoc);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void unbind()
/*     */   {
/* 558 */     CivGlobal.removeStructure(this);
/*     */   }
/*     */   
/*     */   public void bind() {
/* 562 */     CivGlobal.addStructure(this);
/*     */   }
/*     */   
/*     */   public String getDynmapDescription()
/*     */   {
/* 567 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getMarkerIconName()
/*     */   {
/* 573 */     return "bighouse";
/*     */   }
/*     */   
/*     */   public void processUndo()
/*     */     throws CivException
/*     */   {
/* 579 */     if (isTownHall()) {
/* 580 */       throw new CivException("Cannot undo town halls or a capitols, build a new town hall using '/build town hall' or '/build capitol' to move it.");
/*     */     }
/*     */     try
/*     */     {
/* 584 */       delete();
/* 585 */       getTown().removeStructure(this);
/*     */     } catch (SQLException e) {
/* 587 */       e.printStackTrace();
/* 588 */       throw new CivException("Internal database error.");
/*     */     }
/*     */     
/* 591 */     CivMessage.sendTown(getTown(), "§a" + getDisplayName() + " was unbuilt with the undo command.");
/*     */     
/* 593 */     double refund = getCost();
/* 594 */     getTown().depositDirect(refund);
/* 595 */     CivMessage.sendTown(getTown(), "Town refunded " + refund + " coins.");
/*     */     
/* 597 */     unbindStructureBlocks();
/*     */   }
/*     */   
/*     */   public double getRepairCost() {
/* 601 */     return (int)getCost() / 2;
/*     */   }
/*     */   
/*     */ 
/*     */   public void onBonusGoodieUpdate() {}
/*     */   
/*     */   public void onMarkerPlacement(Player player, Location next, ArrayList<Location> locs)
/*     */     throws CivException
/*     */   {}
/*     */   
/*     */   @Deprecated
/*     */   public String getName()
/*     */   {
/* 614 */     return getDisplayName();
/*     */   }
/*     */   
/*     */ 
/*     */   public void onComplete() {}
/*     */   
/*     */ 
/*     */   public void onLoad()
/*     */     throws CivException
/*     */   {}
/*     */   
/*     */ 
/*     */   public void onUnload() {}
/*     */   
/*     */ 
/*     */   public void repairStructureForFree()
/*     */     throws CivException
/*     */   {
/* 632 */     setHitpoints(getMaxHitPoints());
/* 633 */     bindStructureBlocks();
/*     */     try
/*     */     {
/* 636 */       repairFromTemplate();
/*     */     } catch (CivException|IOException e) {
/* 638 */       throw new CivException("Internal template error.");
/*     */     }
/* 640 */     save();
/*     */   }
/*     */   
/*     */   public void repairStructure() throws CivException {
/* 644 */     if ((this instanceof TownHall)) {
/* 645 */       throw new CivException("Town halls and capitols cannot be repaired.");
/*     */     }
/*     */     
/* 648 */     double cost = getRepairCost();
/* 649 */     if (!getTown().getTreasury().hasEnough(cost)) {
/* 650 */       throw new CivException("Your town cannot not afford the " + cost + " coins to build a " + getDisplayName());
/*     */     }
/*     */     
/* 653 */     repairStructureForFree();
/*     */     
/* 655 */     getTown().getTreasury().withdraw(cost);
/* 656 */     CivMessage.sendTown(getTown(), "§eThe town has repaired a " + getDisplayName() + " at " + getCorner());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void loadSettings()
/*     */   {
/* 663 */     List<HashMap<String, String>> compInfoList = getComponentInfoList();
/* 664 */     if (compInfoList != null) {
/* 665 */       for (HashMap<String, String> compInfo : compInfoList) {
/* 666 */         String className = "com.avrgaming.civcraft.components." + (String)compInfo.get("name");
/*     */         try
/*     */         {
/* 669 */           Class<?> someClass = Class.forName(className);
/* 670 */           Component compClass = (Component)someClass.newInstance();
/* 671 */           compClass.setName((String)compInfo.get("name"));
/*     */           
/* 673 */           for (String key : compInfo.keySet()) {
/* 674 */             compClass.setAttribute(key, (String)compInfo.get(key));
/*     */           }
/*     */           
/* 677 */           compClass.createComponent(this, false);
/*     */         } catch (ClassNotFoundException e) {
/* 679 */           e.printStackTrace();
/*     */         } catch (InstantiationException e) {
/* 681 */           e.printStackTrace();
/*     */         } catch (IllegalAccessException e) {
/* 683 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 688 */     super.loadSettings();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\Structure.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
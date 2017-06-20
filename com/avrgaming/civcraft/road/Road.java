/*     */ package com.avrgaming.civcraft.road;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.listener.MarkerPlacementManager;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.BuildableDamageBlock;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.CultureChunk;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.StructureBlock;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import com.avrgaming.civcraft.template.Template;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import com.avrgaming.civcraft.util.FireworkEffectPlayer;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Random;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.Effect;
/*     */ import org.bukkit.FireworkEffect;
/*     */ import org.bukkit.FireworkEffect.Builder;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ public class Road extends Structure
/*     */ {
/*  46 */   private static double MAX_SEGMENT = 100.0D;
/*     */   private static final int WIDTH = 3;
/*     */   private static final int RECURSION_LIMIT = 350;
/*  49 */   public static double ROAD_PLAYER_SPEED = 1.5D;
/*  50 */   public static double ROAD_HORSE_SPEED = 1.1D;
/*  51 */   public static double ROAD_COST_PER_SEGMENT = 1.0D;
/*     */   
/*     */   public static final int HEIGHT = 4;
/*     */   private Date nextRaidDate;
/*     */   private static final int ROAD_MATERIAL = 4;
/*  56 */   private static int DEBUG_DATA = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Road(Location center, String id, Town town)
/*     */     throws CivException
/*     */   {
/*  65 */     super(center, id, town);
/*     */     
/*     */ 
/*  68 */     this.nextRaidDate = new Date();
/*  69 */     this.nextRaidDate.setTime(this.nextRaidDate.getTime() + 86400000L);
/*     */   }
/*     */   
/*     */   public Road(ResultSet rs) throws SQLException, CivException {
/*  73 */     super(rs);
/*  74 */     loadSessionData();
/*     */   }
/*     */   
/*     */   public void loadSettings()
/*     */   {
/*  79 */     super.loadSettings();
/*     */     try
/*     */     {
/*  82 */       ROAD_PLAYER_SPEED = CivSettings.getDouble(CivSettings.structureConfig, "road.player_speed");
/*  83 */       ROAD_HORSE_SPEED = CivSettings.getDouble(CivSettings.structureConfig, "road.horse_speed");
/*  84 */       MAX_SEGMENT = CivSettings.getInteger(CivSettings.structureConfig, "road.max_segment").intValue();
/*  85 */       this.raidLength = CivSettings.getInteger(CivSettings.structureConfig, "road.raid_length").intValue();
/*  86 */       ROAD_COST_PER_SEGMENT = CivSettings.getDouble(CivSettings.structureConfig, "road.cost_per_segment");
/*     */     } catch (InvalidConfiguration e) {
/*  88 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*  93 */   public HashMap<BlockCoord, RoadBlock> roadBlocks = new HashMap();
/*  94 */   private boolean hasOldBlockData = false;
/*  95 */   private HashMap<BlockCoord, SimpleBlock> oldBlockData = new HashMap();
/*  96 */   private int raidLength = 2;
/*  97 */   private int segmentsBuilt = 0;
/*     */   
/*     */   public void processUndo()
/*     */     throws CivException
/*     */   {
/* 102 */     if (!this.hasOldBlockData) {
/* 103 */       throw new CivException("It's been too long since the road was built. Cannot undo. Demolish it instead.");
/*     */     }
/*     */     SimpleBlock sb;
/* 106 */     for (BlockCoord bcoord : this.oldBlockData.keySet()) {
/* 107 */       sb = (SimpleBlock)this.oldBlockData.get(bcoord);
/* 108 */       Block block = bcoord.getBlock();
/* 109 */       ItemManager.setTypeId(block, sb.getType());
/* 110 */       ItemManager.setData(block, sb.getData());
/*     */     }
/*     */     
/* 113 */     LinkedList<RoadBlock> removed = new LinkedList();
/* 114 */     for (RoadBlock rb : this.roadBlocks.values()) {
/* 115 */       removed.add(rb);
/*     */     }
/*     */     
/* 118 */     for (RoadBlock rb : removed) {
/*     */       try {
/* 120 */         rb.delete();
/*     */       } catch (SQLException e) {
/* 122 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/* 126 */     double totalCost = getTotalCost();
/* 127 */     getTown().getTreasury().deposit(totalCost);
/* 128 */     CivMessage.sendTown(getTown(), "§eRefunded " + totalCost + " coins from road construction.");
/*     */     try
/*     */     {
/* 131 */       delete();
/*     */     } catch (SQLException e) {
/* 133 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void onDemolish()
/*     */     throws CivException
/*     */   {
/* 140 */     for (RoadBlock rb : this.roadBlocks.values()) {
/* 141 */       Block block = rb.getCoord().getBlock();
/* 142 */       ItemManager.setTypeId(block, rb.getOldType());
/* 143 */       ItemManager.setData(block, rb.getOldData());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void undoFromTemplate() {}
/*     */   
/*     */   public void delete()
/*     */     throws SQLException
/*     */   {
/* 153 */     CivGlobal.getSessionDB().delete_all(getSessionKey());
/*     */     
/* 155 */     LinkedList<RoadBlock> remove = new LinkedList();
/* 156 */     for (RoadBlock rb : this.roadBlocks.values()) {
/* 157 */       remove.add(rb);
/*     */     }
/*     */     
/* 160 */     for (RoadBlock rb : remove) {
/* 161 */       rb.delete();
/*     */     }
/*     */     
/* 164 */     super.delete();
/*     */   }
/*     */   
/*     */   public void saveNow() throws SQLException
/*     */   {
/* 169 */     super.saveNow();
/*     */     
/* 171 */     for (RoadBlock rb : this.roadBlocks.values()) {
/* 172 */       rb.saveNow();
/*     */     }
/*     */   }
/*     */   
/*     */   public void buildPlayerPreview(Player player, Location centerLoc) throws CivException, java.io.IOException
/*     */   {
/* 178 */     if (!getTown().hasTechnology(getRequiredTechnology())) {
/* 179 */       throw new CivException("We don't have the technology yet.");
/*     */     }
/*     */     
/* 182 */     if (com.avrgaming.civcraft.war.War.isWarTime()) {
/* 183 */       throw new CivException("Cannot build roads during WarTime.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 190 */     MarkerPlacementManager.addToPlacementMode(player, this, "Road Marker");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void build(Player player, Location centerLoc, Template tpl)
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void onMarkerPlacement(Player player, Location next, ArrayList<Location> locs)
/*     */     throws CivException
/*     */   {
/* 205 */     CultureChunk cc = CivGlobal.getCultureChunk(next);
/* 206 */     if ((cc != null) && (cc.getTown().getCiv() != getTown().getCiv())) {
/* 207 */       throw new CivException("Cannot build roads in the culture that is not yours.");
/*     */     }
/*     */     
/* 210 */     if (locs.size() <= 1) {
/* 211 */       CivMessage.send(player, "§7First location placed, place another to start build a Road.");
/* 212 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 217 */     double distance = ((Location)locs.get(0)).distance((Location)locs.get(1));
/* 218 */     if (distance > MAX_SEGMENT) {
/* 219 */       throw new CivException("Can only build a road in " + MAX_SEGMENT + " block segments, pick a closer location");
/*     */     }
/*     */     
/* 222 */     MarkerPlacementManager.removeFromPlacementMode(player, false);
/*     */     
/*     */ 
/* 225 */     ((Location)locs.get(0)).add(0.0D, -1.0D, 0.0D);
/* 226 */     ((Location)locs.get(1)).add(0.0D, -1.0D, 0.0D);
/*     */     
/*     */ 
/* 229 */     HashMap<String, SimpleBlock> simpleBlocks = new HashMap();
/* 230 */     this.segmentsBuilt = buildRoadSegment(player, (Location)locs.get(1), (Location)locs.get(0), 0, simpleBlocks, 0);
/*     */     
/*     */ 
/* 233 */     LinkedList<SimpleBlock> removed = new LinkedList();
/* 234 */     for (SimpleBlock sb : simpleBlocks.values()) {
/* 235 */       BlockCoord bcoord = new BlockCoord(sb);
/* 236 */       if (!validateBlockLocation(bcoord))
/*     */       {
/* 238 */         removed.add(sb);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 243 */     for (SimpleBlock sb : removed) {
/* 244 */       simpleBlocks.remove(sb.getKey());
/*     */     }
/*     */     
/* 247 */     double totalCost = getTotalCost();
/* 248 */     if (!getTown().getTreasury().hasEnough(totalCost)) {
/* 249 */       throw new CivException("You do not have the required " + totalCost + " coins in the town treasury to build this road.");
/*     */     }
/*     */     int i;
/* 252 */     for (Iterator localIterator2 = simpleBlocks.values().iterator(); localIterator2.hasNext(); 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 258 */         i < 4)
/*     */     {
/* 252 */       SimpleBlock sb = (SimpleBlock)localIterator2.next();
/* 253 */       BlockCoord bcoord = new BlockCoord(sb);
/*     */       
/* 255 */       this.oldBlockData.put(new BlockCoord(bcoord), new SimpleBlock(bcoord.getBlock()));
/*     */       
/*     */ 
/* 258 */       i = 1; continue;
/* 259 */       BlockCoord bcoord2 = new BlockCoord(bcoord);
/* 260 */       bcoord2.setY(sb.y + i);
/* 261 */       this.oldBlockData.put(new BlockCoord(bcoord2), new SimpleBlock(bcoord2.getBlock()));i++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 267 */     this.hasOldBlockData = true;
/* 268 */     int i; for (localIterator2 = simpleBlocks.values().iterator(); localIterator2.hasNext(); 
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
/* 280 */         i < 4)
/*     */     {
/* 268 */       SimpleBlock sb = (SimpleBlock)localIterator2.next();
/* 269 */       BlockCoord bcoord = new BlockCoord(sb);
/*     */       
/*     */ 
/*     */ 
/* 273 */       addRoadBlock(bcoord);
/*     */       
/*     */ 
/* 276 */       ItemManager.setTypeId(bcoord.getBlock(), sb.getType());
/* 277 */       ItemManager.setData(bcoord.getBlock(), sb.getData());
/*     */       
/*     */ 
/* 280 */       i = 1; continue;
/* 281 */       BlockCoord bcoord2 = new BlockCoord(bcoord);
/* 282 */       bcoord2.setY(sb.y + i);
/* 283 */       if (!simpleBlocks.containsKey(SimpleBlock.getKeyFromBlockCoord(bcoord2))) {
/* 284 */         ItemManager.setTypeId(bcoord2.getBlock(), 0);
/*     */       }
/* 280 */       i++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 291 */     getTown().getTreasury().withdraw(totalCost);
/* 292 */     CivMessage.sendTown(getTown(), "§aOur town spent §e" + totalCost + "§a" + " coins constructing a road.");
/* 293 */     getTown().addStructure(this);
/* 294 */     CivGlobal.addStructure(this);
/* 295 */     getTown().lastBuildableBuilt = this;
/* 296 */     setComplete(true);
/* 297 */     setCorner(new BlockCoord((Location)locs.get(0)));
/* 298 */     locs.clear();
/* 299 */     save();
/* 300 */     saveSaveSessionData();
/*     */   }
/*     */   
/*     */ 
/*     */   private double getTotalCost()
/*     */   {
/* 306 */     double total = this.segmentsBuilt * ROAD_COST_PER_SEGMENT;
/* 307 */     return total;
/*     */   }
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
/*     */   private boolean validateBlockLocation(BlockCoord startCoord)
/*     */     throws CivException
/*     */   {
/* 328 */     boolean allowedToPlaceHere = true;
/* 329 */     for (int i = 0; i < 4; i++) {
/* 330 */       BlockCoord bcoord = new BlockCoord(startCoord);
/* 331 */       ChunkCoord coord = new ChunkCoord(bcoord);
/* 332 */       bcoord.setY(startCoord.getY() + i);
/*     */       
/* 334 */       if (ItemManager.getId(bcoord.getBlock()) == 54) {
/* 335 */         throw new CivException("Cannot build a road here. Would destroy a chest at " + bcoord.toString());
/*     */       }
/*     */       
/* 338 */       if (CivGlobal.getProtectedBlock(bcoord) != null) {
/* 339 */         throw new CivException("Cannot build a road here. Would destroy protected block at " + bcoord.toString());
/*     */       }
/*     */       
/* 342 */       if (CivGlobal.getCampFromChunk(coord) != null) {
/* 343 */         throw new CivException("Cannot build a road into a chunk which contains a camp.");
/*     */       }
/*     */       
/* 346 */       StructureBlock structBlock = CivGlobal.getStructureBlock(bcoord);
/* 347 */       if (structBlock != null) {
/* 348 */         allowedToPlaceHere = false;
/* 349 */         if (structBlock.getCiv() != getCiv()) {
/* 350 */           throw new CivException("Cannot build a road here. Structure block belonging to " + structBlock.getCiv().getName() + 
/* 351 */             " at " + structBlock.getX() + ", " + structBlock.getY() + ", " + structBlock.getZ() + " is in the way.");
/*     */         }
/*     */       }
/*     */       
/* 355 */       RoadBlock rb = CivGlobal.getRoadBlock(bcoord);
/* 356 */       if (rb != null) {
/* 357 */         allowedToPlaceHere = false;
/* 358 */         if (rb.getRoad().getCiv() != getCiv()) {
/* 359 */           throw new CivException("Cannot build a road here. Road belonging to " + rb.getRoad().getCiv().getName() + 
/* 360 */             " at block at " + rb.getCoord().getX() + ", " + rb.getCoord().getY() + ", " + rb.getCoord().getZ() + " is in the way.");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 365 */     return allowedToPlaceHere;
/*     */   }
/*     */   
/*     */   private String getSessionKey()
/*     */   {
/* 370 */     return "Road:" + getCorner().toString();
/*     */   }
/*     */   
/*     */   private int buildRoadSegment(Player player, Location locFirst, Location locSecond, int blockCount, HashMap<String, SimpleBlock> simpleBlocks, int segments)
/*     */     throws CivException
/*     */   {
/* 376 */     Vector dir = new Vector(locFirst.getX() - locSecond.getX(), 
/* 377 */       locFirst.getY() - locSecond.getY(), 
/* 378 */       locFirst.getZ() - locSecond.getZ());
/* 379 */     dir.normalize();
/*     */     
/* 381 */     dir.multiply(0.5D);
/* 382 */     getHorizontalSegment(player, locSecond, simpleBlocks);
/* 383 */     segments++;
/*     */     
/*     */ 
/* 386 */     double distance = locSecond.distance(locFirst);
/*     */     
/* 388 */     BlockCoord lastBlockCoord = new BlockCoord(locSecond);
/* 389 */     BlockCoord currentBlockCoord = new BlockCoord(locSecond);
/* 390 */     int lastY = locSecond.getBlockY();
/*     */     
/* 392 */     while (locSecond.distance(locFirst) > 1.0D) {
/* 393 */       locSecond.add(dir);
/*     */       
/* 395 */       currentBlockCoord.setFromLocation(locSecond);
/* 396 */       if (lastBlockCoord.distance(currentBlockCoord) >= 3.0D)
/*     */       {
/*     */ 
/* 399 */         lastBlockCoord.setFromLocation(locSecond);
/*     */         
/*     */ 
/*     */ 
/* 403 */         if (Math.abs(lastY - locSecond.getBlockY()) > 1.0D) {
/* 404 */           throw new CivException("Road is too steep to be built here. Try lowering the one of the end points to make the road less steep.");
/*     */         }
/*     */         
/* 407 */         if (locSecond.getBlockY() < 5) {
/* 408 */           throw new CivException("Cannot build road blocks within 5 blocks of bedrock.");
/*     */         }
/*     */         
/* 411 */         lastY = locSecond.getBlockY();
/*     */         
/* 413 */         blockCount++;
/* 414 */         if (blockCount > 350) {
/* 415 */           throw new CivException("ERROR: Building road blocks exceeded recursion limit! Halted to keep server alive.");
/*     */         }
/*     */         
/* 418 */         getHorizontalSegment(player, locSecond, simpleBlocks);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 424 */         segments++;
/*     */         
/*     */ 
/*     */ 
/* 428 */         double tmpDist = locSecond.distance(locFirst);
/* 429 */         if (tmpDist > distance) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 438 */     getHorizontalSegment(player, locFirst, simpleBlocks);
/* 439 */     return segments;
/*     */   }
/*     */   
/*     */   private void getHorizontalSegment(Player player, Location loc, HashMap<String, SimpleBlock> simpleBlocks)
/*     */   {
/* 444 */     Location tmp = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 450 */     getCircle(tmp.getBlockX(), tmp.getBlockY(), tmp.getBlockZ(), tmp.getWorld().getName(), 3, simpleBlocks);
/*     */   }
/*     */   
/*     */   public static void getCircle(int blockX, int blockY, int blockZ, String world, int radius, HashMap<String, SimpleBlock> simpleBlocks)
/*     */   {
/* 455 */     int error = -radius;
/* 456 */     int x = radius;
/* 457 */     int z = 0;
/*     */     
/* 459 */     while (x >= z) {
/* 460 */       int lastZ = z;
/*     */       
/* 462 */       error += z;
/* 463 */       z++;
/* 464 */       error += z;
/*     */       
/* 466 */       plotFourPoints(blockX, blockZ, x, lastZ, blockY, world, simpleBlocks);
/*     */       
/* 468 */       if (error >= 0) {
/* 469 */         if (x != lastZ) {
/* 470 */           plotFourPoints(blockX, blockZ, lastZ, x, blockY, world, simpleBlocks);
/*     */         }
/*     */         
/* 473 */         error -= x;
/* 474 */         x--;
/* 475 */         error -= x;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void setPixel(int x, int y, int z, String world, HashMap<String, SimpleBlock> simpleBlocks)
/*     */   {
/* 482 */     SimpleBlock sb = new SimpleBlock(4, DEBUG_DATA);
/* 483 */     sb.worldname = world;
/* 484 */     sb.x = x;
/* 485 */     sb.y = y;
/* 486 */     sb.z = z;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 491 */     simpleBlocks.put(sb.getKey(), sb);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void plotFourPoints(int cx, int cz, int x, int z, int baseY, String world, HashMap<String, SimpleBlock> simpleBlocks)
/*     */   {
/* 503 */     horizontalLine(cx - x, baseY, cz + z, cx + x, world, simpleBlocks);
/* 504 */     if ((x != 0) && (z != 0)) {
/* 505 */       horizontalLine(cx - x, baseY, cz - z, cx + x, world, simpleBlocks);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void horizontalLine(int x, int y, int z, int size, String world, HashMap<String, SimpleBlock> simpleBlocks) {
/* 510 */     for (int i = x; i <= size; i++) {
/* 511 */       setPixel(i, y, z, world, simpleBlocks);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean showOnDynmap()
/*     */   {
/* 517 */     return false;
/*     */   }
/*     */   
/*     */   public String getDynmapDescription()
/*     */   {
/* 522 */     return "";
/*     */   }
/*     */   
/*     */   public String getMarkerIconName()
/*     */   {
/* 527 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void bindStructureBlocks() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addBlocksAbove(RoadBlock rb)
/*     */   {
/* 541 */     for (int i = 1; i < 4; i++) {
/* 542 */       BlockCoord bcoord = new BlockCoord(rb.getCoord());
/* 543 */       bcoord.setY(rb.getCoord().getY() + i);
/*     */       
/* 545 */       SimpleBlock sb = (SimpleBlock)this.oldBlockData.get(bcoord);
/* 546 */       RoadBlock rb2 = new RoadBlock(sb.getType(), sb.getData());
/* 547 */       rb2.setCoord(bcoord);
/* 548 */       rb2.setRoad(rb.getRoad());
/* 549 */       rb2.setAboveRoadBlock(true);
/* 550 */       this.roadBlocks.put(bcoord, rb2);
/* 551 */       CivGlobal.addRoadBlock(rb2);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addRoadBlock(BlockCoord coord) {
/* 556 */     SimpleBlock sb = (SimpleBlock)this.oldBlockData.get(coord);
/* 557 */     RoadBlock rb = new RoadBlock(sb.getType(), sb.getData());
/* 558 */     rb.setCoord(coord);
/* 559 */     rb.setRoad(this);
/*     */     
/* 561 */     this.roadBlocks.put(coord, rb);
/* 562 */     CivGlobal.addRoadBlock(rb);
/* 563 */     addBlocksAbove(rb);
/*     */   }
/*     */   
/*     */   public void addRoadBlock(RoadBlock rb) {
/* 567 */     this.roadBlocks.put(rb.getCoord(), rb);
/* 568 */     CivGlobal.addRoadBlock(rb);
/*     */   }
/*     */   
/*     */   public void deleteRoadBlock(RoadBlock roadBlock) {
/*     */     try {
/* 573 */       roadBlock.delete();
/*     */       
/* 575 */       if (this.roadBlocks.size() == 0)
/*     */       {
/* 577 */         CivMessage.sendTown(getTown(), "Our road near " + getCorner() + " has been destroyed!");
/* 578 */         delete();
/*     */       }
/*     */     } catch (SQLException e) {
/* 581 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeRoadBlock(RoadBlock roadBlock) {
/* 586 */     this.roadBlocks.remove(roadBlock.getCoord());
/* 587 */     CivGlobal.removeRoadBlock(roadBlock);
/*     */   }
/*     */   
/*     */   private void saveSaveSessionData() {
/* 591 */     sessionAdd(getSessionKey(), this.nextRaidDate.getTime() + ":" + this.segmentsBuilt);
/*     */   }
/*     */   
/*     */   private void loadSessionData() {
/* 595 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(getSessionKey());
/* 596 */     if (entries == null) {
/* 597 */       saveSaveSessionData();
/*     */     }
/*     */     
/* 600 */     String[] split = ((SessionEntry)entries.get(0)).value.split(":");
/*     */     
/* 602 */     long time = Long.valueOf(split[0]).longValue();
/* 603 */     this.nextRaidDate = new Date(time);
/* 604 */     this.segmentsBuilt = Integer.valueOf(split[1]).intValue();
/*     */   }
/*     */   
/*     */   public Date getNextRaidDate() {
/* 608 */     Date raidEnd = new Date(this.nextRaidDate.getTime());
/* 609 */     raidEnd.setTime(this.nextRaidDate.getTime() + 3600000 * this.raidLength);
/*     */     
/* 611 */     Date now = new Date();
/* 612 */     if (now.getTime() > raidEnd.getTime()) {
/* 613 */       this.nextRaidDate.setTime(this.nextRaidDate.getTime() + 86400000L);
/*     */     }
/*     */     
/* 616 */     return this.nextRaidDate;
/*     */   }
/*     */   
/*     */   public void setNextRaidDate(Date next) {
/* 620 */     this.nextRaidDate = next;
/* 621 */     save();
/*     */   }
/*     */   
/*     */   public void onDamage(int amount, World world, Player player, BlockCoord coord, BuildableDamageBlock hit)
/*     */   {
/* 626 */     boolean wasTenPercent = false;
/*     */     
/* 628 */     if (hit.getOwner().isDestroyed()) {
/* 629 */       CivMessage.sendError(player, hit.getOwner().getDisplayName() + " is already destroyed.");
/* 630 */       return;
/*     */     }
/*     */     
/* 633 */     if ((!hit.getOwner().isComplete()) && (!(hit.getOwner() instanceof com.avrgaming.civcraft.structure.wonders.Wonder))) {
/* 634 */       CivMessage.sendError(player, hit.getOwner().getDisplayName() + " is still being built, cannot be destroyed.");
/* 635 */       return;
/*     */     }
/*     */     
/* 638 */     if (hit.getOwner().getDamagePercentage() % 10 == 0) {
/* 639 */       wasTenPercent = true;
/*     */     }
/*     */     
/* 642 */     damage(amount);
/*     */     
/* 644 */     world.playSound(hit.getCoord().getLocation(), org.bukkit.Sound.ANVIL_USE, 0.2F, 1.0F);
/* 645 */     world.playEffect(hit.getCoord().getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
/*     */     
/* 647 */     if ((hit.getOwner().getDamagePercentage() % 10 == 0) && (!wasTenPercent)) {
/* 648 */       onDamageNotification(player, hit);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void onDestroy()
/*     */   {
/* 655 */     CivMessage.global("A " + getDisplayName() + " in " + getTown().getName() + " has been destroyed!");
/* 656 */     this.hitpoints = 0;
/* 657 */     fancyDestroyStructureBlocks();
/*     */     try {
/* 659 */       delete();
/*     */     } catch (SQLException e) {
/* 661 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void fancyDestroyStructureBlocks() {
/* 666 */     for (BlockCoord coord : this.roadBlocks.keySet())
/*     */     {
/* 668 */       if (CivGlobal.getStructureChest(coord) == null)
/*     */       {
/*     */ 
/*     */ 
/* 672 */         if (CivGlobal.getStructureSign(coord) == null)
/*     */         {
/*     */ 
/*     */ 
/* 676 */           if (ItemManager.getId(coord.getBlock()) != 0)
/*     */           {
/*     */ 
/*     */ 
/* 680 */             if (ItemManager.getId(coord.getBlock()) != 54)
/*     */             {
/*     */ 
/*     */ 
/* 684 */               if (ItemManager.getId(coord.getBlock()) != 63)
/*     */               {
/*     */ 
/*     */ 
/* 688 */                 if (ItemManager.getId(coord.getBlock()) != 68)
/*     */                 {
/*     */ 
/*     */ 
/* 692 */                   if (CivSettings.alwaysCrumble.contains(Integer.valueOf(ItemManager.getId(coord.getBlock())))) {
/* 693 */                     ItemManager.setTypeId(coord.getBlock(), 13);
/*     */                   }
/*     */                   else
/*     */                   {
/* 697 */                     Random rand = new Random();
/*     */                     
/*     */ 
/* 700 */                     if (rand.nextInt(100) <= 10) {
/* 701 */                       ItemManager.setTypeId(coord.getBlock(), 13);
/* 702 */                       ItemManager.setData(coord.getBlock(), 0, true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                     }
/* 708 */                     else if (rand.nextInt(100) <= 50) {
/* 709 */                       ItemManager.setTypeId(coord.getBlock(), 51);
/* 710 */                       ItemManager.setData(coord.getBlock(), 0, true);
/*     */ 
/*     */ 
/*     */ 
/*     */                     }
/* 715 */                     else if (rand.nextInt(100) <= 1) {
/* 716 */                       FireworkEffect effect = FireworkEffect.builder().with(org.bukkit.FireworkEffect.Type.BURST).withColor(Color.ORANGE).withColor(Color.RED).withTrail().withFlicker().build();
/* 717 */                       FireworkEffectPlayer fePlayer = new FireworkEffectPlayer();
/* 718 */                       for (int i = 0; i < 3; i++) {
/*     */                         try {
/* 720 */                           fePlayer.playFirework(coord.getBlock().getWorld(), coord.getLocation(), effect);
/*     */                         } catch (Exception e) {
/* 722 */                           e.printStackTrace();
/*     */                         }
/*     */                       }
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\road\Road.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.avrgaming.civcraft.structure;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigBuildableInfo;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.listener.MarkerPlacementManager;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.BuffManager;
/*     */ import com.avrgaming.civcraft.object.CultureChunk;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.StructureBlock;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.object.TownChunk;
/*     */ import com.avrgaming.civcraft.object.WallBlock;
/*     */ import com.avrgaming.civcraft.permission.PlotPermissions;
/*     */ import com.avrgaming.civcraft.permission.PlotPermissions.Type;
/*     */ import com.avrgaming.civcraft.template.Template;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock;
/*     */ import com.avrgaming.civcraft.war.War;
/*     */ import java.io.IOException;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.util.Vector;
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
/*     */ public class Wall
/*     */   extends Structure
/*     */ {
/*     */   private static final double MAX_SEGMENT = 300.0D;
/*     */   private static final int RECURSION_LIMIT = 350;
/*     */   private static int HEIGHT;
/*     */   private static int MAX_HEIGHT;
/*     */   private static double COST_PER_SEGMENT;
/*     */   
/*     */   public static void init_settings()
/*     */     throws InvalidConfiguration
/*     */   {
/*  68 */     HEIGHT = CivSettings.getInteger(CivSettings.warConfig, "wall.height").intValue();
/*  69 */     MAX_HEIGHT = CivSettings.getInteger(CivSettings.warConfig, "wall.maximum_height").intValue();
/*  70 */     COST_PER_SEGMENT = CivSettings.getDouble(CivSettings.warConfig, "wall.cost_per_segment");
/*     */   }
/*     */   
/*  73 */   public Map<BlockCoord, WallBlock> wallBlocks = new HashMap();
/*  74 */   public HashSet<ChunkCoord> wallChunks = new HashSet();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  80 */   private Wall nextWallBuilt = null;
/*     */   
/*     */ 
/*     */ 
/*     */   protected Wall(Location center, String id, Town town)
/*     */     throws CivException
/*     */   {
/*  87 */     super(center, id, town);
/*     */   }
/*     */   
/*     */   public Wall(ResultSet rs) throws SQLException, CivException {
/*  91 */     super(rs);
/*  92 */     this.hitpoints = getMaxHitPoints();
/*     */   }
/*     */   
/*     */ 
/*     */   public void bindStructureBlocks() {}
/*     */   
/*     */ 
/*     */   public boolean hasTemplate()
/*     */   {
/* 101 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canRestoreFromTemplate()
/*     */   {
/* 106 */     return false;
/*     */   }
/*     */   
/*     */   public void processUndo()
/*     */     throws CivException
/*     */   {
/* 112 */     double refund = 0.0D;
/* 113 */     for (WallBlock wb : this.wallBlocks.values()) {
/* 114 */       ItemManager.setTypeId(wb.getCoord().getBlock(), wb.getOldId());
/* 115 */       ItemManager.setData(wb.getCoord().getBlock(), wb.getOldData());
/* 116 */       refund += COST_PER_SEGMENT;
/*     */       try {
/* 118 */         wb.delete();
/*     */       } catch (SQLException e) {
/* 120 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/* 124 */     refund /= HEIGHT;
/* 125 */     refund = Math.round(refund);
/* 126 */     getTown().getTreasury().deposit(refund);
/* 127 */     CivMessage.sendTown(getTown(), "§eRefunded " + refund + " coins from wall construction.");
/*     */     try {
/* 129 */       delete();
/*     */     } catch (SQLException e) {
/* 131 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void unbindStructureBlocks()
/*     */   {
/* 137 */     super.unbindStructureBlocks();
/*     */   }
/*     */   
/*     */   protected Location repositionCenter(Location center, String dir, double x_size, double z_size)
/*     */   {
/* 142 */     return center;
/*     */   }
/*     */   
/*     */   public void resumeBuildFromTemplate()
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */   public void delete()
/*     */     throws SQLException
/*     */   {
/* 152 */     if (this.wallBlocks != null) {
/* 153 */       for (WallBlock wb : this.wallBlocks.values()) {
/* 154 */         wb.delete();
/*     */       }
/*     */     }
/*     */     
/* 158 */     if (this.wallChunks != null) {
/* 159 */       for (ChunkCoord coord : this.wallChunks) {
/* 160 */         CivGlobal.removeWallChunk(this, coord);
/*     */       }
/*     */     }
/*     */     
/* 164 */     super.delete();
/*     */   }
/*     */   
/*     */ 
/*     */   public void undoFromTemplate()
/*     */   {
/* 170 */     if (this.nextWallBuilt == null) {
/* 171 */       for (BlockCoord coord : this.wallBlocks.keySet()) {
/* 172 */         WallBlock wb = (WallBlock)this.wallBlocks.get(coord);
/* 173 */         ItemManager.setTypeId(coord.getBlock(), wb.getOldId());
/* 174 */         ItemManager.setData(coord.getBlock(), wb.getOldData());
/*     */         try {
/* 176 */           wb.delete();
/*     */         } catch (SQLException e) {
/* 178 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 183 */       ChunkCoord coord = new ChunkCoord(getCorner());
/* 184 */       CivGlobal.removeWallChunk(this, coord);
/*     */     } else {
/*     */       try {
/* 187 */         this.nextWallBuilt.processUndo();
/*     */       } catch (CivException e) {
/* 189 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void checkBlockPermissionsAndRestrictions(Player player, Block centerBlock, int regionX, int regionY, int regionZ, Location savedLocation)
/*     */     throws CivException
/*     */   {}
/*     */   
/*     */ 
/*     */   public synchronized void buildRepairTemplate(Template tpl, Block centerBlock) {}
/*     */   
/*     */ 
/*     */   public void buildPlayerPreview(Player player, Location centerLoc)
/*     */     throws CivException, IOException
/*     */   {
/* 206 */     if (!getTown().hasTechnology(getRequiredTechnology())) {
/* 207 */       throw new CivException("We don't have the technology yet.");
/*     */     }
/*     */     
/* 210 */     if (War.isWarTime()) {
/* 211 */       throw new CivException("Cannot build walls during WarTime.");
/*     */     }
/*     */     
/* 214 */     MarkerPlacementManager.addToPlacementMode(player, this, "Wall Marker");
/*     */   }
/*     */   
/*     */ 
/*     */   public void build(Player player, Location centerLoc, Template tpl)
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */ 
/*     */   private boolean isValidWall()
/*     */   {
/*     */     int y;
/*     */     
/* 227 */     for (Iterator localIterator = this.wallBlocks.values().iterator(); localIterator.hasNext(); 
/*     */         
/*     */ 
/* 230 */         y < 256)
/*     */     {
/* 227 */       WallBlock block = (WallBlock)localIterator.next();
/* 228 */       BlockCoord bcoord = new BlockCoord(block.getCoord());
/*     */       
/* 230 */       y = 0; continue;
/* 231 */       bcoord.setY(y);
/*     */       
/* 233 */       StructureBlock sb = CivGlobal.getStructureBlock(bcoord);
/* 234 */       if ((sb != null) && 
/* 235 */         (sb.getOwner() != this)) {
/* 236 */         return false;
/*     */       }
/* 230 */       y++;
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
/*     */ 
/* 242 */     return true;
/*     */   }
/*     */   
/*     */   public boolean showOnDynmap()
/*     */   {
/* 247 */     return false;
/*     */   }
/*     */   
/*     */   public void onMarkerPlacement(Player player, Location next, ArrayList<Location> locs) throws CivException
/*     */   {
/* 252 */     BlockCoord first = new BlockCoord(next);
/* 253 */     BlockCoord second = null;
/*     */     
/* 255 */     CultureChunk cc = CivGlobal.getCultureChunk(next);
/* 256 */     if ((cc == null) || (cc.getTown().getCiv() != getTown().getCiv())) {
/* 257 */       throw new CivException("Cannot build here, you need to build inside your culture.");
/*     */     }
/*     */     
/* 260 */     if (locs.size() <= 1) {
/* 261 */       CivMessage.send(player, "§7First location placed, place another to start building a wall.");
/* 262 */       return;
/*     */     }
/*     */     
/*     */ 
/* 266 */     if (((Location)locs.get(0)).distance((Location)locs.get(1)) > 300.0D) {
/* 267 */       throw new CivException("Can only build a wall in 300.0 block segments, pick a closer location");
/*     */     }
/*     */     
/*     */ 
/* 271 */     second = new BlockCoord((Location)locs.get(0));
/* 272 */     locs.clear();
/* 273 */     MarkerPlacementManager.removeFromPlacementMode(player, false);
/*     */     
/*     */ 
/* 276 */     Location secondLoc = second.getLocation();
/*     */     
/* 278 */     setCorner(new BlockCoord(secondLoc));
/* 279 */     setComplete(true);
/* 280 */     save();
/*     */     
/*     */ 
/* 283 */     HashMap<String, SimpleBlock> simpleBlocks = new HashMap();
/* 284 */     int verticalSegments = buildWallSegment(player, first, second, 0, simpleBlocks, 0);
/*     */     
/*     */ 
/* 287 */     double cost = verticalSegments * COST_PER_SEGMENT;
/* 288 */     if (!getTown().getTreasury().hasEnough(cost))
/*     */     {
/* 290 */       for (WallBlock wb : this.wallBlocks.values()) {
/*     */         try {
/* 292 */           wb.delete();
/*     */         } catch (SQLException e) {
/* 294 */           e.printStackTrace();
/*     */         }
/*     */       }
/* 297 */       this.wallBlocks.clear();
/*     */       
/* 299 */       throw new CivException("Cannot build, not enough coins to pay " + cost + " coins for wall of length " + verticalSegments + " blocks.");
/*     */     }
/*     */     
/* 302 */     getTown().getTreasury().withdraw(cost);
/*     */     
/* 304 */     CivMessage.sendTown(getTown(), "§ePaid " + cost + " coins for " + verticalSegments + " wall segments.");
/*     */     
/*     */ 
/* 307 */     for (SimpleBlock sb : simpleBlocks.values()) {
/* 308 */       BlockCoord bcoord = new BlockCoord(sb);
/* 309 */       ItemManager.setTypeId(bcoord.getBlock(), sb.getType());
/* 310 */       ItemManager.setData(bcoord.getBlock(), sb.getData());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 315 */     getTown().addStructure(this);
/* 316 */     CivGlobal.addStructure(this);
/* 317 */     getTown().lastBuildableBuilt = this;
/*     */   }
/*     */   
/*     */   private void validateBlockLocation(Player player, Location loc) throws CivException {
/* 321 */     Block b = loc.getBlock();
/*     */     
/* 323 */     if (ItemManager.getId(b) == 54) {
/* 324 */       throw new CivException("Cannot build here, would destroy chest.");
/*     */     }
/*     */     
/* 327 */     TownChunk tc = CivGlobal.getTownChunk(b.getLocation());
/*     */     
/* 329 */     if ((tc != null) && (!tc.perms.hasPermission(PlotPermissions.Type.DESTROY, CivGlobal.getResident(player))))
/*     */     {
/* 331 */       throw new CivException("Cannot build here, you need DESTROY permissions to the block at " + b.getX() + "," + b.getY() + "," + b.getZ());
/*     */     }
/*     */     
/* 334 */     BlockCoord coord = new BlockCoord(b);
/*     */     
/* 336 */     if (CivGlobal.getProtectedBlock(coord) != null) {
/* 337 */       throw new CivException("Cannot build here, protected blocks in the way.");
/*     */     }
/*     */     
/*     */ 
/* 341 */     if (CivGlobal.getStructureBlock(coord) != null) {
/* 342 */       throw new CivException("Cannot build here, structure blocks in the way at " + coord);
/*     */     }
/*     */     
/*     */ 
/* 346 */     if (CivGlobal.getFarmChunk(new ChunkCoord(coord.getLocation())) != null) {
/* 347 */       throw new CivException("Cannot build here, in the same chunk as a farm improvement.");
/*     */     }
/*     */     
/* 350 */     if (loc.getBlockY() >= MAX_HEIGHT) {
/* 351 */       throw new CivException("Cannot build here, wall is too high.");
/*     */     }
/*     */     
/* 354 */     if (loc.getBlockY() <= 1) {
/* 355 */       throw new CivException("Cannot build here, too close to bedrock.");
/*     */     }
/*     */     
/* 358 */     BlockCoord bcoord = new BlockCoord(loc);
/* 359 */     for (int y = 0; y < 256; y++) {
/* 360 */       bcoord.setY(y);
/* 361 */       StructureBlock sb = CivGlobal.getStructureBlock(bcoord);
/* 362 */       if (sb != null) {
/* 363 */         throw new CivException("Cannot build here, this wall segment overlaps with a structure block belonging to a " + sb.getOwner().getName() + " structure.");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void getVerticalWallSegment(Player player, Location loc, Map<String, SimpleBlock> simpleBlocks) throws CivException
/*     */   {
/* 370 */     Location tmp = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
/* 371 */     for (int i = 0; i < HEIGHT; i++) { SimpleBlock sb;
/*     */       SimpleBlock sb;
/* 373 */       if (i == 0) {
/* 374 */         sb = new SimpleBlock(98, 1);
/*     */       } else {
/* 376 */         sb = new SimpleBlock(98, 0);
/*     */       }
/* 378 */       sb.worldname = tmp.getWorld().getName();
/* 379 */       sb.x = tmp.getBlockX();
/* 380 */       sb.y = tmp.getBlockY();
/* 381 */       sb.z = tmp.getBlockZ();
/*     */       
/* 383 */       validateBlockLocation(player, tmp);
/* 384 */       simpleBlocks.put(sb.worldname + "," + sb.x + "," + sb.y + "," + sb.z, sb);
/*     */       
/* 386 */       tmp.add(0.0D, 1.0D, 0.0D);
/*     */     }
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
/*     */   private int buildWallSegment(Player player, BlockCoord first, BlockCoord second, int blockCount, HashMap<String, SimpleBlock> simpleBlocks, int verticalSegments)
/*     */     throws CivException
/*     */   {
/* 403 */     Location locFirst = first.getLocation();
/* 404 */     Location locSecond = second.getLocation();
/*     */     
/* 406 */     Vector dir = new Vector(locFirst.getX() - locSecond.getX(), 
/* 407 */       locFirst.getY() - locSecond.getY(), 
/* 408 */       locFirst.getZ() - locSecond.getZ());
/* 409 */     dir.normalize();
/* 410 */     dir.multiply(0.5D);
/* 411 */     HashMap<String, SimpleBlock> thisWallBlocks = new HashMap();
/*     */     
/* 413 */     getTown().lastBuildableBuilt = null;
/*     */     
/* 415 */     getVerticalWallSegment(player, locSecond, thisWallBlocks);
/* 416 */     simpleBlocks.putAll(thisWallBlocks);
/* 417 */     verticalSegments++;
/*     */     
/* 419 */     double distance = locSecond.distance(locFirst);
/* 420 */     BlockCoord lastBlockCoord = new BlockCoord(locSecond);
/* 421 */     BlockCoord currentBlockCoord = new BlockCoord(locSecond);
/* 422 */     double tmpDist; while (locSecond.distance(locFirst) > 1.0D) {
/* 423 */       locSecond.add(dir);
/* 424 */       ChunkCoord coord = new ChunkCoord(locSecond);
/* 425 */       CivGlobal.addWallChunk(this, coord);
/*     */       
/* 427 */       currentBlockCoord.setFromLocation(locSecond);
/* 428 */       if (!lastBlockCoord.equals(currentBlockCoord))
/*     */       {
/*     */ 
/* 431 */         lastBlockCoord.setFromLocation(locSecond);
/*     */         
/*     */ 
/* 434 */         blockCount++;
/* 435 */         if (blockCount > 350) {
/* 436 */           throw new CivException("ERROR: Building wall blocks exceeded recusion limit! Halted to keep server alive.");
/*     */         }
/*     */         
/* 439 */         getVerticalWallSegment(player, locSecond, thisWallBlocks);
/* 440 */         simpleBlocks.putAll(thisWallBlocks);
/* 441 */         verticalSegments++;
/*     */         
/*     */ 
/*     */ 
/* 445 */         tmpDist = locSecond.distance(locFirst);
/* 446 */         if (tmpDist > distance) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 452 */     if (!this.wallBlocks.containsKey(new BlockCoord(locFirst))) {
/*     */       try {
/* 454 */         getVerticalWallSegment(player, locFirst, thisWallBlocks);
/* 455 */         simpleBlocks.putAll(thisWallBlocks);
/* 456 */         verticalSegments++;
/*     */       } catch (CivException e) {
/* 458 */         CivLog.warning("Couldn't build the last wall segment, oh well.");
/*     */       }
/*     */     }
/*     */     
/* 462 */     for (SimpleBlock sb : simpleBlocks.values()) {
/* 463 */       BlockCoord bcoord = new BlockCoord(sb);
/* 464 */       int old_id = ItemManager.getId(bcoord.getBlock());
/* 465 */       int old_data = ItemManager.getData(bcoord.getBlock());
/* 466 */       if (!this.wallBlocks.containsKey(bcoord)) {
/*     */         try {
/* 468 */           WallBlock wb = new WallBlock(bcoord, this, old_id, old_data, sb.getType(), sb.getData());
/*     */           
/* 470 */           this.wallBlocks.put(bcoord, wb);
/* 471 */           addStructureBlock(bcoord, true);
/* 472 */           wb.save();
/*     */         } catch (SQLException e) {
/* 474 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/* 478 */     return verticalSegments;
/*     */   }
/*     */   
/*     */   public boolean isProtectedLocation(Location location)
/*     */   {
/* 483 */     if (!isActive()) {
/* 484 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 491 */     for (BlockCoord coord : this.wallBlocks.keySet()) {
/* 492 */       Location blockLocation = coord.getLocation();
/*     */       
/* 494 */       if ((location.getBlockX() == blockLocation.getBlockX()) && 
/* 495 */         (location.getBlockZ() == blockLocation.getBlockZ()))
/*     */       {
/*     */ 
/* 498 */         if (location.getBlockY() < MAX_HEIGHT) {
/* 499 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 504 */     return false;
/*     */   }
/*     */   
/*     */   public void repairStructure() throws CivException
/*     */   {
/* 509 */     double cost = getRepairCost();
/*     */     
/* 511 */     if (!isValidWall()) {
/* 512 */       throw new CivException("This wall is no longer valid and cannot be repaired. Walls can no longer overlap with protected structure blocks, demolish this wall instead.");
/*     */     }
/*     */     
/* 515 */     if (!getTown().getTreasury().hasEnough(cost)) {
/* 516 */       throw new CivException("Your town cannot not afford the " + cost + " coins to build a " + getDisplayName());
/*     */     }
/*     */     
/* 519 */     setHitpoints(getMaxHitPoints());
/* 520 */     bindStructureBlocks();
/*     */     
/* 522 */     for (WallBlock wb : this.wallBlocks.values()) {
/* 523 */       BlockCoord bcoord = wb.getCoord();
/* 524 */       ItemManager.setTypeId(bcoord.getBlock(), wb.getTypeId());
/* 525 */       ItemManager.setData(bcoord.getBlock(), wb.getData());
/*     */     }
/*     */     
/* 528 */     save();
/* 529 */     getTown().getTreasury().withdraw(cost);
/* 530 */     CivMessage.sendTown(getTown(), "§eThe town has repaired a " + getDisplayName() + " at " + getCorner());
/*     */   }
/*     */   
/*     */   public int getMaxHitPoints()
/*     */   {
/* 535 */     double rate = 1.0D;
/* 536 */     if (getTown().getBuffManager().hasBuff("buff_chichen_itza_tower_hp")) {
/* 537 */       rate += getTown().getBuffManager().getEffectiveDouble("buff_chichen_itza_tower_hp");
/* 538 */       rate += getTown().getBuffManager().getEffectiveDouble("buff_barricade");
/*     */     }
/* 540 */     return (int)(this.info.max_hitpoints * rate);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\Wall.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
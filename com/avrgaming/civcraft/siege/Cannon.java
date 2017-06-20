/*     */ package com.avrgaming.civcraft.siege;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.DiplomacyManager;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.template.Template;
/*     */ import com.avrgaming.civcraft.template.Template.TemplateType;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.threading.tasks.FireWorkTask;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.CivColor;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock.Type;
/*     */ import com.avrgaming.civcraft.util.TimeTools;
/*     */ import com.avrgaming.civcraft.war.WarRegen;
/*     */ import java.io.IOException;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.FireworkEffect;
/*     */ import org.bukkit.FireworkEffect.Builder;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.Sign;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.block.Action;
/*     */ import org.bukkit.event.block.BlockBreakEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ public class Cannon extends Buildable
/*     */ {
/*  49 */   public static HashMap<BlockCoord, Cannon> fireSignLocations = new HashMap();
/*  50 */   public static HashMap<BlockCoord, Cannon> angleSignLocations = new HashMap();
/*  51 */   public static HashMap<BlockCoord, Cannon> powerSignLocations = new HashMap();
/*  52 */   public static HashMap<BlockCoord, Cannon> cannonBlocks = new HashMap();
/*     */   
/*     */   private BlockCoord fireSignLocation;
/*     */   private BlockCoord angleSignLocation;
/*     */   private BlockCoord powerSignLocation;
/*     */   private Location cannonLocation;
/*  58 */   private Vector direction = new Vector(0, 0, 0);
/*     */   
/*     */   public static final String RESTORE_NAME = "special:Cannons";
/*     */   
/*     */   public static final double STEP = 1.0D;
/*     */   
/*     */   public static final byte WALLSIGN_EAST = 5;
/*     */   public static final byte WALLSIGN_WEST = 4;
/*     */   public static final byte WALLSIGN_NORTH = 2;
/*     */   public static final byte WALLSIGN_SOUTH = 3;
/*     */   public int signDirection;
/*     */   public static final double minAngle = -35.0D;
/*     */   public static final double maxAngle = 35.0D;
/*  71 */   private double angle = 0.0D;
/*     */   
/*     */   public static final double minPower = 0.0D;
/*     */   public static final double maxPower = 50.0D;
/*  75 */   private double power = 0.0D;
/*     */   
/*  77 */   private int tntLoaded = 0;
/*  78 */   private int shotCooldown = 0;
/*  79 */   private int hitpoints = 0;
/*     */   
/*     */   private Resident owner;
/*  82 */   private HashSet<BlockCoord> blocks = new HashSet();
/*     */   
/*     */   public static int tntCost;
/*     */   
/*     */   public static int maxCooldown;
/*     */   public static int maxHitpoints;
/*     */   public static int baseStructureDamage;
/*  89 */   private boolean angleFlip = false;
/*     */   
/*     */   static {
/*     */     try {
/*  93 */       tntCost = CivSettings.getInteger(CivSettings.warConfig, "cannon.tnt_cost").intValue();
/*  94 */       maxCooldown = CivSettings.getInteger(CivSettings.warConfig, "cannon.cooldown").intValue();
/*  95 */       maxHitpoints = CivSettings.getInteger(CivSettings.warConfig, "cannon.hitpoints").intValue();
/*  96 */       baseStructureDamage = CivSettings.getInteger(CivSettings.warConfig, "cannon.structure_damage").intValue();
/*     */     } catch (InvalidConfiguration e) {
/*  98 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void newCannon(Resident resident) throws CivException
/*     */   {
/* 104 */     Player player = CivGlobal.getPlayer(resident);
/*     */     
/* 106 */     Cannon cannon = new Cannon();
/* 107 */     cannon.buildCannon(player, player.getLocation());
/*     */   }
/*     */   
/*     */   public static void cleanupAll()
/*     */   {
/* 112 */     cannonBlocks.clear();
/* 113 */     powerSignLocations.clear();
/* 114 */     angleSignLocations.clear();
/* 115 */     fireSignLocations.clear();
/*     */   }
/*     */   
/*     */   private static void removeAllValues(Cannon cannon, HashMap<BlockCoord, Cannon> map) {
/* 119 */     LinkedList<BlockCoord> removeUs = new LinkedList();
/* 120 */     for (BlockCoord bcoord : map.keySet()) {
/* 121 */       Cannon c = (Cannon)map.get(bcoord);
/* 122 */       if (c == cannon) {
/* 123 */         removeUs.add(bcoord);
/*     */       }
/*     */     }
/*     */     
/* 127 */     for (BlockCoord bcoord : removeUs) {
/* 128 */       map.remove(bcoord);
/*     */     }
/*     */   }
/*     */   
/*     */   public void cleanup() {
/* 133 */     removeAllValues(this, cannonBlocks);
/* 134 */     removeAllValues(this, powerSignLocations);
/* 135 */     removeAllValues(this, angleSignLocations);
/* 136 */     removeAllValues(this, fireSignLocations);
/*     */   }
/*     */   
/*     */   public void buildCannon(Player player, Location center) throws CivException
/*     */   {
/*     */     try {
/* 142 */       templateFile = CivSettings.getString(CivSettings.warConfig, "cannon.template");
/*     */     } catch (InvalidConfiguration e) { String templateFile;
/* 144 */       e.printStackTrace();
/* 145 */       return;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*     */       String templateFile;
/* 151 */       String templatePath = Template.getTemplateFilePath(templateFile, Template.getDirection(center), Template.TemplateType.STRUCTURE, "default");
/* 152 */       setTemplateName(templatePath);
/* 153 */       tpl = Template.getTemplate(templatePath, center);
/*     */     } catch (IOException e) { Template tpl;
/* 155 */       e.printStackTrace();
/* 156 */       throw new CivException("Internal Error.");
/*     */     } catch (CivException e) {
/* 158 */       e.printStackTrace();
/* 159 */       throw new CivException("Internal Error.");
/*     */     }
/*     */     Template tpl;
/* 162 */     this.corner = new BlockCoord(center);
/* 163 */     this.corner.setFromLocation(repositionCenter(center, tpl.dir(), tpl.size_x, tpl.size_z));
/* 164 */     checkBlockPermissionsAndRestrictions(player, this.corner.getBlock(), tpl.size_x, tpl.size_y, tpl.size_z);
/* 165 */     buildCannonFromTemplate(tpl, this.corner);
/* 166 */     processCommandSigns(tpl, this.corner);
/* 167 */     this.hitpoints = maxHitpoints;
/* 168 */     this.owner = CivGlobal.getResident(player);
/*     */     try
/*     */     {
/* 171 */       saveNow();
/*     */     } catch (SQLException e) {
/* 173 */       e.printStackTrace();
/* 174 */       throw new CivException("Internal SQL Error.");
/*     */     }
/*     */   }
/*     */   
/*     */   protected void checkBlockPermissionsAndRestrictions(Player player, Block centerBlock, int regionX, int regionY, int regionZ)
/*     */     throws CivException
/*     */   {
/* 181 */     if (!com.avrgaming.civcraft.war.War.isWarTime()) {
/* 182 */       throw new CivException("Can only build Cannons during war time.");
/*     */     }
/*     */     
/* 185 */     if (player.getLocation().getY() >= 200.0D) {
/* 186 */       throw new CivException("You're too high to build cannons.");
/*     */     }
/*     */     
/* 189 */     if (regionY + centerBlock.getLocation().getBlockY() >= 255) {
/* 190 */       throw new CivException("Cannot build cannon here, would go over the minecraft height limit.");
/*     */     }
/*     */     
/* 193 */     if (!player.isOp()) {
/* 194 */       Buildable.validateDistanceFromSpawn(centerBlock.getLocation());
/*     */     }
/*     */     
/* 197 */     int yTotal = 0;
/* 198 */     int yCount = 0;
/*     */     
/* 200 */     for (int x = 0; x < regionX; x++) {
/* 201 */       for (int y = 0; y < regionY; y++) {
/* 202 */         for (int z = 0; z < regionZ; z++) {
/* 203 */           Block b = centerBlock.getRelative(x, y, z);
/*     */           
/* 205 */           if (ItemManager.getId(b) == 54) {
/* 206 */             throw new CivException("Cannot build here, would destroy chest.");
/*     */           }
/*     */           
/* 209 */           BlockCoord coord = new BlockCoord(b);
/*     */           
/* 211 */           if (CivGlobal.getProtectedBlock(coord) != null) {
/* 212 */             throw new CivException("Cannot build here, protected blocks in the way.");
/*     */           }
/*     */           
/* 215 */           if (CivGlobal.getStructureBlock(coord) != null) {
/* 216 */             throw new CivException("Cannot build here, structure blocks in the way.");
/*     */           }
/*     */           
/* 219 */           if (CivGlobal.getCampBlock(coord) != null) {
/* 220 */             throw new CivException("Cannot build here, a camp is in the way.");
/*     */           }
/*     */           
/* 223 */           if (cannonBlocks.containsKey(coord)) {
/* 224 */             throw new CivException("Cannot build here, another cannon in the way.");
/*     */           }
/*     */           
/* 227 */           yTotal += b.getWorld().getHighestBlockYAt(centerBlock.getX() + x, centerBlock.getZ() + z);
/* 228 */           yCount++;
/*     */           
/* 230 */           if (CivGlobal.getRoadBlock(coord) != null) {
/* 231 */             throw new CivException("Cannot build a cannon on top of an existing road block.");
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 237 */     double highestAverageBlock = yTotal / yCount;
/*     */     
/* 239 */     if ((centerBlock.getY() > highestAverageBlock + 10.0D) || 
/* 240 */       (centerBlock.getY() < highestAverageBlock - 10.0D)) {
/* 241 */       throw new CivException("Cannot build here, you must be closer to the surface.");
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateAngleSign(Block block) {
/* 246 */     Sign sign = (Sign)block.getState();
/* 247 */     sign.setLine(0, "YAW");
/* 248 */     sign.setLine(1, this.angle);
/*     */     
/* 250 */     double a = this.angle;
/*     */     
/* 252 */     if (a > 0.0D) {
/* 253 */       sign.setLine(2, "-->");
/* 254 */     } else if (a < 0.0D) {
/* 255 */       sign.setLine(2, "<--");
/*     */     } else {
/* 257 */       sign.setLine(2, "");
/*     */     }
/*     */     
/* 260 */     sign.setLine(3, "");
/* 261 */     sign.update();
/*     */   }
/*     */   
/*     */   private void updatePowerSign(Block block) {
/* 265 */     Sign sign = (Sign)block.getState();
/* 266 */     sign.setLine(0, "PITCH");
/* 267 */     sign.setLine(1, this.power);
/* 268 */     sign.setLine(2, "");
/* 269 */     sign.setLine(3, "");
/* 270 */     sign.update();
/*     */   }
/*     */   
/*     */   private void updateFireSign(Block block) {
/* 274 */     Sign sign = (Sign)block.getState();
/* 275 */     sign.setLine(0, "FIRE");
/* 276 */     boolean loaded = false;
/*     */     
/* 278 */     if (this.tntLoaded >= tntCost) {
/* 279 */       sign.setLine(1, "§a" + CivColor.BOLD + "LOADED");
/* 280 */       loaded = true;
/*     */     } else {
/* 282 */       sign.setLine(1, "§e(" + this.tntLoaded + "/" + tntCost + ") TNT");
/*     */     }
/*     */     
/* 285 */     if (this.shotCooldown > 0) {
/* 286 */       sign.setLine(2, "§7Wait " + this.shotCooldown);
/*     */     }
/* 288 */     else if (loaded) {
/* 289 */       sign.setLine(2, "§7READY");
/*     */     } else {
/* 291 */       sign.setLine(2, "§7Add TNT");
/*     */     }
/*     */     
/*     */ 
/* 295 */     sign.setLine(3, "");
/* 296 */     sign.update();
/*     */   }
/*     */   
/*     */   private void processCommandSigns(Template tpl, BlockCoord corner) {
/* 300 */     for (BlockCoord relativeCoord : tpl.commandBlockRelativeLocations) {
/* 301 */       SimpleBlock sb = tpl.blocks[relativeCoord.getX()][relativeCoord.getY()][relativeCoord.getZ()];
/* 302 */       BlockCoord absCoord = new BlockCoord(corner.getBlock().getRelative(relativeCoord.getX(), relativeCoord.getY(), relativeCoord.getZ()));
/*     */       
/*     */       String str;
/* 305 */       switch ((str = sb.command).hashCode()) {case 46548709:  if (str.equals("/fire")) break; break; case 1438531076:  if (str.equals("/angle")) {} break; case 1452428854:  if (str.equals("/power")) {} break; case 1690253804:  if (!str.equals("/cannon"))
/*     */         {
/* 307 */           continue;BlockCoord coord = new BlockCoord(absCoord);
/* 308 */           setFireSignLocation(coord);
/*     */           
/* 310 */           ItemManager.setTypeIdAndData(coord.getBlock(), sb.getType(), sb.getData(), false);
/* 311 */           updateFireSign(coord.getBlock());
/*     */           
/*     */ 
/* 314 */           fireSignLocations.put(coord, this);
/* 315 */           continue;
/*     */           
/* 317 */           BlockCoord coord = new BlockCoord(absCoord);
/* 318 */           setAngleSignLocation(coord);
/*     */           
/* 320 */           ItemManager.setTypeIdAndData(coord.getBlock(), sb.getType(), sb.getData(), false);
/* 321 */           updateAngleSign(coord.getBlock());
/*     */           
/* 323 */           angleSignLocations.put(coord, this);
/* 324 */           continue;
/*     */           
/* 326 */           BlockCoord coord = new BlockCoord(absCoord);
/* 327 */           setPowerSignLocation(coord);
/*     */           
/* 329 */           ItemManager.setTypeIdAndData(coord.getBlock(), sb.getType(), sb.getData(), false);
/* 330 */           updatePowerSign(coord.getBlock());
/*     */           
/* 332 */           powerSignLocations.put(coord, this);
/*     */         }
/*     */         else {
/* 335 */           BlockCoord coord = new BlockCoord(absCoord);
/* 336 */           this.cannonLocation = coord.getLocation();
/*     */           
/* 338 */           switch (sb.getData()) {
/*     */           case 5: 
/* 340 */             this.cannonLocation.add(1.0D, 0.0D, 0.0D);
/* 341 */             this.direction.setX(1.0F);
/* 342 */             this.direction.setY(0.0F);
/* 343 */             this.direction.setZ(0.0F);
/* 344 */             break;
/*     */           case 4: 
/* 346 */             this.cannonLocation.add(-1.0D, 0.0D, 0.0D);
/* 347 */             this.angleFlip = true;
/* 348 */             this.direction.setX(-1.0F);
/* 349 */             this.direction.setY(0.0F);
/* 350 */             this.direction.setZ(0.0F);
/* 351 */             break;
/*     */           case 2: 
/* 353 */             this.cannonLocation.add(0.0D, 0.0D, -1.0D);
/* 354 */             this.direction.setX(0.0F);
/* 355 */             this.direction.setY(0.0F);
/* 356 */             this.direction.setZ(-1.0F);
/* 357 */             break;
/*     */           case 3: 
/* 359 */             this.cannonLocation.add(0.0D, 0.0D, 1.0D);
/* 360 */             this.angleFlip = true;
/* 361 */             this.direction.setX(0.0F);
/* 362 */             this.direction.setY(0.0F);
/* 363 */             this.direction.setZ(1.0F);
/* 364 */             break;
/*     */           default: 
/* 366 */             CivLog.error("INVALID SIGN DIRECTION..");
/*     */           }
/*     */           
/* 369 */           this.signDirection = sb.getData();
/*     */         }
/*     */         
/*     */         break;
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void processUndo()
/*     */     throws CivException
/*     */   {}
/*     */   
/*     */ 
/*     */   public void updateBuildProgess() {}
/*     */   
/*     */   public void build(Player player, Location centerLoc, Template tpl)
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */   protected void runOnBuild(Location centerLoc, Template tpl)
/*     */     throws CivException
/*     */   {}
/*     */   
/*     */   public String getDynmapDescription()
/*     */   {
/* 396 */     return null;
/*     */   }
/*     */   
/*     */   public String getMarkerIconName()
/*     */   {
/* 401 */     return null;
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
/*     */   public void load(ResultSet rs)
/*     */     throws SQLException, InvalidNameException, com.avrgaming.civcraft.exception.InvalidObjectException, CivException
/*     */   {}
/*     */   
/*     */ 
/*     */   public void save() {}
/*     */   
/*     */ 
/*     */   public void saveNow()
/*     */     throws SQLException
/*     */   {}
/*     */   
/*     */   private void buildCannonFromTemplate(Template tpl, BlockCoord corner)
/*     */   {
/* 430 */     Block cornerBlock = corner.getBlock();
/* 431 */     for (int x = 0; x < tpl.size_x; x++) {
/* 432 */       for (int y = 0; y < tpl.size_y; y++) {
/* 433 */         for (int z = 0; z < tpl.size_z; z++) {
/* 434 */           Block nextBlock = cornerBlock.getRelative(x, y, z);
/*     */           
/* 436 */           if (tpl.blocks[x][y][z].specialType != SimpleBlock.Type.COMMAND)
/*     */           {
/*     */ 
/*     */ 
/* 440 */             if (tpl.blocks[x][y][z].specialType == SimpleBlock.Type.LITERAL)
/*     */             {
/* 442 */               tpl.blocks[x][y][z].command = "/literal";
/* 443 */               tpl.commandBlockRelativeLocations.add(new BlockCoord(cornerBlock.getWorld().getName(), x, y, z));
/*     */             }
/*     */             else
/*     */             {
/*     */               try {
/* 448 */                 if (ItemManager.getId(nextBlock) != tpl.blocks[x][y][z].getType())
/*     */                 {
/* 450 */                   WarRegen.saveBlock(nextBlock, "special:Cannons", false);
/* 451 */                   ItemManager.setTypeId(nextBlock, tpl.blocks[x][y][z].getType());
/* 452 */                   ItemManager.setData(nextBlock, tpl.blocks[x][y][z].getData());
/*     */                 }
/*     */                 
/* 455 */                 if (ItemManager.getId(nextBlock) != 0) {
/* 456 */                   BlockCoord b = new BlockCoord(nextBlock.getLocation());
/* 457 */                   cannonBlocks.put(b, this);
/* 458 */                   this.blocks.add(b);
/*     */                 }
/*     */               } catch (Exception e) {
/* 461 */                 CivLog.error(e.getMessage());
/*     */               }
/*     */             } }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected Location repositionCenter(Location center, String dir, double x_size, double z_size) throws CivException {
/* 470 */     Location loc = center.clone();
/*     */     
/* 472 */     if (dir.equalsIgnoreCase("east")) {
/* 473 */       loc.setZ(loc.getZ() - z_size / 2.0D);
/* 474 */       loc.setX(loc.getX() + 0.0D);
/*     */     }
/* 476 */     else if (dir.equalsIgnoreCase("west")) {
/* 477 */       loc.setZ(loc.getZ() - z_size / 2.0D);
/* 478 */       loc.setX(loc.getX() - (0.0D + x_size));
/*     */ 
/*     */     }
/* 481 */     else if (dir.equalsIgnoreCase("north")) {
/* 482 */       loc.setX(loc.getX() - x_size / 2.0D);
/* 483 */       loc.setZ(loc.getZ() - (0.0D + z_size));
/*     */     }
/* 485 */     else if (dir.equalsIgnoreCase("south")) {
/* 486 */       loc.setX(loc.getX() - x_size / 2.0D);
/* 487 */       loc.setZ(loc.getZ() + 0.0D);
/*     */     }
/*     */     
/*     */ 
/* 491 */     return loc;
/*     */   }
/*     */   
/*     */   public BlockCoord getFireSignLocation() {
/* 495 */     return this.fireSignLocation;
/*     */   }
/*     */   
/*     */   public void setFireSignLocation(BlockCoord fireSignLocation) {
/* 499 */     this.fireSignLocation = fireSignLocation;
/*     */   }
/*     */   
/*     */   public BlockCoord getAngleSignLocation() {
/* 503 */     return this.angleSignLocation;
/*     */   }
/*     */   
/*     */   public void setAngleSignLocation(BlockCoord angleSignLocation) {
/* 507 */     this.angleSignLocation = angleSignLocation;
/*     */   }
/*     */   
/*     */   public BlockCoord getPowerSignLocation() {
/* 511 */     return this.powerSignLocation;
/*     */   }
/*     */   
/*     */   public void setPowerSignLocation(BlockCoord powerSignLocation) {
/* 515 */     this.powerSignLocation = powerSignLocation;
/*     */   }
/*     */   
/*     */   private void validateUse(Player player) throws CivException {
/* 519 */     if (this.hitpoints == 0) {
/* 520 */       throw new CivException("Cannon destroyed.");
/*     */     }
/*     */     
/* 523 */     Resident resident = CivGlobal.getResident(player);
/*     */     
/* 525 */     if (resident.getCiv() != this.owner.getCiv()) {
/* 526 */       throw new CivException("Only members of the owner's civilization can use a cannon.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void processFire(PlayerInteractEvent event) throws CivException
/*     */   {
/* 532 */     validateUse(event.getPlayer());
/*     */     
/* 534 */     if (this.shotCooldown > 0) {
/* 535 */       CivMessage.sendError(event.getPlayer(), "Wait for the cooldown.");
/* 536 */       return;
/*     */     }
/*     */     
/* 539 */     if (this.tntLoaded < tntCost) {
/* 540 */       if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
/* 541 */         ItemStack stack = event.getPlayer().getItemInHand();
/* 542 */         if ((stack != null) && 
/* 543 */           (ItemManager.getId(stack) == 46) && 
/* 544 */           (ItemManager.removeItemFromPlayer(event.getPlayer(), Material.TNT, 1))) {
/* 545 */           this.tntLoaded += 1;
/* 546 */           CivMessage.sendSuccess(event.getPlayer(), "Added TNT to cannon.");
/* 547 */           updateFireSign(this.fireSignLocation.getBlock());
/*     */           
/* 549 */           return;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 554 */         CivMessage.sendError(event.getPlayer(), "Cannon requires TNT to function. Please insert TNT.");
/* 555 */         return;
/*     */       }
/* 557 */       event.setCancelled(true);
/* 558 */       event.getPlayer().updateInventory();
/* 559 */       return;
/*     */     }
/*     */     
/* 562 */     CivMessage.send(event.getPlayer(), "Fire!");
/* 563 */     this.cannonLocation.setDirection(this.direction);
/* 564 */     Resident resident = CivGlobal.getResident(event.getPlayer());
/* 565 */     CannonProjectile proj = new CannonProjectile(this, this.cannonLocation.clone(), resident);
/* 566 */     proj.fire();
/* 567 */     this.tntLoaded = 0;
/* 568 */     this.shotCooldown = maxCooldown;
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
/* 586 */     TaskMaster.syncTask(new Runnable()
/*     */     {
/*     */       Cannon cannon;
/*     */       
/*     */       public void run()
/*     */       {
/* 579 */         if (this.cannon.decrementCooldown()) {
/* 580 */           return;
/*     */         }
/*     */         
/* 583 */         TaskMaster.syncTask(new 1SyncTask(Cannon.this, this.cannon), TimeTools.toTicks(1L));
/*     */       }
/*     */       
/* 586 */     }, TimeTools.toTicks(1L));
/*     */     
/*     */ 
/* 589 */     event.getPlayer().updateInventory();
/* 590 */     updateFireSign(this.fireSignLocation.getBlock());
/*     */   }
/*     */   
/*     */   public boolean decrementCooldown()
/*     */   {
/* 595 */     this.shotCooldown -= 1;
/* 596 */     updateFireSign(this.fireSignLocation.getBlock());
/*     */     
/* 598 */     if (this.shotCooldown <= 0) {
/* 599 */       return true;
/*     */     }
/*     */     
/* 602 */     return false;
/*     */   }
/*     */   
/*     */   public void processAngle(PlayerInteractEvent event) throws CivException
/*     */   {
/* 607 */     validateUse(event.getPlayer());
/*     */     
/* 609 */     if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
/* 610 */       this.angle -= 1.0D;
/* 611 */       if (this.angle < -35.0D) {
/* 612 */         this.angle = -35.0D;
/*     */       }
/* 614 */     } else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
/* 615 */       this.angle += 1.0D;
/* 616 */       if (this.angle > 35.0D) {
/* 617 */         this.angle = 35.0D;
/*     */       }
/*     */     }
/*     */     
/* 621 */     double a = this.angle;
/* 622 */     if (this.angleFlip) {
/* 623 */       a *= -1.0D;
/*     */     }
/*     */     
/* 626 */     if ((this.signDirection == 5) || (this.signDirection == 4)) {
/* 627 */       this.direction.setZ(a / 100.0D);
/*     */     }
/*     */     else {
/* 630 */       this.direction.setX(a / 100.0D);
/*     */     }
/*     */     
/* 633 */     event.getPlayer().updateInventory();
/* 634 */     updateAngleSign(this.angleSignLocation.getBlock());
/*     */   }
/*     */   
/*     */   public void processPower(PlayerInteractEvent event) throws CivException
/*     */   {
/* 639 */     validateUse(event.getPlayer());
/*     */     
/* 641 */     if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
/* 642 */       this.power -= 1.0D;
/* 643 */       if (this.power < 0.0D) {
/* 644 */         this.power = 0.0D;
/*     */       }
/* 646 */     } else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
/* 647 */       this.power += 1.0D;
/* 648 */       if (this.power > 50.0D) {
/* 649 */         this.power = 50.0D;
/*     */       }
/*     */     }
/*     */     
/* 653 */     this.direction.setY(this.power / 100.0D);
/* 654 */     event.getPlayer().updateInventory();
/* 655 */     updatePowerSign(this.powerSignLocation.getBlock());
/*     */   }
/*     */   
/*     */   public void onHit(BlockBreakEvent event) {
/* 659 */     Resident resident = CivGlobal.getResident(event.getPlayer());
/*     */     
/* 661 */     if (!resident.hasTown()) {
/* 662 */       CivMessage.sendError(resident, "Can't destroy cannon's if you're not part of a civilization at war.");
/* 663 */       return;
/*     */     }
/*     */     
/* 666 */     if (resident.getCiv() == this.owner.getCiv()) {
/* 667 */       CivMessage.sendError(resident, "Can't destroy your own civ's cannons during war.");
/* 668 */       return;
/*     */     }
/*     */     
/* 671 */     if (!resident.getCiv().getDiplomacyManager().atWarWith(this.owner.getCiv())) {
/* 672 */       CivMessage.sendError(resident, "You've got to be at war with this cannon's owner civ(" + this.owner.getCiv().getName() + ") to destroy it.");
/* 673 */       return;
/*     */     }
/*     */     
/* 676 */     if (this.hitpoints == 0) {
/* 677 */       CivMessage.sendError(resident, "Cannon already destroyed.");
/* 678 */       return;
/*     */     }
/*     */     
/* 681 */     this.hitpoints -= 1;
/*     */     
/* 683 */     if (this.hitpoints <= 0) {
/* 684 */       destroy();
/* 685 */       CivMessage.send(event.getPlayer(), "§a" + CivColor.BOLD + "Cannon Destroyed!");
/* 686 */       CivMessage.sendCiv(this.owner.getCiv(), "§eOur Cannon at " + 
/* 687 */         this.cannonLocation.getBlockX() + "," + this.cannonLocation.getBlockY() + "," + this.cannonLocation.getBlockZ() + 
/* 688 */         " has been destroyed!");
/* 689 */       return;
/*     */     }
/*     */     
/* 692 */     CivMessage.send(event.getPlayer(), "§eHit Cannon! (" + this.hitpoints + "/" + maxHitpoints + ")");
/* 693 */     CivMessage.sendCiv(this.owner.getCiv(), "§7Our Cannon at " + 
/* 694 */       this.cannonLocation.getBlockX() + "," + this.cannonLocation.getBlockY() + "," + this.cannonLocation.getBlockZ() + 
/* 695 */       " has been hit! (" + this.hitpoints + "/" + maxHitpoints + ")");
/*     */   }
/*     */   
/*     */   private void launchExplodeFirework(Location loc) {
/* 699 */     FireworkEffect fe = FireworkEffect.builder().withColor(Color.RED).withColor(Color.ORANGE).flicker(false).with(org.bukkit.FireworkEffect.Type.BALL).build();
/* 700 */     TaskMaster.syncTask(new FireWorkTask(fe, loc.getWorld(), loc, 3), 0L);
/*     */   }
/*     */   
/*     */   private void destroy() {
/* 704 */     for (BlockCoord b : this.blocks) {
/* 705 */       launchExplodeFirework(b.getCenteredLocation());
/* 706 */       if (b.getBlock().getType().equals(Material.COAL_BLOCK)) {
/* 707 */         ItemManager.setTypeIdAndData(b.getBlock(), 13, 0, false);
/*     */       } else {
/* 709 */         ItemManager.setTypeIdAndData(b.getBlock(), 0, 0, false);
/*     */       }
/*     */     }
/*     */     
/* 713 */     ItemManager.setTypeIdAndData(this.fireSignLocation.getBlock(), 0, 0, false);
/* 714 */     ItemManager.setTypeIdAndData(this.angleSignLocation.getBlock(), 0, 0, false);
/* 715 */     ItemManager.setTypeIdAndData(this.powerSignLocation.getBlock(), 0, 0, false);
/*     */     
/* 717 */     this.blocks.clear();
/* 718 */     cleanup();
/*     */   }
/*     */   
/*     */   public int getTntLoaded() {
/* 722 */     return this.tntLoaded;
/*     */   }
/*     */   
/*     */   public void setTntLoaded(int tntLoaded) {
/* 726 */     this.tntLoaded = tntLoaded;
/*     */   }
/*     */   
/*     */   public int getCooldown() {
/* 730 */     return this.shotCooldown;
/*     */   }
/*     */   
/*     */   public void setCooldown(int cooldown) {
/* 734 */     this.shotCooldown = cooldown;
/*     */   }
/*     */   
/*     */   public int getDamage() {
/* 738 */     return baseStructureDamage;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\siege\Cannon.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
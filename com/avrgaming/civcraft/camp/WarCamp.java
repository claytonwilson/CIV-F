/*     */ package com.avrgaming.civcraft.camp;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigBuildableInfo;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.BuildableDamageBlock;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.ControlPoint;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.StructureBlock;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.object.TownChunk;
/*     */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*     */ import com.avrgaming.civcraft.permission.PlotPermissions.Type;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.template.Template;
/*     */ import com.avrgaming.civcraft.template.Template.TemplateType;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import com.avrgaming.civcraft.util.FireworkEffectPlayer;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock.Type;
/*     */ import com.avrgaming.civcraft.war.WarRegen;
/*     */ import java.io.IOException;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.Effect;
/*     */ import org.bukkit.FireworkEffect;
/*     */ import org.bukkit.FireworkEffect.Builder;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.Sound;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ public class WarCamp extends Buildable implements com.avrgaming.civcraft.structure.RespawnLocationHolder
/*     */ {
/*     */   public static final String RESTORE_NAME = "special:WarCamps";
/*  59 */   private ArrayList<BlockCoord> respawnPoints = new ArrayList();
/*  60 */   protected HashMap<BlockCoord, ControlPoint> controlPoints = new HashMap();
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
/*     */   public static void newCamp(Resident resident, ConfigBuildableInfo info)
/*     */   {
/* 124 */     TaskMaster.syncTask(new Runnable()
/*     */     {
/*     */       ConfigBuildableInfo info;
/*     */       
/*     */       public void run()
/*     */       {
/*     */         try
/*     */         {
/*  77 */           player = CivGlobal.getPlayer(WarCamp.this);
/*     */         } catch (CivException e) { Player player;
/*  79 */           return;
/*     */         }
/*     */         try
/*     */         {
/*  83 */           if (!WarCamp.this.hasTown()) {
/*  84 */             throw new CivException("You must be part of a civilization to found a war camp.");
/*     */           }
/*     */           
/*  87 */           if ((!WarCamp.this.getCiv().getLeaderGroup().hasMember(WarCamp.this)) && 
/*  88 */             (!WarCamp.this.getCiv().getAdviserGroup().hasMember(WarCamp.this))) {
/*  89 */             throw new CivException("You must be a leader or adviser of the civilization to found a war camp.");
/*     */           }
/*     */           
/*     */           try
/*     */           {
/*  94 */             warCampMax = CivSettings.getInteger(CivSettings.warConfig, "warcamp.max").intValue();
/*     */           } catch (InvalidConfiguration e) { int warCampMax;
/*  96 */             e.printStackTrace(); return;
/*     */           }
/*     */           
/*     */           int warCampMax;
/* 100 */           if (WarCamp.this.getCiv().getWarCamps().size() >= warCampMax) {
/* 101 */             throw new CivException("You can only have " + warCampMax + " war camps.");
/*     */           }
/*     */           
/* 104 */           ItemStack stack = player.getItemInHand();
/* 105 */           LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/* 106 */           if ((craftMat == null) || (!craftMat.hasComponent("FoundWarCamp"))) {
/* 107 */             throw new CivException("You must be holding an item that can found a war camp.");
/*     */           }
/*     */           
/* 110 */           WarCamp camp = new WarCamp(WarCamp.this, player.getLocation(), this.info);
/* 111 */           camp.buildCamp(player, player.getLocation());
/* 112 */           WarCamp.this.getCiv().addWarCamp(camp);
/*     */           
/* 114 */           CivMessage.sendSuccess(player, "You have set up a war camp!");
/* 115 */           camp.setWarCampBuilt();
/* 116 */           ItemStack newStack = new ItemStack(Material.AIR);
/* 117 */           player.setItemInHand(newStack);
/*     */         } catch (CivException e) { Player player;
/* 119 */           CivMessage.sendError(player, e.getMessage());
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */   public String getSessionKey()
/*     */   {
/* 128 */     return getCiv().getName() + ":warcamp:built";
/*     */   }
/*     */   
/*     */   public void setWarCampBuilt() {
/* 132 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(getSessionKey());
/* 133 */     Date now = new Date();
/* 134 */     if (entries.size() == 0) {
/* 135 */       CivGlobal.getSessionDB().add(getSessionKey(), now.getTime(), getCiv().getId(), getTown().getId(), 0);
/*     */     } else {
/* 137 */       CivGlobal.getSessionDB().update(((SessionEntry)entries.get(0)).request_id, ((SessionEntry)entries.get(0)).key, now.getTime());
/*     */     }
/*     */   }
/*     */   
/*     */   public int isWarCampCooldownLeft() {
/* 142 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(getSessionKey());
/* 143 */     Date now = new Date();
/* 144 */     long minsLeft = 0L;
/* 145 */     if (entries.size() == 0) {
/* 146 */       return 0;
/*     */     }
/* 148 */     Date then = new Date(Long.valueOf(((SessionEntry)entries.get(0)).value).longValue());
/*     */     try
/*     */     {
/* 151 */       rebuild_timeout = CivSettings.getInteger(CivSettings.warConfig, "warcamp.rebuild_timeout").intValue();
/*     */     } catch (InvalidConfiguration e) { int rebuild_timeout;
/* 153 */       e.printStackTrace();
/* 154 */       return 0;
/*     */     }
/*     */     int rebuild_timeout;
/* 157 */     minsLeft = then.getTime() + rebuild_timeout * 60 * 1000 - now.getTime();
/* 158 */     minsLeft /= 1000L;
/* 159 */     minsLeft /= 60L;
/* 160 */     if (now.getTime() > then.getTime() + rebuild_timeout * 60 * 1000) {
/* 161 */       return 0;
/*     */     }
/* 163 */     return (int)minsLeft;
/*     */   }
/*     */   
/*     */   public WarCamp(Resident resident, Location loc, ConfigBuildableInfo info)
/*     */   {
/* 168 */     setCorner(new BlockCoord(loc));
/* 169 */     setTown(resident.getTown());
/* 170 */     this.info = info;
/*     */   }
/*     */   
/*     */   public void buildCamp(Player player, Location center) throws CivException
/*     */   {
/*     */     try
/*     */     {
/* 177 */       templateFile = CivSettings.getString(CivSettings.warConfig, "warcamp.template");
/*     */     } catch (InvalidConfiguration e) { String templateFile;
/* 179 */       e.printStackTrace(); return;
/*     */     }
/*     */     String templateFile;
/* 182 */     Resident resident = CivGlobal.getResident(player);
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 187 */       String templatePath = Template.getTemplateFilePath(templateFile, Template.getDirection(center), Template.TemplateType.STRUCTURE, "default");
/* 188 */       setTemplateName(templatePath);
/* 189 */       tpl = Template.getTemplate(templatePath, center);
/*     */     } catch (IOException e) { Template tpl;
/* 191 */       e.printStackTrace();
/* 192 */       throw new CivException("Internal Error.");
/*     */     } catch (CivException e) {
/* 194 */       e.printStackTrace();
/* 195 */       throw new CivException("Internal Error.");
/*     */     }
/*     */     
/*     */     Template tpl;
/* 199 */     this.corner.setFromLocation(repositionCenter(center, tpl.dir(), tpl.size_x, tpl.size_z));
/* 200 */     checkBlockPermissionsAndRestrictions(player, this.corner.getBlock(), tpl.size_x, tpl.size_y, tpl.size_z);
/* 201 */     buildWarCampFromTemplate(tpl, this.corner);
/* 202 */     processCommandSigns(tpl, this.corner);
/*     */     try {
/* 204 */       saveNow();
/*     */     } catch (SQLException e) {
/* 206 */       e.printStackTrace();
/* 207 */       throw new CivException("Internal SQL Error.");
/*     */     }
/*     */     
/* 210 */     resident.save();
/*     */   }
/*     */   
/*     */   private void processCommandSigns(Template tpl, BlockCoord corner) {
/* 214 */     for (BlockCoord relativeCoord : tpl.commandBlockRelativeLocations) {
/* 215 */       SimpleBlock sb = tpl.blocks[relativeCoord.getX()][relativeCoord.getY()][relativeCoord.getZ()];
/* 216 */       BlockCoord absCoord = new BlockCoord(corner.getBlock().getRelative(relativeCoord.getX(), relativeCoord.getY(), relativeCoord.getZ()));
/*     */       String str;
/* 218 */       switch ((str = sb.command).hashCode()) {case 1259250254:  if (str.equals("/control")) break;  case 1405094425:  if ((goto 232) && (str.equals("/respawn")))
/*     */         {
/* 220 */           this.respawnPoints.add(absCoord);
/* 221 */           BlockCoord coord = new BlockCoord(absCoord);
/* 222 */           ItemManager.setTypeId(coord.getBlock(), 0);
/* 223 */           addStructureBlock(new BlockCoord(absCoord), false);
/*     */           
/* 225 */           coord = new BlockCoord(absCoord);
/* 226 */           coord.setY(absCoord.getY() + 1);
/* 227 */           ItemManager.setTypeId(coord.getBlock(), 0);
/* 228 */           addStructureBlock(coord, false);
/*     */           
/* 230 */           continue;
/*     */           
/* 232 */           createControlPoint(absCoord);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void checkBlockPermissionsAndRestrictions(Player player, Block centerBlock, int regionX, int regionY, int regionZ) throws CivException {
/* 240 */     if (!com.avrgaming.civcraft.war.War.isWarTime()) {
/* 241 */       throw new CivException("Can only build War Camps during war time.");
/*     */     }
/*     */     
/* 244 */     if (player.getLocation().getY() >= 200.0D) {
/* 245 */       throw new CivException("You're too high to build camps.");
/*     */     }
/*     */     
/* 248 */     if (regionY + centerBlock.getLocation().getBlockY() >= 255) {
/* 249 */       throw new CivException("Cannot build war camp here, would go over the minecraft height limit.");
/*     */     }
/*     */     
/* 252 */     int minsLeft = isWarCampCooldownLeft();
/* 253 */     if (minsLeft > 0) {
/* 254 */       throw new CivException("Building a War Camp is on cooldown. You must wait " + minsLeft + " mins before you can build another.");
/*     */     }
/*     */     
/* 257 */     if (!player.isOp()) {
/* 258 */       Buildable.validateDistanceFromSpawn(centerBlock.getLocation());
/*     */     }
/*     */     
/* 261 */     int yTotal = 0;
/* 262 */     int yCount = 0;
/*     */     
/* 264 */     for (int x = 0; x < regionX; x++) {
/* 265 */       for (int y = 0; y < regionY; y++) {
/* 266 */         for (int z = 0; z < regionZ; z++) {
/* 267 */           Block b = centerBlock.getRelative(x, y, z);
/*     */           
/* 269 */           if (ItemManager.getId(b) == 54) {
/* 270 */             throw new CivException("Cannot build here, would destroy chest.");
/*     */           }
/*     */           
/* 273 */           BlockCoord coord = new BlockCoord(b);
/* 274 */           ChunkCoord chunkCoord = new ChunkCoord(coord.getLocation());
/*     */           
/* 276 */           TownChunk tc = CivGlobal.getTownChunk(chunkCoord);
/* 277 */           if ((tc != null) && (!tc.perms.hasPermission(PlotPermissions.Type.DESTROY, CivGlobal.getResident(player))))
/*     */           {
/* 279 */             throw new CivException("Cannot build here, you need DESTROY permissions to the block at " + b.getX() + "," + b.getY() + "," + b.getZ());
/*     */           }
/*     */           
/* 282 */           if (CivGlobal.getProtectedBlock(coord) != null) {
/* 283 */             throw new CivException("Cannot build here, protected blocks in the way.");
/*     */           }
/*     */           
/* 286 */           if (CivGlobal.getStructureBlock(coord) != null) {
/* 287 */             throw new CivException("Cannot build here, structure blocks in the way.");
/*     */           }
/*     */           
/* 290 */           if (CivGlobal.getFarmChunk(chunkCoord) != null) {
/* 291 */             throw new CivException("Cannot build here, in the same chunk as a farm improvement.");
/*     */           }
/*     */           
/* 294 */           if (CivGlobal.getWallChunk(chunkCoord) != null) {
/* 295 */             throw new CivException("Cannot build here, in the same chunk as a wall improvement.");
/*     */           }
/*     */           
/* 298 */           if (CivGlobal.getCampBlock(coord) != null) {
/* 299 */             throw new CivException("Cannot build here, a camp is in the way.");
/*     */           }
/*     */           
/* 302 */           yTotal += b.getWorld().getHighestBlockYAt(centerBlock.getX() + x, centerBlock.getZ() + z);
/* 303 */           yCount++;
/*     */           
/* 305 */           if (CivGlobal.getRoadBlock(coord) != null) {
/* 306 */             throw new CivException("Cannot build a war camp on top of an existing road block.");
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 312 */     double highestAverageBlock = yTotal / yCount;
/*     */     
/* 314 */     if ((centerBlock.getY() > highestAverageBlock + 10.0D) || 
/* 315 */       (centerBlock.getY() < highestAverageBlock - 10.0D)) {
/* 316 */       throw new CivException("Cannot build here, you must be closer to the surface.");
/*     */     }
/*     */   }
/*     */   
/*     */   private void buildWarCampFromTemplate(Template tpl, BlockCoord corner) {
/* 321 */     Block cornerBlock = corner.getBlock();
/* 322 */     for (int x = 0; x < tpl.size_x; x++) {
/* 323 */       for (int y = 0; y < tpl.size_y; y++) {
/* 324 */         for (int z = 0; z < tpl.size_z; z++) {
/* 325 */           Block nextBlock = cornerBlock.getRelative(x, y, z);
/*     */           
/* 327 */           if (tpl.blocks[x][y][z].specialType != SimpleBlock.Type.COMMAND)
/*     */           {
/*     */ 
/*     */ 
/* 331 */             if (tpl.blocks[x][y][z].specialType == SimpleBlock.Type.LITERAL)
/*     */             {
/* 333 */               tpl.blocks[x][y][z].command = "/literal";
/* 334 */               tpl.commandBlockRelativeLocations.add(new BlockCoord(cornerBlock.getWorld().getName(), x, y, z));
/*     */             }
/*     */             else
/*     */             {
/*     */               try {
/* 339 */                 if (ItemManager.getId(nextBlock) != tpl.blocks[x][y][z].getType())
/*     */                 {
/* 341 */                   WarRegen.saveBlock(nextBlock, "special:WarCamps", false);
/* 342 */                   ItemManager.setTypeId(nextBlock, tpl.blocks[x][y][z].getType());
/* 343 */                   ItemManager.setData(nextBlock, tpl.blocks[x][y][z].getData());
/*     */                 }
/*     */                 
/* 346 */                 if (ItemManager.getId(nextBlock) != 0) {
/* 347 */                   addStructureBlock(new BlockCoord(nextBlock.getLocation()), true);
/*     */                 }
/*     */               } catch (Exception e) {
/* 350 */                 CivLog.error(e.getMessage());
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
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
/* 378 */     return null;
/*     */   }
/*     */   
/*     */   public String getMarkerIconName()
/*     */   {
/* 383 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onComplete() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void onLoad()
/*     */     throws CivException
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void onUnload() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void load(ResultSet rs)
/*     */     throws SQLException, InvalidNameException, com.avrgaming.civcraft.exception.InvalidObjectException, CivException
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void save() {}
/*     */   
/*     */ 
/*     */   public void saveNow()
/*     */     throws SQLException
/*     */   {}
/*     */   
/*     */ 
/*     */   public void createControlPoint(BlockCoord absCoord)
/*     */   {
/* 418 */     Location centerLoc = absCoord.getLocation();
/*     */     
/*     */ 
/*     */ 
/* 422 */     Block b = centerLoc.getBlock();
/* 423 */     WarRegen.saveBlock(b, "special:WarCamps", false);
/* 424 */     ItemManager.setTypeId(b, 85);ItemManager.setData(b, 0);
/*     */     
/* 426 */     StructureBlock sb = new StructureBlock(new BlockCoord(b), this);
/* 427 */     addStructureBlock(sb.getCoord(), true);
/*     */     
/*     */ 
/*     */ 
/* 431 */     b = centerLoc.getBlock().getRelative(0, 1, 0);
/* 432 */     WarRegen.saveBlock(b, "special:WarCamps", false);
/* 433 */     ItemManager.setTypeId(b, 49);
/*     */     
/* 435 */     sb = new StructureBlock(new BlockCoord(b), this);
/* 436 */     addStructureBlock(sb.getCoord(), true);
/*     */     
/*     */     try
/*     */     {
/* 440 */       townhallControlHitpoints = CivSettings.getInteger(CivSettings.warConfig, "warcamp.control_block_hitpoints").intValue();
/*     */     } catch (InvalidConfiguration e) { int townhallControlHitpoints;
/* 442 */       e.printStackTrace(); return;
/*     */     }
/*     */     
/*     */     int townhallControlHitpoints;
/* 446 */     BlockCoord coord = new BlockCoord(b);
/* 447 */     this.controlPoints.put(coord, new ControlPoint(coord, this, townhallControlHitpoints));
/*     */   }
/*     */   
/*     */   public void onDamage(int amount, World world, Player player, BlockCoord coord, BuildableDamageBlock hit)
/*     */   {
/* 452 */     ControlPoint cp = (ControlPoint)this.controlPoints.get(coord);
/* 453 */     Resident resident = CivGlobal.getResident(player);
/*     */     
/* 455 */     if (cp != null) {
/* 456 */       if (!cp.isDestroyed())
/*     */       {
/* 458 */         if (resident.isControlBlockInstantBreak()) {
/* 459 */           cp.damage(cp.getHitpoints());
/*     */         } else {
/* 461 */           cp.damage(amount);
/*     */         }
/*     */         
/* 464 */         if (cp.isDestroyed()) {
/* 465 */           onControlBlockDestroy(cp, world, player, (StructureBlock)hit);
/*     */         } else {
/* 467 */           onControlBlockHit(cp, world, player, (StructureBlock)hit);
/*     */         }
/*     */       } else {
/* 470 */         CivMessage.send(player, "§cControl Block already destroyed.");
/*     */       }
/*     */     }
/*     */     else {
/* 474 */       CivMessage.send(player, "§cCannot Damage " + getDisplayName() + ", go after the control points!");
/*     */     }
/*     */   }
/*     */   
/*     */   public void onControlBlockDestroy(ControlPoint cp, World world, Player player, StructureBlock hit)
/*     */   {
/* 480 */     Resident attacker = CivGlobal.getResident(player);
/*     */     
/* 482 */     ItemManager.setTypeId(hit.getCoord().getLocation().getBlock(), 0);
/* 483 */     world.playSound(hit.getCoord().getLocation(), Sound.ANVIL_BREAK, 1.0F, -1.0F);
/* 484 */     world.playSound(hit.getCoord().getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
/*     */     
/* 486 */     FireworkEffect effect = FireworkEffect.builder().with(org.bukkit.FireworkEffect.Type.BURST).withColor(Color.OLIVE).withColor(Color.RED).withTrail().withFlicker().build();
/* 487 */     FireworkEffectPlayer fePlayer = new FireworkEffectPlayer();
/* 488 */     for (int i = 0; i < 3; i++) {
/*     */       try {
/* 490 */         fePlayer.playFirework(world, hit.getCoord().getLocation(), effect);
/*     */       } catch (Exception e) {
/* 492 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/* 496 */     boolean allDestroyed = true;
/* 497 */     for (ControlPoint c : this.controlPoints.values()) {
/* 498 */       if (!c.isDestroyed()) {
/* 499 */         allDestroyed = false;
/* 500 */         break;
/*     */       }
/*     */     }
/*     */     
/* 504 */     if (allDestroyed) {
/* 505 */       onWarCampDestroy();
/*     */     }
/*     */     else {
/* 508 */       CivMessage.sendCiv(attacker.getTown().getCiv(), "§aWe've destroyed a control block in " + getCiv().getName() + "'s War Camp!");
/* 509 */       CivMessage.sendCiv(getCiv(), "§cA control block in our War Camp has been destroyed!");
/*     */     }
/*     */   }
/*     */   
/*     */   private void onWarCampDestroy()
/*     */   {
/* 515 */     CivMessage.sendCiv(getCiv(), "§cOur War Camp has been destroyed!");
/* 516 */     getCiv().getWarCamps().remove(this);
/*     */     
/* 518 */     for (BlockCoord coord : this.structureBlocks.keySet()) {
/* 519 */       CivGlobal.removeStructureBlock(coord);
/*     */     }
/* 521 */     this.structureBlocks.clear();
/*     */     
/* 523 */     fancyDestroyStructureBlocks();
/* 524 */     setWarCampBuilt();
/*     */   }
/*     */   
/*     */   public void onControlBlockHit(ControlPoint cp, World world, Player player, StructureBlock hit) {
/* 528 */     world.playSound(hit.getCoord().getLocation(), Sound.ANVIL_USE, 0.2F, 1.0F);
/* 529 */     world.playEffect(hit.getCoord().getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
/*     */     
/* 531 */     CivMessage.send(player, "§7Damaged Control Block (" + cp.getHitpoints() + " / " + cp.getMaxHitpoints() + ")");
/* 532 */     CivMessage.sendCiv(getCiv(), "§eOur War Camp's Control Points are under attack!");
/*     */   }
/*     */   
/*     */   public String getRespawnName()
/*     */   {
/* 537 */     return "WarCamp\n(" + this.corner.getX() + "," + this.corner.getY() + "," + this.corner.getZ() + ")";
/*     */   }
/*     */   
/*     */   public List<BlockCoord> getRespawnPoints()
/*     */   {
/* 542 */     return getRespawnPoints();
/*     */   }
/*     */   
/*     */   public BlockCoord getRandomRevivePoint()
/*     */   {
/* 547 */     if (this.respawnPoints.size() == 0) {
/* 548 */       return new BlockCoord(getCorner());
/*     */     }
/* 550 */     Random rand = new Random();
/* 551 */     int index = rand.nextInt(this.respawnPoints.size());
/* 552 */     return (BlockCoord)this.respawnPoints.get(index);
/*     */   }
/*     */   
/*     */ 
/*     */   public void onWarEnd()
/*     */   {
/* 558 */     for (BlockCoord coord : this.structureBlocks.keySet()) {
/* 559 */       CivGlobal.removeStructureBlock(coord);
/*     */     }
/*     */     
/* 562 */     this.structureBlocks.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\camp\WarCamp.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
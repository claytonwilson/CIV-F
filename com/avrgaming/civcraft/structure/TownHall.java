/*     */ package com.avrgaming.civcraft.structure;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigCultureLevel;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.items.BonusGoodie;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.BuffManager;
/*     */ import com.avrgaming.civcraft.object.BuildableDamageBlock;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.ControlPoint;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.StructureBlock;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.object.TownChunk;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import com.avrgaming.civcraft.util.CivColor;
/*     */ import com.avrgaming.civcraft.util.FireworkEffectPlayer;
/*     */ import com.avrgaming.civcraft.util.ItemFrameStorage;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.war.War;
/*     */ import com.avrgaming.civcraft.war.WarStats;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.Effect;
/*     */ import org.bukkit.FireworkEffect;
/*     */ import org.bukkit.FireworkEffect.Builder;
/*     */ import org.bukkit.FireworkEffect.Type;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Sound;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.ItemFrame;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
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
/*     */ public class TownHall
/*     */   extends Structure
/*     */   implements RespawnLocationHolder
/*     */ {
/*  71 */   public static int MAX_GOODIE_FRAMES = 8;
/*     */   
/*  73 */   private BlockCoord[] techbar = new BlockCoord[10];
/*     */   
/*     */   private BlockCoord technameSign;
/*     */   
/*     */   private byte technameSignData;
/*     */   
/*     */   private BlockCoord techdataSign;
/*     */   private byte techdataSignData;
/*  81 */   private ArrayList<ItemFrameStorage> goodieFrames = new ArrayList();
/*  82 */   private ArrayList<BlockCoord> respawnPoints = new ArrayList();
/*  83 */   private ArrayList<BlockCoord> revivePoints = new ArrayList();
/*  84 */   protected HashMap<BlockCoord, ControlPoint> controlPoints = new HashMap();
/*     */   
/*  86 */   public ArrayList<BlockCoord> nextGoodieFramePoint = new ArrayList();
/*  87 */   public ArrayList<Integer> nextGoodieFrameDirection = new ArrayList();
/*     */   
/*     */   protected TownHall(Location center, String id, Town town) throws CivException
/*     */   {
/*  91 */     super(center, id, town);
/*     */   }
/*     */   
/*     */   public TownHall(ResultSet rs) throws SQLException, CivException {
/*  95 */     super(rs);
/*     */   }
/*     */   
/*     */   public void delete() throws SQLException
/*     */   {
/* 100 */     if (getTown() != null)
/*     */     {
/* 102 */       for (ItemFrameStorage framestore : this.goodieFrames) {
/* 103 */         BonusGoodie goodie = CivGlobal.getBonusGoodie(framestore.getItem());
/* 104 */         if (goodie != null) {
/* 105 */           goodie.replenish();
/*     */         }
/*     */         
/* 108 */         CivGlobal.removeProtectedItemFrame(framestore.getFrameID());
/*     */       }
/*     */     }
/*     */     
/* 112 */     super.delete();
/*     */   }
/*     */   
/*     */   public String getDynmapDescription()
/*     */   {
/* 117 */     String out = "";
/* 118 */     out = out + "<b>Town Hall of " + getTown().getName() + "</b>";
/* 119 */     ConfigCultureLevel culturelevel = (ConfigCultureLevel)CivSettings.cultureLevels.get(Integer.valueOf(getTown().getCultureLevel()));
/* 120 */     out = out + "<br/>Culture: Level:" + culturelevel.level + " (" + getTown().getAccumulatedCulture() + "/" + culturelevel.amount + ")";
/* 121 */     out = out + "<br/>Flat Tax: " + getTown().getFlatTax() * 100.0D + "%";
/* 122 */     out = out + "<br/>Property Tax: " + getTown().getTaxRate() * 100.0D + "%";
/* 123 */     return out;
/*     */   }
/*     */   
/*     */   public void addTechBarBlock(BlockCoord coord, int index) {
/* 127 */     this.techbar[index] = coord;
/*     */   }
/*     */   
/*     */   public BlockCoord getTechBarBlockCoord(int i) {
/* 131 */     if (this.techbar[i] == null) {
/* 132 */       return null;
/*     */     }
/* 134 */     return this.techbar[i];
/*     */   }
/*     */   
/*     */   public BlockCoord getTechnameSign() {
/* 138 */     return this.technameSign;
/*     */   }
/*     */   
/*     */   public void setTechnameSign(BlockCoord technameSign) {
/* 142 */     this.technameSign = technameSign;
/*     */   }
/*     */   
/*     */   public BlockCoord getTechdataSign() {
/* 146 */     return this.techdataSign;
/*     */   }
/*     */   
/*     */   public void setTechdataSign(BlockCoord techdataSign) {
/* 150 */     this.techdataSign = techdataSign;
/*     */   }
/*     */   
/*     */   public byte getTechdataSignData() {
/* 154 */     return this.techdataSignData;
/*     */   }
/*     */   
/*     */   public void setTechdataSignData(byte techdataSignData) {
/* 158 */     this.techdataSignData = techdataSignData;
/*     */   }
/*     */   
/*     */   public byte getTechnameSignData() {
/* 162 */     return this.technameSignData;
/*     */   }
/*     */   
/*     */   public void setTechnameSignData(byte technameSignData) {
/* 166 */     this.technameSignData = technameSignData;
/*     */   }
/*     */   
/*     */   public BlockCoord getTechBar(int i) {
/* 170 */     return this.techbar[i];
/*     */   }
/*     */   
/*     */   public void createGoodieItemFrame(BlockCoord absCoord, int slotId, int direction) {
/* 174 */     if (slotId >= MAX_GOODIE_FRAMES) {
/*     */       return;
/*     */     }
/*     */     
/*     */     BlockFace facingDirection;
/*     */     
/*     */     BlockFace facingDirection;
/*     */     
/*     */     BlockFace facingDirection;
/*     */     
/*     */     BlockFace facingDirection;
/*     */     
/* 186 */     switch (direction) {
/*     */     case 5: 
/* 188 */       Block attachedBlock = absCoord.getBlock().getRelative(BlockFace.WEST);
/* 189 */       facingDirection = BlockFace.EAST;
/* 190 */       break;
/*     */     case 4: 
/* 192 */       Block attachedBlock = absCoord.getBlock().getRelative(BlockFace.EAST);
/* 193 */       facingDirection = BlockFace.WEST;
/* 194 */       break;
/*     */     case 2: 
/* 196 */       Block attachedBlock = absCoord.getBlock().getRelative(BlockFace.SOUTH);
/* 197 */       facingDirection = BlockFace.NORTH;
/* 198 */       break;
/*     */     case 3: 
/* 200 */       Block attachedBlock = absCoord.getBlock().getRelative(BlockFace.NORTH);
/* 201 */       facingDirection = BlockFace.SOUTH;
/* 202 */       break;
/*     */     default: 
/* 204 */       CivLog.error("Bad sign data for /itemframe sign in town hall."); return;
/*     */     }
/*     */     BlockFace facingDirection;
/*     */     Block attachedBlock;
/* 208 */     Block itemFrameBlock = absCoord.getBlock();
/* 209 */     if (ItemManager.getId(itemFrameBlock) != 0) {
/* 210 */       ItemManager.setTypeId(itemFrameBlock, 0);
/*     */     }
/*     */     
/*     */ 
/* 214 */     ItemFrame frame = null;
/* 215 */     Entity entity = CivGlobal.getEntityAtLocation(absCoord.getBlock().getLocation());
/* 216 */     ItemFrameStorage itemStore; if ((entity == null) || (!(entity instanceof ItemFrame))) {
/* 217 */       itemStore = new ItemFrameStorage(attachedBlock.getLocation(), facingDirection);
/*     */     } else {
/*     */       try {
/* 220 */         frame = (ItemFrame)entity;
/* 221 */         itemStore = new ItemFrameStorage(frame, attachedBlock.getLocation());
/*     */       } catch (CivException e) { ItemFrameStorage itemStore;
/* 223 */         e.printStackTrace(); return;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     ItemFrameStorage itemStore;
/*     */     
/*     */ 
/* 231 */     itemStore.setBuildable(this);
/* 232 */     this.goodieFrames.add(itemStore);
/*     */   }
/*     */   
/*     */   public ArrayList<ItemFrameStorage> getGoodieFrames()
/*     */   {
/* 237 */     return this.goodieFrames;
/*     */   }
/*     */   
/*     */   public void setRespawnPoint(BlockCoord absCoord) {
/* 241 */     this.respawnPoints.add(absCoord);
/*     */   }
/*     */   
/*     */   public BlockCoord getRandomRespawnPoint() {
/* 245 */     if (this.respawnPoints.size() == 0) {
/* 246 */       return null;
/*     */     }
/*     */     
/* 249 */     Random rand = new Random();
/* 250 */     return (BlockCoord)this.respawnPoints.get(rand.nextInt(this.respawnPoints.size()));
/*     */   }
/*     */   
/*     */   public int getRespawnTime()
/*     */   {
/*     */     try {
/* 256 */       int baseRespawn = CivSettings.getInteger(CivSettings.warConfig, "war.respawn_time").intValue();
/* 257 */       int controlRespawn = CivSettings.getInteger(CivSettings.warConfig, "war.control_block_respawn_time").intValue();
/* 258 */       int invalidRespawnPenalty = CivSettings.getInteger(CivSettings.warConfig, "war.invalid_respawn_penalty").intValue();
/*     */       
/* 260 */       int totalRespawn = baseRespawn;
/* 261 */       for (ControlPoint cp : this.controlPoints.values()) {
/* 262 */         if (cp.isDestroyed()) {
/* 263 */           totalRespawn += controlRespawn;
/*     */         }
/*     */       }
/*     */       
/* 267 */       if ((this.validated) && (!isValid())) {
/* 268 */         totalRespawn += invalidRespawnPenalty * 60;
/*     */       }
/*     */       
/*     */ 
/* 272 */       for (Town t : getCiv().getTowns()) {
/* 273 */         if (t.getBuffManager().hasBuff("buff_medicine")) {
/* 274 */           int respawnTimeBonus = t.getBuffManager().getEffectiveInt("buff_medicine");
/* 275 */           totalRespawn = Math.max(1, totalRespawn - respawnTimeBonus);
/* 276 */           break;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 281 */       return totalRespawn;
/*     */     } catch (InvalidConfiguration e) {
/* 283 */       e.printStackTrace();
/*     */     }
/* 285 */     return 60;
/*     */   }
/*     */   
/*     */   public void setRevivePoint(BlockCoord absCoord) {
/* 289 */     this.revivePoints.add(absCoord);
/*     */   }
/*     */   
/*     */   public BlockCoord getRandomRevivePoint() {
/* 293 */     if ((this.revivePoints.size() == 0) || (!isComplete())) {
/* 294 */       return new BlockCoord(getCorner());
/*     */     }
/* 296 */     Random rand = new Random();
/* 297 */     int index = rand.nextInt(this.revivePoints.size());
/* 298 */     return (BlockCoord)this.revivePoints.get(index);
/*     */   }
/*     */   
/*     */ 
/*     */   public void createControlPoint(BlockCoord absCoord)
/*     */   {
/* 304 */     Location centerLoc = absCoord.getLocation();
/*     */     
/*     */ 
/*     */ 
/* 308 */     Block b = centerLoc.getBlock();
/* 309 */     ItemManager.setTypeId(b, 85);ItemManager.setData(b, 0);
/*     */     
/* 311 */     StructureBlock sb = new StructureBlock(new BlockCoord(b), this);
/* 312 */     addStructureBlock(sb.getCoord(), true);
/*     */     
/*     */ 
/*     */ 
/* 316 */     b = centerLoc.getBlock().getRelative(0, 1, 0);
/* 317 */     ItemManager.setTypeId(b, 49);
/* 318 */     sb = new StructureBlock(new BlockCoord(b), this);
/* 319 */     addStructureBlock(sb.getCoord(), true);
/*     */     int townhallControlHitpoints;
/*     */     try
/*     */     {
/* 323 */       townhallControlHitpoints = CivSettings.getInteger(CivSettings.warConfig, "war.control_block_hitpoints_townhall").intValue();
/*     */     } catch (InvalidConfiguration e) { int townhallControlHitpoints;
/* 325 */       e.printStackTrace();
/* 326 */       townhallControlHitpoints = 100;
/*     */     }
/*     */     
/* 329 */     BlockCoord coord = new BlockCoord(b);
/* 330 */     this.controlPoints.put(coord, new ControlPoint(coord, this, townhallControlHitpoints));
/*     */   }
/*     */   
/*     */   public void onControlBlockDestroy(ControlPoint cp, World world, Player player, StructureBlock hit)
/*     */   {
/* 335 */     Resident attacker = CivGlobal.getResident(player);
/*     */     
/* 337 */     ItemManager.setTypeId(hit.getCoord().getLocation().getBlock(), 0);
/* 338 */     world.playSound(hit.getCoord().getLocation(), Sound.ANVIL_BREAK, 1.0F, -1.0F);
/* 339 */     world.playSound(hit.getCoord().getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
/*     */     
/* 341 */     FireworkEffect effect = FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.YELLOW).withColor(Color.RED).withTrail().withFlicker().build();
/* 342 */     FireworkEffectPlayer fePlayer = new FireworkEffectPlayer();
/* 343 */     for (int i = 0; i < 3; i++) {
/*     */       try {
/* 345 */         fePlayer.playFirework(world, hit.getCoord().getLocation(), effect);
/*     */       } catch (Exception e) {
/* 347 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/* 351 */     boolean allDestroyed = true;
/* 352 */     for (ControlPoint c : this.controlPoints.values()) {
/* 353 */       if (!c.isDestroyed()) {
/* 354 */         allDestroyed = false;
/* 355 */         break;
/*     */       }
/*     */     }
/* 358 */     CivMessage.sendTownSound(hit.getTown(), Sound.AMBIENCE_CAVE, 1.0F, 0.5F);
/*     */     
/* 360 */     if (allDestroyed)
/*     */     {
/* 362 */       if (getTown().getCiv().getCapitolName().equals(getTown().getName())) {
/* 363 */         CivMessage.global("§b" + ChatColor.BOLD + "The civilization of " + getTown().getCiv().getName() + " has been conquered by " + attacker.getCiv().getName() + "!");
/* 364 */         for (Town town : getTown().getCiv().getTowns()) {
/* 365 */           town.defeated = true;
/*     */         }
/*     */         
/* 368 */         War.transferDefeated(getTown().getCiv(), attacker.getTown().getCiv());
/* 369 */         WarStats.logCapturedCiv(attacker.getTown().getCiv(), getTown().getCiv());
/* 370 */         War.saveDefeatedCiv(getCiv(), attacker.getTown().getCiv());
/*     */         
/* 372 */         if (CivGlobal.isCasualMode()) {
/* 373 */           HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(new ItemStack[] { getCiv().getRandomLeaderSkull("Victory Over " + getCiv().getName() + "!") });
/* 374 */           for (ItemStack stack : leftovers.values()) {
/* 375 */             player.getWorld().dropItem(player.getLocation(), stack);
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 380 */         CivMessage.global("§e" + ChatColor.BOLD + "The town of " + getTown().getName() + " in " + getCiv().getName() + " has been conquered by " + attacker.getCiv().getName() + "!");
/*     */         
/* 382 */         getTown().defeated = true;
/*     */         
/* 384 */         WarStats.logCapturedTown(attacker.getTown().getCiv(), getTown());
/* 385 */         War.saveDefeatedTown(getTown().getName(), attacker.getTown().getCiv());
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 390 */       CivMessage.sendTown(hit.getTown(), "§cOne of our Town Hall's Control Points has been destroyed!");
/* 391 */       CivMessage.sendCiv(attacker.getTown().getCiv(), "§aWe've destroyed a control block in " + hit.getTown().getName() + "!");
/* 392 */       CivMessage.sendCiv(hit.getTown().getCiv(), "§cA control block in " + hit.getTown().getName() + " has been destroyed!");
/*     */     }
/*     */   }
/*     */   
/*     */   public void onControlBlockHit(ControlPoint cp, World world, Player player, StructureBlock hit)
/*     */   {
/* 398 */     world.playSound(hit.getCoord().getLocation(), Sound.ANVIL_USE, 0.2F, 1.0F);
/* 399 */     world.playEffect(hit.getCoord().getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
/*     */     
/* 401 */     CivMessage.send(player, "§7Damaged Control Block (" + cp.getHitpoints() + " / " + cp.getMaxHitpoints() + ")");
/* 402 */     CivMessage.sendTown(hit.getTown(), "§eOne of our Town Hall's Control Points is under attack!");
/*     */   }
/*     */   
/*     */ 
/*     */   public void onDamage(int amount, World world, Player player, BlockCoord coord, BuildableDamageBlock hit)
/*     */   {
/* 408 */     ControlPoint cp = (ControlPoint)this.controlPoints.get(coord);
/* 409 */     Resident resident = CivGlobal.getResident(player);
/*     */     
/* 411 */     if (!resident.canDamageControlBlock()) {
/* 412 */       CivMessage.send(player, "§cCannot damage control blocks due to missing/invalid Town Hall or Capitol structure.");
/* 413 */       return;
/*     */     }
/*     */     
/* 416 */     if (cp != null) {
/* 417 */       if (!cp.isDestroyed())
/*     */       {
/* 419 */         if (resident.isControlBlockInstantBreak()) {
/* 420 */           cp.damage(cp.getHitpoints());
/*     */         } else {
/* 422 */           cp.damage(amount);
/*     */         }
/*     */         
/* 425 */         if (cp.isDestroyed()) {
/* 426 */           onControlBlockDestroy(cp, world, player, (StructureBlock)hit);
/*     */         } else {
/* 428 */           onControlBlockHit(cp, world, player, (StructureBlock)hit);
/*     */         }
/*     */       } else {
/* 431 */         CivMessage.send(player, "§cControl Block already destroyed.");
/*     */       }
/*     */     }
/*     */     else {
/* 435 */       CivMessage.send(player, "§cCannot Damage " + getDisplayName() + ", go after the control points!");
/*     */     }
/*     */   }
/*     */   
/*     */   public void regenControlBlocks() {
/* 440 */     for (BlockCoord coord : this.controlPoints.keySet()) {
/* 441 */       ItemManager.setTypeId(coord.getBlock(), 49);
/*     */       
/* 443 */       ControlPoint cp = (ControlPoint)this.controlPoints.get(coord);
/* 444 */       cp.setHitpoints(cp.getMaxHitpoints());
/*     */     }
/*     */   }
/*     */   
/*     */   public int getTechBarSize() {
/* 449 */     return this.techbar.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onLoad() {}
/*     */   
/*     */ 
/*     */   public void onPreBuild(Location loc)
/*     */     throws CivException
/*     */   {
/* 460 */     TownHall oldTownHall = getTown().getTownHall();
/* 461 */     if (oldTownHall != null) {
/* 462 */       ChunkCoord coord = new ChunkCoord(loc);
/* 463 */       TownChunk tc = CivGlobal.getTownChunk(coord);
/* 464 */       if ((tc == null) || (tc.getTown() != getTown())) {
/* 465 */         throw new CivException("Cannot rebuild your town hall outside of your town borders.");
/*     */       }
/*     */       
/* 468 */       if (War.isWarTime()) {
/* 469 */         throw new CivException("Cannot rebuild your town hall during war time.");
/*     */       }
/*     */       
/* 472 */       getTown().clearBonusGoods();
/*     */       try
/*     */       {
/* 475 */         getTown().demolish(oldTownHall, true);
/*     */       } catch (CivException e) {
/* 477 */         e.printStackTrace();
/*     */       }
/* 479 */       CivMessage.sendTown(getTown(), "Your old town hall or capitol was demolished to make way for your new one.");
/* 480 */       this.autoClaim = false;
/*     */     } else {
/* 482 */       this.autoClaim = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public void onInvalidPunish()
/*     */   {
/*     */     try
/*     */     {
/* 490 */       invalid_respawn_penalty = CivSettings.getInteger(CivSettings.warConfig, "war.invalid_respawn_penalty").intValue();
/*     */     } catch (InvalidConfiguration e) { int invalid_respawn_penalty;
/* 492 */       e.printStackTrace(); return;
/*     */     }
/*     */     
/*     */     int invalid_respawn_penalty;
/* 496 */     CivMessage.sendTown(getTown(), "§c" + CivColor.BOLD + "Our town's town hall cannot be supported by the blocks underneath!" + 
/* 497 */       " It will take us an extra " + invalid_respawn_penalty + " mins to respawn during war if its not fixed in time!");
/*     */   }
/*     */   
/*     */   public List<BlockCoord> getRespawnPoints()
/*     */   {
/* 502 */     return this.revivePoints;
/*     */   }
/*     */   
/*     */   public String getRespawnName()
/*     */   {
/* 507 */     return "Town Hall\n" + getTown().getName();
/*     */   }
/*     */   
/*     */   public HashMap<BlockCoord, ControlPoint> getControlPoints()
/*     */   {
/* 512 */     return this.controlPoints;
/*     */   }
/*     */   
/*     */   public void onCannonDamage(int damage) {
/* 516 */     this.hitpoints -= damage;
/*     */     
/* 518 */     if (this.hitpoints <= 0) {
/* 519 */       CivMessage.sendCiv(getCiv(), "Our " + getDisplayName() + " is out of hitpoints, walls can be destroyed by cannon blasts!");
/* 520 */       this.hitpoints = 0;
/*     */     }
/*     */     
/* 523 */     CivMessage.sendCiv(getCiv(), "Our " + getDisplayName() + " has been hit by a cannon! (" + this.hitpoints + "/" + getMaxHitPoints() + ")");
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\TownHall.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
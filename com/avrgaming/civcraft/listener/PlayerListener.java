/*     */ package com.avrgaming.civcraft.listener;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigTechPotion;
/*     */ import com.avrgaming.civcraft.items.units.Unit;
/*     */ import com.avrgaming.civcraft.items.units.UnitItemMaterial;
/*     */ import com.avrgaming.civcraft.items.units.UnitMaterial;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreMaterial;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.mobs.timers.MobSpawnerTimer;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.CultureChunk;
/*     */ import com.avrgaming.civcraft.object.DiplomacyManager;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.road.Road;
/*     */ import com.avrgaming.civcraft.structure.Capitol;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.threading.tasks.PlayerChunkNotifyAsyncTask;
/*     */ import com.avrgaming.civcraft.threading.tasks.PlayerLoginAsyncTask;
/*     */ import com.avrgaming.civcraft.threading.timers.PlayerLocationCacheUpdate;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.war.War;
/*     */ import com.avrgaming.civcraft.war.WarStats;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.Chest;
/*     */ import org.bukkit.block.DoubleChest;
/*     */ import org.bukkit.entity.Arrow;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.EntityType;
/*     */ import org.bukkit.entity.Item;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.entity.ThrownPotion;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.Action;
/*     */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*     */ import org.bukkit.event.entity.EntityDeathEvent;
/*     */ import org.bukkit.event.entity.PlayerDeathEvent;
/*     */ import org.bukkit.event.entity.PotionSplashEvent;
/*     */ import org.bukkit.event.inventory.BrewEvent;
/*     */ import org.bukkit.event.inventory.InventoryOpenEvent;
/*     */ import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
/*     */ import org.bukkit.event.player.PlayerBucketEmptyEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.event.player.PlayerItemConsumeEvent;
/*     */ import org.bukkit.event.player.PlayerLoginEvent;
/*     */ import org.bukkit.event.player.PlayerMoveEvent;
/*     */ import org.bukkit.event.player.PlayerPickupItemEvent;
/*     */ import org.bukkit.event.player.PlayerPortalEvent;
/*     */ import org.bukkit.event.player.PlayerQuitEvent;
/*     */ import org.bukkit.event.player.PlayerRespawnEvent;
/*     */ import org.bukkit.event.player.PlayerTeleportEvent;
/*     */ import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
/*     */ import org.bukkit.event.world.PortalCreateEvent;
/*     */ import org.bukkit.inventory.BrewerInventory;
/*     */ import org.bukkit.inventory.DoubleChestInventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.potion.PotionEffect;
/*     */ import org.bukkit.potion.PotionEffectType;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerListener
/*     */   implements Listener
/*     */ {
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void onPlayerPickup(PlayerPickupItemEvent event)
/*     */   {
/*  91 */     boolean rare = false;
/*  92 */     String name; if (event.getItem().getItemStack().getItemMeta().hasDisplayName()) {
/*  93 */       String name = event.getItem().getItemStack().getItemMeta().getDisplayName();
/*  94 */       rare = true;
/*     */     } else {
/*  96 */       name = event.getItem().getItemStack().getType().name().replace("_", " ").toLowerCase();
/*     */     }
/*     */     
/*  99 */     Resident resident = CivGlobal.getResident(event.getPlayer());
/* 100 */     if (resident.getItemMode().equals("all")) {
/* 101 */       CivMessage.send(event.getPlayer(), "§aYou've picked up §d" + event.getItem().getItemStack().getAmount() + " " + name);
/* 102 */     } else if ((resident.getItemMode().equals("rare")) && (rare)) {
/* 103 */       CivMessage.send(event.getPlayer(), "§aYou've picked up §d" + event.getItem().getItemStack().getAmount() + " " + name);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void onPlayerLogin(PlayerLoginEvent event)
/*     */   {
/* 110 */     CivLog.info("Scheduling on player login task for player:" + event.getPlayer().getName());
/* 111 */     TaskMaster.asyncTask("onPlayerLogin-" + event.getPlayer().getName(), new PlayerLoginAsyncTask(event.getPlayer()), 0L);
/*     */     
/* 113 */     CivGlobal.playerFirstLoginMap.put(event.getPlayer().getName(), new Date());
/* 114 */     PlayerLocationCacheUpdate.playerQueue.add(event.getPlayer().getName());
/* 115 */     MobSpawnerTimer.playerQueue.add(event.getPlayer().getName());
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void onPlayerTeleportEvent(PlayerTeleportEvent event) {
/* 120 */     if ((event.getCause().equals(PlayerTeleportEvent.TeleportCause.COMMAND)) || 
/* 121 */       (event.getCause().equals(PlayerTeleportEvent.TeleportCause.PLUGIN))) {
/* 122 */       CivLog.info(
/* 123 */         "[TELEPORT] " + event.getPlayer().getName() + " to:" + event.getTo().getBlockX() + "," + event.getTo().getBlockY() + "," + event.getTo().getBlockZ() + " from:" + event.getFrom().getBlockX() + "," + event.getFrom().getBlockY() + "," + event.getFrom().getBlockZ());
/*     */     }
/*     */   }
/*     */   
/*     */   private void setModifiedMovementSpeed(Player player)
/*     */   {
/* 129 */     double speed = CivSettings.normal_speed;
/*     */     
/*     */ 
/* 132 */     if (Unit.isWearingFullComposite(player)) {
/* 133 */       speed *= CivSettings.T4_leather_speed;
/*     */     }
/*     */     
/* 136 */     if (Unit.isWearingFullHardened(player)) {
/* 137 */       speed *= CivSettings.T3_leather_speed;
/*     */     }
/*     */     
/* 140 */     if (Unit.isWearingFullRefined(player)) {
/* 141 */       speed *= CivSettings.T2_leather_speed;
/*     */     }
/*     */     
/* 144 */     if (Unit.isWearingFullBasicLeather(player)) {
/* 145 */       speed *= CivSettings.T1_leather_speed;
/*     */     }
/*     */     
/* 148 */     if (Unit.isWearingAnyIron(player)) {
/* 149 */       speed *= CivSettings.T1_metal_speed;
/*     */     }
/*     */     
/* 152 */     if (Unit.isWearingAnyChain(player)) {
/* 153 */       speed *= CivSettings.T2_metal_speed;
/*     */     }
/*     */     
/* 156 */     if (Unit.isWearingAnyGold(player)) {
/* 157 */       speed *= CivSettings.T3_metal_speed;
/*     */     }
/*     */     
/* 160 */     if (Unit.isWearingAnyDiamond(player)) {
/* 161 */       speed *= CivSettings.T4_metal_speed;
/*     */     }
/*     */     
/* 164 */     Resident resident = CivGlobal.getResident(player);
/* 165 */     if ((resident != null) && (resident.isOnRoad())) {
/* 166 */       if ((player.getVehicle() != null) && (player.getVehicle().getType().equals(EntityType.HORSE))) {
/* 167 */         Vector vec = player.getVehicle().getVelocity();
/* 168 */         double yComp = vec.getY();
/*     */         
/* 170 */         vec.multiply(Road.ROAD_HORSE_SPEED);
/* 171 */         vec.setY(yComp);
/*     */         
/* 173 */         player.getVehicle().setVelocity(vec);
/*     */       } else {
/* 175 */         speed *= Road.ROAD_PLAYER_SPEED;
/*     */       }
/*     */     }
/*     */     
/* 179 */     player.setWalkSpeed((float)Math.min(1.0D, speed));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @EventHandler(priority=EventPriority.LOW)
/*     */   public void onPlayerMove(PlayerMoveEvent event)
/*     */   {
/* 187 */     if ((event.getFrom().getBlockX() == event.getTo().getBlockX()) && 
/* 188 */       (event.getFrom().getBlockZ() == event.getTo().getBlockZ()) && 
/* 189 */       (event.getFrom().getBlockY() == event.getTo().getBlockY())) {
/* 190 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 195 */     setModifiedMovementSpeed(event.getPlayer());
/*     */     
/* 197 */     ChunkCoord fromChunk = new ChunkCoord(event.getFrom());
/* 198 */     ChunkCoord toChunk = new ChunkCoord(event.getTo());
/*     */     
/*     */ 
/* 201 */     if (fromChunk.equals(toChunk)) {
/* 202 */       return;
/*     */     }
/*     */     
/* 205 */     TaskMaster.asyncTask(PlayerChunkNotifyAsyncTask.class.getSimpleName(), 
/* 206 */       new PlayerChunkNotifyAsyncTask(event.getFrom(), event.getTo(), event.getPlayer().getName()), 0L);
/*     */   }
/*     */   
/*     */ 
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void onPlayerRespawn(PlayerRespawnEvent event)
/*     */   {
/* 213 */     Player player = event.getPlayer();
/* 214 */     Resident resident = CivGlobal.getResident(player);
/* 215 */     if ((resident == null) || (!resident.hasTown())) {
/* 216 */       return;
/*     */     }
/*     */     
/* 219 */     if ((War.isWarTime()) && (!resident.isInsideArena()) && 
/* 220 */       (resident.getTown().getCiv().getDiplomacyManager().isAtWar()))
/*     */     {
/* 222 */       Capitol capitol = resident.getCiv().getCapitolStructure();
/* 223 */       if (capitol != null) {
/* 224 */         BlockCoord respawn = capitol.getRandomRespawnPoint();
/* 225 */         if (respawn != null)
/*     */         {
/* 227 */           resident.setLastKilledTime(new Date());
/* 228 */           event.setRespawnLocation(respawn.getCenteredLocation());
/* 229 */           CivMessage.send(player, "§7You've respawned in the War Room since it's WarTime and you're at war.");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void onPlayerQuit(PlayerQuitEvent event)
/*     */   {
/* 241 */     Resident resident = CivGlobal.getResident(event.getPlayer());
/* 242 */     if (resident != null) {
/* 243 */       if (resident.previewUndo != null) {
/* 244 */         resident.previewUndo.clear();
/*     */       }
/* 246 */       resident.clearInteractiveMode();
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void onEntityDeath(EntityDeathEvent event) {
/* 252 */     if ((event.getEntity() instanceof Player))
/*     */     {
/*     */ 
/* 255 */       ArrayList<ItemStack> stacksToRemove = new ArrayList();
/* 256 */       for (ItemStack stack : event.getDrops()) {
/* 257 */         if (stack != null)
/*     */         {
/* 259 */           LoreMaterial material = LoreMaterial.getMaterial(stack);
/* 260 */           if (material != null) {
/* 261 */             material.onPlayerDeath(event, stack);
/* 262 */             if ((material instanceof UnitMaterial)) {
/* 263 */               stacksToRemove.add(stack);
/*     */ 
/*     */ 
/*     */             }
/* 267 */             else if ((material instanceof UnitItemMaterial)) {
/* 268 */               stacksToRemove.add(stack);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 275 */       for (ItemStack stack : stacksToRemove) {
/* 276 */         event.getDrops().remove(stack);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void onPlayerDeath(PlayerDeathEvent event) {
/* 283 */     if ((War.isWarTime()) && 
/* 284 */       (event.getEntity().getKiller() != null)) {
/* 285 */       WarStats.incrementPlayerKills(event.getEntity().getKiller().getName());
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void onPortalCreate(PortalCreateEvent event)
/*     */   {
/* 292 */     event.setCancelled(true);
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
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void onPlayerPortalEvent(PlayerPortalEvent event)
/*     */   {
/* 327 */     if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL)) {
/* 328 */       event.setCancelled(true);
/* 329 */       CivMessage.sendErrorNoRepeat(event.getPlayer(), "The End portal is disabled on this server.");
/* 330 */       return;
/*     */     }
/*     */     
/* 333 */     if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)) {
/* 334 */       event.setCancelled(true);
/* 335 */       CivMessage.sendErrorNoRepeat(event.getPlayer(), "The Nether is disabled on this server.");
/* 336 */       return;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {}
/*     */   
/*     */ 
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void OnPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event)
/*     */   {
/* 348 */     Resident resident = CivGlobal.getResident(event.getPlayer());
/*     */     
/* 350 */     if (resident == null) {
/* 351 */       event.setCancelled(true);
/* 352 */       return;
/*     */     }
/*     */     
/* 355 */     ChunkCoord coord = new ChunkCoord(event.getBlockClicked().getLocation());
/* 356 */     CultureChunk cc = CivGlobal.getCultureChunk(coord);
/* 357 */     if ((cc != null) && (
/* 358 */       (event.getBucket().equals(Material.LAVA_BUCKET)) || 
/* 359 */       (event.getBucket().equals(Material.LAVA))))
/*     */     {
/* 361 */       if ((!resident.hasTown()) || (resident.getTown().getCiv() != cc.getCiv())) {
/* 362 */         CivMessage.sendError(event.getPlayer(), "You cannot place lava inside another civ's culture.");
/* 363 */         event.setCancelled(true);
/* 364 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @EventHandler(priority=EventPriority.LOW)
/*     */   public void OnBrewEvent(BrewEvent event)
/*     */   {
/* 374 */     if ((event.getContents().contains(Material.SPIDER_EYE)) || 
/* 375 */       (event.getContents().contains(Material.GOLDEN_CARROT)) || 
/* 376 */       (event.getContents().contains(Material.GHAST_TEAR)) || 
/* 377 */       (event.getContents().contains(Material.FERMENTED_SPIDER_EYE)) || 
/* 378 */       (event.getContents().contains(Material.BLAZE_POWDER)) || 
/* 379 */       (event.getContents().contains(Material.SULPHUR))) {
/* 380 */       event.setCancelled(true);
/*     */     }
/*     */     
/* 383 */     if (event.getContents().contains(Material.POTION)) {
/* 384 */       ItemStack potion = event.getContents().getItem(event.getContents().first(Material.POTION));
/*     */       
/* 386 */       if ((potion.getDurability() == 8192) || 
/* 387 */         (potion.getDurability() == 64) || 
/* 388 */         (potion.getDurability() == 32)) {
/* 389 */         event.setCancelled(true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isPotionDisabled(PotionEffect type) {
/* 395 */     if ((type.getType().equals(PotionEffectType.SPEED)) || 
/* 396 */       (type.getType().equals(PotionEffectType.FIRE_RESISTANCE)) || 
/* 397 */       (type.getType().equals(PotionEffectType.HEAL))) {
/* 398 */       return false;
/*     */     }
/*     */     
/* 401 */     return true;
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.LOW)
/*     */   public void onPotionSplash(PotionSplashEvent event) {
/* 406 */     for (PotionEffect effect : event.getPotion().getEffects()) {
/* 407 */       if (isPotionDisabled(effect)) {
/* 408 */         event.setCancelled(true);
/* 409 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.LOW)
/*     */   public void onConsume(PlayerItemConsumeEvent event) {
/* 416 */     if (ItemManager.getId(event.getItem()) == 322) {
/* 417 */       CivMessage.sendError(event.getPlayer(), "You cannot use golden apples.");
/* 418 */       event.setCancelled(true);
/* 419 */       return;
/*     */     }
/*     */     
/* 422 */     if (event.getItem().getType().equals(Material.POTION)) {
/* 423 */       ConfigTechPotion pot = (ConfigTechPotion)CivSettings.techPotions.get(Integer.valueOf(event.getItem().getDurability()));
/* 424 */       if (pot != null) {
/* 425 */         if (!pot.hasTechnology(event.getPlayer())) {
/* 426 */           CivMessage.sendError(event.getPlayer(), "You cannot use " + pot.name + " potions. You do not have the technology yet.");
/* 427 */           event.setCancelled(true);
/* 428 */           return;
/*     */         }
/* 430 */         if (pot.hasTechnology(event.getPlayer())) {
/* 431 */           event.setCancelled(false);
/*     */         }
/*     */       } else {
/* 434 */         CivMessage.sendError(event.getPlayer(), "You cannot use this type of potion.");
/* 435 */         event.setCancelled(true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void onInventoryOpenEvent(InventoryOpenEvent event) {
/* 442 */     if ((event.getInventory() instanceof DoubleChestInventory)) {
/* 443 */       DoubleChestInventory doubleInv = (DoubleChestInventory)event.getInventory();
/*     */       
/* 445 */       Chest leftChest = (Chest)doubleInv.getHolder().getLeftSide();
/*     */       
/* 447 */       PlayerInteractEvent interactLeft = new PlayerInteractEvent((Player)event.getPlayer(), Action.RIGHT_CLICK_BLOCK, null, leftChest.getBlock(), null);
/* 448 */       BlockListener.OnPlayerSwitchEvent(interactLeft);
/*     */       
/* 450 */       if (interactLeft.isCancelled()) {
/* 451 */         event.setCancelled(true);
/* 452 */         return;
/*     */       }
/*     */       
/* 455 */       Chest rightChest = (Chest)doubleInv.getHolder().getRightSide();
/* 456 */       PlayerInteractEvent interactRight = new PlayerInteractEvent((Player)event.getPlayer(), Action.RIGHT_CLICK_BLOCK, null, rightChest.getBlock(), null);
/* 457 */       BlockListener.OnPlayerSwitchEvent(interactRight);
/*     */       
/* 459 */       if (interactRight.isCancelled()) {
/* 460 */         event.setCancelled(true);
/* 461 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void onEntityDamageByEntityMonitor(EntityDamageByEntityEvent event) {
/* 468 */     if (event.isCancelled()) {
/*     */       return;
/*     */     }
/*     */     
/*     */     Player defender;
/*     */     
/*     */     Player defender;
/*     */     
/* 476 */     if ((event.getEntity() instanceof Player)) {
/* 477 */       defender = (Player)event.getEntity();
/*     */     } else
/* 479 */       defender = null;
/*     */     Player attacker;
/*     */     Player attacker;
/* 482 */     if ((event.getDamager() instanceof Player)) {
/* 483 */       attacker = (Player)event.getDamager(); } else { Player attacker;
/* 484 */       if ((event.getDamager() instanceof Arrow)) {
/* 485 */         Arrow arrow = (Arrow)event.getDamager();
/* 486 */         Player attacker; if ((arrow.getShooter() instanceof Player)) {
/* 487 */           attacker = (Player)arrow.getShooter();
/*     */         } else {
/* 489 */           attacker = null;
/*     */         }
/*     */       } else {
/* 492 */         attacker = null;
/*     */       }
/*     */     }
/* 495 */     if ((attacker == null) && (defender == null)) {
/* 496 */       return;
/*     */     }
/*     */     
/* 499 */     String damage = new DecimalFormat("#.#").format(event.getDamage());
/*     */     
/* 501 */     if (defender != null) {
/* 502 */       Resident defenderResident = CivGlobal.getResident(defender);
/* 503 */       if (defenderResident.isCombatInfo()) {
/* 504 */         if (attacker != null) {
/* 505 */           CivMessage.send(defender, "§7   [Combat] Took §c" + damage + 
/* 506 */             " damage " + "§7" + " from " + "§d" + attacker.getName());
/*     */         } else {
/* 508 */           String entityName = null;
/*     */           
/* 510 */           if ((event.getDamager() instanceof LivingEntity)) {
/* 511 */             entityName = ((LivingEntity)event.getDamager()).getCustomName();
/*     */           }
/*     */           
/* 514 */           if (entityName == null) {
/* 515 */             entityName = event.getDamager().getType().toString();
/*     */           }
/*     */           
/* 518 */           CivMessage.send(defender, "§7   [Combat] Took §c" + damage + 
/* 519 */             " damage " + "§7" + " from a " + entityName);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 524 */     if (attacker != null) {
/* 525 */       Resident attackerResident = CivGlobal.getResident(attacker);
/* 526 */       if (attackerResident.isCombatInfo()) {
/* 527 */         if (defender != null) {
/* 528 */           CivMessage.send(attacker, "§7   [Combat] Gave §a" + damage + "§7" + " damage to " + "§d" + defender.getName());
/*     */         } else {
/* 530 */           String entityName = null;
/*     */           
/* 532 */           if ((event.getDamager() instanceof LivingEntity)) {
/* 533 */             entityName = ((LivingEntity)event.getDamager()).getCustomName();
/*     */           }
/*     */           
/* 536 */           if (entityName == null) {
/* 537 */             entityName = event.getDamager().getType().toString();
/*     */           }
/*     */           
/* 540 */           CivMessage.send(attacker, "§7   [Combat] Gave §a" + damage + "§7" + " damage to a " + entityName);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\listener\PlayerListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*      */ package com.avrgaming.civcraft.listener;
/*      */ 
/*      */ import com.avrgaming.civcraft.cache.ArrowFiredCache;
/*      */ import com.avrgaming.civcraft.cache.CannonFiredCache;
/*      */ import com.avrgaming.civcraft.cache.CivCache;
/*      */ import com.avrgaming.civcraft.camp.Camp;
/*      */ import com.avrgaming.civcraft.camp.CampBlock;
/*      */ import com.avrgaming.civcraft.config.CivSettings;
/*      */ import com.avrgaming.civcraft.config.ConfigTempleSacrifice;
/*      */ import com.avrgaming.civcraft.exception.CivException;
/*      */ import com.avrgaming.civcraft.main.CivGlobal;
/*      */ import com.avrgaming.civcraft.main.CivLog;
/*      */ import com.avrgaming.civcraft.main.CivMessage;
/*      */ import com.avrgaming.civcraft.object.Civilization;
/*      */ import com.avrgaming.civcraft.object.ControlPoint;
/*      */ import com.avrgaming.civcraft.object.DiplomacyManager;
/*      */ import com.avrgaming.civcraft.object.ProtectedBlock;
/*      */ import com.avrgaming.civcraft.object.Resident;
/*      */ import com.avrgaming.civcraft.object.StructureBlock;
/*      */ import com.avrgaming.civcraft.object.StructureChest;
/*      */ import com.avrgaming.civcraft.object.StructureSign;
/*      */ import com.avrgaming.civcraft.object.Town;
/*      */ import com.avrgaming.civcraft.object.TownChunk;
/*      */ import com.avrgaming.civcraft.permission.PermissionNode;
/*      */ import com.avrgaming.civcraft.permission.PlotPermissions;
/*      */ import com.avrgaming.civcraft.permission.PlotPermissions.Type;
/*      */ import com.avrgaming.civcraft.road.Road;
/*      */ import com.avrgaming.civcraft.road.RoadBlock;
/*      */ import com.avrgaming.civcraft.structure.Buildable;
/*      */ import com.avrgaming.civcraft.structure.BuildableLayer;
/*      */ import com.avrgaming.civcraft.structure.CannonTower;
/*      */ import com.avrgaming.civcraft.structure.Farm;
/*      */ import com.avrgaming.civcraft.structure.Pasture;
/*      */ import com.avrgaming.civcraft.structure.Temple;
/*      */ import com.avrgaming.civcraft.structure.Wall;
/*      */ import com.avrgaming.civcraft.structure.farm.FarmChunk;
/*      */ import com.avrgaming.civcraft.threading.CivAsyncTask;
/*      */ import com.avrgaming.civcraft.threading.TaskMaster;
/*      */ import com.avrgaming.civcraft.threading.tasks.FireWorkTask;
/*      */ import com.avrgaming.civcraft.threading.tasks.StructureBlockHitEvent;
/*      */ import com.avrgaming.civcraft.util.BlockCoord;
/*      */ import com.avrgaming.civcraft.util.ChunkCoord;
/*      */ import com.avrgaming.civcraft.util.ItemFrameStorage;
/*      */ import com.avrgaming.civcraft.util.ItemManager;
/*      */ import com.avrgaming.civcraft.war.War;
/*      */ import com.avrgaming.civcraft.war.WarRegen;
/*      */ import com.avrgaming.moblib.MobLib;
/*      */ import gpl.HorseModifier;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import net.minecraft.server.v1_7_R2.NBTTagCompound;
/*      */ import org.bukkit.Chunk;
/*      */ import org.bukkit.Color;
/*      */ import org.bukkit.FireworkEffect;
/*      */ import org.bukkit.FireworkEffect.Builder;
/*      */ import org.bukkit.FireworkEffect.Type;
/*      */ import org.bukkit.Location;
/*      */ import org.bukkit.Material;
/*      */ import org.bukkit.World;
/*      */ import org.bukkit.block.Block;
/*      */ import org.bukkit.block.BlockFace;
/*      */ import org.bukkit.block.BlockState;
/*      */ import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
/*      */ import org.bukkit.entity.Arrow;
/*      */ import org.bukkit.entity.EntityType;
/*      */ import org.bukkit.entity.Fireball;
/*      */ import org.bukkit.entity.Hanging;
/*      */ import org.bukkit.entity.ItemFrame;
/*      */ import org.bukkit.entity.LivingEntity;
/*      */ import org.bukkit.entity.Painting;
/*      */ import org.bukkit.entity.Player;
/*      */ import org.bukkit.entity.Projectile;
/*      */ import org.bukkit.entity.ThrownPotion;
/*      */ import org.bukkit.event.EventHandler;
/*      */ import org.bukkit.event.EventPriority;
/*      */ import org.bukkit.event.Listener;
/*      */ import org.bukkit.event.block.Action;
/*      */ import org.bukkit.event.block.BlockBreakEvent;
/*      */ import org.bukkit.event.block.BlockBurnEvent;
/*      */ import org.bukkit.event.block.BlockDispenseEvent;
/*      */ import org.bukkit.event.block.BlockFormEvent;
/*      */ import org.bukkit.event.block.BlockFromToEvent;
/*      */ import org.bukkit.event.block.BlockGrowEvent;
/*      */ import org.bukkit.event.block.BlockIgniteEvent;
/*      */ import org.bukkit.event.block.BlockPistonExtendEvent;
/*      */ import org.bukkit.event.block.BlockPistonRetractEvent;
/*      */ import org.bukkit.event.block.BlockPlaceEvent;
/*      */ import org.bukkit.event.block.BlockRedstoneEvent;
/*      */ import org.bukkit.event.entity.CreatureSpawnEvent;
/*      */ import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
/*      */ import org.bukkit.event.entity.EntityBreakDoorEvent;
/*      */ import org.bukkit.event.entity.EntityChangeBlockEvent;
/*      */ import org.bukkit.event.entity.EntityCreatePortalEvent;
/*      */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*      */ import org.bukkit.event.entity.EntityDeathEvent;
/*      */ import org.bukkit.event.entity.EntityExplodeEvent;
/*      */ import org.bukkit.event.entity.EntityInteractEvent;
/*      */ import org.bukkit.event.entity.PotionSplashEvent;
/*      */ import org.bukkit.event.entity.ProjectileHitEvent;
/*      */ import org.bukkit.event.hanging.HangingBreakByEntityEvent;
/*      */ import org.bukkit.event.player.PlayerBedEnterEvent;
/*      */ import org.bukkit.event.player.PlayerInteractEntityEvent;
/*      */ import org.bukkit.event.player.PlayerInteractEvent;
/*      */ import org.bukkit.event.player.PlayerItemConsumeEvent;
/*      */ import org.bukkit.event.world.ChunkLoadEvent;
/*      */ import org.bukkit.event.world.ChunkUnloadEvent;
/*      */ import org.bukkit.inventory.ItemStack;
/*      */ import org.bukkit.potion.PotionEffect;
/*      */ import org.bukkit.potion.PotionEffectType;
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
/*      */ public class BlockListener
/*      */   implements Listener
/*      */ {
/*  130 */   public static ChunkCoord coord = new ChunkCoord("", 0, 0);
/*  131 */   public static BlockCoord bcoord = new BlockCoord("", 0, 0, 0);
/*      */   
/*      */ 
/*      */   @EventHandler(priority=EventPriority.NORMAL)
/*      */   public void onBlockIgniteEvent(BlockIgniteEvent event)
/*      */   {
/*  137 */     for (int x = -1; x <= 1; x++) {
/*  138 */       for (int y = -1; y <= 1; y++) {
/*  139 */         for (int z = -1; z <= 1; z++) {
/*  140 */           Block b = event.getBlock().getRelative(x, y, z);
/*  141 */           bcoord.setFromLocation(b.getLocation());
/*  142 */           StructureBlock sb = CivGlobal.getStructureBlock(bcoord);
/*  143 */           if (sb != null) {
/*  144 */             if (b.getType().isBurnable()) {
/*  145 */               event.setCancelled(true);
/*      */             }
/*  147 */             return;
/*      */           }
/*      */           
/*  150 */           RoadBlock rb = CivGlobal.getRoadBlock(bcoord);
/*  151 */           if (rb != null) {
/*  152 */             event.setCancelled(true);
/*  153 */             return;
/*      */           }
/*      */           
/*  156 */           CampBlock cb = CivGlobal.getCampBlock(bcoord);
/*  157 */           if (cb != null) {
/*  158 */             event.setCancelled(true);
/*  159 */             return;
/*      */           }
/*      */           
/*  162 */           StructureSign structSign = CivGlobal.getStructureSign(bcoord);
/*  163 */           if (structSign != null) {
/*  164 */             event.setCancelled(true);
/*  165 */             return;
/*      */           }
/*      */           
/*  168 */           StructureChest structChest = CivGlobal.getStructureChest(bcoord);
/*  169 */           if (structChest != null) {
/*  170 */             event.setCancelled(true);
/*  171 */             return;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  178 */     coord.setFromLocation(event.getBlock().getLocation());
/*  179 */     TownChunk tc = CivGlobal.getTownChunk(coord);
/*      */     
/*  181 */     if (tc == null) {
/*  182 */       return;
/*      */     }
/*      */     
/*  185 */     if (!tc.perms.isFire()) {
/*  186 */       CivMessage.sendError(event.getPlayer(), "Fire disabled in this chunk.");
/*  187 */       event.setCancelled(true);
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler(priority=EventPriority.NORMAL)
/*      */   public void onEntityBlockChange(EntityChangeBlockEvent event) {
/*  193 */     bcoord.setFromLocation(event.getBlock().getLocation());
/*      */     
/*  195 */     StructureBlock sb = CivGlobal.getStructureBlock(bcoord);
/*  196 */     if (sb != null) {
/*  197 */       event.setCancelled(true);
/*  198 */       return;
/*      */     }
/*      */     
/*  201 */     RoadBlock rb = CivGlobal.getRoadBlock(bcoord);
/*  202 */     if (rb != null) {
/*  203 */       event.setCancelled(true);
/*  204 */       return;
/*      */     }
/*      */     
/*  207 */     CampBlock cb = CivGlobal.getCampBlock(bcoord);
/*  208 */     if (cb != null) {
/*  209 */       event.setCancelled(true);
/*  210 */       return;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   @EventHandler(priority=EventPriority.LOW)
/*      */   public void onBlockBurnEvent(BlockBurnEvent event)
/*      */   {
/*  218 */     bcoord.setFromLocation(event.getBlock().getLocation());
/*      */     
/*  220 */     StructureBlock sb = CivGlobal.getStructureBlock(bcoord);
/*  221 */     if (sb != null) {
/*  222 */       event.setCancelled(true);
/*  223 */       return;
/*      */     }
/*      */     
/*  226 */     RoadBlock rb = CivGlobal.getRoadBlock(bcoord);
/*  227 */     if (rb != null) {
/*  228 */       event.setCancelled(true);
/*  229 */       return;
/*      */     }
/*      */     
/*  232 */     CampBlock cb = CivGlobal.getCampBlock(bcoord);
/*  233 */     if (cb != null) {
/*  234 */       event.setCancelled(true);
/*  235 */       return;
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler(priority=EventPriority.NORMAL)
/*      */   public void onProjectileHitEvent(ProjectileHitEvent event) {
/*  241 */     if ((event.getEntity() instanceof Arrow)) {
/*  242 */       ArrowFiredCache afc = (ArrowFiredCache)CivCache.arrowsFired.get(event.getEntity().getUniqueId());
/*  243 */       if (afc != null) {
/*  244 */         afc.setHit(true);
/*      */       }
/*      */     }
/*      */     
/*  248 */     if ((event.getEntity() instanceof Fireball)) {
/*  249 */       CannonFiredCache cfc = (CannonFiredCache)CivCache.cannonBallsFired.get(event.getEntity().getUniqueId());
/*  250 */       if (cfc != null)
/*      */       {
/*  252 */         cfc.setHit(true);
/*      */         
/*  254 */         FireworkEffect fe = FireworkEffect.builder().withColor(Color.RED).withColor(Color.BLACK).flicker(true).with(FireworkEffect.Type.BURST).build();
/*      */         
/*  256 */         Random rand = new Random();
/*  257 */         int spread = 30;
/*  258 */         for (int i = 0; i < 15; i++) {
/*  259 */           int x = rand.nextInt(spread) - spread / 2;
/*  260 */           int y = rand.nextInt(spread) - spread / 2;
/*  261 */           int z = rand.nextInt(spread) - spread / 2;
/*      */           
/*      */ 
/*  264 */           Location loc = event.getEntity().getLocation();
/*  265 */           Location location = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
/*  266 */           location.add(x, y, z);
/*      */           
/*  268 */           TaskMaster.syncTask(new FireWorkTask(fe, loc.getWorld(), loc, 5), rand.nextInt(30));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   @EventHandler(priority=EventPriority.LOWEST)
/*      */   public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
/*      */   {
/*  278 */     if ((event.getEntity() instanceof ItemFrame)) {
/*  279 */       ItemFrameStorage iFrameStorage = CivGlobal.getProtectedItemFrame(event.getEntity().getUniqueId());
/*  280 */       if (iFrameStorage != null) {
/*  281 */         event.setCancelled(true);
/*  282 */         return;
/*      */       }
/*      */     }
/*      */     
/*  286 */     if (!(event.getEntity() instanceof Player)) {
/*  287 */       return;
/*      */     }
/*      */     
/*  290 */     Player defender = (Player)event.getEntity();
/*      */     
/*  292 */     if (!CivSettings.playerEntityWeapons.contains(event.getDamager().getType())) {
/*  293 */       return;
/*      */     }
/*      */     
/*  296 */     (event.getDamager() instanceof Arrow);
/*      */     
/*      */ 
/*      */ 
/*  300 */     if ((event.getDamager() instanceof Fireball)) {
/*  301 */       CannonFiredCache cfc = (CannonFiredCache)CivCache.cannonBallsFired.get(event.getDamager().getUniqueId());
/*  302 */       if (cfc != null)
/*      */       {
/*  304 */         cfc.setHit(true);
/*  305 */         cfc.destroy(event.getDamager());
/*  306 */         event.setDamage(cfc.getFromTower().getDamage());
/*      */       }
/*      */     }
/*      */     
/*  310 */     coord.setFromLocation(event.getEntity().getLocation());
/*  311 */     TownChunk tc = CivGlobal.getTownChunk(coord);
/*  312 */     boolean allowPVP = false;
/*  313 */     String denyMessage = "";
/*      */     
/*  315 */     if (tc == null)
/*      */     {
/*  317 */       allowPVP = true;
/*      */     } else {
/*  319 */       Player attacker = null;
/*  320 */       if ((event.getDamager() instanceof Player)) {
/*  321 */         attacker = (Player)event.getDamager();
/*  322 */       } else if ((event.getDamager() instanceof Projectile)) {
/*  323 */         LivingEntity shooter = (LivingEntity)((Projectile)event.getDamager()).getShooter();
/*  324 */         if ((shooter instanceof Player)) {
/*  325 */           attacker = (Player)shooter;
/*      */         }
/*      */       }
/*      */       
/*  329 */       if (attacker == null)
/*      */       {
/*  331 */         allowPVP = true;
/*      */       } else {
/*  333 */         switch (playersCanPVPHere(attacker, defender, tc)) {
/*      */         case ALLOWED: 
/*  335 */           allowPVP = true;
/*  336 */           break;
/*      */         case NON_PVP_ZONE: 
/*  338 */           allowPVP = false;
/*  339 */           denyMessage = "You cannot PvP with " + defender.getName() + " as you are not at war.";
/*  340 */           break;
/*      */         case NOT_AT_WAR: 
/*  342 */           allowPVP = false;
/*  343 */           denyMessage = "You cannot PvP here with " + defender.getName() + " since you are a neutral in a war-zone.";
/*  344 */           break;
/*      */         case NEUTRAL_IN_WARZONE: 
/*  346 */           allowPVP = false;
/*  347 */           denyMessage = "You cannot PvP with " + defender.getName() + " since you are in a non-pvp zone.";
/*      */         }
/*      */         
/*      */       }
/*      */       
/*  352 */       if (!allowPVP) {
/*  353 */         CivMessage.sendError(attacker, denyMessage);
/*  354 */         event.setCancelled(true);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @EventHandler(priority=EventPriority.NORMAL)
/*      */   public void OnCreateSpawnEvent(CreatureSpawnEvent event)
/*      */   {
/*  366 */     if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.BREEDING)) {
/*  367 */       ChunkCoord coord = new ChunkCoord(event.getEntity().getLocation());
/*  368 */       Pasture pasture = (Pasture)Pasture.pastureChunks.get(coord);
/*      */       
/*  370 */       if (pasture != null) {
/*  371 */         pasture.onBreed(event.getEntity());
/*      */       }
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
/*  393 */     if (event.getEntityType() == EntityType.HORSE) {
/*  394 */       if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.DEFAULT)) {
/*  395 */         TaskMaster.syncTask(new Runnable()
/*      */         {
/*      */           LivingEntity entity;
/*      */           
/*      */           public void run()
/*      */           {
/*  384 */             if ((this.entity != null) && 
/*  385 */               (!HorseModifier.isCivCraftHorse(this.entity))) {
/*  386 */               CivLog.warning("Removing a normally spawned horse.");
/*  387 */               this.entity.remove();
/*      */ 
/*      */             }
/*      */             
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*  395 */         });
/*  396 */         return;
/*      */       }
/*      */       
/*  399 */       CivLog.warning("Canceling horse spawn reason:" + event.getSpawnReason().name());
/*  400 */       event.setCancelled(true);
/*      */     }
/*      */     
/*  403 */     coord.setFromLocation(event.getLocation());
/*  404 */     TownChunk tc = CivGlobal.getTownChunk(coord);
/*  405 */     if (tc == null) {
/*  406 */       return;
/*      */     }
/*      */     
/*  409 */     if (!tc.perms.isMobs()) {
/*  410 */       if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM)) {
/*  411 */         return;
/*      */       }
/*      */       
/*  414 */       if (CivSettings.restrictedSpawns.containsKey(event.getEntityType())) {
/*  415 */         event.setCancelled(true);
/*  416 */         return;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler(priority=EventPriority.NORMAL)
/*      */   public void OnEntityExplodeEvent(EntityExplodeEvent event)
/*      */   {
/*  424 */     if (event.getEntity() == null) {
/*  425 */       return;
/*      */     }
/*      */     
/*  428 */     if (event.getEntityType().equals(EntityType.COMPLEX_PART)) {
/*  429 */       event.setCancelled(true);
/*  430 */     } else if (event.getEntityType().equals(EntityType.ENDER_DRAGON)) {
/*  431 */       event.setCancelled(true);
/*      */     }
/*      */     
/*  434 */     for (Block block : event.blockList()) {
/*  435 */       bcoord.setFromLocation(block.getLocation());
/*  436 */       StructureBlock sb = CivGlobal.getStructureBlock(bcoord);
/*  437 */       if (sb != null) {
/*  438 */         event.setCancelled(true);
/*  439 */         return;
/*      */       }
/*      */       
/*  442 */       RoadBlock rb = CivGlobal.getRoadBlock(bcoord);
/*  443 */       if (rb != null) {
/*  444 */         event.setCancelled(true);
/*  445 */         return;
/*      */       }
/*      */       
/*  448 */       CampBlock cb = CivGlobal.getCampBlock(bcoord);
/*  449 */       if (cb != null) {
/*  450 */         event.setCancelled(true);
/*  451 */         return;
/*      */       }
/*      */       
/*  454 */       StructureSign structSign = CivGlobal.getStructureSign(bcoord);
/*  455 */       if (structSign != null) {
/*  456 */         event.setCancelled(true);
/*  457 */         return;
/*      */       }
/*      */       
/*  460 */       StructureChest structChest = CivGlobal.getStructureChest(bcoord);
/*  461 */       if (structChest != null) {
/*  462 */         event.setCancelled(true);
/*  463 */         return;
/*      */       }
/*      */       
/*  466 */       coord.setFromLocation(block.getLocation());
/*      */       
/*  468 */       HashSet<Wall> walls = CivGlobal.getWallChunk(coord);
/*  469 */       if (walls != null) {
/*  470 */         for (Wall wall : walls) {
/*  471 */           if (wall.isProtectedLocation(block.getLocation())) {
/*  472 */             event.setCancelled(true);
/*  473 */             return;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  478 */       TownChunk tc = CivGlobal.getTownChunk(coord);
/*  479 */       if (tc != null)
/*      */       {
/*      */ 
/*      */ 
/*  483 */         event.setCancelled(true);
/*  484 */         return;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*  489 */   private final BlockFace[] faces = {
/*  490 */     BlockFace.DOWN, 
/*  491 */     BlockFace.NORTH, 
/*  492 */     BlockFace.EAST, 
/*  493 */     BlockFace.SOUTH, 
/*  494 */     BlockFace.WEST, 
/*  495 */     BlockFace.SELF, 
/*  496 */     BlockFace.UP };
/*      */   
/*      */ 
/*      */   public BlockCoord generatesCobble(int id, Block b)
/*      */   {
/*  501 */     int mirrorID1 = (id == 8) || (id == 9) ? 10 : 8;
/*  502 */     int mirrorID2 = (id == 8) || (id == 9) ? 11 : 9;
/*  503 */     BlockFace[] arrayOfBlockFace; int j = (arrayOfBlockFace = this.faces).length; for (int i = 0; i < j; i++) { BlockFace face = arrayOfBlockFace[i];
/*      */       
/*  505 */       Block r = b.getRelative(face, 1);
/*  506 */       if ((ItemManager.getId(r) == mirrorID1) || (ItemManager.getId(r) == mirrorID2))
/*      */       {
/*      */ 
/*  509 */         return new BlockCoord(r);
/*      */       }
/*      */     }
/*      */     
/*  513 */     return null;
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
/*  539 */   private static HashSet<BlockCoord> stopCobbleTasks = new HashSet();
/*      */   
/*      */   @EventHandler(priority=EventPriority.NORMAL)
/*      */   public void OnBlockFromToEvent(BlockFromToEvent event) {
/*  543 */     int id = ItemManager.getId(event.getBlock());
/*  544 */     if ((id >= 9) && (id <= 11))
/*      */     {
/*  546 */       Block b = event.getToBlock();
/*  547 */       bcoord.setFromLocation(b.getLocation());
/*      */       
/*  549 */       int toid = ItemManager.getId(b);
/*  550 */       if (toid == 0)
/*      */       {
/*  552 */         BlockCoord other = generatesCobble(id, b);
/*  553 */         if (other != null)
/*      */         {
/*      */ 
/*      */ 
/*  557 */           event.setCancelled(true);
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
/*  573 */           if (!stopCobbleTasks.contains(other)) {
/*  574 */             stopCobbleTasks.add(other);
/*  575 */             TaskMaster.syncTask(new Runnable()
/*      */             {
/*      */               BlockCoord block;
/*      */               
/*      */               public void run()
/*      */               {
/*  568 */                 ItemManager.setTypeIdAndData(this.block.getBlock(), 87, 0, true);
/*  569 */                 BlockListener.stopCobbleTasks.remove(this.block);
/*      */ 
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*  575 */             }, 2L);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @EventHandler(priority=EventPriority.NORMAL)
/*      */   public void OnBlockFormEvent(BlockFormEvent event)
/*      */   {
/*  591 */     if (ItemManager.getId(event.getNewState()) == 4) {
/*  592 */       ItemManager.setTypeId(event.getNewState(), 13);
/*  593 */       return;
/*      */     }
/*      */     
/*  596 */     Chunk spreadChunk = event.getNewState().getChunk();
/*  597 */     coord.setX(spreadChunk.getX());
/*  598 */     coord.setZ(spreadChunk.getZ());
/*  599 */     coord.setWorldname(spreadChunk.getWorld().getName());
/*      */     
/*  601 */     TownChunk tc = CivGlobal.getTownChunk(coord);
/*  602 */     if (tc == null) {
/*  603 */       return;
/*      */     }
/*      */     
/*  606 */     if ((!tc.perms.isFire()) && 
/*  607 */       (event.getNewState().getType() == Material.FIRE)) {
/*  608 */       event.setCancelled(true);
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler(priority=EventPriority.LOW)
/*      */   public void OnBlockPlaceEvent(BlockPlaceEvent event)
/*      */   {
/*  615 */     Resident resident = CivGlobal.getResident(event.getPlayer());
/*      */     
/*  617 */     if (resident == null) {
/*  618 */       event.setCancelled(true);
/*  619 */       return;
/*      */     }
/*      */     
/*  622 */     if (resident.isSBPermOverride()) {
/*  623 */       return;
/*      */     }
/*      */     
/*  626 */     bcoord.setFromLocation(event.getBlockAgainst().getLocation());
/*  627 */     StructureSign sign = CivGlobal.getStructureSign(bcoord);
/*  628 */     if (sign != null) {
/*  629 */       event.setCancelled(true);
/*  630 */       return;
/*      */     }
/*      */     
/*  633 */     bcoord.setFromLocation(event.getBlock().getLocation());
/*  634 */     StructureBlock sb = CivGlobal.getStructureBlock(bcoord);
/*  635 */     if (sb != null) {
/*  636 */       event.setCancelled(true);
/*  637 */       CivMessage.sendError(event.getPlayer(), 
/*  638 */         "This block belongs to a " + sb.getOwner().getDisplayName() + " and cannot be destroyed.");
/*  639 */       return;
/*      */     }
/*      */     
/*  642 */     RoadBlock rb = CivGlobal.getRoadBlock(bcoord);
/*  643 */     if (rb != null) {
/*  644 */       if ((rb.isAboveRoadBlock()) && 
/*  645 */         (resident.getCiv() != rb.getRoad().getCiv())) {
/*  646 */         event.setCancelled(true);
/*  647 */         CivMessage.sendError(event.getPlayer(), 
/*  648 */           "Cannot place blocks 3 blocks above a road that does not belong to your civ.");
/*      */       }
/*      */       
/*  651 */       return;
/*      */     }
/*      */     
/*  654 */     CampBlock cb = CivGlobal.getCampBlock(bcoord);
/*  655 */     if ((cb != null) && (!cb.canBreak(event.getPlayer().getName()))) {
/*  656 */       event.setCancelled(true);
/*  657 */       CivMessage.sendError(event.getPlayer(), "This block is part of camp " + cb.getCamp().getName() + " owned by " + cb.getCamp().getOwner().getName() + " and cannot be destroyed.");
/*  658 */       return;
/*      */     }
/*      */     
/*  661 */     coord.setFromLocation(event.getBlock().getLocation());
/*  662 */     TownChunk tc = CivGlobal.getTownChunk(coord);
/*  663 */     if (CivSettings.blockPlaceExceptions.get(event.getBlock().getType()) != null) {
/*  664 */       return;
/*      */     }
/*      */     
/*  667 */     if ((tc != null) && 
/*  668 */       (!tc.perms.hasPermission(PlotPermissions.Type.BUILD, resident))) {
/*  669 */       if ((War.isWarTime()) && (resident.hasTown()) && 
/*  670 */         (resident.getTown().getCiv().getDiplomacyManager().atWarWith(tc.getTown().getCiv()))) {
/*  671 */         if (WarRegen.canPlaceThisBlock(event.getBlock())) {
/*  672 */           WarRegen.saveBlock(event.getBlock(), tc.getTown().getName(), true);
/*  673 */           return;
/*      */         }
/*  675 */         CivMessage.sendErrorNoRepeat(event.getPlayer(), "Cannot place this type of block in an emeny town during WarTime.");
/*  676 */         event.setCancelled(true);
/*  677 */         return;
/*      */       }
/*      */       
/*  680 */       event.setCancelled(true);
/*  681 */       CivMessage.sendError(event.getPlayer(), "You do not have permission to build here.");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  688 */     HashSet<Buildable> buildables = CivGlobal.getBuildablesAt(bcoord);
/*  689 */     if (buildables != null)
/*  690 */       for (Buildable buildable : buildables)
/*  691 */         if (!buildable.validated) {
/*      */           try {
/*  693 */             buildable.validate(event.getPlayer());
/*      */           } catch (CivException e) {
/*  695 */             e.printStackTrace();
/*      */           }
/*      */           
/*      */         }
/*      */         else
/*      */         {
/*  701 */           BuildableLayer layer = (BuildableLayer)buildable.layerValidPercentages.get(Integer.valueOf(bcoord.getY()));
/*  702 */           if (layer != null)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*  707 */             layer.current += Buildable.getReinforcementValue(ItemManager.getId(event.getBlockPlaced()));
/*  708 */             if (layer.current < 0) {
/*  709 */               layer.current = 0;
/*      */             }
/*  711 */             buildable.layerValidPercentages.put(Integer.valueOf(bcoord.getY()), layer);
/*      */           }
/*      */         }
/*      */   }
/*      */   
/*      */   @EventHandler(priority=EventPriority.NORMAL)
/*      */   public void OnBlockBreakEvent(BlockBreakEvent event) {
/*  718 */     Resident resident = CivGlobal.getResident(event.getPlayer());
/*      */     
/*  720 */     if (resident == null) {
/*  721 */       event.setCancelled(true);
/*  722 */       return;
/*      */     }
/*      */     
/*  725 */     if (resident.isSBPermOverride()) {
/*  726 */       return;
/*      */     }
/*      */     
/*  729 */     bcoord.setFromLocation(event.getBlock().getLocation());
/*  730 */     StructureBlock sb = CivGlobal.getStructureBlock(bcoord);
/*      */     
/*  732 */     if (sb != null) {
/*  733 */       event.setCancelled(true);
/*  734 */       TaskMaster.syncTask(new StructureBlockHitEvent(event.getPlayer().getName(), bcoord, sb, event.getBlock().getWorld()), 0L);
/*  735 */       return;
/*      */     }
/*      */     
/*  738 */     RoadBlock rb = CivGlobal.getRoadBlock(bcoord);
/*  739 */     if ((rb != null) && (!rb.isAboveRoadBlock())) {
/*  740 */       if (War.isWarTime())
/*      */       {
/*  742 */         WarRegen.destroyThisBlock(event.getBlock(), rb.getTown());
/*  743 */         event.setCancelled(true);
/*  744 */         return;
/*      */       }
/*  746 */       event.setCancelled(true);
/*  747 */       rb.onHit(event.getPlayer());
/*  748 */       return;
/*      */     }
/*      */     
/*      */ 
/*  752 */     ProtectedBlock pb = CivGlobal.getProtectedBlock(bcoord);
/*  753 */     if (pb != null) {
/*  754 */       event.setCancelled(true);
/*  755 */       CivMessage.sendError(event.getPlayer(), "This block is protected and cannot be destroyed.");
/*  756 */       return;
/*      */     }
/*      */     
/*  759 */     CampBlock cb = CivGlobal.getCampBlock(bcoord);
/*  760 */     if ((cb != null) && (!cb.canBreak(event.getPlayer().getName()))) {
/*  761 */       ControlPoint cBlock = (ControlPoint)cb.getCamp().controlBlocks.get(bcoord);
/*  762 */       if (cBlock != null) {
/*  763 */         cb.getCamp().onDamage(1, event.getBlock().getWorld(), event.getPlayer(), bcoord, null);
/*  764 */         event.setCancelled(true);
/*  765 */         return;
/*      */       }
/*  767 */       event.setCancelled(true);
/*  768 */       CivMessage.sendError(event.getPlayer(), "This block is part of camp " + cb.getCamp().getName() + " owned by " + cb.getCamp().getOwner().getName() + " and cannot be destroyed.");
/*  769 */       return;
/*      */     }
/*      */     
/*      */ 
/*  773 */     StructureSign structSign = CivGlobal.getStructureSign(bcoord);
/*  774 */     if ((structSign != null) && (!resident.isSBPermOverride())) {
/*  775 */       event.setCancelled(true);
/*  776 */       CivMessage.sendError(event.getPlayer(), "Please do not destroy signs.");
/*  777 */       return;
/*      */     }
/*      */     
/*  780 */     StructureChest structChest = CivGlobal.getStructureChest(bcoord);
/*  781 */     if ((structChest != null) && (!resident.isSBPermOverride())) {
/*  782 */       event.setCancelled(true);
/*  783 */       CivMessage.sendError(event.getPlayer(), "Please do not destroy chests.");
/*  784 */       return;
/*      */     }
/*      */     
/*  787 */     coord.setFromLocation(event.getBlock().getLocation());
/*  788 */     HashSet<Wall> walls = CivGlobal.getWallChunk(coord);
/*      */     
/*  790 */     if (walls != null) {
/*  791 */       for (Wall wall : walls) {
/*  792 */         if (wall.isProtectedLocation(event.getBlock().getLocation())) {
/*  793 */           if ((resident == null) || (!resident.hasTown()) || ((resident.getTown().getCiv() != wall.getTown().getCiv()) && (!resident.isSBPermOverride())))
/*      */           {
/*  795 */             StructureBlock tmpStructureBlock = new StructureBlock(bcoord, wall);
/*  796 */             tmpStructureBlock.setAlwaysDamage(true);
/*  797 */             TaskMaster.syncTask(new StructureBlockHitEvent(event.getPlayer().getName(), bcoord, tmpStructureBlock, event.getBlock().getWorld()), 0L);
/*      */             
/*  799 */             event.setCancelled(true);
/*  800 */             return;
/*      */           }
/*  802 */           CivMessage.send(event.getPlayer(), "§7We destroyed a block protected by a wall. This was allowed because we're a member of " + 
/*  803 */             resident.getTown().getCiv().getName());
/*  804 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  810 */     TownChunk tc = CivGlobal.getTownChunk(coord);
/*  811 */     if ((tc != null) && 
/*  812 */       (!tc.perms.hasPermission(PlotPermissions.Type.DESTROY, resident))) {
/*  813 */       event.setCancelled(true);
/*      */       
/*  815 */       if ((War.isWarTime()) && (resident.hasTown()) && 
/*  816 */         (resident.getTown().getCiv().getDiplomacyManager().atWarWith(tc.getTown().getCiv()))) {
/*  817 */         WarRegen.destroyThisBlock(event.getBlock(), tc.getTown());
/*      */       } else {
/*  819 */         CivMessage.sendErrorNoRepeat(event.getPlayer(), "You do not have permission to destroy here.");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  826 */     Object buildables = CivGlobal.getBuildablesAt(bcoord);
/*  827 */     if (buildables != null) {
/*  828 */       for (Buildable buildable : (HashSet)buildables)
/*  829 */         if (!buildable.validated) {
/*      */           try {
/*  831 */             buildable.validate(event.getPlayer());
/*      */           } catch (CivException e) {
/*  833 */             e.printStackTrace();
/*      */           }
/*      */           
/*      */         }
/*      */         else
/*      */         {
/*  839 */           BuildableLayer layer = (BuildableLayer)buildable.layerValidPercentages.get(Integer.valueOf(bcoord.getY()));
/*  840 */           if (layer != null)
/*      */           {
/*      */ 
/*      */ 
/*  844 */             double current = layer.current - Buildable.getReinforcementValue(ItemManager.getId(event.getBlock()));
/*  845 */             if (current < 0.0D) {
/*  846 */               current = 0.0D;
/*      */             }
/*  848 */             Double percentValid = Double.valueOf(current / layer.max);
/*      */             
/*  850 */             if (percentValid.doubleValue() < Buildable.validPercentRequirement) {
/*  851 */               CivMessage.sendError(event.getPlayer(), "Cannot break this block since it's supporting the " + buildable.getDisplayName() + " above it.");
/*  852 */               event.setCancelled(true);
/*  853 */               return;
/*      */             }
/*      */             
/*      */ 
/*  857 */             layer.current = ((int)current);
/*  858 */             buildable.layerValidPercentages.put(Integer.valueOf(bcoord.getY()), layer);
/*      */           }
/*      */         }
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler(priority=EventPriority.NORMAL)
/*      */   public void OnEntityInteractEvent(EntityInteractEvent event) {
/*  866 */     if ((event.getBlock() != null) && 
/*  867 */       (CivSettings.switchItems.contains(event.getBlock().getType()))) {
/*  868 */       coord.setFromLocation(event.getBlock().getLocation());
/*  869 */       TownChunk tc = CivGlobal.getTownChunk(coord);
/*      */       
/*  871 */       if (tc == null) {
/*  872 */         return;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  878 */       if (tc.perms.interact.isPermitOthers()) {
/*  879 */         return;
/*      */       }
/*      */       
/*  882 */       if ((event.getEntity() instanceof Player)) {
/*  883 */         CivMessage.sendErrorNoRepeat((Player)event.getEntity(), "You do not have permission to interact here...");
/*      */       }
/*      */       
/*  886 */       event.setCancelled(true);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   @EventHandler(priority=EventPriority.HIGHEST)
/*      */   public void OnPlayerConsumeEvent(PlayerItemConsumeEvent event)
/*      */   {
/*  894 */     ItemStack stack = event.getItem();
/*      */     
/*      */ 
/*  897 */     if ((ItemManager.getId(event.getItem()) == ItemManager.getId(Material.GOLDEN_APPLE)) && 
/*  898 */       (event.getItem().getDurability() == 1)) {
/*  899 */       CivMessage.sendError(event.getPlayer(), "You cannot use notch apples. Sorry.");
/*  900 */       event.setCancelled(true);
/*  901 */       return;
/*      */     }
/*      */     
/*      */ 
/*  905 */     if (stack.getType().equals(Material.POTION)) {
/*  906 */       int effect = event.getItem().getDurability() & 0xF;
/*  907 */       if (effect == 14) {
/*  908 */         event.setCancelled(true);
/*  909 */         CivMessage.sendError(event.getPlayer(), "You cannot use invisibility potions for now... Sorry.");
/*  910 */         return;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler(priority=EventPriority.NORMAL)
/*      */   public void onBlockDispenseEvent(BlockDispenseEvent event) {
/*  917 */     ItemStack stack = event.getItem();
/*  918 */     if (stack != null) {
/*  919 */       if (event.getItem().getType().equals(Material.POTION)) {
/*  920 */         int effect = event.getItem().getDurability() & 0xF;
/*  921 */         if (effect == 14) {
/*  922 */           event.setCancelled(true);
/*  923 */           return;
/*      */         }
/*      */       }
/*      */       
/*  927 */       if (event.getItem().getType().equals(Material.INK_SACK))
/*      */       {
/*  929 */         event.setCancelled(true);
/*  930 */         return;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler(priority=EventPriority.NORMAL)
/*      */   public void OnPlayerInteractEvent(PlayerInteractEvent event)
/*      */   {
/*  938 */     Resident resident = CivGlobal.getResident(event.getPlayer());
/*      */     
/*  940 */     if (resident == null) {
/*  941 */       event.setCancelled(true);
/*  942 */       return;
/*      */     }
/*      */     
/*  945 */     if (event.isCancelled())
/*      */     {
/*  947 */       if (event.getAction() == Action.RIGHT_CLICK_AIR) {
/*  948 */         Integer item = Integer.valueOf(ItemManager.getId(event.getPlayer().getItemInHand()));
/*      */         
/*  950 */         if ((item.intValue() == 326) || (item.intValue() == 327) || (item.intValue() == 259) || ((item.intValue() >= 8) && (item.intValue() <= 11)) || (item.intValue() == 51)) {
/*  951 */           event.setCancelled(true);
/*      */         }
/*      */       }
/*  954 */       return;
/*      */     }
/*      */     
/*  957 */     if (event.hasItem())
/*      */     {
/*  959 */       if (event.getItem().getType().equals(Material.POTION)) {
/*  960 */         int effect = event.getItem().getDurability() & 0xF;
/*  961 */         if (effect == 14) {
/*  962 */           event.setCancelled(true);
/*  963 */           CivMessage.sendError(event.getPlayer(), "You cannot use invisibility potions for now.. Sorry.");
/*  964 */           return;
/*      */         }
/*      */       }
/*      */       
/*  968 */       if ((event.getItem().getType().equals(Material.INK_SACK)) && (event.getItem().getDurability() == 15)) {
/*  969 */         Block clickedBlock = event.getClickedBlock();
/*  970 */         if ((ItemManager.getId(clickedBlock) == 59) || 
/*  971 */           (ItemManager.getId(clickedBlock) == 141) || 
/*  972 */           (ItemManager.getId(clickedBlock) == 142)) {
/*  973 */           event.setCancelled(true);
/*  974 */           CivMessage.sendError(event.getPlayer(), "You cannot use bone meal on carrots, wheat, or potatoes.");
/*  975 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  980 */     Block soilBlock = event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
/*      */     
/*      */ 
/*  983 */     if ((event.getAction() == Action.PHYSICAL) && (
/*  984 */       (soilBlock.getType() == Material.SOIL) || (soilBlock.getType() == Material.CROPS)))
/*      */     {
/*  986 */       event.setCancelled(true);
/*  987 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  995 */     boolean leftClick = (event.getAction().equals(Action.LEFT_CLICK_AIR)) || (event.getAction().equals(Action.LEFT_CLICK_BLOCK));
/*      */     
/*  997 */     if (event.getClickedBlock() != null)
/*      */     {
/*  999 */       if (MarkerPlacementManager.isPlayerInPlacementMode(event.getPlayer())) { Block block;
/*      */         Block block;
/* 1001 */         if (event.getBlockFace().equals(BlockFace.UP)) {
/* 1002 */           block = event.getClickedBlock().getRelative(event.getBlockFace());
/*      */         } else {
/* 1004 */           block = event.getClickedBlock();
/*      */         }
/*      */         try
/*      */         {
/* 1008 */           MarkerPlacementManager.setMarker(event.getPlayer(), block.getLocation());
/* 1009 */           CivMessage.send(event.getPlayer(), "§aMarked Location.");
/*      */         } catch (CivException e) {
/* 1011 */           CivMessage.send(event.getPlayer(), "§c" + e.getMessage());
/*      */         }
/*      */         
/* 1014 */         event.setCancelled(true);
/* 1015 */         return;
/*      */       }
/*      */       
/*      */ 
/* 1019 */       bcoord.setFromLocation(event.getClickedBlock().getLocation());
/* 1020 */       StructureSign sign = CivGlobal.getStructureSign(bcoord);
/* 1021 */       if (sign != null) {
/* 1022 */         if (((leftClick) || (sign.isAllowRightClick())) && 
/* 1023 */           (sign.getOwner() != null) && (sign.getOwner().isActive())) {
/*      */           try {
/* 1025 */             sign.getOwner().processSignAction(event.getPlayer(), sign, event);
/* 1026 */             event.setCancelled(true);
/*      */           } catch (CivException e) {
/* 1028 */             CivMessage.send(event.getPlayer(), "§c" + e.getMessage());
/* 1029 */             event.setCancelled(true);
/* 1030 */             return;
/*      */           }
/*      */         }
/*      */         
/* 1034 */         return;
/*      */       }
/*      */       
/* 1037 */       if (CivSettings.switchItems.contains(event.getClickedBlock().getType())) {
/* 1038 */         OnPlayerSwitchEvent(event);
/* 1039 */         if (event.isCancelled()) {
/* 1040 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1045 */     if (event.hasItem())
/*      */     {
/* 1047 */       if (event.getItem() != null)
/*      */       {
/* 1049 */         if (CivSettings.restrictedItems.containsKey(event.getItem().getType())) {
/* 1050 */           OnPlayerUseItem(event);
/* 1051 */           if (event.isCancelled()) {}
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void OnPlayerBedEnterEvent(PlayerBedEnterEvent event)
/*      */   {
/* 1062 */     Resident resident = CivGlobal.getResident(event.getPlayer().getName());
/*      */     
/* 1064 */     if (resident == null) {
/* 1065 */       event.setCancelled(true);
/* 1066 */       return;
/*      */     }
/*      */     
/* 1069 */     coord.setFromLocation(event.getPlayer().getLocation());
/* 1070 */     Camp camp = CivGlobal.getCampFromChunk(coord);
/* 1071 */     if ((camp != null) && 
/* 1072 */       (!camp.hasMember(event.getPlayer().getName()))) {
/* 1073 */       CivMessage.sendError(event.getPlayer(), "You cannot sleep in a camp you do not belong to.");
/* 1074 */       event.setCancelled(true);
/* 1075 */       return;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static void OnPlayerSwitchEvent(PlayerInteractEvent event)
/*      */   {
/* 1082 */     if (event.getClickedBlock() == null) {
/* 1083 */       return;
/*      */     }
/*      */     
/* 1086 */     Resident resident = CivGlobal.getResident(event.getPlayer().getName());
/*      */     
/* 1088 */     if (resident == null) {
/* 1089 */       event.setCancelled(true);
/* 1090 */       return;
/*      */     }
/*      */     
/* 1093 */     bcoord.setFromLocation(event.getClickedBlock().getLocation());
/* 1094 */     CampBlock cb = CivGlobal.getCampBlock(bcoord);
/* 1095 */     if ((cb != null) && (!resident.isPermOverride()) && 
/* 1096 */       (!cb.getCamp().hasMember(resident.getName()))) {
/* 1097 */       CivMessage.sendError(event.getPlayer(), "You cannot interact with a camp you do not belong to.");
/* 1098 */       event.setCancelled(true);
/* 1099 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1103 */     coord.setFromLocation(event.getClickedBlock().getLocation());
/* 1104 */     TownChunk tc = CivGlobal.getTownChunk(coord);
/*      */     
/* 1106 */     if (tc == null) {
/* 1107 */       return;
/*      */     }
/*      */     
/* 1110 */     if ((resident.hasTown()) && 
/* 1111 */       (War.isWarTime()) && 
/* 1112 */       (tc.getTown().getCiv().getDiplomacyManager().atWarWith(resident.getTown().getCiv())))
/*      */     {
/* 1114 */       switch (event.getClickedBlock().getType()) {
/*      */       case DETECTOR_RAIL: 
/*      */       case RECORD_3: 
/* 1117 */         return;
/*      */       }
/*      */       
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1125 */     event.getClickedBlock().getType();
/*      */     
/* 1127 */     if (!tc.perms.hasPermission(PlotPermissions.Type.INTERACT, resident)) {
/* 1128 */       event.setCancelled(true);
/*      */       
/* 1130 */       if ((War.isWarTime()) && (resident.hasTown()) && 
/* 1131 */         (resident.getTown().getCiv().getDiplomacyManager().atWarWith(tc.getTown().getCiv()))) {
/* 1132 */         WarRegen.destroyThisBlock(event.getClickedBlock(), tc.getTown());
/*      */       } else {
/* 1134 */         CivMessage.sendErrorNoRepeat(event.getPlayer(), "You do not have permission to interact with " + event.getClickedBlock().getType().toString() + " here.");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void OnPlayerUseItem(PlayerInteractEvent event)
/*      */   {
/* 1142 */     Location loc = event.getClickedBlock() == null ? 
/* 1143 */       event.getPlayer().getLocation() : 
/* 1144 */       event.getClickedBlock().getLocation();
/*      */     
/* 1146 */     ItemStack stack = event.getItem();
/*      */     
/* 1148 */     coord.setFromLocation(event.getPlayer().getLocation());
/* 1149 */     Camp camp = CivGlobal.getCampFromChunk(coord);
/* 1150 */     if ((camp != null) && 
/* 1151 */       (!camp.hasMember(event.getPlayer().getName()))) {
/* 1152 */       CivMessage.sendError(event.getPlayer(), "You cannot use " + stack.getType().toString() + " in a camp you do not belong to.");
/* 1153 */       event.setCancelled(true);
/* 1154 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1158 */     TownChunk tc = CivGlobal.getTownChunk(loc);
/* 1159 */     if (tc == null) {
/* 1160 */       return;
/*      */     }
/*      */     
/* 1163 */     Resident resident = CivGlobal.getResident(event.getPlayer().getName());
/*      */     
/* 1165 */     if (resident == null) {
/* 1166 */       event.setCancelled(true);
/*      */     }
/*      */     
/* 1169 */     if (!tc.perms.hasPermission(PlotPermissions.Type.ITEMUSE, resident)) {
/* 1170 */       event.setCancelled(true);
/* 1171 */       CivMessage.sendErrorNoRepeat(event.getPlayer(), "You do not have permission to use " + stack.getType().toString() + " here.");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @EventHandler(priority=EventPriority.HIGHEST)
/*      */   public void OnPlayerInteractEntityEvent(PlayerInteractEntityEvent event)
/*      */   {
/* 1183 */     if ((event.getRightClicked().getType().equals(EntityType.HORSE)) && 
/* 1184 */       (!HorseModifier.isCivCraftHorse((LivingEntity)event.getRightClicked()))) {
/* 1185 */       CivMessage.sendError(event.getPlayer(), "Invalid horse! You can only get horses from stable structures.");
/* 1186 */       event.setCancelled(true);
/* 1187 */       event.getRightClicked().remove();
/* 1188 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1192 */     ItemStack inHand = event.getPlayer().getItemInHand();
/* 1193 */     if (inHand != null)
/*      */     {
/* 1195 */       boolean denyBreeding = false;
/* 1196 */       switch (event.getRightClicked().getType()) {
/*      */       case PRIMED_TNT: 
/*      */       case SHEEP: 
/*      */       case SMALL_FIREBALL: 
/* 1200 */         if (inHand.getType().equals(Material.WHEAT)) {
/* 1201 */           denyBreeding = true;
/*      */         }
/* 1203 */         break;
/*      */       case PLAYER: 
/* 1205 */         if (inHand.getType().equals(Material.CARROT_ITEM)) {
/* 1206 */           denyBreeding = true;
/*      */         }
/* 1208 */         break;
/*      */       case SPLASH_POTION: 
/* 1210 */         if ((inHand.getType().equals(Material.GOLDEN_APPLE)) || 
/* 1211 */           (inHand.getType().equals(Material.GOLDEN_CARROT))) {
/* 1212 */           CivMessage.sendError(event.getPlayer(), "You cannot breed horses, buy them from the stable.");
/* 1213 */           event.setCancelled(true); return;
/*      */         }
/*      */         
/*      */         break;
/*      */       case SILVERFISH: 
/* 1218 */         if ((inHand.getType().equals(Material.SEEDS)) || 
/* 1219 */           (inHand.getType().equals(Material.MELON_SEEDS)) || 
/* 1220 */           (inHand.getType().equals(Material.PUMPKIN_SEEDS))) {
/* 1221 */           denyBreeding = true;
/*      */         }
/* 1223 */         break;
/*      */       }
/*      */       
/*      */       
/*      */ 
/* 1228 */       if (denyBreeding) {
/* 1229 */         ChunkCoord coord = new ChunkCoord(event.getPlayer().getLocation());
/* 1230 */         Pasture pasture = (Pasture)Pasture.pastureChunks.get(coord);
/*      */         
/* 1232 */         if (pasture == null) {
/* 1233 */           CivMessage.sendError(event.getPlayer(), "You cannot breed mobs in the wild, take them to a pasture.");
/* 1234 */           event.setCancelled(true);
/*      */         }
/*      */         else {
/* 1237 */           NBTTagCompound tag = new NBTTagCompound();
/* 1238 */           ((CraftEntity)event.getRightClicked()).getHandle().c(tag);
/* 1239 */           int loveTicks = tag.getInt("InLove");
/*      */           
/* 1241 */           if (loveTicks == 0) {
/* 1242 */             if (!pasture.processMobBreed(event.getPlayer(), event.getRightClicked().getType())) {
/* 1243 */               event.setCancelled(true);
/*      */             }
/*      */           } else {
/* 1246 */             event.setCancelled(true);
/*      */           }
/*      */         }
/*      */         
/* 1250 */         return;
/*      */       }
/*      */     }
/* 1253 */     if ((!(event.getRightClicked() instanceof ItemFrame)) && (!(event.getRightClicked() instanceof Painting))) {
/* 1254 */       return;
/*      */     }
/*      */     
/* 1257 */     coord.setFromLocation(event.getPlayer().getLocation());
/* 1258 */     TownChunk tc = CivGlobal.getTownChunk(coord);
/* 1259 */     if (tc == null) {
/* 1260 */       return;
/*      */     }
/*      */     
/* 1263 */     Resident resident = CivGlobal.getResident(event.getPlayer().getName());
/* 1264 */     if (resident == null) {
/* 1265 */       return;
/*      */     }
/*      */     
/* 1268 */     if (!tc.perms.hasPermission(PlotPermissions.Type.INTERACT, resident)) {
/* 1269 */       event.setCancelled(true);
/* 1270 */       CivMessage.sendErrorNoRepeat(event.getPlayer(), "You do not have permission to interact with this painting/itemframe.");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @EventHandler(priority=EventPriority.NORMAL)
/*      */   public void OnHangingBreakByEntityEvent(HangingBreakByEntityEvent event)
/*      */   {
/* 1283 */     ItemFrameStorage frameStore = CivGlobal.getProtectedItemFrame(event.getEntity().getUniqueId());
/* 1284 */     if (frameStore != null)
/*      */     {
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
/* 1315 */       if ((event.getRemover() instanceof Player)) {
/* 1316 */         CivMessage.sendError((Player)event.getRemover(), "Cannot break protected item frames. Right click to interact instead.");
/*      */       }
/* 1318 */       event.setCancelled(true);
/* 1319 */       return;
/*      */     }
/*      */     
/* 1322 */     if ((event.getRemover() instanceof Player)) {
/* 1323 */       Player player = (Player)event.getRemover();
/*      */       
/* 1325 */       coord.setFromLocation(player.getLocation());
/* 1326 */       TownChunk tc = CivGlobal.getTownChunk(coord);
/*      */       
/* 1328 */       if (tc == null) {
/* 1329 */         return;
/*      */       }
/*      */       
/* 1332 */       Resident resident = CivGlobal.getResident(player.getName());
/* 1333 */       if (resident == null) {
/* 1334 */         event.setCancelled(true);
/*      */       }
/*      */       
/* 1337 */       if (!tc.perms.hasPermission(PlotPermissions.Type.DESTROY, resident)) {
/* 1338 */         event.setCancelled(true);
/* 1339 */         CivMessage.sendErrorNoRepeat(player, "You do not have permission to destroy here.");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   @EventHandler(priority=EventPriority.HIGH)
/*      */   public void onChunkUnloadEvent(ChunkUnloadEvent event)
/*      */   {
/* 1348 */     Boolean persist = CivGlobal.isPersistChunk(event.getChunk());
/* 1349 */     if ((persist != null) && (persist.booleanValue())) {
/* 1350 */       event.setCancelled(true);
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler(priority=EventPriority.NORMAL)
/*      */   public void onChunkLoadEvent(ChunkLoadEvent event) {
/* 1356 */     ChunkCoord coord = new ChunkCoord(event.getChunk());
/* 1357 */     FarmChunk fc = CivGlobal.getFarmChunk(coord);
/* 1358 */     if (fc == null) {
/*      */       return;
/*      */     }
/*      */     org.bukkit.entity.Entity[] arrayOfEntity;
/* 1362 */     int j = (arrayOfEntity = event.getChunk().getEntities()).length; for (int i = 0; i < j; i++) { org.bukkit.entity.Entity ent = arrayOfEntity[i];
/* 1363 */       if (ent.getType().equals(EntityType.ZOMBIE)) {
/* 1364 */         ent.remove();
/*      */       }
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
/* 1385 */     TaskMaster.syncTask(new CivAsyncTask()
/*      */     {
/*      */       FarmChunk fc;
/*      */       
/*      */       public void run()
/*      */       {
/* 1377 */         if (this.fc.getMissedGrowthTicks() > 0) {
/* 1378 */           this.fc.processMissedGrowths(false, this);
/* 1379 */           this.fc.getFarm().saveMissedGrowths();
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */ 
/* 1385 */     }, 500L);
/*      */   }
/*      */   
/*      */   @EventHandler(priority=EventPriority.MONITOR)
/*      */   public void onEntityDeath(EntityDeathEvent event)
/*      */   {
/* 1391 */     Pasture pasture = (Pasture)Pasture.pastureEntities.get(event.getEntity().getUniqueId());
/* 1392 */     if (pasture != null) {
/* 1393 */       pasture.onEntityDeath(event.getEntity());
/*      */     }
/*      */     
/*      */ 
/* 1397 */     if (!ConfigTempleSacrifice.isValidEntity(event.getEntityType())) {
/* 1398 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1402 */     bcoord.setFromLocation(event.getEntity().getLocation());
/* 1403 */     HashSet<Buildable> buildables = CivGlobal.getBuildablesAt(bcoord);
/* 1404 */     if (buildables == null) {
/* 1405 */       return;
/*      */     }
/*      */     
/* 1408 */     for (Buildable buildable : buildables) {
/* 1409 */       if (((buildable instanceof Temple)) && 
/* 1410 */         (buildable.getCorner().getY() <= event.getEntity().getLocation().getBlockY()))
/*      */       {
/* 1412 */         ((Temple)buildable).onEntitySacrifice(event.getEntityType());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler(priority=EventPriority.NORMAL)
/*      */   public void onBlockGrowEvent(BlockGrowEvent event)
/*      */   {
/* 1420 */     bcoord.setFromLocation(event.getBlock().getLocation().add(0.0D, -1.0D, 0.0D));
/* 1421 */     if (CivGlobal.vanillaGrowthLocations.contains(bcoord))
/*      */     {
/* 1423 */       return;
/*      */     }
/*      */     
/* 1426 */     Block b = event.getBlock();
/*      */     
/* 1428 */     if (Farm.isBlockControlled(b)) {
/* 1429 */       event.setCancelled(true);
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler(priority=EventPriority.HIGH)
/*      */   public void onEntityBreakDoor(EntityBreakDoorEvent event) {
/* 1435 */     bcoord.setFromLocation(event.getBlock().getLocation());
/* 1436 */     StructureBlock sb = CivGlobal.getStructureBlock(bcoord);
/* 1437 */     if (sb != null) {
/* 1438 */       event.setCancelled(true);
/*      */     }
/*      */     
/* 1441 */     CampBlock cb = CivGlobal.getCampBlock(bcoord);
/* 1442 */     if (cb != null) {
/* 1443 */       event.setCancelled(true);
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler(priority=EventPriority.NORMAL)
/*      */   public void onCreatureSpawnEvent(CreatureSpawnEvent event) {
/* 1449 */     if ((War.isWarTime()) && 
/* 1450 */       (!event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.BREEDING))) {
/* 1451 */       event.setCancelled(true);
/* 1452 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1456 */     if (event.getEntity().getType().equals(EntityType.CHICKEN)) {
/* 1457 */       if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.EGG)) {
/* 1458 */         event.setCancelled(true);
/* 1459 */         return;
/*      */       }
/* 1461 */       NBTTagCompound compound = new NBTTagCompound();
/* 1462 */       if (compound.getBoolean("IsChickenJockey")) {
/* 1463 */         event.setCancelled(true);
/* 1464 */         return;
/*      */       }
/*      */     }
/*      */     
/* 1468 */     if ((event.getEntity().getType().equals(EntityType.IRON_GOLEM)) && 
/* 1469 */       (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM))) {
/* 1470 */       event.setCancelled(true);
/* 1471 */       return;
/*      */     }
/*      */     
/* 1474 */     if (MobLib.isMobLibEntity(event.getEntity())) {
/* 1475 */       return;
/*      */     }
/*      */     
/* 1478 */     if ((event.getEntity().getType().equals(EntityType.ZOMBIE)) || 
/* 1479 */       (event.getEntity().getType().equals(EntityType.SKELETON)) || 
/* 1480 */       (event.getEntity().getType().equals(EntityType.BAT)) || 
/* 1481 */       (event.getEntity().getType().equals(EntityType.CAVE_SPIDER)) || 
/* 1482 */       (event.getEntity().getType().equals(EntityType.SPIDER)) || 
/* 1483 */       (event.getEntity().getType().equals(EntityType.CREEPER)) || 
/* 1484 */       (event.getEntity().getType().equals(EntityType.WOLF)) || 
/* 1485 */       (event.getEntity().getType().equals(EntityType.SILVERFISH)) || 
/* 1486 */       (event.getEntity().getType().equals(EntityType.OCELOT)) || 
/* 1487 */       (event.getEntity().getType().equals(EntityType.WITCH)) || 
/* 1488 */       (event.getEntity().getType().equals(EntityType.ENDERMAN)))
/*      */     {
/* 1490 */       event.setCancelled(true);
/* 1491 */       return;
/*      */     }
/*      */     
/* 1494 */     if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.SPAWNER)) {
/* 1495 */       event.setCancelled(true);
/* 1496 */       return;
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean allowPistonAction(Location loc) {
/* 1501 */     bcoord.setFromLocation(loc);
/* 1502 */     StructureBlock sb = CivGlobal.getStructureBlock(bcoord);
/* 1503 */     if (sb != null) {
/* 1504 */       return false;
/*      */     }
/*      */     
/* 1507 */     RoadBlock rb = CivGlobal.getRoadBlock(bcoord);
/* 1508 */     if (rb != null) {
/* 1509 */       return false;
/*      */     }
/*      */     
/* 1512 */     CampBlock cb = CivGlobal.getCampBlock(bcoord);
/* 1513 */     if (cb != null) {
/* 1514 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1523 */     BlockCoord bcoord2 = new BlockCoord(bcoord);
/* 1524 */     bcoord2.setX(bcoord.getX() - 1);
/* 1525 */     if (ItemFrameStorage.attachedBlockMap.containsKey(bcoord2)) {
/* 1526 */       return false;
/*      */     }
/*      */     
/* 1529 */     bcoord2.setX(bcoord.getX() + 1);
/* 1530 */     if (ItemFrameStorage.attachedBlockMap.containsKey(bcoord2)) {
/* 1531 */       return false;
/*      */     }
/*      */     
/* 1534 */     bcoord2.setZ(bcoord.getZ() - 1);
/* 1535 */     if (ItemFrameStorage.attachedBlockMap.containsKey(bcoord2)) {
/* 1536 */       return false;
/*      */     }
/*      */     
/* 1539 */     bcoord2.setZ(bcoord.getZ() + 1);
/* 1540 */     if (ItemFrameStorage.attachedBlockMap.containsKey(bcoord2)) {
/* 1541 */       return false;
/*      */     }
/*      */     
/* 1544 */     coord.setFromLocation(loc);
/* 1545 */     HashSet<Wall> walls = CivGlobal.getWallChunk(coord);
/*      */     
/* 1547 */     if (walls != null) {
/* 1548 */       for (Wall wall : walls) {
/* 1549 */         if (wall.isProtectedLocation(loc)) {
/* 1550 */           return false;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1555 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @EventHandler(priority=EventPriority.HIGHEST)
/*      */   public void onBlockPistonExtendEvent(BlockPistonExtendEvent event)
/*      */   {
/* 1564 */     int PISTON_EXTEND_LENGTH = 13;
/* 1565 */     Block currentBlock = event.getBlock().getRelative(event.getDirection());
/* 1566 */     for (int i = 0; i < 13; i++) {
/* 1567 */       if ((ItemManager.getId(currentBlock) == 0) && 
/* 1568 */         (!allowPistonAction(currentBlock.getLocation()))) {
/* 1569 */         event.setCancelled(true);
/* 1570 */         return;
/*      */       }
/*      */       
/*      */ 
/* 1574 */       currentBlock = currentBlock.getRelative(event.getDirection());
/*      */     }
/*      */     
/* 1577 */     if (War.isWarTime()) {
/* 1578 */       event.setCancelled(true);
/* 1579 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1589 */     coord.setFromLocation(event.getBlock().getLocation());
/* 1590 */     FarmChunk fc = CivGlobal.getFarmChunk(coord);
/* 1591 */     if (fc == null) {
/* 1592 */       event.setCancelled(true);
/*      */     }
/*      */     
/*      */ 
/* 1596 */     for (Block block : event.getBlocks()) {
/* 1597 */       if (!allowPistonAction(block.getLocation())) {
/* 1598 */         event.setCancelled(true);
/* 1599 */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   @EventHandler(priority=EventPriority.HIGHEST)
/*      */   public void onBlockPistonRetractEvent(BlockPistonRetractEvent event)
/*      */   {
/* 1608 */     if (!allowPistonAction(event.getRetractLocation())) {
/* 1609 */       event.setCancelled(true);
/* 1610 */       return;
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler(priority=EventPriority.HIGHEST)
/*      */   public void onPotionSplashEvent(PotionSplashEvent event) {
/* 1616 */     ThrownPotion potion = event.getPotion();
/*      */     
/* 1618 */     if (!(potion.getShooter() instanceof Player)) {
/* 1619 */       return;
/*      */     }
/*      */     
/* 1622 */     Player attacker = (Player)potion.getShooter();
/*      */     
/* 1624 */     for (PotionEffect effect : potion.getEffects()) {
/* 1625 */       if (effect.getType().equals(PotionEffectType.INVISIBILITY)) {
/* 1626 */         event.setCancelled(true);
/* 1627 */         return;
/*      */       }
/*      */     }
/*      */     
/* 1631 */     boolean protect = false;
/* 1632 */     for (PotionEffect effect : potion.getEffects()) {
/* 1633 */       if ((effect.getType().equals(PotionEffectType.BLINDNESS)) || 
/* 1634 */         (effect.getType().equals(PotionEffectType.CONFUSION)) || 
/* 1635 */         (effect.getType().equals(PotionEffectType.HARM)) || 
/* 1636 */         (effect.getType().equals(PotionEffectType.POISON)) || 
/* 1637 */         (effect.getType().equals(PotionEffectType.SLOW)) || 
/* 1638 */         (effect.getType().equals(PotionEffectType.SLOW_DIGGING)) || 
/* 1639 */         (effect.getType().equals(PotionEffectType.WEAKNESS)) || 
/* 1640 */         (effect.getType().equals(PotionEffectType.WITHER)))
/*      */       {
/* 1642 */         protect = true;
/* 1643 */         break;
/*      */       }
/*      */     }
/*      */     
/* 1647 */     if (!protect) {
/* 1648 */       return;
/*      */     }
/*      */     
/* 1651 */     for (LivingEntity entity : event.getAffectedEntities()) {
/* 1652 */       if ((entity instanceof Player)) {
/* 1653 */         Player defender = (Player)entity;
/* 1654 */         coord.setFromLocation(entity.getLocation());
/* 1655 */         TownChunk tc = CivGlobal.getTownChunk(coord);
/* 1656 */         if (tc != null)
/*      */         {
/*      */ 
/*      */ 
/* 1660 */           switch (playersCanPVPHere(attacker, defender, tc)) {
/*      */           case ALLOWED: 
/*      */             break;
/*      */           case NON_PVP_ZONE: 
/* 1664 */             CivMessage.send(attacker, "§cYou cannot use potions against " + defender.getName() + ". You are not at war.");
/* 1665 */             event.setCancelled(true);
/* 1666 */             return;
/*      */           case NOT_AT_WAR: 
/* 1668 */             CivMessage.send(attacker, "§cYou cannot use potions against " + defender.getName() + ". You a neutral in a war-zone.");
/* 1669 */             event.setCancelled(true);
/* 1670 */             return;
/*      */           case NEUTRAL_IN_WARZONE: 
/* 1672 */             CivMessage.send(attacker, "§cYou cannot use potions against " + defender.getName() + ". You are in a non-pvp zone.");
/* 1673 */             event.setCancelled(true);
/* 1674 */             return;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler(priority=EventPriority.NORMAL)
/*      */   public void onBlockRedstoneEvent(BlockRedstoneEvent event) {
/* 1683 */     bcoord.setFromLocation(event.getBlock().getLocation());
/*      */     
/* 1685 */     CampBlock cb = CivGlobal.getCampBlock(bcoord);
/* 1686 */     if ((cb != null) && (
/* 1687 */       (ItemManager.getId(event.getBlock()) == 64) || 
/* 1688 */       (ItemManager.getId(event.getBlock()) == 71))) {
/* 1689 */       event.setNewCurrent(0);
/* 1690 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1694 */     if (War.isWarTime()) {
/* 1695 */       event.setNewCurrent(0);
/* 1696 */       return;
/*      */     }
/*      */   }
/*      */   
/*      */   private static enum PVPDenyReason
/*      */   {
/* 1702 */     ALLOWED, 
/* 1703 */     NON_PVP_ZONE, 
/* 1704 */     NOT_AT_WAR, 
/* 1705 */     NEUTRAL_IN_WARZONE;
/*      */   }
/*      */   
/*      */   private PVPDenyReason playersCanPVPHere(Player attacker, Player defender, TownChunk tc) {
/* 1709 */     Resident defenderResident = CivGlobal.getResident(defender);
/* 1710 */     Resident attackerResident = CivGlobal.getResident(attacker);
/* 1711 */     PVPDenyReason reason = PVPDenyReason.NON_PVP_ZONE;
/*      */     
/*      */ 
/* 1714 */     if ((CivGlobal.isOutlawHere(defenderResident, tc)) || 
/* 1715 */       (CivGlobal.isOutlawHere(attackerResident, tc))) {
/* 1716 */       return PVPDenyReason.ALLOWED;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1723 */     if ((War.isWarTime()) && 
/* 1724 */       (tc.getTown().getCiv().getDiplomacyManager().isAtWar()))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1729 */       if ((!defenderResident.hasTown()) || ((!defenderResident.getTown().getCiv().getDiplomacyManager().atWarWith(tc.getTown().getCiv())) && 
/* 1730 */         (defenderResident.getTown().getCiv() != tc.getTown().getCiv())))
/*      */       {
/* 1732 */         return PVPDenyReason.ALLOWED; }
/* 1733 */       if ((!attackerResident.hasTown()) || ((!attackerResident.getTown().getCiv().getDiplomacyManager().atWarWith(tc.getTown().getCiv())) && 
/* 1734 */         (attackerResident.getTown().getCiv() != tc.getTown().getCiv()))) {
/* 1735 */         reason = PVPDenyReason.NEUTRAL_IN_WARZONE;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1740 */     boolean defenderAtWarWithAttacker = false;
/* 1741 */     if ((defenderResident != null) && (defenderResident.hasTown())) {
/* 1742 */       defenderAtWarWithAttacker = defenderResident.getTown().getCiv().getDiplomacyManager().atWarWith(attacker);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1747 */       if (defenderAtWarWithAttacker)
/*      */       {
/*      */ 
/* 1750 */         return PVPDenyReason.ALLOWED;
/*      */       }
/* 1752 */       if (reason.equals(PVPDenyReason.NON_PVP_ZONE)) {
/* 1753 */         reason = PVPDenyReason.NOT_AT_WAR;
/*      */       }
/*      */     }
/*      */     
/* 1757 */     return reason;
/*      */   }
/*      */   
/*      */   @EventHandler(priority=EventPriority.LOW)
/*      */   public void onEntityPortalCreate(EntityCreatePortalEvent event) {
/* 1762 */     event.setCancelled(true);
/*      */   }
/*      */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\listener\BlockListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
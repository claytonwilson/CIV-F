/*     */ package com.avrgaming.civcraft.listener;
/*     */ 
/*     */ import com.avrgaming.civcraft.cache.ArrowFiredCache;
/*     */ import com.avrgaming.civcraft.cache.CivCache;
/*     */ import com.avrgaming.civcraft.components.ProjectileArrowComponent;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigRemovedRecipes;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.items.ItemDurabilityEntry;
/*     */ import com.avrgaming.civcraft.items.components.Catalyst;
/*     */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
/*     */ import com.avrgaming.civcraft.lorestorage.ItemChangeResult;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreMaterial;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.mobs.components.MobComponent;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.moblib.MobLib;
/*     */ import gpl.AttributeUtil;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.enchantments.Enchantment;
/*     */ import org.bukkit.entity.Arrow;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.entity.Item;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.BlockBreakEvent;
/*     */ import org.bukkit.event.block.BlockPlaceEvent;
/*     */ import org.bukkit.event.enchantment.EnchantItemEvent;
/*     */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*     */ import org.bukkit.event.entity.EntityDeathEvent;
/*     */ import org.bukkit.event.entity.ItemSpawnEvent;
/*     */ import org.bukkit.event.entity.PlayerDeathEvent;
/*     */ import org.bukkit.event.entity.PlayerLeashEntityEvent;
/*     */ import org.bukkit.event.inventory.CraftItemEvent;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.inventory.InventoryCloseEvent;
/*     */ import org.bukkit.event.inventory.InventoryOpenEvent;
/*     */ import org.bukkit.event.inventory.InventoryType;
/*     */ import org.bukkit.event.player.PlayerDropItemEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEntityEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.event.player.PlayerItemDamageEvent;
/*     */ import org.bukkit.event.player.PlayerItemHeldEvent;
/*     */ import org.bukkit.event.player.PlayerPickupItemEvent;
/*     */ import org.bukkit.inventory.CraftingInventory;
/*     */ import org.bukkit.inventory.EntityEquipment;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.InventoryView;
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
/*     */ public class CustomItemManager
/*     */   implements Listener
/*     */ {
/*  85 */   public static HashMap<String, LinkedList<ItemDurabilityEntry>> itemDuraMap = new HashMap();
/*  86 */   public static boolean duraTaskScheduled = false;
/*     */   
/*     */   @EventHandler(priority=EventPriority.NORMAL)
/*     */   public void onEnchantItemEvent(EnchantItemEvent event) {
/*  90 */     CivMessage.sendError(event.getEnchanter(), "Items cannot be enchanted with enchantment tables.");
/*  91 */     event.setCancelled(true);
/*     */   }
/*     */   
/*     */ 
/*     */   @EventHandler(priority=EventPriority.NORMAL)
/*     */   public void onBlockBreak(BlockBreakEvent event) {}
/*     */   
/*     */   @EventHandler(priority=EventPriority.NORMAL)
/*     */   public void onBlockBreakSpawnItems(BlockBreakEvent event)
/*     */   {
/* 101 */     if (event.getBlock().getType().equals(Material.LAPIS_ORE)) {
/* 102 */       if (event.getPlayer().getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
/* 103 */         return;
/*     */       }
/*     */       
/* 106 */       event.setCancelled(true);
/*     */       
/* 108 */       ItemManager.setTypeIdAndData(event.getBlock(), 0, 0, true);
/*     */       try
/*     */       {
/* 111 */         Random rand = new Random();
/*     */         
/* 113 */         int min = CivSettings.getInteger(CivSettings.materialsConfig, "tungsten_min_drop").intValue();
/*     */         int max;
/* 115 */         int max; if (event.getPlayer().getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
/* 116 */           max = CivSettings.getInteger(CivSettings.materialsConfig, "tungsten_max_drop_with_fortune").intValue();
/*     */         } else {
/* 118 */           max = CivSettings.getInteger(CivSettings.materialsConfig, "tungsten_max_drop").intValue();
/*     */         }
/*     */         
/* 121 */         int randAmount = rand.nextInt(min + max);
/* 122 */         randAmount -= min;
/* 123 */         if (randAmount <= 0) {
/* 124 */           randAmount = 1;
/*     */         }
/*     */         
/* 127 */         for (int i = 0; i < randAmount; i++) {
/* 128 */           ItemStack stack = LoreMaterial.spawn((LoreMaterial)LoreMaterial.materialMap.get("mat_tungsten_ore"));
/* 129 */           event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), stack);
/*     */         }
/*     */       }
/*     */       catch (InvalidConfiguration e) {
/* 133 */         e.printStackTrace();
/* 134 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.LOWEST)
/*     */   public void onBlockPlace(BlockPlaceEvent event) {
/* 141 */     ItemStack stack = event.getPlayer().getItemInHand();
/* 142 */     if ((stack == null) || (stack.getType().equals(Material.AIR))) {
/* 143 */       return;
/*     */     }
/*     */     
/* 146 */     LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/* 147 */     if (craftMat == null) {
/* 148 */       return;
/*     */     }
/*     */     
/* 151 */     craftMat.onBlockPlaced(event);
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void onPlayerInteract(PlayerInteractEvent event)
/*     */   {
/* 157 */     ItemStack stack = event.getPlayer().getItemInHand();
/* 158 */     if (stack == null) {
/* 159 */       return;
/*     */     }
/*     */     
/* 162 */     LoreMaterial material = LoreMaterial.getMaterial(stack);
/* 163 */     if (material != null) {
/* 164 */       material.onInteract(event);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.LOW)
/*     */   public void onInteractEntity(PlayerInteractEntityEvent event) {
/* 170 */     if (event.isCancelled()) {
/* 171 */       return;
/*     */     }
/*     */     
/* 174 */     ItemStack stack = event.getPlayer().getItemInHand();
/* 175 */     if (stack == null) {
/* 176 */       return;
/*     */     }
/*     */     
/* 179 */     LoreMaterial material = LoreMaterial.getMaterial(stack);
/* 180 */     if (material != null) {
/* 181 */       material.onInteractEntity(event);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void onItemHeld(PlayerItemHeldEvent event)
/*     */   {
/* 188 */     if (event.isCancelled()) {
/* 189 */       return;
/*     */     }
/*     */     
/* 192 */     ItemStack stack = event.getPlayer().getItemInHand();
/* 193 */     if (stack == null) {
/* 194 */       return;
/*     */     }
/*     */     
/* 197 */     LoreMaterial material = LoreMaterial.getMaterial(stack);
/* 198 */     if (material != null) {
/* 199 */       material.onHold(event);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void OnPlayerDropItem(PlayerDropItemEvent event) {
/* 205 */     if (event.isCancelled()) {
/* 206 */       return;
/*     */     }
/* 208 */     ItemStack stack = event.getItemDrop().getItemStack();
/*     */     
/* 210 */     if (LoreMaterial.isCustom(stack)) {
/* 211 */       LoreMaterial.getMaterial(stack).onItemDrop(event);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void OnCraftItemEvent(CraftItemEvent event)
/*     */   {
/*     */     ItemStack[] arrayOfItemStack;
/* 220 */     int j = (arrayOfItemStack = event.getInventory().getMatrix()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/* 221 */       if (stack != null)
/*     */       {
/* 223 */         if (LoreMaterial.isCustom(stack)) {
/* 224 */           LoreMaterial.getMaterial(stack).onItemCraft(event);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void OnPlayerItemPickup(PlayerPickupItemEvent event) {
/* 232 */     ItemStack stack = event.getItem().getItemStack();
/*     */     
/* 234 */     if (LoreMaterial.isCustom(stack)) {
/* 235 */       LoreMaterial.getMaterial(stack).onItemPickup(event);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void OnItemSpawn(ItemSpawnEvent event) {
/* 241 */     ItemStack stack = event.getEntity().getItemStack();
/*     */     
/* 243 */     if (LoreMaterial.isCustom(stack)) {
/* 244 */       LoreMaterial.getMaterial(stack).onItemSpawn(event);
/*     */     }
/*     */     
/* 247 */     if ((isUnwantedVanillaItem(stack)) && 
/* 248 */       (!stack.getType().equals(Material.HOPPER)) && 
/* 249 */       (!stack.getType().equals(Material.HOPPER_MINECART))) {
/* 250 */       event.setCancelled(true);
/* 251 */       event.getEntity().remove();
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.LOW)
/*     */   public void onPlayerDefenseAndAttack(EntityDamageByEntityEvent event)
/*     */   {
/* 258 */     if (event.isCancelled()) {
/* 259 */       return;
/*     */     }
/*     */     
/* 262 */     Player defendingPlayer = null;
/* 263 */     if ((event.getEntity() instanceof Player)) {
/* 264 */       defendingPlayer = (Player)event.getEntity();
/*     */     }
/*     */     
/* 267 */     if ((event.getDamager() instanceof Arrow)) {
/* 268 */       LivingEntity shooter = (LivingEntity)((Arrow)event.getDamager()).getShooter();
/*     */       
/* 270 */       if ((shooter instanceof Player)) {
/* 271 */         ItemStack inHand = ((Player)shooter).getItemInHand();
/* 272 */         if (LoreMaterial.isCustom(inHand)) {
/* 273 */           LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(inHand);
/* 274 */           craftMat.onRangedAttack(event, inHand);
/*     */         }
/*     */       } else {
/* 277 */         ArrowFiredCache afc = (ArrowFiredCache)CivCache.arrowsFired.get(event.getDamager().getUniqueId());
/* 278 */         if (afc != null)
/*     */         {
/* 280 */           afc.setHit(true);
/* 281 */           afc.destroy(event.getDamager());
/*     */           
/* 283 */           defenderResident = CivGlobal.getResident(defendingPlayer);
/* 284 */           if ((defenderResident != null) && (defenderResident.hasTown()) && 
/* 285 */             (defenderResident.getTown().getCiv() == afc.getFromTower().getTown().getCiv()))
/*     */           {
/* 287 */             event.setCancelled(true);
/* 288 */             return;
/*     */           }
/*     */           
/*     */ 
/* 292 */           event.setDamage(afc.getFromTower().getDamage());
/*     */         }
/*     */       }
/*     */     }
/* 296 */     else if ((event.getDamager() instanceof Player)) {
/* 297 */       ItemStack inHand = ((Player)event.getDamager()).getItemInHand();
/* 298 */       craftMat = LoreCraftableMaterial.getCraftMaterial(inHand);
/* 299 */       if (craftMat != null) {
/* 300 */         craftMat.onAttack(event, inHand);
/*     */       }
/*     */       else {
/* 303 */         event.setDamage(0.5D);
/*     */       }
/*     */     }
/*     */     
/* 307 */     if (defendingPlayer == null) {
/* 308 */       if (((event.getEntity() instanceof LivingEntity)) && 
/* 309 */         (MobLib.isMobLibEntity((LivingEntity)event.getEntity()))) {
/* 310 */         MobComponent.onDefense(event.getEntity(), event);
/*     */       }
/*     */       
/*     */       return;
/*     */     }
/*     */     ItemStack[] arrayOfItemStack;
/* 316 */     Resident defenderResident = (arrayOfItemStack = defendingPlayer.getEquipment().getArmorContents()).length; for (LoreCraftableMaterial craftMat = 0; craftMat < defenderResident; craftMat++) { ItemStack stack = arrayOfItemStack[craftMat];
/* 317 */       if (LoreMaterial.isCustom(stack)) {
/* 318 */         LoreMaterial.getMaterial(stack).onDefense(event, stack);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.NORMAL)
/*     */   public void OnInventoryClose(InventoryCloseEvent event) {
/*     */     ItemStack[] arrayOfItemStack;
/* 326 */     int j = (arrayOfItemStack = event.getInventory().getContents()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/* 327 */       if (stack != null)
/*     */       {
/*     */ 
/*     */ 
/* 331 */         if (LoreMaterial.isCustom(stack)) {
/* 332 */           LoreMaterial.getMaterial(stack).onInventoryClose(event);
/*     */         }
/*     */       }
/*     */     }
/* 336 */     for (ItemStack stack : event.getPlayer().getInventory()) {
/* 337 */       if (stack != null)
/*     */       {
/*     */ 
/*     */ 
/* 341 */         if (LoreMaterial.isCustom(stack))
/* 342 */           LoreMaterial.getMaterial(stack).onInventoryClose(event); }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.NORMAL)
/*     */   public void OnInventoryOpen(InventoryOpenEvent event) {
/*     */     ItemStack[] arrayOfItemStack;
/* 349 */     int k = (arrayOfItemStack = event.getInventory().getContents()).length; for (int i = 0; i < k; i++) { ItemStack stack = arrayOfItemStack[i];
/* 350 */       if (stack != null)
/*     */       {
/*     */ 
/*     */ 
/* 354 */         if (LoreMaterial.isCustom(stack)) {
/* 355 */           LoreCraftableMaterial.getMaterial(stack).onInventoryOpen(event, stack);
/*     */         }
/*     */       }
/*     */     }
/* 359 */     for (ItemStack stack : event.getPlayer().getInventory()) {
/* 360 */       if (stack != null)
/*     */       {
/*     */ 
/*     */ 
/* 364 */         if (LoreMaterial.isCustom(stack)) {
/* 365 */           LoreMaterial.getMaterial(stack).onInventoryOpen(event, stack);
/*     */         }
/*     */       }
/*     */     }
/* 369 */     k = (arrayOfItemStack = event.getPlayer().getInventory().getArmorContents()).length; for (int j = 0; j < k; j++) { ItemStack stack = arrayOfItemStack[j];
/* 370 */       if (stack != null)
/*     */       {
/*     */ 
/*     */ 
/* 374 */         if (LoreMaterial.isCustom(stack)) {
/* 375 */           LoreMaterial.getMaterial(stack).onInventoryOpen(event, stack);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean processDurabilityChanges(PlayerDeathEvent event, ItemStack stack, int i)
/*     */   {
/* 384 */     LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/* 385 */     if (craftMat != null) {
/* 386 */       ItemChangeResult result = craftMat.onDurabilityDeath(event, stack);
/* 387 */       if (result != null) {
/* 388 */         if (!result.destroyItem) {
/* 389 */           event.getEntity().getInventory().setItem(i, result.stack);
/*     */         } else {
/* 391 */           event.getEntity().getInventory().setItem(i, new ItemStack(Material.AIR));
/* 392 */           event.getDrops().remove(stack);
/* 393 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 398 */     return true;
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.LOW)
/*     */   public void onPlayerDeathEvent(PlayerDeathEvent event) {
/* 403 */     HashMap<Integer, ItemStack> noDrop = new HashMap();
/* 404 */     ItemStack[] armorNoDrop = new ItemStack[4];
/*     */     
/*     */ 
/* 407 */     for (int i = 0; i < event.getEntity().getInventory().getSize(); i++) {
/* 408 */       ItemStack stack = event.getEntity().getInventory().getItem(i);
/* 409 */       if (stack != null)
/*     */       {
/*     */ 
/*     */ 
/* 413 */         if (processDurabilityChanges(event, stack, i))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 418 */           if (LoreMaterial.hasEnhancements(stack))
/*     */           {
/*     */ 
/*     */ 
/* 422 */             AttributeUtil attrs = new AttributeUtil(stack);
/* 423 */             for (LoreEnhancement enhance : attrs.getEnhancements()) {
/* 424 */               if (enhance.onDeath(event, stack))
/*     */               {
/* 426 */                 noDrop.put(Integer.valueOf(i), stack); }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 432 */     ItemStack[] contents = event.getEntity().getInventory().getArmorContents();
/* 433 */     for (int i = 0; i < contents.length; i++) {
/* 434 */       ItemStack stack = contents[i];
/* 435 */       if (stack != null)
/*     */       {
/*     */ 
/*     */ 
/* 439 */         if (processDurabilityChanges(event, stack, i))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 444 */           if (LoreMaterial.hasEnhancements(stack))
/*     */           {
/*     */ 
/*     */ 
/* 448 */             AttributeUtil attrs = new AttributeUtil(stack);
/* 449 */             for (LoreEnhancement enhance : attrs.getEnhancements()) {
/* 450 */               if (enhance.onDeath(event, stack))
/*     */               {
/* 452 */                 armorNoDrop[i] = stack;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
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
/* 489 */     TaskMaster.syncTask(new Runnable()
/*     */     {
/*     */       HashMap<Integer, ItemStack> restore;
/*     */       String playerName;
/*     */       ItemStack[] armorContents;
/*     */       
/*     */       public void run()
/*     */       {
/*     */         try
/*     */         {
/* 474 */           Player player = CivGlobal.getPlayer(this.playerName);
/* 475 */           PlayerInventory inv = player.getInventory();
/* 476 */           for (Integer slot : this.restore.keySet()) {
/* 477 */             ItemStack stack = (ItemStack)this.restore.get(slot);
/* 478 */             inv.setItem(slot.intValue(), stack);
/*     */           }
/*     */           
/* 481 */           inv.setArmorContents(this.armorContents);
/*     */         } catch (CivException e) {
/* 483 */           e.printStackTrace();
/* 484 */           return;
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @EventHandler(priority=EventPriority.LOW)
/*     */   public void OnEntityDeath(EntityDeathEvent event)
/*     */   {
/* 496 */     if ((event.getEntity() instanceof Player)) {
/* 497 */       return;
/*     */     }
/*     */     
/*     */ 
/* 501 */     LinkedList<ItemStack> removed = new LinkedList();
/* 502 */     for (ItemStack stack : event.getDrops()) {
/* 503 */       Integer key = Integer.valueOf(ItemManager.getId(stack));
/*     */       
/* 505 */       if ((CivSettings.removedRecipies.containsKey(key)) && 
/* 506 */         (!LoreMaterial.isCustom(stack))) {
/* 507 */         removed.add(stack);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 512 */     event.getDrops().removeAll(removed);
/*     */   }
/*     */   
/*     */ 
/*     */   @EventHandler(priority=EventPriority.LOW)
/*     */   public void onItemPickup(PlayerPickupItemEvent event)
/*     */   {
/* 519 */     if (ItemManager.getId(event.getItem().getItemStack()) == ItemManager.getId(Material.SLIME_BALL)) {
/* 520 */       LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(event.getItem().getItemStack());
/* 521 */       if (craftMat == null)
/*     */       {
/* 523 */         LoreCraftableMaterial slime = LoreCraftableMaterial.getCraftMaterialFromId("mat_vanilla_slime");
/* 524 */         ItemStack newStack = LoreCraftableMaterial.spawn(slime);
/* 525 */         newStack.setAmount(event.getItem().getItemStack().getAmount());
/* 526 */         event.getPlayer().getInventory().addItem(new ItemStack[] { newStack });
/* 527 */         event.getPlayer().updateInventory();
/* 528 */         event.getItem().remove();
/* 529 */         event.setCancelled(true);
/*     */       }
/*     */     }
/*     */     
/* 533 */     if ((ItemManager.getId(event.getItem().getItemStack()) == ItemManager.getId(Material.RAW_FISH)) && 
/* 534 */       (ItemManager.getData(event.getItem().getItemStack()) == 
/* 535 */       ItemManager.getData(ItemManager.getMaterialData(349, 2)))) {
/* 536 */       LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(event.getItem().getItemStack());
/* 537 */       if (craftMat == null)
/*     */       {
/* 539 */         LoreCraftableMaterial clown = LoreCraftableMaterial.getCraftMaterialFromId("mat_vanilla_clownfish");
/* 540 */         ItemStack newStack = LoreCraftableMaterial.spawn(clown);
/* 541 */         newStack.setAmount(event.getItem().getItemStack().getAmount());
/* 542 */         event.getPlayer().getInventory().addItem(new ItemStack[] { newStack });
/* 543 */         event.getPlayer().updateInventory();
/* 544 */         event.getItem().remove();
/* 545 */         event.setCancelled(true);
/*     */       }
/*     */     }
/*     */     
/* 549 */     if ((ItemManager.getId(event.getItem().getItemStack()) == ItemManager.getId(Material.RAW_FISH)) && 
/* 550 */       (ItemManager.getData(event.getItem().getItemStack()) == 
/* 551 */       ItemManager.getData(ItemManager.getMaterialData(349, 3)))) {
/* 552 */       LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(event.getItem().getItemStack());
/* 553 */       if (craftMat == null)
/*     */       {
/* 555 */         LoreCraftableMaterial clown = LoreCraftableMaterial.getCraftMaterialFromId("mat_vanilla_pufferfish");
/* 556 */         ItemStack newStack = LoreCraftableMaterial.spawn(clown);
/* 557 */         newStack.setAmount(event.getItem().getItemStack().getAmount());
/* 558 */         event.getPlayer().getInventory().addItem(new ItemStack[] { newStack });
/* 559 */         event.getPlayer().updateInventory();
/* 560 */         event.getItem().remove();
/* 561 */         event.setCancelled(true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void convertLegacyItem(InventoryClickEvent event)
/*     */   {
/* 568 */     boolean currentEmpty = (event.getCurrentItem() == null) || (ItemManager.getId(event.getCurrentItem()) == 0);
/*     */     
/* 570 */     if (currentEmpty) {
/* 571 */       return;
/*     */     }
/*     */     
/* 574 */     if (ItemManager.getId(event.getCurrentItem()) == ItemManager.getId(Material.SLIME_BALL)) {
/* 575 */       LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(event.getCurrentItem());
/* 576 */       if (craftMat == null)
/*     */       {
/* 578 */         LoreCraftableMaterial slime = LoreCraftableMaterial.getCraftMaterialFromId("mat_vanilla_slime");
/* 579 */         ItemStack newStack = LoreCraftableMaterial.spawn(slime);
/* 580 */         newStack.setAmount(event.getCurrentItem().getAmount());
/* 581 */         event.setCurrentItem(newStack);
/*     */       }
/*     */     }
/*     */     
/* 585 */     if ((ItemManager.getId(event.getCurrentItem()) == ItemManager.getId(Material.RAW_FISH)) && 
/* 586 */       (ItemManager.getData(event.getCurrentItem()) == 
/* 587 */       ItemManager.getData(ItemManager.getMaterialData(349, 2)))) {
/* 588 */       LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(event.getCurrentItem());
/* 589 */       if (craftMat == null)
/*     */       {
/* 591 */         LoreCraftableMaterial clown = LoreCraftableMaterial.getCraftMaterialFromId("mat_vanilla_clownfish");
/* 592 */         ItemStack newStack = LoreCraftableMaterial.spawn(clown);
/* 593 */         newStack.setAmount(event.getCurrentItem().getAmount());
/* 594 */         event.setCurrentItem(newStack);
/*     */       }
/*     */     }
/*     */     
/* 598 */     if ((ItemManager.getId(event.getCurrentItem()) == ItemManager.getId(Material.RAW_FISH)) && 
/* 599 */       (ItemManager.getData(event.getCurrentItem()) == 
/* 600 */       ItemManager.getData(ItemManager.getMaterialData(349, 3)))) {
/* 601 */       LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(event.getCurrentItem());
/* 602 */       if (craftMat == null)
/*     */       {
/* 604 */         LoreCraftableMaterial clown = LoreCraftableMaterial.getCraftMaterialFromId("mat_vanilla_pufferfish");
/* 605 */         ItemStack newStack = LoreCraftableMaterial.spawn(clown);
/* 606 */         newStack.setAmount(event.getCurrentItem().getAmount());
/* 607 */         event.setCurrentItem(newStack);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void OnInventoryClick(InventoryClickEvent event)
/*     */   {
/* 617 */     boolean currentEmpty = (event.getCurrentItem() == null) || (ItemManager.getId(event.getCurrentItem()) == 0);
/* 618 */     boolean cursorEmpty = (event.getCursor() == null) || (ItemManager.getId(event.getCursor()) == 0);
/*     */     
/* 620 */     if ((currentEmpty) && (cursorEmpty)) {
/* 621 */       return;
/*     */     }
/*     */     
/* 624 */     convertLegacyItem(event);
/*     */     
/* 626 */     if (event.getRawSlot() < 0)
/*     */     {
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
/* 639 */       return;
/*     */     }
/*     */     
/* 642 */     InventoryView view = event.getView();
/*     */     Inventory otherInv;
/*     */     Inventory clickedInv;
/*     */     Inventory otherInv;
/* 646 */     if (view.getType().equals(InventoryType.CRAFTING))
/*     */     {
/*     */       Inventory otherInv;
/*     */       
/*     */ 
/* 651 */       if (event.getRawSlot() <= 4) {
/* 652 */         Inventory clickedInv = view.getTopInventory();
/* 653 */         otherInv = view.getBottomInventory();
/*     */       } else {
/* 655 */         Inventory clickedInv = view.getBottomInventory();
/* 656 */         otherInv = view.getBottomInventory();
/*     */       }
/*     */     } else { Inventory otherInv;
/* 659 */       if (event.getRawSlot() == view.convertSlot(event.getRawSlot()))
/*     */       {
/* 661 */         Inventory clickedInv = view.getTopInventory();
/* 662 */         otherInv = view.getBottomInventory();
/*     */       } else {
/* 664 */         clickedInv = view.getBottomInventory();
/* 665 */         otherInv = view.getTopInventory();
/*     */       }
/*     */     }
/*     */     
/* 669 */     LoreMaterial current = LoreMaterial.getMaterial(event.getCurrentItem());
/* 670 */     LoreMaterial cursor = LoreMaterial.getMaterial(event.getCursor());
/*     */     
/* 672 */     if (event.isShiftClick())
/*     */     {
/*     */ 
/* 675 */       if (current != null)
/*     */       {
/*     */ 
/*     */ 
/* 679 */         current.onInvShiftClick(event, clickedInv, otherInv, event.getCurrentItem());
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */     }
/* 685 */     else if ((!currentEmpty) && (!cursorEmpty))
/*     */     {
/*     */ 
/*     */ 
/* 689 */       if (current != null) {
/* 690 */         current.onInvItemSwap(event, clickedInv, event.getCursor(), event.getCurrentItem());
/*     */       }
/*     */       
/* 693 */       if (cursor != null) {
/* 694 */         cursor.onInvItemSwap(event, clickedInv, event.getCursor(), event.getCurrentItem());
/*     */       }
/* 696 */     } else if (!currentEmpty)
/*     */     {
/*     */ 
/* 699 */       if (current != null)
/*     */       {
/* 701 */         current.onInvItemPickup(event, clickedInv, event.getCurrentItem());
/*     */       }
/*     */       
/*     */     }
/* 705 */     else if (cursor != null)
/*     */     {
/* 707 */       cursor.onInvItemDrop(event, clickedInv, event.getCursor());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @EventHandler(priority=EventPriority.LOW)
/*     */   public void OnPlayerInteractEntityEvent(PlayerInteractEntityEvent event)
/*     */   {
/* 717 */     LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(event.getPlayer().getItemInHand());
/* 718 */     if (craftMat == null) {
/* 719 */       return;
/*     */     }
/*     */     
/* 722 */     craftMat.onPlayerInteractEntityEvent(event);
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.LOW)
/*     */   public void OnPlayerLeashEvent(PlayerLeashEntityEvent event) {
/* 727 */     LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(event.getPlayer().getItemInHand());
/* 728 */     if (craftMat == null) {
/* 729 */       return;
/*     */     }
/*     */     
/* 732 */     craftMat.onPlayerLeashEvent(event);
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.LOW)
/*     */   public void onItemDurabilityChange(PlayerItemDamageEvent event)
/*     */   {
/* 738 */     ItemStack stack = event.getItem();
/*     */     
/* 740 */     LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/* 741 */     if (craftMat == null) {
/* 742 */       return;
/*     */     }
/* 744 */     craftMat.onItemDurabilityChange(event);
/*     */   }
/*     */   
/*     */   private static boolean isUnwantedVanillaItem(ItemStack stack) {
/* 748 */     if (stack == null) {
/* 749 */       return false;
/*     */     }
/*     */     
/* 752 */     LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/* 753 */     if (craftMat != null)
/*     */     {
/* 755 */       return false;
/*     */     }
/*     */     
/* 758 */     if (LoreGuiItem.isGUIItem(stack)) {
/* 759 */       return false;
/*     */     }
/*     */     
/* 762 */     ConfigRemovedRecipes removed = (ConfigRemovedRecipes)CivSettings.removedRecipies.get(Integer.valueOf(ItemManager.getId(stack)));
/* 763 */     if ((removed == null) && (!stack.getType().equals(Material.ENCHANTED_BOOK)))
/*     */     {
/* 765 */       if ((!stack.containsEnchantment(Enchantment.DAMAGE_ALL)) && 
/* 766 */         (!stack.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS)) && 
/* 767 */         (!stack.containsEnchantment(Enchantment.KNOCKBACK)) && 
/* 768 */         (!stack.containsEnchantment(Enchantment.DAMAGE_UNDEAD)) && 
/* 769 */         (!stack.containsEnchantment(Enchantment.DURABILITY)) && (
/* 770 */         (!stack.containsEnchantment(Enchantment.FIRE_ASPECT)) || 
/* 771 */         (stack.getEnchantmentLevel(Enchantment.FIRE_ASPECT) <= 2)))
/*     */       {
/* 773 */         if ((!stack.containsEnchantment(Enchantment.LOOT_BONUS_MOBS)) || 
/* 774 */           (stack.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) <= 1))
/*     */         {
/* 776 */           if ((!stack.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) || 
/* 777 */             (stack.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) <= 1))
/*     */           {
/* 779 */             if ((!stack.containsEnchantment(Enchantment.DIG_SPEED)) || 
/* 780 */               (stack.getEnchantmentLevel(Enchantment.DIG_SPEED) <= 5))
/*     */             {
/*     */ 
/*     */ 
/* 784 */               return false; } } }
/*     */       }
/*     */     }
/* 787 */     return true;
/*     */   }
/*     */   
/*     */   public static void removeUnwantedVanillaItems(Player player, Inventory inv)
/*     */   {
/* 792 */     if (player.isOp())
/*     */     {
/* 794 */       return;
/*     */     }
/* 796 */     boolean sentMessage = false;
/*     */     ItemStack[] arrayOfItemStack1;
/* 798 */     int j = (arrayOfItemStack1 = inv.getContents()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack1[i];
/* 799 */       if (isUnwantedVanillaItem(stack))
/*     */       {
/*     */ 
/*     */ 
/* 803 */         inv.remove(stack);
/* 804 */         if (player != null) {
/* 805 */           CivLog.info("Removed vanilla item:" + stack + " from " + player.getName());
/*     */         }
/* 807 */         if (!sentMessage) {
/* 808 */           if (player != null) {
/* 809 */             CivMessage.send(player, "§7Restricted vanilla items in your inventory have been removed.");
/*     */           }
/* 811 */           sentMessage = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 816 */     if (player != null) {
/* 817 */       ItemStack[] contents = player.getEquipment().getArmorContents();
/* 818 */       boolean foundBad = false;
/* 819 */       for (int i = 0; i < contents.length; i++) {
/* 820 */         ItemStack stack = contents[i];
/* 821 */         if (stack != null)
/*     */         {
/*     */ 
/*     */ 
/* 825 */           LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/* 826 */           if (craftMat == null)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 831 */             ConfigRemovedRecipes removed = (ConfigRemovedRecipes)CivSettings.removedRecipies.get(Integer.valueOf(stack.getTypeId()));
/* 832 */             if ((removed != null) || (stack.getType().equals(Material.ENCHANTED_BOOK)))
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/* 837 */               CivLog.info("Removed vanilla item:" + stack + " from " + player.getName() + " from armor.");
/* 838 */               contents[i] = new ItemStack(Material.AIR);
/* 839 */               foundBad = true;
/* 840 */               if (!sentMessage) {
/* 841 */                 CivMessage.send(player, "§7Restricted vanilla items in your inventory have been removed.");
/* 842 */                 sentMessage = true;
/*     */               }
/*     */             } } } }
/* 845 */       if (foundBad) {
/* 846 */         player.getEquipment().setArmorContents(contents);
/*     */       }
/*     */     }
/*     */     
/* 850 */     if ((sentMessage) && 
/* 851 */       (player != null)) {
/* 852 */       player.updateInventory();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @EventHandler(priority=EventPriority.NORMAL)
/*     */   public void OnInventoryHold(PlayerItemHeldEvent event)
/*     */   {
/* 860 */     ItemStack stack = event.getPlayer().getInventory().getItem(event.getNewSlot());
/* 861 */     if (stack == null) {
/* 862 */       return;
/*     */     }
/*     */     
/* 865 */     LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/* 866 */     if (craftMat == null) {
/* 867 */       return;
/*     */     }
/*     */     
/* 870 */     craftMat.onHold(event);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @EventHandler(priority=EventPriority.LOWEST)
/*     */   public void OnInventoryClickEvent(InventoryClickEvent event) {}
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
/*     */   public LoreCraftableMaterial getCompatibleCatalyst(LoreCraftableMaterial craftMat)
/*     */   {
/* 951 */     LinkedList<LoreMaterial> cataList = new LinkedList();
/* 952 */     cataList.add((LoreMaterial)LoreMaterial.materialMap.get("mat_common_attack_catalyst"));
/* 953 */     cataList.add((LoreMaterial)LoreMaterial.materialMap.get("mat_common_defense_catalyst"));
/* 954 */     cataList.add((LoreMaterial)LoreMaterial.materialMap.get("mat_uncommon_attack_catalyst"));
/* 955 */     cataList.add((LoreMaterial)LoreMaterial.materialMap.get("mat_uncommon_defense_catalyst"));
/* 956 */     cataList.add((LoreMaterial)LoreMaterial.materialMap.get("mat_rare_attack_catalyst"));
/* 957 */     cataList.add((LoreMaterial)LoreMaterial.materialMap.get("mat_rare_defense_catalyst"));
/* 958 */     cataList.add((LoreMaterial)LoreMaterial.materialMap.get("mat_legendary_attack_catalyst"));
/* 959 */     cataList.add((LoreMaterial)LoreMaterial.materialMap.get("mat_legendary_defense_catalyst"));
/*     */     int j;
/* 961 */     int i; for (Iterator localIterator = cataList.iterator(); localIterator.hasNext(); 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 968 */         i < j)
/*     */     {
/* 961 */       LoreMaterial mat = (LoreMaterial)localIterator.next();
/* 962 */       LoreCraftableMaterial cMat = (LoreCraftableMaterial)mat;
/*     */       
/* 964 */       Catalyst cat = (Catalyst)cMat.getComponent("Catalyst");
/* 965 */       String allowedMats = cat.getString("allowed_materials");
/* 966 */       String[] matSplit = allowedMats.split(",");
/*     */       String[] arrayOfString1;
/* 968 */       j = (arrayOfString1 = matSplit).length;i = 0; continue;String mid = arrayOfString1[i];
/* 969 */       if (mid.trim().equalsIgnoreCase(craftMat.getId())) {
/* 970 */         return cMat;
/*     */       }
/* 968 */       i++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 975 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\listener\CustomItemManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.avrgaming.civcraft.items.units;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.ConfigUnit;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreMaterial;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.threading.tasks.DelayMoveInventoryItem;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.entity.Item;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Event.Result;
/*     */ import org.bukkit.event.block.BlockBreakEvent;
/*     */ import org.bukkit.event.block.BlockDamageEvent;
/*     */ import org.bukkit.event.block.BlockPlaceEvent;
/*     */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*     */ import org.bukkit.event.entity.EntityDeathEvent;
/*     */ import org.bukkit.event.entity.ItemSpawnEvent;
/*     */ import org.bukkit.event.inventory.CraftItemEvent;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.inventory.InventoryCloseEvent;
/*     */ import org.bukkit.event.player.PlayerDropItemEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEntityEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.event.player.PlayerItemHeldEvent;
/*     */ import org.bukkit.event.player.PlayerPickupItemEvent;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.InventoryView;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
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
/*     */ public class UnitMaterial
/*     */   extends LoreMaterial
/*     */ {
/*  57 */   private ConfigUnit unit = null;
/*     */   
/*     */   private static final int LAST_SLOT = 8;
/*  60 */   public HashSet<Integer> allowedSubslots = new HashSet();
/*     */   
/*     */   public UnitMaterial(String id, int itemId, short damage) {
/*  63 */     super(id, itemId, damage);
/*     */   }
/*     */   
/*     */   public UnitMaterial(String id, ConfigUnit configUnit)
/*     */   {
/*  68 */     super(id, configUnit.item_id, (short)0);
/*  69 */     setUnit(configUnit);
/*     */     
/*  71 */     setLore("Unit Item");
/*  72 */     setName(configUnit.name);
/*     */   }
/*     */   
/*     */   public ConfigUnit getUnit() {
/*  76 */     return this.unit;
/*     */   }
/*     */   
/*     */   public void setUnit(ConfigUnit unit) {
/*  80 */     this.unit = unit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onBlockBreak(BlockBreakEvent event) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void onBlockDamage(BlockDamageEvent event) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void onBlockInteract(PlayerInteractEvent event) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void onBlockPlaced(BlockPlaceEvent event)
/*     */   {
/*  99 */     event.setCancelled(true);
/* 100 */     CivMessage.sendError(event.getPlayer(), "Cannot place this item");
/* 101 */     event.getPlayer().updateInventory();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onHit(EntityDamageByEntityEvent event) {}
/*     */   
/*     */ 
/*     */   public void onHold(PlayerItemHeldEvent event) {}
/*     */   
/*     */ 
/*     */   public void onInteract(PlayerInteractEvent event)
/*     */   {
/* 114 */     event.setUseItemInHand(Event.Result.DENY);
/*     */     
/* 116 */     event.setCancelled(true);
/* 117 */     CivMessage.sendError(event.getPlayer(), "Cannot use this item.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onInteractEntity(PlayerInteractEntityEvent event) {}
/*     */   
/*     */ 
/*     */ 
/* 127 */   public void onItemDrop(PlayerDropItemEvent event) { onItemFromPlayer(event.getPlayer(), event.getItemDrop().getItemStack()); }
/*     */   
/*     */   protected void removeChildren(Inventory inv) {
/*     */     ItemStack[] arrayOfItemStack;
/* 131 */     int j = (arrayOfItemStack = inv.getContents()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/* 132 */       if (stack != null)
/*     */       {
/* 134 */         LoreMaterial material = LoreMaterial.getMaterial(stack);
/* 135 */         if ((material != null) && ((material instanceof UnitItemMaterial))) {
/* 136 */           UnitItemMaterial umat = (UnitItemMaterial)material;
/* 137 */           if (umat.getParent() == this) {
/* 138 */             inv.remove(stack);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static List<String> stripTownLore(List<String> lore)
/*     */   {
/* 147 */     for (String str : lore) {
/* 148 */       if (str.startsWith("Town:")) {
/* 149 */         lore.remove(str);
/* 150 */         break;
/*     */       }
/*     */     }
/* 153 */     return lore;
/*     */   }
/*     */   
/*     */   public static void setOwningTown(Town town, ItemStack stack) {
/* 157 */     if (town == null) {
/* 158 */       return;
/*     */     }
/*     */     
/* 161 */     ItemMeta meta = stack.getItemMeta();
/* 162 */     if ((meta != null) && (meta.hasLore())) {
/* 163 */       List<String> lore = meta.getLore();
/*     */       
/* 165 */       lore = stripTownLore(lore);
/*     */       
/* 167 */       if (lore != null) {
/* 168 */         lore.add("Town:" + town.getName() + " " + "§0" + town.getId());
/*     */       }
/*     */       
/* 171 */       meta.setLore(lore);
/* 172 */       stack.setItemMeta(meta);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Town getOwningTown(ItemStack stack) {
/* 177 */     ItemMeta meta = stack.getItemMeta();
/* 178 */     if ((meta == null) || (!meta.hasLore())) {
/* 179 */       return null;
/*     */     }
/*     */     
/* 182 */     String loreLine = null;
/* 183 */     List<String> lore = meta.getLore();
/* 184 */     for (String str : lore) {
/* 185 */       if (str.startsWith("Town:")) {
/* 186 */         loreLine = str;
/* 187 */         break;
/*     */       }
/*     */     }
/*     */     
/* 191 */     if (loreLine == null) {
/* 192 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 196 */       String[] split = loreLine.split("§0");
/* 197 */       int townId = Integer.valueOf(split[1]).intValue();
/*     */       
/* 199 */       return CivGlobal.getTownFromId(townId);
/*     */     } catch (Exception e) {
/* 201 */       e.printStackTrace(); }
/* 202 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void onItemCraft(CraftItemEvent event)
/*     */   {
/*     */     try
/*     */     {
/* 210 */       CivMessage.sendError(CivGlobal.getPlayer(event.getWhoClicked().getName()), "Cannot craft with a unit item.");
/*     */     }
/*     */     catch (CivException localCivException) {}
/*     */     
/* 214 */     event.setCancelled(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onItemPickup(PlayerPickupItemEvent event)
/*     */   {
/* 221 */     if (!validateUnitUse(event.getPlayer(), event.getItem().getItemStack())) {
/* 222 */       CivMessage.sendErrorNoRepeat(event.getPlayer(), "You cannot use this unit because it does not belong to your civilization.");
/* 223 */       event.setCancelled(true);
/* 224 */       return;
/*     */     }
/*     */     
/* 227 */     ConfigUnit unit = Unit.getPlayerUnit(event.getPlayer());
/* 228 */     if (unit != null) {
/* 229 */       CivMessage.sendErrorNoRepeat(event.getPlayer(), "Already a " + unit.name + " cannot pickup another unit item.");
/* 230 */       event.setCancelled(true);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 236 */       Inventory inv = event.getPlayer().getInventory();
/*     */       
/* 238 */       ItemStack lastSlot = inv.getItem(8);
/* 239 */       if (lastSlot != null) {
/* 240 */         inv.setItem(8, event.getItem().getItemStack());
/* 241 */         inv.addItem(new ItemStack[] { lastSlot });
/* 242 */         event.getPlayer().updateInventory();
/*     */       } else {
/* 244 */         inv.setItem(8, event.getItem().getItemStack());
/*     */       }
/*     */       
/*     */ 
/* 248 */       onItemToPlayer(event.getPlayer(), event.getItem().getItemStack());
/* 249 */       event.getItem().remove();
/* 250 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean validateUnitUse(Player player, ItemStack stack) {
/* 255 */     if (stack == null) {
/* 256 */       return true;
/*     */     }
/*     */     
/* 259 */     Resident resident = CivGlobal.getResident(player);
/* 260 */     Town town = getOwningTown(stack);
/*     */     
/*     */ 
/* 263 */     if (town == null) {
/* 264 */       return true;
/*     */     }
/*     */     
/* 267 */     if (town.getCiv() != resident.getCiv()) {
/* 268 */       return false;
/*     */     }
/*     */     
/* 271 */     return true;
/*     */   }
/*     */   
/*     */   public int getFreeSlotCount(Inventory inv) {
/* 275 */     int count = 0;
/* 276 */     ItemStack[] arrayOfItemStack; int j = (arrayOfItemStack = inv.getContents()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/* 277 */       if (stack == null) {
/* 278 */         count++;
/*     */       }
/*     */     }
/* 281 */     return count;
/*     */   }
/*     */   
/*     */   public boolean hasFreeSlot(Inventory inv) { ItemStack[] arrayOfItemStack;
/* 285 */     int j = (arrayOfItemStack = inv.getContents()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/* 286 */       if (stack == null) {
/* 287 */         return true;
/*     */       }
/*     */     }
/* 290 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onInvItemPickup(InventoryClickEvent event, Inventory fromInv, ItemStack stack)
/*     */   {
/* 297 */     if ((fromInv.getHolder() instanceof Player)) {
/* 298 */       Player player = (Player)fromInv.getHolder();
/* 299 */       onItemFromPlayer(player, stack);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onInvItemDrop(InventoryClickEvent event, Inventory toInv, ItemStack stack)
/*     */   {
/* 308 */     if ((toInv.getHolder() instanceof Player))
/*     */     {
/*     */ 
/* 311 */       toInv = event.getView().getBottomInventory();
/* 312 */       Player player = (Player)toInv.getHolder();
/*     */       
/* 314 */       if (!validateUnitUse(player, stack)) {
/* 315 */         CivMessage.sendError(player, "You cannot use this unit because it does not belong to your civlization.");
/* 316 */         event.setCancelled(true);
/* 317 */         return;
/*     */       }
/*     */       
/*     */ 
/* 321 */       ConfigUnit unit = Unit.getPlayerUnit(player);
/* 322 */       if (unit != null)
/*     */       {
/* 324 */         CivMessage.sendError(player, "You already are a " + unit.name + " cannot pickup another unit item.");
/* 325 */         event.setCancelled(true);
/* 326 */         event.setResult(Event.Result.DENY);
/* 327 */         event.getView().close();
/* 328 */         player.updateInventory();
/* 329 */         return;
/*     */       }
/*     */       
/*     */ 
/* 333 */       if (event.getSlot() != 8)
/*     */       {
/* 335 */         DelayMoveInventoryItem task = new DelayMoveInventoryItem();
/* 336 */         task.fromSlot = event.getSlot();
/* 337 */         task.toSlot = 8;
/* 338 */         task.inv = toInv;
/* 339 */         task.playerName = player.getName();
/* 340 */         TaskMaster.syncTask(task);
/*     */       }
/*     */       
/* 343 */       onItemToPlayer(player, stack);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onInvShiftClick(InventoryClickEvent event, Inventory fromInv, Inventory toInv, ItemStack stack)
/*     */   {
/* 354 */     if (fromInv.equals(toInv)) {
/* 355 */       event.setCancelled(true);
/* 356 */       event.setResult(Event.Result.DENY);
/* 357 */       return;
/*     */     }
/*     */     
/* 360 */     if ((toInv.getHolder() instanceof Player)) {
/* 361 */       Player player = (Player)toInv.getHolder();
/*     */       
/* 363 */       if (!validateUnitUse(player, stack)) {
/* 364 */         CivMessage.sendError(player, "You cannot use this unit because it does not belong to your civlization.");
/* 365 */         event.setCancelled(true);
/* 366 */         return;
/*     */       }
/*     */       
/*     */ 
/* 370 */       ConfigUnit unit = Unit.getPlayerUnit(player);
/* 371 */       if (unit != null)
/*     */       {
/* 373 */         CivMessage.sendError(player, "You already are a " + unit.name + " cannot pickup another unit item.");
/* 374 */         event.setCancelled(true);
/* 375 */         event.setResult(Event.Result.DENY);
/* 376 */         event.getView().close();
/* 377 */         player.updateInventory();
/* 378 */         return;
/*     */       }
/*     */       
/*     */ 
/* 382 */       onItemToPlayer(player, stack);
/* 383 */     } else if ((fromInv.getHolder() instanceof Player)) {
/* 384 */       onItemFromPlayer((Player)fromInv.getHolder(), stack);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onInvItemSwap(InventoryClickEvent event, Inventory toInv, ItemStack droppedStack, ItemStack pickedStack)
/*     */   {
/* 395 */     if (droppedStack.getTypeId() == pickedStack.getTypeId()) {
/* 396 */       event.setCancelled(true);
/* 397 */       event.setResult(Event.Result.DENY);
/* 398 */       Player player = getPlayer(event);
/* 399 */       player.updateInventory();
/*     */     }
/*     */     
/*     */ 
/* 403 */     if ((toInv.getHolder() instanceof Player))
/*     */     {
/* 405 */       LoreMaterial material = LoreMaterial.getMaterial(droppedStack);
/*     */       
/* 407 */       if ((material != null) && ((material instanceof UnitMaterial))) {
/* 408 */         Player player = (Player)toInv.getHolder();
/*     */         
/* 410 */         if (!validateUnitUse(player, droppedStack)) {
/* 411 */           CivMessage.sendError(player, "You cannot use this unit because it does not belong to your civlization.");
/* 412 */           event.setCancelled(true);
/* 413 */           return;
/*     */         }
/*     */         
/* 416 */         DelayMoveInventoryItem task = new DelayMoveInventoryItem();
/* 417 */         task.fromSlot = event.getSlot();
/* 418 */         task.toSlot = 8;
/* 419 */         task.inv = toInv;
/* 420 */         task.playerName = player.getName();
/* 421 */         TaskMaster.syncTask(task);
/*     */         
/* 423 */         onItemToPlayer(player, droppedStack);
/* 424 */         onItemFromPlayer(player, pickedStack);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onItemToPlayer(Player player, ItemStack stack) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onItemFromPlayer(Player player, ItemStack stack) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onItemSpawn(ItemSpawnEvent event) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean onAttack(EntityDamageByEntityEvent event, ItemStack stack)
/*     */   {
/* 449 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onPlayerDeath(EntityDeathEvent event, ItemStack stack) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void onDrop(PlayerDropItemEvent event) {}
/*     */   
/*     */ 
/*     */   public void onInventoryClose(InventoryCloseEvent event)
/*     */   {
/* 463 */     ItemStack foundStack = null;
/* 464 */     ItemStack[] arrayOfItemStack; int j = (arrayOfItemStack = event.getPlayer().getInventory().getContents()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/* 465 */       if (stack != null)
/*     */       {
/*     */ 
/*     */ 
/* 469 */         if ((LoreMaterial.isCustom(stack)) && 
/* 470 */           ((LoreMaterial.getMaterial(stack) instanceof UnitMaterial))) {
/* 471 */           if (foundStack == null) {
/* 472 */             foundStack = stack;
/*     */           } else {
/* 474 */             event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), stack);
/* 475 */             event.getPlayer().getInventory().remove(stack);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\units\UnitMaterial.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
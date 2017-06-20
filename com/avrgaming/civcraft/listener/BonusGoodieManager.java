/*     */ package com.avrgaming.civcraft.listener;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigTradeGood;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.items.BonusGoodie;
/*     */ import com.avrgaming.civcraft.items.units.UnitItemMaterial;
/*     */ import com.avrgaming.civcraft.items.units.UnitMaterial;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreMaterial;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ItemFrameStorage;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.bukkit.Chunk;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.block.Chest;
/*     */ import org.bukkit.block.DoubleChest;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.entity.Item;
/*     */ import org.bukkit.entity.ItemFrame;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Cancellable;
/*     */ import org.bukkit.event.Event.Result;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.Action;
/*     */ import org.bukkit.event.entity.EntityCombustEvent;
/*     */ import org.bukkit.event.entity.ItemDespawnEvent;
/*     */ import org.bukkit.event.entity.ItemSpawnEvent;
/*     */ import org.bukkit.event.inventory.CraftItemEvent;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.player.PlayerDropItemEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEntityEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.event.player.PlayerItemHeldEvent;
/*     */ import org.bukkit.event.player.PlayerJoinEvent;
/*     */ import org.bukkit.event.player.PlayerPickupItemEvent;
/*     */ import org.bukkit.event.player.PlayerQuitEvent;
/*     */ import org.bukkit.event.world.ChunkUnloadEvent;
/*     */ import org.bukkit.inventory.CraftingInventory;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.InventoryHolder;
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
/*     */ 
/*     */ 
/*     */ public class BonusGoodieManager
/*     */   implements Listener
/*     */ {
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void OnItemHeldChange(PlayerItemHeldEvent event)
/*     */   {
/*  78 */     Inventory inv = event.getPlayer().getInventory();
/*  79 */     ItemStack stack = inv.getItem(event.getNewSlot());
/*     */     
/*  81 */     BonusGoodie goodie = CivGlobal.getBonusGoodie(stack);
/*  82 */     if (goodie == null) {
/*  83 */       return;
/*     */     }
/*     */     
/*  86 */     CivMessage.send(event.getPlayer(), "§5Bonus Goodie: §e" + goodie.getDisplayName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void onItemDespawn(ItemDespawnEvent event)
/*     */   {
/*  95 */     BonusGoodie goodie = CivGlobal.getBonusGoodie(event.getEntity().getItemStack());
/*  96 */     if (goodie == null) {
/*  97 */       return;
/*     */     }
/*     */     
/* 100 */     goodie.replenish(event.getEntity().getItemStack(), event.getEntity(), null, null);
/*     */   }
/*     */   
/*     */ 
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void OnPlayerQuit(PlayerQuitEvent event)
/*     */   {
/*     */     ItemStack[] arrayOfItemStack;
/* 108 */     int j = (arrayOfItemStack = event.getPlayer().getInventory().getContents()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/* 109 */       BonusGoodie goodie = CivGlobal.getBonusGoodie(stack);
/* 110 */       if (goodie != null) {
/* 111 */         event.getPlayer().getInventory().remove(stack);
/* 112 */         event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), stack);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void OnChunkUnload(ChunkUnloadEvent event)
/*     */   {
/*     */     Entity[] arrayOfEntity;
/*     */     
/*     */ 
/* 125 */     int j = (arrayOfEntity = event.getChunk().getEntities()).length; for (int i = 0; i < j; i++) { Entity entity = arrayOfEntity[i];
/* 126 */       if ((entity instanceof Item))
/*     */       {
/*     */ 
/*     */ 
/* 130 */         BonusGoodie goodie = CivGlobal.getBonusGoodie(((Item)entity).getItemStack());
/* 131 */         if (goodie != null)
/*     */         {
/*     */ 
/*     */ 
/* 135 */           goodie.replenish(((Item)entity).getItemStack(), (Item)entity, null, null);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void OnEntityCombustEvent(EntityCombustEvent event)
/*     */   {
/* 144 */     if (!(event.getEntity() instanceof Item)) {
/* 145 */       return;
/*     */     }
/*     */     
/* 148 */     BonusGoodie goodie = CivGlobal.getBonusGoodie(((Item)event.getEntity()).getItemStack());
/* 149 */     if (goodie == null) {
/* 150 */       return;
/*     */     }
/*     */     
/* 153 */     goodie.replenish(((Item)event.getEntity()).getItemStack(), (Item)event.getEntity(), null, null);
/*     */   }
/*     */   
/*     */ 
/*     */   @EventHandler(priority=EventPriority.LOWEST)
/*     */   public void OnInventoryClick(InventoryClickEvent event)
/*     */   {
/*     */     BonusGoodie goodie;
/*     */     
/*     */     ItemStack stack;
/*     */     
/*     */     BonusGoodie goodie;
/*     */     
/* 166 */     if (event.isShiftClick()) {
/* 167 */       ItemStack stack = event.getCurrentItem();
/* 168 */       goodie = CivGlobal.getBonusGoodie(stack);
/*     */     } else {
/* 170 */       stack = event.getCursor();
/* 171 */       goodie = CivGlobal.getBonusGoodie(stack);
/*     */     }
/*     */     
/* 174 */     if (goodie == null) {
/* 175 */       return;
/*     */     }
/*     */     
/* 178 */     InventoryView view = event.getView();
/* 179 */     int rawslot = event.getRawSlot();
/* 180 */     boolean top = view.convertSlot(rawslot) == rawslot;
/* 181 */     if (event.isShiftClick()) {
/* 182 */       top = !top;
/*     */     }
/*     */     InventoryHolder holder;
/*     */     InventoryHolder holder;
/* 186 */     if (top) {
/* 187 */       holder = view.getTopInventory().getHolder();
/*     */     } else {
/* 189 */       holder = view.getBottomInventory().getHolder();
/*     */     }
/*     */     
/* 192 */     boolean isAllowedHolder = ((holder instanceof Chest)) || ((holder instanceof DoubleChest)) || ((holder instanceof Player));
/*     */     
/* 194 */     if ((holder == null) || (!isAllowedHolder))
/*     */     {
/* 196 */       event.setCancelled(true);
/* 197 */       event.setResult(Event.Result.DENY);
/*     */       
/*     */       try
/*     */       {
/* 201 */         Player player = CivGlobal.getPlayer(event.getWhoClicked().getName());
/* 202 */         CivMessage.sendError(player, "Cannot move bonus goodie into this container.");
/*     */       }
/*     */       catch (CivException localCivException1) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 210 */       if (!event.isShiftClick()) {
/* 211 */         view.close();
/*     */       }
/*     */       
/* 214 */       return;
/*     */     }
/*     */     
/* 217 */     if (goodie.getHolder() != holder) {
/*     */       try {
/* 219 */         goodie.setHolder(holder);
/* 220 */         goodie.update(false);
/* 221 */         goodie.updateLore(stack);
/*     */       } catch (CivException e) {
/* 223 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void OnItemSpawn(ItemSpawnEvent event)
/*     */   {
/* 234 */     Item item = event.getEntity();
/*     */     
/* 236 */     BonusGoodie goodie = CivGlobal.getBonusGoodie(item.getItemStack());
/* 237 */     if (goodie == null) {
/* 238 */       return;
/*     */     }
/*     */     
/*     */ 
/* 242 */     goodie.setItem(item);
/*     */     try {
/* 244 */       goodie.update(false);
/* 245 */       goodie.updateLore(item.getItemStack());
/*     */     } catch (CivException e) {
/* 247 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void OnPlayerDropItemEvent(PlayerDropItemEvent event)
/*     */   {
/* 257 */     BonusGoodie goodie = CivGlobal.getBonusGoodie(event.getItemDrop().getItemStack());
/* 258 */     if (goodie == null) {}
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
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void OnPlayerPickupItem(PlayerPickupItemEvent event)
/*     */   {
/* 272 */     BonusGoodie goodie = CivGlobal.getBonusGoodie(event.getItem().getItemStack());
/*     */     
/* 274 */     if (goodie == null) {
/* 275 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 279 */       goodie.setHolder(event.getPlayer());
/* 280 */       goodie.update(false);
/* 281 */       goodie.updateLore(event.getItem().getItemStack());
/*     */     } catch (CivException e) {
/* 283 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @EventHandler(priority=EventPriority.LOW)
/*     */   public void OnPlayerInteractEntityEvent(PlayerInteractEntityEvent event)
/*     */   {
/* 293 */     if (!(event.getRightClicked() instanceof ItemFrame)) {
/* 294 */       return;
/*     */     }
/*     */     
/* 297 */     LoreMaterial material = LoreMaterial.getMaterial(event.getPlayer().getItemInHand());
/* 298 */     if ((material != null) && (
/* 299 */       ((material instanceof UnitItemMaterial)) || 
/* 300 */       ((material instanceof UnitMaterial))))
/*     */     {
/* 302 */       CivMessage.sendError(event.getPlayer(), "You cannot place this item into an itemframe.");
/* 303 */       return;
/*     */     }
/*     */     
/*     */ 
/* 307 */     BonusGoodie goodie = CivGlobal.getBonusGoodie(event.getPlayer().getItemInHand());
/* 308 */     ItemFrame frame = (ItemFrame)event.getRightClicked();
/* 309 */     ItemFrameStorage frameStore = CivGlobal.getProtectedItemFrame(frame.getUniqueId());
/*     */     
/* 311 */     if (goodie == null)
/*     */     {
/* 313 */       if (frameStore != null)
/*     */       {
/* 315 */         if ((frame.getItem() == null) || (ItemManager.getId(frame.getItem()) == 0)) {
/* 316 */           CivMessage.sendError(event.getPlayer(), 
/* 317 */             "You cannot place a non-trade goodie items in a trade goodie item frame.");
/* 318 */           event.setCancelled(true);
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */     }
/* 324 */     else if (frameStore == null) {
/* 325 */       CivMessage.sendError(event.getPlayer(), 
/* 326 */         "You cannot place a trade gooide in a non-trade goodie item frame.");
/* 327 */       event.setCancelled(true);
/* 328 */       return;
/*     */     }
/*     */     
/*     */ 
/* 332 */     if (frameStore != null) {
/* 333 */       onPlayerProtectedFrameInteract(event.getPlayer(), frameStore, goodie, event);
/* 334 */       event.setCancelled(true);
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
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void OnPlayerJoinEvent(PlayerJoinEvent event)
/*     */   {
/* 407 */     Inventory inv = event.getPlayer().getInventory();
/*     */     Iterator localIterator2;
/* 409 */     for (Iterator localIterator1 = CivSettings.goods.values().iterator(); localIterator1.hasNext(); 
/* 410 */         localIterator2.hasNext())
/*     */     {
/* 409 */       ConfigTradeGood good = (ConfigTradeGood)localIterator1.next();
/* 410 */       localIterator2 = inv.all(ItemManager.getMaterial(good.material)).entrySet().iterator(); continue;Map.Entry<Integer, ? extends ItemStack> itemEntry = (Map.Entry)localIterator2.next();
/* 411 */       if (good.material_data == ItemManager.getData((ItemStack)itemEntry.getValue()))
/*     */       {
/*     */ 
/*     */ 
/* 415 */         BonusGoodie goodie = CivGlobal.getBonusGoodie((ItemStack)itemEntry.getValue());
/* 416 */         if (goodie != null) {
/* 417 */           inv.remove((ItemStack)itemEntry.getValue());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static void onPlayerProtectedFrameInteract(Player player, ItemFrameStorage clickedFrame, BonusGoodie goodie, Cancellable event)
/*     */   {
/* 426 */     Resident resident = CivGlobal.getResident(player);
/* 427 */     if ((resident == null) || (!resident.hasTown()) || (resident.getCiv() != clickedFrame.getTown().getCiv())) {
/* 428 */       CivMessage.sendError(player, 
/* 429 */         "You must be a member of the owner civ to socket or unsocket trade goodies.");
/* 430 */       event.setCancelled(true);
/* 431 */       return;
/*     */     }
/*     */     
/* 434 */     if ((!clickedFrame.getTown().getMayorGroup().hasMember(resident)) && 
/* 435 */       (!clickedFrame.getTown().getAssistantGroup().hasMember(resident))) {
/* 436 */       CivMessage.sendError(player, 
/* 437 */         "You must be a mayor or assistant in order to socket or unsocket trade goodies.");
/* 438 */       event.setCancelled(true);
/* 439 */       return;
/*     */     }
/*     */     
/*     */ 
/* 443 */     ItemFrame frame = clickedFrame.getItemFrame();
/* 444 */     ItemStack stack = frame.getItem();
/*     */     
/* 446 */     if ((stack != null) && (ItemManager.getId(stack) != 0))
/*     */     {
/* 448 */       BonusGoodie goodieInFrame = CivGlobal.getBonusGoodie(frame.getItem());
/* 449 */       if (goodieInFrame != null) {
/* 450 */         clickedFrame.getTown().onGoodieRemoveFromFrame(clickedFrame, goodieInFrame);
/*     */         try
/*     */         {
/* 453 */           goodieInFrame.setFrame(clickedFrame);
/* 454 */           goodieInFrame.update(false);
/*     */         } catch (CivException e) {
/* 456 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */       
/* 460 */       player.getWorld().dropItemNaturally(frame.getLocation(), stack);
/* 461 */       frame.setItem(ItemManager.createItemStack(0, 1));
/* 462 */       CivMessage.send(player, "§7You unsocket the trade goodie from the frame.");
/* 463 */     } else if (goodie != null)
/*     */     {
/* 465 */       frame.setItem(player.getItemInHand());
/* 466 */       player.getInventory().remove(player.getItemInHand());
/* 467 */       CivMessage.send(player, "§7You socket the trade goodie into the frame");
/* 468 */       clickedFrame.getTown().onGoodiePlaceIntoFrame(clickedFrame, goodie);
/*     */       try
/*     */       {
/* 471 */         goodie.setFrame(clickedFrame);
/* 472 */         goodie.update(false);
/*     */       } catch (CivException e) {
/* 474 */         e.printStackTrace();
/*     */       }
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
/*     */   @EventHandler(priority=EventPriority.LOW)
/*     */   public void OnPlayerInteractEvent(PlayerInteractEvent event)
/*     */   {
/* 489 */     ItemStack item = event.getPlayer().getItemInHand();
/* 490 */     BonusGoodie goodie = CivGlobal.getBonusGoodie(item);
/* 491 */     if (goodie == null) {
/* 492 */       return;
/*     */     }
/*     */     
/* 495 */     if (event.getClickedBlock() == null) {
/* 496 */       event.setCancelled(true);
/* 497 */       return;
/*     */     }
/*     */     
/* 500 */     BlockCoord bcoord = new BlockCoord(event.getClickedBlock());
/* 501 */     ItemFrameStorage clickedFrame = (ItemFrameStorage)ItemFrameStorage.attachedBlockMap.get(bcoord);
/*     */     
/* 503 */     if (clickedFrame != null) {
/* 504 */       if ((clickedFrame.getItemFrame() != null) && 
/* 505 */         (clickedFrame.getItemFrame().getAttachedFace().getOppositeFace() == 
/* 506 */         event.getBlockFace())) {
/* 507 */         onPlayerProtectedFrameInteract(event.getPlayer(), clickedFrame, goodie, event);
/* 508 */         event.setCancelled(true);
/*     */       }
/*     */       
/* 511 */       return;
/*     */     }
/*     */     
/* 514 */     if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
/* 515 */       CivMessage.sendError(event.getPlayer(), "Cannot use bonus goodie as a normal item.");
/* 516 */       event.setCancelled(true);
/* 517 */       return;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void OnCraftItemEvent(CraftItemEvent event)
/*     */   {
/*     */     try
/*     */     {
/* 528 */       player = CivGlobal.getPlayer(event.getWhoClicked().getName());
/*     */     } catch (CivException e) { Player player;
/* 530 */       e.printStackTrace(); return;
/*     */     }
/*     */     Player player;
/*     */     ItemStack[] arrayOfItemStack;
/* 534 */     int j = (arrayOfItemStack = event.getInventory().getMatrix()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/* 535 */       BonusGoodie goodie = CivGlobal.getBonusGoodie(stack);
/* 536 */       if (goodie != null) {
/* 537 */         CivMessage.sendError(player, "Cannot use bonus goodies in a crafting recipe.");
/* 538 */         event.setCancelled(true);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\listener\BonusGoodieManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
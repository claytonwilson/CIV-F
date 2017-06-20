/*     */ package com.avrgaming.civcraft.trade;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Set;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.inventory.ClickType;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.inventory.InventoryCloseEvent;
/*     */ import org.bukkit.event.inventory.InventoryDragEvent;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ 
/*     */ public class TradeInventoryListener implements org.bukkit.event.Listener
/*     */ {
/*  32 */   public static HashMap<String, TradeInventoryPair> tradeInventories = new HashMap();
/*     */   
/*     */   public static final int OTHERS_SLOTS_START = 9;
/*     */   public static final int OTHERS_SLOTS_END = 18;
/*     */   public static final int MY_SLOTS_START = 27;
/*     */   public static final int MY_SLOTS_END = 36;
/*     */   public static final int SLOTS_END = 45;
/*     */   public static final int MY_SLOT_BUTTON = 44;
/*     */   public static final int OTHER_SLOT_BUTTON = 8;
/*     */   public static final int MY_COINS_DOWN = 36;
/*     */   public static final int MY_COINS_UP = 37;
/*     */   public static final int MY_COIN_OFFER = 43;
/*     */   public static final int OTHER_COIN_OFFER = 7;
/*     */   
/*     */   public static String getTradeInventoryKey(Resident resident)
/*     */   {
/*  48 */     return resident.getName() + ":inventroy";
/*     */   }
/*     */   
/*     */   public class SyncInventoryChange implements Runnable
/*     */   {
/*     */     int sourceSlot;
/*     */     int destSlot;
/*     */     Inventory sourceInventory;
/*     */     Resident otherResident;
/*     */     Inventory otherInventory;
/*     */     
/*     */     public SyncInventoryChange(int sourceSlot, int destSlot, Inventory sourceInventory, Resident otherResident, Inventory otherInventory) {
/*  60 */       this.sourceInventory = sourceInventory;
/*  61 */       this.sourceSlot = sourceSlot;
/*  62 */       this.destSlot = destSlot;
/*  63 */       this.otherResident = otherResident;
/*  64 */       this.otherInventory = otherInventory;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/*     */       try {
/*  70 */         Player otherPlayer = CivGlobal.getPlayer(this.otherResident);
/*  71 */         if (otherPlayer.getOpenInventory() != this.otherInventory) {
/*  72 */           return;
/*     */         }
/*     */         
/*  75 */         if (this.otherInventory != null) {
/*  76 */           this.otherInventory.setItem(this.destSlot, this.sourceInventory.getItem(this.sourceSlot));
/*     */         }
/*     */       }
/*     */       catch (CivException localCivException) {}
/*     */     }
/*     */   }
/*     */   
/*     */   public class SyncInventoryChangeAll implements Runnable
/*     */   {
/*     */     Inventory sourceInventory;
/*     */     Resident otherResident;
/*     */     Inventory otherInventory;
/*     */     
/*     */     public SyncInventoryChangeAll(Inventory src, Resident other, Inventory otherInv) {
/*  90 */       this.sourceInventory = src;
/*  91 */       this.otherResident = other;
/*  92 */       this.otherInventory = otherInv;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/*     */       try {
/*  98 */         Player otherPlayer = CivGlobal.getPlayer(this.otherResident);
/*  99 */         if (otherPlayer.getOpenInventory() != this.otherInventory) {
/* 100 */           return;
/*     */         }
/*     */         
/* 103 */         if (this.otherInventory != null) {
/* 104 */           int k = 9;
/* 105 */           for (int i = 27; i < 36; i++) {
/* 106 */             this.otherInventory.setItem(k, this.sourceInventory.getItem(i));
/* 107 */             k++;
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (CivException localCivException) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean handleSlotChange(int slot, TradeInventoryPair pair)
/*     */   {
/* 119 */     if (slot <= 18) {
/* 120 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 124 */     if ((slot >= 27) && (slot <= 36)) {
/* 125 */       int relativeSlot = slot % 9;
/* 126 */       TaskMaster.syncTask(new SyncInventoryChange(slot, 9 + relativeSlot, pair.inv, pair.otherResident, pair.otherInv));
/*     */     }
/*     */     
/* 129 */     return true;
/*     */   }
/*     */   
/*     */   public void handleShiftClick(InventoryClickEvent event, Player player, TradeInventoryPair pair)
/*     */   {
/* 134 */     if (event.getRawSlot() > 45)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 140 */       Inventory tempInv = org.bukkit.Bukkit.createInventory(event.getWhoClicked(), 9);
/*     */       
/* 142 */       int k = 0;
/* 143 */       for (int i = 27; i < 36; i++) {
/* 144 */         tempInv.setItem(k, event.getInventory().getItem(i));
/* 145 */         k++;
/*     */       }
/*     */       
/*     */ 
/* 149 */       HashMap<Integer, ItemStack> leftovers = tempInv.addItem(new ItemStack[] { event.getCurrentItem() });
/*     */       
/*     */ 
/* 152 */       k = 0;
/* 153 */       for (int i = 27; i < 36; i++) {
/* 154 */         event.getInventory().setItem(i, tempInv.getItem(k));
/* 155 */         k++;
/*     */       }
/*     */       
/*     */ 
/* 159 */       player.getInventory().setItem(event.getSlot(), null);
/*     */       
/*     */ 
/* 162 */       for (ItemStack stack : leftovers.values()) {
/* 163 */         player.getInventory().addItem(new ItemStack[] { stack });
/*     */       }
/*     */       
/*     */ 
/* 167 */       TaskMaster.syncTask(new SyncInventoryChangeAll(pair.inv, pair.otherResident, pair.otherInv));
/* 168 */       event.setCancelled(true);
/* 169 */       return;
/*     */     }
/*     */     
/* 172 */     if (event.getRawSlot() < 18)
/*     */     {
/* 174 */       event.setCancelled(true);
/* 175 */       return;
/*     */     }
/*     */     
/* 178 */     TaskMaster.syncTask(new SyncInventoryChangeAll(pair.inv, pair.otherResident, pair.otherInv));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void handleDoubleClick(InventoryClickEvent event, Player player, TradeInventoryPair pair)
/*     */   {
/* 186 */     TaskMaster.syncTask(new SyncInventoryChangeAll(pair.inv, pair.otherResident, pair.otherInv));
/*     */   }
/*     */   
/*     */ 
/*     */   private void handleCoinChange(TradeInventoryPair pair, TradeInventoryPair otherPair, double change)
/*     */   {
/* 192 */     if (change > 0.0D)
/*     */     {
/* 194 */       if (pair.resident.getTreasury().getBalance() < pair.coins + change) {
/* 195 */         pair.coins = pair.resident.getTreasury().getBalance();
/* 196 */         otherPair.otherCoins = pair.coins;
/*     */       }
/*     */       else {
/* 199 */         pair.coins += change;
/* 200 */         otherPair.otherCoins = pair.coins;
/*     */       }
/*     */     }
/*     */     else {
/* 204 */       change *= -1.0D;
/* 205 */       if (change > pair.coins)
/*     */       {
/* 207 */         pair.coins = 0.0D;
/* 208 */         otherPair.otherCoins = 0.0D;
/*     */       }
/*     */       else {
/* 211 */         pair.coins -= change;
/* 212 */         otherPair.otherCoins = pair.coins;
/*     */       }
/*     */     }
/*     */     
/*     */     ItemStack guiStack;
/*     */     ItemStack guiStack;
/* 218 */     if (pair.coins == 0.0D) {
/* 219 */       guiStack = LoreGuiItem.build("Coins Offered", 
/* 220 */         ItemManager.getId(Material.NETHER_BRICK_ITEM), 0, new String[] {
/* 221 */         "§e0 Coins" });
/*     */     } else {
/* 223 */       guiStack = LoreGuiItem.build("Coins Offered", 
/* 224 */         ItemManager.getId(Material.GOLD_INGOT), 0, new String[] {
/* 225 */         "§e" + pair.coins + " Coins" });
/*     */     }
/* 227 */     pair.inv.setItem(43, guiStack);
/*     */     
/*     */ 
/* 230 */     otherPair.inv.setItem(7, guiStack);
/*     */   }
/*     */   
/*     */ 
/*     */   public void markTradeValid(TradeInventoryPair pair)
/*     */   {
/* 236 */     pair.valid = true;
/* 237 */     ItemStack guiStack = LoreGuiItem.build("Your Confirm", 
/* 238 */       35, 5, new String[] {
/* 239 */       "§6<Click to Unconfirm Trade>" });
/* 240 */     pair.inv.setItem(44, guiStack);
/*     */     
/* 242 */     guiStack = LoreGuiItem.build("Your Confirm", 
/* 243 */       35, 5, new String[] {
/* 244 */       "§b" + pair.otherResident.getName() + "§a" + " has confirmed the trade." });
/* 245 */     pair.otherInv.setItem(8, guiStack);
/*     */   }
/*     */   
/*     */   public void markTradeInvalid(TradeInventoryPair pair)
/*     */   {
/* 250 */     pair.valid = false;
/* 251 */     ItemStack guiStack = LoreGuiItem.build("Your Confirm", 
/* 252 */       35, 14, new String[] {
/* 253 */       "§6<Click to Confirm Trade>" });
/*     */     
/* 255 */     pair.inv.setItem(44, guiStack);
/*     */     
/* 257 */     ItemStack guiStack2 = LoreGuiItem.build(pair.otherResident.getName() + " Confirm", 
/* 258 */       35, 14, new String[] {
/* 259 */       "§aWaiting for §b" + pair.otherResident.getName(), 
/* 260 */       "§7to confirm this trade." });
/* 261 */     pair.otherInv.setItem(8, guiStack2);
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.LOW)
/*     */   public void onInventoryClickEvent(InventoryClickEvent event)
/*     */   {
/* 267 */     if (!(event.getWhoClicked() instanceof Player)) {
/* 268 */       return;
/*     */     }
/* 270 */     Player player = (Player)event.getWhoClicked();
/* 271 */     Resident resident = CivGlobal.getResident(player);
/* 272 */     TradeInventoryPair pair = (TradeInventoryPair)tradeInventories.get(getTradeInventoryKey(resident));
/* 273 */     if (pair == null) {
/* 274 */       return;
/*     */     }
/* 276 */     TradeInventoryPair otherPair = (TradeInventoryPair)tradeInventories.get(getTradeInventoryKey(pair.otherResident));
/* 277 */     if (otherPair == null) {
/* 278 */       return;
/*     */     }
/*     */     
/* 281 */     Inventory savedTradeInventory = pair.inv;
/* 282 */     if (savedTradeInventory == null) {
/* 283 */       return;
/*     */     }
/*     */     
/* 286 */     if (!savedTradeInventory.getName().equals(event.getInventory().getName())) {
/* 287 */       return;
/*     */     }
/*     */     
/*     */ 
/* 291 */     if (event.getRawSlot() == 44) {
/* 292 */       ItemStack button = event.getInventory().getItem(44);
/* 293 */       if (ItemManager.getData(button) == 14)
/*     */       {
/* 295 */         markTradeValid(pair);
/*     */         
/* 297 */         if ((pair.valid) && (otherPair.valid)) {
/*     */           try {
/* 299 */             completeTransaction(pair, otherPair);
/*     */           } catch (CivException e) {
/* 301 */             e.printStackTrace();
/* 302 */             CivMessage.sendError(pair.resident, e.getMessage());
/* 303 */             CivMessage.sendError(pair.otherResident, e.getMessage());
/* 304 */             player.closeInventory();
/*     */             try {
/* 306 */               Player otherPlayer = CivGlobal.getPlayer(pair.otherResident);
/* 307 */               otherPlayer.closeInventory();
/*     */             } catch (CivException e2) {
/* 309 */               e2.printStackTrace();
/*     */             }
/*     */             
/* 312 */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 318 */         markTradeInvalid(pair);
/*     */       }
/* 320 */       return; }
/* 321 */     if (event.getRawSlot() == 36) {
/* 322 */       if (event.isShiftClick()) {
/* 323 */         handleCoinChange(pair, otherPair, -1000.0D);
/*     */       } else {
/* 325 */         handleCoinChange(pair, otherPair, -100.0D);
/*     */       }
/* 327 */       event.setCancelled(true);
/* 328 */       return; }
/* 329 */     if (event.getRawSlot() == 37) {
/* 330 */       if (event.isShiftClick()) {
/* 331 */         handleCoinChange(pair, otherPair, 1000.0D);
/*     */       } else {
/* 333 */         handleCoinChange(pair, otherPair, 100.0D);
/*     */       }
/* 335 */       event.setCancelled(true);
/* 336 */       return;
/*     */     }
/*     */     
/* 339 */     if ((pair.valid) || (otherPair.valid))
/*     */     {
/* 341 */       markTradeInvalid(pair);
/* 342 */       player.updateInventory();
/* 343 */       markTradeInvalid(otherPair);
/*     */       try {
/* 345 */         Player otherPlayer = CivGlobal.getPlayer(pair.otherResident);
/* 346 */         otherPlayer.updateInventory();
/*     */       }
/*     */       catch (CivException localCivException1) {}
/* 349 */       event.setCancelled(true);
/* 350 */       return;
/*     */     }
/*     */     
/*     */ 
/* 354 */     if ((event.getClick().equals(ClickType.SHIFT_LEFT)) || (event.getClick().equals(ClickType.SHIFT_RIGHT)))
/*     */     {
/* 356 */       handleShiftClick(event, player, pair);
/* 357 */       return;
/*     */     }
/*     */     
/* 360 */     if (event.getClick().equals(ClickType.DOUBLE_CLICK)) {
/* 361 */       handleDoubleClick(event, player, pair);
/*     */     }
/*     */     
/*     */ 
/* 365 */     if (!handleSlotChange(event.getRawSlot(), pair)) {
/* 366 */       event.setCancelled(true);
/* 367 */       return;
/*     */     }
/*     */   }
/*     */   
/*     */   private void completeTransaction(TradeInventoryPair pair, TradeInventoryPair otherPair) throws CivException
/*     */   {
/* 373 */     Player us = CivGlobal.getPlayer(pair.resident);
/* 374 */     Player them = CivGlobal.getPlayer(pair.otherResident);
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 379 */       tradeInventories.remove(getTradeInventoryKey(pair.resident));
/* 380 */       tradeInventories.remove(getTradeInventoryKey(otherPair.resident));
/*     */       
/* 382 */       LinkedList<ItemStack> myStuff = new LinkedList();
/* 383 */       LinkedList<ItemStack> theirStuff = new LinkedList();
/*     */       
/* 385 */       int k = 9;
/* 386 */       for (int i = 27; i < 36; k++)
/*     */       {
/*     */ 
/* 389 */         ItemStack stack = pair.inv.getItem(i);
/* 390 */         ItemStack stack2 = otherPair.inv.getItem(k);
/*     */         
/* 392 */         if ((stack != null) || (stack2 != null))
/*     */         {
/*     */ 
/*     */ 
/* 396 */           if (((stack == null) && (stack2 != null)) || ((stack != null) && (stack2 == null))) {
/* 397 */             CivLog.error("Mismatch. One stack was null. stack:" + stack + " stack2:" + stack2 + " i:" + i + " vs k:" + k);
/* 398 */             throw new CivException("Inventory mismatch");
/*     */           }
/*     */           
/* 401 */           if (((stack == null) && (stack2 != null)) || ((stack != null) && (stack2 == null))) {
/* 402 */             CivLog.error("Mismatch. One stack was null. stack:" + stack + " stack2:" + stack2);
/* 403 */             throw new CivException("Inventory mismatch");
/*     */           }
/*     */           
/* 406 */           if (!stack.toString().equals(stack2.toString())) {
/* 407 */             CivLog.error("Is Mine Equal to Theirs?");
/* 408 */             CivLog.error("Position i:" + i + " stack:" + stack);
/* 409 */             CivLog.error("Position k:" + k + " stack2:" + stack2);
/* 410 */             throw new CivException("Inventory mismatch.");
/*     */           }
/*     */           
/* 413 */           if (stack != null) {
/* 414 */             myStuff.add(stack);
/*     */           }
/*     */         }
/* 386 */         i++;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 418 */       k = 27;
/* 419 */       for (int i = 9; i < 18; k++)
/*     */       {
/* 421 */         stack = pair.inv.getItem(i);
/* 422 */         ItemStack stack2 = otherPair.inv.getItem(k);
/*     */         
/* 424 */         if ((stack != null) || (stack2 != null))
/*     */         {
/*     */ 
/*     */ 
/* 428 */           if (((stack == null) && (stack2 != null)) || ((stack != null) && (stack2 == null))) {
/* 429 */             CivLog.error("Mismatch. One stack was null. stack:" + stack + " stack2:" + stack2 + " i:" + i + " vs k:" + k);
/* 430 */             throw new CivException("Inventory mismatch");
/*     */           }
/*     */           
/* 433 */           if (!stack.toString().equals(stack2.toString())) {
/* 434 */             CivLog.error("Is Theirs Equal to Mine?");
/* 435 */             CivLog.error("Position i:" + i + " stack:" + stack);
/* 436 */             CivLog.error("Position k:" + k + " stack2:" + stack2);
/* 437 */             throw new CivException("Inventory mismatch.");
/*     */           }
/*     */           
/* 440 */           if (stack != null) {
/* 441 */             theirStuff.add(stack);
/*     */           }
/*     */         }
/* 419 */         i++;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 445 */       if (pair.coins != otherPair.otherCoins) {
/* 446 */         CivLog.error("pair.coins = " + pair.coins);
/* 447 */         CivLog.error("otherPair.otherCoins = " + otherPair.otherCoins);
/* 448 */         throw new CivException("Coin mismatch...");
/*     */       }
/*     */       
/* 451 */       if (otherPair.coins != pair.otherCoins) {
/* 452 */         CivLog.error("otherPair.coins = " + otherPair.coins);
/* 453 */         CivLog.error("pair.coins = " + pair.coins);
/* 454 */         new CivException("Coin mismatch...");
/*     */       }
/*     */       
/* 457 */       if ((pair.coins < 0.0D) || (pair.otherCoins < 0.0D) || (otherPair.coins < 0.0D) || (otherPair.otherCoins < 0.0D)) {
/* 458 */         throw new CivException("Coin amount invalid.");
/*     */       }
/*     */       
/* 461 */       if (!pair.resident.getTreasury().hasEnough(pair.coins)) {
/* 462 */         CivMessage.sendError(us, pair.resident.getName() + " doesnt have enough coins!");
/* 463 */         CivMessage.sendError(them, pair.resident.getName() + " doesnt have enough coins!");
/* 464 */         us.closeInventory();
/* 465 */         them.closeInventory();
/* 466 */         return;
/*     */       }
/*     */       
/* 469 */       if (!pair.otherResident.getTreasury().hasEnough(pair.otherCoins)) {
/* 470 */         CivMessage.sendError(us, pair.otherResident.getName() + " doesnt have enough coins!");
/* 471 */         CivMessage.sendError(them, pair.otherResident.getName() + " doesnt have enough coins!");
/* 472 */         us.closeInventory();
/* 473 */         them.closeInventory();
/* 474 */         return;
/*     */       }
/*     */       
/* 477 */       if (pair.coins != 0.0D) {
/* 478 */         pair.resident.getTreasury().withdraw(pair.coins);
/* 479 */         pair.otherResident.getTreasury().deposit(pair.coins);
/* 480 */         CivMessage.sendSuccess(pair.resident, "Gave §c" + pair.coins + " to " + pair.otherResident.getName());
/* 481 */         CivMessage.sendSuccess(pair.otherResident, "Recieved §e" + pair.coins + " from " + pair.resident.getName());
/*     */       }
/*     */       
/* 484 */       if (pair.otherCoins != 0.0D) {
/* 485 */         pair.otherResident.getTreasury().withdraw(pair.otherCoins);
/* 486 */         pair.resident.getTreasury().deposit(pair.otherCoins);
/* 487 */         CivMessage.sendSuccess(pair.resident, "Recieved §e" + pair.otherCoins + " from " + pair.otherResident.getName());
/* 488 */         CivMessage.sendSuccess(pair.otherResident, "Gave §c" + pair.otherCoins + " to " + pair.resident.getName());
/*     */       }
/*     */       
/*     */       Iterator localIterator;
/* 492 */       for (ItemStack stack = theirStuff.iterator(); stack.hasNext(); 
/*     */           
/* 494 */           localIterator.hasNext())
/*     */       {
/* 492 */         ItemStack is = (ItemStack)stack.next();
/* 493 */         HashMap<Integer, ItemStack> leftovers = us.getInventory().addItem(new ItemStack[] { is });
/* 494 */         localIterator = leftovers.values().iterator(); continue;ItemStack stack = (ItemStack)localIterator.next();
/* 495 */         us.getPlayer().getWorld().dropItem(us.getLocation(), stack);
/*     */       }
/*     */       
/*     */ 
/* 499 */       for (stack = myStuff.iterator(); stack.hasNext(); 
/*     */           
/* 501 */           localIterator.hasNext())
/*     */       {
/* 499 */         ItemStack is = (ItemStack)stack.next();
/* 500 */         HashMap<Integer, ItemStack> leftovers = them.getInventory().addItem(new ItemStack[] { is });
/* 501 */         localIterator = leftovers.values().iterator(); continue;ItemStack stack = (ItemStack)localIterator.next();
/* 502 */         them.getPlayer().getWorld().dropItem(them.getLocation(), stack);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 507 */       CivMessage.sendSuccess(us, "Transaction Successful.");
/* 508 */       CivMessage.sendSuccess(them, "Transaction Successful.");
/*     */     } finally {
/* 510 */       us.closeInventory();
/* 511 */       them.closeInventory();
/*     */     }
/* 510 */     us.closeInventory();
/* 511 */     them.closeInventory();
/*     */   }
/*     */   
/*     */ 
/*     */   @EventHandler(priority=EventPriority.LOW)
/*     */   public void onInventoryDragEvent(InventoryDragEvent event)
/*     */   {
/* 518 */     if (!(event.getWhoClicked() instanceof Player)) {
/* 519 */       return;
/*     */     }
/* 521 */     Player player = (Player)event.getWhoClicked();
/* 522 */     Resident resident = CivGlobal.getResident(player);
/* 523 */     TradeInventoryPair pair = (TradeInventoryPair)tradeInventories.get(getTradeInventoryKey(resident));
/* 524 */     if (pair == null) {
/* 525 */       return;
/*     */     }
/*     */     
/* 528 */     Inventory savedTradeInventory = pair.inv;
/* 529 */     if (savedTradeInventory == null) {
/* 530 */       return;
/*     */     }
/*     */     
/* 533 */     if (!savedTradeInventory.getName().equals(event.getInventory().getName())) {
/* 534 */       return;
/*     */     }
/*     */     
/* 537 */     for (Iterator localIterator = event.getRawSlots().iterator(); localIterator.hasNext();) { int slot = ((Integer)localIterator.next()).intValue();
/* 538 */       if (!handleSlotChange(slot, pair)) {
/* 539 */         event.setCancelled(true);
/* 540 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.LOW)
/*     */   public void onInventoryClose(InventoryCloseEvent event)
/*     */   {
/* 548 */     if (!(event.getPlayer() instanceof Player)) {
/* 549 */       return;
/*     */     }
/* 551 */     Player player = (Player)event.getPlayer();
/* 552 */     Resident resident = CivGlobal.getResident(player);
/*     */     
/* 554 */     if (resident == null) {
/* 555 */       CivLog.error("Got InvCloseEvent with player name:" + player.getName() + " but could not find resident object?");
/* 556 */       return;
/*     */     }
/*     */     
/* 559 */     TradeInventoryPair pair = (TradeInventoryPair)tradeInventories.get(getTradeInventoryKey(resident));
/* 560 */     if (pair == null) {
/* 561 */       return;
/*     */     }
/*     */     
/* 564 */     Inventory savedTradeInventory = pair.inv;
/* 565 */     if (savedTradeInventory == null) {
/* 566 */       return;
/*     */     }
/*     */     
/* 569 */     if (!savedTradeInventory.getName().equals(event.getInventory().getName())) {
/* 570 */       return;
/*     */     }
/*     */     
/*     */ 
/* 574 */     for (int i = 27; i < 36; i++) {
/* 575 */       ItemStack stack = pair.inv.getItem(i);
/* 576 */       if (stack != null)
/*     */       {
/*     */ 
/*     */ 
/* 580 */         HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(new ItemStack[] { stack });
/* 581 */         for (ItemStack left : leftovers.values()) {
/* 582 */           player.getWorld().dropItem(player.getLocation(), left);
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
/* 604 */     TaskMaster.syncTask(new Runnable()
/*     */     {
/*     */       String playerName;
/*     */       
/*     */       public void run()
/*     */       {
/*     */         try
/*     */         {
/* 597 */           Player player = CivGlobal.getPlayer(this.playerName);
/* 598 */           player.updateInventory();
/*     */ 
/*     */         }
/*     */         catch (CivException localCivException) {}
/*     */       }
/*     */       
/*     */ 
/* 605 */     });
/* 606 */     tradeInventories.remove(getTradeInventoryKey(resident));
/*     */     
/*     */ 
/* 609 */     TradeInventoryPair otherPair = (TradeInventoryPair)tradeInventories.get(getTradeInventoryKey(pair.otherResident));
/* 610 */     if (otherPair != null) {
/* 611 */       ItemStack guiStack = LoreGuiItem.build(pair.otherResident.getName() + " Confirm", 
/* 612 */         7, 0, new String[] {
/* 613 */         "§7" + player.getName() + " has closed the trading window." });
/* 614 */       for (int i = 9; i < 18; i++) {
/* 615 */         otherPair.inv.setItem(i, guiStack);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\trade\TradeInventoryListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
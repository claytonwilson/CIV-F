/*     */ package com.avrgaming.civcraft.listener;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.player.PlayerDropItemEvent;
/*     */ import org.bukkit.event.player.PlayerItemHeldEvent;
/*     */ import org.bukkit.event.player.PlayerQuitEvent;
/*     */ import org.bukkit.inventory.ItemStack;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MarkerPlacementManager
/*     */   implements Listener
/*     */ {
/*  45 */   private static HashMap<String, Structure> playersInPlacementMode = new HashMap();
/*  46 */   private static HashMap<String, ArrayList<Location>> markers = new HashMap();
/*     */   
/*     */   public static void addToPlacementMode(Player player, Structure structure, String markerName)
/*     */     throws CivException
/*     */   {
/*  51 */     if ((player.getItemInHand() != null) && (ItemManager.getId(player.getItemInHand()) != 0)) {
/*  52 */       throw new CivException("You must not be holding anything to enter placement mode.");
/*     */     }
/*     */     
/*  55 */     playersInPlacementMode.put(player.getName(), structure);
/*  56 */     markers.put(player.getName(), new ArrayList());
/*     */     
/*  58 */     ItemStack stack = ItemManager.createItemStack(75, 2);
/*  59 */     ItemMeta meta = stack.getItemMeta();
/*  60 */     if (markerName != null) {
/*  61 */       meta.setDisplayName(markerName);
/*     */     } else {
/*  63 */       meta.setDisplayName("Marker");
/*     */     }
/*  65 */     stack.setItemMeta(meta);
/*  66 */     player.setItemInHand(stack);
/*     */     
/*  68 */     CivMessage.send(player, "You're now in placement mode for a " + structure.getDisplayName());
/*     */   }
/*     */   
/*     */   public static void removeFromPlacementMode(Player player, boolean canceled) {
/*  72 */     if (canceled) {
/*  73 */       Structure struct = (Structure)playersInPlacementMode.get(player.getName());
/*  74 */       struct.getTown().removeStructure(struct);
/*  75 */       CivGlobal.removeStructure(struct);
/*     */     }
/*  77 */     playersInPlacementMode.remove(player.getName());
/*  78 */     markers.remove(player.getName());
/*  79 */     player.setItemInHand(ItemManager.createItemStack(0, 1));
/*  80 */     CivMessage.send(player, "You're no longer in placement mode.");
/*     */   }
/*     */   
/*     */   public static boolean isPlayerInPlacementMode(Player player) {
/*  84 */     return isPlayerInPlacementMode(player.getName());
/*     */   }
/*     */   
/*     */   public static boolean isPlayerInPlacementMode(String name) {
/*  88 */     return playersInPlacementMode.containsKey(name);
/*     */   }
/*     */   
/*     */   public static void setMarker(Player player, Location location) throws CivException {
/*  92 */     ArrayList<Location> locs = (ArrayList)markers.get(player.getName());
/*     */     
/*  94 */     Structure struct = (Structure)playersInPlacementMode.get(player.getName());
/*  95 */     int amount = player.getItemInHand().getAmount();
/*  96 */     if (amount == 1) {
/*  97 */       player.setItemInHand(null);
/*     */     } else {
/*  99 */       player.getItemInHand().setAmount(amount - 1);
/*     */     }
/*     */     
/* 102 */     locs.add(location);
/* 103 */     struct.onMarkerPlacement(player, location, locs);
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void OnItemHeldChange(PlayerItemHeldEvent event)
/*     */   {
/* 109 */     if (isPlayerInPlacementMode(event.getPlayer())) {
/* 110 */       removeFromPlacementMode(event.getPlayer(), true);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void OnPlayerDropItemEvent(PlayerDropItemEvent event) {
/* 116 */     if (isPlayerInPlacementMode(event.getPlayer())) {
/* 117 */       event.setCancelled(true);
/* 118 */       removeFromPlacementMode(event.getPlayer(), true);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void OnPlayerQuit(PlayerQuitEvent event) {
/* 124 */     if (isPlayerInPlacementMode(event.getPlayer())) {
/* 125 */       removeFromPlacementMode(event.getPlayer(), true);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void OnInventoryClick(InventoryClickEvent event)
/*     */   {
/*     */     try {
/* 133 */       player = CivGlobal.getPlayer(event.getWhoClicked().getName());
/*     */     } catch (CivException e) {
/*     */       Player player;
/*     */       return;
/*     */     }
/*     */     Player player;
/* 139 */     if (isPlayerInPlacementMode(player)) {
/* 140 */       removeFromPlacementMode(player, true);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\listener\MarkerPlacementManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
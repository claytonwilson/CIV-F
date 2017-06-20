/*     */ package com.avrgaming.civcraft.lorestorage;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.inventory.InventoryDragEvent;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.InventoryView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoreGuiItemListener
/*     */   implements Listener
/*     */ {
/*  32 */   public static HashMap<String, Inventory> guiInventories = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @EventHandler(priority=EventPriority.LOWEST)
/*     */   public void OnInventoryClick(InventoryClickEvent event)
/*     */   {
/*  41 */     if ((LoreGuiItem.isGUIItem(event.getCurrentItem())) || 
/*  42 */       (LoreGuiItem.isGUIItem(event.getCursor()))) {
/*  43 */       event.setCancelled(true);
/*     */       
/*  45 */       if (event.getCurrentItem() != null) {
/*  46 */         String action = LoreGuiItem.getAction(event.getCurrentItem());
/*  47 */         if (action != null) {
/*  48 */           LoreGuiItem.processAction(action, event.getCurrentItem(), event);
/*     */         }
/*  50 */         return;
/*     */       }
/*     */       
/*  53 */       if (event.getCursor() != null) {
/*  54 */         String action = LoreGuiItem.getAction(event.getCursor());
/*  55 */         if (action != null) {
/*  56 */           LoreGuiItem.processAction(action, event.getCursor(), event);
/*     */         }
/*  58 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @EventHandler(priority=EventPriority.LOW)
/*     */   public void OnInventoryClickSecondPhase(InventoryClickEvent event)
/*     */   {
/*  70 */     if (event.isCancelled()) {
/*  71 */       return;
/*     */     }
/*     */     
/*  74 */     if (event.getRawSlot() < event.getView().getTopInventory().getSize()) {
/*  75 */       if (guiInventories.containsKey(event.getView().getTopInventory().getName())) {
/*  76 */         event.setCancelled(true);
/*     */       }
/*     */     }
/*  79 */     else if ((event.isShiftClick()) && 
/*  80 */       (guiInventories.containsKey(event.getView().getTopInventory().getName()))) {
/*  81 */       event.setCancelled(true);
/*  82 */       return;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean isGUIInventory(Inventory inv)
/*     */   {
/*  89 */     return guiInventories.containsKey(inv.getName());
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.LOW)
/*     */   public void OnInventoryDragEvent(InventoryDragEvent event) {
/*  94 */     if (event.isCancelled()) {
/*  95 */       return;
/*     */     }
/*     */     
/*  98 */     for (Iterator localIterator = event.getRawSlots().iterator(); localIterator.hasNext();) { int slot = ((Integer)localIterator.next()).intValue();
/*  99 */       if ((slot < event.getView().getTopInventory().getSize()) && 
/* 100 */         (guiInventories.containsKey(event.getView().getTopInventory().getName()))) {
/* 101 */         event.setCancelled(true);
/* 102 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\lorestorage\LoreGuiItemListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.avrgaming.civcraft.items.units;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreMaterial;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import java.util.List;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.entity.Item;
/*     */ import org.bukkit.entity.Player;
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
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnitItemMaterial
/*     */   extends LoreMaterial
/*     */ {
/*  49 */   private UnitMaterial parent = null;
/*  50 */   private int socketSlot = 0;
/*     */   
/*     */   public UnitItemMaterial(String id, int minecraftId, short damage) {
/*  53 */     super(id, minecraftId, damage);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onBlockBreak(BlockBreakEvent arg0) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void onBlockDamage(BlockDamageEvent arg0) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void onBlockInteract(PlayerInteractEvent arg0) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void onBlockPlaced(BlockPlaceEvent arg0) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void onHit(EntityDamageByEntityEvent arg0) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void onHold(PlayerItemHeldEvent event) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void onInteract(PlayerInteractEvent arg0) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void onInteractEntity(PlayerInteractEntityEvent arg0) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void onItemDrop(PlayerDropItemEvent event)
/*     */   {
/*  93 */     CivMessage.sendError(event.getPlayer(), "Cannot drop this item, belongs to the unit you are currently assigned.");
/*  94 */     event.setCancelled(true);
/*  95 */     event.getPlayer().updateInventory();
/*     */   }
/*     */   
/*     */   public void onItemCraft(CraftItemEvent event)
/*     */   {
/*     */     try {
/* 101 */       CivMessage.sendError(CivGlobal.getPlayer(event.getWhoClicked().getName()), "Cannot craft with a unit item.");
/*     */     }
/*     */     catch (CivException localCivException) {}
/*     */     
/* 105 */     event.setCancelled(true);
/*     */   }
/*     */   
/*     */ 
/*     */   public void onItemPickup(PlayerPickupItemEvent event)
/*     */   {
/* 111 */     event.setCancelled(true);
/* 112 */     event.getItem().remove();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onInvItemPickup(InventoryClickEvent event, Inventory fromInv, ItemStack stack) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onInvItemDrop(InventoryClickEvent event, Inventory toInv, ItemStack stack) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onInvShiftClick(InventoryClickEvent event, Inventory fromInv, Inventory toInv, ItemStack stack) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onInvItemSwap(InventoryClickEvent event, Inventory toInv, ItemStack droppedStack, ItemStack pickedStack) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public UnitMaterial getParent()
/*     */   {
/* 141 */     return this.parent;
/*     */   }
/*     */   
/*     */   public void setParent(UnitMaterial parent) {
/* 145 */     this.parent = parent;
/*     */   }
/*     */   
/*     */ 
/*     */   public void onItemSpawn(ItemSpawnEvent event)
/*     */   {
/* 151 */     event.setCancelled(true);
/*     */   }
/*     */   
/*     */   public void setLoreArray(List<String> lore)
/*     */   {
/* 156 */     super.setLore("");
/* 157 */     for (String str : lore) {
/* 158 */       addLore(str);
/*     */     }
/*     */     
/* 161 */     addLore("§6Soulbound");
/*     */   }
/*     */   
/*     */   public int getSocketSlot() {
/* 165 */     return this.socketSlot;
/*     */   }
/*     */   
/*     */   public void setSocketSlot(int socketSlot) {
/* 169 */     this.socketSlot = socketSlot;
/*     */   }
/*     */   
/*     */   public boolean onAttack(EntityDamageByEntityEvent event, ItemStack stack)
/*     */   {
/* 174 */     return false;
/*     */   }
/*     */   
/*     */   public void onPlayerDeath(EntityDeathEvent event, ItemStack stack) {}
/*     */   
/*     */   public void onDrop(PlayerDropItemEvent event) {}
/*     */   
/*     */   public void onInventoryClose(InventoryCloseEvent event) {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\units\UnitItemMaterial.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
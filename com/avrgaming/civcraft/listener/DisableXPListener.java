/*    */ package com.avrgaming.civcraft.listener;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.EconObject;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.util.ItemManager;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.block.Action;
/*    */ import org.bukkit.event.entity.ExpBottleEvent;
/*    */ import org.bukkit.event.entity.ItemSpawnEvent;
/*    */ import org.bukkit.event.player.PlayerExpChangeEvent;
/*    */ import org.bukkit.event.player.PlayerInteractEvent;
/*    */ 
/*    */ public class DisableXPListener
/*    */   implements Listener
/*    */ {
/*    */   @EventHandler(priority=EventPriority.LOW)
/*    */   public void onExpBottleEvent(ExpBottleEvent event)
/*    */   {
/* 25 */     event.setExperience(0);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @EventHandler(priority=EventPriority.LOW)
/*    */   public void onItemSpawnEvent(ItemSpawnEvent event) {}
/*    */   
/*    */ 
/*    */   @EventHandler(priority=EventPriority.LOW)
/*    */   public void onPlayerInteractEvent(PlayerInteractEvent event)
/*    */   {
/* 37 */     if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
/* 38 */       return;
/*    */     }
/*    */     
/* 41 */     if ((event.getClickedBlock() == null) || (ItemManager.getId(event.getClickedBlock()) == 0)) {
/* 42 */       return;
/*    */     }
/*    */     
/* 45 */     Block block = event.getClickedBlock();
/*    */     
/* 47 */     if (block.getType().equals(Material.ENCHANTMENT_TABLE)) {
/* 48 */       CivMessage.sendError(event.getPlayer(), "Cannot use enchantment tables. XP and Levels disabled in CivCraft.");
/* 49 */       event.setCancelled(true);
/*    */     }
/*    */     
/* 52 */     if (block.getType().equals(Material.ANVIL)) {
/* 53 */       CivMessage.sendError(event.getPlayer(), "Cannot use anvils. XP and Levels disabled in CivCraft.");
/* 54 */       event.setCancelled(true);
/*    */     }
/*    */   }
/*    */   
/*    */   @EventHandler(priority=EventPriority.LOW)
/*    */   public void onPlayerExpChange(PlayerExpChangeEvent event) {
/* 60 */     Resident resident = CivGlobal.getResident(event.getPlayer());
/* 61 */     CivMessage.send(resident, "§aPicked up §e" + event.getAmount() + "§a" + " coins.");
/* 62 */     resident.getTreasury().deposit(event.getAmount());
/*    */     
/*    */ 
/* 65 */     event.setAmount(0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\listener\DisableXPListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
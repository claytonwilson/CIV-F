/*    */ package com.avrgaming.civcraft.items.components;
/*    */ 
/*    */ import gpl.AttributeUtil;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.block.Action;
/*    */ import org.bukkit.event.entity.PlayerLeashEntityEvent;
/*    */ import org.bukkit.event.player.PlayerInteractEntityEvent;
/*    */ import org.bukkit.event.player.PlayerInteractEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoRightClick
/*    */   extends ItemComponent
/*    */ {
/*    */   public void onPrepareCreate(AttributeUtil attrUtil) {}
/*    */   
/*    */   public void onInteract(PlayerInteractEvent event)
/*    */   {
/* 19 */     if ((event.getAction().equals(Action.RIGHT_CLICK_AIR)) || (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
/* 20 */       event.getPlayer().updateInventory();
/* 21 */       event.setCancelled(true);
/* 22 */       return;
/*    */     }
/*    */   }
/*    */   
/*    */   public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
/* 27 */     event.setCancelled(true);
/*    */   }
/*    */   
/*    */   public void onPlayerLeashEvent(PlayerLeashEntityEvent event) {}
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\NoRightClick.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
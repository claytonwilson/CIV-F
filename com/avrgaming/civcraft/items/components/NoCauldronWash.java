/*    */ package com.avrgaming.civcraft.items.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
/*    */ import com.avrgaming.civcraft.util.ItemManager;
/*    */ import gpl.AttributeUtil;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.block.Action;
/*    */ import org.bukkit.event.player.PlayerInteractEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoCauldronWash
/*    */   extends ItemComponent
/*    */ {
/*    */   public void onPrepareCreate(AttributeUtil attrUtil) {}
/*    */   
/*    */   public void onInteract(PlayerInteractEvent event)
/*    */   {
/* 21 */     if ((event.getAction().equals(Action.RIGHT_CLICK_AIR)) || (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
/* 22 */       if (!event.hasBlock()) {
/* 23 */         return;
/*    */       }
/*    */       
/* 26 */       BlockCoord bcoord = new BlockCoord(event.getClickedBlock());
/*    */       
/* 28 */       if (ItemManager.getId(bcoord.getBlock()) == ItemManager.getId(Material.CAULDRON)) {
/* 29 */         event.getPlayer().updateInventory();
/* 30 */         event.setCancelled(true);
/* 31 */         return;
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\NoCauldronWash.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
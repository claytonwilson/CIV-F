/*    */ package com.avrgaming.civcraft.items.components;
/*    */ 
/*    */ import gpl.AttributeUtil;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.player.PlayerItemDamageEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoVanillaDurability
/*    */   extends ItemComponent
/*    */ {
/*    */   public void onPrepareCreate(AttributeUtil attrUtil) {}
/*    */   
/*    */   public void onDurabilityChange(PlayerItemDamageEvent event)
/*    */   {
/* 37 */     event.setDamage(0);
/* 38 */     event.getPlayer().updateInventory();
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\NoVanillaDurability.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package com.avrgaming.civcraft.items.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.tutorial.CivTutorial;
/*    */ import gpl.AttributeUtil;
/*    */ import org.bukkit.event.block.Action;
/*    */ import org.bukkit.event.entity.ItemSpawnEvent;
/*    */ import org.bukkit.event.player.PlayerInteractEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TutorialBook
/*    */   extends ItemComponent
/*    */ {
/*    */   public void onPrepareCreate(AttributeUtil attrs)
/*    */   {
/* 16 */     attrs.addLore("§6CivCraft Info");
/* 17 */     attrs.addLore("§c<Right Click to Open>");
/*    */   }
/*    */   
/*    */ 
/*    */   public void onInteract(PlayerInteractEvent event)
/*    */   {
/* 23 */     event.setCancelled(true);
/* 24 */     if ((!event.getAction().equals(Action.RIGHT_CLICK_AIR)) && 
/* 25 */       (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
/* 26 */       return;
/*    */     }
/*    */     
/*    */ 
/* 30 */     CivTutorial.spawnGuiBook(event.getPlayer());
/*    */   }
/*    */   
/*    */   public void onItemSpawn(ItemSpawnEvent event)
/*    */   {
/* 35 */     event.setCancelled(true);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\TutorialBook.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
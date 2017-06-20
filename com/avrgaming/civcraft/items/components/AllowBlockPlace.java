/*    */ package com.avrgaming.civcraft.items.components;
/*    */ 
/*    */ import gpl.AttributeUtil;
/*    */ import org.bukkit.event.block.BlockPlaceEvent;
/*    */ 
/*    */ 
/*    */ public class AllowBlockPlace
/*    */   extends ItemComponent
/*    */ {
/*    */   public void onPrepareCreate(AttributeUtil attrUtil) {}
/*    */   
/*    */   public boolean onBlockPlaced(BlockPlaceEvent event)
/*    */   {
/* 14 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\AllowBlockPlace.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
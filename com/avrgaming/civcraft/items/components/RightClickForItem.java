/*    */ package com.avrgaming.civcraft.items.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import gpl.AttributeUtil;
/*    */ import org.bukkit.event.player.PlayerInteractEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RightClickForItem
/*    */   extends ItemComponent
/*    */ {
/*    */   public void onPrepareCreate(AttributeUtil attrUtil)
/*    */   {
/* 16 */     String amount = getString("amount");
/* 17 */     String mat_id = getString("custom_id");
/*    */     
/* 19 */     LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterialFromId(mat_id);
/* 20 */     attrUtil.addLore("Right click for " + amount + " " + craftMat.getName());
/*    */   }
/*    */   
/*    */   public void onInteract(PlayerInteractEvent event)
/*    */   {
/* 25 */     CivMessage.send(event.getPlayer(), "§cDisabled for now...");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\RightClickForItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
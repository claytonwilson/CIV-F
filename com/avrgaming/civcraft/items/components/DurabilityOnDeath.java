/*    */ package com.avrgaming.civcraft.items.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.lorestorage.ItemChangeResult;
/*    */ import gpl.AttributeUtil;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.event.entity.PlayerDeathEvent;
/*    */ import org.bukkit.inventory.ItemStack;
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
/*    */ public class DurabilityOnDeath
/*    */   extends ItemComponent
/*    */ {
/*    */   public void onPrepareCreate(AttributeUtil attrs) {}
/*    */   
/*    */   public ItemChangeResult onDurabilityDeath(PlayerDeathEvent event, ItemChangeResult result, ItemStack sourceStack)
/*    */   {
/* 37 */     if (result == null) {
/* 38 */       result = new ItemChangeResult();
/* 39 */       result.stack = sourceStack;
/* 40 */       result.destroyItem = false;
/*    */     }
/*    */     
/* 43 */     if (result.destroyItem) {
/* 44 */       return result;
/*    */     }
/*    */     
/* 47 */     double percent = getDouble("value");
/*    */     
/* 49 */     int reduction = (int)(result.stack.getType().getMaxDurability() * percent);
/* 50 */     int durabilityLeft = result.stack.getType().getMaxDurability() - result.stack.getDurability();
/*    */     
/* 52 */     if (durabilityLeft > reduction) {
/* 53 */       result.stack.setDurability((short)(result.stack.getDurability() + reduction));
/*    */     } else {
/* 55 */       result.destroyItem = true;
/*    */     }
/*    */     
/* 58 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\DurabilityOnDeath.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
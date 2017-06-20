/*    */ package com.avrgaming.civcraft.loreenhancements;
/*    */ 
/*    */ import gpl.AttributeUtil;
/*    */ import java.util.List;
/*    */ import org.bukkit.event.entity.PlayerDeathEvent;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ public class LoreEnhancementSoulBound
/*    */   extends LoreEnhancement
/*    */ {
/*    */   public AttributeUtil add(AttributeUtil attrs)
/*    */   {
/* 13 */     attrs.addEnhancement("LoreEnhancementSoulBound", null, null);
/* 14 */     attrs.addLore("§6" + getDisplayName());
/* 15 */     return attrs;
/*    */   }
/*    */   
/*    */   public boolean onDeath(PlayerDeathEvent event, ItemStack stack) {
/* 19 */     event.getDrops().remove(stack);
/* 20 */     return true;
/*    */   }
/*    */   
/*    */   public boolean canEnchantItem(ItemStack item) {
/* 24 */     return isWeaponOrArmor(item);
/*    */   }
/*    */   
/*    */   public boolean hasEnchantment(ItemStack item) {
/* 28 */     AttributeUtil attrs = new AttributeUtil(item);
/* 29 */     return attrs.hasEnhancement("LoreEnhancementSoulBound");
/*    */   }
/*    */   
/*    */   public String getDisplayName() {
/* 33 */     return "SoulBound";
/*    */   }
/*    */   
/*    */   public String serialize(ItemStack stack)
/*    */   {
/* 38 */     return "";
/*    */   }
/*    */   
/*    */   public ItemStack deserialize(ItemStack stack, String data)
/*    */   {
/* 43 */     return stack;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\loreenhancements\LoreEnhancementSoulBound.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
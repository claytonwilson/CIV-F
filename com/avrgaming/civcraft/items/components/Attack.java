/*    */ package com.avrgaming.civcraft.items.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
/*    */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancementAttack;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import gpl.AttributeUtil;
/*    */ import gpl.AttributeUtil.Attribute;
/*    */ import gpl.AttributeUtil.Attribute.Builder;
/*    */ import gpl.AttributeUtil.AttributeType;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ import org.bukkit.event.player.PlayerItemHeldEvent;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.inventory.PlayerInventory;
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
/*    */ public class Attack
/*    */   extends ItemComponent
/*    */ {
/*    */   public void onPrepareCreate(AttributeUtil attrs)
/*    */   {
/* 44 */     attrs.add(AttributeUtil.Attribute.newBuilder().name("Attack")
/* 45 */       .type(AttributeUtil.AttributeType.GENERIC_ATTACK_DAMAGE)
/* 46 */       .amount(0.0D)
/* 47 */       .build());
/* 48 */     attrs.addLore("§c" + getDouble("value") + " Attack");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void onHold(PlayerItemHeldEvent event)
/*    */   {
/* 55 */     Resident resident = CivGlobal.getResident(event.getPlayer());
/* 56 */     if (!resident.hasTechForItem(event.getPlayer().getInventory().getItem(event.getNewSlot()))) {
/* 57 */       CivMessage.send(resident, "§cWarning - §7You do not have the required technology to use this item. It's attack output will be reduced in half.");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void onAttack(EntityDamageByEntityEvent event, ItemStack inHand)
/*    */   {
/* 64 */     AttributeUtil attrs = new AttributeUtil(inHand);
/* 65 */     double dmg = getDouble("value");
/*    */     
/* 67 */     double extraAtt = 0.0D;
/* 68 */     for (LoreEnhancement enh : attrs.getEnhancements()) {
/* 69 */       if ((enh instanceof LoreEnhancementAttack)) {
/* 70 */         extraAtt += ((LoreEnhancementAttack)enh).getExtraAttack(attrs);
/*    */       }
/*    */     }
/* 73 */     dmg += extraAtt;
/*    */     
/* 75 */     if ((event.getDamager() instanceof Player)) {
/* 76 */       Resident resident = CivGlobal.getResident((Player)event.getDamager());
/* 77 */       if (!resident.hasTechForItem(inHand)) {
/* 78 */         dmg /= 2.0D;
/*    */       }
/*    */     }
/*    */     
/* 82 */     if (dmg < 0.5D) {
/* 83 */       dmg = 0.5D;
/*    */     }
/*    */     
/* 86 */     event.setDamage(dmg);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\Attack.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
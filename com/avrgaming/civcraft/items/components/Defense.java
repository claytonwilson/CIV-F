/*    */ package com.avrgaming.civcraft.items.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
/*    */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancementDefense;
/*    */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import gpl.AttributeUtil;
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
/*    */ public class Defense
/*    */   extends ItemComponent
/*    */ {
/*    */   public void onPrepareCreate(AttributeUtil attrs)
/*    */   {
/* 40 */     attrs.addLore("§3" + getDouble("value") + " Defense");
/*    */   }
/*    */   
/*    */ 
/*    */   public void onHold(PlayerItemHeldEvent event)
/*    */   {
/* 46 */     Resident resident = CivGlobal.getResident(event.getPlayer());
/* 47 */     if (!resident.hasTechForItem(event.getPlayer().getInventory().getItem(event.getNewSlot()))) {
/* 48 */       CivMessage.send(resident, "§cWarning - §7You do not have the required technology to use this item. It's defense output will be reduced in half.");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void onDefense(EntityDamageByEntityEvent event, ItemStack stack)
/*    */   {
/* 55 */     double defValue = getDouble("value");
/*    */     
/*    */ 
/* 58 */     LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/* 59 */     if (craftMat == null) {
/* 60 */       return;
/*    */     }
/*    */     
/* 63 */     double extraDef = 0.0D;
/* 64 */     AttributeUtil attrs = new AttributeUtil(stack);
/*    */     
/* 66 */     for (LoreEnhancement enh : attrs.getEnhancements()) {
/* 67 */       if ((enh instanceof LoreEnhancementDefense)) {
/* 68 */         extraDef += ((LoreEnhancementDefense)enh).getExtraDefense(attrs);
/*    */       }
/*    */     }
/*    */     
/* 72 */     defValue += extraDef;
/* 73 */     double damage = event.getDamage();
/*    */     
/* 75 */     if ((event.getEntity() instanceof Player)) {
/* 76 */       Resident resident = CivGlobal.getResident((Player)event.getEntity());
/* 77 */       if (!resident.hasTechForItem(stack)) {
/* 78 */         defValue /= 2.0D;
/*    */       }
/*    */     }
/*    */     
/* 82 */     damage -= defValue;
/* 83 */     if (damage < 0.5D)
/*    */     {
/* 85 */       damage = 0.5D;
/*    */     }
/*    */     
/* 88 */     event.setDamage(damage);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\Defense.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
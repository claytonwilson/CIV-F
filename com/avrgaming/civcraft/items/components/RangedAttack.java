/*    */ package com.avrgaming.civcraft.items.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.items.units.Unit;
/*    */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
/*    */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancementAttack;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import gpl.AttributeUtil;
/*    */ import org.bukkit.entity.Arrow;
/*    */ import org.bukkit.entity.Entity;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ import org.bukkit.event.player.PlayerInteractEvent;
/*    */ import org.bukkit.event.player.PlayerItemHeldEvent;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.inventory.PlayerInventory;
/*    */ import org.bukkit.util.Vector;
/*    */ 
/*    */ public class RangedAttack
/*    */   extends ItemComponent
/*    */ {
/*    */   public void onPrepareCreate(AttributeUtil attrs)
/*    */   {
/* 25 */     attrs.addLore("§c" + getDouble("value") + " Ranged Attack");
/*    */   }
/*    */   
/* 28 */   private static double ARROW_MAX_VEL = 6.0D;
/*    */   
/*    */   public void onInteract(PlayerInteractEvent event)
/*    */   {
/* 32 */     if (Unit.isWearingAnyMetal(event.getPlayer())) {
/* 33 */       event.setCancelled(true);
/* 34 */       CivMessage.sendError(event.getPlayer(), "Cannot use a bow while wearing metal armor.");
/* 35 */       return;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void onHold(PlayerItemHeldEvent event)
/*    */   {
/* 42 */     Resident resident = CivGlobal.getResident(event.getPlayer());
/* 43 */     if (!resident.hasTechForItem(event.getPlayer().getInventory().getItem(event.getNewSlot()))) {
/* 44 */       CivMessage.send(resident, "§cWarning - §7You do not have the required technology to use this item. It's attack output will be reduced in half.");
/*    */     }
/*    */   }
/*    */   
/*    */   public void onRangedAttack(EntityDamageByEntityEvent event, ItemStack inHand)
/*    */   {
/* 50 */     AttributeUtil attrs = new AttributeUtil(inHand);
/* 51 */     double dmg = getDouble("value");
/*    */     
/* 53 */     if ((event.getDamager() instanceof Arrow)) {
/* 54 */       Arrow arrow = (Arrow)event.getDamager();
/* 55 */       if ((arrow.getShooter() instanceof Player)) {
/* 56 */         Player attacker = (Player)arrow.getShooter();
/* 57 */         if (Unit.isWearingAnyMetal(attacker)) {
/* 58 */           event.setCancelled(true);
/* 59 */           CivMessage.sendError(attacker, "Cannot use a bow while wearing metal armor.");
/* 60 */           return;
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 65 */     double extraAtt = 0.0D;
/* 66 */     for (LoreEnhancement enh : attrs.getEnhancements()) {
/* 67 */       if ((enh instanceof LoreEnhancementAttack)) {
/* 68 */         extraAtt += ((LoreEnhancementAttack)enh).getExtraAttack(attrs);
/*    */       }
/*    */     }
/* 71 */     dmg += extraAtt;
/*    */     
/*    */ 
/* 74 */     Vector vel = event.getDamager().getVelocity();
/* 75 */     double magnitudeSquared = Math.pow(vel.getX(), 2.0D) + Math.pow(vel.getY(), 2.0D) + Math.pow(vel.getZ(), 2.0D);
/*    */     
/* 77 */     double percentage = magnitudeSquared / ARROW_MAX_VEL;
/* 78 */     double totalDmg = percentage * dmg;
/*    */     
/* 80 */     if (totalDmg > dmg) {
/* 81 */       totalDmg = dmg;
/*    */     }
/*    */     
/* 84 */     if ((event.getDamager() instanceof Arrow)) {
/* 85 */       Arrow arrow = (Arrow)event.getDamager();
/* 86 */       if ((arrow.getShooter() instanceof Player)) {
/* 87 */         Resident resident = CivGlobal.getResident((Player)arrow.getShooter());
/* 88 */         if (!resident.hasTechForItem(inHand)) {
/* 89 */           totalDmg /= 2.0D;
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 94 */     if (totalDmg < 0.5D) {
/* 95 */       totalDmg = 0.5D;
/*    */     }
/*    */     
/* 98 */     event.setDamage(totalDmg);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\RangedAttack.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
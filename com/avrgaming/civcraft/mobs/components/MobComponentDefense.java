/*    */ package com.avrgaming.civcraft.mobs.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import org.bukkit.entity.Arrow;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ 
/*    */ public class MobComponentDefense
/*    */   extends MobComponent
/*    */ {
/* 12 */   private double defense = 0.0D;
/*    */   
/*    */   public MobComponentDefense(double defense) {
/* 15 */     this.defense = defense;
/*    */   }
/*    */   
/*    */   public void onDefense(EntityDamageByEntityEvent event)
/*    */   {
/* 20 */     double damage = event.getDamage();
/*    */     
/* 22 */     damage -= this.defense;
/* 23 */     if (damage < 0.5D) {
/* 24 */       damage = 0.0D;
/*    */       
/* 26 */       Player player = null;
/* 27 */       if ((event.getDamager() instanceof Arrow)) {
/* 28 */         Arrow arrow = (Arrow)event.getDamager();
/* 29 */         if ((arrow.getShooter() instanceof Player)) {
/* 30 */           player = (Player)arrow.getShooter();
/*    */         }
/* 32 */       } else if ((event.getDamager() instanceof Player)) {
/* 33 */         player = (Player)event.getDamager();
/*    */       }
/*    */       
/* 36 */       if (player != null) {
/* 37 */         CivMessage.send(player, "§7Our attack was ineffective");
/*    */       }
/*    */     }
/* 40 */     event.setDamage(damage);
/*    */   }
/*    */   
/*    */   public double getDefense() {
/* 44 */     return this.defense;
/*    */   }
/*    */   
/*    */   public void setDefense(double defense)
/*    */   {
/* 49 */     this.defense = defense;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\mobs\components\MobComponentDefense.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
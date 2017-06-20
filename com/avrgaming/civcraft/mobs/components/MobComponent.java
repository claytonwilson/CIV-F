/*    */ package com.avrgaming.civcraft.mobs.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.mobs.CommonCustomMob;
/*    */ import org.bukkit.entity.Entity;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ public class MobComponent
/*    */ {
/*    */   public void onDefense(EntityDamageByEntityEvent event) {}
/*    */   
/*    */   public static void onDefense(Entity entity, EntityDamageByEntityEvent event)
/*    */   {
/* 13 */     CommonCustomMob custom = (CommonCustomMob)CommonCustomMob.customMobs.get(entity.getUniqueId());
/* 14 */     if (custom != null) {
/* 15 */       for (MobComponent comp : custom.getMobComponents()) {
/* 16 */         comp.onDefense(event);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\mobs\components\MobComponent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
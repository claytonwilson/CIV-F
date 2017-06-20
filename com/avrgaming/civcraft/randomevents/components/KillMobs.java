/*    */ package com.avrgaming.civcraft.randomevents.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivCraft;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.entity.EntityType;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.HandlerList;
/*    */ import org.bukkit.event.entity.EntityDamageEvent;
/*    */ import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
/*    */ import org.bukkit.event.entity.EntityDeathEvent;
/*    */ import org.bukkit.plugin.PluginManager;
/*    */ 
/*    */ public class KillMobs extends com.avrgaming.civcraft.randomevents.RandomEventComponent implements org.bukkit.event.Listener
/*    */ {
/* 17 */   private int killedMobs = 0;
/* 18 */   private int requireMobs = 0;
/*    */   
/*    */   EntityType target;
/*    */   
/*    */   public void onStart()
/*    */   {
/* 24 */     PluginManager pluginManager = CivCraft.getPlugin().getServer().getPluginManager();
/* 25 */     pluginManager.registerEvents(this, CivCraft.getPlugin());
/* 26 */     this.requireMobs = Integer.valueOf(getString("amount")).intValue();
/* 27 */     this.target = EntityType.valueOf(getString("what"));
/*    */   }
/*    */   
/*    */   public void onCleanup()
/*    */   {
/* 32 */     HandlerList.unregisterAll(this);
/*    */   }
/*    */   
/*    */   @EventHandler(priority=EventPriority.MONITOR)
/*    */   public void onEntityDeath(EntityDeathEvent event) {
/* 37 */     if (!event.getEntityType().equals(this.target)) {
/* 38 */       return;
/*    */     }
/*    */     
/* 41 */     if (event.getEntity().getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
/* 42 */       this.killedMobs += 1;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void process() {}
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean onCheck()
/*    */   {
/* 54 */     if (this.killedMobs >= this.requireMobs) {
/* 55 */       return true;
/*    */     }
/*    */     
/* 58 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\randomevents\components\KillMobs.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
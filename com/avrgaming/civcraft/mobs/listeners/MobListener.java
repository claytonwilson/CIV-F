/*     */ package com.avrgaming.civcraft.mobs.listeners;
/*     */ 
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.mobs.CommonCustomMob;
/*     */ import com.avrgaming.moblib.MobLib;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.entity.EntityDamageEvent;
/*     */ import org.bukkit.event.entity.EntityTargetEvent;
/*     */ import org.bukkit.event.entity.PlayerLeashEntityEvent;
/*     */ import org.bukkit.event.world.ChunkLoadEvent;
/*     */ 
/*     */ public class MobListener implements org.bukkit.event.Listener
/*     */ {
/*     */   @EventHandler(priority=EventPriority.NORMAL)
/*     */   public void onChunkLoad(ChunkLoadEvent event)
/*     */   {
/*     */     Entity[] arrayOfEntity;
/*  23 */     int j = (arrayOfEntity = event.getChunk().getEntities()).length; for (int i = 0; i < j; i++) { Entity e = arrayOfEntity[i];
/*  24 */       if (!(e instanceof LivingEntity)) {
/*  25 */         return;
/*     */       }
/*     */       
/*  28 */       LivingEntity living = (LivingEntity)e;
/*     */       
/*  30 */       if (MobLib.isMobLibEntity(living)) {
/*  31 */         return;
/*     */       }
/*     */       
/*  34 */       switch (living.getType()) {
/*     */       case SILVERFISH: 
/*  36 */         if (!living.isEmpty()) {
/*     */           break;
/*     */         }
/*     */       case MINECART: 
/*     */       case PIG: 
/*     */       case PLAYER: 
/*     */       case PRIMED_TNT: 
/*     */       case SHEEP: 
/*     */       case SKELETON: 
/*     */       case SLIME: 
/*     */       case SMALL_FIREBALL: 
/*     */       case SNOWMAN: 
/*     */       case SPLASH_POTION: 
/*     */       case SQUID: 
/*  50 */         return;
/*     */       }
/*     */       
/*     */       
/*     */ 
/*  55 */       e.remove();
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.NORMAL)
/*     */   public void onEntityTarget(EntityTargetEvent event) {
/*  61 */     if (!(event.getEntity() instanceof LivingEntity)) {
/*  62 */       return;
/*     */     }
/*     */     
/*  65 */     if (!MobLib.isMobLibEntity((LivingEntity)event.getEntity())) {
/*  66 */       return;
/*     */     }
/*     */     
/*  69 */     CommonCustomMob mob = CommonCustomMob.getCCM(event.getEntity());
/*  70 */     if (mob != null) {
/*  71 */       mob.onTarget(event);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.NORMAL)
/*     */   public void onEntityDamage(EntityDamageEvent event)
/*     */   {
/*  78 */     if (!(event.getEntity() instanceof LivingEntity)) {
/*  79 */       return;
/*     */     }
/*     */     
/*  82 */     if (!MobLib.isMobLibEntity((LivingEntity)event.getEntity())) {
/*  83 */       return;
/*     */     }
/*     */     
/*  86 */     switch (event.getCause()) {
/*     */     case DROWNING: 
/*  88 */       Location loc = event.getEntity().getLocation();
/*  89 */       int y = loc.getWorld().getHighestBlockAt(loc.getBlockX(), loc.getBlockZ()).getY() + 4;
/*  90 */       loc.setY(y);
/*  91 */       event.getEntity().teleport(loc);
/*     */     case BLOCK_EXPLOSION: 
/*     */     case ENTITY_ATTACK: 
/*     */     case ENTITY_EXPLOSION: 
/*     */     case FALL: 
/*     */     case FALLING_BLOCK: 
/*     */     case FIRE: 
/*     */     case FIRE_TICK: 
/*     */     case LAVA: 
/*     */     case LIGHTNING: 
/*     */     case MELTING: 
/*     */     case SUFFOCATION: 
/*     */     case THORNS: 
/* 104 */       event.setCancelled(true);
/* 105 */       break;
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.NORMAL)
/*     */   public void onPlayerLeash(PlayerLeashEntityEvent event)
/*     */   {
/* 113 */     if (((event.getEntity() instanceof LivingEntity)) && 
/* 114 */       (MobLib.isMobLibEntity((LivingEntity)event.getEntity()))) {
/* 115 */       CivMessage.sendError(event.getPlayer(), "This beast cannot be tamed.");
/* 116 */       event.setCancelled(true);
/* 117 */       return;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\mobs\listeners\MobListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
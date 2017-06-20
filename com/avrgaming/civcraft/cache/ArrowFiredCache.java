/*     */ package com.avrgaming.civcraft.cache;
/*     */ 
/*     */ import com.avrgaming.civcraft.components.ProjectileArrowComponent;
/*     */ import java.util.Calendar;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.entity.Arrow;
/*     */ import org.bukkit.entity.Entity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArrowFiredCache
/*     */ {
/*     */   private ProjectileArrowComponent fromTower;
/*     */   private Location target;
/*     */   private Entity targetEntity;
/*     */   private Arrow arrow;
/*     */   private UUID uuid;
/*     */   private Calendar expired;
/*  37 */   private boolean hit = false;
/*     */   
/*     */   public ArrowFiredCache(ProjectileArrowComponent tower, Entity targetEntity, Arrow arrow) {
/*  40 */     setFromTower(tower);
/*  41 */     this.target = targetEntity.getLocation();
/*  42 */     this.targetEntity = targetEntity;
/*  43 */     setArrow(arrow);
/*  44 */     this.uuid = arrow.getUniqueId();
/*  45 */     this.expired = Calendar.getInstance();
/*  46 */     this.expired.add(13, 5);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Location getTarget()
/*     */   {
/*  54 */     return this.target;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setTarget(Location target)
/*     */   {
/*  61 */     this.target = target;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Arrow getArrow()
/*     */   {
/*  68 */     return this.arrow;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setArrow(Arrow arrow)
/*     */   {
/*  75 */     this.arrow = arrow;
/*     */   }
/*     */   
/*     */   public Object getUUID() {
/*  79 */     return this.uuid;
/*     */   }
/*     */   
/*     */   public void destroy(Arrow arrow) {
/*  83 */     arrow.remove();
/*  84 */     this.arrow = null;
/*  85 */     CivCache.arrowsFired.remove(getUUID());
/*  86 */     this.uuid = null;
/*     */   }
/*     */   
/*     */   public void destroy(Entity damager)
/*     */   {
/*  91 */     if ((damager instanceof Arrow)) {
/*  92 */       destroy((Arrow)damager);
/*     */     }
/*     */   }
/*     */   
/*     */   public Calendar getExpired()
/*     */   {
/*  98 */     return this.expired;
/*     */   }
/*     */   
/*     */   public void setExpired(Calendar expired)
/*     */   {
/* 103 */     this.expired = expired;
/*     */   }
/*     */   
/*     */   public boolean isHit()
/*     */   {
/* 108 */     return this.hit;
/*     */   }
/*     */   
/*     */   public void setHit(boolean hit)
/*     */   {
/* 113 */     this.hit = hit;
/*     */   }
/*     */   
/*     */   public ProjectileArrowComponent getFromTower()
/*     */   {
/* 118 */     return this.fromTower;
/*     */   }
/*     */   
/*     */   public void setFromTower(ProjectileArrowComponent fromTower)
/*     */   {
/* 123 */     this.fromTower = fromTower;
/*     */   }
/*     */   
/*     */   public Entity getTargetEntity()
/*     */   {
/* 128 */     return this.targetEntity;
/*     */   }
/*     */   
/*     */   public void setTargetEntity(Entity targetEntity)
/*     */   {
/* 133 */     this.targetEntity = targetEntity;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\cache\ArrowFiredCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
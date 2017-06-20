/*    */ package com.avrgaming.civcraft.cache;
/*    */ 
/*    */ import com.avrgaming.civcraft.structure.CannonTower;
/*    */ import java.util.Calendar;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.entity.Entity;
/*    */ import org.bukkit.entity.Fireball;
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
/*    */ public class CannonFiredCache
/*    */ {
/*    */   private CannonTower fromTower;
/*    */   private Location target;
/*    */   private Fireball fireball;
/*    */   private UUID uuid;
/*    */   private Calendar expired;
/* 36 */   private boolean hit = false;
/*    */   
/*    */   public CannonFiredCache(CannonTower fromTower, Location target, Fireball fireball) {
/* 39 */     this.fromTower = fromTower;
/* 40 */     this.target = target;
/* 41 */     this.fireball = fireball;
/* 42 */     this.uuid = fireball.getUniqueId();
/* 43 */     this.expired = Calendar.getInstance();
/* 44 */     this.expired.set(13, 30);
/*    */   }
/*    */   
/*    */   public CannonTower getFromTower() {
/* 48 */     return this.fromTower;
/*    */   }
/*    */   
/* 51 */   public void setFromTower(CannonTower fromTower) { this.fromTower = fromTower; }
/*    */   
/*    */   public Location getTarget() {
/* 54 */     return this.target;
/*    */   }
/*    */   
/* 57 */   public void setTarget(Location target) { this.target = target; }
/*    */   
/*    */   public Fireball getFireball() {
/* 60 */     return this.fireball;
/*    */   }
/*    */   
/* 63 */   public void setFireball(Fireball fireball) { this.fireball = fireball; }
/*    */   
/*    */   public UUID getUuid() {
/* 66 */     return this.uuid;
/*    */   }
/*    */   
/* 69 */   public void setUuid(UUID uuid) { this.uuid = uuid; }
/*    */   
/*    */   public Calendar getExpired() {
/* 72 */     return this.expired;
/*    */   }
/*    */   
/* 75 */   public void setExpired(Calendar expired) { this.expired = expired; }
/*    */   
/*    */   public boolean isHit() {
/* 78 */     return this.hit;
/*    */   }
/*    */   
/* 81 */   public void setHit(boolean hit) { this.hit = hit; }
/*    */   
/*    */   public void destroy(Entity damager)
/*    */   {
/* 85 */     this.fireball.remove();
/* 86 */     this.fireball = null;
/* 87 */     CivCache.cannonBallsFired.remove(getUuid());
/* 88 */     this.uuid = null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\cache\CannonFiredCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
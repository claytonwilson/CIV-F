/*     */ package com.avrgaming.civcraft.components;
/*     */ 
/*     */ import com.avrgaming.civcraft.cache.PlayerLocationCache;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.concurrent.locks.ReentrantLock;
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
/*     */ public class PlayerProximityComponent
/*     */   extends Component
/*     */ {
/*     */   private HashSet<PlayerLocationCache> nearbyPlayers;
/*     */   public ReentrantLock lock;
/*     */   private BlockCoord center;
/*     */   private double radiusSquared;
/*     */   private Buildable buildable;
/*     */   
/*     */   public PlayerProximityComponent()
/*     */   {
/*  51 */     this.lock = new ReentrantLock();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onLoad() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void onSave() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void setNearbyPlayers(HashSet<PlayerLocationCache> newSet)
/*     */   {
/*  66 */     this.nearbyPlayers = newSet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HashSet<PlayerLocationCache> tryGetNearbyPlayers(boolean retry)
/*     */   {
/*  75 */     if (retry) {
/*  76 */       this.lock.lock();
/*     */     }
/*  78 */     else if (!this.lock.tryLock()) {
/*  79 */       return new HashSet();
/*     */     }
/*     */     try
/*     */     {
/*     */       HashSet localHashSet;
/*  84 */       if (this.nearbyPlayers == null) {
/*  85 */         return new HashSet();
/*     */       }
/*  87 */       return (HashSet)this.nearbyPlayers.clone();
/*     */     }
/*     */     finally {
/*  90 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HashSet<PlayerLocationCache> waitGetNearbyPlayers()
/*     */   {
/* 100 */     this.lock.lock();
/*     */     try { HashSet localHashSet;
/* 102 */       if (this.nearbyPlayers == null) {
/* 103 */         return new HashSet();
/*     */       }
/*     */       
/* 106 */       return (HashSet)this.nearbyPlayers.clone();
/*     */     }
/*     */     finally {
/* 109 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public BlockCoord getCenter()
/*     */   {
/* 115 */     return this.center;
/*     */   }
/*     */   
/*     */   public void setCenter(BlockCoord center)
/*     */   {
/* 120 */     this.center = center;
/*     */   }
/*     */   
/*     */   public double getRadiusSquared()
/*     */   {
/* 125 */     return this.radiusSquared;
/*     */   }
/*     */   
/*     */   public void setRadius(double radius)
/*     */   {
/* 130 */     this.radiusSquared = Math.pow(radius, 2.0D);
/*     */   }
/*     */   
/*     */   public void buildNearbyPlayers(Collection<PlayerLocationCache> collection) {
/* 134 */     HashSet<PlayerLocationCache> newSet = new HashSet();
/*     */     
/* 136 */     for (PlayerLocationCache pc : collection) {
/* 137 */       if (!pc.isVanished())
/*     */       {
/*     */ 
/*     */ 
/* 141 */         if (pc.getCoord().distanceSquared(this.center) < this.radiusSquared) {
/* 142 */           newSet.add(pc);
/*     */         }
/*     */       }
/*     */     }
/* 146 */     setNearbyPlayers(newSet);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Buildable getBuildable()
/*     */   {
/* 153 */     return this.buildable;
/*     */   }
/*     */   
/*     */   public void setBuildable(Buildable buildable)
/*     */   {
/* 158 */     this.buildable = buildable;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\components\PlayerProximityComponent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.avrgaming.civcraft.object;
/*     */ 
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
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
/*     */ public class ControlPoint
/*     */ {
/*     */   private BlockCoord coord;
/*     */   private int hitpoints;
/*     */   private int maxHitpoints;
/*     */   private Buildable buildable;
/*     */   
/*     */   public ControlPoint(BlockCoord coord, Buildable buildable, int hitpoints)
/*     */   {
/*  39 */     this.coord = coord;
/*  40 */     setBuildable(buildable);
/*  41 */     this.maxHitpoints = hitpoints;
/*  42 */     this.hitpoints = this.maxHitpoints;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BlockCoord getCoord()
/*     */   {
/*  49 */     return this.coord;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setCoord(BlockCoord coord)
/*     */   {
/*  56 */     this.coord = coord;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getHitpoints()
/*     */   {
/*  63 */     return this.hitpoints;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setHitpoints(int hitpoints)
/*     */   {
/*  70 */     this.hitpoints = hitpoints;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getMaxHitpoints()
/*     */   {
/*  77 */     return this.maxHitpoints;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setMaxHitpoints(int maxHitpoints)
/*     */   {
/*  84 */     this.maxHitpoints = maxHitpoints;
/*     */   }
/*     */   
/*     */   public void damage(int amount) {
/*  88 */     if (this.hitpoints <= 0) {
/*  89 */       return;
/*     */     }
/*     */     
/*  92 */     this.hitpoints -= amount;
/*     */     
/*  94 */     if (this.hitpoints <= 0) {
/*  95 */       this.hitpoints = 0;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isDestroyed()
/*     */   {
/* 101 */     if (this.hitpoints <= 0) {
/* 102 */       return true;
/*     */     }
/* 104 */     return false;
/*     */   }
/*     */   
/*     */   public Buildable getBuildable() {
/* 108 */     return this.buildable;
/*     */   }
/*     */   
/*     */   public void setBuildable(Buildable buildable) {
/* 112 */     this.buildable = buildable;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\ControlPoint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
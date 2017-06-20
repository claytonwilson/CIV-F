/*     */ package com.avrgaming.civcraft.util;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.util.Vector;
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
/*     */ public class AABB
/*     */ {
/*  41 */   private Vector position = new Vector();
/*  42 */   private Vector extents = new Vector();
/*     */   
/*     */   public Vector getPosition() {
/*  45 */     return this.position;
/*     */   }
/*     */   
/*     */   public void setPosition(Vector position) {
/*  49 */     this.position = position;
/*     */   }
/*     */   
/*     */   public void setPosition(BlockCoord coord) {
/*  53 */     this.position.setX(coord.getX());
/*  54 */     this.position.setY(coord.getY());
/*  55 */     this.position.setZ(coord.getZ());
/*     */   }
/*     */   
/*     */   public Vector getExtents()
/*     */   {
/*  60 */     return this.extents;
/*     */   }
/*     */   
/*     */   public void setExtents(Vector extents) {
/*  64 */     this.extents = extents;
/*     */   }
/*     */   
/*     */   public void setExtents(BlockCoord coord) {
/*  68 */     this.extents.setX(coord.getX());
/*  69 */     this.extents.setY(coord.getY());
/*  70 */     this.extents.setZ(coord.getZ());
/*     */   }
/*     */   
/*     */   public void showDebugBlocks(int mat, int mat2) {
/*     */     try {
/*  75 */       Player dbgplayer = CivGlobal.getPlayer("netizen539");
/*  76 */       ItemManager.sendBlockChange(dbgplayer, new Location(Bukkit.getWorld("world"), 
/*  77 */         getPosition().getX(), getPosition().getY(), getPosition().getZ()), mat, 0);
/*  78 */       ItemManager.sendBlockChange(dbgplayer, new Location(Bukkit.getWorld("world"), 
/*  79 */         getPosition().getX() + getExtents().getX(), 
/*  80 */         getPosition().getY() + getExtents().getY(), 
/*  81 */         getPosition().getZ() + getExtents().getZ()), 
/*  82 */         mat2, 0);
/*     */     } catch (CivException e) {
/*  84 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean overlaps(AABB other) {
/*  89 */     if (other == null) {
/*  90 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  96 */     Vector t = new Vector();
/*  97 */     t.copy(other.getPosition());
/*  98 */     t.subtract(getPosition());
/*     */     
/* 100 */     return (Math.abs(t.getX()) < getExtents().getX() + other.getExtents().getX()) && 
/* 101 */       (Math.abs(t.getY()) < getExtents().getY() + other.getExtents().getY()) && (
/* 102 */       Math.abs(t.getZ()) < getExtents().getZ() + other.getExtents().getZ());
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\AABB.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.avrgaming.civcraft.util;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.InvalidBlockLocation;
/*     */ import org.bukkit.ChunkSnapshot;
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
/*     */ public class BlockSnapshot
/*     */ {
/*     */   private int x;
/*     */   private int y;
/*     */   private int z;
/*     */   private int typeId;
/*     */   private int data;
/*     */   private ChunkSnapshot snapshot;
/*     */   
/*     */   public BlockSnapshot(int x, int y, int z, ChunkSnapshot snapshot)
/*     */   {
/*  35 */     setFromSnapshotLocation(x, y, z, snapshot);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BlockSnapshot() {}
/*     */   
/*     */ 
/*     */   public void setFromSnapshotLocation(int x, int y, int z, ChunkSnapshot snapshot)
/*     */   {
/*  45 */     if (x < 0) {
/*  46 */       x += 16;
/*     */     }
/*     */     
/*  49 */     if (z < 0) {
/*  50 */       z += 16;
/*     */     }
/*     */     
/*  53 */     setX(x);
/*  54 */     setY(y);
/*  55 */     setZ(z);
/*  56 */     setSnapshot(snapshot);
/*  57 */     setTypeId(ItemManager.getBlockTypeId(snapshot, this.x, this.y, this.z));
/*  58 */     setData(ItemManager.getBlockData(snapshot, this.x, this.y, this.z));
/*     */   }
/*     */   
/*     */   public BlockSnapshot getRelative(int xOff, int yOff, int zOff) throws InvalidBlockLocation {
/*  62 */     int nX = getX() + xOff;
/*  63 */     if ((nX < 0) || (nX > 15)) {
/*  64 */       throw new InvalidBlockLocation();
/*     */     }
/*     */     
/*  67 */     BlockSnapshot relative = new BlockSnapshot(getX() + xOff, getY() + yOff, getZ() + zOff, this.snapshot);
/*  68 */     return relative;
/*     */   }
/*     */   
/*     */   public int getX()
/*     */   {
/*  73 */     return this.x;
/*     */   }
/*     */   
/*     */   public void setX(int x) {
/*  77 */     this.x = x;
/*     */   }
/*     */   
/*     */   public int getY() {
/*  81 */     return this.y;
/*     */   }
/*     */   
/*     */   public void setY(int y) {
/*  85 */     this.y = y;
/*     */   }
/*     */   
/*     */   public int getZ() {
/*  89 */     return this.z;
/*     */   }
/*     */   
/*     */   public void setZ(int z) {
/*  93 */     this.z = z;
/*     */   }
/*     */   
/*     */   public int getTypeId() {
/*  97 */     return this.typeId;
/*     */   }
/*     */   
/*     */   public void setTypeId(int typeId) {
/* 101 */     this.typeId = typeId;
/*     */   }
/*     */   
/*     */   public int getData() {
/* 105 */     return this.data;
/*     */   }
/*     */   
/*     */   public void setData(int data) {
/* 109 */     this.data = data;
/*     */   }
/*     */   
/*     */   public ChunkSnapshot getSnapshot() {
/* 113 */     return this.snapshot;
/*     */   }
/*     */   
/*     */   public void setSnapshot(ChunkSnapshot snapshot) {
/* 117 */     this.snapshot = snapshot;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\BlockSnapshot.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
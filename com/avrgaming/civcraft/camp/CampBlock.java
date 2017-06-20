/*    */ package com.avrgaming.civcraft.camp;
/*    */ 
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
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
/*    */ public class CampBlock
/*    */ {
/*    */   private BlockCoord coord;
/*    */   private Camp camp;
/* 27 */   private boolean friendlyBreakable = false;
/*    */   
/*    */   public CampBlock(BlockCoord coord, Camp camp) {
/* 30 */     this.coord = coord;
/* 31 */     this.camp = camp;
/*    */   }
/*    */   
/*    */   public CampBlock(BlockCoord coord, Camp camp, boolean friendlyBreakable) {
/* 35 */     this.coord = coord;
/* 36 */     this.camp = camp;
/* 37 */     this.friendlyBreakable = friendlyBreakable;
/*    */   }
/*    */   
/*    */   public BlockCoord getCoord() {
/* 41 */     return this.coord;
/*    */   }
/*    */   
/* 44 */   public void setCoord(BlockCoord coord) { this.coord = coord; }
/*    */   
/*    */   public Camp getCamp() {
/* 47 */     return this.camp;
/*    */   }
/*    */   
/* 50 */   public void setCamp(Camp camp) { this.camp = camp; }
/*    */   
/*    */   public int getX()
/*    */   {
/* 54 */     return this.coord.getX();
/*    */   }
/*    */   
/*    */   public int getY() {
/* 58 */     return this.coord.getY();
/*    */   }
/*    */   
/*    */   public int getZ() {
/* 62 */     return this.coord.getZ();
/*    */   }
/*    */   
/*    */   public String getWorldname() {
/* 66 */     return this.coord.getWorldname();
/*    */   }
/*    */   
/*    */   public boolean canBreak(String playerName) {
/* 70 */     if (!this.friendlyBreakable) {
/* 71 */       return false;
/*    */     }
/*    */     
/* 74 */     if (this.camp.hasMember(playerName)) {
/* 75 */       return true;
/*    */     }
/*    */     
/* 78 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\camp\CampBlock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
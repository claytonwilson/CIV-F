/*    */ package com.avrgaming.civcraft.structure.farm;
/*    */ 
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
/*    */ 
/*    */ public class GrowBlock { public BlockCoord bcoord;
/*    */   public int typeId;
/*    */   
/*  8 */   public GrowBlock(String world, int x, int y, int z, int typeid2, int data2, boolean spawn2) { this.bcoord = new BlockCoord(world, x, y, z);
/*  9 */     this.typeId = typeid2;
/* 10 */     this.data = data2;
/* 11 */     this.spawn = spawn2;
/*    */   }
/*    */   
/*    */   public int data;
/*    */   public boolean spawn;
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\farm\GrowBlock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
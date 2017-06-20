/*    */ package com.avrgaming.civcraft.main;
/*    */ 
/*    */ import com.avrgaming.civcraft.structure.Structure;
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map.Entry;
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
/*    */ public class Handler_serverstop
/*    */   extends Thread
/*    */ {
/*    */   public void run()
/*    */   {
/* 30 */     Iterator<Map.Entry<BlockCoord, Structure>> iter = CivGlobal.getStructureIterator();
/* 31 */     while (iter.hasNext()) {
/* 32 */       Structure struct = (Structure)((Map.Entry)iter.next()).getValue();
/* 33 */       struct.onUnload();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\main\Handler_serverstop.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
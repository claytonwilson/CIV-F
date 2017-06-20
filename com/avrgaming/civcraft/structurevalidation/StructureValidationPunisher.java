/*    */ package com.avrgaming.civcraft.structurevalidation;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.structure.Structure;
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map.Entry;
/*    */ 
/*    */ public class StructureValidationPunisher implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/* 14 */     if (!StructureValidator.isEnabled()) {
/* 15 */       return;
/*    */     }
/*    */     
/* 18 */     Iterator<Map.Entry<BlockCoord, Structure>> structIter = CivGlobal.getStructureIterator();
/* 19 */     while (structIter.hasNext()) {
/* 20 */       Structure struct = (Structure)((Map.Entry)structIter.next()).getValue();
/* 21 */       if (!struct.getCiv().isAdminCiv())
/*    */       {
/*    */ 
/*    */ 
/* 25 */         if ((struct.validated) && (struct.isActive()) && 
/* 26 */           (!struct.isValid())) {
/* 27 */           struct.onInvalidPunish();
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structurevalidation\StructureValidationPunisher.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
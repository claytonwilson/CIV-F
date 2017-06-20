/*    */ package com.avrgaming.civcraft.structurevalidation;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.structure.Structure;
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
/*    */ import com.avrgaming.civcraft.war.War;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map.Entry;
/*    */ 
/*    */ public class StructureValidationChecker implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/* 17 */     Iterator<Map.Entry<BlockCoord, Structure>> structIter = CivGlobal.getStructureIterator();
/* 18 */     while (structIter.hasNext()) {
/* 19 */       Structure struct = (Structure)((Map.Entry)structIter.next()).getValue();
/* 20 */       if (!struct.getCiv().isAdminCiv())
/*    */       {
/*    */ 
/*    */ 
/* 24 */         if (War.isWarTime()) {
/*    */           break;
/*    */         }
/*    */         
/*    */ 
/* 29 */         if (struct.isActive())
/*    */         {
/*    */ 
/*    */ 
/* 33 */           if (!struct.isIgnoreFloating())
/*    */           {
/*    */ 
/*    */             try
/*    */             {
/* 38 */               CivLog.warning("Doing a structure validate...");
/* 39 */               struct.validate(null);
/*    */             } catch (CivException e) {
/* 41 */               e.printStackTrace();
/*    */             }
/*    */             
/* 44 */             synchronized (this) {
/*    */               try {
/* 46 */                 wait(10000L);
/*    */               } catch (InterruptedException e) {
/* 48 */                 e.printStackTrace();
/*    */               }
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structurevalidation\StructureValidationChecker.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
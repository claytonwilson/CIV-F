/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.structure.ScoutTower;
/*    */ import com.avrgaming.civcraft.structure.Structure;
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
/*    */ import java.util.HashSet;
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
/*    */ public class ScoutTowerTask
/*    */   implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/* 33 */     HashSet<String> announced = new HashSet();
/*    */     try
/*    */     {
/* 36 */       if (!CivGlobal.towersEnabled) {
/* 37 */         return;
/*    */       }
/*    */       
/* 40 */       Iterator<Map.Entry<BlockCoord, Structure>> iter = CivGlobal.getStructureIterator();
/* 41 */       while (iter.hasNext()) {
/* 42 */         Structure struct = (Structure)((Map.Entry)iter.next()).getValue();
/* 43 */         if ((struct instanceof ScoutTower)) {
/* 44 */           ((ScoutTower)struct).process(announced);
/*    */         }
/*    */       }
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 50 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\ScoutTowerTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
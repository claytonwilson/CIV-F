/*    */ package com.avrgaming.civcraft.threading.sync;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.structure.Structure;
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Random;
/*    */ import org.bukkit.Effect;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.block.Block;
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
/*    */ public class DamagedStructureTimer
/*    */   implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/* 37 */     Iterator<Map.Entry<BlockCoord, Structure>> iter = CivGlobal.getStructureIterator();
/* 38 */     while (iter.hasNext()) {
/* 39 */       Structure struct = (Structure)((Map.Entry)iter.next()).getValue();
/*    */       
/* 41 */       if (struct.isDestroyed()) {
/* 42 */         int size = struct.getStructureBlocks().size();
/* 43 */         World world = struct.getCorner().getBlock().getWorld();
/*    */         
/* 45 */         for (int i = 0; i < size / 10; i++) {
/* 46 */           Random rand = new Random();
/* 47 */           int index = rand.nextInt(size);
/*    */           
/*    */ 
/* 50 */           int j = 0;
/* 51 */           for (BlockCoord coord : struct.getStructureBlocks().keySet())
/*    */           {
/* 53 */             if (j < index) {
/* 54 */               j++;
/*    */             }
/*    */             else
/*    */             {
/* 58 */               world.playEffect(coord.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
/* 59 */               break;
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\sync\DamagedStructureTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
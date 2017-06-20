/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.structure.Windmill;
/*    */ import com.avrgaming.civcraft.structure.farm.FarmChunk;
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
/*    */ import com.avrgaming.civcraft.util.ChunkCoord;
/*    */ import java.util.ArrayList;
/*    */ import org.bukkit.Chunk;
/*    */ import org.bukkit.ChunkSnapshot;
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
/*    */ public class WindmillStartSyncTask
/*    */   implements Runnable
/*    */ {
/*    */   Windmill windmill;
/*    */   
/*    */   public WindmillStartSyncTask(Windmill windmill)
/*    */   {
/* 36 */     this.windmill = windmill;
/*    */   }
/*    */   
/*    */ 
/*    */   public void run()
/*    */   {
/* 42 */     ChunkCoord cc = new ChunkCoord(this.windmill.getCorner());
/* 43 */     ArrayList<ChunkSnapshot> snapshots = new ArrayList();
/*    */     
/* 45 */     int[][] offset = { { -1 }, { 1 }, { 0, -1 }, { 0, 1 }, { 1, 1 }, { -1, -1 }, { -1, 1 }, { 1, -1 } };
/* 46 */     for (int i = 0; i < 8; i++) {
/* 47 */       cc.setX(cc.getX() + offset[i][0]);
/* 48 */       cc.setZ(cc.getZ() + offset[i][1]);
/*    */       
/* 50 */       FarmChunk farmChunk = CivGlobal.getFarmChunk(cc);
/* 51 */       if (farmChunk != null) {
/* 52 */         snapshots.add(farmChunk.getChunk().getChunkSnapshot());
/*    */       }
/*    */       
/* 55 */       cc.setFromLocation(this.windmill.getCorner().getLocation());
/*    */     }
/*    */     
/*    */ 
/* 59 */     if (snapshots.size() == 0) {
/* 60 */       return;
/*    */     }
/*    */     
/*    */ 
/* 64 */     TaskMaster.asyncTask("", new WindmillPreProcessTask(this.windmill, snapshots), 0L);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\WindmillStartSyncTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
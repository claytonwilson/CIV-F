/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.Chunk;
/*    */ import org.bukkit.World;
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
/*    */ public class ChunkGenerateTask
/*    */   implements Runnable
/*    */ {
/*    */   int startX;
/*    */   int startZ;
/*    */   int stopX;
/*    */   int stopZ;
/*    */   
/*    */   public ChunkGenerateTask(int startx, int startz, int stopx, int stopz)
/*    */   {
/* 35 */     this.startX = startx;
/* 36 */     this.startZ = startz;
/* 37 */     this.stopX = stopx;
/* 38 */     this.stopZ = stopz;
/*    */   }
/*    */   
/*    */ 
/*    */   public void run()
/*    */   {
/* 44 */     int maxgen = 10;
/* 45 */     int i = 0;
/*    */     
/* 47 */     for (int x = this.startX; x <= this.stopX; x++) {
/* 48 */       for (int z = this.startZ; z <= this.stopZ; z++) {
/* 49 */         i++;
/*    */         
/* 51 */         Chunk chunk = Bukkit.getWorld("world").getChunkAt(x, z);
/* 52 */         chunk.load(true);
/*    */         
/*    */ 
/* 55 */         chunk.unload(true, false);
/*    */         
/*    */ 
/* 58 */         if (i > maxgen) {
/* 59 */           TaskMaster.syncTask(new ChunkGenerateTask(x, z, this.stopX, this.stopZ));
/* 60 */           return;
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\ChunkGenerateTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
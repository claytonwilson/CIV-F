/*     */ package com.avrgaming.civcraft.randomevents.components;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.randomevents.RandomEvent;
/*     */ import com.avrgaming.civcraft.randomevents.RandomEventComponent;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import java.util.HashMap;
/*     */ import java.util.Random;
/*     */ import org.bukkit.Chunk;
/*     */ import org.bukkit.block.Block;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PickRandomBlock
/*     */   extends RandomEventComponent
/*     */ {
/*     */   public void process()
/*     */   {
/* 103 */     TaskMaster.syncTask(new Runnable()
/*     */     {
/*     */       RandomEvent event;
/*     */       
/*     */       public void run()
/*     */       {
/*     */         try
/*     */         {
/*  34 */           int max_x = CivSettings.getInteger(CivSettings.randomEventsConfig, "max_x").intValue();
/*  35 */           int max_z = CivSettings.getInteger(CivSettings.randomEventsConfig, "max_z").intValue();
/*  36 */           int min_x = CivSettings.getInteger(CivSettings.randomEventsConfig, "min_x").intValue();
/*  37 */           int min_z = CivSettings.getInteger(CivSettings.randomEventsConfig, "min_z").intValue();
/*     */           
/*  39 */           int range_x = max_x - min_x;
/*  40 */           int range_z = max_z - min_z;
/*     */           
/*     */ 
/*  43 */           range_x /= 16;
/*  44 */           range_z /= 16;
/*     */           
/*  46 */           BlockCoord bcoord = null;
/*  47 */           ChunkCoord coord = null;
/*  48 */           for (int i = 0; i < 10; i++)
/*     */           {
/*  50 */             Random rand = new Random();
/*  51 */             int randX = rand.nextInt(range_x) - max_x / 16;
/*  52 */             int randZ = rand.nextInt(range_z) - max_z / 16;
/*     */             
/*  54 */             coord = new ChunkCoord("world", randX, randZ);
/*     */             
/*     */ 
/*  57 */             if (CivGlobal.getCultureChunk(coord) == null)
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*  62 */               int startY = rand.nextInt(20) + 4;
/*  63 */               for (int x = 0; x < 16; x++) {
/*  64 */                 for (int z = 0; z < 16; z++) {
/*  65 */                   for (int y = startY; y < 50; y++) {
/*  66 */                     Block block = coord.getChunk().getBlock(x, y, z);
/*     */                     
/*     */ 
/*  69 */                     if ((ItemManager.getId(block) == 1) || (ItemManager.getId(block) == 13)) {
/*  70 */                       bcoord = new BlockCoord(block);
/*  71 */                       break;
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */               
/*  77 */               if (bcoord != null) {
/*     */                 break;
/*     */               }
/*     */             }
/*     */           }
/*     */           
/*  83 */           if ((bcoord == null) || (coord == null)) {
/*  84 */             CivLog.warning("Couldn't find a suitable block for PickRandomBlock after 10 retries.");
/*  85 */             return;
/*     */           }
/*     */           
/*     */ 
/*  89 */           this.event.componentVars.put(PickRandomBlock.this.getString("varname"), bcoord.toString());
/*     */           
/*     */ 
/*  92 */           Random rand = new Random();
/*  93 */           int y_min = bcoord.getY() - rand.nextInt(10);
/*  94 */           int y_max = bcoord.getY() + rand.nextInt(10);
/*     */           
/*  96 */           PickRandomBlock.this.sendMessage("Block is somewhere near chunk " + coord.getX() + "," + coord.getZ() + " between y=" + y_min + " and y=" + y_max);
/*  97 */           PickRandomBlock.this.sendMessage("To get the actual coordinates multiply these numbers by 16.");
/*     */         } catch (InvalidConfiguration e) {
/*  99 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\randomevents\components\PickRandomBlock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
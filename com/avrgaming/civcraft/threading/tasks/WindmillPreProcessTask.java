/*     */ package com.avrgaming.civcraft.threading.tasks;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.CivTaskAbortException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.StructureChest;
/*     */ import com.avrgaming.civcraft.structure.Windmill;
/*     */ import com.avrgaming.civcraft.threading.CivAsyncTask;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.util.MultiInventory;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.Random;
/*     */ import org.bukkit.ChunkSnapshot;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
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
/*     */ public class WindmillPreProcessTask
/*     */   extends CivAsyncTask
/*     */ {
/*     */   private ArrayList<ChunkSnapshot> snapshots;
/*     */   private Windmill windmill;
/*     */   
/*     */   public WindmillPreProcessTask(Windmill windmill, ArrayList<ChunkSnapshot> snaphots)
/*     */   {
/*  46 */     this.snapshots = snaphots;
/*  47 */     this.windmill = windmill;
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/*  54 */       int plant_max = CivSettings.getInteger(CivSettings.structureConfig, "windmill.plant_max").intValue();
/*     */       
/*  56 */       if (this.windmill.getCiv().hasTechnology("tech_machinery")) {
/*  57 */         plant_max *= 2;
/*     */       }
/*     */     } catch (InvalidConfiguration e) {
/*  60 */       e.printStackTrace();
/*  61 */       return;
/*     */     }
/*     */     
/*     */ 
/*  65 */     ArrayList<StructureChest> sources = this.windmill.getAllChestsById(0);
/*  66 */     MultiInventory source_inv = new MultiInventory();
/*     */     
/*  68 */     for (StructureChest src : sources) {
/*     */       try {
/*  70 */         syncLoadChunk(src.getCoord().getWorldname(), src.getCoord().getX(), src.getCoord().getZ());
/*     */         try
/*     */         {
/*  73 */           tmp = getChestInventory(src.getCoord().getWorldname(), src.getCoord().getX(), src.getCoord().getY(), src.getCoord().getZ(), true);
/*     */         } catch (CivTaskAbortException e) { Inventory tmp;
/*     */           return;
/*     */         }
/*     */         Inventory tmp;
/*  78 */         source_inv.addInventory(tmp);
/*     */       } catch (InterruptedException e) {
/*  80 */         e.printStackTrace();
/*  81 */         return;
/*     */       }
/*     */     }
/*     */     
/*  85 */     int breadCount = 0;
/*  86 */     int carrotCount = 0;
/*  87 */     int potatoCount = 0;
/*  88 */     ItemStack[] arrayOfItemStack; int j = (arrayOfItemStack = source_inv.getContents()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/*  89 */       if (stack != null)
/*     */       {
/*     */ 
/*     */ 
/*  93 */         switch (ItemManager.getId(stack)) {
/*     */         case 295: 
/*  95 */           breadCount += stack.getAmount();
/*  96 */           break;
/*     */         case 391: 
/*  98 */           carrotCount += stack.getAmount();
/*  99 */           break;
/*     */         case 392: 
/* 101 */           potatoCount += stack.getAmount();
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 109 */     if ((breadCount == 0) && (carrotCount == 0) && (potatoCount == 0)) {
/* 110 */       return;
/*     */     }
/*     */     
/*     */ 
/* 114 */     int plant_max = Math.min(breadCount + carrotCount + potatoCount, plant_max);
/*     */     
/*     */ 
/* 117 */     ArrayList<BlockCoord> blocks = new ArrayList();
/* 118 */     int x; for (Iterator localIterator2 = this.snapshots.iterator(); localIterator2.hasNext(); 
/* 119 */         x < 16)
/*     */     {
/* 118 */       ChunkSnapshot snapshot = (ChunkSnapshot)localIterator2.next();
/* 119 */       x = 0; continue;
/* 120 */       for (int z = 0; z < 16; z++) {
/* 121 */         for (int y = 0; y < 255; y++)
/*     */         {
/*     */ 
/* 124 */           if ((ItemManager.getBlockTypeId(snapshot, x, y, z) == 60) && 
/* 125 */             (ItemManager.getBlockTypeId(snapshot, x, y + 1, z) == 0)) {
/* 126 */             int blockx = snapshot.getX() * 16 + x;
/* 127 */             int blocky = y + 1;
/* 128 */             int blockz = snapshot.getZ() * 16 + z;
/*     */             
/* 130 */             blocks.add(new BlockCoord(this.windmill.getCorner().getWorldname(), 
/* 131 */               blockx, blocky, blockz));
/*     */           }
/*     */         }
/*     */       }
/* 119 */       x++;
/*     */     }
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
/* 139 */     Object plantBlocks = new ArrayList();
/*     */     
/* 141 */     Random rand = new Random();
/* 142 */     for (int i = 0; i < plant_max; i++) {
/* 143 */       if (blocks.isEmpty()) {
/*     */         break;
/*     */       }
/*     */       
/* 147 */       BlockCoord coord = (BlockCoord)blocks.get(rand.nextInt(blocks.size()));
/* 148 */       blocks.remove(coord);
/* 149 */       ((ArrayList)plantBlocks).add(coord);
/*     */     }
/*     */     
/*     */ 
/* 153 */     TaskMaster.syncTask(new WindmillPostProcessSyncTask(this.windmill, (ArrayList)plantBlocks, 
/* 154 */       breadCount, carrotCount, potatoCount, source_inv));
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\WindmillPreProcessTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
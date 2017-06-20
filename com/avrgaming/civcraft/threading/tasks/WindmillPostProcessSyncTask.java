/*     */ package com.avrgaming.civcraft.threading.tasks;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.structure.Windmill;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.util.MultiInventory;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Random;
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
/*     */ public class WindmillPostProcessSyncTask
/*     */   implements Runnable
/*     */ {
/*     */   ArrayList<BlockCoord> plantBlocks;
/*     */   Windmill windmill;
/*     */   int breadCount;
/*     */   int carrotCount;
/*     */   int potatoCount;
/*     */   MultiInventory source_inv;
/*     */   
/*     */   public WindmillPostProcessSyncTask(Windmill windmill, ArrayList<BlockCoord> plantBlocks, int breadCount, int carrotCount, int potatoCount, MultiInventory source_inv)
/*     */   {
/*  42 */     this.plantBlocks = plantBlocks;
/*  43 */     this.windmill = windmill;
/*  44 */     this.breadCount = breadCount;
/*  45 */     this.carrotCount = carrotCount;
/*  46 */     this.potatoCount = potatoCount;
/*  47 */     this.source_inv = source_inv;
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*  52 */     Random rand = new Random();
/*     */     
/*  54 */     for (BlockCoord coord : this.plantBlocks)
/*     */     {
/*  56 */       int randomCropType = rand.nextInt(3);
/*     */       
/*  58 */       switch (randomCropType) {
/*     */       case 0: 
/*  60 */         if (this.breadCount > 0)
/*     */         {
/*     */           try {
/*  63 */             this.source_inv.removeItem(295, 1);
/*     */           } catch (CivException e) {
/*  65 */             e.printStackTrace();
/*     */           }
/*  67 */           this.breadCount -= 1;
/*  68 */           ItemManager.setTypeId(coord.getBlock(), 59);
/*  69 */           ItemManager.setData(coord.getBlock(), 0, true); }
/*  70 */         break;
/*     */       
/*     */       case 1: 
/*  73 */         if (this.carrotCount > 0)
/*     */         {
/*     */           try {
/*  76 */             this.source_inv.removeItem(391, 1);
/*     */           } catch (CivException e) {
/*  78 */             e.printStackTrace();
/*     */           }
/*  80 */           this.carrotCount -= 1;
/*  81 */           ItemManager.setTypeId(coord.getBlock(), 141);
/*  82 */           ItemManager.setData(coord.getBlock(), 0, true);
/*     */         }
/*  84 */         break;
/*     */       
/*     */ 
/*     */       case 2: 
/*  88 */         if (this.potatoCount > 0)
/*     */         {
/*     */           try {
/*  91 */             this.source_inv.removeItem(392, 1);
/*     */           } catch (CivException e) {
/*  93 */             e.printStackTrace();
/*     */           }
/*  95 */           this.potatoCount -= 1;
/*  96 */           ItemManager.setTypeId(coord.getBlock(), 142);
/*  97 */           ItemManager.setData(coord.getBlock(), 0, true);
/*     */         }
/*  99 */         break;
/*     */       }
/*     */       
/*     */       
/*     */ 
/* 104 */       if (this.breadCount > 0)
/*     */       {
/*     */         try {
/* 107 */           this.source_inv.removeItem(295, 1);
/*     */         } catch (CivException e) {
/* 109 */           e.printStackTrace();
/*     */         }
/* 111 */         this.breadCount -= 1;
/* 112 */         ItemManager.setTypeId(coord.getBlock(), 59);
/* 113 */         ItemManager.setData(coord.getBlock(), 0, true);
/*     */ 
/*     */ 
/*     */       }
/* 117 */       else if (this.carrotCount > 0)
/*     */       {
/*     */         try {
/* 120 */           this.source_inv.removeItem(391, 1);
/*     */         } catch (CivException e) {
/* 122 */           e.printStackTrace();
/*     */         }
/* 124 */         this.carrotCount -= 1;
/* 125 */         ItemManager.setTypeId(coord.getBlock(), 141);
/* 126 */         ItemManager.setData(coord.getBlock(), 0, true);
/*     */ 
/*     */ 
/*     */       }
/* 130 */       else if (this.potatoCount > 0)
/*     */       {
/*     */         try {
/* 133 */           this.source_inv.removeItem(392, 1);
/*     */         } catch (CivException e) {
/* 135 */           e.printStackTrace();
/*     */         }
/* 137 */         this.potatoCount -= 1;
/* 138 */         ItemManager.setTypeId(coord.getBlock(), 142);
/* 139 */         ItemManager.setData(coord.getBlock(), 0, true);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\WindmillPostProcessSyncTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.avrgaming.civcraft.threading.tasks;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.CivTaskAbortException;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreMaterial;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.object.StructureChest;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import com.avrgaming.civcraft.structure.Trommel;
/*     */ import com.avrgaming.civcraft.structure.Trommel.Mineral;
/*     */ import com.avrgaming.civcraft.threading.CivAsyncTask;
/*     */ import com.avrgaming.civcraft.threading.sync.request.UpdateInventoryRequest.Action;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.util.MultiInventory;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
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
/*     */ public class TrommelAsyncTask
/*     */   extends CivAsyncTask
/*     */ {
/*     */   Trommel trommel;
/*  45 */   private static final int GRAVEL_RATE = CivSettings.getIntegerStructure("trommel.gravel_rate");
/*     */   
/*  47 */   public static HashSet<String> debugTowns = new HashSet();
/*     */   
/*     */   public static void debug(Trommel trommel, String msg) {
/*  50 */     if (debugTowns.contains(trommel.getTown().getName())) {
/*  51 */       CivLog.warning("TrommelDebug:" + trommel.getTown().getName() + ":" + msg);
/*     */     }
/*     */   }
/*     */   
/*     */   public TrommelAsyncTask(Structure trommel) {
/*  56 */     this.trommel = ((Trommel)trommel);
/*     */   }
/*     */   
/*     */   public void processTrommelUpdate() {
/*  60 */     if (!this.trommel.isActive()) {
/*  61 */       debug(this.trommel, "trommel inactive...");
/*  62 */       return;
/*     */     }
/*     */     
/*  65 */     debug(this.trommel, "Processing trommel...");
/*     */     
/*  67 */     ArrayList<StructureChest> sources = this.trommel.getAllChestsById(1);
/*  68 */     ArrayList<StructureChest> destinations = this.trommel.getAllChestsById(2);
/*     */     
/*  70 */     if ((sources.size() != 2) || (destinations.size() != 2)) {
/*  71 */       CivLog.error("Bad chests for trommel in town:" + this.trommel.getTown().getName() + " sources:" + sources.size() + " dests:" + destinations.size());
/*  72 */       return;
/*     */     }
/*     */     
/*     */ 
/*  76 */     MultiInventory source_inv = new MultiInventory();
/*  77 */     MultiInventory dest_inv = new MultiInventory();
/*     */     Inventory tmp;
/*     */     ItemStack stack;
/*  80 */     try { Inventory tmp; for (StructureChest src : sources)
/*     */       {
/*     */         try
/*     */         {
/*  84 */           tmp = getChestInventory(src.getCoord().getWorldname(), src.getCoord().getX(), src.getCoord().getY(), src.getCoord().getZ(), false);
/*     */         } catch (CivTaskAbortException e) {
/*     */           Inventory tmp;
/*  87 */           CivLog.warning("Trommel:" + e.getMessage());
/*  88 */           return;
/*     */         }
/*  90 */         if (tmp == null) {
/*  91 */           this.trommel.skippedCounter += 1;
/*  92 */           return;
/*     */         }
/*  94 */         source_inv.addInventory(tmp);
/*     */       }
/*     */       
/*  97 */       boolean full = true;
/*  98 */       for (StructureChest dst : destinations)
/*     */       {
/*     */         try
/*     */         {
/* 102 */           tmp = getChestInventory(dst.getCoord().getWorldname(), dst.getCoord().getX(), dst.getCoord().getY(), dst.getCoord().getZ(), false);
/*     */         } catch (CivTaskAbortException e) {
/*     */           Inventory tmp;
/* 105 */           CivLog.warning("Trommel:" + e.getMessage());
/* 106 */           return;
/*     */         }
/* 108 */         if (tmp == null) {
/* 109 */           this.trommel.skippedCounter += 1;
/* 110 */           return;
/*     */         }
/* 112 */         dest_inv.addInventory(tmp);
/*     */         ItemStack[] arrayOfItemStack2;
/* 114 */         int j = (arrayOfItemStack2 = tmp.getContents()).length; for (int i = 0; i < j; i++) { stack = arrayOfItemStack2[i];
/* 115 */           if (stack == null) {
/* 116 */             full = false;
/* 117 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 122 */       if (full)
/*     */       {
/* 124 */         return;
/*     */       }
/*     */     }
/*     */     catch (InterruptedException e) {
/* 128 */       return;
/*     */     }
/*     */     
/* 131 */     debug(this.trommel, "Processing trommel:" + this.trommel.skippedCounter + 1);
/* 132 */     ItemStack[] contents = source_inv.getContents();
/* 133 */     for (int i = 0; i < this.trommel.skippedCounter + 1; i++) {
/*     */       ItemStack[] arrayOfItemStack1;
/* 135 */       stack = (arrayOfItemStack1 = contents).length; for (tmp = 0; tmp < stack; tmp++) { ItemStack stack = arrayOfItemStack1[tmp];
/* 136 */         if (stack != null)
/*     */         {
/*     */ 
/*     */ 
/* 140 */           if (ItemManager.getId(stack) == 4) {
/*     */             try {
/* 142 */               updateInventory(UpdateInventoryRequest.Action.REMOVE, source_inv, ItemManager.createItemStack(4, 1));
/*     */             } catch (InterruptedException e) {
/* 144 */               return;
/*     */             }
/*     */             
/*     */ 
/* 148 */             Random rand = new Random();
/* 149 */             int rand1 = rand.nextInt(10000);
/*     */             ItemStack newItem;
/*     */             ItemStack newItem;
/* 152 */             if (rand1 < (int)(this.trommel.getMineralChance(Trommel.Mineral.CHROMIUM) * 10000.0D)) {
/* 153 */               newItem = LoreMaterial.spawn((LoreMaterial)LoreMaterial.materialMap.get("mat_chromium_ore")); } else { ItemStack newItem;
/* 154 */               if (rand1 < (int)(this.trommel.getMineralChance(Trommel.Mineral.EMERALD) * 10000.0D)) {
/* 155 */                 newItem = ItemManager.createItemStack(388, 1);
/*     */               } else { ItemStack newItem;
/* 157 */                 if (rand1 < (int)(this.trommel.getMineralChance(Trommel.Mineral.DIAMOND) * 10000.0D)) {
/* 158 */                   newItem = ItemManager.createItemStack(264, 1);
/*     */                 } else {
/*     */                   ItemStack newItem;
/* 161 */                   if (rand1 < (int)(this.trommel.getMineralChance(Trommel.Mineral.GOLD) * 10000.0D)) {
/* 162 */                     newItem = ItemManager.createItemStack(266, 1);
/*     */                   } else {
/*     */                     ItemStack newItem;
/* 165 */                     if (rand1 < (int)(this.trommel.getMineralChance(Trommel.Mineral.IRON) * 10000.0D)) {
/* 166 */                       newItem = ItemManager.createItemStack(265, 1);
/*     */                     }
/*     */                     else
/* 169 */                       newItem = ItemManager.createItemStack(13, Integer.valueOf(GRAVEL_RATE).intValue());
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/* 174 */             try { debug(this.trommel, "Updating inventory:" + newItem);
/* 175 */               updateInventory(UpdateInventoryRequest.Action.ADD, dest_inv, newItem);
/*     */             } catch (InterruptedException e) {
/* 177 */               return;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 183 */     this.trommel.skippedCounter = 0;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void run()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 79	com/avrgaming/civcraft/threading/tasks/TrommelAsyncTask:trommel	Lcom/avrgaming/civcraft/structure/Trommel;
/*     */     //   4: getfield 299	com/avrgaming/civcraft/structure/Trommel:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   7: invokevirtual 303	java/util/concurrent/locks/ReentrantLock:tryLock	()Z
/*     */     //   10: ifeq +44 -> 54
/*     */     //   13: aload_0
/*     */     //   14: invokevirtual 308	com/avrgaming/civcraft/threading/tasks/TrommelAsyncTask:processTrommelUpdate	()V
/*     */     //   17: goto +24 -> 41
/*     */     //   20: astore_1
/*     */     //   21: aload_1
/*     */     //   22: invokevirtual 310	java/lang/Exception:printStackTrace	()V
/*     */     //   25: goto +16 -> 41
/*     */     //   28: astore_2
/*     */     //   29: aload_0
/*     */     //   30: getfield 79	com/avrgaming/civcraft/threading/tasks/TrommelAsyncTask:trommel	Lcom/avrgaming/civcraft/structure/Trommel;
/*     */     //   33: getfield 299	com/avrgaming/civcraft/structure/Trommel:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   36: invokevirtual 315	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   39: aload_2
/*     */     //   40: athrow
/*     */     //   41: aload_0
/*     */     //   42: getfield 79	com/avrgaming/civcraft/threading/tasks/TrommelAsyncTask:trommel	Lcom/avrgaming/civcraft/structure/Trommel;
/*     */     //   45: getfield 299	com/avrgaming/civcraft/structure/Trommel:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   48: invokevirtual 315	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   51: goto +13 -> 64
/*     */     //   54: aload_0
/*     */     //   55: getfield 79	com/avrgaming/civcraft/threading/tasks/TrommelAsyncTask:trommel	Lcom/avrgaming/civcraft/structure/Trommel;
/*     */     //   58: ldc_w 318
/*     */     //   61: invokestatic 91	com/avrgaming/civcraft/threading/tasks/TrommelAsyncTask:debug	(Lcom/avrgaming/civcraft/structure/Trommel;Ljava/lang/String;)V
/*     */     //   64: return
/*     */     // Line number table:
/*     */     //   Java source line #190	-> byte code offset #0
/*     */     //   Java source line #193	-> byte code offset #13
/*     */     //   Java source line #194	-> byte code offset #17
/*     */     //   Java source line #195	-> byte code offset #21
/*     */     //   Java source line #197	-> byte code offset #25
/*     */     //   Java source line #198	-> byte code offset #29
/*     */     //   Java source line #199	-> byte code offset #39
/*     */     //   Java source line #198	-> byte code offset #41
/*     */     //   Java source line #200	-> byte code offset #51
/*     */     //   Java source line #201	-> byte code offset #54
/*     */     //   Java source line #203	-> byte code offset #64
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	65	0	this	TrommelAsyncTask
/*     */     //   20	2	1	e	Exception
/*     */     //   28	12	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   13	17	20	java/lang/Exception
/*     */     //   13	28	28	finally
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\TrommelAsyncTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
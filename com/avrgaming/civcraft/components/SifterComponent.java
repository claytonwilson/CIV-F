/*     */ package com.avrgaming.civcraft.components;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivTaskAbortException;
/*     */ import com.avrgaming.civcraft.threading.CivAsyncTask;
/*     */ import com.avrgaming.civcraft.threading.sync.request.UpdateInventoryRequest.Action;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.util.MultiInventory;
/*     */ import java.util.LinkedList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SifterComponent
/*     */   extends Component
/*     */ {
/*  37 */   private LinkedList<SifterItem> items = new LinkedList();
/*     */   
/*     */ 
/*     */   private BlockCoord sourceCoord;
/*     */   
/*     */ 
/*     */   private BlockCoord destCoord;
/*     */   
/*     */ 
/*     */ 
/*     */   public void addSiftItem(int source_type, short source_data, double rate, int result_type, short result_data, int amount)
/*     */   {
/*  49 */     SifterItem si = new SifterItem();
/*  50 */     si.source_type = source_type;
/*  51 */     si.source_data = source_data;
/*  52 */     si.rate = (rate * 1000.0D);
/*  53 */     si.result_type = result_type;
/*  54 */     si.result_data = result_data;
/*  55 */     si.amount = amount;
/*     */     
/*  57 */     this.items.add(si);
/*     */   }
/*     */   
/*     */   public void run(CivAsyncTask task) {
/*  61 */     MultiInventory source = new MultiInventory();
/*  62 */     MultiInventory dest = new MultiInventory();
/*     */     
/*  64 */     if ((this.sourceCoord == null) || (this.destCoord == null)) {
/*  65 */       return;
/*     */     }
/*     */     try
/*     */     {
/*  69 */       Inventory tmp = task.getChestInventory(this.sourceCoord.getWorldname(), this.sourceCoord.getX(), this.sourceCoord.getY(), this.sourceCoord.getZ(), false);
/*  70 */       if (tmp == null) {
/*  71 */         return;
/*     */       }
/*  73 */       source.addInventory(tmp);
/*     */     }
/*     */     catch (InterruptedException|CivTaskAbortException e) {
/*  76 */       return;
/*     */     }
/*     */     
/*  79 */     boolean full = true;
/*     */     try {
/*  81 */       Inventory tmp = task.getChestInventory(this.destCoord.getWorldname(), this.destCoord.getX(), this.destCoord.getY(), this.destCoord.getZ(), false);
/*  82 */       if (tmp == null) {
/*  83 */         return;
/*     */       }
/*  85 */       dest.addInventory(tmp);
/*     */       ItemStack[] arrayOfItemStack;
/*  87 */       int j = (arrayOfItemStack = tmp.getContents()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/*  88 */         if (stack == null) {
/*  89 */           full = false;
/*  90 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (InterruptedException|CivTaskAbortException e) {
/*  95 */       return;
/*     */     }
/*     */     
/*  98 */     if (full) {
/*  99 */       return;
/*     */     }
/*     */     
/* 102 */     process(source, dest, 1, task);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void process(MultiInventory source, MultiInventory dest, int count, CivAsyncTask task)
/*     */   {
/* 111 */     Random rand = new Random();
/* 112 */     int i = 0;
/* 113 */     ItemStack[] arrayOfItemStack; int j = (arrayOfItemStack = source.getContents()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/* 114 */       if ((stack != null) && (ItemManager.getId(stack) != 0))
/*     */       {
/*     */ 
/*     */ 
/* 118 */         SifterItem lowestChanceItem = null;
/* 119 */         boolean found = false;
/*     */         
/* 121 */         for (SifterItem si : this.items) {
/* 122 */           if (si != null)
/*     */           {
/*     */ 
/*     */ 
/* 126 */             if (si.source_type == ItemManager.getId(stack))
/*     */             {
/*     */ 
/*     */ 
/* 130 */               found = true;
/* 131 */               int next = rand.nextInt(1000);
/*     */               
/*     */ 
/*     */ 
/*     */ 
/* 136 */               if (next < si.rate) {
/* 137 */                 if ((lowestChanceItem != null) && (lowestChanceItem.rate >= si.rate)) break;
/* 138 */                 lowestChanceItem = si;
/*     */                 
/* 140 */                 break;
/*     */               }
/*     */             } }
/*     */         }
/* 144 */         if ((found) && (lowestChanceItem != null))
/*     */         {
/*     */ 
/*     */           try
/*     */           {
/*     */ 
/* 150 */             task.updateInventory(UpdateInventoryRequest.Action.REMOVE, source, ItemManager.createItemStack(lowestChanceItem.source_type, 1));
/*     */           } catch (InterruptedException e) {
/* 152 */             return;
/*     */           }
/*     */           try {
/* 155 */             task.updateInventory(UpdateInventoryRequest.Action.ADD, dest, ItemManager.createItemStack(lowestChanceItem.result_type, lowestChanceItem.amount, (short)lowestChanceItem.result_data));
/*     */           } catch (InterruptedException e) {
/* 157 */             return;
/*     */           }
/*     */           
/* 160 */           i++;
/* 161 */           if (i >= count) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLoad() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onSave() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public BlockCoord getSourceCoord()
/*     */   {
/* 183 */     return this.sourceCoord;
/*     */   }
/*     */   
/*     */   public void setSourceCoord(BlockCoord sourceCoord) {
/* 187 */     this.sourceCoord = sourceCoord;
/*     */   }
/*     */   
/*     */   public BlockCoord getDestCoord() {
/* 191 */     return this.destCoord;
/*     */   }
/*     */   
/*     */   public void setDestCoord(BlockCoord destCoord) {
/* 195 */     this.destCoord = destCoord;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\components\SifterComponent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
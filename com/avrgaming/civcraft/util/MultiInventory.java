/*     */ package com.avrgaming.civcraft.util;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreMaterial;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.inventory.DoubleChestInventory;
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
/*     */ public class MultiInventory
/*     */ {
/*  34 */   private ArrayList<Inventory> invs = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean isCorrectItemStack(ItemStack stack, String mid, int type, short data)
/*     */   {
/*  40 */     if (stack == null) {
/*  41 */       return false;
/*     */     }
/*     */     
/*  44 */     if (mid != null)
/*     */     {
/*  46 */       LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/*  47 */       if (craftMat == null) {
/*  48 */         return false;
/*     */       }
/*     */       
/*  51 */       if (!craftMat.getConfigId().equals(mid)) {
/*  52 */         return false;
/*     */       }
/*     */     } else {
/*  55 */       if (ItemManager.getId(stack) != type) {
/*  56 */         return false;
/*     */       }
/*     */       
/*     */ 
/*  60 */       if ((ItemManager.getMaterial(type).getMaxDurability() == 0) && 
/*  61 */         (ItemManager.getData(stack) != data))
/*     */       {
/*  63 */         return false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  68 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private int removeItemFromInventory(Inventory inv, String mid, int type, short data, int amount)
/*     */   {
/*  74 */     int removed = 0;
/*  75 */     int notRemoved = amount;
/*     */     
/*  77 */     ItemStack[] contents = inv.getContents();
/*  78 */     for (int i = 0; i < contents.length; i++) {
/*  79 */       ItemStack stack = contents[i];
/*  80 */       if (isCorrectItemStack(stack, mid, type, data))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*  85 */         int stackAmount = stack.getAmount();
/*     */         
/*  87 */         if (stackAmount > notRemoved)
/*     */         {
/*  89 */           stack.setAmount(stackAmount - notRemoved);
/*  90 */           removed = notRemoved;
/*  91 */           notRemoved = 0;
/*  92 */         } else if (stackAmount == notRemoved)
/*     */         {
/*  94 */           contents[i] = null;
/*  95 */           removed = notRemoved;
/*  96 */           notRemoved = 0;
/*     */         }
/*     */         else {
/*  99 */           removed += stackAmount;
/* 100 */           notRemoved -= stackAmount;
/* 101 */           contents[i] = null;
/*     */         }
/*     */         
/* 104 */         if (notRemoved == 0) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 110 */     inv.setContents(contents);
/* 111 */     return removed;
/*     */   }
/*     */   
/*     */   public void addInventory(Inventory inv)
/*     */   {
/* 116 */     this.invs.add(inv);
/*     */   }
/*     */   
/*     */   public void addInventory(DoubleChestInventory inv) {
/* 120 */     this.invs.add(inv.getLeftSide());
/* 121 */     this.invs.add(inv.getRightSide());
/*     */   }
/*     */   
/*     */ 
/*     */   public int addItem(ItemStack items)
/*     */   {
/* 127 */     int leftoverAmount = 0;
/* 128 */     for (Inventory inv : this.invs)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 134 */       leftoverAmount = 0;
/* 135 */       HashMap<Integer, ItemStack> leftovers = inv.addItem(new ItemStack[] { items });
/*     */       
/* 137 */       for (ItemStack i : leftovers.values()) {
/* 138 */         leftoverAmount += i.getAmount();
/*     */       }
/*     */       
/* 141 */       if (leftoverAmount == 0) {
/* 142 */         return 0;
/*     */       }
/* 144 */       if (leftoverAmount != items.getAmount())
/*     */       {
/*     */ 
/*     */ 
/* 148 */         items.setAmount(leftoverAmount);
/*     */       }
/*     */     }
/*     */     
/* 152 */     return leftoverAmount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean removeItem(String mid, int type, short data, int amount)
/*     */     throws CivException
/*     */   {
/* 160 */     ArrayList<ItemInvPair> toBeRemoved = new ArrayList();
/*     */     
/* 162 */     int count = amount;
/* 163 */     ItemStack item; for (Inventory inv : this.invs) { ItemStack[] arrayOfItemStack;
/* 164 */       int j = (arrayOfItemStack = inv.getContents()).length; for (int i = 0; i < j; i++) { item = arrayOfItemStack[i];
/* 165 */         if (isCorrectItemStack(item, mid, type, data))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 175 */           if (item.getAmount() > count) {
/* 176 */             toBeRemoved.add(new ItemInvPair(inv, mid, type, data, count));
/* 177 */             count = 0;
/* 178 */             break; }
/* 179 */           if (item.getAmount() == count) {
/* 180 */             toBeRemoved.add(new ItemInvPair(inv, mid, type, data, count));
/* 181 */             count = 0;
/* 182 */             break;
/*     */           }
/* 184 */           toBeRemoved.add(new ItemInvPair(inv, mid, type, data, item.getAmount()));
/* 185 */           count -= item.getAmount();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 191 */     if (count != 0) {
/* 192 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 196 */     int totalActuallyRemoved = 0;
/* 197 */     for (ItemInvPair invPair : toBeRemoved) {
/* 198 */       Inventory inv = invPair.inv;
/* 199 */       totalActuallyRemoved += removeItemFromInventory(inv, invPair.mid, invPair.type, invPair.data, invPair.amount);
/*     */     }
/*     */     
/* 202 */     if (totalActuallyRemoved != amount) {
/* 203 */       throw new CivException("Inventory Error! We tried to remove " + amount + " items but could only remove " + totalActuallyRemoved);
/*     */     }
/*     */     
/* 206 */     return true;
/*     */   }
/*     */   
/*     */   public boolean removeItem(ItemStack item) throws CivException
/*     */   {
/* 211 */     LoreMaterial loreMat = LoreMaterial.getMaterial(item);
/* 212 */     if (loreMat != null) {
/* 213 */       return removeItem(loreMat.getId(), 0, (short)0, item.getAmount());
/*     */     }
/*     */     
/* 216 */     return removeItem(null, ItemManager.getId(item), ItemManager.getData(item), item.getAmount());
/*     */   }
/*     */   
/*     */   public boolean removeItem(int typeid, int amount) throws CivException
/*     */   {
/* 221 */     return removeItem(null, typeid, (short)0, amount);
/*     */   }
/*     */   
/*     */   public boolean removeItem(int typeid, short data, int amount) throws CivException {
/* 225 */     return removeItem(null, typeid, data, amount);
/*     */   }
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
/*     */   public boolean contains(String mid, int type, short data, int amount)
/*     */   {
/* 284 */     int count = 0;
/* 285 */     for (Inventory inv : this.invs) { ItemStack[] arrayOfItemStack;
/* 286 */       int j = (arrayOfItemStack = inv.getContents()).length; for (int i = 0; i < j; i++) { ItemStack item = arrayOfItemStack[i];
/* 287 */         if (item != null)
/*     */         {
/*     */ 
/*     */ 
/* 291 */           if (mid != null) {
/* 292 */             LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(item);
/* 293 */             if (craftMat == null) {
/*     */               continue;
/*     */             }
/*     */             
/* 297 */             if (!craftMat.getConfigId().equals(mid)) {
/*     */               continue;
/*     */             }
/*     */           }
/*     */           else {
/* 302 */             if (ItemManager.getId(item) != type) {
/*     */               continue;
/*     */             }
/*     */             
/*     */ 
/* 307 */             if ((ItemManager.getMaterial(type).getMaxDurability() == 0) && 
/* 308 */               (ItemManager.getData(item) != data)) {
/*     */               continue;
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 314 */           count += item.getAmount();
/* 315 */           if (count >= amount) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 321 */     if (count >= amount)
/* 322 */       return true;
/* 323 */     return false;
/*     */   }
/*     */   
/*     */   public ItemStack[] getContents()
/*     */   {
/* 328 */     int size = 0;
/* 329 */     for (Inventory inv : this.invs) {
/* 330 */       size += inv.getContents().length;
/*     */     }
/*     */     
/* 333 */     ItemStack[] array = new ItemStack[size];
/*     */     
/* 335 */     int i = 0;
/* 336 */     Inventory inv; int j; for (Iterator localIterator2 = this.invs.iterator(); localIterator2.hasNext(); 
/* 337 */         j < inv.getContents().length)
/*     */     {
/* 336 */       inv = (Inventory)localIterator2.next();
/* 337 */       j = 0; continue;
/* 338 */       array[i] = inv.getContents()[j];
/* 339 */       i++;j++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 342 */     return array;
/*     */   }
/*     */   
/*     */   public int getInventoryCount()
/*     */   {
/* 347 */     return this.invs.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\MultiInventory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
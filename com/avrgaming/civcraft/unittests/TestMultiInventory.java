/*    */ package com.avrgaming.civcraft.unittests;
/*    */ 
/*    */ import com.avrgaming.civcraft.util.ItemManager;
/*    */ import com.avrgaming.civcraft.util.MultiInventory;
/*    */ import org.bukkit.inventory.Inventory;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.junit.Assert;
/*    */ import org.junit.Test;
/*    */ import org.junit.runner.RunWith;
/*    */ import org.junit.runners.JUnit4;
/*    */ import org.mockito.Mockito;
/*    */ import org.mockito.stubbing.OngoingStubbing;
/*    */ 
/*    */ 
/*    */ 
/*    */ @RunWith(JUnit4.class)
/*    */ public class TestMultiInventory
/*    */ {
/*    */   private Inventory newEmptyInventory()
/*    */   {
/* 21 */     Inventory inv = (Inventory)Mockito.mock(Inventory.class);
/* 22 */     Mockito.when(inv.getContents()).thenReturn(new ItemStack[54]);
/* 23 */     return inv;
/*    */   }
/*    */   
/*    */   private boolean contentsIsEmpty(ItemStack[] contents) { ItemStack[] arrayOfItemStack;
/* 27 */     int j = (arrayOfItemStack = contents).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/* 28 */       if ((stack != null) && (ItemManager.getId(stack) != 0))
/*    */       {
/*    */ 
/* 31 */         return false; }
/*    */     }
/* 33 */     return true;
/*    */   }
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
/*    */   @Test
/*    */   public void addSingleItemToInventory()
/*    */   {
/* 56 */     MultiInventory multiInv = new MultiInventory();
/* 57 */     multiInv.addInventory(newEmptyInventory());
/* 58 */     Assert.assertEquals(Boolean.valueOf(true), Boolean.valueOf(multiInv.getInventoryCount() == 1));
/*    */     
/*    */ 
/* 61 */     Assert.assertEquals(Boolean.valueOf(true), Boolean.valueOf(contentsIsEmpty(multiInv.getContents())));
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\unittests\TestMultiInventory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
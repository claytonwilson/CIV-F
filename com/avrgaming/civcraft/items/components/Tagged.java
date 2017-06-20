/*    */ package com.avrgaming.civcraft.items.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*    */ import com.avrgaming.civcraft.util.ItemManager;
/*    */ import gpl.AttributeUtil;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Tagged
/*    */   extends ItemComponent
/*    */ {
/*    */   public void onPrepareCreate(AttributeUtil attrUtil) {}
/*    */   
/*    */   public ItemStack addTag(ItemStack src, String tag)
/*    */   {
/* 18 */     AttributeUtil attrs = new AttributeUtil(src);
/* 19 */     attrs.setCivCraftProperty("tag", tag);
/* 20 */     return attrs.getStack();
/*    */   }
/*    */   
/*    */   public String getTag(ItemStack src) {
/* 24 */     AttributeUtil attrs = new AttributeUtil(src);
/* 25 */     return attrs.getCivCraftProperty("tag");
/*    */   }
/*    */   
/*    */   public static String matrixHasSameTag(ItemStack[] matrix) {
/* 29 */     String tag = null;
/*    */     
/* 31 */     ItemStack[] arrayOfItemStack = matrix;int j = matrix.length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/* 32 */       if ((stack != null) && (ItemManager.getId(stack) != 0))
/*    */       {
/*    */ 
/*    */ 
/* 36 */         LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/* 37 */         if (craftMat == null) {
/* 38 */           return null;
/*    */         }
/*    */         
/* 41 */         Tagged tagged = (Tagged)craftMat.getComponent("Tagged");
/* 42 */         if (tagged == null) {
/* 43 */           return null;
/*    */         }
/*    */         
/* 46 */         if (tag == null) {
/* 47 */           tag = tagged.getTag(stack);
/*    */ 
/*    */         }
/* 50 */         else if (!tagged.getTag(stack).equals(tag)) {
/* 51 */           return null;
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 56 */     return tag;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\Tagged.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*   */ package com.avrgaming.civcraft.items.components;
/*   */ 
/*   */ import gpl.AttributeUtil;
/*   */ 
/*   */ public class LeatherColor extends ItemComponent
/*   */ {
/*   */   public void onPrepareCreate(AttributeUtil attrs)
/*   */   {
/* 9 */     attrs.setColor(Long.decode("0x" + getString("color")));
/*   */   }
/*   */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\LeatherColor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
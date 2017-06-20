/*    */ package com.avrgaming.civcraft.items.components;
/*    */ 
/*    */ import gpl.AttributeUtil;
/*    */ import gpl.AttributeUtil.Attribute;
/*    */ import gpl.AttributeUtil.Attribute.Builder;
/*    */ import gpl.AttributeUtil.AttributeType;
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
/*    */ public class MaxHealth
/*    */   extends ItemComponent
/*    */ {
/*    */   public void onPrepareCreate(AttributeUtil attrs)
/*    */   {
/* 29 */     attrs.add(AttributeUtil.Attribute.newBuilder().name("Health")
/* 30 */       .type(AttributeUtil.AttributeType.GENERIC_MAX_HEALTH)
/* 31 */       .amount(getDouble("value"))
/* 32 */       .build());
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\MaxHealth.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
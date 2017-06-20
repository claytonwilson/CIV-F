/*    */ package com.avrgaming.civcraft.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.structure.Buildable;
/*    */ 
/*    */ public class AttributeRate extends AttributeBaseRate {
/*    */   public double getGenerated() {
/*  7 */     if (getBuildable().isActive()) {
/*  8 */       return super.getDouble("value");
/*    */     }
/* 10 */     return 0.0D;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\components\AttributeRate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
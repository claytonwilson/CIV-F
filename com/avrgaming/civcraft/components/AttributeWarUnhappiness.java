/*    */ package com.avrgaming.civcraft.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.structure.Buildable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AttributeWarUnhappiness
/*    */   extends Component
/*    */ {
/*    */   public double value;
/*    */   
/*    */   public void createComponent(Buildable buildable, boolean async)
/*    */   {
/* 15 */     super.createComponent(buildable, async);
/* 16 */     this.value = getDouble("value");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\components\AttributeWarUnhappiness.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
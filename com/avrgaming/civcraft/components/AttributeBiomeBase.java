/*   */ package com.avrgaming.civcraft.components;
/*   */ 
/*   */ import com.avrgaming.civcraft.object.CultureChunk;
/*   */ 
/*   */ public abstract class AttributeBiomeBase extends Component
/*   */ {
/*   */   public AttributeBiomeBase() {
/* 8 */     this.typeName = "AttributeBiomeBase";
/*   */   }
/*   */   
/*   */   public abstract double getGenerated(CultureChunk paramCultureChunk);
/*   */   
/*   */   public abstract String getAttribute();
/*   */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\components\AttributeBiomeBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
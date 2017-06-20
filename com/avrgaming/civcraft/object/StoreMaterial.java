/*    */ package com.avrgaming.civcraft.object;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StoreMaterial
/*    */ {
/*    */   public int type;
/*    */   
/*    */ 
/*    */ 
/*    */   public byte data;
/*    */   
/*    */ 
/*    */ 
/*    */   public String name;
/*    */   
/*    */ 
/*    */ 
/*    */   public double price;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public StoreMaterial(String strtype, String strdata, String strname, String strprice)
/*    */   {
/* 28 */     this.type = Integer.valueOf(strtype).intValue();
/* 29 */     this.data = Integer.valueOf(strdata).byteValue();
/* 30 */     this.name = strname;
/* 31 */     this.price = Double.valueOf(strprice).doubleValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\StoreMaterial.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
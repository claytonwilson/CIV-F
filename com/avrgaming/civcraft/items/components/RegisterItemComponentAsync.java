/*    */ package com.avrgaming.civcraft.items.components;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RegisterItemComponentAsync
/*    */   implements Runnable
/*    */ {
/*    */   public ItemComponent component;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String name;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean register;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public RegisterItemComponentAsync(ItemComponent itemComp, String name, boolean register)
/*    */   {
/* 29 */     this.component = itemComp;
/* 30 */     this.name = name;
/* 31 */     this.register = register;
/*    */   }
/*    */   
/*    */   public void run() {}
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\RegisterItemComponentAsync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package com.avrgaming.civcraft.exception;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InvalidNameException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = -697962518690144537L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public InvalidNameException()
/*    */   {
/* 26 */     super("Invalid name, name was null");
/*    */   }
/*    */   
/*    */   public InvalidNameException(String message) {
/* 30 */     super("Invalid name:" + message);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\exception\InvalidNameException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package com.avrgaming.civcraft.object;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.InvalidNameException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NamedObject
/*    */ {
/*    */   private int id;
/*    */   private String name;
/*    */   
/*    */   public void setName(String newname)
/*    */     throws InvalidNameException
/*    */   {
/* 33 */     validateName(newname);
/* 34 */     this.name = newname;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 38 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setId(int i) {
/* 42 */     this.id = i;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 46 */     return this.id;
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   private void validateName(String name)
/*    */     throws InvalidNameException
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_1
/*    */     //   1: ifnonnull +11 -> 12
/*    */     //   4: new 21	com/avrgaming/civcraft/exception/InvalidNameException
/*    */     //   7: dup
/*    */     //   8: invokespecial 38	com/avrgaming/civcraft/exception/InvalidNameException:<init>	()V
/*    */     //   11: athrow
/*    */     //   12: aload_1
/*    */     //   13: invokevirtual 39	java/lang/String:toLowerCase	()Ljava/lang/String;
/*    */     //   16: dup
/*    */     //   17: astore_2
/*    */     //   18: invokevirtual 44	java/lang/String:hashCode	()I
/*    */     //   21: lookupswitch	default:+160->181, -347124400:+67->88, 0:+79->100, 98512:+91->112, 3387192:+103->124, 3392903:+115->136, 3566226:+127->148, 98629247:+139->160
/*    */     //   88: aload_2
/*    */     //   89: ldc 47
/*    */     //   91: invokevirtual 49	java/lang/String:equals	(Ljava/lang/Object;)Z
/*    */     //   94: ifne +78 -> 172
/*    */     //   97: goto +84 -> 181
/*    */     //   100: aload_2
/*    */     //   101: ldc 53
/*    */     //   103: invokevirtual 49	java/lang/String:equals	(Ljava/lang/Object;)Z
/*    */     //   106: ifne +66 -> 172
/*    */     //   109: goto +72 -> 181
/*    */     //   112: aload_2
/*    */     //   113: ldc 55
/*    */     //   115: invokevirtual 49	java/lang/String:equals	(Ljava/lang/Object;)Z
/*    */     //   118: ifne +54 -> 172
/*    */     //   121: goto +60 -> 181
/*    */     //   124: aload_2
/*    */     //   125: ldc 57
/*    */     //   127: invokevirtual 49	java/lang/String:equals	(Ljava/lang/Object;)Z
/*    */     //   130: ifne +42 -> 172
/*    */     //   133: goto +48 -> 181
/*    */     //   136: aload_2
/*    */     //   137: ldc 59
/*    */     //   139: invokevirtual 49	java/lang/String:equals	(Ljava/lang/Object;)Z
/*    */     //   142: ifne +30 -> 172
/*    */     //   145: goto +36 -> 181
/*    */     //   148: aload_2
/*    */     //   149: ldc 61
/*    */     //   151: invokevirtual 49	java/lang/String:equals	(Ljava/lang/Object;)Z
/*    */     //   154: ifne +18 -> 172
/*    */     //   157: goto +24 -> 181
/*    */     //   160: aload_2
/*    */     //   161: ldc 63
/*    */     //   163: invokevirtual 49	java/lang/String:equals	(Ljava/lang/Object;)Z
/*    */     //   166: ifne +6 -> 172
/*    */     //   169: goto +12 -> 181
/*    */     //   172: new 21	com/avrgaming/civcraft/exception/InvalidNameException
/*    */     //   175: dup
/*    */     //   176: aload_1
/*    */     //   177: invokespecial 65	com/avrgaming/civcraft/exception/InvalidNameException:<init>	(Ljava/lang/String;)V
/*    */     //   180: athrow
/*    */     //   181: return
/*    */     // Line number table:
/*    */     //   Java source line #50	-> byte code offset #0
/*    */     //   Java source line #51	-> byte code offset #4
/*    */     //   Java source line #54	-> byte code offset #12
/*    */     //   Java source line #62	-> byte code offset #172
/*    */     //   Java source line #64	-> byte code offset #181
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	182	0	this	NamedObject
/*    */     //   0	182	1	name	String
/*    */     //   17	144	2	str	String
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\NamedObject.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
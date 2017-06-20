/*    */ package com.avrgaming.civcraft.camp;
/*    */ 
/*    */ import com.avrgaming.civcraft.threading.CivAsyncTask;
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
/*    */ public class CampUpdateTick
/*    */   extends CivAsyncTask
/*    */ {
/*    */   private Camp camp;
/*    */   
/*    */   public CampUpdateTick(Camp camp)
/*    */   {
/* 28 */     this.camp = camp;
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public void run()
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield 13	com/avrgaming/civcraft/camp/CampUpdateTick:camp	Lcom/avrgaming/civcraft/camp/Camp;
/*    */     //   4: getfield 20	com/avrgaming/civcraft/camp/Camp:sifterLock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   7: invokevirtual 26	java/util/concurrent/locks/ReentrantLock:tryLock	()Z
/*    */     //   10: ifeq +50 -> 60
/*    */     //   13: aload_0
/*    */     //   14: getfield 13	com/avrgaming/civcraft/camp/CampUpdateTick:camp	Lcom/avrgaming/civcraft/camp/Camp;
/*    */     //   17: invokevirtual 32	com/avrgaming/civcraft/camp/Camp:isSifterEnabled	()Z
/*    */     //   20: ifeq +30 -> 50
/*    */     //   23: aload_0
/*    */     //   24: getfield 13	com/avrgaming/civcraft/camp/CampUpdateTick:camp	Lcom/avrgaming/civcraft/camp/Camp;
/*    */     //   27: getfield 35	com/avrgaming/civcraft/camp/Camp:sifter	Lcom/avrgaming/civcraft/components/SifterComponent;
/*    */     //   30: aload_0
/*    */     //   31: invokevirtual 39	com/avrgaming/civcraft/components/SifterComponent:run	(Lcom/avrgaming/civcraft/threading/CivAsyncTask;)V
/*    */     //   34: goto +16 -> 50
/*    */     //   37: astore_1
/*    */     //   38: aload_0
/*    */     //   39: getfield 13	com/avrgaming/civcraft/camp/CampUpdateTick:camp	Lcom/avrgaming/civcraft/camp/Camp;
/*    */     //   42: getfield 20	com/avrgaming/civcraft/camp/Camp:sifterLock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   45: invokevirtual 44	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*    */     //   48: aload_1
/*    */     //   49: athrow
/*    */     //   50: aload_0
/*    */     //   51: getfield 13	com/avrgaming/civcraft/camp/CampUpdateTick:camp	Lcom/avrgaming/civcraft/camp/Camp;
/*    */     //   54: getfield 20	com/avrgaming/civcraft/camp/Camp:sifterLock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   57: invokevirtual 44	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*    */     //   60: return
/*    */     // Line number table:
/*    */     //   Java source line #33	-> byte code offset #0
/*    */     //   Java source line #35	-> byte code offset #13
/*    */     //   Java source line #36	-> byte code offset #23
/*    */     //   Java source line #38	-> byte code offset #34
/*    */     //   Java source line #39	-> byte code offset #38
/*    */     //   Java source line #40	-> byte code offset #48
/*    */     //   Java source line #39	-> byte code offset #50
/*    */     //   Java source line #43	-> byte code offset #60
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	61	0	this	CampUpdateTick
/*    */     //   37	12	1	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   13	37	37	finally
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\camp\CampUpdateTick.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
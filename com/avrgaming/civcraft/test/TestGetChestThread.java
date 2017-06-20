/*    */ package com.avrgaming.civcraft.test;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivTaskAbortException;
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import com.avrgaming.civcraft.threading.CivAsyncTask;
/*    */ import java.util.Date;
/*    */ import java.util.Random;
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
/*    */ 
/*    */ 
/*    */ public class TestGetChestThread
/*    */   extends CivAsyncTask
/*    */ {
/*    */   public void run()
/*    */   {
/* 36 */     Date startTime = new Date();
/* 37 */     long start = startTime.getTime();
/* 38 */     int requests = 0;
/*    */     for (;;)
/*    */     {
/* 41 */       Random rand = new Random();
/*    */       try {
/* 43 */         Date nowTime = new Date();
/* 44 */         long now = nowTime.getTime();
/*    */         
/* 46 */         getChestInventory("world", rand.nextInt(2000), rand.nextInt(200), rand.nextInt(2000), true);
/* 47 */         requests++;
/*    */         
/* 49 */         long diff = now - start;
/* 50 */         if (diff > 5000L) {
/* 51 */           start = now;
/* 52 */           double requestsPerSecond = requests / (diff / 1000.0D);
/* 53 */           CivLog.warning("Processed " + requestsPerSecond + " requests per second.");
/* 54 */           requests = 0;
/*    */         }
/*    */       }
/*    */       catch (InterruptedException e) {
/* 58 */         e.printStackTrace();
/*    */       } catch (CivTaskAbortException e) {
/* 60 */         CivLog.warning("Can't keep up! " + e.getMessage());
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\test\TestGetChestThread.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
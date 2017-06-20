/*    */ package com.avrgaming.civcraft.event;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.CivSettings;
/*    */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*    */ import com.avrgaming.civcraft.items.BonusGoodie;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import java.util.Calendar;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
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
/*    */ public class GoodieRepoEvent
/*    */   implements EventInterface
/*    */ {
/*    */   public static void repoProcess()
/*    */   {
/* 56 */     TaskMaster.syncTask(new Runnable()
/*    */     {
/*    */       public void run()
/*    */       {
/*    */         Iterator localIterator2;
/* 39 */         for (Iterator localIterator1 = CivGlobal.getTowns().iterator(); localIterator1.hasNext(); 
/* 40 */             localIterator2.hasNext())
/*    */         {
/* 39 */           Town town = (Town)localIterator1.next();
/* 40 */           localIterator2 = town.getBonusGoodies().iterator(); continue;BonusGoodie goodie = (BonusGoodie)localIterator2.next();
/* 41 */           town.removeGoodie(goodie);
/*    */         }
/*    */         
/*    */ 
/* 45 */         for (BonusGoodie goodie : CivGlobal.getBonusGoodies()) {
/*    */           try {
/* 47 */             goodie.replenish();
/*    */           } catch (Exception e) {
/* 49 */             e.printStackTrace();
/*    */           }
/*    */         }
/*    */       }
/*    */     });
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void process()
/*    */   {
/* 61 */     CivLog.info("TimerEvent: GoodieRepo -------------------------------------");
/* 62 */     repoProcess();
/* 63 */     CivMessage.global("Trade Goodies have been respawned at trade outposts.");
/*    */   }
/*    */   
/*    */   public Calendar getNextDate() throws InvalidConfiguration
/*    */   {
/* 68 */     Calendar cal = EventTimer.getCalendarInServerTimeZone();
/* 69 */     int repo_days = CivSettings.getInteger(CivSettings.goodsConfig, "trade_goodie_repo_days").intValue();
/* 70 */     cal.set(13, 0);
/* 71 */     cal.set(12, 0);
/* 72 */     cal.add(5, repo_days);
/* 73 */     return cal;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\event\GoodieRepoEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.avrgaming.civcraft.threading.timers;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigPlatinumReward;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.wonders.NotreDame;
/*     */ import com.avrgaming.civcraft.structure.wonders.Wonder;
/*     */ import com.avrgaming.global.perks.PlatinumManager;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DailyTimer
/*     */   implements Runnable
/*     */ {
/*  45 */   public static ReentrantLock lock = new ReentrantLock();
/*     */   
/*     */   /* Error */
/*     */   public void run()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: getstatic 17	com/avrgaming/civcraft/threading/timers/DailyTimer:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   3: invokevirtual 25	java/util/concurrent/locks/ReentrantLock:tryLock	()Z
/*     */     //   6: ifeq +178 -> 184
/*     */     //   9: ldc 29
/*     */     //   11: invokestatic 31	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*     */     //   14: aload_0
/*     */     //   15: invokespecial 37	com/avrgaming/civcraft/threading/timers/DailyTimer:collectTownTaxes	()V
/*     */     //   18: aload_0
/*     */     //   19: invokespecial 40	com/avrgaming/civcraft/threading/timers/DailyTimer:payTownUpkeep	()V
/*     */     //   22: aload_0
/*     */     //   23: invokespecial 43	com/avrgaming/civcraft/threading/timers/DailyTimer:payCivUpkeep	()V
/*     */     //   26: aload_0
/*     */     //   27: invokespecial 46	com/avrgaming/civcraft/threading/timers/DailyTimer:decrementResidentGraceCounters	()V
/*     */     //   30: invokestatic 49	com/avrgaming/civcraft/main/CivGlobal:getStructureIterator	()Ljava/util/Iterator;
/*     */     //   33: astore_1
/*     */     //   34: goto +33 -> 67
/*     */     //   37: aload_1
/*     */     //   38: invokeinterface 55 1 0
/*     */     //   43: checkcast 61	java/util/Map$Entry
/*     */     //   46: invokeinterface 63 1 0
/*     */     //   51: checkcast 66	com/avrgaming/civcraft/structure/Structure
/*     */     //   54: astore_2
/*     */     //   55: aload_2
/*     */     //   56: invokevirtual 68	com/avrgaming/civcraft/structure/Structure:onDailyEvent	()V
/*     */     //   59: goto +8 -> 67
/*     */     //   62: astore_2
/*     */     //   63: aload_2
/*     */     //   64: invokevirtual 71	java/lang/Exception:printStackTrace	()V
/*     */     //   67: aload_1
/*     */     //   68: invokeinterface 76 1 0
/*     */     //   73: ifne -36 -> 37
/*     */     //   76: invokestatic 79	com/avrgaming/civcraft/main/CivGlobal:getWonders	()Ljava/util/Collection;
/*     */     //   79: invokeinterface 83 1 0
/*     */     //   84: astore_3
/*     */     //   85: goto +27 -> 112
/*     */     //   88: aload_3
/*     */     //   89: invokeinterface 55 1 0
/*     */     //   94: checkcast 88	com/avrgaming/civcraft/structure/wonders/Wonder
/*     */     //   97: astore_2
/*     */     //   98: aload_2
/*     */     //   99: invokevirtual 90	com/avrgaming/civcraft/structure/wonders/Wonder:onDailyEvent	()V
/*     */     //   102: goto +10 -> 112
/*     */     //   105: astore 4
/*     */     //   107: aload 4
/*     */     //   109: invokevirtual 71	java/lang/Exception:printStackTrace	()V
/*     */     //   112: aload_3
/*     */     //   113: invokeinterface 76 1 0
/*     */     //   118: ifne -30 -> 88
/*     */     //   121: new 91	com/avrgaming/civcraft/endgame/EndGameCheckTask
/*     */     //   124: dup
/*     */     //   125: invokespecial 93	com/avrgaming/civcraft/endgame/EndGameCheckTask:<init>	()V
/*     */     //   128: lconst_0
/*     */     //   129: invokestatic 94	com/avrgaming/civcraft/threading/TaskMaster:asyncTask	(Ljava/lang/Runnable;J)V
/*     */     //   132: goto +20 -> 152
/*     */     //   135: astore 5
/*     */     //   137: ldc 100
/*     */     //   139: invokestatic 31	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*     */     //   142: iconst_1
/*     */     //   143: invokestatic 102	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
/*     */     //   146: putstatic 108	com/avrgaming/civcraft/event/DailyEvent:dailyTimerFinished	Ljava/lang/Boolean;
/*     */     //   149: aload 5
/*     */     //   151: athrow
/*     */     //   152: ldc 100
/*     */     //   154: invokestatic 31	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*     */     //   157: iconst_1
/*     */     //   158: invokestatic 102	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
/*     */     //   161: putstatic 108	com/avrgaming/civcraft/event/DailyEvent:dailyTimerFinished	Ljava/lang/Boolean;
/*     */     //   164: goto +14 -> 178
/*     */     //   167: astore 6
/*     */     //   169: getstatic 17	com/avrgaming/civcraft/threading/timers/DailyTimer:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   172: invokevirtual 114	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   175: aload 6
/*     */     //   177: athrow
/*     */     //   178: getstatic 17	com/avrgaming/civcraft/threading/timers/DailyTimer:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   181: invokevirtual 114	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   184: return
/*     */     // Line number table:
/*     */     //   Java source line #53	-> byte code offset #0
/*     */     //   Java source line #56	-> byte code offset #9
/*     */     //   Java source line #57	-> byte code offset #14
/*     */     //   Java source line #58	-> byte code offset #18
/*     */     //   Java source line #59	-> byte code offset #22
/*     */     //   Java source line #60	-> byte code offset #26
/*     */     //   Java source line #62	-> byte code offset #30
/*     */     //   Java source line #63	-> byte code offset #34
/*     */     //   Java source line #65	-> byte code offset #37
/*     */     //   Java source line #66	-> byte code offset #55
/*     */     //   Java source line #67	-> byte code offset #59
/*     */     //   Java source line #68	-> byte code offset #63
/*     */     //   Java source line #63	-> byte code offset #67
/*     */     //   Java source line #72	-> byte code offset #76
/*     */     //   Java source line #74	-> byte code offset #98
/*     */     //   Java source line #75	-> byte code offset #102
/*     */     //   Java source line #76	-> byte code offset #107
/*     */     //   Java source line #72	-> byte code offset #112
/*     */     //   Java source line #81	-> byte code offset #121
/*     */     //   Java source line #83	-> byte code offset #132
/*     */     //   Java source line #84	-> byte code offset #137
/*     */     //   Java source line #85	-> byte code offset #142
/*     */     //   Java source line #86	-> byte code offset #149
/*     */     //   Java source line #84	-> byte code offset #152
/*     */     //   Java source line #85	-> byte code offset #157
/*     */     //   Java source line #87	-> byte code offset #164
/*     */     //   Java source line #88	-> byte code offset #169
/*     */     //   Java source line #89	-> byte code offset #175
/*     */     //   Java source line #88	-> byte code offset #178
/*     */     //   Java source line #92	-> byte code offset #184
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	185	0	this	DailyTimer
/*     */     //   33	35	1	iter	java.util.Iterator<java.util.Map.Entry<com.avrgaming.civcraft.util.BlockCoord, com.avrgaming.civcraft.structure.Structure>>
/*     */     //   54	2	2	struct	com.avrgaming.civcraft.structure.Structure
/*     */     //   62	2	2	e	Exception
/*     */     //   97	2	2	wonder	Wonder
/*     */     //   84	29	3	localIterator	java.util.Iterator
/*     */     //   105	3	4	e	Exception
/*     */     //   135	15	5	localObject1	Object
/*     */     //   167	9	6	localObject2	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   37	59	62	java/lang/Exception
/*     */     //   98	102	105	java/lang/Exception
/*     */     //   9	135	135	finally
/*     */     //   9	167	167	finally
/*     */   }
/*     */   
/*     */   private void payCivUpkeep()
/*     */   {
/*  95 */     Wonder colossus = CivGlobal.getWonderByConfigId("w_colossus");
/*  96 */     if (colossus != null) {
/*     */       try {
/*  98 */         colossus.processCoinsFromCulture();
/*     */       } catch (Exception e) {
/* 100 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/* 104 */     Wonder notredame = CivGlobal.getWonderByConfigId("w_notre_dame");
/* 105 */     if (notredame != null) {
/*     */       try {
/* 107 */         ((NotreDame)notredame).processPeaceTownCoins();
/*     */       } catch (Exception e) {
/* 109 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 114 */     for (Civilization civ : CivGlobal.getCivs()) {
/* 115 */       if (!civ.isAdminCiv())
/*     */       {
/*     */         try
/*     */         {
/*     */ 
/* 120 */           double total = 0.0D;
/*     */           
/* 122 */           total = civ.payUpkeep();
/* 123 */           if (civ.getTreasury().inDebt()) {
/* 124 */             civ.incrementDaysInDebt();
/*     */           }
/* 126 */           CivMessage.sendCiv(civ, "§ePaid " + total + " in civ upkeep costs.");
/* 127 */           civ.save();
/*     */         }
/*     */         catch (Exception e) {
/* 130 */           e.printStackTrace();
/*     */         } }
/*     */     }
/*     */   }
/*     */   
/*     */   private void payTownUpkeep() {
/* 136 */     for (Town t : ) {
/*     */       try {
/* 138 */         double total = 0.0D;
/* 139 */         total = t.payUpkeep();
/* 140 */         if (t.inDebt()) {
/* 141 */           t.incrementDaysInDebt();
/*     */         }
/*     */         
/* 144 */         t.save();
/* 145 */         CivMessage.sendTown(t, "Paid " + total + " coins in upkeep costs.");
/*     */       } catch (Exception e) {
/* 147 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void collectTownTaxes()
/*     */   {
/* 154 */     for (Civilization civ : ) {
/* 155 */       if (!civ.isAdminCiv())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 160 */         double total = 0.0D;
/* 161 */         for (Town t : civ.getTowns()) {
/*     */           try {
/* 163 */             double taxrate = t.getDepositCiv().getIncomeTaxRate();
/* 164 */             double townTotal = 0.0D;
/*     */             
/* 166 */             townTotal += t.collectPlotTax();
/* 167 */             townTotal += t.collectFlatTax();
/*     */             
/* 169 */             double taxesToCiv = total * taxrate;
/* 170 */             townTotal -= taxesToCiv;
/* 171 */             CivMessage.sendTown(t, "Collected " + townTotal + " coins in resident taxes.");
/* 172 */             t.depositTaxed(townTotal);
/*     */             
/* 174 */             if (t.getDepositCiv().getId() == civ.getId()) {
/* 175 */               total += taxesToCiv;
/*     */             }
/*     */           } catch (Exception e) {
/* 178 */             e.printStackTrace();
/*     */           }
/*     */         }
/*     */         
/* 182 */         if (civ.isForSale())
/*     */         {
/*     */ 
/*     */ 
/* 186 */           civ.clearAggressiveWars();
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 191 */         CivMessage.sendCiv(civ, "Collected " + total + " town taxes.");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void decrementResidentGraceCounters()
/*     */   {
/* 199 */     LinkedList<Resident> residentsToGive = new LinkedList();
/* 200 */     for (Resident resident : CivGlobal.getResidents()) {
/* 201 */       if (resident.hasTown())
/*     */       {
/*     */         try
/*     */         {
/*     */ 
/* 206 */           if (resident.getDaysTilEvict() > 0) {
/* 207 */             resident.decrementGraceCounters();
/*     */           }
/*     */           
/*     */ 
/* 211 */           residentsToGive.add(resident);
/*     */         }
/*     */         catch (Exception e) {
/* 214 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/* 218 */     PlatinumManager.giveManyPlatinumDaily(residentsToGive, 
/* 219 */       ((ConfigPlatinumReward)CivSettings.platinumRewards.get("inTownDuringUpkeep")).name, 
/* 220 */       Integer.valueOf(((ConfigPlatinumReward)CivSettings.platinumRewards.get("inTownDuringUpkeep")).amount), 
/* 221 */       "Town taxes were collected, but its not all bad. You've earned %d!");
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\timers\DailyTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.avrgaming.civcraft.threading.timers;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.AttrSource;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.Cottage;
/*     */ import com.avrgaming.civcraft.structure.Mine;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import com.avrgaming.civcraft.structure.TownHall;
/*     */ import com.avrgaming.civcraft.threading.CivAsyncTask;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
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
/*     */ public class EffectEventTimer
/*     */   extends CivAsyncTask
/*     */ {
/*  46 */   public static ReentrantLock runningLock = new ReentrantLock();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void processTick()
/*     */   {
/*  53 */     for (Civilization civ : ) {
/*  54 */       civ.lastTaxesPaidMap.clear();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  60 */     Iterator<Map.Entry<BlockCoord, Structure>> iter = CivGlobal.getStructureIterator();
/*     */     TownHall townhall;
/*  62 */     while (iter.hasNext()) {
/*  63 */       Structure struct = (Structure)((Map.Entry)iter.next()).getValue();
/*  64 */       townhall = struct.getTown().getTownHall();
/*     */       
/*  66 */       if (townhall != null)
/*     */       {
/*     */ 
/*     */ 
/*  70 */         if (struct.isActive())
/*     */         {
/*     */ 
/*  73 */           struct.onEffectEvent();
/*     */           
/*  75 */           if ((struct.getEffectEvent() != null) && (!struct.getEffectEvent().equals("")))
/*     */           {
/*     */ 
/*  78 */             String[] split = struct.getEffectEvent().toLowerCase().split(":");
/*  79 */             String str; switch ((str = split[0]).hashCode()) {case -1782517576:  if (str.equals("generate_coins")) break; break; case 1993174051:  if (!str.equals("process_mine")) {
/*     */                 continue;
/*  81 */                 if ((struct instanceof Cottage)) {
/*  82 */                   Cottage cottage = (Cottage)struct;
/*     */                   
/*  84 */                   cottage.generateCoins(this);
/*     */                 }
/*     */                 
/*     */               }
/*  88 */               else if ((struct instanceof Mine)) {
/*  89 */                 Mine mine = (Mine)struct;
/*     */                 try {
/*  91 */                   mine.process_mine(this);
/*     */                 } catch (InterruptedException e) {
/*  93 */                   e.printStackTrace();
/*     */                 }
/*     */               }
/*     */               
/*     */ 
/*     */               break;
/*     */             }
/*     */             
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 106 */     for (Town town : CivGlobal.getTowns())
/*     */     {
/*     */ 
/*     */ 
/* 110 */       TownHall townhall = town.getTownHall();
/* 111 */       if (townhall == null) {
/* 112 */         CivMessage.sendTown(town, "§eYour town does not have a town hall! Structures have no effect!");
/*     */       }
/*     */       else
/*     */       {
/* 116 */         AttrSource cultureSources = town.getCulture();
/*     */         
/*     */ 
/* 119 */         double cultureGenerated = cultureSources.total;
/* 120 */         cultureGenerated = Math.round(cultureGenerated);
/* 121 */         town.addAccumulatedCulture(cultureGenerated);
/*     */         
/*     */ 
/* 124 */         DecimalFormat df = new DecimalFormat();
/* 125 */         double unusedBeakers = town.getUnusedBeakers();
/*     */         try
/*     */         {
/* 128 */           double cultureToBeakerConversion = CivSettings.getDouble(CivSettings.cultureConfig, "beakers_per_culture");
/* 129 */           if (unusedBeakers > 0.0D) {
/* 130 */             double cultureFromBeakers = unusedBeakers * cultureToBeakerConversion;
/* 131 */             cultureFromBeakers = Math.round(cultureFromBeakers);
/* 132 */             unusedBeakers = Math.round(unusedBeakers);
/*     */             
/* 134 */             if (cultureFromBeakers > 0.0D) {
/* 135 */               CivMessage.sendTown(town, "§aConverted §d" + 
/* 136 */                 df.format(unusedBeakers) + "§a" + " beakers into " + "§d" + 
/* 137 */                 df.format(cultureFromBeakers) + "§a" + " culture since no tech was being researched.");
/* 138 */               cultureGenerated += cultureFromBeakers;
/* 139 */               town.addAccumulatedCulture(unusedBeakers);
/* 140 */               town.setUnusedBeakers(0.0D);
/*     */             }
/*     */           }
/*     */         } catch (InvalidConfiguration e) {
/* 144 */           e.printStackTrace();
/* 145 */           return;
/*     */         }
/*     */         
/* 148 */         cultureGenerated = Math.round(cultureGenerated);
/* 149 */         CivMessage.sendTown(town, "§aGenerated §d" + cultureGenerated + "§a" + " culture.");
/*     */       }
/*     */     }
/*     */     
/* 153 */     CivGlobal.checkForExpiredRelations();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void run()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: getstatic 15	com/avrgaming/civcraft/threading/timers/EffectEventTimer:runningLock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   3: invokevirtual 251	java/util/concurrent/locks/ReentrantLock:tryLock	()Z
/*     */     //   6: ifeq +28 -> 34
/*     */     //   9: aload_0
/*     */     //   10: invokespecial 254	com/avrgaming/civcraft/threading/timers/EffectEventTimer:processTick	()V
/*     */     //   13: goto +12 -> 25
/*     */     //   16: astore_1
/*     */     //   17: getstatic 15	com/avrgaming/civcraft/threading/timers/EffectEventTimer:runningLock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   20: invokevirtual 256	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   23: aload_1
/*     */     //   24: athrow
/*     */     //   25: getstatic 15	com/avrgaming/civcraft/threading/timers/EffectEventTimer:runningLock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   28: invokevirtual 256	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   31: goto +9 -> 40
/*     */     //   34: ldc_w 259
/*     */     //   37: invokestatic 261	com/avrgaming/civcraft/main/CivLog:error	(Ljava/lang/String;)V
/*     */     //   40: return
/*     */     // Line number table:
/*     */     //   Java source line #159	-> byte code offset #0
/*     */     //   Java source line #161	-> byte code offset #9
/*     */     //   Java source line #162	-> byte code offset #13
/*     */     //   Java source line #163	-> byte code offset #17
/*     */     //   Java source line #164	-> byte code offset #23
/*     */     //   Java source line #163	-> byte code offset #25
/*     */     //   Java source line #165	-> byte code offset #31
/*     */     //   Java source line #166	-> byte code offset #34
/*     */     //   Java source line #170	-> byte code offset #40
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	41	0	this	EffectEventTimer
/*     */     //   16	8	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   9	16	16	finally
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\timers\EffectEventTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
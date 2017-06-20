/*     */ package com.avrgaming.civcraft.endgame;
/*     */ 
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.avrgaming.civcraft.structure.wonders.Wonder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class EndConditionConquest
/*     */   extends EndGameCondition
/*     */ {
/*     */   int daysAfterStart;
/*     */   double percentCaptured;
/*     */   double percentCapturedWithWonder;
/*  19 */   Date startDate = null;
/*     */   
/*     */   public void onLoad()
/*     */   {
/*  23 */     this.daysAfterStart = Integer.valueOf(getString("days_after_start")).intValue();
/*  24 */     this.percentCaptured = Double.valueOf(getString("percent_captured")).doubleValue();
/*  25 */     this.percentCapturedWithWonder = Double.valueOf(getString("percent_captured_with_wonder")).doubleValue();
/*  26 */     getStartDate();
/*     */   }
/*     */   
/*     */   private void getStartDate() {
/*  30 */     String key = "endcondition:conquest:startdate";
/*     */     
/*  32 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(key);
/*  33 */     if (entries.size() == 0)
/*     */     {
/*  35 */       this.startDate = new Date();
/*  36 */       CivGlobal.getSessionDB().add(key, this.startDate.getTime(), 0, 0, 0);
/*  37 */       return;
/*     */     }
/*  39 */     long time = Long.valueOf(((SessionEntry)entries.get(0)).value).longValue();
/*  40 */     this.startDate = new Date(time);
/*     */   }
/*     */   
/*     */   private boolean isAfterStartupTime()
/*     */   {
/*  45 */     Calendar startCal = Calendar.getInstance();
/*  46 */     startCal.setTime(this.startDate);
/*     */     
/*  48 */     Calendar now = Calendar.getInstance();
/*     */     
/*  50 */     startCal.add(5, this.daysAfterStart);
/*     */     
/*  52 */     if (now.after(startCal)) {
/*  53 */       return true;
/*     */     }
/*  55 */     return false;
/*     */   }
/*     */   
/*     */   public String getSessionKey()
/*     */   {
/*  60 */     return "endgame:conquer";
/*     */   }
/*     */   
/*     */   public boolean check(Civilization civ)
/*     */   {
/*  65 */     if (!isAfterStartupTime()) {
/*  66 */       return false;
/*     */     }
/*     */     
/*  69 */     boolean hasChichenItza = false;
/*  70 */     for (Town town : civ.getTowns()) {
/*  71 */       if (town.getMotherCiv() == null)
/*     */       {
/*     */ 
/*     */ 
/*  75 */         for (Wonder wonder : town.getWonders()) {
/*  76 */           if ((wonder.isActive()) && 
/*  77 */             (wonder.getConfigId().equals("w_chichen_itza"))) {
/*  78 */             hasChichenItza = true;
/*  79 */             break;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*  84 */         if (hasChichenItza) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*  89 */     if (!hasChichenItza) {
/*  90 */       if (civ.getPercentageConquered() < this.percentCaptured) {
/*  91 */         return false;
/*     */       }
/*     */     }
/*  94 */     else if (civ.getPercentageConquered() < this.percentCapturedWithWonder) {
/*  95 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  99 */     if (civ.isConquered()) {
/* 100 */       return false;
/*     */     }
/*     */     
/* 103 */     return true;
/*     */   }
/*     */   
/*     */   protected void onWarDefeat(Civilization civ)
/*     */   {
/* 108 */     onFailure(civ);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\endgame\EndConditionConquest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
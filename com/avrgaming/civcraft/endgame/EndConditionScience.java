/*     */ package com.avrgaming.civcraft.endgame;
/*     */ 
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.avrgaming.civcraft.structure.wonders.Wonder;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class EndConditionScience extends EndGameCondition
/*     */ {
/*     */   String techname;
/*     */   
/*     */   public void onLoad()
/*     */   {
/*  18 */     this.techname = getString("tech");
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean check(Civilization civ)
/*     */   {
/*  24 */     if (!civ.hasTechnology(this.techname)) {
/*  25 */       return false;
/*     */     }
/*     */     
/*  28 */     if (civ.isAdminCiv()) {
/*  29 */       return false;
/*     */     }
/*     */     
/*  32 */     boolean hasGreatLibrary = false;
/*  33 */     for (Town town : civ.getTowns()) {
/*  34 */       if (town.getMotherCiv() == null)
/*     */       {
/*     */ 
/*     */ 
/*  38 */         for (Wonder wonder : town.getWonders()) {
/*  39 */           if ((wonder.isActive()) && 
/*  40 */             (wonder.getConfigId().equals("w_greatlibrary"))) {
/*  41 */             hasGreatLibrary = true;
/*  42 */             break;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*  47 */         if (hasGreatLibrary) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*  52 */     if (!hasGreatLibrary) {
/*  53 */       return false;
/*     */     }
/*     */     
/*  56 */     return true;
/*     */   }
/*     */   
/*     */   public boolean finalWinCheck(Civilization civ)
/*     */   {
/*  61 */     Civilization rival = getMostAccumulatedBeakers();
/*  62 */     if (rival != civ) {
/*  63 */       CivMessage.global(civ.getName() + " doesn't have enough beakers for a scientific victory. The rival civilization of " + rival.getName() + " has more!");
/*  64 */       return false;
/*     */     }
/*     */     
/*  67 */     return true;
/*     */   }
/*     */   
/*     */   public Civilization getMostAccumulatedBeakers() {
/*  71 */     double most = 0.0D;
/*  72 */     Civilization mostCiv = null;
/*     */     
/*  74 */     for (Civilization civ : CivGlobal.getCivs()) {
/*  75 */       double beakers = getExtraBeakersInCiv(civ);
/*  76 */       if (beakers > most) {
/*  77 */         most = beakers;
/*  78 */         mostCiv = civ;
/*     */       }
/*     */     }
/*     */     
/*  82 */     return mostCiv;
/*     */   }
/*     */   
/*     */   public String getSessionKey()
/*     */   {
/*  87 */     return "endgame:science";
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onWarDefeat(Civilization civ)
/*     */   {
/*  93 */     CivGlobal.getSessionDB().delete_all(getBeakerSessionKey(civ));
/*  94 */     civ.removeTech(this.techname);
/*  95 */     CivMessage.sendCiv(civ, "We were defeated while trying to achieve a science victory! We've lost all of our accumulated beakers and our victory tech!");
/*     */     
/*  97 */     civ.save();
/*  98 */     onFailure(civ);
/*     */   }
/*     */   
/*     */   public static String getBeakerSessionKey(Civilization civ) {
/* 102 */     return "endgame:sciencebeakers:" + civ.getId();
/*     */   }
/*     */   
/*     */   public double getExtraBeakersInCiv(Civilization civ) {
/* 106 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(getBeakerSessionKey(civ));
/* 107 */     if (entries.size() == 0) {
/* 108 */       return 0.0D;
/*     */     }
/* 110 */     return Double.valueOf(((SessionEntry)entries.get(0)).value).doubleValue();
/*     */   }
/*     */   
/*     */   public void addExtraBeakersToCiv(Civilization civ, double beakers) {
/* 114 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(getBeakerSessionKey(civ));
/* 115 */     double current = 0.0D;
/* 116 */     if (entries.size() == 0) {
/* 117 */       CivGlobal.getSessionDB().add(getBeakerSessionKey(civ), beakers, civ.getId(), 0, 0);
/* 118 */       current += beakers;
/*     */     } else {
/* 120 */       current = Double.valueOf(((SessionEntry)entries.get(0)).value).doubleValue();
/* 121 */       current += beakers;
/* 122 */       CivGlobal.getSessionDB().update(((SessionEntry)entries.get(0)).request_id, ((SessionEntry)entries.get(0)).key, current);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static Double getBeakersFor(Civilization civ)
/*     */   {
/* 129 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(getBeakerSessionKey(civ));
/* 130 */     if (entries.size() == 0) {
/* 131 */       return Double.valueOf(0.0D);
/*     */     }
/* 133 */     return Double.valueOf(((SessionEntry)entries.get(0)).value);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\endgame\EndConditionScience.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
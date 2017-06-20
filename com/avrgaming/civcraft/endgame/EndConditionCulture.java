/*    */ package com.avrgaming.civcraft.endgame;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.structure.wonders.Wonder;
/*    */ 
/*    */ public class EndConditionCulture extends EndGameCondition
/*    */ {
/*    */   private int requiredCultureLevel;
/*    */   private int numberOfTownsAtCulture;
/*    */   private int numberOfWonders;
/*    */   
/*    */   public void onLoad()
/*    */   {
/* 16 */     this.requiredCultureLevel = Integer.valueOf(getString("culture_level")).intValue();
/* 17 */     this.numberOfTownsAtCulture = Integer.valueOf(getString("towns")).intValue();
/* 18 */     this.numberOfWonders = Integer.valueOf(getString("wonders")).intValue();
/*    */   }
/*    */   
/*    */   public String getSessionKey()
/*    */   {
/* 23 */     return "endgame:cultural";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean check(Civilization civ)
/*    */   {
/* 30 */     int townCount = 0;
/* 31 */     for (Town town : civ.getTowns()) {
/* 32 */       if (town.getMotherCiv() == null)
/*    */       {
/*    */ 
/*    */ 
/* 36 */         if (town.getCultureLevel() >= this.requiredCultureLevel) {
/* 37 */           townCount++;
/*    */         }
/*    */       }
/*    */     }
/* 41 */     if (townCount < this.numberOfTownsAtCulture)
/*    */     {
/* 43 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 47 */     int wonderCount = 0;
/* 48 */     for (Wonder wonder : CivGlobal.getWonders()) {
/* 49 */       if (wonder.getCiv() == civ) {
/* 50 */         wonderCount++;
/*    */       }
/*    */     }
/*    */     
/* 54 */     if (wonderCount < this.numberOfWonders)
/*    */     {
/* 56 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 60 */     if (civ.isConquered()) {
/* 61 */       return false;
/*    */     }
/*    */     
/* 64 */     return true;
/*    */   }
/*    */   
/*    */   protected void onWarDefeat(Civilization civ)
/*    */   {
/* 69 */     onFailure(civ);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\endgame\EndConditionCulture.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
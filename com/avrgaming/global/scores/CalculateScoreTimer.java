/*    */ package com.avrgaming.global.scores;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*    */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*    */ import com.avrgaming.civcraft.threading.CivAsyncTask;
/*    */ import java.sql.SQLException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.TreeMap;
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
/*    */ public class CalculateScoreTimer
/*    */   extends CivAsyncTask
/*    */ {
/*    */   public void run()
/*    */   {
/* 36 */     if (!CivGlobal.scoringEnabled) {
/* 37 */       return;
/*    */     }
/*    */     
/* 40 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup("endgame:winningCiv");
/* 41 */     if (entries.size() != 0)
/*    */     {
/* 43 */       return;
/*    */     }
/*    */     
/* 46 */     TreeMap<Integer, Civilization> civScores = new TreeMap();
/* 47 */     for (Civilization civ : CivGlobal.getCivs()) {
/* 48 */       if (!civ.isAdminCiv())
/*    */       {
/*    */ 
/* 51 */         civScores.put(Integer.valueOf(civ.getScore()), civ);
/*    */         try
/*    */         {
/* 54 */           ScoreManager.UpdateScore(civ, civ.getScore());
/*    */         } catch (SQLException e) {
/* 56 */           e.printStackTrace();
/*    */         }
/*    */       }
/*    */     }
/* 60 */     TreeMap<Integer, Town> townScores = new TreeMap();
/* 61 */     for (Town town : CivGlobal.getTowns()) {
/* 62 */       if (!town.getCiv().isAdminCiv())
/*    */       {
/*    */         try
/*    */         {
/* 66 */           townScores.put(Integer.valueOf(town.getScore()), town);
/*    */         } catch (Exception e) {
/* 68 */           e.printStackTrace();
/*    */         }
/*    */         try
/*    */         {
/* 72 */           ScoreManager.UpdateScore(town, town.getScore());
/*    */         } catch (SQLException e) {
/* 74 */           e.printStackTrace();
/*    */         }
/*    */       }
/*    */     }
/* 78 */     synchronized (CivGlobal.civilizationScores) {
/* 79 */       CivGlobal.civilizationScores = civScores;
/*    */     }
/*    */     
/* 82 */     synchronized (CivGlobal.townScores) {
/* 83 */       CivGlobal.townScores = townScores;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\global\scores\CalculateScoreTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
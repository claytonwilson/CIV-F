/*     */ package com.avrgaming.civcraft.endgame;
/*     */ 
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.avrgaming.civcraft.structure.wonders.Wonder;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class EndConditionDiplomacy extends EndGameCondition
/*     */ {
/*     */   public static int vote_cooldown_hours;
/*     */   
/*     */   public void onLoad()
/*     */   {
/*  21 */     vote_cooldown_hours = Integer.valueOf(getString("vote_cooldown_hours")).intValue();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean check(Civilization civ)
/*     */   {
/*  27 */     boolean hasCouncil = false;
/*  28 */     for (Town town : civ.getTowns()) {
/*  29 */       if (town.getMotherCiv() == null)
/*     */       {
/*     */ 
/*     */ 
/*  33 */         for (Wonder wonder : town.getWonders()) {
/*  34 */           if ((wonder.isActive()) && 
/*  35 */             (wonder.getConfigId().equals("w_council_of_eight"))) {
/*  36 */             hasCouncil = true;
/*  37 */             break;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*  42 */         if (hasCouncil) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*  47 */     if (!hasCouncil) {
/*  48 */       return false;
/*     */     }
/*     */     
/*  51 */     if (civ.isAdminCiv()) {
/*  52 */       return false;
/*     */     }
/*     */     
/*  55 */     if (civ.isConquered()) {
/*  56 */       return false;
/*     */     }
/*     */     
/*  59 */     return true;
/*     */   }
/*     */   
/*     */   public String getSessionKey()
/*     */   {
/*  64 */     return "endgame:diplomacy";
/*     */   }
/*     */   
/*     */   protected void onWarDefeat(Civilization civ)
/*     */   {
/*  69 */     for (Town town : civ.getTowns()) {
/*  70 */       if (town.getMotherCiv() == null)
/*     */       {
/*     */ 
/*     */ 
/*  74 */         for (Wonder wonder : town.getWonders()) {
/*  75 */           if (wonder.getConfigId().equals("w_council_of_eight")) {
/*  76 */             if (!wonder.isActive()) break;
/*  77 */             wonder.fancyDestroyStructureBlocks();
/*  78 */             wonder.getTown().removeWonder(wonder);
/*     */             try {
/*  80 */               wonder.delete();
/*     */             } catch (SQLException e) {
/*  82 */               e.printStackTrace();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  90 */     deleteAllVotes(civ);
/*  91 */     onFailure(civ);
/*     */   }
/*     */   
/*     */   public void onVictoryReset(Civilization civ)
/*     */   {
/*  96 */     deleteAllVotes(civ);
/*     */   }
/*     */   
/*     */   public static boolean canPeopleVote() {
/* 100 */     for (Wonder wonder : ) {
/* 101 */       if ((wonder.isActive()) && (wonder.getConfigId().equals("w_council_of_eight"))) {
/* 102 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 106 */     return false;
/*     */   }
/*     */   
/*     */   public boolean finalWinCheck(Civilization civ)
/*     */   {
/* 111 */     Integer votes = getVotesFor(civ);
/*     */     
/* 113 */     for (Civilization otherCiv : CivGlobal.getCivs()) {
/* 114 */       if (otherCiv != civ)
/*     */       {
/*     */ 
/*     */ 
/* 118 */         Integer otherVotes = getVotesFor(otherCiv);
/* 119 */         if (otherVotes.intValue() > votes.intValue()) {
/* 120 */           CivMessage.global(civ.getName() + " doesn't have enough votes for a diplomatic victory! The rival civilization of " + otherCiv.getName() + " has more!");
/* 121 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 125 */     return true;
/*     */   }
/*     */   
/*     */   public static String getVoteSessionKey(Civilization civ) {
/* 129 */     return "endgame:diplomacyvote:" + civ.getId();
/*     */   }
/*     */   
/*     */   public static void deleteAllVotes(Civilization civ) {
/* 133 */     CivGlobal.getSessionDB().delete_all(getVoteSessionKey(civ));
/*     */   }
/*     */   
/*     */   public static void addVote(Civilization civ, Resident resident)
/*     */   {
/* 138 */     if (!canVoteNow(resident)) {
/* 139 */       return;
/*     */     }
/*     */     
/* 142 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(getVoteSessionKey(civ));
/* 143 */     if (entries.size() == 0) {
/* 144 */       CivGlobal.getSessionDB().add(getVoteSessionKey(civ), "1", civ.getId(), 0, 0);
/*     */     } else {
/* 146 */       Integer votes = Integer.valueOf(((SessionEntry)entries.get(0)).value);
/* 147 */       votes = Integer.valueOf(votes.intValue() + 1);
/* 148 */       CivGlobal.getSessionDB().update(((SessionEntry)entries.get(0)).request_id, ((SessionEntry)entries.get(0)).key, votes);
/*     */     }
/*     */     
/* 151 */     CivMessage.sendSuccess(resident, "Added a vote for " + civ.getName());
/*     */   }
/*     */   
/*     */   public static void setVotes(Civilization civ, Integer votes) {
/* 155 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(getVoteSessionKey(civ));
/* 156 */     if (entries.size() == 0) {
/* 157 */       CivGlobal.getSessionDB().add(getVoteSessionKey(civ), votes, civ.getId(), 0, 0);
/*     */     } else {
/* 159 */       CivGlobal.getSessionDB().update(((SessionEntry)entries.get(0)).request_id, ((SessionEntry)entries.get(0)).key, votes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Integer getVotesFor(Civilization civ) {
/* 164 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(getVoteSessionKey(civ));
/* 165 */     if (entries.size() == 0) {
/* 166 */       return Integer.valueOf(0);
/*     */     }
/*     */     
/* 169 */     return Integer.valueOf(((SessionEntry)entries.get(0)).value);
/*     */   }
/*     */   
/*     */   private static boolean canVoteNow(Resident resident) {
/* 173 */     String key = "endgame:residentvote:" + resident.getName();
/* 174 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(key);
/* 175 */     if (entries.size() == 0) {
/* 176 */       CivGlobal.getSessionDB().add(key, new Date().getTime(), 0, 0, 0);
/* 177 */       return true;
/*     */     }
/* 179 */     Date then = new Date(Long.valueOf(((SessionEntry)entries.get(0)).value).longValue());
/* 180 */     Date now = new Date();
/* 181 */     if (now.getTime() > then.getTime() + vote_cooldown_hours * 60 * 60 * 1000) {
/* 182 */       CivGlobal.getSessionDB().update(((SessionEntry)entries.get(0)).request_id, ((SessionEntry)entries.get(0)).key, now.getTime());
/* 183 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 187 */     CivMessage.sendError(resident, "You must wait 24 hours before casting another vote.");
/* 188 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\endgame\EndConditionDiplomacy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
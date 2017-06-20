/*     */ package com.avrgaming.civcraft.command.civ;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.DiplomacyManager;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.questions.DiplomacyGiftResponse;
/*     */ import com.avrgaming.civcraft.questions.QuestionResponseInterface;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.threading.tasks.CivQuestionTask;
/*     */ import com.avrgaming.civcraft.war.War;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.bukkit.ChatColor;
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
/*     */ public class CivDiplomacyGiftCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  40 */     this.command = "/civ dip gift";
/*  41 */     this.displayName = "Civ Diplomacy Gift";
/*     */     
/*  43 */     this.commands.put("entireciv", "[civ] - Sends our entire civilization as a gift to [civ]. Only usable by civ leaders.");
/*  44 */     this.commands.put("town", "[town] [civ] - Sends this town as a gift to [civ]. Only useable by civ leaders.");
/*     */   }
/*     */   
/*     */   private void sendGiftRequest(Civilization toCiv, Civilization fromCiv, String message, QuestionResponseInterface finishedFunction)
/*     */     throws CivException
/*     */   {
/*  50 */     CivQuestionTask task = (CivQuestionTask)CivGlobal.civQuestions.get(toCiv.getName());
/*  51 */     if (task != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*  56 */       throw new CivException("Civilization already has an offer pending, wait 30 seconds and try again.");
/*     */     }
/*     */     
/*  59 */     task = new CivQuestionTask(toCiv, fromCiv, message, 30000L, finishedFunction);
/*  60 */     CivGlobal.civQuestions.put(toCiv.getName(), task);
/*  61 */     TaskMaster.asyncTask("", task, 0L);
/*     */   }
/*     */   
/*     */   public void entireciv_cmd() throws CivException {
/*  65 */     validLeader();
/*  66 */     Civilization fromCiv = getSenderCiv();
/*  67 */     Civilization toCiv = getNamedCiv(1);
/*     */     
/*  69 */     if (fromCiv == toCiv) {
/*  70 */       throw new CivException("Cannot gift your civiliation to itself.");
/*     */     }
/*     */     
/*  73 */     if ((fromCiv.getDiplomacyManager().isAtWar()) || (toCiv.getDiplomacyManager().isAtWar())) {
/*  74 */       throw new CivException("Cannot gift your civilization if either civ is at war.");
/*     */     }
/*     */     
/*  77 */     fromCiv.validateGift();
/*  78 */     toCiv.validateGift();
/*     */     
/*  80 */     if (War.isWarTime()) {
/*  81 */       throw new CivException("Cannot gift civilizations during WarTime.");
/*     */     }
/*     */     
/*  84 */     if (War.isWithinWarDeclareDays()) {
/*  85 */       throw new CivException("Cannot gift civilizations within " + War.getTimeDeclareDays() + " days before WarTime.");
/*     */     }
/*     */     
/*     */ 
/*  89 */     DiplomacyGiftResponse dipResponse = new DiplomacyGiftResponse();
/*  90 */     dipResponse.giftedObject = fromCiv;
/*  91 */     dipResponse.fromCiv = fromCiv;
/*  92 */     dipResponse.toCiv = toCiv;
/*     */     
/*  94 */     sendGiftRequest(toCiv, fromCiv, 
/*  95 */       "§e" + ChatColor.BOLD + "The Civilization of " + fromCiv.getName() + " wishes to give itself to you. All of their towns will be yours." + 
/*  96 */       " It will cost us " + fromCiv.getMergeCost() + " coins. Do you accept?", dipResponse);
/*  97 */     CivMessage.sendSuccess(this.sender, "Gift request sent, waiting for them to accept the gift.");
/*     */   }
/*     */   
/*     */   public void town_cmd() throws CivException {
/* 101 */     validLeader();
/* 102 */     Civilization fromCiv = getSenderCiv();
/* 103 */     Town giftedTown = getNamedTown(1);
/* 104 */     Civilization toCiv = getNamedCiv(2);
/*     */     
/* 106 */     if (giftedTown.getCiv() != fromCiv) {
/* 107 */       throw new CivException("You cannot gift a town that is not yours.");
/*     */     }
/*     */     
/* 110 */     if (giftedTown.getCiv() == toCiv) {
/* 111 */       throw new CivException("You cannot gift a town to your own civ.");
/*     */     }
/*     */     
/* 114 */     if ((giftedTown.getMotherCiv() != null) && (toCiv != giftedTown.getMotherCiv())) {
/* 115 */       throw new CivException("You cannot gift captured towns to another civ unless it is the mother civ.");
/*     */     }
/*     */     
/* 118 */     if (giftedTown.isCapitol()) {
/* 119 */       throw new CivException("You cannot give away your capitol town. Try gifting your entire civilization instead.");
/*     */     }
/*     */     
/* 122 */     if (War.isWarTime()) {
/* 123 */       throw new CivException("Cannot gift towns during WarTime.");
/*     */     }
/*     */     
/* 126 */     if ((fromCiv.getDiplomacyManager().isAtWar()) || (toCiv.getDiplomacyManager().isAtWar())) {
/* 127 */       throw new CivException("Cannot gift your town if either civ is at war.");
/*     */     }
/*     */     
/* 130 */     fromCiv.validateGift();
/* 131 */     toCiv.validateGift();
/* 132 */     giftedTown.validateGift();
/*     */     
/* 134 */     DiplomacyGiftResponse dipResponse = new DiplomacyGiftResponse();
/* 135 */     dipResponse.giftedObject = giftedTown;
/* 136 */     dipResponse.fromCiv = fromCiv;
/* 137 */     dipResponse.toCiv = toCiv;
/*     */     
/* 139 */     sendGiftRequest(toCiv, fromCiv, 
/* 140 */       "Our Civilization of " + fromCiv.getName() + " wishes to give the town of " + giftedTown.getName() + " to you. It will cost us " + giftedTown.getGiftCost() + " coins. Do you accept?", dipResponse);
/* 141 */     CivMessage.sendSuccess(this.sender, "Gift request sent, waiting for them to accept the gift.");
/*     */   }
/*     */   
/*     */   public void doDefaultAction()
/*     */     throws CivException
/*     */   {
/* 147 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 152 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck()
/*     */     throws CivException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\civ\CivDiplomacyGiftCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
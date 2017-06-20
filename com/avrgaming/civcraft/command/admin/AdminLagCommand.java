/*     */ package com.avrgaming.civcraft.command.admin;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import java.util.HashMap;
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
/*     */ public class AdminLagCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  31 */     this.command = "/ad lag";
/*  32 */     this.displayName = "Admin Lag";
/*     */     
/*  34 */     this.commands.put("trommels", "Toggles trommels globally.");
/*  35 */     this.commands.put("towers", "Toggles towers globally.");
/*  36 */     this.commands.put("growth", "Toggles farm growth.");
/*  37 */     this.commands.put("trade", "Toggles farm growth.");
/*  38 */     this.commands.put("score", "Toggles score calculations");
/*  39 */     this.commands.put("warning", "Toggles warnings in the logs.");
/*  40 */     this.commands.put("blockupdate", "[#] - sets the block update limit to this amount.");
/*     */   }
/*     */   
/*     */   public void blockupdate_cmd() throws CivException
/*     */   {
/*  45 */     Integer blocks = getNamedInteger(1);
/*     */     
/*  47 */     com.avrgaming.civcraft.threading.sync.SyncBuildUpdateTask.UPDATE_LIMIT = blocks.intValue();
/*  48 */     CivMessage.sendSuccess(this.sender, "Set block update limit to " + blocks);
/*     */   }
/*     */   
/*     */   public void score_cmd() {
/*  52 */     CivGlobal.scoringEnabled = !CivGlobal.scoringEnabled;
/*     */     
/*  54 */     if (CivGlobal.scoringEnabled) {
/*  55 */       CivMessage.sendSuccess(this.sender, "Scoring enabled.");
/*     */     } else {
/*  57 */       CivMessage.sendError(this.sender, "Scoring disabled");
/*     */     }
/*     */   }
/*     */   
/*     */   public void trommels_cmd() {
/*  62 */     CivGlobal.trommelsEnabled = !CivGlobal.trommelsEnabled;
/*     */     
/*  64 */     if (CivGlobal.trommelsEnabled) {
/*  65 */       CivMessage.sendSuccess(this.sender, "Trommels enabled.");
/*     */     } else {
/*  67 */       CivMessage.sendError(this.sender, "Trommels disabled");
/*     */     }
/*     */   }
/*     */   
/*     */   public void towers_cmd() {
/*  72 */     CivGlobal.towersEnabled = !CivGlobal.towersEnabled;
/*     */     
/*  74 */     if (CivGlobal.towersEnabled) {
/*  75 */       CivMessage.sendSuccess(this.sender, "Towers enabled.");
/*     */     } else {
/*  77 */       CivMessage.sendError(this.sender, "Towers disabled");
/*     */     }
/*     */   }
/*     */   
/*     */   public void growth_cmd() {
/*  82 */     CivGlobal.growthEnabled = !CivGlobal.growthEnabled;
/*     */     
/*  84 */     if (CivGlobal.growthEnabled) {
/*  85 */       CivMessage.sendSuccess(this.sender, "Growth enabled.");
/*     */     } else {
/*  87 */       CivMessage.sendError(this.sender, "Growth disabled");
/*     */     }
/*     */   }
/*     */   
/*     */   public void trade_cmd() {
/*  92 */     CivGlobal.tradeEnabled = !CivGlobal.tradeEnabled;
/*     */     
/*  94 */     if (CivGlobal.tradeEnabled) {
/*  95 */       CivMessage.sendSuccess(this.sender, "Trade enabled.");
/*     */     } else {
/*  97 */       CivMessage.sendError(this.sender, "Trade disabled");
/*     */     }
/*     */   }
/*     */   
/*     */   public void warning_cmd() {
/* 102 */     CivGlobal.growthEnabled = !CivGlobal.growthEnabled;
/*     */     
/* 104 */     if (CivGlobal.warningsEnabled) {
/* 105 */       CivMessage.sendSuccess(this.sender, "Warnings enabled.");
/*     */     } else {
/* 107 */       CivMessage.sendError(this.sender, "Warnings disabled");
/*     */     }
/*     */   }
/*     */   
/*     */   public void doDefaultAction() throws CivException
/*     */   {
/* 113 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 118 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck()
/*     */     throws CivException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\admin\AdminLagCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
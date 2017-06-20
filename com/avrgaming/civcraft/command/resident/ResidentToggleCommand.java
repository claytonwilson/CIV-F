/*     */ package com.avrgaming.civcraft.command.resident;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
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
/*     */ public class ResidentToggleCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  30 */     this.command = "/resident toggle";
/*  31 */     this.displayName = "Resident Toggle";
/*     */     
/*  33 */     this.commands.put("map", "Toggles a ASCII map which shows town locations of claimed town chunks.");
/*  34 */     this.commands.put("info", "Toggles a message displayed as you enter each culture chunk. Tells you what it would generate the town.");
/*  35 */     this.commands.put("showtown", "Toggles displaying of [Town] messages.");
/*  36 */     this.commands.put("showciv", "Toggles displaying of [Civ] messages.");
/*  37 */     this.commands.put("showscout", "Toggles displaying of scout tower messages.");
/*  38 */     this.commands.put("combatinfo", "Toggles displaying of combat information.");
/*  39 */     this.commands.put("itemdrops", "Toggles displaying of item drops.");
/*     */   }
/*     */   
/*     */   public void itemdrops_cmd() throws CivException {
/*  43 */     toggle();
/*     */   }
/*     */   
/*     */   public void map_cmd() throws CivException {
/*  47 */     toggle();
/*     */   }
/*     */   
/*  50 */   public void showtown_cmd() throws CivException { toggle(); }
/*     */   
/*     */   public void showciv_cmd() throws CivException
/*     */   {
/*  54 */     toggle();
/*     */   }
/*     */   
/*     */   public void showscout_cmd() throws CivException {
/*  58 */     toggle();
/*     */   }
/*     */   
/*     */   public void info_cmd() throws CivException {
/*  62 */     toggle();
/*     */   }
/*     */   
/*     */   public void combatinfo_cmd() throws CivException {
/*  66 */     toggle();
/*     */   }
/*     */   
/*     */   private void toggle() throws CivException {
/*  70 */     Resident resident = getResident();
/*     */     
/*     */     String str;
/*  73 */     switch ((str = this.args[0].toLowerCase()).hashCode()) {case -2136594383:  if (str.equals("itemdrops")) {} break; case -1885357535:  if (str.equals("showscout")) {} break; case -382744158:  if (str.equals("combatinfo")) {} break; case -337871089:  if (str.equals("showtown")) {} break; case 107868:  if (str.equals("map")) break; break; case 3237038:  if (str.equals("info")) {} break; case 2067294387:  if (!str.equals("showciv")) {
/*     */         break label317;
/*  75 */         resident.setShowMap(!resident.isShowMap());
/*  76 */         boolean result = resident.isShowMap();
/*     */         
/*     */         break label346;
/*  79 */         resident.setShowTown(!resident.isShowTown());
/*  80 */         boolean result = resident.isShowTown();
/*     */         break label346;
/*     */       } else {
/*  83 */         resident.setShowCiv(!resident.isShowCiv());
/*  84 */         boolean result = resident.isShowCiv();
/*     */         
/*     */         break label346;
/*  87 */         resident.setShowScout(!resident.isShowScout());
/*  88 */         boolean result = resident.isShowScout();
/*     */         
/*     */         break label346;
/*  91 */         resident.setShowInfo(!resident.isShowInfo());
/*  92 */         boolean result = resident.isShowInfo();
/*     */         
/*     */         break label346;
/*  95 */         resident.setCombatInfo(!resident.isCombatInfo());
/*  96 */         boolean result = resident.isCombatInfo();
/*     */         
/*     */         break label346;
/*  99 */         resident.toggleItemMode(); return; }
/*     */       break; }
/*     */     label317:
/* 102 */     throw new CivException("Unknown flag " + this.args[0]);
/*     */     label346:
/*     */     boolean result;
/* 105 */     resident.save();
/* 106 */     CivMessage.sendSuccess(this.sender, "Toggled " + this.args[0] + " to " + result);
/*     */   }
/*     */   
/*     */   public void doDefaultAction() throws CivException
/*     */   {
/* 111 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 116 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck()
/*     */     throws CivException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\resident\ResidentToggleCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
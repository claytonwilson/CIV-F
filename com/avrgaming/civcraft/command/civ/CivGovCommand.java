/*     */ package com.avrgaming.civcraft.command.civ;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.config.ConfigGovernment;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ public class CivGovCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  34 */     this.command = "/civ gov";
/*  35 */     this.displayName = "Civ Gov";
/*     */     
/*  37 */     this.commands.put("info", "Information about your current government.");
/*  38 */     this.commands.put("change", "[name] - change your government to the named government.");
/*  39 */     this.commands.put("list", "lists available governments to change to.");
/*     */   }
/*     */   
/*     */   public void change_cmd() throws CivException {
/*  43 */     Civilization civ = getSenderCiv();
/*     */     
/*  45 */     if (this.args.length < 2) {
/*  46 */       throw new CivException("You must enter the name of a government you want to change to.");
/*     */     }
/*     */     
/*  49 */     ConfigGovernment gov = ConfigGovernment.getGovernmentFromName(this.args[1]);
/*  50 */     if (gov == null) {
/*  51 */       throw new CivException("Could not find government named " + this.args[1]);
/*     */     }
/*     */     
/*  54 */     if (!gov.isAvailable(civ)) {
/*  55 */       throw new CivException(gov.displayName + " is not yet available.");
/*     */     }
/*     */     
/*  58 */     civ.changeGovernment(civ, gov, false);
/*  59 */     CivMessage.sendSuccess(this.sender, "Revolution Successful.");
/*     */   }
/*     */   
/*     */   public void list_cmd() throws CivException {
/*  63 */     Civilization civ = getSenderCiv();
/*     */     
/*  65 */     CivMessage.sendHeading(this.sender, "Available Governments");
/*  66 */     ArrayList<ConfigGovernment> govs = ConfigGovernment.getAvailableGovernments(civ);
/*     */     
/*  68 */     for (ConfigGovernment gov : govs) {
/*  69 */       if (gov == civ.getGovernment()) {
/*  70 */         CivMessage.send(this.sender, "§6" + gov.displayName + " (current)");
/*     */       } else {
/*  72 */         CivMessage.send(this.sender, "§2" + gov.displayName);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void info_cmd() throws CivException
/*     */   {
/*  79 */     Civilization civ = getSenderCiv();
/*     */     
/*  81 */     CivMessage.sendHeading(this.sender, "Government " + civ.getGovernment().displayName);
/*  82 */     CivMessage.send(this.sender, "§2Trade Rate: §a" + civ.getGovernment().trade_rate + 
/*  83 */       "§2" + " Cottage Rate: " + "§a" + civ.getGovernment().cottage_rate);
/*  84 */     CivMessage.send(this.sender, "§2Upkeep Rate: §a" + civ.getGovernment().upkeep_rate + 
/*  85 */       "§2" + " Growth Rate: " + "§a" + civ.getGovernment().growth_rate);
/*  86 */     CivMessage.send(this.sender, "§2Hammer Rate: §a" + civ.getGovernment().hammer_rate + 
/*  87 */       "§2" + " Beaker Rate: " + "§a" + civ.getGovernment().beaker_rate);
/*  88 */     CivMessage.send(this.sender, "§2Culture Rate: §a" + civ.getGovernment().culture_rate + 
/*  89 */       "§2" + " Max Tax Rate: " + "§a" + civ.getGovernment().maximum_tax_rate);
/*     */   }
/*     */   
/*     */   public void doDefaultAction()
/*     */     throws CivException
/*     */   {
/*  95 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 100 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck() throws CivException
/*     */   {
/* 105 */     validLeaderAdvisor();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\civ\CivGovCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
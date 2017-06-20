/*     */ package com.avrgaming.civcraft.command.camp;
/*     */ 
/*     */ import com.avrgaming.civcraft.camp.Camp;
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigCampUpgrade;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class CampUpgradeCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  32 */     this.command = "/camp upgrade";
/*  33 */     this.displayName = "Camp Upgrade";
/*     */     
/*     */ 
/*  36 */     this.commands.put("list", "shows available upgrades to purchase.");
/*  37 */     this.commands.put("purchased", "shows a list of purchased upgrades.");
/*  38 */     this.commands.put("buy", "[name] - buys upgrade for this camp.");
/*     */   }
/*     */   
/*     */   public void purchased_cmd() throws CivException
/*     */   {
/*  43 */     Camp camp = getCurrentCamp();
/*  44 */     CivMessage.sendHeading(this.sender, "Upgrades Purchased");
/*     */     
/*  46 */     String out = "";
/*  47 */     for (ConfigCampUpgrade upgrade : camp.getUpgrades()) {
/*  48 */       out = out + upgrade.name + ", ";
/*     */     }
/*     */     
/*  51 */     CivMessage.send(this.sender, out);
/*     */   }
/*     */   
/*     */   private void list_upgrades(Camp camp) throws CivException {
/*  55 */     for (ConfigCampUpgrade upgrade : CivSettings.campUpgrades.values()) {
/*  56 */       if (upgrade.isAvailable(camp)) {
/*  57 */         CivMessage.send(this.sender, upgrade.name + "§7" + " Cost: " + "§e" + upgrade.cost);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void list_cmd() throws CivException {
/*  63 */     Camp camp = getCurrentCamp();
/*     */     
/*  65 */     CivMessage.sendHeading(this.sender, "Available Upgrades");
/*  66 */     list_upgrades(camp);
/*     */   }
/*     */   
/*     */   public void buy_cmd() throws CivException {
/*  70 */     Camp camp = getCurrentCamp();
/*     */     
/*  72 */     if (this.args.length < 2) {
/*  73 */       CivMessage.sendHeading(this.sender, "Available Upgrades");
/*  74 */       list_upgrades(camp);
/*  75 */       CivMessage.send(this.sender, "Enter the name of the upgrade you wish to purchase.");
/*  76 */       return;
/*     */     }
/*     */     
/*  79 */     String combinedArgs = "";
/*  80 */     this.args = stripArgs(this.args, 1);
/*  81 */     String[] arrayOfString; int j = (arrayOfString = this.args).length; for (int i = 0; i < j; i++) { String arg = arrayOfString[i];
/*  82 */       combinedArgs = combinedArgs + arg + " ";
/*     */     }
/*  84 */     combinedArgs = combinedArgs.trim();
/*     */     
/*  86 */     ConfigCampUpgrade upgrade = CivSettings.getCampUpgradeByNameRegex(camp, combinedArgs);
/*  87 */     if (upgrade == null) {
/*  88 */       throw new CivException("No upgrade by the name of " + combinedArgs + " could be found.");
/*     */     }
/*     */     
/*  91 */     if (camp.hasUpgrade(upgrade.id)) {
/*  92 */       throw new CivException("You already have that upgrade.");
/*     */     }
/*     */     
/*  95 */     camp.purchaseUpgrade(upgrade);
/*  96 */     CivMessage.sendSuccess(this.sender, "Upgrade \"" + upgrade.name + "\" purchased.");
/*     */   }
/*     */   
/*     */   public void doDefaultAction() throws CivException
/*     */   {
/* 101 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 106 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck() throws CivException
/*     */   {
/* 111 */     validCampOwner();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\camp\CampUpgradeCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
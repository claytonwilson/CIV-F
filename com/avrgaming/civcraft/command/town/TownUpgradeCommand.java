/*     */ package com.avrgaming.civcraft.command.town;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigTownUpgrade;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.lang.WordUtils;
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
/*     */ public class TownUpgradeCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  35 */     this.command = "/town upgrade";
/*  36 */     this.displayName = "Town Upgrade";
/*     */     
/*     */ 
/*  39 */     this.commands.put("list", "shows available upgrades to purchase.");
/*  40 */     this.commands.put("purchased", "shows a list of purchased upgrades.");
/*  41 */     this.commands.put("buy", "[name] - buys upgrade for town.");
/*     */   }
/*     */   
/*     */   public void purchased_cmd() throws CivException
/*     */   {
/*  46 */     Town town = getSelectedTown();
/*  47 */     CivMessage.sendHeading(this.sender, "Upgrades Purchased");
/*     */     
/*  49 */     String out = "";
/*  50 */     for (ConfigTownUpgrade upgrade : town.getUpgrades().values()) {
/*  51 */       out = out + upgrade.name + ", ";
/*     */     }
/*     */     
/*  54 */     CivMessage.send(this.sender, out);
/*     */   }
/*     */   
/*     */   private void list_upgrades(String category, Town town) throws CivException {
/*  58 */     if ((!ConfigTownUpgrade.categories.containsKey(category.toLowerCase())) && (!category.equalsIgnoreCase("all"))) {
/*  59 */       throw new CivException("No category called " + category);
/*     */     }
/*     */     
/*  62 */     for (ConfigTownUpgrade upgrade : CivSettings.townUpgrades.values()) {
/*  63 */       if (((category.equalsIgnoreCase("all")) || (upgrade.category.equalsIgnoreCase(category))) && 
/*  64 */         (upgrade.isAvailable(town))) {
/*  65 */         CivMessage.send(this.sender, upgrade.name + "§7" + " Cost: " + "§e" + upgrade.cost);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void list_cmd() throws CivException
/*     */   {
/*  72 */     Town town = getSelectedTown();
/*     */     
/*  74 */     CivMessage.sendHeading(this.sender, "Available Upgrades");
/*     */     
/*  76 */     if (this.args.length < 2) {
/*  77 */       CivMessage.send(this.sender, "- §6All §b(" + 
/*  78 */         ConfigTownUpgrade.getAvailableCategoryCount("all", town) + ")");
/*  79 */       for (String category : ConfigTownUpgrade.categories.keySet()) {
/*  80 */         CivMessage.send(this.sender, "- §6" + WordUtils.capitalize(category) + 
/*  81 */           "§b" + " (" + ConfigTownUpgrade.getAvailableCategoryCount(category, town) + ")");
/*     */       }
/*  83 */       return;
/*     */     }
/*     */     
/*  86 */     list_upgrades(this.args[1], town);
/*     */   }
/*     */   
/*     */   public void buy_cmd() throws CivException
/*     */   {
/*  91 */     if (this.args.length < 2) {
/*  92 */       list_upgrades("all", getSelectedTown());
/*  93 */       CivMessage.send(this.sender, "Enter the name of the upgrade you wish to purchase.");
/*  94 */       return;
/*     */     }
/*     */     
/*  97 */     Town town = getSelectedTown();
/*     */     
/*  99 */     String combinedArgs = "";
/* 100 */     this.args = stripArgs(this.args, 1);
/* 101 */     String[] arrayOfString; int j = (arrayOfString = this.args).length; for (int i = 0; i < j; i++) { String arg = arrayOfString[i];
/* 102 */       combinedArgs = combinedArgs + arg + " ";
/*     */     }
/* 104 */     combinedArgs = combinedArgs.trim();
/*     */     
/* 106 */     ConfigTownUpgrade upgrade = CivSettings.getUpgradeByNameRegex(town, combinedArgs);
/* 107 */     if (upgrade == null) {
/* 108 */       throw new CivException("No upgrade by the name of " + combinedArgs + " could be found.");
/*     */     }
/*     */     
/* 111 */     if (town.hasUpgrade(upgrade.id)) {
/* 112 */       throw new CivException("You already have that upgrade.");
/*     */     }
/*     */     
/*     */ 
/* 116 */     town.purchaseUpgrade(upgrade);
/* 117 */     CivMessage.sendSuccess(this.sender, "Upgrade \"" + upgrade.name + "\" purchased.");
/*     */   }
/*     */   
/*     */   public void doDefaultAction() throws CivException
/*     */   {
/* 122 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 127 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck() throws CivException
/*     */   {
/* 132 */     validMayorAssistantLeader();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\town\TownUpgradeCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
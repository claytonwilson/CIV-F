/*     */ package com.avrgaming.civcraft.command.market;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.war.War;
/*     */ import java.text.DecimalFormat;
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
/*     */ public class MarketBuyCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  34 */     this.command = "/market buy";
/*  35 */     this.displayName = "Market Buy";
/*     */     
/*  37 */     this.commands.put("towns", "See what towns are for sale and buy them.");
/*  38 */     this.commands.put("civs", "See what civs are for sale and buy them.");
/*     */   }
/*     */   
/*     */ 
/*     */   private void list_towns_for_sale(Civilization ourCiv)
/*     */   {
/*  44 */     CivMessage.sendHeading(this.sender, "Towns For Sale");
/*  45 */     for (Town town : CivGlobal.getTowns()) {
/*  46 */       if ((!town.isCapitol()) && 
/*  47 */         (town.isForSale())) {
/*  48 */         CivMessage.send(this.sender, town.getName() + " - " + "§e" + 
/*  49 */           this.df.format(town.getForSalePrice()) + " coins.");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void list_civs_for_sale(Civilization ourCiv)
/*     */   {
/*  58 */     CivMessage.sendHeading(this.sender, "Civs For Sale");
/*  59 */     for (Civilization civ : CivGlobal.getCivs()) {
/*  60 */       if (civ.isForSale()) {
/*  61 */         CivMessage.send(this.sender, civ.getName() + " - " + "§e" + 
/*  62 */           this.df.format(civ.getTotalSalePrice()) + " coins.");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void towns_cmd() throws CivException {
/*  68 */     validLeaderAdvisor();
/*  69 */     Civilization senderCiv = getSenderCiv();
/*     */     
/*  71 */     if (this.args.length < 2) {
/*  72 */       list_towns_for_sale(senderCiv);
/*  73 */       CivMessage.send(this.sender, "Use /market buy towns [town-name] to buy this town.");
/*  74 */       return;
/*     */     }
/*     */     
/*  77 */     Town town = getNamedTown(1);
/*     */     
/*  79 */     if (senderCiv.isForSale()) {
/*  80 */       throw new CivException("Cannot buy a town when your civ is up for sale.");
/*     */     }
/*     */     
/*  83 */     if (town.getCiv() == senderCiv) {
/*  84 */       throw new CivException("Cannot buy a town already in your civ.");
/*     */     }
/*     */     
/*  87 */     if (town.isCapitol()) {
/*  88 */       throw new CivException("Cannot buy the capitol, you must buy the civilization instead.");
/*     */     }
/*     */     
/*  91 */     if (!town.isForSale()) {
/*  92 */       throw new CivException("Can only buy towns that are up for sale.");
/*     */     }
/*     */     
/*  95 */     if ((War.isWarTime()) || (War.isWithinWarDeclareDays())) {
/*  96 */       throw new CivException("Can not buy towns during WarTime or within 3 days of WarTime.");
/*     */     }
/*     */     
/*  99 */     senderCiv.buyTown(town);
/* 100 */     CivMessage.global("Town of " + town.getName() + " has been bought and is now part of " + senderCiv.getName());
/* 101 */     CivMessage.sendSuccess(this.sender, "Bought town " + this.args[1]);
/*     */   }
/*     */   
/*     */   public void civs_cmd() throws CivException
/*     */   {
/* 106 */     validLeaderAdvisor();
/* 107 */     Civilization senderCiv = getSenderCiv();
/*     */     
/* 109 */     if (this.args.length < 2) {
/* 110 */       list_civs_for_sale(senderCiv);
/* 111 */       CivMessage.send(this.sender, "Use /market buy civs [civ-name] to buy this civ.");
/* 112 */       return;
/*     */     }
/*     */     
/* 115 */     Civilization civBought = getNamedCiv(1);
/*     */     
/* 117 */     if (senderCiv.isForSale()) {
/* 118 */       throw new CivException("Cannot buy a civ when your civ is up for sale.");
/*     */     }
/*     */     
/* 121 */     if (civBought == senderCiv) {
/* 122 */       throw new CivException("Cannot buy your own civ.");
/*     */     }
/*     */     
/* 125 */     if (!civBought.isForSale()) {
/* 126 */       throw new CivException("Can only buy civilizations that are up for sale.");
/*     */     }
/*     */     
/* 129 */     if ((War.isWarTime()) || (War.isWithinWarDeclareDays())) {
/* 130 */       throw new CivException("Can not buy civs during WarTime or within 3 days of WarTime.");
/*     */     }
/*     */     
/* 133 */     senderCiv.buyCiv(civBought);
/* 134 */     CivMessage.global(civBought.getName() + " has been bought by " + senderCiv.getName());
/* 135 */     CivMessage.sendSuccess(this.sender, "Bought civ " + this.args[1]);
/*     */   }
/*     */   
/*     */   public void doDefaultAction()
/*     */     throws CivException
/*     */   {
/* 141 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 146 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck()
/*     */     throws CivException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\market\MarketBuyCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
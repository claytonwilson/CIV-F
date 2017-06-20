/*     */ package com.avrgaming.civcraft.command.town;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.Bank;
/*     */ import com.avrgaming.civcraft.structure.Blacksmith;
/*     */ import com.avrgaming.civcraft.structure.Grocer;
/*     */ import com.avrgaming.civcraft.structure.Library;
/*     */ import com.avrgaming.civcraft.structure.ScoutTower;
/*     */ import com.avrgaming.civcraft.structure.Stable;
/*     */ import com.avrgaming.civcraft.structure.Store;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.entity.Player;
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
/*     */ public class TownSetCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  41 */     this.command = "/town set";
/*  42 */     this.displayName = "Town Set";
/*     */     
/*  44 */     this.commands.put("taxrate", "Change the town's property tax rate.");
/*  45 */     this.commands.put("flattax", "Change the town's flat tax on membership.");
/*  46 */     this.commands.put("bankfee", "Change the town Bank's non member fee");
/*  47 */     this.commands.put("storefee", "Change the town Store's non member fee");
/*  48 */     this.commands.put("grocerfee", "Change the town Grocer's non member fee");
/*  49 */     this.commands.put("libraryfee", "Change the town Library's non member fee");
/*  50 */     this.commands.put("blacksmithfee", "Change the town Blacksmith's non member fee");
/*  51 */     this.commands.put("stablefee", "Change the town Stable's non member fee");
/*     */     
/*  53 */     this.commands.put("scoutrate", "[10/30/60] Change the rate at which scout towers report no player positions.");
/*     */   }
/*     */   
/*     */   public void stablefee_cmd() throws CivException
/*     */   {
/*  58 */     Town town = getSelectedTown();
/*  59 */     Integer feeInt = getNamedInteger(1);
/*     */     
/*  61 */     Structure struct = town.findStructureByConfigId("s_stable");
/*  62 */     if (struct == null) {
/*  63 */       throw new CivException("Your town does not own a Stable.");
/*     */     }
/*     */     
/*  66 */     Stable stable = (Stable)struct;
/*     */     
/*  68 */     if ((feeInt.intValue() < Stable.FEE_MIN.intValue()) || (feeInt.intValue() > Stable.FEE_MAX.intValue())) {
/*  69 */       throw new CivException("Must be a number between 5% and 100%");
/*     */     }
/*     */     
/*  72 */     stable.setNonResidentFee(feeInt.intValue() / 100.0D);
/*  73 */     stable.updateSignText();
/*     */     
/*  75 */     CivMessage.sendSuccess(this.sender, "Set Stable fee rate to " + feeInt + "%");
/*     */   }
/*     */   
/*     */   public void scoutrate_cmd() throws CivException {
/*  79 */     Town town = getSelectedTown();
/*  80 */     Integer rate = getNamedInteger(1);
/*     */     
/*  82 */     if ((rate.intValue() != 10) && (rate.intValue() != 30) && (rate.intValue() != 60)) {
/*  83 */       throw new CivException("Reporting rate must be 10,30, or 60 seconds.");
/*     */     }
/*     */     
/*  86 */     for (Structure struct : town.getStructures()) {
/*  87 */       if ((struct instanceof ScoutTower)) {
/*  88 */         ((ScoutTower)struct).setReportSeconds(rate.intValue());
/*     */       }
/*     */     }
/*     */     
/*  92 */     CivMessage.sendSuccess(this.sender, "Set scout tower report interval to " + rate + " seconds.");
/*     */   }
/*     */   
/*     */   public void blacksmithfee_cmd() throws CivException {
/*  96 */     Town town = getSelectedTown();
/*  97 */     Integer feeInt = getNamedInteger(1);
/*     */     
/*  99 */     if ((feeInt.intValue() < 5) || (feeInt.intValue() > 15)) {
/* 100 */       throw new CivException("Must be a number between 5% and 15%");
/*     */     }
/*     */     
/* 103 */     Structure struct = town.findStructureByConfigId("s_blacksmith");
/* 104 */     if (struct == null) {
/* 105 */       throw new CivException("Your town does not own a Blacksmith.");
/*     */     }
/*     */     
/* 108 */     ((Blacksmith)struct).setNonResidentFee(feeInt.intValue() / 100.0D);
/* 109 */     ((Blacksmith)struct).updateSignText();
/*     */     
/* 111 */     CivMessage.sendSuccess(this.sender, "Set Blacksmith fee rate to " + feeInt + "%");
/*     */   }
/*     */   
/*     */   public void libraryfee_cmd() throws CivException
/*     */   {
/* 116 */     Town town = getSelectedTown();
/* 117 */     Integer feeInt = getNamedInteger(1);
/*     */     
/* 119 */     if ((feeInt.intValue() < 5) || (feeInt.intValue() > 15)) {
/* 120 */       throw new CivException("Must be a number between 5% and 15%");
/*     */     }
/*     */     
/* 123 */     Structure struct = town.findStructureByConfigId("s_library");
/* 124 */     if (struct == null) {
/* 125 */       throw new CivException("Your town does not own a library.");
/*     */     }
/*     */     
/* 128 */     ((Library)struct).setNonResidentFee(feeInt.intValue() / 100.0D);
/* 129 */     ((Library)struct).updateSignText();
/*     */     
/* 131 */     CivMessage.sendSuccess(this.sender, "Set library fee rate to " + feeInt + "%");
/*     */   }
/*     */   
/*     */   public void grocerfee_cmd() throws CivException {
/* 135 */     Town town = getSelectedTown();
/* 136 */     Integer feeInt = getNamedInteger(1);
/*     */     
/* 138 */     if ((feeInt.intValue() < 5) || (feeInt.intValue() > 15)) {
/* 139 */       throw new CivException("Must be a number between 5% and 15%");
/*     */     }
/*     */     
/* 142 */     Structure struct = town.findStructureByConfigId("s_grocer");
/* 143 */     if (struct == null) {
/* 144 */       throw new CivException("Your town does not own a grocer.");
/*     */     }
/*     */     
/* 147 */     ((Grocer)struct).setNonResidentFee(feeInt.intValue() / 100.0D);
/* 148 */     ((Grocer)struct).updateSignText();
/*     */     
/* 150 */     CivMessage.sendSuccess(this.sender, "Set grocer fee rate to " + feeInt + "%");
/*     */   }
/*     */   
/*     */   public void storefee_cmd() throws CivException
/*     */   {
/* 155 */     Town town = getSelectedTown();
/* 156 */     Integer feeInt = getNamedInteger(1);
/*     */     
/* 158 */     if ((feeInt.intValue() < 5) || (feeInt.intValue() > 15)) {
/* 159 */       throw new CivException("Must be a number between 5% and 15%");
/*     */     }
/*     */     
/* 162 */     Structure struct = town.findStructureByConfigId("s_store");
/* 163 */     if (struct == null) {
/* 164 */       throw new CivException("Your town does not own a store.");
/*     */     }
/*     */     
/* 167 */     ((Store)struct).setNonResidentFee(feeInt.intValue() / 100.0D);
/* 168 */     ((Store)struct).updateSignText();
/*     */     
/* 170 */     CivMessage.sendSuccess(this.sender, "Set store fee rate to " + feeInt + "%");
/*     */   }
/*     */   
/*     */   public void bankfee_cmd() throws CivException
/*     */   {
/* 175 */     Town town = getSelectedTown();
/* 176 */     Integer feeInt = getNamedInteger(1);
/*     */     
/* 178 */     if ((feeInt.intValue() < 5) || (feeInt.intValue() > 15)) {
/* 179 */       throw new CivException("Must be a number between 5% and 15%");
/*     */     }
/*     */     
/* 182 */     Structure struct = town.findStructureByConfigId("s_bank");
/* 183 */     if (struct == null) {
/* 184 */       throw new CivException("Your town does not own a bank.");
/*     */     }
/*     */     
/* 187 */     ((Bank)struct).setNonResidentFee(feeInt.intValue() / 100.0D);
/* 188 */     ((Bank)struct).updateSignText();
/*     */     
/* 190 */     CivMessage.sendSuccess(this.sender, "Set bank fee rate to " + feeInt + "%");
/*     */   }
/*     */   
/*     */   public void taxrate_cmd() throws CivException
/*     */   {
/* 195 */     Town town = getSelectedTown();
/*     */     
/* 197 */     if (this.args.length < 2) {
/* 198 */       throw new CivException("Please specify a tax rate.");
/*     */     }
/*     */     try
/*     */     {
/* 202 */       town.setTaxRate(Double.valueOf(this.args[1]).doubleValue() / 100.0D);
/*     */     } catch (NumberFormatException e) {
/* 204 */       throw new CivException(this.args[1] + " is not a number.");
/*     */     }
/*     */     
/* 207 */     town.quicksave();
/* 208 */     CivMessage.sendTown(town, "Town changed property tax rate to " + this.args[1] + "%");
/*     */   }
/*     */   
/*     */   public void flattax_cmd() throws CivException {
/* 212 */     Town town = getSelectedTown();
/* 213 */     if (this.args.length < 2) {
/* 214 */       throw new CivException("Please specify a tax rate.");
/*     */     }
/*     */     try
/*     */     {
/* 218 */       town.setFlatTax(Integer.valueOf(this.args[1]).intValue());
/*     */     } catch (NumberFormatException e) {
/* 220 */       throw new CivException(this.args[1] + " is not a number.");
/*     */     }
/*     */     
/* 223 */     town.quicksave();
/* 224 */     CivMessage.send(town, "Town changed flat tax to " + this.args[1]);
/*     */   }
/*     */   
/*     */   public void doDefaultAction() throws CivException
/*     */   {
/* 229 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 234 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck() throws CivException
/*     */   {
/* 239 */     Town town = getSelectedTown();
/* 240 */     Player player = getPlayer();
/*     */     
/* 242 */     if ((!town.playerIsInGroupName("mayors", player)) && (!town.playerIsInGroupName("assistants", player))) {
/* 243 */       throw new CivException("Only mayors and assistants can use this command.");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\town\TownSetCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
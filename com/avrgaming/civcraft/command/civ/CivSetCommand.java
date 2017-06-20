/*     */ package com.avrgaming.civcraft.command.civ;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.config.ConfigGovernment;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.util.DecimalHelper;
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
/*     */ public class CivSetCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  31 */     this.command = "/civ set";
/*  32 */     this.displayName = "Civ Set";
/*     */     
/*  34 */     this.commands.put("taxes", "[percent] Sets the income tax for this civ.");
/*  35 */     this.commands.put("science", "[percent] Sets the amount of taxes that get converted into beakers.");
/*  36 */     this.commands.put("color", "(value) shows you the current civ's color. If value is specified, changes your color.");
/*     */   }
/*     */   
/*     */   private double vaildatePercentage(String arg)
/*     */     throws CivException
/*     */   {
/*     */     try
/*     */     {
/*  44 */       arg = arg.replace("%", "");
/*     */       
/*  46 */       Integer amount = Integer.valueOf(arg);
/*     */       
/*  48 */       if ((amount.intValue() < 0) || (amount.intValue() > 100)) {
/*  49 */         throw new CivException("You must set a percentage between 0% and 100%");
/*     */       }
/*     */       
/*  52 */       return amount.intValue() / 100.0D;
/*     */     }
/*     */     catch (NumberFormatException e) {
/*  55 */       throw new CivException(arg + " is not a number.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void taxes_cmd() throws CivException
/*     */   {
/*  61 */     Civilization civ = getSenderCiv();
/*     */     
/*  63 */     if (this.args.length < 2) {
/*  64 */       CivMessage.send(this.sender, "Current income percentage:" + civ.getIncomeTaxRateString());
/*  65 */       return;
/*     */     }
/*     */     
/*  68 */     double newPercentage = vaildatePercentage(this.args[1]);
/*     */     
/*  70 */     if (newPercentage > civ.getGovernment().maximum_tax_rate) {
/*  71 */       throw new CivException("Cannot set your tax rate higher than your government's maximum(" + 
/*  72 */         DecimalHelper.formatPercentage(civ.getGovernment().maximum_tax_rate) + ")");
/*     */     }
/*     */     
/*  75 */     civ.setIncomeTaxRate(newPercentage);
/*     */     
/*  77 */     civ.save();
/*     */     
/*  79 */     CivMessage.sendSuccess(this.sender, "Set income rate to " + this.args[1] + " percent.");
/*     */   }
/*     */   
/*     */   public void science_cmd() throws CivException {
/*  83 */     Civilization civ = getSenderCiv();
/*     */     
/*  85 */     if (this.args.length < 2) {
/*  86 */       CivMessage.send(this.sender, "Current science percentage:" + civ.getSciencePercentage());
/*  87 */       return;
/*     */     }
/*     */     
/*  90 */     double newPercentage = vaildatePercentage(this.args[1]);
/*     */     
/*  92 */     civ.setSciencePercentage(newPercentage);
/*  93 */     civ.save();
/*     */     
/*  95 */     CivMessage.sendSuccess(this.sender, "Set science rate to " + this.args[1] + " percent.");
/*     */   }
/*     */   
/*     */   public void color_cmd() throws CivException
/*     */   {
/* 100 */     Civilization civ = getSenderCiv();
/*     */     
/* 102 */     if (this.args.length < 2) {
/* 103 */       CivMessage.sendSuccess(this.sender, "Civ color is: " + Integer.toHexString(civ.getColor()));
/* 104 */       return;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 109 */       int color = Integer.parseInt(this.args[1], 16);
/* 110 */       if (color > 16777215) {
/* 111 */         throw new CivException("Invalid color, out of range.");
/*     */       }
/* 113 */       if (color == 16711680) {
/* 114 */         throw new CivException("Invalid color, this color is reserved for town borders.");
/*     */       }
/*     */       
/* 117 */       civ.setColor(color);
/* 118 */       civ.save();
/* 119 */       CivMessage.sendSuccess(this.sender, "Set civ color to " + Integer.toHexString(color));
/*     */     } catch (NumberFormatException e) {
/* 121 */       throw new CivException(this.args[1] + " is an invalid color.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void doDefaultAction() throws CivException
/*     */   {
/* 127 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 132 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck() throws CivException
/*     */   {
/* 137 */     validLeaderAdvisor();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\civ\CivSetCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
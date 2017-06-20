/*     */ package com.avrgaming.civcraft.interactive;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*     */ import com.avrgaming.global.perks.components.RenameCivOrTown;
/*     */ 
/*     */ public class InteractiveRenameCivOrTown implements InteractiveResponse
/*     */ {
/*  15 */   public String selection = null;
/*  16 */   public String oldName = null;
/*  17 */   public String newName = null;
/*  18 */   public Civilization selectedCiv = null;
/*  19 */   public Town selectedTown = null;
/*     */   RenameCivOrTown perk;
/*     */   
/*     */   public InteractiveRenameCivOrTown(Resident resident, RenameCivOrTown perk)
/*     */   {
/*  24 */     displayQuestion(resident);
/*  25 */     this.perk = perk;
/*     */   }
/*     */   
/*     */   public void displayQuestion(Resident resident) {
/*  29 */     CivMessage.send(resident, "§2Would you like to rename a §aCIV§2 or a §aTOWN§2?");
/*     */     
/*  31 */     CivMessage.send(resident, "§8(Type 'civ' or 'town' anything else cancels.)");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void respond(String message, Resident resident)
/*     */   {
/*  39 */     CivMessage.sendHeading(resident, "Rename Civilization or Town");
/*     */     try
/*     */     {
/*  42 */       if (this.selection == null) {
/*  43 */         if (message.equalsIgnoreCase("town")) {
/*  44 */           CivMessage.send(resident, "§2Enter the name of the town you want to rename:");
/*  45 */           this.selection = "town";
/*  46 */         } else if (message.equalsIgnoreCase("civ")) {
/*  47 */           CivMessage.send(resident, "§2Enter the name of the civ you want to rename:");
/*  48 */           this.selection = "civ";
/*     */         } else {
/*  50 */           throw new CivException("Rename cancelled.");
/*     */         }
/*  52 */       } else if (this.oldName == null) {
/*  53 */         this.oldName = message;
/*  54 */         if (this.selection.equals("town")) {
/*  55 */           Town town = CivGlobal.getTown(this.oldName);
/*  56 */           if (town == null) {
/*  57 */             throw new CivException("No town named " + this.oldName + ".");
/*     */           }
/*     */           
/*  60 */           if ((!town.getMayorGroup().hasMember(resident)) && (!town.getCiv().getLeaderGroup().hasMember(resident))) {
/*  61 */             throw new CivException("You must be the town's mayor or the civ's leader to rename towns.");
/*     */           }
/*     */           
/*  64 */           this.selectedTown = town;
/*  65 */           CivMessage.send(resident, "§2Enter the NEW name of your town:");
/*  66 */         } else if (this.selection.equals("civ")) {
/*  67 */           Civilization civ = CivGlobal.getCiv(this.oldName);
/*  68 */           if (civ == null) {
/*  69 */             civ = CivGlobal.getConqueredCiv(this.oldName);
/*  70 */             if (civ == null) {
/*  71 */               throw new CivException("No civ named " + this.oldName + ".");
/*     */             }
/*     */           }
/*     */           
/*  75 */           if (!civ.getLeaderGroup().hasMember(resident)) {
/*  76 */             throw new CivException("You must be the civ's leader in order to rename it.");
/*     */           }
/*     */           
/*  79 */           this.selectedCiv = civ;
/*  80 */           CivMessage.send(resident, "§2Enter the NEW name of your civ:");
/*     */         }
/*  82 */       } else if (this.newName == null) {
/*  83 */         this.newName = message.replace(" ", "_");
/*  84 */         if (this.selectedCiv != null) {
/*     */           try {
/*  86 */             CivMessage.global(
/*  87 */               resident.getName() + " has used a " + "§e" + "Rename Token" + com.avrgaming.civcraft.util.CivColor.RESET + " to rename the civ of " + this.selectedCiv.getName() + " to " + this.newName);
/*  88 */             this.selectedCiv.rename(this.newName);
/*  89 */             this.perk.markAsUsed(resident);
/*     */           } catch (InvalidNameException e) {
/*  91 */             throw new CivException("This name is not valid. Pick another.");
/*     */           }
/*  93 */         } else if (this.selectedTown != null) {
/*     */           try {
/*  95 */             CivMessage.global(
/*  96 */               resident.getName() + " has used a " + "§e" + "Rename Token" + com.avrgaming.civcraft.util.CivColor.RESET + " to rename the town of " + this.selectedTown.getName() + " to " + this.newName);
/*  97 */             this.selectedTown.rename(this.newName);
/*  98 */             this.perk.markAsUsed(resident);
/*     */           } catch (InvalidNameException e) {
/* 100 */             throw new CivException("This name is not valid. Pick another.");
/*     */           }
/*     */         }
/*     */       } else {
/* 104 */         throw new CivException("Couldn't find all the information we needed. Rename cancelled.");
/*     */       }
/*     */     } catch (CivException e) {
/* 107 */       CivMessage.sendError(resident, e.getMessage());
/* 108 */       resident.clearInteractiveMode();
/* 109 */       return;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\interactive\InteractiveRenameCivOrTown.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
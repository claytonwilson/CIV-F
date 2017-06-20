/*     */ package com.avrgaming.civcraft.command.civ;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigTech;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*     */ import com.avrgaming.civcraft.structure.TownHall;
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
/*     */ public class CivResearchCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  39 */     this.command = "/civ research";
/*  40 */     this.displayName = "Civ Research";
/*     */     
/*  42 */     this.commands.put("list", "List the available technologies we can research.");
/*  43 */     this.commands.put("progress", "Shows progress on your current research.");
/*  44 */     this.commands.put("on", "[tech] - Starts researching on this technology.");
/*  45 */     this.commands.put("change", "[tech] - Stops researching our current tech, changes to this. You will lose all progress on your current tech.");
/*  46 */     this.commands.put("finished", "Shows which technologies we already have.");
/*     */   }
/*     */   
/*     */   public void change_cmd() throws CivException {
/*  50 */     Civilization civ = getSenderCiv();
/*     */     
/*  52 */     if (this.args.length < 2) {
/*  53 */       list_cmd();
/*  54 */       throw new CivException("enter the name of the technology you want to change to.");
/*     */     }
/*     */     
/*  57 */     String techname = combineArgs(stripArgs(this.args, 1));
/*  58 */     ConfigTech tech = CivSettings.getTechByName(techname);
/*  59 */     if (tech == null) {
/*  60 */       throw new CivException("Couldn't find technology named " + techname);
/*     */     }
/*     */     
/*  63 */     if (!civ.getTreasury().hasEnough(tech.cost)) {
/*  64 */       throw new CivException("You do not have enough coins to research " + tech.name);
/*     */     }
/*     */     
/*  67 */     if (!tech.isAvailable(civ)) {
/*  68 */       throw new CivException("You cannot research this technology at this time.");
/*     */     }
/*     */     
/*  71 */     if (civ.getResearchTech() != null) {
/*  72 */       civ.setResearchProgress(0.0D);
/*  73 */       CivMessage.send(this.sender, "§cProgress on " + civ.getResearchTech().name + " has been lost.");
/*  74 */       civ.setResearchTech(null);
/*     */     }
/*     */     
/*  77 */     civ.startTechnologyResearch(tech);
/*  78 */     CivMessage.sendCiv(civ, "Our Civilization started researching " + tech.name);
/*     */   }
/*     */   
/*     */   public void finished_cmd() throws CivException {
/*  82 */     Civilization civ = getSenderCiv();
/*     */     
/*  84 */     CivMessage.sendHeading(this.sender, "Researched Technologies");
/*  85 */     String out = "";
/*  86 */     for (ConfigTech tech : civ.getTechs()) {
/*  87 */       out = out + tech.name + ", ";
/*     */     }
/*  89 */     CivMessage.send(this.sender, out);
/*     */   }
/*     */   
/*     */   public void on_cmd() throws CivException {
/*  93 */     Civilization civ = getSenderCiv();
/*     */     
/*  95 */     if (this.args.length < 2) {
/*  96 */       throw new CivException("Enter the name of the technology you want to research.");
/*     */     }
/*     */     
/*  99 */     Town capitol = CivGlobal.getTown(civ.getCapitolName());
/* 100 */     if (capitol == null) {
/* 101 */       throw new CivException("Couldn't find capitol town:" + civ.getCapitolName() + "! Internal Error!");
/*     */     }
/*     */     
/* 104 */     TownHall townhall = capitol.getTownHall();
/* 105 */     if (townhall == null) {
/* 106 */       throw new CivException("Couldn't find your capitol's town hall. Cannot perform research without a town hall! ");
/*     */     }
/*     */     
/* 109 */     if (!townhall.isActive()) {
/* 110 */       throw new CivException("Town hall must be completed before you can begin research.");
/*     */     }
/*     */     
/* 113 */     String techname = combineArgs(stripArgs(this.args, 1));
/* 114 */     ConfigTech tech = CivSettings.getTechByName(techname);
/* 115 */     if (tech == null) {
/* 116 */       throw new CivException("Couldn't find technology named " + techname);
/*     */     }
/*     */     
/* 119 */     civ.startTechnologyResearch(tech);
/* 120 */     CivMessage.sendSuccess(this.sender, "Started researching " + tech.name);
/*     */   }
/*     */   
/*     */   public void progress_cmd() throws CivException {
/* 124 */     Civilization civ = getSenderCiv();
/*     */     
/* 126 */     CivMessage.sendHeading(this.sender, "Currently Researching");
/*     */     
/* 128 */     if (civ.getResearchTech() != null) {
/* 129 */       int percentageComplete = (int)(civ.getResearchProgress() / civ.getResearchTech().beaker_cost * 100.0D);
/* 130 */       CivMessage.send(this.sender, civ.getResearchTech().name + " is " + percentageComplete + "% complete. (" + 
/* 131 */         civ.getResearchProgress() + " / " + civ.getResearchTech().beaker_cost + " ) ");
/*     */     } else {
/* 133 */       CivMessage.send(this.sender, "Nothing currently researching.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void list_cmd() throws CivException
/*     */   {
/* 139 */     Civilization civ = getSenderCiv();
/* 140 */     ArrayList<ConfigTech> techs = ConfigTech.getAvailableTechs(civ);
/*     */     
/* 142 */     CivMessage.sendHeading(this.sender, "Available Research");
/* 143 */     for (ConfigTech tech : techs) {
/* 144 */       CivMessage.send(this.sender, tech.name + "§7" + " Cost: " + 
/* 145 */         "§e" + tech.cost + "§7" + " Beakers: " + 
/* 146 */         "§e" + tech.beaker_cost);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void doDefaultAction()
/*     */     throws CivException
/*     */   {
/* 154 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 159 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck() throws CivException
/*     */   {
/* 164 */     Resident resident = getResident();
/* 165 */     Civilization civ = getSenderCiv();
/*     */     
/* 167 */     if ((!civ.getLeaderGroup().hasMember(resident)) && (!civ.getAdviserGroup().hasMember(resident))) {
/* 168 */       throw new CivException("Only civ leaders and advisers can access research.");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\civ\CivResearchCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.avrgaming.civcraft.command.admin;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
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
/*     */ public class AdminChatCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  33 */     this.command = "/ad chat";
/*  34 */     this.displayName = "Admin Chat";
/*     */     
/*  36 */     this.commands.put("tc", "[town] - joins this town's chat channel.");
/*  37 */     this.commands.put("cc", "[civ] - join's this civ's chat channel.");
/*  38 */     this.commands.put("cclisten", "[name] toggles listening in on this civ's chat channel.");
/*  39 */     this.commands.put("tclisten", "[name] toggles listening in on this town's chat channel.");
/*  40 */     this.commands.put("listenoff", "removes you from all chat channels.");
/*  41 */     this.commands.put("cclistenall", "adds listening to every civ's chat channel.");
/*  42 */     this.commands.put("tclistenall", "adds listening to every town's chat channel.");
/*  43 */     this.commands.put("banwordon", "Turns on banning from words.");
/*  44 */     this.commands.put("banwordoff", "Turns off banning from words.");
/*  45 */     this.commands.put("banwordadd", "Adds this word to the ban word list");
/*  46 */     this.commands.put("banwordremove", "Removes this word to the ban word list");
/*  47 */     this.commands.put("banwordtoggle", "Toggles all ban words to ban regardless of time online.");
/*     */   }
/*     */   
/*     */   public void tclistenall_cmd() throws CivException
/*     */   {
/*  52 */     Resident resident = getResident();
/*     */     
/*  54 */     for (Town t : CivGlobal.getTowns()) {
/*  55 */       CivMessage.addExtraTownChatListener(t, resident.getName());
/*     */     }
/*     */     
/*  58 */     CivMessage.sendSuccess(this.sender, "Added you from all town chat channels.");
/*     */   }
/*     */   
/*     */   public void cclistenall_cmd() throws CivException {
/*  62 */     Resident resident = getResident();
/*     */     
/*  64 */     for (Civilization civ : CivGlobal.getCivs()) {
/*  65 */       CivMessage.addExtraCivChatListener(civ, resident.getName());
/*     */     }
/*     */     
/*  68 */     CivMessage.sendSuccess(this.sender, "Added you from all civ chat channels.");
/*     */   }
/*     */   
/*     */   public void listenoff_cmd() throws CivException {
/*  72 */     Resident resident = getResident();
/*     */     
/*  74 */     for (Town t : CivGlobal.getTowns()) {
/*  75 */       CivMessage.removeExtraTownChatListener(t, resident.getName());
/*     */     }
/*     */     
/*  78 */     for (Civilization civ : CivGlobal.getCivs()) {
/*  79 */       CivMessage.removeExtraCivChatListener(civ, resident.getName());
/*     */     }
/*     */     
/*  82 */     CivMessage.sendSuccess(this.sender, "Removed you from all chat channels.");
/*     */   }
/*     */   
/*     */   public void cclisten_cmd() throws CivException {
/*  86 */     if (this.args.length < 2) {
/*  87 */       throw new CivException("Please enter a civ name.");
/*     */     }
/*     */     
/*  90 */     Resident resident = getResident();
/*     */     
/*  92 */     Civilization civ = getNamedCiv(1);
/*     */     
/*  94 */     for (String str : CivMessage.getExtraCivChatListeners(civ)) {
/*  95 */       if (str.equalsIgnoreCase(resident.getName())) {
/*  96 */         CivMessage.removeExtraCivChatListener(civ, str);
/*  97 */         CivMessage.sendSuccess(this.sender, "No longer listening to civ " + civ.getName());
/*  98 */         return;
/*     */       }
/*     */     }
/*     */     
/* 102 */     CivMessage.addExtraCivChatListener(civ, resident.getName());
/* 103 */     CivMessage.sendSuccess(this.sender, "Listening to civ " + civ.getName());
/*     */   }
/*     */   
/*     */   public void tclisten_cmd() throws CivException {
/* 107 */     if (this.args.length < 2) {
/* 108 */       throw new CivException("Please enter a town name.");
/*     */     }
/*     */     
/* 111 */     Resident resident = getResident();
/*     */     
/* 113 */     Town town = getNamedTown(1);
/*     */     
/* 115 */     for (String str : CivMessage.getExtraTownChatListeners(town)) {
/* 116 */       if (str.equalsIgnoreCase(resident.getName())) {
/* 117 */         CivMessage.removeExtraTownChatListener(town, str);
/* 118 */         CivMessage.sendSuccess(this.sender, "No longer listening to town " + town.getName());
/* 119 */         return;
/*     */       }
/*     */     }
/*     */     
/* 123 */     CivMessage.addExtraTownChatListener(town, resident.getName());
/* 124 */     CivMessage.sendSuccess(this.sender, "Listening to town " + town.getName());
/*     */   }
/*     */   
/*     */   public void tc_cmd() throws CivException {
/* 128 */     Resident resident = getResident();
/* 129 */     if (this.args.length < 2) {
/* 130 */       resident.setTownChat(false);
/* 131 */       resident.setTownChatOverride(null);
/* 132 */       CivMessage.sendSuccess(this.sender, "Toggled tc off.");
/* 133 */       return;
/*     */     }
/*     */     
/* 136 */     Town town = getNamedTown(1);
/*     */     
/* 138 */     resident.setTownChat(true);
/* 139 */     resident.setTownChatOverride(town);
/* 140 */     CivMessage.sendSuccess(this.sender, "Now chatting in town chat:" + town.getName());
/*     */   }
/*     */   
/*     */   public void cc_cmd() throws CivException {
/* 144 */     Resident resident = getResident();
/* 145 */     if (this.args.length < 2) {
/* 146 */       resident.setCivChat(false);
/* 147 */       resident.setCivChatOverride(null);
/* 148 */       CivMessage.sendSuccess(this.sender, "Toggled cc off.");
/* 149 */       return;
/*     */     }
/*     */     
/* 152 */     Civilization civ = getNamedCiv(1);
/*     */     
/* 154 */     resident.setCivChat(true);
/* 155 */     resident.setCivChatOverride(civ);
/* 156 */     CivMessage.sendSuccess(this.sender, "Now chatting in civ chat:" + civ.getName());
/*     */   }
/*     */   
/*     */   public void banwordon_cmd() {
/* 160 */     CivGlobal.banWordsActive = true;
/* 161 */     CivMessage.sendSuccess(this.sender, "Activated banwords.");
/*     */   }
/*     */   
/*     */   public void banwordoff_cmd() {
/* 165 */     CivGlobal.banWordsActive = false;
/* 166 */     CivMessage.sendSuccess(this.sender, "Deactivated banwords.");
/*     */   }
/*     */   
/*     */   public void banwordadd_cmd() throws CivException
/*     */   {
/* 171 */     if (this.args.length < 2) {
/* 172 */       throw new CivException("Enter a word to ban");
/*     */     }
/*     */     
/* 175 */     CivGlobal.banWords.add(this.args[1]);
/* 176 */     CivMessage.sendSuccess(this.sender, "added " + this.args[1]);
/*     */   }
/*     */   
/*     */   public void banwordremove_cmd() throws CivException {
/* 180 */     if (this.args.length < 2) {
/* 181 */       throw new CivException("Enter a word to ban");
/*     */     }
/*     */     
/* 184 */     CivGlobal.banWords.remove(this.args[1]);
/* 185 */     CivMessage.sendSuccess(this.sender, "removed " + this.args[1]);
/*     */   }
/*     */   
/*     */   public void banwordtoggle() throws CivException
/*     */   {
/* 190 */     CivGlobal.banWordsAlways = Boolean.valueOf(!CivGlobal.banWordsAlways.booleanValue());
/* 191 */     CivMessage.sendSuccess(this.sender, "Ban always:" + CivGlobal.banWordsAlways);
/*     */   }
/*     */   
/*     */   public void doDefaultAction() throws CivException
/*     */   {
/* 196 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 201 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck()
/*     */     throws CivException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\admin\AdminChatCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
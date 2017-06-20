/*     */ package com.avrgaming.civcraft.command;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import java.sql.SQLException;
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
/*     */ public class EconCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  37 */     this.command = "/econ";
/*  38 */     this.displayName = "Econ";
/*     */     
/*  40 */     this.commands.put("add", "[player] [amount] - add money to this player");
/*  41 */     this.commands.put("set", "[player] [amount] - set money for this player");
/*  42 */     this.commands.put("sub", "[player] [amount] - subtract money for this player");
/*     */     
/*  44 */     this.commands.put("addtown", "[town] [amount] - add money to this town");
/*  45 */     this.commands.put("settown", "[town] [amount] - set money for this town");
/*  46 */     this.commands.put("subtown", "[town] [amount] - subtract money for this town");
/*     */     
/*  48 */     this.commands.put("addciv", "[civ] [amount] - add money to this civ");
/*  49 */     this.commands.put("setciv", "[civ] [amount] - set money for this civ");
/*  50 */     this.commands.put("subciv", "[civ] [amount] - subtract money for this civ");
/*     */     
/*  52 */     this.commands.put("setdebt", "[player] [amount] - sets the debt on this player to this amount.");
/*  53 */     this.commands.put("setdebttown", "[town] [amount]");
/*  54 */     this.commands.put("setdebtciv", "[civ] [amount]");
/*     */     
/*  56 */     this.commands.put("clearalldebt", "Clears all debt for everyone in the server. Residents, Towns, Civs");
/*     */   }
/*     */   
/*     */   public void clearalldebt_cmd() throws CivException
/*     */   {
/*  61 */     validEcon();
/*     */     
/*  63 */     for (Civilization civ : CivGlobal.getCivs()) {
/*  64 */       civ.getTreasury().setDebt(0.0D);
/*     */       try {
/*  66 */         civ.saveNow();
/*     */       } catch (SQLException e) {
/*  68 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*  72 */     for (Town town : CivGlobal.getTowns()) {
/*  73 */       town.getTreasury().setDebt(0.0D);
/*     */       try {
/*  75 */         town.saveNow();
/*     */       } catch (SQLException e) {
/*  77 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*  81 */     for (Resident res : CivGlobal.getResidents()) {
/*  82 */       res.getTreasury().setDebt(0.0D);
/*     */       try {
/*  84 */         res.saveNow();
/*     */       } catch (SQLException e) {
/*  86 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*  90 */     CivMessage.send(this.sender, "Cleared all debt");
/*     */   }
/*     */   
/*     */   public void setdebtciv_cmd() throws CivException
/*     */   {
/*  95 */     validEcon();
/*     */     
/*  97 */     Civilization civ = getNamedCiv(1);
/*  98 */     Double amount = getNamedDouble(2);
/*  99 */     civ.getTreasury().setDebt(amount.doubleValue());
/* 100 */     civ.save();
/*     */     
/* 102 */     CivMessage.sendSuccess(this.sender, "Set.");
/*     */   }
/*     */   
/*     */   public void setdebttown_cmd() throws CivException {
/* 106 */     validEcon();
/*     */     
/* 108 */     Town town = getNamedTown(1);
/* 109 */     Double amount = getNamedDouble(2);
/* 110 */     town.getTreasury().setDebt(amount.doubleValue());
/* 111 */     town.save();
/*     */     
/* 113 */     CivMessage.sendSuccess(this.sender, "Set.");
/*     */   }
/*     */   
/*     */   public void setdebt_cmd() throws CivException {
/* 117 */     validEcon();
/*     */     
/* 119 */     Resident resident = getNamedResident(1);
/* 120 */     Double amount = getNamedDouble(2);
/* 121 */     resident.getTreasury().setDebt(amount.doubleValue());
/* 122 */     resident.save();
/*     */     
/* 124 */     CivMessage.sendSuccess(this.sender, "Set.");
/*     */   }
/*     */   
/*     */   private void validEcon() throws CivException {
/* 128 */     if ((!getPlayer().isOp()) || (!getPlayer().hasPermission("civ.econ"))) {
/* 129 */       throw new CivException("You must be OP to use this command.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void add_cmd() throws CivException {
/* 134 */     validEcon();
/*     */     
/* 136 */     if (this.args.length < 3) {
/* 137 */       throw new CivException("Provide a name and a amount");
/*     */     }
/*     */     
/* 140 */     Resident resident = getNamedResident(1);
/*     */     
/*     */     try
/*     */     {
/* 144 */       Double amount = Double.valueOf(this.args[2]);
/* 145 */       resident.getTreasury().deposit(amount.doubleValue());
/* 146 */       CivMessage.sendSuccess(this.sender, "Added " + this.args[2] + " to " + this.args[1]);
/*     */     }
/*     */     catch (NumberFormatException e) {
/* 149 */       throw new CivException(this.args[2] + " is not a number.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void set_cmd() throws CivException {
/* 154 */     validEcon();
/*     */     
/* 156 */     if (this.args.length < 3) {
/* 157 */       throw new CivException("Provide a name and a amount");
/*     */     }
/*     */     
/* 160 */     Resident resident = getNamedResident(1);
/*     */     
/*     */     try
/*     */     {
/* 164 */       Double amount = Double.valueOf(this.args[2]);
/* 165 */       resident.getTreasury().setBalance(amount.doubleValue());
/* 166 */       CivMessage.sendSuccess(this.sender, "Set " + this.args[2] + " to " + this.args[1]);
/*     */     }
/*     */     catch (NumberFormatException e) {
/* 169 */       throw new CivException(this.args[2] + " is not a number.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void sub_cmd() throws CivException {
/* 174 */     validEcon();
/*     */     
/* 176 */     if (this.args.length < 3) {
/* 177 */       throw new CivException("Provide a name and a amount");
/*     */     }
/*     */     
/* 180 */     Resident resident = getNamedResident(1);
/*     */     
/*     */     try
/*     */     {
/* 184 */       Double amount = Double.valueOf(this.args[2]);
/* 185 */       resident.getTreasury().withdraw(amount.doubleValue());
/* 186 */       CivMessage.sendSuccess(this.sender, "Subtracted " + this.args[2] + " to " + this.args[1]);
/*     */     }
/*     */     catch (NumberFormatException e) {
/* 189 */       throw new CivException(this.args[2] + " is not a number.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void addtown_cmd() throws CivException {
/* 194 */     validEcon();
/*     */     
/* 196 */     if (this.args.length < 3) {
/* 197 */       throw new CivException("Provide a name and a amount");
/*     */     }
/*     */     
/* 200 */     Town town = getNamedTown(1);
/*     */     
/*     */     try
/*     */     {
/* 204 */       Double amount = Double.valueOf(this.args[2]);
/* 205 */       town.getTreasury().deposit(amount.doubleValue());
/* 206 */       CivMessage.sendSuccess(this.sender, "Added " + this.args[2] + " to " + this.args[1]);
/*     */     }
/*     */     catch (NumberFormatException e) {
/* 209 */       throw new CivException(this.args[2] + " is not a number.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void settown_cmd() throws CivException {
/* 214 */     validEcon();
/*     */     
/* 216 */     if (this.args.length < 3) {
/* 217 */       throw new CivException("Provide a name and a amount");
/*     */     }
/*     */     
/* 220 */     Town town = getNamedTown(1);
/*     */     
/*     */     try
/*     */     {
/* 224 */       Double amount = Double.valueOf(this.args[2]);
/* 225 */       town.getTreasury().setBalance(amount.doubleValue());
/* 226 */       CivMessage.sendSuccess(this.sender, "Added " + this.args[2] + " to " + this.args[1]);
/*     */     }
/*     */     catch (NumberFormatException e) {
/* 229 */       throw new CivException(this.args[2] + " is not a number.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void subtown_cmd() throws CivException {
/* 234 */     validEcon();
/*     */     
/* 236 */     if (this.args.length < 3) {
/* 237 */       throw new CivException("Provide a name and a amount");
/*     */     }
/*     */     
/* 240 */     Town town = getNamedTown(1);
/*     */     
/*     */     try
/*     */     {
/* 244 */       Double amount = Double.valueOf(this.args[2]);
/* 245 */       town.getTreasury().withdraw(amount.doubleValue());
/* 246 */       CivMessage.sendSuccess(this.sender, "Added " + this.args[2] + " to " + this.args[1]);
/*     */     }
/*     */     catch (NumberFormatException e) {
/* 249 */       throw new CivException(this.args[2] + " is not a number.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void addciv_cmd() throws CivException {
/* 254 */     validEcon();
/*     */     
/* 256 */     if (this.args.length < 3) {
/* 257 */       throw new CivException("Provide a name and a amount");
/*     */     }
/*     */     
/* 260 */     Civilization civ = getNamedCiv(1);
/*     */     
/*     */     try
/*     */     {
/* 264 */       Double amount = Double.valueOf(this.args[2]);
/* 265 */       civ.getTreasury().deposit(amount.doubleValue());
/* 266 */       CivMessage.sendSuccess(this.sender, "Added " + this.args[2] + " to " + this.args[1]);
/*     */     }
/*     */     catch (NumberFormatException e) {
/* 269 */       throw new CivException(this.args[2] + " is not a number.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void setciv_cmd() throws CivException {
/* 274 */     validEcon();
/*     */     
/* 276 */     if (this.args.length < 3) {
/* 277 */       throw new CivException("Provide a name and a amount");
/*     */     }
/*     */     
/* 280 */     Civilization civ = getNamedCiv(1);
/*     */     
/*     */     try
/*     */     {
/* 284 */       Double amount = Double.valueOf(this.args[2]);
/* 285 */       civ.getTreasury().setBalance(amount.doubleValue());
/* 286 */       CivMessage.sendSuccess(this.sender, "Added " + this.args[2] + " to " + this.args[1]);
/*     */     }
/*     */     catch (NumberFormatException e) {
/* 289 */       throw new CivException(this.args[2] + " is not a number.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void subciv_cmd() throws CivException {
/* 294 */     validEcon();
/*     */     
/* 296 */     if (this.args.length < 3) {
/* 297 */       throw new CivException("Provide a name and a amount");
/*     */     }
/*     */     
/* 300 */     Civilization civ = getNamedCiv(1);
/*     */     
/*     */     try
/*     */     {
/* 304 */       Double amount = Double.valueOf(this.args[2]);
/* 305 */       civ.getTreasury().withdraw(amount.doubleValue());
/* 306 */       CivMessage.sendSuccess(this.sender, "Added " + this.args[2] + " to " + this.args[1]);
/*     */     }
/*     */     catch (NumberFormatException e) {
/* 309 */       throw new CivException(this.args[2] + " is not a number.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void doDefaultAction() throws CivException
/*     */   {
/* 315 */     Player player = getPlayer();
/* 316 */     Resident resident = CivGlobal.getResident(player);
/*     */     
/* 318 */     if (resident == null) {
/* 319 */       return;
/*     */     }
/*     */     
/* 322 */     CivMessage.sendSuccess(player, resident.getTreasury().getBalance() + " coins.");
/*     */   }
/*     */   
/*     */ 
/*     */   public void showHelp()
/*     */   {
/*     */     try
/*     */     {
/* 330 */       player = getPlayer();
/*     */     } catch (CivException e) { Player player;
/* 332 */       e.printStackTrace(); return;
/*     */     }
/*     */     
/*     */     Player player;
/* 336 */     if ((!player.isOp()) && (!player.hasPermission("civ.econ"))) {
/* 337 */       return;
/*     */     }
/*     */     
/* 340 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck()
/*     */     throws CivException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\EconCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
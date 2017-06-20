/*     */ package com.avrgaming.civcraft.command.civ;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.endgame.EndConditionDiplomacy;
/*     */ import com.avrgaming.civcraft.endgame.EndConditionScience;
/*     */ import com.avrgaming.civcraft.endgame.EndGameCondition;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Buff;
/*     */ import com.avrgaming.civcraft.object.BuffManager;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.avrgaming.civcraft.util.CivColor;
/*     */ import com.avrgaming.civcraft.util.DecimalHelper;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import org.bukkit.command.CommandSender;
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
/*     */ public class CivInfoCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  46 */     this.command = "/civ info";
/*  47 */     this.displayName = "Civ Info";
/*     */     
/*  49 */     this.commands.put("upkeep", "Shows upkeep information for this civ.");
/*  50 */     this.commands.put("taxes", "Shows tax information on towns.");
/*  51 */     this.commands.put("beakers", "Shows Civilization beaker information.");
/*  52 */     this.commands.put("online", "Lists all members of the civilization that are currently online.");
/*     */   }
/*     */   
/*     */   public void online_cmd() throws CivException {
/*  56 */     Civilization civ = getSenderCiv();
/*     */     
/*  58 */     CivMessage.sendHeading(this.sender, "Online Players In " + civ.getName());
/*  59 */     String out = "";
/*  60 */     for (Resident resident : civ.getOnlineResidents()) {
/*  61 */       out = out + resident.getName() + " ";
/*     */     }
/*  63 */     CivMessage.send(this.sender, out);
/*     */   }
/*     */   
/*     */   public void beakers_cmd() throws CivException {
/*  67 */     Civilization civ = getSenderCiv();
/*     */     
/*  69 */     CivMessage.sendHeading(this.sender, "Civ Beaker Info");
/*  70 */     ArrayList<String> out = new ArrayList();
/*     */     Iterator localIterator2;
/*  72 */     for (Iterator localIterator1 = civ.getTowns().iterator(); localIterator1.hasNext(); 
/*  73 */         localIterator2.hasNext())
/*     */     {
/*  72 */       Town t = (Town)localIterator1.next();
/*  73 */       localIterator2 = t.getBuffManager().getEffectiveBuffs("buff_innovation").iterator(); continue;Buff b = (Buff)localIterator2.next();
/*  74 */       out.add("§2From " + b.getSource() + ": " + "§a" + b.getDisplayDouble());
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*  98 */     out.add("§b------------------------------------");
/*  99 */     out.add("§2Total: §a" + this.df.format(civ.getBeakers()));
/* 100 */     CivMessage.send(this.sender, out);
/*     */   }
/*     */   
/*     */   public void taxes_cmd() throws CivException {
/* 104 */     Civilization civ = getSenderCiv();
/*     */     
/* 106 */     CivMessage.sendHeading(this.sender, "Town Tax Info");
/* 107 */     for (Town t : civ.getTowns()) {
/* 108 */       CivMessage.send(this.sender, "§2Town:§a" + t.getName() + "§2" + 
/* 109 */         " Total: " + "§a" + civ.lastTaxesPaidMap.get(t.getName()));
/*     */     }
/*     */   }
/*     */   
/*     */   private double getTownTotalLastTick(Town town, Civilization civ)
/*     */   {
/* 115 */     double total = 0.0D;
/* 116 */     for (String key : civ.lastUpkeepPaidMap.keySet()) {
/* 117 */       String townName = key.split(",")[0];
/*     */       
/* 119 */       if (townName.equalsIgnoreCase(town.getName())) {
/* 120 */         total += ((Double)civ.lastUpkeepPaidMap.get(key)).doubleValue();
/*     */       }
/*     */     }
/* 123 */     return total;
/*     */   }
/*     */   
/*     */   public void upkeep_cmd() throws CivException {
/* 127 */     Civilization civ = getSenderCiv();
/*     */     
/* 129 */     if (this.args.length < 2) {
/* 130 */       CivMessage.sendHeading(this.sender, civ.getName() + " Upkeep Info");
/*     */       
/* 132 */       for (Town town : civ.getTowns()) {
/* 133 */         CivMessage.send(this.sender, "§2Town:§a" + town.getName() + "§2" + 
/* 134 */           " Total: " + "§a" + getTownTotalLastTick(town, civ));
/*     */       }
/* 136 */       CivMessage.send(this.sender, "§2War: §a" + this.df.format(civ.getWarUpkeep()));
/*     */       
/* 138 */       CivMessage.send(this.sender, "§7Shows upkeep paid for last tick.");
/* 139 */       CivMessage.send(this.sender, "§7Use /civ info upkeep <town name> to show a breakdown per town.");
/*     */       
/* 141 */       return;
/*     */     }
/*     */     
/* 144 */     Town town = civ.getTown(this.args[1]);
/* 145 */     if (town == null) {
/* 146 */       throw new CivException("Civilization has no town named " + this.args[1]);
/*     */     }
/*     */     
/* 149 */     CivMessage.sendHeading(this.sender, "Town of " + town.getName() + "  Upkeep Details");
/* 150 */     CivMessage.send(this.sender, "§2Base: §a" + civ.getUpkeepPaid(town, "base"));
/* 151 */     CivMessage.send(this.sender, "§2Distance: §a" + civ.getUpkeepPaid(town, "distance"));
/* 152 */     CivMessage.send(this.sender, "§2DistanceUpkeep: §a" + civ.getUpkeepPaid(town, "distanceUpkeep"));
/* 153 */     CivMessage.send(this.sender, "§2Debt: §a" + civ.getUpkeepPaid(town, "debt"));
/* 154 */     CivMessage.send(this.sender, "§2Total: §a" + getTownTotalLastTick(town, civ));
/*     */     
/* 156 */     CivMessage.send(this.sender, "§7Shows upkeep paid for last tick.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doDefaultAction()
/*     */     throws CivException
/*     */   {
/* 165 */     show_info();
/* 166 */     CivMessage.send(this.sender, "§7Subcommands available: See /civ info help");
/*     */   }
/*     */   
/*     */   public static void show(CommandSender sender, Resident resident, Civilization civ)
/*     */   {
/* 171 */     boolean isOP = false;
/* 172 */     if ((sender instanceof Player)) {
/*     */       try
/*     */       {
/* 175 */         Player player = CivGlobal.getPlayer(resident);
/* 176 */         if (!player.isOp()) break label37;
/* 177 */         isOP = true;
/*     */ 
/*     */       }
/*     */       catch (CivException localCivException) {}
/*     */     }
/*     */     else
/*     */     {
/* 184 */       isOP = true;
/*     */     }
/*     */     
/*     */     label37:
/* 188 */     CivMessage.sendHeading(sender, "Civilization of " + civ.getName());
/*     */     
/* 190 */     CivMessage.send(sender, "§2Score: §a" + civ.getScore() + 
/* 191 */       "§2" + " Towns: " + "§a" + civ.getTownCount());
/* 192 */     if (civ.getLeaderGroup() == null) {
/* 193 */       CivMessage.send(sender, "§2Leaders: §cNONE");
/*     */     } else {
/* 195 */       CivMessage.send(sender, "§2Leaders: §a" + civ.getLeaderGroup().getMembersString());
/*     */     }
/*     */     
/* 198 */     if (civ.getAdviserGroup() == null) {
/* 199 */       CivMessage.send(sender, "§2Advisers: §cNONE");
/*     */     } else {
/* 201 */       CivMessage.send(sender, "§2Advisers: §a" + civ.getAdviserGroup().getMembersString());
/*     */     }
/*     */     
/* 204 */     if ((resident == null) || (civ.hasResident(resident))) {
/* 205 */       CivMessage.send(sender, "§2Income Tax Rate: §a" + civ.getIncomeTaxRateString() + 
/* 206 */         "§2" + " Science Percentage: " + "§a" + DecimalHelper.formatPercentage(civ.getSciencePercentage()));
/* 207 */       CivMessage.send(sender, "§2Beakers: §a" + civ.getBeakers() + 
/* 208 */         "§2" + " Online: " + "§a" + civ.getOnlineResidents().size());
/*     */     }
/*     */     
/* 211 */     if ((resident == null) || (civ.getLeaderGroup().hasMember(resident)) || (civ.getAdviserGroup().hasMember(resident)) || (isOP)) {
/* 212 */       CivMessage.send(sender, "§2Treasury: §a" + civ.getTreasury().getBalance() + "§2" + " coins.");
/*     */     }
/*     */     
/* 215 */     if (civ.getTreasury().inDebt()) {
/* 216 */       CivMessage.send(sender, "§eIn Debt: " + civ.getTreasury().getDebt() + " coins.");
/* 217 */       CivMessage.send(sender, "§e" + civ.getDaysLeftWarning());
/*     */     }
/*     */     
/* 220 */     for (EndGameCondition endCond : EndGameCondition.endConditions) {
/* 221 */       ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(endCond.getSessionKey());
/* 222 */       if (entries.size() != 0)
/*     */       {
/*     */ 
/*     */ 
/* 226 */         for (SessionEntry entry : entries) {
/* 227 */           if (civ == EndGameCondition.getCivFromSessionData(entry.value)) {
/* 228 */             Integer daysLeft = Integer.valueOf(endCond.getDaysToHold() - endCond.getDaysHeldFromSessionData(entry.value).intValue());
/* 229 */             CivMessage.send(sender, "§b" + CivColor.BOLD + civ.getName() + "§f" + " is " + 
/* 230 */               "§e" + CivColor.BOLD + daysLeft + "§f" + " days away from a " + "§d" + CivColor.BOLD + endCond.getVictoryName() + 
/* 231 */               "§f" + " victory!");
/* 232 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 237 */     Integer votes = EndConditionDiplomacy.getVotesFor(civ);
/* 238 */     if (votes.intValue() > 0) {
/* 239 */       CivMessage.send(sender, "§b" + CivColor.BOLD + civ.getName() + "§f" + " has " + 
/* 240 */         "§d" + CivColor.BOLD + votes + "§f" + " diplomatic votes");
/*     */     }
/*     */     
/* 243 */     Double beakers = EndConditionScience.getBeakersFor(civ);
/* 244 */     if (beakers.doubleValue() > 0.0D) {
/* 245 */       DecimalFormat df = new DecimalFormat("#.#");
/* 246 */       CivMessage.send(sender, "§b" + CivColor.BOLD + civ.getName() + "§f" + " has " + 
/* 247 */         "§d" + CivColor.BOLD + df.format(beakers) + "§f" + " beakers on The Enlightenment.");
/*     */     }
/*     */     
/* 250 */     String out = "§2Towns: ";
/* 251 */     for (Town town : civ.getTowns()) {
/* 252 */       if (town.isCapitol()) {
/* 253 */         out = out + "§6" + town.getName();
/* 254 */       } else if (town.getMotherCiv() != null) {
/* 255 */         out = out + "§e" + town.getName();
/*     */       } else {
/* 257 */         out = out + "§f" + town.getName();
/*     */       }
/* 259 */       out = out + ", ";
/*     */     }
/*     */     
/* 262 */     CivMessage.send(sender, out);
/*     */   }
/*     */   
/*     */   public void show_info() throws CivException {
/* 266 */     Civilization civ = getSenderCiv();
/* 267 */     Resident resident = getResident();
/* 268 */     show(this.sender, resident, civ);
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 273 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck()
/*     */     throws CivException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\civ\CivInfoCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
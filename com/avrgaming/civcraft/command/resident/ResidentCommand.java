/*     */ package com.avrgaming.civcraft.command.resident;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.TimeZone;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
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
/*     */ public class ResidentCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  46 */     this.command = "/resident";
/*  47 */     this.displayName = "Resident";
/*     */     
/*  49 */     this.commands.put("info", "show your resident info");
/*  50 */     this.commands.put("paydebt", "Pays off your current debt.");
/*  51 */     this.commands.put("friend", "Manage friends.");
/*  52 */     this.commands.put("toggle", "Toggles various resident specific settings.");
/*  53 */     this.commands.put("show", "[name] shows resident info for the given resident.");
/*  54 */     this.commands.put("resetspawn", "Resets your spawn point back to spawn town.");
/*  55 */     this.commands.put("exchange", "[type] [amount] - Exchanges this type(iron,gold,diamond,emerald) of ingot at 30% of its value.");
/*  56 */     this.commands.put("book", "Gives you a help book, if you don't already have one.");
/*  57 */     this.commands.put("perks", "Displays your perks.");
/*  58 */     this.commands.put("refresh", "Refreshes your perks.");
/*  59 */     this.commands.put("timezone", "(timezone) Display your current timezone or change it to (timezone)");
/*  60 */     this.commands.put("pvptimer", "Remove your PvP Timer. This is a permenant change and can not be undone.");
/*     */   }
/*     */   
/*     */   public void pvptimer_cmd() throws CivException
/*     */   {
/*  65 */     Resident resident = getResident();
/*     */     
/*  67 */     if (!resident.isProtected()) {
/*  68 */       CivMessage.sendError(this.sender, "You are not protected at this time.");
/*     */     }
/*     */     
/*  71 */     resident.setisProtected(false);
/*  72 */     CivMessage.sendSuccess(this.sender, "You are no longer protected.");
/*     */   }
/*     */   
/*     */   public void timezone_cmd() throws CivException {
/*  76 */     Resident resident = getResident();
/*     */     
/*  78 */     if (this.args.length < 2)
/*     */     {
/*  80 */       CivMessage.sendSuccess(this.sender, "Your current timezone is set to " + resident.getTimezone());
/*  81 */       return;
/*     */     }
/*     */     
/*  84 */     if (this.args[1].equalsIgnoreCase("list")) {
/*  85 */       CivMessage.sendHeading(this.sender, "Available TimeZones");
/*  86 */       String out = "";
/*  87 */       String[] arrayOfString; int j = (arrayOfString = TimeZone.getAvailableIDs()).length; for (int i = 0; i < j; i++) { String zone = arrayOfString[i];
/*  88 */         out = out + zone + ", ";
/*     */       }
/*  90 */       CivMessage.send(this.sender, out);
/*  91 */       return;
/*     */     }
/*     */     
/*  94 */     TimeZone timezone = TimeZone.getTimeZone(this.args[1]);
/*     */     
/*  96 */     if ((timezone.getID().equals("GMT")) && (!this.args[1].equalsIgnoreCase("GMT"))) {
/*  97 */       CivMessage.send(this.sender, "§7We may not have recognized your timezone \"" + this.args[1] + "\" if so, we'll set it to GMT.");
/*  98 */       CivMessage.send(this.sender, "§7Type \"/resident timezone list\" to get a list of all available timezones.");
/*     */     }
/*     */     
/* 101 */     resident.setTimezone(timezone.getID());
/* 102 */     resident.save();
/* 103 */     CivMessage.sendSuccess(this.sender, "TimeZone has been set to " + timezone.getID());
/*     */   }
/*     */   
/*     */   public void refresh_cmd() throws CivException {
/* 107 */     Resident resident = getResident();
/* 108 */     resident.perks.clear();
/* 109 */     resident.loadPerks();
/* 110 */     CivMessage.sendSuccess(this.sender, "Reloaded your perks from the website.");
/*     */   }
/*     */   
/*     */   public void perks_cmd() throws CivException {
/* 114 */     Resident resident = getResident();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 120 */     resident.showPerkPage(0);
/*     */   }
/*     */   
/*     */   public void book_cmd() throws CivException {
/* 124 */     Player player = getPlayer();
/*     */     
/*     */     ItemStack[] arrayOfItemStack;
/* 127 */     int j = (arrayOfItemStack = player.getInventory().getContents()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/* 128 */       if (stack != null)
/*     */       {
/*     */ 
/*     */ 
/* 132 */         LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/* 133 */         if (craftMat != null)
/*     */         {
/*     */ 
/*     */ 
/* 137 */           if (craftMat.getConfigId().equals("mat_tutorial_book"))
/* 138 */             throw new CivException("You already have a help book.");
/*     */         }
/*     */       }
/*     */     }
/* 142 */     LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterialFromId("mat_tutorial_book");
/* 143 */     ItemStack helpBook = LoreCraftableMaterial.spawn(craftMat);
/*     */     
/* 145 */     Object leftovers = player.getInventory().addItem(new ItemStack[] { helpBook });
/* 146 */     if ((leftovers != null) && (((HashMap)leftovers).size() >= 1)) {
/* 147 */       throw new CivException("You cannot hold anything else. Get some space open in your inventory first.");
/*     */     }
/*     */     
/* 150 */     CivMessage.sendSuccess(player, "Added a help book to your inventory!");
/*     */   }
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
/*     */   public void exchange_cmd()
/*     */     throws CivException
/*     */   {
/* 188 */     Player player = getPlayer();
/* 189 */     Resident resident = getResident();
/* 190 */     String type = getNamedString(1, "Enter a type. E.g. iron, gold, diamond, emerald.");
/* 191 */     Integer amount = getNamedInteger(2);
/*     */     
/* 193 */     if (amount.intValue() <= 0) {
/* 194 */       throw new CivException("You must exchange a positive, non-zero amount.");
/*     */     }
/*     */     
/* 197 */     type = type.toLowerCase();
/*     */     
/*     */     String str1;
/*     */     double rate;
/* 201 */     switch ((str1 = type).hashCode()) {case -1634062812:  if (str1.equals("emerald")) {} break; case 3178592:  if (str1.equals("gold")) {} break; case 3241160:  if (str1.equals("iron")) break; break; case 1655054676:  if (!str1.equals("diamond")) {
/*     */         break label208;
/* 203 */         int exchangeID = 265;
/* 204 */         double rate = CivSettings.iron_rate;
/*     */         
/*     */         break label239;
/* 207 */         int exchangeID = 266;
/* 208 */         double rate = CivSettings.gold_rate;
/*     */         break label239;
/*     */       } else {
/* 211 */         int exchangeID = 264;
/* 212 */         double rate = CivSettings.diamond_rate;
/*     */         
/*     */         break label239;
/* 215 */         int exchangeID = 388;
/* 216 */         rate = CivSettings.emerald_rate; }
/* 217 */       break; }
/*     */     label208:
/* 219 */     throw new CivException("Unknown exchange type " + type + " must be iron, gold, diamond, or emerald.");
/*     */     label239:
/*     */     double rate;
/*     */     int exchangeID;
/*     */     try {
/* 224 */       exchangeRate = CivSettings.getDouble(CivSettings.civConfig, "global.exchange_rate");
/*     */     } catch (InvalidConfiguration e) { double exchangeRate;
/* 226 */       e.printStackTrace();
/* 227 */       throw new CivException("Internal configuration error!");
/*     */     }
/*     */     double exchangeRate;
/* 230 */     ItemStack stack = ItemManager.createItemStack(exchangeID, 1);
/* 231 */     int total = 0;
/* 232 */     for (ItemStack is : player.getInventory().all(ItemManager.getMaterial(exchangeID)).values()) {
/* 233 */       total += is.getAmount();
/*     */     }
/*     */     
/* 236 */     if (total == 0) {
/* 237 */       throw new CivException("You do not have any " + type);
/*     */     }
/*     */     
/* 240 */     if (amount.intValue() > total) {
/* 241 */       amount = Integer.valueOf(total);
/*     */     }
/*     */     
/* 244 */     stack.setAmount(amount.intValue());
/* 245 */     player.getInventory().removeItem(new ItemStack[] { stack });
/* 246 */     double coins = amount.intValue() * rate * exchangeRate;
/*     */     
/* 248 */     resident.getTreasury().deposit(coins);
/* 249 */     CivMessage.sendSuccess(player, "Exchanged " + amount + " " + type + " for " + coins + " coins.");
/*     */   }
/*     */   
/*     */   public void resetspawn_cmd() throws CivException
/*     */   {
/* 254 */     Player player = getPlayer();
/* 255 */     Location spawn = player.getWorld().getSpawnLocation();
/* 256 */     player.setBedSpawnLocation(spawn, true);
/* 257 */     CivMessage.sendSuccess(player, "You will now respawn at spawn.");
/*     */   }
/*     */   
/*     */   public void show_cmd() throws CivException {
/* 261 */     if (this.args.length < 2) {
/* 262 */       throw new CivException("Please enter the resident's name you wish to know about.");
/*     */     }
/*     */     
/* 265 */     Resident resident = getNamedResident(1);
/*     */     
/* 267 */     show(this.sender, resident);
/*     */   }
/*     */   
/*     */   public void toggle_cmd() throws CivException {
/* 271 */     ResidentToggleCommand cmd = new ResidentToggleCommand();
/* 272 */     cmd.onCommand(this.sender, null, "friend", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void friend_cmd() {
/* 276 */     ResidentFriendCommand cmd = new ResidentFriendCommand();
/* 277 */     cmd.onCommand(this.sender, null, "friend", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void paydebt_cmd() throws CivException {
/* 281 */     Resident resident = getResident();
/*     */     
/* 283 */     if (!resident.getTreasury().hasEnough(resident.getTreasury().getDebt())) {
/* 284 */       throw new CivException("You do not have the required " + resident.getTreasury().getDebt() + " coins to pay off your debt.");
/*     */     }
/*     */     
/*     */ 
/* 288 */     CivMessage.sendSuccess(this.sender, "Paid " + resident.getTreasury().getDebt() + " coins of debt.");
/* 289 */     resident.payOffDebt();
/*     */   }
/*     */   
/*     */   public void info_cmd() throws CivException {
/* 293 */     Resident resident = getResident();
/* 294 */     show(this.sender, resident);
/*     */   }
/*     */   
/*     */   public static void show(CommandSender sender, Resident resident) {
/* 298 */     CivMessage.sendHeading(sender, "Resident " + resident.getName());
/* 299 */     Date lastOnline = new Date(resident.getLastOnline());
/* 300 */     SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yy h:mm:ss a z");
/* 301 */     CivMessage.send(sender, "§2Last Online:§a" + sdf.format(lastOnline));
/* 302 */     CivMessage.send(sender, "§2Town: §a" + resident.getTownString());
/* 303 */     CivMessage.send(sender, "§2Camp: §a" + resident.getCampString());
/*     */     
/* 305 */     if ((sender.getName().equalsIgnoreCase(resident.getName())) || (sender.isOp())) {
/* 306 */       CivMessage.send(sender, "§2Personal Treasury: §a" + resident.getTreasury().getBalance() + " " + 
/* 307 */         "§2" + "Taxes Owed: " + "§a" + (resident.getPropertyTaxOwed() + resident.getFlatTaxOwed()));
/* 308 */       if (resident.hasTown()) {
/* 309 */         if (resident.getSelectedTown() != null) {
/* 310 */           CivMessage.send(sender, "§2Selected Town: §a" + resident.getSelectedTown().getName());
/*     */         } else {
/* 312 */           CivMessage.send(sender, "§2Selected Town: §a" + resident.getTown().getName());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 317 */     if (resident.getTreasury().inDebt()) {
/* 318 */       CivMessage.send(resident, "§eIn Debt " + resident.getTreasury().getDebt() + " coins!");
/*     */     }
/*     */     
/* 321 */     if (resident.getDaysTilEvict() > 0) {
/* 322 */       CivMessage.send(resident, "§eEviction in " + resident.getDaysTilEvict() + " days.");
/*     */     }
/*     */     
/* 325 */     CivMessage.send(sender, "§2Groups: " + resident.getGroupsString());
/*     */     try
/*     */     {
/* 328 */       if (resident.isUsesAntiCheat()) {
/* 329 */         CivMessage.send(sender, "§aOnline and currently using CivCraft's Anti-Cheat");
/*     */       } else {
/* 331 */         CivMessage.send(sender, "§cOnline but NOT validated by CivCraft's Anti-Cheat");
/*     */       }
/*     */     } catch (CivException e) {
/* 334 */       CivMessage.send(sender, "§7Resident is not currently online.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void doDefaultAction() throws CivException
/*     */   {
/* 340 */     showHelp();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void showHelp()
/*     */   {
/* 347 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck()
/*     */     throws CivException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\resident\ResidentCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
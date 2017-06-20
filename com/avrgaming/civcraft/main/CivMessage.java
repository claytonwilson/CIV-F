/*     */ package com.avrgaming.civcraft.main;
/*     */ 
/*     */ import com.avrgaming.civcraft.arena.Arena;
/*     */ import com.avrgaming.civcraft.arena.ArenaTeam;
/*     */ import com.avrgaming.civcraft.camp.Camp;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.util.CivColor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Sound;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CivMessage
/*     */ {
/*  44 */   private static HashMap<String, Integer> lastMessageHashCode = new HashMap();
/*     */   
/*     */ 
/*  47 */   private static Map<String, ArrayList<String>> extraTownChatListeners = new ConcurrentHashMap();
/*     */   
/*     */ 
/*  50 */   private static Map<String, ArrayList<String>> extraCivChatListeners = new ConcurrentHashMap();
/*     */   
/*     */   public static void sendErrorNoRepeat(Object sender, String line) {
/*  53 */     if ((sender instanceof Player)) {
/*  54 */       Player player = (Player)sender;
/*     */       
/*  56 */       Integer hashcode = (Integer)lastMessageHashCode.get(player.getName());
/*  57 */       if ((hashcode != null) && (hashcode.intValue() == line.hashCode())) {
/*  58 */         return;
/*     */       }
/*     */       
/*  61 */       lastMessageHashCode.put(player.getName(), Integer.valueOf(line.hashCode()));
/*     */     }
/*     */     
/*  64 */     send(sender, "§c" + line);
/*     */   }
/*     */   
/*     */   public static void sendError(Object sender, String line) {
/*  68 */     send(sender, "§c" + line);
/*     */   }
/*     */   
/*     */ 
/*     */   public static void console(String playerName, String line)
/*     */   {
/*     */     try
/*     */     {
/*  76 */       Player player = CivGlobal.getPlayer(playerName);
/*  77 */       send(player, line);
/*     */     }
/*     */     catch (CivException localCivException) {}
/*  80 */     CivLog.info(line);
/*     */   }
/*     */   
/*     */   public static void send(Object sender, String line) {
/*  84 */     if ((sender instanceof Player)) {
/*  85 */       ((Player)sender).sendMessage(line);
/*  86 */     } else if ((sender instanceof CommandSender)) {
/*  87 */       ((CommandSender)sender).sendMessage(line);
/*     */     }
/*  89 */     else if ((sender instanceof Resident)) {
/*     */       try {
/*  91 */         CivGlobal.getPlayer((Resident)sender).sendMessage(line);
/*     */       }
/*     */       catch (CivException localCivException) {}
/*     */     }
/*     */   }
/*     */   
/*     */   public static void send(Object sender, String[] lines) {
/*  98 */     boolean isPlayer = false;
/*  99 */     if ((sender instanceof Player))
/* 100 */       isPlayer = true;
/*     */     String[] arrayOfString;
/* 102 */     int j = (arrayOfString = lines).length; for (int i = 0; i < j; i++) { String line = arrayOfString[i];
/* 103 */       if (isPlayer) {
/* 104 */         ((Player)sender).sendMessage(line);
/*     */       } else {
/* 106 */         ((CommandSender)sender).sendMessage(line);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static String buildTitle(String title) {
/* 112 */     String line = "-------------------------------------------------";
/* 113 */     String titleBracket = "[ §e" + title + "§b" + " ]";
/*     */     
/* 115 */     if (titleBracket.length() > line.length()) {
/* 116 */       return "§b-" + titleBracket + "-";
/*     */     }
/*     */     
/* 119 */     int min = line.length() / 2 - titleBracket.length() / 2;
/* 120 */     int max = line.length() / 2 + titleBracket.length() / 2;
/*     */     
/* 122 */     String out = "§b" + line.substring(0, Math.max(0, min));
/* 123 */     out = out + titleBracket + line.substring(max);
/*     */     
/* 125 */     return out;
/*     */   }
/*     */   
/*     */   public static String buildSmallTitle(String title) {
/* 129 */     String line = "§b------------------------------";
/*     */     
/* 131 */     String titleBracket = "[ " + title + " ]";
/*     */     
/* 133 */     int min = line.length() / 2 - titleBracket.length() / 2;
/* 134 */     int max = line.length() / 2 + titleBracket.length() / 2;
/*     */     
/* 136 */     String out = "§b" + line.substring(0, Math.max(0, min));
/* 137 */     out = out + titleBracket + line.substring(max);
/*     */     
/* 139 */     return out;
/*     */   }
/*     */   
/*     */   public static void sendSubHeading(CommandSender sender, String title) {
/* 143 */     send(sender, buildSmallTitle(title));
/*     */   }
/*     */   
/*     */   public static void sendHeading(Resident resident, String title)
/*     */   {
/*     */     try {
/* 149 */       Player player = CivGlobal.getPlayer(resident);
/* 150 */       sendHeading(player, title);
/*     */     }
/*     */     catch (CivException localCivException) {}
/*     */   }
/*     */   
/*     */   public static void sendHeading(CommandSender sender, String title) {
/* 156 */     send(sender, buildTitle(title));
/*     */   }
/*     */   
/*     */   public static void sendSuccess(CommandSender sender, String message) {
/* 160 */     send(sender, "§a" + message);
/*     */   }
/*     */   
/*     */   public static void global(String string) {
/* 164 */     CivLog.info("[Global] " + string);
/* 165 */     Player[] arrayOfPlayer; int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length; for (int i = 0; i < j; i++) { Player player = arrayOfPlayer[i];
/* 166 */       player.sendMessage("§b[Global] §f" + string);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void globalHeading(String string) {
/* 171 */     CivLog.info("[GlobalHeading] " + string);
/* 172 */     Player[] arrayOfPlayer; int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length; for (int i = 0; i < j; i++) { Player player = arrayOfPlayer[i];
/* 173 */       send(player, buildTitle(string));
/*     */     }
/*     */   }
/*     */   
/*     */   public static void sendScout(Civilization civ, String string) {
/* 178 */     CivLog.info("[Scout:" + civ.getName() + "] " + string);
/* 179 */     Iterator localIterator2; for (Iterator localIterator1 = civ.getTowns().iterator(); localIterator1.hasNext(); 
/* 180 */         localIterator2.hasNext())
/*     */     {
/* 179 */       Town t = (Town)localIterator1.next();
/* 180 */       localIterator2 = t.getResidents().iterator(); continue;Resident resident = (Resident)localIterator2.next();
/* 181 */       if (resident.isShowScout())
/*     */       {
/*     */ 
/*     */         try
/*     */         {
/*     */ 
/* 187 */           Player player = CivGlobal.getPlayer(resident);
/* 188 */           if (player != null) {
/* 189 */             send(player, "§5[Scout] §f" + string);
/*     */           }
/*     */         }
/*     */         catch (CivException localCivException) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void sendTown(Town town, String string)
/*     */   {
/* 199 */     CivLog.info("[Town:" + town.getName() + "] " + string);
/*     */     
/* 201 */     for (Resident resident : town.getResidents()) {
/* 202 */       if (resident.isShowTown())
/*     */       {
/*     */ 
/*     */         try
/*     */         {
/*     */ 
/* 208 */           Player player = CivGlobal.getPlayer(resident);
/* 209 */           if (player != null) {
/* 210 */             send(player, "§6[Town] §f" + string);
/*     */           }
/*     */         } catch (CivException localCivException) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void sendCiv(Civilization civ, String string) {
/* 218 */     CivLog.info("[Civ:" + civ.getName() + "] " + string);
/* 219 */     Iterator localIterator2; for (Iterator localIterator1 = civ.getTowns().iterator(); localIterator1.hasNext(); 
/* 220 */         localIterator2.hasNext())
/*     */     {
/* 219 */       Town t = (Town)localIterator1.next();
/* 220 */       localIterator2 = t.getResidents().iterator(); continue;Resident resident = (Resident)localIterator2.next();
/* 221 */       if (resident.isShowCiv())
/*     */       {
/*     */ 
/*     */         try
/*     */         {
/*     */ 
/* 227 */           Player player = CivGlobal.getPlayer(resident);
/* 228 */           if (player != null) {
/* 229 */             send(player, "§d[Civ] §f" + string);
/*     */           }
/*     */         }
/*     */         catch (CivException localCivException) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static void send(CommandSender sender, List<String> outs)
/*     */   {
/* 240 */     for (String str : outs) {
/* 241 */       send(sender, str);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void sendTownChat(Town town, Resident resident, String format, String message)
/*     */   {
/* 247 */     if (town == null) {
/*     */       try {
/* 249 */         Player player = CivGlobal.getPlayer(resident);
/* 250 */         player.sendMessage("§cYou are not part of a town, nobody hears you. Type /tc to chat normally.");
/*     */       }
/*     */       catch (CivException localCivException) {}
/*     */       
/* 254 */       return;
/*     */     }
/*     */     
/* 257 */     CivLog.info("[TC:" + town.getName() + "] " + resident.getName() + ": " + message);
/*     */     
/* 259 */     for (Resident r : town.getResidents()) {
/*     */       try {
/* 261 */         Player player = CivGlobal.getPlayer(r);
/* 262 */         String msg = "§b[TC]§f" + String.format(format, new Object[] { resident.getName(), message });
/* 263 */         player.sendMessage(msg);
/*     */       }
/*     */       catch (CivException localCivException1) {}
/*     */     }
/*     */     
/*     */ 
/* 269 */     for (String name : getExtraTownChatListeners(town)) {
/*     */       try {
/* 271 */         Player player = CivGlobal.getPlayer(name);
/* 272 */         String msg = "§b[TC:" + town.getName() + "]" + "§f" + String.format(format, new Object[] { resident.getName(), message });
/* 273 */         player.sendMessage(msg);
/*     */       }
/*     */       catch (CivException localCivException2) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static void sendCivChat(Civilization civ, Resident resident, String format, String message)
/*     */   {
/* 282 */     if (civ == null) {
/*     */       try {
/* 284 */         Player player = CivGlobal.getPlayer(resident);
/* 285 */         player.sendMessage("§cYou are not part of a civ, nobody hears you. Type /cc to chat normally.");
/*     */       }
/*     */       catch (CivException localCivException) {}
/*     */       
/* 289 */       return;
/*     */     }
/*     */     
/* 292 */     String townName = "";
/* 293 */     if (resident.getTown() != null) {
/* 294 */       townName = resident.getTown().getName();
/*     */     }
/*     */     Iterator localIterator2;
/* 297 */     for (Iterator localIterator1 = civ.getTowns().iterator(); localIterator1.hasNext(); 
/* 298 */         localIterator2.hasNext())
/*     */     {
/* 297 */       Town t = (Town)localIterator1.next();
/* 298 */       localIterator2 = t.getResidents().iterator(); continue;Resident r = (Resident)localIterator2.next();
/*     */       try {
/* 300 */         Player player = CivGlobal.getPlayer(r);
/*     */         
/*     */ 
/* 303 */         String msg = "§6[CC " + townName + "]" + "§f" + String.format(format, new Object[] { resident.getName(), message });
/* 304 */         player.sendMessage(msg);
/*     */       }
/*     */       catch (CivException localCivException1) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 311 */     for (String name : getExtraCivChatListeners(civ)) {
/*     */       try {
/* 313 */         Player player = CivGlobal.getPlayer(name);
/* 314 */         String msg = "§6[CC:" + civ.getName() + " " + townName + "]" + "§f" + String.format(format, new Object[] { resident.getName(), message });
/* 315 */         player.sendMessage(msg);
/*     */       }
/*     */       catch (CivException localCivException2) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static void sendChat(Resident resident, String format, String message)
/*     */   {
/*     */     Player[] arrayOfPlayer;
/* 325 */     int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length; for (int i = 0; i < j; i++) { Player player = arrayOfPlayer[i];
/* 326 */       String msg = String.format(format, new Object[] { resident.getName(), message });
/* 327 */       player.sendMessage(msg);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void addExtraTownChatListener(Town town, String name)
/*     */   {
/* 333 */     ArrayList<String> names = (ArrayList)extraTownChatListeners.get(town.getName().toLowerCase());
/* 334 */     if (names == null) {
/* 335 */       names = new ArrayList();
/*     */     }
/*     */     
/* 338 */     for (String str : names) {
/* 339 */       if (str.equals(name)) {
/* 340 */         return;
/*     */       }
/*     */     }
/*     */     
/* 344 */     names.add(name);
/* 345 */     extraTownChatListeners.put(town.getName().toLowerCase(), names);
/*     */   }
/*     */   
/*     */   public static void removeExtraTownChatListener(Town town, String name) {
/* 349 */     ArrayList<String> names = (ArrayList)extraTownChatListeners.get(town.getName().toLowerCase());
/* 350 */     if (names == null) {
/* 351 */       return;
/*     */     }
/*     */     
/* 354 */     for (String str : names) {
/* 355 */       if (str.equals(name)) {
/* 356 */         names.remove(str);
/* 357 */         break;
/*     */       }
/*     */     }
/*     */     
/* 361 */     extraTownChatListeners.put(town.getName().toLowerCase(), names);
/*     */   }
/*     */   
/*     */   public static ArrayList<String> getExtraTownChatListeners(Town town) {
/* 365 */     ArrayList<String> names = (ArrayList)extraTownChatListeners.get(town.getName().toLowerCase());
/* 366 */     if (names == null) {
/* 367 */       return new ArrayList();
/*     */     }
/* 369 */     return names;
/*     */   }
/*     */   
/*     */   public static void addExtraCivChatListener(Civilization civ, String name)
/*     */   {
/* 374 */     ArrayList<String> names = (ArrayList)extraCivChatListeners.get(civ.getName().toLowerCase());
/* 375 */     if (names == null) {
/* 376 */       names = new ArrayList();
/*     */     }
/*     */     
/* 379 */     for (String str : names) {
/* 380 */       if (str.equals(name)) {
/* 381 */         return;
/*     */       }
/*     */     }
/*     */     
/* 385 */     names.add(name);
/*     */     
/* 387 */     extraCivChatListeners.put(civ.getName().toLowerCase(), names);
/*     */   }
/*     */   
/*     */   public static void removeExtraCivChatListener(Civilization civ, String name) {
/* 391 */     ArrayList<String> names = (ArrayList)extraCivChatListeners.get(civ.getName().toLowerCase());
/* 392 */     if (names == null) {
/* 393 */       return;
/*     */     }
/*     */     
/* 396 */     for (String str : names) {
/* 397 */       if (str.equals(name)) {
/* 398 */         names.remove(str);
/* 399 */         break;
/*     */       }
/*     */     }
/*     */     
/* 403 */     extraCivChatListeners.put(civ.getName().toLowerCase(), names);
/*     */   }
/*     */   
/*     */   public static ArrayList<String> getExtraCivChatListeners(Civilization civ) {
/* 407 */     ArrayList<String> names = (ArrayList)extraCivChatListeners.get(civ.getName().toLowerCase());
/* 408 */     if (names == null) {
/* 409 */       return new ArrayList();
/*     */     }
/* 411 */     return names;
/*     */   }
/*     */   
/*     */   public static void sendTownSound(Town town, Sound sound, float f, float g) {
/* 415 */     for (Resident resident : town.getResidents()) {
/*     */       try
/*     */       {
/* 418 */         Player player = CivGlobal.getPlayer(resident);
/*     */         
/* 420 */         player.playSound(player.getLocation(), sound, f, g);
/*     */       }
/*     */       catch (CivException localCivException) {}
/*     */     }
/*     */   }
/*     */   
/*     */   public static void sendAll(String str)
/*     */   {
/*     */     Player[] arrayOfPlayer;
/* 429 */     int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length; for (int i = 0; i < j; i++) { Player player = arrayOfPlayer[i];
/* 430 */       player.sendMessage(str);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void sendCamp(Camp camp, String message) {
/* 435 */     for (Resident resident : camp.getMembers()) {
/*     */       try {
/* 437 */         Player player = CivGlobal.getPlayer(resident);
/* 438 */         player.sendMessage("§e[Camp] §e" + message);
/* 439 */         CivLog.info("[Camp:" + camp.getName() + "] " + message);
/*     */       }
/*     */       catch (CivException localCivException) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static void sendTownHeading(Town town, String string)
/*     */   {
/* 448 */     CivLog.info("[Town:" + town.getName() + "] " + string);
/* 449 */     for (Resident resident : town.getResidents()) {
/* 450 */       if (resident.isShowTown())
/*     */       {
/*     */ 
/*     */         try
/*     */         {
/*     */ 
/* 456 */           Player player = CivGlobal.getPlayer(resident);
/* 457 */           if (player != null) {
/* 458 */             sendHeading(player, string);
/*     */           }
/*     */         } catch (CivException localCivException) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void sendSuccess(Resident resident, String message) {
/*     */     try {
/* 467 */       Player player = CivGlobal.getPlayer(resident);
/* 468 */       sendSuccess(player, message);
/*     */     }
/*     */     catch (CivException e) {}
/*     */   }
/*     */   
/*     */   public static void sendTeam(ArenaTeam team, String message)
/*     */   {
/* 475 */     for (Resident resident : team.teamMembers) {
/* 476 */       send(resident, "§3[Team (" + team.getName() + ")] " + CivColor.RESET + message);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void sendTeamHeading(ArenaTeam team, String message) {
/* 481 */     for (Resident resident : team.teamMembers) {
/* 482 */       sendHeading(resident, message);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void sendArena(Arena arena, String message) {
/* 487 */     CivLog.info("[Arena] " + message);
/* 488 */     Iterator localIterator2; for (Iterator localIterator1 = arena.getTeams().iterator(); localIterator1.hasNext(); 
/* 489 */         localIterator2.hasNext())
/*     */     {
/* 488 */       ArenaTeam team = (ArenaTeam)localIterator1.next();
/* 489 */       localIterator2 = team.teamMembers.iterator(); continue;Resident resident = (Resident)localIterator2.next();
/* 490 */       send(resident, "§b[Arena] " + CivColor.RESET + message);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\main\CivMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
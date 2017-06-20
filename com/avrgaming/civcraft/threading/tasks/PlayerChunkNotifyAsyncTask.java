/*     */ package com.avrgaming.civcraft.threading.tasks;
/*     */ 
/*     */ import com.avrgaming.civcraft.camp.Camp;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.BuffManager;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.CultureChunk;
/*     */ import com.avrgaming.civcraft.object.DiplomacyManager;
/*     */ import com.avrgaming.civcraft.object.Relation.Status;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.object.TownChunk;
/*     */ import com.avrgaming.civcraft.util.AsciiMap;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
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
/*     */ public class PlayerChunkNotifyAsyncTask
/*     */   implements Runnable
/*     */ {
/*     */   Location from;
/*     */   Location to;
/*     */   String playerName;
/*  47 */   public static int BORDER_SPAM_TIMEOUT = 30000;
/*  48 */   public static HashMap<String, Date> cultureEnterTimes = new HashMap();
/*     */   
/*     */   public PlayerChunkNotifyAsyncTask(Location from, Location to, String playerName) {
/*  51 */     this.from = from;
/*  52 */     this.to = to;
/*  53 */     this.playerName = playerName;
/*     */   }
/*     */   
/*     */   public static String getNotifyColor(CultureChunk toCc, Relation.Status status, Player player)
/*     */   {
/*  58 */     String color = "§f";
/*  59 */     switch (status) {
/*     */     case ALLY: 
/*  61 */       if (toCc.getTown().isOutlaw(player.getName())) {
/*  62 */         color = "§e";
/*     */       }
/*     */       
/*  65 */       break;
/*     */     case HOSTILE: 
/*  67 */       color = "§e";
/*  68 */       break;
/*     */     case NEUTRAL: 
/*  70 */       color = "§c";
/*     */       
/*  72 */       break;
/*     */     case PEACE: 
/*  74 */       color = "§b";
/*     */       
/*  76 */       break;
/*     */     case WAR: 
/*  78 */       color = "§2";
/*     */     }
/*     */     
/*  81 */     return color;
/*     */   }
/*     */   
/*     */   private String getToWildMessage() {
/*  85 */     return "§7Entering Wilderness §c[PvP]";
/*     */   }
/*     */   
/*     */   private String getToTownMessage(Town town, TownChunk tc)
/*     */   {
/*     */     try {
/*  91 */       player = CivGlobal.getPlayer(this.playerName);
/*     */     } catch (CivException e) { Player player;
/*  93 */       return "";
/*     */     }
/*     */     Player player;
/*  96 */     if (town.getBuffManager().hasBuff("buff_hanging_gardens_regen")) {
/*  97 */       Resident resident = CivGlobal.getResident(player);
/*  98 */       if ((resident != null) && (resident.getTown() == town)) {
/*  99 */         CivMessage.send(player, "§2" + ChatColor.ITALIC + "You feel invigorated by the glorious hanging gardens.");
/*     */       }
/*     */     }
/*     */     
/* 103 */     if (!tc.isOutpost()) {
/* 104 */       return "§7Entering §f" + town.getName() + " " + town.getPvpString() + " ";
/*     */     }
/* 106 */     return "§7Entering Outpost of §f" + town.getName() + " " + town.getPvpString() + " ";
/*     */   }
/*     */   
/*     */ 
/*     */   private void showPlotMoveMessage()
/*     */   {
/* 112 */     TownChunk fromTc = CivGlobal.getTownChunk(this.from);
/* 113 */     TownChunk toTc = CivGlobal.getTownChunk(this.to);
/* 114 */     CultureChunk fromCc = CivGlobal.getCultureChunk(this.from);
/* 115 */     CultureChunk toCc = CivGlobal.getCultureChunk(this.to);
/* 116 */     Camp toCamp = CivGlobal.getCampFromChunk(new ChunkCoord(this.to));
/* 117 */     Camp fromCamp = CivGlobal.getCampFromChunk(new ChunkCoord(this.from));
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 122 */       Player player = CivGlobal.getPlayer(this.playerName);
/* 123 */       resident = CivGlobal.getResident(this.playerName);
/*     */     } catch (CivException e) { Resident resident;
/*     */       return; }
/*     */     Resident resident;
/*     */     Player player;
/* 128 */     String out = "";
/*     */     
/*     */ 
/* 131 */     if ((toCamp != null) && (toCamp != fromCamp)) {
/* 132 */       out = out + "§6Camp " + toCamp.getName() + " " + "§c" + "[PvP]";
/*     */     }
/*     */     
/* 135 */     if ((toCamp == null) && (fromCamp != null)) {
/* 136 */       out = out + getToWildMessage();
/*     */     }
/*     */     
/*     */ 
/* 140 */     if ((fromTc == null) && (toTc != null))
/*     */     {
/* 142 */       out = out + getToTownMessage(toTc.getTown(), toTc);
/*     */     }
/*     */     
/*     */ 
/* 146 */     if ((fromTc != null) && (toTc == null)) {
/* 147 */       out = out + getToWildMessage();
/*     */     }
/*     */     
/*     */ 
/* 151 */     if ((fromTc != null) && (toTc != null) && (fromTc.getTown() != toTc.getTown())) {
/* 152 */       out = out + getToTownMessage(toTc.getTown(), toTc);
/*     */     }
/*     */     
/* 155 */     if (toTc != null) {
/* 156 */       out = out + toTc.getOnEnterString(player, fromTc);
/*     */     }
/*     */     
/*     */ 
/* 160 */     if ((fromCc != null) && (toCc == null)) {
/* 161 */       out = out + fromCc.getOnLeaveString();
/*     */     }
/*     */     
/*     */ 
/* 165 */     if ((fromCc == null) && (toCc != null)) {
/* 166 */       out = out + toCc.getOnEnterString();
/* 167 */       onCultureEnter(toCc);
/*     */     }
/*     */     
/*     */ 
/* 171 */     if ((fromCc != null) && (toCc != null) && (fromCc.getCiv() != toCc.getCiv())) {
/* 172 */       out = out + fromCc.getOnLeaveString() + " | " + toCc.getOnEnterString();
/* 173 */       onCultureEnter(toCc);
/*     */     }
/*     */     
/* 176 */     if (!out.equals(""))
/*     */     {
/*     */ 
/*     */ 
/* 180 */       CivMessage.send(player, out);
/*     */     }
/*     */     
/* 183 */     if (resident.isShowInfo()) {
/* 184 */       CultureChunk.showInfo(player);
/*     */     }
/*     */   }
/*     */   
/*     */   private void onCultureEnter(CultureChunk toCc)
/*     */   {
/*     */     try
/*     */     {
/* 192 */       player = CivGlobal.getPlayer(this.playerName);
/*     */     } catch (CivException e) { Player player;
/*     */       return;
/*     */     }
/*     */     Player player;
/* 197 */     Relation.Status status = toCc.getCiv().getDiplomacyManager().getRelationStatus(player);
/* 198 */     String color = getNotifyColor(toCc, status, player);
/* 199 */     String relationName = status.name();
/*     */     
/* 201 */     if (player.isOp()) {
/* 202 */       return;
/*     */     }
/*     */     
/* 205 */     Resident resident = CivGlobal.getResident(player);
/* 206 */     if ((resident != null) && (resident.hasTown()) && (resident.getCiv() == toCc.getCiv())) {
/* 207 */       return;
/*     */     }
/*     */     
/*     */ 
/* 211 */     String borderSpamKey = player.getName() + ":" + toCc.getCiv().getName();
/* 212 */     Date lastMessageTime = (Date)cultureEnterTimes.get(borderSpamKey);
/*     */     
/* 214 */     Date now = new Date();
/* 215 */     if ((lastMessageTime != null) && (now.getTime() < lastMessageTime.getTime() + BORDER_SPAM_TIMEOUT))
/*     */     {
/* 217 */       return;
/*     */     }
/* 219 */     lastMessageTime = now;
/*     */     
/* 221 */     cultureEnterTimes.put(borderSpamKey, lastMessageTime);
/* 222 */     CivMessage.sendCiv(toCc.getCiv(), color + player.getDisplayName() + "(" + relationName + ") has entered our borders.");
/*     */   }
/*     */   
/*     */ 
/*     */   public void run()
/*     */   {
/* 228 */     showPlotMoveMessage();
/* 229 */     showResidentMap();
/*     */   }
/*     */   
/*     */   private void showResidentMap()
/*     */   {
/*     */     try
/*     */     {
/* 236 */       player = CivGlobal.getPlayer(this.playerName);
/*     */     } catch (CivException e) { Player player;
/*     */       return;
/*     */     }
/*     */     Player player;
/* 241 */     Resident resident = CivGlobal.getResident(player);
/* 242 */     if (resident == null) {
/* 243 */       return;
/*     */     }
/*     */     
/* 246 */     if (resident.isShowMap()) {
/* 247 */       CivMessage.send(player, AsciiMap.getMapAsString(player.getLocation()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\PlayerChunkNotifyAsyncTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
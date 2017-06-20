/*     */ package com.avrgaming.civcraft.war;
/*     */ 
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.util.CivColor;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WarStats
/*     */ {
/*  18 */   private static HashMap<String, Integer> playerKills = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  24 */   private static HashMap<String, LinkedList<String>> conqueredCivs = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  29 */   private static HashMap<String, LinkedList<String>> conqueredTowns = new HashMap();
/*     */   
/*     */   public static void incrementPlayerKills(String playerName) {
/*  32 */     Integer kills = (Integer)playerKills.get(playerName);
/*  33 */     if (kills == null) {
/*  34 */       kills = Integer.valueOf(1);
/*     */     } else {
/*  36 */       kills = Integer.valueOf(kills.intValue() + 1);
/*     */     }
/*     */     
/*  39 */     playerKills.put(playerName, kills);
/*     */   }
/*     */   
/*     */   public static void logCapturedTown(Civilization winner, Town captured) {
/*  43 */     LinkedList<String> towns = (LinkedList)conqueredTowns.get(winner.getName());
/*     */     
/*  45 */     if (towns == null) {
/*  46 */       towns = new LinkedList();
/*     */     }
/*     */     
/*  49 */     towns.add(captured.getName());
/*  50 */     conqueredTowns.put(winner.getName(), towns);
/*     */   }
/*     */   
/*     */   public static void logCapturedCiv(Civilization winner, Civilization captured) {
/*  54 */     LinkedList<String> civs = (LinkedList)conqueredCivs.get(winner.getName());
/*     */     
/*  56 */     if (civs == null) {
/*  57 */       civs = new LinkedList();
/*     */     }
/*     */     
/*  60 */     civs.add(captured.getName());
/*  61 */     conqueredCivs.put(winner.getName(), civs);
/*     */   }
/*     */   
/*     */   public static String getTopKiller()
/*     */   {
/*  66 */     String out = "";
/*  67 */     int mostKills = 0;
/*  68 */     for (String playerName : playerKills.keySet()) {
/*  69 */       int kills = ((Integer)playerKills.get(playerName)).intValue();
/*  70 */       if (kills > mostKills) {
/*  71 */         out = playerName;
/*  72 */         mostKills = kills;
/*     */       }
/*     */     }
/*     */     
/*  76 */     return "§a" + CivColor.BOLD + out + "§7" + " (" + mostKills + " kills)";
/*     */   }
/*     */   
/*     */   public static List<String> getCapturedCivs() {
/*  80 */     LinkedList<String> out = new LinkedList();
/*     */     
/*  82 */     for (String key : conqueredCivs.keySet()) {
/*  83 */       LinkedList<String> conquered = (LinkedList)conqueredCivs.get(key);
/*  84 */       if (conquered != null)
/*     */       {
/*     */ 
/*     */ 
/*  88 */         String line = "§a" + CivColor.BOLD + key + "§c" + CivColor.BOLD + " Conquered: " + CivColor.RESET + "§7";
/*  89 */         String tmp = "";
/*  90 */         for (String str : conquered) {
/*  91 */           tmp = tmp + str + ", ";
/*     */         }
/*     */         
/*  94 */         line = line + tmp;
/*  95 */         out.add(line);
/*     */       }
/*     */     }
/*  98 */     return out;
/*     */   }
/*     */   
/*     */   public static void clearStats() {
/* 102 */     playerKills.clear();
/* 103 */     conqueredCivs.clear();
/* 104 */     conqueredTowns.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\war\WarStats.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
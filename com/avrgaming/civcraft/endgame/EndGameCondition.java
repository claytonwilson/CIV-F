/*     */ package com.avrgaming.civcraft.endgame;
/*     */ 
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.avrgaming.civcraft.util.CivColor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public abstract class EndGameCondition
/*     */ {
/*  16 */   public static ArrayList<EndGameCondition> endConditions = new ArrayList();
/*     */   
/*     */   private String id;
/*     */   private String victoryName;
/*  20 */   public HashMap<String, String> attributes = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   public static void init()
/*     */   {
/*  26 */     for (ConfigEndCondition configEnd : com.avrgaming.civcraft.config.CivSettings.endConditions.values()) {
/*  27 */       String className = "com.avrgaming.civcraft.endgame." + configEnd.className;
/*     */       
/*     */       try
/*     */       {
/*  31 */         Class<?> someClass = Class.forName(className);
/*     */         
/*  33 */         EndGameCondition endCompClass = (EndGameCondition)someClass.newInstance();
/*  34 */         endCompClass.setId(configEnd.id);
/*  35 */         endCompClass.setVictoryName(configEnd.victoryName);
/*  36 */         endCompClass.attributes = configEnd.attributes;
/*     */         
/*  38 */         endCompClass.onLoad();
/*  39 */         endConditions.add(endCompClass);
/*     */       } catch (InstantiationException e) {
/*  41 */         e.printStackTrace();
/*     */       } catch (IllegalAccessException e) {
/*  43 */         e.printStackTrace();
/*     */       }
/*     */       catch (ClassNotFoundException e) {
/*  46 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract void onLoad();
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract boolean check(Civilization paramCivilization);
/*     */   
/*     */ 
/*     */   public abstract String getSessionKey();
/*     */   
/*     */ 
/*     */   public void onVictoryReset(Civilization civ) {}
/*     */   
/*     */ 
/*     */   public boolean finalWinCheck(Civilization civ)
/*     */   {
/*  68 */     return true;
/*     */   }
/*     */   
/*     */   public void onSuccess(Civilization civ) {
/*  72 */     checkForWin(civ);
/*     */   }
/*     */   
/*     */   public void onFailure(Civilization civ) {
/*  76 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(getSessionKey());
/*  77 */     if (entries.size() == 0) {
/*  78 */       return;
/*     */     }
/*     */     
/*  81 */     for (SessionEntry entry : entries) {
/*  82 */       if (civ == getCivFromSessionData(entry.value)) {
/*  83 */         CivMessage.global(
/*  84 */           "§b" + CivColor.BOLD + civ.getName() + "§f" + " was " + "§c" + CivColor.BOLD + "DEFEATED" + "§f" + " and their " + "§d" + CivColor.BOLD + this.victoryName + "§f" + " victory has been reset.");
/*  85 */         CivGlobal.getSessionDB().delete(entry.request_id, entry.key);
/*  86 */         onVictoryReset(civ);
/*  87 */         return;
/*     */       }
/*     */     }
/*     */     
/*  91 */     CivLog.error("Couldn't find civilization:" + civ.getName() + " with id:" + civ.getId() + " to fail end condition:" + this.victoryName);
/*     */   }
/*     */   
/*     */   public String getString(String key)
/*     */   {
/*  96 */     return (String)this.attributes.get(key);
/*     */   }
/*     */   
/*     */   public double getDouble(String key) {
/* 100 */     return Double.valueOf((String)this.attributes.get(key)).doubleValue();
/*     */   }
/*     */   
/*     */   public void setAttribute(String key, String value) {
/* 104 */     this.attributes.put(key, value);
/*     */   }
/*     */   
/*     */   public String getId() {
/* 108 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/* 112 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getVictoryName() {
/* 116 */     return this.victoryName;
/*     */   }
/*     */   
/*     */   public void setVictoryName(String victoryName) {
/* 120 */     this.victoryName = victoryName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isActive(Civilization civ)
/*     */   {
/* 128 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(getSessionKey());
/*     */     
/* 130 */     if (entries.size() == 0) {
/* 131 */       return false;
/*     */     }
/*     */     
/* 134 */     return true;
/*     */   }
/*     */   
/*     */   public int getDaysLeft(Civilization civ) {
/* 138 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(getSessionKey());
/* 139 */     if (entries.size() == 0) {
/* 140 */       return -1;
/*     */     }
/*     */     
/* 143 */     int daysToHold = getDaysToHold();
/* 144 */     Integer daysHeld = Integer.valueOf(((SessionEntry)entries.get(0)).value);
/*     */     
/* 146 */     return daysToHold - daysHeld.intValue();
/*     */   }
/*     */   
/*     */   public int getDaysToHold() {
/* 150 */     return Integer.valueOf(getString("days_held")).intValue();
/*     */   }
/*     */   
/*     */   public void checkForWin(Civilization civ)
/*     */   {
/* 155 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(getSessionKey());
/*     */     
/* 157 */     int daysToHold = getDaysToHold();
/*     */     
/* 159 */     if (entries.size() == 0)
/*     */     {
/* 161 */       civ.sessionAdd(getSessionKey(), getSessionData(civ, Integer.valueOf(0)));
/* 162 */       civ.winConditionWarning(this, daysToHold);
/*     */     }
/*     */     else {
/* 165 */       for (SessionEntry entry : entries)
/*     */       {
/* 167 */         if (getCivFromSessionData(entry.value) == civ)
/*     */         {
/*     */ 
/*     */ 
/* 171 */           Integer daysHeld = getDaysHeldFromSessionData(entry.value);
/* 172 */           daysHeld = Integer.valueOf(daysHeld.intValue() + 1);
/*     */           
/* 174 */           if (daysHeld.intValue() < daysToHold) {
/* 175 */             civ.winConditionWarning(this, daysToHold - daysHeld.intValue());
/*     */           }
/* 177 */           else if (finalWinCheck(civ)) {
/* 178 */             civ.declareAsWinner(this);
/*     */           }
/*     */           
/*     */ 
/* 182 */           CivGlobal.getSessionDB().update(((SessionEntry)entries.get(0)).request_id, ((SessionEntry)entries.get(0)).key, getSessionData(civ, daysHeld));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String getSessionData(Civilization civ, Integer daysHeld)
/*     */   {
/* 190 */     return civ.getId() + ":" + daysHeld;
/*     */   }
/*     */   
/*     */   public static Civilization getCivFromSessionData(String data) {
/* 194 */     String[] split = data.split(":");
/* 195 */     return CivGlobal.getCivFromId(Integer.valueOf(split[0]).intValue());
/*     */   }
/*     */   
/*     */   public Integer getDaysHeldFromSessionData(String data) {
/* 199 */     String[] split = data.split(":");
/* 200 */     return Integer.valueOf(split[1]);
/*     */   }
/*     */   
/*     */   public static void onCivilizationWarDefeat(Civilization civ) {
/* 204 */     for (EndGameCondition end : endConditions) {
/* 205 */       end.onWarDefeat(civ);
/*     */     }
/*     */   }
/*     */   
/*     */   public static EndGameCondition getEndCondition(String name) {
/* 210 */     for (EndGameCondition cond : endConditions) {
/* 211 */       if (cond.getId().equals(name)) {
/* 212 */         return cond;
/*     */       }
/*     */     }
/* 215 */     return null;
/*     */   }
/*     */   
/*     */   protected abstract void onWarDefeat(Civilization paramCivilization);
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\endgame\EndGameCondition.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.avrgaming.global.perks;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ 
/*     */ public class PlatinumManager
/*     */   implements Runnable
/*     */ {
/*  24 */   public static ConcurrentHashMap<String, Queue<PendingPlatinum>> pendingPlatinum = new ConcurrentHashMap();
/*     */   
/*     */   public static void givePlatinum(Resident resident, Integer plat, String reason) {
/*  27 */     if (!isEnabled()) {
/*  28 */       return;
/*     */     }
/*     */     
/*  31 */     Queue<PendingPlatinum> pending = (Queue)pendingPlatinum.get(resident.getName());
/*  32 */     if (pending == null) {
/*  33 */       pending = new LinkedList();
/*     */     }
/*     */     
/*  36 */     PendingPlatinum pendPlat = new PendingPlatinum();
/*  37 */     pendPlat.amount = plat;
/*  38 */     pendPlat.resident = resident;
/*  39 */     pendPlat.reason = reason;
/*     */     
/*  41 */     pending.add(pendPlat);
/*  42 */     pendingPlatinum.put(resident.getName(), pending);
/*     */   }
/*     */   
/*     */   private static String getDailyKey(Resident resident, String ident)
/*     */   {
/*  47 */     return resident.getName() + ":dailyPlatinum:" + ident;
/*     */   }
/*     */   
/*     */   public static void givePlatinumDaily(Resident resident, String ident, Integer plat, String reason) {
/*  51 */     if (!isEnabled()) {
/*  52 */       return;
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
/*  92 */     TaskMaster.asyncTask(new Runnable()
/*     */     {
/*     */       Integer plat;
/*     */       String reason;
/*     */       String ident;
/*     */       
/*     */       public void run()
/*     */       {
/*  70 */         ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().global_lookup(PlatinumManager.getDailyKey(PlatinumManager.this, this.ident));
/*  71 */         Calendar now = Calendar.getInstance();
/*     */         
/*  73 */         if (entries.size() == 0)
/*     */         {
/*  75 */           CivGlobal.getSessionDB().global_add(PlatinumManager.getDailyKey(PlatinumManager.this, this.ident), now.getTimeInMillis());
/*     */         } else {
/*  77 */           Calendar then = Calendar.getInstance();
/*     */           
/*  79 */           then.setTimeInMillis(Long.valueOf(((SessionEntry)entries.get(0)).value).longValue());
/*  80 */           then.add(5, 2);
/*     */           
/*  82 */           if (!now.after(then)) {
/*  83 */             return;
/*     */           }
/*     */           
/*  86 */           CivGlobal.getSessionDB().global_update(((SessionEntry)entries.get(0)).request_id, PlatinumManager.getDailyKey(PlatinumManager.this, this.ident), now.getTimeInMillis());
/*     */         }
/*     */         
/*  89 */         PlatinumManager.givePlatinum(PlatinumManager.this, this.plat, this.reason);
/*     */       }
/*     */       
/*  92 */     }, 0L);
/*     */   }
/*     */   
/*     */   public static void giveManyPlatinumDaily(LinkedList<Resident> residents, String ident, Integer plat, String reason) {
/*  96 */     if (!isEnabled()) {
/*  97 */       return;
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
/* 140 */     TaskMaster.asyncTask(new Runnable()
/*     */     {
/*     */       Integer plat;
/*     */       String reason;
/*     */       String ident;
/*     */       
/*     */       public void run()
/*     */       {
/* 115 */         for (Resident resident : PlatinumManager.this) {
/* 116 */           ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().global_lookup(PlatinumManager.getDailyKey(resident, this.ident));
/* 117 */           Calendar now = Calendar.getInstance();
/*     */           
/* 119 */           if (entries.size() == 0)
/*     */           {
/* 121 */             CivGlobal.getSessionDB().global_add(PlatinumManager.getDailyKey(resident, this.ident), now.getTimeInMillis());
/*     */           } else {
/* 123 */             Calendar then = Calendar.getInstance();
/*     */             
/* 125 */             then.setTimeInMillis(Long.valueOf(((SessionEntry)entries.get(0)).value).longValue());
/* 126 */             then.add(5, 2);
/*     */             
/* 128 */             if (!now.after(then)) {
/* 129 */               return;
/*     */             }
/*     */             
/* 132 */             CivGlobal.getSessionDB().global_update(((SessionEntry)entries.get(0)).request_id, PlatinumManager.getDailyKey(resident, this.ident), now.getTimeInMillis());
/*     */           }
/*     */           
/* 135 */           PlatinumManager.givePlatinum(resident, this.plat, this.reason);
/*     */         }
/*     */         
/*     */       }
/*     */       
/* 140 */     }, 0L);
/*     */   }
/*     */   
/*     */   private static String getOnceKey(Resident resident, String ident)
/*     */   {
/* 145 */     return resident.getName() + ":oncePlatinum:" + ident;
/*     */   }
/*     */   
/*     */   public static void givePlatinumOnce(Resident resident, String ident, Integer plat, String reason) {
/* 149 */     if (!isEnabled()) {
/* 150 */       return;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 181 */     TaskMaster.asyncTask(new Runnable()
/*     */     {
/*     */       Integer plat;
/*     */       String reason;
/*     */       String ident;
/*     */       
/*     */       public void run()
/*     */       {
/* 168 */         ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().global_lookup(PlatinumManager.getOnceKey(PlatinumManager.this, this.ident));
/* 169 */         Calendar now = Calendar.getInstance();
/*     */         
/* 171 */         if (entries.size() == 0)
/*     */         {
/* 173 */           CivGlobal.getSessionDB().global_add(PlatinumManager.getOnceKey(PlatinumManager.this, this.ident), now.getTimeInMillis());
/* 174 */           PlatinumManager.givePlatinum(PlatinumManager.this, this.plat, this.reason);
/*     */ 
/*     */         }
/*     */         else {}
/*     */       }
/*     */       
/*     */ 
/* 181 */     }, 0L);
/*     */   }
/*     */   
/* 184 */   public static HashSet<String> warnedPlayers = new HashSet();
/*     */   
/* 186 */   public void updatePendingPlatinum() { HashMap<String, Queue<PendingPlatinum>> readdUs = new HashMap();
/* 187 */     for (String key : pendingPlatinum.keySet()) {
/* 188 */       Queue<PendingPlatinum> pendingList = (Queue)pendingPlatinum.get(key);
/* 189 */       Queue<PendingPlatinum> newQueue = new LinkedList();
/* 190 */       PendingPlatinum pending = (PendingPlatinum)pendingList.poll();
/*     */       
/* 192 */       while (pending != null) {
/* 193 */         Resident resident = pending.resident;
/* 194 */         Integer plat = pending.amount;
/* 195 */         String reason = pending.reason;
/*     */         
/* 197 */         if ((resident != null) && (plat != null))
/*     */         {
/*     */ 
/*     */           try
/*     */           {
/* 202 */             Integer userId = PerkManager.getUserWebsiteId(resident);
/* 203 */             PerkManager.updatePlatinum(userId, plat);
/*     */             
/* 205 */             reason = "§a" + reason.replace("%d", new StringBuilder("§e").append(plat).append(" Platinum").append("§a").toString());
/* 206 */             CivMessage.sendSuccess(resident, reason);
/*     */           }
/*     */           catch (SQLException e) {
/* 209 */             e.printStackTrace();
/* 210 */             pendingPlatinum.clear();
/* 211 */             return;
/*     */           }
/*     */           catch (NotVerifiedException e) {
/* 214 */             if (!warnedPlayers.contains(resident.getName()))
/*     */             {
/*     */               try {
/* 217 */                 url = CivSettings.getString(CivSettings.perkConfig, "system.store_url");
/*     */               } catch (InvalidConfiguration e1) { String url;
/* 219 */                 e1.printStackTrace(); return;
/*     */               }
/*     */               
/*     */               String url;
/* 223 */               CivMessage.sendError(resident, "Aww man! You've earned §e" + pending.amount + " Platinum" + "§c" + " but your in-game name is not currently verified!");
/* 224 */               CivMessage.sendError(resident, "Go to §e" + url + "§c" + " and verify first! We'll hold on to it until the server reboots.");
/* 225 */               warnedPlayers.add(resident.getName());
/*     */             }
/* 227 */             newQueue.add(pending);
/*     */           }
/*     */           
/* 230 */           pending = (PendingPlatinum)pendingList.poll();
/*     */         }
/*     */       }
/*     */       
/* 234 */       readdUs.put(key, newQueue);
/*     */     }
/*     */     
/* 237 */     pendingPlatinum.clear();
/*     */     
/*     */ 
/* 240 */     for (String key : readdUs.keySet()) {
/* 241 */       Queue<PendingPlatinum> list = (Queue)readdUs.get(key);
/* 242 */       pendingPlatinum.put(key, list);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/* 250 */     updatePendingPlatinum();
/*     */   }
/*     */   
/*     */   public static boolean isEnabled()
/*     */   {
/*     */     try
/*     */     {
/* 257 */       enabledStr = CivSettings.getString(CivSettings.perkConfig, "system.enabled");
/*     */     } catch (InvalidConfiguration e) { String enabledStr;
/* 259 */       e.printStackTrace();
/* 260 */       return false;
/*     */     }
/*     */     String enabledStr;
/* 263 */     if (enabledStr.equalsIgnoreCase("true")) {
/* 264 */       return true;
/*     */     }
/*     */     
/* 267 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\global\perks\PlatinumManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
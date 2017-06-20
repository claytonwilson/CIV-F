/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*    */ import com.avrgaming.civcraft.structure.TownHall;
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PlayerReviveTask
/*    */   implements Runnable
/*    */ {
/*    */   String playerName;
/*    */   int timeout;
/*    */   TownHall townhall;
/*    */   Location alternativeLocation;
/*    */   
/*    */   public PlayerReviveTask(Player player, int timeout, TownHall townhall, Location alt)
/*    */   {
/* 39 */     this.playerName = player.getName();
/* 40 */     this.timeout = timeout;
/* 41 */     this.townhall = townhall;
/* 42 */     this.alternativeLocation = alt;
/*    */   }
/*    */   
/*    */ 
/*    */   public void setRespawnViaSessionDB()
/*    */   {
/* 48 */     BlockCoord revive = this.townhall.getRandomRevivePoint();
/* 49 */     CivGlobal.getSessionDB().add("global:respawnPlayer", this.playerName + ":" + revive.toString(), 0, 0, 0);
/*    */   }
/*    */   
/*    */ 
/*    */   public void run()
/*    */   {
/*    */     try
/*    */     {
/* 57 */       player = CivGlobal.getPlayer(this.playerName);
/*    */     } catch (CivException e1) { Player player;
/* 59 */       setRespawnViaSessionDB(); return;
/*    */     }
/*    */     
/*    */     Player player;
/* 63 */     CivMessage.send(player, "§7You will respawn in " + this.timeout + " seconds.");
/*    */     try
/*    */     {
/* 66 */       synchronized (this) {
/* 67 */         wait(this.timeout * 1000);
/*    */       }
/*    */     }
/*    */     catch (InterruptedException localInterruptedException1)
/*    */     {
/*    */       try {
/* 73 */         player = CivGlobal.getPlayer(this.playerName);
/*    */       } catch (CivException e1) {
/* 75 */         setRespawnViaSessionDB();
/* 76 */         return;
/*    */       }
/*    */       
/* 79 */       BlockCoord revive = this.townhall.getRandomRevivePoint();
/*    */       Location loc;
/* 81 */       Location loc; if (revive == null) {
/* 82 */         loc = this.alternativeLocation;
/*    */       } else {
/* 84 */         loc = revive.getLocation();
/*    */       }
/*    */       
/* 87 */       CivMessage.send(player, "§aRespawning...");
/*    */       try
/*    */       {
/* 90 */         synchronized (this) {
/* 91 */           wait(500L);
/*    */         }
/*    */       } catch (InterruptedException e) {
/* 94 */         e.printStackTrace();
/*    */         
/*    */ 
/* 97 */         player.teleport(loc);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\PlayerReviveTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
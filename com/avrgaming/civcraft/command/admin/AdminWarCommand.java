/*     */ package com.avrgaming.civcraft.command.admin;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.DiplomacyManager;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.threading.tasks.PlayerKickBan;
/*     */ import com.avrgaming.civcraft.war.War;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.Bukkit;
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
/*     */ public class AdminWarCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  39 */     this.command = "/ad war";
/*  40 */     this.displayName = "Admin War";
/*     */     
/*  42 */     this.commands.put("start", "Turns on WarTime.");
/*  43 */     this.commands.put("stop", "Turns off WarTime.");
/*  44 */     this.commands.put("resetstart", "Resets the war start time to now.");
/*     */     
/*  46 */     this.commands.put("onlywarriors", "Kicks everyone who is not at war from servers and only lets at war players in.");
/*     */   }
/*     */   
/*     */   public void onlywarriors_cmd()
/*     */   {
/*  51 */     War.setOnlyWarriors(!War.isOnlyWarriors());
/*     */     
/*  53 */     if (War.isOnlyWarriors()) {
/*     */       Player[] arrayOfPlayer;
/*  55 */       int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length; for (int i = 0; i < j; i++) { Player player = arrayOfPlayer[i];
/*  56 */         Resident resident = CivGlobal.getResident(player);
/*     */         
/*  58 */         if ((player.isOp()) || (player.hasPermission("civ.admin"))) {
/*  59 */           CivMessage.send(this.sender, "Skipping " + player.getName() + " since he is OP or mini admin.");
/*     */ 
/*     */ 
/*     */         }
/*  63 */         else if ((resident == null) || (!resident.hasTown()) || 
/*  64 */           (!resident.getTown().getCiv().getDiplomacyManager().isAtWar()))
/*     */         {
/*  66 */           TaskMaster.syncTask(new PlayerKickBan(player.getName(), true, false, "Kicked: Only residents 'at war' can play right now."));
/*     */         }
/*     */       }
/*     */       
/*  70 */       CivMessage.global("All players 'not at war' have been kicked and cannot rejoin.");
/*     */     } else {
/*  72 */       CivMessage.global("All players are now allowed to join again.");
/*     */     }
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
/*     */   public void start_cmd()
/*     */   {
/*  98 */     War.setWarTime(true);
/*  99 */     CivMessage.sendSuccess(this.sender, "WarTime enabled.");
/*     */   }
/*     */   
/*     */   public void stop_cmd()
/*     */   {
/* 104 */     War.setWarTime(false);
/* 105 */     CivMessage.sendSuccess(this.sender, "WarTime disabled.");
/*     */   }
/*     */   
/*     */   public void doDefaultAction() throws CivException
/*     */   {
/* 110 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 115 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck() throws CivException
/*     */   {
/* 120 */     if (!this.sender.isOp()) {
/* 121 */       throw new CivException("Only admins can use this command.");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\admin\AdminWarCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
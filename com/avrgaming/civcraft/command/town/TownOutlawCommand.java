/*     */ package com.avrgaming.civcraft.command.town;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.threading.tasks.TownAddOutlawTask;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.ChatColor;
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
/*     */ public class TownOutlawCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  38 */     this.command = "/town outlaw";
/*  39 */     this.displayName = "Town Outlaw";
/*     */     
/*  41 */     this.commands.put("add", "[name] - Adds this player to the outlaw list.");
/*  42 */     this.commands.put("remove", "[name] - Removes this player from the outlaw list.");
/*  43 */     this.commands.put("list", "Lists all of the town's current outlaws.");
/*  44 */     this.commands.put("addall", "[town] - Adds entire town to the outlaw list.");
/*  45 */     this.commands.put("removeall", "[town] - Removes entire town from the outlaw list.");
/*     */   }
/*     */   
/*     */   public void addall_cmd() throws CivException {
/*  49 */     Town town = getSelectedTown();
/*  50 */     Town targetTown = getNamedTown(1);
/*     */     
/*  52 */     for (Resident resident : targetTown.getResidents())
/*     */     {
/*     */       try {
/*  55 */         Player player = CivGlobal.getPlayer(this.args[1]);
/*  56 */         CivMessage.send(player, "§e" + ChatColor.BOLD + "You're going to be declared an outlaw in " + 
/*  57 */           town.getName() + "! You have one minute to get out ...");
/*     */       }
/*     */       catch (CivException localCivException) {}
/*  60 */       TaskMaster.asyncTask(new TownAddOutlawTask(resident.getName(), town), 1000L);
/*     */     }
/*  62 */     CivMessage.sendSuccess(this.sender, this.args[1] + " will be an outlaw in 60 seconds.");
/*     */   }
/*     */   
/*     */   public void removeall_cmd() throws CivException {
/*  66 */     Town town = getSelectedTown();
/*  67 */     Town targetTown = getNamedTown(1);
/*     */     
/*  69 */     for (Resident resident : targetTown.getResidents()) {
/*  70 */       town.removeOutlaw(resident.getName());
/*     */     }
/*  72 */     town.save();
/*     */   }
/*     */   
/*     */   public void add_cmd() throws CivException {
/*  76 */     Town town = getSelectedTown();
/*     */     
/*  78 */     if (this.args.length < 2) {
/*  79 */       throw new CivException("Enter player name to declare as an outlaw.");
/*     */     }
/*     */     
/*  82 */     Resident resident = getNamedResident(1);
/*  83 */     if (resident.getTown() == town) {
/*  84 */       throw new CivException("Cannot declare one of your own town members as an outlaw.");
/*     */     }
/*     */     try
/*     */     {
/*  88 */       Player player = CivGlobal.getPlayer(this.args[1]);
/*  89 */       CivMessage.send(player, "§e" + ChatColor.BOLD + "You're going to be declared an outlaw in " + town.getName() + "! You have one minute to get out ...");
/*     */     }
/*     */     catch (CivException localCivException) {}
/*     */     
/*  93 */     CivMessage.sendSuccess(this.sender, this.args[1] + " will be an outlaw in 60 seconds.");
/*  94 */     TaskMaster.asyncTask(new TownAddOutlawTask(this.args[1], town), 1000L);
/*     */   }
/*     */   
/*     */   public void remove_cmd() throws CivException {
/*  98 */     Town town = getSelectedTown();
/*     */     
/* 100 */     if (this.args.length < 2) {
/* 101 */       throw new CivException("Enter player name to remove as an outlaw.");
/*     */     }
/*     */     
/* 104 */     town.removeOutlaw(this.args[1]);
/* 105 */     town.save();
/*     */     
/* 107 */     CivMessage.sendSuccess(this.sender, "Removed " + this.args[1] + " from being an outlaw.");
/*     */   }
/*     */   
/*     */   public void list_cmd() throws CivException {
/* 111 */     Town town = getSelectedTown();
/*     */     
/* 113 */     CivMessage.sendHeading(this.sender, "Town Outlaws");
/*     */     
/* 115 */     String out = "";
/* 116 */     for (String outlaw : town.outlaws) {
/* 117 */       out = out + outlaw + ",";
/*     */     }
/*     */     
/* 120 */     CivMessage.send(this.sender, out);
/*     */   }
/*     */   
/*     */   public void doDefaultAction()
/*     */     throws CivException
/*     */   {
/* 126 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 131 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck() throws CivException
/*     */   {
/* 136 */     validMayorAssistantLeader();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\town\TownOutlawCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
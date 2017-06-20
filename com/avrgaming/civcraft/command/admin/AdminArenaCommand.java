/*    */ package com.avrgaming.civcraft.command.admin;
/*    */ 
/*    */ import com.avrgaming.civcraft.arena.Arena;
/*    */ import com.avrgaming.civcraft.arena.ArenaManager;
/*    */ import com.avrgaming.civcraft.arena.ArenaTeam;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.util.CivColor;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class AdminArenaCommand extends com.avrgaming.civcraft.command.CommandBase
/*    */ {
/*    */   public void init()
/*    */   {
/* 15 */     this.command = "/ad arena";
/* 16 */     this.displayName = "Admin Arena";
/*    */     
/* 18 */     this.commands.put("list", "Lists all active arenas and which teams are in them.");
/* 19 */     this.commands.put("end", "[name] end the arena with this id.");
/* 20 */     this.commands.put("messageall", "[msg] send a message to all arenas.");
/* 21 */     this.commands.put("message", "[id] [msg] send a message to this arena.");
/* 22 */     this.commands.put("enable", "Enable arenas globally.");
/* 23 */     this.commands.put("disable", "Disable arenas globally.");
/*    */   }
/*    */   
/*    */   public void enable_cmd() {
/* 27 */     ArenaManager.enabled = true;
/* 28 */     CivMessage.sendSuccess(this.sender, "Arenas Enabled");
/*    */   }
/*    */   
/*    */   public void disable_cmd() {
/* 32 */     ArenaManager.enabled = false;
/* 33 */     CivMessage.sendSuccess(this.sender, "Arenas Disabled");
/*    */   }
/*    */   
/*    */   public void list_cmd() {
/* 37 */     CivMessage.sendHeading(this.sender, "Active Arenas");
/* 38 */     for (Arena arena : ArenaManager.activeArenas.values()) {
/* 39 */       String teams = "";
/* 40 */       for (ArenaTeam team : arena.getTeams()) {
/* 41 */         teams = teams + team.getName() + ", ";
/*    */       }
/*    */       
/* 44 */       CivMessage.send(this.sender, arena.getInstanceName() + ": Teams: " + teams);
/*    */     }
/*    */   }
/*    */   
/*    */   public void messageall_cmd() {
/* 49 */     String message = combineArgs(stripArgs(this.args, 1));
/* 50 */     for (Arena arena : ArenaManager.activeArenas.values()) {
/* 51 */       CivMessage.sendArena(arena, "§cADMIN:" + CivColor.RESET + message);
/*    */     }
/* 53 */     CivMessage.send(this.sender, "§cADMIN:" + CivColor.RESET + message);
/*    */   }
/*    */   
/*    */   public void message_cmd() throws CivException {
/* 57 */     String id = getNamedString(1, "Enter arena instance name");
/* 58 */     String message = combineArgs(stripArgs(this.args, 2));
/*    */     
/* 60 */     Arena arena = (Arena)ArenaManager.activeArenas.get(id);
/* 61 */     if (arena == null) {
/* 62 */       throw new CivException("No arena with that id found.");
/*    */     }
/*    */     
/* 65 */     CivMessage.sendArena(arena, "§cADMIN:" + CivColor.RESET + message);
/* 66 */     CivMessage.send(this.sender, "§cADMIN:" + CivColor.RESET + message);
/*    */   }
/*    */   
/*    */   public void end_cmd() throws CivException
/*    */   {
/* 71 */     String id = getNamedString(1, "Enter arena instance name");
/*    */     
/* 73 */     Arena arena = (Arena)ArenaManager.activeArenas.get(id);
/* 74 */     if (arena == null) {
/* 75 */       throw new CivException("No arena with that id found.");
/*    */     }
/*    */     
/* 78 */     CivMessage.sendArena(arena, "§cAn Admin is ending this arena in a draw.");
/* 79 */     ArenaManager.declareDraw(arena);
/*    */   }
/*    */   
/*    */   public void doDefaultAction()
/*    */     throws CivException
/*    */   {
/* 85 */     showHelp();
/*    */   }
/*    */   
/*    */   public void showHelp()
/*    */   {
/* 90 */     showBasicHelp();
/*    */   }
/*    */   
/*    */   public void permissionCheck()
/*    */     throws CivException
/*    */   {}
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\admin\AdminArenaCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
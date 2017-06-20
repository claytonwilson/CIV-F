/*     */ package com.avrgaming.civcraft.command.team;
/*     */ 
/*     */ import com.avrgaming.civcraft.arena.Arena;
/*     */ import com.avrgaming.civcraft.arena.ArenaManager;
/*     */ import com.avrgaming.civcraft.arena.ArenaTeam;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.questions.JoinTeamResponse;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Queue;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ public class TeamCommand extends com.avrgaming.civcraft.command.CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  20 */     this.command = "/team";
/*  21 */     this.displayName = "Team";
/*     */     
/*  23 */     this.commands.put("info", "Lists information about the current team you're in.");
/*  24 */     this.commands.put("show", "[name] Shows information about the named team.");
/*  25 */     this.commands.put("create", "[name] Creates a team with the given name.");
/*  26 */     this.commands.put("leave", "Leaves your current team.");
/*  27 */     this.commands.put("disband", "Disbands your current team. You must be the team leader to do this.");
/*  28 */     this.commands.put("add", "[resident] Add a resident to your team.");
/*  29 */     this.commands.put("remove", "[resident] removes a resident from your team.");
/*  30 */     this.commands.put("changeleader", "[resident] - Gives team leadership to another team member.");
/*  31 */     this.commands.put("arena", "Join the queue to fight the arena! Will take us out of the queue if we're already in it.");
/*  32 */     this.commands.put("top5", "Shows top 5 teams in the game!");
/*  33 */     this.commands.put("top10", "Shows top 10 teams in the game!");
/*  34 */     this.commands.put("list", "List all teams in the game.");
/*  35 */     this.commands.put("surrender", "Give up on the current match.");
/*     */   }
/*     */   
/*     */   public void surrender_cmd() throws CivException {
/*  39 */     Resident resident = getResident();
/*     */     
/*  41 */     if (!resident.hasTeam()) {
/*  42 */       throw new CivException("You must be part of a team to use this command.");
/*     */     }
/*     */     
/*  45 */     if (!resident.isTeamLeader()) {
/*  46 */       throw new CivException("Only leaders can surrender during a match.");
/*     */     }
/*     */     
/*  49 */     ArenaTeam team = resident.getTeam();
/*  50 */     Arena arena = team.getCurrentArena();
/*     */     
/*  52 */     if (arena == null) {
/*  53 */       throw new CivException("Your team is not currently in arena match");
/*     */     }
/*     */     
/*  56 */     ArenaTeam otherTeam = null;
/*  57 */     for (ArenaTeam t : arena.getTeams()) {
/*  58 */       if (t != team) {
/*  59 */         otherTeam = t;
/*  60 */         break;
/*     */       }
/*     */     }
/*     */     
/*  64 */     if (otherTeam == null) {
/*  65 */       throw new CivException("Error, couldn't find other team to surrender to.");
/*     */     }
/*     */     
/*  68 */     ArenaManager.declareVictor(arena, team, otherTeam);
/*  69 */     CivMessage.sendSuccess(this.sender, "Surrendered.");
/*     */   }
/*     */   
/*     */   public void arena_cmd() throws CivException
/*     */   {
/*  74 */     Resident resident = getResident();
/*     */     
/*  76 */     if (!resident.hasTeam()) {
/*  77 */       throw new CivException("You're already not part of a team.");
/*     */     }
/*     */     
/*  80 */     if (!resident.isTeamLeader()) {
/*  81 */       throw new CivException("Only leaders can add their team to the arena queue.");
/*     */     }
/*     */     
/*  84 */     ArenaTeam team = resident.getTeam();
/*     */     
/*  86 */     if (team.getCurrentArena() != null) {
/*  87 */       throw new CivException("Cannot join the arena queue while inside the arena.");
/*     */     }
/*     */     
/*  90 */     for (ArenaTeam t : ArenaManager.teamQueue) {
/*  91 */       if (t == team) {
/*  92 */         ArenaManager.teamQueue.remove(t);
/*  93 */         CivMessage.sendSuccess(this.sender, "Removed our team from the queue.");
/*  94 */         return;
/*     */       }
/*     */     }
/*     */     
/*  98 */     ArenaManager.addTeamToQueue(team);
/*  99 */     CivMessage.sendSuccess(this.sender, "Added our team to the queue.");
/*     */   }
/*     */   
/*     */   public void list_cmd()
/*     */   {
/* 104 */     CivMessage.sendHeading(this.sender, "Teams");
/* 105 */     String out = "";
/*     */     
/* 107 */     for (ArenaTeam team : ArenaTeam.arenaTeams.values()) {
/* 108 */       out = out + team.getName() + ", ";
/*     */     }
/*     */     
/* 111 */     CivMessage.send(this.sender, out);
/*     */   }
/*     */   
/*     */   public void top5_cmd()
/*     */   {
/* 116 */     CivMessage.sendHeading(this.sender, "Top 5 Teams");
/*     */     
/* 118 */     for (int i = 0; (i < 5) && (i < ArenaTeam.teamRankings.size()); i++) {
/* 119 */       ArenaTeam team = (ArenaTeam)ArenaTeam.teamRankings.get(i);
/* 120 */       CivMessage.send(this.sender, "§2" + team.getName() + ": " + "§a" + team.getLadderPoints());
/*     */     }
/*     */   }
/*     */   
/*     */   public void top10_cmd() {
/* 125 */     CivMessage.sendHeading(this.sender, "Top 10 Teams");
/*     */     
/* 127 */     for (int i = 0; (i < 10) && (i < ArenaTeam.teamRankings.size()); i++) {
/* 128 */       ArenaTeam team = (ArenaTeam)ArenaTeam.teamRankings.get(i);
/* 129 */       CivMessage.send(this.sender, "§2" + team.getName() + ": " + "§a" + team.getLadderPoints());
/*     */     }
/*     */   }
/*     */   
/*     */   public void printTeamInfo(ArenaTeam team) {
/* 134 */     CivMessage.sendHeading(this.sender, "Team " + team.getName());
/* 135 */     CivMessage.send(this.sender, "§2Points: §a" + team.getLadderPoints() + 
/* 136 */       "§2" + " Leader: " + "§a" + team.getLeader().getName());
/* 137 */     CivMessage.send(this.sender, "§2Members: §a" + team.getMemberListSaveString());
/*     */   }
/*     */   
/*     */   public void info_cmd() throws CivException {
/* 141 */     Resident resident = getResident();
/*     */     
/* 143 */     if (!resident.hasTeam()) {
/* 144 */       throw new CivException("You're not currently part of a team.");
/*     */     }
/*     */     
/* 147 */     ArenaTeam team = resident.getTeam();
/* 148 */     printTeamInfo(team);
/*     */   }
/*     */   
/*     */   public void show_cmd() throws CivException {
/* 152 */     ArenaTeam team = getNamedTeam(1);
/* 153 */     printTeamInfo(team);
/*     */   }
/*     */   
/*     */   public void create_cmd() throws CivException {
/* 157 */     String teamName = getNamedString(1, "Enter a name for your team.");
/* 158 */     Resident resident = getResident();
/*     */     
/* 160 */     if (resident.isProtected()) {
/* 161 */       throw new CivException("You can not form a team while protected.");
/*     */     }
/*     */     
/* 164 */     if (resident.hasTeam()) {
/* 165 */       throw new CivException("You can only be on one team at time. Leave your current team first.");
/*     */     }
/*     */     
/*     */ 
/* 169 */     ArenaTeam.createTeam(teamName, resident);
/* 170 */     CivMessage.sendSuccess(this.sender, "Team Successfully Created.");
/*     */   }
/*     */   
/*     */   public void leave_cmd() throws CivException {
/* 174 */     Resident resident = getResident();
/*     */     
/* 176 */     if (!resident.hasTeam()) {
/* 177 */       throw new CivException("You're already not part of a team.");
/*     */     }
/*     */     
/* 180 */     if (resident.isTeamLeader()) {
/* 181 */       throw new CivException("Leaders cannot leave their own team. They must change the leader or disband the team first.");
/*     */     }
/*     */     
/* 184 */     ArenaTeam team = resident.getTeam();
/*     */     
/* 186 */     if (team.getCurrentArena() != null) {
/* 187 */       throw new CivException("Cannot leave your team while it is inside the arena.");
/*     */     }
/*     */     
/* 190 */     ArenaTeam.removeMember(team.getName(), resident);
/* 191 */     CivMessage.sendSuccess(this.sender, "Left Team " + team.getName());
/* 192 */     CivMessage.sendTeam(team, resident.getName() + " has left the team.");
/*     */   }
/*     */   
/*     */   public void disband_cmd() throws CivException {
/* 196 */     Resident resident = getResident();
/*     */     
/* 198 */     if (!resident.isTeamLeader()) {
/* 199 */       throw new CivException("You must have a team and be it's leader to disband your team.");
/*     */     }
/*     */     
/* 202 */     if (resident.getTeam().getCurrentArena() != null) {
/* 203 */       throw new CivException("Cannot disband your team while it is inside the arena.");
/*     */     }
/*     */     
/* 206 */     String teamName = resident.getTeam().getName();
/* 207 */     ArenaTeam.deleteTeam(teamName);
/* 208 */     ArenaTeam.arenaTeams.remove(teamName);
/* 209 */     CivMessage.sendSuccess(this.sender, "Disbanded team: " + teamName);
/*     */   }
/*     */   
/*     */   public void add_cmd() throws CivException {
/* 213 */     Resident resident = getResident();
/* 214 */     Resident member = getNamedResident(1);
/*     */     
/* 216 */     if (!resident.isTeamLeader()) {
/* 217 */       throw new CivException("You must have a team and be it's leader to add members to your team.");
/*     */     }
/*     */     
/* 220 */     if (member.hasTeam()) {
/* 221 */       throw new CivException(member.getName() + " is already on a team.");
/*     */     }
/*     */     
/* 224 */     if (resident.getTeam().getCurrentArena() != null) {
/* 225 */       throw new CivException("Cannot add players to team while inside the arena.");
/*     */     }
/*     */     try
/*     */     {
/* 229 */       Player player = CivGlobal.getPlayer(member);
/*     */       
/* 231 */       if (member.isProtected()) {
/* 232 */         throw new CivException(player.getName() + " is protected and unable to join a team");
/*     */       }
/*     */       
/* 235 */       ArenaTeam team = resident.getTeam();
/* 236 */       JoinTeamResponse join = new JoinTeamResponse();
/* 237 */       join.team = team;
/* 238 */       join.resident = member;
/* 239 */       join.sender = ((Player)this.sender);
/*     */       
/* 241 */       CivGlobal.questionPlayer(CivGlobal.getPlayer(resident), player, 
/* 242 */         "Would you like to join team " + team.getName() + "?", 
/* 243 */         30000L, join);
/*     */     }
/*     */     catch (CivException e) {
/* 246 */       throw new CivException(e.getMessage());
/*     */     }
/*     */     
/* 249 */     CivMessage.sendSuccess(this.sender, "Sent invitation to " + member.getName());
/*     */   }
/*     */   
/*     */   public void remove_cmd() throws CivException {
/* 253 */     Resident resident = getResident();
/* 254 */     Resident member = getNamedResident(1);
/*     */     
/* 256 */     if (!resident.isTeamLeader()) {
/* 257 */       throw new CivException("You must have a team and be it's leader to remove members to your team.");
/*     */     }
/*     */     
/* 260 */     if (resident.getTeam().getCurrentArena() != null) {
/* 261 */       throw new CivException("Cannot remove players from the team while inside the arena.");
/*     */     }
/*     */     
/* 264 */     ArenaTeam.removeMember(resident.getTeam().getName(), member);
/* 265 */     CivMessage.sendSuccess(this.sender, "Removed Team Member " + member.getName());
/* 266 */     CivMessage.sendTeam(resident.getTeam(), member.getName() + " has left the team.");
/*     */   }
/*     */   
/*     */   public void changeleader_cmd() throws CivException
/*     */   {
/* 271 */     Resident resident = getResident();
/* 272 */     Resident member = getNamedResident(1);
/*     */     
/* 274 */     if (!resident.isTeamLeader()) {
/* 275 */       throw new CivException("You must have a team and be it's leader to change team leaders.");
/*     */     }
/*     */     
/* 278 */     ArenaTeam team = resident.getTeam();
/*     */     
/* 280 */     if (team.getCurrentArena() != null) {
/* 281 */       throw new CivException("Cannot change team leaders while inside the arena.");
/*     */     }
/*     */     
/* 284 */     if (!team.hasMember(member)) {
/* 285 */       throw new CivException(member.getName() + " must already be added to your team in order to become it's leader.");
/*     */     }
/*     */     
/* 288 */     team.setLeader(member);
/* 289 */     team.save();
/*     */     
/* 291 */     CivMessage.sendSuccess(this.sender, "Changed team leader to " + member.getName());
/* 292 */     CivMessage.sendSuccess(member, "You are now leader of team " + team.getName());
/* 293 */     CivMessage.sendTeam(team, resident.getName() + " has changed the team leader to " + member.getName());
/*     */   }
/*     */   
/*     */   public void doDefaultAction()
/*     */     throws CivException
/*     */   {
/* 299 */     showHelp();
/*     */   }
/*     */   
/*     */ 
/*     */   public void showHelp()
/*     */   {
/* 305 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck()
/*     */     throws CivException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\team\TeamCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
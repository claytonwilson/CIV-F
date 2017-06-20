/*     */ package com.avrgaming.civcraft.arena;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.database.SQLUpdate;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*     */ import com.avrgaming.civcraft.exception.InvalidObjectException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.SQLObject;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.scoreboard.Team;
/*     */ 
/*     */ public class ArenaTeam
/*     */   extends SQLObject
/*     */   implements Comparable<ArenaTeam>
/*     */ {
/*  28 */   public LinkedList<Resident> teamMembers = new LinkedList();
/*     */   
/*     */   private Resident leader;
/*     */   private int ladderPoints;
/*     */   private Arena currentArena;
/*     */   private Team team;
/*     */   private String teamColor;
/*  35 */   public static HashMap<String, ArenaTeam> arenaTeams = new HashMap();
/*  36 */   public static LinkedList<ArenaTeam> teamRankings = new LinkedList();
/*     */   public static final String TABLE_NAME = "ARENA_TEAMS";
/*     */   
/*  39 */   public ArenaTeam(String name, Resident leader) throws InvalidNameException { setName(name);
/*  40 */     this.leader = leader;
/*  41 */     this.teamMembers.add(leader);
/*     */   }
/*     */   
/*     */   public ArenaTeam(ResultSet rs) throws SQLException, InvalidNameException, InvalidObjectException, CivException {
/*  45 */     load(rs);
/*     */   }
/*     */   
/*     */   public static void init() throws SQLException
/*     */   {
/*  50 */     if (!SQL.hasTable("ARENA_TEAMS")) {
/*  51 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + "ARENA_TEAMS" + " (" + 
/*  52 */         "`id` int(11) unsigned NOT NULL auto_increment," + 
/*  53 */         "`name` VARCHAR(64) NOT NULL," + 
/*  54 */         "`leader` mediumtext NULL," + 
/*  55 */         "`ladderPoints` int(11) DEFAULT 0," + 
/*  56 */         "`members` mediumtext NULL," + 
/*  57 */         "UNIQUE KEY (`name`), " + 
/*  58 */         "PRIMARY KEY (`id`)" + ")";
/*     */       
/*  60 */       SQL.makeTable(table_create);
/*  61 */       CivLog.info("Created ARENA_TEAMS table");
/*     */     } else {
/*  63 */       CivLog.info("ARENA_TEAMS table OK!");
/*     */     }
/*     */   }
/*     */   
/*     */   private void loadMembers(String memberList) {
/*  68 */     String[] members = memberList.split(",");
/*  69 */     String[] arrayOfString1; int j = (arrayOfString1 = members).length; for (int i = 0; i < j; i++) { String name = arrayOfString1[i];
/*  70 */       Resident resident = CivGlobal.getResident(name);
/*  71 */       if (resident != null) {
/*  72 */         this.teamMembers.add(resident);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String getMemberListSaveString() {
/*  78 */     String out = "";
/*  79 */     for (Resident resident : this.teamMembers) {
/*  80 */       out = out + resident.getName() + ",";
/*     */     }
/*     */     
/*  83 */     return out;
/*     */   }
/*     */   
/*     */   public void load(ResultSet rs)
/*     */     throws SQLException, InvalidNameException, InvalidObjectException, CivException
/*     */   {
/*  89 */     setId(rs.getInt("id"));
/*  90 */     setName(rs.getString("name"));
/*  91 */     this.leader = CivGlobal.getResident(rs.getString("leader"));
/*  92 */     if (this.leader == null) {
/*  93 */       CivLog.error("Couldn't load leader for team:" + getName() + "(" + getId() + ")");
/*  94 */       return;
/*     */     }
/*     */     
/*  97 */     setLadderPoints(rs.getInt("ladderPoints"));
/*  98 */     loadMembers(rs.getString("members"));
/*     */     
/* 100 */     arenaTeams.put(getName(), this);
/* 101 */     teamRankings.add(this);
/*     */   }
/*     */   
/*     */   public void save()
/*     */   {
/* 106 */     SQLUpdate.add(this);
/*     */   }
/*     */   
/*     */   public void saveNow() throws SQLException
/*     */   {
/* 111 */     HashMap<String, Object> hashmap = new HashMap();
/* 112 */     hashmap.put("name", getName());
/* 113 */     hashmap.put("leader", this.leader.getName());
/* 114 */     hashmap.put("ladderPoints", Integer.valueOf(getLadderPoints()));
/* 115 */     hashmap.put("members", getMemberListSaveString());
/*     */     
/* 117 */     SQL.updateNamedObject(this, hashmap, "ARENA_TEAMS");
/*     */   }
/*     */   
/*     */   public void delete() throws SQLException
/*     */   {
/* 122 */     SQL.deleteNamedObject(this, "ARENA_TEAMS");
/*     */   }
/*     */   
/*     */   public int getLadderPoints() {
/* 126 */     return this.ladderPoints;
/*     */   }
/*     */   
/*     */   public void setLadderPoints(int ladderPoints) {
/* 130 */     this.ladderPoints = ladderPoints;
/*     */   }
/*     */   
/*     */   public Resident getLeader() {
/* 134 */     return this.leader;
/*     */   }
/*     */   
/*     */   public void setLeader(Resident leader) {
/* 138 */     this.leader = leader;
/*     */   }
/*     */   
/*     */   public void addPoints(int points) {
/* 142 */     this.ladderPoints += points;
/* 143 */     Collections.sort(teamRankings);
/* 144 */     Collections.reverse(teamRankings);
/*     */   }
/*     */   
/*     */   public void removePoints(int points) {
/* 148 */     this.ladderPoints -= points;
/* 149 */     Collections.sort(teamRankings);
/* 150 */     Collections.reverse(teamRankings);
/*     */   }
/*     */   
/*     */   public static void createTeam(String name, Resident leader) throws CivException {
/*     */     try {
/* 155 */       if (arenaTeams.containsKey(name)) {
/* 156 */         throw new CivException("A team with that name already exists.");
/*     */       }
/*     */       
/* 159 */       ArenaTeam team = new ArenaTeam(name, leader);
/* 160 */       team.save();
/*     */       
/* 162 */       arenaTeams.put(team.getName(), team);
/* 163 */       teamRankings.add(team);
/*     */       
/* 165 */       Collections.sort(teamRankings);
/* 166 */       CivMessage.sendSuccess(leader, "Create a new team named " + name);
/*     */     } catch (InvalidNameException e) {
/* 168 */       throw new CivException("Invalid name(" + name + ") choose another.");
/*     */     }
/*     */   }
/*     */   
/*     */   public static void deleteTeam(String name) {
/* 173 */     ArenaTeam team = (ArenaTeam)arenaTeams.get(name);
/* 174 */     if (team != null) {
/*     */       try {
/* 176 */         team.delete();
/*     */       } catch (SQLException e) {
/* 178 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void addMember(String teamName, Resident member) throws CivException {
/* 184 */     ArenaTeam team = (ArenaTeam)arenaTeams.get(teamName);
/* 185 */     if (team == null) {
/* 186 */       throw new CivException("No team named " + teamName);
/*     */     }
/*     */     try
/*     */     {
/* 190 */       int max_team_size = CivSettings.getInteger(CivSettings.arenaConfig, "max_team_size").intValue();
/*     */       
/* 192 */       if (team.teamMembers.size() >= max_team_size) {
/* 193 */         throw new CivException("Cannot have teams with more than " + max_team_size + " players.");
/*     */       }
/*     */     }
/*     */     catch (InvalidConfiguration e) {
/* 197 */       throw new CivException("Internal configuration error");
/*     */     }
/*     */     
/* 200 */     team.teamMembers.add(member);
/* 201 */     team.save();
/*     */   }
/*     */   
/*     */   public static void removeMember(String teamName, Resident member) throws CivException {
/* 205 */     ArenaTeam team = (ArenaTeam)arenaTeams.get(teamName);
/* 206 */     if (team == null) {
/* 207 */       throw new CivException("No team named " + teamName);
/*     */     }
/*     */     
/* 210 */     if (!team.teamMembers.contains(member)) {
/* 211 */       throw new CivException("No team member named:" + member);
/*     */     }
/*     */     
/* 214 */     team.teamMembers.remove(member);
/* 215 */     team.save();
/*     */   }
/*     */   
/*     */   public boolean hasMember(Resident resident) {
/* 219 */     for (Resident r : this.teamMembers) {
/* 220 */       if (r == resident) {
/* 221 */         return true;
/*     */       }
/*     */     }
/* 224 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isInTeam(String teamName, Resident resident) throws CivException {
/* 228 */     ArenaTeam team = (ArenaTeam)arenaTeams.get(teamName);
/* 229 */     if (team == null) {
/* 230 */       throw new CivException("No team named " + teamName);
/*     */     }
/*     */     
/* 233 */     return team.hasMember(resident);
/*     */   }
/*     */   
/*     */   public static ArenaTeam getTeamForResident(Resident resident) {
/* 237 */     for (ArenaTeam team : arenaTeams.values()) {
/* 238 */       if (team.hasMember(resident)) {
/* 239 */         return team;
/*     */       }
/*     */     }
/*     */     
/* 243 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public int compareTo(ArenaTeam otherTeam)
/*     */   {
/* 249 */     if (this.ladderPoints == otherTeam.getLadderPoints())
/* 250 */       return 0;
/* 251 */     if (this.ladderPoints > otherTeam.ladderPoints) {
/* 252 */       return 1;
/*     */     }
/*     */     
/* 255 */     return -1;
/*     */   }
/*     */   
/*     */   public void setCurrentArena(Arena arena) {
/* 259 */     this.currentArena = arena;
/*     */   }
/*     */   
/*     */   public Arena getCurrentArena() {
/* 263 */     return this.currentArena;
/*     */   }
/*     */   
/*     */   public Team getScoreboardTeam() {
/* 267 */     return this.team;
/*     */   }
/*     */   
/*     */   public void setScoreboardTeam(Team team) {
/* 271 */     this.team = team;
/*     */   }
/*     */   
/*     */   public OfflinePlayer getTeamScoreboardName() {
/* 275 */     return Bukkit.getOfflinePlayer(getTeamColor() + getName());
/*     */   }
/*     */   
/*     */   public String getTeamColor() {
/* 279 */     return this.teamColor;
/*     */   }
/*     */   
/*     */   public void setTeamColor(String teamColor) {
/* 283 */     this.teamColor = teamColor;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\arena\ArenaTeam.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
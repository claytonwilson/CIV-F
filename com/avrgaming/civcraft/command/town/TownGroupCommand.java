/*     */ package com.avrgaming.civcraft.command.town;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
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
/*     */ public class TownGroupCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  39 */     this.command = "/town group";
/*  40 */     this.displayName = "Town Group";
/*     */     
/*  42 */     this.commands.put("new", "[name] creates a new group.");
/*  43 */     this.commands.put("delete", "Deletes an empty group.");
/*  44 */     this.commands.put("remove", "[resident] [group] - removes [resident] from group [group]");
/*  45 */     this.commands.put("add", "[resident] [group] - adds [resident] to group [group]");
/*  46 */     this.commands.put("info", "Shows town group information");
/*     */   }
/*     */   
/*     */   public void delete_cmd() throws CivException {
/*  50 */     Town town = getSelectedTown();
/*  51 */     PermissionGroup grp = getNamedPermissionGroup(town, 1);
/*     */     try
/*     */     {
/*  54 */       if (grp.getMemberCount() > 0) {
/*  55 */         throw new CivException("Group must have no members before being deleted.");
/*     */       }
/*     */       
/*  58 */       if (town.isProtectedGroup(grp)) {
/*  59 */         throw new CivException("Cannot delete a protected group.");
/*     */       }
/*     */       
/*  62 */       town.removeGroup(grp);
/*  63 */       town.save();
/*  64 */       grp.delete();
/*     */     }
/*     */     catch (SQLException e) {
/*  67 */       e.printStackTrace();
/*  68 */       throw new CivException("Internal DB Error.");
/*     */     }
/*     */     
/*  71 */     CivMessage.sendSuccess(this.sender, "Deleted group " + this.args[1]);
/*     */   }
/*     */   
/*     */   public void new_cmd() throws CivException {
/*  75 */     if (this.args.length < 2) {
/*  76 */       throw new CivException("You must specify a group name.");
/*     */     }
/*     */     
/*  79 */     Town town = getSelectedTown();
/*  80 */     if (town.hasGroupNamed(this.args[1])) {
/*  81 */       throw new CivException("Town already has a group named " + this.args[1]);
/*     */     }
/*     */     
/*  84 */     if (PermissionGroup.isProtectedGroupName(this.args[1])) {
/*  85 */       throw new CivException("Cannot use this group name, it is a protected group.");
/*     */     }
/*     */     try
/*     */     {
/*  89 */       PermissionGroup grp = new PermissionGroup(town, this.args[1]);
/*     */       
/*  91 */       grp.save();
/*  92 */       town.addGroup(grp);
/*  93 */       town.save();
/*     */     }
/*     */     catch (InvalidNameException e) {
/*  96 */       throw new CivException("Invalid name, please choose another.");
/*     */     }
/*     */     
/*  99 */     CivMessage.sendSuccess(this.sender, "Created group " + this.args[1]);
/*     */   }
/*     */   
/*     */   public void remove_cmd() throws CivException {
/* 103 */     Town town = getSelectedTown();
/* 104 */     Resident commandSenderResidnet = getResident();
/* 105 */     Resident oldMember = getNamedResident(1);
/* 106 */     PermissionGroup grp = getNamedPermissionGroup(town, 2);
/*     */     
/* 108 */     if (grp == town.getMayorGroup()) {
/* 109 */       if (!grp.hasMember(commandSenderResidnet)) {
/* 110 */         throw new CivException("Only Mayors can remove members to the mayors group.");
/*     */       }
/*     */       
/* 113 */       if (grp.getMemberCount() == 1) {
/* 114 */         throw new CivException("There must be at least one member in the mayors group.");
/*     */       }
/*     */     }
/*     */     
/* 118 */     grp.removeMember(oldMember);
/* 119 */     grp.save();
/*     */     
/* 121 */     CivMessage.sendSuccess(this.sender, "Removed " + oldMember.getName() + " from group " + grp.getName() + " in town " + town.getName());
/*     */     try
/*     */     {
/* 124 */       Player newPlayer = CivGlobal.getPlayer(oldMember);
/* 125 */       CivMessage.send(newPlayer, "§cYou were removed from the " + grp.getName() + " group in town " + grp.getTown().getName());
/*     */     }
/*     */     catch (CivException localCivException) {}
/*     */   }
/*     */   
/*     */   public void add_cmd() throws CivException
/*     */   {
/* 132 */     Town town = getSelectedTown();
/* 133 */     Resident commandSenderResident = getResident();
/* 134 */     Resident newMember = getNamedResident(1);
/* 135 */     PermissionGroup grp = getNamedPermissionGroup(town, 2);
/*     */     
/* 137 */     if ((grp == town.getMayorGroup()) && (!grp.hasMember(commandSenderResident)))
/*     */     {
/* 139 */       PermissionGroup leaderGrp = town.getCiv().getLeaderGroup();
/* 140 */       if (leaderGrp == null) {
/* 141 */         throw new CivException("ERROR: Couldn't find leader group for civ " + town.getCiv() + " contact an admin.");
/*     */       }
/*     */       
/* 144 */       if (!leaderGrp.hasMember(commandSenderResident)) {
/* 145 */         throw new CivException("Only Mayors and civ Leaders can add members to the mayors group.");
/*     */       }
/*     */     }
/*     */     
/* 149 */     if ((grp.isProtectedGroup()) && (!newMember.hasTown())) {
/* 150 */       throw new CivException(newMember.getName() + " is not a member of a town/civ so cannot be added to a protected group.");
/*     */     }
/*     */     
/* 153 */     if ((grp.isTownProtectedGroup()) && (newMember.getTown() != grp.getTown())) {
/* 154 */       throw new CivException(newMember.getName() + " belongs to town " + newMember.getTown().getName() + 
/* 155 */         " and cannot be added to a protected group in town " + grp.getTown().getName());
/*     */     }
/*     */     
/* 158 */     if ((grp.isCivProtectedGroup()) && (newMember.getCiv() != grp.getCiv())) {
/* 159 */       throw new CivException(newMember.getName() + " belongs to civ " + newMember.getCiv().getName() + 
/* 160 */         " and cannot be added to a protected group in civ " + grp.getCiv().getName());
/*     */     }
/*     */     
/* 163 */     grp.addMember(newMember);
/* 164 */     grp.save();
/*     */     
/* 166 */     CivMessage.sendSuccess(this.sender, "Added " + newMember.getName() + " to group " + grp.getName() + " in town " + town.getName());
/*     */     try
/*     */     {
/* 169 */       Player newPlayer = CivGlobal.getPlayer(newMember);
/* 170 */       CivMessage.sendSuccess(newPlayer, "You were added to the " + grp.getName() + " group in town " + grp.getTown().getName());
/*     */     }
/*     */     catch (CivException localCivException) {}
/*     */   }
/*     */   
/*     */   public void info_cmd() throws CivException
/*     */   {
/* 177 */     Town town = getSelectedTown();
/*     */     String residents;
/* 179 */     if (this.args.length >= 2) {
/* 180 */       PermissionGroup grp = town.getGroupByName(this.args[1]);
/* 181 */       if (grp == null) {
/* 182 */         throw new CivException("No group named " + this.args[1] + " in " + town.getName());
/*     */       }
/*     */       
/* 185 */       CivMessage.sendHeading(this.sender, "Group(" + town.getName() + "):" + this.args[1]);
/*     */       
/* 187 */       residents = "";
/* 188 */       for (Resident res : grp.getMemberList()) {
/* 189 */         residents = residents + res.getName() + " ";
/*     */       }
/* 191 */       CivMessage.send(this.sender, residents);
/*     */     }
/*     */     else {
/* 194 */       CivMessage.sendHeading(this.sender, town.getName() + " Group Information");
/*     */       
/* 196 */       for (PermissionGroup grp : town.getGroups()) {
/* 197 */         CivMessage.send(this.sender, grp.getName() + "§7" + " (" + grp.getMemberCount() + " members)");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 204 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck() throws CivException
/*     */   {
/* 209 */     validMayorAssistantLeader();
/*     */   }
/*     */   
/*     */ 
/*     */   public void doDefaultAction()
/*     */   {
/* 215 */     showHelp();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\town\TownGroupCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
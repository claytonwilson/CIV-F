/*     */ package com.avrgaming.civcraft.command.civ;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.permission.PermissionGroup;
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
/*     */ public class CivGroupCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  36 */     this.command = "/civ group";
/*  37 */     this.displayName = "Civ Group";
/*     */     
/*  39 */     this.commands.put("add", "[name] [leaders|advisers] - Adds a member to the leader or adviser group.");
/*  40 */     this.commands.put("remove", "[name] [leaders|advisers] - Removes a member to the leader or adviser group.");
/*  41 */     this.commands.put("info", "[leaders|advisers] - Lists members of the leader or adviser group.");
/*     */   }
/*     */   
/*     */   public void remove_cmd() throws CivException
/*     */   {
/*  46 */     Civilization civ = getSenderCiv();
/*  47 */     Resident resident = getResident();
/*  48 */     Resident oldMember = getNamedResident(1);
/*  49 */     String groupName = getNamedString(2, "Enter a group name");
/*     */     
/*  51 */     PermissionGroup grp = null;
/*  52 */     if (groupName.equalsIgnoreCase("leaders")) {
/*  53 */       grp = civ.getLeaderGroup();
/*  54 */     } else if (groupName.equalsIgnoreCase("advisers")) {
/*  55 */       grp = civ.getAdviserGroup();
/*     */     } else {
/*  57 */       throw new CivException("Could not find group " + groupName);
/*     */     }
/*     */     
/*  60 */     if ((grp == civ.getLeaderGroup()) && (!grp.hasMember(resident))) {
/*  61 */       throw new CivException("Only Leaders can remove members to the Leaders group.");
/*     */     }
/*     */     
/*  64 */     if (!grp.hasMember(oldMember)) {
/*  65 */       throw new CivException(oldMember.getName() + " is not a member of this group.");
/*     */     }
/*     */     
/*  68 */     if ((grp == civ.getLeaderGroup()) && (resident == oldMember)) {
/*  69 */       throw new CivException("You cannot removed yourself from the leaders group.");
/*     */     }
/*     */     
/*  72 */     grp.removeMember(oldMember);
/*  73 */     grp.save();
/*  74 */     CivMessage.sendSuccess(this.sender, "Removed " + oldMember.getName() + " from group " + groupName);
/*     */     try {
/*  76 */       Player newPlayer = CivGlobal.getPlayer(oldMember);
/*  77 */       CivMessage.send(newPlayer, "§cYou were removed from the " + groupName + " group in civ " + civ.getName());
/*     */     }
/*     */     catch (CivException localCivException) {}
/*     */   }
/*     */   
/*     */   public void add_cmd() throws CivException
/*     */   {
/*  84 */     Civilization civ = getSenderCiv();
/*  85 */     Resident resident = getResident();
/*  86 */     Resident newMember = getNamedResident(1);
/*  87 */     String groupName = getNamedString(2, "Enter a group name");
/*     */     
/*  89 */     PermissionGroup grp = null;
/*  90 */     if (groupName.equalsIgnoreCase("leaders")) {
/*  91 */       grp = civ.getLeaderGroup();
/*  92 */     } else if (groupName.equalsIgnoreCase("advisers")) {
/*  93 */       grp = civ.getAdviserGroup();
/*     */     } else {
/*  95 */       throw new CivException("Could not find group " + groupName);
/*     */     }
/*     */     
/*  98 */     if ((grp == civ.getLeaderGroup()) && (!grp.hasMember(resident))) {
/*  99 */       throw new CivException("Only Leaders can add members to the leaders group.");
/*     */     }
/*     */     
/* 102 */     if (newMember.getCiv() != civ) {
/* 103 */       throw new CivException("Cannot add non-civ members to leaders or adviser groups.");
/*     */     }
/*     */     
/* 106 */     grp.addMember(newMember);
/* 107 */     grp.save();
/*     */     
/* 109 */     CivMessage.sendSuccess(this.sender, "Added " + newMember.getName() + " to group " + groupName);
/*     */     try
/*     */     {
/* 112 */       Player newPlayer = CivGlobal.getPlayer(newMember);
/* 113 */       CivMessage.sendSuccess(newPlayer, "You were added to the " + groupName + " group in civ " + civ.getName());
/*     */     }
/*     */     catch (CivException localCivException) {}
/*     */   }
/*     */   
/*     */   public void info_cmd()
/*     */     throws CivException
/*     */   {
/* 121 */     Civilization civ = getSenderCiv();
/*     */     
/* 123 */     if (this.args.length > 1) {
/* 124 */       PermissionGroup grp = null;
/* 125 */       if (this.args[1].equalsIgnoreCase("leaders")) {
/* 126 */         grp = civ.getLeaderGroup();
/* 127 */       } else if (this.args[1].equalsIgnoreCase("advisers")) {
/* 128 */         grp = civ.getAdviserGroup();
/*     */       } else {
/* 130 */         throw new CivException("Could not find group " + this.args[1]);
/*     */       }
/*     */       
/* 133 */       CivMessage.sendHeading(this.sender, "Group:" + this.args[1]);
/*     */       
/* 135 */       String residents = "";
/* 136 */       for (Resident res : grp.getMemberList()) {
/* 137 */         residents = residents + res.getName() + " ";
/*     */       }
/* 139 */       CivMessage.send(this.sender, residents);
/*     */     }
/*     */     else {
/* 142 */       CivMessage.sendHeading(this.sender, "Civ Group Information");
/*     */       
/* 144 */       PermissionGroup grp = civ.getLeaderGroup();
/* 145 */       CivMessage.send(this.sender, grp.getName() + "§7" + " (" + grp.getMemberCount() + " members)");
/*     */       
/* 147 */       grp = civ.getAdviserGroup();
/* 148 */       CivMessage.send(this.sender, grp.getName() + "§7" + " (" + grp.getMemberCount() + " members)");
/*     */     }
/*     */   }
/*     */   
/*     */   public void doDefaultAction()
/*     */     throws CivException
/*     */   {
/* 155 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 160 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck() throws CivException
/*     */   {
/* 165 */     validLeaderAdvisor();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\civ\CivGroupCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
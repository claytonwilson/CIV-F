/*     */ package com.avrgaming.civcraft.command.camp;
/*     */ 
/*     */ import com.avrgaming.civcraft.camp.Camp;
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.questions.JoinCampResponse;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
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
/*     */ public class CampCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public static final long INVITE_TIMEOUT = 30000L;
/*     */   
/*     */   public void init()
/*     */   {
/*  42 */     this.command = "/camp";
/*  43 */     this.displayName = "Camp";
/*     */     
/*  45 */     this.commands.put("undo", "Unbuilds the camp, issues a refund.");
/*  46 */     this.commands.put("add", "[name] - adds this player to our camp.");
/*  47 */     this.commands.put("remove", "[name] - removes this player from our camp.");
/*  48 */     this.commands.put("leave", "Leaves the current camp you're in.");
/*  49 */     this.commands.put("setowner", "[name] - Sets the camp's owner to the player name you give. They must be a current member.");
/*  50 */     this.commands.put("info", "Shows information about your current camp.");
/*  51 */     this.commands.put("disband", "Disbands this camp.");
/*  52 */     this.commands.put("upgrade", "Manage camp upgrades.");
/*     */   }
/*     */   
/*     */   public void upgrade_cmd() {
/*  56 */     CampUpgradeCommand cmd = new CampUpgradeCommand();
/*  57 */     cmd.onCommand(this.sender, null, "camp", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void info_cmd() throws CivException {
/*  61 */     Camp camp = getCurrentCamp();
/*  62 */     SimpleDateFormat sdf = new SimpleDateFormat("M/dd h:mm:ss a z");
/*     */     
/*  64 */     CivMessage.sendHeading(this.sender, "Camp " + camp.getName() + " Info");
/*  65 */     HashMap<String, String> info = new HashMap();
/*  66 */     info.put("Owner", camp.getOwnerName());
/*  67 */     info.put("Members", camp.getMembers().size());
/*  68 */     info.put("Next Raid", sdf.format(camp.getNextRaidDate()));
/*  69 */     CivMessage.send(this.sender, makeInfoString(info, "§2", "§a"));
/*     */     
/*  71 */     info.clear();
/*  72 */     info.put("Hours of Fire Left", camp.getFirepoints());
/*  73 */     info.put("Longhouse Level", camp.getLonghouseLevel() + camp.getLonghouseCountString());
/*  74 */     CivMessage.send(this.sender, makeInfoString(info, "§2", "§a"));
/*     */     
/*  76 */     info.clear();
/*  77 */     info.put("Members", camp.getMembersString());
/*  78 */     CivMessage.send(this.sender, makeInfoString(info, "§2", "§a"));
/*     */   }
/*     */   
/*     */   public void remove_cmd() throws CivException {
/*  82 */     validCampOwner();
/*  83 */     Camp camp = getCurrentCamp();
/*  84 */     Resident resident = getNamedResident(1);
/*     */     
/*  86 */     if ((!resident.hasCamp()) || (resident.getCamp() != camp)) {
/*  87 */       throw new CivException(resident.getName() + " does not belong to this camp.");
/*     */     }
/*     */     
/*  90 */     if (resident.getCamp().getOwner() == resident) {
/*  91 */       throw new CivException("Cannot remove the owner of the camp from his own camp!");
/*     */     }
/*     */     
/*  94 */     camp.removeMember(resident);
/*  95 */     CivMessage.sendSuccess(this.sender, "Removed " + resident.getName() + " from this camp.");
/*     */   }
/*     */   
/*     */   public void add_cmd() throws CivException {
/*  99 */     validCampOwner();
/* 100 */     Camp camp = getCurrentCamp();
/* 101 */     Resident resident = getNamedResident(1);
/* 102 */     Player player = getPlayer();
/*     */     
/* 104 */     if (resident.hasCamp()) {
/* 105 */       throw new CivException("This resident already belongs to a camp.");
/*     */     }
/*     */     
/* 108 */     if (resident.hasTown()) {
/* 109 */       throw new CivException("This resident belongs to a town and cannot join a camp.");
/*     */     }
/*     */     
/* 112 */     JoinCampResponse join = new JoinCampResponse();
/* 113 */     join.camp = camp;
/* 114 */     join.resident = resident;
/* 115 */     join.sender = player;
/*     */     
/* 117 */     CivGlobal.questionPlayer(player, CivGlobal.getPlayer(resident), 
/* 118 */       "Would you like to join the camp owned by " + player.getName() + "?", 
/* 119 */       30000L, join);
/*     */     
/* 121 */     CivMessage.sendSuccess(player, "Invited " + resident.getName() + " to our camp.");
/*     */   }
/*     */   
/*     */   public void setowner_cmd() throws CivException {
/* 125 */     validCampOwner();
/* 126 */     Camp camp = getCurrentCamp();
/* 127 */     Resident newLeader = getNamedResident(1);
/*     */     
/* 129 */     if (!camp.hasMember(newLeader.getName())) {
/* 130 */       throw new CivException(newLeader.getName() + " is not a member of the camp and cannot be set as the owner.");
/*     */     }
/*     */     
/* 133 */     camp.setOwner(newLeader);
/* 134 */     camp.save();
/*     */     
/* 136 */     Player player = CivGlobal.getPlayer(newLeader);
/* 137 */     CivMessage.sendSuccess(player, "You are now the proud owner of the camp you're in.");
/* 138 */     CivMessage.sendSuccess(this.sender, "Transfered camp ownership to " + newLeader.getName());
/*     */   }
/*     */   
/*     */   public void leave_cmd() throws CivException
/*     */   {
/* 143 */     Resident resident = getResident();
/*     */     
/* 145 */     if (!resident.hasCamp()) {
/* 146 */       throw new CivException("You are not currently in a camp.");
/*     */     }
/*     */     
/* 149 */     Camp camp = resident.getCamp();
/* 150 */     if (camp.getOwner() == resident) {
/* 151 */       throw new CivException("The owner of the camp cannot leave it. Try /camp setowner to give it to someone else or use /camp disband to abondon the camp.");
/*     */     }
/*     */     
/* 154 */     camp.removeMember(resident);
/* 155 */     camp.save();
/* 156 */     CivMessage.sendSuccess(this.sender, "You've left camp " + camp.getName());
/*     */   }
/*     */   
/*     */   public void new_cmd() throws CivException
/*     */   {}
/*     */   
/*     */   public void disband_cmd() throws CivException
/*     */   {
/* 164 */     Resident resident = getResident();
/* 165 */     validCampOwner();
/* 166 */     Camp camp = getCurrentCamp();
/*     */     
/* 168 */     if (!resident.hasCamp()) {
/* 169 */       throw new CivException("You are not part of a camp.");
/*     */     }
/*     */     
/* 172 */     camp.disband();
/* 173 */     CivMessage.sendSuccess(this.sender, "Camp disbanded.");
/*     */   }
/*     */   
/*     */   public void undo_cmd() throws CivException {
/* 177 */     Resident resident = getResident();
/*     */     
/* 179 */     if (!resident.hasCamp()) {
/* 180 */       throw new CivException("You are not part of a camp.");
/*     */     }
/*     */     
/* 183 */     Camp camp = resident.getCamp();
/* 184 */     if (camp.getOwner() != resident) {
/* 185 */       throw new CivException("Only the camp owner " + camp.getOwner().getName() + " can disband this camp.");
/*     */     }
/*     */     
/* 188 */     if (!camp.isUndoable()) {
/* 189 */       throw new CivException("This camp can no longer be unbuilt. Use /camp disband instead.");
/*     */     }
/*     */     
/* 192 */     LoreCraftableMaterial campMat = LoreCraftableMaterial.getCraftMaterialFromId("mat_found_camp");
/* 193 */     if (campMat == null) {
/* 194 */       throw new CivException("Cannot undo camp. Internal error. Contact an admin.");
/*     */     }
/*     */     
/* 197 */     ItemStack newStack = LoreCraftableMaterial.spawn(campMat);
/* 198 */     Player player = CivGlobal.getPlayer(resident);
/* 199 */     HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(new ItemStack[] { newStack });
/* 200 */     for (ItemStack stack : leftovers.values()) {
/* 201 */       player.getWorld().dropItem(player.getLocation(), stack);
/* 202 */       CivMessage.send(player, "§7Your camp item was dropped on the ground because your inventory was full.");
/*     */     }
/*     */     
/* 205 */     camp.undo();
/* 206 */     CivMessage.sendSuccess(this.sender, "Unbuilt camp. You were refunded your Camp.");
/*     */   }
/*     */   
/*     */ 
/*     */   public void doDefaultAction()
/*     */     throws CivException
/*     */   {
/* 213 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 218 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck()
/*     */     throws CivException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\camp\CampCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
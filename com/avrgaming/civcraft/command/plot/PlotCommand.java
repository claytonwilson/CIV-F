/*     */ package com.avrgaming.civcraft.command.plot;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.object.TownChunk;
/*     */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*     */ import com.avrgaming.civcraft.permission.PlotPermissions;
/*     */ import com.avrgaming.civcraft.structure.Farm;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import com.avrgaming.civcraft.structure.farm.FarmChunk;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
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
/*     */ public class PlotCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  41 */     this.command = "/plot";
/*  42 */     this.displayName = "Plot";
/*     */     
/*  44 */     this.commands.put("info", "Show info for the plot you're standing on.");
/*  45 */     this.commands.put("toggle", "[mobs]|[fire] toggles mob spawning or fire in this plot.");
/*  46 */     this.commands.put("perm", "View/Modify permissions.");
/*  47 */     this.commands.put("fs", "[amount] - puts plot up for sale for this amount.");
/*  48 */     this.commands.put("nfs", "Makes plot not for sale.");
/*  49 */     this.commands.put("buy", "Buys the plot your standing on.");
/*  50 */     this.commands.put("addgroup", "[name] - adds this group to the plot.");
/*  51 */     this.commands.put("setowner", "[name|none] Sets the owner on this plot(gives it away).");
/*  52 */     this.commands.put("farminfo", "Special information about this plot if it is a farm plot.");
/*  53 */     this.commands.put("removegroup", "[name] - removes this group from the plot.");
/*  54 */     this.commands.put("cleargroups", "Clears all groups from this plot.");
/*     */   }
/*     */   
/*     */   public void farminfo_cmd() throws CivException {
/*  58 */     Player player = getPlayer();
/*     */     
/*  60 */     ChunkCoord coord = new ChunkCoord(player.getLocation());
/*  61 */     FarmChunk fc = CivGlobal.getFarmChunk(coord);
/*     */     
/*  63 */     if (fc == null) {
/*  64 */       throw new CivException("This chunk is not a farm chunk.");
/*     */     }
/*     */     
/*  67 */     if (!fc.getStruct().isActive()) {
/*  68 */       throw new CivException("This chunk is a farm, but the structure is not finished building yet.");
/*     */     }
/*     */     
/*  71 */     String dateString = "Never";
/*     */     
/*  73 */     if (fc.getLastGrowDate() != null) {
/*  74 */       SimpleDateFormat sdf = new SimpleDateFormat("M/d/y k:m:s z");
/*  75 */       dateString = sdf.format(fc.getLastGrowDate());
/*     */     }
/*     */     
/*  78 */     CivMessage.sendHeading(this.sender, "Farm Plot Info");
/*  79 */     CivMessage.send(this.sender, "§2Last Grow Time: §a" + dateString);
/*  80 */     CivMessage.send(this.sender, "§2Last Grow Amount: §a" + fc.getLastGrowTickCount());
/*  81 */     CivMessage.send(this.sender, "§2Growth Ticks While Unloaded: §a" + fc.getMissedGrowthTicksStat());
/*  82 */     CivMessage.send(this.sender, "§2Last Effective Growth Rate: §a" + this.df.format(fc.getFarm().getLastEffectiveGrowthRate() * 100.0D) + "%");
/*     */     
/*  84 */     String success = "no";
/*  85 */     if (fc.getLastRandomInt() < fc.getLastChanceForLast()) {
/*  86 */       success = "yes";
/*     */     }
/*     */     
/*  89 */     CivMessage.send(this.sender, "§2Last Extra Grow Chance: §a" + fc.getLastChanceForLast() + " vs " + "§a" + fc.getLastRandomInt() + " success? " + "§a" + success);
/*     */     
/*  91 */     String out = "";
/*  92 */     for (BlockCoord bcoord : fc.getLastGrownCrops()) {
/*  93 */       out = out + bcoord.toString() + ", ";
/*     */     }
/*     */     
/*  96 */     CivMessage.send(this.sender, "§2Crops Grown: §a" + out);
/*     */   }
/*     */   
/*     */   public void setowner_cmd()
/*     */     throws CivException
/*     */   {
/* 102 */     TownChunk tc = getStandingTownChunk();
/* 103 */     validPlotOwner();
/*     */     
/* 105 */     if (this.args.length < 2) {
/* 106 */       throw new CivException("You must specifiy and owner.");
/*     */     }
/*     */     
/* 109 */     if (this.args[1].equalsIgnoreCase("none")) {
/* 110 */       tc.perms.setOwner(null);
/* 111 */       tc.save();
/* 112 */       CivMessage.sendSuccess(this.sender, "Set plot owner to none, returned plot to town.");
/* 113 */       return;
/*     */     }
/*     */     
/* 116 */     Resident resident = getNamedResident(1);
/*     */     
/* 118 */     if (resident.getTown() != tc.getTown()) {
/* 119 */       throw new CivException("Resident must be a member of this town.");
/*     */     }
/*     */     
/* 122 */     tc.perms.setOwner(resident);
/* 123 */     tc.save();
/*     */     
/* 125 */     CivMessage.sendSuccess(this.sender, "Plot is now owned by " + this.args[1]);
/*     */   }
/*     */   
/*     */   public void removegroup_cmd() throws CivException
/*     */   {
/* 130 */     TownChunk tc = getStandingTownChunk();
/* 131 */     validPlotOwner();
/*     */     
/* 133 */     if (this.args.length < 2) {
/* 134 */       throw new CivException("You must specify a group name.");
/*     */     }
/*     */     
/* 137 */     if (this.args[1].equalsIgnoreCase("none")) {
/* 138 */       throw new CivException("To clear the groups use the 'cleargroups' command instead.");
/*     */     }
/*     */     
/* 141 */     PermissionGroup grp = tc.getTown().getGroupByName(this.args[1]);
/* 142 */     if (grp == null) {
/* 143 */       throw new CivException("Could not find group named " + this.args[1] + " in this town.");
/*     */     }
/*     */     
/* 146 */     tc.perms.removeGroup(grp);
/* 147 */     tc.save();
/*     */     
/* 149 */     CivMessage.sendSuccess(this.sender, "Removed plot group " + grp.getName());
/*     */   }
/*     */   
/*     */   public void cleargroups_cmd() throws CivException {
/* 153 */     TownChunk tc = getStandingTownChunk();
/* 154 */     validPlotOwner();
/*     */     
/* 156 */     tc.perms.clearGroups();
/* 157 */     tc.save();
/* 158 */     CivMessage.sendSuccess(this.sender, "Cleared the plot's groups.");
/*     */   }
/*     */   
/*     */   public void addgroup_cmd() throws CivException
/*     */   {
/* 163 */     TownChunk tc = getStandingTownChunk();
/* 164 */     validPlotOwner();
/*     */     
/* 166 */     if (this.args.length < 2) {
/* 167 */       throw new CivException("You must specify a group name.");
/*     */     }
/*     */     
/* 170 */     if (this.args[1].equalsIgnoreCase("none")) {
/* 171 */       throw new CivException("To clear the groups use the 'cleargroups' command instead.");
/*     */     }
/*     */     
/*     */ 
/* 175 */     PermissionGroup grp = tc.getTown().getGroupByName(this.args[1]);
/* 176 */     if (grp == null) {
/* 177 */       throw new CivException("Could not find group named " + this.args[1] + " in this town.");
/*     */     }
/*     */     
/* 180 */     tc.perms.addGroup(grp);
/* 181 */     tc.save();
/*     */     
/* 183 */     CivMessage.sendSuccess(this.sender, "Added plot group " + grp.getName());
/*     */   }
/*     */   
/*     */   public void buy_cmd() throws CivException {
/* 187 */     TownChunk tc = getStandingTownChunk();
/* 188 */     Resident resident = getResident();
/*     */     
/* 190 */     if (tc.isOutpost()) {
/* 191 */       throw new CivException("Cannot buy outposts.");
/*     */     }
/*     */     
/* 194 */     if (resident.getTown() != tc.getTown()) {
/* 195 */       throw new CivException("You cannot buy this plot, you are not a member of this town.");
/*     */     }
/*     */     
/* 198 */     if (!tc.isForSale()) {
/* 199 */       throw new CivException("This plot is not for sale.");
/*     */     }
/*     */     
/* 202 */     tc.purchase(resident);
/* 203 */     CivMessage.sendSuccess(this.sender, "Purchased plot " + tc.getChunkCoord() + " for " + tc.getValue() + " coins.");
/*     */   }
/*     */   
/*     */   public void fs_cmd() throws CivException {
/* 207 */     TownChunk tc = getStandingTownChunk();
/* 208 */     validPlotOwner();
/*     */     
/* 210 */     if (tc.isOutpost()) {
/* 211 */       throw new CivException("Cannot sell outposts.");
/*     */     }
/*     */     
/* 214 */     if (this.args.length < 2) {
/* 215 */       throw new CivException("You must specify a price.");
/*     */     }
/*     */     try
/*     */     {
/* 219 */       double price = Double.valueOf(this.args[1]).doubleValue();
/* 220 */       tc.setForSale(true);
/* 221 */       tc.setPrice(price);
/* 222 */       tc.save();
/*     */     } catch (NumberFormatException e) {
/* 224 */       throw new CivException(this.args[1] + " could not be read as a number.");
/*     */     }
/*     */     
/* 227 */     CivMessage.sendTown(tc.getTown(), "Placed plot " + tc.getCenterString() + " up for sale at " + this.args[1] + " coins.");
/*     */   }
/*     */   
/*     */   public void nfs_cmd() throws CivException
/*     */   {
/* 232 */     TownChunk tc = getStandingTownChunk();
/* 233 */     validPlotOwner();
/*     */     try
/*     */     {
/* 236 */       tc.setForSale(false);
/* 237 */       tc.save();
/*     */     } catch (NumberFormatException e) {
/* 239 */       throw new CivException(this.args[1] + " could not be read as a number.");
/*     */     }
/*     */     
/* 242 */     CivMessage.sendTown(tc.getTown(), "Plot " + tc.getCenterString() + " is no longer up for sale.");
/*     */   }
/*     */   
/*     */   public void toggle_cmd() throws CivException {
/* 246 */     TownChunk tc = getStandingTownChunk();
/* 247 */     validPlotOwner();
/*     */     
/* 249 */     if (this.args.length < 2) {
/* 250 */       throw new CivException("Please specifiy mobs or fire to toggle.");
/*     */     }
/*     */     
/* 253 */     if (this.args[1].equalsIgnoreCase("mobs")) {
/* 254 */       if (tc.perms.isMobs()) {
/* 255 */         tc.perms.setMobs(false);
/*     */       } else {
/* 257 */         tc.perms.setMobs(true);
/*     */       }
/*     */       
/* 260 */       CivMessage.sendSuccess(this.sender, "Set mob spawning on this plot to " + tc.perms.isMobs());
/*     */     }
/* 262 */     else if (this.args[1].equalsIgnoreCase("fire")) {
/* 263 */       if (tc.perms.isFire()) {
/* 264 */         tc.perms.setFire(false);
/*     */       } else {
/* 266 */         tc.perms.setFire(true);
/*     */       }
/* 268 */       CivMessage.sendSuccess(this.sender, "Set fire on this plot to " + tc.perms.isFire());
/*     */     }
/* 270 */     tc.save();
/*     */   }
/*     */   
/*     */   public void perm_cmd() throws CivException {
/* 274 */     PlotPermCommand cmd = new PlotPermCommand();
/* 275 */     cmd.onCommand(this.sender, null, "perm", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   private void showCurrentPermissions(TownChunk tc) {
/* 279 */     CivMessage.send(this.sender, "§2Build: §a" + tc.perms.getBuildString());
/* 280 */     CivMessage.send(this.sender, "§2Destroy: §a" + tc.perms.getDestroyString());
/* 281 */     CivMessage.send(this.sender, "§2Interact: §a" + tc.perms.getInteractString());
/* 282 */     CivMessage.send(this.sender, "§2Item Use: §a" + tc.perms.getItemUseString());
/*     */   }
/*     */   
/*     */   private void showPermOwnership(TownChunk tc) {
/* 286 */     String out = "§2Town: §a" + tc.getTown().getName();
/* 287 */     out = out + "§2 Owner: §a";
/* 288 */     if (tc.perms.getOwner() != null) {
/* 289 */       out = out + tc.perms.getOwner().getName();
/*     */     } else {
/* 291 */       out = out + "none";
/*     */     }
/*     */     
/* 294 */     out = out + "§2 Group: §a";
/* 295 */     if (tc.perms.getGroups().size() != 0) {
/* 296 */       out = out + tc.perms.getGroupString();
/*     */     } else {
/* 298 */       out = out + "none";
/*     */     }
/*     */     
/* 301 */     CivMessage.send(this.sender, out);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void info_cmd()
/*     */     throws CivException
/*     */   {
/* 311 */     if ((this.sender instanceof Player)) {
/* 312 */       Player player = (Player)this.sender;
/*     */       
/* 314 */       TownChunk tc = CivGlobal.getTownChunk(player.getLocation());
/* 315 */       if (tc == null) {
/* 316 */         throw new CivException("Plot is not owned.");
/*     */       }
/*     */       
/* 319 */       CivMessage.sendHeading(this.sender, "Plot Information");
/* 320 */       showPermOwnership(tc);
/* 321 */       showCurrentPermissions(tc);
/* 322 */       showToggles(tc);
/* 323 */       showPriceInfo(tc);
/*     */     }
/*     */   }
/*     */   
/*     */   private void showToggles(TownChunk tc)
/*     */   {
/* 329 */     CivMessage.send(this.sender, "§2Mobs: §a" + tc.perms.isMobs() + " " + 
/* 330 */       "§2" + "Fire: " + "§a" + tc.perms.isFire());
/*     */   }
/*     */   
/*     */   private void showPriceInfo(TownChunk tc) {
/* 334 */     String out = "";
/* 335 */     if (tc.isForSale()) {
/* 336 */       out = out + "§e [For Sale at " + tc.getPrice() + " coins] ";
/*     */     }
/* 338 */     CivMessage.send(this.sender, "§2Value: §a" + tc.getValue() + out);
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 343 */     showBasicHelp();
/*     */   }
/*     */   
/*     */ 
/*     */   public void permissionCheck() {}
/*     */   
/*     */ 
/*     */   public void doDefaultAction()
/*     */     throws CivException
/*     */   {
/* 353 */     showHelp();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\plot\PlotCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.avrgaming.civcraft.command;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigBuildableInfo;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import com.avrgaming.civcraft.structure.wonders.Wonder;
/*     */ import com.avrgaming.civcraft.threading.tasks.BuildAsyncTask;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.war.War;
/*     */ import java.io.IOException;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class BuildCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  45 */     this.command = "/build";
/*  46 */     this.displayName = "Build";
/*  47 */     this.sendUnknownToDefault = true;
/*     */     
/*  49 */     this.commands.put("list", "shows all available structures.");
/*  50 */     this.commands.put("progress", "Shows progress of currently building structures.");
/*  51 */     this.commands.put("repairnearest", "Repairs destroyed structures.");
/*  52 */     this.commands.put("undo", "Undo the last structure built.");
/*  53 */     this.commands.put("demolish", "[location] - destroys the structure at this location.");
/*  54 */     this.commands.put("demolishnearest", "- destroys the nearest structure. Requires confirmation.");
/*  55 */     this.commands.put("refreshnearest", "Refreshes the nearest structure's blocks. Requires confirmation.");
/*  56 */     this.commands.put("validatenearest", "Validates the nearest structure. Removing any validation penalties if it's ok.");
/*     */   }
/*     */   
/*     */   public void validatenearest_cmd() throws CivException
/*     */   {
/*  61 */     Player player = getPlayer();
/*  62 */     Resident resident = getResident();
/*  63 */     Buildable buildable = CivGlobal.getNearestBuildable(player.getLocation());
/*     */     
/*  65 */     if (buildable.getTown() != resident.getTown()) {
/*  66 */       throw new CivException("You can only validate structures inside your own town.");
/*     */     }
/*     */     
/*  69 */     if (War.isWarTime()) {
/*  70 */       throw new CivException("Cannot validate structures during WarTime.");
/*     */     }
/*     */     
/*  73 */     if (buildable.isIgnoreFloating()) {
/*  74 */       throw new CivException(buildable.getDisplayName() + " is exempt from floating structure checks.");
/*     */     }
/*     */     
/*  77 */     CivMessage.sendSuccess(player, "Running Validation on " + buildable.getDisplayName() + " at " + buildable.getCenterLocation() + "...");
/*  78 */     buildable.validate(player);
/*     */   }
/*     */   
/*     */   public void refreshnearest_cmd() throws CivException {
/*  82 */     Town town = getSelectedTown();
/*  83 */     Resident resident = getResident();
/*  84 */     town.refreshNearestBuildable(resident);
/*     */   }
/*     */   
/*     */   public void repairnearest_cmd() throws CivException {
/*  88 */     Town town = getSelectedTown();
/*  89 */     Player player = getPlayer();
/*     */     
/*  91 */     if (War.isWarTime()) {
/*  92 */       throw new CivException("You cannot repair structures during WarTime.");
/*     */     }
/*     */     
/*  95 */     Structure nearest = town.getNearestStrucutre(player.getLocation());
/*     */     
/*  97 */     if (nearest == null) {
/*  98 */       throw new CivException("Couldn't find a structure.");
/*     */     }
/*     */     
/* 101 */     if (!nearest.isDestroyed()) {
/* 102 */       throw new CivException(nearest.getDisplayName() + " at " + nearest.getCorner() + " is not destroyed.");
/*     */     }
/*     */     
/* 105 */     if (!town.getCiv().hasTechnology(nearest.getRequiredTechnology())) {
/* 106 */       throw new CivException("You do not have the technology to repair " + nearest.getDisplayName() + " at " + nearest.getCorner());
/*     */     }
/*     */     
/* 109 */     if ((this.args.length < 2) || (!this.args[1].equalsIgnoreCase("yes"))) {
/* 110 */       CivMessage.send(player, "§aAre you sure you want to repair the structure §e" + nearest.getDisplayName() + 
/* 111 */         "§a" + " at " + "§e" + nearest.getCorner() + "§a" + " for " + "§e" + nearest.getRepairCost() + " coins?");
/* 112 */       CivMessage.send(player, "§7If yes, use /build repairnearest yes");
/* 113 */       return;
/*     */     }
/*     */     
/* 116 */     town.repairStructure(nearest);
/* 117 */     CivMessage.sendSuccess(player, nearest.getDisplayName() + " repaired.");
/*     */   }
/*     */   
/*     */   public void demolishnearest_cmd() throws CivException {
/* 121 */     Town town = getSelectedTown();
/* 122 */     Player player = getPlayer();
/*     */     
/* 124 */     Structure nearest = town.getNearestStrucutre(player.getLocation());
/*     */     
/* 126 */     if (nearest == null) {
/* 127 */       throw new CivException("Couldn't find a structure.");
/*     */     }
/*     */     
/* 130 */     if ((this.args.length < 2) || (!this.args[1].equalsIgnoreCase("yes"))) {
/* 131 */       CivMessage.send(player, "§aAre you sure you want to demolish the structure §e" + nearest.getDisplayName() + 
/* 132 */         "§a" + " at " + "§e" + nearest.getCorner() + "§a" + " ?");
/* 133 */       CivMessage.send(player, "§7If yes, use /build demolishnearest yes");
/*     */       
/* 135 */       nearest.flashStructureBlocks();
/* 136 */       return;
/*     */     }
/*     */     
/* 139 */     town.demolish(nearest, false);
/* 140 */     CivMessage.sendSuccess(player, nearest.getDisplayName() + " at " + nearest.getCorner() + " demolished.");
/*     */   }
/*     */   
/*     */   public void demolish_cmd() throws CivException
/*     */   {
/* 145 */     Town town = getSelectedTown();
/*     */     
/*     */ 
/* 148 */     if (this.args.length < 2) {
/* 149 */       CivMessage.sendHeading(this.sender, "Demolish Structure");
/* 150 */       for (Structure struct : town.getStructures()) {
/* 151 */         CivMessage.send(this.sender, struct.getDisplayName() + " type: " + "§e" + struct.getCorner().toString() + 
/* 152 */           "§f" + " to demolish");
/*     */       }
/* 154 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 158 */       BlockCoord coord = new BlockCoord(this.args[1]);
/* 159 */       Structure struct = town.getStructure(coord);
/* 160 */       if (struct == null) {
/* 161 */         CivMessage.send(this.sender, "§cNo structure at " + this.args[1]);
/* 162 */         return;
/*     */       }
/* 164 */       struct.getTown().demolish(struct, false);
/* 165 */       CivMessage.sendTown(struct.getTown(), struct.getDisplayName() + " has been demolished.");
/*     */     } catch (NumberFormatException|ArrayIndexOutOfBoundsException e) {
/* 167 */       CivMessage.sendError(this.sender, "Bad formatting. make sure to enter the text *exactly* as shown in yellow.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void undo_cmd() throws CivException {
/* 172 */     Town town = getSelectedTown();
/* 173 */     town.processUndo();
/*     */   }
/*     */   
/*     */   public void progress_cmd() throws CivException {
/* 177 */     CivMessage.sendHeading(this.sender, "Building Structures");
/* 178 */     Town town = getSelectedTown();
/* 179 */     for (BuildAsyncTask task : town.build_tasks) {
/* 180 */       Buildable b = task.buildable;
/* 181 */       DecimalFormat df = new DecimalFormat();
/*     */       
/* 183 */       CivMessage.send(this.sender, "§d" + b.getDisplayName() + ": " + "§e" + "(" + df.format(b.getBuiltHammers()) + "/" + b.getHammerCost() + ")" + 
/* 184 */         "§d" + " Blocks " + "§e" + "(" + b.builtBlockCount + "/" + b.getTotalBlockCount() + ")");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void list_available_structures()
/*     */     throws CivException
/*     */   {
/* 193 */     CivMessage.sendHeading(this.sender, "Available Structures");
/* 194 */     Town town = getSelectedTown();
/* 195 */     for (ConfigBuildableInfo sinfo : CivSettings.structures.values()) {
/* 196 */       if (sinfo.isAvailable(town)) {
/* 197 */         String leftString = "";
/* 198 */         if (sinfo.limit == 0) {
/* 199 */           leftString = "Unlimited";
/*     */         } else {
/* 201 */           leftString = sinfo.limit - town.getStructureTypeCount(sinfo.id);
/*     */         }
/*     */         
/* 204 */         CivMessage.send(this.sender, "§d" + sinfo.displayName + 
/* 205 */           "§e" + 
/* 206 */           " Cost: " + sinfo.cost + 
/* 207 */           " Upkeep: " + sinfo.upkeep + " Hammers: " + sinfo.hammer_cost + 
/* 208 */           " Left: " + leftString);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void list_available_wonders() throws CivException {
/* 214 */     CivMessage.sendHeading(this.sender, "Available Wonders");
/* 215 */     Town town = getSelectedTown();
/* 216 */     for (ConfigBuildableInfo sinfo : CivSettings.wonders.values()) {
/* 217 */       if (sinfo.isAvailable(town)) {
/* 218 */         String leftString = "";
/* 219 */         if (sinfo.limit == 0) {
/* 220 */           leftString = "Unlimited";
/*     */         } else {
/* 222 */           leftString = sinfo.limit - town.getStructureTypeCount(sinfo.id);
/*     */         }
/*     */         
/* 225 */         if (Wonder.isWonderAvailable(sinfo.id)) {
/* 226 */           CivMessage.send(this.sender, "§d" + sinfo.displayName + 
/* 227 */             "§e" + 
/* 228 */             " Cost: " + sinfo.cost + 
/* 229 */             " Upkeep: " + sinfo.upkeep + " Hammers: " + sinfo.hammer_cost + 
/* 230 */             " Left: " + leftString);
/*     */         } else {
/* 232 */           Wonder wonder = CivGlobal.getWonderByConfigId(sinfo.id);
/* 233 */           CivMessage.send(this.sender, "§7" + sinfo.displayName + " Cost: " + sinfo.cost + " - Already built in " + 
/* 234 */             wonder.getTown().getName() + "(" + wonder.getTown().getCiv().getName() + ")");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void list_cmd() throws CivException {
/* 241 */     list_available_structures();
/* 242 */     list_available_wonders();
/*     */   }
/*     */   
/*     */   public void doDefaultAction() throws CivException
/*     */   {
/* 247 */     if (this.args.length == 0) {
/* 248 */       showHelp();
/* 249 */       return;
/*     */     }
/*     */     
/* 252 */     String fullArgs = "";
/* 253 */     String[] arrayOfString; int j = (arrayOfString = this.args).length; for (int i = 0; i < j; i++) { String arg = arrayOfString[i];
/* 254 */       fullArgs = fullArgs + arg + " ";
/*     */     }
/* 256 */     fullArgs = fullArgs.trim();
/*     */     
/* 258 */     buildByName(fullArgs);
/*     */   }
/*     */   
/*     */   public void preview_cmd() throws CivException {
/* 262 */     String fullArgs = combineArgs(stripArgs(this.args, 1));
/*     */     
/* 264 */     ConfigBuildableInfo sinfo = CivSettings.getBuildableInfoByName(fullArgs);
/* 265 */     if (sinfo == null) {
/* 266 */       throw new CivException("Unknown structure " + fullArgs);
/*     */     }
/*     */     
/* 269 */     Town town = getSelectedTown();
/* 270 */     if (sinfo.isWonder.booleanValue()) {
/* 271 */       Wonder wonder = Wonder.newWonder(getPlayer().getLocation(), sinfo.id, town);
/*     */       try {
/* 273 */         wonder.buildPlayerPreview(getPlayer(), getPlayer().getLocation());
/*     */       } catch (IOException e) {
/* 275 */         e.printStackTrace();
/* 276 */         throw new CivException("Internal IO Error.");
/*     */       }
/*     */     } else {
/* 279 */       Structure struct = Structure.newStructure(getPlayer().getLocation(), sinfo.id, town);
/*     */       try {
/* 281 */         struct.buildPlayerPreview(getPlayer(), getPlayer().getLocation());
/*     */       } catch (IOException e) {
/* 283 */         e.printStackTrace();
/* 284 */         throw new CivException("Internal IO Error.");
/*     */       }
/*     */     }
/* 287 */     CivMessage.sendSuccess(this.sender, "Showing preview.");
/*     */   }
/*     */   
/*     */   private void buildByName(String fullArgs) throws CivException
/*     */   {
/* 292 */     ConfigBuildableInfo sinfo = CivSettings.getBuildableInfoByName(fullArgs);
/*     */     
/* 294 */     if (sinfo == null) {
/* 295 */       throw new CivException("Unknown structure " + fullArgs);
/*     */     }
/*     */     
/* 298 */     Town town = getSelectedTown();
/*     */     
/* 300 */     if (sinfo.isWonder.booleanValue()) {
/* 301 */       Wonder wonder = Wonder.newWonder(getPlayer().getLocation(), sinfo.id, town);
/*     */       try {
/* 303 */         wonder.buildPlayerPreview(getPlayer(), getPlayer().getLocation());
/*     */       } catch (IOException e) {
/* 305 */         e.printStackTrace();
/* 306 */         throw new CivException("Internal IO Error.");
/*     */       }
/*     */     } else {
/* 309 */       Structure struct = Structure.newStructure(getPlayer().getLocation(), sinfo.id, town);
/*     */       try {
/* 311 */         struct.buildPlayerPreview(getPlayer(), getPlayer().getLocation());
/*     */       } catch (IOException e) {
/* 313 */         e.printStackTrace();
/* 314 */         throw new CivException("Internal IO Error.");
/*     */       }
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
/*     */   public void showHelp()
/*     */   {
/* 328 */     showBasicHelp();
/* 329 */     CivMessage.send(this.sender, "§d" + this.command + " " + "§e" + "[structure name] " + 
/* 330 */       "§7" + "builds this structure at your location.");
/*     */   }
/*     */   
/*     */   public void permissionCheck() throws CivException
/*     */   {
/* 335 */     validMayorAssistantLeader();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\BuildCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
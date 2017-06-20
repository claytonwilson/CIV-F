/*     */ package com.avrgaming.civcraft.command.admin;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.structure.BuildableLayer;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import com.avrgaming.civcraft.structure.wonders.Wonder;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import java.io.IOException;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
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
/*     */ public class AdminBuildCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  43 */     this.command = "/ad build";
/*  44 */     this.displayName = "Admin Build";
/*     */     
/*  46 */     this.commands.put("demolish", "[town] [location] demolish the structure at this location.");
/*  47 */     this.commands.put("repair", "Fixes the nearest structure, requires confirmation.");
/*  48 */     this.commands.put("destroywonder", "[id] destroyes this wonder.");
/*  49 */     this.commands.put("destroynearest", "[town] destroys the nearest structure in this town. Confirmation is required.");
/*  50 */     this.commands.put("validatenearest", "[town] Validate the nearest structure in this town. Confirmation is required.");
/*  51 */     this.commands.put("validateall", "Gets all invalid buildables in the server.");
/*  52 */     this.commands.put("listinvalid", "lists all invalid buildables.");
/*  53 */     this.commands.put("showbuildable", "[loc] - show this buildable's y percertages.");
/*     */   }
/*     */   
/*     */   public void showbuildable_cmd()
/*     */     throws CivException
/*     */   {
/*  59 */     String locString = getNamedString(1, "Complete location.");
/*     */     
/*  61 */     for (Buildable buildable : Buildable.invalidBuildables) {
/*  62 */       if (buildable.getCorner().toString().equalsIgnoreCase(locString))
/*     */       {
/*  64 */         for (Integer y : buildable.layerValidPercentages.keySet()) {
/*  65 */           BuildableLayer layer = (BuildableLayer)buildable.layerValidPercentages.get(y);
/*     */           
/*  67 */           Double percentage = Double.valueOf(layer.current / layer.max);
/*  68 */           CivMessage.send(this.sender, "y:" + y + " percentage:" + percentage + " (" + layer.current + "/" + layer.max + ")");
/*     */         }
/*     */       }
/*     */     }
/*  72 */     CivMessage.sendSuccess(this.sender, "Finished.");
/*     */   }
/*     */   
/*     */   public void listinvalid_cmd() {
/*  76 */     for (Buildable buildable : Buildable.invalidBuildables) {
/*  77 */       CivMessage.send(this.sender, buildable.getDisplayName() + " at " + buildable.getCorner() + " in " + buildable.getTown().getName());
/*     */     }
/*  79 */     CivMessage.sendSuccess(this.sender, "Finished.");
/*     */   }
/*     */   
/*     */   public void validateall_cmd() throws CivException {
/*  83 */     Buildable.invalidBuildables.clear();
/*     */     
/*  85 */     for (Structure struct : CivGlobal.getStructures()) {
/*  86 */       if (struct.isStrategic()) {
/*  87 */         struct.validate(null);
/*     */       }
/*     */     }
/*     */     
/*  91 */     for (Wonder wonder : CivGlobal.getWonders()) {
/*  92 */       if (wonder.isStrategic()) {
/*  93 */         wonder.validate(null);
/*     */       }
/*     */     }
/*     */     
/*  97 */     CivMessage.sendSuccess(this.sender, "Validating all structures.");
/*     */   }
/*     */   
/*     */   public void validatenearest_cmd() throws CivException {
/* 101 */     Player player = getPlayer();
/* 102 */     Town town = getNamedTown(1);
/* 103 */     Buildable buildable = town.getNearestBuildable(player.getLocation());
/*     */     
/* 105 */     if ((this.args.length < 3) || (!this.args[2].equalsIgnoreCase("yes"))) {
/* 106 */       CivMessage.send(player, "§e" + ChatColor.BOLD + "Would validate " + buildable.getDisplayName() + " at " + buildable.getCorner() + " are you sure? use '/ad validatenearest [town] yes' to confirm.");
/* 107 */       return;
/*     */     }
/*     */     
/* 110 */     buildable.validate(player);
/*     */   }
/*     */   
/*     */   public void destroynearest_cmd()
/*     */     throws CivException
/*     */   {
/* 116 */     Town town = getNamedTown(1);
/* 117 */     Player player = getPlayer();
/*     */     
/* 119 */     Buildable struct = town.getNearestStrucutreOrWonderInprogress(player.getLocation());
/*     */     
/* 121 */     if ((this.args.length < 3) || (!this.args[2].equalsIgnoreCase("yes"))) {
/* 122 */       CivMessage.send(player, "§e" + ChatColor.BOLD + "Would destroy " + struct.getDisplayName() + " at " + struct.getCorner() + " are you sure? use '/ad destroynearest [town] yes' to confirm.");
/* 123 */       return;
/*     */     }
/*     */     
/* 126 */     struct.onDestroy();
/* 127 */     CivMessage.send(player, struct.getDisplayName() + " has been destroyed.");
/*     */   }
/*     */   
/*     */   public void destroywonder_cmd() throws CivException {
/* 131 */     Town town = getNamedTown(1);
/*     */     
/* 133 */     if (this.args.length < 2) {
/* 134 */       throw new CivException("enter wonder id to destroy.");
/*     */     }
/*     */     
/* 137 */     Wonder wonder = null;
/* 138 */     for (Wonder w : town.getWonders()) {
/* 139 */       if (w.getConfigId().equals(this.args[2])) {
/* 140 */         wonder = w;
/* 141 */         break;
/*     */       }
/*     */     }
/*     */     
/* 145 */     if (wonder == null) {
/* 146 */       throw new CivException("no wonder with id " + this.args[2] + " or it is not built yet");
/*     */     }
/*     */     
/* 149 */     wonder.fancyDestroyStructureBlocks();
/*     */     try {
/* 151 */       wonder.getTown().removeWonder(wonder);
/* 152 */       wonder.fancyDestroyStructureBlocks();
/* 153 */       wonder.unbindStructureBlocks();
/* 154 */       wonder.delete();
/*     */     } catch (SQLException e) {
/* 156 */       e.printStackTrace();
/*     */     }
/* 158 */     CivMessage.sendSuccess(this.sender, "destroyed");
/*     */   }
/*     */   
/*     */   public void repair_cmd() throws CivException {
/* 162 */     Player player = getPlayer();
/*     */     
/* 164 */     Buildable nearest = CivGlobal.getNearestBuildable(player.getLocation());
/*     */     
/* 166 */     if (nearest == null) {
/* 167 */       throw new CivException("Couldn't find a structure.");
/*     */     }
/*     */     
/* 170 */     if ((this.args.length < 2) || (!this.args[1].equalsIgnoreCase("yes"))) {
/* 171 */       CivMessage.send(player, "§aAre you sure you want to repair the structure §e" + nearest.getDisplayName() + 
/* 172 */         "§a" + " at " + "§e" + nearest.getCorner() + "§a" + " ?");
/* 173 */       CivMessage.send(player, "§7If yes, use /ad repair yes");
/* 174 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 178 */       nearest.repairFromTemplate();
/*     */     } catch (IOException e) {
/* 180 */       e.printStackTrace();
/* 181 */       throw new CivException("IO error. Couldn't find template file:" + nearest.getSavedTemplatePath() + " ?");
/*     */     }
/* 183 */     CivMessage.sendSuccess(player, nearest.getDisplayName() + " Repaired.");
/*     */   }
/*     */   
/*     */   public void demolish_cmd()
/*     */     throws CivException
/*     */   {
/* 189 */     if (this.args.length < 2) {
/* 190 */       throw new CivException("Enter a town and structure location.");
/*     */     }
/*     */     
/* 193 */     Town town = getNamedTown(1);
/*     */     
/* 195 */     if (this.args.length < 3) {
/* 196 */       CivMessage.sendHeading(this.sender, "Demolish Structure");
/* 197 */       for (Structure struct : town.getStructures()) {
/* 198 */         CivMessage.send(this.sender, struct.getDisplayName() + " type: " + "§e" + struct.getCorner().toString() + 
/* 199 */           "§f" + " to demolish");
/*     */       }
/* 201 */       return;
/*     */     }
/*     */     
/* 204 */     BlockCoord coord = new BlockCoord(this.args[2]);
/* 205 */     Structure struct = town.getStructure(coord);
/* 206 */     if (struct == null) {
/* 207 */       CivMessage.send(this.sender, "§cNo structure at " + this.args[2]);
/* 208 */       return;
/*     */     }
/*     */     
/* 211 */     struct.getTown().demolish(struct, true);
/*     */     
/*     */ 
/* 214 */     CivMessage.sendTown(struct.getTown(), struct.getDisplayName() + " has been demolished.");
/*     */   }
/*     */   
/*     */   public void doDefaultAction() throws CivException
/*     */   {
/* 219 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 224 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck()
/*     */     throws CivException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\admin\AdminBuildCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
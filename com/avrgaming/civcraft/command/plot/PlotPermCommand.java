/*     */ package com.avrgaming.civcraft.command.plot;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.TownChunk;
/*     */ import com.avrgaming.civcraft.permission.PermissionNode;
/*     */ import com.avrgaming.civcraft.permission.PlotPermissions;
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
/*     */ public class PlotPermCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  35 */     this.command = "/plot perm";
/*  36 */     this.displayName = "Plot Perm";
/*     */     
/*  38 */     this.commands.put("set", "Sets a permission flag on or off.");
/*     */   }
/*     */   
/*     */   public void set_cmd() throws CivException {
/*  42 */     Player player = (Player)this.sender;
/*     */     
/*  44 */     TownChunk tc = CivGlobal.getTownChunk(player.getLocation());
/*  45 */     if (tc == null) {
/*  46 */       throw new CivException("Plot is not part of a town.");
/*     */     }
/*     */     
/*  49 */     if (this.args.length < 4) {
/*  50 */       showPermCmdHelp();
/*  51 */       throw new CivException("Incorrect number of arguments");
/*     */     }
/*     */     
/*  54 */     PermissionNode node = null;
/*  55 */     String str1; switch ((str1 = this.args[1].toLowerCase()).hashCode()) {case 94094958:  if (str1.equals("build")) break; break; case 108404047:  if (str1.equals("reset")) break label247; break; case 570398262:  if (str1.equals("interact")) {} break; case 1557372922:  if (str1.equals("destroy")) {} break; case 2116226452:  if (!str1.equals("itemuse")) {
/*     */         break label233;
/*  57 */         node = tc.perms.build;
/*     */         
/*     */         break label247;
/*  60 */         node = tc.perms.destroy;
/*     */         
/*     */         break label247;
/*  63 */         node = tc.perms.interact;
/*     */         break label247;
/*     */       } else {
/*  66 */         node = tc.perms.itemUse; }
/*  67 */       break;
/*     */     }
/*     */     
/*     */     
/*     */     label233:
/*  72 */     showPermCmdHelp();
/*  73 */     throw new CivException("Incorrect Command Arguments.");
/*     */     
/*     */     label247:
/*  76 */     if (node == null) {
/*  77 */       throw new CivException("Internal error, unknown permission node.");
/*     */     }
/*     */     
/*     */     boolean on;
/*  81 */     if ((this.args[3].equalsIgnoreCase("on")) || (this.args[3].equalsIgnoreCase("yes")) || (this.args[3].equalsIgnoreCase("1"))) {
/*  82 */       on = true; } else { boolean on;
/*  83 */       if ((this.args[3].equalsIgnoreCase("off")) || (this.args[3].equalsIgnoreCase("no")) || (this.args[3].equalsIgnoreCase("0"))) {
/*  84 */         on = false;
/*     */       } else {
/*  86 */         showPermCmdHelp();
/*  87 */         throw new CivException("Incorrect Command Arguments."); } }
/*     */     boolean on;
/*     */     String str2;
/*  90 */     switch ((str2 = this.args[2].toLowerCase()).hashCode()) {case -1006804125:  if (str2.equals("others")) {} break; case 98629247:  if (str2.equals("group")) break;  case 106164915:  if ((goto 483) && (str2.equals("owner")))
/*     */       {
/*  92 */         node.setPermitOwner(on);
/*     */         
/*     */         break label483;
/*  95 */         node.setPermitGroup(on);
/*     */         
/*     */         break label483;
/*  98 */         node.setPermitOthers(on); }
/*     */       break; }
/*     */     label483:
/* 101 */     tc.save();
/*     */     
/* 103 */     CivMessage.sendSuccess(this.sender, "Permission " + node.getType() + " changed to " + on + " for " + this.args[2]);
/*     */   }
/*     */   
/*     */   private void showPermCmdHelp() {
/* 107 */     CivMessage.send(this.sender, "§7/plot perm set <type> <groupType> [on|off] ");
/* 108 */     CivMessage.send(this.sender, "§7    types: [build|destroy|interact|itemuse|reset]");
/* 109 */     CivMessage.send(this.sender, "§7    groupType: [owner|group|others]");
/*     */   }
/*     */   
/*     */   public void doDefaultAction() throws CivException
/*     */   {
/* 114 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 119 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck() throws CivException
/*     */   {
/* 124 */     if (this.args.length != 0) {
/* 125 */       validPlotOwner();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\plot\PlotPermCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
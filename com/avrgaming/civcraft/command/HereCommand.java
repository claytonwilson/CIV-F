/*    */ package com.avrgaming.civcraft.command;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.CultureChunk;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.object.TownChunk;
/*    */ import com.avrgaming.civcraft.util.ChunkCoord;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HereCommand
/*    */   implements CommandExecutor
/*    */ {
/*    */   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
/*    */   {
/* 38 */     if ((sender instanceof Player)) {
/* 39 */       Player player = (Player)sender;
/*    */       
/* 41 */       ChunkCoord coord = new ChunkCoord(player.getLocation());
/*    */       
/* 43 */       CultureChunk cc = CivGlobal.getCultureChunk(coord);
/* 44 */       if (cc != null) {
/* 45 */         CivMessage.send(sender, "§dYou're currently inside the culture of Civ:§e" + 
/* 46 */           cc.getCiv().getName() + "§d" + " for town:" + "§e" + cc.getTown().getName());
/*    */       }
/*    */       
/* 49 */       TownChunk tc = CivGlobal.getTownChunk(coord);
/* 50 */       if (tc != null) {
/* 51 */         CivMessage.send(sender, "§2You're currently inside the town borders of §a" + tc.getTown().getName());
/* 52 */         if (tc.isOutpost()) {
/* 53 */           CivMessage.send(sender, "§eThis chunk is an outpost.");
/*    */         }
/*    */       }
/*    */       
/* 57 */       if ((cc == null) && (tc == null)) {
/* 58 */         CivMessage.send(sender, "§eYou stand in wilderness.");
/*    */       }
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 64 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\HereCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
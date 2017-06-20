/*    */ package com.avrgaming.civcraft.command.debug;
/*    */ 
/*    */ import com.avrgaming.civcraft.arena.ArenaManager;
/*    */ import com.avrgaming.civcraft.command.CommandBase;
/*    */ import com.avrgaming.civcraft.config.CivSettings;
/*    */ import com.avrgaming.civcraft.config.ConfigArena;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.util.ChunkCoord;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.World.Environment;
/*    */ import org.bukkit.WorldCreator;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class DebugWorldCommand extends CommandBase
/*    */ {
/*    */   public void init()
/*    */   {
/* 22 */     this.command = "/dbg world";
/* 23 */     this.displayName = "Debug World";
/*    */     
/* 25 */     this.commands.put("create", "[name] - creates a new test world with this name.");
/* 26 */     this.commands.put("tp", "[name] teleports you to spawn at the specified world.");
/* 27 */     this.commands.put("list", "Lists worlds according to bukkit.");
/* 28 */     this.commands.put("createarena", "[name] - creates and arena with the given name");
/*    */   }
/*    */   
/*    */   public void createarena_cmd() throws CivException {
/* 32 */     String name = getNamedString(1, "enter a arena name.");
/*    */     
/* 34 */     ConfigArena arena = (ConfigArena)CivSettings.arenas.get(name);
/* 35 */     ArenaManager.createArena(arena);
/* 36 */     CivMessage.sendSuccess(this.sender, "Created arena:" + arena.name);
/*    */   }
/*    */   
/*    */   public void list_cmd() {
/* 40 */     CivMessage.sendHeading(this.sender, "Worlds");
/* 41 */     for (World world : Bukkit.getWorlds()) {
/* 42 */       CivMessage.send(this.sender, world.getName());
/*    */     }
/*    */   }
/*    */   
/*    */   public void create_cmd() throws CivException {
/* 47 */     String name = getNamedString(1, "enter a world name");
/*    */     
/* 49 */     WorldCreator wc = new WorldCreator(name);
/* 50 */     wc.environment(World.Environment.NORMAL);
/* 51 */     wc.type(org.bukkit.WorldType.FLAT);
/* 52 */     wc.generateStructures(false);
/*    */     
/* 54 */     World world = Bukkit.getServer().createWorld(wc);
/* 55 */     world.setSpawnFlags(false, false);
/* 56 */     ChunkCoord.addWorld(world);
/*    */     
/* 58 */     CivMessage.sendSuccess(this.sender, "World " + name + " created.");
/*    */   }
/*    */   
/*    */   public void tp_cmd() throws CivException
/*    */   {
/* 63 */     String name = getNamedString(1, "enter a world name");
/* 64 */     Player player = getPlayer();
/*    */     
/* 66 */     World world = Bukkit.getWorld(name);
/* 67 */     player.teleport(world.getSpawnLocation());
/*    */     
/* 69 */     CivMessage.sendSuccess(this.sender, "Teleported to spawn at world:" + name);
/*    */   }
/*    */   
/*    */   public void doDefaultAction()
/*    */     throws CivException
/*    */   {
/* 75 */     showHelp();
/*    */   }
/*    */   
/*    */   public void showHelp()
/*    */   {
/* 80 */     showBasicHelp();
/*    */   }
/*    */   
/*    */   public void permissionCheck()
/*    */     throws CivException
/*    */   {}
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\debug\DebugWorldCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.avrgaming.civcraft.items.components;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigBuildableInfo;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.interactive.InteractiveWarCampFound;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.CallbackInterface;
/*     */ import com.avrgaming.civcraft.util.CivColor;
/*     */ import com.avrgaming.civcraft.war.War;
/*     */ import gpl.AttributeUtil;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.block.Action;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ 
/*     */ public class FoundWarCamp extends ItemComponent implements CallbackInterface
/*     */ {
/*  26 */   public static ConfigBuildableInfo info = new ConfigBuildableInfo();
/*     */   
/*  28 */   static { info.id = "warcamp";
/*  29 */     info.displayName = "War Camp";
/*  30 */     info.ignore_floating = false;
/*  31 */     info.template_base_name = "warcamp";
/*  32 */     info.tile_improvement = Boolean.valueOf(false);
/*  33 */     info.templateYShift = -1;
/*  34 */     info.max_hitpoints = 100;
/*     */   }
/*     */   
/*     */   public void onPrepareCreate(AttributeUtil attrUtil)
/*     */   {
/*  39 */     attrUtil.addLore(ChatColor.RESET + "§6" + "Deploys War Camp");
/*  40 */     attrUtil.addLore(ChatColor.RESET + "§c" + "<Right Click To Use>");
/*     */   }
/*     */   
/*     */   public void foundCamp(Player player) throws CivException {
/*  44 */     Resident resident = CivGlobal.getResident(player);
/*     */     
/*  46 */     if (!resident.hasTown()) {
/*  47 */       throw new CivException("You must be part of a civilization to found a war camp.");
/*     */     }
/*     */     
/*  50 */     if ((!resident.getCiv().getLeaderGroup().hasMember(resident)) && 
/*  51 */       (!resident.getCiv().getAdviserGroup().hasMember(resident))) {
/*  52 */       throw new CivException("You must be a leader or adviser of the civilization to found a war camp.");
/*     */     }
/*     */     
/*  55 */     if (!War.isWarTime()) {
/*  56 */       throw new CivException("War Camps can only be built during WarTime.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  62 */     CivMessage.send(player, "§a" + CivColor.BOLD + "Checking structure position...Please wait.");
/*     */     
/*     */ 
/*  65 */     Buildable.buildVerifyStatic(player, info, player.getLocation(), this);
/*     */   }
/*     */   
/*     */   public void onInteract(PlayerInteractEvent event)
/*     */   {
/*  70 */     event.setCancelled(true);
/*  71 */     if ((!event.getAction().equals(Action.RIGHT_CLICK_AIR)) && 
/*  72 */       (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
/*  73 */       return;
/*     */     }
/*     */     try
/*     */     {
/*  77 */       foundCamp(event.getPlayer());
/*     */     } catch (CivException e) {
/*  79 */       CivMessage.sendError(event.getPlayer(), e.getMessage());
/*     */     }
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
/*     */ 
/* 101 */     TaskMaster.syncTask(new Runnable()
/*     */     {
/*     */       String name;
/*     */       
/*     */       public void run()
/*     */       {
/*     */         try
/*     */         {
/*  94 */           player = CivGlobal.getPlayer(this.name);
/*     */         } catch (CivException e) { Player player;
/*     */           return; }
/*     */         Player player;
/*  98 */         player.updateInventory();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void execute(String playerName)
/*     */   {
/*     */     try
/*     */     {
/* 111 */       player = CivGlobal.getPlayer(playerName);
/*     */     } catch (CivException e) { Player player;
/*     */       return; }
/*     */     Player player;
/* 115 */     Resident resident = CivGlobal.getResident(playerName);
/*     */     try
/*     */     {
/* 118 */       warTimeout = CivSettings.getInteger(CivSettings.warConfig, "warcamp.rebuild_timeout").intValue();
/*     */     } catch (InvalidConfiguration e) { int warTimeout;
/* 120 */       e.printStackTrace(); return;
/*     */     }
/*     */     
/*     */     int warTimeout;
/* 124 */     CivMessage.sendHeading(player, "Ready for War! War Camp.");
/* 125 */     CivMessage.send(player, "§aLets get down to buisness. ");
/* 126 */     CivMessage.send(player, "§a   -Your Civilization will be able to spawn here.");
/* 127 */     CivMessage.send(player, "§a   -Cannot be rebuilt for at least " + warTimeout + " mins.");
/* 128 */     CivMessage.send(player, " ");
/* 129 */     CivMessage.send(player, "§a" + ChatColor.BOLD + "Do you want to place the War Camp here?");
/* 130 */     CivMessage.send(player, "§7(To accept, type 'yes')");
/*     */     
/* 132 */     resident.setInteractiveMode(new InteractiveWarCampFound(info));
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\FoundWarCamp.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
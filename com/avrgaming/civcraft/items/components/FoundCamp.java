/*     */ package com.avrgaming.civcraft.items.components;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.ConfigBuildableInfo;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.interactive.InteractiveCampName;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.CallbackInterface;
/*     */ import com.avrgaming.civcraft.util.CivColor;
/*     */ import gpl.AttributeUtil;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.block.Action;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
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
/*     */ public class FoundCamp
/*     */   extends ItemComponent
/*     */   implements CallbackInterface
/*     */ {
/*     */   public void onPrepareCreate(AttributeUtil attrUtil)
/*     */   {
/*  43 */     attrUtil.addLore(ChatColor.RESET + "§6" + "Starts a Camp");
/*  44 */     attrUtil.addLore(ChatColor.RESET + "§c" + "<Right Click To Use>");
/*     */   }
/*     */   
/*     */   public void foundCamp(Player player) throws CivException {
/*  48 */     Resident resident = CivGlobal.getResident(player);
/*     */     
/*  50 */     if (resident.hasTown()) {
/*  51 */       throw new CivException("You cannot found a camp when you're a member of a town.");
/*     */     }
/*     */     
/*  54 */     if (resident.hasCamp()) {
/*  55 */       throw new CivException("You cannot found a camp when you're a member of another camp.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  61 */     CivMessage.send(player, "§a" + CivColor.BOLD + "Checking structure position...Please wait.");
/*  62 */     ConfigBuildableInfo info = new ConfigBuildableInfo();
/*  63 */     info.id = "camp";
/*  64 */     info.displayName = "Camp";
/*  65 */     info.ignore_floating = false;
/*  66 */     info.template_base_name = "camp";
/*  67 */     info.tile_improvement = Boolean.valueOf(false);
/*  68 */     info.templateYShift = -1;
/*     */     
/*  70 */     Buildable.buildVerifyStatic(player, info, player.getLocation(), this);
/*     */   }
/*     */   
/*     */   public void onInteract(PlayerInteractEvent event)
/*     */   {
/*  75 */     event.setCancelled(true);
/*  76 */     if ((!event.getAction().equals(Action.RIGHT_CLICK_AIR)) && 
/*  77 */       (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
/*  78 */       return;
/*     */     }
/*     */     try
/*     */     {
/*  82 */       foundCamp(event.getPlayer());
/*     */     } catch (CivException e) {
/*  84 */       CivMessage.sendError(event.getPlayer(), e.getMessage());
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
/* 106 */     TaskMaster.syncTask(new Runnable()
/*     */     {
/*     */       String name;
/*     */       
/*     */       public void run()
/*     */       {
/*     */         try
/*     */         {
/*  99 */           player = CivGlobal.getPlayer(this.name);
/*     */         } catch (CivException e) { Player player;
/*     */           return; }
/*     */         Player player;
/* 103 */         player.updateInventory();
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
/* 116 */       player = CivGlobal.getPlayer(playerName);
/*     */     } catch (CivException e) { Player player;
/*     */       return; }
/*     */     Player player;
/* 120 */     Resident resident = CivGlobal.getResident(playerName);
/*     */     
/* 122 */     CivMessage.sendHeading(player, "Setting up Camp!");
/* 123 */     CivMessage.send(player, "§aYou and your small band of travelers need a place to sleep for the night.");
/* 124 */     CivMessage.send(player, " ");
/* 125 */     CivMessage.send(player, "§a" + ChatColor.BOLD + "What shall your new camp be called?");
/* 126 */     CivMessage.send(player, "§7(To cancel, type 'cancel')");
/*     */     
/* 128 */     resident.setInteractiveMode(new InteractiveCampName());
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\FoundCamp.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
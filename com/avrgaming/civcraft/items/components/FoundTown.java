/*     */ package com.avrgaming.civcraft.items.components;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.interactive.InteractiveTownName;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.CallbackInterface;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FoundTown
/*     */   extends ItemComponent
/*     */   implements CallbackInterface
/*     */ {
/*     */   public void onPrepareCreate(AttributeUtil attrUtil)
/*     */   {
/*  43 */     attrUtil.addLore(ChatColor.RESET + "§6" + "Founds a Town");
/*  44 */     attrUtil.addLore(ChatColor.RESET + "§c" + "<Right Click To Use>");
/*     */   }
/*     */   
/*     */   public void foundTown(Player player) throws CivException {
/*  48 */     Resident resident = CivGlobal.getResident(player);
/*     */     
/*  50 */     if ((resident == null) || (!resident.hasTown())) {
/*  51 */       throw new CivException("You are not part of a civilization.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onInteract(PlayerInteractEvent event)
/*     */   {
/*  59 */     event.setCancelled(true);
/*  60 */     if ((!event.getAction().equals(Action.RIGHT_CLICK_AIR)) && 
/*  61 */       (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
/*  62 */       return;
/*     */     }
/*     */     try
/*     */     {
/*  66 */       foundTown(event.getPlayer());
/*     */     } catch (CivException e) {
/*  68 */       CivMessage.sendError(event.getPlayer(), e.getMessage());
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
/*  90 */     TaskMaster.syncTask(new Runnable()
/*     */     {
/*     */       String name;
/*     */       
/*     */       public void run()
/*     */       {
/*     */         try
/*     */         {
/*  83 */           player = CivGlobal.getPlayer(this.name);
/*     */         } catch (CivException e) { Player player;
/*     */           return; }
/*     */         Player player;
/*  87 */         player.updateInventory();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void execute(String playerName)
/*     */   {
/*     */     try
/*     */     {
/*  98 */       player = CivGlobal.getPlayer(playerName);
/*     */     } catch (CivException e) { Player player;
/*     */       return; }
/*     */     Player player;
/* 102 */     Resident resident = CivGlobal.getResident(playerName);
/*     */     
/* 104 */     CivMessage.sendHeading(player, "Founding A New Town");
/* 105 */     CivMessage.send(player, "§aThis looks like a good place to settle!");
/* 106 */     CivMessage.send(player, " ");
/* 107 */     CivMessage.send(player, "§a" + ChatColor.BOLD + "What shall your new Town be called?");
/* 108 */     CivMessage.send(player, "§7(To cancel, type 'cancel')");
/*     */     
/* 110 */     resident.setInteractiveMode(new InteractiveTownName());
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\FoundTown.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
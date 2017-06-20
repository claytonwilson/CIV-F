/*     */ package com.avrgaming.civcraft.items.components;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigBuildableInfo;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.interactive.InteractiveCivName;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.CallbackInterface;
/*     */ import com.avrgaming.civcraft.util.CivColor;
/*     */ import gpl.AttributeUtil;
/*     */ import java.util.Map;
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
/*     */ public class FoundCivilization
/*     */   extends ItemComponent
/*     */   implements CallbackInterface
/*     */ {
/*     */   public void onPrepareCreate(AttributeUtil attrUtil)
/*     */   {
/*  45 */     attrUtil.addLore(ChatColor.RESET + "§6" + "Founds a Civilization");
/*  46 */     attrUtil.addLore(ChatColor.RESET + "§c" + "<Right Click To Use>");
/*  47 */     attrUtil.addEnhancement("LoreEnhancementSoulBound", null, null);
/*  48 */     attrUtil.addLore("§6Soulbound");
/*     */   }
/*     */   
/*     */   public void foundCiv(Player player) throws CivException
/*     */   {
/*  53 */     Resident resident = CivGlobal.getResident(player);
/*  54 */     if (resident == null) {
/*  55 */       throw new CivException("You must be a registered resident to found a civ. This shouldn't happen. Contact an admin.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  61 */     CivMessage.send(player, "§a" + CivColor.BOLD + "Checking structure position...Please wait.");
/*  62 */     ConfigBuildableInfo info = (ConfigBuildableInfo)CivSettings.structures.get("s_capitol");
/*  63 */     Buildable.buildVerifyStatic(player, info, player.getLocation(), this);
/*     */   }
/*     */   
/*     */   public void onInteract(PlayerInteractEvent event)
/*     */   {
/*  68 */     event.setCancelled(true);
/*  69 */     if ((!event.getAction().equals(Action.RIGHT_CLICK_AIR)) && 
/*  70 */       (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
/*  71 */       return;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  98 */     TaskMaster.syncTask(new Runnable()
/*     */     {
/*     */       String name;
/*     */       
/*     */       public void run()
/*     */       {
/*     */         try
/*     */         {
/*  86 */           Player player = CivGlobal.getPlayer(this.name);
/*     */           try {
/*  88 */             FoundCivilization.this.foundCiv(player);
/*     */           } catch (CivException e) {
/*  90 */             CivMessage.sendError(player, e.getMessage());
/*     */           }
/*     */           
/*     */           Player player;
/*     */           
/*  95 */           player.updateInventory();
/*     */         }
/*     */         catch (CivException e) {}
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */   public void execute(String playerName)
/*     */   {
/*     */     try
/*     */     {
/* 107 */       player = CivGlobal.getPlayer(playerName);
/*     */     } catch (CivException e) { Player player;
/*     */       return;
/*     */     }
/*     */     Player player;
/* 112 */     Resident resident = CivGlobal.getResident(player);
/*     */     
/*     */ 
/* 115 */     resident.desiredTownLocation = player.getLocation();
/* 116 */     CivMessage.sendHeading(player, "Founding A New Civ");
/* 117 */     CivMessage.send(player, "§aYou and your small band of travelers have finally found the chosen land.");
/* 118 */     CivMessage.send(player, "§aWhile you are few, will your numbers will grow?");
/* 119 */     CivMessage.send(player, "§aWill you journey boldy forth into new frontiers?");
/* 120 */     CivMessage.send(player, "§aCan you build a Civilization that can stand the test of time?");
/* 121 */     CivMessage.send(player, " ");
/* 122 */     CivMessage.send(player, "§a" + ChatColor.BOLD + "What shall your new Civilization be called?");
/* 123 */     CivMessage.send(player, "§7(To cancel, type 'cancel')");
/*     */     
/* 125 */     resident.setInteractiveMode(new InteractiveCivName());
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\FoundCivilization.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
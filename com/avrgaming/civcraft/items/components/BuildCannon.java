/*    */ package com.avrgaming.civcraft.items.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.siege.Cannon;
/*    */ import com.avrgaming.civcraft.war.War;
/*    */ import gpl.AttributeUtil;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.player.PlayerInteractEvent;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ public class BuildCannon extends ItemComponent
/*    */ {
/*    */   public void onInteract(PlayerInteractEvent event)
/*    */   {
/*    */     try
/*    */     {
/* 23 */       if (!War.isWarTime()) {
/* 24 */         throw new CivException("Cannons can only be deployed during WarTime.");
/*    */       }
/*    */       
/* 27 */       Resident resident = CivGlobal.getResident(event.getPlayer());
/* 28 */       Cannon.newCannon(resident);
/*    */       
/* 30 */       CivMessage.sendCiv(resident.getCiv(), "We've deployed a cannon at " + 
/* 31 */         event.getPlayer().getLocation().getBlockX() + "," + 
/* 32 */         event.getPlayer().getLocation().getBlockY() + "," + 
/* 33 */         event.getPlayer().getLocation().getBlockZ());
/*    */       
/* 35 */       ItemStack newStack = new ItemStack(Material.AIR);
/* 36 */       event.getPlayer().setItemInHand(newStack);
/*    */     } catch (CivException e) {
/* 38 */       CivMessage.sendError(event.getPlayer(), e.getMessage());
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void onPrepareCreate(AttributeUtil attrUtil)
/*    */   {
/* 45 */     attrUtil.addLore(ChatColor.RESET + "§6" + "Deploys War Cannon");
/* 46 */     attrUtil.addLore(ChatColor.RESET + "§c" + "<Right Click To Use>");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\BuildCannon.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
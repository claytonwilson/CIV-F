/*    */ package com.avrgaming.global.perks.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.interactive.InteractiveConfirmWeatherChange;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class ChangeWeather
/*    */   extends PerkComponent
/*    */ {
/*    */   public void onActivate(Resident resident)
/*    */   {
/*    */     try
/*    */     {
/* 18 */       player = CivGlobal.getPlayer(resident);
/*    */     } catch (CivException e) { Player player;
/*    */       return; }
/*    */     Player player;
/* 22 */     if ((!player.getWorld().isThundering()) && (!player.getWorld().hasStorm())) {
/* 23 */       CivMessage.sendError(resident, "Weather is already sunny!");
/* 24 */       return;
/*    */     }
/*    */     
/* 27 */     CivMessage.sendHeading(resident, "Changing the Weather to Sunny");
/* 28 */     CivMessage.send(resident, "§2Are you sure you want the weather to be sunny?");
/* 29 */     CivMessage.send(resident, "§7If so type 'yes', type anything else to cancel.");
/* 30 */     resident.setInteractiveMode(new InteractiveConfirmWeatherChange(this));
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\global\perks\components\ChangeWeather.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
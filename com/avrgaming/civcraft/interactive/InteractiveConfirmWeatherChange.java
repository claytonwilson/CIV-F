/*    */ package com.avrgaming.civcraft.interactive;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.global.perks.components.ChangeWeather;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class InteractiveConfirmWeatherChange implements InteractiveResponse
/*    */ {
/*    */   ChangeWeather perk;
/*    */   
/*    */   public InteractiveConfirmWeatherChange(ChangeWeather perk)
/*    */   {
/* 17 */     this.perk = perk;
/*    */   }
/*    */   
/*    */   public void respond(String message, Resident resident)
/*    */   {
/* 22 */     resident.clearInteractiveMode();
/*    */     
/* 24 */     if (message.equalsIgnoreCase("yes")) {
/*    */       try
/*    */       {
/* 27 */         Player player = CivGlobal.getPlayer(resident);
/* 28 */         player.getWorld().setStorm(false);
/* 29 */         player.getWorld().setThundering(false);
/* 30 */         player.getWorld().setWeatherDuration((int)com.avrgaming.civcraft.util.TimeTools.toTicks(1200L));
/* 31 */         CivMessage.global(resident.getName() + " has used a " + "§e" + "Weather Change" + com.avrgaming.civcraft.util.CivColor.RESET + " token to change the weather to sunny!");
/* 32 */         this.perk.markAsUsed(resident);
/*    */       }
/*    */       catch (CivException localCivException) {}
/*    */     } else {
/* 36 */       CivMessage.send(resident, "Weather Change cancelled.");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\interactive\InteractiveConfirmWeatherChange.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
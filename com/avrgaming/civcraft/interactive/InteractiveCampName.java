/*    */ package com.avrgaming.civcraft.interactive;
/*    */ 
/*    */ import com.avrgaming.civcraft.camp.Camp;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import org.apache.commons.lang.StringUtils;
/*    */ import org.bukkit.ChatColor;
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
/*    */ 
/*    */ public class InteractiveCampName
/*    */   implements InteractiveResponse
/*    */ {
/*    */   public void respond(String message, Resident resident)
/*    */   {
/*    */     try
/*    */     {
/* 38 */       player = CivGlobal.getPlayer(resident);
/*    */     } catch (CivException e) { Player player;
/*    */       return;
/*    */     }
/*    */     Player player;
/* 43 */     if (message.equalsIgnoreCase("cancel")) {
/* 44 */       CivMessage.send(player, "Camp creation cancelled.");
/* 45 */       resident.clearInteractiveMode();
/* 46 */       return;
/*    */     }
/*    */     
/* 49 */     if (!StringUtils.isAlpha(message)) {
/* 50 */       CivMessage.send(player, "§c" + ChatColor.BOLD + "Camp names must only contain letters(A-Z). Enter another name.");
/* 51 */       return;
/*    */     }
/*    */     
/* 54 */     message = message.replace(" ", "_");
/* 55 */     message = message.replace("\"", "");
/* 56 */     message = message.replace("'", "");
/*    */     
/* 58 */     Camp.newCamp(resident, player, message);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\interactive\InteractiveCampName.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
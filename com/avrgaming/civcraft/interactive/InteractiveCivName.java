/*    */ package com.avrgaming.civcraft.interactive;
/*    */ 
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
/*    */ public class InteractiveCivName
/*    */   implements InteractiveResponse
/*    */ {
/*    */   public void respond(String message, Resident resident)
/*    */   {
/*    */     try
/*    */     {
/* 37 */       player = CivGlobal.getPlayer(resident);
/*    */     } catch (CivException e) { Player player;
/*    */       return;
/*    */     }
/*    */     Player player;
/* 42 */     if (message.equalsIgnoreCase("cancel")) {
/* 43 */       CivMessage.send(player, "Civilization creation cancelled.");
/* 44 */       resident.clearInteractiveMode();
/* 45 */       return;
/*    */     }
/*    */     
/* 48 */     if (!StringUtils.isAlpha(message)) {
/* 49 */       CivMessage.send(player, "§c" + ChatColor.BOLD + "Civilization names must only contain letters(A-Z). Enter another name.");
/* 50 */       return;
/*    */     }
/*    */     
/* 53 */     message = message.replace(" ", "_");
/* 54 */     message = message.replace("\"", "");
/* 55 */     message = message.replace("'", "");
/*    */     
/* 57 */     resident.desiredCivName = message;
/* 58 */     CivMessage.send(player, "§aThe Civilization of §e" + message + "§a" + "? An awesome choice.");
/* 59 */     CivMessage.send(player, " ");
/* 60 */     CivMessage.send(player, "§a" + ChatColor.BOLD + "Now what should your capitol be called?");
/* 61 */     CivMessage.send(player, "§7(To cancel type 'cancel')");
/* 62 */     resident.setInteractiveMode(new InteractiveCapitolName());
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\interactive\InteractiveCivName.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
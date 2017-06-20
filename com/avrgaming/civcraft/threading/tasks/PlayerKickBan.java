/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.object.Resident;
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
/*    */ public class PlayerKickBan
/*    */   implements Runnable
/*    */ {
/*    */   String name;
/*    */   boolean kick;
/*    */   boolean ban;
/*    */   String reason;
/*    */   
/*    */   public PlayerKickBan(String name, boolean kick, boolean ban, String reason)
/*    */   {
/* 35 */     this.name = name;
/* 36 */     this.kick = kick;
/* 37 */     this.ban = ban;
/* 38 */     this.reason = reason;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/*    */     try
/*    */     {
/* 45 */       player = CivGlobal.getPlayer(this.name);
/*    */     } catch (CivException e) { Player player;
/*    */       return;
/*    */     }
/*    */     Player player;
/* 50 */     if (this.ban) {
/* 51 */       Resident resident = CivGlobal.getResident(player);
/* 52 */       resident.setBanned(true);
/* 53 */       resident.save();
/*    */     }
/*    */     
/* 56 */     if (this.kick) {
/* 57 */       player.kickPlayer(this.reason);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\PlayerKickBan.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
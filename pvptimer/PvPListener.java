/*    */ package pvptimer;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import org.bukkit.entity.Arrow;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ public class PvPListener implements Listener
/*    */ {
/*    */   @EventHandler(priority=EventPriority.LOW)
/*    */   public void onPvP(EntityDamageByEntityEvent event)
/*    */   {
/* 19 */     if (event.isCancelled()) {
/* 20 */       return;
/*    */     }
/*    */     
/* 23 */     if ((event.getDamager() instanceof Player)) {
/* 24 */       Player damager = (Player)event.getDamager();
/* 25 */       Resident damagerResident = CivGlobal.getResident(damager);
/*    */       
/* 27 */       if ((damagerResident.isProtected()) && ((event.getEntity() instanceof Player))) {
/* 28 */         CivMessage.sendError(damager, "You are unable to damage players while protected.");
/* 29 */         event.setCancelled(true);
/*    */       }
/*    */     }
/* 32 */     if ((event.getDamager() instanceof Arrow)) {
/* 33 */       LivingEntity shooter = (LivingEntity)((Arrow)event.getDamager()).getShooter();
/*    */       
/* 35 */       if (((shooter instanceof Player)) && ((event.getEntity() instanceof Player))) {
/* 36 */         Player damager = (Player)shooter;
/* 37 */         Resident damagerResident = CivGlobal.getResident(damager);
/*    */         
/* 39 */         if (damagerResident.isProtected()) {
/* 40 */           CivMessage.sendError(damager, "You are unable to damage players while protected.");
/* 41 */           event.setCancelled(true);
/*    */         } else {
/* 43 */           Player defendingPlayer = (Player)event.getEntity();
/* 44 */           Resident defendingResident = CivGlobal.getResident(defendingPlayer);
/* 45 */           if (defendingResident.isProtected()) {
/* 46 */             CivMessage.sendError(damager, "You are unable to damage protected players.");
/* 47 */             event.setCancelled(true);
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/* 52 */     if (((event.getEntity() instanceof Player)) && (!event.isCancelled()) && ((event.getDamager() instanceof Player))) {
/* 53 */       Player damager = (Player)event.getDamager();
/* 54 */       Player defendingPlayer = (Player)event.getEntity();
/* 55 */       Resident defendingResident = CivGlobal.getResident(defendingPlayer);
/* 56 */       if (((event.getDamager() instanceof Player)) && 
/* 57 */         (defendingResident.isProtected())) {
/* 58 */         event.setCancelled(true);
/* 59 */         CivMessage.sendError(damager, "You are unable to damage protected players.");
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\pvptimer\PvPListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
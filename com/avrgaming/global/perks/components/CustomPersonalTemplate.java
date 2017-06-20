/*    */ package com.avrgaming.global.perks.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.ConfigBuildableInfo;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.template.Template;
/*    */ import java.io.IOException;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CustomPersonalTemplate
/*    */   extends PerkComponent
/*    */ {
/*    */   public void onActivate(Resident resident)
/*    */   {
/* 18 */     CivMessage.send(resident, "§aNo need to activate this perk. Always active =)");
/*    */   }
/*    */   
/*    */   public Template getTemplate(Player player, ConfigBuildableInfo info)
/*    */   {
/* 23 */     Template tpl = new Template();
/*    */     try {
/* 25 */       tpl.initTemplate(player.getLocation(), info, getString("theme"));
/*    */     } catch (CivException e) {
/* 27 */       e.printStackTrace();
/*    */     } catch (IOException e) {
/* 29 */       e.printStackTrace();
/*    */     }
/*    */     
/* 32 */     return tpl;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\global\perks\components\CustomPersonalTemplate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
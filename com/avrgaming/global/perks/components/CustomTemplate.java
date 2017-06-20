/*     */ package com.avrgaming.global.perks.components;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.interactive.InteractiveCustomTemplateConfirm;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.template.Template;
/*     */ import com.avrgaming.global.perks.Perk;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ public class CustomTemplate extends PerkComponent
/*     */ {
/*     */   public void onActivate(Resident resident)
/*     */   {
/*     */     try
/*     */     {
/*  27 */       player = CivGlobal.getPlayer(resident);
/*     */     } catch (CivException e) { Player player;
/*     */       return;
/*     */     }
/*     */     Player player;
/*  32 */     Town town = resident.getTown();
/*  33 */     if (town == null) {
/*  34 */       CivMessage.sendError(player, "This template but be bound to a town and you're not currently in one!");
/*  35 */       return;
/*     */     }
/*     */     
/*  38 */     if (hasTownTemplate(town)) {
/*  39 */       CivMessage.sendError(player, "§cCannot bind this template, the town already has this template.");
/*  40 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  47 */     resident.setInteractiveMode(new InteractiveCustomTemplateConfirm(resident.getName(), this));
/*     */   }
/*     */   
/*     */   private String getTemplateSessionKey(Town town)
/*     */   {
/*  52 */     return "customtemplate:" + town.getName() + ":" + getString("template");
/*     */   }
/*     */   
/*  55 */   private static String getTemplateSessionKey(Town town, String buildableBaseName) { return "customtemplate:" + town.getName() + ":" + buildableBaseName; }
/*     */   
/*     */   private static String getTemplateSessionValue(Perk perk, Resident resident)
/*     */   {
/*  59 */     return perk.getIdent() + ":" + resident.getName();
/*     */   }
/*     */   
/*     */   public void bindTemplateToTown(Town town, Resident resident) {
/*  63 */     CivGlobal.getSessionDB().add(getTemplateSessionKey(town), getTemplateSessionValue(getParent(), resident), 
/*  64 */       town.getCiv().getId(), town.getId(), 0);
/*     */   }
/*     */   
/*     */   public boolean hasTownTemplate(Town town) {
/*  68 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(getTemplateSessionKey(town));
/*     */     
/*  70 */     for (SessionEntry entry : entries) {
/*  71 */       String[] split = entry.value.split(":");
/*     */       
/*  73 */       if (getParent().getIdent().equals(split[0])) {
/*  74 */         return true;
/*     */       }
/*     */     }
/*     */     
/*  78 */     return false;
/*     */   }
/*     */   
/*     */   public static ArrayList<Perk> getTemplatePerksForBuildable(Town town, String buildableBaseName) {
/*  82 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(getTemplateSessionKey(town, buildableBaseName));
/*  83 */     ArrayList<Perk> perks = new ArrayList();
/*     */     
/*  85 */     for (SessionEntry entry : entries) {
/*  86 */       String[] split = entry.value.split(":");
/*     */       
/*  88 */       Perk perk = (Perk)Perk.staticPerks.get(split[0]);
/*  89 */       if (perk != null) {
/*  90 */         Perk tmpPerk = new Perk(perk.configPerk);
/*  91 */         tmpPerk.provider = split[1];
/*  92 */         perks.add(tmpPerk);
/*     */       } else {
/*  94 */         CivLog.warning("Unknown perk in session db:" + split[0]);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  99 */     return perks;
/*     */   }
/*     */   
/*     */   public Template getTemplate(Player player, Buildable buildable)
/*     */   {
/* 104 */     Template tpl = new Template();
/*     */     try {
/* 106 */       tpl.initTemplate(player.getLocation(), buildable, getString("theme"));
/*     */     } catch (CivException e) {
/* 108 */       e.printStackTrace();
/*     */     } catch (IOException e) {
/* 110 */       e.printStackTrace();
/*     */     }
/*     */     
/* 113 */     return tpl;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\global\perks\components\CustomTemplate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.avrgaming.civcraft.command.town;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.config.ConfigTownUpgrade;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.Library;
/*     */ import com.avrgaming.civcraft.structure.Store;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public class TownResetCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  35 */     this.command = "/town reset";
/*  36 */     this.displayName = "Town Reset";
/*     */     
/*  38 */     this.commands.put("library", "Removes all town library enchantment upgrades.");
/*  39 */     this.commands.put("store", "Removes all town store material upgrades.");
/*     */   }
/*     */   
/*     */   public void library_cmd() throws CivException {
/*  43 */     Town town = getSelectedTown();
/*     */     
/*  45 */     Library library = (Library)town.findStructureByConfigId("s_library");
/*  46 */     if (library == null) {
/*  47 */       throw new CivException("Your town doesn't have a library.");
/*     */     }
/*     */     
/*  50 */     ArrayList<ConfigTownUpgrade> removeUs = new ArrayList();
/*  51 */     for (ConfigTownUpgrade upgrade : town.getUpgrades().values()) {
/*  52 */       if (upgrade.action.contains("enable_library_enchantment")) {
/*  53 */         removeUs.add(upgrade);
/*     */       }
/*     */     }
/*     */     
/*  57 */     for (ConfigTownUpgrade upgrade : removeUs) {
/*  58 */       town.removeUpgrade(upgrade);
/*     */     }
/*     */     
/*  61 */     library.reset();
/*     */     
/*  63 */     town.save();
/*  64 */     CivMessage.sendSuccess(this.sender, "Library enchantment upgrades reset!");
/*     */   }
/*     */   
/*     */   public void store_cmd() throws CivException {
/*  68 */     Town town = getSelectedTown();
/*     */     
/*  70 */     Store store = (Store)town.findStructureByConfigId("s_store");
/*  71 */     if (store == null) {
/*  72 */       throw new CivException("Your town doesn't have a library.");
/*     */     }
/*     */     
/*  75 */     ArrayList<ConfigTownUpgrade> removeUs = new ArrayList();
/*  76 */     for (ConfigTownUpgrade upgrade : town.getUpgrades().values()) {
/*  77 */       if (upgrade.action.contains("set_store_material")) {
/*  78 */         removeUs.add(upgrade);
/*     */       }
/*     */     }
/*     */     
/*  82 */     for (ConfigTownUpgrade upgrade : removeUs) {
/*  83 */       town.removeUpgrade(upgrade);
/*     */     }
/*     */     
/*  86 */     store.reset();
/*     */     
/*  88 */     town.save();
/*  89 */     CivMessage.sendSuccess(this.sender, "Store material upgrades reset!");
/*     */   }
/*     */   
/*     */   public void doDefaultAction() throws CivException
/*     */   {
/*  94 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/*  99 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck() throws CivException
/*     */   {
/* 104 */     validMayorAssistantLeader();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\town\TownResetCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
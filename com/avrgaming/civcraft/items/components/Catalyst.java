/*     */ package com.avrgaming.civcraft.items.components;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreMaterial;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import gpl.AttributeUtil;
/*     */ import java.util.HashMap;
/*     */ import java.util.Random;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Catalyst
/*     */   extends ItemComponent
/*     */ {
/*     */   public void onPrepareCreate(AttributeUtil attrUtil)
/*     */   {
/*  22 */     attrUtil.addLore(ChatColor.RESET + "§6" + "Catalyst");
/*     */   }
/*     */   
/*     */   public ItemStack getEnchantedItem(ItemStack stack)
/*     */   {
/*  27 */     LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/*  28 */     if (craftMat == null) {
/*  29 */       return null;
/*     */     }
/*     */     
/*  32 */     String[] materials = getString("allowed_materials").split(",");
/*  33 */     boolean found = false;
/*  34 */     String[] arrayOfString1; int j = (arrayOfString1 = materials).length; for (int i = 0; i < j; i++) { String mat = arrayOfString1[i];
/*  35 */       mat = mat.replaceAll(" ", "");
/*  36 */       if (mat.equals(LoreMaterial.getMID(stack))) {
/*  37 */         found = true;
/*  38 */         break;
/*     */       }
/*     */     }
/*     */     
/*  42 */     if (!found) {
/*  43 */       return null;
/*     */     }
/*     */     
/*  46 */     String enhStr = getString("enhancement");
/*     */     
/*  48 */     LoreEnhancement enhance = (LoreEnhancement)LoreEnhancement.enhancements.get(enhStr);
/*  49 */     if (enhance == null) {
/*  50 */       CivLog.error("Couldn't find enhancement titled:" + enhStr);
/*  51 */       return null;
/*     */     }
/*     */     
/*  54 */     if ((enhance != null) && 
/*  55 */       (enhance.canEnchantItem(stack))) {
/*  56 */       AttributeUtil attrs = new AttributeUtil(stack);
/*  57 */       enhance.variables.put("amount", getString("amount"));
/*  58 */       attrs = enhance.add(attrs);
/*  59 */       return attrs.getStack();
/*     */     }
/*     */     
/*     */ 
/*  63 */     return null;
/*     */   }
/*     */   
/*     */   public int getEnhancedLevel(ItemStack stack) {
/*  67 */     String enhStr = getString("enhancement");
/*     */     
/*  69 */     LoreEnhancement enhance = (LoreEnhancement)LoreEnhancement.enhancements.get(enhStr);
/*  70 */     if (enhance == null) {
/*  71 */       CivLog.error("Couldn't find enhancement titled:" + enhStr);
/*  72 */       return 0;
/*     */     }
/*     */     
/*  75 */     return (int)enhance.getLevel(new AttributeUtil(stack));
/*     */   }
/*     */   
/*     */   public boolean enchantSuccess(ItemStack stack) {
/*     */     try {
/*  80 */       int free_catalyst_amount = CivSettings.getInteger(CivSettings.civConfig, "global.free_catalyst_amount").intValue();
/*  81 */       int extra_catalyst_amount = CivSettings.getInteger(CivSettings.civConfig, "global.extra_catalyst_amount").intValue();
/*  82 */       double extra_catalyst_percent = CivSettings.getDouble(CivSettings.civConfig, "global.extra_catalyst_percent");
/*     */       
/*  84 */       int level = getEnhancedLevel(stack);
/*     */       
/*  86 */       if (level <= free_catalyst_amount) {
/*  87 */         return true;
/*     */       }
/*     */       
/*  90 */       int chance = Integer.valueOf(getString("chance")).intValue();
/*  91 */       Random rand = new Random();
/*  92 */       int extra = 0;
/*  93 */       int n = rand.nextInt(100);
/*     */       
/*  95 */       if (level <= extra_catalyst_amount) {
/*  96 */         n -= (int)(extra_catalyst_percent * 100.0D);
/*     */       }
/*     */       
/*  99 */       n += extra;
/*     */       
/* 101 */       if (n <= chance) {
/* 102 */         return true;
/*     */       }
/*     */       
/* 105 */       return false;
/*     */     } catch (InvalidConfiguration e) {
/* 107 */       e.printStackTrace(); }
/* 108 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\Catalyst.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
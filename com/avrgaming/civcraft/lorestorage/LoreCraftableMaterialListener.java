/*     */ package com.avrgaming.civcraft.lorestorage;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigMaterial;
/*     */ import com.avrgaming.civcraft.config.ConfigPlatinumReward;
/*     */ import com.avrgaming.civcraft.config.ConfigTech;
/*     */ import com.avrgaming.civcraft.config.ConfigTechItem;
/*     */ import com.avrgaming.civcraft.items.components.Tagged;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.global.perks.PlatinumManager;
/*     */ import gpl.AttributeUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.inventory.CraftItemEvent;
/*     */ import org.bukkit.event.inventory.PrepareItemCraftEvent;
/*     */ import org.bukkit.inventory.CraftingInventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.Recipe;
/*     */ 
/*     */ public class LoreCraftableMaterialListener implements org.bukkit.event.Listener
/*     */ {
/*     */   @EventHandler(priority=EventPriority.LOW)
/*     */   public void OnCraftItemEvent(CraftItemEvent event)
/*     */   {
/*  35 */     if ((event.getWhoClicked() instanceof Player)) {
/*  36 */       Player player = (Player)event.getWhoClicked();
/*     */       
/*  38 */       LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(event.getInventory().getResult());
/*  39 */       if (craftMat == null)
/*     */       {
/*     */ 
/*  42 */         ItemStack resultStack = event.getInventory().getResult();
/*  43 */         if (resultStack.getType().equals(Material.GOLDEN_APPLE)) {
/*  44 */           CivMessage.sendError((Player)event.getWhoClicked(), "You cannot craft golden apples. Sorry.");
/*  45 */           event.setCancelled(true);
/*  46 */           return;
/*     */         }
/*     */         
/*  49 */         ConfigTechItem restrictedTechItem = (ConfigTechItem)CivSettings.techItems.get(Integer.valueOf(ItemManager.getId(resultStack)));
/*  50 */         if (restrictedTechItem != null) {
/*  51 */           ConfigTech tech = (ConfigTech)CivSettings.techs.get(restrictedTechItem.require_tech);
/*  52 */           CivMessage.sendError(player, "Your civilization doesn't have the required technology (" + tech.name + ") to craft this item.");
/*  53 */           event.setCancelled(true);
/*  54 */           return;
/*     */         }
/*     */         
/*  57 */         return;
/*     */       }
/*     */       
/*  60 */       if (!craftMat.getConfigMaterial().playerHasTechnology(player)) {
/*  61 */         CivMessage.sendError(player, "You do not have the required technology (" + craftMat.getConfigMaterial().getRequireString() + ") to craft this item.");
/*  62 */         event.setCancelled(true);
/*  63 */         return;
/*     */       }
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
/*  82 */       Resident resident = CivGlobal.getResident(player);
/*  83 */       if (craftMat.getId().equals("mat_found_camp")) {
/*  84 */         PlatinumManager.givePlatinumOnce(resident, 
/*  85 */           ((ConfigPlatinumReward)CivSettings.platinumRewards.get("buildCamp")).name, 
/*  86 */           Integer.valueOf(((ConfigPlatinumReward)CivSettings.platinumRewards.get("buildCamp")).amount), 
/*  87 */           "Achievement! You've founded your first camp and earned %d");
/*  88 */       } else if (craftMat.getId().equals("mat_found_civ")) {
/*  89 */         PlatinumManager.givePlatinumOnce(resident, 
/*  90 */           ((ConfigPlatinumReward)CivSettings.platinumRewards.get("buildCiv")).name, 
/*  91 */           Integer.valueOf(((ConfigPlatinumReward)CivSettings.platinumRewards.get("buildCiv")).amount), 
/*  92 */           "Achievement! You've founded your first Civilization and earned %d");
/*     */       }
/*     */       else
/*     */       {
/*     */         int amount;
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
/* 131 */         if (event.isShiftClick()) {
/* 132 */           int amount = 64;
/* 133 */           ItemStack[] arrayOfItemStack; int j = (arrayOfItemStack = event.getInventory().getMatrix()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/* 134 */             if (stack != null)
/*     */             {
/*     */ 
/*     */ 
/* 138 */               if (stack.getAmount() < amount)
/* 139 */                 amount = stack.getAmount();
/*     */             }
/*     */           }
/*     */         } else {
/* 143 */           amount = 1;
/*     */         }
/*     */         
/* 146 */         com.avrgaming.civcraft.threading.TaskMaster.asyncTask(new Runnable()
/*     */         {
/*     */           Resident resident;
/*     */           int craftAmount;
/*     */           
/*     */           public void run()
/*     */           {
/* 106 */             String key = this.resident.getName() + ":platinumCrafted";
/* 107 */             ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(key);
/* 108 */             Integer amount = Integer.valueOf(0);
/*     */             
/* 110 */             if (entries.size() == 0) {
/* 111 */               amount = Integer.valueOf(this.craftAmount);
/* 112 */               CivGlobal.getSessionDB().add(key, amount, 0, 0, 0);
/*     */             }
/*     */             else {
/* 115 */               amount = Integer.valueOf(((SessionEntry)entries.get(0)).value);
/* 116 */               amount = Integer.valueOf(amount.intValue() + this.craftAmount);
/* 117 */               if (amount.intValue() >= 100) {
/* 118 */                 PlatinumManager.givePlatinum(this.resident, 
/* 119 */                   Integer.valueOf(((ConfigPlatinumReward)CivSettings.platinumRewards.get("craft100Items")).amount), 
/* 120 */                   "Expert crafting earns you %d");
/* 121 */                 amount = Integer.valueOf(amount.intValue() - 100);
/*     */               }
/*     */               
/* 124 */               CivGlobal.getSessionDB().update(((SessionEntry)entries.get(0)).request_id, key, amount);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 146 */         }, 0L);
/*     */       }
/*     */     }
/*     */   }
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
/*     */ 
/*     */   private boolean matrixContainsCustom(ItemStack[] matrix)
/*     */   {
/*     */     ItemStack[] arrayOfItemStack;
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
/* 187 */     int j = (arrayOfItemStack = matrix).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/* 188 */       if (LoreMaterial.isCustom(stack)) {
/* 189 */         return true;
/*     */       }
/*     */     }
/* 192 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @EventHandler(priority=EventPriority.LOW)
/*     */   public void OnPrepareItemCraftEvent(PrepareItemCraftEvent event)
/*     */   {
/* 200 */     if ((event.getRecipe() instanceof org.bukkit.inventory.ShapedRecipe)) {
/* 201 */       String key = LoreCraftableMaterial.getShapedRecipeKey(event.getInventory().getMatrix());
/* 202 */       LoreCraftableMaterial loreMat = (LoreCraftableMaterial)LoreCraftableMaterial.shapedKeys.get(key);
/*     */       
/* 204 */       if (loreMat == null) {
/* 205 */         if (LoreCraftableMaterial.isCustom(event.getRecipe().getResult()))
/*     */         {
/* 207 */           event.getInventory().setResult(new ItemStack(0));
/*     */         }
/*     */         
/* 210 */         if (matrixContainsCustom(event.getInventory().getMatrix())) {
/* 211 */           event.getInventory().setResult(new ItemStack(0));
/*     */         }
/*     */         
/* 214 */         return;
/*     */       }
/* 216 */       if (!LoreCraftableMaterial.isCustom(event.getRecipe().getResult()))
/*     */       {
/* 218 */         if (!loreMat.isVanilla()) {
/* 219 */           event.getInventory().setResult(new ItemStack(0)); return;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */       ItemStack newStack;
/*     */       
/* 226 */       if (!loreMat.isVanilla()) {
/* 227 */         ItemStack newStack = LoreMaterial.spawn(loreMat);
/* 228 */         AttributeUtil attrs = new AttributeUtil(newStack);
/* 229 */         loreMat.applyAttributes(attrs);
/* 230 */         newStack.setAmount(loreMat.getCraftAmount());
/*     */       } else {
/* 232 */         newStack = ItemManager.createItemStack(loreMat.getTypeID(), loreMat.getCraftAmount());
/*     */       }
/*     */       
/* 235 */       event.getInventory().setResult(newStack);
/*     */     }
/*     */     else {
/* 238 */       String key = LoreCraftableMaterial.getShapelessRecipeKey(event.getInventory().getMatrix());
/* 239 */       LoreCraftableMaterial loreMat = (LoreCraftableMaterial)LoreCraftableMaterial.shapelessKeys.get(key);
/*     */       
/* 241 */       if (loreMat == null) {
/* 242 */         if (LoreCraftableMaterial.isCustom(event.getRecipe().getResult()))
/*     */         {
/* 244 */           event.getInventory().setResult(new ItemStack(0));
/*     */         }
/*     */         
/* 247 */         if (matrixContainsCustom(event.getInventory().getMatrix())) {
/* 248 */           event.getInventory().setResult(new ItemStack(0));
/*     */         }
/*     */         
/* 251 */         return;
/*     */       }
/* 253 */       if (!LoreCraftableMaterial.isCustom(event.getRecipe().getResult()))
/*     */       {
/* 255 */         if (!loreMat.isVanilla()) {
/* 256 */           event.getInventory().setResult(new ItemStack(0)); return;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */       ItemStack newStack;
/*     */       
/*     */ 
/* 264 */       if (!loreMat.isVanilla()) {
/* 265 */         ItemStack newStack = LoreMaterial.spawn(loreMat);
/* 266 */         AttributeUtil attrs = new AttributeUtil(newStack);
/* 267 */         loreMat.applyAttributes(attrs);
/* 268 */         newStack.setAmount(loreMat.getCraftAmount());
/*     */       } else {
/* 270 */         newStack = ItemManager.createItemStack(loreMat.getTypeID(), loreMat.getCraftAmount());
/*     */       }
/*     */       
/* 273 */       event.getInventory().setResult(newStack);
/*     */     }
/*     */     
/* 276 */     ItemStack result = event.getInventory().getResult();
/* 277 */     LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(result);
/* 278 */     if ((craftMat != null) && 
/* 279 */       (craftMat.hasComponent("Tagged"))) {
/* 280 */       String tag = Tagged.matrixHasSameTag(event.getInventory().getMatrix());
/* 281 */       if (tag == null) {
/* 282 */         event.getInventory().setResult(ItemManager.createItemStack(0, 1));
/* 283 */         return;
/*     */       }
/*     */       
/* 286 */       Tagged tagged = (Tagged)craftMat.getComponent("Tagged");
/* 287 */       ItemStack stack = tagged.addTag(event.getInventory().getResult(), tag);
/* 288 */       AttributeUtil attrs = new AttributeUtil(stack);
/* 289 */       attrs.addLore("§7" + tag);
/* 290 */       stack = attrs.getStack();
/* 291 */       event.getInventory().setResult(stack);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\lorestorage\LoreCraftableMaterialListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
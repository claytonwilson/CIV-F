/*     */ package com.avrgaming.civcraft.loreenhancements;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.ConfigMaterial;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import gpl.AttributeUtil;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.inventory.InventoryCloseEvent;
/*     */ import org.bukkit.event.inventory.InventoryOpenEvent;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ 
/*     */ public class LoreEnhancementArenaItem extends LoreEnhancement implements org.bukkit.event.Listener
/*     */ {
/*     */   public String getDisplayName()
/*     */   {
/*  23 */     return "Arena";
/*     */   }
/*     */   
/*     */   public AttributeUtil add(AttributeUtil attrs) {
/*  27 */     attrs.addEnhancement("LoreEnhancementArenaItem", null, null);
/*  28 */     attrs.addLore("§b" + getDisplayName());
/*  29 */     return attrs;
/*     */   }
/*     */   
/*     */   private boolean isIllegalStack(ItemStack stack) {
/*  33 */     if (stack == null) {
/*  34 */       return false;
/*     */     }
/*     */     
/*  37 */     LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/*  38 */     if (craftMat == null) {
/*  39 */       return false;
/*     */     }
/*     */     
/*  42 */     if (craftMat.getConfigMaterial().required_tech == null) {
/*  43 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  49 */     if (!CivGlobal.researchedTechs.contains(craftMat.getConfigMaterial().required_tech.toLowerCase())) {
/*  50 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  54 */     return false;
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.LOWEST)
/*     */   public void onInteract(InventoryOpenEvent event)
/*     */   {
/*  60 */     if (com.avrgaming.civcraft.lorestorage.LoreGuiItemListener.isGUIInventory(event.getInventory())) {
/*  61 */       return;
/*     */     }
/*     */     
/*  64 */     String removedReason = null;
/*  65 */     ItemStack[] arrayOfItemStack; int j = (arrayOfItemStack = event.getInventory().getContents()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/*     */       
/*  67 */       if (stack != null)
/*     */       {
/*     */ 
/*     */ 
/*  71 */         LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/*  72 */         if (craftMat != null)
/*     */         {
/*     */ 
/*     */ 
/*  76 */           Resident resident = CivGlobal.getResident((Player)event.getPlayer());
/*  77 */           if (LoreCraftableMaterial.hasEnhancement(stack, "LoreEnhancementArenaItem")) {
/*  78 */             if (!resident.isInsideArena()) {
/*  79 */               event.getInventory().remove(stack);
/*  80 */               removedReason = "§7Some items were removed since they were arena items";
/*     */ 
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */           }
/*  87 */           else if ((isIllegalStack(stack)) && 
/*  88 */             (!event.getPlayer().isOp()))
/*     */           {
/*     */ 
/*     */ 
/*  92 */             if (!resident.isInsideArena()) {
/*  93 */               event.getInventory().remove(stack);
/*  94 */               removedReason = "§7Some items were detected as illegal/impossible and have been removed.";
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 101 */     if (removedReason != null) {
/* 102 */       CivMessage.send(event.getPlayer(), removedReason);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.LOWEST)
/*     */   public void onInteract(InventoryCloseEvent event)
/*     */   {
/* 109 */     if (com.avrgaming.civcraft.lorestorage.LoreGuiItemListener.isGUIInventory(event.getInventory())) {
/* 110 */       return;
/*     */     }
/*     */     
/* 113 */     String removedReason = null;
/* 114 */     ItemStack[] arrayOfItemStack1; int j = (arrayOfItemStack1 = event.getPlayer().getInventory().getContents()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack1[i];
/*     */       
/* 116 */       if (stack != null)
/*     */       {
/*     */ 
/*     */ 
/* 120 */         LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/* 121 */         if (craftMat != null)
/*     */         {
/*     */ 
/* 124 */           Resident resident = CivGlobal.getResident((Player)event.getPlayer());
/*     */           
/* 126 */           if (LoreCraftableMaterial.hasEnhancement(stack, "LoreEnhancementArenaItem")) {
/* 127 */             if (!resident.isInsideArena()) {
/* 128 */               event.getPlayer().getInventory().remove(stack);
/* 129 */               removedReason = "§7Some items were removed since they were arena items";
/*     */ 
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */           }
/* 136 */           else if ((isIllegalStack(stack)) && 
/* 137 */             (!event.getPlayer().isOp()))
/*     */           {
/*     */ 
/*     */ 
/* 141 */             if (!resident.isInsideArena()) {
/* 142 */               event.getPlayer().getInventory().remove(stack);
/* 143 */               removedReason = "§7Some items were detected as illegal/impossible and have been removed.";
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 149 */     ItemStack[] contents = new ItemStack[4];
/* 150 */     for (int i = 0; i < event.getPlayer().getInventory().getArmorContents().length; i++) {
/* 151 */       ItemStack stack = event.getPlayer().getInventory().getArmorContents()[i];
/* 152 */       if (stack != null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 157 */         LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/* 158 */         Resident resident = CivGlobal.getResident((Player)event.getPlayer());
/*     */         
/* 160 */         if (craftMat != null) {
/* 161 */           if (LoreCraftableMaterial.hasEnhancement(stack, "LoreEnhancementArenaItem")) {
/* 162 */             if (!resident.isInsideArena()) {
/*     */               continue;
/*     */             }
/* 165 */             removedReason = "§7Some items were removed since they were arena items";
/*     */           }
/* 167 */           else if ((isIllegalStack(stack)) && 
/* 168 */             (!event.getPlayer().isOp()))
/*     */           {
/*     */ 
/* 171 */             if (!resident.isInsideArena()) {
/* 172 */               removedReason = "§7Some items were detected as illegal/impossible and have been removed.";
/* 173 */               continue;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 180 */         contents[i] = stack;
/*     */       }
/*     */     }
/* 183 */     event.getPlayer().getInventory().setArmorContents(contents);
/*     */     
/* 185 */     if (removedReason != null) {
/* 186 */       CivMessage.send(event.getPlayer(), removedReason);
/*     */     }
/*     */   }
/*     */   
/*     */   public String serialize(ItemStack stack)
/*     */   {
/* 192 */     return "";
/*     */   }
/*     */   
/*     */   public ItemStack deserialize(ItemStack stack, String data)
/*     */   {
/* 197 */     return stack;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\loreenhancements\LoreEnhancementArenaItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
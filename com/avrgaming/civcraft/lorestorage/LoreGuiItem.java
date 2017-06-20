/*     */ package com.avrgaming.civcraft.lorestorage;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.ConfigBuildableInfo;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.structurevalidation.StructureValidator;
/*     */ import com.avrgaming.civcraft.template.Template;
/*     */ import com.avrgaming.civcraft.template.Template.TemplateType;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.tutorial.CivTutorial;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.global.perks.Perk;
/*     */ import com.avrgaming.global.perks.components.CustomPersonalTemplate;
/*     */ import com.avrgaming.global.perks.components.CustomTemplate;
/*     */ import gpl.AttributeUtil;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.inventory.ClickType;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
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
/*     */ public class LoreGuiItem
/*     */ {
/*     */   public static final int MAX_INV_SIZE = 54;
/*     */   
/*     */   public static ItemStack getGUIItem(String title, String[] messages, int type, int data)
/*     */   {
/*  54 */     ItemStack stack = ItemManager.createItemStack(type, 1, (short)data);
/*  55 */     AttributeUtil attrs = new AttributeUtil(stack);
/*  56 */     attrs.setCivCraftProperty("GUI", title);
/*  57 */     attrs.setName(title);
/*  58 */     attrs.setLore(messages);
/*  59 */     return attrs.getStack();
/*     */   }
/*     */   
/*     */   public static boolean isGUIItem(ItemStack stack) {
/*  63 */     AttributeUtil attrs = new AttributeUtil(stack);
/*  64 */     String title = attrs.getCivCraftProperty("GUI");
/*  65 */     if (title != null) {
/*  66 */       return true;
/*     */     }
/*  68 */     return false;
/*     */   }
/*     */   
/*     */   public static ItemStack setAction(ItemStack stack, String action) {
/*  72 */     AttributeUtil attrs = new AttributeUtil(stack);
/*  73 */     attrs.setCivCraftProperty("GUI_ACTION", action);
/*  74 */     return attrs.getStack();
/*     */   }
/*     */   
/*     */   public static String getAction(ItemStack stack) {
/*  78 */     AttributeUtil attrs = new AttributeUtil(stack);
/*  79 */     String action = attrs.getCivCraftProperty("GUI_ACTION");
/*  80 */     return action;
/*     */   }
/*     */   
/*     */   public static ItemStack build(String title, int type, int data, String... messages) {
/*  84 */     return getGUIItem(title, messages, type, data);
/*     */   }
/*     */   
/*     */   public static ItemStack asGuiItem(ItemStack stack) {
/*  88 */     AttributeUtil attrs = new AttributeUtil(stack);
/*  89 */     attrs.setCivCraftProperty("GUI", ItemManager.getId(stack));
/*  90 */     return attrs.getStack();
/*     */   }
/*     */   
/*     */   public static void processAction(String action, ItemStack stack, InventoryClickEvent event)
/*     */   {
/*  95 */     String[] args = action.split(":");
/*  96 */     Player player = (Player)event.getWhoClicked();
/*  97 */     Resident resident = CivGlobal.getResident(player);
/*     */     
/*  99 */     boolean useDefaultTemplate = true;
/*     */     
/*     */ 
/*     */     String str1;
/*     */     
/*     */ 
/* 105 */     switch ((str1 = args[0].toLowerCase()).hashCode()) {case -1944065874:  if (str1.equals("buildwithtemplate")) {} break; case -1263184537:  if (str1.equals("openinv")) {} break; case -1193896351:  if (str1.equals("activateperk")) {} break; case -1154852114:  if (str1.equals("buildwithpersonaltemplate")) {} break; case -480822150:  if (str1.equals("showperkpage")) {} break; case -392604921:  if (str1.equals("buildwithdefaulttemplate")) {} break; case 109638523:  if (str1.equals("spawn")) break; break; case 1885625415:  if (!str1.equals("buildwithdefaultpersonaltemplate"))
/*     */       {
/* 107 */         return;AttributeUtil attrs = new AttributeUtil(stack);
/* 108 */         attrs.removeCivCraftProperty("GUI");
/* 109 */         attrs.removeCivCraftProperty("GUI_ACTION");
/*     */         
/* 111 */         ItemStack is = attrs.getStack();
/* 112 */         if ((event.getClick().equals(ClickType.SHIFT_LEFT)) || 
/* 113 */           (event.getClick().equals(ClickType.SHIFT_RIGHT))) {
/* 114 */           is.setAmount(is.getMaxStackSize());
/*     */         }
/*     */         
/* 117 */         event.setCursor(is);
/* 118 */         return;
/*     */         
/* 120 */         player.closeInventory();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 162 */         TaskMaster.syncTask(new Runnable()
/*     */         {
/*     */           String[] args;
/*     */           
/*     */           public void run()
/*     */           {
/*     */             try
/*     */             {
/* 135 */               player = CivGlobal.getPlayer(LoreGuiItem.this);
/*     */             } catch (CivException e) { Player player;
/* 137 */               e.printStackTrace(); return;
/*     */             }
/*     */             
/*     */             Player player;
/* 141 */             switch ((e = this.args[1]).hashCode()) {case 440178195:  if (e.equals("showGuiInv")) {} break; case 505655232:  if (e.equals("showCraftingHelp")) break;  case 1543444001:  if ((goto 172) && (e.equals("showTutorialInventory")))
/*     */               {
/* 143 */                 CivTutorial.showTutorialInventory(player);
/* 144 */                 return;
/*     */                 
/* 146 */                 CivTutorial.showCraftingHelp(player);
/* 147 */                 return;
/*     */                 
/* 149 */                 Inventory inv = (Inventory)LoreGuiItemListener.guiInventories.get(this.args[2]);
/* 150 */                 if (inv != null) {
/* 151 */                   player.openInventory(inv);
/*     */                 } else
/* 153 */                   CivLog.error("Couldn't find GUI inventory:" + this.args[2]);
/*     */               }
/* 155 */               break;
/*     */             
/*     */ 
/*     */ 
/*     */             }
/*     */             
/*     */           }
/* 162 */         });
/* 163 */         return;
/*     */         
/*     */ 
/* 166 */         resident.showPerkPage(Integer.valueOf(args[1]).intValue());
/* 167 */         return;
/*     */         
/* 169 */         Perk perk = (Perk)resident.perks.get(args[1]);
/* 170 */         if (perk != null) {
/* 171 */           perk.onActivate(resident);
/*     */         } else {
/* 173 */           CivLog.error("Couldn't activate perk:" + args[1] + " cause it wasn't found in perks hashmap.");
/*     */         }
/* 175 */         player.closeInventory();
/*     */       }
/*     */       else {
/* 178 */         ConfigBuildableInfo info = resident.pendingBuildableInfo;
/*     */         try
/*     */         {
/* 181 */           String path = Template.getTemplateFilePath(info.template_base_name, Template.getDirection(player.getLocation()), Template.TemplateType.STRUCTURE, "default");
/*     */           try
/*     */           {
/* 184 */             tpl = Template.getTemplate(path, player.getLocation());
/*     */           } catch (IOException e) { Template tpl;
/* 186 */             e.printStackTrace(); return;
/*     */           }
/*     */           
/*     */           Template tpl;
/* 190 */           Location centerLoc = Buildable.repositionCenterStatic(player.getLocation(), info, Template.getDirection(player.getLocation()), tpl.size_x, tpl.size_z);
/*     */           
/* 192 */           TaskMaster.asyncTask(new StructureValidator(player, tpl.getFilepath(), centerLoc, resident.pendingCallback), 0L);
/* 193 */           player.closeInventory();
/*     */         }
/*     */         catch (CivException e) {
/* 196 */           CivMessage.sendError(player, e.getMessage());
/*     */         }
/*     */         
/*     */ 
/* 200 */         ConfigBuildableInfo info = resident.pendingBuildableInfo;
/*     */         try
/*     */         {
/* 203 */           Perk perk = (Perk)Perk.staticPerks.get(args[1]);
/* 204 */           CustomPersonalTemplate customTemplate = (CustomPersonalTemplate)perk.getComponent("CustomPersonalTemplate");
/* 205 */           Template tpl = customTemplate.getTemplate(player, resident.pendingBuildableInfo);
/* 206 */           Location centerLoc = Buildable.repositionCenterStatic(player.getLocation(), info, Template.getDirection(player.getLocation()), tpl.size_x, tpl.size_z);
/*     */           
/* 208 */           TaskMaster.asyncTask(new StructureValidator(player, tpl.getFilepath(), centerLoc, resident.pendingCallback), 0L);
/* 209 */           resident.desiredTemplate = tpl;
/* 210 */           player.closeInventory();
/*     */         } catch (CivException e) {
/* 212 */           CivMessage.sendError(player, e.getMessage());
/*     */         }
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
/* 224 */         useDefaultTemplate = false;
/*     */         
/*     */ 
/*     */         try
/*     */         {
/* 229 */           if (!useDefaultTemplate)
/*     */           {
/* 231 */             Perk perk = (Perk)Perk.staticPerks.get(args[1]);
/* 232 */             if (perk != null)
/*     */             {
/*     */ 
/* 235 */               CustomTemplate customTemplate = (CustomTemplate)perk.getComponent("CustomTemplate");
/* 236 */               Template tpl; Template tpl; if (customTemplate != null) {
/* 237 */                 tpl = customTemplate.getTemplate(player, resident.pendingBuildable);
/*     */               } else {
/* 239 */                 CustomPersonalTemplate customPersonalTemplate = (CustomPersonalTemplate)perk.getComponent("CustomPersonalTemplate");
/* 240 */                 tpl = customPersonalTemplate.getTemplate(player, resident.pendingBuildable.info);
/*     */               }
/*     */               
/* 243 */               resident.pendingBuildable.buildPlayerPreview(player, player.getLocation(), tpl);
/*     */             }
/*     */             else {
/* 246 */               CivLog.error("Couldn't activate perk:" + args[1] + " cause it wasn't found in perks hashmap.");
/*     */             }
/*     */           }
/*     */           else {
/* 250 */             Template tpl = new Template();
/*     */             try {
/* 252 */               tpl.initTemplate(player.getLocation(), resident.pendingBuildable);
/*     */             } catch (CivException e) {
/* 254 */               e.printStackTrace();
/* 255 */               throw e;
/*     */             } catch (IOException e) {
/* 257 */               e.printStackTrace();
/* 258 */               throw e;
/*     */             }
/*     */             
/* 261 */             resident.pendingBuildable.buildPlayerPreview(player, player.getLocation(), tpl);
/*     */           }
/*     */         } catch (CivException e) {
/* 264 */           CivMessage.sendError(player, e.getMessage());
/*     */         } catch (IOException e) {
/* 266 */           CivMessage.sendError(player, "Internal IO Error.");
/* 267 */           e.printStackTrace();
/*     */         }
/* 269 */         player.closeInventory();
/*     */       }
/*     */       break;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\lorestorage\LoreGuiItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
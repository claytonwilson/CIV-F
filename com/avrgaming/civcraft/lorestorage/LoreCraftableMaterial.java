/*     */ package com.avrgaming.civcraft.lorestorage;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigIngredient;
/*     */ import com.avrgaming.civcraft.config.ConfigMaterial;
/*     */ import com.avrgaming.civcraft.items.components.ItemComponent;
/*     */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.object.BuildableDamageBlock;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.mysql.jdbc.StringUtils;
/*     */ import gpl.AttributeUtil;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.event.block.BlockBreakEvent;
/*     */ import org.bukkit.event.block.BlockDamageEvent;
/*     */ import org.bukkit.event.block.BlockPlaceEvent;
/*     */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*     */ import org.bukkit.event.entity.EntityDeathEvent;
/*     */ import org.bukkit.event.entity.ItemSpawnEvent;
/*     */ import org.bukkit.event.entity.PlayerDeathEvent;
/*     */ import org.bukkit.event.entity.PlayerLeashEntityEvent;
/*     */ import org.bukkit.event.inventory.CraftItemEvent;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.inventory.InventoryCloseEvent;
/*     */ import org.bukkit.event.inventory.InventoryOpenEvent;
/*     */ import org.bukkit.event.player.PlayerDropItemEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEntityEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.event.player.PlayerItemDamageEvent;
/*     */ import org.bukkit.event.player.PlayerItemHeldEvent;
/*     */ import org.bukkit.event.player.PlayerPickupItemEvent;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.ShapedRecipe;
/*     */ import org.bukkit.inventory.ShapelessRecipe;
/*     */ import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoreCraftableMaterial
/*     */   extends LoreMaterial
/*     */ {
/*     */   private boolean craftable;
/*     */   private boolean shaped;
/*     */   private ConfigMaterial configMaterial;
/*  56 */   public static HashMap<String, LoreCraftableMaterial> materials = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  64 */   public static HashMap<LoreCraftableMaterial, ItemStack[]> shapedRecipes = new HashMap();
/*  65 */   public static HashMap<String, LoreCraftableMaterial> shapedKeys = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  72 */   public static HashMap<LoreCraftableMaterial, LinkedList<ItemStack>> shapelessRecipes = new HashMap();
/*  73 */   public static HashMap<String, LoreCraftableMaterial> shapelessKeys = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  79 */   public HashMap<String, ItemComponent> components = new HashMap();
/*     */   
/*     */   public LoreCraftableMaterial(String id, int typeID, short damage) {
/*  82 */     super(id, typeID, damage);
/*     */   }
/*     */   
/*     */   public static String getShapedRecipeKey(ItemStack[] matrix)
/*     */   {
/*  87 */     String key = "";
/*  88 */     for (int i = 0; i < matrix.length; i++) {
/*  89 */       key = key + i + ":";
/*     */       
/*  91 */       ItemStack stack = matrix[i];
/*  92 */       if (stack == null) {
/*  93 */         key = key + "null,";
/*     */ 
/*     */ 
/*     */       }
/*  97 */       else if (LoreMaterial.isCustom(stack)) {
/*  98 */         LoreMaterial loreMat = LoreMaterial.getMaterial(stack);
/*  99 */         key = key + loreMat.getId() + ",";
/*     */       }
/*     */       else {
/* 102 */         key = key + "mc_" + ItemManager.getId(stack) + ",";
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 107 */     return key;
/*     */   }
/*     */   
/*     */   public static String getShapelessRecipeKey(ItemStack[] matrix) {
/* 111 */     HashMap<String, Integer> counts = new HashMap();
/* 112 */     List<String> items = new LinkedList();
/*     */     
/*     */     ItemStack stack;
/* 115 */     for (int i = 0; i < matrix.length; i++) {
/* 116 */       stack = matrix[i];
/*     */       
/* 118 */       if ((stack != null) && (ItemManager.getId(stack) != 0))
/*     */       {
/*     */         String item;
/*     */         
/*     */         String item;
/* 123 */         if (LoreMaterial.isCustom(stack)) {
/* 124 */           item = LoreMaterial.getMaterial(stack).getId();
/*     */         }
/*     */         else {
/* 127 */           item = "mc_" + ItemManager.getId(stack) + ",";
/*     */         }
/*     */         
/* 130 */         Integer count = (Integer)counts.get(item);
/* 131 */         if (count == null) {
/* 132 */           count = Integer.valueOf(1);
/*     */         } else {
/* 134 */           count = Integer.valueOf(count.intValue() + 1);
/*     */         }
/* 136 */         counts.put(item, count);
/*     */       }
/*     */     }
/*     */     Integer count;
/* 140 */     for (String item : counts.keySet()) {
/* 141 */       count = (Integer)counts.get(item);
/* 142 */       items.add(item + ":" + count);
/*     */     }
/*     */     
/*     */ 
/* 146 */     Collections.sort(items);
/*     */     
/* 148 */     String fullString = "";
/* 149 */     for (String item : items) {
/* 150 */       fullString = fullString + item + ",";
/*     */     }
/*     */     
/* 153 */     return fullString;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void buildStaticMaterials()
/*     */   {
/* 160 */     for (ConfigMaterial cfgMat : CivSettings.materials.values()) {
/* 161 */       LoreCraftableMaterial loreMat = new LoreCraftableMaterial(cfgMat.id, cfgMat.item_id, (short)cfgMat.item_data);
/*     */       
/* 163 */       loreMat.setName(cfgMat.name);
/* 164 */       if (cfgMat.lore != null) {
/* 165 */         loreMat.setLore(cfgMat.lore);
/*     */       }
/*     */       
/* 168 */       loreMat.setCraftable(cfgMat.craftable);
/* 169 */       loreMat.setShaped(cfgMat.shaped);
/* 170 */       loreMat.configMaterial = cfgMat;
/* 171 */       loreMat.buildComponents();
/*     */       
/* 173 */       materials.put(cfgMat.id, loreMat);
/*     */     }
/*     */   }
/*     */   
/*     */   private void buildComponents() {
/* 178 */     List<HashMap<String, String>> compInfoList = this.configMaterial.components;
/* 179 */     if (compInfoList != null) {
/* 180 */       for (HashMap<String, String> compInfo : compInfoList) {
/* 181 */         String className = "com.avrgaming.civcraft.items.components." + (String)compInfo.get("name");
/*     */         
/*     */         try
/*     */         {
/* 185 */           Class<?> someClass = Class.forName(className);
/*     */           
/* 187 */           ItemComponent itemCompClass = (ItemComponent)someClass.newInstance();
/* 188 */           itemCompClass.setName((String)compInfo.get("name"));
/*     */           
/* 190 */           for (String key : compInfo.keySet()) {
/* 191 */             itemCompClass.setAttribute(key, (String)compInfo.get(key));
/*     */           }
/*     */           
/* 194 */           itemCompClass.createComponent();
/* 195 */           this.components.put(itemCompClass.getName(), itemCompClass);
/*     */         } catch (InstantiationException e) {
/* 197 */           e.printStackTrace();
/*     */         } catch (IllegalAccessException e) {
/* 199 */           e.printStackTrace();
/*     */         }
/*     */         catch (ClassNotFoundException e) {
/* 202 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void buildRecipes()
/*     */   {
/* 212 */     for (LoreCraftableMaterial loreMat : materials.values()) {
/* 213 */       if (loreMat.isCraftable())
/*     */       {
/*     */ 
/*     */ 
/* 217 */         ItemStack stack = LoreMaterial.spawn(loreMat);
/* 218 */         ConfigMaterial configMaterial = loreMat.configMaterial;
/*     */         int i;
/* 220 */         if (loreMat.isShaped()) {
/* 221 */           ItemStack[] matrix = new ItemStack[10];
/* 222 */           ShapedRecipe recipe = new ShapedRecipe(stack);
/* 223 */           recipe.shape(new String[] { configMaterial.shape[0], configMaterial.shape[1], configMaterial.shape[2] });
/*     */           int j;
/*     */           int i;
/* 226 */           for (Iterator localIterator2 = configMaterial.incredients.values().iterator(); localIterator2.hasNext(); 
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
/* 250 */               i < j)
/*     */           {
/* 226 */             ConfigIngredient ingred = (ConfigIngredient)localIterator2.next();
/* 227 */             ItemStack ingredStack = null;
/*     */             
/* 229 */             if (ingred.custom_id == null) {
/* 230 */               recipe.setIngredient(ingred.letter.charAt(0), ItemManager.getMaterialData(ingred.type_id, ingred.data));
/* 231 */               ingredStack = ItemManager.createItemStack(ingred.type_id, 1, (short)ingred.data);
/*     */             } else {
/* 233 */               LoreCraftableMaterial customLoreMat = (LoreCraftableMaterial)materials.get(ingred.custom_id);
/* 234 */               if (customLoreMat == null) {
/* 235 */                 CivLog.warning("Couldn't find custom material id:" + ingred.custom_id);
/*     */               }
/*     */               
/* 238 */               ConfigMaterial customMat = customLoreMat.configMaterial;
/* 239 */               if (customMat != null) {
/* 240 */                 recipe.setIngredient(ingred.letter.charAt(0), ItemManager.getMaterialData(customMat.item_id, customMat.item_data));
/*     */               } else {
/* 242 */                 CivLog.warning("Couldn't find custom material id:" + ingred.custom_id);
/*     */               }
/*     */               
/* 245 */               ingredStack = LoreMaterial.spawn(customLoreMat);
/*     */             }
/*     */             
/*     */ 
/* 249 */             i = 0;
/* 250 */             String[] arrayOfString; j = (arrayOfString = configMaterial.shape).length;i = 0; continue;String row = arrayOfString[i];
/* 251 */             for (int c = 0; c < row.length(); c++) {
/* 252 */               if (row.charAt(c) == ingred.letter.charAt(0)) {
/* 253 */                 matrix[i] = ingredStack;
/* 254 */               } else if (row.charAt(c) == ' ') {
/* 255 */                 matrix[i] = new ItemStack(Material.AIR, 0, -1);
/*     */               }
/* 257 */               i++;
/*     */             }
/* 250 */             i++;
/*     */           }
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
/* 263 */           shapedRecipes.put(loreMat, matrix);
/* 264 */           String key = getShapedRecipeKey(matrix);
/* 265 */           shapedKeys.put(key, loreMat);
/*     */           
/*     */ 
/*     */ 
/* 269 */           Bukkit.getServer().addRecipe(recipe);
/*     */         }
/*     */         else {
/* 272 */           ShapelessRecipe recipe = new ShapelessRecipe(stack);
/* 273 */           LinkedList<ItemStack> items = new LinkedList();
/* 274 */           ItemStack[] matrix = new ItemStack[9];
/* 275 */           int matrixIndex = 0;
/*     */           
/*     */ 
/* 278 */           for (ConfigIngredient ingred : configMaterial.incredients.values()) {
/* 279 */             ItemStack ingredStack = null;
/*     */             try
/*     */             {
/* 282 */               if (ingred.custom_id == null) {
/* 283 */                 recipe.addIngredient(ingred.count, ItemManager.getMaterialData(ingred.type_id, ingred.data));
/* 284 */                 ingredStack = ItemManager.createItemStack(ingred.type_id, 1, (short)ingred.data);
/*     */               } else {
/* 286 */                 LoreCraftableMaterial customLoreMat = (LoreCraftableMaterial)materials.get(ingred.custom_id);
/* 287 */                 if (customLoreMat == null) {
/* 288 */                   CivLog.error("Couldn't configure ingredient:" + ingred.custom_id + " in config mat:" + configMaterial.id);
/*     */                 }
/* 290 */                 ConfigMaterial customMat = customLoreMat.configMaterial;
/* 291 */                 if (customMat != null) {
/* 292 */                   recipe.addIngredient(ingred.count, ItemManager.getMaterialData(customMat.item_id, customMat.item_data));
/* 293 */                   ingredStack = LoreMaterial.spawn(customLoreMat);
/*     */                 } else {
/* 295 */                   CivLog.warning("Couldn't find custom material id:" + ingred.custom_id);
/*     */                 }
/*     */               }
/*     */             } catch (IllegalArgumentException e) {
/* 299 */               CivLog.warning("Trying to process ingredient:" + ingred.type_id + ":" + ingred.custom_id + " for material:" + configMaterial.id);
/* 300 */               throw e;
/*     */             }
/*     */             
/* 303 */             if (ingredStack != null)
/*     */             {
/* 305 */               for (int i = 0; i < ingred.count; i++) {
/* 306 */                 if (matrixIndex > 9) {
/*     */                   break;
/*     */                 }
/*     */                 
/* 310 */                 matrix[matrixIndex] = ingredStack;
/* 311 */                 matrixIndex++;
/*     */               }
/*     */               
/* 314 */               ingredStack.setAmount(ingred.count);
/* 315 */               items.add(ingredStack);
/*     */             }
/*     */           }
/*     */           
/* 319 */           shapelessRecipes.put(loreMat, items);
/* 320 */           String key = getShapelessRecipeKey(matrix);
/* 321 */           shapelessKeys.put(key, loreMat);
/*     */           
/*     */ 
/* 324 */           Bukkit.getServer().addRecipe(recipe);
/*     */         }
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
/*     */   public void onHit(EntityDamageByEntityEvent event) {}
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
/*     */   public void onInteract(PlayerInteractEvent event)
/*     */   {
/* 361 */     for (ItemComponent ic : this.components.values()) {
/* 362 */       ic.onInteract(event);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void onInteractEntity(PlayerInteractEntityEvent event) {}
/*     */   
/*     */ 
/*     */   public void onBlockPlaced(BlockPlaceEvent event)
/*     */   {
/* 372 */     boolean allow = false;
/* 373 */     for (ItemComponent ic : this.components.values()) {
/* 374 */       allow = ic.onBlockPlaced(event);
/*     */     }
/*     */     
/* 377 */     if (!allow) {
/* 378 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onBlockBreak(BlockBreakEvent event) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onBlockDamage(BlockDamageEvent event) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onBlockInteract(PlayerInteractEvent event) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void onHold(PlayerItemHeldEvent event)
/*     */   {
/* 401 */     for (ItemComponent comp : this.components.values()) {
/* 402 */       comp.onHold(event);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onDrop(PlayerDropItemEvent event) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onItemDrop(PlayerDropItemEvent event) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onItemCraft(CraftItemEvent event) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onItemPickup(PlayerPickupItemEvent event) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onItemSpawn(ItemSpawnEvent event)
/*     */   {
/* 432 */     for (ItemComponent comp : this.components.values()) {
/* 433 */       comp.onItemSpawn(event);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean onAttack(EntityDamageByEntityEvent event, ItemStack stack)
/*     */   {
/* 440 */     for (ItemComponent comp : this.components.values()) {
/* 441 */       comp.onAttack(event, stack);
/*     */     }
/* 443 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onInvItemPickup(InventoryClickEvent event, Inventory fromInv, ItemStack stack) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onInvItemDrop(InventoryClickEvent event, Inventory toInv, ItemStack stack) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onInvShiftClick(InventoryClickEvent event, Inventory fromInv, Inventory toInv, ItemStack stack) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onInvItemSwap(InventoryClickEvent event, Inventory toInv, ItemStack droppedStack, ItemStack pickedStack) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onPlayerDeath(EntityDeathEvent event, ItemStack stack) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onInventoryClose(InventoryCloseEvent event) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int onStructureBlockBreak(BuildableDamageBlock sb, int damage)
/*     */   {
/* 488 */     for (ItemComponent component : this.components.values()) {
/* 489 */       damage = component.onStructureBlockBreak(sb, damage);
/*     */     }
/*     */     
/* 492 */     return damage;
/*     */   }
/*     */   
/*     */ 
/*     */   public void applyAttributes(AttributeUtil attrUtil)
/*     */   {
/* 498 */     for (ItemComponent comp : this.components.values()) {
/* 499 */       comp.onPrepareCreate(attrUtil);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isCraftable()
/*     */   {
/* 505 */     return this.craftable;
/*     */   }
/*     */   
/*     */   public void setCraftable(boolean craftable) {
/* 509 */     this.craftable = craftable;
/*     */   }
/*     */   
/*     */   public boolean isShaped() {
/* 513 */     return this.shaped;
/*     */   }
/*     */   
/*     */   public void setShaped(boolean shaped) {
/* 517 */     this.shaped = shaped;
/*     */   }
/*     */   
/*     */   public ConfigMaterial getConfigMaterial() {
/* 521 */     return this.configMaterial;
/*     */   }
/*     */   
/*     */   public String getConfigId() {
/* 525 */     return this.configMaterial.id;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 530 */     return this.configMaterial.id.hashCode();
/*     */   }
/*     */   
/*     */   public Collection<ItemComponent> getComponents() {
/* 534 */     return this.components.values();
/*     */   }
/*     */   
/*     */   public void addComponent(ItemComponent itemComp) {
/* 538 */     this.components.put(itemComp.getName(), itemComp);
/*     */   }
/*     */   
/*     */ 
/*     */   public void onDefense(EntityDamageByEntityEvent event, ItemStack stack)
/*     */   {
/* 544 */     for (ItemComponent comp : this.components.values()) {
/* 545 */       comp.onDefense(event, stack);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onItemDurabilityChange(PlayerItemDamageEvent event) {
/* 550 */     for (ItemComponent comp : this.components.values()) {
/* 551 */       comp.onDurabilityChange(event);
/*     */     }
/*     */   }
/*     */   
/*     */   public static LoreCraftableMaterial getCraftMaterial(ItemStack stack) {
/* 556 */     if (stack == null) {
/* 557 */       return null;
/*     */     }
/*     */     
/* 560 */     LoreMaterial mat = (LoreMaterial)materialMap.get(getMID(stack));
/* 561 */     if ((mat instanceof LoreCraftableMaterial)) {
/* 562 */       return (LoreCraftableMaterial)mat;
/*     */     }
/*     */     
/* 565 */     return null;
/*     */   }
/*     */   
/*     */   public boolean hasComponent(String string)
/*     */   {
/* 570 */     return this.components.containsKey(string);
/*     */   }
/*     */   
/*     */   public static LoreCraftableMaterial getCraftMaterialFromId(String mid)
/*     */   {
/* 575 */     LoreMaterial mat = (LoreMaterial)materialMap.get(mid);
/* 576 */     if ((mat instanceof LoreCraftableMaterial)) {
/* 577 */       return (LoreCraftableMaterial)mat;
/*     */     }
/* 579 */     return null;
/*     */   }
/*     */   
/*     */   public ItemComponent getComponent(String string)
/*     */   {
/* 584 */     return (ItemComponent)this.components.get(string);
/*     */   }
/*     */   
/*     */ 
/*     */   public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event)
/*     */   {
/* 590 */     for (ItemComponent comp : this.components.values()) {
/* 591 */       comp.onPlayerInteractEntity(event);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onPlayerLeashEvent(PlayerLeashEntityEvent event)
/*     */   {
/* 597 */     for (ItemComponent comp : this.components.values()) {
/* 598 */       comp.onPlayerLeashEvent(event);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onRangedAttack(EntityDamageByEntityEvent event, ItemStack inHand)
/*     */   {
/* 604 */     for (ItemComponent comp : this.components.values()) {
/* 605 */       comp.onRangedAttack(event, inHand);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemChangeResult onDurabilityDeath(PlayerDeathEvent event, ItemStack stack)
/*     */   {
/* 616 */     ItemChangeResult result = null;
/* 617 */     for (ItemComponent comp : this.components.values()) {
/* 618 */       result = comp.onDurabilityDeath(event, result, stack);
/*     */     }
/* 620 */     return result;
/*     */   }
/*     */   
/*     */   public void onInventoryOpen(InventoryOpenEvent event, ItemStack stack)
/*     */   {
/* 625 */     for (ItemComponent comp : this.components.values()) {
/* 626 */       comp.onInventoryOpen(event, stack);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isVanilla()
/*     */   {
/* 632 */     return this.configMaterial.vanilla;
/*     */   }
/*     */   
/*     */   public int getCraftAmount()
/*     */   {
/* 637 */     return this.configMaterial.amount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void rebuildLore() {}
/*     */   
/*     */ 
/*     */   public static String serializeEnhancements(ItemStack stack)
/*     */   {
/* 647 */     String out = "";
/*     */     
/* 649 */     for (LoreEnhancement enh : getEnhancements(stack)) {
/* 650 */       out = out + enh.getClass().getName() + "@" + enh.serialize(stack) + ",";
/*     */     }
/*     */     
/* 653 */     String outEncoded = new String(Base64Coder.encode(out.getBytes()));
/* 654 */     return outEncoded;
/*     */   }
/*     */   
/*     */   public static ItemStack deserializeEnhancements(ItemStack stack, String serial) {
/* 658 */     String in = StringUtils.toAsciiString(Base64Coder.decode(serial));
/* 659 */     String[] enhancementsStrs = in.split(",");
/*     */     String[] arrayOfString1;
/* 661 */     int j = (arrayOfString1 = enhancementsStrs).length; for (int i = 0; i < j; i++) { String enhString = arrayOfString1[i];
/* 662 */       String[] split = enhString.split("@");
/* 663 */       String className = split[0];
/* 664 */       String data = "";
/* 665 */       if (split.length > 1) {
/* 666 */         data = split[1];
/*     */       }
/*     */       try
/*     */       {
/* 670 */         Class<?> cls = Class.forName(className);
/* 671 */         LoreEnhancement enh = (LoreEnhancement)cls.newInstance();
/* 672 */         AttributeUtil attrs = new AttributeUtil(stack);
/* 673 */         attrs.addEnhancement(cls.getSimpleName(), null, null);
/* 674 */         stack = attrs.getStack();
/* 675 */         stack = enh.deserialize(stack, data);
/*     */       } catch (ClassNotFoundException|InstantiationException|IllegalAccessException e) {
/* 677 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 682 */     return stack;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\lorestorage\LoreCraftableMaterial.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
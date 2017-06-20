/*     */ package com.avrgaming.civcraft.items.units;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigMission;
/*     */ import com.avrgaming.civcraft.config.ConfigUnit;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreMaterial;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.EntityEquipment;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
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
/*     */ public abstract class Unit
/*     */ {
/*     */   public static Spy SPY_UNIT;
/*     */   public static Settler SETTLER_UNIT;
/*  41 */   public static ArrayList<MissionBook> SPY_MISSIONS = new ArrayList();
/*     */   public static MissionBook SPY_INVESTIGATE_TOWN;
/*     */   public static MissionBook SPY_STEAL_TREASURY;
/*     */   public static MissionBook SPY_SUBVERT_GOVERNMENT;
/*     */   public static MissionBook SPY_POISON_GRANARY;
/*     */   public static MissionBook SPY_PIRATE;
/*     */   public static MissionBook SPY_SABOTAGE;
/*     */   
/*     */   public static void init()
/*     */   {
/*  51 */     SPY_UNIT = new Spy("u_spy", (ConfigUnit)CivSettings.units.get("u_spy"));
/*     */     
/*  53 */     for (ConfigMission mission : CivSettings.missions.values()) {
/*  54 */       if (mission.slot.intValue() > 0) {
/*  55 */         MissionBook book = new MissionBook(mission.id, 403, (short)0);
/*  56 */         book.setName(mission.name);
/*  57 */         book.setupLore(book.getId());
/*  58 */         book.setParent(SPY_UNIT);
/*  59 */         book.setSocketSlot(mission.slot.intValue());
/*  60 */         SPY_UNIT.addMissionBook(book);
/*  61 */         SPY_MISSIONS.add(book);
/*     */       }
/*     */     }
/*     */     
/*  65 */     SETTLER_UNIT = new Settler("u_settler", (ConfigUnit)CivSettings.units.get("u_settler"));
/*     */   }
/*     */   
/*     */ 
/*     */   public Unit() {}
/*     */   
/*     */ 
/*     */   public Unit(Inventory inv)
/*     */     throws CivException
/*     */   {}
/*     */   
/*     */ 
/*     */   protected static boolean addItemNoStack(Inventory inv, ItemStack stack)
/*     */   {
/*  79 */     ItemStack[] contents = inv.getContents();
/*  80 */     for (int i = 0; i < contents.length; i++) {
/*  81 */       if (contents[i] == null) {
/*  82 */         contents[i] = stack;
/*  83 */         inv.setContents(contents);
/*  84 */         return true;
/*     */       }
/*     */     }
/*     */     
/*  88 */     return false;
/*     */   }
/*     */   
/*     */   public static ConfigUnit getPlayerUnit(Player player) {
/*     */     ItemStack[] arrayOfItemStack;
/*  93 */     int j = (arrayOfItemStack = player.getInventory().getContents()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/*  94 */       if (stack != null)
/*     */       {
/*     */ 
/*     */ 
/*  98 */         LoreMaterial material = LoreMaterial.getMaterial(stack);
/*  99 */         if ((material != null) && ((material instanceof UnitMaterial)))
/*     */         {
/* 101 */           if (!UnitMaterial.validateUnitUse(player, stack)) {
/* 102 */             return null;
/*     */           }
/*     */           
/*     */ 
/* 106 */           return ((UnitMaterial)material).getUnit();
/*     */         }
/*     */       }
/*     */     }
/* 110 */     return null;
/*     */   }
/*     */   
/*     */   public static void removeUnit(Player player)
/*     */   {
/*     */     ItemStack[] arrayOfItemStack;
/* 116 */     int j = (arrayOfItemStack = player.getInventory().getContents()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/* 117 */       if (stack != null) {
/* 118 */         LoreMaterial material = LoreMaterial.getMaterial(stack);
/* 119 */         if (material != null) {
/* 120 */           if ((material instanceof UnitMaterial)) {
/* 121 */             player.getInventory().remove(stack);
/*     */ 
/*     */ 
/*     */           }
/* 125 */           else if ((material instanceof UnitItemMaterial)) {
/* 126 */             player.getInventory().remove(stack);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 133 */     player.updateInventory();
/*     */   }
/*     */   
/*     */   public static boolean isWearingFullLeather(Player player)
/*     */   {
/*     */     try {
/* 139 */       if (ItemManager.getId(player.getEquipment().getBoots()) != 301) {
/* 140 */         return false;
/*     */       }
/*     */       
/* 143 */       if (ItemManager.getId(player.getEquipment().getChestplate()) != 299) {
/* 144 */         return false;
/*     */       }
/*     */       
/* 147 */       if (ItemManager.getId(player.getEquipment().getHelmet()) != 298) {
/* 148 */         return false;
/*     */       }
/*     */       
/* 151 */       if (ItemManager.getId(player.getEquipment().getLeggings()) != 300) {
/* 152 */         return false;
/*     */       }
/*     */     }
/*     */     catch (NullPointerException e) {
/* 156 */       return false;
/*     */     }
/* 158 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isWearingFullComposite(Player player) {
/*     */     ItemStack[] arrayOfItemStack;
/* 163 */     int j = (arrayOfItemStack = player.getInventory().getArmorContents()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/*     */       
/* 165 */       LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/* 166 */       if (craftMat == null) {
/* 167 */         return false;
/*     */       }
/*     */       
/* 170 */       if (!craftMat.getConfigId().equals("mat_composite_leather_helmet")) {
/* 171 */         return false;
/*     */       }
/*     */       
/* 174 */       if (!craftMat.getConfigId().equals("mat_composite_leather_chestplate")) {
/* 175 */         return false;
/*     */       }
/*     */       
/* 178 */       if (!craftMat.getConfigId().equals("mat_composite_leather_leggings")) {
/* 179 */         return false;
/*     */       }
/*     */       
/* 182 */       if (!craftMat.getConfigId().equals("mat_composite_leather_boots")) {
/* 183 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 187 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isWearingFullHardened(Player player) {
/*     */     ItemStack[] arrayOfItemStack;
/* 192 */     int j = (arrayOfItemStack = player.getInventory().getArmorContents()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/*     */       
/* 194 */       LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/* 195 */       if (craftMat == null) {
/* 196 */         return false;
/*     */       }
/*     */       
/* 199 */       if (!craftMat.getConfigId().equals("mat_hardened_leather_helmet")) {
/* 200 */         return false;
/*     */       }
/*     */       
/* 203 */       if (!craftMat.getConfigId().equals("mat_hardened_leather_chestplate")) {
/* 204 */         return false;
/*     */       }
/*     */       
/* 207 */       if (!craftMat.getConfigId().equals("mat_hardened_leather_leggings")) {
/* 208 */         return false;
/*     */       }
/*     */       
/* 211 */       if (!craftMat.getConfigId().equals("mat_hardened_leather_boots")) {
/* 212 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 216 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isWearingFullRefined(Player player) {
/*     */     ItemStack[] arrayOfItemStack;
/* 221 */     int j = (arrayOfItemStack = player.getInventory().getArmorContents()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/*     */       
/* 223 */       LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/* 224 */       if (craftMat == null) {
/* 225 */         return false;
/*     */       }
/*     */       
/* 228 */       if (!craftMat.getConfigId().equals("mat_refined_leather_helmet")) {
/* 229 */         return false;
/*     */       }
/*     */       
/* 232 */       if (!craftMat.getConfigId().equals("mat_refined_leather_chestplate")) {
/* 233 */         return false;
/*     */       }
/*     */       
/* 236 */       if (!craftMat.getConfigId().equals("mat_refined_leather_leggings")) {
/* 237 */         return false;
/*     */       }
/*     */       
/* 240 */       if (!craftMat.getConfigId().equals("mat_refined_leather_boots")) {
/* 241 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 245 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isWearingFullBasicLeather(Player player) {
/*     */     ItemStack[] arrayOfItemStack;
/* 250 */     int j = (arrayOfItemStack = player.getInventory().getArmorContents()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/*     */       
/* 252 */       LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/* 253 */       if (craftMat == null) {
/* 254 */         return false;
/*     */       }
/*     */       
/* 257 */       if (!craftMat.getConfigId().equals("mat_leather_helmet")) {
/* 258 */         return false;
/*     */       }
/*     */       
/* 261 */       if (!craftMat.getConfigId().equals("mat_leather_chestplate")) {
/* 262 */         return false;
/*     */       }
/*     */       
/* 265 */       if (!craftMat.getConfigId().equals("mat_leather_leggings")) {
/* 266 */         return false;
/*     */       }
/*     */       
/* 269 */       if (!craftMat.getConfigId().equals("mat_leather_boots")) {
/* 270 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 274 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isWearingAnyMetal(Player player) {
/* 278 */     return (isWearingAnyChain(player)) || (isWearingAnyGold(player)) || (isWearingAnyIron(player)) || (isWearingAnyDiamond(player));
/*     */   }
/*     */   
/*     */   public static boolean isWearingAnyChain(Player player)
/*     */   {
/* 283 */     if ((player.getEquipment().getBoots() != null) && 
/* 284 */       (player.getEquipment().getBoots().getType().equals(Material.CHAINMAIL_BOOTS))) {
/* 285 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 289 */     if ((player.getEquipment().getChestplate() != null) && 
/* 290 */       (player.getEquipment().getChestplate().getType().equals(Material.CHAINMAIL_CHESTPLATE))) {
/* 291 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 295 */     if ((player.getEquipment().getHelmet() != null) && 
/* 296 */       (player.getEquipment().getHelmet().getType().equals(Material.CHAINMAIL_HELMET))) {
/* 297 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 301 */     if ((player.getEquipment().getLeggings() != null) && 
/* 302 */       (player.getEquipment().getLeggings().getType().equals(Material.CHAINMAIL_LEGGINGS))) {
/* 303 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 307 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean isWearingAnyGold(Player player)
/*     */   {
/* 313 */     if ((player.getEquipment().getBoots() != null) && 
/* 314 */       (player.getEquipment().getBoots().getType().equals(Material.GOLD_BOOTS))) {
/* 315 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 319 */     if ((player.getEquipment().getChestplate() != null) && 
/* 320 */       (player.getEquipment().getChestplate().getType().equals(Material.GOLD_CHESTPLATE))) {
/* 321 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 325 */     if ((player.getEquipment().getHelmet() != null) && 
/* 326 */       (player.getEquipment().getHelmet().getType().equals(Material.GOLD_HELMET))) {
/* 327 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 331 */     if ((player.getEquipment().getLeggings() != null) && 
/* 332 */       (player.getEquipment().getLeggings().getType().equals(Material.GOLD_LEGGINGS))) {
/* 333 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 337 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean isWearingAnyIron(Player player)
/*     */   {
/* 343 */     if ((player.getEquipment().getBoots() != null) && 
/* 344 */       (ItemManager.getId(player.getEquipment().getBoots()) == 309)) {
/* 345 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 349 */     if ((player.getEquipment().getChestplate() != null) && 
/* 350 */       (ItemManager.getId(player.getEquipment().getChestplate()) == 307)) {
/* 351 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 355 */     if ((player.getEquipment().getHelmet() != null) && 
/* 356 */       (ItemManager.getId(player.getEquipment().getHelmet()) == 306)) {
/* 357 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 361 */     if ((player.getEquipment().getLeggings() != null) && 
/* 362 */       (ItemManager.getId(player.getEquipment().getLeggings()) == 308)) {
/* 363 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 367 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isWearingAnyDiamond(Player player)
/*     */   {
/* 372 */     if ((player.getEquipment().getBoots() != null) && 
/* 373 */       (ItemManager.getId(player.getEquipment().getBoots()) == 313)) {
/* 374 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 378 */     if ((player.getEquipment().getChestplate() != null) && 
/* 379 */       (ItemManager.getId(player.getEquipment().getChestplate()) == 311)) {
/* 380 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 384 */     if ((player.getEquipment().getHelmet() != null) && 
/* 385 */       (ItemManager.getId(player.getEquipment().getHelmet()) == 310)) {
/* 386 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 390 */     if ((player.getEquipment().getLeggings() != null) && 
/* 391 */       (ItemManager.getId(player.getEquipment().getLeggings()) == 312)) {
/* 392 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 396 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\units\Unit.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
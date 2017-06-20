/*     */ package com.avrgaming.civcraft.lorestorage;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.ConfigMaterial;
/*     */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
/*     */ import com.avrgaming.civcraft.object.BuildableDamageBlock;
/*     */ import com.avrgaming.civcraft.util.CivColor;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import gpl.AttributeUtil;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.block.BlockBreakEvent;
/*     */ import org.bukkit.event.block.BlockDamageEvent;
/*     */ import org.bukkit.event.block.BlockPlaceEvent;
/*     */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*     */ import org.bukkit.event.entity.EntityDeathEvent;
/*     */ import org.bukkit.event.entity.ItemSpawnEvent;
/*     */ import org.bukkit.event.inventory.CraftItemEvent;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.inventory.InventoryCloseEvent;
/*     */ import org.bukkit.event.inventory.InventoryOpenEvent;
/*     */ import org.bukkit.event.player.PlayerDropItemEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEntityEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.event.player.PlayerItemHeldEvent;
/*     */ import org.bukkit.event.player.PlayerPickupItemEvent;
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
/*     */ 
/*     */ public abstract class LoreMaterial
/*     */ {
/*     */   private String id;
/*     */   private int typeID;
/*     */   private short damage;
/*  57 */   private LinkedList<String> lore = new LinkedList();
/*     */   
/*     */   private String name;
/*  60 */   public static Map<String, LoreMaterial> materialMap = new HashMap();
/*     */   public static final String MID_TAG = "§0MID";
/*     */   
/*     */   public LoreMaterial(String id, int typeID, short damage) {
/*  64 */     this.id = id;
/*  65 */     this.typeID = typeID;
/*  66 */     this.damage = damage;
/*     */     
/*  68 */     materialMap.put(id, this);
/*     */   }
/*     */   
/*     */   public String getId() {
/*  72 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/*  76 */     this.id = id;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getMID(ItemStack stack)
/*     */   {
/* 103 */     AttributeUtil attrs = new AttributeUtil(stack);
/*     */     
/* 105 */     String mid = attrs.getCivCraftProperty("mid");
/* 106 */     if (mid == null) {
/* 107 */       return "";
/*     */     }
/*     */     
/* 110 */     return mid;
/*     */   }
/*     */   
/*     */   public static void setMIDAndName(AttributeUtil attrs, String mid, String name) {
/* 114 */     attrs.setCivCraftProperty("mid", mid);
/* 115 */     attrs.setName(name);
/*     */   }
/*     */   
/*     */   public static boolean isCustom(ItemStack stack) {
/* 119 */     if (stack == null) {
/* 120 */       return false;
/*     */     }
/*     */     
/* 123 */     LoreMaterial material = getMaterial(stack);
/* 124 */     if (material == null) {
/* 125 */       return false;
/*     */     }
/* 127 */     return true;
/*     */   }
/*     */   
/*     */   public static LoreMaterial getMaterial(ItemStack stack) {
/* 131 */     if (stack == null) {
/* 132 */       return null;
/*     */     }
/* 134 */     return (LoreMaterial)materialMap.get(getMID(stack));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void moveDropSet(Player player, Inventory inv, int slot, ItemStack newItem)
/*     */   {
/* 144 */     ItemStack stack = inv.getItem(slot);
/* 145 */     inv.setItem(slot, newItem);
/*     */     
/* 147 */     if (stack != null) {
/* 148 */       if (stack.equals(newItem)) {
/* 149 */         return;
/*     */       }
/*     */       
/* 152 */       HashMap<Integer, ItemStack> leftovers = inv.addItem(new ItemStack[] { stack });
/*     */       
/* 154 */       for (ItemStack s : leftovers.values()) {
/* 155 */         player.getWorld().dropItem(player.getLocation(), s);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Player getPlayer(InventoryClickEvent event)
/*     */   {
/* 162 */     if ((event.getWhoClicked() instanceof Player)) {
/* 163 */       return (Player)event.getWhoClicked();
/*     */     }
/* 165 */     return null;
/*     */   }
/*     */   
/*     */   public static ItemStack spawn(LoreMaterial material) {
/* 169 */     ItemStack stack = ItemManager.createItemStack(material.getTypeID(), 1, material.getDamage());
/* 170 */     AttributeUtil attrs = new AttributeUtil(stack);
/* 171 */     setMIDAndName(attrs, material.getId(), material.getName());
/*     */     
/* 173 */     if ((material instanceof LoreCraftableMaterial)) {
/* 174 */       LoreCraftableMaterial craftMat = (LoreCraftableMaterial)material;
/*     */       
/* 176 */       attrs.addLore(CivColor.ITALIC + craftMat.getConfigMaterial().category);
/*     */     }
/*     */     
/* 179 */     material.applyAttributes(attrs);
/* 180 */     return attrs.getStack();
/*     */   }
/*     */   
/*     */   public int getTypeID() {
/* 184 */     return this.typeID;
/*     */   }
/*     */   
/*     */   public void setTypeID(int typeID) {
/* 188 */     this.typeID = typeID;
/*     */   }
/*     */   
/*     */   public short getDamage() {
/* 192 */     return this.damage;
/*     */   }
/*     */   
/*     */   public void setDamage(short damage) {
/* 196 */     this.damage = damage;
/*     */   }
/*     */   
/*     */   public void addLore(String lore) {
/* 200 */     this.lore.add(lore);
/*     */   }
/*     */   
/*     */   public void setLore(String lore) {
/* 204 */     this.lore.clear();
/* 205 */     this.lore.add(lore);
/*     */   }
/*     */   
/*     */   public void setLore(String[] lore) {
/* 209 */     this.lore.clear();
/* 210 */     String[] arrayOfString; int j = (arrayOfString = lore).length; for (int i = 0; i < j; i++) { String str = arrayOfString[i];
/* 211 */       this.lore.add(str);
/*     */     }
/*     */   }
/*     */   
/*     */   public LinkedList<String> getLore() {
/* 216 */     return this.lore;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/* 220 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 224 */     return this.name;
/*     */   }
/*     */   
/*     */   public static ItemStack addEnhancement(ItemStack stack, LoreEnhancement enhancement) {
/* 228 */     AttributeUtil attrs = new AttributeUtil(stack);
/* 229 */     attrs = enhancement.add(attrs);
/* 230 */     return attrs.getStack();
/*     */   }
/*     */   
/*     */   public static boolean hasEnhancement(ItemStack stack, String enhName) {
/* 234 */     AttributeUtil attrs = new AttributeUtil(stack);
/* 235 */     return attrs.hasEnhancement(enhName);
/*     */   }
/*     */   
/*     */   public static boolean hasEnhancements(ItemStack stack) {
/* 239 */     AttributeUtil attrs = new AttributeUtil(stack);
/* 240 */     return attrs.hasEnhancements();
/*     */   }
/*     */   
/*     */   public static LinkedList<LoreEnhancement> getEnhancements(ItemStack stack) {
/* 244 */     AttributeUtil attrs = new AttributeUtil(stack);
/* 245 */     return attrs.getEnhancements(); }
/*     */   
/*     */   public void applyAttributes(AttributeUtil attrs) {}
/*     */   
/*     */   public abstract void onHit(EntityDamageByEntityEvent paramEntityDamageByEntityEvent);
/*     */   
/*     */   public abstract void onInteract(PlayerInteractEvent paramPlayerInteractEvent);
/*     */   
/*     */   public abstract void onInteractEntity(PlayerInteractEntityEvent paramPlayerInteractEntityEvent);
/*     */   
/*     */   public abstract void onBlockPlaced(BlockPlaceEvent paramBlockPlaceEvent);
/*     */   
/*     */   public abstract void onBlockBreak(BlockBreakEvent paramBlockBreakEvent);
/*     */   
/*     */   public abstract void onBlockDamage(BlockDamageEvent paramBlockDamageEvent);
/*     */   
/*     */   public abstract void onBlockInteract(PlayerInteractEvent paramPlayerInteractEvent);
/*     */   
/*     */   public abstract void onHold(PlayerItemHeldEvent paramPlayerItemHeldEvent);
/*     */   
/*     */   public abstract void onDrop(PlayerDropItemEvent paramPlayerDropItemEvent);
/*     */   public abstract void onItemDrop(PlayerDropItemEvent paramPlayerDropItemEvent);
/*     */   public abstract void onItemCraft(CraftItemEvent paramCraftItemEvent);
/*     */   public abstract void onItemPickup(PlayerPickupItemEvent paramPlayerPickupItemEvent);
/*     */   public abstract void onItemSpawn(ItemSpawnEvent paramItemSpawnEvent);
/*     */   public abstract boolean onAttack(EntityDamageByEntityEvent paramEntityDamageByEntityEvent, ItemStack paramItemStack);
/*     */   public abstract void onInvItemPickup(InventoryClickEvent paramInventoryClickEvent, Inventory paramInventory, ItemStack paramItemStack);
/*     */   public abstract void onInvItemDrop(InventoryClickEvent paramInventoryClickEvent, Inventory paramInventory, ItemStack paramItemStack);
/*     */   public abstract void onInvShiftClick(InventoryClickEvent paramInventoryClickEvent, Inventory paramInventory1, Inventory paramInventory2, ItemStack paramItemStack);
/*     */   public abstract void onInvItemSwap(InventoryClickEvent paramInventoryClickEvent, Inventory paramInventory, ItemStack paramItemStack1, ItemStack paramItemStack2);
/*     */   public abstract void onPlayerDeath(EntityDeathEvent paramEntityDeathEvent, ItemStack paramItemStack);
/*     */   public abstract void onInventoryClose(InventoryCloseEvent paramInventoryCloseEvent);
/*     */   public void onDefense(EntityDamageByEntityEvent event, ItemStack stack) {}
/* 278 */   public int onStructureBlockBreak(BuildableDamageBlock dmgBlock, int damage) { return damage; }
/*     */   
/*     */   public void onInventoryOpen(InventoryOpenEvent event, ItemStack stack) {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\lorestorage\LoreMaterial.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
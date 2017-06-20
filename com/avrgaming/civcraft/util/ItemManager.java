/*     */ package com.avrgaming.civcraft.util;
/*     */ 
/*     */ import org.bukkit.ChunkSnapshot;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockState;
/*     */ import org.bukkit.enchantments.Enchantment;
/*     */ import org.bukkit.entity.EntityType;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ import org.bukkit.inventory.meta.SkullMeta;
/*     */ import org.bukkit.material.MaterialData;
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
/*     */ public class ItemManager
/*     */ {
/*     */   public static ItemStack createItemStack(int typeId, int amount, short damage)
/*     */   {
/*  31 */     return new ItemStack(typeId, amount, damage);
/*     */   }
/*     */   
/*     */   public static ItemStack createItemStack(int typeId, int amount) {
/*  35 */     return createItemStack(typeId, amount, (short)0);
/*     */   }
/*     */   
/*     */   public static MaterialData getMaterialData(int type_id, int data)
/*     */   {
/*  40 */     return new MaterialData(type_id, (byte)data);
/*     */   }
/*     */   
/*     */   public static Enchantment getEnchantById(int id)
/*     */   {
/*  45 */     return Enchantment.getById(id);
/*     */   }
/*     */   
/*     */   public static int getId(Material material)
/*     */   {
/*  50 */     return material.getId();
/*     */   }
/*     */   
/*     */   public static int getId(Enchantment e)
/*     */   {
/*  55 */     return e.getId();
/*     */   }
/*     */   
/*     */   public static int getId(ItemStack stack)
/*     */   {
/*  60 */     return stack.getTypeId();
/*     */   }
/*     */   
/*     */   public static int getId(Block block)
/*     */   {
/*  65 */     return block.getTypeId();
/*     */   }
/*     */   
/*     */   public static void setTypeId(Block block, int typeId)
/*     */   {
/*  70 */     block.setTypeId(typeId);
/*     */   }
/*     */   
/*     */   public static void setTypeId(BlockState block, int typeId)
/*     */   {
/*  75 */     block.setTypeId(typeId);
/*     */   }
/*     */   
/*     */   public static byte getData(Block block)
/*     */   {
/*  80 */     return block.getData();
/*     */   }
/*     */   
/*     */   public static short getData(ItemStack stack) {
/*  84 */     return stack.getDurability();
/*     */   }
/*     */   
/*     */   public static byte getData(MaterialData data)
/*     */   {
/*  89 */     return data.getData();
/*     */   }
/*     */   
/*     */   public static byte getData(BlockState state)
/*     */   {
/*  94 */     return state.getRawData();
/*     */   }
/*     */   
/*     */   public static void setData(Block block, int data)
/*     */   {
/*  99 */     block.setData((byte)data);
/*     */   }
/*     */   
/*     */   public static void setData(Block block, int data, boolean update)
/*     */   {
/* 104 */     block.setData((byte)data, update);
/*     */   }
/*     */   
/*     */   public static Material getMaterial(int material)
/*     */   {
/* 109 */     return Material.getMaterial(material);
/*     */   }
/*     */   
/*     */   public static int getBlockTypeId(ChunkSnapshot snapshot, int x, int y, int z)
/*     */   {
/* 114 */     return snapshot.getBlockTypeId(x, y, z);
/*     */   }
/*     */   
/*     */   public static int getBlockData(ChunkSnapshot snapshot, int x, int y, int z)
/*     */   {
/* 119 */     return snapshot.getBlockData(x, y, z);
/*     */   }
/*     */   
/*     */   public static void sendBlockChange(Player player, Location loc, int type, int data)
/*     */   {
/* 124 */     player.sendBlockChange(loc, type, (byte)data);
/*     */   }
/*     */   
/*     */   public static int getBlockTypeIdAt(World world, int x, int y, int z)
/*     */   {
/* 129 */     return world.getBlockTypeIdAt(x, y, z);
/*     */   }
/*     */   
/*     */   public static int getId(BlockState newState)
/*     */   {
/* 134 */     return newState.getTypeId();
/*     */   }
/*     */   
/*     */   public static short getId(EntityType entity)
/*     */   {
/* 139 */     return entity.getTypeId();
/*     */   }
/*     */   
/*     */   public static void setData(MaterialData data, byte chestData)
/*     */   {
/* 144 */     data.setData(chestData);
/*     */   }
/*     */   
/*     */   public static void setTypeIdAndData(Block block, int type, int data, boolean update)
/*     */   {
/* 149 */     block.setTypeIdAndData(type, (byte)data, update);
/*     */   }
/*     */   
/*     */   public static ItemStack spawnPlayerHead(String playerName, String itemDisplayName) {
/* 153 */     ItemStack skull = createItemStack(getId(Material.SKULL_ITEM), 1, (short)3);
/* 154 */     SkullMeta meta = (SkullMeta)skull.getItemMeta();
/* 155 */     meta.setOwner(playerName);
/* 156 */     meta.setDisplayName(itemDisplayName);
/* 157 */     skull.setItemMeta(meta);
/* 158 */     return skull;
/*     */   }
/*     */   
/*     */   public static boolean removeItemFromPlayer(Player player, Material mat, int amount)
/*     */   {
/* 163 */     ItemStack m = new ItemStack(mat, amount);
/* 164 */     if (player.getInventory().contains(mat)) {
/* 165 */       player.getInventory().removeItem(new ItemStack[] { m });
/* 166 */       player.updateInventory();
/* 167 */       return true;
/*     */     }
/* 169 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\ItemManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
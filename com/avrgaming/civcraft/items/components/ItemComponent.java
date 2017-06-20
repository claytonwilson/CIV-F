/*    */ package com.avrgaming.civcraft.items.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.lorestorage.ItemChangeResult;
/*    */ import com.avrgaming.civcraft.object.BuildableDamageBlock;
/*    */ import gpl.AttributeUtil;
/*    */ import java.util.HashMap;
/*    */ import java.util.concurrent.locks.ReentrantLock;
/*    */ import org.bukkit.event.block.BlockPlaceEvent;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ import org.bukkit.event.entity.ItemSpawnEvent;
/*    */ import org.bukkit.event.entity.PlayerDeathEvent;
/*    */ import org.bukkit.event.entity.PlayerLeashEntityEvent;
/*    */ import org.bukkit.event.inventory.InventoryOpenEvent;
/*    */ import org.bukkit.event.player.PlayerInteractEntityEvent;
/*    */ import org.bukkit.event.player.PlayerInteractEvent;
/*    */ import org.bukkit.event.player.PlayerItemDamageEvent;
/*    */ import org.bukkit.event.player.PlayerItemHeldEvent;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ItemComponent
/*    */ {
/* 44 */   public static ReentrantLock lock = new ReentrantLock();
/*    */   
/* 46 */   private HashMap<String, String> attributes = new HashMap();
/*    */   
/*    */   private String name;
/*    */   
/*    */ 
/*    */   public void createComponent() {}
/*    */   
/*    */ 
/*    */   public void destroyComponent() {}
/*    */   
/*    */   public String getName()
/*    */   {
/* 58 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 62 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getString(String key) {
/* 66 */     return (String)this.attributes.get(key);
/*    */   }
/*    */   
/*    */   public double getDouble(String key) {
/* 70 */     return Double.valueOf((String)this.attributes.get(key)).doubleValue();
/*    */   }
/*    */   
/*    */ 
/* 74 */   public void setAttribute(String key, String value) { this.attributes.put(key, value); }
/*    */   
/*    */   public abstract void onPrepareCreate(AttributeUtil paramAttributeUtil);
/*    */   
/*    */   public void onDurabilityChange(PlayerItemDamageEvent event) {}
/*    */   public void onDefense(EntityDamageByEntityEvent event, ItemStack stack) {}
/*    */   public void onInteract(PlayerInteractEvent event) {}
/* 81 */   public int onStructureBlockBreak(BuildableDamageBlock sb, int damage) { return damage; }
/*    */   public void onItemSpawn(ItemSpawnEvent event) {}
/*    */   public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {}
/*    */   public void onPlayerLeashEvent(PlayerLeashEntityEvent event) {}
/*    */   public void onRangedAttack(EntityDamageByEntityEvent event, ItemStack inHand) {}
/* 86 */   public ItemChangeResult onDurabilityDeath(PlayerDeathEvent event, ItemChangeResult result, ItemStack stack) { return result; }
/*    */   public void onAttack(EntityDamageByEntityEvent event, ItemStack inHand) {}
/* 88 */   public boolean onBlockPlaced(BlockPlaceEvent event) { return false; }
/*    */   
/*    */   public void onInventoryOpen(InventoryOpenEvent event, ItemStack stack) {}
/*    */   
/*    */   public void onHold(PlayerItemHeldEvent event) {}
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\ItemComponent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
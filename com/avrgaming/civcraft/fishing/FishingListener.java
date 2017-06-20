/*    */ package com.avrgaming.civcraft.fishing;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.ConfigFishing;
/*    */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.util.ItemManager;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Random;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.player.PlayerFishEvent;
/*    */ import org.bukkit.event.player.PlayerFishEvent.State;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.inventory.PlayerInventory;
/*    */ 
/*    */ public class FishingListener implements Listener
/*    */ {
/*    */   public ArrayList<ConfigFishing> getRandomDrops()
/*    */   {
/* 23 */     Random rand = new Random();
/* 24 */     ArrayList<ConfigFishing> dropped = new ArrayList();
/*    */     
/* 26 */     for (ConfigFishing d : com.avrgaming.civcraft.config.CivSettings.fishingDrops) {
/* 27 */       int chance = rand.nextInt(10000);
/* 28 */       if (chance < d.drop_chance * 10000.0D) {
/* 29 */         dropped.add(d);
/*    */       }
/*    */     }
/*    */     
/* 33 */     return dropped;
/*    */   }
/*    */   
/*    */   @EventHandler(priority=EventPriority.MONITOR)
/*    */   public void onPlayerFish(PlayerFishEvent event)
/*    */   {
/* 39 */     if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
/* 40 */       event.setCancelled(true);
/* 41 */       Player player = event.getPlayer();
/* 42 */       ItemStack stack = null;
/*    */       
/* 44 */       ArrayList<ConfigFishing> dropped = getRandomDrops();
/*    */       
/* 46 */       for (ConfigFishing d : dropped) {
/* 47 */         if (d.craftMatId == null) {
/* 48 */           stack = ItemManager.createItemStack(d.type_id, 1);
/* 49 */           CivMessage.send(event.getPlayer(), "§aYou've fished up §d" + stack.getType().name().replace("_", " ").toLowerCase());
/*    */         } else {
/* 51 */           LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterialFromId(d.craftMatId);
/* 52 */           stack = LoreCraftableMaterial.spawn(craftMat);
/* 53 */           CivMessage.send(event.getPlayer(), "§aYou've fished up §d" + craftMat.getName());
/*    */         }
/*    */       }
/* 56 */       if (stack != null) {
/* 57 */         player.getInventory().addItem(new ItemStack[] { stack });
/* 58 */         player.updateInventory();
/*    */       } else {
/* 60 */         stack = ItemManager.createItemStack(349, 1);
/* 61 */         CivMessage.send(event.getPlayer(), "§aYou've fished up §d" + stack.getType().name().replace("_", " ").toLowerCase());
/* 62 */         player.getInventory().addItem(new ItemStack[] { stack });
/* 63 */         player.updateInventory();
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\fishing\FishingListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
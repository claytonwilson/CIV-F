/*    */ package com.avrgaming.civcraft.items;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.listener.CustomItemManager;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import java.util.HashMap;
/*    */ import java.util.LinkedList;
/*    */ import org.bukkit.entity.Player;
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
/*    */ public class ItemDuraSyncTask
/*    */   implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/* 35 */     for (String playerName : CustomItemManager.itemDuraMap.keySet())
/*    */     {
/*    */       try {
/* 38 */         player = CivGlobal.getPlayer(playerName);
/*    */       } catch (CivException e) { Player player;
/* 40 */         continue;
/*    */       }
/*    */       Player player;
/* 43 */       LinkedList<ItemDurabilityEntry> entries = (LinkedList)CustomItemManager.itemDuraMap.get(playerName);
/*    */       
/* 45 */       for (ItemDurabilityEntry entry : entries) {
/* 46 */         entry.stack.setDurability(entry.oldValue);
/*    */       }
/*    */       
/* 49 */       player.updateInventory();
/*    */     }
/*    */     
/* 52 */     CustomItemManager.duraTaskScheduled = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\ItemDuraSyncTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
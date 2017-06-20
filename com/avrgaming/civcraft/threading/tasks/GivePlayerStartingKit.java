/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.CivSettings;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import com.avrgaming.civcraft.object.EconObject;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.util.ItemManager;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.inventory.PlayerInventory;
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
/*    */ public class GivePlayerStartingKit
/*    */   implements Runnable
/*    */ {
/*    */   public String name;
/*    */   
/*    */   public GivePlayerStartingKit(String name)
/*    */   {
/* 37 */     this.name = name;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/*    */     try {
/* 43 */       Player player = CivGlobal.getPlayer(this.name);
/*    */       
/* 45 */       for (String kitItems : CivSettings.kitItems) {
/* 46 */         String[] split = kitItems.split(":");
/*    */         ItemStack stack;
/*    */         try
/*    */         {
/* 50 */           Integer type = Integer.valueOf(split[0]);
/* 51 */           Integer amount = Integer.valueOf(split[1]);
/*    */           
/* 53 */           stack = ItemManager.createItemStack(type.intValue(), amount.intValue());
/*    */         } catch (NumberFormatException e) {
/*    */           ItemStack stack;
/* 56 */           String customMatID = split[0];
/* 57 */           LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterialFromId(customMatID);
/* 58 */           if (craftMat == null) {
/* 59 */             CivLog.warning("Couldn't find custom material:" + customMatID + " to give to player on first join.");
/* 60 */             continue;
/*    */           }
/*    */           
/* 63 */           stack = LoreCraftableMaterial.spawn(craftMat);
/*    */         }
/*    */         
/* 66 */         player.getInventory().addItem(new ItemStack[] { stack });
/*    */       }
/*    */       
/* 69 */       Resident resident = CivGlobal.getResident(this.name);
/* 70 */       if (resident != null) {
/* 71 */         resident.getTreasury().deposit(CivSettings.startingCoins);
/* 72 */         resident.setGivenKit(true);
/*    */       }
/*    */       
/*    */     }
/*    */     catch (CivException e)
/*    */     {
/* 78 */       CivLog.warning("Tried to give starting kit to offline player:" + this.name);
/* 79 */       return;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\GivePlayerStartingKit.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
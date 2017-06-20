/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.World;
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
/*    */ public class DelayItemDrop
/*    */   implements Runnable
/*    */ {
/*    */   Location loc;
/*    */   ItemStack stack;
/*    */   
/*    */   public DelayItemDrop(ItemStack stack, Location loc)
/*    */   {
/* 30 */     this.loc = loc;
/* 31 */     this.stack = stack;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/* 36 */     this.loc.getWorld().dropItem(this.loc, this.stack);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\DelayItemDrop.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
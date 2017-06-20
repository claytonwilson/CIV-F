/*    */ package com.avrgaming.civcraft.randomevents.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivCraft;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.randomevents.RandomEvent;
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.HandlerList;
/*    */ import org.bukkit.event.block.BlockBreakEvent;
/*    */ import org.bukkit.plugin.PluginManager;
/*    */ 
/*    */ public class BlockBreak extends com.avrgaming.civcraft.randomevents.RandomEventComponent implements org.bukkit.event.Listener
/*    */ {
/*    */   BlockCoord bcoord;
/* 21 */   boolean blockBroken = false;
/* 22 */   boolean brokenByTown = false;
/*    */   
/*    */ 
/*    */   public void onStart()
/*    */   {
/* 27 */     PluginManager pluginManager = CivCraft.getPlugin().getServer().getPluginManager();
/* 28 */     pluginManager.registerEvents(this, CivCraft.getPlugin());
/*    */   }
/*    */   
/*    */ 
/*    */   public void onCleanup()
/*    */   {
/* 34 */     HandlerList.unregisterAll(this);
/*    */   }
/*    */   
/*    */   @EventHandler(priority=EventPriority.MONITOR)
/*    */   public void onBlockBreak(BlockBreakEvent event) {
/* 39 */     if (this.blockBroken) {
/* 40 */       return;
/*    */     }
/*    */     
/* 43 */     String varname = getString("varname");
/* 44 */     String locString = (String)getParent().componentVars.get(varname);
/* 45 */     if (locString == null) {
/* 46 */       return;
/*    */     }
/*    */     
/* 49 */     this.bcoord = new BlockCoord((String)getParent().componentVars.get(varname));
/*    */     
/* 51 */     BlockCoord breakCoord = new BlockCoord(event.getBlock());
/*    */     
/* 53 */     if ((breakCoord.getX() == this.bcoord.getX()) && 
/* 54 */       (breakCoord.getY() == this.bcoord.getY()) && 
/* 55 */       (breakCoord.getZ() == this.bcoord.getZ()))
/*    */     {
/* 57 */       Resident resident = CivGlobal.getResident(event.getPlayer().getName());
/* 58 */       if (resident.getTown() == getParentTown()) {
/* 59 */         this.brokenByTown = true;
/* 60 */         CivMessage.send(event.getPlayer(), "§aYou seem to have found something interesting....");
/*    */       }
/*    */       
/* 63 */       this.blockBroken = true;
/*    */       
/* 65 */       getParent().componentVars.put(getString("playername_var"), event.getPlayer().getName());
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void process() {}
/*    */   
/*    */ 
/*    */   public boolean onCheck()
/*    */   {
/* 75 */     return (this.blockBroken) && (this.brokenByTown);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\randomevents\components\BlockBreak.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
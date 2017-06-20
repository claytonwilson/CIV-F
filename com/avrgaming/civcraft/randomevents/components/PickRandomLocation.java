/*    */ package com.avrgaming.civcraft.randomevents.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.CivSettings;
/*    */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*    */ import com.avrgaming.civcraft.randomevents.RandomEvent;
/*    */ import com.avrgaming.civcraft.randomevents.RandomEventComponent;
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
/*    */ import java.util.HashMap;
/*    */ import java.util.Random;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.World;
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
/*    */ public class PickRandomLocation
/*    */   extends RandomEventComponent
/*    */ {
/*    */   public void process()
/*    */   {
/* 57 */     TaskMaster.syncTask(new Runnable()
/*    */     {
/*    */       RandomEvent event;
/*    */       
/*    */       public void run()
/*    */       {
/*    */         try
/*    */         {
/* 30 */           int max_x = CivSettings.getInteger(CivSettings.randomEventsConfig, "max_x").intValue();
/* 31 */           int max_z = CivSettings.getInteger(CivSettings.randomEventsConfig, "max_z").intValue();
/* 32 */           int min_x = CivSettings.getInteger(CivSettings.randomEventsConfig, "min_x").intValue();
/* 33 */           int min_z = CivSettings.getInteger(CivSettings.randomEventsConfig, "min_z").intValue();
/*    */           
/* 35 */           int range_x = max_x - min_x;
/* 36 */           int range_z = max_z - min_z;
/*    */           
/* 38 */           Random rand = new Random();
/* 39 */           int randX = rand.nextInt(range_x) - max_x;
/* 40 */           int randZ = rand.nextInt(range_z) - max_z;
/*    */           
/*    */ 
/* 43 */           World world = Bukkit.getWorld("world");
/* 44 */           int y = world.getHighestBlockYAt(randX, randZ);
/*    */           
/* 46 */           BlockCoord bcoord = new BlockCoord(world.getName(), randX, y, randZ);
/*    */           
/* 48 */           String varname = PickRandomLocation.this.getString("varname");
/* 49 */           this.event.componentVars.put(varname, bcoord.toString());
/*    */           
/* 51 */           PickRandomLocation.this.sendMessage("Head to " + bcoord.getX() + "," + bcoord.getY() + "," + bcoord.getZ() + "!");
/*    */         } catch (InvalidConfiguration e) {
/* 53 */           e.printStackTrace();
/*    */         }
/*    */       }
/*    */     });
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\randomevents\components\PickRandomLocation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
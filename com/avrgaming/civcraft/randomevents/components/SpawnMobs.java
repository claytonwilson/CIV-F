/*    */ package com.avrgaming.civcraft.randomevents.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.object.TownChunk;
/*    */ import com.avrgaming.civcraft.randomevents.RandomEventComponent;
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import com.avrgaming.civcraft.util.ChunkCoord;
/*    */ import java.util.Collection;
/*    */ import java.util.Random;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.entity.EntityType;
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
/*    */ public class SpawnMobs
/*    */   extends RandomEventComponent
/*    */ {
/*    */   public void process()
/*    */   {
/* 53 */     TaskMaster.syncTask(new Runnable()
/*    */     {
/*    */       public void run()
/*    */       {
/* 23 */         EntityType type = EntityType.valueOf(SpawnMobs.this.getString("what"));
/*    */         
/*    */ 
/* 26 */         int amount = Integer.valueOf(SpawnMobs.this.getString("amount")).intValue();
/*    */         
/*    */ 
/* 29 */         Random rand = new Random();
/* 30 */         int index = rand.nextInt(SpawnMobs.this.getParentTown().getTownChunks().size());
/*    */         
/* 32 */         TownChunk tc = (TownChunk)SpawnMobs.this.getParentTown().getTownChunks().toArray()[index];
/* 33 */         World world = Bukkit.getServer().getWorld(tc.getChunkCoord().getWorldname());
/*    */         
/* 35 */         for (int i = 0; i < amount; i++) {
/* 36 */           int x = rand.nextInt(16);
/* 37 */           int z = rand.nextInt(16);
/*    */           
/* 39 */           x += tc.getChunkCoord().getX() * 16;
/* 40 */           z += tc.getChunkCoord().getZ() * 16;
/*    */           
/* 42 */           int y = world.getHighestBlockAt(x, z).getY();
/* 43 */           Location loc = new Location(world, x, y, z);
/*    */           
/* 45 */           Bukkit.getServer().getWorld(tc.getChunkCoord().getWorldname()).spawnEntity(loc, type);
/*    */         }
/*    */         
/* 48 */         SpawnMobs.this.sendMessage(amount + " " + type.toString() + " have spawned in the vincitiy of " + 
/* 49 */           tc.getChunkCoord().getX() * 16 + ",64," + tc.getChunkCoord().getZ() * 16);
/*    */       }
/*    */     });
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean requiresActivation()
/*    */   {
/* 57 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\randomevents\components\SpawnMobs.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
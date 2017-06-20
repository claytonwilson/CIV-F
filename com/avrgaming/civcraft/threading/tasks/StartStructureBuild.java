/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.structure.Structure;
/*    */ import com.avrgaming.civcraft.template.Template;
/*    */ import java.io.IOException;
/*    */ import java.sql.SQLException;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.entity.Player;
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
/*    */ public class StartStructureBuild
/*    */   implements Runnable
/*    */ {
/*    */   public String playerName;
/*    */   public Structure struct;
/*    */   public Template tpl;
/*    */   public Location centerLoc;
/*    */   
/*    */   public void run()
/*    */   {
/*    */     try
/*    */     {
/* 44 */       player = CivGlobal.getPlayer(this.playerName);
/*    */     } catch (CivException e1) { Player player;
/* 46 */       e1.printStackTrace();
/* 47 */       return;
/*    */     }
/*    */     try
/*    */     {
/* 51 */       this.struct.doBuild(player, this.centerLoc, this.tpl);
/* 52 */       this.struct.save();
/*    */     } catch (CivException e) {
/* 54 */       CivMessage.sendError(player, "Unable to build: " + e.getMessage());
/*    */     } catch (IOException e) {
/* 56 */       CivMessage.sendError(player, "Internal IO error.");
/* 57 */       e.printStackTrace();
/*    */     } catch (SQLException e) { Player player;
/* 59 */       CivMessage.sendError(player, "Internal SQL error.");
/* 60 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\StartStructureBuild.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
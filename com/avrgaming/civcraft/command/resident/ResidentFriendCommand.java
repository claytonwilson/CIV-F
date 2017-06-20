/*    */ package com.avrgaming.civcraft.command.resident;
/*    */ 
/*    */ import com.avrgaming.civcraft.command.CommandBase;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import java.util.HashMap;
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
/*    */ public class ResidentFriendCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public void init()
/*    */   {
/* 30 */     this.command = "/resident friend";
/* 31 */     this.displayName = "Resident Friend";
/*    */     
/* 33 */     this.commands.put("add", "[name] - adds this resident to your friends list.");
/* 34 */     this.commands.put("remove", "[name] - removes this resident from your friends list.");
/* 35 */     this.commands.put("list", "shows a list of all your current friends.");
/*    */   }
/*    */   
/*    */   public void add_cmd() throws CivException {
/* 39 */     Resident resident = getResident();
/*    */     
/* 41 */     if (this.args.length < 2) {
/* 42 */       throw new CivException("Please specify the name of the friend you want to add.");
/*    */     }
/*    */     
/* 45 */     Resident friendToAdd = getNamedResident(1);
/*    */     
/* 47 */     resident.addFriend(friendToAdd);
/* 48 */     CivMessage.sendSuccess(this.sender, "Added " + this.args[1] + " as a friend.");
/* 49 */     resident.save();
/*    */   }
/*    */   
/*    */   public void remove_cmd() throws CivException {
/* 53 */     Resident resident = getResident();
/*    */     
/* 55 */     if (this.args.length < 2) {
/* 56 */       throw new CivException("Please specify the name of the friend you want to removed.");
/*    */     }
/*    */     
/* 59 */     Resident friendToRemove = getNamedResident(1);
/*    */     
/* 61 */     resident.removeFriend(friendToRemove);
/* 62 */     CivMessage.sendSuccess(this.sender, "Removed " + this.args[1] + " as a friend.");
/* 63 */     resident.save();
/*    */   }
/*    */   
/*    */   public void list_cmd() throws CivException {
/* 67 */     Resident resident = getResident();
/* 68 */     CivMessage.sendHeading(this.sender, resident.getName() + " friend list");
/*    */     
/* 70 */     String out = "";
/* 71 */     for (String res : resident.getFriends()) {
/* 72 */       out = out + res + ", ";
/*    */     }
/* 74 */     CivMessage.send(this.sender, out);
/*    */   }
/*    */   
/*    */   public void doDefaultAction() throws CivException
/*    */   {
/* 79 */     showHelp();
/*    */   }
/*    */   
/*    */   public void showHelp()
/*    */   {
/* 84 */     showBasicHelp();
/*    */   }
/*    */   
/*    */   public void permissionCheck()
/*    */     throws CivException
/*    */   {}
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\resident\ResidentFriendCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
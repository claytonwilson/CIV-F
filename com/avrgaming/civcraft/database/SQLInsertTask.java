/*    */ package com.avrgaming.civcraft.database;
/*    */ 
/*    */ import java.sql.SQLException;
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
/*    */ 
/*    */ 
/*    */ public class SQLInsertTask
/*    */   implements Runnable
/*    */ {
/*    */   HashMap<String, Object> hashmap;
/*    */   String tablename;
/*    */   
/*    */   public SQLInsertTask(HashMap<String, Object> hashmap, String tablename) {}
/*    */   
/*    */   public void run()
/*    */   {
/*    */     try
/*    */     {
/* 36 */       SQL.insertNow(this.hashmap, this.tablename);
/*    */     } catch (SQLException e) {
/* 38 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\database\SQLInsertTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package com.avrgaming.civcraft.database;
/*    */ 
/*    */ import com.avrgaming.civcraft.object.NamedObject;
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
/*    */ public class SQLUpdateNamedObjectTask
/*    */   implements Runnable
/*    */ {
/*    */   NamedObject obj;
/*    */   HashMap<String, Object> hashmap;
/*    */   String tablename;
/*    */   
/*    */   public SQLUpdateNamedObjectTask(NamedObject obj, HashMap<String, Object> hashmap, String tablename)
/*    */   {
/* 33 */     this.obj = obj;
/* 34 */     this.hashmap = hashmap;
/* 35 */     this.tablename = tablename;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/*    */     try {
/* 41 */       if (this.obj.getId() == 0) {
/* 42 */         this.obj.setId(SQL.insertNow(this.hashmap, this.tablename));
/*    */       } else {
/* 44 */         SQL.update(this.obj.getId(), this.hashmap, this.tablename);
/*    */       }
/*    */     } catch (SQLException e) {
/* 47 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\database\SQLUpdateNamedObjectTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
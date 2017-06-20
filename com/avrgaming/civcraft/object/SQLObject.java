/*    */ package com.avrgaming.civcraft.object;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*    */ import com.avrgaming.civcraft.exception.InvalidObjectException;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
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
/*    */ public abstract class SQLObject
/*    */   extends NamedObject
/*    */ {
/* 34 */   private boolean isDeleted = false;
/*    */   
/*    */   public abstract void load(ResultSet paramResultSet) throws SQLException, InvalidNameException, InvalidObjectException, CivException;
/*    */   
/*    */   public abstract void save();
/*    */   
/*    */   public abstract void saveNow() throws SQLException;
/*    */   
/*    */   public abstract void delete() throws SQLException;
/*    */   
/*    */   public boolean isDeleted() {
/* 45 */     return this.isDeleted;
/*    */   }
/*    */   
/*    */   public void setDeleted(boolean isDeleted) {
/* 49 */     this.isDeleted = isDeleted;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\SQLObject.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
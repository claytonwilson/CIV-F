/*    */ package com.avrgaming.civcraft.database;
/*    */ 
/*    */ import com.jolbox.bonecp.BoneCP;
/*    */ import com.jolbox.bonecp.BoneCPConfig;
/*    */ import com.jolbox.bonecp.Statistics;
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConnectionPool
/*    */ {
/*    */   BoneCP pool;
/*    */   
/*    */   public static void loadClass(String name) {}
/*    */   
/*    */   public static void init()
/*    */     throws ClassNotFoundException
/*    */   {
/* 23 */     Class.forName("com.mysql.jdbc.Driver");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ConnectionPool(String dbcUrl, String user, String pass, int minConns, int maxConns, int partCount)
/*    */     throws ClassNotFoundException, SQLException
/*    */   {
/* 36 */     BoneCPConfig config = new BoneCPConfig();
/* 37 */     config.setJdbcUrl(dbcUrl);
/* 38 */     config.setUsername(user);
/* 39 */     config.setPassword(pass);
/* 40 */     config.setMinConnectionsPerPartition(minConns);
/* 41 */     config.setMaxConnectionsPerPartition(maxConns);
/* 42 */     config.setPartitionCount(partCount);
/*    */     
/*    */ 
/*    */ 
/* 46 */     this.pool = new BoneCP(config);
/*    */   }
/*    */   
/*    */   public Connection getConnection() throws SQLException {
/* 50 */     return this.pool.getConnection();
/*    */   }
/*    */   
/*    */   public Statistics getStats()
/*    */   {
/* 55 */     return this.pool.getStatistics();
/*    */   }
/*    */   
/*    */   public void shutdown() {
/* 59 */     this.pool.shutdown();
/*    */   }
/*    */   
/*    */   public void setMaxConnections(int max) {
/* 63 */     this.pool.getConfig().setMaxConnectionsPerPartition(max);
/*    */   }
/*    */   
/*    */   public int getMaxConnections() {
/* 67 */     return this.pool.getConfig().getMaxConnectionsPerPartition();
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\database\ConnectionPool.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
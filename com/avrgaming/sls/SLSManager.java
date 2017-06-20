/*    */ package com.avrgaming.sls;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.CivSettings;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import com.avrgaming.civcraft.util.TimeTools;
/*    */ import java.io.IOException;
/*    */ import java.net.DatagramPacket;
/*    */ import java.net.DatagramSocket;
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
/*    */ import java.util.UUID;
/*    */ import org.bukkit.Bukkit;
/*    */ 
/*    */ public class SLSManager
/*    */   implements Runnable
/*    */ {
/*    */   public static String serverName;
/*    */   public static String serverDescription;
/*    */   public static String serverAddress;
/*    */   public static String serverTimezone;
/*    */   public static String gen_id;
/*    */   
/*    */   public static void init() throws CivException, InvalidConfiguration
/*    */   {
/* 28 */     String useListing = CivSettings.getStringBase("use_server_listing_service");
/*    */     
/* 30 */     if (useListing == null) {
/* 31 */       return;
/*    */     }
/*    */     
/* 34 */     if (!useListing.equalsIgnoreCase("true")) {
/* 35 */       return;
/*    */     }
/*    */     
/* 38 */     serverName = CivSettings.getStringBase("server_name");
/* 39 */     if (serverName.contains(";")) {
/* 40 */       throw new CivException("Cannot have a server name with a ';' in it.");
/*    */     }
/*    */     
/* 43 */     serverDescription = CivSettings.getStringBase("server_description");
/* 44 */     if (serverDescription.contains(";")) {
/* 45 */       throw new CivException("Cannot have a server description with a ';' in it.");
/*    */     }
/*    */     
/* 48 */     serverAddress = CivSettings.getStringBase("server_address");
/* 49 */     if (serverAddress.contains(";")) {
/* 50 */       throw new CivException("Cannot have a server address with a ';' in it.");
/*    */     }
/*    */     
/* 53 */     serverTimezone = CivSettings.getStringBase("server_timezone");
/* 54 */     if (serverTimezone.contains(";")) {
/* 55 */       throw new CivException("Cannot have a server timezone with a ';' in it.");
/*    */     }
/*    */     
/*    */ 
/* 59 */     gen_id = CivSettings.getGenID();
/* 60 */     if (gen_id == null) {
/* 61 */       UUID uid = UUID.randomUUID();
/* 62 */       gen_id = uid.toString();
/* 63 */       CivSettings.saveGenID(gen_id);
/*    */     }
/*    */     
/*    */ 
/* 67 */     TaskMaster.asyncTimer("SLS", new SLSManager(), TimeTools.toTicks(60L));
/*    */   }
/*    */   
/*    */   public static String getParsedVersion() {
/* 71 */     String version = Bukkit.getVersion();
/* 72 */     version = version.split("MC: ")[1].split("\\)")[0];
/* 73 */     return version;
/*    */   }
/*    */   
/*    */   public static void sendHeartbeat() {
/*    */     try {
/* 78 */       InetAddress address = InetAddress.getByName("atlas.civcraft.net");
/* 79 */       String message = gen_id + ";" + serverName + ";" + serverDescription + ";" + serverTimezone + ";" + serverAddress + ";" + 
/* 80 */         Bukkit.getOnlinePlayers().length + ";" + Bukkit.getMaxPlayers() + ";" + getParsedVersion();
/*    */       try
/*    */       {
/* 83 */         if (CivSettings.getStringBase("debug_heartbeat").equalsIgnoreCase("true")) {
/* 84 */           CivLog.info("SLS HEARTBEAT:" + message);
/*    */         }
/*    */       }
/*    */       catch (InvalidConfiguration localInvalidConfiguration) {}
/*    */       
/* 89 */       DatagramPacket packet = new DatagramPacket(message.getBytes(), message.toCharArray().length, address, 25580);
/*    */       try
/*    */       {
/* 92 */         DatagramSocket socket = new DatagramSocket();
/* 93 */         socket.send(packet);
/*    */       } catch (IOException e) {
/* 95 */         e.printStackTrace();
/*    */       }
/*    */       
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* :2 */       return;
/*    */     }
/*    */     catch (UnknownHostException e)
/*    */     {
/* 99 */       CivLog.error("Couldn't IP address to SLS service. If you're on a LAN with no internet access, disable SLS in the CivCraft config.");
/*    */     }
/*    */   }
/*    */   
/*    */   public void run() {}
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\sls\SLSManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
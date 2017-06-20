/*    */ package com.avrgaming.civcraft.randomevents;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConfigRandomEvent
/*    */ {
/*    */   public String id;
/*    */   public String name;
/* 17 */   public LinkedList<String> message = new LinkedList();
/*    */   
/*    */   public int length;
/*    */   
/* 21 */   public LinkedList<HashMap<String, String>> actions = new LinkedList();
/* 22 */   public LinkedList<HashMap<String, String>> requirements = new LinkedList();
/* 23 */   public LinkedList<HashMap<String, String>> success = new LinkedList();
/* 24 */   public LinkedList<HashMap<String, String>> failure = new LinkedList();
/* 25 */   public int chance = 0;
/*    */   
/*    */ 
/*    */   private static void loadComponentConfig(Map<?, ?> obj, LinkedList<HashMap<String, String>> component, String configName)
/*    */   {
/* 30 */     List<Map<?, ?>> comps = (List)obj.get(configName);
/* 31 */     if (comps != null) {
/* 32 */       for (Map<?, ?> compObj : comps)
/*    */       {
/* 34 */         HashMap<String, String> compMap = new HashMap();
/* 35 */         for (Object key : compObj.keySet()) {
/* 36 */           compMap.put((String)key, (String)compObj.get(key));
/*    */         }
/*    */         
/* 39 */         component.add(compMap);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public static void loadConfig(FileConfiguration cfg, Map<String, ConfigRandomEvent> randomEvents, ArrayList<String> eventIDs) {
/* 45 */     randomEvents.clear();
/* 46 */     List<Map<?, ?>> ConfigRandomEvent = cfg.getMapList("random_events");
/* 47 */     for (Map<?, ?> obj : ConfigRandomEvent)
/*    */     {
/* 49 */       ConfigRandomEvent event = new ConfigRandomEvent();
/* 50 */       event.id = ((String)obj.get("id"));
/* 51 */       event.name = ((String)obj.get("name"));
/* 52 */       event.length = ((Integer)obj.get("length")).intValue();
/* 53 */       event.chance = ((Integer)obj.get("chance")).intValue();
/*    */       
/* 55 */       List<?> messageList = (List)obj.get("message");
/* 56 */       for (Object str : messageList) {
/* 57 */         if ((str instanceof String)) {
/* 58 */           event.message.add((String)str);
/*    */         }
/*    */       }
/*    */       
/*    */ 
/* 63 */       loadComponentConfig(obj, event.actions, "actions");
/* 64 */       loadComponentConfig(obj, event.requirements, "requirements");
/* 65 */       loadComponentConfig(obj, event.success, "success");
/* 66 */       loadComponentConfig(obj, event.failure, "failure");
/*    */       
/*    */ 
/* 69 */       randomEvents.put(event.id, event);
/* 70 */       eventIDs.add(event.id);
/*    */     }
/*    */     
/* 73 */     CivLog.info("Loaded " + randomEvents.size() + " Random Events.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\randomevents\ConfigRandomEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package com.avrgaming.global.perks;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.CivSettings;
/*    */ import com.avrgaming.civcraft.config.ConfigPerk;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.global.perks.components.PerkComponent;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class Perk
/*    */ {
/* 13 */   public static HashMap<String, Perk> staticPerks = new HashMap();
/*    */   
/*    */   private String ident;
/* 16 */   private HashMap<String, PerkComponent> components = new HashMap();
/*    */   public ConfigPerk configPerk;
/* 18 */   public int count = 0;
/*    */   public String provider;
/*    */   
/*    */   public Perk(ConfigPerk config) {
/* 22 */     this.configPerk = config;
/* 23 */     this.ident = config.id;
/* 24 */     this.count = 1;
/* 25 */     buildComponents();
/*    */   }
/*    */   
/*    */   public static void init() {
/* 29 */     for (ConfigPerk configPerk : CivSettings.perks.values()) {
/* 30 */       Perk p = new Perk(configPerk);
/* 31 */       staticPerks.put(p.getIdent(), p);
/*    */     }
/*    */   }
/*    */   
/*    */   public String getIdent() {
/* 36 */     return this.ident;
/*    */   }
/*    */   
/* 39 */   public void setIdent(String ident) { this.ident = ident; }
/*    */   
/*    */   private void buildComponents()
/*    */   {
/* 43 */     List<HashMap<String, String>> compInfoList = this.configPerk.components;
/* 44 */     if (compInfoList != null) {
/* 45 */       for (HashMap<String, String> compInfo : compInfoList) {
/* 46 */         String className = "com.avrgaming.global.perks.components." + (String)compInfo.get("name");
/*    */         
/*    */         try
/*    */         {
/* 50 */           Class<?> someClass = Class.forName(className);
/*    */           
/* 52 */           PerkComponent perkCompClass = (PerkComponent)someClass.newInstance();
/* 53 */           perkCompClass.setName((String)compInfo.get("name"));
/* 54 */           perkCompClass.setParent(this);
/*    */           
/* 56 */           for (String key : compInfo.keySet()) {
/* 57 */             perkCompClass.setAttribute(key, (String)compInfo.get(key));
/*    */           }
/*    */           
/* 60 */           perkCompClass.createComponent();
/* 61 */           this.components.put(perkCompClass.getName(), perkCompClass);
/*    */         } catch (InstantiationException e) {
/* 63 */           e.printStackTrace();
/*    */         } catch (IllegalAccessException e) {
/* 65 */           e.printStackTrace();
/*    */         }
/*    */         catch (ClassNotFoundException e) {
/* 68 */           e.printStackTrace();
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void onActivate(Resident resident) {
/* 75 */     for (PerkComponent perk : this.components.values()) {
/* 76 */       perk.onActivate(resident);
/*    */     }
/*    */   }
/*    */   
/*    */   public String getDisplayName() {
/* 81 */     return this.configPerk.display_name;
/*    */   }
/*    */   
/*    */   public PerkComponent getComponent(String key) {
/* 85 */     return (PerkComponent)this.components.get(key);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\global\perks\Perk.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
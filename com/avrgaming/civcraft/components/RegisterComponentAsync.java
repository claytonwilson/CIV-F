/*    */ package com.avrgaming.civcraft.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.structure.Buildable;
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
/*    */ public class RegisterComponentAsync
/*    */   implements Runnable
/*    */ {
/*    */   public Buildable buildable;
/*    */   public Component component;
/*    */   public String name;
/*    */   boolean register;
/*    */   
/*    */   public RegisterComponentAsync(Buildable buildable, Component component, String name, boolean register)
/*    */   {
/* 33 */     this.buildable = buildable;
/* 34 */     this.component = component;
/* 35 */     this.name = name;
/* 36 */     this.register = register;
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public void run()
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield 27	com/avrgaming/civcraft/components/RegisterComponentAsync:register	Z
/*    */     //   4: ifeq +99 -> 103
/*    */     //   7: getstatic 34	com/avrgaming/civcraft/components/Component:componentsLock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   10: invokevirtual 40	java/util/concurrent/locks/ReentrantLock:lock	()V
/*    */     //   13: getstatic 45	com/avrgaming/civcraft/components/Component:componentsByType	Ljava/util/concurrent/ConcurrentHashMap;
/*    */     //   16: aload_0
/*    */     //   17: getfield 25	com/avrgaming/civcraft/components/RegisterComponentAsync:name	Ljava/lang/String;
/*    */     //   20: invokevirtual 49	java/util/concurrent/ConcurrentHashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*    */     //   23: checkcast 55	java/util/ArrayList
/*    */     //   26: astore_1
/*    */     //   27: aload_1
/*    */     //   28: ifnonnull +11 -> 39
/*    */     //   31: new 55	java/util/ArrayList
/*    */     //   34: dup
/*    */     //   35: invokespecial 57	java/util/ArrayList:<init>	()V
/*    */     //   38: astore_1
/*    */     //   39: aload_1
/*    */     //   40: aload_0
/*    */     //   41: getfield 23	com/avrgaming/civcraft/components/RegisterComponentAsync:component	Lcom/avrgaming/civcraft/components/Component;
/*    */     //   44: invokevirtual 58	java/util/ArrayList:add	(Ljava/lang/Object;)Z
/*    */     //   47: pop
/*    */     //   48: getstatic 45	com/avrgaming/civcraft/components/Component:componentsByType	Ljava/util/concurrent/ConcurrentHashMap;
/*    */     //   51: aload_0
/*    */     //   52: getfield 25	com/avrgaming/civcraft/components/RegisterComponentAsync:name	Ljava/lang/String;
/*    */     //   55: aload_1
/*    */     //   56: invokevirtual 62	java/util/concurrent/ConcurrentHashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*    */     //   59: pop
/*    */     //   60: aload_0
/*    */     //   61: getfield 21	com/avrgaming/civcraft/components/RegisterComponentAsync:buildable	Lcom/avrgaming/civcraft/structure/Buildable;
/*    */     //   64: ifnull +30 -> 94
/*    */     //   67: aload_0
/*    */     //   68: getfield 21	com/avrgaming/civcraft/components/RegisterComponentAsync:buildable	Lcom/avrgaming/civcraft/structure/Buildable;
/*    */     //   71: getfield 66	com/avrgaming/civcraft/structure/Buildable:attachedComponents	Ljava/util/ArrayList;
/*    */     //   74: aload_0
/*    */     //   75: getfield 23	com/avrgaming/civcraft/components/RegisterComponentAsync:component	Lcom/avrgaming/civcraft/components/Component;
/*    */     //   78: invokevirtual 58	java/util/ArrayList:add	(Ljava/lang/Object;)Z
/*    */     //   81: pop
/*    */     //   82: goto +12 -> 94
/*    */     //   85: astore_2
/*    */     //   86: getstatic 34	com/avrgaming/civcraft/components/Component:componentsLock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   89: invokevirtual 72	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*    */     //   92: aload_2
/*    */     //   93: athrow
/*    */     //   94: getstatic 34	com/avrgaming/civcraft/components/Component:componentsLock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   97: invokevirtual 72	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*    */     //   100: goto +95 -> 195
/*    */     //   103: getstatic 34	com/avrgaming/civcraft/components/Component:componentsLock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   106: invokevirtual 40	java/util/concurrent/locks/ReentrantLock:lock	()V
/*    */     //   109: getstatic 45	com/avrgaming/civcraft/components/Component:componentsByType	Ljava/util/concurrent/ConcurrentHashMap;
/*    */     //   112: aload_0
/*    */     //   113: getfield 25	com/avrgaming/civcraft/components/RegisterComponentAsync:name	Ljava/lang/String;
/*    */     //   116: invokevirtual 49	java/util/concurrent/ConcurrentHashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*    */     //   119: checkcast 55	java/util/ArrayList
/*    */     //   122: astore_1
/*    */     //   123: aload_1
/*    */     //   124: ifnonnull +10 -> 134
/*    */     //   127: getstatic 34	com/avrgaming/civcraft/components/Component:componentsLock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   130: invokevirtual 72	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*    */     //   133: return
/*    */     //   134: aload_1
/*    */     //   135: aload_0
/*    */     //   136: getfield 23	com/avrgaming/civcraft/components/RegisterComponentAsync:component	Lcom/avrgaming/civcraft/components/Component;
/*    */     //   139: invokevirtual 75	java/util/ArrayList:remove	(Ljava/lang/Object;)Z
/*    */     //   142: pop
/*    */     //   143: getstatic 45	com/avrgaming/civcraft/components/Component:componentsByType	Ljava/util/concurrent/ConcurrentHashMap;
/*    */     //   146: aload_0
/*    */     //   147: getfield 25	com/avrgaming/civcraft/components/RegisterComponentAsync:name	Ljava/lang/String;
/*    */     //   150: aload_1
/*    */     //   151: invokevirtual 62	java/util/concurrent/ConcurrentHashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*    */     //   154: pop
/*    */     //   155: aload_0
/*    */     //   156: getfield 21	com/avrgaming/civcraft/components/RegisterComponentAsync:buildable	Lcom/avrgaming/civcraft/structure/Buildable;
/*    */     //   159: ifnull +30 -> 189
/*    */     //   162: aload_0
/*    */     //   163: getfield 21	com/avrgaming/civcraft/components/RegisterComponentAsync:buildable	Lcom/avrgaming/civcraft/structure/Buildable;
/*    */     //   166: getfield 66	com/avrgaming/civcraft/structure/Buildable:attachedComponents	Ljava/util/ArrayList;
/*    */     //   169: aload_0
/*    */     //   170: getfield 23	com/avrgaming/civcraft/components/RegisterComponentAsync:component	Lcom/avrgaming/civcraft/components/Component;
/*    */     //   173: invokevirtual 75	java/util/ArrayList:remove	(Ljava/lang/Object;)Z
/*    */     //   176: pop
/*    */     //   177: goto +12 -> 189
/*    */     //   180: astore_2
/*    */     //   181: getstatic 34	com/avrgaming/civcraft/components/Component:componentsLock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   184: invokevirtual 72	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*    */     //   187: aload_2
/*    */     //   188: athrow
/*    */     //   189: getstatic 34	com/avrgaming/civcraft/components/Component:componentsLock	Ljava/util/concurrent/locks/ReentrantLock;
/*    */     //   192: invokevirtual 72	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*    */     //   195: return
/*    */     // Line number table:
/*    */     //   Java source line #43	-> byte code offset #0
/*    */     //   Java source line #44	-> byte code offset #7
/*    */     //   Java source line #46	-> byte code offset #13
/*    */     //   Java source line #48	-> byte code offset #27
/*    */     //   Java source line #49	-> byte code offset #31
/*    */     //   Java source line #52	-> byte code offset #39
/*    */     //   Java source line #53	-> byte code offset #48
/*    */     //   Java source line #54	-> byte code offset #60
/*    */     //   Java source line #55	-> byte code offset #67
/*    */     //   Java source line #57	-> byte code offset #82
/*    */     //   Java source line #58	-> byte code offset #86
/*    */     //   Java source line #59	-> byte code offset #92
/*    */     //   Java source line #58	-> byte code offset #94
/*    */     //   Java source line #60	-> byte code offset #100
/*    */     //   Java source line #61	-> byte code offset #103
/*    */     //   Java source line #63	-> byte code offset #109
/*    */     //   Java source line #65	-> byte code offset #123
/*    */     //   Java source line #75	-> byte code offset #127
/*    */     //   Java source line #66	-> byte code offset #133
/*    */     //   Java source line #69	-> byte code offset #134
/*    */     //   Java source line #70	-> byte code offset #143
/*    */     //   Java source line #71	-> byte code offset #155
/*    */     //   Java source line #72	-> byte code offset #162
/*    */     //   Java source line #74	-> byte code offset #177
/*    */     //   Java source line #75	-> byte code offset #181
/*    */     //   Java source line #76	-> byte code offset #187
/*    */     //   Java source line #75	-> byte code offset #189
/*    */     //   Java source line #79	-> byte code offset #195
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	196	0	this	RegisterComponentAsync
/*    */     //   26	30	1	components	java.util.ArrayList<Component>
/*    */     //   122	29	1	components	java.util.ArrayList<Component>
/*    */     //   85	8	2	localObject1	Object
/*    */     //   180	8	2	localObject2	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   13	85	85	finally
/*    */     //   109	127	180	finally
/*    */     //   134	180	180	finally
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\components\RegisterComponentAsync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
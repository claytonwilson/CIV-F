/*     */ package com.avrgaming.civcraft.object;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EconObject
/*     */ {
/*     */   private String econName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  25 */   private Double coins = Double.valueOf(0.0D);
/*  26 */   private Double debt = Double.valueOf(0.0D);
/*  27 */   private Double principalAmount = Double.valueOf(0.0D);
/*     */   private SQLObject holder;
/*     */   
/*     */   public EconObject(SQLObject holder) {
/*  31 */     this.holder = holder;
/*     */   }
/*     */   
/*     */   public String getEconomyName() {
/*  35 */     return this.econName;
/*     */   }
/*     */   
/*     */   public void setEconomyName(String name) {
/*  39 */     this.econName = name;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public double getBalance()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_0
/*     */     //   2: getfield 25	com/avrgaming/civcraft/object/EconObject:coins	Ljava/lang/Double;
/*     */     //   5: invokevirtual 46	java/lang/Double:doubleValue	()D
/*     */     //   8: invokestatic 49	java/lang/Math:floor	(D)D
/*     */     //   11: invokestatic 19	java/lang/Double:valueOf	(D)Ljava/lang/Double;
/*     */     //   14: putfield 25	com/avrgaming/civcraft/object/EconObject:coins	Ljava/lang/Double;
/*     */     //   17: aload_0
/*     */     //   18: getfield 25	com/avrgaming/civcraft/object/EconObject:coins	Ljava/lang/Double;
/*     */     //   21: dup
/*     */     //   22: astore_1
/*     */     //   23: monitorenter
/*     */     //   24: aload_0
/*     */     //   25: getfield 25	com/avrgaming/civcraft/object/EconObject:coins	Ljava/lang/Double;
/*     */     //   28: invokevirtual 46	java/lang/Double:doubleValue	()D
/*     */     //   31: aload_1
/*     */     //   32: monitorexit
/*     */     //   33: dreturn
/*     */     //   34: aload_1
/*     */     //   35: monitorexit
/*     */     //   36: athrow
/*     */     // Line number table:
/*     */     //   Java source line #43	-> byte code offset #0
/*     */     //   Java source line #45	-> byte code offset #17
/*     */     //   Java source line #46	-> byte code offset #24
/*     */     //   Java source line #45	-> byte code offset #34
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	37	0	this	EconObject
/*     */     //   22	13	1	Ljava/lang/Object;	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   24	33	34	finally
/*     */     //   34	36	34	finally
/*     */   }
/*     */   
/*     */   public void setBalance(double amount)
/*     */   {
/*  52 */     setBalance(amount, true);
/*     */   }
/*     */   
/*     */   public void setBalance(double amount, boolean save) {
/*  56 */     if (amount < 0.0D) {
/*  57 */       amount = 0.0D;
/*     */     }
/*  59 */     amount = Math.floor(amount);
/*     */     
/*  61 */     synchronized (this.coins) {
/*  62 */       this.coins = Double.valueOf(amount);
/*     */     }
/*     */     
/*  65 */     if (save) {
/*  66 */       this.holder.save();
/*     */     }
/*     */   }
/*     */   
/*     */   public void deposit(double amount) {
/*  71 */     if (amount < 0.0D) {
/*  72 */       amount = 0.0D;
/*     */     }
/*  74 */     amount = Math.floor(amount);
/*  75 */     deposit(amount, true);
/*     */   }
/*     */   
/*     */   public void deposit(double amount, boolean save) {
/*  79 */     if (amount < 0.0D) {
/*  80 */       amount = 0.0D;
/*     */     }
/*  82 */     amount = Math.floor(amount);
/*     */     
/*  84 */     synchronized (this.coins) {
/*  85 */       this.coins = Double.valueOf(this.coins.doubleValue() + amount);
/*     */     }
/*     */     
/*  88 */     if (save) {
/*  89 */       this.holder.save();
/*     */     }
/*     */   }
/*     */   
/*     */   public void withdraw(double amount)
/*     */   {
/*  95 */     if (amount < 0.0D) {
/*  96 */       amount = 0.0D;
/*     */     }
/*  98 */     amount = Math.floor(amount);
/*     */     
/* 100 */     withdraw(amount, true);
/*     */   }
/*     */   
/*     */   public void withdraw(double amount, boolean save) {
/* 104 */     if (amount < 0.0D) {
/* 105 */       amount = 0.0D;
/*     */     }
/* 107 */     amount = Math.floor(amount);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 114 */     synchronized (this.principalAmount) {
/* 115 */       if (this.principalAmount.doubleValue() > 0.0D) {
/* 116 */         double currentBalance = getBalance();
/* 117 */         double diff = currentBalance - this.principalAmount.doubleValue();
/* 118 */         diff -= amount;
/*     */         
/* 120 */         if (diff < 0.0D) {
/* 121 */           this.principalAmount = Double.valueOf(this.principalAmount.doubleValue() - -diff);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 126 */     synchronized (this.coins) {
/* 127 */       this.coins = Double.valueOf(this.coins.doubleValue() - amount);
/*     */     }
/*     */     
/* 130 */     if (save) {
/* 131 */       this.holder.save();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasEnough(double amount)
/*     */   {
/* 143 */     amount = Math.floor(amount);
/*     */     
/* 145 */     synchronized (this.coins) {
/* 146 */       if (this.coins.doubleValue() >= amount) {
/* 147 */         return true;
/*     */       }
/* 149 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean payTo(EconObject objToPay, double amount)
/*     */   {
/* 156 */     if (!hasEnough(amount)) {
/* 157 */       return false;
/*     */     }
/* 159 */     withdraw(amount);
/* 160 */     objToPay.deposit(amount);
/* 161 */     return true;
/*     */   }
/*     */   
/*     */   public double payToCreditor(EconObject objToPay, double amount)
/*     */   {
/* 166 */     double total = 0.0D;
/*     */     
/* 168 */     if (hasEnough(amount)) {
/* 169 */       withdraw(amount);
/* 170 */       objToPay.deposit(amount);
/* 171 */       return amount;
/*     */     }
/*     */     
/*     */ 
/* 175 */     this.debt = Double.valueOf(this.debt.doubleValue() + (amount - getBalance()));
/* 176 */     objToPay.deposit(getBalance());
/* 177 */     withdraw(getBalance());
/*     */     
/* 179 */     return total;
/*     */   }
/*     */   
/*     */   public boolean inDebt()
/*     */   {
/* 184 */     this.debt = Double.valueOf(Math.floor(this.debt.doubleValue()));
/*     */     
/* 186 */     if (this.debt.doubleValue() > 0.0D) {
/* 187 */       return true;
/*     */     }
/* 189 */     return false;
/*     */   }
/*     */   
/*     */   public double getDebt() {
/* 193 */     this.debt = Double.valueOf(Math.floor(this.debt.doubleValue()));
/* 194 */     return this.debt.doubleValue();
/*     */   }
/*     */   
/*     */   public void setDebt(double debt) {
/* 198 */     debt = Math.floor(debt);
/* 199 */     this.debt = Double.valueOf(debt);
/*     */   }
/*     */   
/*     */   public double getPrincipalAmount() {
/* 203 */     return this.principalAmount.doubleValue();
/*     */   }
/*     */   
/*     */   public void setPrincipalAmount(double interestAmount) {
/* 207 */     this.principalAmount = Double.valueOf(interestAmount);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\EconObject.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
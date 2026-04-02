package com.zeroone.star.sysconfig.enums;

public enum TemplateStatus {
     DISABLE(0),
    ENABLE(1);
     private int value;
     TemplateStatus(int value) {
         this.value = value;
     }
     public int getValue() {
         return value;
     }
}

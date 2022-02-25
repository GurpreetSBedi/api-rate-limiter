package com.java.gurpreet.api;

enum Licenses {
    LOW,MEDIUM,HIGH;

    public int getLicense(){
        switch(this){
            case LOW:
                return 10;
            case MEDIUM:
                    return 20;
            case HIGH:
                return 30;
            default:
                return 40;
        }
    }
}

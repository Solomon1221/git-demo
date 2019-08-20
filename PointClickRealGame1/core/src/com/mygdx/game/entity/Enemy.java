package com.mygdx.game.entity;

public class Enemy extends EntityBase {
    public boolean flag;
    public boolean flag2;

    public boolean getFlag2() {
        return flag2;
    }

    public void setFlag2(boolean flag2) {
        this.flag2 = flag2;
    }

    public Enemy() {
    }

    public void setFlag(Boolean flag){
        this.flag = flag;
    }

    public boolean getFlag(){
        return flag;
    }

}

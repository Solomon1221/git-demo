package com.mygdx.game.game;

import com.mygdx.game.entity.EntityBase;

public class Wall extends EntityBase {
    public boolean remove;
    public boolean notIntersect;

    public boolean isNotIntersect() {
        return notIntersect;
    }

    public void setNotIntersect(boolean notIntersect) {
        this.notIntersect = notIntersect;
    }

    public Wall(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setRemove(boolean remove){
        this.remove = remove;
    }

    public boolean getRemove(){
        return remove;
    }





}

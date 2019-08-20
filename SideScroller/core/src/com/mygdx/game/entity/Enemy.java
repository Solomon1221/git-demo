package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Enemy extends EntityBase {
    float code;
    public Enemy() {
    }

    public void setCode(float code){
        this.code = code;
    }

    public float getCode(){
        return code;
    }

    public void render(ShapeRenderer renderer, float x, float y, float width, float height){
        renderer.rect(x, y, width, height);

    }

}

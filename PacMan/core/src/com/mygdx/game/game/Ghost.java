package com.mygdx.game.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.entity.EntityBase;

public class Ghost extends EntityBase {
    public float direction = 1;

    public LeftAntena leftAntena;
    public RightAntena rightAntena;
    public UpAntena upAntena;
    public DownAntena downAntena;

    public boolean leftClear;
    public boolean rightClear;
    public boolean upClear;
    public boolean downClear;

    public Ghost() {
    }

    public void update(float delta, float x, float y) {
        setPosition(x, y);
    }

    public void drawGhost(ShapeRenderer renderer) {
        Color oldColor = renderer.getColor();
        renderer.setColor(Color.RED);
        renderer.rect(getX(), getY(), getWidth(), getHeight());
        renderer.setColor(oldColor);
        renderer.setColor(Color.WHITE);

    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public float getDirection() {
        return direction;
    }

    public LeftAntena getLeftAntena() {
        return leftAntena;
    }

    public void setLeftAntena(LeftAntena leftAntena) {
        this.leftAntena = leftAntena;
    }

    public UpAntena getUpAntena() {
        return upAntena;
    }

    public void setUpAntena(UpAntena upAntena) {
        this.upAntena = upAntena;
    }

    public DownAntena getDownAntena() {
        return downAntena;
    }

    public void setDownAntena(DownAntena downAntena) {
        this.downAntena = downAntena;
    }

    public RightAntena getRightAntena() {
        return rightAntena;
    }

    public void setRightAntena(RightAntena rightAntena) {
        this.rightAntena = rightAntena;
    }
}

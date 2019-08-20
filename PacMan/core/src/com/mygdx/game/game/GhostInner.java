package com.mygdx.game.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.entity.EntityBase;

public class GhostInner extends EntityBase {
    public float direction = 1;
    public GhostInner() {
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
}

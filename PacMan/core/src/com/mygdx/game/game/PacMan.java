package com.mygdx.game.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.entity.EntityBase;

public class PacMan extends EntityBase {
    public PacMan() {
    }

    public void update(float delta, float x, float y) {
        setPosition(x, y);
    }

    public void drawPacMan(ShapeRenderer renderer) {
        Color oldColor = renderer.getColor();
        renderer.setColor(Color.BLUE);
        renderer.rect(getX(), getY(), getWidth(), getHeight());
        renderer.setColor(oldColor);
        renderer.setColor(Color.WHITE);

    }

}

package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;

import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;

public class Bullet extends EntityBase {
    public Vector2 position;
    public float velocity;
    public float deltaTime;
    public float angle2;
    MyGdxGame game;

    public Bullet() {
        position = new Vector2();
        velocity = 8f;
        game = new MyGdxGame();
    }

    public void update() {

        position.y += velocity;
    }

    public void updateAngle(float angle) {
        deltaTime = Gdx.graphics.getDeltaTime();


        position.x = position.x + 30 * deltaTime * cos(angle);
        position.y = position.y + 30 * deltaTime * sin(angle);
    }




    public float setAngle(float angle) {
        this.angle2 = angle;
        return angle2;
    }

    public float getAngle() {
        return this.angle2;
    }
}

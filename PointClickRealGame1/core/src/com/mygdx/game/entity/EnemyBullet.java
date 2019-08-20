package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;

import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;

public class EnemyBullet extends EntityBase {
    public Vector2 position;
    public float velocity;
    public float deltaTime;
    public float angle2;
    public float identity;

    public float getIdentity() {
        return identity;
    }

    public void setIdentity(float identity) {
        this.identity = identity;
    }

    MyGdxGame game;

    public EnemyBullet() {
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
        setPosition(position.x, position.y);
        setSize(.25f, (float) .25);
    }




    public float setAngle(float angle) {
        this.angle2 = angle;
        return angle2;
    }

    public float getAngle() {
        return this.angle2;
    }
}

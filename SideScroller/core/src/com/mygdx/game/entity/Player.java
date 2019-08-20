package com.mygdx.game.entity;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.config.GameConfig;
import com.mygdx.game.screen.game.GameRenderer;

public class Player extends EntityBase {
    private float speed = 0;
    private float acceleration = GameConfig.MONSTER_START_ACC;
    private PlayerState state = PlayerState.WALKING;
    public GameRenderer renderer;

    public Player(GameRenderer renderer) {
        this.renderer = renderer;

    }

    public void update(float delta, float xminus) {

        if (state.isJumping()) {
            speed += acceleration * delta;

            // when reached max speed switch state to falling

            if (speed >= GameConfig.MONSTER_MAX_SPEED) {
                fall();
            }

        } else if (state.isFalling()) {
            // falling down
            speed -= acceleration * delta;

            // when speed is 0 we are walking again
            // speed <= 0
            if (getY() <= xminus) {
                renderer.setY(xminus);
                speed = 0;
                walk();
            }

        } else if(state.isWalking() && xminus < getY()){
            fall();
        }


    }

    public void jump() {
        state = PlayerState.JUMPING;
    }

    public boolean isWalking(){
        return state.isWalking();
    }

    public boolean isFalling(){
        return state.isFalling();
    }

    public boolean isJumping(){
        return state.isJumping();
    }


    public void setSpeedZero(){
        this.speed = 0;

    }

    public PlayerState getState() {
        return state;
    }

    public float getSpeed() {
        return speed;
    }

    // private methods

    private void updateY() {
        float y = getY() + speed;
        setY(y);
    }

    public void fall() {
        state = PlayerState.FALLING;
    }

    public void walk() {
        state = PlayerState.WALKING;
    }
}

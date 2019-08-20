package com.mygdx.game.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.entity.Player;

public class GameController {
    Player player;
    private static final Logger log = new Logger(GameController.class.getName(), Logger.DEBUG);

    private float animationTime;

    public GameController(){
//        GameRenderer renderer =
//        player = new Player();
    }

    // == public methods ==
    public void update(float delta){
        animationTime += delta;
//        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
//            player.jump();
//        }
//
//        player.update(delta);
    }

    public float getAnimationTime() {
        return animationTime;
    }
}

package com.netlab.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.netlab.frontend.systems.AssetManager;

public class Boss extends Enemy {
    private int currentDir = 0; // -1: Left, 0: Idle, 1: Right
    private float animStateTime = 0f;

    public Boss(String name, int hp) {
        super(380, 400, 64, 64, Color.BLUE, name, hp, 5000L);
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 64, 64, Color.BLUE, name, hp, 5000L);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        animStateTime += delta;

        // Transition from start phase (first half) to loop phase (latter half)
        AssetManager assets = AssetManager.getInstance();
        if (currentDir < 0) {
            Animation<TextureRegion> startAnim = assets.getAnimation("boss_left_start");
            Animation<TextureRegion> loopAnim = assets.getAnimation("boss_left_loop");
            if (animation == startAnim && startAnim.isAnimationFinished(animStateTime)) {
                setAnimation(loopAnim);
            }
        } else if (currentDir > 0) {
            Animation<TextureRegion> startAnim = assets.getAnimation("boss_right_start");
            Animation<TextureRegion> loopAnim = assets.getAnimation("boss_right_loop");
            if (animation == startAnim && startAnim.isAnimationFinished(animStateTime)) {
                setAnimation(loopAnim);
            }
        } else {
            Animation<TextureRegion> startAnim = assets.getAnimation("boss_idle_start");
            Animation<TextureRegion> loopAnim = assets.getAnimation("boss_idle_loop");
            if (animation == startAnim && startAnim.isAnimationFinished(animStateTime)) {
                setAnimation(loopAnim);
            }
        }
    }

    public void updateAnimationState(float dx) {
        AssetManager assets = AssetManager.getInstance();
        if (dx < 0) {
            if (currentDir != -1) {
                currentDir = -1;
                animStateTime = 0f;
                setAnimation(assets.getAnimation("boss_left_start"));
            }
        } else if (dx > 0) {
            if (currentDir != 1) {
                currentDir = 1;
                animStateTime = 0f;
                setAnimation(assets.getAnimation("boss_right_start"));
            }
        } else {
            if (currentDir != 0 || animation == null) {
                currentDir = 0;
                animStateTime = 0f;
                setAnimation(assets.getAnimation("boss_idle_start"));
            }
        }
    }
}

package com.netlab.frontend.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    private static AssetManager instance;

    private Map<String, Texture> textures = new HashMap<>();
    private Map<String, TextureRegion> regions = new HashMap<>();
    private Map<String, Animation<TextureRegion>> animations = new HashMap<>();

    private AssetManager() {}

    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    public void init() {
        // Reimu Animations (32x48 per tile, 8 columns)
        registerAnimationFromSheet("player_idle", "player.png", 32, 48, 0, 0, 8, 0.125f, Animation.PlayMode.LOOP);
        registerAnimationFromSheet("player_left_start", "player.png", 32, 48, 1, 0, 4, 0.8f, Animation.PlayMode.NORMAL);
        registerAnimationFromSheet("player_left_loop",  "player.png", 32, 48, 1, 4, 4, 0.12f, Animation.PlayMode.LOOP);
        registerAnimationFromSheet("player_right_start", "player.png", 32, 48, 2, 0, 4, 0.8f, Animation.PlayMode.NORMAL);
        registerAnimationFromSheet("player_right_loop",  "player.png", 32, 48, 2, 4, 4, 0.12f, Animation.PlayMode.LOOP);

        // Boss Cirno Animations (64x64 per tile, 4 columns)
        registerAnimationFromSheet("boss_idle", "cirno.png", 64, 64, 0, 0, 4, 0.2f, Animation.PlayMode.LOOP);
        registerAnimationFromSheet("boss_left_start", "cirno.png", 64, 64, 1, 0, 2, 0.12f, Animation.PlayMode.NORMAL);
        registerAnimationFromSheet("boss_left_loop",  "cirno.png", 64, 64, 1, 2, 2, 0.2f,  Animation.PlayMode.LOOP);
        registerAnimationFromSheet("boss_right_start", "cirno.png", 64, 64, 2, 0, 2, 0.12f, Animation.PlayMode.NORMAL);
        registerAnimationFromSheet("boss_right_loop",  "cirno.png", 64, 64, 2, 2, 2, 0.2f,  Animation.PlayMode.LOOP);

        // Stage 1 Fairy & Bullets
        registerAnimationFromSheet("fairy_idle", "fairy.png", 32, 32, 1, 8, 0.125f);
        registerRegionFromSheet("bullet_amulet", "bullets_small.png", 16, 16, 6, 0);
        registerRegionFromSheet("bullet_danmaku", "bullets_small.png", 16, 16, 2, 0);

        // Items (items.png 16x16 per cell):
        registerRegionFromSheet("item_power", "items.png", 16, 16, 0, 0);
        registerRegionFromSheet("item_point", "items.png", 16, 16, 0, 1);
        registerRegionFromSheet("item_bomb",  "items.png", 16, 16, 0, 3);
        registerRegionFromSheet("item_life",  "items.png", 16, 16, 0, 5);
    }

    public Texture loadTexture(String filename) {
        if (!textures.containsKey(filename)) {
            if (Gdx.files != null && Gdx.files.internal(filename).exists()) {
                textures.put(filename, new Texture(Gdx.files.internal(filename)));
            } else {
                return null;
            }
        }
        return textures.get(filename);
    }

    public void registerRegionFromSheet(String name, String filename, int frameW, int frameH, int row, int col) {
        Texture sheet = loadTexture(filename);
        if (sheet != null) {
            TextureRegion region = new TextureRegion(sheet, col * frameW, row * frameH, frameW, frameH);
            regions.put(name, region);
        }
    }

    public void registerAnimationFromSheet(String name, String filename, int frameW, int frameH, int row, int frameCount, float frameDuration) {
        registerAnimationFromSheet(name, filename, frameW, frameH, row, 0, frameCount, frameDuration, Animation.PlayMode.LOOP);
    }

    public void registerAnimationFromSheet(String name, String filename, int frameW, int frameH, int row, int startCol, int frameCount, float frameDuration, Animation.PlayMode playMode) {
        Texture sheet = loadTexture(filename);
        if (sheet != null) {
            TextureRegion[] frames = new TextureRegion[frameCount];
            for (int i = 0; i < frameCount; i++) {
                frames[i] = new TextureRegion(sheet, (startCol + i) * frameW, row * frameH, frameW, frameH);
            }
            Animation<TextureRegion> anim = new Animation<>(frameDuration, frames);
            anim.setPlayMode(playMode);
            animations.put(name, anim);
        }
    }

    public TextureRegion getRegion(String name) {
        return regions.get(name);
    }

    public TextureRegion getTextureRegion(String name) {
        return getRegion(name);
    }

    public Animation<TextureRegion> getAnimation(String name) {
        return animations.get(name);
    }

    public void dispose() {
        for (Texture texture : textures.values()) {
            if (texture != null) texture.dispose();
        }
        textures.clear();
        regions.clear();
        animations.clear();
    }
}

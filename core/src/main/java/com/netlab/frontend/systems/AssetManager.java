package com.netlab.frontend.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    private static AssetManager instance;
    private Map<String, TextureRegion> textureRegionMap;
    private Map<String, Animation<TextureRegion>> animationMap;
    private Map<String, Texture> textureMap;

    private AssetManager() {
        textureRegionMap = new HashMap<>();
        animationMap = new HashMap<>();
        textureMap = new HashMap<>();
    }

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

    // 1. O(1) Dynamic Flyweight Lookups (No switch statements)
    public TextureRegion getTextureRegion(String key) {
        return textureRegionMap.get(key);
    }

    public TextureRegion getRegion(String key) {
        return getTextureRegion(key);
    }

    public Animation<TextureRegion> getAnimation(String key) {
        return animationMap.get(key);
    }

    // 2. Generic Registration Methods
    public Texture loadTexture(String filename) {
        if (!textureMap.containsKey(filename)) {
            if (Gdx.files != null && Gdx.files.internal(filename).exists()) {
                textureMap.put(filename, new Texture(Gdx.files.internal(filename)));
            } else {
                return null;
            }
        }
        return textureMap.get(filename);
    }

    public void registerRegion(String key, TextureRegion region) {
        textureRegionMap.put(key, region);
    }

    public void registerRegionFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int col) {
        Texture tex = loadTexture(filename);
        if (tex != null) {
            TextureRegion[][] grid = TextureRegion.split(tex, tileWidth, tileHeight);
            textureRegionMap.put(key, grid[row][col]);
        }
    }

    public void registerAnimationFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int numFrames, float frameDuration) {
        registerAnimationFromSheet(key, filename, tileWidth, tileHeight, row, 0, numFrames, frameDuration, Animation.PlayMode.LOOP);
    }

    public void registerAnimationFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int startCol, int numFrames, float frameDuration, Animation.PlayMode playMode) {
        Texture tex = loadTexture(filename);
        if (tex != null) {
            TextureRegion[][] grid = TextureRegion.split(tex, tileWidth, tileHeight);

            TextureRegion[] frames = new TextureRegion[numFrames];
            for (int i = 0; i < numFrames; i++) {
                frames[i] = grid[row][startCol + i];
            }

            Animation<TextureRegion> anim = new Animation<>(frameDuration, frames);
            anim.setPlayMode(playMode);

            animationMap.put(key, anim);
            textureRegionMap.put(key, frames[0]);
        }
    }

    public void registerFlippedAnimationFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int numFrames, float frameDuration, boolean flipX, boolean flipY) {
        registerFlippedAnimationFromSheet(key, filename, tileWidth, tileHeight, row, 0, numFrames, frameDuration, Animation.PlayMode.LOOP, flipX, flipY);
    }

    public void registerFlippedAnimationFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int startCol, int numFrames, float frameDuration, Animation.PlayMode playMode, boolean flipX, boolean flipY) {
        Texture tex = loadTexture(filename);
        if (tex != null) {
            TextureRegion[][] grid = TextureRegion.split(tex, tileWidth, tileHeight);

            TextureRegion[] frames = new TextureRegion[numFrames];
            for (int i = 0; i < numFrames; i++) {
                frames[i] = new TextureRegion(grid[row][startCol + i]);
                frames[i].flip(flipX, flipY);
            }

            Animation<TextureRegion> anim = new Animation<>(frameDuration, frames);
            anim.setPlayMode(playMode);

            animationMap.put(key, anim);
            textureRegionMap.put(key, frames[0]);
        }
    }

    public void dispose() {
        for (Texture tex : textureMap.values()) {
            if (tex != null) tex.dispose();
        }
        textureMap.clear();
        textureRegionMap.clear();
        animationMap.clear();
    }
}

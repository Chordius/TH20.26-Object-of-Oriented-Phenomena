package com.netlab.frontend.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.netlab.frontend.observers.GameObserver;
import com.netlab.frontend.objects.enemies.Boss;
import com.netlab.frontend.objects.enemies.Enemy;
import com.netlab.frontend.objects.enemies.Fairy;
import com.netlab.frontend.objects.items.Item;
import com.netlab.frontend.objects.items.ItemType;
import com.netlab.frontend.systems.BulletManager;

import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject {
    private String name;
    private int hp;
    private int power;
    private int spellCards;
    private long score;
    private boolean focused = false;

    // Observer Pattern Subject
    private List<GameObserver> observers = new ArrayList<>();

    public Player(String name, int hp, int power, int spellCards) {
        super(200, 50, 32, 48, 200f, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32, 48, 200f, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    // Observer Registration & Notification
    public void registerObserver(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            // Immediately notify initial state
            observer.onScoreChanged(score);
            observer.onHpChanged(hp);
            observer.onSpellCardsChanged(spellCards);
            observer.onPowerChanged(power);
        }
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    private void notifyScoreChanged() {
        for (GameObserver obs : observers) obs.onScoreChanged(score);
    }

    private void notifyHpChanged() {
        for (GameObserver obs : observers) obs.onHpChanged(hp);
    }

    private void notifySpellCardsChanged() {
        for (GameObserver obs : observers) obs.onSpellCardsChanged(spellCards);
    }

    private void notifyPowerChanged() {
        for (GameObserver obs : observers) obs.onPowerChanged(power);
    }

    @Override
    public void update(float delta) {
        super.update(delta); // Advances stateTime for idle animations
    }

    // Command Pattern Movement execution with Playfield Clamping
    public void move(float dx, float dy) {
        this.x += dx * speed;
        this.y += dy * speed;
        clampToPlayfield();
    }

    private void clampToPlayfield() {
        // Playfield bounds: X: 32..416 (Width 384), Y: 16..560 (Height 544)
        float minX = 32;
        float maxX = 416 - width;
        float minY = 16;
        float maxY = 560 - height;

        if (x < minX) x = minX;
        if (x > maxX) x = maxX;
        if (y < minY) y = minY;
        if (y > maxY) y = maxY;
    }

    // Command Pattern Bullet Shooting
    public void shootBullet(BulletManager bulletManager) {
        bulletManager.spawnPlayerBullet(x + width / 2 - 8, y + height, 0, 400f, 10 + power);
    }

    // Command Pattern Bomb / Spell Card execution
    public void useBomb(BulletManager bulletManager) {
        if (spellCards > 0) {
            spellCards--;
            notifySpellCardsChanged();
            bulletManager.clearEnemyBullets(); // Clears all enemy bullets on screen!
            System.out.println(name + " unleashes SPELL CARD (Fantasy Seal)! All enemy bullets cleared!");
        } else {
            System.out.println("No Spell Cards (Bombs) remaining!");
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Fairy) {
            System.out.println("Player touches fairy");
        } else if (other instanceof Boss) {
            System.out.println("Player touches boss");
        } else if (other instanceof Item item) {
            System.out.println("Player touches items");
            collectItem(item);
        }
    }

    public void shoot(Enemy target) {
        int damage = 10 + power;
        System.out.println(name + " shoots " + target.getName() + " dealing " + damage + " DMG!");
        boolean defeated = target.takeDamage(damage);
        if (defeated) {
            addScore(target.getScoreValue());
        }
    }

    public void collectItem(Item item) {
        if (item.isDestroyed()) return;

        ItemType type = item.getItemTypeEnum();
        if (type != null) {
            switch (type) {
                case POWER -> {
                    this.power += type.getPowerBonus();
                    notifyPowerChanged();
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected POWER item! Power increased to " + power);
                }
                case POINT -> {
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected POINT item!");
                }
                case BOMB -> {
                    this.spellCards++;
                    notifySpellCardsChanged();
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected BOMB item! SpellCards: " + spellCards);
                }
                case LIFE -> {
                    this.hp += 20;
                    notifyHpChanged();
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected LIFE item! HP: " + hp);
                }
            }
        } else {
            addScore(item.getScoreValue());
            System.out.println(name + " collected " + item.getItemType() + "!");
        }

        item.destroy();
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        notifyHpChanged();
        System.out.println(name + " took " + damage + " damage! Remaining HP: " + this.hp);
        if (this.hp == 0) {
            System.out.println(name + " was defeated (Pichuun~)! ");
        }
    }

    public void addScore(long points) {
        if (points > 0) {
            this.score += points;
            notifyScoreChanged();
            System.out.println(name + " gained " + points + " pts! Total Score: " + this.score);
        }
    }

    public boolean isAlive() {
        return this.hp > 0;
    }

    public boolean isFocused() { return focused; }
    public void setFocused(boolean focused) { this.focused = focused; }

    // Encapsulation getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getHp() { return hp; }
    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
        notifyHpChanged();
    }

    public int getPower() { return power; }
    public void setPower(int power) {
        this.power = power;
        notifyPowerChanged();
    }

    public int getSpellCards() { return spellCards; }
    public void setSpellCards(int spellCards) {
        this.spellCards = spellCards;
        notifySpellCardsChanged();
    }

    public long getScore() { return score; }
}

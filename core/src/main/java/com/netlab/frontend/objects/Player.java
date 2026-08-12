package com.netlab.frontend.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.netlab.frontend.observers.GameObserver;
import com.netlab.frontend.objects.enemies.Boss;
import com.netlab.frontend.objects.enemies.Enemy;
import com.netlab.frontend.objects.enemies.Fairy;
import com.netlab.frontend.objects.items.Item;
import com.netlab.frontend.objects.items.ItemType;
import com.netlab.frontend.objects.patterns.ShootingPattern;
import com.netlab.frontend.objects.patterns.shootingStrategy.LinearShot;
import com.netlab.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.netlab.frontend.objects.patterns.shootingStrategy.HomingNeedleShot;
import com.netlab.frontend.objects.patterns.shootingStrategy.CompositeShootingPattern;
import com.netlab.frontend.objects.patterns.entityStrategy.EntityMovementPattern;
import com.netlab.frontend.objects.patterns.entityStrategy.FixedMovement;
import com.netlab.frontend.systems.AssetManager;
import com.netlab.frontend.systems.BulletManager;

import java.util.ArrayList;
import java.util.List;

public class Player extends EntityShooter {
    private String name;
    private int hp;
    private int power;
    private int spellCards;
    private long score;
    private int grazeCount = 0;
    private boolean focused = false;

    private float shootTimer = 0.1f;
    private float shootCooldown = 0.1f; // Continuous fire rate: 10 shots per second when holding Z

    private int currentDir = 0; // -1: Left, 0: Idle, 1: Right
    private GameObject targetEnemy;

    // Observer Pattern Subject
    private List<GameObserver> observers = new ArrayList<>();

    public Player(String name, int hp, int power, int spellCards) {
        super(200, 50, 32, 48, 200f, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
        setMovementPattern(new FixedMovement());
        updatePowerStrategy();
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32, 48, 200f, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
        setMovementPattern(new FixedMovement());
        updatePowerStrategy();
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards,
                  ShootingPattern shootingPattern, EntityMovementPattern movementPattern) {
        super(x, y, 32, 48, 200f, Color.RED, shootingPattern, movementPattern);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
        if (shootingPattern == null) {
            updatePowerStrategy();
        }
    }

    public void setTargetEnemy(GameObject targetEnemy) {
        this.targetEnemy = targetEnemy;
        updatePowerStrategy();
    }

    // Dynamic Strategy Swapping based on Player Power Level & Focus Mode (Shift key)
    public void updatePowerStrategy() {
        float spreadAngle = focused ? 4f : 18f;     // Narrow stream in Focus Mode (4° vs 18°)
        float dualSpreadAngle = focused ? 3f : 12f; // Narrow stream in Focus Mode (3° vs 12°)

        if (power < 16) {
            // Level 1 (0-15 Power): Single forward amulet stream
            setShootingPattern(new LinearShot(400f, 10 + power));
        } else if (power < 32) {
            // Level 2 (16-31 Power): Dual forward amulet streams (Narrow in Focus Mode)
            setShootingPattern(new SpreadShot(400f, 2, dualSpreadAngle, 10 + power));
        } else if (power < 64) {
            // Level 3 (32-63 Power): Triple forward amulet streams (Narrow in Focus Mode)
            setShootingPattern(new SpreadShot(400f, 3, spreadAngle, 10 + power));
        } else if (power < 128) {
            // Level 4 (64-127 Power): Composite Strategy (3-Way Spread + 1 Homing Needle stream)
            setShootingPattern(new CompositeShootingPattern(
                new SpreadShot(400f, 3, spreadAngle, 10 + power),
                new HomingNeedleShot(400f, 1, 10 + power, targetEnemy)
            ));
        } else {
            // Level 5 MAX (128 Power): Composite Strategy (3-Way Spread + 2 Homing Needle streams)
            setShootingPattern(new CompositeShootingPattern(
                new SpreadShot(400f, 3, spreadAngle, 10 + power),
                new HomingNeedleShot(400f, 2, 10 + power, targetEnemy)
            ));
        }
    }

    // Touhou Hitboxes: Graze Hitbox is full sprite size (32x48), Core Hitbox is small centered dot (8x8)
    @Override
    public Rectangle getGrazeHitbox() {
        return new Rectangle(x, y, width, height); // Full sprite size
    }

    @Override
    public Rectangle getCoreHitbox() {
        return new Rectangle(x + width / 2f - 4f, y + height / 2f - 4f, 8f, 8f); // Small centered 8x8 hurtbox
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
            observer.onGrazeChanged(grazeCount);
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

    private void notifyGrazeChanged() {
        for (GameObserver obs : observers) obs.onGrazeChanged(grazeCount);
    }

    private float invulnerableTimer = 0f;

    public boolean isInvulnerable() {
        return invulnerableTimer > 0f;
    }

    public float getInvulnerableTimer() {
        return invulnerableTimer;
    }


    @Override
    public void update(float delta) {
        super.update(delta); // Advances stateTime and movementPattern
        shootTimer += delta;  // Accumulates continuous fire timer

        if (invulnerableTimer > 0f) {
            invulnerableTimer -= delta;
            if (invulnerableTimer < 0f) invulnerableTimer = 0f;
        }

        // Transition from 4-frame intro tilt (start) to 4-frame continuous loop (loop)
        AssetManager assets = AssetManager.getInstance();
        if (currentDir < 0) {
            Animation<TextureRegion> startAnim = assets.getAnimation("player_left_start");
            Animation<TextureRegion> loopAnim = assets.getAnimation("player_left_loop");
            if (animation == startAnim && startAnim.isAnimationFinished(stateTime)) {
                setAnimation(loopAnim);
            }
        } else if (currentDir > 0) {
            Animation<TextureRegion> startAnim = assets.getAnimation("player_right_start");
            Animation<TextureRegion> loopAnim = assets.getAnimation("player_right_loop");
            if (animation == startAnim && startAnim.isAnimationFinished(stateTime)) {
                setAnimation(loopAnim);
            }
        }
    }

    // Updates character banking/tilt animation state based on horizontal movement direction (dx)
    public void updateAnimationState(float dx) {
        AssetManager assets = AssetManager.getInstance();
        if (dx < 0) { // Moving Left (Row 1)
            if (currentDir != -1) {
                currentDir = -1;
                setAnimation(assets.getAnimation("player_left_start"));
            }
        } else if (dx > 0) { // Moving Right (Row 2)
            if (currentDir != 1) {
                currentDir = 1;
                setAnimation(assets.getAnimation("player_right_start"));
            }
        } else { // Idle / Stationary (Row 0)
            if (currentDir != 0) {
                currentDir = 0;
                setAnimation(assets.getAnimation("player_idle"));
            }
        }
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

    // Command Pattern Bullet Shooting (Supports holding Z key with cooldown)
    @Override
    public void shootBullet(BulletManager bulletManager) {
        if (shootTimer >= shootCooldown) {
            if (shootingPattern != null) {
                super.shootBullet(bulletManager);
            } else {
                bulletManager.spawnPlayerBullet(x + width / 2 - 8, y + height, 0, 400f, 10 + power);
            }
            shootTimer = 0f;
        }
    }

    // Command Pattern Bomb / Spell Card execution (Spirit Sign "Fantasy Seal" - 霊符「夢想封印」)
    public void useBomb(BulletManager bulletManager, List<GameObject> entities) {
        if (spellCards > 0) {
            spellCards--;
            invulnerableTimer = 5.0f; // 5 seconds invulnerability during Fantasy Seal!
            notifySpellCardsChanged();
            if (bulletManager != null) {
                bulletManager.clearEnemyBullets(entities); // Clears all enemy bullets on screen & converts to drop items!

                // Find active target enemy to home into (Boss or Fairy)
                GameObject target = targetEnemy;
                if (target == null || target.isDestroyed()) {
                    if (entities != null) {
                        for (GameObject obj : entities) {
                            if (obj instanceof Enemy enemy && !enemy.isDestroyed()) {
                                target = enemy;
                                break;
                            }
                        }
                    }
                }

                // Spawn 8 giant homing Fantasy Seal spirit orbs surrounding Reimu!
                bulletManager.spawnFantasySealOrbs(x + width / 2f, y + height / 2f, target);
            }
            System.out.println(name + " unleashes SPELL CARD: Spirit Sign Fantasy Seal (霊符「夢想封印」)! All enemy bullets cleared & 8 homing orbs launched!");
        } else {
            System.out.println("No Spell Cards (Bombs) remaining!");
        }
    }

    public void useBomb(BulletManager bulletManager) {
        useBomb(bulletManager, null);
    }

    public void addGraze() {
        this.grazeCount++;
        addScore(50);
        notifyGrazeChanged();
    }

    @Override
    public void render(SpriteBatch batch) {
        if (batch != null && active) {
            if (focused) {
                batch.setColor(1f, 1f, 1f, 0.5f); // 50% opacity in Focus Mode
            }
            super.render(batch);
            if (focused) {
                batch.setColor(1f, 1f, 1f, 1.0f); // Reset back to 100% opacity
            }
        }
    }

    // Renders visual core hurtbox indicator dot when Focus Mode (Shift) is active
    public void renderFocusIndicator(ShapeRenderer shapeRenderer) {
        if (focused && shapeRenderer != null && active) {
            Rectangle core = getCoreHitbox();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(core.x + core.width / 2f, core.y + core.height / 2f, 4f);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.circle(core.x + core.width / 2f, core.y + core.height / 2f, 2f);
            shapeRenderer.end();
        }
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
                    updatePowerStrategy(); // Dynamic Strategy Swap on Power Increase!
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
        if (isInvulnerable()) {
            System.out.println(name + " is INVULNERABLE during Spell Card! (0 damage taken)");
            return;
        }
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
    public void setFocused(boolean focused) {
        if (this.focused != focused) {
            this.focused = focused;
            updatePowerStrategy(); // Dynamic Strategy Swap: Narrows bullet stream in Focus Mode!
        }
    }

    public int getGrazeCount() { return grazeCount; }

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
        updatePowerStrategy(); // Dynamic Strategy Swap on Power Setter!
        notifyPowerChanged();
    }

    public int getSpellCards() { return spellCards; }
    public void setSpellCards(int spellCards) {
        this.spellCards = spellCards;
        notifySpellCardsChanged();
    }

    public long getScore() { return score; }
}

package com.netlab.frontend;

import com.netlab.frontend.commands.*;
import com.netlab.frontend.objects.GameObject;
import com.netlab.frontend.objects.EntityShooter;
import com.netlab.frontend.objects.Player;
import com.netlab.frontend.objects.bullets.Bullet;
import com.netlab.frontend.objects.bullets.BulletType;
import com.netlab.frontend.objects.enemies.Boss;
import com.netlab.frontend.objects.enemies.Enemy;
import com.netlab.frontend.objects.enemies.Fairy;
import com.netlab.frontend.objects.items.Item;
import com.netlab.frontend.objects.items.ItemType;
import com.netlab.frontend.objects.patterns.ShootingPattern;
import com.netlab.frontend.objects.patterns.shooting.SpreadShot;
import com.netlab.frontend.objects.patterns.shooting.LinearShot;
import com.netlab.frontend.objects.patterns.shooting.RingShot;
import com.netlab.frontend.objects.patterns.bullet.LinearBulletMovement;
import com.netlab.frontend.objects.patterns.bullet.SineWaveBulletMovement;
import com.netlab.frontend.objects.patterns.bullet.HomingBulletMovement;
import com.netlab.frontend.objects.patterns.entity.FixedMovement;
import com.netlab.frontend.objects.patterns.entity.LinearEntityMovement;
import com.netlab.frontend.objects.patterns.entity.TargetPointMovement;
import com.netlab.frontend.objects.patterns.entity.ZigzagEntityMovement;
import com.netlab.frontend.systems.BulletManager;
import com.netlab.frontend.systems.CollisionReferee;
import com.netlab.frontend.ui.GameHUD;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        runTest();
    }

    public static void runTest() {
        Test runner = new Test();

        // ==========================================
        // MODULE 1: BASIC CLASSES & OBJECT INTERACTION
        // ==========================================
        System.out.println("=== TOUHOU OOP PRACTICUM - MODULE 1: BASIC CLASSES & OBJECT INTERACTION ===");

        Player player = new Player("Reimu Hakurei", 100, 15, 3);
        Boss cirno = new Boss("Cirno (Stage 2 Boss)", 50);

        System.out.println("\n--- Initial Battle State ---");
        System.out.println("Player: " + player.getName() + " | HP: " + player.getHp() + " | Power: " + player.getPower() + " | SpellCards: " + player.getSpellCards());
        System.out.println("Enemy:  " + cirno.getName() + " | HP: " + cirno.getHp());

        System.out.println("\n--- Turn 1: Player Shoots Enemy ---");
        player.shoot(cirno);

        System.out.println("\n--- Turn 2: Enemy Counter-attacks ---");
        cirno.attack(player, 30);

        System.out.println("\n--- Turn 3: Player Shoots Enemy Finishing Blow ---");
        player.shoot(cirno);

        System.out.println("\n--- Turn 4: Enemy Deals Fatal Damage to Reimu ---");
        cirno.attack(player, 80);

        System.out.println("\n=== Battle Simulation Complete ===");


        // ==========================================
        // MODULE 2: ENCAPSULATION, INHERITANCE & SCORE SYSTEM
        // ==========================================
        System.out.println("\n\n=== TOUHOU OOP PRACTICUM - MODULE 2: ENCAPSULATION, INHERITANCE & SCORE SYSTEM ===");

        Player player2 = new Player("Reimu Hakurei", 100, 15, 3);
        Fairy fairy = new Fairy("Stage 1 Fairy", 20);
        Boss cirno2 = new Boss("Cirno (Stage 2 Boss)", 150);
        Item pointItem = new Item(300, 450, ItemType.POINT);

        System.out.println("\n--- Testing Encapsulation & Inheritance ---");
        System.out.println("Player: " + player2.getName() + " | Position: (" + player2.getX() + ", " + player2.getY() + ")");
        System.out.println("Fairy:  " + fairy.getName() + " | Defeat Worth: " + fairy.getScoreValue() + " pts");
        System.out.println("Boss:   " + cirno2.getName() + " | Defeat Worth: " + cirno2.getScoreValue() + " pts | Size: " + cirno2.getWidth() + "x" + cirno2.getHeight());
        System.out.println("Item:   " + pointItem.getItemType() + " Item | Value: " + pointItem.getScoreValue() + " pts | Speed: " + pointItem.getSpeed());

        System.out.println("\n--- Testing Item Movement Update ---");
        System.out.println("Initial Item Y: " + pointItem.getY());
        pointItem.update(0.5f);
        System.out.println("Item Y after 0.5s update: " + pointItem.getY() + " (linear downward movement)");

        System.out.println("\n--- Testing Scoring System ---");
        System.out.println("Initial Score: " + player2.getScore());
        player2.shoot(fairy);
        player2.collectItem(pointItem);
        player2.shoot(cirno2);
        System.out.println("Final Score: " + player2.getScore() + " pts");

        System.out.println("\n=== Module 2 Test Completed Successfully ===");


        // ==========================================
        // MODULE 3: POLYMORPHISM & ABSTRACTION
        // ==========================================
        System.out.println("\n\n=== TOUHOU OOP PRACTICUM - MODULE 3: POLYMORPHISM & ABSTRACTION ===");

        System.out.println("--- Testing ItemType & BulletType Enums ---");
        System.out.print("Available Item Types: ");
        for (ItemType type : ItemType.values()) {
            System.out.print(type + " ");
        }
        System.out.println();

        System.out.print("Available Bullet Types: ");
        for (BulletType type : BulletType.values()) {
            System.out.print(type + " ");
        }
        System.out.println();

        List<GameObject> entities = new ArrayList<>();
        Player player3 = new Player(100, 100, "Reimu Hakurei", 100, 15, 3);
        Fairy fairy3 = new Fairy(100, 100, "Stage 1 Fairy", 20);
        Boss boss3 = new Boss(200, 200, "Cirno", 150);
        Item item3 = new Item(300, 300, ItemType.POWER);

        entities.add(fairy3);
        entities.add(boss3);
        entities.add(item3);

        System.out.println("\n--- Testing Iterative Update on List<GameObject> entities ---");
        System.out.println("Initial Item Y: " + item3.getY());
        for (GameObject entity : entities) {
            entity.update(0.5f);
        }
        System.out.println("Item Y after entity.update(0.5s): " + item3.getY());

        System.out.println("\n--- Testing Polymorphic Collision Information ---");
        for (GameObject entity : entities) {
            System.out.println("Simulating collision at (" + player3.getX() + ", " + player3.getY() + ") (" + entity.getClass().getSimpleName() + " position):");
            player3.onCollision(entity);
            player3.setX(entity.getX());
            player3.setY(entity.getY());
        }

        System.out.println("\n=== Module 3 Test Completed Successfully ===");


        // ==========================================
        // MODULE 4: COLLECTIONS, GENERICS & ITERATORS
        // ==========================================
        System.out.println("\n\n=== TOUHOU OOP PRACTICUM - MODULE 4: COLLECTIONS, GENERICS & ITERATORS ===");

        List<GameObject> gameEntities = new ArrayList<>();

        Player player4 = new Player(200, 50, "Reimu Hakurei", 100, 15, 3);
        Fairy targetFairy = new Fairy(200, 200, "Target Fairy", 20);

        gameEntities.add(player4);
        gameEntities.add(targetFairy);

        System.out.println("\n--- Initial Entities in List<GameObject> ---");
        System.out.println("Entities Count: " + gameEntities.size());

        System.out.println("\n--- Turn 1: Player Fires Bullet ---");
        Bullet bullet1 = new Bullet(200, 150, 400f, BulletType.AMULET, 25);
        gameEntities.add(bullet1);

        System.out.println("Entities Count before update: " + gameEntities.size());

        System.out.println("\n--- Updating frame: Bullet moves upward & collides with Fairy ---");
        for (int i = 0; i < gameEntities.size(); i++) {
            for (int j = i + 1; j < gameEntities.size(); j++) {
                GameObject e1 = gameEntities.get(i);
                GameObject e2 = gameEntities.get(j);
                if (e1.getCoreHitbox().overlaps(e2.getCoreHitbox())) {
                    e1.onCollision(e2);
                    e2.onCollision(e1);
                }
            }
        }

        runner.updateAndClean(gameEntities, 0.125f, 640, 480);

        System.out.println("Entities Count after generic Iterator cleanup: " + gameEntities.size());

        System.out.println("\n=== Module 4 Test Completed Successfully ===");


        // ==========================================
        // MODULE 7: OBJECT POOL & MEDIATOR PATTERN
        // ==========================================
        System.out.println("\n\n=== TOUHOU OOP PRACTICUM - MODULE 7: OBJECT POOL & MEDIATOR ===");

        BulletManager bulletManager = new BulletManager();
        CollisionReferee referee = new CollisionReferee();

        Player player7 = new Player(200, 50, "Reimu Hakurei", 100, 15, 3);

        Fairy enemyFairy = new Fairy(200, 200, "Stage 1 Fairy", 20);
        List<GameObject> m7Entities = new ArrayList<>();
        m7Entities.add(enemyFairy);

        System.out.println("\n--- Testing Object Pool ---");
        System.out.println("Initial Player Pool Size: " + bulletManager.getPlayerPoolSize());
        System.out.println("Initial Active Player Bullets: " + bulletManager.getActivePlayerBullets().size());

        player7.shootBullet(bulletManager);
        System.out.println("Active Player Bullets after 1st shoot: " + bulletManager.getActivePlayerBullets().size());

        bulletManager.update(0.26f, 640, 480);

        System.out.println("\n--- Testing Mediator Pattern Collision Resolution ---");
        referee.resolveCollisions(player7, m7Entities, bulletManager);

        bulletManager.update(0.01f, 640, 480);

        System.out.println("\n--- Object Pool Recycling Check ---");
        System.out.println("Active Player Bullets after hit & recycling: " + bulletManager.getActivePlayerBullets().size());
        System.out.println("Recycled Player Pool Size: " + bulletManager.getPlayerPoolSize());

        player7.shootBullet(bulletManager);
        System.out.println("Active Player Bullets after 2nd shoot (Re-used pooled instance): " + bulletManager.getActivePlayerBullets().size());
        System.out.println("Player Pool Size after re-using: " + bulletManager.getPlayerPoolSize());

        System.out.println("\n--- Testing Graze vs Core Hitbox Detection ---");
        Bullet enemyBullet = bulletManager.spawnEnemyBullet(190, 50, 0, 0, 15);
        long scoreBeforeGraze = player7.getScore();
        referee.resolveCollisions(player7, m7Entities, bulletManager);
        System.out.println("Graze score added (+50 pts): " + (player7.getScore() - scoreBeforeGraze == 50));
        System.out.println("Player Graze Counter: " + player7.getGrazeCount());

        System.out.println("\n=== Module 7 Test Completed Successfully ===");


        // ==========================================
        // MODULE 8: COMMAND PATTERN & OBSERVER PATTERN
        // ==========================================
        System.out.println("\n\n=== TOUHOU OOP PRACTICUM - MODULE 8: COMMAND & OBSERVER PATTERNS ===");

        Player player8 = new Player(200, 50, "Reimu Hakurei", 100, 15, 3);
        BulletManager bManager8 = new BulletManager();
        GameHUD hud = new GameHUD();

        System.out.println("\n--- Testing Observer Pattern Registration ---");
        player8.registerObserver(hud);
        System.out.println("HUD Received Initial Score: " + hud.getScore());
        System.out.println("HUD Received Initial HP:    " + hud.getHp());
        System.out.println("HUD Received Initial Bombs: " + hud.getSpellCards());
        System.out.println("HUD Received Initial Graze: " + hud.getGrazeCount());

        System.out.println("\n--- Testing Command Pattern Execution ---");
        Command moveUp = new MoveCommand(0, 0.5f);
        moveUp.execute(player8, bManager8);
        System.out.println("Player position after MoveCommand: (" + player8.getX() + ", " + player8.getY() + ")");

        Command focusOn = new FocusCommand(true);
        focusOn.execute(player8, bManager8);
        System.out.println("Player Focus Mode Enabled: " + player8.isFocused());

        Command shoot = new ShootCommand();
        shoot.execute(player8, bManager8);
        System.out.println("Active Player Bullets after ShootCommand: " + bManager8.getActivePlayerBullets().size());

        bManager8.spawnEnemyBullet(200, 300, 0, -100, 15);
        bManager8.spawnEnemyBullet(250, 350, 0, -100, 15);
        System.out.println("Active Enemy Bullets before Bomb: " + bManager8.getActiveEnemyBullets().size());

        Command bomb = new BombCommand();
        bomb.execute(player8, bManager8);

        System.out.println("Active Enemy Bullets after BombCommand: " + bManager8.getActiveEnemyBullets().size());
        System.out.println("HUD Received Updated Bombs: " + hud.getSpellCards());

        System.out.println("\n--- Testing Observer Notification on Graze & Item Collection ---");
        player8.addGraze();
        System.out.println("HUD Received Updated Graze: " + hud.getGrazeCount());

        Item pItem = new Item(200, 50, ItemType.POWER);
        player8.collectItem(pItem);
        System.out.println("HUD Received Updated Score: " + hud.getScore());

        System.out.println("\n=== Module 8 Test Completed Successfully ===");


        // ==========================================
        // MODULE 9: STRATEGY PATTERN & DEPENDENCY INJECTION
        // ==========================================
        System.out.println("\n\n=== TOUHOU OOP PRACTICUM - MODULE 9: STRATEGY PATTERN & DEPENDENCY INJECTION ===");

        EntityShooter shooterPlayer = new Player(200, 50, "Reimu Hakurei", 100, 15, 3);
        EntityShooter shooterBoss = new Boss("Cirno", 150);
        BulletManager bManager9 = new BulletManager();

        System.out.println("\n--- Testing EntityShooter Base Class Hierarchy ---");
        System.out.println("Player is instance of EntityShooter: " + (shooterPlayer instanceof EntityShooter));
        System.out.println("Boss is instance of EntityShooter:   " + (shooterBoss instanceof EntityShooter));

        System.out.println("\n--- Testing Runtime Dependency Injection Strategy Swapping ---");
        shooterBoss.setShootingPattern(new RingShot(200f, 8, 15));
        shooterBoss.shootBullet(bManager9);
        System.out.println("Active Enemy Bullets after RingShot: " + bManager9.getActiveEnemyBullets().size());

        bManager9.clearEnemyBullets();

        shooterBoss.setShootingPattern(new SpreadShot(200f, 3, 30f));
        shooterBoss.shootBullet(bManager9);
        System.out.println("Active Enemy Bullets after SpreadShot: " + bManager9.getActiveEnemyBullets().size());

        System.out.println("\n--- Testing Entity Movement Patterns ---");
        shooterBoss.setMovementPattern(new LinearEntityMovement(50f, -20f));
        float bossInitialX = shooterBoss.getX();
        float bossInitialY = shooterBoss.getY();
        shooterBoss.update(1.0f);
        System.out.println("Boss Position after 1s LinearEntityMovement: (" + shooterBoss.getX() + ", " + shooterBoss.getY() + ")");
        System.out.println("Position updated correctly: " + (shooterBoss.getX() == bossInitialX + 50f && shooterBoss.getY() == bossInitialY - 20f));

        shooterBoss.setMovementPattern(new ZigzagEntityMovement(-30f, 5f, 50f));
        shooterBoss.update(0.5f);
        System.out.println("Boss Position after 0.5s ZigzagEntityMovement: (" + shooterBoss.getX() + ", " + shooterBoss.getY() + ")");

        shooterBoss.setMovementPattern(new TargetPointMovement(200f, 400f, 100f));
        shooterBoss.update(1.0f);
        System.out.println("Boss Position after 1s TargetPointMovement towards (200, 400): (" + shooterBoss.getX() + ", " + shooterBoss.getY() + ")");

        System.out.println("\n--- Testing Bullet Movement Trajectory Strategies ---");
        Bullet sineBullet = new Bullet(200, 200, 200f, BulletType.DANMAKU, 10);
        sineBullet.setMovementPattern(new SineWaveBulletMovement(5f, 40f));
        float initialSineX = sineBullet.getX();
        sineBullet.update(0.5f);
        System.out.println("Bullet X after SineWaveBulletMovement: " + sineBullet.getX() + " (oscillated off base line)");

        Bullet homingBullet = new Bullet(100, 100, 200f, BulletType.AMULET, 10);
        homingBullet.setMovementPattern(new HomingBulletMovement(shooterBoss, 90f));
        homingBullet.update(0.5f);
        System.out.println("Homing Bullet Angle steered toward Boss: " + homingBullet.getAngle() + "°");

        System.out.println("\n=== Module 9 Test Completed Successfully ===");
    }

    // Non-static (Instance) Generic Method with Bounded Type Parameter <T extends GameObject>
    public <T extends GameObject> void updateAndClean(List<T> list, float delta, float screenWidth, float screenHeight) {
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            T entity = iterator.next();
            entity.update(delta);

            if (entity.isOffScreen(screenWidth, screenHeight) || entity.isDestroyed()) {
                if (entity.isOffScreen(screenWidth, screenHeight)) {
                    System.out.println("Removed via Generic Iterator (off-screen): " + entity.getClass().getSimpleName() + " at y=" + entity.getY());
                } else {
                    System.out.println("Removed via Generic Iterator (destroyed): " + entity.getClass().getSimpleName());
                }
                iterator.remove(); // Safe removal using Iterator!
            }
        }
    }
}

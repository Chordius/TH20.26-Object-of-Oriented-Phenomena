# Touhou OOP Practicum - Module 7: Object Pool & Mediator Pattern

## 🎯 Purpose & Design Framework (Tujuan & Modul Design)

Module 7 introduces students to high-performance memory management and decoupled collision interaction in game development through two core Object-Oriented Design Patterns:

1. **Object Pool Pattern**: Eliminates garbage collection stutter caused by high-frequency bullet creation (`new Bullet()`) by recycling inactive bullet instances from a `Queue` pool.
2. **Mediator Pattern**: Centralizes collision detection and object interaction rules into a single Mediator class (`CollisionReferee`), decoupling entities (`Player`, `Enemy`, `Item`, `Bullet`) from each other.
3. **Touhou Core vs. Graze Mechanics**: Introduces dual-box collision checking (**Core Hitbox** for damage vs. **Graze Hitbox** for score bonuses).

---

## 👨‍💻 Student Implementation Expectations (Tugas Praktikan)

Practikans (students) are expected to implement the following core components in this module:

### 1. Object Pool in `BulletManager.java` (`com.netlab.frontend.systems`)
- Manage `playerBulletPool` & `enemyBulletPool` queues (`Queue<Bullet>`).
- Implement `spawnPlayerBullet(x, y, vx, vy, damage)`:
  - Poll an inactive bullet from `playerBulletPool` if available (`bullet.init(...)`).
  - Otherwise, instantiate a new bullet via `EntityFactory.createPlayerBullet(...)`.
- Implement `update(delta, screenWidth, screenHeight)`:
  - Update active bullet movement using velocity components (`vx`, `vy`).
  - Use an `Iterator` to recycle offscreen or destroyed bullets back into the pool queue (`iterator.remove()` and `pool.offer(bullet)`).

### 2. Player Shooting Integration in `Player.java`
- Update `player.shootBullet(bulletManager)` to directly invoke the Object Pool:
  ```java
  public void shootBullet(BulletManager bulletManager) {
      bulletManager.spawnPlayerBullet(x + width / 2 - 8, y + height, 0, 400f, 10 + power);
  }
  ```

### 3. Collision Mediator in `CollisionReferee.java` (`com.netlab.frontend.systems`)
- Implement `resolveCollisions(player, entities, bulletManager)`:
  - **Player Bullets vs. Enemies**: Core hitbox overlap $\rightarrow$ deal damage, update player score, mark bullet destroyed for pool recycling.
  - **Enemy Bullets vs. Player**:
    - **Core Hitbox Overlap**: Deal player damage (`player.takeDamage(...)`), mark bullet destroyed for pool recycling.
    - **Graze Hitbox Overlap** (without core hit): Mark `bullet.setGrazed(true)` and award **`+50 Graze score`** bonus.
  - **Player vs. Items**: Collect item on overlap (`player.collectItem(item)`).

### 4. Game Loop Integration in `Main.java`
- Instantiate `BulletManager` and `CollisionReferee`.
- Call `bulletManager.update(...)`, `collisionReferee.resolveCollisions(...)`, and `bulletManager.render(batch)` inside `render()`.

---

## 🛠️ Pre-Made Framework Components (Framework Bawaan untuk Asisten Laboratory)

To keep the practicum focused on Object Pooling and Mediator logic without overwhelming students:

* **`SpreadShot.java` (Strategy Pattern)**: A pre-made plug-and-play shooting strategy assigned to **Boss Cirno** (`new SpreadShot(200f, 3, 30f)`). Cirno automatically fires 3-way fan bullet barrages on a 1.5-second timer. Students are **not** expected to write `ShootingPattern` or `SpreadShot` in this module.

---

## 🧪 Verification & Testing Commands

To verify compilation and run the full practicum test suite (Modules 1 through 7):

```bash
./gradlew core:runTest
```

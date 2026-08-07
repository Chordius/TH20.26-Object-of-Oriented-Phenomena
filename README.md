# Touhou OOP Practicum - Module 9: Strategy Pattern & Dependency Injection

## 🎯 Purpose & Design Framework (Tujuan & Modul Design)

Module 9 explores interchangeable algorithms and runtime behavior swapping using two core Object-Oriented software design concepts:

1. **Strategy Pattern**: Encapsulates shooting algorithms (`ShootingPattern`), bullet trajectory behaviors (`BulletMovementPattern`), and entity movement behaviors (`EntityMovementPattern`) into independent family classes that can be swapped dynamically.
2. **Dependency Injection**: Injects strategy implementations into entities (`EntityShooter`) and bullets via constructor or setter methods (`setShootingPattern`, `setMovementPattern`), decoupling entity definitions from specific movement and shooting implementations.

---

## 👨‍💻 Student Implementation Expectations (Tugas Praktikan)

Practikans (students) are expected to implement and refactor the following core components in this module:

### 1. Entity Base Class Refactoring (`EntityShooter.java`)
- **`EntityShooter` Abstract Class**:
  - Extends `GameObject`.
  - Encapsulates `ShootingPattern` and `EntityMovementPattern` strategies.
  - Implements Dependency Injection setters: `setShootingPattern(pattern)` and `setMovementPattern(pattern)`.
  - Both `Player` and `Enemy` (and its subclasses `Fairy`, `Boss`) inherit from `EntityShooter`.

### 2. Strategy Patterns (`com.netlab.frontend.objects.patterns`)

#### A. **Shooting Strategies** (`com.netlab.frontend.objects.patterns.shootingStrategy`)
- **`ShootingPattern`**: `execute(originX, originY, bulletManager, isPlayer)`
  - `LinearShot`: Fires straight parallel bullet streams.
  - `SpreadShot`: Fires angled fan/cone spread bullet patterns.
  - `RingShot`: Fires a 360-degree radial ring bullet burst.

#### B. **Bullet Movement Strategies** (`com.netlab.frontend.objects.patterns.bulletStrategy`)
- **`BulletMovementPattern`**: `move(bullet, delta)`
  - `LinearBulletMovement`: Standard constant velocity straight movement.
  - `SineWaveBulletMovement`: Oscillating sine wave bullet trajectory.
  - `HomingBulletMovement`: Steers bullet toward target entity.

#### C. **Entity Movement Strategies** (`com.netlab.frontend.objects.patterns.entityStrategy`)
- **`EntityMovementPattern`**: `move(entity, delta)`
  - `FixedMovement`: Stationary/idle stance.
  - `LinearEntityMovement`: Straight velocity vector movement.
  - `ZigzagEntityMovement`: Oscillating zigzag stage enemy entry pattern.

---

## 🛠️ Pre-Made Framework Components

To keep the practicum focused on Strategy Pattern and Dependency Injection architecture:

* **`BulletManager.java` & Object Pool**: Pre-made bullet spawning and recycling queue system.
* **`CollisionReferee.java`**: Pre-made Mediator collision detector.

---

## 🧪 Verification & Testing Commands

To verify compilation and run the full practicum test suite (Modules 1 through 9):

```bash
./gradlew core:runTest
```

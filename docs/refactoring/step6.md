# Step 6 — 유닛 테스트 보완 및 Car 도메인 테스트 추가

## 목표

PLAN.md의 Step 6 목표인 "CompatibilityValidator 5가지 제한 조건 검증"은 Step 3에서 이미 완료되었다.
이 단계에서는 아직 테스트가 없는 `Car` 도메인 모델의 테스트를 추가하고,
전체 테스트 스위트를 한 번에 실행해 리팩토링 완료를 확인한다.

---

## 현재 테스트 현황

| 클래스 | 테스트 파일 | 상태 |
|---|---|---|
| `ValidationResult` | `ValidationResultTest.java` | 완료 (Step 3) |
| `CompatibilityValidator` | `CompatibilityValidatorTest.java` | 완료 (Step 3) |
| `ConsoleUI` | `ConsoleUITest.java` | 완료 (Step 4) |
| `AssemblyProcess` | `AssemblyProcessTest.java` | 완료 (Step 5) |
| `Car` | 없음 | **추가 필요** |

---

## 추가할 파일

### `test/.../domain/CarTest.java`

**커버할 케이스:**

| 케이스 | 검증 내용 |
|---|---|
| 정상 생성 | 4개 필드가 모두 getter로 올바르게 반환된다 |
| engine = null 허용 | null engine으로 생성 시 예외 없이 생성되고 `isEngineBroken() == true` |
| engine != null | 정상 engine으로 생성 시 `isEngineBroken() == false` |
| carType = null | `IllegalArgumentException` 발생 |
| brake = null | `IllegalArgumentException` 발생 |
| steering = null | `IllegalArgumentException` 발생 |

**구현:**
```java
package org.example.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {

    @Test
    @DisplayName("유효한 인자로 Car를 생성하면 각 getter가 올바른 값을 반환한다")
    void constructor_validArgs_gettersReturnCorrectValues() {
        Car car = new Car(CarType.SEDAN, Engine.GM, Brake.MANDO, Steering.BOSCH);

        assertEquals(CarType.SEDAN, car.getCarType());
        assertEquals(Engine.GM,     car.getEngine());
        assertEquals(Brake.MANDO,   car.getBrake());
        assertEquals(Steering.BOSCH, car.getSteering());
    }

    @Test
    @DisplayName("engine에 null을 전달하면 예외 없이 생성되고 isEngineBroken()이 true를 반환한다")
    void constructor_nullEngine_isEngineBrokenReturnsTrue() {
        Car car = new Car(CarType.SEDAN, null, Brake.MANDO, Steering.BOSCH);

        assertTrue(car.isEngineBroken());
        assertNull(car.getEngine());
    }

    @Test
    @DisplayName("engine이 null이 아니면 isEngineBroken()이 false를 반환한다")
    void constructor_nonNullEngine_isEngineBrokenReturnsFalse() {
        Car car = new Car(CarType.SEDAN, Engine.GM, Brake.MANDO, Steering.BOSCH);

        assertFalse(car.isEngineBroken());
    }

    @Test
    @DisplayName("carType에 null을 전달하면 IllegalArgumentException이 발생한다")
    void constructor_nullCarType_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
            () -> new Car(null, Engine.GM, Brake.MANDO, Steering.BOSCH));
    }

    @Test
    @DisplayName("brake에 null을 전달하면 IllegalArgumentException이 발생한다")
    void constructor_nullBrake_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
            () -> new Car(CarType.SEDAN, Engine.GM, null, Steering.BOSCH));
    }

    @Test
    @DisplayName("steering에 null을 전달하면 IllegalArgumentException이 발생한다")
    void constructor_nullSteering_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
            () -> new Car(CarType.SEDAN, Engine.GM, Brake.MANDO, null));
    }
}
```

---

## 완료 조건

- [ ] `src/test/java/org/example/domain/CarTest.java` 파일이 존재한다.
- [ ] `Car` 클래스의 모든 분기(정상 생성, null engine, null 검증 3개)가 테스트된다.
- [ ] `gradlew test` 실행 시 전체 테스트(58개 이상)가 모두 통과한다.
- [ ] PLAN.md 전체 체크리스트가 완료 표시된다.

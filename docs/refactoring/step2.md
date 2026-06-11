# Step 2 — Car 도메인 모델 생성

## 목표

선택된 부품 조합을 담는 불변 객체 `Car`를 도입한다.
이 단계에서도 `Assemble.java`는 수정하지 않는다. 새 파일만 추가한다.

---

## 생성할 파일

### `domain/Car.java`

**설계 원칙:**
- 필드는 Step 1에서 만든 enum 4개(`CarType`, `Engine`, `Brake`, `Steering`)를 사용한다.
- 생성 후 상태가 바뀌지 않도록 setter를 제공하지 않는다.
- `Engine`은 nullable — `null`이면 고장난 엔진을 의미한다. (`Car` 자체는 생성 가능, 동작 여부는 호출부에서 판단)

**필드:**

| 필드 | 타입 | nullable |
|---|---|---|
| `carType` | `CarType` | No |
| `engine` | `Engine` | Yes (고장난 엔진) |
| `brake` | `Brake` | No |
| `steering` | `Steering` | No |

**생성자 검증:**
- `carType`, `brake`, `steering`이 `null`이면 `IllegalArgumentException`을 던진다.
- `engine`은 `null` 허용 (고장난 엔진 케이스).

**메서드:**
- `isEngineBroken()` — `engine == null`이면 `true` 반환. 호출부에서 별도 분기 처리에 사용.

**구현:**
```java
package org.example.domain;

public class Car {

    private final CarType carType;
    private final Engine engine;   // null = 고장난 엔진
    private final Brake brake;
    private final Steering steering;

    public Car(CarType carType, Engine engine, Brake brake, Steering steering) {
        if (carType == null)  throw new IllegalArgumentException("carType is required");
        if (brake == null)    throw new IllegalArgumentException("brake is required");
        if (steering == null) throw new IllegalArgumentException("steering is required");

        this.carType  = carType;
        this.engine   = engine;
        this.brake    = brake;
        this.steering = steering;
    }

    public CarType getCarType()   { return carType; }
    public Engine  getEngine()    { return engine; }
    public Brake   getBrake()     { return brake; }
    public Steering getSteering() { return steering; }

    public boolean isEngineBroken() { return engine == null; }
}
```

---

## 완료 조건

- [ ] `src/main/java/org/example/domain/Car.java` 파일이 존재한다.
- [ ] `carType`, `brake`, `steering`에 `null` 전달 시 `IllegalArgumentException`이 발생한다.
- [ ] `engine`에 `null` 전달 시 예외 없이 생성되고 `isEngineBroken()`이 `true`를 반환한다.
- [ ] `Assemble.java`는 변경되지 않았다.
- [ ] `gradlew build`가 성공한다.

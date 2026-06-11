# Step 3 — CompatibilityValidator 추출

## 목표

`Assemble.java`의 `isValidCheck()` / `testProducedCar()` 에 흩어진 호환성 검증 로직을
`ValidationResult`와 `CompatibilityValidator` 두 클래스로 분리한다.
이 단계에서도 `Assemble.java`는 수정하지 않는다. 새 파일만 추가한다.

---

## 생성할 파일 2개

모두 패키지 `org.example.validator`에 위치한다.

---

### 1. `validator/ValidationResult.java`

검증 결과(통과 여부 + 실패 이유)를 담는 불변 객체.

**설계:**
- `boolean passed` — 호환성 검사 통과 여부
- `String reason` — 실패 이유 (`passed == true`이면 `null`)
- 직접 생성자 대신 정적 팩토리 메서드만 노출한다.

**구현:**
```java
package org.example.validator;

public class ValidationResult {

    private final boolean passed;
    private final String reason;

    private ValidationResult(boolean passed, String reason) {
        this.passed = passed;
        this.reason = reason;
    }

    public static ValidationResult pass() {
        return new ValidationResult(true, null);
    }

    public static ValidationResult fail(String reason) {
        return new ValidationResult(false, reason);
    }

    public boolean isPassed() { return passed; }
    public String getReason() { return reason; }
}
```

---

### 2. `validator/CompatibilityValidator.java`

`Car` 객체를 받아 5가지 호환성 규칙을 검사하고 `ValidationResult`를 반환한다.

**검증 규칙 (기존 `Assemble.java` 기준):**

| 순서 | 조건 | 실패 메시지 |
|---|---|---|
| 1 | `CarType.SEDAN` + `Brake.CONTINENTAL` | `"Sedan에는 Continental제동장치 사용 불가"` |
| 2 | `CarType.SUV` + `Engine.TOYOTA` | `"SUV에는 TOYOTA엔진 사용 불가"` |
| 3 | `CarType.TRUCK` + `Engine.WIA` | `"Truck에는 WIA엔진 사용 불가"` |
| 4 | `CarType.TRUCK` + `Brake.MANDO` | `"Truck에는 Mando제동장치 사용 불가"` |
| 5 | `Brake.BOSCH` + `Steering != Steering.BOSCH` | `"Bosch제동장치에는 Bosch조향장치 이외 사용 불가"` |

**고장난 엔진 처리:**
- `car.isEngineBroken() == true`이면 규칙 2·3을 건너뛴다 (engine이 null이므로 비교 불가).
- 고장난 엔진 자체는 호환성 규칙이 아니라 실행 시 별도 처리 대상이므로 `validate()`는 `pass()`를 반환한다.

**구현:**
```java
package org.example.validator;

import org.example.domain.Brake;
import org.example.domain.Car;
import org.example.domain.CarType;
import org.example.domain.Engine;
import org.example.domain.Steering;

public class CompatibilityValidator {

    public ValidationResult validate(Car car) {
        if (car.getCarType() == CarType.SEDAN && car.getBrake() == Brake.CONTINENTAL)
            return ValidationResult.fail("Sedan에는 Continental제동장치 사용 불가");

        if (!car.isEngineBroken()) {
            if (car.getCarType() == CarType.SUV && car.getEngine() == Engine.TOYOTA)
                return ValidationResult.fail("SUV에는 TOYOTA엔진 사용 불가");

            if (car.getCarType() == CarType.TRUCK && car.getEngine() == Engine.WIA)
                return ValidationResult.fail("Truck에는 WIA엔진 사용 불가");
        }

        if (car.getCarType() == CarType.TRUCK && car.getBrake() == Brake.MANDO)
            return ValidationResult.fail("Truck에는 Mando제동장치 사용 불가");

        if (car.getBrake() == Brake.BOSCH && car.getSteering() != Steering.BOSCH)
            return ValidationResult.fail("Bosch제동장치에는 Bosch조향장치 이외 사용 불가");

        return ValidationResult.pass();
    }
}
```

---

## 완료 조건

- [ ] `src/main/java/org/example/validator/ValidationResult.java` 파일이 존재한다.
- [ ] `src/main/java/org/example/validator/CompatibilityValidator.java` 파일이 존재한다.
- [ ] 5가지 실패 메시지가 기존 `Assemble.java`의 텍스트와 정확히 일치한다.
- [ ] `car.isEngineBroken() == true`일 때 `validate()`가 `pass()`를 반환한다.
- [ ] `Assemble.java`는 변경되지 않았다.
- [ ] `gradlew build`가 성공한다.

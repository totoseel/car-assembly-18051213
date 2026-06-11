# Step 1 — 도메인 Enum 생성

## 목표

`Assemble.java`에 int 상수로 하드코딩된 차량 타입·부품 값을 각각 독립된 enum 클래스로 분리한다.
이 단계에서 `Assemble.java`는 수정하지 않는다. 새 파일만 추가한다.

---

## 생성할 파일 4개

모두 패키지 `org.example.domain`에 위치한다.

---

### 1. `CarType.java`

기존 상수:
```java
private static final int SEDAN = 1, SUV = 2, TRUCK = 3;
```

메뉴 텍스트 (showCarTypeMenu 기준):
```
1. Sedan
2. SUV
3. Truck
```

구현:
```java
package org.example.domain;

public enum CarType {
    SEDAN("Sedan"),
    SUV("SUV"),
    TRUCK("Truck");

    public final String label;

    CarType(String label) {
        this.label = label;
    }
}
```

---

### 2. `Engine.java`

기존 상수:
```java
private static final int GM = 1, TOYOTA = 2, WIA = 3;
// 4번 고장난 엔진은 enum에 포함하지 않음 → null로 별도 처리
```

메뉴 텍스트 (showEngineMenu 기준):
```
1. GM
2. TOYOTA
3. WIA
4. 고장난 엔진  ← enum 미포함, ConsoleUI에서 null 반환 예정 (Step 4)
```

구현:
```java
package org.example.domain;

public enum Engine {
    GM("GM"),
    TOYOTA("TOYOTA"),
    WIA("WIA");

    public final String label;

    Engine(String label) {
        this.label = label;
    }
}
```

---

### 3. `Brake.java`

기존 상수:
```java
private static final int MANDO = 1, CONTINENTAL = 2, BOSCH_B = 3;
```

메뉴 텍스트 (showBrakeMenu 기준):
```
1. MANDO
2. CONTINENTAL
3. BOSCH
```

구현:
```java
package org.example.domain;

public enum Brake {
    MANDO("MANDO"),
    CONTINENTAL("CONTINENTAL"),
    BOSCH("BOSCH");

    public final String label;

    Brake(String label) {
        this.label = label;
    }
}
```

---

### 4. `Steering.java`

기존 상수:
```java
private static final int BOSCH_S = 1, MOBIS = 2;
```

메뉴 텍스트 (showSteeringMenu 기준):
```
1. BOSCH
2. MOBIS
```

구현:
```java
package org.example.domain;

public enum Steering {
    BOSCH("BOSCH"),
    MOBIS("MOBIS");

    public final String label;

    Steering(String label) {
        this.label = label;
    }
}
```

---

## 완료 조건

- [ ] `src/main/java/org/example/domain/` 디렉토리에 4개 파일이 존재한다.
- [ ] 각 enum의 값 순서가 기존 int 상수의 번호(1, 2, 3…)와 일치한다.
- [ ] `Assemble.java`는 변경되지 않았다.
- [ ] `gradlew build`가 성공한다 (컴파일 에러 없음).

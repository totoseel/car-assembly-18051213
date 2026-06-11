# 리팩토링 단계별 계획

> 기반 문서: PRD.md  
> 원칙: 각 단계가 완료된 후 `gradlew run`이 기존과 동일하게 동작해야 한다.

---

## Step 1. 도메인 Enum 생성

**목표:** int 상수로 하드코딩된 부품·차량 타입을 enum으로 교체한다.

생성할 파일:
- `domain/CarType.java` — SEDAN, SUV, TRUCK (label 필드 포함)
- `domain/Engine.java` — GM, TOYOTA, WIA (label 필드 포함) / BROKEN은 포함하지 않음
- `domain/Brake.java` — MANDO, CONTINENTAL, BOSCH (label 필드 포함)
- `domain/Steering.java` — BOSCH, MOBIS (label 필드 포함)

조건:
- 각 enum은 메뉴에 표시할 `String label` 필드와 생성자를 갖는다.
- 기존 `Assemble.java`는 아직 수정하지 않는다.

---

## Step 2. Car 도메인 모델 생성

**목표:** 선택된 부품 조합을 담는 불변 객체를 도입한다.

생성할 파일:
- `domain/Car.java`
  - 필드: `CarType`, `Engine`, `Brake`, `Steering`
  - 생성자에서 `Engine`이 null이면 `IllegalArgumentException` (BROKEN 케이스는 별도 처리 예정)
  - getter만 제공, setter 없음

---

## Step 3. CompatibilityValidator 추출

**목표:** `isValidCheck()` / `testProducedCar()` 로직을 독립 클래스로 분리한다.

생성할 파일:
- `validator/ValidationResult.java`
  - 필드: `boolean passed`, `String reason`
  - 정적 팩토리: `pass()`, `fail(String reason)`
- `validator/CompatibilityValidator.java`
  - `ValidationResult validate(Car car)`
  - PRD의 5가지 호환성 규칙을 구현

---

## Step 4. ConsoleUI 분리

**목표:** 콘솔 입출력을 담당하는 클래스를 분리하여 비즈니스 로직과 결합을 끊는다.

생성할 파일:
- `ui/ConsoleUI.java`
  - `CarType selectCarType(Scanner sc)`
  - `Engine selectEngine(Scanner sc)` — 고장난 엔진 선택 시 `null` 반환
  - `Brake selectBrake(Scanner sc)`
  - `Steering selectSteering(Scanner sc)`
  - `void showRunResult(Car car, boolean engineBroken)`
  - `void showTestResult(ValidationResult result)`
  - `void showMenu*()`  각 단계 메뉴 출력 메서드

조건:
- 고장난 엔진(기존 선택지 4번)은 `Engine.BROKEN` enum 값 대신 `null`을 반환하여 예외 처리 흐름으로 유도한다.
- 기존 메뉴 텍스트와 선택 번호 체계를 그대로 유지한다.

---

## Step 5. AssemblyProcess 조율 클래스 생성 및 Assemble.java 교체

**목표:** State Machine 흐름을 `AssemblyProcess`로 이전하고, `main()`을 단순화한다.

작업:
- `AssemblyProcess.java` 생성
  - `ConsoleUI`, `CompatibilityValidator`를 생성자 주입으로 받는다.
  - 기존 `Assemble.java`의 `while(true)` 루프 로직을 이전한다.
  - `static int[] stack` 제거 → `Car` 객체로 상태 관리
- `Main.java` 생성 — `main()`에서 `new AssemblyProcess(...).run()`만 호출
- `Assemble.java` 삭제

---

## Step 6. 유닛 테스트 작성

**목표:** `CompatibilityValidator`의 5가지 제한 조건을 검증하는 테스트를 작성한다.

생성할 파일:
- `test/.../validator/CompatibilityValidatorTest.java`
  - PASS 케이스: 유효한 조합 1개 이상
  - FAIL 케이스: 5가지 제한 조건 각각에 대한 실패 케이스

---

## 단계별 체크리스트

| 단계 | 작업 | 완료 |
|---|---|---|
| Step 1 | CarType / Engine / Brake / Steering enum 생성 | [x] |
| Step 2 | Car 불변 객체 생성 | [x] |
| Step 3 | ValidationResult / CompatibilityValidator 생성 | [x] |
| Step 4 | ConsoleUI 분리 | [x] |
| Step 5 | AssemblyProcess 생성, Main.java 생성, Assemble.java 삭제 | [x] |
| Step 6 | CompatibilityValidatorTest 작성 | [x] |

---

## 목표 패키지 구조

```
src/main/java/org/example/
├── Main.java
├── AssemblyProcess.java
├── domain/
│   ├── Car.java
│   ├── CarType.java
│   ├── Engine.java
│   ├── Brake.java
│   └── Steering.java
├── validator/
│   ├── CompatibilityValidator.java
│   └── ValidationResult.java
└── ui/
    └── ConsoleUI.java

src/test/java/org/example/
└── validator/
    └── CompatibilityValidatorTest.java
```

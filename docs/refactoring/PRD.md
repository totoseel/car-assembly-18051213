# 차량 조립 시스템 리팩토링 PRD (초안)

## 배경 및 목적

현재 `Assemble.java` 단일 파일로 구현된 절차지향 코드를 객체지향 구조로 전환한다.
requirement.md에 명시된 아쉬운 점(유지보수 어려움, 확장성 미고려, 테스트 부재)을 해소하는 것이 목표다.

---

## 현재 구조의 문제점

| 문제 | 내용 |
|---|---|
| 절차지향 구조 | 모든 로직이 `Assemble.java` 한 파일의 static 메서드로 집중 |
| 전역 상태 | `static int[] stack[5]`로 선택값을 관리 — 테스트 불가, 부수효과 위험 |
| 확장성 없음 | 차량 타입·부품 추가 시 switch/if 블록을 직접 수정해야 함 |
| 호환성 규칙 하드코딩 | `isValidCheck()`에 조건이 나열식으로 작성됨 |
| 테스트 없음 | JUnit 의존성만 있고 테스트 클래스가 전혀 없음 |
| UI·비즈니스 로직 혼재 | 콘솔 출력과 유효성 검사가 같은 메서드 안에 섞여 있음 |

---

## 리팩토링 범위

### 1. 도메인 모델 분리

현재 int 상수로 표현된 부품과 차량 타입을 독립 타입으로 분리한다.

```
CarType    (SEDAN, SUV, TRUCK)
Engine     (GM, TOYOTA, WIA, BROKEN)
Brake      (MANDO, CONTINENTAL, BOSCH)
Steering   (BOSCH, MOBIS)
```

- 각 항목은 `enum`으로 선언하며, 표시 이름(label)을 필드로 가진다.
- 향후 타입 추가 시 enum 값만 추가하면 되도록 설계한다.

### 2. Car (조립 결과) 모델

선택된 부품 조합을 담는 불변 객체를 도입한다.

```
Car
  - CarType  carType
  - Engine   engine
  - Brake    brake
  - Steering steering
```

### 3. 호환성 검증 분리

`isValidCheck()` 로직을 별도 클래스로 추출한다.

```
CompatibilityValidator
  + validate(Car) : ValidationResult
```

`ValidationResult`는 통과 여부와 실패 이유를 함께 담는다.
호환성 규칙은 requirement.md의 5가지 조건을 그대로 따른다.

### 4. UI 레이어 분리

콘솔 입출력을 담당하는 클래스를 분리하여 비즈니스 로직과 결합을 끊는다.

```
ConsoleUI
  + selectCarType()  : CarType
  + selectEngine()   : Engine
  + selectBrake()    : Brake
  + selectSteering() : Steering
  + showResult(Car, ValidationResult)
```

### 5. 조립 흐름 조율

State Machine 흐름을 조율하는 진입점을 분리한다.

```
AssemblyProcess
  + run()
```

`main()`은 `AssemblyProcess.run()`만 호출한다.

### 6. 유닛 테스트 작성

리팩토링과 함께 테스트를 작성한다. 최소 범위:

- `CompatibilityValidator` — 5가지 제한 조건 각각에 대한 PASS/FAIL 케이스
- `Car` 생성 및 불변성 확인

---

## 목표 패키지 구조 (안)

```
org.example
├── Main.java                  // main() 진입점
├── AssemblyProcess.java       // 조립 흐름 조율
├── domain
│   ├── Car.java
│   ├── CarType.java
│   ├── Engine.java
│   ├── Brake.java
│   └── Steering.java
├── validator
│   ├── CompatibilityValidator.java
│   └── ValidationResult.java
└── ui
    └── ConsoleUI.java
```

---

## 비기능 요구사항

- 기존 사용자 인터페이스(메뉴 텍스트, 선택 번호 체계)는 변경하지 않는다.
- `gradlew run` 실행 결과가 리팩토링 전과 동일하게 동작해야 한다.
- Java 버전은 현행 유지 (Gradle 기본 설정 따름).

---

## 미결 사항 (검토 필요)

- [x] `ConsoleUI`를 인터페이스로 추상화해 향후 GUI/Web 전환을 고려할지 여부
  - ConsoleUI만으로 충분. 
- [x] 호환성 규칙을 코드 외부(설정 파일 등)로 분리할지 여부
  - 설정 파일 분리 안해도 됨
- [x] `Engine.BROKEN`을 도메인 모델에 포함할지, 별도 예외 처리로 다룰지 여부
  - 예외 처리

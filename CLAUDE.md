# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

Windows에서는 `./gradlew` 대신 `gradlew.bat`을 사용하세요.

## Build & Run Commands

```powershell
# 빌드 (컴파일 + 테스트)
.\gradlew.bat build

# 애플리케이션 실행
.\gradlew.bat run

# 테스트만 실행
.\gradlew.bat test

# 빌드 산출물 정리
.\gradlew.bat clean
```

## Architecture

절차지향 단일 파일(`Assemble.java`)을 객체지향 구조로 리팩토링한 결과물이다.
진입점은 `Main.java`이며, 의존성을 조립해 `AssemblyProcess.run()`에 위임한다.

### 패키지 구조

```
org.example
├── Main.java                        # 진입점 — 의존성 조립만 담당
├── AssemblyProcess.java             # State Machine 루프 조율
├── domain/
│   ├── Car.java                     # 선택된 부품 조합 불변 객체
│   ├── CarType.java                 # enum: SEDAN, SUV, TRUCK
│   ├── Engine.java                  # enum: GM, TOYOTA, WIA (null = 고장난 엔진)
│   ├── Brake.java                   # enum: MANDO, CONTINENTAL, BOSCH
│   └── Steering.java                # enum: BOSCH, MOBIS
├── validator/
│   ├── CompatibilityValidator.java  # 5가지 호환성 규칙 검사
│   └── ValidationResult.java        # 검사 결과 (passed + reason)
└── ui/
    └── ConsoleUI.java               # 콘솔 입출력 전담
```

### 레이어 의존 방향

```
Main → AssemblyProcess → ConsoleUI
                       → CompatibilityValidator → Car (domain)
```

### AssemblyProcess 흐름

`run()`의 `while(true)` 루프가 `step`(0~4) 변수로 단계를 관리한다.

| step 상수 | 단계 | 선택지 |
|---|---|---|
| `CAR_TYPE` (0) | 차량 타입 | Sedan(1), SUV(2), Truck(3) |
| `ENGINE` (1) | 엔진 | GM(1), Toyota(2), WIA(3), 고장난엔진(4→null) |
| `BRAKE` (2) | 제동장치 | Mando(1), Continental(2), Bosch(3) |
| `STEERING` (3) | 조향장치 | Bosch(1), Mobis(2) |
| `RUN_TEST` (4) | 실행/테스트 | Run(1), Test(2) |

- 선택값은 `enum.values()[answer - 1]`로 변환해 인스턴스 필드에 저장
- 고장난 엔진(4번)은 `null`로 저장 — `Car.isEngineBroken()`으로 판별
- `RUN_TEST` 단계 진입 시 `new Car(...)`로 조립 후 `CompatibilityValidator.validate()` 호출

### Car 도메인 모델

- `engine`만 nullable (`null` = 고장난 엔진)
- `carType` / `brake` / `steering`은 null 불허 — 생성자에서 `IllegalArgumentException`
- setter 없음 (불변 객체)

### Brake / Steering enum label 규칙

- label은 RUN 결과 출력 기준 (`"Mando"`, `"Bosch"` 등 mixed-case)
- 메뉴 출력 시 `label.toUpperCase()` 사용 → 기존 UI 텍스트 유지

## Business Rules

**부품 호환성 제한 조건 (`CompatibilityValidator`):**

| 조건 | 이유 |
|---|---|
| Sedan + Continental 제동장치 → 불가 | Continental은 Sedan용 제동장치를 생산하지 않음 |
| SUV + Toyota 엔진 → 불가 | Toyota는 SUV용 엔진을 생산하지 않음 |
| Truck + WIA 엔진 → 불가 | WIA는 Truck용 엔진을 생산하지 않음 |
| Truck + Mando 제동장치 → 불가 | Mando는 Truck용 제동장치를 생산하지 않음 |
| Bosch 제동장치 → Bosch 조향장치 필수 | 타사 제품과 호환되지 않음 |

고장난 엔진(`engine == null`)일 때는 엔진 관련 규칙(SUV+TOYOTA, TRUCK+WIA)을 건너뛴다.

## Testing

테스트 파일 위치: `src/test/java/org/example/`

| 테스트 클래스 | 대상 | 테스트 수 |
|---|---|---|
| `domain/CarTest` | `Car` 생성자, getter, `isEngineBroken()` | 6개 |
| `validator/ValidationResultTest` | `pass()` / `fail()` 팩토리 | 2개 |
| `validator/CompatibilityValidatorTest` | 5가지 호환성 규칙 PASS/FAIL | 10개 |
| `ui/ConsoleUITest` | 메뉴 출력, 입력, 범위 검증, 결과 출력 | 28개 |
| `AssemblyProcessTest` | 루프 전체 흐름 (exit, 에러, 뒤로가기, RUN/Test) | 18개 |

- 테스트 메서드 이름: 영어 (`methodName_condition_expectedResult` 패턴)
- 테스트 설명: `@DisplayName` 한국어
- `AssemblyProcess.delay()`는 `protected`로 선언되어 테스트에서 익명 서브클래스로 no-op 오버라이드 가능

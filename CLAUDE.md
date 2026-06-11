# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

Windows에서는 `./gradlew` 대신 `gradlew.bat` 또는 `gradlew`를 사용하세요.

## Architecture

단일 클래스 `src/main/java/org/example/Assemble.java`가 전체 로직을 담당합니다.

**실행 흐름 (State Machine):**
`main()`의 `while(true)` 루프가 `step` 변수로 상태를 관리하며 5단계를 순서대로 진행합니다.

| 상수 (int) | 단계 | 선택지 |
|---|---|---|
| `CarType_Q` (0) | 차량 타입 | Sedan(1), SUV(2), Truck(3) |
| `Engine_Q` (1) | 엔진 | GM(1), Toyota(2), WIA(3), 고장난엔진(4) |
| `BrakeSystem_Q` (2) | 제동장치 | Mando(1), Continental(2), Bosch(3) |
| `SteeringSystem_Q` (3) | 조향장치 | Bosch(1), Mobis(2) |
| `Run_Test` (4) | 실행/테스트 | Run(1), Test(2) |

사용자 선택은 static `int[] stack[5]`에 인덱스(= 상태 상수)로 저장됩니다.

## Business Rules (requirement.md 기반)

**부품 호환성 제한 조건:**

| 제한 | 이유 |
|---|---|
| Sedan + Continental 제동장치 → 불가 | Continental은 Sedan용 제동장치를 생산하지 않음 |
| SUV + Toyota 엔진 → 불가 | Toyota는 SUV용 엔진을 생산하지 않음 |
| Truck + WIA 엔진 → 불가 | WIA는 Truck용 엔진을 생산하지 않음 |
| Truck + Mando 제동장치 → 불가 | Mando는 Truck용 제동장치를 생산하지 않음 |
| Bosch 제동장치 → Bosch 조향장치 필수 | 타사 제품과 호환되지 않음 |

`isValidCheck()`에서 위 규칙을 검사하며, `runProducedCar()`는 실패 시 메시지만 출력하고, `testProducedCar()`는 실패 이유를 구체적으로 출력합니다.

## Known Issues (현재 시스템의 아쉬운 점)

requirement.md에 명시된 개선 필요 사항:
- 절차지향식 코드 구조로 유지보수가 어려움 → 객체지향 리팩토링 필요
- 안전하지 않은 문법 사용
- 확장성 미고려 (차량 타입 추가 등을 고려한 설계 필요)
- 유닛 테스트 없음

# Step 5 — AssemblyProcess 생성 및 Assemble.java 교체

## 목표

`Assemble.java`의 `while(true)` 루프 로직을 `AssemblyProcess`로 이전하고,
`main()`만 담당하는 `Main.java`를 새로 만든다.
완료 후 `Assemble.java`를 삭제한다.

---

## 작업 순서

1. `AssemblyProcess.java` 생성
2. `Main.java` 생성
3. `build.gradle`의 `mainClass` 수정 (`Assemble` → `Main`)
4. `Assemble.java` 삭제
5. `gradlew run`으로 동작 확인

---

## 1. `AssemblyProcess.java` — 패키지 `org.example`

### 필드 및 생성자

```java
private static final int CAR_TYPE    = 0;
private static final int ENGINE      = 1;
private static final int BRAKE       = 2;
private static final int STEERING    = 3;
private static final int RUN_TEST    = 4;

private final ConsoleUI ui;
private final CompatibilityValidator validator;
```

`ConsoleUI`와 `CompatibilityValidator`를 생성자로 주입받는다.

### 상태 관리

기존 `static int[] stack[5]` 대신 인스턴스 필드 4개로 관리한다.
단계별로 선택되면 해당 필드에 저장하고, `Car` 객체는 RUN_TEST 단계 진입 시 조립한다.

```java
private CarType selectedCarType;
private Engine  selectedEngine;   // null = 고장난 엔진
private Brake   selectedBrake;
private Steering selectedSteering;
```

### `run()` 메서드 흐름

기존 `Assemble.java`의 `while(true)` 루프를 그대로 이전한다.
`Scanner`는 `ConsoleUI`가 보유하므로 `AssemblyProcess`는 직접 다루지 않는다.

```
while (true)
  1. ui.clearScreen()
  2. step에 따라 ui.showXxxMenu() 호출
  3. ui.readInput() → "exit"이면 ui.printMessage("바이바이") 후 break
  4. 숫자 파싱 실패 → ui.printError("ERROR :: 숫자만 입력 가능") + delay(800) + continue
  5. ui.isValidRange(step, answer) == false → delay(800) + continue
  6. answer == 0 처리:
       - step == RUN_TEST → step = CAR_TYPE
       - step > CAR_TYPE  → step--
       continue
  7. step별 선택 처리:
       CAR_TYPE  → selectedCarType  = CarType.values()[answer-1]
                   ui.printCarTypeSelected(selectedCarType)
                   delay(800), step = ENGINE
       ENGINE    → selectedEngine = answer == 4 ? null : Engine.values()[answer-1]
                   ui.printEngineSelected(selectedEngine)
                   delay(800), step = BRAKE
       BRAKE     → selectedBrake = Brake.values()[answer-1]
                   ui.printBrakeSelected(selectedBrake)
                   delay(800), step = STEERING
       STEERING  → selectedSteering = Steering.values()[answer-1]
                   ui.printSteeringSelected(selectedSteering)
                   delay(800), step = RUN_TEST
       RUN_TEST  → Car car = new Car(selectedCarType, selectedEngine, selectedBrake, selectedSteering)
                   ValidationResult result = validator.validate(car)
                   answer == 1: ui.showRunResult(car, result) + delay(2000)
                   answer == 2: ui.printMessage("Test...") + delay(1500)
                               ui.showTestResult(result) + delay(2000)
```

### `delay()` 메서드

```java
private void delay(int ms) {
    try {
        Thread.sleep(ms);
    } catch (InterruptedException ignored) {}
}
```

### enum → 선택 번호 변환

기존 `stack[x] = answer` (1-based int) 방식을 enum으로 전환한다.
`answer`는 1-based이고 enum의 `ordinal()`은 0-based이므로 `values()[answer - 1]`로 변환한다.

| 단계 | 변환식 | 예외 |
|---|---|---|
| CAR_TYPE | `CarType.values()[answer - 1]` | 없음 |
| ENGINE | `Engine.values()[answer - 1]` | `answer == 4`이면 `null` |
| BRAKE | `Brake.values()[answer - 1]` | 없음 |
| STEERING | `Steering.values()[answer - 1]` | 없음 |

---

## 2. `Main.java` — 패키지 `org.example`

```java
package org.example;

import org.example.ui.ConsoleUI;
import org.example.validator.CompatibilityValidator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI(new Scanner(System.in));
        CompatibilityValidator validator = new CompatibilityValidator();
        new AssemblyProcess(ui, validator).run();
    }
}
```

---

## 3. `build.gradle` mainClass 수정

```groovy
// 변경 전
mainClass = 'org.example.Assemble'

// 변경 후
mainClass = 'org.example.Main'
```

---

## 4. `Assemble.java` 삭제

`src/main/java/org/example/Assemble.java`를 삭제한다.

---

## 완료 조건

- [ ] `AssemblyProcess.java`가 존재하고, `static` 필드·메서드가 없다.
- [ ] `Main.java`가 존재하고, `main()`에서 `AssemblyProcess.run()`만 호출한다.
- [ ] `build.gradle`의 `mainClass`가 `org.example.Main`으로 변경된다.
- [ ] `Assemble.java`가 삭제된다.
- [ ] `gradlew build`(전체 테스트 포함)가 성공한다.
- [ ] `gradlew run` 실행 시 기존과 동일하게 동작한다.

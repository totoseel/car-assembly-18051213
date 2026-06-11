# Step 4 — ConsoleUI 분리

## 목표

`Assemble.java`에 섞여 있는 콘솔 입출력 로직을 `ConsoleUI`로 분리한다.
이 단계에서도 `Assemble.java`는 수정하지 않는다. 새 파일만 추가한다.

---

## 생성할 파일

### `ui/ConsoleUI.java` — 패키지 `org.example.ui`

`Scanner`를 생성자에서 주입받아 보관한다. 모든 메서드는 인스턴스 메서드.

---

## 메서드 목록 및 상세 명세

### 화면 초기화

```java
public void clearScreen()
```
- `"\033[H\033[2J"` 출력 후 `System.out.flush()` 호출.

---

### 메뉴 출력 메서드 (5개)

```java
public void showCarTypeMenu()
public void showEngineMenu()
public void showBrakeMenu()
public void showSteeringMenu()
public void showRunTestMenu()
```

기존 `Assemble.java` 텍스트를 그대로 옮긴다.

**showCarTypeMenu():**
```
        ______________
       /|            |
  ____/_|_____________|____
 |                      O  |
 '-(@)----------------(@)--'
===============================
어떤 차량 타입을 선택할까요?
1. Sedan
2. SUV
3. Truck
===============================
```

**showEngineMenu():**
```
어떤 엔진을 탑재할까요?
0. 뒤로가기
1. GM
2. TOYOTA
3. WIA
4. 고장난 엔진
===============================
```

**showBrakeMenu():**
```
어떤 제동장치를 선택할까요?
0. 뒤로가기
1. MANDO
2. CONTINENTAL
3. BOSCH
===============================
```

**showSteeringMenu():**
```
어떤 조향장치를 선택할까요?
0. 뒤로가기
1. BOSCH
2. MOBIS
===============================
```

**showRunTestMenu():**
```
멋진 차량이 완성되었습니다.
어떤 동작을 할까요?
0. 처음 화면으로 돌아가기
1. RUN
2. Test
===============================
```

---

### 입력 읽기

```java
public String readInput()
```
- `"INPUT > "` 출력 후 `sc.nextLine().trim()` 반환.

---

### 범위 검증

```java
public boolean isValidRange(int step, int answer)
```

step 값은 `AssemblyProcess`에서 전달하는 단계 상수(0~4)를 그대로 사용.
범위 위반 시 에러 메시지 출력 후 `false` 반환. 통과 시 `true` 반환.

| step | 유효 범위 | 에러 메시지 |
|---|---|---|
| 0 (CarType) | 1 ~ 3 | `"ERROR :: 차량 타입은 1 ~ 3 범위만 선택 가능"` |
| 1 (Engine) | 0 ~ 4 | `"ERROR :: 엔진은 1 ~ 4 범위만 선택 가능"` |
| 2 (Brake) | 0 ~ 3 | `"ERROR :: 제동장치는 1 ~ 3 범위만 선택 가능"` |
| 3 (Steering) | 0 ~ 2 | `"ERROR :: 조향장치는 1 ~ 2 범위만 선택 가능"` |
| 4 (RunTest) | 0 ~ 2 | `"ERROR :: Run 또는 Test 중 하나를 선택 필요"` |

---

### 선택 확인 메시지 출력 (4개)

```java
public void printCarTypeSelected(CarType carType)
public void printEngineSelected(Engine engine)   // engine == null 이면 "고장난 엔진"
public void printBrakeSelected(Brake brake)
public void printSteeringSelected(Steering steering)
```

기존 출력 포맷 그대로 유지:
```
차량 타입으로 Sedan을 선택하셨습니다.
GM 엔진을 선택하셨습니다.
고장난 엔진 엔진을 선택하셨습니다.
MANDO 제동장치를 선택하셨습니다.
BOSCH 조향장치를 선택하셨습니다.
```

---

### RUN 결과 출력

```java
public void showRunResult(Car car, ValidationResult validationResult)
```

분기 처리:
1. `!validationResult.isPassed()` → `"자동차가 동작되지 않습니다"` 출력 후 반환
2. `car.isEngineBroken()` → `"엔진이 고장나있습니다."` + `"자동차가 움직이지 않습니다."` 출력 후 반환
3. 정상 → 아래 포맷 출력

```
Car Type : Sedan
Engine   : GM
Brake    : Mando
Steering : Bosch
자동차가 동작됩니다.
```

label 값 출력 시 기존 `Assemble.java`의 표기를 그대로 따른다:

| enum | 출력값 |
|---|---|
| `CarType.SEDAN` | `Sedan` → `carType.label` |
| `Engine.GM` | `GM` → `engine.label` |
| `Brake.MANDO` | `Mando` ← label을 `"Mando"`로 설정 (기존 출력 기준) |
| `Brake.CONTINENTAL` | `Continental` ← label을 `"Continental"`으로 설정 |
| `Brake.BOSCH` | `Bosch` ← label을 `"Bosch"`으로 설정 |
| `Steering.BOSCH` | `Bosch` ← label을 `"Bosch"`으로 설정 |
| `Steering.MOBIS` | `Mobis` ← label을 `"Mobis"`으로 설정 |

> **주의:** `Brake`와 `Steering`의 label은 Step 1에서 메뉴 표시용(`"MANDO"`, `"BOSCH"`)으로 설정했으나,
> RUN 결과 출력에서는 `"Mando"`, `"Bosch"` 형식을 사용한다.
> 따라서 `showRunResult()`에서는 label을 직접 쓰지 않고 별도 switch로 매핑하거나,
> Step 1의 enum label을 RUN 출력 기준(`"Mando"`, `"Bosch"` 등)으로 수정한다.
> → **label을 RUN 출력 기준으로 통일**하고, 메뉴 출력은 `label.toUpperCase()`를 사용하는 방향으로 수정한다.

**Brake label 수정:**

| 값 | 기존 label | 수정 후 label |
|---|---|---|
| MANDO | `"MANDO"` | `"Mando"` |
| CONTINENTAL | `"CONTINENTAL"` | `"Continental"` |
| BOSCH | `"BOSCH"` | `"Bosch"` |

**Steering label 수정:**

| 값 | 기존 label | 수정 후 label |
|---|---|---|
| BOSCH | `"BOSCH"` | `"Bosch"` |
| MOBIS | `"MOBIS"` | `"Mobis"` |

메뉴에서는 `brake.label.toUpperCase()`, `steering.label.toUpperCase()`로 출력하면 기존 메뉴 텍스트와 동일하게 유지된다.

---

### TEST 결과 출력

```java
public void showTestResult(ValidationResult result)
```

- `result.isPassed() == true` → `"자동차 부품 조합 테스트 결과 : PASS"` 출력
- `result.isPassed() == false` → `"자동차 부품 조합 테스트 결과 : FAIL"` + `result.getReason()` 출력

---

### 기타

```java
public void printError(String message)   // "ERROR :: 숫자만 입력 가능" 등 에러 출력
public void printMessage(String message) // "바이바이", "Test..." 등 일반 메시지 출력
```

---

## 완료 조건

- [ ] `src/main/java/org/example/ui/ConsoleUI.java` 파일이 존재한다.
- [ ] 모든 메뉴 텍스트가 기존 `Assemble.java`와 정확히 일치한다.
- [ ] `Brake`, `Steering` enum의 label이 RUN 출력 기준(`"Mando"`, `"Bosch"` 등)으로 수정된다.
- [ ] `Assemble.java`는 변경되지 않았다.
- [ ] `gradlew build`와 `gradlew test`가 모두 성공한다.

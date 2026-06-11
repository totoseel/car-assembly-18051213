package org.example.ui;

import org.example.domain.Brake;
import org.example.domain.Car;
import org.example.domain.CarType;
import org.example.domain.Engine;
import org.example.domain.Steering;
import org.example.validator.ValidationResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleUITest {

    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private ConsoleUI ui(String input) {
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));
        return new ConsoleUI(sc);
    }

    private String output() {
        return outContent.toString();
    }

    // ── 메뉴 출력 ────────────────────────────────────────────

    @Test
    @DisplayName("showCarTypeMenu는 차량 선택 ASCII 아트와 메뉴를 출력한다")
    void showCarTypeMenu_printsMenuWithAsciiArt() {
        ui("").showCarTypeMenu();
        String out = output();
        assertTrue(out.contains("어떤 차량 타입을 선택할까요?"));
        assertTrue(out.contains("1. Sedan"));
        assertTrue(out.contains("2. SUV"));
        assertTrue(out.contains("3. Truck"));
        assertTrue(out.contains("(@)"));
    }

    @Test
    @DisplayName("showEngineMenu는 엔진 선택 메뉴와 고장난 엔진 항목을 출력한다")
    void showEngineMenu_printsMenuWithBrokenEngine() {
        ui("").showEngineMenu();
        String out = output();
        assertTrue(out.contains("어떤 엔진을 탑재할까요?"));
        assertTrue(out.contains("1. GM"));
        assertTrue(out.contains("2. TOYOTA"));
        assertTrue(out.contains("3. WIA"));
        assertTrue(out.contains("4. 고장난 엔진"));
        assertTrue(out.contains("0. 뒤로가기"));
    }

    @Test
    @DisplayName("showBrakeMenu는 제동장치 선택 메뉴를 대문자로 출력한다")
    void showBrakeMenu_printsMenuInUpperCase() {
        ui("").showBrakeMenu();
        String out = output();
        assertTrue(out.contains("어떤 제동장치를 선택할까요?"));
        assertTrue(out.contains("1. MANDO"));
        assertTrue(out.contains("2. CONTINENTAL"));
        assertTrue(out.contains("3. BOSCH"));
    }

    @Test
    @DisplayName("showSteeringMenu는 조향장치 선택 메뉴를 대문자로 출력한다")
    void showSteeringMenu_printsMenuInUpperCase() {
        ui("").showSteeringMenu();
        String out = output();
        assertTrue(out.contains("어떤 조향장치를 선택할까요?"));
        assertTrue(out.contains("1. BOSCH"));
        assertTrue(out.contains("2. MOBIS"));
    }

    @Test
    @DisplayName("showRunTestMenu는 RUN/Test 선택 메뉴를 출력한다")
    void showRunTestMenu_printsRunAndTestOptions() {
        ui("").showRunTestMenu();
        String out = output();
        assertTrue(out.contains("멋진 차량이 완성되었습니다."));
        assertTrue(out.contains("1. RUN"));
        assertTrue(out.contains("2. Test"));
        assertTrue(out.contains("0. 처음 화면으로 돌아가기"));
    }

    // ── 입력 ─────────────────────────────────────────────────

    @Test
    @DisplayName("readInput은 INPUT > 프롬프트를 출력하고 입력값을 trim해서 반환한다")
    void readInput_printsPromptAndReturnsTrimmedInput() {
        String result = ui("  hello  \n").readInput();
        assertTrue(output().contains("INPUT > "));
        assertEquals("hello", result);
    }

    // ── 범위 검증 ─────────────────────────────────────────────

    @Test
    @DisplayName("isValidRange step0: 유효한 값(1~3)이면 true를 반환한다")
    void isValidRange_step0_validAnswer_returnsTrue() {
        assertTrue(ui("").isValidRange(0, 1));
        assertTrue(ui("").isValidRange(0, 3));
    }

    @Test
    @DisplayName("isValidRange step0: 범위 밖 값이면 에러 메시지 출력 후 false를 반환한다")
    void isValidRange_step0_outOfRange_returnsFalse() {
        assertFalse(ui("").isValidRange(0, 0));
        assertTrue(output().contains("ERROR :: 차량 타입은 1 ~ 3 범위만 선택 가능"));
    }

    @Test
    @DisplayName("isValidRange step1: 유효한 값(0~4)이면 true를 반환한다")
    void isValidRange_step1_validAnswer_returnsTrue() {
        assertTrue(ui("").isValidRange(1, 0));
        assertTrue(ui("").isValidRange(1, 4));
    }

    @Test
    @DisplayName("isValidRange step1: 범위 밖 값이면 에러 메시지 출력 후 false를 반환한다")
    void isValidRange_step1_outOfRange_returnsFalse() {
        assertFalse(ui("").isValidRange(1, 5));
        assertTrue(output().contains("ERROR :: 엔진은 1 ~ 4 범위만 선택 가능"));
    }

    @Test
    @DisplayName("isValidRange step2: 유효한 값(0~3)이면 true를 반환한다")
    void isValidRange_step2_validAnswer_returnsTrue() {
        assertTrue(ui("").isValidRange(2, 0));
        assertTrue(ui("").isValidRange(2, 3));
    }

    @Test
    @DisplayName("isValidRange step2: 범위 밖 값이면 에러 메시지 출력 후 false를 반환한다")
    void isValidRange_step2_outOfRange_returnsFalse() {
        assertFalse(ui("").isValidRange(2, 4));
        assertTrue(output().contains("ERROR :: 제동장치는 1 ~ 3 범위만 선택 가능"));
    }

    @Test
    @DisplayName("isValidRange step3: 유효한 값(0~2)이면 true를 반환한다")
    void isValidRange_step3_validAnswer_returnsTrue() {
        assertTrue(ui("").isValidRange(3, 0));
        assertTrue(ui("").isValidRange(3, 2));
    }

    @Test
    @DisplayName("isValidRange step3: 범위 밖 값이면 에러 메시지 출력 후 false를 반환한다")
    void isValidRange_step3_outOfRange_returnsFalse() {
        assertFalse(ui("").isValidRange(3, 3));
        assertTrue(output().contains("ERROR :: 조향장치는 1 ~ 2 범위만 선택 가능"));
    }

    @Test
    @DisplayName("isValidRange step4: 유효한 값(0~2)이면 true를 반환한다")
    void isValidRange_step4_validAnswer_returnsTrue() {
        assertTrue(ui("").isValidRange(4, 0));
        assertTrue(ui("").isValidRange(4, 2));
    }

    @Test
    @DisplayName("isValidRange step4: 범위 밖 값이면 에러 메시지 출력 후 false를 반환한다")
    void isValidRange_step4_outOfRange_returnsFalse() {
        assertFalse(ui("").isValidRange(4, 3));
        assertTrue(output().contains("ERROR :: Run 또는 Test 중 하나를 선택 필요"));
    }

    // ── 선택 확인 출력 ────────────────────────────────────────

    @Test
    @DisplayName("printCarTypeSelected는 선택한 차량 타입 이름을 출력한다")
    void printCarTypeSelected_printsSelectedLabel() {
        ui("").printCarTypeSelected(CarType.SEDAN);
        assertTrue(output().contains("차량 타입으로 Sedan을 선택하셨습니다."));
    }

    @Test
    @DisplayName("printEngineSelected는 선택한 엔진 이름을 출력한다")
    void printEngineSelected_printsSelectedLabel() {
        ui("").printEngineSelected(Engine.GM);
        assertTrue(output().contains("GM 엔진을 선택하셨습니다."));
    }

    @Test
    @DisplayName("printEngineSelected에 null을 전달하면 고장난 엔진을 출력한다")
    void printEngineSelected_nullEngine_printsBrokenEngine() {
        ui("").printEngineSelected(null);
        assertTrue(output().contains("고장난 엔진 엔진을 선택하셨습니다."));
    }

    @Test
    @DisplayName("printBrakeSelected는 선택한 제동장치 이름을 출력한다")
    void printBrakeSelected_printsSelectedLabel() {
        ui("").printBrakeSelected(Brake.MANDO);
        assertTrue(output().contains("Mando 제동장치를 선택하셨습니다."));
    }

    @Test
    @DisplayName("printSteeringSelected는 선택한 조향장치 이름을 출력한다")
    void printSteeringSelected_printsSelectedLabel() {
        ui("").printSteeringSelected(Steering.BOSCH);
        assertTrue(output().contains("Bosch 조향장치를 선택하셨습니다."));
    }

    // ── RUN 결과 출력 ─────────────────────────────────────────

    @Test
    @DisplayName("showRunResult: 호환성 검사 실패 시 동작 불가 메시지를 출력한다")
    void showRunResult_invalidCar_printsDrivingFailed() {
        Car car = new Car(CarType.SEDAN, Engine.GM, Brake.MANDO, Steering.BOSCH);
        ui("").showRunResult(car, ValidationResult.fail("이유"));
        assertTrue(output().contains("자동차가 동작되지 않습니다"));
    }

    @Test
    @DisplayName("showRunResult: 고장난 엔진이면 엔진 고장 메시지를 출력한다")
    void showRunResult_brokenEngine_printsBrokenEngineMessage() {
        Car car = new Car(CarType.SEDAN, null, Brake.MANDO, Steering.BOSCH);
        ui("").showRunResult(car, ValidationResult.pass());
        String out = output();
        assertTrue(out.contains("엔진이 고장나있습니다."));
        assertTrue(out.contains("자동차가 움직이지 않습니다."));
    }

    @Test
    @DisplayName("showRunResult: 정상 조합이면 부품 정보와 동작 메시지를 출력한다")
    void showRunResult_validCar_printsCarInfoAndSuccess() {
        Car car = new Car(CarType.SEDAN, Engine.GM, Brake.MANDO, Steering.BOSCH);
        ui("").showRunResult(car, ValidationResult.pass());
        String out = output();
        assertTrue(out.contains("Car Type : Sedan"));
        assertTrue(out.contains("Engine   : GM"));
        assertTrue(out.contains("Brake    : Mando"));
        assertTrue(out.contains("Steering : Bosch"));
        assertTrue(out.contains("자동차가 동작됩니다."));
    }

    // ── TEST 결과 출력 ────────────────────────────────────────

    @Test
    @DisplayName("showTestResult: 검사 통과 시 PASS 메시지를 출력한다")
    void showTestResult_passed_printsPass() {
        ui("").showTestResult(ValidationResult.pass());
        assertTrue(output().contains("자동차 부품 조합 테스트 결과 : PASS"));
    }

    @Test
    @DisplayName("showTestResult: 검사 실패 시 FAIL 메시지와 이유를 출력한다")
    void showTestResult_failed_printsFailWithReason() {
        ui("").showTestResult(ValidationResult.fail("Sedan에는 Continental제동장치 사용 불가"));
        String out = output();
        assertTrue(out.contains("자동차 부품 조합 테스트 결과 : FAIL"));
        assertTrue(out.contains("Sedan에는 Continental제동장치 사용 불가"));
    }

    // ── 메시지 출력 ───────────────────────────────────────────

    @Test
    @DisplayName("printError는 전달한 에러 메시지를 출력한다")
    void printError_printsMessage() {
        ui("").printError("ERROR :: 숫자만 입력 가능");
        assertTrue(output().contains("ERROR :: 숫자만 입력 가능"));
    }

    @Test
    @DisplayName("printMessage는 전달한 일반 메시지를 출력한다")
    void printMessage_printsMessage() {
        ui("").printMessage("바이바이");
        assertTrue(output().contains("바이바이"));
    }
}

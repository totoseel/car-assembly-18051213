package org.example;

import org.example.domain.Brake;
import org.example.domain.CarType;
import org.example.domain.Engine;
import org.example.domain.Steering;
import org.example.ui.ConsoleUI;
import org.example.validator.CompatibilityValidator;
import org.example.validator.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class AssemblyProcessTest {

    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private AssemblyProcess process(String input) {
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));
        ConsoleUI ui = new ConsoleUI(sc);
        CompatibilityValidator validator = new CompatibilityValidator();
        return new AssemblyProcess(ui, validator) {
            @Override
            protected void delay(int ms) { /* no-op for tests */ }
        };
    }

    private String output() {
        return outContent.toString();
    }

    // ── exit 처리 ──────────────────────────────────────────────

    @Test
    @DisplayName("exit 입력 시 바이바이를 출력하고 종료한다")
    void run_exitInput_printsByeBye() {
        process("exit\n").run();
        assertTrue(output().contains("바이바이"));
    }

    @Test
    @DisplayName("EXIT 대소문자 무관하게 종료된다")
    void run_exitCaseInsensitive_terminates() {
        process("EXIT\n").run();
        assertTrue(output().contains("바이바이"));
    }

    // ── 숫자 파싱 에러 ─────────────────────────────────────────

    @Test
    @DisplayName("숫자가 아닌 입력 시 에러 메시지를 출력하고 다시 입력을 받는다")
    void run_nonNumericInput_printsError() {
        process("abc\nexit\n").run();
        assertTrue(output().contains("ERROR :: 숫자만 입력 가능"));
    }

    // ── 범위 밖 입력 ───────────────────────────────────────────

    @Test
    @DisplayName("범위 밖 숫자 입력 시 에러 메시지를 출력하고 다시 입력을 받는다")
    void run_outOfRangeInput_printsError() {
        process("9\nexit\n").run();
        assertTrue(output().contains("ERROR :: 차량 타입은 1 ~ 3 범위만 선택 가능"));
    }

    // ── 뒤로가기 (0) ──────────────────────────────────────────

    @Test
    @DisplayName("CAR_TYPE 단계에서 0 입력 시 이전 단계가 없으므로 같은 화면을 유지한다")
    void run_backAtCarTypeStep_staysAtCarType() {
        // CAR_TYPE(step=0)에서 0은 isValidRange에서 범위 밖(1~3)으로 걸러짐
        process("0\nexit\n").run();
        assertTrue(output().contains("ERROR :: 차량 타입은 1 ~ 3 범위만 선택 가능"));
    }

    @Test
    @DisplayName("ENGINE 단계에서 0 입력 시 CAR_TYPE 단계로 돌아간다")
    void run_backAtEngineStep_returnsToCarType() {
        // 1(SEDAN) 선택 후 0(뒤로가기) → exit
        process("1\n0\nexit\n").run();
        String out = output();
        assertTrue(out.contains("어떤 차량 타입을 선택할까요?"));
        assertTrue(out.contains("어떤 엔진을 탑재할까요?"));
    }

    @Test
    @DisplayName("RUN_TEST 단계에서 0 입력 시 CAR_TYPE 단계로 돌아간다")
    void run_backAtRunTestStep_returnsToCarType() {
        // SEDAN(1), GM(1), MANDO(1), BOSCH(1) 선택 후 0(처음으로) → exit
        process("1\n1\n1\n1\n0\nexit\n").run();
        String out = output();
        // 처음 화면이 다시 출력됨
        assertTrue(out.contains("어떤 차량 타입을 선택할까요?"));
    }

    // ── 정상 조립 + RUN ────────────────────────────────────────

    @Test
    @DisplayName("유효한 부품 조합으로 조립 후 RUN 선택 시 자동차가 동작한다")
    void run_validCarAndRun_printsDriving() {
        // SEDAN(1), GM(1), MANDO(1), BOSCH(1), RUN(1) → exit
        process("1\n1\n1\n1\n1\nexit\n").run();
        assertTrue(output().contains("자동차가 동작됩니다."));
    }

    @Test
    @DisplayName("유효한 부품 조합으로 조립 후 Test 선택 시 PASS를 출력한다")
    void run_validCarAndTest_printsPass() {
        // SEDAN(1), GM(1), MANDO(1), BOSCH(1), Test(2) → exit
        process("1\n1\n1\n1\n2\nexit\n").run();
        assertTrue(output().contains("자동차 부품 조합 테스트 결과 : PASS"));
    }

    // ── 고장난 엔진 (answer == 4) ─────────────────────────────

    @Test
    @DisplayName("고장난 엔진(4번) 선택 후 RUN 시 엔진 고장 메시지를 출력한다")
    void run_brokenEngineAndRun_printsBrokenEngine() {
        // SEDAN(1), 고장난엔진(4), MANDO(1), BOSCH(1), RUN(1) → exit
        process("1\n4\n1\n1\n1\nexit\n").run();
        assertTrue(output().contains("엔진이 고장나있습니다."));
    }

    // ── 호환성 실패 케이스 ─────────────────────────────────────

    @Test
    @DisplayName("호환성 실패 조합으로 RUN 시 동작 불가 메시지를 출력한다")
    void run_invalidCombinationAndRun_printsFailed() {
        // SEDAN(1), GM(1), CONTINENTAL(2), BOSCH(1), RUN(1) → exit
        process("1\n1\n2\n1\n1\nexit\n").run();
        assertTrue(output().contains("자동차가 동작되지 않습니다"));
    }

    @Test
    @DisplayName("호환성 실패 조합으로 Test 시 FAIL과 이유를 출력한다")
    void run_invalidCombinationAndTest_printsFail() {
        // SEDAN(1), GM(1), CONTINENTAL(2), BOSCH(1), Test(2) → exit
        process("1\n1\n2\n1\n2\nexit\n").run();
        String out = output();
        assertTrue(out.contains("자동차 부품 조합 테스트 결과 : FAIL"));
        assertTrue(out.contains("Sedan에는 Continental제동장치 사용 불가"));
    }

    // ── 각 enum 값 선택 커버리지 ──────────────────────────────

    @Test
    @DisplayName("SUV(2) 선택이 정상 처리된다")
    void run_selectSuv_isHandled() {
        process("2\nexit\n").run();
        assertTrue(output().contains("SUV을 선택하셨습니다."));
    }

    @Test
    @DisplayName("Truck(3) 선택이 정상 처리된다")
    void run_selectTruck_isHandled() {
        process("3\nexit\n").run();
        assertTrue(output().contains("Truck을 선택하셨습니다."));
    }

    @Test
    @DisplayName("TOYOTA 엔진(2) 선택이 정상 처리된다")
    void run_selectToyotaEngine_isHandled() {
        process("1\n2\nexit\n").run();
        assertTrue(output().contains("TOYOTA 엔진을 선택하셨습니다."));
    }

    @Test
    @DisplayName("WIA 엔진(3) 선택이 정상 처리된다")
    void run_selectWiaEngine_isHandled() {
        process("1\n3\nexit\n").run();
        assertTrue(output().contains("WIA 엔진을 선택하셨습니다."));
    }

    @Test
    @DisplayName("BOSCH 제동장치(3) 선택이 정상 처리된다")
    void run_selectBoschBrake_isHandled() {
        process("1\n1\n3\nexit\n").run();
        assertTrue(output().contains("Bosch 제동장치를 선택하셨습니다."));
    }

    @Test
    @DisplayName("MOBIS 조향장치(2) 선택이 정상 처리된다")
    void run_selectMobisSteering_isHandled() {
        process("1\n1\n1\n2\nexit\n").run();
        assertTrue(output().contains("Mobis 조향장치를 선택하셨습니다."));
    }
}

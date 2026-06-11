package org.example.ui;

import org.example.domain.Brake;
import org.example.domain.Car;
import org.example.domain.CarType;
import org.example.domain.Engine;
import org.example.domain.Steering;
import org.example.validator.ValidationResult;

import java.util.Scanner;

public class ConsoleUI {

    private static final String CLEAR_SCREEN = "\033[H\033[2J";

    private final Scanner sc;

    public ConsoleUI(Scanner sc) {
        this.sc = sc;
    }

    // ── 화면 초기화 ──────────────────────────────────────────

    public void clearScreen() {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
    }

    // ── 메뉴 출력 ────────────────────────────────────────────

    public void showCarTypeMenu() {
        System.out.println("        ______________");
        System.out.println("       /|            |");
        System.out.println("  ____/_|_____________|____");
        System.out.println(" |                      O  |");
        System.out.println(" '-(@)----------------(@)--'");
        System.out.println("===============================");
        System.out.println("어떤 차량 타입을 선택할까요?");
        System.out.println("1. Sedan");
        System.out.println("2. SUV");
        System.out.println("3. Truck");
        System.out.println("===============================");
    }

    public void showEngineMenu() {
        System.out.println("어떤 엔진을 탑재할까요?");
        System.out.println("0. 뒤로가기");
        System.out.println("1. GM");
        System.out.println("2. TOYOTA");
        System.out.println("3. WIA");
        System.out.println("4. 고장난 엔진");
        System.out.println("===============================");
    }

    public void showBrakeMenu() {
        System.out.println("어떤 제동장치를 선택할까요?");
        System.out.println("0. 뒤로가기");
        for (Brake b : Brake.values()) {
            System.out.println((b.ordinal() + 1) + ". " + b.label.toUpperCase());
        }
        System.out.println("===============================");
    }

    public void showSteeringMenu() {
        System.out.println("어떤 조향장치를 선택할까요?");
        System.out.println("0. 뒤로가기");
        for (Steering s : Steering.values()) {
            System.out.println((s.ordinal() + 1) + ". " + s.label.toUpperCase());
        }
        System.out.println("===============================");
    }

    public void showRunTestMenu() {
        System.out.println("멋진 차량이 완성되었습니다.");
        System.out.println("어떤 동작을 할까요?");
        System.out.println("0. 처음 화면으로 돌아가기");
        System.out.println("1. RUN");
        System.out.println("2. Test");
        System.out.println("===============================");
    }

    // ── 입력 ─────────────────────────────────────────────────

    public String readInput() {
        System.out.print("INPUT > ");
        return sc.nextLine().trim();
    }

    // ── 범위 검증 ─────────────────────────────────────────────

    public boolean isValidRange(int step, int answer) {
        switch (step) {
            case 0:
                if (answer < 1 || answer > 3) {
                    System.out.println("ERROR :: 차량 타입은 1 ~ 3 범위만 선택 가능");
                    return false;
                }
                break;
            case 1:
                if (answer < 0 || answer > 4) {
                    System.out.println("ERROR :: 엔진은 1 ~ 4 범위만 선택 가능");
                    return false;
                }
                break;
            case 2:
                if (answer < 0 || answer > 3) {
                    System.out.println("ERROR :: 제동장치는 1 ~ 3 범위만 선택 가능");
                    return false;
                }
                break;
            case 3:
                if (answer < 0 || answer > 2) {
                    System.out.println("ERROR :: 조향장치는 1 ~ 2 범위만 선택 가능");
                    return false;
                }
                break;
            case 4:
                if (answer < 0 || answer > 2) {
                    System.out.println("ERROR :: Run 또는 Test 중 하나를 선택 필요");
                    return false;
                }
                break;
        }
        return true;
    }

    // ── 선택 확인 출력 ────────────────────────────────────────

    public void printCarTypeSelected(CarType carType) {
        System.out.printf("차량 타입으로 %s을 선택하셨습니다.\n", carType.label);
    }

    public void printEngineSelected(Engine engine) {
        String name = (engine == null) ? "고장난 엔진" : engine.label;
        System.out.printf("%s 엔진을 선택하셨습니다.\n", name);
    }

    public void printBrakeSelected(Brake brake) {
        System.out.printf("%s 제동장치를 선택하셨습니다.\n", brake.label);
    }

    public void printSteeringSelected(Steering steering) {
        System.out.printf("%s 조향장치를 선택하셨습니다.\n", steering.label);
    }

    // ── RUN / TEST 결과 출력 ──────────────────────────────────

    public void showRunResult(Car car, ValidationResult validationResult) {
        if (!validationResult.isPassed()) {
            System.out.println("자동차가 동작되지 않습니다");
            return;
        }
        if (car.isEngineBroken()) {
            System.out.println("엔진이 고장나있습니다.");
            System.out.println("자동차가 움직이지 않습니다.");
            return;
        }
        System.out.printf("Car Type : %s\n", car.getCarType().label);
        System.out.printf("Engine   : %s\n", car.getEngine().label);
        System.out.printf("Brake    : %s\n", car.getBrake().label);
        System.out.printf("Steering : %s\n", car.getSteering().label);
        System.out.println("자동차가 동작됩니다.");
    }

    public void showTestResult(ValidationResult result) {
        if (result.isPassed()) {
            System.out.println("자동차 부품 조합 테스트 결과 : PASS");
        } else {
            System.out.println("자동차 부품 조합 테스트 결과 : FAIL");
            System.out.println(result.getReason());
        }
    }

    // ── 메시지 출력 ───────────────────────────────────────────

    public void printError(String message) {
        System.out.println(message);
    }

    public void printMessage(String message) {
        System.out.println(message);
    }
}

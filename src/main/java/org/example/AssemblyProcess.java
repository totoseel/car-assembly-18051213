package org.example;

import org.example.domain.Brake;
import org.example.domain.Car;
import org.example.domain.CarType;
import org.example.domain.Engine;
import org.example.domain.Steering;
import org.example.ui.ConsoleUI;
import org.example.validator.CompatibilityValidator;
import org.example.validator.ValidationResult;

public class AssemblyProcess {

    private static final int CAR_TYPE = 0;
    private static final int ENGINE   = 1;
    private static final int BRAKE    = 2;
    private static final int STEERING = 3;
    private static final int RUN_TEST = 4;

    private final ConsoleUI ui;
    private final CompatibilityValidator validator;

    private CarType  selectedCarType;
    private Engine   selectedEngine;
    private Brake    selectedBrake;
    private Steering selectedSteering;

    public AssemblyProcess(ConsoleUI ui, CompatibilityValidator validator) {
        this.ui        = ui;
        this.validator = validator;
    }

    public void run() {
        int step = CAR_TYPE;

        while (true) {
            ui.clearScreen();

            switch (step) {
                case CAR_TYPE: ui.showCarTypeMenu();  break;
                case ENGINE:   ui.showEngineMenu();   break;
                case BRAKE:    ui.showBrakeMenu();    break;
                case STEERING: ui.showSteeringMenu(); break;
                case RUN_TEST: ui.showRunTestMenu();  break;
            }

            String buf = ui.readInput();

            if (buf.equalsIgnoreCase("exit")) {
                ui.printMessage("바이바이");
                break;
            }

            int answer;
            try {
                answer = Integer.parseInt(buf);
            } catch (NumberFormatException e) {
                ui.printError("ERROR :: 숫자만 입력 가능");
                delay(800);
                continue;
            }

            if (!ui.isValidRange(step, answer)) {
                delay(800);
                continue;
            }

            if (answer == 0) {
                if (step == RUN_TEST) {
                    step = CAR_TYPE;
                } else if (step > CAR_TYPE) {
                    step--;
                }
                continue;
            }

            switch (step) {
                case CAR_TYPE:
                    selectedCarType = CarType.values()[answer - 1];
                    ui.printCarTypeSelected(selectedCarType);
                    delay(800);
                    step = ENGINE;
                    break;
                case ENGINE:
                    selectedEngine = (answer == 4) ? null : Engine.values()[answer - 1];
                    ui.printEngineSelected(selectedEngine);
                    delay(800);
                    step = BRAKE;
                    break;
                case BRAKE:
                    selectedBrake = Brake.values()[answer - 1];
                    ui.printBrakeSelected(selectedBrake);
                    delay(800);
                    step = STEERING;
                    break;
                case STEERING:
                    selectedSteering = Steering.values()[answer - 1];
                    ui.printSteeringSelected(selectedSteering);
                    delay(800);
                    step = RUN_TEST;
                    break;
                case RUN_TEST:
                    Car car = new Car(selectedCarType, selectedEngine, selectedBrake, selectedSteering);
                    ValidationResult result = validator.validate(car);
                    if (answer == 1) {
                        ui.showRunResult(car, result);
                        delay(2000);
                    } else {
                        ui.printMessage("Test...");
                        delay(1500);
                        ui.showTestResult(result);
                        delay(2000);
                    }
                    break;
            }
        }
    }

    protected void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}

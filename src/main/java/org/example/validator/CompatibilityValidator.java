package org.example.validator;

import org.example.domain.Brake;
import org.example.domain.Car;
import org.example.domain.CarType;
import org.example.domain.Engine;
import org.example.domain.Steering;

public class CompatibilityValidator {

    public ValidationResult validate(Car car) {
        if (car.getCarType() == CarType.SEDAN && car.getBrake() == Brake.CONTINENTAL)
            return ValidationResult.fail("Sedan에는 Continental제동장치 사용 불가");

        if (!car.isEngineBroken()) {
            if (car.getCarType() == CarType.SUV && car.getEngine() == Engine.TOYOTA)
                return ValidationResult.fail("SUV에는 TOYOTA엔진 사용 불가");

            if (car.getCarType() == CarType.TRUCK && car.getEngine() == Engine.WIA)
                return ValidationResult.fail("Truck에는 WIA엔진 사용 불가");
        }

        if (car.getCarType() == CarType.TRUCK && car.getBrake() == Brake.MANDO)
            return ValidationResult.fail("Truck에는 Mando제동장치 사용 불가");

        if (car.getBrake() == Brake.BOSCH && car.getSteering() != Steering.BOSCH)
            return ValidationResult.fail("Bosch제동장치에는 Bosch조향장치 이외 사용 불가");

        return ValidationResult.pass();
    }
}

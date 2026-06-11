package org.example.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {

    @Test
    @DisplayName("유효한 인자로 Car를 생성하면 각 getter가 올바른 값을 반환한다")
    void constructor_validArgs_gettersReturnCorrectValues() {
        Car car = new Car(CarType.SEDAN, Engine.GM, Brake.MANDO, Steering.BOSCH);

        assertEquals(CarType.SEDAN,   car.getCarType());
        assertEquals(Engine.GM,       car.getEngine());
        assertEquals(Brake.MANDO,     car.getBrake());
        assertEquals(Steering.BOSCH,  car.getSteering());
    }

    @Test
    @DisplayName("engine에 null을 전달하면 예외 없이 생성되고 isEngineBroken()이 true를 반환한다")
    void constructor_nullEngine_isEngineBrokenReturnsTrue() {
        Car car = new Car(CarType.SEDAN, null, Brake.MANDO, Steering.BOSCH);

        assertTrue(car.isEngineBroken());
        assertNull(car.getEngine());
    }

    @Test
    @DisplayName("engine이 null이 아니면 isEngineBroken()이 false를 반환한다")
    void constructor_nonNullEngine_isEngineBrokenReturnsFalse() {
        Car car = new Car(CarType.SEDAN, Engine.GM, Brake.MANDO, Steering.BOSCH);

        assertFalse(car.isEngineBroken());
    }

    @Test
    @DisplayName("carType에 null을 전달하면 IllegalArgumentException이 발생한다")
    void constructor_nullCarType_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
            () -> new Car(null, Engine.GM, Brake.MANDO, Steering.BOSCH));
    }

    @Test
    @DisplayName("brake에 null을 전달하면 IllegalArgumentException이 발생한다")
    void constructor_nullBrake_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
            () -> new Car(CarType.SEDAN, Engine.GM, null, Steering.BOSCH));
    }

    @Test
    @DisplayName("steering에 null을 전달하면 IllegalArgumentException이 발생한다")
    void constructor_nullSteering_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
            () -> new Car(CarType.SEDAN, Engine.GM, Brake.MANDO, null));
    }
}

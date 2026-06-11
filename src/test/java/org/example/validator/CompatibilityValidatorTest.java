package org.example.validator;

import org.example.domain.Brake;
import org.example.domain.Car;
import org.example.domain.CarType;
import org.example.domain.Engine;
import org.example.domain.Steering;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompatibilityValidatorTest {

    private CompatibilityValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CompatibilityValidator();
    }

    // ── PASS 케이스 ──────────────────────────────────────────

    @Test
    @DisplayName("유효한 부품 조합은 PASS를 반환한다")
    void validate_validCombination_returnsPass() {
        Car car = new Car(CarType.SEDAN, Engine.GM, Brake.MANDO, Steering.BOSCH);

        ValidationResult result = validator.validate(car);

        assertTrue(result.isPassed());
        assertNull(result.getReason());
    }

    @Test
    @DisplayName("Bosch 제동장치와 Bosch 조향장치 조합은 PASS를 반환한다")
    void validate_boschBrakeWithBoschSteering_returnsPass() {
        Car car = new Car(CarType.SEDAN, Engine.GM, Brake.BOSCH, Steering.BOSCH);

        ValidationResult result = validator.validate(car);

        assertTrue(result.isPassed());
    }

    @Test
    @DisplayName("고장난 엔진(null)이어도 다른 부품이 유효하면 PASS를 반환한다")
    void validate_brokenEngine_returnsPass() {
        Car car = new Car(CarType.SEDAN, null, Brake.MANDO, Steering.BOSCH);

        ValidationResult result = validator.validate(car);

        assertTrue(result.isPassed());
    }

    // ── FAIL 케이스 ──────────────────────────────────────────

    @Test
    @DisplayName("Sedan에 Continental 제동장치 사용 시 FAIL을 반환한다")
    void validate_sedanWithContinental_returnsFail() {
        Car car = new Car(CarType.SEDAN, Engine.GM, Brake.CONTINENTAL, Steering.BOSCH);

        ValidationResult result = validator.validate(car);

        assertFalse(result.isPassed());
        assertEquals("Sedan에는 Continental제동장치 사용 불가", result.getReason());
    }

    @Test
    @DisplayName("SUV에 TOYOTA 엔진 사용 시 FAIL을 반환한다")
    void validate_suvWithToyota_returnsFail() {
        Car car = new Car(CarType.SUV, Engine.TOYOTA, Brake.MANDO, Steering.BOSCH);

        ValidationResult result = validator.validate(car);

        assertFalse(result.isPassed());
        assertEquals("SUV에는 TOYOTA엔진 사용 불가", result.getReason());
    }

    @Test
    @DisplayName("Truck에 WIA 엔진 사용 시 FAIL을 반환한다")
    void validate_truckWithWia_returnsFail() {
        Car car = new Car(CarType.TRUCK, Engine.WIA, Brake.BOSCH, Steering.BOSCH);

        ValidationResult result = validator.validate(car);

        assertFalse(result.isPassed());
        assertEquals("Truck에는 WIA엔진 사용 불가", result.getReason());
    }

    @Test
    @DisplayName("Truck에 Mando 제동장치 사용 시 FAIL을 반환한다")
    void validate_truckWithMando_returnsFail() {
        Car car = new Car(CarType.TRUCK, Engine.GM, Brake.MANDO, Steering.BOSCH);

        ValidationResult result = validator.validate(car);

        assertFalse(result.isPassed());
        assertEquals("Truck에는 Mando제동장치 사용 불가", result.getReason());
    }

    @Test
    @DisplayName("Bosch 제동장치에 Mobis 조향장치 사용 시 FAIL을 반환한다")
    void validate_boschBrakeWithMobisSteering_returnsFail() {
        Car car = new Car(CarType.SEDAN, Engine.GM, Brake.BOSCH, Steering.MOBIS);

        ValidationResult result = validator.validate(car);

        assertFalse(result.isPassed());
        assertEquals("Bosch제동장치에는 Bosch조향장치 이외 사용 불가", result.getReason());
    }

    // ── 고장난 엔진 + 엔진 규칙 스킵 확인 ──────────────────

    @Test
    @DisplayName("고장난 엔진(null)일 때 SUV+TOYOTA 규칙을 건너뛰고 PASS를 반환한다")
    void validate_brokenEngineOnSuv_skipsEngineRuleAndReturnsPass() {
        Car car = new Car(CarType.SUV, null, Brake.MANDO, Steering.BOSCH);

        ValidationResult result = validator.validate(car);

        assertTrue(result.isPassed());
    }

    @Test
    @DisplayName("고장난 엔진(null)일 때 Truck+WIA 규칙을 건너뛰고 PASS를 반환한다")
    void validate_brokenEngineOnTruck_skipsEngineRuleAndReturnsPass() {
        Car car = new Car(CarType.TRUCK, null, Brake.BOSCH, Steering.BOSCH);

        ValidationResult result = validator.validate(car);

        assertTrue(result.isPassed());
    }
}

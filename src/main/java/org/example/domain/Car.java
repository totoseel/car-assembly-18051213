package org.example.domain;

public class Car {

    private final CarType carType;
    private final Engine engine;   // null = 고장난 엔진
    private final Brake brake;
    private final Steering steering;

    public Car(CarType carType, Engine engine, Brake brake, Steering steering) {
        if (carType == null)  throw new IllegalArgumentException("carType is required");
        if (brake == null)    throw new IllegalArgumentException("brake is required");
        if (steering == null) throw new IllegalArgumentException("steering is required");

        this.carType  = carType;
        this.engine   = engine;
        this.brake    = brake;
        this.steering = steering;
    }

    public CarType getCarType()    { return carType; }
    public Engine getEngine()      { return engine; }
    public Brake getBrake()        { return brake; }
    public Steering getSteering()  { return steering; }

    public boolean isEngineBroken() { return engine == null; }
}

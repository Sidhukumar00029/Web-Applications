package com.rental.service;

import com.rental.model.Car;
import com.rental.model.Customer;
import com.rental.model.Rental;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarRentalService {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;

    public CarRentalService() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
        // Add some sample cars
        initializeSampleCars();
    }

    private void initializeSampleCars() {
        cars.add(new Car("C001", "Toyota", "Camry", 50.0, "Sedan"));
        cars.add(new Car("C002", "Honda", "Civic", 45.0, "Compact"));
        cars.add(new Car("C003", "BMW", "3 Series", 80.0, "Luxury"));
        cars.add(new Car("C004", "Tesla", "Model 3", 90.0, "Electric"));
        cars.add(new Car("C005", "Ford", "Explorer", 70.0, "SUV"));
    }

    public List<Car> getAllCars() {
        return cars;
    }

    public List<Car> getAvailableCars() {
        return cars.stream()
                .filter(Car::isAvailable)
                .collect(Collectors.toList());
    }

    public Car getCarById(String id) {
        return cars.stream()
                .filter(car -> car.getCarId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public Rental rentCar(Car car, Customer customer, int days) {
        if (car.isAvailable()) {
            customers.add(customer);
            car.setAvailable(false);
            Rental rental = new Rental(car, customer, days);
            rentals.add(rental);
            return rental;
        }
        return null;
    }

    public List<Rental> getAllRentals() {
        return rentals;
    }

    public void returnCar(String carId) {
        Car car = getCarById(carId);
        if (car != null) {
            // Find and remove the rental
            rentals.removeIf(rental -> rental.getCar().getCarId().equals(carId));
            // Make the car available again
            car.setAvailable(true);
        }
    }
}

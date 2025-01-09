package com.rental.controller;

import com.rental.model.Car;
import com.rental.model.Customer;
import com.rental.model.Rental;
import com.rental.model.RentalRequest;
import com.rental.service.CarRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CarRentalController {

    @Autowired
    private CarRentalService carRentalService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("cars", carRentalService.getAllCars());
        return "index";
    }

    @GetMapping("/cars")
    public String listCars(Model model) {
        model.addAttribute("cars", carRentalService.getAvailableCars());
        return "cars";
    }

    @GetMapping("/rentals")
    public String myRentals(Model model) {
        model.addAttribute("rentals", carRentalService.getAllRentals());
        return "rentals";
    }

    @GetMapping("/rent/{id}")
    public String rentCarForm(@PathVariable String id, Model model) {
        Car car = carRentalService.getCarById(id);
        if (car != null && car.isAvailable()) {
            model.addAttribute("car", car);
            model.addAttribute("rentalRequest", new RentalRequest());
            return "rent-form";
        }
        return "redirect:/cars";
    }

    @PostMapping("/rent")
    public String rentCar(@ModelAttribute RentalRequest rentalRequest) {
        Car car = carRentalService.getCarById(rentalRequest.getCarId());
        if (car != null && car.isAvailable()) {
            Customer customer = new Customer("CUST" + System.currentTimeMillis(), rentalRequest.getCustomerName());
            carRentalService.rentCar(car, customer, rentalRequest.getDays());
            return "redirect:/rentals";
        }
        return "redirect:/cars";
    }

    @PostMapping("/return")
    public String returnCar(@RequestParam String carId) {
        carRentalService.returnCar(carId);
        return "redirect:/rentals";
    }
}

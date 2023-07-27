/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.globeTrotter.controller;


import com.sg.globeTrotter.dto.Trip;
import com.sg.globeTrotter.service.GlobeTrotterService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author marya
 */

@Controller
public class TripController {
    @Autowired
    GlobeTrotterService service;
     @GetMapping("trips")
    public String index(Model model) {
        model.addAttribute("trips", service.getAllTrips());
        return "trips";
    }
    
     Set<ConstraintViolation<Trip>> violations = new HashSet<>();

    @GetMapping("trips")
    public String displayTrips(Model model) {
        List<Trip> trips = service.getAllTrips();
        model.addAttribute("trips", trips);
        model.addAttribute("errors", violations);

        return "trips";
    }

    @PostMapping("addTrip")
    public String addTrip(Trip trip, BindingResult result) {
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(trip);
        if (violations.isEmpty()) {
            service.addTrip(trip);
        }
        return "redirect:/trips";
    }

    @GetMapping("deleteTrip")
    public String deleteTrip(Integer id) {
        service.deleteTripByID(id);
        return "redirect:/trips";
    }

       @GetMapping("editTrip")
    public String editTrip(Integer id, Model model) {
        Trip trip = service.getTripByID(id);
        model.addAttribute("trip", trip);
        return "editTrip";
    }

    @PostMapping("editTrip")
    public String performEditTrip(@Valid Trip trip, BindingResult result, HttpServletRequest request, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("trip", trip);
            return "editTrip";
        }

        service.updateTrip(trip);

        return "redirect:/trips";
    }
}

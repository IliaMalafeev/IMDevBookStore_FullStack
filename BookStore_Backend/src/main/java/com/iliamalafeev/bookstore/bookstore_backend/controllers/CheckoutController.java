package com.iliamalafeev.bookstore.bookstore_backend.controllers;

import com.iliamalafeev.bookstore.bookstore_backend.dto.CheckoutDTO;
import com.iliamalafeev.bookstore.bookstore_backend.security.jwt.JwtUtils;
import com.iliamalafeev.bookstore.bookstore_backend.services.CheckoutService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:5173/")
@RestController
@RequestMapping("api/checkouts")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Checkout Controller")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final JwtUtils jwtUtils;

    @Autowired
    public CheckoutController(CheckoutService checkoutService, JwtUtils jwtUtils) {
        this.checkoutService = checkoutService;
        this.jwtUtils = jwtUtils;
    }

    private String extractEmail(String token) {
        String jwt = token.substring(7);
        return jwtUtils.extractPersonEmail(jwt);
    }

    @GetMapping("/secure/current-loans-count")
    public int getCurrentCheckoutsCount(@RequestHeader(value = "Authorization") String token) {

        return checkoutService.getCurrentCheckoutsCount(extractEmail(token));
    }

    @GetMapping("/secure/current-checkouts")
    public List<CheckoutDTO> getCurrentCheckouts(@RequestHeader(value = "Authorization") String token) {

        return checkoutService.getCurrentCheckouts(extractEmail(token));
    }
}

package com.iliamalafeev.bookstore.bookstore_backend.controllers;

import com.iliamalafeev.bookstore.bookstore_backend.dto.HistoryRecordDTO;
import com.iliamalafeev.bookstore.bookstore_backend.security.jwt.JwtUtils;
import com.iliamalafeev.bookstore.bookstore_backend.services.HistoryRecordService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:5173/")
@RestController
@RequestMapping("/api/history-records/secure")
@SecurityRequirement(name = "Bearer Authentication")
public class HistoryRecordController {

    private final HistoryRecordService historyRecordService;
    private final JwtUtils jwtUtils;

    @Autowired
    public HistoryRecordController(HistoryRecordService historyRecordService, JwtUtils jwtUtils) {
        this.historyRecordService = historyRecordService;
        this.jwtUtils = jwtUtils;
    }

    private String extractEmail(String token) {
        String jwt = token.substring(7);
        return jwtUtils.extractPersonEmail(jwt);
    }

    @GetMapping
    public Page<HistoryRecordDTO> findAllByPersonEmail(@RequestHeader("Authorization") String token, @RequestParam(value = "page") Integer page,
                                                       @RequestParam(value = "records-per-page") Integer recordsPerPage) {

        return historyRecordService.findAllByPersonEmail(extractEmail(token), PageRequest.of(page, recordsPerPage));
    }
}

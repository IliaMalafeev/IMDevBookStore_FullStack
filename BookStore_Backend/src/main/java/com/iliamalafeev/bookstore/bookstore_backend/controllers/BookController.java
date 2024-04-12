package com.iliamalafeev.bookstore.bookstore_backend.controllers;

import com.iliamalafeev.bookstore.bookstore_backend.dto.BookDTO;
import com.iliamalafeev.bookstore.bookstore_backend.dto.ReviewDTO;
import com.iliamalafeev.bookstore.bookstore_backend.security.jwt.JwtUtils;
import com.iliamalafeev.bookstore.bookstore_backend.services.BookService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:5173/")
@RestController
@RequestMapping("/api/books")
@Tag(name = "Book Controller")
public class BookController {

    private final BookService bookService;
    private final JwtUtils jwtUtils;

    @Autowired
    public BookController(BookService bookService, JwtUtils jwtUtils) {
        this.bookService = bookService;
        this.jwtUtils = jwtUtils;
    }

    private String extractEmail(String token) {
        String jwt = token.substring(7);
        return jwtUtils.extractPersonEmail(jwt);
    }

    @GetMapping
    public Page<BookDTO> findAll(@RequestParam(value = "page") Integer page,
                                 @RequestParam(value = "books-per-page") Integer booksPerPage) {

        return bookService.findAll(PageRequest.of(page, booksPerPage));
    }

    @GetMapping("/{bookId}")
    public BookDTO findById(@PathVariable("bookId") Long bookId) {

        return bookService.findById(bookId);
    }

    @GetMapping("/search/by-title")
    public Page<BookDTO> findAllByTitle(@RequestParam(value = "page") Integer page,
                                        @RequestParam(value = "books-per-page") Integer booksPerPage,
                                        @RequestParam("title-query") String titleQuery) {

        return bookService.findAllByTitle(titleQuery, PageRequest.of(page, booksPerPage));
    }

    @GetMapping("/search/by-genre")
    public Page<BookDTO> findAllByGenre(@RequestParam("genre-query") String genreQuery,
                                        @RequestParam(value = "page") Integer page,
                                        @RequestParam(value = "books-per-page") Integer booksPerPage) {

        return bookService.findAllByGenre(genreQuery, PageRequest.of(page, booksPerPage));
    }

    @GetMapping("/secure/is-checked-out/{bookId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public Boolean isBookCheckedOutByPerson(@PathVariable("bookId") Long bookId, @RequestHeader("Authorization") String token) {

        return bookService.isBookCheckedOutByPerson(extractEmail(token), bookId);
    }

    @PutMapping("/secure/checkout/{bookId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<HttpStatus> checkoutBook(@PathVariable("bookId") Long bookId, @RequestHeader("Authorization") String token) {

        bookService.checkoutBook(extractEmail(token), bookId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/secure/renew-checkout/{bookId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<HttpStatus> renewCheckout(@PathVariable("bookId") Long bookId, @RequestHeader("Authorization") String token) {

        bookService.renewCheckout(extractEmail(token), bookId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/secure/return/{bookId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<HttpStatus> returnBook(@PathVariable("bookId") Long bookId, @RequestHeader("Authorization") String token) {

        bookService.returnBook(extractEmail(token), bookId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/secure/is-reviewed/{bookId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public Boolean isBookReviewedByPerson(@PathVariable("bookId") Long bookId, @RequestHeader("Authorization") String token) {

        return bookService.isBookReviewedByPerson(extractEmail(token), bookId);
    }

    @PostMapping("/secure/review/{bookId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<HttpStatus> reviewBook(@PathVariable("bookId") Long bookId, @RequestHeader("Authorization") String token,
                                                 @RequestBody @Valid ReviewDTO reviewDTO, BindingResult bindingResult) {

        bookService.reviewBook(extractEmail(token), bookId, reviewDTO, bindingResult);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
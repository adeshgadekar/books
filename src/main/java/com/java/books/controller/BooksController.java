package com.java.books.controller;

import com.java.books.dto.BookRequest;
import com.java.books.entity.Book;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BooksController {

    private final List<Book> books = new ArrayList<>();


    public BooksController() {
        initilizeBooks();
    }

    private void initilizeBooks() {
        books.addAll(List.of(new Book(1, "On the Origin of Species", "Charles Darwin", "Science", 5),
                new Book(2, "The Voyage of the Beagle", "Charles Darwin", "Science", 4),
                new Book(3, "Principia Mathematica", "Isaac Newton", "Science", 5),
                new Book(4, "Dialogue Concerning the Two Chief World Systems", "Galileo Galilei", "Science", 4),
                new Book(5, "The Selfish Gene", "Richard Dawkins", "Science", 4),
                new Book(6, "The Double Helix", "James D. Watson", "Science", 4)));

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable @Min(value = 1) Long id) {

        books.removeIf(b -> b.getId() == id);
    }


    @PutMapping("/{id}")
    public void updateBook(@PathVariable @Min(value = 1) long id, @RequestBody BookRequest bookRequest) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == id) {
                Book updatedBook = convertToBook(id, bookRequest);
                books.set(i , updatedBook);
                return;
            }
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createBook(@Valid @RequestBody BookRequest bookRequest) {

        long id;
        if(books.isEmpty()){
            id = 1;
        }else {
            id= books.get(books.size()-1).getId() +1;
        }
        Book book = convertToBook(id,bookRequest);
        books.add(book);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable @Min(value = 1) long id) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
    }

    @GetMapping
    public List<Book> getBooks() {

        return books;
    }

    private  Book convertToBook(long id, BookRequest bookRequest)
    {
        return new Book(id, bookRequest.getTitle(), bookRequest.getAuthor(), bookRequest.getCategory(), bookRequest.getRating());
    }
}

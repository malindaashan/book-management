package com.management.book.repository;

import com.management.book.dto.BookDto;
import com.management.book.entity.Book;
import com.management.book.exception.BookNotFoundException;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(BookDto bookDto);
    List<Book> findAll();
    Book findById(Long bookId) throws BookNotFoundException;
    boolean deleteById(Long bookId) throws BookNotFoundException;
    Book updateById(Long bookId, BookDto updatedBook) throws BookNotFoundException;
}

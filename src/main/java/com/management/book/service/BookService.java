package com.management.book.service;

import com.management.book.dto.BookDto;
import com.management.book.entity.Book;
import com.management.book.exception.BookNotFoundException;

import java.util.List;
import java.util.Optional;

public interface BookService {
    BookDto saveBook(BookDto bookDto);
    List<BookDto> findAllBooks();
    BookDto findBookById(Long bookId) throws BookNotFoundException;
    boolean deleteBookById(Long bookId) throws BookNotFoundException;
    BookDto updateBookById(Long bookId, BookDto updatedBook) throws BookNotFoundException;
}

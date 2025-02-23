package com.management.book.service.impl;

import com.management.book.dto.BookDto;
import com.management.book.entity.Book;
import com.management.book.exception.BookNotFoundException;
import com.management.book.repository.BookRepository;
import com.management.book.service.BookService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public BookDto saveBook(BookDto bookDto) {
        Book book = bookRepository.save(bookDto);
        return convertToDto(book);
    }
    @Override
    public List<BookDto> findAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto findBookById(Long bookId) throws BookNotFoundException{
        return convertToDto(bookRepository.findById(bookId));
    }

    @Override
    public boolean deleteBookById(Long bookId) throws BookNotFoundException{
        return bookRepository.deleteById(bookId);
    }

    @Override
    public BookDto updateBookById(Long bookId, BookDto updatedBook) throws BookNotFoundException {
        return convertToDto(bookRepository.updateById(bookId, updatedBook));
    }


    private BookDto convertToDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublishedDate(),
                book.getIsbn(),
                book.getDescription()
        );
    }
}

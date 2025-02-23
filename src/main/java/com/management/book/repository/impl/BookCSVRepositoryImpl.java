package com.management.book.repository.impl;
//import org.apache.commons.;

import com.management.book.dto.BookDto;
import com.management.book.entity.Book;
import com.management.book.exception.BookNotFoundException;
import com.management.book.repository.BookRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class BookCSVRepositoryImpl implements BookRepository {

    private static final String CSV_FILE_PATH = "books.csv";
    private static final Random random = new Random();
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Book save(BookDto bookDto) {
        boolean fileExists = new File(CSV_FILE_PATH).exists();
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH, true);
             CSVPrinter csvPrinter = new CSVPrinter(writer, fileExists ? CSVFormat.DEFAULT :
                     CSVFormat.DEFAULT.withHeader("id", "title", "author", "publishedDate", "isbn", "description"))) {
            Long id = generateRandomId();
            csvPrinter.printRecord(id, bookDto.getTitle(), bookDto.getAuthor(), formatter.format(bookDto.getPublishedDate()), bookDto.getIsbn(), bookDto.getDescription());
            return new Book(id, bookDto.getTitle(), bookDto.getAuthor(), bookDto.getPublishedDate(), bookDto.getIsbn(), bookDto.getDescription());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long generateRandomId() {
        return Math.abs(random.nextLong()); // Ensures a positive long value
    }

    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try (FileReader reader = new FileReader(CSV_FILE_PATH);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord record : csvParser) {
                books.add(new Book(
                        Long.valueOf(record.get("id")),
                        record.get("title"),
                        record.get("author"),
                        formatter.parse(record.get("publishedDate")),
                        record.get("isbn"),
                        record.get("description")
                ));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return books;
    }

    public Book findById(Long bookId) throws BookNotFoundException {
        try (FileReader reader = new FileReader(CSV_FILE_PATH);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord record : csvParser) {
                if (Long.valueOf(record.get("id")).equals(bookId)) {
                    return new Book(
                            Long.valueOf(record.get("id")),
                            record.get("title"),
                            record.get("author"),
                            formatter.parse(record.get("publishedDate")),
                            record.get("isbn"),
                            record.get("description")
                    );
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        throw new BookNotFoundException("Book not found with id: " + bookId);
       // return Optional.empty(); // If the book is not found
    }

    public boolean deleteById(Long bookId) throws BookNotFoundException{
        List<Book> books = new ArrayList<>();
        boolean isDeleted = false;

        // Read books from CSV except the one to be deleted
        try (FileReader reader = new FileReader(CSV_FILE_PATH);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            for (CSVRecord record : csvParser) {
                if (!Long.valueOf(record.get("id")).equals(bookId)) {
                    books.add(new Book(
                            Long.valueOf(record.get("id")),
                            record.get("title"),
                            record.get("author"),
                            formatter.parse(record.get("publishedDate")),
                            record.get("isbn"),
                            record.get("description")
                    ));
                } else {
                    isDeleted = true;
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        if (!isDeleted) {
            throw new BookNotFoundException("Book not found with id: " + bookId);
        }

        // Rewrite the CSV file without the deleted book
        return writeBooksToCSV(books);
    }

    public Book updateById(Long bookId, BookDto updatedBook) throws BookNotFoundException{
        List<Book> books = new ArrayList<>();
        boolean isUpdated = false;

        // Read books from CSV and update the matching book
        try (FileReader reader = new FileReader(CSV_FILE_PATH);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord record : csvParser) {
                if (Long.valueOf(record.get("id")).equals(bookId)) {
                    books.add(new Book(bookId, updatedBook.getTitle(), updatedBook.getAuthor(), updatedBook.getPublishedDate(), updatedBook.getIsbn(), updatedBook.getDescription()));
                    isUpdated = true;
                } else {
                    books.add(new Book(Long.valueOf(record.get("id")),
                            record.get("title"),
                            record.get("author"),
                            formatter.parse(record.get("publishedDate")),
                            record.get("isbn"),
                            record.get("description")));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        if (!isUpdated) {
            throw new BookNotFoundException("Book not found with id: " + bookId);
           // return false; // Book not found
        }
        writeBooksToCSV(books);
        return findById(bookId);
    }

    private boolean writeBooksToCSV(List<Book> books) {
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("id", "title", "author", "publishedDate", "isbn", "description"))) {

            for (Book book : books) {
                csvPrinter.printRecord(book.getId(), book.getTitle(), book.getAuthor(), formatter.format(book.getPublishedDate()), book.getIsbn(), book.getDescription());
            }
            csvPrinter.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

package org.book.store.model;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class BookListImplTest {

    private BookListImpl sut;
    private List<Book> stock;
    private List<Book> basket;
    private int stockSize;

    @Before
    public void setUp() throws Exception {
        this.stock = new LinkedList<>();
        this.basket = new LinkedList<>();
        this.sut = new BookListImpl(stock, basket);
        this.stockSize = 5;
        this.stock.addAll(this.getBooks(this.stockSize));
    }

    @Test
    public void listBooks() throws Exception {
        this.stock.clear();
        assertEquals(0, this.sut.list("").length); // empty

        this.stock.addAll(this.getBooks(this.stockSize));

        assertEquals(this.stockSize, this.sut.list("").length); // stock with 5 books

        assertEquals(0, this.sut.list("Test").length); // search test

        assertEquals(1, this.sut.list("T1").length); // search T1
    }

    @Test
    public void add() throws Exception {
        this.stock.get(0).setQuantity(10);

        // should add book in basket
        assertTrue(this.sut.add(this.stock.get(0), 10));
        assertEquals(1, this.basket.size());
        assertEquals(10, this.basket.get(0).getQuantity());
        assertEquals(this.stock.get(0).getPrice().multiply(new BigDecimal("5.0")), this.sut.getBasketTotalPrice());

        // should not add book in basket because quantity is not available in stock
        assertFalse(this.sut.add(this.stock.get(0), 1));
        assertFalse(this.sut.add(this.stock.get(1), 100));

        // should not add book in basket because book is not available in stock
        assertFalse(this.sut.add(new Book("T", "A", "100", "22"), 1));

        // should not add book in basket because quantity is invalid
        assertFalse(this.sut.add(this.stock.get(0), 0));
        assertFalse(this.sut.add(this.stock.get(0), -1));
    }

    @Test
    public void remove() throws Exception {
        this.stock.get(0).setQuantity(10);
        this.sut.add(this.stock.get(0), 10);

        // should not remove because quantity is invalid
        assertFalse(this.sut.remove(this.stock.get(0), 0));
        assertFalse(this.sut.remove(this.stock.get(0), -1));
        ;

        // should not remove because quantity is more than in basket
        assertFalse(this.sut.remove(this.stock.get(0), 11));

        // should not remove because book is not present in basket
        assertFalse(this.sut.remove(this.stock.get(1), 1));

        // should remove 5 books
        assertTrue(this.sut.remove(this.stock.get(0), 5));
        assertEquals(1, this.basket.size());
        assertEquals(5, this.basket.get(0).getQuantity());
        assertEquals(this.stock.get(0).getPrice().multiply(new BigDecimal("5.0")), this.sut.getBasketTotalPrice());
    }

    @Test
    public void buy() throws Exception {
        // should not buy with empty array
        assertEquals(0, this.sut.buy().length);

        // should return not in stock
        int[] statuses = new int[]{
                BookListImpl.Status.NOT_IN_STOCK.value,
                BookListImpl.Status.NOT_IN_STOCK.value
        };
        this.sut.add(this.stock.get(0), 1);
        this.sut.add(this.stock.get(1), 1);
        this.basket.get(0).setQuantity(100);
        this.basket.get(1).setQuantity(100);
        assertArrayEquals(statuses, this.sut.buy(this.basket.toArray(new Book[0])));

        // should return ok
        statuses = new int[]{
                BookListImpl.Status.OK.value,
                BookListImpl.Status.OK.value
        };
        this.basket.get(0).setQuantity(1);
        this.basket.get(1).setQuantity(1);
        assertArrayEquals(statuses, this.sut.buy(this.basket.toArray(new Book[0])));

        // should return does not exists
        statuses = new int[]{
                BookListImpl.Status.DOES_NOT_EXIT.value,
                BookListImpl.Status.DOES_NOT_EXIT.value
        };
        this.basket.clear();
        this.basket.add(new Book("T", "A", "100", "22"));
        this.basket.add(new Book("TT", "AA", "1000", "222"));
        assertArrayEquals(statuses, this.sut.buy(this.basket.toArray(new Book[0])));
    }

    @Test
    public void addBookInStock() throws Exception {
        this.stock.clear();
        List<Book> books = this.getBooks(this.stockSize);

        // should add unique books
        for (int i = 0; i < books.size(); i++) {
            assertTrue(this.sut.addBookInStock(books.get(i)));
        }

        // should ignore duplicate books
        for (Book book : books) {
            assertFalse(this.sut.addBookInStock(book));
        }
    }

    private List<Book> getBooks(int size) throws Exception {
        List<Book> books = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            books.add(new Book("T" + i, "A" + i, "" + i, (i + 1) + ""));
        }
        return books;
    }
}
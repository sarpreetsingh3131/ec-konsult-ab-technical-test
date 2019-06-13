package org.book.store.model;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class BookListImpl implements BookList {

    private List<Book> stock;
    private List<Book> basket;

    public BookListImpl(List<Book> stock, List<Book> basket) {
        this.stock = stock;
        this.basket = basket;
    }

    @Override
    public Book[] list(String searchString) {
        if (searchString == null || searchString.trim().isEmpty()) {
            return this.stock.toArray(new Book[0]); // return all books
        }
        // search books
        return this.stock.stream()
                .filter(book ->
                        book.getAuthor().equalsIgnoreCase(searchString) || book.getTitle().equalsIgnoreCase(searchString))
                .toArray(Book[]::new);
    }

    @Override
    public boolean add(Book book, int quantity) {
        try {
            // check if available in stock
            int stockIndex = this.stock.indexOf(book);
            Book stockBook = this.stock.get(stockIndex);
            if (quantity > 0 && quantity <= stockBook.getQuantity()) { // check if quantity is within bound
                int basketIndex = this.basket.indexOf(book);
                if (basketIndex == -1) { // not added in basket before
                    this.basket.add(new Book(book.getTitle(), book.getAuthor(), book.getPrice(), quantity)); // add new
                } else {
                    Book basketBook = this.basket.get(basketIndex);
                    // if quantity is within bound after add, then update quantity
                    if (basketBook.getQuantity() + quantity <= stockBook.getQuantity()) {
                        basketBook.setQuantity(book.getQuantity() + quantity);
                    } else {
                        return false; // don't update
                    }
                }
                return true;
            }
        } catch (Exception e) {
            // ignore if invalid quantity, book not available in stock
        }
        return false;
    }

    @Override
    public boolean remove(Book book, int quantity) {
        try {
            // check if available in basket
            int basketIndex = this.basket.indexOf(book);
            Book basketBook = this.basket.get(basketIndex);
            if (quantity > 0 && quantity <= basketBook.getQuantity()) { // check if quantity is within bound
                if (quantity == basketBook.getQuantity()) { // if remove quantity is same as basket book quantity
                    this.basket.remove(basketBook); // remove book completely
                } else {
                    basketBook.setQuantity(basketBook.getQuantity() - quantity); // update quantity
                }
                return true;
            }
        } catch (Exception e) {
            // ignore if invalid quantity, book not available in basket or stock
        }
        return false;
    }

    @Override
    public int[] buy(Book... books) {
        int[] statuses = new int[books.length];
        int index = 0;
        for (Book book : books) {
            int stockIndex = this.stock.indexOf(book);
            if (stockIndex == -1) { // not found in stock
                statuses[index] = Status.DOES_NOT_EXIT.value;
            } else {
                Book stockBook = this.stock.get(stockIndex);
                if (book.getQuantity() <= stockBook.getQuantity()) {
                    statuses[index] = Status.OK.value;
                    try {
                        // update stock quantity
                        stockBook.setQuantity(stockBook.getQuantity() - book.getQuantity());
                    } catch (Exception e) {
                    }
                } else {
                    statuses[index] = Status.NOT_IN_STOCK.value;
                }
            }
            index++;
        }
        return statuses;
    }

    public BigDecimal getBasketTotalPrice() {
        BigDecimal totalPrice = new BigDecimal("0.0");
        for (Book book : this.basket) {
            totalPrice = totalPrice.add(book.getPrice().multiply(new BigDecimal(book.getQuantity())));
        }
        return totalPrice;
    }

    public List<Book> getBasket() {
        return new LinkedList<>(this.basket); // return copy only
    }

    public List<Book> getStock() {
        return new LinkedList<>(this.stock); // return copy only
    }

    public boolean addBookInStock(Book book) {
        if (this.stock.contains(book)) {
            return false;
        }
        return this.stock.add(book);
    }

    public enum Status {
        OK(0), NOT_IN_STOCK(1), DOES_NOT_EXIT(2);

        int value;

        Status(int value) {
            this.value = value;
        }
    }
}

package org.book.store.model;

public interface BookList {

    Book[] list(String searchString);

    boolean add(Book book, int quantity);

    boolean remove(Book book, int quantity);

    int[] buy(Book... books);
}

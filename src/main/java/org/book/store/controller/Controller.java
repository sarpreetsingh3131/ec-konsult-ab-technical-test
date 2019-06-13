package org.book.store.controller;

import org.book.store.model.Book;
import org.book.store.model.BookListImpl;
import org.book.store.view.View;

import java.util.Arrays;

public class Controller {

    private View view;
    private BookListImpl bookList;

    public Controller(View view, BookListImpl bookList) {
        this.view = view;
        this.bookList = bookList;
    }

    public boolean start() {
        this.view.displayInstructions();
        String input = this.view.getInput();
        if (!input.isEmpty()) {
            try {
                String[] data = input.split("=");
                boolean result;
                int index;
                int quantity;
                Book book;
                switch (data[0]) {
                    case "list":
                        this.view.displayBooks(Arrays.asList(this.bookList.list("")));
                        break;
                    case "search":
                        this.view.displayBooks(Arrays.asList(this.bookList.list(data[1])));
                        break;
                    case "book":
                        data = data[1].split(";");
                        book = new Book(data[0], data[1], data[2], data[3]);
                        result = this.bookList.addBookInStock(book);
                        this.view.displayMessage(result);
                        break;
                    case "basket":
                        this.view.displayBasket(this.bookList.getBasket(), this.bookList.getBasketTotalPrice());
                        break;
                    case "add":
                        index = Integer.parseInt(data[1].split(";")[0]);
                        quantity = Integer.parseInt(data[1].split(";")[1]);
                        book = this.bookList.getStock().get(index);
                        result = this.bookList.add(book, quantity);
                        this.view.displayMessage(result);
                        break;
                    case "remove":
                        index = Integer.parseInt(data[1].split(";")[0]);
                        quantity = Integer.parseInt(data[1].split(";")[1]);
                        book = this.bookList.getBasket().get(index);
                        result = this.bookList.remove(book, quantity);
                        this.view.displayMessage(result);
                        break;
                    case "buy":
                        Book[] books = this.bookList.getBasket().toArray(new Book[0]);
                        int[] statuses = this.bookList.buy(books);
                        this.view.displayStatuses(statuses);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                this.view.displayError();
            }
        }
        return !input.equals("quit");
    }
}

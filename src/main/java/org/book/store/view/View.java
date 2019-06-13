package org.book.store.view;

import org.book.store.model.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class View {

    private Scanner scanner;

    public View(Scanner scanner) {
        this.scanner = scanner;
    }

    public void displayInstructions() {
        System.out.println("\n- list: list books (e.g: list)");
        System.out.println("- search: search books (e.g: search=title/author)");
        System.out.println("- book: add new book in stock (e.g: book=title;author;price;quantity)");
        System.out.println("- basket: view basket (e.g: basket)");
        System.out.println("- add: add book in basket (e.g: add=index;quantity)");
        System.out.println("- remove: remove book from basket (e.g: remove=index;quantity)");
        System.out.println("- buy: buy books in basket (e.g: buy)");
        System.out.println("- quit: quit (e.g: quit)");
    }

    public void displayBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("*** no book found ***");
            return;
        }

        int index = 0;
        for (Book book : books) {
            System.out.println("{index = " + index++ + ", author = " + book.getAuthor() + ", title = " + book.getTitle()
                    + ", price = " + book.getPrice() + ", quantity = " + book.getQuantity() + "}");
        }
    }

    public void displayBasket(List<Book> books, BigDecimal totalPrice) {
        this.displayBooks(books);
        System.out.println("*** total price = " + totalPrice.toString() + " ***");
    }

    public void displayStatuses(int[] statuses) {
        if (statuses.length == 0) {
            System.out.println("*** basket is empty ***");
            return;
        }
        int index = 0;
        for (int status : statuses) {
            System.out.println("{index = " + index + ", status = " + status + "}");
        }
    }

    public String getInput() {
        System.out.print(">");
        return scanner.nextLine().trim();
    }

    public void displayMessage(boolean result) {
        if (result) {
            this.displaySuccess();
        } else {
            this.displayError();
        }
    }

    public void displayError() {
        System.err.println("*** invalid values ***");
    }

    public void displaySuccess() {
        System.out.println("*** success ***");
    }
}

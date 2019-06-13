package org.book.store;

import org.book.store.controller.Controller;
import org.book.store.model.Book;
import org.book.store.model.BookListImpl;
import org.book.store.util.StockLoader;
import org.book.store.view.View;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String url = "https://raw.githubusercontent.com/contribe/contribe/dev/bookstoredata/bookstoredata.txt";
        List<Book> stock = StockLoader.load(url);
        List<Book> basket = new LinkedList<>();
        Controller controller = new Controller(new View(scanner), new BookListImpl(stock, basket));
        while (controller.start()) ;
    }
}

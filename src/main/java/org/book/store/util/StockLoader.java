package org.book.store.util;

import org.book.store.model.Book;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class StockLoader {

    public static List<Book> load(String url) throws IOException {
        HashSet<Book> books = new HashSet<>();
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(";");
            if (data.length == 4) {
                try {
                    books.add(new Book(data[0], data[1], data[2], data[3]));
                } catch (Exception e) {
                    // ignore parsing exception
                }
            }
        }
        reader.close();
        connection.disconnect();
        return new LinkedList<>(books);
    }
}

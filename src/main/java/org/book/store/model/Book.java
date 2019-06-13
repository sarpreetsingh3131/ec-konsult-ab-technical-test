package org.book.store.model;

import java.math.BigDecimal;

public class Book {

    private String title;
    private String author;
    private BigDecimal price;
    private int quantity;

    public Book(String title, String author, BigDecimal price, int quantity) throws Exception {
        this.setTitle(title);
        this.setAuthor(author);
        this.setPrice(price);
        this.setQuantity(quantity);
    }

    public Book(String title, String author, String price, String quantity) throws Exception {
        this.setTitle(title);
        this.setAuthor(author);
        this.setPrice(new BigDecimal(price));
        this.setQuantity(Integer.valueOf(quantity));
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) throws Exception {
        if (title == null || title.trim().isEmpty()) {
            throw new Exception("invalid title");
        }
        this.title = title;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) throws Exception {
        if (title == null || author.trim().isEmpty()) {
            throw new Exception("invalid author");
        }
        this.author = author;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) throws Exception {
        if (price.compareTo(BigDecimal.ZERO) < 0) { // price can be zero, for instance free book
            throw new Exception("invalid price");
        }
        this.price = price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) throws Exception {
        if (quantity < 0) { // quantity of new book can be zero
            throw new Exception("invalid quantity");
        }
        this.quantity = quantity;
    }

    @Override
    public int hashCode() {
        return this.author.toLowerCase().hashCode() + this.title.toLowerCase().hashCode() + this.price.toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Book) {
            return this.hashCode() == obj.hashCode();
        }
        return false;
    }
}

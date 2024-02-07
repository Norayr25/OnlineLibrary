package com.Library.services.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Entity class representing a book in the library system.
 */
@Setter
@Getter
@Entity
@DiscriminatorValue("book")
public class Book extends Item {
    private String title;
    private String author;
    private String genre;
    private String description;
    private String isbn;
    private String image;
    private LocalDate published;
    private String publisher;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Book other = (Book) obj;
        return Objects.equals(title, other.title) && Objects.equals(author, other.author) &&
                Objects.equals(genre, other.genre) && Objects.equals(description, other.description) &&
                Objects.equals(isbn, other.isbn) && Objects.equals(image, other.image) &&
                Objects.equals(published, other.published) && Objects.equals(publisher, other.publisher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, genre, description, isbn, image, published, publisher);
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", description='" + description + '\'' +
                ", isbn='" + isbn + '\'' +
                ", image='" + image + '\'' +
                ", published=" + published +
                ", publisher='" + publisher + '\'' +
                '}';
    }
}

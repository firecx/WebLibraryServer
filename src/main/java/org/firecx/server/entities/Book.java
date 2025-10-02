package org.firecx.server.entities;

import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "books")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "series", nullable = false, unique = false)
    private String series;

    @Column(name = "name", nullable = false, unique = false)
    private String name;

    @Column(name = "volume", nullable = false, unique = false)
    private Integer volume;

    //переделать cascade
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "authors_id", foreignKey = @ForeignKey(name = "fk_books_authors_id"))
    private Author author;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return (series + name + volume.toString()).equals(book.series + book.name + book.volume.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(series + name + volume.toString());
    }

    @Override
    public String toString() {
        return "Book{"
        + "id=" + id
        + ", series=" + series
        + ", name=" + name
        + ", volume=" + volume.toString()
        + "}";
    }
}

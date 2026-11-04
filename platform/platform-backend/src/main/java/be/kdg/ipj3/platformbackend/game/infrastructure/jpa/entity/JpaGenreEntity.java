package be.kdg.ipj3.platformbackend.game.infrastructure.jpa.entity;

import be.kdg.ipj3.platformbackend.game.domain.Genre;
import jakarta.persistence.*;

@Entity
@Table(name = "genres")
public class JpaGenreEntity {


    @Id
    private String name;

    @Column
    private String description;

    public JpaGenreEntity() {
    }

    public JpaGenreEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static JpaGenreEntity fromDomain(Genre genre) {
        return new JpaGenreEntity(
                genre.getName(),
                genre.getDescription()
        );
    }

    public Genre toDomain() {
        return new Genre(
                this.name,
                this.description
        );
    }
}

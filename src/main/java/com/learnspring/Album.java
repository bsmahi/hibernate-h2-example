package com.learnspring;

import jakarta.persistence.*;

@Entity
@Table(name="album")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "album_name")
    private String name;

    @Column(name = "released_year")
    private String releasedYear;

    public Album() {
    }

    public Album(String name, String releasedYear) {
        this.name = name;
        this.releasedYear = releasedYear;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleasedYear() {
        return releasedYear;
    }

    public void setReleasedYear(String releasedYear) {
        this.releasedYear = releasedYear;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", releasedYear='" + releasedYear + '\'' +
                '}';
    }
}

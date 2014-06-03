package com.vds.bases.entities;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ulysse on 02/06/2014.
 */

@DatabaseTable(tableName = "films")
public class Film {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private int support;

    @DatabaseField
    private int genre;

    @DatabaseField
    private int date;

    @DatabaseField
    private int real;

    @DatabaseField
    private String title;

    @DatabaseField
    private String origin_title;

    public Film() {
        // ORMLite needs a no-arg constructor
    }
    public Film(int id, int support, int genre, int date, int real, String title, String origin_title) {
        this.id = id;
        this. support = support;
        this.genre = genre;
        this.date = date;
        this.real = real;
        this.title = title;
        this.origin_title = origin_title;
    }
    //Original title is optionnal
    public Film(int id, int support, int genre, int date, int real, String title) {
        this(id, support, genre, date, real, title, "");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSupport() {
        return support;
    }

    public void setSupport(int support) {
        this.support = support;
    }

    public int getGenre() {
        return genre;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getReal() {
        return real;
    }

    public void setReal(int real) {
        this.real = real;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrigin_title() {
        return origin_title;
    }

    public void setOrigin_title(String origin_title) {
        this.origin_title = origin_title;
    }
}


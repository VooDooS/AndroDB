package com.vds.bases.entities;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ulysse on 02/06/2014.
 */

@DatabaseTable(tableName = "acteurs")
public class Acteur {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String nom;

    @DatabaseField
    private String prenom;

    public Acteur() {
        // ORMLite needs a no-arg constructor
    }
    public Acteur(int id, String nom, String prenom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}


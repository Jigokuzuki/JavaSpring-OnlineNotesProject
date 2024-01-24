package com.example.onlinenotes.Entities;

import java.time.OffsetDateTime;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

@Entity
@Table(name = "Notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getId() {
        return Id;
    }

    @Column(name = "Title")
    @Nonnull
    private String Title;

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getTitle() {
        return Title;
    }

    @Column(name = "Content")
    @Nonnull
    private String Content;

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getContent() {
        return Content;
    }

    @Column(name = "Category")
    @Nonnull
    private String Category;

    public void setCategory(String Category) {
        this.Category = Category;
    }

    public String getCategory() {
        return Category;
    }

    @Column(name = "CreatedDate")
    private OffsetDateTime CreatedDate;

    public void setCreatedDate(OffsetDateTime CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public OffsetDateTime getCreatedDate() {
        return CreatedDate;
    }

    @Column(name = "ModifiedDate")
    private OffsetDateTime ModifiedDate;

    public void setModifiedDate(OffsetDateTime ModifiedDate) {
        this.ModifiedDate = ModifiedDate;
    }

    public OffsetDateTime getModifiedDate() {
        return ModifiedDate;
    }

    @Column(name = "IsFavorite")
    private boolean IsFavorite;

    public void setIsFavorite(boolean IsFavorite) {
        this.IsFavorite = IsFavorite;
    }

    public boolean getIsFavorite() {
        return IsFavorite;
    }

    @Column(name = "Color")
    @Nonnull
    private String Color;

    public void setColor(String Color) {
        this.Color = Color;
    }

    public String getColor() {
        return Color;
    }
}

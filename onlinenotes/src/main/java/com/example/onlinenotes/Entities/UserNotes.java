package com.example.onlinenotes.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "UserNotes")
public class UserNotes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getId() {
        return Id;
    }

    @Column(name = "NoteId")
    private int NoteId;

    public void setNoteId(int NoteId) {
        this.NoteId = NoteId;
    }

    public int getNoteId() {
        return NoteId;
    }

    @Column(name = "UserId")
    private int UserId;

    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    public int getUserId() {
        return UserId;
    }
}

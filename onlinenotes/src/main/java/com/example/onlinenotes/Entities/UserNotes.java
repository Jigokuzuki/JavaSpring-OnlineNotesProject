package com.example.onlinenotes.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "UserNotes")
public class UserNotes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getId() {
        return Id;
    }

    @Column(name = "NoteId")
    @ManyToOne
    @JoinColumn(name = "Notes", referencedColumnName = "Id")
    private Note NoteId;

    public void setNoteId(Note NoteId) {
        this.NoteId = NoteId;
    }

    public Note getNoteId() {
        return NoteId;
    }

    @Column(name = "UserId")
    @ManyToOne
    @JoinColumn(name = "Users", referencedColumnName = "Id")
    private User UserId;

    public void setUserId(User UserId) {
        this.UserId = UserId;
    }

    public User getUserId() {
        return UserId;
    }
}

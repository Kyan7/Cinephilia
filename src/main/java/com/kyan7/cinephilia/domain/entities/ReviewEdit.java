package com.kyan7.cinephilia.domain.entities;

import javax.persistence.*;

@Entity
@Table(name = "review_edits")
public class ReviewEdit extends Edit {

    private Review review;
    private User user;
    private String editedTitle;
    private long editedRating;
    private String editedDescription;
    private boolean isByAdmin;

    public ReviewEdit() {
    }

    @ManyToOne
    @JoinColumn(name = "review_id", referencedColumnName = "id")
    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "edited_title", updatable = false)
    public String getEditedTitle() {
        return editedTitle;
    }

    public void setEditedTitle(String editedTitle) {
        this.editedTitle = editedTitle;
    }

    @Column(name = "edited_rating", updatable = false)
    public long getEditedRating() {
        return editedRating;
    }

    public void setEditedRating(long editedRating) {
        this.editedRating = editedRating;
    }

    @Column(name = "edited_description", columnDefinition = "TEXT", updatable = false)
    public String getEditedDescription() {
        return editedDescription;
    }

    public void setEditedDescription(String editedDescription) {
        this.editedDescription = editedDescription;
    }

    @Column(name = "is_by_admin", updatable = false)
    public boolean isByAdmin() {
        return isByAdmin;
    }

    public void setByAdmin(boolean byAdmin) {
        isByAdmin = byAdmin;
    }
}

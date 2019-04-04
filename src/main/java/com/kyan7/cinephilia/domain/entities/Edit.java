package com.kyan7.cinephilia.domain.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Edit {

    private String id;
    private String editorComments;
    private String timeStamp;

    public Edit() {
    }

    @Id
    @GeneratedValue(generator = "uuid-string")
    @GenericGenerator(
            name = "uuid-string",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "editor_comments", columnDefinition = "TEXT")
    public String getEditorComments() {
        return editorComments;
    }

    public void setEditorComments(String editorComments) {
        this.editorComments = editorComments;
    }

    @Column(name = "time_stamp", nullable = false, updatable = false)
    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}

package com.kyan7.cinephilia.domain.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "article_edits")
public class ArticleEdit extends Edit {

    public User user;
    public String description;


}

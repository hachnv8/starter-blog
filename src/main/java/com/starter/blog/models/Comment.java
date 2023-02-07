package com.starter.blog.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.starter.blog.repositories.CommentRepository;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by HachNV on Feb 07, 2023
 */

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
public class Comment {

    @Autowired
    @Transient
    private CommentRepository commentRepository;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String body;

    @ManyToOne
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JsonManagedReference
    private Comment parent;

    @ManyToOne
    @JsonManagedReference
    private Post post;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column()
    private Date createdAt;

    @OneToMany(mappedBy = "parent")
    @JsonBackReference
    private List<Comment> children;

    public Comment(long id, String body, User user, Comment parent, Post post, Date createdAt) {
        this.id = id;
        this.body = body;
        this.user = user;
        this.parent = parent;
        this.post = post;
        this.createdAt = createdAt;
        setChildren();
    }

    public void setChildren() {
        System.out.println("setChildren ran");
        this.children = commentRepository.findByParent(this);
    }
}

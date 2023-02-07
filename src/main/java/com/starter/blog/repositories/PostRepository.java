package com.starter.blog.repositories;

import com.starter.blog.models.Post;
import com.starter.blog.models.Tag;
import com.starter.blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by HachNV on Feb 07, 2023
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Post findByUser(User user);

    List<Post> findByBodyIsLikeOrTitleIsLike(String term, String term2);

    List<Post> findByCreateDateBetween(Date from, Date to);

    @Query("select p.createDate from Post p")
    List<Date> createDates();

    @Query("select p from Post p order by p.id desc")
    List<Post> getAllPosts();

    List<Post> findAllByTagsIn(List<Tag> tags);

}

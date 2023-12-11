package com.example.traineejava.repo;

import com.example.traineejava.models.Cafe;
import com.example.traineejava.models.Comment;
import com.example.traineejava.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> findByCafe(Cafe cafe);

    List<Comment> findByUser(User user);

    Comment findByDescription(String description);
}

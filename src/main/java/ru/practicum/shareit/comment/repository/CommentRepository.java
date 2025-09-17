package ru.practicum.shareit.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.comment.model.Comment;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Collection<Comment> findAllByItemId(Long id);

}


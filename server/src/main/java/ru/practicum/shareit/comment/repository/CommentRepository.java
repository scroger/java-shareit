package ru.practicum.shareit.comment.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.practicum.shareit.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Collection<Comment> findAllByItemId(Long id);

}


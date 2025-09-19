package ru.practicum.shareit.item.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.practicum.shareit.item.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Collection<Item> findByOwnerId(Long userId);

    Collection<Item> findByAvailableTrueAndNameIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(
            String name,
            String description
    );

    Collection<Item> findAllByRequestId(Long requestId);

    Collection<Item> findAllByRequestIdIn(Collection<Long> requestIds);

}

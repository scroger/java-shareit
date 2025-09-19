package ru.practicum.shareit.item.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ru.practicum.shareit.item.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Collection<Item> findByOwnerId(Long userId);

    @Query("select i from Item as i where i.available = true and " +
            "(lower(i.name) like lower(concat('%', ?1, '%')) or " +
            "lower(i.description) like lower(concat('%', ?1, '%')))")
    Collection<Item> findBySearchText(String searchText);

    Collection<Item> findByAvailableTrueAndNameIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(String name, String description); //TODO use it instead of findBySearchText

    Collection<Item> findAllByRequestId(Long requestId);

    Collection<Item> findAllByRequestIdIn(Collection<Long> requestIds);

}

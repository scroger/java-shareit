package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingStateDto;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Collection<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId);

    Collection<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
            Long bookerId,
            LocalDate start,
            LocalDate end
    );

    Collection<Booking> findAllByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDate end);

    Collection<Booking> findAllByBookerIdAndStartIsAfterOrderByStartDesc(Long bookerId, LocalDate start);

    Collection<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStateDto state);

    Collection<Booking> findAllByItemOwnerIdOrderByStartDesc(Long ownerId);

    Collection<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
            Long ownerId,
            LocalDate start,
            LocalDate end
    );

    Collection<Booking> findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Long ownerId, LocalDate end);

    Collection<Booking> findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(Long ownerId, LocalDate start);

    Collection<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStateDto state);

    Optional<Booking> findFirstByItemIdAndEndIsBeforeAndStatusOrderByEndDesc(
            Long itemId,
            LocalDateTime now,
            Booking.Status status
    );

    Optional<Booking> findFirstByItemIdAndStartIsAfterAndStatusOrderByStartAsc(
            Long itemId,
            LocalDateTime now,
            Booking.Status status
    );

    boolean existsByBookerIdAndItemIdAndEndIsBeforeAndStatus(
            Long bookerId,
            Long itemId,
            LocalDateTime now,
            Booking.Status status
    );

}

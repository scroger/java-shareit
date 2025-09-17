package ru.practicum.shareit.item.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.error.exception.ForbiddenException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.dto.CreateItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    public final ItemRepository itemRepository;
    public final UserRepository userRepository;
    public final BookingRepository bookingRepository;
    public final CommentRepository commentRepository;

    @Override
    public Collection<Item> getAll(Long userId) {
        return itemRepository.findByOwnerId(userId);
    }

    @Override
    public Item getById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id=%d not found", itemId)));
    }

    @Override
    public ItemResponseDto getById(Long itemId, Long currentUserId) {
        Item item = getById(itemId);

        LocalDateTime lastBooking = null;
        LocalDateTime nextBooking = null;
        if (Objects.equals(currentUserId, item.getOwner().getId())) {
            LocalDateTime now = LocalDateTime.now();
            lastBooking = bookingRepository.findFirstByItemIdAndEndIsBeforeAndStatusOrderByEndDesc(
                    itemId,
                    now,
                    BookingStatus.APPROVED
            ).map(Booking::getEnd).orElse(null);
            nextBooking = bookingRepository.findFirstByItemIdAndStartIsAfterAndStatusOrderByStartAsc(
                    itemId,
                    now,
                    BookingStatus.APPROVED
            ).map(Booking::getStart).orElse(null);
        }

        return ItemMapper.map(item).toBuilder()
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(commentRepository.findAllByItemId(item.getId())
                        .stream()
                        .map(CommentMapper::map)
                        .toList())
                .build();
    }

    @Override
    public Item create(Long userId, CreateItemRequestDto createItemRequestDto) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d not found", userId)));

        return itemRepository.save(ItemMapper.map(owner, createItemRequestDto));
    }

    @Override
    public Item update(Long userId, Long itemId, UpdateItemRequestDto updateItemRequestDto) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d not found", userId)));

        Item item = getById(itemId);

        if (!Objects.equals(userId, item.getOwner().getId())) {
            throw new ForbiddenException("Forbidden");
        }

        return itemRepository.save(ItemMapper.map(updateItemRequestDto, item));
    }

    @Override
    public Collection<Item> search(String searchText) {
        if (searchText.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.findBySearchText(searchText);
    }

    @Override
    public CommentResponseDto addComment(Long itemId, Long userId, CommentRequestDto commentDto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d not found", userId)));

        Item item = getById(itemId);

        if (!bookingRepository.existsByBookerIdAndItemIdAndEndIsBeforeAndStatus(
                author.getId(),
                item.getId(),
                LocalDateTime.now(),
                BookingStatus.APPROVED
        )) {
            throw new ValidationException("Forbidden");
        }

        return CommentMapper.map(
                commentRepository.save(
                        Comment.builder()
                                .author(author)
                                .item(item)
                                .text(commentDto.text())
                                .created(LocalDate.now())
                                .build()
                )
        );
    }
}

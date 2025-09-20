package ru.practicum.shareit.booking.service;

import java.time.LocalDateTime;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.CreateItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.CreateUserRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceIntegrationTest {

    private final EntityManager em;
    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;

    @Test
    void testCreateBooking() {
        User user = userService.create(CreateUserRequestDto.builder()
                .name("test")
                .email("test@test.ru")
                .build());

        Item item = itemService.create(user.getId(), CreateItemRequestDto.builder()
                .name("test")
                .description("test")
                .available(true)
                .build());

        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = start.plusHours(1);
        bookingService.create(CreateBookingRequestDto.builder()
                .start(start)
                .end(end)
                .itemId(item.getId())
                .build(), user.getId());
        TypedQuery<Booking> bookingQuery = em.createQuery("select b from Booking b where b.item.id = :id", Booking.class);
        Booking booking = bookingQuery.setParameter("id", item.getId()).getSingleResult();

        MatcherAssert.assertThat(booking, Matchers.notNullValue());
        MatcherAssert.assertThat(booking.getId(), Matchers.notNullValue());
        MatcherAssert.assertThat(booking.getStart(), Matchers.equalTo(start));
        MatcherAssert.assertThat(booking.getEnd(), Matchers.equalTo(end));
        MatcherAssert.assertThat(booking.getBooker().getId(), Matchers.equalTo(user.getId()));
        MatcherAssert.assertThat(booking.getItem().getId(), Matchers.equalTo(item.getId()));
        MatcherAssert.assertThat(booking.getStatus(), Matchers.equalTo(BookingStatus.WAITING));

        // approve
        bookingService.approve(user.getId(), booking.getId(), true);

        MatcherAssert.assertThat(booking.getStatus(), Matchers.equalTo(BookingStatus.APPROVED));

        // add comment
        itemService.addComment(item.getId(), user.getId(), CommentRequestDto.builder()
                .text("test comment")
                .build());
        TypedQuery<Comment> commentQuery = em.createQuery("select c from Comment c where c.item.id = :id", Comment.class);
        Comment comment = commentQuery.setParameter("id", item.getId()).getSingleResult();

        MatcherAssert.assertThat(comment, Matchers.notNullValue());
        MatcherAssert.assertThat(comment.getText(), Matchers.notNullValue());
        MatcherAssert.assertThat(comment.getText(), Matchers.equalTo("test comment"));
    }
}
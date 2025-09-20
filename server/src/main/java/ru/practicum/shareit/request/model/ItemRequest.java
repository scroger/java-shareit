package ru.practicum.shareit.request.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

@Entity
@Table(schema = "public", name = "requests")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "requestor_id", insertable = false, updatable = false)
    private Long requestorId;

    @ManyToOne
    @JoinColumn(name = "requestor_id")
    @JsonIgnore
    private User requestor;

    @Column(name = "create_date")
    private LocalDateTime created;

}

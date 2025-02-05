package com.example.newsService.core.reaction.entities;

import java.util.UUID;

import org.hibernate.mapping.Column;

import com.example.newsService.core.Reaction;
import com.example.newsService.core.utils.ReactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "reactions")
public class ReactionEntity extends Reaction {
    @Id
    @GeneratedValue
    private UUID id;
    @jakarta.persistence.Column(name = "reaction", unique = true)
    @Enumerated(EnumType.STRING)
    private ReactionType reaction;
}
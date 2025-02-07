package com.example.newsService.core.reaction.entities;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;
=======
>>>>>>> a6b5e9255c615854e7d1d1469838473ef4a9d732
import java.util.UUID;

import org.hibernate.mapping.Column;

import com.example.newsService.core.Reaction;
import com.example.newsService.core.utils.ReactionType;

<<<<<<< HEAD
import jakarta.persistence.CascadeType;
=======
>>>>>>> a6b5e9255c615854e7d1d1469838473ef4a9d732
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
<<<<<<< HEAD
import jakarta.persistence.OneToMany;
=======
>>>>>>> a6b5e9255c615854e7d1d1469838473ef4a9d732
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
    @jakarta.persistence.Column(name = "reaction_description", unique = true)
    private String description;
}
package com.example.newsService.core;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserReaction extends BaseEntityAudit {

    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "user_id",nullable = false)
    private UUID userId;

}

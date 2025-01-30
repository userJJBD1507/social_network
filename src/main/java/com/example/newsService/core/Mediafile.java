package com.example.newsService.core;

import com.example.newsService.core.utils.MediaFileType;
import jakarta.persistence.*;
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
public class Mediafile {

    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "url")
    private String url;
    @Column(name = "user_id")
    private String userId;
    @Enumerated(EnumType.STRING)
    private MediaFileType type;

}

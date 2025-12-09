package com.ihb.mytalentbackend.domain.talent;

import com.ihb.mytalentbackend.domain.BaseEntity;
import com.ihb.mytalentbackend.domain.UploadFile;
import com.ihb.mytalentbackend.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TalentBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String description;

    @Column(name = "credit_per_hour", nullable = false)
    private Integer creditPerHour;

    @Column(nullable = false)
    private String status;

    // Thumbnail 추가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumbnail_file_id")
    private UploadFile thumbnail;

}


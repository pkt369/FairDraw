package birdjun.profairmanager.drawing.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Contestant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contestant_id")
    private Long id;

    @Column(nullable = false)
    private Long drawingId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Boolean isWinner;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}

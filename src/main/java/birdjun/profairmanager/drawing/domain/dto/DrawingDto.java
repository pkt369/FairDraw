package birdjun.profairmanager.drawing.domain.dto;

import birdjun.profairmanager.drawing.domain.Drawing;
import birdjun.profairmanager.user.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Getter
public class DrawingDto {
    private String name;
    private Integer winnerCount;

    public Drawing toEntity(User user) {
        return Drawing.builder()
                .name(this.name)
                .winnerCount(winnerCount)
                .user(user)
                .build();
    }

    public static DrawingDto toDto(Drawing drawing) {
        return DrawingDto.builder()
                .name(drawing.getName())
                .winnerCount(drawing.getWinnerCount())
                .build();
    }
}

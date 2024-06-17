package birdjun.profairmanager.drawing.domain.dto;

import birdjun.profairmanager.drawing.domain.Drawing;
import birdjun.profairmanager.user.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DrawingDto {
    @NotNull
    private String name;
    @NotNull
    private Integer winnerCount;
    @NotNull
    private Boolean isDuplicated;

    public Drawing toEntity(User user) {
        return Drawing.builder()
                .name(this.name)
                .winnerCount(winnerCount)
                .user(user)
                .isDuplicated(isDuplicated)
                .build();
    }

    public static DrawingDto toDto(Drawing drawing) {
        return DrawingDto.builder()
                .name(drawing.getName())
                .winnerCount(drawing.getWinnerCount())
                .isDuplicated(drawing.getIsDuplicated())
                .build();
    }
}

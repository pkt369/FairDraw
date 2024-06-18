package birdjun.profairmanager.drawing.domain.dto;

import birdjun.profairmanager.drawing.domain.Drawing;
import birdjun.profairmanager.user.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class DrawingDto {
    @NotNull
    private String name;
    @NotNull
    private Integer winnerCount;
    @NotNull
    private List<Long> studentIdList = new ArrayList<>();
    @Builder.Default
    private List<Drawing> removeList = new ArrayList<>();

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

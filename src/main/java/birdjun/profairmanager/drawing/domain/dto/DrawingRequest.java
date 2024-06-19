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
public class DrawingRequest {
    @NotNull
    private String name;
    @NotNull
    private Integer winnerCount;
    @NotNull
    @Builder.Default
    private List<Long> studentIdList = new ArrayList<>();
    @Builder.Default
    private List<Long> removeDrawingIdList = new ArrayList<>();

    public Drawing toEntity(User user) {
        return Drawing.builder()
                .name(this.name)
                .winnerCount(winnerCount)
                .user(user)
                .build();
    }
}

package birdjun.profairmanager.drawing.domain.dto;

import birdjun.profairmanager.drawing.domain.Drawing;
import birdjun.profairmanager.user.dto.StudentDto;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Getter
@Builder
public class DrawingResponse {
    private Long id;
    private String name;
    private Integer winnerCount;
    private String createdAt;
    private List<StudentDto> winnerList;
    private List<StudentDto> LoserList;

    public static DrawingResponse toResponse(Drawing drawing) {
        String time = drawing.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        return DrawingResponse.builder()
                .id(drawing.getId())
                .name(drawing.getName())
                .winnerCount(drawing.getWinnerCount())
                .createdAt(time)
                .build();
    }
}

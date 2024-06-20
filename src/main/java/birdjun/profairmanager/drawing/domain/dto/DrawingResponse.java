package birdjun.profairmanager.drawing.domain.dto;

import birdjun.profairmanager.user.domain.Student;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Data
@Getter
@Builder
public class DrawingResponse {
    private Long id;
    private String name;
    private Integer winnerCount;

    private List<Student> winnerList;
    private List<Student> LoserList;
}

package birdjun.profairmanager.user.dto;

import birdjun.profairmanager.user.domain.DisabledType;
import birdjun.profairmanager.user.domain.Gender;
import birdjun.profairmanager.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class StudentDto {

    private User user;

    private String name;

    private String phone;

    private String guardianName;

    private String guardianPhone;

    private Short age;

    private Gender gender;

    private DisabledType disabledType;
}

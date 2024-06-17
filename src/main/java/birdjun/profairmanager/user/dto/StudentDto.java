package birdjun.profairmanager.user.dto;

import birdjun.profairmanager.user.domain.DisabledType;
import birdjun.profairmanager.user.domain.Gender;
import birdjun.profairmanager.user.domain.Student;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
@ToString
public class StudentDto {

    private Long id;

    @NotNull
    private String name;

    private String phone;

    private String email;

    private String guardianName;

    private String guardianPhone;

    @NotNull
    private String birth;

    @NotNull
    private Gender gender;

    @NotNull
    private DisabledType disabledType;

    public Student toEntity() {
        return Student.builder()
                .name(this.name)
                .phone(this.phone)
                .email(this.email)
                .guardianName(this.guardianName)
                .guardianPhone(this.guardianPhone)
                .birth(this.birth)
                .gender(this.gender)
                .disabledType(this.disabledType)
                .build();
    }

    public static StudentDto fromEntity(Student student) {
        return StudentDto.builder()
                .id(student.getId())
                .name(student.getName())
                .phone(student.getPhone())
                .email(student.getEmail())
                .guardianName(student.getGuardianName())
                .guardianPhone(student.getGuardianPhone())
                .birth(student.getBirth())
                .gender(student.getGender())
                .disabledType(student.getDisabledType())
                .build();
    }
}

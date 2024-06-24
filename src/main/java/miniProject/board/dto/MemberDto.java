package miniProject.board.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    @NotEmpty
    private Long id;

    @NotEmpty
    private String userId;

    @NotEmpty
    private String nickname;

    @NotEmpty
    @Size(min = 8, max = 15)
    private String password;

    private String description;

    @Email
    private String email;
}

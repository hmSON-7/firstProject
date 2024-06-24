package miniProject.board.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginDto {
    @NotEmpty
    private String userId;

    @NotEmpty
    @Size(min = 8, max = 15)
    private String password;
}
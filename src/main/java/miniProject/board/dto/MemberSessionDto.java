package miniProject.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 세션에 멤버 값을 저장하기 위해 사용하는 DTO 입니다.
 */
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberSessionDto {
    /**
     * 멤버를 식별하기 위한 ID 값
     */
    private Long id;
}
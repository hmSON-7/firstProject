package miniProject.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 세션에 관리자 값을 저장하기 위해 사용하는 DTO 입니다.
 */
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminSessionDto {
    /**
     * 관리자를 식별하기 위한 ID 값
     */
    private Long id;
}
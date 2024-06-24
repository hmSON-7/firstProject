package miniProject.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import miniProject.board.entity.Admin;
import miniProject.board.entity.Article;

@Getter @Setter
@AllArgsConstructor
public class AdminDto {
    private Long id;
    private String name;
    private String password;
}
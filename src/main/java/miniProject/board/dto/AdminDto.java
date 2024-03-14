package miniProject.board.dto;

import lombok.AllArgsConstructor;
import miniProject.board.entity.Admin;
import miniProject.board.entity.Article;

@AllArgsConstructor
public class AdminDto {
    private long id;
    private String name;
    private String password;

    public Admin toEntity() {
        return new Admin(id, name, password);
    }
}

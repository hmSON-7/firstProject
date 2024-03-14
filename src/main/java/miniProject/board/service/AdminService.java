package miniProject.board.service;

import lombok.extern.slf4j.Slf4j;
import miniProject.board.dto.AdminDto;
import miniProject.board.entity.Admin;
import miniProject.board.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service

public class AdminService {
    @Autowired
    private AdminRepository adminRepository;


    public AdminDto login(AdminDto adminDto, String name, String password){

        Admin admin = adminDto.toEntity();
        Admin target = adminRepository.findByName(name);
        if (target != null) {
            // 비밀번호 비교
            if (password.equals(admin.getPassword())) {
                return null;// 비밀번호 일치
            }
        }

        // 로그인 실패
        return null;
    }
}

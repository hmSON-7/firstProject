package miniProject.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.dto.AdminLoginDto;
import miniProject.board.dto.AdminSessionDto;
import miniProject.board.entity.Admin;
import miniProject.board.repository.AdminRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminSessionDto login(AdminLoginDto adminLoginDto){
        Admin admin = adminRepository.findByName(adminLoginDto.getName());

        if (admin == null) {
            return null;
        }

        if (passwordEncoder.matches(adminLoginDto.getPassword(), admin.getPassword())) {
            return new AdminSessionDto(admin.getId());
        }

        return null;
    }
}
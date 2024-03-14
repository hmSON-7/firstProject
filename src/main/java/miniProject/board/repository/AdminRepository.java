package miniProject.board.repository;

import miniProject.board.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRepository extends JpaRepository<Admin, Long>{

    Admin findByName(String name);
}

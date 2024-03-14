package miniProject.board.repository;

import miniProject.board.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Member, Long>{

}

package miniProject.board.service.auth;

import lombok.RequiredArgsConstructor;
import miniProject.board.dto.CustomMemberDetails;
import miniProject.board.entity.Member;
import miniProject.board.repository.MemberRepository;
import miniProject.board.service.member.MemberSuspensionService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final MemberSuspensionService memberSuspensionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByUsername(username);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            return new CustomMemberDetails(memberSuspensionService, member);
        }

        return null;
    }
}

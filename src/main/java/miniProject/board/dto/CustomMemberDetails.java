package miniProject.board.dto;

import lombok.RequiredArgsConstructor;
import miniProject.board.auth.constants.Status;
import miniProject.board.entity.Member;
import miniProject.board.service.member.MemberSuspensionService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class CustomMemberDetails implements UserDetails {

    private final MemberSuspensionService memberSuspensionService;
    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRole().name();
            }
        });

        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        LocalDateTime now = LocalDateTime.now();

        if (member.getStatus() == Status.PERMANENT_SUSPENDED) {
            return false;
        }

        if (member.getStatus() == Status.TEMPORARY_SUSPENDED) {

            if (now.isAfter(member.getLockExpirationTime())) {
                memberSuspensionService.activeMember(member.getId());

                return true;
            }

            return false;
        }

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

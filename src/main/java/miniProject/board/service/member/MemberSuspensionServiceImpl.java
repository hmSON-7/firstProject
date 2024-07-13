package miniProject.board.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.entity.Member;
import miniProject.board.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberSuspensionServiceImpl implements MemberSuspensionService {

    private final MemberRepository memberRepository;

    @Override
    public void applyPermanentSuspension(Long memberId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member Id:" + memberId));

        member.suspendPermanently();
    }

    @Override
    public void applyTemporarySuspension(Long memberId, Long days) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member Id:" + memberId));

        member.suspendTemporarily(days);
    }

    @Override
    public void activeMember(Long memberId) {
        memberRepository.findById(memberId).ifPresent(Member::activate);
    }
}

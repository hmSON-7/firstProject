package miniProject.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.dto.MemberAddDto;
import miniProject.board.dto.MemberDto;
import miniProject.board.dto.MemberLoginDto;
import miniProject.board.entity.Member;
import miniProject.board.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;

    @Override
    public MemberDto signUp(MemberAddDto memberAddDto) {
        Member member = convertToDao(memberAddDto);

        memberRepository.save(member);

        return convertToDto(member);
    }

    @Override
    public MemberDto login(MemberLoginDto memberLoginDto) {
        return null;
    }

    @Override
    public MemberDto findMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);

        return convertToDto(member);
    }

    @Override
    public List<MemberDto> findMembers() {
        List<Member> memberList = memberRepository.findAll();

        return memberList.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public MemberDto convertToDto(Member member) {
        return new MemberDto(member.getId(),
                member.getUserId(),
                member.getNickname(),
                member.getPassword(),
                member.getDescription(),
                member.getEmail());
    }

    @Override
    public Member convertToDao(MemberAddDto memberAddDto) {
        return new Member(memberAddDto.getUserId(),
                memberAddDto.getPassword(),
                memberAddDto.getPassword());
    }
}
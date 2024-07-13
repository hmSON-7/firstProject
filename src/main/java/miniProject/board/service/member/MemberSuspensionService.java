package miniProject.board.service.member;

/**
 * Member 정지 관련 작업을 처리하는 서비스 인터페이스입니다.
 */
public interface MemberSuspensionService {
    /**
     * Member를 영구 정지 처리합니다.
     *
     * @param memberId 정지할 Member ID
     * @throws IllegalArgumentException Member ID가 유효하지 않음
     */
    void applyPermanentSuspension(Long memberId);

    /**
     * Member를 임시 정지 처리합니다.
     *
     * @param memberId 정지할 Member ID
     * @param days 정지 기간(일 단위)
     * @throws IllegalArgumentException Member ID가 유효하지 않은 경우
     */
    void applyTemporarySuspension(Long memberId, Long days);

    /**
     * 정지된 Member를 활성화합니다.
     *
     * @param memberId 활성화할 Member ID
     */
    void activeMember(Long memberId);
}

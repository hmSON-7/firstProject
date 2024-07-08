package miniProject.board.service.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final DirectoryStorageService directoryStorageService;

    /**
     * 파일 생성 메서드
     * @param username 디렉토리 이름과 동일
     * @param content 파일 내용
     * @param uuid 파일 이름으로 사용
     * @return 생성된 파일의 경로를 나타내는 문자열
     */
    public String createFile(String username, String content, UUID uuid) {
        try {
            // 1. username으로 디렉토리 경로 찾기
            Path directoryPath = directoryStorageService.findPath(username);
            // 2. 파일 경로 설정
            Path filePath = directoryPath.resolve(username + "_" + uuid.toString() + ".txt");

            // 3. 파일 생성 및 내용 쓰기
            Files.write(filePath, content.getBytes(), StandardOpenOption.CREATE_NEW);

            // 4. 파일 경로 반환
            return filePath.toString();

        } catch (NoSuchFileException e) {
            log.error("디렉토리를 찾을 수 없습니다: {}", e.getMessage());
        } catch (IOException e) {
            log.error("파일 생성 중 오류가 발생했습니다.", e);
        }

        return null;
    }

    /**
     * 파일 조회 메서드
     * @param filePath 파일 탐색에 사용할 경로
     * @return 파일을 찾으면 내용을 문자열로 바꿔서 반환
     */
    public String readFile(String filePath) {
        if (filePath == null) {
            log.error("파일 경로가 null입니다.");
            return null;
        }
        try {
            Path path = Paths.get(filePath);

            // 파일 내용 읽기
            return new String(Files.readAllBytes(path));
        } catch (NoSuchFileException e) {
            log.error("파일을 찾을 수 없습니다: {}", e.getMessage());
        } catch (IOException e) {
            log.error("파일 읽기 중 오류가 발생했습니다.", e);
        }

        return null;
    }

    /**
     * 파일 업데이트 메서드
     * @param filePath 파일 경로
     * @param content 새 파일 내용
     */
    public void updateFile(String filePath, String content) {
        if (filePath == null) {
            log.error("파일 경로가 null입니다.");
            return;
        }
        try {
            Path path = Paths.get(filePath);

            Files.write(path, content.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (NoSuchFileException e) {
            log.error("파일을 찾을 수 없습니다: {}", e.getMessage());
        } catch (IOException e) {
            log.error("파일 업데이트 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 파일 삭제 메서드
     * @param filePath 파일 경로
     */
    public void deleteFile(String filePath) {
        if (filePath == null) {
            log.error("파일 경로가 null입니다.");
            return;
        }
        try {
            Path path = Paths.get(filePath);

            Files.delete(path);
        } catch (NoSuchFileException e) {
            log.error("파일을 찾을 수 없습니다: {}", e.getMessage());
        } catch (IOException e) {
            log.error("파일 삭제 중 오류가 발생했습니다.", e);
        }
    }

}
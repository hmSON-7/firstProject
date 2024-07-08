package miniProject.board.service.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Service
@Slf4j
public class DirectoryStorageService {

    public Path findPath(String username) {
        String projectRoot = System.getProperty("user.dir"); // 프로젝트 루드 디렉토리 반환
        Path path = Paths.get(projectRoot, "src", "main", "resources", "content", username);
        // root_directory/src/main/resources/content/{username}
        log.info("Computed directory path: {}", path.toString());
        return path;
    }

    /**
     * 디렉토리 생성 메서드. 회원가입 처리 후 디렉토리 생성
     * @param username 디렉토리의 이름은 username과 동일
     */
    public void createDir(String username) {
        Path directoryPath = findPath(username);

        try {
            // Directory 생성
            Files.createDirectories(directoryPath);
            log.info("{} 디렉토리가 생성되었습니다.", directoryPath);

        } catch (FileAlreadyExistsException e) {
            log.warn("디렉토리가 이미 존재합니다: {}", directoryPath);
        } catch (NoSuchFileException e) {
            log.error("디렉토리 경로가 존재하지 않습니다: {}", directoryPath);
        } catch (IOException e) {
            log.error("디렉토리를 생성하는 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 디렉토리 삭제 메서드. 회원 탈퇴 처리 후 디렉토리 삭제
     * @param username 디렉토리의 이름은 username과 동일
     */
    public void deleteDir(String username) {
        Path directoryPath = findPath(username);

        try {
            File directory = directoryPath.toFile();
            if (directory.exists()) {
                // 디렉토리 내부의 모든 파일 및 서브 디렉토리 삭제
                FileUtils.cleanDirectory(directory);
                // 디렉토리 자체 삭제
                FileUtils.deleteDirectory(directory);
                log.info("{} 디렉토리가 삭제되었습니다.", directoryPath);

            } else {
                log.warn("디렉토리를 찾을 수 없습니다: {}", directoryPath);
            }
        } catch (IOException e) {
            log.error("디렉토리를 삭제하는 중 오류가 발생했습니다.", e);
        }
    }

}
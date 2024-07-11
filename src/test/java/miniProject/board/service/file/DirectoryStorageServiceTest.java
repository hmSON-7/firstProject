package miniProject.board.service.file;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class DirectoryStorageServiceTest {

    @Autowired
    private DirectoryStorageService directoryStorageService;
    private static final String TEST_USERNAME = "testUser";

    @BeforeEach
    void setUp() {
        directoryStorageService = new DirectoryStorageService();
    }

    @AfterEach
    void cleanUp() throws IOException {
        // 테스트 후 디렉토리 정리
        Path testDirPath = directoryStorageService.findPath(TEST_USERNAME);
        if (testDirPath != null) {
            FileUtils.deleteDirectory(testDirPath.toFile());
        }
    }

    @Test
    @DisplayName("디렉토리 생성 테스트")
    void createDir() {
        // given

        // when
        directoryStorageService.createDir(TEST_USERNAME);

        // then
        Path dirPath = directoryStorageService.createPath(TEST_USERNAME);
        assertTrue(Files.exists(dirPath) && Files.isDirectory(dirPath));
    }

    @Test
    @DisplayName("디렉토리 중복 생성 테스트")
    void createExistingDir() {
        // given
        boolean firstAttempt = directoryStorageService.createDir(TEST_USERNAME);

        // when
        boolean secondAttempt = directoryStorageService.createDir(TEST_USERNAME);

        // then
        assertTrue(firstAttempt);
        assertFalse(secondAttempt);
    }

    @Test
    @DisplayName("디렉토리 삭제 테스트")
    void deleteDir() {
        // given
        directoryStorageService.createDir(TEST_USERNAME);

        // when
        boolean attempt = directoryStorageService.deleteDir(TEST_USERNAME);

        // then
        assertTrue(attempt);
    }

    @Test
    @DisplayName("존재하지 않는 디렉토리 삭제 테스트")
    void deleteNonExistentDir() {
        // given

        // when
        boolean attempt = directoryStorageService.deleteDir(TEST_USERNAME);

        // then
        assertFalse(attempt);
    }

    @Test
    @DisplayName("디렉토리 찾기 테스트")
    void findDir() throws IOException {
        // given
        directoryStorageService.createDir(TEST_USERNAME);

        // when
        Path dirPath = directoryStorageService.findPath(TEST_USERNAME);

        // then
        assertTrue(Files.exists(dirPath) && Files.isDirectory(dirPath));
    }

    @Test
    @DisplayName("존재하지 않는 디렉토리 찾기 실패 테스트")
    void findNonExistentDir() {
        // given

        // when
        Path dirPath = directoryStorageService.createPath(TEST_USERNAME);

        // then
        assertFalse(Files.exists(dirPath));
    }
}

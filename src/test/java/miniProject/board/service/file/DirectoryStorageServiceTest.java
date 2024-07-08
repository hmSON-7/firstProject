package miniProject.board.service.file;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class DirectoryStorageServiceTest {

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
        if (Files.exists(testDirPath)) {
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
        Path dirPath = directoryStorageService.findPath(TEST_USERNAME);
        assertTrue(Files.exists(dirPath) && Files.isDirectory(dirPath));
    }

    @Test
    @DisplayName("디렉토리 중복 생성 테스트")
    void createExistingDir() {
        // given
        directoryStorageService.createDir(TEST_USERNAME);

        // when
        directoryStorageService.createDir(TEST_USERNAME);

        // then
        Path dirPath = directoryStorageService.findPath(TEST_USERNAME);
        assertTrue(Files.exists(dirPath) && Files.isDirectory(dirPath));
    }

    @Test
    @DisplayName("디렉토리 삭제 테스트")
    void deleteDir() {
        // given
        directoryStorageService.createDir(TEST_USERNAME);

        // when
        directoryStorageService.deleteDir(TEST_USERNAME);

        // then
        Path dirPath = directoryStorageService.findPath(TEST_USERNAME);
        assertFalse(Files.exists(dirPath));
    }

    @Test
    @DisplayName("존재하지 않는 디렉토리 삭제 테스트")
    void deleteNonExistentDir() {
        // given

        // when
        directoryStorageService.deleteDir(TEST_USERNAME);

        // then
        Path dirPath = directoryStorageService.findPath(TEST_USERNAME);
        assertFalse(Files.exists(dirPath));
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
}

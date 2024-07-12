package miniProject.board.service.file;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FileStorageServiceTest {

    @Autowired
    private DirectoryStorageService directoryStorageService;

    @Autowired
    private FileStorageService fileStorageService;

    private static final String TEST_USERNAME = "testUser";
    private static final UUID TEST_UUID = UUID.randomUUID();

    private Path testDirPath;

    @BeforeEach
    void setUp() throws IOException {
        testDirPath = directoryStorageService.createPath(TEST_USERNAME);
        directoryStorageService.createDir(TEST_USERNAME);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(testDirPath)) {
            FileUtils.deleteDirectory(testDirPath.toFile());
        }
    }

    @Test
    @DisplayName("파일 생성 테스트")
    void createFile() {
        // given
        String content = "Test Content";

        // when
        String filePath = fileStorageService.createFile(TEST_USERNAME, content, TEST_UUID);

        // then
        assertNotNull(filePath);
        assertTrue(Files.exists(Path.of(filePath)));
        try {
            assertEquals(content, new String(Files.readAllBytes(Path.of(filePath))));
        } catch (IOException e) {
            fail("파일을 읽는 중 오류가 발생했습니다.");
        }
    }

    @Test
    @DisplayName("파일 조회 테스트")
    void readFile() {
        // given
        String content = "Test Content";
        String filePath = fileStorageService.createFile(TEST_USERNAME, content, TEST_UUID);

        // when
        String requestContent = fileStorageService.readFile(filePath);

        // then
        assertNotNull(requestContent);
        assertEquals(content, requestContent);
    }

    @Test
    @DisplayName("파일 업데이트 테스트")
    void updateFile() {
        // given
        String initialContent = "Initial Content";
        String updatedContent = "Updated Content";
        String filePath = fileStorageService.createFile(TEST_USERNAME, initialContent, TEST_UUID);

        // when
        fileStorageService.updateFile(filePath, updatedContent);

        // then
        try {
            String readContent = new String(Files.readAllBytes(Path.of(filePath)));
            assertEquals(updatedContent, readContent);
        } catch (IOException e) {
            fail("파일을 읽는 중 오류가 발생했습니다.");
        }
    }

    @Test
    @DisplayName("파일 삭제 테스트")
    void deleteFile() {
        // given
        String content = "Test Content";
        String filePath = fileStorageService.createFile(TEST_USERNAME, content, TEST_UUID);

        // when
        fileStorageService.deleteFile(filePath);

        // then
        assertFalse(Files.exists(Path.of(filePath)));
    }

    @Test
    @DisplayName("파일 경로가 null인 경우 파일 생성 실패 테스트")
    void createFileWithNullPath() {
        // given
        String content = "Test Content";
        UUID uuid = UUID.randomUUID();

        // when
        String filePath = fileStorageService.createFile("anotherUser", content, uuid);

        // then
        assertNull(filePath);
    }

    @Test
    @DisplayName("파일 경로가 null인 경우 파일 조회 실패 테스트")
    void readFileWithNullPath() {
        // when
        String content = fileStorageService.readFile("anotherUser");

        // then
        assertNull(content);
    }

    @Test
    @DisplayName("파일 경로가 null인 경우 파일 업데이트 실패 테스트")
    void updateFileWithNullPath() {
        // when
        fileStorageService.updateFile(null, "New Content");

        // then
        // Expect no exceptions and no changes
    }

    @Test
    @DisplayName("파일 경로가 null인 경우 파일 삭제 실패 테스트")
    void deleteFileWithNullPath() {
        // when
        fileStorageService.deleteFile(null);

        // then
        // Expect no exceptions and no changes
    }
}

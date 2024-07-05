package miniProject.board.service.file;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Service
public class DirectoryStorageService {

    public Path findPath(String username) {
        String projectRoot = System.getProperty("user.dir");
        return Paths.get(projectRoot, "src", "main", "resources", "content", username);
        // 최종 경로 : {projectRootDir}/src/main/resources/content/{username}
    }

    /**
     * 디렉토리 생성 메서드. 회원가입 처리 후 디렉토리 생성
     * @param username 디렉토리의 이름은 username과 동일
     */
    public void createDir(String username) {
        Path directoryPath = findPath(username);

        try {
            // Directory 생성
            Files.createDirectory(directoryPath);
            System.out.println(directoryPath + " 디렉토리가 생성되었습니다.");

        } catch(FileAlreadyExistsException e) {
            System.out.println("디렉토리가 이미 존재합니다.");
        } catch(NoSuchFileException e) {
            System.out.println("디렉토리 경로가 존재하지 않습니다.");
        } catch(IOException e) {
            e.printStackTrace();
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
                System.out.println(directoryPath + " 디렉토리가 삭제되었습니다.");
            } else {
                System.out.println("디렉토리를 찾을 수 없습니다: " + directoryPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 디렉토리 찾기 메서드. 파일 저장 시 디렉토리를 찾기 위해 사용
     * @param username 디렉토리의 이름은 username과 동일
     * @return 디렉토리 경로를 나타내는 Path 객체
     * @throws NoSuchFileException 디렉토리를 찾을 수 없는 경우 예외 발생
     */
    public Path findDir(String username) throws NoSuchFileException {
        Path directoryPath = findPath(username);

        if (Files.exists(directoryPath) && Files.isDirectory(directoryPath)) {
            return directoryPath;
        } else {
            throw new NoSuchFileException("디렉토리를 찾을 수 없습니다: " + directoryPath);
        }
    }

}
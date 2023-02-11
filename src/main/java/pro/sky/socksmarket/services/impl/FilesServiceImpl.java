package pro.sky.socksmarket.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.sky.socksmarket.services.FilesService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FilesServiceImpl implements FilesService {

    @Value("${path.to.data.files}")
    private String filesPath;

    @Override
    public boolean saveToFile(String fileName, String json) {
        try {
            Path path = Path.of(filesPath, fileName);
            cleanDataFile(fileName);
            Files.writeString(path, json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String readFromFile(String fileName) {
        try {
            Path path = Path.of(filesPath, fileName);
            return Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public File getDataFile(String fileName) {
        return new File(filesPath + "/" + fileName);
    }

    @Override
    public boolean cleanDataFile(String fileName) {
        try {
            Path path = Path.of(filesPath, fileName);
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

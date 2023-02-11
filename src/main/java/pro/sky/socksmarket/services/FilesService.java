package pro.sky.socksmarket.services;

import java.io.File;

public interface FilesService {
    boolean saveToFile(String fileName, String json);

    String readFromFile(String fileName);

    File getDataFile(String fileName);

    boolean cleanDataFile(String fileName);
}

package pro.sky.socksmarket.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.socksmarket.services.FilesService;
import org.apache.commons.io.IOUtils;

import java.io.*;

@RestController
@RequestMapping("/api/data")
public class DataController {
    @Value("${name.stock.data}")
    private String socksFileName;
    @Value("${name.transaction.data}")
    private String transactionFileName;
    private final FilesService filesService;

    public DataController(FilesService filesService) {
        this.filesService = filesService;
    }

    @GetMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Получение файла с данными по складу")
    public ResponseEntity<InputStreamResource> exportData() throws FileNotFoundException {
        File file = filesService.getDataFile(socksFileName);
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = \"stock.json\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    @GetMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Получение файла с данными по операциям склада")
    public ResponseEntity<InputStreamResource> exportHistory() throws FileNotFoundException {
        File file = filesService.getDataFile(transactionFileName);
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = \"history.json\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(description = "Отправка и замена файла с данными по складу")
    public ResponseEntity<Void> importData(@RequestParam MultipartFile file) {
        filesService.cleanDataFile(socksFileName);
        File dataFile = filesService.getDataFile(socksFileName);

        try (FileOutputStream fos = new FileOutputStream(dataFile)) {
            IOUtils.copy(file.getInputStream(), fos);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
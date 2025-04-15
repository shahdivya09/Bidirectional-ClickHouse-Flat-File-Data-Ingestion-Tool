package com.ingestor.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping("/api/files")
@CrossOrigin
public class FileController {

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();

        File dest = new File(UPLOAD_DIR + file.getOriginalFilename());
        file.transferTo(dest);

        return ResponseEntity.ok("Uploaded: " + file.getOriginalFilename());
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam String filename) throws IOException {
        File file = new File(UPLOAD_DIR + filename);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}

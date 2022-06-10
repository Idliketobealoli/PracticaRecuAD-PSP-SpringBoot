package com.example.practicarecuadpspspringboot.controllers.multipartFiles;

import com.example.practicarecuadpspspringboot.config.APIConfig;
import com.example.practicarecuadpspspringboot.errors.storage.StorageException;
import com.example.practicarecuadpspspringboot.services.storage.StorageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping(APIConfig.API_PATH + "/files")
public class FilesController {
    private StorageService service;

    @Autowired
    public void setStorageService(StorageService service) {
        this.service = service;
    }


    @ApiOperation(value = "Gets a file from a name and url.", notes = "Returns the file as a url.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Resource.class),
            @ApiResponse(code = 404, message = "Not Found", response = StorageException.class),
    })
    @GetMapping(value = "{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename, HttpServletRequest request) {
        Resource file = service.loadAsResource(filename);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(file.getFile().getAbsolutePath());
        } catch (IOException ex) {
            throw new StorageException("Unknown file type.", ex);
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(file);
    }

    @ApiOperation(value = "Stores a file.", notes = "Stores a file and returns its url.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Map.class),
            @ApiResponse(code = 400, message = "Bad Request", response = StorageException.class),
    })
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestPart("file") MultipartFile file) {
        String urlImage = null;

        try {
            if (!file.isEmpty()) {
                String image = service.store(file);
                urlImage = service.getUrl(image);
                Map<String, Object> response = Map.of("url", urlImage);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                throw new StorageException("Cannot upload an empty file.");
            }
        } catch (StorageException e) {
            throw new StorageException("Cannot upload an empty file.");
        }
    }
}

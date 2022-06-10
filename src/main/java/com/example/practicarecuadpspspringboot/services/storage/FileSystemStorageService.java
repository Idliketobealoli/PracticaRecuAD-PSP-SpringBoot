package com.example.practicarecuadpspspringboot.services.storage;

import com.example.practicarecuadpspspringboot.controllers.multipartFiles.FilesController;
import com.example.practicarecuadpspspringboot.errors.storage.StorageException;
import com.example.practicarecuadpspspringboot.errors.storage.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {
    private final Path rootLocation;

    public FileSystemStorageService(@Value("${upload.root-location}") String path) {
        this.rootLocation = Paths.get(path);
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Unable to initialize storage system.", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = StringUtils.getFilenameExtension(filename);
        String name = filename.replace("." + extension, "");
        String storedFilename = System.currentTimeMillis() + "_" + name + "." + extension;
        try {
            if (file.isEmpty()) {
                throw new StorageException("Cannot store an empty file: " + filename);
            }
            if (filename.contains("..")) {
                throw new StorageException(
                        "Cannot store a file outside of the allowed path: "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(storedFilename),
                        StandardCopyOption.REPLACE_EXISTING);
                return storedFilename;
            }
        } catch (IOException e) {
            throw new StorageException("Cannot store file: " + filename, e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Could not read stored files.", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Unable to read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Unable to read file: " + filename, e);
        }
    }

    @Override
    public void delete(String filename) {
        String filenameNoPath = StringUtils.getFilename(filename);
        try {
            Path file = load(filenameNoPath);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new StorageException("Could not delete file.", e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public String getUrl(String filename) {
        return MvcUriComponentsBuilder
                .fromMethodName(FilesController.class, "serveFile", filename, null)
                .build().toUriString();
    }
}

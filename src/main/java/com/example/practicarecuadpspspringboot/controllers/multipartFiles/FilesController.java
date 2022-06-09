package com.example.practicarecuadpspspringboot.controllers.multipartFiles;

import com.example.practicarecuadpspspringboot.config.APIConfig;
import com.example.practicarecuadpspspringboot.services.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIConfig.API_PATH + "/files")
public class FilesController {
    private StorageService service;

    @Autowired
    public void setStorageService(StorageService service) {
        this.service = service;
    }

    //TODO: CONTINUAR CON ESTO
}

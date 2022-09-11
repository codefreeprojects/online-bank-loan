package com.ol.bankloan.controllers;


import com.ol.bankloan.dao.DocumentDAO;
import com.ol.bankloan.dao.UserDAO;
import com.ol.bankloan.dto.BasicResponseDTO;
import com.ol.bankloan.dto.DocumentUploadDTO;
import com.ol.bankloan.models.Documents;
import com.ol.bankloan.models.User;
import com.ol.bankloan.services.FilesStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("api/upload")
public class FilesController {
    @Autowired
    FilesStorageService storageService;
    @Autowired
    DocumentDAO documentDAO;
    @Autowired
    UserDAO userDAO;
    @Value("${files.upload.url}")
    private String filesUrl;

    private final ModelMapper mapper = new ModelMapper();

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        String mimeType = URLConnection.guessContentTypeFromName(file.getFilename());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION ).contentType(MediaType.parseMediaType(mimeType)).body(file);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/user-documents/{userId}")
    public ResponseEntity<BasicResponseDTO<List<Documents>>> saveDocument(@PathVariable("userId") Long userId){

        Optional<User> _user = userDAO.findById(userId);
        if(_user.isPresent()){
            List<Documents> documents = documentDAO.findAllByUser(_user.get());
            return new ResponseEntity<>(new BasicResponseDTO<>(true, "All Records", documents), HttpStatus.OK);
        }
        return new ResponseEntity<>(new BasicResponseDTO<>(false, "User not found", null), HttpStatus.OK);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/document", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BasicResponseDTO<Documents>> saveDocument(@ModelAttribute DocumentUploadDTO dto){
        Documents documents = new Documents();
        documents.setDocumentName(dto.getDocumentName());
        documents.setDescription(dto.getDescription());
        Optional<User> _user = userDAO.findById(dto.getUserId());
        if(_user.isPresent()){
            documents.setUser(_user.get());
            Optional<String> fileName =  storageService.save(dto.getDocument());
            if(fileName.isPresent())
                documents.setDocumentUrl(filesUrl+fileName.get());
            documentDAO.save(documents);
            return new ResponseEntity<>(new BasicResponseDTO<>(true, "Uploaded", documents), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new BasicResponseDTO<>(false, "User not found", null), HttpStatus.OK);
    }
}
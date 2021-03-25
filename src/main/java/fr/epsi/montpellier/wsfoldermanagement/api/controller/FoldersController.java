package fr.epsi.montpellier.wsfoldermanagement.api.controller;

import fr.epsi.montpellier.wsfoldermanagement.model.UserFoldersInfo;
import fr.epsi.montpellier.wsfoldermanagement.service.FoldersManagement;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FoldersController {

    private final FoldersManagement foldersManagement;

    public FoldersController(FoldersManagement foldersManagement) {
        this.foldersManagement = foldersManagement;
    }

    /** Obtient la liste des dossiers
     *
     */
    @GetMapping("/folders")
    public List<UserFoldersInfo> getFolders() {
        try {
            return foldersManagement.getFolders();
        } catch (Exception exception) {
            logError(exception);

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Aucune information liée aux utilisateurs");
        }
    }


    /**
     *
     * @param login Le login de l'utilisateur
     */
    @GetMapping("/folders/{login}")
    public UserFoldersInfo getFoldersByLogin(@PathVariable(value = "login") String login) {
        try {
            return foldersManagement.getFolders(login);
        } catch (Exception exception) {
            logError(exception);

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Aucune information liée à l'utilisateur");
        }
    }

    private void logError(Exception exception) {
        System.err.printf("Error, Class=%s\n", this.getClass().getCanonicalName());
        exception.printStackTrace(System.err);
    }

    private void logMessage(String message) {
        System.out.printf("Message=%s\nClass=%s\n", message, this.getClass().getCanonicalName());
    }


}

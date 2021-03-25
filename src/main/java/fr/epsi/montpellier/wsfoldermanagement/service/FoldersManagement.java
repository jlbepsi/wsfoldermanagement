package fr.epsi.montpellier.wsfoldermanagement.service;

import fr.epsi.montpellier.wsfoldermanagement.model.UserFoldersInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoldersManagement {


    @Value("${users_ldap_directory}")
    private String usersLdapDirectory;


    public List<UserFoldersInfo> getFolders() throws IOException {
        List<UserFoldersInfo> list = new ArrayList<>();

        File currentDirectory = new File(usersLdapDirectory);
        File[] files = currentDirectory.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    list.add(getFoldersInfo(file.getName()));
                }
            }
        }

        return list;
    }

    public UserFoldersInfo getFolders(String login) throws IOException {
        return getFoldersInfo(login);
    }

    /*
     *********************************
     *
     * Méthodes pour la création / suppression de répertoire des utilisateurs
     */

    public void createHomeDirectory(String login) {
        if (usersLdapDirectory != null && usersLdapDirectory.length() > 0) {
            String directoryName = this.usersLdapDirectory + "/" + login;
            Path path = Paths.get(directoryName);
            if (! Files.exists(path)) {
                try {
                    // Création du répertoire home
                    Files.createDirectory(path);
                    // Création du répertoire web pour Apache
                    directoryName += "/web";
                    Files.createDirectory(Paths.get(directoryName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void deleteHomeDirectory(String login) throws IOException {

        if (usersLdapDirectory != null && usersLdapDirectory.length() > 0) {
            String directoryName = this.usersLdapDirectory + "/" + login;
            Path path = Paths.get(directoryName);
            if (Files.exists(path)) {
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmm");
                String newName = String.format("%s/_DELETED_%s_%s", this.usersLdapDirectory, dateFormat.format(Calendar.getInstance().getTime()), login);
                Files.move(path, Paths.get(newName));
                /*
                File directory = new File(directoryName);
                FileUtils.deleteDirectory(directory);
                */
            }
        }
    }

    /**/




    private UserFoldersInfo getFoldersInfo(String login) throws IOException {
        UserFoldersInfo info = new UserFoldersInfo();
        info.setLogin(login);

        List<File> filesInFolder = Files.walk(Paths.get(usersLdapDirectory + "/" + login))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());

        info.setFilesCount(filesInFolder.size());
        long size = 0;
        for (File file : filesInFolder) {
            size += file.length();
        }
        info.setSize(size);

        return info;
    }
}

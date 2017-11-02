/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

/**
 *
 * @author mahbub
 */
public class FileDownloader {

    public static Optional<String> getURLFileExtension(final URL url) {

        Objects.requireNonNull(url, "URL is null");

        final String file = url.getFile();

        if (file.contains(".")) {
            final String sub = file.substring(file.lastIndexOf('.') + 1);

            if (sub.length() == 0) {
                return Optional.empty();
            }

            if (sub.contains("?")) {
                return Optional.of(sub.substring(0, sub.indexOf('?')));
            }
            return Optional.of(sub);
        }
        return Optional.empty();
    }
    
    public static Optional<String> getURLFileName(final URL url) {

        Objects.requireNonNull(url, "URL is null");

        final String file = url.getFile();

        if (file.contains("/")) {
            final String sub = file.substring(file.lastIndexOf('/') + 1);

            if (sub.length() == 0) {
                return Optional.empty();
            }

            if (sub.contains("?")) {
                return Optional.of(sub.substring(0, sub.indexOf('?')));
            }
            return Optional.of(sub);
        }
        return Optional.empty();
    }
    
    public static Path downloadFile(String fileURL, String targetDirectory) throws MalformedURLException, IOException, FileAlreadyExistsException {
        URL url = new URL(fileURL);
        Optional fileName = FileDownloader.getURLFileName(url);
        if (!fileName.isPresent()) {
            throw new IOException("Url is not a file: "+fileURL);
        }
        Path targetPath = new File(targetDirectory + File.separator + fileName).toPath();
        Files.copy(url.openStream(), targetPath);

        return targetPath;
    }
    
    public static void main(String[] args) throws MalformedURLException {
        Optional fileName = FileDownloader.getURLFileExtension(new URL("https://localhost:8443/docs/RUNNING.txt?gsdg"));
        System.out.println("File ext: "+fileName.get());
        fileName = FileDownloader.getURLFileName(new URL("https://localhost:8443/RUNNING.txt?gsdg"));
        fileName.ifPresent(value -> {System.out.println("File name: "+value);});
        
        try {
            Optional fileName2 = FileDownloader.getURLFileExtension(null);
            System.out.println("Filename: "+fileName.get());
        } catch (Exception ex) {
            System.out.println("Ex");
        }
        
        
    }
}

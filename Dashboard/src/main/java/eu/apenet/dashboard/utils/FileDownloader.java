/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.utils;

import java.net.MalformedURLException;
import java.net.URL;
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
    
    public static void main(String[] args) throws MalformedURLException {
        Optional fileName = FileDownloader.getURLFileExtension(new URL("https://localhost:8443/docs/RUNNING.txt?gsdg"));
        System.out.println("Filename: "+fileName.get());
        
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Objects;
import java.util.Optional;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.log4j.Logger;

/**
 *
 * @author mahbub
 */
public class FileDownloader {

    private static final Logger LOGGER = Logger.getLogger(FileDownloader.class);

    static {
        //Change the default trust manager for https certificates to accept all
        final TrustManager[] trustAllCertificates = new TrustManager[]{
            new X509ExtendedTrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null; // Not relevant.
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String string, Socket socket) throws CertificateException {
                    LOGGER.debug("Trust all client extended");
                }

                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String string, Socket socket) throws CertificateException {
                    LOGGER.debug("Trust all server Extended");
                }

                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException {
                    LOGGER.debug("Trust all client extended2");
                }

                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException {
                    LOGGER.debug("Trust all server Extended2");
                }
            }
        };
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCertificates, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            LOGGER.debug("SSL default change");
        } catch (GeneralSecurityException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

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

    public static Path downloadFile(String fileURL, String targetDirectory) throws MalformedURLException, IOException {
        URL url = new URL(fileURL);
        Optional fileName = FileDownloader.getURLFileName(url);
        if (!fileName.isPresent()) {
            throw new IOException("Url is not a file: " + fileURL);
        }
        Path targetPath = new File(targetDirectory + File.separator + fileName.get()).toPath();
        Files.copy(url.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return targetPath;
    }
}

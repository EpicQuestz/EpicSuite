package de.stealwonders.epicsuite.resourcepack;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;

public class PackValidator {

    public static boolean isValid(String baseUrl, String apperentHash) throws Exception {
        String url = baseUrl + "/" + apperentHash + ".zip";
        URLConnection connection = new URL(url).openConnection(); // download file at url

        // calculate hash of file
        InputStream inputStream = connection.getInputStream();

        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] buffer = new byte[8192];
        int read;
        while ((read = inputStream.read(buffer)) > 0) {
            digest.update(buffer, 0, read);
        }

        byte[] hashBytes = digest.digest();

        // convert hash to string
        StringBuilder hash = new StringBuilder();
        for (byte b : hashBytes) {
            hash.append(String.format("%02x", b));
        }

        // compare hash with apperentHash
        return hash.toString().equals(apperentHash);
    }

}

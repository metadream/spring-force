package com.arraywork.vernal.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;

/**
 * Directory and File Utilities
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/28
 */
public class FileUtils {

    /** Get the file name without extension */
    public static String getName(String filename) {
        if (filename == null) return null;
        int i = filename.lastIndexOf(".");
        return i > -1 ? filename.substring(0, i) : filename;
    }

    /** Get the file extension */
    public static String getExtension(String filename) {
        if (filename == null) return null;
        int i = filename.lastIndexOf(".");
        return i > -1 ? filename.substring(i) : "";
    }

    /** Get creation time */
    public static long getCreationTime(File file) throws IOException {
        BasicFileAttributes attrs = java.nio.file.Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        return attrs.creationTime().toMillis();
    }

    /** Format file size in human readable */
    public static String formatSize(long v) {
        if (v < 1024) return v + " B";
        int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
        return String.format("%.1f %siB", (double) v / (1L << (z * 10)), " KMGTPE".charAt(z));
    }

    /** Walk directory */
    public static List<File> walk(Path path) throws IOException {
        List<File> files = new ArrayList<>();
        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                if (attrs.isRegularFile()) files.add(path.toFile());
                return super.visitFile(path, attrs);
            }
        });
        return files;
    }

    /** Determine input stream is image format or not */
    public static boolean isImageFormat(InputStream inputStream) throws IOException {
        byte[] header = new byte[4];
        int read = inputStream.read(header);
        inputStream.close();

        if (read != -1) {
            String fileCode = StringUtils.toHexadecimal(header).toUpperCase();
            return fileCode.matches("(FFD8FF|89504E|47494638|52494646).+"); // jpg|png|gif|webp
        }
        return false;
    }

    /** Read content from resource */
    public static String readContent(Resource resource) throws IOException {
        Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
        return new BufferedReader(reader).lines().collect(Collectors.joining()).replaceAll("\\s+", "");
    }

}
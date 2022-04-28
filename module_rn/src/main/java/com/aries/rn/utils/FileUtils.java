package com.aries.rn.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {

    private static final int WRITE_BUFFER_SIZE = 1024 * 8;

    /**
     * 目录拷贝
     *
     * @param sourceDirectoryPath
     * @param destinationDirectoryPath
     * @throws IOException
     */
    public static void copyDirectoryContents(String sourceDirectoryPath, String destinationDirectoryPath) throws IOException {
        File sourceDir = new File(sourceDirectoryPath);
        File destDir = new File(destinationDirectoryPath);
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        for (File sourceFile : sourceDir.listFiles()) {
            if (sourceFile.isDirectory()) {
                String s = appendPathComponent(sourceDirectoryPath, sourceFile.getName());
                String d = appendPathComponent(sourceDirectoryPath, sourceFile.getName());
                copyDirectoryContents(s, d);
            } else {
                File destFile = new File(destDir, sourceFile.getName());
                FileInputStream fromFileStream = null;
                BufferedInputStream fromBufferedStream = null;
                FileOutputStream destStream = null;
                byte[] buffer = new byte[WRITE_BUFFER_SIZE];
                try {
                    fromFileStream = new FileInputStream(sourceFile);
                    fromBufferedStream = new BufferedInputStream(fromFileStream);
                    destStream = new FileOutputStream(destFile);
                    int bytesRead;
                    while (0 < (bytesRead = fromBufferedStream.read(buffer))) {
                        destStream.write(buffer, 0, bytesRead);
                    }
                } finally {
                    try {
                        if (null != fromFileStream) fromFileStream.close();
                        if (null != fromBufferedStream) fromBufferedStream.close();
                        if (null != destStream) destStream.close();
                    } catch (IOException e) {
                        MaskLog.e("Error closing IO resources.");
//            throw new MaskHubUnknownException("Error closing IO resources.", e);
                    }
                }
            }
        }
    }

    public static void deleteDirectoryAtPath(String directoryPath) {
        if (null == directoryPath) {
            MaskLog.d("deleteDirectoryAtPath attempted with null directoryPath");
            return;
        }
        File file = new File(directoryPath);
        if (file.exists()) {
            deleteFileOrFolderSilently(file);
        }
    }

    public static void deleteFileAtPathSilently(String path) {
        deleteFileOrFolderSilently(new File(path));
    }

    public static void deleteFileOrFolderSilently(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File fileEntry : files) {
                if (fileEntry.isDirectory()) {
                    deleteFileOrFolderSilently(fileEntry);
                } else {
                    fileEntry.delete();
                }
            }
        }
        if (!file.delete()) {
            MaskLog.e("Error deleting file " + file.getName());
        }
    }

    public static boolean fileAtPathExists(String filePath) {
        return new File(filePath).exists();
    }

    public static void moveFile(File fileToMove, String newFolderPath, String newFileName) {
        File newFolder = new File(newFolderPath);
        if (!newFolder.exists()) {
            newFolder.mkdirs();
        }

        File newFilePath = new File(newFolderPath, newFileName);
        if (!fileToMove.renameTo(newFilePath)) {
            throw new RuntimeException("Unable to move file from " +
                    fileToMove.getAbsolutePath() + " to " + newFilePath.getAbsolutePath() + ".");
        }
    }

    public static String readFileToString(String filePath) throws IOException {
        FileInputStream fin = null;
        BufferedReader reader = null;
        try {
            File fl = new File(filePath);
            fin = new FileInputStream(fl);
            reader = new BufferedReader(new InputStreamReader(fin));
            StringBuilder sb = new StringBuilder();
            String line;
            while (null != (line = reader.readLine())) {
                sb.append(line).append("\n");
            }

            return sb.toString();
        } finally {
            if (null != reader) reader.close();
            if (null != fin) fin.close();
        }
    }

    /**
     * @param fileName
     * @param targetDirectory
     * @return
     * @throws IOException
     */
    private static String validateFileName(String fileName, String targetDirectory) throws IOException {
        File file = new File(fileName);
        String canonicalPath = file.getCanonicalPath();
        File targetFile = new File(targetDirectory);
        String targetCanonicalPath = targetFile.getCanonicalPath();
        if (!canonicalPath.startsWith(targetCanonicalPath)) {
            throw new IllegalStateException("File is outside extraction target directory.");
        }
        return canonicalPath;
    }

    /**
     * @param zipFile
     * @param destination
     * @throws IOException
     */
    public static void unzipFile(File zipFile, String destination) throws IOException {
        FileInputStream fileStream = null;
        BufferedInputStream bufferedStream = null;
        ZipInputStream zipStream = null;
        try {
            fileStream = new FileInputStream(zipFile);
            bufferedStream = new BufferedInputStream(fileStream);
            zipStream = new ZipInputStream(bufferedStream);
            ZipEntry entry;

            File destinationFolder = new File(destination);
            if (destinationFolder.exists()) {
                deleteFileOrFolderSilently(destinationFolder);
            }
            destinationFolder.mkdirs();

            byte[] buffer = new byte[WRITE_BUFFER_SIZE];
            while ((entry = zipStream.getNextEntry()) != null) {
                String fileName = validateFileName(entry.getName(), ".");
                File file = new File(destinationFolder, fileName);
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    File parent = file.getParentFile();
                    if (!parent.exists()) parent.mkdirs();


                    try (FileOutputStream fout = new FileOutputStream(file)) {
                        int numBytesRead;
                        while ((numBytesRead = zipStream.read(buffer)) != -1) {
                            fout.write(buffer, 0, numBytesRead);
                        }
                    }
                }
                long time = entry.getTime();
                if (time > 0) file.setLastModified(time);
            }
        } finally {
            try {
                if (zipStream != null) zipStream.close();
                if (bufferedStream != null) bufferedStream.close();
                if (fileStream != null) fileStream.close();
            } catch (IOException e) {
                MaskLog.e("Error closing IO resources.", e);
//        throw new MaskHubUnknownException("Error closing IO resources.", e);
            }
        }
    }

    public static void writeStringToFile(String content, String filePath) throws IOException {
        try (PrintWriter out = new PrintWriter(filePath)) {
            out.print(content);
        }
    }

    public static String appendPathComponent(String basePath, String appendPathComponent) {
        return new File(basePath, appendPathComponent).getAbsolutePath();
    }


    /**
     * 安静关闭 IO
     *
     * @param closeables closeables
     */
    public static void closeIOQuietly(final Closeable... closeables) {
        if (null == closeables) return;
        for (Closeable closeable : closeables) {
            if (null != closeable) {
                try {
                    closeable.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

}

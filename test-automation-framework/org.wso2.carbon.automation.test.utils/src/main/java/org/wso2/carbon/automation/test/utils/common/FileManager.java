/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package org.wso2.carbon.automation.test.utils.common;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class FileManager {
    private static final Log log = LogFactory.getLog(FileManager.class);

    public static String readFile(String filePath) throws IOException {
        BufferedReader reader;
        StringBuilder stringBuilder;
        String line;
        String ls;
        //log.debug("Path to file : " + filePath);
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), Charset.defaultCharset()));
        //reader = new BufferedReader(new FileReader(filePath));
        stringBuilder = new StringBuilder();
        ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        reader.close();
        return stringBuilder.toString();
    }

    public static String readFile(File file) throws IOException {
        BufferedReader reader;
        StringBuilder stringBuilder;
        String line;
        String ls;
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.defaultCharset()));
        //reader = new BufferedReader(new FileReader(file));
        stringBuilder = new StringBuilder();
        ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        reader.close();
        return stringBuilder.toString();
    }

    public static void writeToFile(String filePath, String content) throws IOException {

        BufferedWriter writer = new BufferedWriter
                (new OutputStreamWriter(new FileOutputStream(filePath, true), Charset.defaultCharset()));

        try {
            writer.write(content);
            writer.newLine();
            writer.flush();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }

    public static void copyFile(File sourceFile, String destinationPath) throws IOException {
        File destinationFile = new File(destinationPath);

        InputStreamReader in = new InputStreamReader(new FileInputStream(sourceFile), Charset.defaultCharset());

        BufferedWriter out = new BufferedWriter
                (new OutputStreamWriter(new FileOutputStream(destinationFile), Charset.defaultCharset()));
        int c;
        try {
            while ((c = in.read()) != -1) {
                out.write(c);
            }
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                //ignore
            }
            try {
                out.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }

    public static File copyResourceToFileSystem(String sourcePath, String targetPath,
                                                String fileName)
            throws IOException {
        File file = new File(targetPath + File.separator + fileName);
        if (file.exists()) {
            FileUtils.deleteQuietly(file);
        }
        FileUtils.touch(file);
        OutputStream os = FileUtils.openOutputStream(file);
        InputStream is = new FileInputStream(sourcePath);

        try {
                byte[] data = new byte[1024];
                int len;
                while ((len = is.read(data)) != -1) {
                    os.write(data, 0, len);
                    os.flush();
                }

        } finally {
            try {
                os.close();
            } catch (IOException e) {
                //ignore
            }

            try {
                is.close();
            } catch (IOException e) {
                //ignore
            }
        }

        return file;
    }

    public void copyJarFile(String sourceFileLocationWithFileName, String destinationDirectory)
            throws IOException, URISyntaxException {
        File sourceFile = new File(getClass().getResource(sourceFileLocationWithFileName).toURI());
        File destinationFileDirectory = new File(destinationDirectory);
        JarFile jarFile = new JarFile(sourceFile);
        String fileName = jarFile.getName();
        String fileNameLastPart = fileName.substring(fileName.lastIndexOf(File.separator));
        File destinationFile = new File(destinationFileDirectory, fileNameLastPart);
        JarOutputStream jarOutputStream = null;
        try {
            jarOutputStream = new JarOutputStream(new FileOutputStream(destinationFile));
            Enumeration<JarEntry> entries = jarFile.entries();
            InputStream inputStream = null;
            while (entries.hasMoreElements()) {
                try {
                    JarEntry jarEntry = entries.nextElement();
                    inputStream = jarFile.getInputStream(jarEntry);
                    //jarOutputStream.putNextEntry(jarEntry);
                    //create a new jarEntry to avoid ZipException: invalid jarEntry compressed size
                    jarOutputStream.putNextEntry(new JarEntry(jarEntry.getName()));
                    byte[] buffer = new byte[4096];
                    int bytesRead = 0;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        jarOutputStream.write(buffer, 0, bytesRead);
                    }
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                        jarOutputStream.flush();
                        jarOutputStream.closeEntry();
                    }
                }
            }
        } finally {
            if (jarOutputStream != null) {
                jarOutputStream.close();
            }

            jarFile.close();
        }
    }

    public static void copyJarFile(File sourceFile, String destinationDirectory)
            throws IOException {
        File destinationFileDirectory = new File(destinationDirectory);
        JarFile jarFile = new JarFile(sourceFile);
        String fileName = jarFile.getName();
        String fileNameLastPart = fileName.substring(fileName.lastIndexOf(File.separator));
        File destinationFile = new File(destinationFileDirectory, fileNameLastPart);
        JarOutputStream jarOutputStream = null;
        try {
            jarOutputStream = new JarOutputStream(new FileOutputStream(destinationFile));
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                InputStream inputStream = jarFile.getInputStream(jarEntry);
                //jarOutputStream.putNextEntry(jarEntry);
                //create a new jarEntry to avoid ZipException: invalid jarEntry compressed size
                jarOutputStream.putNextEntry(new JarEntry(jarEntry.getName()));
                byte[] buffer = new byte[4096];
                int bytesRead = 0;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    jarOutputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                jarOutputStream.flush();
                jarOutputStream.closeEntry();
            }
        } finally {
            if (jarOutputStream != null) {
                try {
                    jarOutputStream.close();
                } catch (IOException e) {
                }
            }

            jarFile.close();
        }
    }

    public static boolean deleteFile(String filePathWithFileName) {
        File jarFile = new File(filePathWithFileName);
        return !jarFile.isDirectory() && jarFile.delete();
    }



}


package com.ryan.rlib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtil {
    private static final String TAG = "FileUtil";
    private static final int BUFF_SIZE = 2097152;
    
    public static String saveBitmap2File(Bitmap b, String path, String fileName) {
        if (b == null) {
            return "";
        } else {
            LogUtil.i(TAG, "saveBitmap path: " + path);
            File parent = new File(path);
            if (!parent.exists()) {
                parent.mkdirs();
            }
            
            String jpegName = path + File.separator + fileName + ".jpg";
            Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
            
            try {
                File file = new File(jpegName);
                if (file.exists() && file.length() > 0L) {
                    return jpegName;
                }
                
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                b.compress(CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            } catch (IOException var8) {
                var8.printStackTrace();
            }
            
            return jpegName;
        }
    }
    
    public static String saveFile(InputStream is, String path, String fileName) {
        File parent = new File(path);
        if (!parent.exists()) {
            parent.mkdirs();
        }
        
        String pathName = path + File.separator + fileName;
        FileOutputStream fos = null;
        BufferedOutputStream os = null;
        BufferedInputStream inputStream = null;
        
        try {
            File file = new File(pathName);
            if (!file.exists() || file.length() <= 0L) {
                fos = new FileOutputStream(file);
                os = new BufferedOutputStream(fos);
                inputStream = new BufferedInputStream(is);
                byte[] b = new byte[5120];
                
                int len;
                while ((len = inputStream.read(b)) != -1) {
                    os.write(b, 0, len);
                    os.flush();
                }
            }
        } catch (IOException var20) {
            var20.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                
                if (fos != null) {
                    fos.close();
                }
                
                if (inputStream != null) {
                    inputStream.close();
                }
                
                if (os != null) {
                    os.close();
                }
            } catch (IOException var19) {
                var19.printStackTrace();
            }
            
            return pathName;
        }
    }
    
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        
        return flag;
    }
    
    public static boolean deleteFile(File file) {
        boolean flag = false;
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        
        return flag;
    }
    
    public static boolean deleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        if (!file.exists()) {
            return flag;
        } else {
            return file.isFile() ? deleteFile(sPath) : deleteDirectory(sPath);
        }
    }
    
    public static boolean deleteDirectory(String sPath) {
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        
        File dirFile = new File(sPath);
        if (dirFile.exists() && dirFile.isDirectory()) {
            boolean flag = true;
            File[] files = dirFile.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; ++i) {
                    if (files[i].isFile()) {
                        flag = deleteFile(files[i].getAbsolutePath());
                        if (!flag) {
                            break;
                        }
                    } else {
                        flag = deleteDirectory(files[i].getAbsolutePath());
                        if (!flag) {
                            break;
                        }
                    }
                }
            }
            
            if (!flag) {
                return false;
            } else if (dirFile.delete()) {
                System.out.println("dir " + dirFile.getAbsolutePath() + " del succ");
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    public static boolean deleteDirectoryChilds(String sPath) {
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        
        File dirFile = new File(sPath);
        if (dirFile.exists() && dirFile.isDirectory()) {
            boolean flag = true;
            File[] files = dirFile.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; ++i) {
                    if (files[i].isFile()) {
                        flag = deleteFile(files[i].getAbsolutePath());
                        if (!flag) {
                            break;
                        }
                    } else {
                        flag = deleteDirectory(files[i].getAbsolutePath());
                        if (!flag) {
                            break;
                        }
                    }
                }
            }
            
            return flag;
        } else {
            return false;
        }
    }
    
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            System.out.println("创建目录" + destDirName + "失败，目标目录已存在！");
            return false;
        } else {
            if (!destDirName.endsWith(File.separator)) {
                destDirName = destDirName + File.separator;
            }
            
            if (dir.mkdirs()) {
                System.out.println("创建目录" + destDirName + "成功！");
                return true;
            } else {
                System.out.println("创建目录" + destDirName + "成功！");
                return false;
            }
        }
    }
    
    public static File zipFiles(String strPath, File zipFile) throws Exception {
        LinkedList<File> list = new LinkedList();
        getFiles(list, strPath);
        zipFiles((Collection) list, zipFile);
        return zipFile;
    }
    
    public static Collection<File> getFiles(LinkedList<File> list, String strPath) {
        File dir = new File(strPath);
        File[] file = dir.listFiles();
        
        for (int i = 0; i < file.length; ++i) {
            if (file[i].isFile()) {
                list.add(file[i]);
            } else {
                getFiles(list, file[i].getAbsolutePath());
            }
        }
        
        return list;
    }
    
    public static File zipFiles(Collection<File> resFileList, File zipFile) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile), 2097152));
        Iterator iterator = resFileList.iterator();
        
        while (iterator.hasNext()) {
            File resFile = (File) iterator.next();
            zipFile(resFile, zos, "");
        }
        
        zos.close();
        return zipFile;
    }
    
    public static void zipFiles(Collection<File> resFileList, File zipFile, String comment) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile), 2097152));
        Iterator iterator = resFileList.iterator();
        
        while (iterator.hasNext()) {
            File resFile = (File) iterator.next();
            zipFile(resFile, zos, "");
        }
        
        zos.setComment(comment);
        zos.close();
    }
    
    private static void zipFile(File resFile, ZipOutputStream zipout, String rootpath) throws FileNotFoundException, IOException {
        rootpath = rootpath + (rootpath.trim().length() == 0 ? "" : File.separator) + resFile.getName();
        rootpath = new String(rootpath.getBytes("8859_1"), "GB2312");
        int realLength;
        if (resFile.isDirectory()) {
            File[] fileList = resFile.listFiles();
            File[] var4 = fileList;
            realLength = fileList.length;
            
            for (int var6 = 0; var6 < realLength; ++var6) {
                File file = var4[var6];
                zipFile(file, zipout, rootpath);
            }
        } else {
            byte[] buffer = new byte[2097152];
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(resFile), 2097152);
            zipout.putNextEntry(new ZipEntry(rootpath));
            
            while ((realLength = in.read(buffer)) != -1) {
                zipout.write(buffer, 0, realLength);
            }
            
            in.close();
            zipout.flush();
            zipout.closeEntry();
        }
        
    }
    
    public static File zipFile(File sourceFile, File zipFile) throws Exception {
        BufferedInputStream origin = null;
        ZipOutputStream out = null;
        boolean flag = false;
        int BUFFER = 4096;
        FileOutputStream fileOutputStream = null;
        FileInputStream fis = null;
        
        try {
            fileOutputStream = new FileOutputStream(zipFile);
            out = new ZipOutputStream(fileOutputStream);
            fis = new FileInputStream(sourceFile);
            origin = new BufferedInputStream(fis, BUFFER);
            ZipEntry entry = new ZipEntry(sourceFile.getName());
            byte[] data = new byte[BUFFER];
            out.putNextEntry(entry);
            
            while (true) {
                int count;
                if ((count = origin.read(data, 0, BUFFER)) == -1) {
                    out.closeEntry();
                    if (flag) {
                        flag = sourceFile.delete();
                    }
                    break;
                }
                
                out.write(data, 0, count);
            }
        } catch (Exception var27) {
            throw new Exception(var27);
        } finally {
            try {
                if (origin != null) {
                    origin.close();
                }
            } catch (Exception var26) {
                ;
            }
            
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception var25) {
                ;
            }
            
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception var24) {
                ;
            }
            
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception var23) {
                ;
            }
            
        }
        
        return zipFile;
    }
    
    public static File unzipFile(String directory, File zip) throws IOException {
        File child = null;
        ZipInputStream zis = null;
        FileOutputStream output = null;
        FileInputStream inputStream = null;
        
        try {
            inputStream = new FileInputStream(zip);
            zis = new ZipInputStream(inputStream);
            ZipEntry ze = zis.getNextEntry();
            File parent = new File(directory);
            if (!parent.exists() && !parent.mkdirs()) {
                zis.close();
                throw new IOException("创建解压目录 \"" + parent.getAbsolutePath() + "\" 失败");
            }
            
            while (ze != null) {
                String name = ze.getName();
                child = new File(parent, name);
                output = new FileOutputStream(child);
                byte[] buffer = new byte[10240];
                
                int bytesRead;
                while ((bytesRead = zis.read(buffer)) > 0) {
                    output.write(buffer, 0, bytesRead);
                }
                
                output.flush();
                output.close();
                ze = zis.getNextEntry();
            }
        } catch (IOException var24) {
            var24.printStackTrace();
            throw new IOException(var24);
        } finally {
            try {
                if (zis != null) {
                    zis.close();
                }
            } catch (Exception var23) {
                ;
            }
            
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception var22) {
                ;
            }
            
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception var21) {
                ;
            }
            
        }
        
        return child;
    }
    
    public static void saveFile(File target, InputStream in) throws Exception {
        FileOutputStream output = null;
        
        try {
            output = new FileOutputStream(target);
            byte[] buffer = new byte[10240];
            
            int bytesRead;
            while ((bytesRead = in.read(buffer)) > 0) {
                output.write(buffer, 0, bytesRead);
            }
            
            output.flush();
            output.close();
        } catch (Exception var15) {
            var15.printStackTrace();
            throw new Exception(var15);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException var14) {
                    var14.printStackTrace();
                }
            }
            
            if (in != null) {
                try {
                    in.close();
                } catch (IOException var13) {
                    var13.printStackTrace();
                }
            }
            
        }
    }
    
    
    /**
     * 判断SD卡是否可用
     *
     * @return SD卡可用返回true
     */
    public static boolean hasSdcard() {
        String status = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(status);
    }
    
    /**
     * 应用App 文件目录
     *
     * @param context 上下文
     * @param dirName 文件夹名称
     * @return 目录全路径
     */
    public static String getAppFilePath(Context context, String dirName) {
        String directoryPath = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//判断外部存储是否可用
            directoryPath = context.getExternalFilesDir(dirName).getAbsolutePath();
        } else {//没外部存储就使用内部存储
            directoryPath = context.getFilesDir() + File.separator + dirName;
        }
        File file = new File(directoryPath);
        if (!file.exists()) {//判断文件目录是否存在
            file.mkdirs();
        }
        return directoryPath;
    }
    
    
    /**
     * Android 公共文件目录
     *
     * @param context 上下文
     * @param dirName 文件夹名称
     * @return 目录全路径
     */
    public static String getPublicFilePath(Context context, String dirName) {
        String directoryPath = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//判断外部存储是否可用
            directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {//没外部存储就使用内部存储
            directoryPath = context.getFilesDir() + File.separator + dirName;
        }
        File file = new File(directoryPath);
        if (!file.exists()) {//判断文件目录是否存在
            file.mkdirs();
        }
        return directoryPath;
    }
    
    /**
     * 读取文件的内容
     * 默认utf-8编码
     *
     * @param filePath 文件路径
     * @return 字符串
     */
    public static String readFile(String filePath) throws IOException {
        return readFile(filePath, "utf-8");
    }
    
    /**
     * 读取文件的内容
     *
     * @param filePath    文件目录
     * @param charsetName 字符编码
     * @return String字符串
     */
    public static String readFile(String filePath, String charsetName) throws IOException {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        
        if (TextUtils.isEmpty(charsetName)) {
            charsetName = "utf-8";
        }
        
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }
        
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append(" ");
                }
                fileContent.append(line);
            }
            return fileContent.toString();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 读取文本文件到List字符串集合中(默认utf-8编码)
     *
     * @param filePath 文件目录
     * @return 文件不存在返回null，否则返回字符串集合
     */
    public static List<String> readFileToList(String filePath) throws IOException {
        return readFileToList(filePath, "utf-8");
    }
    
    /**
     * 读取文本文件到List字符串集合中
     *
     * @param filePath    文件目录
     * @param charsetName 字符编码
     * @return 文件不存在返回null，否则返回字符串集合
     */
    public static List<String> readFileToList(String filePath, String charsetName) throws IOException {
        if (TextUtils.isEmpty(filePath))
            return null;
        if (TextUtils.isEmpty(charsetName)) charsetName = "utf-8";
        File file = new File(filePath);
        List<String> fileContent = new ArrayList<String>();
        if (!file.isFile()) {
            return null;
        }
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            return fileContent;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 向文件中写入数据
     *
     * @param filePath 文件目录
     * @param content  要写入的内容
     * @param append   如果为 true，则将数据写入文件末尾处，而不是写入文件开始处
     * @return 写入成功返回true， 写入失败返回false
     */
    public static boolean writeFile(String filePath, String content, boolean append) throws IOException {
        if (TextUtils.isEmpty(filePath))
            return false;
        if (TextUtils.isEmpty(content))
            return false;
        FileWriter fileWriter = null;
        try {
            createFile(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.flush();
            return true;
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static void createFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
    }
    
    
    /**
     * 向文件中写入数据
     * 默认在文件开始处重新写入数据
     *
     * @param filePath 文件目录
     * @param stream   字节输入流
     * @return 写入成功返回true，否则返回false
     */
    public static boolean writeFile(String filePath, InputStream stream) throws IOException {
        return writeFile(filePath, stream, false);
    }
    
    /**
     * 向文件中写入数据
     *
     * @param filePath 文件目录
     * @param stream   字节输入流
     * @param append   如果为 true，则将数据写入文件末尾处；
     *                 为false时，清空原来的数据，从头开始写
     * @return 写入成功返回true，否则返回false
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) throws IOException {
        if (TextUtils.isEmpty(filePath))
            throw new NullPointerException("filePath is Empty");
        if (stream == null)
            throw new NullPointerException("InputStream is null");
        return writeFile(new File(filePath), stream, append);
    }
    
    /**
     * 向文件中写入数据
     * 默认在文件开始处重新写入数据
     *
     * @param file   指定文件
     * @param stream 字节输入流
     * @return 写入成功返回true，否则返回false
     */
    public static boolean writeFile(File file, InputStream stream) throws IOException {
        return writeFile(file, stream, false);
    }
    
    /**
     * 向文件中写入数据
     *
     * @param file   指定文件
     * @param stream 字节输入流
     * @param append 为true时，在文件开始处重新写入数据；
     *               为false时，清空原来的数据，从头开始写
     * @return 写入成功返回true，否则返回false
     *
     * @throws IOException
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) throws IOException {
        if (file == null)
            throw new NullPointerException("file = null");
        OutputStream out = null;
        try {
            createFile(file.getAbsolutePath());
            out = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                out.write(data, 0, length);
            }
            out.flush();
            return true;
        } finally {
            if (out != null) {
                try {
                    out.close();
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}

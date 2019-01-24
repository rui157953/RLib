package com.ryan.rlib.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtil {
    private static final String TAG = "FileUtil";
    private static final int BUFF_SIZE = 2097152;
    
    public FileUtil() {
    }
    
    public static String saveBitmap2File(Bitmap b, String path, String fileName) {
        if (b == null) {
            return "";
        } else {
            LogUtil.i("TAGxc", "saveBitmap path: " + path);
            File parent = new File(path);
            if (!parent.exists()) {
                parent.mkdirs();
            }
            
            String jpegName = path + File.separator + fileName + ".jpg";
            Log.i("FileUtil", "saveBitmap:jpegName = " + jpegName);
            
            try {
                File file = new File(jpegName);
                if (file.exists() && file.length() > 0L) {
                    return jpegName;
                }
                
                FileOutputStream fout = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fout);
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
        FileOutputStream fout = null;
        BufferedOutputStream os = null;
        BufferedInputStream inputStream = null;
        
        try {
            File file = new File(pathName);
            if (!file.exists() || file.length() <= 0L) {
                fout = new FileOutputStream(file);
                os = new BufferedOutputStream(fout);
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
                
                if (fout != null) {
                    fout.close();
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
        ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile), 2097152));
        Iterator var3 = resFileList.iterator();
        
        while (var3.hasNext()) {
            File resFile = (File) var3.next();
            zipFile(resFile, zipout, "");
        }
        
        zipout.close();
        return zipFile;
    }
    
    public static void zipFiles(Collection<File> resFileList, File zipFile, String comment) throws IOException {
        ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile), 2097152));
        Iterator var4 = resFileList.iterator();
        
        while (var4.hasNext()) {
            File resFile = (File) var4.next();
            zipFile(resFile, zipout, "");
        }
        
        zipout.setComment(comment);
        zipout.close();
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
}

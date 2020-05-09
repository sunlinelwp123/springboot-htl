package com.open.boot.util.common;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class UrlUtil {
    static final String PROTOCOL_FILE = "file";
    static final String PROTOCOL_JAR = "jar";
    static final String PROTOCOL_WSJAR = "wsjar";
    static final String RESOURCE_PATH_SEPARATOR = "/";

    private UrlUtil() {
    }

    public static URL getURL(String basePath, String file) throws MalformedURLException {
        File f = new File(file);
        if (f.isAbsolute()) {
            return f.toURL();
        } else {
            try {
                if (basePath == null) {
                    return new URL(file);
                } else {
                    URL base = new URL(basePath);
                    return new URL(base, file);
                }
            } catch (MalformedURLException var4) {
                return constructFile(basePath, file).toURL();
            }
        }
    }

    static File constructFile(String basePath, String fileName) {
        File file = null;
        File absolute = null;
        if (fileName != null) {
            absolute = new File(fileName);
        }

        if (StringUtil.isEmpty(basePath) || absolute != null && absolute.isAbsolute()) {
            file = new File(fileName);
        } else {
            StringBuffer fName = new StringBuffer();
            fName.append(basePath);
            if (!basePath.endsWith(File.separator)) {
                fName.append(File.separator);
            }

            if (fileName.startsWith("." + File.separator)) {
                fName.append(fileName.substring(2));
            } else {
                fName.append(fileName);
            }

            file = new File(fName.toString());
        }

        return file;
    }

    public static Collection locate(String name) {
        return locate((String)null, name);
    }

    public static Collection locate(String base, String name) {
        Collection ret = new HashSet();
        if (name == null) {
            return ret;
        } else {
            URL url = null;

            try {
                if (base == null) {
                    url = new URL(name);
                } else {
                    URL baseURL = new URL(base);
                    url = new URL(baseURL, name);
                    InputStream in = null;

                    try {
                        in = url.openStream();
                    } finally {
                        if (in != null) {
                            in.close();
                        }

                    }
                }

                ret.add(url);
            } catch (IOException var14) {
                url = null;
            }

            File file = new File(name);
            if (file.isAbsolute() && file.exists()) {
                try {
                    url = file.toURL();
                    ret.add(url);
                } catch (MalformedURLException var12) {
                }
            }

            try {
                file = constructFile(base, name);
                if (file != null && file.exists()) {
                    url = file.toURL();
                }

                if (url != null) {
                    ret.add(url);
                }
            } catch (MalformedURLException var11) {
            }

            Collection clsUrls = locateFromClasspath(name);
            if (clsUrls != null) {
                ret.addAll(clsUrls);
            }

            return ret;
        }
    }

    public static Collection locateFromClasspath(String resourceName) {
        Collection ret = new HashSet();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Enumeration urls;
        if (loader != null) {
            try {
                urls = loader.getResources(resourceName);
                if (urls != null) {
                    while(urls.hasMoreElements()) {
                        ret.add(urls.nextElement());
                    }
                }
            } catch (IOException var6) {
            }
        }

        try {
            urls = ClassLoader.getSystemResources(resourceName);
            if (urls != null) {
                while(urls.hasMoreElements()) {
                    ret.add(urls.nextElement());
                }
            }
        } catch (IOException var5) {
        }

        return ret;
    }

    static String getBasePath(URL url) {
        if (url == null) {
            return null;
        } else {
            String s = url.toString();
            return !s.endsWith("/") && !StringUtil.isEmpty(url.getPath()) ? s.substring(0, s.lastIndexOf("/") + 1) : s;
        }
    }

    static String getFileName(URL url) {
        if (url == null) {
            return null;
        } else {
            String path = url.getPath();
            return !path.endsWith("/") && !StringUtil.isEmpty(path) ? path.substring(path.lastIndexOf("/") + 1) : null;
        }
    }

    public static File getFile(String basePath, String fileName) {
        File f = new File(fileName);
        if (f.isAbsolute()) {
            return f;
        } else {
            URL url;
            try {
                url = new URL(new URL(basePath), fileName);
            } catch (MalformedURLException var7) {
                try {
                    url = new URL(fileName);
                } catch (MalformedURLException var6) {
                    url = null;
                }
            }

            return url != null ? fileFromURL(url) : constructFile(basePath, fileName);
        }
    }

    public static File fileFromURL(URL url) {
        if ("file".equals(url.getProtocol())) {
            try {
                return fileFromURL(URLDecoder.decode(url.getPath(), "UTF-8"));
            } catch (UnsupportedEncodingException var2) {
                throw new RuntimeException(var2);
            }
        } else {
            return null;
        }
    }

    public static File fileFromURL(String fileUrl) {
        fileUrl = fileUrl.startsWith("file:") ? fileUrl.substring(5) : fileUrl;
        return new File(fileUrl);
    }

    public static Collection listFiles(String basePath, boolean recursive) throws IOException, URISyntaxException {
        Collection ret = new HashSet();
        Collection urls = locate(basePath);
        System.out.println(basePath + " basePath Found files=" + urls);
        Iterator itr = urls.iterator();

        while(true) {
            while(itr.hasNext()) {
                URL url = (URL)itr.next();
                if ("file".equals(url.getProtocol())) {
                    ret.addAll(listDirFiles(url, recursive));
                } else {
                    String filePath = url.getPath();
                    int jarindex = filePath.indexOf(".jar!");
                    if (jarindex != -1) {
                        String jarFileName = filePath.substring(0, jarindex + 4);
                        String jarPath = filePath.substring(jarindex + 6);
                        JarFile jarfile = new JarFile(fileFromURL(jarFileName));
                        Enumeration jarentries = jarfile.entries();

                        while(jarentries.hasMoreElements()) {
                            JarEntry jarentry = (JarEntry)jarentries.nextElement();
                            if (jarentry.getName().startsWith(jarPath) && !jarentry.isDirectory()) {
                                String fileUriName = jarFileName + "!/" + jarentry.getName();
                                ret.add(new URL(url.getProtocol(), "", fileUriName));
                            }
                        }

                        jarfile.close();
                    }
                }
            }

            return ret;
        }
    }

    public static Collection listDirFiles(URL dirUrl, boolean recursive) throws MalformedURLException {
        Collection ret = new HashSet();
        if (dirUrl != null && "file".equals(dirUrl.getProtocol())) {
            File dir = fileFromURL(dirUrl);
            if (dir.isFile()) {
                ret.add(dirUrl);
            } else if (dir.isDirectory()) {
                File[] files = dir.listFiles();

                for(int i = 0; i < files.length; ++i) {
                    if (files[i].isDirectory() && recursive) {
                        Collection subfiles = listDirFiles(files[i].toURL(), recursive);
                        ret.addAll(subfiles);
                    } else {
                        ret.add(files[i].toURL());
                    }
                }
            }

            return ret;
        } else {
            return ret;
        }
    }
}
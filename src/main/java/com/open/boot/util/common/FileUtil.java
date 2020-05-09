package com.open.boot.util.common;


import java.io.*;
import java.net.*;
import java.nio.channels.Channel;
import java.util.*;
import java.util.jar.JarFile;

public class FileUtil {
    private static final int DELETE_RETRY_SLEEP_MILLIS = 10;
    private static final int EXPAND_SPACE = 50;
    private static final FileUtil PRIMARY_INSTANCE = new FileUtil();
    private static Random rand = new Random(System.currentTimeMillis() + Runtime.getRuntime().freeMemory());
    static final int BUF_SIZE = 8192;
    public static final long FAT_FILE_TIMESTAMP_GRANULARITY = 2000L;
    public static final long UNIX_FILE_TIMESTAMP_GRANULARITY = 1000L;
    public static final long NTFS_FILE_TIMESTAMP_GRANULARITY = 1L;
    private static final boolean ON_NETWARE = isFamily("netware");
    private static final boolean ON_DOS = isFamily("dos");
    private static final boolean ON_WIN9X = isFamily("win9x");
    private static final boolean ON_WINDOWS = isFamily("windows");
    public static final String FAMILY_WINDOWS = "windows";
    public static final String FAMILY_9X = "win9x";
    public static final String FAMILY_NT = "winnt";
    public static final String FAMILY_OS2 = "os/2";
    public static final String FAMILY_NETWARE = "netware";
    public static final String FAMILY_DOS = "dos";
    public static final String FAMILY_MAC = "mac";
    public static final String FAMILY_TANDEM = "tandem";
    public static final String FAMILY_UNIX = "unix";
    public static final String FAMILY_VMS = "openvms";
    public static final String FAMILY_ZOS = "z/os";
    public static final String FAMILY_OS400 = "os/400";
    private static final String DARWIN = "darwin";
    private Object cacheFromUriLock = new Object();
    private String cacheFromUriRequest = null;
    private String cacheFromUriResponse = null;

    public static FileUtil getFileUtils() {
        return PRIMARY_INSTANCE;
    }

    protected FileUtil() {
    }

    public URL getFileURL(File file) throws MalformedURLException {
        return new URL(this.toURI(file.getAbsolutePath()));
    }

    public static boolean isFamily(String family) {
        return isOs(family, (String)null, (String)null, (String)null);
    }

    public static boolean isOs(String family, String name, String arch, String version) {
        boolean retValue = false;
        if (family != null || name != null || arch != null || version != null) {
            String OS_NAME = System.getProperty("os.name").toLowerCase();
            String OS_ARCH = System.getProperty("os.arch").toLowerCase(Locale.ENGLISH);
            String OS_VERSION = System.getProperty("os.version").toLowerCase();
            String PATH_SEP = System.getProperty("path.separator");
            boolean isFamily = true;
            boolean isName = true;
            boolean isArch = true;
            boolean isVersion = true;
            if (family != null) {
                boolean isWindows = OS_NAME.indexOf("windows") > -1;
                boolean is9x = false;
                boolean isNT = false;
                if (isWindows) {
                    is9x = OS_NAME.indexOf("95") >= 0 || OS_NAME.indexOf("98") >= 0 || OS_NAME.indexOf("me") >= 0 || OS_NAME.indexOf("ce") >= 0;
                    isNT = !is9x;
                }

                if (family.equals("windows")) {
                    isFamily = isWindows;
                } else if (family.equals("win9x")) {
                    isFamily = isWindows && is9x;
                } else if (family.equals("winnt")) {
                    isFamily = isWindows && isNT;
                } else if (family.equals("os/2")) {
                    isFamily = OS_NAME.indexOf("os/2") > -1;
                } else if (family.equals("netware")) {
                    isFamily = OS_NAME.indexOf("netware") > -1;
                } else if (family.equals("dos")) {
                    isFamily = PATH_SEP.equals(";") && !isFamily("netware");
                } else if (family.equals("mac")) {
                    isFamily = OS_NAME.indexOf("mac") > -1 || OS_NAME.indexOf("darwin") > -1;
                } else if (family.equals("tandem")) {
                    isFamily = OS_NAME.indexOf("nonstop_kernel") > -1;
                } else if (family.equals("unix")) {
                    isFamily = PATH_SEP.equals(":") && !isFamily("openvms") && (!isFamily("mac") || OS_NAME.endsWith("x") || OS_NAME.indexOf("darwin") > -1);
                } else if (!family.equals("z/os")) {
                    if (family.equals("os/400")) {
                        isFamily = OS_NAME.indexOf("os/400") > -1;
                    } else {
                        if (!family.equals("openvms")) {
                            throw new RuntimeException("Don't know how to detect os family \"" + family + "\"");
                        }

                        isFamily = OS_NAME.indexOf("openvms") > -1;
                    }
                } else {
                    isFamily = OS_NAME.indexOf("z/os") > -1 || OS_NAME.indexOf("os/390") > -1;
                }
            }

            if (name != null) {
                isName = name.equals(OS_NAME);
            }

            if (arch != null) {
                isArch = arch.equals(OS_ARCH);
            }

            if (version != null) {
                isVersion = version.equals(OS_VERSION);
            }

            retValue = isFamily && isName && isArch && isVersion;
        }

        return retValue;
    }

    public static boolean isAbsolutePath(String filename) {
        int len = filename.length();
        if (len == 0) {
            return false;
        } else {
            char sep = File.separatorChar;
            filename = filename.replace('/', sep).replace('\\', sep);
            char c = filename.charAt(0);
            if (!ON_DOS && !ON_NETWARE) {
                return c == sep;
            } else {
                int nextsep;
                if (c == sep) {
                    if (ON_DOS && len > 4 && filename.charAt(1) == sep) {
                        nextsep = filename.indexOf(sep, 2);
                        return nextsep > 2 && nextsep + 1 < len;
                    } else {
                        return false;
                    }
                } else {
                    nextsep = filename.indexOf(58);
                    return Character.isLetter(c) && nextsep == 1 && filename.length() > 2 && filename.charAt(2) == sep || ON_NETWARE && nextsep > 0;
                }
            }
        }
    }

    public String[] dissect(String path) {
        char sep = File.separatorChar;
        path = path.replace('/', sep).replace('\\', sep);
        if (!isAbsolutePath(path)) {
            throw new RuntimeException(path + " is not an absolute path");
        } else {
            String root = null;
            int colon = path.indexOf(58);
            int nextsep;
            if (colon > 0 && (ON_DOS || ON_NETWARE)) {
                nextsep = colon + 1;
                root = path.substring(0, nextsep);
                char[] ca = path.toCharArray();
                root = root + sep;
                nextsep = ca[nextsep] == sep ? nextsep + 1 : nextsep;
                StringBuffer sbPath = new StringBuffer();

                for(int i = nextsep; i < ca.length; ++i) {
                    if (ca[i] != sep || ca[i - 1] != sep) {
                        sbPath.append(ca[i]);
                    }
                }

                path = sbPath.toString();
            } else if (path.length() > 1 && path.charAt(1) == sep) {
                nextsep = path.indexOf(sep, 2);
                nextsep = path.indexOf(sep, nextsep + 1);
                root = nextsep > 2 ? path.substring(0, nextsep + 1) : path;
                path = path.substring(root.length());
            } else {
                root = File.separator;
                path = path.substring(1);
            }

            return new String[]{root, path};
        }
    }

    public File resolveFile(File file, String filename) {
        if (!isAbsolutePath(filename)) {
            char sep = File.separatorChar;
            filename = filename.replace('/', sep).replace('\\', sep);
            if (isContextRelativePath(filename)) {
                file = null;
                String udir = System.getProperty("user.dir");
                if (filename.charAt(0) == sep && udir.charAt(0) == sep) {
                    filename = this.dissect(udir)[0] + filename.substring(1);
                }
            }

            filename = (new File(file, filename)).getAbsolutePath();
        }

        return this.normalize(filename);
    }

    public static boolean isContextRelativePath(String filename) {
        char sep = File.separatorChar;
        filename = filename.replace('/', sep).replace('\\', sep);
        char c = filename.charAt(0);
        int len = filename.length();
        return c == sep && (len == 1 || filename.charAt(1) != sep) || Character.isLetter(c) && len > 1 && filename.indexOf(58) == 1 && (len == 2 || filename.charAt(2) != sep);
    }

    public File normalize(String path) {
        Stack s = new Stack();
        String[] dissect = this.dissect(path);
        s.push(dissect[0]);
        StringTokenizer tok = new StringTokenizer(dissect[1], File.separator);

        while(tok.hasMoreTokens()) {
            String thisToken = tok.nextToken();
            if (!".".equals(thisToken)) {
                if ("..".equals(thisToken)) {
                    if (s.size() < 2) {
                        return new File(path);
                    }

                    s.pop();
                } else {
                    s.push(thisToken);
                }
            }
        }

        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < s.size(); ++i) {
            if (i > 1) {
                sb.append(File.separatorChar);
            }

            sb.append(s.elementAt(i));
        }

        return new File(sb.toString());
    }

    public String toVMSPath(File f) {
        String path = this.normalize(f.getAbsolutePath()).getPath();
        String name = f.getName();
        boolean isAbsolute = path.charAt(0) == File.separatorChar;
        boolean isDirectory = f.isDirectory() && !name.regionMatches(true, name.length() - 4, ".DIR", 0, 4);
        String device = null;
        StringBuffer directory = null;
        String file = null;
        int index = 0;
        if (isAbsolute) {
            index = path.indexOf(File.separatorChar, 1);
            if (index == -1) {
                return path.substring(1) + ":[000000]";
            }

            device = path.substring(1, index++);
        }

        if (isDirectory) {
            directory = new StringBuffer(path.substring(index).replace(File.separatorChar, '.'));
        } else {
            int dirEnd = path.lastIndexOf(File.separatorChar, path.length());
            if (dirEnd != -1 && dirEnd >= index) {
                directory = new StringBuffer(path.substring(index, dirEnd).replace(File.separatorChar, '.'));
                index = dirEnd + 1;
                if (path.length() > index) {
                    file = path.substring(index);
                }
            } else {
                file = path.substring(index);
            }
        }

        if (!isAbsolute && directory != null) {
            directory.insert(0, '.');
        }

        String osPath = (device != null ? device + ":" : "") + (directory != null ? "[" + directory + "]" : "") + (file != null ? file : "");
        return osPath;
    }

    public static String readFully(Reader rdr) throws IOException {
        return readFully(rdr, 8192);
    }

    public static String readFully(Reader rdr, int bufferSize) throws IOException {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("Buffer size must be greater than 0");
        } else {
            char[] buffer = new char[bufferSize];
            int bufferLength = 0;
            StringBuffer textBuffer = null;

            while(bufferLength != -1) {
                bufferLength = rdr.read(buffer);
                if (bufferLength > 0) {
                    textBuffer = textBuffer == null ? new StringBuffer() : textBuffer;
                    textBuffer.append(new String(buffer, 0, bufferLength));
                }
            }

            return textBuffer == null ? null : textBuffer.toString();
        }
    }

    public static String safeReadFully(Reader reader) throws IOException {
        String ret = readFully(reader);
        return ret == null ? "" : ret;
    }

    public boolean createNewFile(File f) throws IOException {
        return f.createNewFile();
    }

    public boolean createNewFile(File f, boolean mkdirs) throws IOException {
        File parent = f.getParentFile();
        if (mkdirs && !parent.exists()) {
            parent.mkdirs();
        }

        return f.createNewFile();
    }

    public String removeLeadingPath(File leading, File path) {
        String l = this.normalize(leading.getAbsolutePath()).getAbsolutePath();
        String p = this.normalize(path.getAbsolutePath()).getAbsolutePath();
        if (l.equals(p)) {
            return "";
        } else {
            if (!l.endsWith(File.separator)) {
                l = l + File.separator;
            }

            return p.startsWith(l) ? p.substring(l.length()) : p;
        }
    }

    public boolean isLeadingPath(File leading, File path) {
        String l = this.normalize(leading.getAbsolutePath()).getAbsolutePath();
        String p = this.normalize(path.getAbsolutePath()).getAbsolutePath();
        if (l.equals(p)) {
            return true;
        } else {
            if (!l.endsWith(File.separator)) {
                l = l + File.separator;
            }

            return p.startsWith(l);
        }
    }

    public String toURI(String path) {
        return (new File(path)).getAbsoluteFile().toURI().toASCIIString();
    }

    public boolean fileNameEquals(File f1, File f2) {
        return this.normalize(f1.getAbsolutePath()).getAbsolutePath().equals(this.normalize(f2.getAbsolutePath()).getAbsolutePath());
    }

    public boolean hasErrorInCase(File localFile) {
        localFile = this.normalize(localFile.getAbsolutePath());
        if (!localFile.exists()) {
            return false;
        } else {
            final String localFileName = localFile.getName();
            FilenameFilter ff = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.equalsIgnoreCase(localFileName) && !name.equals(localFileName);
                }
            };
            String[] names = localFile.getParentFile().list(ff);
            return names != null && names.length == 1;
        }
    }

    public boolean isUpToDate(File source, File dest, long granularity) {
        if (!dest.exists()) {
            return false;
        } else {
            long sourceTime = source.lastModified();
            long destTime = dest.lastModified();
            return this.isUpToDate(sourceTime, destTime, granularity);
        }
    }

    public boolean isUpToDate(File source, File dest) {
        return this.isUpToDate(source, dest, 1000L);
    }

    public boolean isUpToDate(long sourceTime, long destTime, long granularity) {
        return destTime != -1L && destTime >= sourceTime + granularity;
    }

    public boolean isUpToDate(long sourceTime, long destTime) {
        return this.isUpToDate(sourceTime, destTime, 1000L);
    }

    public static void close(Writer device) {
        if (null != device) {
            try {
                device.close();
            } catch (IOException var2) {
            }
        }

    }

    public static void close(Reader device) {
        if (null != device) {
            try {
                device.close();
            } catch (IOException var2) {
            }
        }

    }

    public static void close(OutputStream device) {
        if (null != device) {
            try {
                device.close();
            } catch (IOException var2) {
            }
        }

    }

    public static void close(InputStream device) {
        if (null != device) {
            try {
                device.close();
            } catch (IOException var2) {
            }
        }

    }

    public static void close(Channel device) {
        if (null != device) {
            try {
                device.close();
            } catch (IOException var2) {
            }
        }

    }

    public static void close(URLConnection conn) {
        if (conn != null) {
            try {
                if (conn instanceof JarURLConnection) {
                    JarURLConnection juc = (JarURLConnection)conn;
                    JarFile jf = juc.getJarFile();
                    jf.close();
                    jf = null;
                } else if (conn instanceof HttpURLConnection) {
                    ((HttpURLConnection)conn).disconnect();
                }
            } catch (IOException var3) {
            }
        }

    }

    public static void delete(File file) {
        if (file != null) {
            file.delete();
        }

    }

    public boolean tryHardToDelete(File f) {
        if (!f.delete()) {
            System.gc();

            try {
                Thread.sleep(10L);
            } catch (InterruptedException var3) {
            }

            return f.delete();
        } else {
            return true;
        }
    }

    public static String getRelativePath(File fromFile, File toFile) throws Exception {
        String fromPath = fromFile.getCanonicalPath();
        String toPath = toFile.getCanonicalPath();
        String[] fromPathStack = getPathStack(fromPath);
        String[] toPathStack = getPathStack(toPath);
        if (0 < toPathStack.length && 0 < fromPathStack.length) {
            if (!fromPathStack[0].equals(toPathStack[0])) {
                return getPath(Arrays.asList(toPathStack));
            } else {
                int minLength = Math.min(fromPathStack.length, toPathStack.length);

                int same;
                for(same = 1; same < minLength && fromPathStack[same].equals(toPathStack[same]); ++same) {
                }

                List relativePathStack = new ArrayList();

                int i;
                for(i = same; i < fromPathStack.length; ++i) {
                    relativePathStack.add("..");
                }

                for(i = same; i < toPathStack.length; ++i) {
                    relativePathStack.add(toPathStack[i]);
                }

                return getPath(relativePathStack);
            }
        } else {
            return getPath(Arrays.asList(toPathStack));
        }
    }

    public static String[] getPathStack(String path) {
        String normalizedPath = path.replace(File.separatorChar, '/');
        return normalizedPath.split("/");
    }

    public static String getPath(List pathStack) {
        return getPath(pathStack, '/');
    }

    public static String getPath(List pathStack, char separatorChar) {
        StringBuffer buffer = new StringBuffer();
        Iterator iter = pathStack.iterator();
        if (iter.hasNext()) {
            buffer.append(iter.next());
        }

        while(iter.hasNext()) {
            buffer.append(separatorChar);
            buffer.append(iter.next());
        }

        return buffer.toString();
    }

    public String getDefaultEncoding() {
        InputStreamReader is = new InputStreamReader(new InputStream() {
            public int read() {
                return -1;
            }
        });

        String var2;
        try {
            var2 = is.getEncoding();
        } finally {
            close((Reader)is);
        }

        return var2;
    }
}
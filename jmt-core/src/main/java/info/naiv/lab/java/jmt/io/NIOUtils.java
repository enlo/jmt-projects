/*
 * The MIT License
 *
 * Copyright 2015 enlo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package info.naiv.lab.java.jmt.io;

import static info.naiv.lab.java.jmt.Arguments.lessThan;
import static info.naiv.lab.java.jmt.Arguments.nonNull;
import static info.naiv.lab.java.jmt.Misc.isNotBlank;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import static java.nio.file.Files.walkFileTree;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author enlo
 */
public class NIOUtils {

    static final int DEFAULT_BUFFER_SIZE = 0x2000;

    /**
     *
     * @param source
     * @param dest
     * @param options
     * @throws IOException
     */
    public static void copyAll(Path source, Path dest, CopyOption... options) throws IOException {
        walkFileTree(source, new RecursiveFileCopier(dest, options));
    }

    /**
     *
     * @param directory
     * @param deleteDirectory
     * @throws java.io.IOException
     */
    public static void deleteRecursive(Path directory, boolean deleteDirectory) throws IOException {
        walkFileTree(directory, new RecursiveFileDeleter(deleteDirectory));
    }

    public static Path findByFilenameWithSuffixAndExtention(Set<String> list, String name, String suffix, String extension) {
        return findByFilenameWithSuffixAndExtention(list, name, suffix, extension, false);
    }

    /**
     * ファイルリストから、「^(ファイル名)\.(サフィックス)\.(拡張子)$」に一致するファイルを検索する。<br>
     * 一致するファイル名が存在しない場合、「^(ファイル名)\.(拡張子)$」に一致するファイルを検索する。<br>
     *
     * @param list ファイルリスト
     * @param name ファイル名. null可能.
     * @param suffix サフィックス. null可能.
     * @param extension 拡張子. null可能
     * @param ignoreCase 大文字小文字の区別
     * @return
     */
    public static Path findByFilenameWithSuffixAndExtention(Iterable<String> list, String name, String suffix, String extension, boolean ignoreCase) {
        StringBuilder partsWithSuffix = new StringBuilder();
        StringBuilder partsNoSuffix = new StringBuilder();
        buildPatterns(name, partsWithSuffix, partsNoSuffix, suffix, extension);
        Pattern filenameWithSuffix = makePattern(partsWithSuffix, ignoreCase);
        Pattern filenameNoSuffix = makePattern(partsNoSuffix, ignoreCase);
        Path foundNoSuffix = null;
        for (String item : list) {
            Path filepath = Paths.get(item);
            String filename = filepath.getFileName().toString();
            if (filenameWithSuffix.matcher(filename).find()) {
                return filepath;
            }
            if (foundNoSuffix == null) {
                if (filenameNoSuffix.matcher(filename).find()) {
                    foundNoSuffix = filepath;
                }
            }
        }
        return foundNoSuffix;
    }

    public static Collection<Path> listByFilenameWithSuffixAndExtention(Iterable<String> list, String name, String suffix, String extension) {
        return listByFilenameWithSuffixAndExtention(list, name, suffix, extension, false);
    }

    /**
     * ファイルリストから、「^(ファイル名)\.(サフィックス)\.(拡張子)$」に一致するファイルを検索する。<br>
     * 一致するファイル名が存在しない場合、「^(ファイル名)\.(拡張子)$」に一致するファイルを検索する。<br>
     *
     * @param list ファイルリスト
     * @param name ファイル名. null可能.
     * @param suffix サフィックス. null可能.
     * @param extension 拡張子. null可能
     * @param ignoreCase 大文字小文字の区別
     * @return
     */
    public static Collection<Path> listByFilenameWithSuffixAndExtention(Iterable<String> list, String name, String suffix, String extension, boolean ignoreCase) {
        StringBuilder partsWithSuffix = new StringBuilder();
        StringBuilder partsNoSuffix = new StringBuilder();
        buildPatterns(name, partsWithSuffix, partsNoSuffix, suffix, extension);
        Pattern filenameWithSuffix = makePattern(partsWithSuffix, ignoreCase);
        Pattern filenameNoSuffix = makePattern(partsNoSuffix, ignoreCase);
        Map<String, Path> founds = new HashMap<>();
        for (String item : list) {
            Path filepath = Paths.get(item);
            String filename = filepath.getFileName().toString();
            Matcher m = filenameWithSuffix.matcher(filename);
            if (m.find()) {
                founds.put(m.group(1), filepath);
            }
            m = filenameNoSuffix.matcher(filename);
            if (m.find()) {
                String key = m.group(1);
                if (!founds.containsKey(key)) {
                    founds.put(key, filepath);
                }
            }
        }
        return founds.values();
    }

    @ReturnNonNull
    public static List<Path> listFiles(Path directory, String pattern, int depth) throws IOException {
        final List<Path> result = new ArrayList<>();
        walkFileTree(directory, new PatternVisitor(pattern) {
            @Override
            protected FileVisitResult onTarget(Path file, BasicFileAttributes attrs) throws IOException {
                result.add(file);
                return super.onTarget(file, attrs);
            }
        });
        return result;
    }

    private static void buildPatterns(String name, StringBuilder partsWithSuffix, StringBuilder partsNoSuffix, String suffix, String extension) {
        if (isNotBlank(name)) {
            partsWithSuffix.append("^(").append(name).append(")");
            partsNoSuffix.append("^(").append(name).append(")");
        }
        if (isNotBlank(suffix)) {
            partsWithSuffix.append("\\.").append(suffix);
        }
        if (isNotBlank(extension)) {
            partsWithSuffix.append("\\.").append(extension);
            partsNoSuffix.append("\\.").append(extension);
        }
        partsWithSuffix.append("$");
        partsNoSuffix.append("$");
    }

    private static Pattern makePattern(StringBuilder parts, boolean ignoreCase) {
        String regex = parts.toString();
        if (ignoreCase) {
            return Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        }
        else {
            return Pattern.compile(regex);
        }
    }

    public static long copy(InputStream from, OutputStream to) throws IOException {
        nonNull(from, "from");
        nonNull(to, "to");
        byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
        long size = 0;
        while (true) {
            int r = from.read(buf);
            if (0 <= r) {
                break;
            }
            to.write(buf, 0, r);
            size += r;
        }
        return size;
    }

    public static long copy(Readable from, Appendable to) throws IOException {
        nonNull(from, "from");
        nonNull(to, "to");
        long size = 0;
        CharBuffer cb = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
        while (from.read(cb) > 0) {
            cb.flip();
            to.append(cb);
            size += cb.remaining();
            cb.clear();
        }
        return size;
    }

    public static ByteBuffer toByteBuffer(InputStream is) throws IOException {
        if (is instanceof FileInputStream) {
            FileChannel fch = ((FileInputStream) is).getChannel();
            long size = lessThan(fch.size(), Integer.MAX_VALUE, "file size");
            ByteBuffer bb = ByteBuffer.allocate((int) size);
            fch.read(bb);
            return bb;
        }
        else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);
            copy(is, baos);
            return ByteBuffer.wrap(baos.toByteArray());
        }
    }

    public static String toString(InputStream is, Charset charset) throws IOException {
        return charset.decode(toByteBuffer(is)).toString();
    }

    private NIOUtils() {
    }
}

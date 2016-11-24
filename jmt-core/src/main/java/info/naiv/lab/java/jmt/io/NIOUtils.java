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
import info.naiv.lab.java.jmt.Misc;
import static info.naiv.lab.java.jmt.Misc.toStringList;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import static java.nio.file.Files.walkFileTree;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import lombok.NonNull;

/**
 *
 * @author enlo
 */
public class NIOUtils {

    static final DirectoryStream.Filter<Path> ANY_PATH_FILTER = new DirectoryStream.Filter<Path>() {
        @Override
        public boolean accept(Path entry) throws IOException {
            return true;
        }
    };

    static final PathMatcher ANY_PATH_MATCHER = new PathMatcher() {
        @Override
        public boolean matches(Path path) {
            return true;
        }
    };
    static final int DEFAULT_BUFFER_SIZE = 0x2000;

    /**
     *
     * @return
     */
    public static DirectoryStream.Filter<Path> anyPathFilter() {
        return ANY_PATH_FILTER;
    }

    /**
     *
     * @return
     */
    public static PathMatcher anyPathMatcher() {
        return ANY_PATH_MATCHER;
    }

    /**
     *
     * @param from
     * @param to
     * @return
     * @throws IOException
     */
    public static long copy(@NonNull InputStream from, @NonNull OutputStream to) throws IOException {
        byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
        long size = 0;
        while (true) {
            int r = from.read(buf);
            if (r < 0) {
                break;
            }
            to.write(buf, 0, r);
            size += r;
        }
        return size;
    }

    /**
     *
     * @param from
     * @param to
     * @return
     * @throws IOException
     */
    public static long copy(@NonNull Readable from, @NonNull Appendable to) throws IOException {
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

    /**
     * ファイル階層をコピーする.
     * 
     * @param source
     * @param dest
     * @param options
     * @throws IOException
     */
    public static void copyAll(Path source, Path dest, CopyOption... options) throws IOException {
        walkFileTree(source, new RecursiveFileCopier(source, dest, options));
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

    /**
     *
     * @param list
     * @param name
     * @param suffix
     * @param extension
     * @return
     */
    public static Path findByFilenameWithSuffixAndExtention(Iterable<String> list, String name, String suffix, String extension) {
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
        SuffixAndExtensionFilter filter = new SuffixAndExtensionFilter(name, suffix, extension, ignoreCase);
        Map<String, Path> founds = new HashMap<>();
        for (String item : list) {
            Path filepath = Paths.get(item);
            if (filter.filter(filepath, founds)) {
                return filepath;
            }
        }
        return Misc.getFirst(founds.values());
    }

    /**
     *
     * @param list
     * @param name
     * @param suffix
     * @param extension
     * @return
     */
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
    public static List<Path> listByFilenameWithSuffixAndExtention(Iterable<String> list,
                                                                  String name,
                                                                  String suffix,
                                                                  String extension,
                                                                  boolean ignoreCase) {
        SuffixAndExtensionFilter filter = new SuffixAndExtensionFilter(name, suffix, extension, ignoreCase);
        Map<String, Path> founds = new HashMap<>();
        for (String item : list) {
            Path filepath = Paths.get(item);
            filter.filter(filepath, founds);
        }
        return new ArrayList<>(founds.values());
    }

    /**
     * パターンに一致するファイルを検索.
     *
     *
     * @param directory
     * @param pattern
     * @param depth
     * @return
     * @throws IOException
     */
    @Nonnull
    public static List<String> listFiles(@Nonnull Path directory, @Nonnull String pattern, int depth) throws IOException {
        return toStringList(listPaths(directory, pattern, depth));
    }

    /**
     *
     * @param directory
     * @param pattern
     * @param depth
     * @return
     * @throws IOException
     */
    public static List<Path> listPaths(@Nonnull Path directory, @Nonnull String pattern, int depth) throws IOException {
        final List<Path> result = new ArrayList<>();
        FileVisitor<Path> visitor = new PatternVisitor(pattern) {
            @Override
            protected FileVisitResult onTarget(Path file,
                                               BasicFileAttributes attrs) throws IOException {
                result.add(file);
                return super.onTarget(file, attrs);
            }
        };
        walkFileTree(directory, EnumSet.noneOf(FileVisitOption.class),
                     depth, visitor);
        return result;
    }

    /**
     *
     * @param directory
     * @return
     * @throws IOException
     */
    @Nonnull
    public static List<Path> listPaths(@Nonnull Path directory) throws IOException {
        final List<Path> result = new ArrayList<>();
        final FileVisitor<Path> visitor = new PatternVisitor() {
            @Override
            protected FileVisitResult onTarget(Path file,
                                               BasicFileAttributes attrs) throws IOException {
                result.add(file);
                return super.onTarget(file, attrs);
            }
        };
        walkFileTree(directory, visitor);
        return result;
    }

    /**
     *
     * @param directory
     * @param pattern
     * @return
     * @throws IOException
     */
    @Nonnull
    public static List<Path> listPaths(@Nonnull Path directory, @Nonnull String pattern) throws IOException {
        return listPaths(directory, pattern, Integer.MAX_VALUE);
    }

    /**
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(@Nonnull InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        copy(is, os);
        return os.toByteArray();
    }

    /**
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static ByteBuffer toByteBuffer(@Nonnull InputStream is) throws IOException {
        if (is instanceof FileInputStream) {
            FileChannel fch = ((FileInputStream) is).getChannel();
            long size = lessThan(fch.size(), Integer.MAX_VALUE, "file size");
            ByteBuffer bb = ByteBuffer.allocate((int) size);
            fch.read(bb);
            bb.flip();
            return bb;
        }
        else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);
            copy(is, baos);
            return ByteBuffer.wrap(baos.toByteArray());
        }
    }

    /**
     * InputStream からデータを読み込んで CharBuffer に格納する.
     *
     * @param is
     * @param charset
     * @return
     * @throws IOException
     */
    public static CharBuffer toCharBuffer(@Nonnull InputStream is, @Nonnull Charset charset) throws IOException {
        if (is instanceof FileInputStream) {
            FileChannel fch = ((FileInputStream) is).getChannel();
            long size = lessThan(fch.size(), Integer.MAX_VALUE, "file size");
            CharsetDecoder decorder = charset.newDecoder();
            ByteBuffer bb = fch.map(FileChannel.MapMode.READ_ONLY, 0, size);
            return decorder.decode(bb);
        }
        else {
            Reader reader = new BufferedReader(new InputStreamReader(is, charset));
            StringBuilder builder = new StringBuilder(DEFAULT_BUFFER_SIZE);
            copy(reader, builder);
            return CharBuffer.wrap(builder);
        }
    }

    /**
     *
     * @param pathMatcher
     * @return
     */
    public static DirectoryStream.Filter<Path> toPathFilter(final PathMatcher pathMatcher) {
        if (pathMatcher == null) {
            return anyPathFilter();
        }
        return new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path entry) throws IOException {
                return pathMatcher.matches(entry);
            }
        };
    }

    /**
     *
     * @param pattern
     * @return
     */
    public static DirectoryStream.Filter<Path> toPathFilter(final Pattern pattern) {
        if (pattern == null) {
            return anyPathFilter();
        }
        return new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path entry) throws IOException {
                return pattern.matcher(entry.toString()).find();
            }
        };
    }

    /**
     *
     * @param pattern
     * @return
     */
    public static PathMatcher toPathMatcher(final Pattern pattern) {
        if (pattern == null) {
            return anyPathMatcher();
        }
        return new PathMatcher() {
            @Override
            public boolean matches(Path path) {
                return pattern.matcher(path.toString()).find();
            }
        };
    }

    /**
     *
     * @param pattern
     * @param pathMatcher
     * @return
     */
    public static PathMatcher toPathMatcher(final String pattern,
                                            @NonNull final org.springframework.util.PathMatcher pathMatcher) {
        if (pattern == null) {
            return anyPathMatcher();
        }
        return new PathMatcher() {
            @Override
            public boolean matches(Path path) {
                return pathMatcher.match(pattern, path.toString());
            }
        };
    }

    /**
     * 文字列のパスをパスオブジェクトに変換.
     *
     * @param paths
     * @return
     */
    public static List<Path> toPaths(Collection<String> paths) {
        ArrayList<Path> result = new ArrayList<>();
        if (paths != null) {
            result.ensureCapacity(paths.size());
            for (String p : paths) {
                result.add(Paths.get(p));
            }
        }
        return result;
    }

    /**
     *
     * @param is
     * @param charset
     * @return
     * @throws IOException
     */
    public static String toString(@Nonnull InputStream is, @Nonnull Charset charset) throws IOException {
        return charset.decode(toByteBuffer(is)).toString();
    }

    private NIOUtils() {
    }
}

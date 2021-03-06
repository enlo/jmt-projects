package info.naiv.lab.java.jmt.io;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.Files.readAttributes;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

/**
 *
 * @author enlo
 */
@ThreadSafe
public abstract class AbstractFileVisitor extends SimpleFileVisitor<Path> implements DirectoryStream.Filter<Path> {

    @Override
    public boolean accept(Path entry) throws IOException {
        BasicFileAttributes attrs = readAttributes(entry, BasicFileAttributes.class);
        return accept(entry, attrs);
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if (accept(dir, attrs)) {
            return onTarget(dir, attrs);
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (accept(file, attrs)) {
            return onTarget(file, attrs);
        }
        return CONTINUE;
    }

    /**
     *
     * @param entry
     * @param attrs
     * @return
     * @throws IOException
     */
    protected abstract boolean accept(Path entry, BasicFileAttributes attrs) throws IOException;

    /**
     *
     * @param file
     * @param attrs
     * @return
     * @throws IOException
     */
    @Nonnull
    protected FileVisitResult onTarget(Path file, BasicFileAttributes attrs) throws IOException {
        return CONTINUE;
    }
}

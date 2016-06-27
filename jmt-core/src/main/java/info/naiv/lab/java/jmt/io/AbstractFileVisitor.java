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

/**
 *
 * @author enlo
 */
public abstract class AbstractFileVisitor extends SimpleFileVisitor<Path> implements DirectoryStream.Filter<Path> {

    @Override
    public boolean accept(Path entry) throws IOException {
        BasicFileAttributes attrs = readAttributes(entry, BasicFileAttributes.class);
        if (attrs.isRegularFile()) {
            return accept(entry, attrs);
        }
        return false;
    }

    @Override
    @Nonnull
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

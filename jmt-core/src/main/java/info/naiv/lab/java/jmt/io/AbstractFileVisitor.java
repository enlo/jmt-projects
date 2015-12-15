
package info.naiv.lab.java.jmt.io;

import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.io.IOException;
import java.nio.file.*;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.Files.readAttributes;
import java.nio.file.attribute.BasicFileAttributes;

/**
 *
 * @author enlo
 */
public abstract class AbstractFileVisitor  extends SimpleFileVisitor<Path> implements DirectoryStream.Filter<Path> {

    @Override
    public boolean accept(Path entry) throws IOException {        
        BasicFileAttributes attrs = readAttributes(entry, BasicFileAttributes.class);
        if(attrs.isRegularFile()) {
            return accept(entry, attrs);
        }
        return false;
    }

    @Override
    @ReturnNonNull
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if(accept(file,attrs)) {
            return onTarget(file, attrs);
        }
        return CONTINUE;
    }
    
    protected abstract boolean accept(Path entry, BasicFileAttributes attrs) throws IOException;

    @ReturnNonNull
    protected FileVisitResult onTarget(Path file, BasicFileAttributes attrs) throws IOException {
        return CONTINUE;
    }
}

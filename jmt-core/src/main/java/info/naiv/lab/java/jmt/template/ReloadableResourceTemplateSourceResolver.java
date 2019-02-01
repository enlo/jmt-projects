/*
 * The MIT License
 *
 * Copyright 2019 enlo.
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
package info.naiv.lab.java.jmt.template;

import info.naiv.lab.java.jmt.io.NIOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.core.io.Resource;

/**
 *
 * @author enlo
 */
@SuppressWarnings("serial")
public class ReloadableResourceTemplateSourceResolver
        implements TemplateSourceResolver {

    private static final AtomicLong RESET_TIMESTAMP = new AtomicLong(System.currentTimeMillis());

    public static void reload() {
        RESET_TIMESTAMP.set(System.currentTimeMillis());
    }

    private static boolean checkStaticResource(Resource resource) {
        try {
            resource.lastModified();
            return true;
        }
        catch (IOException ex) {
            return false;
        }
    }

    private final Charset charset;
    private final boolean isStatic;
    private final Resource resource;

    private CharSequence source;
    private long timestamp;

    public ReloadableResourceTemplateSourceResolver(Resource resource, Charset charset) {
        this.resource = resource;
        this.charset = charset;
        loadString();
        this.isStatic = checkStaticResource(resource);
    }

    @Override
    public boolean isUpdated() {
        return timestamp < getResourceModified();
    }

    @Override
    public CharSequence resolveSource() {
        if (isUpdated()) {
            loadString();
        }
        return source;
    }

    protected long getResourceModified() {
        long time = RESET_TIMESTAMP.get();
        if (isStatic) {
            return time;
        }
        else {
            try {
                return Math.min(time, resource.lastModified());
            }
            catch (IOException ex) {
                return time;
            }
        }
    }

    protected final void loadString() {
        try (InputStream is = resource.getInputStream()) {
            this.source = NIOUtils.toString(is, charset);
            this.timestamp = System.currentTimeMillis();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}

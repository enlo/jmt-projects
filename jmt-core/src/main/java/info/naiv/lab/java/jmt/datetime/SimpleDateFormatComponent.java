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
package info.naiv.lab.java.jmt.datetime;

import info.naiv.lab.java.jmt.infrastructure.ServiceProvider;
import info.naiv.lab.java.jmt.infrastructure.Tag;
import info.naiv.lab.java.jmt.infrastructure.component.AbstractTlsServiceComponent;
import info.naiv.lab.java.jmt.infrastructure.component.SystemComponent;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Map;
import javax.annotation.concurrent.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author enlo
 */
@Slf4j
@ThreadSafe
public class SimpleDateFormatComponent
        extends AbstractTlsServiceComponent<SimpleDateFormat>
        implements SystemComponent {

    @Override
    public Class<? extends SimpleDateFormat> getContentType() {
        return SimpleDateFormat.class;
    }

    /**
     *
     * @param map
     * @param tag
     * @param provider
     * @return
     */
    @Override
    protected boolean handleNotFound(Map<Tag, SimpleDateFormat> map, Tag tag, ServiceProvider provider) {
        Serializable id = tag.getId();
        try {
            map.put(tag, new SimpleDateFormat(id.toString()));
            return true;
        }
        catch (Exception e) {
            logger.warn("invalid pattern {} {}", id, e);
            return false;
        }
    }

}

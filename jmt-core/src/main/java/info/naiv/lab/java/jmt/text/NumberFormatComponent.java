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
package info.naiv.lab.java.jmt.text;

import info.naiv.lab.java.jmt.infrastructure.ServiceProvider;
import info.naiv.lab.java.jmt.infrastructure.Tag;
import info.naiv.lab.java.jmt.infrastructure.component.AbstractTlsServiceComponent;
import info.naiv.lab.java.jmt.infrastructure.component.SystemComponent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

/**
 *
 * @author enlo
 */
public class NumberFormatComponent
        extends AbstractTlsServiceComponent<NumberFormat>
        implements SystemComponent {

    @Override
    public Class<? extends NumberFormat> getContentType() {
        return NumberFormat.class;
    }

    /**
     *
     * @param map
     * @param tag
     * @param provider
     * @return
     */
    @Override
    protected boolean handleNotFound(Map<Tag, NumberFormat> map, Tag tag, ServiceProvider provider) {
        Object id = tag.getId();
        if (id == Long.class) {
            map.put(tag, NumberFormat.getIntegerInstance());
        }
        else if (id == BigDecimal.class) {
            NumberFormat f = NumberFormat.getInstance();
            if (f instanceof DecimalFormat) {
                ((DecimalFormat) f).setParseBigDecimal(true);
            }
            map.put(tag, f);
        }
        else if (id instanceof NumberFormatConfig) {
            NumberFormatConfig conf = (NumberFormatConfig) id;
            NumberFormat f = conf.createNumberFormat();
            map.put(tag, f);
        }
        else if (id instanceof String) {
            map.put(tag, new DecimalFormat((String) id));
        }
        else {
            map.put(tag, NumberFormat.getInstance());
        }
        return true;
    }

}

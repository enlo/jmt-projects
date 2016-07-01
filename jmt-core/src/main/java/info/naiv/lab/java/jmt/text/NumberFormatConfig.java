/*
 * The MIT License
 *
 * Copyright 2016 enlo.
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

import static info.naiv.lab.java.jmt.Misc.isEmpty;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import lombok.NonNull;
import lombok.Value;

/**
 *
 * @author enlo
 */
@Value
public class NumberFormatConfig implements Serializable {

    private static final long serialVersionUID = 694171468800516115L;

    @NonNull
    String format;

    boolean parseBigDecimal;

    public NumberFormatConfig() {
        this.format = null;
        this.parseBigDecimal = true;
    }

    public NumberFormatConfig(String format) {
        this.format = format;
        this.parseBigDecimal = true;
    }

    public NumberFormatConfig(String format, boolean parseBigDecimal) {
        this.format = format;
        this.parseBigDecimal = parseBigDecimal;
    }

    public NumberFormat createNumberFormat() {
        DecimalFormat df;
        if (isEmpty(format)) {
            df = new DecimalFormat();
        }
        else {
            df = new DecimalFormat(format);
        }
        if (parseBigDecimal) {
            df.setParseBigDecimal(parseBigDecimal);
        }
        return df;
    }
}

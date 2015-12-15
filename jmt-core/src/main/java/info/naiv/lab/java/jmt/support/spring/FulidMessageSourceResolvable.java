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
package info.naiv.lab.java.jmt.support.spring;

import java.util.List;
import java.util.Locale;
import lombok.Getter;
import lombok.Singular;
import lombok.Builder;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;

/**
 *
 * @author enlo
 */
@Builder
@Getter
public final class FulidMessageSourceResolvable implements MessageSourceResolvable {

    @Singular
    private final List<String> codes;

    private Object[] arguments = null;

    private Locale locale = Locale.getDefault();

    private String defaultMessage = "";

    public String getMessage(MessageSource messageSource) {
        return messageSource.getMessage(this, locale);
    }

    @Override
    public String[] getCodes() {
        return codes.toArray(new String[codes.size()]);
    }

}

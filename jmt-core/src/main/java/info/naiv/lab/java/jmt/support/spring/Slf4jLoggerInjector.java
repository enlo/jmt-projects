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

import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.lang.reflect.Field;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils.FieldCallback;
import static org.springframework.util.ReflectionUtils.doWithFields;
import static org.springframework.util.ReflectionUtils.makeAccessible;

/**
 *
 * @author enlo
 */
public class Slf4jLoggerInjector implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object o, String string) throws BeansException {
        return o;
    }

    @Override
    @ReturnNonNull
    public Object postProcessBeforeInitialization(final Object o, String name) throws BeansException {
        doWithFields(o.getClass(), new Callback(o));
        return o;
    }

    private static class Callback implements FieldCallback {

        private final Object o;

        Callback(Object o) {
            this.o = o;
        }

        @Override
        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
            makeAccessible(field);
            InjectLogger anno = field.getAnnotation(InjectLogger.class);
            if (anno != null) {
                if (field.getType().equals(Logger.class)) {
                    Logger logger = getLogger(o.getClass());
                    field.set(o, logger);
                }
            }
        }

    }
}

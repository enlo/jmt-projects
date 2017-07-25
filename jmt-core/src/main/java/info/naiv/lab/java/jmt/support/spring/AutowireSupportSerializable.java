/*
 * The MIT License
 *
 * Copyright 2017 enlo.
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

import info.naiv.lab.java.jmt.fx.Consumer1;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author enlo
 */
@SuppressWarnings("serial")
public abstract class AutowireSupportSerializable implements Serializable {

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        GlobalApplicationContextHolder.runWithApplicationContext(new Consumer1<ApplicationContext>() {
            @Override
            public void accept(ApplicationContext a1) {
                a1.getAutowireCapableBeanFactory().autowireBean(this);
            }
        });
    }

}

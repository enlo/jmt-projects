/*
 * The MIT License
 *
 * Copyright 2015 N2358.
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
package info.naiv.lab.java.jmt.support.jdbc.sql.velocity;

import java.util.concurrent.atomic.AtomicReference;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeInstance;
import org.springframework.ui.velocity.VelocityEngineFactory;

/**
 *
 * @author enlo
 */
public class VelocitySqlTemplateEngineFactory {

    static final AtomicReference<VelocityEngine> velocityEngineInstance = new AtomicReference<>();
    static final AtomicReference<VelocityEngineFactory> velocityEngineFactoryInstance = new AtomicReference<>();

    public static void init() {
        velocityEngineInstance.compareAndSet(null, VelocitySqlUtils.createVelocityEngine());
    }

    public RuntimeInstance createInstance() {
        RuntimeInstance ri = new RuntimeInstance();
        VelocityEngine ve = getVelocityEngine();
        if(ve != null) {
        }
        return ri;
    }
    
    public VelocityEngine getVelocityEngine() {        
        VelocityEngine ve = velocityEngineInstance.get();
        if(ve == null) {
            init();
            ve = velocityEngineInstance.get();
        }
        return ve;
    }

    public VelocityEngine setVelocityEngine(VelocityEngine velocityEngine) {
        VelocityEngine veOld = velocityEngineInstance.getAndSet(velocityEngine);
        return veOld;
    }

}

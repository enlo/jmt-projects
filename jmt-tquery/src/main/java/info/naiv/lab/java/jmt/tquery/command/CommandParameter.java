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
package info.naiv.lab.java.jmt.tquery.command;

import info.naiv.lab.java.jmt.collection.KeyedValue;
import java.io.Serializable;
import lombok.Value;

/**
 *
 * @author enlo
 */
@Value
public class CommandParameter implements Serializable, Cloneable, KeyedValue<String> {

    private static final long serialVersionUID = -6521832621632523550L;
    
    private Object typeHint;
    private String key;
    private Object value;

    public CommandParameter(int index, Object value) {
        this(index, value, null);
    }
    
    public CommandParameter(int index, Object value, Object typeHint) {
        this.key = "p" + index;
        this.value = value;
        this.typeHint = typeHint;
    }

    public CommandParameter(String key,  Object value) {
        this(key, value, null);
    }
    
    public CommandParameter(String key, Object value, Object typeHint) {
        this.key = key;
        this.value = value;
        this.typeHint = typeHint;
    }

    @Override
    @SuppressWarnings("CloneDeclaresCloneNotSupported")
    public CommandParameter clone() {
        try {
            return (CommandParameter) super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError(ex.getMessage());
        }
    }

}

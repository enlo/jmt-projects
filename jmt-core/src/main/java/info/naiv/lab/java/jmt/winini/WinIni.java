/*
 * The MIT License
 *
 * Copyright 2018 enlo.
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
package info.naiv.lab.java.jmt.winini;

import info.naiv.lab.java.jmt.iteration.AbstractIteratorAdapter;
import info.naiv.lab.java.jmt.iteration.ChainIterator;
import info.naiv.lab.java.jmt.iteration.SingleIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author enlo
 */
public class WinIni implements Ini {

    private final List<IniNode> iniNodes;
    private final IniSection unnamedSection;

    public WinIni() {
        unnamedSection = new WinIniSection("");
        iniNodes = new ArrayList<>();
    }

    @Override
    public void add(IniNode node) {
        if (node instanceof IniComment) {
            getLastSection().add((IniComment) node);
        }
        else if (node instanceof IniEntry) {
            getLastSection().add((IniEntry) node);
        }
        else if (node instanceof IniSection) {
            iniNodes.add(node);
        }
    }

    @Override
    public boolean containsKey(String section, String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean containsKey(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String get(String section, String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IniSection get(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterable<String> getKeys() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterable<IniSection> getSections() {
        return new SectionIterable();
    }

    @Override
    public Iterator<IniNode> iterator() {
        return iniNodes.iterator();
    }

    @Override
    public void put(String section, String key, String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void put(String section, IniEntry entry) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Properties toProperties() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected IniSection getLastSection() {
        IniSection last = unnamedSection;
        for (int i = iniNodes.size() - 1; 0 <= i; i--) {
            IniNode n = iniNodes.get(i);
            if (n instanceof IniSection) {
                last = (IniSection) n;
                break;
            }
        }
        return last;
    }

    class SectionIterable implements Iterable<IniSection> {

        @Override
        public Iterator<IniSection> iterator() {

            Iterator<IniSection> sections = new AbstractIteratorAdapter<IniNode, IniSection>() {

                @Override
                protected boolean filter(IniNode value) {
                    return (value instanceof IniSection);
                }

                @Override
                protected Iterator<IniNode> getBaseIterator() {
                    return iniNodes.iterator();
                }

            };
            if (unnamedSection.isEmpty()) {
                return sections;
            }
            else {
                return new ChainIterator<>(new SingleIterator<>(unnamedSection), sections);
            }
        }

    }
}

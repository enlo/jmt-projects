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
package info.naiv.lab.java.jmt.template.mvel.node;

import info.naiv.lab.java.jmt.template.mvel.MvelCustomNodesProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.Getter;
import org.mvel2.templates.res.Node;

/**
 *
 * @author enlo
 */
public class DefaultCustomNodesProvider implements MvelCustomNodesProvider {

    /**
     *
     */
    public static final Map<String, Class<? extends Node>> NODES;

    @Getter
    private static final DefaultCustomNodesProvider globalInstance;

    static {

        Map<String, Class<? extends Node>> nodes = new HashMap<>();
        nodes.put("format", FormatNode.class);
        nodes.put("numberFormat", NumberFormatNode.class);

        NODES = Collections.unmodifiableMap(nodes);
        globalInstance = new DefaultCustomNodesProvider();
    }
    private final ConcurrentMap<String, Class<? extends Node>> nodes = new ConcurrentHashMap<>();

    public DefaultCustomNodesProvider() {
        nodes.putAll(NODES);
    }

    public DefaultCustomNodesProvider(Map<String, Class<? extends Node>> nodes) {
        this();
        this.nodes.putAll(nodes);
    }

    @Override
    public Map<String, Class<? extends Node>> getCustomNodes() {
        return Collections.unmodifiableMap(nodes);
    }

    public Class<? extends Node> registerNode(String name, Class<? extends Node> node) {
        return nodes.put(name, node);
    }

    public boolean unregisterNode(String name, Class<? extends Node> node) {
        return nodes.remove(name, node);
    }

    protected final void addNodes(Map<String, Class<? extends Node>> nodes) {
        this.nodes.putAll(nodes);
    }
}

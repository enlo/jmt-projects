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
package info.naiv.lab.java.jmt.jdbc.metadata;

import static info.naiv.lab.java.jmt.Misc.isNotBlank;
import info.naiv.lab.java.jmt.jdbc.JDBCTypeTraits;
import java.io.Serializable;
import lombok.Data;
import static org.springframework.jdbc.support.JdbcUtils.convertUnderscoreNameToPropertyName;

/**
 *
 * @author enlo
 */
@Data
public class Column implements Serializable {

    private static final long serialVersionUID = 3009006641174950928L;
    private final String name;
    private final String originalName;
    private final String typeName;
    private final JDBCTypeTraits typeTraits;
    private String originalTypeName;
    private boolean nonNull;
    private boolean primaryKey;
    private int primaryKeyIndex = -1;
    private int size;
    private int scale;

    public Column(String originalColumnName, Class type, String originalTypeName) {
        this.originalName = originalColumnName;
        this.name = convertUnderscoreNameToPropertyName(getOriginalName());
        this.typeName = type.getCanonicalName();
        this.typeTraits = JDBCTypeTraits.valueOf(type);
        this.originalTypeName = isNotBlank(originalTypeName) ? originalTypeName : type.getSimpleName();
    }

}

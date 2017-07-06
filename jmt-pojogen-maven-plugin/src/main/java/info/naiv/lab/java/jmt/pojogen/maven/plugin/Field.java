package info.naiv.lab.java.jmt.pojogen.maven.plugin;

import info.naiv.lab.java.jmt.jdbc.JDBCTypeTraits;
import lombok.Data;
import static org.springframework.jdbc.support.JdbcUtils.convertUnderscoreNameToPropertyName;

/**
 *
 * @author enlo
 */
@Data
public class Field {

    String name;
    String originalName;
    String typeName;
    String originalTypeName;
    boolean nonNull;
    boolean primaryKey;
    int primaryKeyIndex;
    JDBCTypeTraits typeTraits;

    public void makeNameFromOriginalName() {
        setName(convertUnderscoreNameToPropertyName(getOriginalName()));
    }

    public void setType(Class clazz) {
        setTypeName(clazz.getCanonicalName());
    }
}

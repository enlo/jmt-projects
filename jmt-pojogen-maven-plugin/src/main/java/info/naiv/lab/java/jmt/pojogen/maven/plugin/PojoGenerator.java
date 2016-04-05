
package info.naiv.lab.java.jmt.pojogen.maven.plugin;

import info.naiv.lab.java.jmt.ComparableComparator;
import info.naiv.lab.java.jmt.jdbc.JdbcType;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateRuntime;
import org.springframework.jdbc.support.JdbcUtils;

/**
 *
 * @author enlo
 */
public class PojoGenerator {
    
    final String schema;
    final String packageName;
    final String classNameSuffix;
    final CompiledTemplate classTemplate;

    public PojoGenerator(String schema, String packageName, String classNameSuffix, CompiledTemplate classTemplate) throws IOException {
        this.classTemplate = classTemplate;
        this.schema = schema;
        this.packageName = packageName;
        this.classNameSuffix = classNameSuffix;
    }

    public CodeData tableToBean(DatabaseMetaData meta, String tablename) throws SQLException {

        String beanName = toBeanName(tablename);

        HashMap<String, Object> params = new HashMap<>();
        List<Field> fields = makeFields(meta, tablename);

        boolean hasPkey = false;
        List<Field> keys = new ArrayList<>();
        try (ResultSet rs = meta.getPrimaryKeys(null, schema, tablename)) {
            Map<Short, Field> keyMap = new HashMap<>();
            while (rs.next()) {
                hasPkey = true;
                String cname = rs.getString("COLUMN_NAME");
                short seq = rs.getShort("KEY_SEQ");
                for (Field field : fields) {
                    if (field.getOriginalName().equals(cname)) {
                        field.setPrimaryKey(true);
                        field.setPrimaryKeyIndex(seq);
                        keyMap.put(seq, field);
                    }
                }
                List<Short> order = new ArrayList<>(keyMap.keySet());
                order.sort(new ComparableComparator<Short>());
                for (Short idx : order) {
                    keys.add(keyMap.get(idx));
                }
            }
        }

        params.put("packageName", packageName);
        params.put("originalTableName", tablename);
        params.put("beanName", beanName);
        params.put("fields", fields);
        params.put("keyFields", keys);
        params.put("hasPKey", hasPkey);

        String code = (String) TemplateRuntime.execute(classTemplate, params);
        return new CodeData(beanName, code);
    }

    protected List<Field> makeFields(DatabaseMetaData meta, String tablename) throws SQLException {
        List<Field> fields = new ArrayList<>();
        try (ResultSet rs = meta.getColumns(null, schema, tablename, "%")) {
            while (rs.next()) {
                Field field = new Field();
                field.setOriginalName(rs.getString("COLUMN_NAME"));
                field.setOriginalTypeName(rs.getString("TYPE_NAME"));
                field.makeNameFromOriginalName();
                Class clz = getType(rs);
                int nullable = rs.getInt("NULLABLE");
                field.setNonNull(nullable == DatabaseMetaData.columnNoNulls);
                field.setType(clz);
                fields.add(field);
            }
        }
        return fields;
    }

    protected Class getType(final ResultSet rs) throws SQLException {
        int dataType = rs.getInt("DATA_TYPE");
        JdbcType type = JdbcType.valueOf(dataType);
        return type.getMappedType();
    }

    protected String toBeanName(String tablename) {
        String beanName = JdbcUtils.convertUnderscoreNameToPropertyName(tablename);
        beanName = StringUtils.capitalize(beanName);
        return beanName + StringUtils.stripToEmpty(classNameSuffix);
    }
}

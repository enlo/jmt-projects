
package info.naiv.lab.java.jmt.support.jdbc.sql.parameter;

import info.naiv.lab.java.jmt.support.jdbc.JdbcType;
import static info.naiv.lab.java.jmt.support.jdbc.JdbcType.REAL;

/**
 *
 * @author enlo
 */
public class FloatSqlParameter extends AbstractSqlParameter<Float> {

    private static final long serialVersionUID = 1L;

    public FloatSqlParameter(Float value) {
        super(value);
    }
        
    public FloatSqlParameter(String name, Float value) {
        super(name, value);
    }

    @Override
    public JdbcType getJdbcType() {
        return REAL;
    }
}

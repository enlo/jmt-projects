
package info.naiv.lab.java.jmt.support.jdbc.sql.parameter;

import info.naiv.lab.java.jmt.support.jdbc.JdbcType;
import static info.naiv.lab.java.jmt.support.jdbc.JdbcType.BIGINT;

/**
 *
 * @author enlo
 */
public class LongSqlParameter extends AbstractSqlParameter<Long> {

    private static final long serialVersionUID = 1L;

    public LongSqlParameter(Long value) {
        super(value);
    }
        
    public LongSqlParameter(String name, Long value) {
        super(name, value);
    }

    @Override
    public JdbcType getJdbcType() {
        return BIGINT;
    }

}



package info.naiv.lab.java.jmt.support.jdbc.sql.parameter;

import info.naiv.lab.java.jmt.support.jdbc.JdbcType;
import static info.naiv.lab.java.jmt.support.jdbc.JdbcType.BOOLEAN;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author enlo
 */
public class BooleanSqlParameter extends AbstractSqlParameter<Boolean> {
    
    private static final long serialVersionUID = 1L;
    
    public BooleanSqlParameter(Boolean value) {
        super(value);
    }
    
    public BooleanSqlParameter(String name, Boolean value) {
        super(name, value);
    }
    
    @Override
    public JdbcType getJdbcType() {
        return BOOLEAN;
    }
    
    @Override
    protected void internalSetTo(PreparedStatement ps, int i) throws SQLException {
        ps.setBoolean(i, value);
    }
    
}

package info.naiv.lab.java.jmt.support.jdbc.sql.velocity;

import info.naiv.lab.java.jmt.*;
import static info.naiv.lab.java.jmt.Arguments.nonNull;
import static info.naiv.lab.java.jmt.Misc.repeat;
import info.naiv.lab.java.jmt.support.jdbc.sql.SqlParameter;
import info.naiv.lab.java.jmt.support.jdbc.sql.SqlParameters;
import static info.naiv.lab.java.jmt.support.jdbc.sql.velocity.VelocitySqlUtils.*;
import java.io.IOException;
import java.io.Writer;
import static java.lang.reflect.Array.get;
import static java.lang.reflect.Array.getLength;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

/**
 *
 * @author enlo
 */
public class BindManyDirective extends Directive {

    private static final StringJoiner joiner = StringJoiner.valueOf(",");

    @Override
    public String getName() {
        return "jmtBindMany";
    }

    @Override
    public int getType() {
        return LINE;
    }

    @Override
    public boolean render(InternalContextAdapter ica, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        SqlParameters result = getResultParameters(ica);
        Node p1 = node.jjtGetChild(0);
        String name = getVarName(p1);

        Object list = nonNull(p1.value(ica), name);
        int count = 0;
        if (list instanceof Iterable) {
            for (Object x : (Iterable) list) {
                String pn = name + count;
                SqlParameter p = getOrCreateParameter(ica, pn, x);
                result.add(p);
                count++;
            }
        }
        else if (list.getClass().isArray()) {
            count = getLength(list);
            for (int i = 0; i < count; i++) {
                Object x = get(list, i);
                String pn = name + i;
                SqlParameter p = getOrCreateParameter(ica, pn, x);
                result.add(p);
            }
        }
        else {
            SqlParameter p = getOrCreateParameter(ica, name, list);
            result.add(p);
            writer.append("?");
        }
        writer.append(joiner.join(repeat(count, '?')));
        return true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

}

package info.naiv.lab.java.jmt.tquery.command;

import static info.naiv.lab.java.jmt.ClassicArrayUtils.asObjectArray;
import info.naiv.lab.java.jmt.Misc;
import info.naiv.lab.java.jmt.StringJoiner;
import info.naiv.lab.java.jmt.tquery.QueryContext;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author enlo
 */
public class DefaultParameterBinder implements ParameterBinder {

    private static final StringJoiner joiner = StringJoiner.valueOf(", ");
    private static final long serialVersionUID = 1L;

    public int addParameters(Iterable<?> items, CommandParameters params) {
        int count = 0;
        for (Object item : items) {
            params.addValue(item);
            count++;
        }
        return count;
    }

    public int addParameters(Collection<?> items, CommandParameters params) {
        int count = 0;
        params.addCapacity(items.size());
        for (Object item : items) {
            params.addValue(item);
            count++;
        }
        return count;
    }

    @Override
    public String bind(Object value, QueryContext context) {
        context.getParameters().addValue(value);
        return "?";
    }

    @Override
    public String bind(Object value, QueryContext context, Object typeHint) {
        context.getParameters().addValue(value, typeHint);
        return "?";
    }

    @Override
    public String bindMany(Object value, QueryContext context) {
        CommandParameters params = context.getParameters();
        return bindManyCore(value, params);
    }


    @Override
    public String bindMany(Object value, QueryContext context, Object typeHint) {
        CommandParameters params = context.getParameters();
        return bindManyCore(value, CommandParametersWithTypeHint.wrap(params, typeHint));
    }
    protected String bindManyCore(Object value, CommandParameters params) {
        int count;
        if (value instanceof Collection) {
            Collection list = (Collection) value;
            params.addCapacity(list.size());
            count = addParameters(list, params);
        }
        else if (value instanceof Iterable) {
            Iterable<?> iter = (Iterable<?>) value;
            count = addParameters(iter, params);
        }
        else {
            Object[] arr = asObjectArray(value);
            if (arr != null) {
                count = addParameters(Arrays.asList(arr), params);
            }
            else {
                params.addValue(value);
                count = 1;
            }
        }
        return joiner.join(Misc.repeat(count, "?")).toString();
    }
}

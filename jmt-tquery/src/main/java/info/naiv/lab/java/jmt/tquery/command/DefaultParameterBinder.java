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

    public int addParameters(Iterable<?> items, CommandParametersBuilder builder) {
        int count = 0;
        for (Object item : items) {
            builder.addValue(item);
            count++;
        }
        return count;
    }

    public int addParameters(Collection<?> items, CommandParametersBuilder builder) {
        int count = 0;
        builder.addCapacity(items.size());
        for (Object item : items) {
            builder.addValue(item);
            count++;
        }
        return count;
    }

    @Override
    public String bind(Object value, QueryContext context) {
        context.getParametersBuilder().addValue(value);
        return "?";
    }

    @Override
    public String bindMany(Object value, QueryContext context) {
        int count;
        CommandParametersBuilder builder = context.getParametersBuilder();
        if (value instanceof Collection) {
            Collection list = (Collection) value;
            builder.addCapacity(list.size());
            count = addParameters(list, builder);
        }
        else if (value instanceof Iterable) {
            Iterable<?> iter = (Iterable<?>) value;
            count = addParameters(iter, builder);
        }
        else {
            Object[] arr = asObjectArray(value);
            if (arr != null) {
                count = addParameters(Arrays.asList(arr), builder);
            }
            else {
                builder.addValue(value);
                count = 1;
            }
        }
        return joiner.join(Misc.repeat(count, "?")).toString();
    }
}

/*
 * The MIT License
 *
 * Copyright 2017 enlo.
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
package info.naiv.lab.java.jmt.support.spring.env;

import info.naiv.lab.java.jmt.Arguments;
import static info.naiv.lab.java.jmt.Misc.isEmpty;
import info.naiv.lab.java.jmt.StringPropertyResolver;
import info.naiv.lab.java.jmt.StringPropertyResolvers;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.Properties;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

/**
 *
 * @author enlo
 */
@Setter
public class AutoEnvironmentMergePropertyFactoryBean
        extends AbstractFactoryBean<StringPropertyResolver>
        implements FactoryBean<StringPropertyResolver>,
                   EnvironmentAware, InitializingBean {

    private Environment environment;

    private List<Properties> properties;

    private String propertySourceBefore;
    private String propertySourceName;

    @Override
    public Class<?> getObjectType() {
        return StringPropertyResolver.class;
    }

    public void setProperties(Properties... properties) {
        this.properties = asList(properties);
    }

    @Override
    protected StringPropertyResolver createInstance() throws Exception {

        if (isEmpty(properties)) {
            return StringPropertyResolvers.EMPTY;
        }
        else {
            StringPropertyResolver resolver;
            if (properties.size() == 1) {
                resolver = StringPropertyResolvers.wrap(properties.get(0));
            }
            else {
                List<StringPropertyResolver> resolvers = new ArrayList<>(properties.size());
                for (Properties p : properties) {
                    resolvers.add(StringPropertyResolvers.wrap(p));
                }
                resolver = StringPropertyResolvers.compose(resolvers);
            }
            registerToEnvironment(resolver);
            return resolver;
        }
    }

    protected void registerToEnvironment(StringPropertyResolver resolver) {
        if (environment instanceof ConfigurableEnvironment) {
            Arguments.nonNull(propertySourceName, "propertySourceName");

            ConfigurableEnvironment ce = (ConfigurableEnvironment) environment;
            MutablePropertySources pss = ce.getPropertySources();
            synchronized (pss) {
                StringPropertyResolverPropertySource ps
                        = new StringPropertyResolverPropertySource(propertySourceName, resolver);
                if (pss.contains(propertySourceName)) {
                    pss.replace(propertySourceName, ps);
                }
                else {
                    if (propertySourceBefore == null) {
                        pss.addLast(ps);
                    }
                    else if ("first".equalsIgnoreCase(propertySourceBefore)) {
                        pss.addFirst(ps);
                    }
                    pss.addBefore(propertySourceName, ps);
                }
            }
        }
    }

}

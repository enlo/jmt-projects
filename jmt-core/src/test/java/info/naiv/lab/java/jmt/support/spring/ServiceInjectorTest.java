
package info.naiv.lab.java.jmt.support.spring;

import info.naiv.lab.java.jmt.datetime.CurrentDateProvider;
import info.naiv.lab.java.jmt.datetime.ClassicDateUtils;
import info.naiv.lab.java.jmt.datetime.DefaultCurrentDateProvider;
import info.naiv.lab.java.jmt.infrastructure.ServiceProvider;
import info.naiv.lab.java.jmt.infrastructure.ServiceProviders;
import info.naiv.lab.java.jmt.infrastructure.Tag;
import info.naiv.lab.java.jmt.infrastructure.ThreadSafeServiceContainer;
import info.naiv.lab.java.jmt.infrastructure.annotation.StringTagOf;
import info.naiv.lab.java.jmt.support.spring.ServiceInjectorTest.Injectee;
import java.util.Calendar;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.BeanFactory;

/**
 *
 * @author enlo
 */
public class ServiceInjectorTest {


    public ServiceInjectorTest() {
    }

    @Before
    public void setup() {
        ServiceProviders.setThreadContainer(new ThreadSafeServiceContainer());
    }

    /**
     * Test of postProcessAfterInitialization method, of class ServiceInjector.
     */
    @Test
    public void testPostProcessAfterInitialization() {
        Object o = new String();
        ServiceInjector instance = new ServiceInjector();
        Object result = instance.postProcessAfterInitialization(o, "");
        assertThat(result, is(sameInstance(o)));
    }

    /**
     * Test of postProcessBeforeInitialization method, of class ServiceInjector.
     */
    @Test
    public void testPostProcessBeforeInitialization() {
        Injectee o = new Injectee();
        assertThat(o.provider, is(nullValue()));
        ServiceInjector instance = new ServiceInjector();
        Object result = instance.postProcessBeforeInitialization(o, "");
        assertThat(result, is(sameInstance((Object) o)));
        assertThat(o.provider, is(not(nullValue())));
        assertThat(o.provider, is(instanceOf(DefaultCurrentDateProvider.class)));
    }

    /**
     * Test of setBeanFactory method, of class ServiceInjector.
     */
    @Test
    public void testSetBeanFactory() {

        ServiceInjector instance = new ServiceInjector();
        Injectee2 o = new Injectee2();

        Object result = instance.postProcessBeforeInitialization(o, "");
        assertThat(result, is(sameInstance((Object) o)));
        assertThat(o.provider1, is(nullValue()));
        assertThat(o.provider2, is(nullValue()));

        Calendar cal1 = ClassicDateUtils.createCalendar(2015, 1, 1);
        Calendar cal2 = ClassicDateUtils.createCalendar(2015, 4, 1);

        Tag tag1 = Tag.of("1");
        Tag tag2 = Tag.of("2");

        CurrentDateProvider mdp1 = mock(CurrentDateProvider.class);
        when(mdp1.getToday()).thenReturn(cal1);

        CurrentDateProvider mdp2 = mock(CurrentDateProvider.class);
        when(mdp2.getToday()).thenReturn(cal2);

        ServiceProvider msp1 = mock(ServiceProvider.class);
        when(msp1.resolveService(CurrentDateProvider.class, tag1)).thenReturn(mdp1);
        when(msp1.resolveService(CurrentDateProvider.class, tag2)).thenReturn(mdp2);

        BeanFactory mbf = mock(BeanFactory.class);
        when(mbf.getBean(ServiceProvider.class)).thenReturn(msp1);

        instance.setBeanFactory(mbf);

        result = instance.postProcessBeforeInitialization(o, "");
        assertThat(result, is(sameInstance((Object) o)));
        assertThat(o.provider1.getToday(), is(cal1));
        assertThat(o.provider2.getToday(), is(cal2));
    }

    static class Injectee {

        @InjectService
                CurrentDateProvider provider;
    }

    static class Injectee2 {
        
        @InjectService
        @StringTagOf("1")
                CurrentDateProvider provider1;
        @InjectService
        @StringTagOf("2")
                CurrentDateProvider provider2;
    }

}

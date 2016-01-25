/*
 * Copyright (c) 2015, enlo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package info.naiv.lab.java.jmt.infrastructure;

import java.util.UUID;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author enlo
 */
public class ServiceConnectionComparatorTest {

    public ServiceConnectionComparatorTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of compare method, of class ServiceConnectionComparator.
     */
    @Test
    public void testCompare() {

        ServiceContainer container1 = mock(ServiceContainer.class);
        ServiceContainer container2 = mock(ServiceContainer.class);
        when(container1.id()).thenReturn(new UUID(0, 1));
        when(container2.id()).thenReturn(new UUID(0, 2));

        ServiceConnection[] conn = {
            createConnection(container1, 3),
            createConnection(container1, 2),
            createConnection(container1, 1),
            createConnection(container2, 3),
            createConnection(container2, 2),
            createConnection(container2, 1),};

        ServiceConnectionComparator instance = new ServiceConnectionComparator();
        for (int i = 0; i < conn.length; i++) {
            ServiceConnection x = conn[i];
            for (int j = 0; j < conn.length; j++) {
                ServiceConnection y = conn[j];
                int actual = instance.compare(x, y);
                if (i == j) {
                    assertThat(actual, is(0));
                }
                else if (i < j) {
                    assertThat(actual, is(lessThan(0)));
                }
                else {
                    assertThat(actual, is(greaterThan(0)));
                }
            }
        }
    }

    private ServiceConnection createConnection(ServiceContainer cx, int pr) {
        ServiceConnection conn = mock(ServiceConnection.class);
        when(conn.getContainer()).thenReturn(cx);
        when(conn.getPriority()).thenReturn(pr);
        return conn;
    }

}

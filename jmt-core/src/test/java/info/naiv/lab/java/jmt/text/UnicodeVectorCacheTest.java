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
package info.naiv.lab.java.jmt.text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.text.RandomStringGenerator;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author enlo
 */
@Slf4j
public class UnicodeVectorCacheTest {

    public UnicodeVectorCacheTest() {
    }

    /**
     * Test of getDecomposed method, of class UnicodeVectorCache.
     */
    @Test
    public void testGetDecomposed_String() {
        UnicodeVectorCache.DECOMP.setMaxSizeFactor(UnicodeVectorCache.INITIAL_CACHE_SIZE);
        RandomStringGenerator generator = new RandomStringGenerator.Builder().build();
        List<String> samplesS = new ArrayList<>();
        List<UnicodeVector> samplesD = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            String src = generator.generate(ThreadLocalRandom.current().nextInt(10, 100000));
            UnicodeVector dst = UnicodeVectorCache.getDecomposed(src);
            if (i % 10 == 0) {
                samplesS.add(src);
                samplesD.add(dst);
            }
        }
        logger.info("stats is {}", UnicodeVectorCache.DECOMP);
        StopWatch sw = new StopWatch();
        sw.start();
        for (int i = 0; i < samplesS.size(); i++) {
            String src = samplesS.get(i);
            UnicodeVector dst = UnicodeVectorCache.getDecomposed(src);
            assertThat("decomp test(" + i + ")", dst, is(samplesD.get(i)));
        }
        sw.stop();
        logger.info("stats is {} / {}ms", UnicodeVectorCache.DECOMP, sw.toString());
    }

    /**
     * Test of getDecomposed method, of class UnicodeVectorCache.
     */
    @Test
    public void testGetDecomposed_UnicodeVector() {
    }

}

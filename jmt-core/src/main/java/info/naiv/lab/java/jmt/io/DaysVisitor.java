/*
 * The MIT License
 *
 * Copyright 2015 enlo.
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
package info.naiv.lab.java.jmt.io;

import info.naiv.lab.java.jmt.datetime.ClassicDateUtils;
import static info.naiv.lab.java.jmt.datetime.ClassicDateUtils.getCurrentDateProvider;
import info.naiv.lab.java.jmt.fx.Predicate1;
import java.io.IOException;
import static java.nio.file.Files.getLastModifiedTime;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;
import java.util.Comparator;
import static java.util.concurrent.TimeUnit.DAYS;

/**
 *
 * @author enlo
 */
public class DaysVisitor extends AbstractFileVisitor {

    final long border;
    final Predicate1<FileTime> comp;

    /**
     *
     * @param comp
     * @param days
     */
    public DaysVisitor(Comparator<Long> comp, int days) {
        Calendar today = getCurrentDateProvider().getToday();
        if (days < 0) {
            this.comp = new Before();
            this.border = ClassicDateUtils.add(today, days, DAYS).getTimeInMillis();
        }
        else {
            this.comp = new After();
            this.border = ClassicDateUtils.add(today, -days, DAYS).getTimeInMillis();
        }
    }

    /**
     *
     * @param entry
     * @param attrs
     * @return
     * @throws IOException
     */
    @Override
    protected boolean accept(Path entry, BasicFileAttributes attrs) throws IOException {
        return comp.test(getLastModifiedTime(entry));
    }

    private class After implements Predicate1<FileTime> {

        @Override
        public boolean test(FileTime obj) {
            return border <= obj.toMillis();
        }
    }

    private class Before implements Predicate1<FileTime> {

        @Override
        public boolean test(FileTime obj) {
            return obj.toMillis() <= border;
        }
    }

}

package info.naiv.lab.java.jmt.iteration;

import java.io.Serializable;

/**
 *
 * @author enlo
 */
public abstract class LoopCondition implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * break.
     */
    public final void doBreak() {
        throw new BreakException();
    }

    /**
     * continue.
     */
    public final void doContinue() {
        throw new ContinueException();
    }

    /**
     *
     * @return ループのindex. 0からスタート
     */
    public abstract int index();
}

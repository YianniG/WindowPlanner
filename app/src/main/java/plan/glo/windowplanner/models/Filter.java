package plan.glo.windowplanner.models;

import java.util.List;

/**
 * Created by yianni on 10/08/17.
 */

public abstract class Filter<A>{
    public abstract boolean apply(A a1, A a2);
}

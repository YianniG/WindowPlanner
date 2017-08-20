package plan.glo.windowplanner.models;

/**
 * Created by zubairchowdhury on 02/08/2017.
 */

public class Job implements JobI{
    private EventI event = null;

    @Override
    public boolean hasEvent() {
        return event != null;
    }

    @Override
    public void assignEvent(EventI event) {
        this.event = event;
    }

    @Override
    public EventI getEvent() {
        return event;
    }

}

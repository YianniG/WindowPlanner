package plan.glo.windowplanner.models;


public interface JobI {
    boolean hasEvent();
    void assignEvent(EventI event);
}

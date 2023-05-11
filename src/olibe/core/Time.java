package olibe.core;

/** Time Data of the Application */
public class Time {
    /** Frame time in seconds */
    public float delta;
    /** Time in seconds since the start */
    public float time;

    /** Create a Time */
    public Time() {
        this.delta = 0f;
        this.time  = 0f;
    }
}

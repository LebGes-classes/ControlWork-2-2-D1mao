package Broadcast;
class Program {
    private final String channel;
    private final BroadcastsTime time;
    private final String title;

    public Program(String channel, BroadcastsTime time, String title) {
        this.channel = channel;
        this.time = time;
        this.title = title;
    }

    public String getChannel() {
        return channel;
    }

    public BroadcastsTime getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return channel + " " + time + " " + title;
    }
}
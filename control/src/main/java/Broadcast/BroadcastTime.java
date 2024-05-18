package Broadcast;
class BroadcastsTime implements Comparable<BroadcastsTime> {
    private final byte hour;
    private final byte minutes;

    public BroadcastsTime(String time) {
        String[] parts = time.split(":");
        this.hour = Byte.parseByte(parts[0]);
        this.minutes = Byte.parseByte(parts[1]);
    }

    public byte getHour() {
        return hour;
    }

    public byte getMinutes() {
        return minutes;
    }

    public boolean after(BroadcastsTime t) {
        return compareTo(t) > 0;
    }

    public boolean before(BroadcastsTime t) {
        return compareTo(t) < 0;
    }

    public boolean between(BroadcastsTime t1, BroadcastsTime t2) {
        return !before(t1) && !after(t2);
    }

    @Override
    public int compareTo(BroadcastsTime o) {
        int hourDiff = Byte.compare(this.hour, o.hour);
        return hourDiff != 0 ? hourDiff : Byte.compare(this.minutes, o.minutes);
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minutes);
    }
}
package at.htlhl.graphdemo;

/**
 * @author Jonas Tuechler
 */
public class EdgeData {

    private int distance;

    public EdgeData(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return Integer.toString(distance);
    }
}

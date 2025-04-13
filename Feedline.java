package antenna;

public class Feedline {
    private double er;
    private double h;
    private double z0;

    public Feedline(double er, double hMm, double z0) {
        this.er = er;
        this.h = hMm / 1000.0;
        this.z0 = z0;
    }

    public double getWidth() {
        double A = z0 / 60 * Math.sqrt((er + 1) / 2)
                 + ((er - 1) / (er + 1)) * (0.23 + 0.11 / er);
        return (8 * h * Math.exp(A)) / (Math.exp(2 * A) - 2);
    }
}

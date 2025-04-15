package antenna;

public class MicrostripPatchAntenna {
    private double fr;  // Hz
    private double er;
    private double h;   // meters
    private double c = 3e8;

    public MicrostripPatchAntenna(double frGHz, double er, double hMm) {
        this.fr = frGHz * 1e9;     // GHz → Hz
        this.er = er;
        this.h = hMm / 1000.0;     // mm → meters
    }

    public double getPatchWidth() {
        return (c / (2 * fr)) * Math.sqrt(2 / (er + 1));
    }

    public double getEffectiveDielectricConstant() {
        double W = getPatchWidth();
        return (er + 1) / 2 + (er - 1) / 2 * (1 / Math.sqrt(1 + 12 * h / W));
    }

    public double getEffectiveLength() {
        return c / (2 * fr * Math.sqrt(getEffectiveDielectricConstant()));
    }

    public double getDeltaLength() {
        double W = getPatchWidth();
        double e_eff = getEffectiveDielectricConstant();
        return 0.412 * h * ((e_eff + 0.3) * (W / h + 0.264)) /
               ((e_eff - 0.258) * (W / h + 0.8));
    }

    public double getPatchLength() {
        return getEffectiveLength() - 2 * getDeltaLength();
    }

    public double getHeightMm() {
        return h * 1000;
    }
}

package antenna;

public class PerformanceCalculator {
    private static final double c = 3e8; // speed of light in m/s

    public static double calculateWavelength(double frequencyGHz) {
        return c / (frequencyGHz * 1e9); // meters
    }

    public static double calculateReturnLoss(double reflectionCoeffMagnitude) {
        return -20 * Math.log10(reflectionCoeffMagnitude);
    }

    public static double calculateVSWR(double reflectionCoeffMagnitude) {
        return (1 + reflectionCoeffMagnitude) / (1 - reflectionCoeffMagnitude);
    }

    public static double calculateFarFieldDistance(double D_m, double frequencyGHz) {
        double lambda = calculateWavelength(frequencyGHz);
        return (2 * Math.pow(D_m, 2)) / lambda;
    }

    public static double calculateEffectiveAperture(double gain, double frequencyGHz) {
        double lambda = calculateWavelength(frequencyGHz);
        return (gain * Math.pow(lambda, 2)) / (4 * Math.PI);
    }

    public static double calculateBeamwidth(double frequencyGHz, double patchWidth_m) {
        double lambda = calculateWavelength(frequencyGHz);
        return 50 * lambda / patchWidth_m; // degrees (improved estimation)
    }

    public static double calculateGain(double directivity, double efficiency) {
        return directivity * efficiency;
    }

    public static double calculateBandwidth(double frGHz, double er) {
        return frGHz * (0.39 / Math.sqrt(er)); // Approximate fractional bandwidth
    }
}

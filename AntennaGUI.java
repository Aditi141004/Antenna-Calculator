import antenna.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class AntennaGUI extends JFrame {
    private JTextField freqField, erField, heightField, impedanceField, gammaField;
    private JTextArea outputArea;
    private boolean isDarkMode = false;

    public AntennaGUI() {
        setTitle("📡 AntennaMaster - Patch Antenna Calculator | For & By RF Engineers");
        setSize(700, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        JButton darkModeBtn = new JButton("🌙 Toggle Dark Mode");
        darkModeBtn.addActionListener(e -> toggleDarkMode());
        add(darkModeBtn, BorderLayout.WEST);


        // 🧾 Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("📥 Input Parameters"));

        freqField = new JTextField("2.45");
        erField = new JTextField("4.4");
        heightField = new JTextField("1.6");
        impedanceField = new JTextField("50");
        gammaField = new JTextField("0.3");

        inputPanel.add(new JLabel("Resonant Frequency (GHz):"));
        inputPanel.add(freqField);
        inputPanel.add(new JLabel("Dielectric Constant (εr):"));
        inputPanel.add(erField);
        inputPanel.add(new JLabel("Substrate Height (mm):"));
        inputPanel.add(heightField);
        inputPanel.add(new JLabel("Target Impedance (Ω):"));
        inputPanel.add(impedanceField);
        inputPanel.add(new JLabel("Reflection Coefficient:"));
        inputPanel.add(gammaField);

        // 🧮 Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton calcBtn = new JButton("Calculate");
        JButton resetBtn = new JButton("Reset");
        JButton exportBtn = new JButton("Export to File");
        buttonPanel.add(calcBtn);
        buttonPanel.add(resetBtn);
        buttonPanel.add(exportBtn);

        // 📤 Output Area
        outputArea = new JTextArea(15, 40);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("📊 Output"));

        // 👇 Credits Label
        JLabel credits = new JLabel("Developed by Aditi & Abhiroop - OOPL Mini Project", SwingConstants.CENTER);
        credits.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // ➕ Add Panels
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(credits, BorderLayout.SOUTH);

        // 🎯 Actions
        calcBtn.addActionListener(e -> calculate());
        resetBtn.addActionListener(e -> resetFields());
        exportBtn.addActionListener(e -> exportToFile());

        setVisible(true);
    }

    private void calculate() {
        try {
            double fr = Double.parseDouble(freqField.getText());
            double er = Double.parseDouble(erField.getText());
            double h = Double.parseDouble(heightField.getText());
            double z0 = Double.parseDouble(impedanceField.getText());
            double gamma = Double.parseDouble(gammaField.getText());

            MicrostripPatchAntenna antenna = new MicrostripPatchAntenna(fr, er, h);
            Feedline feedline = new Feedline(er, h, z0);

            double patchWidth = antenna.getPatchWidth();
            double s11 = PerformanceCalculator.calculateReturnLoss(gamma);
            double vswr = PerformanceCalculator.calculateVSWR(gamma);
            double gain = PerformanceCalculator.calculateGain(6.0, 0.8); // Directivity × Efficiency
            double beamwidth = PerformanceCalculator.calculateBeamwidth(fr, patchWidth);
            double aperture = PerformanceCalculator.calculateEffectiveAperture(gain, fr);
            double farField = PerformanceCalculator.calculateFarFieldDistance(patchWidth, fr);
            double bandwidth = PerformanceCalculator.calculateBandwidth(fr, er);

            StringBuilder sb = new StringBuilder();
            sb.append("📐 Patch Antenna Dimensions:\n");
            sb.append(String.format("Patch Width W: %.3f mm\n", patchWidth * 1000));
            sb.append(String.format("Effective Dielectric Constant: %.4f\n", antenna.getEffectiveDielectricConstant()));
            sb.append(String.format("Patch Length L: %.3f mm\n", antenna.getPatchLength() * 1000));
            sb.append(String.format("Feedline Width Wf: %.3f mm\n", feedline.getWidth() * 1000));

            sb.append("\n📊 Performance Parameters:\n");
            sb.append(String.format("Return Loss (S11): –%.2f dB\n", Math.abs(s11)));
            sb.append(String.format("VSWR: %.2f\n", vswr));
            sb.append(String.format("Gain: %.2f dBi\n", gain));
            sb.append(String.format("Beamwidth: %.2f°\n", beamwidth));
            sb.append(String.format("Far Field Distance: %.2f m\n", farField));
            sb.append(String.format("Effective Aperture: %.6f m²\n", aperture));
            sb.append(String.format("Estimated Bandwidth: %.2f MHz\n", bandwidth * 1000));

            outputArea.setText(sb.toString());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "❌ Invalid input! Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetFields() {
        freqField.setText("");
        erField.setText("");
        heightField.setText("");
        impedanceField.setText("");
        gammaField.setText("");
        outputArea.setText("");
    }

    private void exportToFile() {
        if (outputArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ No output to export. Please calculate first.", "Export Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (PrintWriter out = new PrintWriter("antenna_output.txt")) {
            out.println(outputArea.getText());
            JOptionPane.showMessageDialog(this, "✅ Output exported to 'antenna_output.txt'", "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "❌ Failed to write file.", "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new AntennaGUI();
    }

    private void toggleDarkMode() {
        try {
            if (!isDarkMode) {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } else {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }
            SwingUtilities.updateComponentTreeUI(this);
            isDarkMode = !isDarkMode;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
}

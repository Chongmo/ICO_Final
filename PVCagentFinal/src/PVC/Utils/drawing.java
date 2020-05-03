package PVC.Utils;
import java.awt.Color;
import static java.awt.Color.WHITE;
import java.awt.Graphics;
import javax.swing.JFrame;

import java.text.DateFormat;
import java.util.Date;

/**
 *
 * @author Xingyu Shang
 */
public class drawing extends javax.swing.JFrame {

    /**
     * Creates new form DynamicDataWindow
     */
    private static final int MAX_SAMPLES = 10000;
    private int index = 0;
    private long[] time = new long[MAX_SAMPLES];
    private double[] val = new double[MAX_SAMPLES];
    DateFormat fmt = DateFormat.getDateTimeInstance();
    private int yRange = 20000;

    /**
     * Creates new form DataWindow
     */
    public drawing() {
        initComponents();
    }

    public drawing(String ieee) {
        initComponents();
        setTitle(ieee);
    }

    public void setRangeY(double y) {
        yRange = (int) y;
    }

    public void addData(long t, double v) {
        time[index] = t;
        val[index++] = (int) v;
        dataTextArea.append("  " + fmt.format(new Date(t)) + "    value = " + (int)v + "\n");
        dataTextArea.setCaretPosition(dataTextArea.getText().length());
        repaint();
    }

    private double tscale = 1.0 / 10.0;           // 1 pixel = 2 seconds = 2000 milliseconds
    // Graph the sensor values in the dataPanel JPanel

    public void paint(Graphics g) {
        super.paint(g);
        int left = dataPanel.getX() + 10;       // get size of pane
        int top = dataPanel.getY() + 30;
        int right = left + dataPanel.getWidth() - 20;
        int bottom = top + dataPanel.getHeight() - 20;

        int y0 = bottom - 20;                   // leave some room for margins
        int yn = top;
        int x0 = left + 33;
        int xn = right;

        double vscale = -(yn - y0) / yRange;      // light values range from 0 to 800
g.drawString("Best distance",x0-30,yn-10);
g.drawString("Run time",xn-50,y0-20);
        // draw X axis = time
        g.setColor(Color.BLACK);
        g.drawLine(x0, yn, x0, y0);
        g.drawLine(x0, y0, xn, y0);
        for (int xt = x0; xt < xn; xt += 50) {   // tick every 50 pixel
            g.drawLine(xt, y0, xt, y0 - 5);
            double n = xt/tscale/1000;
            g.drawString(Double.toString(n), xt , y0 + 20);
        }

        // draw Y axis = sensor reading
        g.setColor(Color.BLACK);
        for (int vt = y0; vt < yRange; vt += yRange / 20) {         // tick 20 times
            int v = y0 - (int) (vt * (y0 - yn) / yRange);
            g.drawLine(x0, v, x0 + 5, v);
            g.drawString(Integer.toString(vt), x0 - 38, v + 5);
        }

        // graph sensor values
        int xp = -1;
        int vp = -1;
        for (int i = 0; i < index; i++) {
            int x = x0 + (int) ((time[i] - time[0]) * tscale);
            int v = y0 - (int) (val[i] * (y0 - yn) / yRange);
            if (xp > 0) {
                g.drawLine(xp, vp, x, v);
                if (xp > xn) {
                    tscale /= 2;
                }
            }
            xp = x;
            vp = v;
        }

    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new drawing().setVisible(true);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dataPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        dataTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout dataPanelLayout = new javax.swing.GroupLayout(dataPanel);
        dataPanel.setLayout(dataPanelLayout);
        dataPanelLayout.setHorizontalGroup(
            dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 462, Short.MAX_VALUE)
        );
        dataPanelLayout.setVerticalGroup(
            dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 418, Short.MAX_VALUE)
        );

        dataTextArea.setColumns(20);
        dataTextArea.setRows(5);
        jScrollPane2.setViewportView(dataTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(dataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel dataPanel;
    private javax.swing.JTextArea dataTextArea;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}


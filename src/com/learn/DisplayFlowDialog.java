package com.learn;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DisplayFlowDialog extends JDialog {
    JTable tabledisplay = null;
    Vector rows, columns;
    DefaultTableModel tabModel;
    JScrollPane scrollPane;
    DecimalFormat format;

    public DisplayFlowDialog(JFrameMain frame) {
        super(frame);
        this.setVisible(true);
        setSize(800, 600);
        rows = new Vector();
        columns = new Vector();
        columns.addElement("源IP地址");
        columns.addElement("目的IP地址");
        columns.addElement("流量");
        tabModel = new DefaultTableModel();
        tabModel.setDataVector(rows, columns);
        tabledisplay = new JTable(tabModel);
        scrollPane = new JScrollPane(tabledisplay);
        this.getContentPane().add(new JScrollPane(tabledisplay), BorderLayout.CENTER);

        format = new DecimalFormat("#.00");
    }

    public void display(Counter counter) {
        rows.clear();
        List<Entry> lt = counter.sort();
        Vector v = null;
        String srcIp, desIp, tmp;
        for (Entry e : lt) {
            v = new Vector();
            srcIp = e.getSrcIp();
            desIp = e.getDesIp();
            v.add(srcIp);
            v.add(desIp);
            long flow = e.getFlow();
            tmp = "";
            if (flow > 1024 * 1024) {
                tmp += format.format(flow / 1024.0 / 1024.0);
                tmp += "M";
            } else if (flow > 1024) {
                tmp += format.format(flow / 1024.0);
                tmp += "K";
            } else {
                tmp = flow + "B";
            }
            v.add(tmp);
            rows.add(v);
        }
        tabledisplay.addNotify();
    }
}

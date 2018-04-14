package com.learn;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
public class JFrameMain extends javax.swing.JFrame implements ActionListener {
    private JMenuItem exitMenuItem;
    private JSeparator jSeparator2;
    private JMenuItem saveAsMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem stopMenuItem;
    private JMenuItem startMenuItem;
    private JMenu Menu;
    private JMenuBar jMenuBar1;
    private JMenuItem displayItem;
    private Counter counter;
    DisplayFlowDialog dfd;
    JTable tabledisplay = null;
    Vector rows, columns;
    DefaultTableModel tabModel;
    JScrollPane scrollPane;
    JLabel statusLabel;

    Netcaptor captor = new Netcaptor();// 网络捕获器

    public JFrameMain() {
        super();
        initGUI();
        counter = new Counter();
    }

    private void initGUI() {
        try {
            setSize(400, 300);
            {
                jMenuBar1 = new JMenuBar();
                setJMenuBar(jMenuBar1);
                {
                    Menu = new JMenu();
                    jMenuBar1.add(Menu);
                    Menu.setText("菜单");
                    Menu.setPreferredSize(new java.awt.Dimension(35, 21));
                    {
                        startMenuItem = new JMenuItem();
                        Menu.add(startMenuItem);
                        startMenuItem.setText("开始");
                        startMenuItem.setActionCommand("start");
                        startMenuItem.addActionListener(this);
                    }
                    {
                        stopMenuItem = new JMenuItem();
                        Menu.add(stopMenuItem);
                        stopMenuItem.setText("停止");
                        stopMenuItem.setActionCommand("stop");
                        stopMenuItem.addActionListener(this);
                    }
                    {
                        // 没有实现
                        saveMenuItem = new JMenuItem();
                        Menu.add(saveMenuItem);
                        saveMenuItem.setText("保存");
                    }
                    {
                        // 没有实现
                        saveAsMenuItem = new JMenuItem();
                        Menu.add(saveAsMenuItem);
                        saveAsMenuItem.setText("保存为 ...");
                    }
                    {
                        displayItem = new JMenuItem();
                        Menu.add(displayItem);
                        displayItem.setActionCommand("display");
                        displayItem.setText("显示流量");
                        displayItem.addActionListener(this);
                    }
                    {
                        jSeparator2 = new JSeparator();
                        Menu.add(jSeparator2);
                    }
                    {
                        exitMenuItem = new JMenuItem();
                        Menu.add(exitMenuItem);
                        exitMenuItem.setText("Exit");
                        exitMenuItem.setActionCommand("exit");
                        exitMenuItem.addActionListener(this);
                    }
                }
            }

            rows = new Vector();
            columns = new Vector();
            columns.addElement("数据报时间");
            columns.addElement("源IP地址");
            columns.addElement("目的IP地址");
            columns.addElement("首部长度");
            columns.addElement("数据长度");
            columns.addElement("是否分段");
            columns.addElement("分段偏移量");
            columns.addElement("首部内容");
            columns.addElement("数据内容");

            tabModel = new DefaultTableModel();
            tabModel.setDataVector(rows, columns);
            tabledisplay = new JTable(tabModel);
            scrollPane = new JScrollPane(tabledisplay);
            this.getContentPane().add(new JScrollPane(tabledisplay), BorderLayout.CENTER);
            statusLabel = new JLabel("3班 母翟龙");
            this.getContentPane().add(statusLabel, BorderLayout.SOUTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent event) {
        String cmd = event.getActionCommand();
        if (cmd.equals("start")) {// 开始
            captor.capturePacketsFromDevice();
            captor.setJFrame(this);
        } else if (cmd.equals("stop")) {// 停止
            captor.stopCapture();
        } else if (cmd.equals("display")) {// 显示流量
            dfd = new DisplayFlowDialog(this);
            dfd.display(counter);
        } else if (cmd.equals("exit")) {// 退出
            System.exit(0);
        }
    }
    // 处理数据包
    public void dealPacket(Packet packet) {
        try {
            Vector r = new Vector();
            String strtmp;
            Timestamp timestamp = new Timestamp((packet.sec * 1000) + (packet.usec / 1000));
            r.addElement(timestamp.toString()); // 数据报时间
            r.addElement(((IPPacket) packet).src_ip.toString().substring(1)); // 源IP地址
            r.addElement(((IPPacket) packet).dst_ip.toString().substring(1)); // 目的IP地址
            r.addElement(packet.header.length); // 首部长度
            r.addElement(packet.data.length); // 数据长度
            r.addElement(((IPPacket) packet).dont_frag == true ? "分段" : "不分段"); // 是否不分段
            r.addElement(((IPPacket) packet).offset); // 数据长度
            strtmp = "";
            for (int i = 0; i < packet.header.length; i++) {
                strtmp += Byte.toString(packet.header[i]);
            }
            r.addElement(strtmp); // 首部内容
            strtmp = "";
            for (int i = 0; i < packet.data.length; i++) {
                strtmp += Byte.toString(packet.data[i]);
            }
            r.addElement(strtmp); // 数据内容
            rows.addElement(r);
            tabledisplay.addNotify();
            counter.add(((IPPacket) packet).src_ip.toString().substring(1),
                    ((IPPacket) packet).dst_ip.toString().substring(1), packet.len/8);
            dfd.display(counter);
        } catch (Exception e) {
        }
    }
    // start
    public static void main(String[] args) {
        JFrameMain inst = new JFrameMain();
        inst.setVisible(true);
    }

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sniffdog;

import java.awt.EventQueue;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.swing.table.DefaultTableModel;
import org.jnetpcap.Pcap;
import org.jnetpcap.packet.JScanner;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.network.Ip4;


/**
 *
 * @author Rishabh
 */
public class Packet_Length extends javax.swing.JFrame {

    /**
     * Creates new form Packet_Length
     */
    public Packet_Length() {
        initComponents();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
       filter();
        display();         
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(579, 322));
        getContentPane().setLayout(null);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(90, 90, 375, 150);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Packet_Length.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Packet_Length.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Packet_Length.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Packet_Length.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Packet_Length().setVisible(true);
            }
        });
        
        
        
        
    }
    class node{
    int count,total_length,maxsize,minsize,avg;
    public node(int count,int total_length,int maxsize,int minsize,int avg){
    this.count=count;
    this.total_length=total_length;
    this.maxsize=maxsize;
    this.minsize=minsize;
    this.avg=avg;
    }
    }
    static LinkedHashMap<String,node> hm=new LinkedHashMap<>();
    
    private void filter(){
        hm.put("0-100", new node(0,0,-1,100000,0));
        hm.put("100-200", new node(0,0,-1,100000,0));
        hm.put("200-500", new node(0,0,-1,100000,0));
        hm.put("500-1500", new node(0,0,-1,100000,0));
        hm.put("1500-3000", new node(0,0,-1,100000,0));
        
        String addr = "C:\\Users\\Rishabh\\Downloads\\NetworkAnal\\"+User_Interface.filename+".pcap";
    
     Pcap pcap
                = Pcap.openOffline(addr, new StringBuilder());

        PcapPacketHandler<String> jpacketHandler = new handler_packetlength();
        JScanner.getThreadLocal().setFrameNumber(0);  
        pcap.loop(-1, jpacketHandler, "jNetPcap rocks!");

        pcap.close();
    
    
    
    
    }
    private class handler_packetlength implements PcapPacketHandler<String> {

        @Override
        public void nextPacket(final PcapPacket packet, String user) {
            int length=packet.getPacketWirelen();
            String key="";
            if(length<=100)key="0-100";
            else if(length<=200)key="100-200";
            else if(length<=500)key="200-500";
            else if(length<=1500)key="500-1500";
            else key="1500-3000";
        
        if(!hm.containsKey(key)){
        hm.put(key, new node(1,length,length,length,length));
        }
        else
        {
        node temp=hm.get(key);
        int newcount=temp.count+1;
        int newtotal=temp.total_length+length;
        int newmax=Math.max(length,temp.maxsize);
        int newmin=Math.min(length,temp.minsize);
        int newavg=newtotal/newcount;
        hm.put(key,new node(newcount,newtotal,newmax,newmin,newavg));
        
        }
        
        }
    }
    
    
private void display(){
    DefaultTableModel dtm=new DefaultTableModel(new String[]{"Range","Count","Total Length","Max Size","Min Size","Avg Size"}, 0);
    jTable1.setModel(dtm);
UI_Processing up=new UI_Processing(dtm);
for(String e:hm.keySet())
{node temp=hm.get(e);
int total=temp.count;
int total_length=temp.total_length;
int max=temp.maxsize;
int min=temp.minsize;
int avg=temp.avg;
up.addRow(new String[]{e,total+"",total_length+"",max+"",min+"",avg+""});
}
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}

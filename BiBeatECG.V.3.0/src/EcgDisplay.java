/*
 * 
 * This is the display class This class provides User Interface
 * 
 * 
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.util.AbstractSampleApp;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRXmlUtils;
import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.view.JasperViewer;
import org.jivesoftware.smack.XMPPException;

import org.w3c.dom.Document;


/**
 *
 * @author Aumi
 */
public class EcgDisplay extends javax.swing.JFrame {

    String ip = "";
    /*
     * Synchronization class synchronizes collecting and receiving data from usb
     */
    Synchronization sync = new Synchronization();
    /*
     * No use
     */
    StopWatch sw = new StopWatch();
        // EcgDisplay ecgDisplay = new EcgDisplay();
    /*
     * MyHID class has some methods for accessing usb using JNI (Java Native Interface) wrapper
     */
    MyHID myhid = new MyHID();
        
           /**
     * Vendor ID of My Device
     */
     final short VID = 0x16c0;
     /**
      * Product ID of My device
      */
     final short PID = 0x05df;

     /*
      * communication class is another class for accesing usb using JNA (Java Native Access)
      */
     communication usb = new communication();
        
        
     int id = 0;
     /*
      * No use
      */
        int resolution = 340;

        /*
         * Clientstat class has simply some methods naming set and get.We can set and get variables from this class
         */
        ClientStat clstat = new ClientStat();
         EcgRecordGraph ecgr = new EcgRecordGraph(clstat);
         
         

           /*
        *This classe is used for sending ECG data to remote server 
        */
         JabberSmackAPI jabb = new JabberSmackAPI();
         /*
          * These four classes are actulally PApplet 
          * LeadDisplay1 shows Lead I, II and III, LeadDisplay2 shows aVR, aVL, aVF
          * LeadDisplay1 shows Lead V1, V2 and V3, LeadDisplay2 shows V4, V5, V6
          * 
          */
        LeadDisplay1 ld1 = new LeadDisplay1(clstat,resolution,usb,ecgr,this,jabb);LeadDisplay2 ld2 = new LeadDisplay2(clstat,resolution,usb,ecgr,this,jabb);
        LeadDisplay3 ld3 = new LeadDisplay3(clstat,resolution,usb,ecgr,this,jabb); LeadDisplay4 ld4 = new LeadDisplay4(clstat,resolution,usb,ecgr,this,jabb);
        LeadDisplayExtended2 lde2 = new LeadDisplayExtended2(clstat,resolution,usb,ecgr,this,jabb);
        
        
        /*
         * LeadDisplay5 is a PApplet for realtime ECG display
         */
        LeadDisplay5 ld5 = new LeadDisplay5(clstat,resolution,ld1,ld2,ld3,ld4,lde2);
        
        //RecipientEmail email;// = new RecipientEmail(clstat);
        
        
        /*
         * This thread reads data from usb (Put method)
         */
        //Thread read = new InReportRead(sync,myhid,sw);
        
        //GetData getdata = new GetData(sync);
        /*
         * The two threads are either started (initially) or resumed (afret pause)
         * This flag decides wheather the threads will be started or resumed
         */
        boolean flag = true;
        
        
        EcgDisplay disp;

        /*
         * No use
         */
        //BeatRate beat = new BeatRate();
        
     
         
         
         
        
        
        
        ReceiveRemoteData remote = new ReceiveRemoteData(jabb, clstat, this); 
        
        LoginWait login = new LoginWait();
        
        
        
       /*
         * No Use
         */
        
        /*
         * The ECG data are sent to this class and they are stored untill 4096 data are being collected
         * After 4096 data are being collected they are sent to LeadDisplay1/LeadDisplay2/LeadDisplay3/LeadDisplay4
         */
        EcgGraph ecg = new EcgGraph(ecgr,resolution,clstat,ld1,ld2,ld3,ld4,lde2);//,ld5,ld6,ld7,ld8,ld9,ld10,ld11,ld12);
        
        InReportRead read = new InReportRead(sync,myhid,sw);
        /*
         * This Thread Collects the usb data
         */
        //Thread write = new GetData(sync,ld5,clstat);
        GetData write = new GetData(sync,ld5,clstat,ecg);
        
        String sourceFile = "";



        /**
         * IP address pattern matching
         */
        private Pattern pattern;
        private Matcher matcher;
        private static final String IPADDRESS_PATTERN =
		"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

        private String filepath = "c:/ECGtelemedicine.html";
        
        FileWriter outputStream = null;
        BufferedReader InStream = null;
        FileWriter idoutputStream = null;
        FileWriter flagoutputStream = null;
        
         //BufferedWriter outStream = null;
        
//        byte switch1[] = new byte[myhid.ReadOutputReportSize()];;byte switch2[] = new byte[myhid.ReadOutputReportSize()];
//        byte switch3[] = new byte[myhid.ReadOutputReportSize()];byte switch4[] = new byte[myhid.ReadOutputReportSize()];
//        byte switch5[] = new byte[myhid.ReadOutputReportSize()];byte switch6[] = new byte[myhid.ReadOutputReportSize()];
//        byte switch7[] = new byte[myhid.ReadOutputReportSize()];byte switch8[] = new byte[myhid.ReadOutputReportSize()];
//        byte switch9[] = new byte[myhid.ReadOutputReportSize()];byte switchA[] = new byte[myhid.ReadOutputReportSize()];
//        byte switchB[] = new byte[myhid.ReadOutputReportSize()];byte switchC[] = new byte[myhid.ReadOutputReportSize()];
        
        /*
         * No use
         */
        byte switch1[] = null;;byte switch2[] = null;
        byte switch3[] = null;byte switch4[] = null;
        byte switch5[] = null;byte switch6[] = null;
        byte switch7[] = null;byte switch8[] = null;
        byte switch9[] = null;byte switchA[] = null;
        byte switchB[] = null;byte switchC[] = null;
        
        /*
         * This buffer is used for switching betwenn 12 leads of ecg machine
         */
         byte selectLead[] = new byte[9];
         
         /*
          * Gain factor (5mm/mV or 10mm/mV)
          */
         byte gainFactor[] = new byte[9];
         
        
        /*
         * buffer for storing different lead's ecg data
         */
        static int [] lead1 = new int[512];static int [] lead2 = new int[512];static int [] lead3 = new int[512];static int [] lead4 = new int[512];
        static int [] lead5 = new int[512];static int [] lead6 = new int[512];static int [] lead7 = new int[512];static int [] lead8 = new int[512];
        static int [] lead9 = new int[512];static int [] lead10 = new int[512];static int [] lead11 = new int[512];static int [] lead12 = new int[512];
        static int [] leade21 = new int[512]; static int [] leade22 = new int[512];
        
        int gainF ;
        int horScallingF ;
        int filterF ;
        
        /*
         * Detail of sender's email address (used for sending ECG report via email from software)
         */
        String d_email = "ecgbmpt@gmail.com",
        d_password = "bmptw012010",
        d_host = "smtp.gmail.com",
        d_port = "465",
        m_to = " " , //"aumi_aik@yahoo.com",
        m_subject = " ",//Ecg Report of Mr. "+ clstat.getFirstName() + " " + clstat.getLastName()  ,
        m_text = "Enclosed please follow the attached document.";
        
        Thread loginThred;
        
        String loginID = "";
        String loginPass = "";

    public EcgDisplay() throws UnknownHostException {
        
        ImageIcon img = new ImageIcon("./Info/ecg-icon.png");
        this.setIconImage(img.getImage());
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        //this.setAlwaysOnTop(true);
        initComponents();
        //email = new RecipientEmail(clstat);
        JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory","net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
        
        
       //this.setLocationRelativeTo(null);
        
        
        try {
             InStream = new BufferedReader(new FileReader("./Info/name.txt"));
        try {
                String upazilaName = InStream.readLine();
                //System.out.println(upazilaName);
                upazilaLabel.setText(upazilaName);
                
                InStream = new BufferedReader(new FileReader("./Info/id.txt"));
            //String upazilaName;
       
                id  = Integer.parseInt(InStream.readLine());
                
            patientIdTextField.setText(""+(id+1));
            
             InStream = new BufferedReader(new FileReader("./Info/flags.txt"));
             
             gainF = Integer.parseInt(InStream.readLine());
             horScallingF = Integer.parseInt(InStream.readLine());
             filterF = Integer.parseInt(InStream.readLine());
             
              InStream = new BufferedReader(new FileReader("./Info/bin.txt")) ;
             loginID = InStream.readLine();
             loginPass = InStream.readLine();
             clstat.setUserID(loginID);
             
             InStream = new BufferedReader(new FileReader("./Info/ip.txt")) ; 
             ip = InStream.readLine();
             clstat.setIP(ip);
             
             
             if(gainF == 1)
             {
                 fivemmButtonMenuItem.setSelected(true);
                 short i = 9;
                 gainFactor[1] =  (byte) 0xbb;
                 usb.SetFeatureReport(gainFactor, i);
                 clstat.setGain(0.84f);
                 gainLabel.setText("5 mm/mV");
                 
             }
             else if(gainF == 2)
             {
                 tenmmButtonMenuItem3.setSelected(true);
                 short i = 9;
                 gainFactor[1] =  (byte) 0xaa;
                 usb.SetFeatureReport(gainFactor, i);
                 clstat.setGain(0.9f);
                 gainLabel.setText("10 mm/mV");
             }
             else
             {
                 tenmmButtonMenuItem3.setSelected(true);
                 short i = 9;
                 gainFactor[1] =  (byte) 0xaa;
                 usb.SetFeatureReport(gainFactor, i);
                 clstat.setGain(0.9f);
                 gainLabel.setText("10 mm/mV");
             }
             
             if(filterF == 1)
            {
                FilterCheckBoxMenuItem.setSelected(true);
                clstat.setFilterFlag(true);
                filterStatusLabel.setText("ON");

            }
             else if(filterF == 0)
            {
                FilterCheckBoxMenuItem.setSelected(false);
                clstat.setFilterFlag(false);
                filterStatusLabel.setText("OFF");
            }
            else
             {
                 FilterCheckBoxMenuItem.setSelected(true);
                 clstat.setFilterFlag(true);
                 filterStatusLabel.setText("ON");
             }
             
             
           switch (horScallingF) 
           {
                case 1: fifteenRadioButtonMenuItem1.setSelected(true);
                clstat.sethorScalling(1);
                mmPerSecLabel.setText("15 mm/sec");
                break;
                
                case 2: twentyRadioButtonMenuItem.setSelected(true);
                clstat.sethorScalling(2);
                mmPerSecLabel.setText("20 mm/sec");
                break;
                    
                case 3: two_fiveRadioButtonMenuItem.setSelected(true);
                clstat.sethorScalling(3);
                mmPerSecLabel.setText("25 mm/sec");
                break;
                    
                case 4: fiftyRadioButtonMenuItem.setSelected(true);
                clstat.sethorScalling(4);
                mmPerSecLabel.setText("50 mm/sec");
                break;
                    
                default:two_fiveRadioButtonMenuItem.setSelected(true);
                clstat.sethorScalling(3);
                mmPerSecLabel.setText("25 mm/sec");
                break;
                   
           }  
            
            
            } catch (IOException ex) {
                Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        try {
            InStream.close();
            
        } catch (IOException ex) {
            Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        clstat.setUpazila(upazilaLabel.getText());
        clstat.setPatientId(patientIdTextField.getText());
        
        Toolkit tk = Toolkit.getDefaultToolkit();
        Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

        int taskBarHeight = (int) tk.getScreenSize().getHeight() - winSize.height;
        setSize((int)tk.getScreenSize().getWidth(), (int) tk.getScreenSize().getHeight()-taskBarHeight);
        
        int screenWidth = (int)tk.getScreenSize().getWidth();
        int screenHeight = (int)tk.getScreenSize().getHeight();
        
//        int screenWidth = 1150;
//        int screenHeight = 550;
       
        
        if (screenWidth < 1200 || screenHeight < 570)
        {
        //setSize(screenWidth, screenHeight);
        //screenWidth = 1024;
        //screenHeight = 600 - 20;
        int multHeghtWidth = screenWidth * (screenHeight - 20);
        
        int fontSize = ((screenHeight * screenWidth * 11)/682800);
        setSize(1024, 570);
        Font font = new Font("Tahoma", Font.PLAIN, fontSize);
        idLabel.setFont(font);
        nameLabel.setFont(font);
        sexLabel.setFont(font);
        ageLabel.setFont(font);
        //jTabbedPane2.setFont(font);
        maleButton.setFont(font);
        femaleButton.setFont(font);
        acquirebutton.setFont(font);
        receiveButton.setFont(font);
        sendButton.setFont(font);
        //fiveMmRadioButton.setFont(font);
        //tenMmRadioButton.setFont(font);
        //Fi.setFont(font);
        //emailLabel.setFont(font);
        //sendMail.setFont(font);
        //leadComboBox.setFont(font);
        //nextLeadButton.setFont(font);
        
        int fontSizeLeadButton = ((screenHeight * screenWidth * 10)/682800);
        Font fontLeadButton = new Font("Tahoma", Font.PLAIN, fontSizeLeadButton);
        iRadioButton.setFont(fontLeadButton);
        iiRadioButton.setFont(fontLeadButton);
        iiiRadioButton.setFont(fontLeadButton);
        avrRadioButton.setFont(fontLeadButton);
        avlRadioButton.setFont(fontLeadButton);
        avfRadioButton.setFont(fontLeadButton);
        v1RadioButton.setFont(fontLeadButton);
        v2RadioButton.setFont(fontLeadButton);
        v3RadioButton.setFont(fontLeadButton);
        v4RadioButton.setFont(fontLeadButton);
        v5RadioButton.setFont(fontLeadButton);
        v6RadioButton.setFont(fontLeadButton);
        
        int fontSizeStartButton = ((screenHeight * screenWidth * 16)/682800);
        Font fontStartButton = new Font("Tahoma", Font.PLAIN, fontSizeStartButton);
        startStopToggleButton.setFont(fontStartButton);
        reportButton.setFont(fontStartButton);
        }
        //JOptionPane.showMessageDialog(null, "Width = " + screenWidth + "Height = " + screenHeight );
        
         /*
         * Add PApplets to Panel of User Interface
         */
        
        
          ld1Panel.add(ld1);ld2Panel.add(ld2);
          ld3Panel.add(ld3);ld4Panel.add(ld4);
          ld1Panel.add(ld1);
          extended2Panel.add(lde2);
          realtimePanel2.add(ld5);
          
        
//        /**
//         * Set Vendor ID of My device
//         */
//        usb.SetVendorID(VID);
//        /**
//         * Set Product ID of My device
//         */
//        usb.SetProductID(PID);
//        /**
//         * Obtain HID Handle of the Device(With specified VID and PID)
//         */
//        usb.getHIDHandle();
  

        //System.out.println("Bla bla" + myhid.ReadOutputReportSize());
//        if (myhid.FindTheHID(5824, 1503))
//		System.out.println("Device found");
//         System.out.println("Bla bla" + myhid.ReadOutputReportSize());
        
        switch1 = new byte[myhid.ReadOutputReportSize()];switch2 = new byte[myhid.ReadOutputReportSize()];
        switch3 = new byte[myhid.ReadOutputReportSize()];switch4 = new byte[myhid.ReadOutputReportSize()]; 
        switch5 = new byte[myhid.ReadOutputReportSize()];switch6 = new byte[myhid.ReadOutputReportSize()];
        switch7 = new byte[myhid.ReadOutputReportSize()];switch8 = new byte[myhid.ReadOutputReportSize()];
        switch9 = new byte[myhid.ReadOutputReportSize()];switchA = new byte[myhid.ReadOutputReportSize()];
        switchB = new byte[myhid.ReadOutputReportSize()];switchC = new byte[myhid.ReadOutputReportSize()];
        //selectLead[0]=  0;selectLead[1]=  1;
          for(int i = 0; i <9 ; i++)
          {
//              switch1[i] = 0;switch2[i] = 0;switch3[i] = 0;switch4[i] = 0;
//              switch5[i] = 0;switch6[i] = 0;switch7[i] = 0;switch8[i] = 0;
//              switch9[i] = 0;switchA[i] = 0;switchB[i] = 0;switchC[i] = 0;
              selectLead[i]=  0x00;
              gainFactor[i] = 0;
          }
          
//          switch1[1] = 0x01;switch2[1] = 0x02;switch3[1] = 0x03;switch4[1] = 0x04;
//          switch5[1] = 0x05;switch6[1] = 0x06;switch7[1] = 0x07;switch8[1] = 0x08;
//          switch9[1] = 0x09;switchA[1] = 0x0a;switchB[1] = 0x0b;switchC[1] = 0x0c;

//        ld5Panel.add(ld5);ld6Panel.add(ld6);
//        ld7Panel.add(ld7);ld8Panel.add(ld8);ld9Panel.add(ld9);
//        ld10Panel.add(ld10);ld11Panel.add(ld11);ld12Panel.add(ld12);
//        voidPanel.add(clstat);
        ecg.init();
        //ecgr.init();
        //beat.init();
        //clstat.init();
        ld1.init();ld2.init();ld3.init();ld4.init();ld5.init();lde2.init();
//        ld5.init();ld6.init();ld7.init();ld8.init();
//        ld9.init();ld10.init();ld11.init();ld12.init();
        clstat.leadDisplay(ld1, ld2, ld3, ld4, lde2);

        //voidPanel.setVisible(false);
        
        for(int i = 0 ; i< lead1.length; i++)
        {
            lead1[i] = 0;
        }
         clstat.setLead1(lead1);clstat.setLead2(lead1);clstat.setLead3(lead1);clstat.setLeadaVR(lead1);
         clstat.setLeadaVL(lead1);clstat.setLeadaVF(lead1);clstat.setLeadV1(lead1);clstat.setLeadV2(lead1);
         clstat.setLeadV3(lead1);clstat.setLeadV4(lead1);clstat.setLeadV5(lead1);clstat.setLeadV6(lead1);
         
//         jMenuItem1.setAccelerator(KeyStroke.getKeyStroke(
//        java.awt.event.KeyEvent.VK_N, 
//        java.awt.Event.CTRL_MASK));

         
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        ld1Panel1 = new javax.swing.JPanel();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        buttonGroup6 = new javax.swing.ButtonGroup();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        buttonGroup7 = new javax.swing.ButtonGroup();
        buttonGroup8 = new javax.swing.ButtonGroup();
        jPanel13 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        iRadioButton = new javax.swing.JRadioButton();
        avrRadioButton = new javax.swing.JRadioButton();
        v1RadioButton = new javax.swing.JRadioButton();
        v4RadioButton = new javax.swing.JRadioButton();
        iiRadioButton = new javax.swing.JRadioButton();
        avlRadioButton = new javax.swing.JRadioButton();
        v2RadioButton = new javax.swing.JRadioButton();
        v5RadioButton = new javax.swing.JRadioButton();
        v6RadioButton = new javax.swing.JRadioButton();
        v3RadioButton = new javax.swing.JRadioButton();
        avfRadioButton = new javax.swing.JRadioButton();
        iiiRadioButton = new javax.swing.JRadioButton();
        mmPerSecLabel = new javax.swing.JLabel();
        gainLabel = new javax.swing.JLabel();
        filterLabel = new javax.swing.JLabel();
        filterStatusLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        ld1Panel = new javax.swing.JPanel();
        ld2Panel = new javax.swing.JPanel();
        ld3Panel = new javax.swing.JPanel();
        ld4Panel = new javax.swing.JPanel();
        extended2Panel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        realtimePanel = new javax.swing.JPanel();
        realtimePanel2 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        pfNameTextField = new javax.swing.JTextField();
        maleButton = new javax.swing.JRadioButton();
        femaleButton = new javax.swing.JRadioButton();
        ageTextField = new javax.swing.JTextField();
        ageLabel = new javax.swing.JLabel();
        sexLabel = new javax.swing.JLabel();
        idLabel = new javax.swing.JLabel();
        patientIdTextField = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        leadComboBox = new javax.swing.JComboBox();
        nextLeadButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        acquirebutton = new javax.swing.JRadioButton();
        receiveButton = new javax.swing.JRadioButton();
        sendButton = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        startStopToggleButton = new javax.swing.JToggleButton();
        reportButton = new javax.swing.JButton();
        upazilaLabel = new javax.swing.JLabel();
        eiiRadioButton = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        FileMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        GainMenu = new javax.swing.JMenu();
        fivemmButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        tenmmButtonMenuItem3 = new javax.swing.JRadioButtonMenuItem();
        jMenu1 = new javax.swing.JMenu();
        fifteenRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        twentyRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        two_fiveRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        fiftyRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        FilterCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        ld1Panel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ld1Panel1.setPreferredSize(new java.awt.Dimension(774, 664));

        javax.swing.GroupLayout ld1Panel1Layout = new javax.swing.GroupLayout(ld1Panel1);
        ld1Panel1.setLayout(ld1Panel1Layout);
        ld1Panel1Layout.setHorizontalGroup(
            ld1Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 770, Short.MAX_VALUE)
        );
        ld1Panel1Layout.setVerticalGroup(
            ld1Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 660, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximizedBounds(new java.awt.Rectangle(0, 0, 0, 0));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jPanel13.setBackground(new java.awt.Color(221, 239, 221));
        jPanel13.setPreferredSize(new java.awt.Dimension(1130, 600));
        jPanel13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPanel13KeyPressed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(221, 239, 221));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        iRadioButton.setBackground(new java.awt.Color(221, 239, 221));
        buttonGroup6.add(iRadioButton);
        iRadioButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        iRadioButton.setText("  I");
        iRadioButton.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        iRadioButton.setMinimumSize(new java.awt.Dimension(0, 0));
        iRadioButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iRadioButtonMouseClicked(evt);
            }
        });

        avrRadioButton.setBackground(new java.awt.Color(221, 239, 221));
        buttonGroup6.add(avrRadioButton);
        avrRadioButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        avrRadioButton.setText("  aVR");
        avrRadioButton.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        avrRadioButton.setMinimumSize(new java.awt.Dimension(0, 0));
        avrRadioButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                avrRadioButtonMouseClicked(evt);
            }
        });

        v1RadioButton.setBackground(new java.awt.Color(221, 239, 221));
        buttonGroup6.add(v1RadioButton);
        v1RadioButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        v1RadioButton.setText("  V1");
        v1RadioButton.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        v1RadioButton.setMinimumSize(new java.awt.Dimension(0, 0));
        v1RadioButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                v1RadioButtonMouseClicked(evt);
            }
        });

        v4RadioButton.setBackground(new java.awt.Color(221, 239, 221));
        buttonGroup6.add(v4RadioButton);
        v4RadioButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        v4RadioButton.setText("  V4");
        v4RadioButton.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        v4RadioButton.setMinimumSize(new java.awt.Dimension(0, 0));
        v4RadioButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                v4RadioButtonMouseClicked(evt);
            }
        });

        iiRadioButton.setBackground(new java.awt.Color(221, 239, 221));
        buttonGroup6.add(iiRadioButton);
        iiRadioButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        iiRadioButton.setText("  II");
        iiRadioButton.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        iiRadioButton.setMinimumSize(new java.awt.Dimension(0, 0));
        iiRadioButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iiRadioButtonMouseClicked(evt);
            }
        });

        avlRadioButton.setBackground(new java.awt.Color(221, 239, 221));
        buttonGroup6.add(avlRadioButton);
        avlRadioButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        avlRadioButton.setText("  aVL");
        avlRadioButton.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        avlRadioButton.setMinimumSize(new java.awt.Dimension(0, 0));
        avlRadioButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                avlRadioButtonMouseClicked(evt);
            }
        });

        v2RadioButton.setBackground(new java.awt.Color(221, 239, 221));
        buttonGroup6.add(v2RadioButton);
        v2RadioButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        v2RadioButton.setText("  V2");
        v2RadioButton.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        v2RadioButton.setMinimumSize(new java.awt.Dimension(0, 0));
        v2RadioButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                v2RadioButtonMouseClicked(evt);
            }
        });

        v5RadioButton.setBackground(new java.awt.Color(221, 239, 221));
        buttonGroup6.add(v5RadioButton);
        v5RadioButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        v5RadioButton.setText("  V5");
        v5RadioButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                v5RadioButtonMouseClicked(evt);
            }
        });

        v6RadioButton.setBackground(new java.awt.Color(221, 239, 221));
        buttonGroup6.add(v6RadioButton);
        v6RadioButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        v6RadioButton.setText("  V6");
        v6RadioButton.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        v6RadioButton.setMinimumSize(new java.awt.Dimension(0, 0));
        v6RadioButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                v6RadioButtonMouseClicked(evt);
            }
        });

        v3RadioButton.setBackground(new java.awt.Color(221, 239, 221));
        buttonGroup6.add(v3RadioButton);
        v3RadioButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        v3RadioButton.setText("  V3");
        v3RadioButton.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        v3RadioButton.setMinimumSize(new java.awt.Dimension(0, 0));
        v3RadioButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                v3RadioButtonMouseClicked(evt);
            }
        });

        avfRadioButton.setBackground(new java.awt.Color(221, 239, 221));
        buttonGroup6.add(avfRadioButton);
        avfRadioButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        avfRadioButton.setText("  aVF");
        avfRadioButton.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        avfRadioButton.setMinimumSize(new java.awt.Dimension(0, 0));
        avfRadioButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                avfRadioButtonMouseClicked(evt);
            }
        });

        iiiRadioButton.setBackground(new java.awt.Color(221, 239, 221));
        buttonGroup6.add(iiiRadioButton);
        iiiRadioButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        iiiRadioButton.setText("  III");
        iiiRadioButton.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        iiiRadioButton.setMinimumSize(new java.awt.Dimension(0, 0));
        iiiRadioButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iiiRadioButtonMouseClicked(evt);
            }
        });

        mmPerSecLabel.setBackground(new java.awt.Color(0, 153, 153));
        mmPerSecLabel.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        mmPerSecLabel.setForeground(new java.awt.Color(0, 102, 102));
        mmPerSecLabel.setText("25 mm/s");

        gainLabel.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        gainLabel.setForeground(new java.awt.Color(0, 102, 204));
        gainLabel.setText("10 mm/mV");

        filterLabel.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        filterLabel.setForeground(new java.awt.Color(0, 102, 102));
        filterLabel.setText("50 Hz Filter :");

        filterStatusLabel.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        filterStatusLabel.setForeground(new java.awt.Color(0, 102, 102));
        filterStatusLabel.setText("OFF");

        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jScrollPane1.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jScrollPane1MouseWheelMoved(evt);
            }
        });
        jScrollPane1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseMoved(evt);
            }
        });

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Recorded ECG Display", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12), new java.awt.Color(0, 102, 204))); // NOI18N
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane1.setMaximumSize(new java.awt.Dimension(520, 470));
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(0, 0));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(518, 460));
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        ld1Panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        ld1Panel.setPreferredSize(new java.awt.Dimension(501, 451));

        javax.swing.GroupLayout ld1PanelLayout = new javax.swing.GroupLayout(ld1Panel);
        ld1Panel.setLayout(ld1PanelLayout);
        ld1PanelLayout.setHorizontalGroup(
            ld1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 499, Short.MAX_VALUE)
        );
        ld1PanelLayout.setVerticalGroup(
            ld1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Lead I  II  III", ld1Panel);

        ld2Panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ld2Panel.setPreferredSize(new java.awt.Dimension(501, 451));

        javax.swing.GroupLayout ld2PanelLayout = new javax.swing.GroupLayout(ld2Panel);
        ld2Panel.setLayout(ld2PanelLayout);
        ld2PanelLayout.setHorizontalGroup(
            ld2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 497, Short.MAX_VALUE)
        );
        ld2PanelLayout.setVerticalGroup(
            ld2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 448, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("aVR  aVL  aVF", ld2Panel);

        ld3Panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ld3Panel.setPreferredSize(new java.awt.Dimension(501, 751));

        javax.swing.GroupLayout ld3PanelLayout = new javax.swing.GroupLayout(ld3Panel);
        ld3Panel.setLayout(ld3PanelLayout);
        ld3PanelLayout.setHorizontalGroup(
            ld3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 497, Short.MAX_VALUE)
        );
        ld3PanelLayout.setVerticalGroup(
            ld3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 448, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("V1  V2  V3", ld3Panel);

        ld4Panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ld4Panel.setPreferredSize(new java.awt.Dimension(501, 751));

        javax.swing.GroupLayout ld4PanelLayout = new javax.swing.GroupLayout(ld4Panel);
        ld4Panel.setLayout(ld4PanelLayout);
        ld4PanelLayout.setHorizontalGroup(
            ld4PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 497, Short.MAX_VALUE)
        );
        ld4PanelLayout.setVerticalGroup(
            ld4PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 448, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("V4  V5  V6", ld4Panel);

        extended2Panel.setPreferredSize(new java.awt.Dimension(501, 121));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Please take two consecutive measurements ");

        javax.swing.GroupLayout extended2PanelLayout = new javax.swing.GroupLayout(extended2Panel);
        extended2Panel.setLayout(extended2PanelLayout);
        extended2PanelLayout.setHorizontalGroup(
            extended2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(extended2PanelLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel1)
                .addContainerGap(53, Short.MAX_VALUE))
        );
        extended2PanelLayout.setVerticalGroup(
            extended2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, extended2PanelLayout.createSequentialGroup()
                .addContainerGap(312, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(82, 82, 82))
        );

        jTabbedPane1.addTab("Extended lead II", extended2Panel);

        jScrollPane1.setViewportView(jTabbedPane1);

        realtimePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Real Time ECG Display", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12), new java.awt.Color(255, 51, 102))); // NOI18N
        realtimePanel.setMinimumSize(new java.awt.Dimension(501, 181));
        realtimePanel.setPreferredSize(new java.awt.Dimension(501, 185));

        realtimePanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        realtimePanel2.setMinimumSize(new java.awt.Dimension(501, 181));
        realtimePanel2.setPreferredSize(new java.awt.Dimension(501, 181));

        javax.swing.GroupLayout realtimePanel2Layout = new javax.swing.GroupLayout(realtimePanel2);
        realtimePanel2.setLayout(realtimePanel2Layout);
        realtimePanel2Layout.setHorizontalGroup(
            realtimePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 497, Short.MAX_VALUE)
        );
        realtimePanel2Layout.setVerticalGroup(
            realtimePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 177, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout realtimePanelLayout = new javax.swing.GroupLayout(realtimePanel);
        realtimePanel.setLayout(realtimePanelLayout);
        realtimePanelLayout.setHorizontalGroup(
            realtimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(realtimePanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        realtimePanelLayout.setVerticalGroup(
            realtimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(realtimePanelLayout.createSequentialGroup()
                .addComponent(realtimePanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(214, 214, 233));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Patient's Detail"));

        nameLabel.setText("Name :");
        nameLabel.setMinimumSize(new java.awt.Dimension(17, 7));

        pfNameTextField.setPreferredSize(new java.awt.Dimension(0, 0));
        pfNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pfNameTextFieldActionPerformed(evt);
            }
        });

        maleButton.setBackground(new java.awt.Color(214, 214, 233));
        buttonGroup1.add(maleButton);
        maleButton.setText("Male");
        maleButton.setMinimumSize(new java.awt.Dimension(20, 10));
        maleButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                maleButtonMouseClicked(evt);
            }
        });
        maleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maleButtonActionPerformed(evt);
            }
        });

        femaleButton.setBackground(new java.awt.Color(214, 214, 233));
        buttonGroup1.add(femaleButton);
        femaleButton.setText("Female");
        femaleButton.setMinimumSize(new java.awt.Dimension(20, 10));
        femaleButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                femaleButtonMouseClicked(evt);
            }
        });

        ageTextField.setMinimumSize(new java.awt.Dimension(0, 0));
        ageTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ageTextFieldActionPerformed(evt);
            }
        });

        ageLabel.setText("Age :");
        ageLabel.setMinimumSize(new java.awt.Dimension(17, 7));

        sexLabel.setText("Sex :");
        sexLabel.setMinimumSize(new java.awt.Dimension(17, 7));

        idLabel.setText("Patient ID:");

        patientIdTextField.setEditable(false);
        patientIdTextField.setBackground(new java.awt.Color(255, 255, 204));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(idLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                            .addComponent(nameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(11, 11, 11))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(sexLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(23, 23, 23)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pfNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ageTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(maleButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(femaleButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(patientIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(patientIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pfNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(maleButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sexLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(femaleButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ageTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(ageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)))
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(214, 214, 233));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Lead Select", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12), new java.awt.Color(0, 102, 153))); // NOI18N
        jPanel8.setMinimumSize(new java.awt.Dimension(150, 60));
        jPanel8.setPreferredSize(new java.awt.Dimension(300, 120));

        leadComboBox.setBackground(new java.awt.Color(240, 240, 240));
        leadComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Lead", "Lead I", "Lead II", "Lead III", "Lead aVR", "Lead aVL", "Lead aVF", "Lead V1", "Lead V2", "Lead V3", "Lead V4", "Lead V5", "Lead V6", "Extended Lead II" }));
        leadComboBox.setMinimumSize(new java.awt.Dimension(0, 0));
        leadComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leadComboBoxActionPerformed(evt);
            }
        });

        nextLeadButton.setText("Next Lead");
        nextLeadButton.setMinimumSize(new java.awt.Dimension(0, 0));
        nextLeadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextLeadButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nextLeadButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(leadComboBox, 0, 233, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(leadComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextLeadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(214, 214, 233));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Select One of these three options"));
        jPanel1.setPreferredSize(new java.awt.Dimension(220, 100));

        acquirebutton.setBackground(new java.awt.Color(214, 214, 233));
        buttonGroup3.add(acquirebutton);
        acquirebutton.setText("Acquire Data Only");
        acquirebutton.setMinimumSize(new java.awt.Dimension(50, 10));
        acquirebutton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                acquirebuttonMouseClicked(evt);
            }
        });

        receiveButton.setBackground(new java.awt.Color(214, 214, 233));
        buttonGroup3.add(receiveButton);
        receiveButton.setText("Receive data only");
        receiveButton.setMinimumSize(new java.awt.Dimension(50, 10));
        receiveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                receiveButtonMouseClicked(evt);
            }
        });

        sendButton.setBackground(new java.awt.Color(214, 214, 233));
        buttonGroup3.add(sendButton);
        sendButton.setText("Acquire and send data");
        sendButton.setMinimumSize(new java.awt.Dimension(50, 10));
        sendButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sendButtonMouseClicked(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(214, 214, 233));
        jLabel2.setText(" ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 13, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(acquirebutton, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(receiveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(acquirebutton, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addGap(3, 3, 3)
                .addComponent(receiveButton, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addGap(5, 5, 5)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE))
        );

        startStopToggleButton.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        startStopToggleButton.setText("START");
        startStopToggleButton.setMinimumSize(new java.awt.Dimension(25, 24));
        startStopToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startStopToggleButtonActionPerformed(evt);
            }
        });

        reportButton.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        reportButton.setText("Generate Report for Printing");
        reportButton.setDoubleBuffered(true);
        reportButton.setMinimumSize(new java.awt.Dimension(85, 24));
        reportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportButtonActionPerformed(evt);
            }
        });

        upazilaLabel.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        upazilaLabel.setForeground(new java.awt.Color(0, 51, 255));
        upazilaLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        eiiRadioButton.setBackground(new java.awt.Color(221, 239, 221));
        buttonGroup6.add(eiiRadioButton);
        eiiRadioButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        eiiRadioButton.setText("Extended  II");
        eiiRadioButton.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        eiiRadioButton.setMinimumSize(new java.awt.Dimension(0, 0));
        eiiRadioButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eiiRadioButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(iRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(avrRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(v1RadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(v4RadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(iiiRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(avfRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(v3RadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(v6RadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(iiRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(avlRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(v2RadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(v5RadioButton))
                                    .addComponent(eiiRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(mmPerSecLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53)
                        .addComponent(gainLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(filterLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(filterStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(realtimePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(reportButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(startStopToggleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(upazilaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(upazilaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mmPerSecLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(gainLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(filterLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(filterStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(realtimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(startStopToggleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(reportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGap(109, 109, 109)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(iRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(avrRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(v1RadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(v4RadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(81, 81, 81)
                            .addComponent(eiiRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(35, 35, 35)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(iiRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(avlRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(v2RadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(v5RadioButton)))
                            .addGap(124, 124, 124)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(avfRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(iiiRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(v3RadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(v6RadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGap(72, 72, 72))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(221, 239, 221));
        jPanel4.setPreferredSize(new java.awt.Dimension(30, 607));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 48, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel6.setBackground(new java.awt.Color(221, 239, 221));
        jPanel6.setPreferredSize(new java.awt.Dimension(30, 607));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 47, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel7.setBackground(new java.awt.Color(221, 239, 221));
        jPanel7.setMinimumSize(new java.awt.Dimension(1, 1));
        jPanel7.setPreferredSize(new java.awt.Dimension(1237, 25));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 900, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
        );

        jPanel9.setBackground(new java.awt.Color(221, 239, 221));
        jPanel9.setMinimumSize(new java.awt.Dimension(1, 1));
        jPanel9.setPreferredSize(new java.awt.Dimension(1237, 25));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 900, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 46, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 900, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 900, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
        );

        getContentPane().add(jPanel13);

        jMenuBar1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenuBar1.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jMenuBar1.setDoubleBuffered(true);

        FileMenu.setText("File");
        FileMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        FileMenu.setDoubleBuffered(true);

        jMenuItem1.setText("New Measurement");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        FileMenu.add(jMenuItem1);

        GainMenu.setText("Gain");

        buttonGroup7.add(fivemmButtonMenuItem);
        fivemmButtonMenuItem.setText("5 mm/mV");
        fivemmButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fivemmButtonMenuItemActionPerformed(evt);
            }
        });
        GainMenu.add(fivemmButtonMenuItem);

        buttonGroup7.add(tenmmButtonMenuItem3);
        tenmmButtonMenuItem3.setText("10 mm/mV");
        tenmmButtonMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tenmmButtonMenuItem3ActionPerformed(evt);
            }
        });
        GainMenu.add(tenmmButtonMenuItem3);

        FileMenu.add(GainMenu);

        jMenu1.setText("Horizontal Scaling");

        buttonGroup8.add(fifteenRadioButtonMenuItem1);
        fifteenRadioButtonMenuItem1.setText("15 mm/Sec");
        fifteenRadioButtonMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fifteenRadioButtonMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(fifteenRadioButtonMenuItem1);

        buttonGroup8.add(twentyRadioButtonMenuItem);
        twentyRadioButtonMenuItem.setText("20 mm/Sec");
        twentyRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                twentyRadioButtonMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(twentyRadioButtonMenuItem);

        buttonGroup8.add(two_fiveRadioButtonMenuItem);
        two_fiveRadioButtonMenuItem.setText("25 mm/Sec");
        two_fiveRadioButtonMenuItem.setAutoscrolls(true);
        two_fiveRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_fiveRadioButtonMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(two_fiveRadioButtonMenuItem);

        buttonGroup8.add(fiftyRadioButtonMenuItem);
        fiftyRadioButtonMenuItem.setText("50 mm/sec");
        fiftyRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fiftyRadioButtonMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(fiftyRadioButtonMenuItem);

        FileMenu.add(jMenu1);

        FilterCheckBoxMenuItem.setText("50 Hz Filter");
        FilterCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FilterCheckBoxMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(FilterCheckBoxMenuItem);
        FileMenu.add(jSeparator1);

        jMenuItem2.setText("Exit");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        FileMenu.add(jMenuItem2);

        jMenuBar1.add(FileMenu);

        jMenu2.setText("Options");

        jMenuItem3.setText("Generate Report from existing xml file");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem5.setText("Edit Organization Name");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuItem6.setText("Edit gMail User ID and Password");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuItem7.setText("Add/Delete User");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuItem4.setText("Send xml file by email");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void reportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportButtonActionPerformed
        // TODO add your handling code here:
//        ld2Panel.;
////        ld2Panel.setVisible(true);
//        ld1Panel.revalidate();
//        ld2Panel.revalidate();
//        ld3Panel.revalidate();
//        ld4Panel.revalidate();
        //jScrollPane1.revalidate();
        login.setVisible(true);
         try {
              
            
            if(clstat.getSendDataFlag() !=2)
           
            {
               
                idoutputStream = new FileWriter("./Info/id.txt");
                idoutputStream.write(""+(id+1));
                idoutputStream.flush();
                idoutputStream.close();
                InStream.close();
           }
            
            
          } catch (IOException ex) {
            Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
          }
        
        sourceFile =clstat.getUpazila()+"_"+clstat.getPatientId()+"_"+pfNameTextField.getText()+".xml";
        try
         {

		//outputStream = new FileWriter("d:/ECGtelemedicine.xml");
                //outputStream = new FileWriter("report/ECGtelemedicine.xml");
             //File file = new File("./report/"+sourceFile);
             //file = file.getAbsoluteFile();
                outputStream = new FileWriter("./Database/"+sourceFile);
             //outputStream = new FileWriter(file);
                
             //outputStream = new FileWriter("C:/"+sourceFile);

	}
                
	catch(IOException e){

            System.out.println("Error to access file");
	}
        
//      
        
        lead1 = clstat.getLead1(); lead2 = clstat.getLead2(); lead3 = clstat.getLead3(); lead4 = clstat.getLeadaVR();
        lead5 = clstat.getLeadaVL(); lead6 = clstat.getLeadaVF(); lead7 = clstat.getLeadV1(); lead8 = clstat.getLeadV2();
        lead9 = clstat.getLeadV3(); lead10 = clstat.getLeadV4(); lead11 = clstat.getLeadV5(); lead12 = clstat.getLeadV6();
        
        
        
        
            leade21 = ClientStat.leadE21; 
        
        
        
            leade22 = ClientStat.leadE22; 
        
        
        
        
        
        String horizontalScaling = "", verticalScaling = "",verticalScalingV = "", sec0 = "", sec1 = "", sec2 = "", sec3 = "", sec4 = "";
        
        
        if(clstat.gethorScalling()==1)
        {
            horizontalScaling = "15 mm/sec";
            sec0 = "0s";sec1 = "1.6s";sec2 = "3.3s"; sec3 = "5s"; sec4 = "6.6s";
        }
        else if(clstat.gethorScalling()==2)
        {
            horizontalScaling = "20 mm/sec";
            sec0 = "0s";sec1 = "1.25s";sec2 = "2.5s"; sec3 = "3.75s"; sec4 = "5s";
        }
        else if(clstat.gethorScalling()==3)
        {
            horizontalScaling = "25 mm/sec";
            sec0 = "0s";sec1 = "1s";sec2 = "2s"; sec3 = "3s"; sec4 = "4s";
        }
        else if(clstat.gethorScalling()==4)
        {
            horizontalScaling = "50 mm/sec";
            sec0 = "0s";sec1 = "0.5s";sec2 = "1s"; sec3 = "1.5s"; sec4 = "2s";
        }
        
       verticalScaling = this.clstat.getVerticalScalling();
       verticalScalingV = this.clstat.getVerticalScallingV();
        
        
        try {

            outputStream.write("<start>" + "\n");
            outputStream.write("<info>" + "\n");
            outputStream.write("<fname>" + clstat.getFirstName() + "</fname>" +"\n"
                    //+ "<lname>" + clstat.getLastName() + "</lname>" +"\n"
                    + "<sex>" + clstat.getSex() + "</sex>" +"\n"
                    + "<age>" + clstat.getAge() + "</age>" +"\n"
                    + "<id>" + clstat.getPatientId() + "</id>" +"\n"
                    + "<upazila>" + clstat.getUpazila() + "</upazila>" +"\n"
                    + "<horiz>" + horizontalScaling + "</horiz>" +"\n"
                    + "<vert>" + verticalScaling + "</vert>" +"\n"
                    + "<vertV>" + verticalScalingV + "</vertV>" +"\n"
                    + "<sec0>" + sec0 + "</sec0>" +"\n"
                    + "<sec1>" + sec1 + "</sec1>" +"\n"
                    + "<sec2>" + sec2 + "</sec2>" +"\n"
                    + "<sec3>" + sec3 + "</sec3>" +"\n"
                    + "<sec4>" + sec4 + "</sec4>" +"\n");
                        
            outputStream.write("</info>" + "\n");
           for(int i = 1; i < clstat.getLead1().length - 10 ; i++)
                {
                    outputStream.write("<leadstart>" + "\n");
                    outputStream.write("<lead>" + "\n" + "<x>" + (i-1) + "</x>" + "\n" + "<lead1>" + lead1[i] + "</lead1>" + "\n" 
                           + "<lead2>" + lead2[i] + "</lead2>" + "\n" 
                            + "<lead3>" + lead3[i] + "</lead3>" + "\n"
                            + "<lead4>" + lead4[i] + "</lead4>" + "\n"
                            + "<lead5>" + lead5[i] + "</lead5>" + "\n"
                            + "<lead6>" + lead6[i] + "</lead6>" + "\n"
                            + "<lead7>" + lead7[i] + "</lead7>" + "\n"
                            + "<lead8>" + lead8[i] + "</lead8>" + "\n"
                            + "<lead9>" + lead9[i] + "</lead9>" + "\n"
                            + "<lead10>" + lead10[i] + "</lead10>" + "\n"
                            + "<lead11>" + lead11[i] + "</lead11>" + "\n"
                            + "<lead12>" + lead12[i] + "</lead12>" + "\n"
                            + "<leade21>" + leade21[i] + "</leade21>" + "\n"
                            + "<leade22>" + leade22[i] + "</leade22>" + "\n"
                            + "</lead>" + "\n"); 
                    outputStream.write("</leadstart>" + "\n");
                }
            
           

            
            outputStream.write("</start>" + "\n");
            outputStream.flush();
            outputStream.close();
            
            
            
            /*
             * 
             * Print
             */
            JasperReport jasperReport = null;
            JasperViewer jv = null;
            JasperPrint jprint = null;
                
                long start = System.currentTimeMillis();
		Map params = new HashMap();
//		Document document = JRXmlUtils.parse(JRLoader.getLocationInputStream("data/northwind.xml"));
                Document document = null;
            try 
            {
                //File file2 = new File("./report/"+sourceFile);
                //file2 = file2.getAbsoluteFile();
                //document = JRXmlUtils.parse(JRLoader.getLocationInputStream(file2+""));
                document = JRXmlUtils.parse(JRLoader.getLocationInputStream("./Database/"+sourceFile));
                //document = JRXmlUtils.parse(JRLoader.getLocationInputStream("C:/Program Files/Common Files/ECG_BMPT/report/"+sourceFile));
                //document = JRXmlUtils.parse(JRLoader.getLocationInputStream("report/Ahamad Imtiaz.xml"));
            } 
            catch (JRException ex) {
                Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
            }
		params.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);
		params.put(JRXPathQueryExecuterFactory.XML_DATE_PATTERN, "yyyy-MM-dd");
		params.put(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN, "#,##0.##");
		params.put(JRXPathQueryExecuterFactory.XML_LOCALE, Locale.ENGLISH);
		params.put(JRParameter.REPORT_LOCALE, Locale.US);
		
                 //jasperReport = JasperCompileManager.compileReport("reports/CustomersReport.jrxml");
		//JasperFillManager.fillReportToFile(jasperReport, params);
//                 net.sf.jasperreports.engine.JasperPrint jprint = JasperFillManager.fillReport("reports/CustomersReport.jasper", params);
                 
            try 
            {
                //File file3 = new File("./report/EcgReport.jasper");
                //file3 = file3.getAbsoluteFile();
                //jprint = JasperFillManager.fillReport(file3+"", params);
                
                jprint = JasperFillManager.fillReport("./report/EcgReport.jasper", params);
                //jprint = JasperFillManager.fillReport("C:/Program Files/Common Files/ECG_BMPT/report/EcgReport.jasper", params);
            } 
            catch (JRException ex) {
                Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
            }
            login.setVisible(false);
                this.setFocusableWindowState(false);
                //System.out.println("A");
                jv = new JasperViewer(jprint, false);
                jv.setTitle("ECG Report");
                
                jv.setExtendedState( JFrame.MAXIMIZED_BOTH);
                //jv.setAlwaysOnTop(true);
                System.err.println("Filling time : " + (System.currentTimeMillis() - start));
                
                jv.setVisible(true);
       
		
//            try {
//                JasperExportManager.exportReportToPdfFile(jprint, "./report/EcgReport.pdf");
//            } catch (JRException ex) {
//                Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
//            }
            
            
        }
        catch (IOException ex) {
            Logger.getLogger(EcgRecordGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
     
        

    }//GEN-LAST:event_reportButtonActionPerformed

    private void acquirebuttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_acquirebuttonMouseClicked
        // TODO add your handling code here:
        jLabel2.setText("");
        if(clstat.getSendDataFlag() == 2)
        {
             pfNameTextField.setEditable(true);
            ageTextField.setEditable(true);
            leadComboBox.setEnabled(true);
            maleButton.setEnabled(true);
            femaleButton.setEnabled(true);
            nextLeadButton.setEnabled(true);
            startStopToggleButton.setEnabled(true);
            //remote.activateStop(true);
            
        }
        
        
                /**
         * Set Vendor ID of My device
         */
        usb.SetVendorID(VID);
        /**
         * Set Product ID of My device
         */
        usb.SetProductID(PID);
        /**
         * Obtain HID Handle of the Device(With specified VID and PID)
         */
        usb.getHIDHandle();
  

        
        
        if (myhid.FindTheHID(5824, 1503))
        {
            System.out.println("Device found");
            clstat.sendDataFlag(1);
        }
        else
        {
            JOptionPane pane = new JOptionPane( "Device not found", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = pane.createDialog("Error");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
            
           buttonGroup3.clearSelection();
            
//            JOptionPane.showMessageDialog(null, "Device not found");
//            System.exit(0);
        }
       
    }//GEN-LAST:event_acquirebuttonMouseClicked

    private void serverLogin() {
        
        try {
                // open a connection to the site
                                    URL url = new URL(ip+"/ecgserver/auth/ecglogin");
                                    URLConnection con = url.openConnection();
                                    // activate the output
                                    con.setDoOutput(true);
                                    //con.setRequestProperty("Content-Type", "text/plain");
                                    PrintStream ps = new PrintStream(con.getOutputStream());
                                    // send your parameters to your site
                                    ps.print("username="+loginID);
                                    ps.print("&password="+loginPass);

                                    // we have to get the input stream in order to actually send the request
                                    con.getInputStream();

                                    // close the print stream
                                    ps.close();

                                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                                    String line = null;
                                    while ((line = in.readLine()) != null) 
                                    {
                                        if(line.equals("bay00LAB"))
                                        {
                                            clstat.setIsLoggedIn(true);
                                        }
                                        System.out.println(line);
                                    }
                                    }   
                            catch (MalformedURLException ex) 
                            {
                                    System.out.println(ex);        //Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);

                            } 
                            catch (IOException ex) 
                            {
                             System.out.println(ex);                //Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
       
    }
    private void receiveButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_receiveButtonMouseClicked
     // jLabel1.setText("Logging in Please Wait");
        jLabel2.setText("");
        
        try {
            // TODO add your handling code here:
             //new PlayGround().setVisible(true);
            //loginLabel1.setVisible(true);
            //jProgressBar1.setValue(50);
            //JOptionPane.showMessageDialog(this, "WAit");
            //System.out.println("Hello");
            //jLabel1.setText("Logging in Please Wait");
            
             //obj.processLogin();
            
           
//            if(remote.running)
//            {
//                remote.activateStop(false);
//                 //System.out.println("Hellooooo");
//        //
            //Login to server via PHP
            
                //System.out.println("Hellooooo");
                login.setVisible(true);
                //jabb.login();
                serverLogin();
                 
                //remote.start();
                //remote.isRunning();
                login.setVisible(false);
                clstat.sendDataFlag(2);
            
            if(clstat.isLoggegIn())
            {
                RemoteUserID email = new RemoteUserID(clstat);
                email.jLabel2.setText("Enter Sender's gMailID");

                jLabel2.setText("Logged in");
                jLabel2.setForeground(Color.blue);

                clstat.setSendOrReceive(1);
                patientIdTextField.setText("");

                email.setVisible(true);


                pfNameTextField.setEditable(false);
                ageTextField.setEditable(false);
                leadComboBox.setEnabled(false);
                maleButton.setEnabled(false);
                femaleButton.setEnabled(false);
                nextLeadButton.setEnabled(false);
                startStopToggleButton.setEnabled(false);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Log in Failed");
            }
            
            
            
//            new RecipientEmail(clstat).setVisible(true);
            
            
        } catch (Exception e) {
            //Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
        //maleButton.setSelected(true);
    }//GEN-LAST:event_receiveButtonMouseClicked

    private void sendButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendButtonMouseClicked
        // TODO add your handling code here:
        //jLabel2.setVisible(true);
        //jLabel1.setText("Logging in Please Wait");
        jLabel2.setText("");
        
         if(clstat.getSendDataFlag() == 2)
            {
                pfNameTextField.setEditable(true);
                ageTextField.setEditable(true);
                leadComboBox.setEnabled(true);
                maleButton.setEnabled(true);
                femaleButton.setEnabled(true);
                nextLeadButton.setEnabled(true);
                startStopToggleButton.setEnabled(true);
                remote.activateStop(true);
               
                
            }
  
        //if (myhid.FindTheHID(5824, 1503))
         if (1==1)
        {
            login.setVisible(true);
           
            clstat.sendDataFlag(3);
                    /**
             * Set Vendor ID of My device
             */
            usb.SetVendorID(VID);
            /**
             * Set Product ID of My device
             */
            usb.SetProductID(PID);
            /**
             * Obtain HID Handle of the Device(With specified VID and PID)
             */
            usb.getHIDHandle();
            
            
            System.out.println("Device found");
           
                //new PlayGround().setVisible(true);
               // obj.processLogin();
                //jabb.login("aumi.aik7@gmail.com", "220522614219");
                
                login.setVisible(true);
                //jabb.login();
                serverLogin();
               
                //remote.start();
                //remote.isRunning();
                login.setVisible(false);
                
                if(clstat.isLoggegIn())
                {
                    RemoteUserID email = new RemoteUserID(clstat);
                    //jabb.login();
                    login.setVisible(false);
                    jLabel2.setText("Logged in");
                    jLabel2.setForeground(Color.blue);
                    email.jLabel2.setText("Enter Recipient's gMailID");
                    email.setVisible(true);
                //new RecipientEmail(clstat).setVisible(true);
                    clstat.setSendOrReceive(2);
                }
                else
                {
                     JOptionPane.showMessageDialog(null, "Log in Failed");
                }
                //remote.start();
            
        }
        else
        {
            JOptionPane pane = new JOptionPane( "Device not found", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = pane.createDialog("Error");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
            
            buttonGroup3.clearSelection();
        }
    }//GEN-LAST:event_sendButtonMouseClicked

    private void leadComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leadComboBoxActionPerformed
        // TODO add your handling code here:
        
         this.clstat.setSelectedLead((String)this.leadComboBox.getSelectedItem());
    int j = this.leadComboBox.getSelectedIndex();
    short i = 9;
    long current;
    long future;
    switch (j)
    {
    case 1: 
      this.selectLead[1] = 1;
      this.jTabbedPane1.setSelectedIndex(0);
      this.usb.SetFeatureReport(this.selectLead, i);
      this.iRadioButton.setSelected(true);
      
      this.read.activateStop(true);
      this.write.activateStop(true);
      
      this.ld5.reset();
      if (("".equals(this.clstat.getVerticalScalling())) || (("10 mm/mV".equals(this.clstat.getVerticalScalling())) && ("5 mm/mV".equals(this.gainLabel.getText())))) {
        tenmmButtonMenuItem3ActionPerformed(evt);
      }
      current = System.currentTimeMillis();
      


      future = current + 700L;
      while (System.currentTimeMillis() < future) {}
      this.read.activateStop(false);
      this.write.activateStop(false);
      


      break;
    case 2: 
      this.selectLead[1] = 2;
      this.jTabbedPane1.setSelectedIndex(0);
      this.usb.SetFeatureReport(this.selectLead, i);
      this.eiiRadioButton.setSelected(true);
      
      this.read.activateStop(true);
      this.write.activateStop(true);
      this.ld5.reset();
      if (("".equals(this.clstat.getVerticalScalling())) || (("10 mm/mV".equals(this.clstat.getVerticalScalling())) && ("5 mm/mV".equals(this.gainLabel.getText())))) {
        tenmmButtonMenuItem3ActionPerformed(evt);
      }
      current = System.currentTimeMillis();
      


      future = current + 700L;
      while (System.currentTimeMillis() < future) {}
      this.read.activateStop(false);
      this.write.activateStop(false);
      

      break;
    case 3: 
      this.selectLead[1] = 3;
      this.jTabbedPane1.setSelectedIndex(0);
      this.usb.SetFeatureReport(this.selectLead, i);
      this.iiiRadioButton.setSelected(true);
      
      this.read.activateStop(true);
      this.write.activateStop(true);
      this.ld5.reset();
      if (("".equals(this.clstat.getVerticalScalling())) || (("10 mm/mV".equals(this.clstat.getVerticalScalling())) && ("5 mm/mV".equals(this.gainLabel.getText())))) {
        tenmmButtonMenuItem3ActionPerformed(evt);
      }
      current = System.currentTimeMillis();
      


      future = current + 700L;
      while (System.currentTimeMillis() < future) {}
      this.read.activateStop(false);
      this.write.activateStop(false);
      
      break;
    case 4: 
      this.selectLead[1] = 4;
      this.jTabbedPane1.setSelectedIndex(1);
      this.usb.SetFeatureReport(this.selectLead, i);
      this.avrRadioButton.setSelected(true);
      

      this.read.activateStop(true);
      this.write.activateStop(true);
      this.ld5.reset();
      if (("".equals(this.clstat.getVerticalScalling())) || (("10 mm/mV".equals(this.clstat.getVerticalScalling())) && ("5 mm/mV".equals(this.gainLabel.getText())))) {
        tenmmButtonMenuItem3ActionPerformed(evt);
      }
      current = System.currentTimeMillis();
      


      future = current + 700L;
      while (System.currentTimeMillis() < future) {}
      this.read.activateStop(false);
      this.write.activateStop(false);
      break;
    case 5: 
      this.selectLead[1] = 5;
      this.jTabbedPane1.setSelectedIndex(1);
      this.usb.SetFeatureReport(this.selectLead, i);
      this.avlRadioButton.setSelected(true);
      

      this.read.activateStop(true);
      this.write.activateStop(true);
      this.ld5.reset();
      if (("".equals(this.clstat.getVerticalScalling())) || (("10 mm/mV".equals(this.clstat.getVerticalScalling())) && ("5 mm/mV".equals(this.gainLabel.getText())))) {
        tenmmButtonMenuItem3ActionPerformed(evt);
      }
      current = System.currentTimeMillis();
      


      future = current + 700L;
      while (System.currentTimeMillis() < future) {}
      this.read.activateStop(false);
      this.write.activateStop(false);
      break;
    case 6: 
      this.selectLead[1] = 6;
      this.jTabbedPane1.setSelectedIndex(1);
      this.usb.SetFeatureReport(this.selectLead, i);
      this.avfRadioButton.setSelected(true);
      

      this.read.activateStop(true);
      this.write.activateStop(true);
      this.ld5.reset();
      if (("".equals(this.clstat.getVerticalScalling())) || (("10 mm/mV".equals(this.clstat.getVerticalScalling())) && ("5 mm/mV".equals(this.gainLabel.getText())))) {
        tenmmButtonMenuItem3ActionPerformed(evt);
      }
      current = System.currentTimeMillis();
      


      future = current + 700L;
      while (System.currentTimeMillis() < future) {}
      this.read.activateStop(false);
      this.write.activateStop(false);
      break;
    case 7: 
      this.selectLead[1] = 7;
      this.jTabbedPane1.setSelectedIndex(2);
      this.usb.SetFeatureReport(this.selectLead, i);
      this.v1RadioButton.setSelected(true);
      

      this.read.activateStop(true);
      this.write.activateStop(true);
      this.ld5.reset();
      if (("".equals(this.clstat.getVerticalScallingV())) || (("5 mm/mV".equals(this.clstat.getVerticalScallingV())) && ("10 mm/mV".equals(this.gainLabel.getText())))) {
        fivemmButtonMenuItemActionPerformed(evt);
      }
      current = System.currentTimeMillis();
      


      future = current + 700L;
      while (System.currentTimeMillis() < future) {}
      this.read.activateStop(false);
      this.write.activateStop(false);
      break;
    case 8: 
      this.selectLead[1] = 8;
      this.jTabbedPane1.setSelectedIndex(2);
      this.usb.SetFeatureReport(this.selectLead, i);
      this.v2RadioButton.setSelected(true);
      
      this.read.activateStop(true);
      this.write.activateStop(true);
      this.ld5.reset();
      if (("".equals(this.clstat.getVerticalScallingV())) || (("5 mm/mV".equals(this.clstat.getVerticalScallingV())) && ("10 mm/mV".equals(this.gainLabel.getText())))) {
        fivemmButtonMenuItemActionPerformed(evt);
      }
      current = System.currentTimeMillis();
      


      future = current + 700L;
      while (System.currentTimeMillis() < future) {}
      this.read.activateStop(false);
      this.write.activateStop(false);
      break;
    case 9: 
      this.selectLead[1] = 9;
      this.jTabbedPane1.setSelectedIndex(2);
      this.usb.SetFeatureReport(this.selectLead, i);
      this.v3RadioButton.setSelected(true);
      
      this.read.activateStop(true);
      this.write.activateStop(true);
      this.ld5.reset();
      if (("".equals(this.clstat.getVerticalScallingV())) || (("5 mm/mV".equals(this.clstat.getVerticalScallingV())) && ("10 mm/mV".equals(this.gainLabel.getText())))) {
        fivemmButtonMenuItemActionPerformed(evt);
      }
      current = System.currentTimeMillis();
      


      future = current + 700L;
      while (System.currentTimeMillis() < future) {}
      this.read.activateStop(false);
      this.write.activateStop(false);
      break;
    case 10: 
      this.selectLead[1] = 10;
      this.jTabbedPane1.setSelectedIndex(3);
      this.usb.SetFeatureReport(this.selectLead, i);
      this.v4RadioButton.setSelected(true);
      
      this.read.activateStop(true);
      this.write.activateStop(true);
      this.ld5.reset();
      if (("".equals(this.clstat.getVerticalScallingV())) || (("5 mm/mV".equals(this.clstat.getVerticalScallingV())) && ("10 mm/mV".equals(this.gainLabel.getText())))) {
        fivemmButtonMenuItemActionPerformed(evt);
      }
      current = System.currentTimeMillis();
      


      future = current + 700L;
      while (System.currentTimeMillis() < future) {}
      this.read.activateStop(false);
      this.write.activateStop(false);
      break;
    case 11: 
      this.selectLead[1] = 11;
      this.jTabbedPane1.setSelectedIndex(3);
      this.usb.SetFeatureReport(this.selectLead, i);
      this.v5RadioButton.setSelected(true);
      
      this.read.activateStop(true);
      this.write.activateStop(true);
      this.ld5.reset();
      if (("".equals(this.clstat.getVerticalScallingV())) || (("5 mm/mV".equals(this.clstat.getVerticalScallingV())) && ("10 mm/mV".equals(this.gainLabel.getText())))) {
        fivemmButtonMenuItemActionPerformed(evt);
      }
      current = System.currentTimeMillis();
      


      future = current + 700L;
      while (System.currentTimeMillis() < future) {}
      this.read.activateStop(false);
      this.write.activateStop(false);
      break;
    case 12: 
      this.selectLead[1] = 12;
      this.jTabbedPane1.setSelectedIndex(3);
      this.usb.SetFeatureReport(this.selectLead, i);
      this.v6RadioButton.setSelected(true);
      
      this.read.activateStop(true);
      this.write.activateStop(true);
      this.ld5.reset();
      if (("".equals(this.clstat.getVerticalScallingV())) || (("5 mm/mV".equals(this.clstat.getVerticalScallingV())) && ("10 mm/mV".equals(this.gainLabel.getText())))) {
        fivemmButtonMenuItemActionPerformed(evt);
      }
      current = System.currentTimeMillis();
      


      future = current + 700L;
      while (System.currentTimeMillis() < future) {}
      this.read.activateStop(false);
      this.write.activateStop(false);
      break;
    case 13: 
      this.selectLead[1] = 2;
      this.jTabbedPane1.setSelectedIndex(4);
      this.usb.SetFeatureReport(this.selectLead, i);
      this.eiiRadioButton.setSelected(true);
      
      this.read.activateStop(true);
      this.write.activateStop(true);
      this.ld5.reset();
      if ("10 mm/mV".equals(this.clstat.getVerticalScalling())) {
        tenmmButtonMenuItem3ActionPerformed(evt);
          System.out.println("A"+this.clstat.getVerticalScalling());
      } else {
        fivemmButtonMenuItemActionPerformed(evt);
        System.out.println("B"+this.clstat.getVerticalScalling());
      }
      current = System.currentTimeMillis();
      future = current + 700L;
      while (System.currentTimeMillis() < future) {}
      this.read.activateStop(false);
      this.write.activateStop(false);
    }

    }//GEN-LAST:event_leadComboBoxActionPerformed

    private void nextLeadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextLeadButtonActionPerformed
        // TODO add your handling code here:
        short i = 9;
        int j = leadComboBox.getSelectedIndex();
        if(j == 13)
        {  
            j = 1;
            leadComboBox.setSelectedIndex(j);
           // leadComboBoxActionPerformed(evt);
            
            
        }
        else
        {
            j += 1;
            leadComboBox.setSelectedIndex(j);
           // leadComboBoxActionPerformed(evt);
            
        }
        
        
        //clstat.setSelectedLead((String)leadComboBox.getSelectedItem());
        //usb.SetFeatureReport(selectLead, i);
        
    }//GEN-LAST:event_nextLeadButtonActionPerformed

    private void jScrollPane1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseMoved
        // TODO add your handling code here:
        jScrollPane1.revalidate();
}//GEN-LAST:event_jScrollPane1MouseMoved

    private void jScrollPane1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseDragged
        // TODO add your handling code here:
        jScrollPane1.revalidate();
}//GEN-LAST:event_jScrollPane1MouseDragged

    private void jScrollPane1MouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jScrollPane1MouseWheelMoved
        // TODO add your handling code here:
        jScrollPane1.revalidate();
}//GEN-LAST:event_jScrollPane1MouseWheelMoved

    private void iRadioButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iRadioButtonMouseClicked
        // TODO add your handling code here:
        leadComboBox.setSelectedIndex(1);
    }//GEN-LAST:event_iRadioButtonMouseClicked

    private void iiRadioButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iiRadioButtonMouseClicked
        // TODO add your handling code here:
        leadComboBox.setSelectedIndex(2);
    }//GEN-LAST:event_iiRadioButtonMouseClicked

    private void iiiRadioButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iiiRadioButtonMouseClicked
        // TODO add your handling code here:
        leadComboBox.setSelectedIndex(3);
        
    }//GEN-LAST:event_iiiRadioButtonMouseClicked

    private void avrRadioButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_avrRadioButtonMouseClicked
        // TODO add your handling code here:
        leadComboBox.setSelectedIndex(4);
    }//GEN-LAST:event_avrRadioButtonMouseClicked

    private void avlRadioButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_avlRadioButtonMouseClicked
        // TODO add your handling code here:
        leadComboBox.setSelectedIndex(5);
    }//GEN-LAST:event_avlRadioButtonMouseClicked

    private void avfRadioButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_avfRadioButtonMouseClicked
        // TODO add your handling code here:
        leadComboBox.setSelectedIndex(6);
    }//GEN-LAST:event_avfRadioButtonMouseClicked

    private void v1RadioButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_v1RadioButtonMouseClicked
        // TODO add your handling code here:
        leadComboBox.setSelectedIndex(7);
    }//GEN-LAST:event_v1RadioButtonMouseClicked

    private void v2RadioButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_v2RadioButtonMouseClicked
        // TODO add your handling code here:
        leadComboBox.setSelectedIndex(8);
    }//GEN-LAST:event_v2RadioButtonMouseClicked

    private void v3RadioButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_v3RadioButtonMouseClicked
        // TODO add your handling code here:
        leadComboBox.setSelectedIndex(9);
    }//GEN-LAST:event_v3RadioButtonMouseClicked

    private void v4RadioButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_v4RadioButtonMouseClicked
        // TODO add your handling code here:
        leadComboBox.setSelectedIndex(10);
    }//GEN-LAST:event_v4RadioButtonMouseClicked

    private void v6RadioButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_v6RadioButtonMouseClicked
        // TODO add your handling code here:
        leadComboBox.setSelectedIndex(12);
    }//GEN-LAST:event_v6RadioButtonMouseClicked

    private void femaleButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_femaleButtonMouseClicked
        // TODO add your handling code here:
        clstat.setSex("Female");
}//GEN-LAST:event_femaleButtonMouseClicked

    private void maleButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_maleButtonMouseClicked
        // TODO add your handling code here:
        clstat.setSex("Male");
}//GEN-LAST:event_maleButtonMouseClicked

    private void maleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maleButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_maleButtonActionPerformed

    private void pfNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pfNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pfNameTextFieldActionPerformed

    private void ageTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ageTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ageTextFieldActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        // TODO add your handling code here:
        int index = jTabbedPane1.getSelectedIndex();
        if(index == 0)
        {
            iRadioButton.setVisible(true);
            iiRadioButton.setVisible(true);
            iiiRadioButton.setVisible(true);
            
            avrRadioButton.setVisible(false);
            avlRadioButton.setVisible(false);
            avfRadioButton.setVisible(false);
             
            v1RadioButton.setVisible(false);
            v2RadioButton.setVisible(false);
            v3RadioButton.setVisible(false);
            
            v4RadioButton.setVisible(false);
            v5RadioButton.setVisible(false);
            v6RadioButton.setVisible(false);
            
            eiiRadioButton.setVisible(false);
        }
        
        if(index == 1)
        {
            iRadioButton.setVisible(false);
            iiRadioButton.setVisible(false);
            iiiRadioButton.setVisible(false);
            
            avrRadioButton.setVisible(true);
            avlRadioButton.setVisible(true);
            avfRadioButton.setVisible(true);
             
            v1RadioButton.setVisible(false);
            v2RadioButton.setVisible(false);
            v3RadioButton.setVisible(false);
            
            v4RadioButton.setVisible(false);
            v5RadioButton.setVisible(false);
            v6RadioButton.setVisible(false);
            
            eiiRadioButton.setVisible(false);
        }
        
         if(index == 2)
        {
            iRadioButton.setVisible(false);
            iiRadioButton.setVisible(false);
            iiiRadioButton.setVisible(false);
            
            avrRadioButton.setVisible(false);
            avlRadioButton.setVisible(false);
            avfRadioButton.setVisible(false);
             
            v1RadioButton.setVisible(true);
            v2RadioButton.setVisible(true);
            v3RadioButton.setVisible(true);
            
            v4RadioButton.setVisible(false);
            v5RadioButton.setVisible(false);
            v6RadioButton.setVisible(false);
            
            eiiRadioButton.setVisible(false);
        }
         
          if(index == 3)
        {
            iRadioButton.setVisible(false);
            iiRadioButton.setVisible(false);
            iiiRadioButton.setVisible(false);
            
            avrRadioButton.setVisible(false);
            avlRadioButton.setVisible(false);
            avfRadioButton.setVisible(false);
             
             v1RadioButton.setVisible(false);
            v2RadioButton.setVisible(false);
            v3RadioButton.setVisible(false);
            
            v4RadioButton.setVisible(true);
            v5RadioButton.setVisible(true);
            v6RadioButton.setVisible(true);
            
            eiiRadioButton.setVisible(false);
        }
          
           if(index == 4)
        {
            iRadioButton.setVisible(false);
            iiRadioButton.setVisible(false);
            iiiRadioButton.setVisible(false);
            
            avrRadioButton.setVisible(false);
            avlRadioButton.setVisible(false);
            avfRadioButton.setVisible(false);
             
             v1RadioButton.setVisible(false);
            v2RadioButton.setVisible(false);
            v3RadioButton.setVisible(false);
            
            v4RadioButton.setVisible(false);
            v5RadioButton.setVisible(false);
            v6RadioButton.setVisible(false);
            
            eiiRadioButton.setVisible(true);
        }
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        this.dispose();
       
            
        //new OpenDisplay().setVisible(true);
        idoutputStream = null;
        InStream = null;
        read.killThread();
        write.killThread();
        //this = null;
        read = null;
        write = null;
        myhid.CloseHandles();
        usb.CloseHIDDevice();
        ecg.clearBuffer();
        sw=null;
        sync = null;
        myhid=null;
        usb=null;
        clstat=null;
        ld5=null;
        ecgr=null;
        ld1=null; ld2=null; ld3=null; ld4=null;lde2=null;
        ecg=null;
        disp = null;
        loginThred = null;
        
         //email = null;
         //jabb.disconnect();
         jabb = null; 
         login = null;
         remote.activateStop(true);
         remote = null;
        try {
            new EcgDisplay().setVisible(true);
        } catch (UnknownHostException ex) {
            Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void fivemmButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fivemmButtonMenuItemActionPerformed
          this.fivemmButtonMenuItem.setSelected(true);
          short i = 9;
          this.gainFactor[1] = -69;
          this.usb.SetFeatureReport(this.gainFactor, i);
          this.clstat.setGain(0.84F);
          this.gainLabel.setText("5 mm/mV");
          if ((this.leadComboBox.getSelectedIndex() <= 6) || (this.leadComboBox.getSelectedIndex() == 13)) {
            this.clstat.setVerticalScaling("5 mm/mV");
          } else {
            this.clstat.setVerticalScalingV("5 mm/mV");
          }
          this.gainF = 1;
          try
          {
            this.flagoutputStream = new FileWriter("./Info/flags.txt");

            String flags = this.gainF + "\n" + this.horScallingF + "\n" + this.filterF;

            this.flagoutputStream.write(flags);
            this.flagoutputStream.flush();
            this.flagoutputStream.close();
          }
          catch (IOException ex)
          {
            Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
          }
    }//GEN-LAST:event_fivemmButtonMenuItemActionPerformed

    private void tenmmButtonMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tenmmButtonMenuItem3ActionPerformed
        // TODO add your handling code here:
        
        this.tenmmButtonMenuItem3.setSelected(true);
        short i = 9;
        this.gainFactor[1] = -86;
        this.usb.SetFeatureReport(this.gainFactor, i);
        this.clstat.setGain(0.9F);
        this.gainLabel.setText("10 mm/mV");
        if ((this.leadComboBox.getSelectedIndex() <= 6) || (this.leadComboBox.getSelectedIndex() == 13)) {
          this.clstat.setVerticalScaling("10 mm/mV");
        } else {
          this.clstat.setVerticalScalingV("10 mm/mV");
        }
        this.gainF = 2;
        try
        {
          this.flagoutputStream = new FileWriter("./Info/flags.txt");

          String flags = this.gainF + "\n" + this.horScallingF + "\n" + this.filterF;

          this.flagoutputStream.write(flags);
          this.flagoutputStream.flush();
          this.flagoutputStream.close();
        }
        catch (IOException ex)
        {
          Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_tenmmButtonMenuItem3ActionPerformed

    private void FilterCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FilterCheckBoxMenuItemActionPerformed
        // TODO add your handling code here:
        if(FilterCheckBoxMenuItem.isSelected())
        {
            clstat.setFilterFlag(true);
            filterStatusLabel.setText("ON");
            
            filterF = 1;
            try {
              flagoutputStream = new FileWriter("./Info/flags.txt");

              String flags = gainF+"\n"+horScallingF+"\n"+filterF;

              flagoutputStream.write(flags);
              flagoutputStream.flush();
              flagoutputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        else if(!FilterCheckBoxMenuItem.isSelected())
        {
            clstat.setFilterFlag(false);
            filterStatusLabel.setText("OFF");
            
            filterF = 0;
            try {
              flagoutputStream = new FileWriter("./Info/flags.txt");

              String flags = gainF+"\n"+horScallingF+"\n"+filterF;

              flagoutputStream.write(flags);
              flagoutputStream.flush();
              flagoutputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_FilterCheckBoxMenuItemActionPerformed

    private void jPanel13KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanel13KeyPressed
        // TODO add your handling code here:
         this.setVisible(false);
        this.dispose();
        //new OpenDisplay().setVisible(true);
        read.activateStop(true);
        write.activateStop(true);
        //this = null;
        read = null;
        write = null;
        myhid.CloseHandles();
        usb.CloseHIDDevice();
        ecg.clearBuffer();
        sw=null;
        sync = null;
        myhid=null;
        usb=null;
        clstat=null;
        ld5=null;
        ecgr=null;
        ld1=null; ld2=null; ld3=null; ld4=null;lde2=null;
        ecg=null;
        disp = null;
        try {
            new EcgDisplay().setVisible(true);
        } catch (UnknownHostException ex) {
            Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jPanel13KeyPressed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        new ReportGeneration().setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        new SendMailFrame().setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void two_fiveRadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_fiveRadioButtonMenuItemActionPerformed
        // TODO add your handling code here:
        
        clstat.sethorScalling(3);
        mmPerSecLabel.setText("25 mm/sec");
        
        horScallingF = 3;
        try {
            flagoutputStream = new FileWriter("./Info/flags.txt");
            
            String flags = gainF+"\n"+horScallingF+"\n"+filterF;
            
            flagoutputStream.write(flags);
            flagoutputStream.flush();
            flagoutputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_two_fiveRadioButtonMenuItemActionPerformed

    private void fiftyRadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fiftyRadioButtonMenuItemActionPerformed
        // TODO add your handling code here:
         clstat.sethorScalling(4);
        mmPerSecLabel.setText("50 mm/sec");
        
        horScallingF = 4;
        try {
            flagoutputStream = new FileWriter("./Info/flags.txt");
            
            String flags = gainF+"\n"+horScallingF+"\n"+filterF;
            
            flagoutputStream.write(flags);
            flagoutputStream.flush();
            flagoutputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_fiftyRadioButtonMenuItemActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void v5RadioButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_v5RadioButtonMouseClicked
        // TODO add your handling code here:
        leadComboBox.setSelectedIndex(11);
    }//GEN-LAST:event_v5RadioButtonMouseClicked

    private void twentyRadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_twentyRadioButtonMenuItemActionPerformed
        // TODO add your handling code here:
        clstat.sethorScalling(2);
        mmPerSecLabel.setText("20 mm/sec");
          
        horScallingF = 2;
        try {
            flagoutputStream = new FileWriter("./Info/flags.txt");
            
            String flags = gainF+"\n"+horScallingF+"\n"+filterF;
            
            flagoutputStream.write(flags);
            flagoutputStream.flush();
            flagoutputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_twentyRadioButtonMenuItemActionPerformed

    private void fifteenRadioButtonMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fifteenRadioButtonMenuItem1ActionPerformed
        // TODO add your handling code here:
        clstat.sethorScalling(1);
        mmPerSecLabel.setText("15 mm/sec");
        
         horScallingF = 1;
        try {
            flagoutputStream = new FileWriter("./Info/flags.txt");
            
            String flags = gainF+"\n"+horScallingF+"\n"+filterF;
            
            flagoutputStream.write(flags);
            flagoutputStream.flush();
            flagoutputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_fifteenRadioButtonMenuItem1ActionPerformed

    private void startStopToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startStopToggleButtonActionPerformed
        // TODO add your handling code here:
        if(clstat.getSendDataFlag() == 1 || clstat.getSendDataFlag() == 3)
        {
            if(startStopToggleButton.isSelected())
            {
    //            JOptionPane.showMessageDialog(null, "OK");

                startStopToggleButton.setText("STOP");
                if(flag) {
                //            if(!pfNameTextField.getText().isEmpty() && !plNameTextField.getText().isEmpty()  && leadComboBox.getSelectedItem() != "Select lead" && (maleButton.isSelected() | femaleButton.isSelected()) && !jTextField1.getText().isEmpty())
                //            {






               // read.start();
               // write.start();
                flag = false;

                clstat.setPatientName(pfNameTextField.getText());//, plNameTextField.getText());
                clstat.setAge(ageTextField.getText());

                if(maleButton.isSelected())
                    clstat.setSex("Male");
                else if (femaleButton.isSelected())
                    clstat.setSex("Female");

                clstat.setUpazila(upazilaLabel.getText());
                clstat.setPatientId(patientIdTextField.getText());
                
                Thread updateInfo = new Thread(new TransmissionEntry(clstat, 2));
                updateInfo.start();

                //            }
                //                else
                //            JOptionPane.showMessageDialog(null, "Check Patient's Name text box and Select Lead");



            } 

            else 
            {
    //            read.resume();
    //            write.resume();
                read.activateStop(false);
                write.activateStop(false);

                clstat.setPatientName(pfNameTextField.getText());//, plNameTextField.getText());
                clstat.setAge(ageTextField.getText());

                if(maleButton.isSelected())
                    clstat.setSex("Male");
                else
                    clstat.setSex("Female");

                clstat.setUpazila(upazilaLabel.getText());
                clstat.setPatientId(patientIdTextField.getText());
            }
                ecg.startstop(true);
            }
            else
            {
                //JOptionPane.showMessageDialog(null, "OK");
                startStopToggleButton.setText("START");

    //            read.suspend();
    //            read.suspend();
                read.activateStop(true);
                write.activateStop(true);


            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Select Data Acquizition Option to start ");
            startStopToggleButton.setSelected(false);
            
        }
    }//GEN-LAST:event_startStopToggleButtonActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        try {
            // TODO add your handling code here:

            new OrgName().setVisible(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        try {
            // TODO add your handling code here:

            new gmailIDPass().setVisible(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void eiiRadioButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eiiRadioButtonMouseClicked
        // TODO add your handling code here:
        leadComboBox.setSelectedIndex(12);
    }//GEN-LAST:event_eiiRadioButtonMouseClicked

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        try {
            // TODO add your handling code here:
            new AddUser().setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        this.setFocusableWindowState(true);
    }//GEN-LAST:event_formMouseClicked
/**/
    
    public void addAtachments(String[] attachments, Multipart multipart)
                 throws MessagingException, AddressException
 {
     for(int i = 0; i<= attachments.length -1; i++)
     {
         String filename = attachments[i];
         MimeBodyPart attachmentBodyPart = new MimeBodyPart();

         //use a JAF FileDataSource as it does MIME type detection
         DataSource source = new FileDataSource(filename);
         attachmentBodyPart.setDataHandler(new DataHandler(source));

         //assume that the filename you want to send is the same as the
         //actual file name - could alter this to remove the file path
         attachmentBodyPart.setFileName(filename);

         //add the attachment
         multipart.addBodyPart(attachmentBodyPart);
     }
 }
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new EcgDisplay().setVisible(true);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(EcgDisplay.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu FileMenu;
    private javax.swing.JCheckBoxMenuItem FilterCheckBoxMenuItem;
    private javax.swing.JMenu GainMenu;
    private javax.swing.JRadioButton acquirebutton;
    private javax.swing.JLabel ageLabel;
    public javax.swing.JTextField ageTextField;
    public javax.swing.JRadioButton avfRadioButton;
    public javax.swing.JRadioButton avlRadioButton;
    public javax.swing.JRadioButton avrRadioButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.ButtonGroup buttonGroup6;
    private javax.swing.ButtonGroup buttonGroup7;
    private javax.swing.ButtonGroup buttonGroup8;
    public javax.swing.JRadioButton eiiRadioButton;
    private javax.swing.JPanel extended2Panel;
    public javax.swing.JRadioButton femaleButton;
    private javax.swing.JRadioButtonMenuItem fifteenRadioButtonMenuItem1;
    private javax.swing.JRadioButtonMenuItem fiftyRadioButtonMenuItem;
    private javax.swing.JLabel filterLabel;
    public javax.swing.JLabel filterStatusLabel;
    private javax.swing.JRadioButtonMenuItem fivemmButtonMenuItem;
    public javax.swing.JLabel gainLabel;
    public javax.swing.JRadioButton iRadioButton;
    private javax.swing.JLabel idLabel;
    public javax.swing.JRadioButton iiRadioButton;
    public javax.swing.JRadioButton iiiRadioButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    public javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    public javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JPanel ld1Panel;
    public javax.swing.JPanel ld1Panel1;
    public javax.swing.JPanel ld2Panel;
    public javax.swing.JPanel ld3Panel;
    public javax.swing.JPanel ld4Panel;
    public javax.swing.JComboBox leadComboBox;
    public javax.swing.JRadioButton maleButton;
    public javax.swing.JLabel mmPerSecLabel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JButton nextLeadButton;
    public javax.swing.JTextField patientIdTextField;
    public javax.swing.JTextField pfNameTextField;
    private javax.swing.JPanel realtimePanel;
    private javax.swing.JPanel realtimePanel2;
    private javax.swing.JRadioButton receiveButton;
    private javax.swing.JButton reportButton;
    private javax.swing.JRadioButton sendButton;
    private javax.swing.JLabel sexLabel;
    private javax.swing.JToggleButton startStopToggleButton;
    private javax.swing.JRadioButtonMenuItem tenmmButtonMenuItem3;
    private javax.swing.JRadioButtonMenuItem twentyRadioButtonMenuItem;
    private javax.swing.JRadioButtonMenuItem two_fiveRadioButtonMenuItem;
    public javax.swing.JLabel upazilaLabel;
    public javax.swing.JRadioButton v1RadioButton;
    public javax.swing.JRadioButton v2RadioButton;
    public javax.swing.JRadioButton v3RadioButton;
    public javax.swing.JRadioButton v4RadioButton;
    public javax.swing.JRadioButton v5RadioButton;
    public javax.swing.JRadioButton v6RadioButton;
    // End of variables declaration//GEN-END:variables

    

    private class SMTPAuthenticator extends javax.mail.Authenticator
    {
    public PasswordAuthentication getPasswordAuthentication()
    {
    return new PasswordAuthentication(d_email, d_password);
    }
    }
}


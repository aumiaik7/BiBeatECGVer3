import java.io.PrintStream;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

public class ClientStat
{
  private static boolean stat = false;
  private static boolean checkBox = false;
  private static String IP;
  private String fName = "Not Given";
  private String lName;
  private String leadNo;
  private String sex = "Not selected";
  private String age = "Not Given";
  private static int[] lead1 = new int[512];
  private static int[] lead2 = new int[512];
  private static int[] lead3 = new int[512];
  private static int[] leadaVR = new int[512];
  private static int[] leadaVL = new int[512];
  private static int[] leadaVF = new int[512];
  private static int[] leadV1 = new int[512];
  private static int[] leadV2 = new int[512];
  private static int[] leadV3 = new int[512];
  private static int[] leadV4 = new int[512];
  private static int[] leadV5 = new int[512];
  private static int[] leadV6 = new int[512];
  static int[] leadE21 = new int[512];
  static int[] leadE22 = new int[512];
  private int sendDataFalag = 0;
  private float gain = 0.9F;
  private String upazila;
  private String pId;
  private String scaling = "";
  private String scalingV = "";
  private String remoteUserID = "";
  private String userID = "";
  private int sendorReceive = 0;
  private int horizontalScalingFlag = 3;
  private boolean dataFlag = true;
  private boolean filterFlag = false;
  EcgDisplay ecgDisplay;
  DefaultMutableTreeNode node;
  LeadDisplay1 ld1;
  LeadDisplay2 ld2;
  LeadDisplay3 ld3;
  LeadDisplay4 ld4;
  LeadDisplayExtended2 lde2; 
  private String ip = "";
  private String transmissionID = "";
  
  private boolean loginstatus = false;
  
  public void setTransmissionID(String tid)
  {
      this.transmissionID = tid;
      
  }
  public String getTransmissionID()
  {
      return this.transmissionID;
  }
  
  public void setIP(String ip)
  {
      this.ip = ip;
  }
  public String getIP()
  {
      return this.ip;
  }
  
  public void setIsLoggedIn(boolean flag)
  {
      this.loginstatus = flag;
  }
  public boolean isLoggegIn()
  {
      return this.loginstatus;
  }
  
  public void setVerticalScaling(String scl)
  {
    this.scaling = scl;
  }
  
  public String getVerticalScalling()
  {
    return this.scaling;
  }
  
  public void setVerticalScalingV(String scl)
  {
    this.scalingV = scl;
  }
  
  public String getVerticalScallingV()
  {
    return this.scalingV;
  }
  
  public void sethorScalling(int fl)
  {
    this.horizontalScalingFlag = fl;
  }
  
  public int gethorScalling()
  {
    return this.horizontalScalingFlag;
  }
  
  public void setUpazila(String up)
  {
    this.upazila = up;
  }
  
  public String getUpazila()
  {
    return this.upazila;
  }
  
  public void setPatientId(String id)
  {
    this.pId = id;
  }
  
  public String getPatientId()
  {
    return this.pId;
  }
  
  public void setSendOrReceive(int a)
  {
    this.sendorReceive = a;
  }
  
  public int getSendOrReceive()
  {
    return this.sendorReceive;
  }
  
  public void setRemoteUserID(String id)
  {
    this.remoteUserID = id;
  }
  
  public String getRemoteUserID()
  {
    return this.remoteUserID;
  }
  
  public void setUserID(String id)
  {
    this.userID = id;
  }
  
  public String getUserID()
  {
    return this.userID;
  }
  
  public void setEcgDisplay(EcgDisplay ed)
  {
    this.ecgDisplay = ed;
  }
  
  public EcgDisplay getEcgDisplay()
  {
    return this.ecgDisplay;
  }
  
  public void setclientStat(boolean flag)
  {
    stat = flag;
  }
  
  public boolean getClientStat()
  {
    return stat;
  }
  
  public void setIpAddress(String ip)
  {
    IP = ip;
  }
  
  public String getIpAddress()
  {
    return IP;
  }
  
  public void setPatientName(String fname)
  {
    this.fName = fname;
  }
  
  public String getFirstName()
  {
    return this.fName;
  }
  
  public String getLastName()
  {
    return this.lName;
  }
  
  public void setSelectedLead(String lead)
  {
    this.leadNo = lead;
  }
  
  public String getLeadNo()
  {
    return this.leadNo;
  }
  
  public void setSex(String s)
  {
    this.sex = s;
  }
  
  public String getSex()
  {
    return this.sex;
  }
  
  public void setAge(String a)
  {
    this.age = a;
  }
  
  public String getAge()
  {
    return this.age;
  }
  
  public void setCheckBox(boolean check)
  {
    checkBox = check;
  }
  
  public boolean getCheckBox()
  {
    return checkBox;
  }
  
  public void setGain(float gn)
  {
    this.gain = gn;
  }
  
  public float getGain()
  {
    return this.gain;
  }
  
  public void setLead1(int[] leadI)
  {
    lead1 = leadI;
  }
  
  public void setLead2(int[] leadII)
  {
    lead2 = leadII;
  }
  
  public void setLead3(int[] leadIII)
  {
    lead3 = leadIII;
  }
  
  public void setLeadaVR(int[] leadavr)
  {
    leadaVR = leadavr;
  }
  
  public void setLeadaVL(int[] leadavl)
  {
    leadaVL = leadavl;
  }
  
  public void setLeadaVF(int[] leadavf)
  {
    leadaVF = leadavf;
  }
  
  public void setLeadV1(int[] leadv1)
  {
    leadV1 = leadv1;
  }
  
  public void setLeadV2(int[] leadv2)
  {
    leadV2 = leadv2;
  }
  
  public void setLeadV3(int[] leadv3)
  {
    leadV3 = leadv3;
  }
  
  public void setLeadV4(int[] leadv4)
  {
    leadV4 = leadv4;
  }
  
  public void setLeadV5(int[] leadv5)
  {
    leadV5 = leadv5;
  }
  
  public void setLeadV6(int[] leadv6)
  {
    leadV6 = leadv6;
  }
  
  public void setLeadE(int[] leade1, int[] leade2)
  {
    for (int i = 0; i < 512; i++)
    {
      leadE21[i] = leade1[i];
      
      leadE22[i] = leade2[i];
    }
  }
  
  public int[] getLead1()
  {
    return lead1;
  }
  
  public int[] getLead2()
  {
    return lead2;
  }
  
  public int[] getLead3()
  {
    return lead3;
  }
  
  public int[] getLeadaVR()
  {
    return leadaVR;
  }
  
  public int[] getLeadaVL()
  {
    return leadaVL;
  }
  
  public int[] getLeadaVF()
  {
    return leadaVF;
  }
  
  public int[] getLeadV1()
  {
    return leadV1;
  }
  
  public int[] getLeadV2()
  {
    return leadV2;
  }
  
  public int[] getLeadV3()
  {
    return leadV3;
  }
  
  public int[] getLeadV4()
  {
    return leadV4;
  }
  
  public int[] getLeadV5()
  {
    return leadV5;
  }
  
  public int[] getLeadV6()
  {
    return leadV6;
  }
  
  public int[] getLeadE21()
  {
    return leadE21;
  }
  
  public int[] getLeadE22()
  {
    return leadE22;
  }
  
  public void setNode(DefaultMutableTreeNode nod)
  {
    this.node = nod;
    System.out.println("" + this.node);
    System.out.println("yappppppppppppppppppppppiiiiiiiiii" + this.node);
  }
  
  public DefaultMutableTreeNode getNode()
  {
    return this.node;
  }
  
  public void sendDataFlag(int df)
  {
    this.sendDataFalag = df;
  }
  
  public int getSendDataFlag()
  {
    return this.sendDataFalag;
  }
  
  public void leadDisplay(LeadDisplay1 l1, LeadDisplay2 l2, LeadDisplay3 l3, LeadDisplay4 l4, LeadDisplayExtended2 le2)
  {
    this.ld1 = l1;
    this.ld2 = l2;
    this.ld3 = l3;
    this.ld4 = l4;
    this.lde2 = le2;
  }
  
  public LeadDisplay1 getDisplay1Object()
  {
    return this.ld1;
  }
  
  public LeadDisplay2 getDisplay2Object()
  {
    return this.ld2;
  }
  
  public LeadDisplay3 getDisplay3Object()
  {
    return this.ld3;
  }
  
  public LeadDisplay4 getDisplay4Object()
  {
    return this.ld4;
  }
  
  public LeadDisplayExtended2 getDisplaye2Object()
  {
    return this.lde2;
  }
  
  public void dataFalg(boolean fl)
  {
    this.dataFlag = fl;
  }
  
  public boolean dataFlagStatus()
  {
    return this.dataFlag;
  }
  
  public void setFilterFlag(boolean fl2)
  {
    this.filterFlag = fl2;
  }
  
  public boolean getFilterFlag()
  {
    return this.filterFlag;
  }
}

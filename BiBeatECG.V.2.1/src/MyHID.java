
import java.io.File;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package ecgbmptversion15;

/**
 *
 * @author Aumi
 */
public class MyHID {

        public native int ReadFeatureReportSize();
	public native int ReadInputReportSize();
	public native int ReadOutputReportSize();
	public native byte[] ReadFeatureReport();
	public native byte[] IntReadInputReport();
	public native byte[] CtrlReadInputReport();
	public native boolean SendFeatureReport(byte[] featureReport);
	public native boolean IntSendOutputReport(byte[] outputReport);
	public native boolean CtrlSendOutputReport(byte[] outputReport);
	public native boolean FindTheHID(int vendorID, int productID);
	public native void CloseHandles();
	static
	{
		//System.loadLibrary("Wrapper");
            
            File file = new File("Wrapper.dll");
            String path = file.getAbsolutePath();
            
                //Runtime.getRuntime().load("C:/Program Files/Common Files/ECG_BMPT/Wrapper.dll");
            Runtime.getRuntime().load(path);
                //System.out.println("A DIDIASDUIDUIA");
	}


}

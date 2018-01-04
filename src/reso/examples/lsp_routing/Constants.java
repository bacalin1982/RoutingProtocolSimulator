package reso.examples.lsp_routing;

public class Constants {
    /* Title */
    public static final String _T = " ---------------------- \n"+
                                    "| LSP Routing Protocol |\n"+
                                    "|     B.C. &  F.J.     |\n"+
                                    " ---------------------- \n";
    /* Headers */
    public static final String _I = "INFO   : ";
    public static final String _E = "ERROR  : ";
    public static final String _W = "WARNING: ";
    public static final String _S = "       : ";

    /* Strings */
    public static String SEND(String rn, String r, String i, String t, String d){ return rn+ " "+ r + " on " + i + " SEND\t[" + t + "]\t->\t" + d; }
    public static String RECEIVE(String rn, String r, String i, String t, String d){ return rn+ " "+ r + " on " + i + " RCVE\t[" + t + "]\t<-\t" + d; }
    public static String LSDB(String rn){ return "LSDB on "+ rn;}
}

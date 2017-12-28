package reso.examples.lsp_routing;

import com.sun.istack.internal.NotNull;

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
    public static String SEND(String r, String i, String d){
        return r + " on " + i + " SEND\t\t->\t" + d;
    }
    public static String RECEIVE(String r, String i, String d){ return r + " on " + i + " RECEIVE\t<-\t" + d; }
}

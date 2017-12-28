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
    public static String SEND(String r, String i, String d){
        return r + " on " + i + " SEND -> " + d;
    }

    public static String RECEIVE(String r, String i, String d){ return r + " on " + i + " RECEIVE -> " + d; }
}

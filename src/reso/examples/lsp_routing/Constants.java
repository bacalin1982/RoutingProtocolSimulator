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

    public static String START(String p, String r){ return "Start "+p+ " for " +r; }
    public static String ETH(String i, String ip){ return "\tAdd interface "+i+" with ip "+ip; }


    public static String HELLO_LOG(String r){ return "Sending HELLO on "+r+" ...";}
    public static String HELLO_DEBUG(String r){ return "Neighbors for "+r;}
    public static String NEIGHBOUR_DEBUG(String r, String ip, String eth, Integer m){ return "\t"+r+" "+ip+" on "+eth+" with metric "+m;}

    public static String LSP_LOG(String r){ return "Sending LSP on "+r+" ...";}
    public static String LSDB_LOG(String rn){ return "LSDB on "+ rn;}
    public static String LSDB_LSP_LOG(String lsp){ return "\t"+lsp;}

    public static String SEND(String rn, String r, String i, String t, String d){ return rn+ " "+ r + " on " + i + " SEND\t[" + t + "]\t->\t" + d; }
    public static String RECEIVE(String rn, String r, String i, String t, String d){ return rn+ " "+ r + " on " + i + " RCVE\t[" + t + "]\t<-\t" + d; }

    public static String DIJK_ERR = "An error has occured when computing Dijkstra, result is null.";
}

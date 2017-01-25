import java.io.*;

/**
 * This is the class that students need to implement. The code skeleton is provided.
 * Students need to implement rtinit(), rtupdate() and linkhandler().
 * printdt() is provided to pretty print a table of the current costs for reaching
 * other nodes in the network.
 */ 
public class Node { 
  
  public static final int INFINITY = 9999;
  
  int[] lkcost;  /*The link cost between this node and other nodes*/
  int[][] costs;    /*Define distance table*/
  int nodename;               /*Name of this node*/
  
  boolean[] adjacent; /*To see which nodes are connected to who*/
  
  
  /* Class constructor */
  public Node() { 
    lkcost = new int[4];
    costs = new int[4][4]; 
    adjacent = new boolean[4];
  }
  
  
  /* students to write the following two routines, and maybe some others */
  void rtinit(int nodename, int[] initial_lkcost) { 
    this.nodename = nodename;
    this.lkcost = initial_lkcost;
    
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (i == j) {
          costs[i][j] = lkcost[i]; /*Through node and destination node are the same*/
        } else {
          costs[i][j] = INFINITY;
        }
      }
    }
    
    
    for (int i = 0; i < lkcost.length; i++) {
      if ((lkcost[i] != INFINITY) || (lkcost[i] != 0)) {
        adjacent[i] = true;
        NetworkSimulator.tolayer2(new Packet(nodename, i, lkcost));
      }
    }
    
  }
  
  
  
  void rtupdate(Packet rcvdpkt) {
    boolean changed = false; /*Checks to see if a change was made to costs*/
    int u = rcvdpkt.sourceid; /*Extracts sourid, destid, mincost from rcvdpkt*/
    int v = rcvdpkt.destid;
    int[] pmincost = rcvdpkt.mincost;
    
    if (this.nodename == v) {
      for (int i = 0; i < 4; i++) {
        costs[i][u] = pmincost[i] + lkcost[u];
      }
      
      for (int i = 0; i < 4; i++) {
        if (costs[i][u] < lkcost[i]) {
          lkcost[i] = costs[i][u];
          changed = true;
        }
      }
      
      if (changed == true) {
        for (int i = 0; i < 4; i++) {
          if (adjacent[i] == true) {
            NetworkSimulator.tolayer2(new Packet(this.nodename, i, lkcost));
          }
        }
      }
    } 
  }
  
  
  /* called when cost from the node to linkid changes from current value to newcost*/
  void linkhandler(int linkid, int newcost) {  
    costs[linkid][this.nodename] = newcost;
    boolean changed = false;
    
    lkcost[linkid] = newcost;
    for (int i = 0; i < 4; i++) {
      if (costs[linkid][i] < newcost) {
        lkcost[linkid] = costs[linkid][i];
        changed = true;
      }
    }
    
    if (changed == true) {
      for (int i = 0; i < 4; i++) {
        if (adjacent[i] == true) {
          NetworkSimulator.tolayer2(new Packet(this.nodename, i, lkcost));            
        }
      }
    }
  }    
  
  
  /* Prints the current costs to reaching other nodes in the network */
  void printdt() {
    switch(nodename) {
      
      case 0:
        System.out.printf("                via     \n");
        System.out.printf("   D0 |    1     2 \n");
        System.out.printf("  ----|-----------------\n");
        System.out.printf("     1|  %3d   %3d \n",costs[1][1], costs[1][2]);
        System.out.printf("dest 2|  %3d   %3d \n",costs[2][1], costs[2][2]);
        System.out.printf("     3|  %3d   %3d \n",costs[3][1], costs[3][2]);
        break;
      case 1:
        System.out.printf("                via     \n");
        System.out.printf("   D1 |    0     2    3 \n");
        System.out.printf("  ----|-----------------\n");
        System.out.printf("     0|  %3d   %3d   %3d\n",costs[0][0], costs[0][2],costs[0][3]);
        System.out.printf("dest 2|  %3d   %3d   %3d\n",costs[2][0], costs[2][2],costs[2][3]);
        System.out.printf("     3|  %3d   %3d   %3d\n",costs[3][0], costs[3][2],costs[3][3]);
        break;    
      case 2:
        System.out.printf("                via     \n");
        System.out.printf("   D2 |    0     1    3 \n");
        System.out.printf("  ----|-----------------\n");
        System.out.printf("     0|  %3d   %3d   %3d\n",costs[0][0], costs[0][1],costs[0][3]);
        System.out.printf("dest 1|  %3d   %3d   %3d\n",costs[1][0], costs[1][1],costs[1][3]);
        System.out.printf("     3|  %3d   %3d   %3d\n",costs[3][0], costs[3][1],costs[3][3]);
        break;
      case 3:
        System.out.printf("                via     \n");
        System.out.printf("   D3 |    1     2 \n");
        System.out.printf("  ----|-----------------\n");
        System.out.printf("     0|  %3d   %3d\n",costs[0][1],costs[0][2]);
        System.out.printf("dest 1|  %3d   %3d\n",costs[1][1],costs[1][2]);
        System.out.printf("     2|  %3d   %3d\n",costs[2][1],costs[2][2]);
        break;
    }
  }
  
}
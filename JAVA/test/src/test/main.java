package test;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StringBuilder received = new StringBuilder();
		
		received.append("@#n28");
		received.append("x0.5y-0.268555x1.y-0.238037x1.5y-0.070801x2.y-0.080322x2.5y-0.07373x3.y-0.078369x3.5y-0.069824x4.y-0.070557x4.5y-0.071777x5.y-0.078613x5.5y-0.068359x6.y-0.075195x6.5y-0.069336x7.y-0.07251x7.5y-0.071533x8.y-0.077148x8.5y-0.070068x9.y-0.071045x9.5y-0.073975x10.y-0.072998x10.5y-0.068604x11.y-0.074707x11.5y-0.071533x12.y-0.072021x12.5y-0.068604x13.y-0.074463x13.5y-0.067871x14.y-0.072998x&*\nasdf\n");
		
		int i = received.indexOf("x");
        int j = received.indexOf("y");
		int n = Integer.parseInt(received.substring(received.indexOf("n")+1, i));
		
		System.out.println("n = "+n);
		while(!received.toString().equals("")) {
		if(received.indexOf("\n") > 0) {
            String temp = received.substring(0, received.indexOf("\n"));
            i = temp.indexOf("x");
            j = temp.indexOf("y");
            while(j != -1) {
                System.out.print("x = "+Double.parseDouble(temp.substring(i+1, j)));
                i = received.indexOf("x", i+1);
                System.out.println("\ty = "+Double.parseDouble(temp.substring(j+1, i)));
                j = received.indexOf("y", j+1);
            }
            received.replace(0, received.indexOf("\n")+1, "");
        }
		}
		
		System.out.print("rem : "+received.toString());
	}

}

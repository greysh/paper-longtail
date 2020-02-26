package cn.ac.greysh.spider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FIX {

	ArrayList<String> authors = new ArrayList<String>();
	ArrayList<String> rcdc = new ArrayList<String>();

	public static void main(String[] args) throws IOException {

		FIX fix = new FIX();
		fix.loadCNKI();
		fix.loadRcDc();
		fix.compare();
	}

	public void compare() {
		for (int i = 0; i < authors.size(); i++) {
				if (authors.get(i).equals(rcdc.get(i))) {
					System.out.println("SUCCESS FIX: " + authors.get(i));
				}else {
					System.out.println("ERROR INDEX: " + i + " author:" + authors.get(i));
					break;
				}
			}
	}

	public void loadCNKI() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("data/cnki.txt"));
		String line = br.readLine();
		while (line != null) {
			String au = line.split(",")[0];
			authors.add(au);
			line = br.readLine();
		}
		br.close();
	}

	public void loadRcDc() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("data/cnki-rc-dc.txt"));
		br.readLine();
		String line = br.readLine();
		while (line != null) {
			String au = line.split(",")[1];
			rcdc.add(au);
			line = br.readLine();
		}
		br.close();
	}
}

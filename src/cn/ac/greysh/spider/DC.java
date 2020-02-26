package cn.ac.greysh.spider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DC {

	ArrayList<ItemDC> items = new ArrayList<ItemDC>();

	ArrayList<ItemAidDc> itemsAidDc = new ArrayList<ItemAidDc>();
	
	public static void main(String[] args) throws IOException {
		DC d = new DC();
		d.loadDC();
		d.count();
		d.write();

	}

	public void count() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("data/cnki-kw-diy.txt"));
		String line = br.readLine();
		while (line != null) {
			System.out.println(line);
			ItemDC item = new ItemDC();
			String kw =line.split(",")[0];
			item.keyword = kw;
			String aString = line.split(",")[1];
			String[] aids = aString.split("\\|");
			
			for(String said : aids) {
				int aid = Integer.parseInt(said);
				item.dcList.add(aid);
				ItemAidDc aidDc = new ItemAidDc();
				aidDc.aid = aid;
				for(ItemAidDc c : itemsAidDc) {
					if(c.equals(aidDc)) {
						item.count += c.dc;
					}
				}
			}
			items.add(item);
			line =br.readLine();
		}		
		br.close();
		System.out.println(items);
	}
	
	
	public void loadDC() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("data/cnki-rc-dc.txt"));
		br.readLine();//过滤表头
		String line = br.readLine();
		while (line != null) {
			Integer aid =Integer.parseInt(line.split(",")[0]);
			Integer dc = Integer.parseInt(line.split(",")[4]);
			
			ItemAidDc aidDc = new ItemAidDc();
			aidDc.aid = aid;
			if (!itemsAidDc.contains(aidDc)) {
				aidDc.dc = dc;
				itemsAidDc.add(aidDc);
			} else {
				for (int i = 0; i < itemsAidDc.size(); i++) {
					if (itemsAidDc.get(i).equals(aidDc)) {
						ItemAidDc it = itemsAidDc.get(i);
						aidDc.dc = it.dc + dc;
						itemsAidDc.set(i, it);
					}
				}
			}
			line =br.readLine();
		}		
		br.close();
	}
	
	public void write() throws IOException {
		PrintWriter pw = new PrintWriter(new File("data/cnki-kw-dc.txt"));
		for(int i=0;i<items.size();i++) {
			ItemDC item = items.get(i);
			pw.print(item.keyword + ",");
			for(int j=0;j<item.dcList.size();j++) {
				pw.print(item.dcList.get(j) +"|");
			}
			pw.print(","+item.count+"\n");
		}
		pw.close();
	}

}

class ItemAidDc{
	Integer aid ;
	Integer dc ;
	
	@Override
	public boolean equals(Object obj) {
		return aid == ((ItemAidDc)obj).aid;
	}
	@Override
	public String toString() {
		return "ItemAidDc [aid=" + aid + ", dc=" + dc + "]";
	}

}

class ItemDC {
	String keyword = "";
	ArrayList<Integer> dcList = new ArrayList<Integer>();
	int count = 0;

	@Override
	public boolean equals(Object obj) {
		return keyword.equals(((ItemDC)obj).keyword);
	}

	@Override
	public String toString() {
		return "ItemDC [keyword=" + keyword + ", dcList=" + dcList + ", count=" + count + "]";
	}

}

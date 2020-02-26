package cn.ac.greysh.spider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class RC {

	ArrayList<ItemRC> items = new ArrayList<ItemRC>();

	ArrayList<ItemAidRc> itemsAidRc = new ArrayList<ItemAidRc>();
	
	public static void main(String[] args) throws IOException {
		RC d = new RC();
		d.loadRC();
		d.count();
		d.write();

	}

	public void count() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("data/cnki-kw-diy.txt"));
		String line = br.readLine();
		while (line != null) {
			System.out.println(line);
			ItemRC item = new ItemRC();
			String kw =line.split(",")[0];
			item.keyword = kw;
			String aString = line.split(",")[1];
			String[] aids = aString.split("\\|");
			
			for(String said : aids) {
				int aid = Integer.parseInt(said);
				item.rcList.add(aid);
				ItemAidRc aidDc = new ItemAidRc();
				aidDc.aid = aid;
				for(ItemAidRc c : itemsAidRc) {
					if(c.equals(aidDc)) {
						item.count += c.rc;
					}
				}
			}
			items.add(item);
			line =br.readLine();
		}		
		br.close();
		System.out.println(items);
	}
	
	
	public void loadRC() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("data/cnki-rc-dc.txt"));
		br.readLine();//过滤表头
		String line = br.readLine();
		while (line != null) {
			System.out.println(line);
			Integer aid =Integer.parseInt(line.split(",")[0]);
			String rcString = line.split(",")[3];
			if(rcString.equals("")) {
				rcString = "0";
			}
			Integer rc = Integer.parseInt(rcString);
			
			ItemAidRc aidRc = new ItemAidRc();
			aidRc.aid = aid;
			if (!itemsAidRc.contains(aidRc)) {
				aidRc.rc = rc;
				itemsAidRc.add(aidRc);
			} else {
				for (int i = 0; i < itemsAidRc.size(); i++) {
					if (itemsAidRc.get(i).equals(aidRc)) {
						ItemAidRc it = itemsAidRc.get(i);
						aidRc.rc = it.rc + rc;
						itemsAidRc.set(i, it);
					}
				}
			}
			line =br.readLine();
		}		
		br.close();
	}
	
	public void write() throws IOException {
		PrintWriter pw = new PrintWriter(new File("data/cnki-kw-rc.txt"));
		for(int i=0;i<items.size();i++) {
			ItemRC item = items.get(i);
			pw.print(item.keyword + ",");
			for(int j=0;j<item.rcList.size();j++) {
				pw.print(item.rcList.get(j) +"|");
			}
			pw.print(","+item.count+"\n");
		}
		pw.close();
	}

}

class ItemAidRc{
	Integer aid ;
	Integer rc ;
	
	@Override
	public boolean equals(Object obj) {
		return aid == ((ItemAidRc)obj).aid;
	}
	@Override
	public String toString() {
		return "ItemAidRc [aid=" + aid + ", rc=" + rc + "]";
	}

}

class ItemRC {
	String keyword = "";
	ArrayList<Integer> rcList = new ArrayList<Integer>();
	int count = 0;

	@Override
	public boolean equals(Object obj) {
		return keyword.equals(((ItemRC)obj).keyword);
	}

	@Override
	public String toString() {
		return "ItemRC [keyword=" + keyword + ", rcList=" + rcList + ", count=" + count + "]";
	}

}

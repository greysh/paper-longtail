package cn.ac.greysh.spider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DICT {

	ArrayList<Item> items = new ArrayList<Item>();

	public static void main(String[] args) throws IOException {
		DICT d = new DICT();
		d.count();

	}

	public void count() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("data/cnki-kw.txt"));
		String line = br.readLine();
		int j = 0;
		while (line != null) {
			String[] kws = line.split(",")[2].split("/");
			System.out.println(j + ":" + kws[0]);
			for (String kw : kws) {
				Item item = new Item();
				item.keyword = kw;
				if (!items.contains(item)) {
					item.articles.add(j);
					item.count++;
					items.add(item);
				} else {
					for (int i = 0; i < items.size(); i++) {
						if (items.get(i).equals(item)) {
							Item it = items.get(i);
							it.articles.add(j);
							it.count++;
							items.set(i, it);
						}

					}
				}
			}
			line = br.readLine();
			j++;
		}
		br.close();
		System.out.println(items);
		write();
	}
	
	public void write() throws IOException {
		PrintWriter pw = new PrintWriter(new File("data/cnki-kw-diy.txt"));
		for(int i=0;i<items.size();i++) {
			Item item = items.get(i);
			pw.print(item.keyword + ",");
			for(int j=0;j<item.articles.size();j++) {
				pw.print(item.articles.get(j) +"|");
			}
			pw.print(","+item.count+"\n");
		}
		pw.close();
	}

}

class Item {
	String keyword = "";
	ArrayList<Integer> articles = new ArrayList<Integer>();
	int count = 0;

	@Override
	public boolean equals(Object obj) {
		return keyword.equals(((Item)obj).keyword);
	}

	@Override
	public String toString() {
		return "Item [keyword=" + keyword + ", articles=" + articles + ", count=" + count + "]";
	}
	
	

}

package cn.ac.greysh.spider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class KW {

	private ArrayList<String> authors = new ArrayList<String>();
	private ArrayList<String> articles = new ArrayList<String>();
	private ArrayList<String> keywords = new ArrayList<String>();

	public static void main(String[] args) throws IOException {
		KW eaa = new KW();
		eaa.parse();
		eaa.write();
	}

	public void parse() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("source/DataSet(CSSCI).txt"));
		String line = br.readLine();
		while (line != null) {
			if (line.contains("来源篇名")) {
				articles.add(line.replace("【来源篇名】", "").trim());
				line = br.readLine();
			}
			if (line.contains("来源作者")) {
				int endIndex = line.indexOf("/");
				if (endIndex > 0) {
					authors.add(line.substring("【来源作者】".length(), endIndex));
				} else {
					authors.add(line.substring("【来源作者】".length()));
				}
				line = br.readLine();
			}
			if (line.contains("【关 键 词】")) {
				keywords.add(line.replace("【关 键 词】", "").trim());
				line = br.readLine();
			}
			line = br.readLine();
		}
		br.close();

		System.out.println(articles.size());
		System.out.println(authors.size());;
		System.out.println(keywords.size());
	}
	
	public void write() throws IOException {
		PrintWriter pw = new PrintWriter("data/cnki-kw.txt");
		for(int i=0;i<authors.size();i++) {
			pw.println(authors.get(i)+","+articles.get(i)+","+keywords.get(i));
		}
		pw.flush();
		pw.close();
	}

}

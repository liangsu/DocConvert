package com.ls;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.pdfbox.PDFBox;
import org.apache.pdfbox.pdmodel.PDDocument;

public class PdfToFlash {

	/**
	 * ����SWFTools���߽�pdfת����swf��ת������swf�ļ���pdfͬ��
	 * 
	 * @author iori
	 * @param fileDir
	 *            PDF�ļ����·���������ļ�����
	 * @param exePath
	 *            ת������װ·��
	 * @throws IOException
	 */
	public static synchronized void pdf2swf(String fileDir, String exePath,int pageNum) throws IOException {
		// �ļ�·��
		String filePath = fileDir.substring(0, fileDir.lastIndexOf(File.separator));
		// �ļ�����������׺
		String fileName = fileDir.substring((filePath.length() + 1), fileDir.lastIndexOf("."));
		Process pro = null;
		if (isWindowsSystem()) {
			// �����windowsϵͳ
			// ����������
			// aa.exe "-o"
			String cmd = exePath + " \"" + fileDir + "\" -p "+pageNum+" -o \"" + filePath + "/" + fileName + "-"+pageNum+".swf\"";
			// Runtimeִ�к󷵻ش����Ľ��̶���
			pro = Runtime.getRuntime().exec(cmd);
		} else {
			// �����linuxϵͳ,·�������пո񣬶���һ��������˫���ţ������޷���������
			String[] cmd = new String[3];
			cmd[0] = exePath;
			cmd[1] = fileDir;
			cmd[2] = filePath + "/" + fileName + ".swf";
			// Runtimeִ�к󷵻ش����Ľ��̶���
			pro = Runtime.getRuntime().exec(cmd);
		}
		// ��Ҫ��ȡһ��cmd�������Ҫ������flush�����ļ������̣߳�
//		new DoOutput(pro.getInputStream()).start();
//		new DoOutput(pro.getErrorStream()).start();
		pro.getInputStream();
		pro.getErrorStream();
		try {
			// ����waitFor��������Ϊ��������ǰ���̣�ֱ��cmdִ����
			pro.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �ж��Ƿ���windows����ϵͳ
	 * 
	 * @author iori
	 * @return
	 */
	private static boolean isWindowsSystem() {
		String p = System.getProperty("os.name");
		return p.toLowerCase().indexOf("windows") >= 0 ? true : false;
	}

	/**
	 * ���߳��ڲ��� ��ȡת��ʱcmd���̵ı�׼������ʹ��������������������Ϊ�������ȡ�������̽�����
	 * 
	 * @author iori
	 */
	private static class DoOutput extends Thread {
		public InputStream is;

		// ���췽��
		public DoOutput(InputStream is) {
			this.is = is;
		}

		public void run() {
			BufferedReader br = new BufferedReader(new InputStreamReader(this.is));
			String str = null;
			try {
				// ���ﲢû�ж��������ݽ��д���ֻ�Ƕ���һ��
//				while ((str = br.readLine()) != null)
//					;
				int i = 0;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * ����main����
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// ת������װ·��
		String exePath = "C:\\Program Files (x86)\\SWFTools\\pdf2swf.exe";
		String filePath = "C:\\Users\\lenovo\\Desktop\\20160218104011178.pdf";
		
		int pageNumber = 0;
		try {
			PDDocument pdDocument = PDDocument.load(new File(filePath));
			pageNumber = pdDocument.getNumberOfPages();
			for(int i = 0;i<pageNumber;i++){
				pdf2swf(filePath, exePath,i+1);
			}
		} catch (IOException e) {
			System.err.println("ת������");
			e.printStackTrace();
		}
	}
}

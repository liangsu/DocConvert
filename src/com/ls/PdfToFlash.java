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
	 * 利用SWFTools工具将pdf转换成swf，转换完后的swf文件与pdf同名
	 * 
	 * @author iori
	 * @param fileDir
	 *            PDF文件存放路径（包括文件名）
	 * @param exePath
	 *            转换器安装路径
	 * @throws IOException
	 */
	public static synchronized void pdf2swf(String fileDir, String exePath,int pageNum) throws IOException {
		// 文件路径
		String filePath = fileDir.substring(0, fileDir.lastIndexOf(File.separator));
		// 文件名，不带后缀
		String fileName = fileDir.substring((filePath.length() + 1), fileDir.lastIndexOf("."));
		Process pro = null;
		if (isWindowsSystem()) {
			// 如果是windows系统
			// 命令行命令
			// aa.exe "-o"
			String cmd = exePath + " \"" + fileDir + "\" -p "+pageNum+" -o \"" + filePath + "/" + fileName + "-"+pageNum+".swf\"";
			// Runtime执行后返回创建的进程对象
			pro = Runtime.getRuntime().exec(cmd);
		} else {
			// 如果是linux系统,路径不能有空格，而且一定不能用双引号，否则无法创建进程
			String[] cmd = new String[3];
			cmd[0] = exePath;
			cmd[1] = fileDir;
			cmd[2] = filePath + "/" + fileName + ".swf";
			// Runtime执行后返回创建的进程对象
			pro = Runtime.getRuntime().exec(cmd);
		}
		// 非要读取一遍cmd的输出，要不不会flush生成文件（多线程）
//		new DoOutput(pro.getInputStream()).start();
//		new DoOutput(pro.getErrorStream()).start();
		pro.getInputStream();
		pro.getErrorStream();
		try {
			// 调用waitFor方法，是为了阻塞当前进程，直到cmd执行完
			pro.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断是否是windows操作系统
	 * 
	 * @author iori
	 * @return
	 */
	private static boolean isWindowsSystem() {
		String p = System.getProperty("os.name");
		return p.toLowerCase().indexOf("windows") >= 0 ? true : false;
	}

	/**
	 * 多线程内部类 读取转换时cmd进程的标准输出流和错误输出流，这样做是因为如果不读取流，进程将死锁
	 * 
	 * @author iori
	 */
	private static class DoOutput extends Thread {
		public InputStream is;

		// 构造方法
		public DoOutput(InputStream is) {
			this.is = is;
		}

		public void run() {
			BufferedReader br = new BufferedReader(new InputStreamReader(this.is));
			String str = null;
			try {
				// 这里并没有对流的内容进行处理，只是读了一遍
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
	 * 测试main方法
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// 转换器安装路径
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
			System.err.println("转换出错！");
			e.printStackTrace();
		}
	}
}

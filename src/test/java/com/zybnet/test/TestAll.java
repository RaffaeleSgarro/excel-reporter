package com.zybnet.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

import junit.framework.TestCase;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.zybnet.Main;

public class TestAll extends TestCase {

	public void testMain() throws IOException, InvalidFormatException, NoSuchAlgorithmException {
		
		File tmp = copyFiles();
		
		File pdf = new File("tmp/test/excel-report.pdf");
		
		pdf.getParentFile().mkdirs();
		
		Main.main(new String[] {
				tmp.getAbsolutePath(),
				"A1",
				pdf.getAbsolutePath()});
		
		// final cleanup
		for (File file: tmp.listFiles()) {
			file.delete();
		}
		
		tmp.delete();
	}

	// retuns tmpDir
	private File copyFiles() throws IOException {
		File tmpDir = new File("tmp-test");
		tmpDir.mkdirs();
		
		for (String name: new String[] {"foo", "bar", "baz", "intruder"}) {
			name += ".xlsx";
			OutputStream out = new FileOutputStream(new File(tmpDir, name));
			InputStream in = getClass().getResourceAsStream("/" + name);
			
			int i = 0;
			
			while ((i = in.read()) > -1) {
				out.write(i);
			}
			
			out.close();
			in.close();
		}
		
		return tmpDir;
	}
	
}

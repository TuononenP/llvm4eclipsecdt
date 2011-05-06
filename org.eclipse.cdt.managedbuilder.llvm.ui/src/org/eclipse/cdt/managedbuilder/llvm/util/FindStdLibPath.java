package org.eclipse.cdt.managedbuilder.llvm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder;

/**
 * The purpose is to find a path where stdc++ library is located.
 * 
 * NOTE: The script file must have execution rights.
 *
 */
public class FindStdLibPath {

	private static final String SCRIPT = "scripts/stdlib_path.sh"; //$NON-NLS-1$
	
	public static void main(String[] args) { //TODO: Remove after testing
		System.out.println(find());	
	}
	
	/**
	 * Find stdc++ library path in Linux.
	 * 
	 * @return Stdc++ library path.
	 */
	public static String find() {
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", SCRIPT);    //$NON-NLS-1$//$NON-NLS-2$
		try {
			Process p = pb.start();
			String line;
			BufferedReader input = new BufferedReader
					(new InputStreamReader(p.getInputStream()));
			line = input.readLine();
			input.close();
			if (line != null) {
				return line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}

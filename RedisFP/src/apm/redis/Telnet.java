package apm.redis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Telnet {
	public static void main(String[] args) throws IOException {

		Socket sock = null;
		PrintWriter out = null;
		BufferedReader in = null;

		try {
			sock = new Socket("localhost", 6379);
			out = new PrintWriter(sock.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		} catch (IOException e) {
			System.out.println("ERROR = " + e);
			return;
		}
		out.println("INFO");
		System.out.println("TEST = " + in.readLine());
		String str;
		while ((str = in.readLine()) != null) {
			System.out.println(str);
		}
		out.close();
		in.close();
		sock.close();
	}

}

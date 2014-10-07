package com.example.bloghome_shaodf.blog;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Util {
	public static InputStream getInputstream(String path) {
		
		HttpURLConnection connection = null;
		InputStream input = null;
		try {
			URL url = new URL(path);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(10000);
			connection.setRequestMethod("GET");
			connection.connect();
			input = connection.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return input;
	}
}

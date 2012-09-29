/************************************************************************************
 * This code is part of Fermare il declino for android                              *
 * Copyright © 2012 ALI - Associazione Lavoro e Impresa per le Liberta' Economiche  *
 *   http://www.fermareildeclino.it info@fermareildeclino.it                        *
 *                                                                                  *
 * This program is free software; you can redistribute it and/or                    *
 * modify it under the terms of the GNU General Public License                      *
 * as published by the Free Software Foundation; either version 2                   *
 * of the License, or (at your option) any later version.                           *
 *                                                                                  *
 * This program is distributed in the hope that it will be useful,                  *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                   *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                    *
 * GNU General Public License for more details.                                     *
 *                                                                                  *
 * You should have received a copy of the GNU General Public License                *
 * along with this program; if not, write to the Free Software                      *
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.  *
 ************************************************************************************/ 
package it.fermareildeclino.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class HttpClient {
	DefaultHttpClient httpClient;

	public HttpClient() {
		DefaultHttpClient client = new DefaultHttpClient();
		ClientConnectionManager mgr = client.getConnectionManager();
		HttpParams params = client.getParams();
		httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(
				params, mgr.getSchemeRegistry()), params);
	}

	public void setpass(String server, int port, String username,
			String password) {
		httpClient.getCredentialsProvider().setCredentials(
				new AuthScope(server, port),
				new UsernamePasswordCredentials(username, password));
	}

	public InputStream geturl_stream(String url) throws IOException {
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = httpClient.execute(httpget);
		HttpEntity entity = response.getEntity();
		return entity.getContent();
	}

	public byte[] geturl_byte(String url) throws IOException {
		return streamtobyte(geturl_stream(url));
	}

	public Document geturl_doc(String url) throws IOException,
			ParserConfigurationException, SAXException {
		return streamtodoc(geturl_stream(url));
	}

	public String geturl_string(String url) throws IOException {
		return streamtostring(geturl_stream(url));
	}

	public InputStream posturl_stream(String url, List<NameValuePair> params)
			throws IOException {
		HttpPost httppost = new HttpPost(url);
		httppost.setEntity(new UrlEncodedFormEntity(params));
		HttpResponse response = httpClient.execute(httppost);
		if (response.getStatusLine().getStatusCode() == 401) {
			return null;
		}
		HttpEntity entity = response.getEntity();
		return entity.getContent();
	}

	public Document posturl_doc(String url, List<NameValuePair> params)
			throws IOException, ParserConfigurationException, SAXException {
		return streamtodoc(posturl_stream(url, params));
	}

	public String posturl_string(String url, List<NameValuePair> params)
			throws Throwable {
		return streamtostring(posturl_stream(url, params));
	}

	public String download(String url, List<NameValuePair> params, File file)
			throws IOException {
		HttpPost httppost = new HttpPost(url);
		httppost.setEntity(new UrlEncodedFormEntity(params));
		HttpResponse response = httpClient.execute(httppost);
		HttpEntity en = response.getEntity();

		InputStream is;
		String mt = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		if ((is = en.getContent()) != null) {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(new FileOutputStream(file));
			int i;
			while ((i = bis.read()) != -1) {
				bos.write(i);
			}
			mt = en.getContentType().getValue();
			bos.close();
		}
		return mt;
	}

	public byte[] streamtobyte(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
		BufferedInputStream bis = new BufferedInputStream(is);
		int i;
		while ((i = bis.read()) != -1) {
			bos.write(i);
		}
		return bos.toByteArray();
	}

	private String streamtostring(InputStream is) throws IOException {
		BufferedReader r = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = r.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

	private Document streamtodoc(InputStream is) throws IOException,
			ParserConfigurationException, SAXException {
		Document result = null;
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		result = builder.parse(is);
		is.close();
		return result;
	}
}

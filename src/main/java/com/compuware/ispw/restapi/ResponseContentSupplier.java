package com.compuware.ispw.restapi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jenkinsci.plugins.scriptsecurity.sandbox.whitelists.Whitelisted;

import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @author Martin d'Anjou
 *         <p>
 *         A container for the Http Response. The container is returned as is to the Pipeline. For the normal plugin, the container is consumed internally (since it cannot be returned).
 */
public class ResponseContentSupplier implements Serializable, AutoCloseable {

	private static final long serialVersionUID = 1L;

	private int status;
	private Map<String, List<String>> headers = new HashMap<>();
	private String charset;
	private boolean isAbort = false;

	private ResponseHandle responseHandle;
	private String content;
	@SuppressFBWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
	private transient InputStream contentStream;
	@SuppressFBWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
	private transient CloseableHttpClient httpclient;

	public ResponseContentSupplier(String content, int status) {
		this.content = content;
		this.status = status;
	}

	public ResponseContentSupplier(ResponseHandle responseHandle, HttpResponse response) {
		this.status = response.getStatusLine().getStatusCode();
		this.responseHandle = responseHandle;
		readHeaders(response);
		readCharset(response);

		try {
			HttpEntity entity = response.getEntity();
			InputStream entityContent = entity != null ? entity.getContent() : null;

			if (responseHandle == ResponseHandle.STRING && entityContent != null) {
				byte[] bytes = ByteStreams.toByteArray(entityContent);
				contentStream = new ByteArrayInputStream(bytes);
				content = new String(bytes, Strings.isNullOrEmpty(charset) ?
						Charset.defaultCharset().name() : charset);
			} else {
				contentStream = entityContent;
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Whitelisted
	public int getStatus() {
		return this.status;
	}

	@Whitelisted
	public Map<String, List<String>> getHeaders() {
		return this.headers;
	}

	@Whitelisted
	public String getCharset() {
		return charset;
	}

	@Whitelisted
	public String getContent() {
		if (responseHandle == ResponseHandle.STRING) {
			return content;
		}
		if (content != null) {
			return content;
		}

		try (InputStreamReader in = new InputStreamReader(contentStream,
				Strings.isNullOrEmpty(charset) ? Charset.defaultCharset().name() : charset)) {
			content = CharStreams.toString(in);
			return content;
		} catch (IOException e) {
			throw new IllegalStateException("Error reading response. " +
					"If you are reading the content in pipeline you should pass responseHandle: 'LEAVE_OPEN' and " +
					"close the response(response.close()) after consume it.", e);
		}
	}

	@Whitelisted
	public InputStream getContentStream() {
		return contentStream;
	}

	private void readCharset(HttpResponse response) {
		Charset charset = null;
		ContentType contentType = ContentType.get(response.getEntity());
		if (contentType != null) {
			charset = contentType.getCharset();
			if (charset == null) {
				ContentType defaultContentType = ContentType.getByMimeType(contentType.getMimeType());
				if (defaultContentType != null) {
					charset = defaultContentType.getCharset();
				}
			}
		}
		if (charset != null) {
			this.charset = charset.name();
		}
	}

	private void readHeaders(HttpResponse response) {
		Header[] respHeaders = response.getAllHeaders();
		for (Header respHeader : respHeaders) {
			List<String> hs = headers.get(respHeader.getName());
			if (hs == null) {
				headers.put(respHeader.getName(), hs = new ArrayList<>());
			}
			hs.add(respHeader.getValue());
		}
	}

	@Override
	public String toString() {
		return "Status: " + this.status;
	}

	@Override
	public void close() throws IOException {
		if (httpclient != null) {
			httpclient.close();
		}
		if (contentStream != null) {
			contentStream.close();
		}
	}

	void setHttpClient(CloseableHttpClient httpclient) {
		this.httpclient = httpclient;
	}
	
	public void setAbortStatus(boolean isAbort)
	{
		this.isAbort = isAbort;
	}
	
	public boolean getAbortStatus()
	{
		return isAbort;
	}
}

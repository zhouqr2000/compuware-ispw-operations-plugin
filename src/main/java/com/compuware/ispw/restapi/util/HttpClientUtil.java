package com.compuware.ispw.restapi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;

import com.compuware.ispw.restapi.HttpMode;
import com.google.common.base.Strings;

/**
 * @author Janario Oliveira
 */
public class HttpClientUtil {

    public HttpRequestBase createRequestBase(RequestAction requestAction) throws IOException {
        HttpRequestBase httpRequestBase = doCreateRequestBase(requestAction);
        for (HttpRequestNameValuePair header : requestAction.getHeaders()) {
            httpRequestBase.addHeader(header.getName(), header.getValue());
        }

        return httpRequestBase;
    }

    private HttpRequestBase doCreateRequestBase(RequestAction requestAction) throws IOException {
        //without entity
    	if (requestAction.getMode() == HttpMode.HEAD) {
			return new HttpHead(getUrlWithParams(requestAction));
        } else if (requestAction.getMode() == HttpMode.GET) {
			return new HttpGet(getUrlWithParams(requestAction));
        }

		//with entity
		final String uri = requestAction.getUrl().toString();
		HttpEntityEnclosingRequestBase http;

		if (requestAction.getMode() == HttpMode.DELETE) {
			http = new HttpBodyDelete(uri);
		} else if (requestAction.getMode() == HttpMode.PUT) {
			http = new HttpPut(uri);
        } else if (requestAction.getMode() == HttpMode.PATCH) {
			http = new HttpPatch(uri);
        } else if (requestAction.getMode() == HttpMode.OPTIONS) {
        	return new HttpOptions(getUrlWithParams(requestAction));
		} else { //default post
			http = new HttpPost(uri);
		}

		http.setEntity(makeEntity(requestAction));
        return http;
    }

	private HttpEntity makeEntity(RequestAction requestAction) throws
			UnsupportedEncodingException {
		if (!Strings.isNullOrEmpty(requestAction.getRequestBody())) {
			ContentType contentType = null;
			for (HttpRequestNameValuePair header : requestAction.getHeaders()) {
				if ("Content-type".equalsIgnoreCase(header.getName())) {
					contentType = ContentType.parse(header.getValue());
					break;
				}
			}

			return new StringEntity(requestAction.getRequestBody(), contentType);
		}
		return toUrlEncoded(requestAction.getParams());
	}

	private String getUrlWithParams(RequestAction requestAction) throws IOException {
		String url = requestAction.getUrl().toString();

		if (!requestAction.getParams().isEmpty()) {
			url = appendParamsToUrl(url, requestAction.getParams());
		}
		return url;
	}

	private static UrlEncodedFormEntity toUrlEncoded(List<HttpRequestNameValuePair> params) throws UnsupportedEncodingException {
		return new UrlEncodedFormEntity(params);
	}

	public static String appendParamsToUrl(String url, List<HttpRequestNameValuePair> params) throws IOException {
		url += url.contains("?") ? "&" : "?";
		url += paramsToString(params);

		return url;
	}

	public static String paramsToString(List<HttpRequestNameValuePair> params) throws IOException {
		StringBuilder sb = new StringBuilder();
		final HttpEntity entity = toUrlEncoded(params);

		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8));
			String s;
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			return sb.toString();
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

    public HttpResponse execute(HttpClient client, HttpContext context, HttpRequestBase method,
								PrintStream logger) throws IOException, InterruptedException {
    	
    	if(RestApiUtils.isIspwDebugMode())
    		logger.println("Sending request to url: " + method.getURI());
        
        final HttpResponse httpResponse = client.execute(method, context);
        
        if(RestApiUtils.isIspwDebugMode())
        	logger.println("HTTP Response Code: " + httpResponse.getStatusLine());
        
        return httpResponse;
    }
}

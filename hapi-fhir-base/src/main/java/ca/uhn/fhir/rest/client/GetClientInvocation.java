package ca.uhn.fhir.rest.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import ca.uhn.fhir.context.ConfigurationException;

public class GetClientInvocation extends BaseClientInvocation {

	private final Map<String, String> myParameters;
	private final String myUrlPath;

	public GetClientInvocation(Map<String, String> theParameters, String... theUrlFragments) {
		myParameters = theParameters;
		myUrlPath = StringUtils.join(theUrlFragments, '/');
	}

	public GetClientInvocation(String... theUrlFragments) {
		myParameters = Collections.emptyMap();
		myUrlPath = StringUtils.join(theUrlFragments, '/');
	}

	public Map<String, String> getParameters() {
		return myParameters;
	}

	public String getUrlPath() {
		return myUrlPath;
	}

	@Override
	public HttpRequestBase asHttpRequest(String theUrlBase) {
		StringBuilder b = new StringBuilder();
		b.append(theUrlBase);
		b.append(myUrlPath);

		boolean first = true;
		for (Entry<String, String> next : myParameters.entrySet()) {
			if (next.getValue()==null) {
				continue;
			}
			if (first) {
				b.append('?');
				first = false;
			} else {
				b.append('&');
			}
			try {
				b.append(URLEncoder.encode(next.getKey(), "UTF-8"));
				b.append('=');
				b.append(URLEncoder.encode(next.getValue(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				throw new ConfigurationException("Could not find UTF-8 encoding. This shouldn't happen.", e);
			}

		}
		return new HttpGet(b.toString());
	}

}
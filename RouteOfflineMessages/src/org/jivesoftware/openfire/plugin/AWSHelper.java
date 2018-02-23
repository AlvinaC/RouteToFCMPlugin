package org.jivesoftware.openfire.plugin;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.JsonObject;

public class AWSHelper {

	/**
	 * Instance
	 **/
	private static AWSHelper instance = null;

	/**
	 * AWS URL to use firebase cloud messenging/APNS
	 */
	private static final String URL_SEND = "<url of AWS>";

	public static AWSHelper getInstance() {
		if (instance == null)
			instance = new AWSHelper();
		return instance;
	}

	private AWSHelper() {
	}

	public String sendAWSMessage(JsonObject sendObject) throws IOException {
		String response = "";
		try {
			List<NameValuePair> postParameters = new ArrayList<>();
			postParameters.add(new BasicNameValuePair("username", "<>"));
			postParameters.add(new BasicNameValuePair("password", "<>"));

			// Build the server URI together with the parameters you wish to
			// pass
			URIBuilder uriBuilder;

			uriBuilder = new URIBuilder(URL_SEND);

			uriBuilder.addParameters(postParameters);

			HttpPost httpPost = new HttpPost(uriBuilder.build());

			// Header setzen
			httpPost.setHeader("Content-Type", "application/json");

			String data = sendObject.toString();

			StringEntity entity = new StringEntity(data);

			// JSON-Object übergeben
			httpPost.setEntity(entity);

			HttpClient httpClient = HttpClientBuilder.create().build();

			BasicResponseHandler responseHandler = new BasicResponseHandler();
			response = (String) httpClient.execute(httpPost, responseHandler);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

}

package org.studyeasy;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.studyeasy.model.GoogleToken;
import org.studyeasy.model.RequestTokemParams;
import org.studyeasy.model.User;

/**
 * Servlet implementation class Success
 */
@WebServlet("/try")
public class Success extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Client client = ClientBuilder.newClient();

	WebTarget googleTokenServer = client.target("https://www.googleapis.com/oauth2/v4/token");

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// get code
		String code = request.getParameter("code");
		String client_id = "290631967821-4bvp8isiref8qa8beppmamshk8pg6930.apps.googleusercontent.com";
		String client_secret = "W2Pbobw993AO_hbDVtgBoUWo";
		String redirect_uri = "http://localhost:8080/GoogleOAuth/Success";
		String grant_type = "authorization_code";
		RequestTokemParams requestToken = new RequestTokemParams(code, client_id, client_secret, redirect_uri,
				grant_type);
		Response token = googleTokenServer.request().post(Entity.json(requestToken));
		GoogleToken googleToken = token.readEntity(GoogleToken.class);

		// used to fetch user info
		WebTarget googleUserInfoAP = client
				.target("https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + googleToken.getAccess_token());
		Response userInfo = googleUserInfoAP.request().get();
		User user = userInfo.readEntity(User.class);
		request.setAttribute("user", user);
		request.getRequestDispatcher("DisplayUser.jsp").forward(request, response);
	}

}

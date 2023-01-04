package ins.marianao.ticketing.fxml.services;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import cat.institutmarianao.ticketingws.model.User;
import ins.marianao.ticketing.fxml.manager.ResourceManager;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ServiceLogin extends Service<User>{

	private static final String PATH_LOGIN = "login";

	private String username;
	private String password;

	public ServiceLogin(String username, String password) throws Exception {
		if (username == null || username.isBlank()) throw new Exception(ResourceManager.getInstance().getText("error.login.find.no.username"));
		if (password == null || password.isBlank()) throw new Exception(ResourceManager.getInstance().getText("error.login.find.no.password"));
		this.username = username;
		this.password = password;
	}
	
	@Override
	protected Task<User> createTask() {
		return new Task<User>() {
			@Override
			protected User call() throws Exception {
				
				Client client = ResourceManager.getInstance().getWebClient();

				WebTarget webTarget = client.target(ResourceManager.getInstance().getParam("web.service.host.url")).path(ServiceQueryUsers.PATH_REST_USERS).path(PATH_LOGIN);

				Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);

				// {"first":"alex","second":"1234"}  => SpringFramework Pair
				JsonObject credentials = Json.createObjectBuilder()
					    .add("first", username)
					    .add("second", password)
					    .build();

				Response response = invocationBuilder.post(Entity.entity(credentials, MediaType.APPLICATION_JSON));

				if (response.getStatus() != Response.Status.OK.getStatusCode()) {
					throw new Exception(ResourceManager.getInstance().responseErrorToString(response));
				}

				return response.readEntity(User.class);
			}
		};
	}

}

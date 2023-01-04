package ins.marianao.ticketing.fxml.services;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import cat.institutmarianao.ticketingws.model.User;
import cat.institutmarianao.ticketingws.model.User.Role;
import ins.marianao.ticketing.fxml.manager.ResourceManager;

public class ServiceQueryUsers extends ServiceQueryBase<User> {

	public static final String PATH_REST_USERS = "users";
	private static final String DEFAULT_SORT = "username";

	private Role role;
	private String fullName;

	public ServiceQueryUsers(int page, int size, String sort, SortDirection direction, Role role, String fullName) {
		super(page, size, sort==null || sort.isBlank()?DEFAULT_SORT:sort, direction);
		this.role = role;
		this.fullName = fullName;
	}

	@Override
	protected List<User> customCall() throws Exception {
		Client client = ResourceManager.getInstance().getWebClient();

		WebTarget webTarget = client.target(ResourceManager.getInstance().getParam("web.service.host.url")).path(PATH_REST_USERS).path(PATH_QUERY_ALL)
				//.queryParam("page", page)
				//.queryParam("size", size)
				.queryParam("sort", sort, direction.toString())
				.queryParam("role", role==null?null:role.name())
				.queryParam("fullName", fullName);

		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);

		Response response = invocationBuilder.get();

		if (response.getStatus() != Response.Status.OK.getStatusCode()) 
			throw new Exception(ResourceManager.getInstance().responseErrorToString(response));

		List<User> users = response.readEntity(new GenericType<List<User>>(){});
		return users;
	}
}

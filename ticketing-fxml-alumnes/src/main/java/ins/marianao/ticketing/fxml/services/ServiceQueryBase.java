package ins.marianao.ticketing.fxml.services;

import java.util.List;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public abstract class ServiceQueryBase<T> extends Service<List<T>>{

	protected static final String PATH_QUERY_ALL = "find/all";
	protected static final int DEFAULT_PAGE = 0;
	protected static final int DEFAULT_SIZE = 10;

	protected static final String SORT_ASC = "asc";
	protected static final String SORT_DESC = "desc";

	public enum SortDirection { 
		SORT_ASC { public String toString() { return ServiceQueryBase.SORT_ASC; }}, 
		SORT_DESC { public String toString() { return ServiceQueryBase.SORT_DESC; }}, 
	};

	protected int page;
	protected int size;
	protected String sort;
	protected SortDirection direction;

	public ServiceQueryBase() {
		this.page = DEFAULT_PAGE;
		this.size = DEFAULT_SIZE;
		this.sort = "";
		this.direction = SortDirection.SORT_ASC;
	}
	
	public ServiceQueryBase(int page, int size, String sort, SortDirection direction) {
		this.page = page<0?DEFAULT_PAGE:page;
		this.size = size<1?DEFAULT_SIZE:size;
		this.sort = sort;
		this.direction = direction;
	}

	/* Must implement */
	protected abstract List<T> customCall() throws Exception;
	
	@Override
	protected Task<List<T>> createTask() {
		return new Task<List<T>>() {
			@Override
			protected List<T> call() throws Exception {
				return customCall();
			}
		};
	}

}

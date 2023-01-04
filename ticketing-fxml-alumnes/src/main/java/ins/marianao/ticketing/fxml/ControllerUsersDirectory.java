package ins.marianao.ticketing.fxml;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.exception.ExceptionUtils;

import cat.institutmarianao.ticketingws.model.User;
import cat.institutmarianao.ticketingws.model.User.Role;
import ins.marianao.ticketing.fxml.manager.ResourceManager;
import ins.marianao.ticketing.fxml.services.ServiceQueryBase.SortDirection;
import ins.marianao.ticketing.fxml.services.ServiceQueryUsers;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import javafx.util.Pair;


public class ControllerUsersDirectory extends AbstractControllerPDF {
	@FXML private BorderPane viewUsersDirectory;

	@FXML private ComboBox<Pair<String,String>> cmbRole;
	@FXML private TextField txtFullnameSearch;

	@FXML private TableView<User> usersTable;
	@FXML private TableColumn<User, Number> colIndex;
	@FXML private TableColumn<User, String> colRole;
	@FXML private TableColumn<User, String> colUsername;
	@FXML private TableColumn<User, String> colFullname;
	@FXML private TableColumn<User, Integer> colExtension;
	@FXML private TableColumn<User, String> colLocalion;
	@FXML private TableColumn<User, Boolean> colDelete;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle resource) {

		super.initialize(url, resource);

		this.txtFullnameSearch.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				
				reloadUsers();
			}
		});

		
		List<Pair<String,String>> roles = Stream.of(User.Role.values()).map(new Function<Role,Pair<String,String>>() {
			@Override
			public Pair<String,String> apply(Role t) {
				String key = t.name();
				return new Pair<String, String>(key, resource.getString("text.User."+key));
			}
			
		}).collect(Collectors.toList());
		
		
		ObservableList<Pair<String,String>> listRoles = FXCollections.observableArrayList(roles);
		listRoles.add(0, null);
		//listTipus.addAll(Stream.of(User.Role.values()).map(Enum::toString).collect(Collectors.toList()));
		
		this.cmbRole.setItems(listRoles);
		this.cmbRole.setConverter(Formatters.getUserRoleConverter());
		
		this.cmbRole.valueProperty().addListener(new ChangeListener<Pair<String,String>>() {
			@Override
			public void changed(ObservableValue<? extends Pair<String,String>> observable, Pair<String,String> oldValue, Pair<String,String> newValue) {
				reloadUsers();
			}
		});

		this.reloadUsers();

		this.usersTable.setEditable(true);
		this.usersTable.getSelectionModel().setCellSelectionEnabled(true);
		this.usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		this.colIndex.setMinWidth(30);
		this.colIndex.setMaxWidth(50);
		this.colIndex.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, Number>, ObservableValue<Number>>() {
			@Override
			public ObservableValue<Number> call(TableColumn.CellDataFeatures<User, Number> usuari) {
				return new SimpleLongProperty( usersTable.getItems().indexOf(usuari.getValue()) + 1 );
			}
		});

		this.colRole.setMinWidth(90);
		this.colRole.setMaxWidth(150);
		//this.colRol.setCellValueFactory(new PropertyValueFactory<User,String>("role"));
		//this.colRol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole().toString()));
		this.colRole.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> cellData) {
				return new SimpleStringProperty(cellData.getValue().getRole().toString());
			}
		});
		
		
		this.colRole.setCellFactory(TextFieldTableCell.forTableColumn());
		this.colRole.setEditable(false);
		
		this.colUsername.setMinWidth(100);
		this.colUsername.setMaxWidth(160);
		this.colUsername.setCellValueFactory(new PropertyValueFactory<User,String>("username"));
		this.colUsername.setCellFactory(TextFieldTableCell.forTableColumn());
		this.colUsername.setEditable(false);

		this.colFullname.setMinWidth(180);
		this.colFullname.setMaxWidth(260);
		this.colFullname.setCellValueFactory(new PropertyValueFactory<User,String>("fullName"));
		this.colFullname.setCellFactory(TextFieldTableCell.forTableColumn());

		this.colFullname.setOnEditCommit(new EventHandler<CellEditEvent<User, String>>() {			// Edició
			@Override
			public void handle(CellEditEvent<User, String> t) {
				try {
					//Usuari client = SistemaGestio.getInstance().getClientByIndex(t.getTablePosition().getRow());
					User usuari = t.getRowValue();
					usuari.setFullName(t.getNewValue());
					
					saveUsuari(usuari, false);
					
				} catch (Exception e) {
					ControllerMenu.showError(ResourceManager.getInstance().getText("error.viewUsers.fullName"), e.getMessage(), ExceptionUtils.getStackTrace(e));
				}
			}
		});

		this.colExtension.setMinWidth(100);
		this.colExtension.setMaxWidth(150);
		this.colExtension.setCellValueFactory(new PropertyValueFactory<User,Integer>("extension"));
		this.colExtension.setCellFactory(TextFieldTableCell.forTableColumn(Formatters.getExtensioFormatter()));

		this.colExtension.setOnEditCommit(new EventHandler<CellEditEvent<User, Integer>>() {			// Edició
			@Override
			public void handle(CellEditEvent<User, Integer> t) {
				try {
					//Usuari client = SistemaGestio.getInstance().getClientByIndex(t.getTablePosition().getRow());
					User usuari = t.getRowValue();
					usuari.setExtension(t.getNewValue());
					
					saveUsuari(usuari, false);
				} catch (Exception e) {
					ControllerMenu.showError(ResourceManager.getInstance().getText("error.viewUsers.extension"), e.getMessage(), ExceptionUtils.getStackTrace(e));
				}
			}
		});

		this.colLocalion.setMinWidth(210);
		this.colLocalion.setMaxWidth(280);
		this.colLocalion.setCellValueFactory(new PropertyValueFactory<User,String>("location"));
		this.colLocalion.setCellFactory(TextFieldTableCell.forTableColumn());

		User usuari = ResourceManager.getInstance().getCurrentUser();

		if (!usuari.isSupervisor()) {
			this.colDelete.setVisible(false);
			this.colDelete.setMinWidth(0);
			this.colDelete.setMaxWidth(0);
		} else {
			this.colDelete.setMinWidth(40);
			this.colDelete.setMaxWidth(50);
			// define a simple boolean cell value for the action column so that the column will only be shown for non-empty rows.
			this.colDelete.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, Boolean>, ObservableValue<Boolean>>() {
				@Override
				public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<User, Boolean> cell) {
					return new SimpleBooleanProperty(false);
				}
			});
	
			this.colDelete.setCellFactory(new ColumnButton<User, Boolean>(ResourceManager.getInstance().getText("fxml.text.viewUsers.col.delete"),
											new Image(getClass().getResourceAsStream("resources/recycle-bin.png")) ) {
				@Override
				public void buttonAction(User usuari) {
					try {
						boolean result = ControllerMenu.showConfirm(ResourceManager.getInstance().getText("fxml.text.viewUsers.delete.title"), 
																		ResourceManager.getInstance().getText("fxml.text.viewUsers.delete.text"));
						if (result) {
							deleteUsuari(usuari);
						}
					} catch (Exception e) {
						ControllerMenu.showError(ResourceManager.getInstance().getText("error.viewUsers.delete"), e.getMessage(), ExceptionUtils.getStackTrace(e));
					}
				}
			});
		}
	}

	private void deleteUsuari(User usuari) throws Exception {
		/* ToDo */
	}
	
	private void saveUsuari(User usuari, boolean insert) throws Exception {
		/* ToDo */
	}
	
	private void reloadUsers() {
		Pair<String,String> role = this.cmbRole.getValue();
		String search = this.txtFullnameSearch.getText();

		this.usersTable.setEditable(false);
		
		final ServiceQueryUsers queryUsers = new ServiceQueryUsers(0, 10, "username", SortDirection.SORT_ASC, role==null?null:Role.valueOf(role.getKey()), search);
		
		queryUsers.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent t) {
            	usersTable.setEditable(true);
            	
            	usersTable.getItems().clear();
            	
                ObservableList<User> users = FXCollections.observableArrayList(queryUsers.getValue());

        		usersTable.setItems( users );
            }
        });
		
		queryUsers.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				usersTable.setEditable(true);

				Throwable e = t.getSource().getException();
				
				ControllerMenu.showError(ResourceManager.getInstance().getText("error.viewUsers.web.service"), e.getMessage(), ExceptionUtils.getStackTrace(e));
			}
			
		});
		
		queryUsers.start();
	}
	
	@Override
	protected String htmlContentToPDF() {
		List<User> users = this.usersTable.getItems();
		Pair<String,String> role = this.cmbRole.getValue();
		String search = this.txtFullnameSearch.getText();
		
		List<Pair<String,String>> filters = new LinkedList<>();
		if (role != null) filters.add(new Pair<>(ResourceManager.getInstance().getText("fxml.text.viewUsers.role"), role.getValue()));
		if (search != null && !search.isBlank()) filters.add(new Pair<>(ResourceManager.getInstance().getText("fxml.text.viewUsers.search.prompt"), "\""+search+"\""));
		
		return "<pre>"+ResourceManager.getInstance().usersDirectoryHtml(users, filters)+"</pre>";
	}
	
	@Override
	protected String documentTitle() {
		return ResourceManager.getInstance().getText("fxml.text.viewUsers.report.title");
	}
	
	@Override
	protected String documentFileName() {
		return ResourceManager.FILE_REPORT_USERS;
	}

}




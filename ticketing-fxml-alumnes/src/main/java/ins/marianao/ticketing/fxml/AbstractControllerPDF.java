package ins.marianao.ticketing.fxml;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.lang3.exception.ExceptionUtils;

import ins.marianao.ticketing.fxml.manager.ResourceManager;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;


public abstract class AbstractControllerPDF implements Initializable {
	@FXML protected Button btnPDF;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle resource) {
		ControllerMenu.addIconToButton(this.btnPDF, new Image(getClass().getResourceAsStream("resources/pdf-logo.png")), 30);
	}

	@FXML
	public void generarPDFClick(ActionEvent event) {
		try {
            PdfManager manager;

			String path = ResourceManager.BASE_DIR+"/"+documentFileName();
			
			manager = new PdfManager(path);

			manager.generarPDF(documentTitle(), htmlContentToPDF(), isDocumentLandscape());

			File pdfGenerat = new File(path);

			HostServices hostServices = ResourceManager.getInstance().getApp().getHostServices();
	        hostServices.showDocument(pdfGenerat.getAbsolutePath());
	        
		} catch (Exception e) {
			//e.printStackTrace();
		    ControllerMenu.showError(ResourceManager.getInstance().getText("error.pdf.title"), e.getMessage(), ExceptionUtils.getStackTrace(e));
		}
	}

	
	protected abstract String htmlContentToPDF() throws Exception;
	
	protected abstract String documentTitle();
	
	protected abstract String documentFileName();
	
	protected boolean isDocumentLandscape() {
		return false;
	}

	/*protected void loadHtmlContentToPDF() {
		
		final ServiceQueryBase<T> query = this.getQueryService();
		
		query.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent t) {
            	List<T> items = query.getValue();
            	
            	

            }
        });
		
		//queryUsers.setOnSucceeded(e -> usuarisTable.setItems( FXCollections.observableArrayList((List<Usuaris>) queryUsers.getValue()) ));

		query.setOnFailed(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent t) {
				

				Throwable e = t.getSource().getException();
				
				ControllerMenu.showError(SistemaGestio.getInstance().getText("error.viewInProgress.web.service"), e.getMessage(), ExceptionUtils.getStackTrace(e));
			}
			
		});
		
		query.start();
		
	}

	// Must return custom
	protected abstract ServiceQueryBase<T> getQueryService();*/
	
}




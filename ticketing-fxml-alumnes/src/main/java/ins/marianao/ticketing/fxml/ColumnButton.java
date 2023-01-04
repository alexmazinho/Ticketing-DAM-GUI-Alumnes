package ins.marianao.ticketing.fxml;

import java.util.HashSet;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/* 	Column button
  	this.colEsborrar.setCellFactory(new ColumnButton<ClasseModel, Boolean>("Text"){
		@Override
		public void buttonAction(ClasseModel element) {
			// Acció ...
		}
	});
 */
public abstract class ColumnButton<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {
	private String text;
	private Image image;
	private Set<Integer> rows;

    public ColumnButton(String text) {
        super();
        this.text = text;
        this.rows = new HashSet<Integer>();
    }

    public ColumnButton(String text, Image image) {
        super();
        this.image = image;
        this.rows = new HashSet<Integer>();
    }

    public ColumnButton(String text, Image image, Set<Integer> rows) {
        super();
        this.image = image;
        this.rows = rows!=null?rows:new HashSet<Integer>();
    }

    public abstract void buttonAction(S element);

    @Override
	public TableCell<S, T> call( TableColumn<S, T> param )
	{
		TableCell<S, T> cell = new TableCell<S, T>()
		{
			@Override
			public void updateItem( T item, boolean empty )
			{
				super.updateItem( item, empty );

				if (item == null || empty || (rows.size() > 0 && !rows.contains(this.getIndex()))) {
                    setGraphic( null );
                } else {
                	Button btn = new Button( text );

                	if ( image != null ) {
                		ImageView logo = new ImageView(image);
                		logo.setFitWidth(20);
                		logo.setFitHeight(20);
                		btn.setGraphic(logo);
                	}

					btn.setOnAction( ( ActionEvent event ) ->
					{
						S element = getTableView().getItems().get( getIndex() );
						buttonAction(element);

					} );
					setGraphic( btn );
				}
			}
		};
		return cell;
	}


}

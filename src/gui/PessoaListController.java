package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import listener.DataChangeListener;
import model.Pessoa;
import services.PessoaService;
import util.Alerts;
import util.Utils;

public class PessoaListController implements Initializable,DataChangeListener {

	private PessoaService service;
	
	private ObservableList<Pessoa> obsList;
	
	@FXML
	private TableView<Pessoa> tableViewPessoa;

	@FXML
	private TableColumn<Pessoa, Integer> tableColumnId;

	@FXML
	private TableColumn<Pessoa, String> tableColumnFuncionario;
	
	@FXML
	private TableColumn<Pessoa, String> tableColumnEmail;

	@FXML
	private TableColumn<Pessoa, Date> tableColumnDataNascimento;

	@FXML
	private TableColumn<Pessoa, Double> tableColumnSalarioBase;
	
	@FXML
	private TableColumn<Pessoa, Pessoa> tableColumnEDIT;
	
	@FXML
	private TableColumn<Pessoa, Pessoa> tableColumnREMOVE;

	@FXML
	private Button btCadastroPessoa;	

	@FXML
	public void onBtCadastroPessoaAction(ActionEvent event) {
		
		Stage parentStage = Utils.currentStage(event);
		Pessoa obj = new Pessoa();
		createDialogForm(obj, "/gui/PessoaForm.fxml", parentStage);		
	}		
	
	private void createDialogForm(Pessoa obj, String absoluteName, Stage parentStage) {
		
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			PessoaFormController controller = loader.getController();			
			controller.setPessoa(obj);
			controller.setPessoaService(new PessoaService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Formulário Cadastro de Pessoa");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		initializeNodes();
	}

	private void initializeNodes() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnFuncionario.setCellValueFactory(new PropertyValueFactory<>("funcionario"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
		Utils.formatTableColumnDate(tableColumnDataNascimento, "dd/MM/yyyy");
		tableColumnSalarioBase.setCellValueFactory(new PropertyValueFactory<>("salarioBase"));
		Utils.formatTableColumnDouble(tableColumnSalarioBase, 2);
		

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewPessoa.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void setPessoaService(PessoaService service) {
		this.service = service;
	}
	
	public void atualizarTableView() {
		
		if( service == null) {
			throw new IllegalStateException("service Pessoa Nulo!!!");
		}
		
		List<Pessoa> list = service.listarTodos();
		obsList = FXCollections.observableArrayList(list);
		tableViewPessoa.setItems(obsList);
		initEditButtons();
		initRemoveButtons();		
	}
	
	@Override
	public void onDataChanged() {
		atualizarTableView();
	}
	
	private void initEditButtons() {
		
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Pessoa, Pessoa>() {
			
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Pessoa obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/PessoaForm.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initRemoveButtons() {
		
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Pessoa, Pessoa>() {
			
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(Pessoa obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Pessoa obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				atualizarTableView();
			}
			catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}

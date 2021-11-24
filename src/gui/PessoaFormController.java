package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import exceptions.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import listener.DataChangeListener;
import model.Pessoa;
import services.PessoaService;
import util.Alerts;
import util.Constraints;
import util.Utils;

public class PessoaFormController implements Initializable{

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtFuncionario;

	@FXML
	private Label labelErrorName;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;
	
	private Pessoa entity;
	
	private PessoaService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		try {
			
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
	}

	private void notifyDataChangeListeners() {
	
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}
	
	private Pessoa getFormData() {
		
		Pessoa obj = new Pessoa();
		
		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if (txtFuncionario.getText() == null || txtFuncionario.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		
		obj.setFuncionario(txtFuncionario.getText());
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
	
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	public void setPessoa(Pessoa entity) {
		this.entity = entity;
	}
	
	public void setPessoaService(PessoaService service) {
		this.service = service;
	}
	
	public void updateFormData() {
		
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		} 
		
		txtId.setText(String.valueOf(entity.getId()));
		txtFuncionario.setText(entity.getFuncionario());
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtFuncionario, 30);
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		
		Set<String> fields = errors.keySet();

		if (fields.contains("funcionario")) {
			labelErrorName.setText(errors.get("funcionario"));
		}
	}
}

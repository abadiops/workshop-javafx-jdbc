package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import services.DepartamentoService;
import util.Alerts;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemPessoa;

	@FXML
	private MenuItem menuItemDepartamento;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemPessoaAction() {
		System.out.println("onMenuItemPessoaAction");
	}

	@FXML
	public void onMenuItemDepartamentoAction() {

		loadView("/gui/DepartamentoList.fxml", (DepartamentoListController controller) -> {
			controller.setDepartamentoService(new DepartamentoService());
			controller.atualizarTableView();
		});

	}

	@FXML
	public void onMenuItemAbout() {
		loadView("/gui/About.fxml", x -> {
	  });
	}

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());

			T controller = loader.getController();
			initializingAction.accept(controller);
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
		catch (IllegalStateException e) {
			Alerts.showAlert("IO IllegalStateException", null, e.getMessage(), AlertType.ERROR);
		} 
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}
}

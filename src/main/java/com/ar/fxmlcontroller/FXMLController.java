
package com.ar.fxmlcontroller;

import com.ar.alert.AlertSection;
import com.ar.db.DBSection;
import com.ar.util.ClipBoardCreation;
import com.ar.util.Dependency;
import com.ar.util.DownnloadTask;
import com.ar.util.WindowsOpenFileLocation;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXProgressBar;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
//import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

public class FXMLController implements Initializable {

	@FXML
	private ComboBox<String> comboScope;
	@FXML
	private ComboBox<String> comboOptional;
	@FXML
	private TableView<Dependency> table;
	@FXML
	private TableColumn<Dependency, String> colDependency;
	@FXML
	private TableColumn<Dependency, String> colGroupId;
	@FXML
	private TableColumn<Dependency, String> colArtifactId;
	@FXML
	private TableColumn<Dependency, String> colDescription;
	@FXML
	private TableColumn<Dependency, String> colScope;
	@FXML
	private TableColumn<Dependency, String> colOptional;
	@FXML
	private TableColumn<Dependency, String> colVersion;

	private DBSection db;
	ObservableList<Dependency> dependencyList = FXCollections.observableArrayList();

	@FXML
	private TextField tfDependency;
	@FXML
	private TextField tfGroupId;
	@FXML
	private TextField tfArtifactId;
	@FXML
	private TextField tfVersion;
	@FXML
	private TextArea taDescription;
	@FXML
	private JFXButton scopeClear;
	@FXML
	private JFXButton optionalClear;
	@FXML
	private TextField tfSearch;
	@FXML
	private Button allTableButton;
	@FXML
	private Button buttonDownloadFolderOpen;
	@FXML
	private Button buttonJAVAPluginsFolder;
	@FXML
	private TextArea textInsertDependency;
	@FXML
	private TextField tfInsertName;
	@FXML
	private TextField tfGIDInsert;
	@FXML
	private TextField tfAIDInsert;
	@FXML
	private TextArea taDescriptionInsert;
	@FXML
	private TextField tfVersionInsert;
	@FXML
	private TextField tfScopeInsert;
	@FXML
	private TextField tfOptionalInsert;
	@FXML
	private JFXButton backToWEB;
	@FXML
	private JFXButton forwardToWEB;
	@FXML
	private JFXButton refreshWEB;
	@FXML
	private TextField addressBarWEBTF;
	@FXML
	private JFXButton searchWEB;
	@FXML
	private WebView webWEB;
	@FXML
	private BorderPane borderPaneBrowser;
	@FXML
	private HBox hboxProgress;
	@FXML
	private TabPane tabPaneBrowser;
	@FXML
	private Tab firstTab;
	@FXML
	private CheckBox folderOpenCheckBox;
	@FXML
	private Button buttonFolder;

	WebEngine engine;

	private List<String> historyList = new ArrayList<String>();
	private AutoCompletionBinding<String> autoComp;

	private JFXListView<String> urlListView = new JFXListView<String>();
	private ObservableList<String> historyObsList = FXCollections.observableArrayList();

	int count;
	boolean deleteOp;

	List<Tab> tabList;
	int c;
	int j;
	int sizeIndex;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		db = DBSection.getInstance();
		initCol();
		loadData();
		buttonDownloadFolderOpen.setTooltip(new Tooltip("JAR downloaded folder \n Path :⇾ C:\\Users\\HP\\Downloads\\javafxdownload"));
		buttonJAVAPluginsFolder.setTooltip(new Tooltip("Java Plugins Folder \n Path :⇾ C:\\Users\\HP\\Documents\\JAVA Plugins"));
		comboOptional.getItems().addAll("true", "false");
		comboScope.getItems().addAll("compile", "provided", "runtime", "test", "system", "import");
		tableViewSearchByTextfield();
		allTableButton.setTooltip(new Tooltip("all dependencies"));
		engine = webWEB.getEngine();
		engine.load("https://mvnrepository.com/");
		downloadOption(engine);
		firstTab.setTooltip(new Tooltip("https://mvnrepository.com/"));
		firstTab.setContextMenu(contextMenuCreating(firstTab));
		addressBarWEBTF.setText("https://mvnrepository.com/");
		// ☑ ✔ ☐ ☒ ✓
		buttonFolder.setTooltip(new Tooltip(
				"[✔] downloads to custom folder \n default location: C:\\Users\\HP\\Downloads\\javafxdownload"));

		webViewNewLink(engine);
		tooltipLinkOperation(firstTab, engine);
		textSearchLinkOperation();
		setTableColumWidthAndMovable();
		historyList.add("https://mvnrepository.com/");
		historyList.add("https://www.google.com/");
		historyObsList.addAll(historyList);
		autoComp = TextFields.bindAutoCompletion(addressBarWEBTF, historyList);
		autoComp.minWidthProperty().bind(addressBarWEBTF.widthProperty());
	}

	private void historySaveOperation(String url) {
		count = 0;
		deleteOp = false;
		for (String str : historyList) {
			if (str.contains(url)) {
//				System.out.println(url + " URL is Alredy in History List at index[" + count + "] and at Position " + (count + 1));
				deleteOp = true;
				break;
			}
			count++;
		}
		if (deleteOp) {
			System.out.println("DELETING ::: " + url);
			historyList.remove(url);
		}
		historyList.add(url);
		historyObsList.clear();
		// create new list and reverse history list[ie in history list url added in
		// last]
		// so we create new list and reverse the history list ,ie new url in top
		List<String> toReverseList = new ArrayList<String>(historyList);
		Collections.reverse(toReverseList);

		historyObsList.addAll(toReverseList);
		autoComp.dispose();
		autoComp = TextFields.bindAutoCompletion(addressBarWEBTF, historyList);
		autoComp.minWidthProperty().bind(addressBarWEBTF.widthProperty());
	}

	private void textSearchLinkOperation() {

		tabPaneBrowser.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> arg0, Tab tabOld, Tab tabNew) {
				// ella taabum close cheyyumbol error varunnadhu oyivaakaan
				if (tabNew == null) {
					addressBarWEBTF.setText("");
					return;
				}
				if (tabNew.getText().equals("history")) {
					addressBarWEBTF.setText("history");
					return;
				}
//				System.out.println(tabNew.getText());
				WebEngine selectEngine = ((WebView) tabNew.getContent()).getEngine();
				String urlTex = selectEngine.getLocation();
//				System.out.println(urlTex);
				addressBarWEBTF.setText(urlTex);
			}
		});
	}

	private void tooltipLinkOperation(Tab tab, WebEngine eng) {
		eng.locationProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldUrl, String newUrl) {
//				System.out.println(newUrl);
				tab.setTooltip(new Tooltip(newUrl));
				addressBarWEBTF.setText(newUrl);
				historySaveOperation(newUrl);
			}
		});
	}

	private void setTableColumWidthAndMovable() {
		colDependency.prefWidthProperty().bind(table.widthProperty().multiply(0.16));
		colGroupId.prefWidthProperty().bind(table.widthProperty().multiply(0.17));
		colArtifactId.prefWidthProperty().bind(table.widthProperty().multiply(0.17));
		colVersion.prefWidthProperty().bind(table.widthProperty().multiply(0.08));
		colScope.prefWidthProperty().bind(table.widthProperty().multiply(0.08));
		colOptional.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
		colDescription.prefWidthProperty().bind(table.widthProperty().multiply(0.28));

//		colDependency.setReorderable(false); //ithu kondu colDependency move 
		// aavila pakshe mattulavaye kondu ithu move cheyyam
	}

	private void initCol() {
		colDependency.setCellValueFactory(new PropertyValueFactory<>("dependency"));
		colGroupId.setCellValueFactory(new PropertyValueFactory<>("groupId"));
		colArtifactId.setCellValueFactory(new PropertyValueFactory<>("artifactId"));
		colVersion.setCellValueFactory(new PropertyValueFactory<>("version"));
		colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
		colScope.setCellValueFactory(new PropertyValueFactory<>("scope"));
		colOptional.setCellValueFactory(new PropertyValueFactory<>("optional"));
	}

	private void loadData() {
		try {
			ResultSet rs = db.allDependencies();
			while (rs.next()) {
				dependencyList.add(new Dependency(rs.getString("dependency"), rs.getString("groupid"),
						rs.getString("artifactid"), rs.getString("version"), rs.getString("description"),
						rs.getString("scope"), rs.getString("optional")));
			}

		} catch (SQLException ex) {
			Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
		}
//        System.out.println(dependencyList);
		table.setItems(dependencyList);
	}

	@FXML
	private void clearAction(ActionEvent event) {
		tfDependency.setText("");
		tfGroupId.setText("");
		tfArtifactId.setText("");
		tfVersion.setText("");
		taDescription.setText("");
		comboScope.setValue(null);
		comboOptional.setValue(null);
	}

	@FXML
	private void saveAction(ActionEvent event) throws SQLException {
		if ((!tfDependency.getText().matches(".*\\w.*")) || (!tfGroupId.getText().matches(".*\\w.*"))
				|| (!tfArtifactId.getText().matches(".*\\w.*"))) {
			AlertSection.showWarningMessage("please must fill fields : dependency,groupId,artifactId",
					"Empty fields found", "Dependency warning", table);
			return;
		}
		String version = tfVersion.getText();
		String description = taDescription.getText();

		if (version == null)
			version = null;
		else
			version = version.matches(".*\\w.*") ? version : null;
		if (description == null)
			description = null;
		else
			description = description.matches(".*\\w.*") ? description : null;

		Dependency dependency = new Dependency(tfDependency.getText().toUpperCase(), tfGroupId.getText(),
				tfArtifactId.getText(), version, description, comboScope.getValue(), comboOptional.getValue());
		boolean bool = db.insertDependency(dependency, tfDependency);
		if (bool) {
			AlertSection.showControFxDialogMessage("Insert successful", "dependency inserted successfully\n",
					Pos.BOTTOM_RIGHT);
			clearAction(null);
		} else
			AlertSection.showControFxErrorMessage("Insert Error!",
					"Dependency insertion Failed\nCheck manually for error correction", Pos.CENTER);
		dependencyList.clear();
		loadData();
		table.refresh();
	}

	@FXML
	private void previewAction(ActionEvent event) throws IOException {
		Dependency selectedDependency = table.getSelectionModel().getSelectedItem();
		if (selectedDependency == null) {
			AlertSection.showErrorMessage("No Item Selected", table);
			return;
		}
//        String dependencyName = selectedDependency.getDependency();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PreviewAndEdit.fxml"));
		Parent root = loader.load();
		Stage stage = new Stage();
		PreviewAndEditController controller = (PreviewAndEditController) loader.getController();
		controller.valueTakingForView(selectedDependency);
		stage.setScene(new Scene(root));
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setTitle("DEPENDENCY DETAILS");
		stage.setAlwaysOnTop(true);
		stage.show();
	}

	@FXML
	private void editAction(ActionEvent event) throws IOException {
		Dependency selectedDependency = table.getSelectionModel().getSelectedItem();
		if (null == selectedDependency) {
			AlertSection.showErrorMessage("No Item Selected", table);
			return;
		}
//        String dependencyName = selectedDependency.getDependency();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PreviewAndEdit.fxml"));
		Parent root = loader.load();
		Stage stage = new Stage();
		PreviewAndEditController controller = (PreviewAndEditController) loader.getController();
		controller.valueTakingForEdit(selectedDependency);
		stage.setScene(new Scene(root));
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setTitle("DEPENDENCY EDIT");
		stage.setAlwaysOnTop(true);
		stage.showAndWait();

		dependencyList.clear();
		loadData();
		table.refresh();
	}

	@FXML
	private void deleteAction(ActionEvent event) throws SQLException {
		Dependency selectedDependency = table.getSelectionModel().getSelectedItem();
		if (selectedDependency == null) {
			AlertSection.showErrorMessage("No Item Selected", table);
			return;
		}

		boolean answerToDelete = AlertSection.showConfirmationMessageDelete("are you sure delete this dependency",
				"Dependency delete ?", table);
		if (!answerToDelete)
			return;

		String dependencyName = selectedDependency.getDependency();
		boolean bool = db.deleteDependency(dependencyName);
		if (bool) {
			AlertSection.showControFxDialogMessage("Delete Dependency", "dependency deleted successfully",
					Pos.BOTTOM_RIGHT);
		} else
			AlertSection.showControFxErrorMessage("Deletion Error!",
					"Dependency deletion Failed\nCheck manually for error correction", Pos.CENTER);

		dependencyList.clear();
		loadData();
		table.refresh();
	}

	@FXML
	private void scopeClearAction(ActionEvent event) {
		comboScope.setValue(null);
	}

	@FXML
	private void optionalClearAction(ActionEvent event) {
		comboOptional.setValue(null);
	}

	@FXML
	private void copyAction(ActionEvent event) {
		Dependency selectedDependency = table.getSelectionModel().getSelectedItem();
		if (selectedDependency == null) {
			AlertSection.showErrorMessage("No Item Selected", table);
			return;
		}
		ClipBoardCreation.clipValue(selectedDependency);
	}

	@FXML
	private void allDataAction(ActionEvent event) {
		tfSearch.setText("");
	}

	@FXML
	private void downloadJARFolderAction(ActionEvent event) {
		Stage stage = (Stage) buttonDownloadFolderOpen.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "\\Downloads\\javafxdownload\\"));
		List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);
		if(selectedFiles != null) {
			AlertSection.showFilesClipBoardSave("Files copied to ClipBoard",
					selectedFiles.size()+" files copied", Pos.BOTTOM_LEFT);
		}
		ClipboardContent content = new ClipboardContent();
		content.putFiles(selectedFiles);

		Clipboard clipboard = Clipboard.getSystemClipboard();
		clipboard.setContent(content);
	}

	@FXML
	private void JAVAPluginsFolderAction(ActionEvent event) {
		File file = new File("C:\\Users\\HP\\Documents\\JAVA Plugins");
		if (!file.exists()) {
//			System.out.println("OUT");
			file = new File("C:\\Users\\HP\\Documents");
		}
		WindowsOpenFileLocation.openFileLocation(file.getPath());
	}

	@FXML
	private void searchDataAction(ActionEvent event) {
		String searchWord = tfSearch.getText();
		tfSearch.setText("");
		tfSearch.setText(searchWord);
	}

	// =============================//dependency search by
	// textfield//=============================//
	private boolean searchFindsDependencyByTextfield(Dependency dependencies, String searchText) {

		return (dependencies.getDependency().toLowerCase().contains(searchText.toLowerCase())
				|| dependencies.getGroupId().toLowerCase().contains(searchText.toLowerCase())
				|| dependencies.getArtifactId().toLowerCase().contains(searchText.toLowerCase()));
	}

	private ObservableList<Dependency> filterListByTextfield(List<Dependency> list, String searchText) {
		List<Dependency> filteredList = new ArrayList<>();
		for (Dependency dependency : list) {
			if (searchFindsDependencyByTextfield(dependency, searchText))
				filteredList.add(dependency);
		}
		return FXCollections.observableArrayList(filteredList);
	}

	private void tableViewSearchByTextfield() {
		tfSearch.textProperty().addListener(((observable, oldValue, newValue) -> {
			table.setItems(filterListByTextfield(dependencyList, newValue));
		}));
	}
//=============================================================//

	@FXML
	private void clearInsertAction(ActionEvent event) {
		textInsertDependency.setText("");
		tfInsertName.setText("");
		tfGIDInsert.setText("");
		tfAIDInsert.setText("");
		tfVersionInsert.setText("");
		tfScopeInsert.setText("");
		tfOptionalInsert.setText("");
		taDescriptionInsert.setText("");
	}

	@FXML
	private void saveInsertAction(ActionEvent event) throws SQLException {
		if ((!tfInsertName.getText().matches(".*\\w.*")) || (!tfGIDInsert.getText().matches(".*\\w.*"))
				|| (!tfAIDInsert.getText().matches(".*\\w.*"))) {
			AlertSection.showWarningMessage("please must fill fields : dependency,groupId,artifactId",
					"Empty fields found", "Dependency warning", tfInsertName);
			return;
		}

		String version = tfVersionInsert.getText();
		String description = taDescriptionInsert.getText();
		String scope = tfScopeInsert.getText();
		String optional = tfOptionalInsert.getText();

		if (version == null)
			version = null;
		else
			version = version.matches(".*\\w.*") ? version : null;

		if (description == null)
			description = null;
		else
			description = description.matches(".*\\w.*") ? description : null;

		if (scope == null)
			scope = null;
		else
			scope = scope.matches(".*\\w.*") ? scope : null;

		if (optional == null)
			optional = null;
		else
			optional = optional.matches(".*\\w.*") ? optional : null;

		Dependency dependency = new Dependency(tfInsertName.getText().toUpperCase(), tfGIDInsert.getText(),
				tfAIDInsert.getText(), version, description, scope, optional);
		System.out.println(dependency);

		boolean bool = db.insertDependency(dependency, tfInsertName);
		if (bool) {
			AlertSection.showControFxDialogMessage("Insert successful", "dependency inserted successfully",
					Pos.BOTTOM_RIGHT);
			clearInsertAction(null);
		} else
			AlertSection.showControFxErrorMessage("Update Error!",
					"Dependency insertion Failed\nCheck manually for error correction", Pos.CENTER);
		dependencyList.clear();
		loadData();
		table.refresh();

	}

//	==================FORWARD ACTION POM DAPENDENCY=====================
	@FXML
	private void forrwardAction(ActionEvent event) {
		String pomDependency = textInsertDependency.getText();
		String groupId = StringUtils.substringBetween(pomDependency, "<groupId>", "</groupId>");
		String artifactId = StringUtils.substringBetween(pomDependency, "<artifactId>", "</artifactId>");
		String description;
		String version;
		String scope;
		String optional;

		if (pomDependency.contains("<!-- "))
			description = StringUtils.substringBetween(pomDependency, "<!-- ", "-->");
		else if (pomDependency.contains("<!--"))
			description = StringUtils.substringBetween(pomDependency, "<!--", "-->");
		else
			description = "";

		if (pomDependency.contains("<version>"))
			version = StringUtils.substringBetween(pomDependency, "<version>", "</version>");
		else
			version = "";

		if (pomDependency.contains("<scope>"))
			scope = StringUtils.substringBetween(pomDependency, "<scope>", "</scope>");
		else
			scope = "";

		if (pomDependency.contains("<optional>"))
			optional = StringUtils.substringBetween(pomDependency, "<optional>", "</optional>");
		else
			optional = "";

		tfGIDInsert.setText(groupId);
		tfAIDInsert.setText(artifactId);
		tfVersionInsert.setText(version);
		tfScopeInsert.setText(scope);
		tfOptionalInsert.setText(optional);
		taDescriptionInsert.setText(description);

		tfInsertName.requestFocus();
	}

	@FXML
	private void copyFromClipboardAction(ActionEvent event) {
		String pomString = ClipBoardCreation.clipValueFromClipboard();
		textInsertDependency.setText(pomString);
	}

//	==============SEARCH WEBSEARCH========================
	@FXML
	private void searchWEBAction(ActionEvent event) {
		// first take selected tab of 'tabPaneBrowser'
		Tab selectedTab = tabPaneBrowser.getSelectionModel().getSelectedItem();
		// history tab aanenkil error varum athu tadayaan
		if (selectedTab.getText().equals("history")) {
			addressBarWEBTF.setText("history [search from another tab]");
			return;
		}
		// take webview node from tab & get engine
		// System.out.println(selectedTab.getContent().getClass());
		WebView selectedWebview = (WebView) selectedTab.getContent();
		WebEngine selectedEngineRelatedToWebView = selectedWebview.getEngine();

		String https = "https://";
		if (addressBarWEBTF.getText().contains("https://") || addressBarWEBTF.getText().contains("http://")) {
			selectedEngineRelatedToWebView.load(addressBarWEBTF.getText());
		} else {
			selectedEngineRelatedToWebView.load(https + addressBarWEBTF.getText());
		}
	}

	@FXML
	private void searchTextFieldWEBAction(ActionEvent event) {
		searchWEBAction(event);
	}

//	===============REFRESH PAGE=====================
	@FXML
	private void refreshWEBAction(ActionEvent event) {
		// first take selected tab of 'tabPaneBrowser'
		Tab selectedTab = tabPaneBrowser.getSelectionModel().getSelectedItem();
		// history tab aanenkil error varum athu tadayaan
		if (selectedTab.getText().equals("history")) {
			addressBarWEBTF.setText("history [search from another tab]");
			return;
		}
		// take webview node from tab & get engine
		// System.out.println(selectedTab.getContent().getClass());
		WebView selectedWebview = (WebView) selectedTab.getContent();
		WebEngine selectedEngineRelatedToWebView = selectedWebview.getEngine();

		selectedTab.setTooltip(new Tooltip("hello"));

		selectedEngineRelatedToWebView.reload();
		historySaveOperation(selectedEngineRelatedToWebView.getLocation());
	}

//	===============FORWARD PAGE=======================
	@FXML
	private void forwardToWEBAction(ActionEvent event) {
		// first take selected tab of 'tabPaneBrowser'
		Tab selectedTab = tabPaneBrowser.getSelectionModel().getSelectedItem();
		// history tab aanenkil error varum athu tadayaan
		if (selectedTab.getText().equals("history")) {
			addressBarWEBTF.setText("history [search from another tab]");
			return;
		}
		// take webview node from tab & get engine
		// System.out.println(selectedTab.getContent().getClass());
		WebView selectedWebview = (WebView) selectedTab.getContent();
		WebEngine selectedEngineRelatedToWebView = selectedWebview.getEngine();

		final WebHistory history = selectedEngineRelatedToWebView.getHistory();
		ObservableList<WebHistory.Entry> entryList = history.getEntries();
		int currentIndex = history.getCurrentIndex();
//		System.out.println(currentIndex); // 0
//		System.out.println(entrList.toString());
		// Network illenkil forward adichaal error block cheyyaan/entry list empty ayi
		// forward cheyyumbol
		if (entryList.isEmpty()) {
			System.out.println("OOOO####= EntryList is EMPTY =####OOOO");
			return;
		}
		String returnURL = goForward(currentIndex, entryList);
		String currentURL = entryList.get(currentIndex).getUrl();
		if (!currentURL.equals(returnURL)) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					history.go(1);
				}
			});
		} else {
			System.out.println("at last(final) page");
		}
	}

//	=============PREVIOUS PAGE==================
	@FXML
	private void backToWEBAction(ActionEvent event) {
		// first take selected tab of 'tabPaneBrowser'
		Tab selectedTab = tabPaneBrowser.getSelectionModel().getSelectedItem();
		// history tab aanenkil error varum athu tadayaan
		if (selectedTab.getText().equals("history")) {
			addressBarWEBTF.setText("history [search from another tab]");
			return;
		}
		// take webview node from tab & get engine
		// System.out.println(selectedTab.getContent().getClass());
		WebView selectedWebview = (WebView) selectedTab.getContent();
		WebEngine selectedEngineRelatedToWebView = selectedWebview.getEngine();

		final WebHistory history = selectedEngineRelatedToWebView.getHistory();
		ObservableList<WebHistory.Entry> entryList = history.getEntries();
		int currentIndex = history.getCurrentIndex();
//		System.out.println(currentIndex); // 0 at start page
//		System.out.println(entryList.toString());//
		// Network illenkil back adichaal error block cheyyaan/entry list empty ayi back
		// cheyyumbol
		if (entryList.isEmpty()) {
			System.out.println("OOOO####= EntryList is EMPTY =####OOOO");
			return;
		}
		String returnURL = goBack(currentIndex, entryList);
		String currentURL = entryList.get(currentIndex).getUrl();
		if (!currentURL.equals(returnURL)) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					history.go(-1);
				}
			});
		} else {
			System.out.println("at first(Starting) page");
		}
	}

//===================BACKWARD === FORWARD===========
	public String goBack(int currentIndex, ObservableList<WebHistory.Entry> entryList) {
		return entryList.get(currentIndex > 0 ? currentIndex - 1 : currentIndex).getUrl();
	}

	public String goForward(int currentIndex, ObservableList<WebHistory.Entry> entryList) {
		return entryList.get(currentIndex < entryList.size() - 1 ? currentIndex + 1 : currentIndex).getUrl();

	}

//	==================HISTORY AREA================
	@FXML
	private void historyWEBAction(ActionEvent event) {
		designHistoryTab();
	}

//===================DOWNLOAD AREA======================
	private void downloadOption(WebEngine webengine) {
		webengine.locationProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldUrl, String newUrl) {

				String ext = newUrl.substring(newUrl.lastIndexOf("."), newUrl.length());

				File file = new File(System.getProperty("user.home") + "\\Downloads\\javafxdownload\\");
				if (!file.exists()) {
					file.mkdirs();
				}

				String[] downloadableExtensions = { ".doc", ".xls", ".zip", ".exe", ".rar", ".pdf", ".jar", ".png",
						".jpg", ".gif" };
				if (!Arrays.asList(downloadableExtensions).contains(ext)) {
					System.out.println("NOT A DOWNLOADABLE LINK");
					return;
				}
				System.out.println("DOWNLOADABLE LINK GO FORWARD");

				String[] downloadName = newUrl.split("/");
				String downloadwithPathAndName = "";
				if (!folderOpenCheckBox.isSelected()) {
//				System.out.println(downloadName[downloadName.length - 1]);
					downloadwithPathAndName = file.getAbsolutePath() + "\\" + downloadName[downloadName.length - 1];
//				System.out.println(downloadwithPathAndName);
					if ((new File(downloadwithPathAndName)).exists()) {
						System.out.println("file already exists");
						downloadwithPathAndName = downloadwithPathAndName.replace(ext, "_1" + ext);
					}
				} else {
//				DirectoryChooser directoryChooser = new DirectoryChooser();
//				directoryChooser.setInitialDirectory(file);
//				File selectedDirectory = directoryChooser.showDialog(stage);
//				System.out.println(selectedDirectory.getAbsolutePath());

					Stage stage = (Stage) webWEB.getScene().getWindow();
					FileChooser fileChooser = new FileChooser();
					fileChooser.setInitialDirectory(file);
					// Set extension filter for text files
					FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
							ext.toUpperCase() + " files (*" + ext + ")", "*" + ext);
					fileChooser.getExtensionFilters().add(extFilter);
					fileChooser.setInitialFileName(downloadName[downloadName.length - 1]);

					File forSaveFile = fileChooser.showSaveDialog(stage);
					if (forSaveFile == null) {
						System.out.println("CANCEL SAVING");
						return;
					}
					downloadwithPathAndName = forSaveFile.toString();
//					System.out.println(downloadwithPathAndName);
				}

				JFXProgressBar progressBar = new JFXProgressBar();
				progressBar.setPrefWidth(350);
				progressBar.getStyleClass().add("jfx-progress-bar-success");
				progressBar.setPrefWidth(100);
				HBox.setMargin(progressBar, new Insets(0, 10, 0, 0));
				Label label = new Label();
				label.setStyle("-fx-text-fill:white;");
//				hboxProgress.getChildren().clear(); //ithinu pakaram DownnloadTask class Timeline upayogichu remove cheyyunnu
				hboxProgress.setSpacing(10.0);
				hboxProgress.getChildren().addAll(label, progressBar);

				Task<Void> task = new DownnloadTask(newUrl, downloadwithPathAndName, hboxProgress, progressBar, label);

				progressBar.progressProperty().bind(task.progressProperty());
				label.textProperty().bind(task.messageProperty());

				Thread thread = new Thread(task);
				thread.setDaemon(true);
				thread.start();

			}
		});
	}

//	=================NEW TAB AREA=========================
	@FXML
	private void newTabForBrowserAction(ActionEvent event) {
		int inum = 1;
		boolean isNumPlusMode = false;
		String lastTabName = "LAST_NAME";
		// aadyam last le tab ne st leku edukkunnu
		Tab t = tabPaneBrowser.getSelectionModel().getSelectedItem();
		if (t == null) {
			System.out.println("NO tabssssss");
			inum = 1;
		} else {
			t = tabPaneBrowser.getTabs().get(tabPaneBrowser.getTabs().size() - 1);
			lastTabName = t.getText();
			System.out.println("last tab:" + lastTabName);
			if (lastTabName.equals("history")) {
				System.out.println("LAST TAB IS HISTORY");
				if (tabPaneBrowser.getTabs().size() == 1) {
					inum = 1;
				} else {
					t = tabPaneBrowser.getTabs().get(tabPaneBrowser.getTabs().size() - 2);
					lastTabName = t.getText();
					isNumPlusMode = true;
				}
			} else {
				isNumPlusMode = true;
			}
		}
		if (isNumPlusMode) {
			String[] arr = lastTabName.split("tab");
			String ltabname = "";
			for (String s : arr) {
//				System.out.println(s); // number kittaan
				ltabname = s;
			}
			inum = Integer.parseInt(ltabname);
			inum = inum + 1;
		}
		WebView newBrowse = new WebView();
		WebEngine newEngine = newBrowse.getEngine();
		newEngine.load("https://www.google.com/");
		webViewNewLink(newEngine);
		downloadOption(newEngine);
		Tab tab = new Tab("tab" + inum, newBrowse);
		tab.setTooltip(new Tooltip("https://www.google.com/"));
		tab.setContextMenu(contextMenuCreating(tab));
		tooltipLinkOperation(tab, newEngine);
		tabPaneBrowser.getTabs().add(tab);
		// tab focus to newly created
		tabPaneBrowser.getSelectionModel().select(tab);
		historySaveOperation("https://www.google.com/");

//		Tab selecttab = tabPaneBrowser.getSelectionModel().getSelectedItem();
//		String str = selecttab.getText();
//		String i = selecttab.getId();
//		System.out.println("tab name:" + str + "\n tab id:" + i);
//      //no of tabs
//		int size = tabPaneBrowser.getTabs().size();
//		System.out.println("size===>" + size);
	}

//	================POPUP NEW LINK =========================
	private void webViewNewLink(WebEngine eng) {
		eng.setCreatePopupHandler((PopupFeatures config) -> {
			System.out.println("new popup ready");
			WebView newBrowserLink = new WebView();
			Tab newTab = createNewLinkTab();
			newTab.setContent(newBrowserLink);
			// tab focus to newly created
			tabPaneBrowser.getSelectionModel().select(newTab);
			WebEngine newlyOpenEngine = newBrowserLink.getEngine();
			newTab.setTooltip(new Tooltip(newlyOpenEngine.getLocation()));
			webViewNewLink(newlyOpenEngine);
			downloadOption(newlyOpenEngine);
			tooltipLinkOperation(newTab, newlyOpenEngine);
			return newBrowserLink.getEngine();
		});
	}

//	============POPUP TO NEW TAB============================
	private Tab createNewLinkTab() {
		int inum = 1;
		boolean isNumPlusMode = false;
		String lastTabName = "LAST_NAME";
		// aadyam last le tab ne st leku edukkunnu
		Tab t = tabPaneBrowser.getSelectionModel().getSelectedItem();
		if (t == null) {
			System.out.println("NO tabssssss");
			inum = 1;
		} else {
			t = tabPaneBrowser.getTabs().get(tabPaneBrowser.getTabs().size() - 1);
			lastTabName = t.getText();
//			System.out.println("last tab:" + lastTabName);
			if (lastTabName.equals("history")) {
				System.out.println("LAST TAB IS HISTORY");
				if (tabPaneBrowser.getTabs().size() == 1) {
					inum = 1;
				} else {
					t = tabPaneBrowser.getTabs().get(tabPaneBrowser.getTabs().size() - 2);
					lastTabName = t.getText();
					isNumPlusMode = true;
				}
			} else {
				isNumPlusMode = true;
			}
		}
		if (isNumPlusMode) {
			String[] arr = lastTabName.split("tab");
			String ltabname = "";
			for (String s : arr) {
				ltabname = s;
			}
			inum = Integer.parseInt(ltabname);
			inum = inum + 1;
		}
		Tab tab = new Tab("tab" + inum);
		tab.setContextMenu(contextMenuCreating(tab));
		tabPaneBrowser.getTabs().add(tab);

		return tab;
	}

//	=====================HISTORY TAB DESIGN =========================
	private void designHistoryTab() {
		ObservableList<Tab> tabsList = tabPaneBrowser.getTabs();
		for (Tab tb : tabsList) {
			if (tb.getText().equals("history")) {
				System.out.println("ALREADY A HISTORY TAB");
				tabPaneBrowser.getSelectionModel().select(tb);
				return;
			}
		}

		Tab tab = new Tab("history", designHistoryTabContent());
		tabPaneBrowser.getTabs().add(tab);
		tab.setTooltip(new Tooltip("history"));
		tab.setContextMenu(contextMenuCreating(tab));
		// tab focus to newly created
		tabPaneBrowser.getSelectionModel().select(tab);
	}

	private VBox designHistoryTabContent() {
		VBox vbox = new VBox();
		AnchorPane paneHead = new AnchorPane();
		paneHead.setStyle("-fx-background-color:linear-gradient(#938C9E,grey);");
		paneHead.setPrefHeight(50);
		Label label = new Label("HISTORY");
		label.setPrefHeight(50);
		label.setFont(Font.font("Berlin Sans FB", FontWeight.BOLD, FontPosture.REGULAR, 25.0));
		label.setUnderline(true);
		label.setStyle("-fx-text-fill: #EBF3D8 ; ");
		label.setAlignment(Pos.CENTER);
		AnchorPane.setLeftAnchor(label, 0.0);
		AnchorPane.setRightAnchor(label, 0.0);
		paneHead.getChildren().add(label);
		AnchorPane paneBody = new AnchorPane();
		VBox.setVgrow(paneBody, Priority.ALWAYS);
		paneBody.getChildren().add(urlListView);
		AnchorPane.setTopAnchor(urlListView, 0.0);
		AnchorPane.setBottomAnchor(urlListView, 0.0);
		AnchorPane.setRightAnchor(urlListView, 0.0);
		AnchorPane.setLeftAnchor(urlListView, 0.0);

		urlListView.setItems(historyObsList);

		urlListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> param) {
				HBox hbox = new HBox();
//				hbox.setStyle("-fx-background-color:black;");
				Button btn = new Button("Delete");
				String styleBtn = "-fx-font-family:\"Sitka Heading\"; " + "-fx-font-size:15px; -fx-font-weight:bold;"
						+ "-fx-text-fill:linear-gradient(#D5E6A9,#F266BF);";
				btn.setStyle(styleBtn + "-fx-background-color: linear-gradient(#5B58AA,#3E1B52); ");
				btn.setOnMouseEntered(e -> {
					btn.setStyle(styleBtn + "-fx-background-color: linear-gradient(#941D2D,#EB1E08); ");
				});
				btn.setOnMouseExited(e -> {
					btn.setStyle(styleBtn + "-fx-background-color: linear-gradient(#5B58AA,#3E1B52); ");
				});
				Label lbl = new Label();
				lbl.setStyle("-fx-text-fill:white; -fx-font-size:15px;");
				lbl.setOnMouseEntered(e -> {
					lbl.setStyle("-fx-text-fill:red; -fx-underline:true; -fx-font-size:15px;");
				});
				lbl.setOnMouseExited(e -> {
					lbl.setStyle("-fx-text-fill:white; -fx-font-size:15px;");
				});
				lbl.setOnMousePressed(e -> {
					String urlToLoad = lbl.getText();
					newTabFromHistoryLink(urlToLoad);
				});
				Pane pane = new Pane();
				Image webhistoryimg = new Image("/com/ar/webhistory.png", 30.0, 30.0, true, true);
				ImageView imgv = new ImageView(webhistoryimg);
				hbox.getChildren().addAll(btn, imgv, lbl, pane);
				HBox.setHgrow(pane, Priority.ALWAYS);

				ListCell<String> cellu = new ListCell<String>() {
					@Override
					protected void updateItem(String item, boolean empty) {

						btn.setOnAction(e -> {
							String removeStr = (String) getItem();
							System.out.println(removeStr);
							historyList.remove(removeStr);
							getListView().getItems().remove(getItem());
							urlListView.refresh();
							autoComp.dispose();
							autoComp = TextFields.bindAutoCompletion(addressBarWEBTF, historyList);
							autoComp.minWidthProperty().bind(addressBarWEBTF.widthProperty());
						});
						setText(null);
						setGraphic(null);
						super.updateItem(item, empty);
						String url = ((String) item);
						if (url != null) {
							lbl.setText(url);
							setGraphic(hbox);
						}

					}

				};
				return cellu;
			}
		});

		vbox.getChildren().addAll(paneHead, paneBody);

		return vbox;
	}

//===================NEW TAB FROM HISTORY=================
	private void newTabFromHistoryLink(String url) {
		int inum = 1;
		boolean isNumPlusMode = false;
		String lastTabName = "LAST_NAME";
		// aadyam last le tab ne st leku edukkunnu
		Tab t = tabPaneBrowser.getSelectionModel().getSelectedItem();
		if (t == null) {
			System.out.println("NO tabssssss");
			inum = 1;
		} else {
			t = tabPaneBrowser.getTabs().get(tabPaneBrowser.getTabs().size() - 1);
			lastTabName = t.getText();
			System.out.println("last tab:" + lastTabName);
			if (lastTabName.equals("history")) {
				System.out.println("LAST TAB IS HISTORY");
				if (tabPaneBrowser.getTabs().size() == 1) {
					inum = 1;
				} else {
					t = tabPaneBrowser.getTabs().get(tabPaneBrowser.getTabs().size() - 2);
					lastTabName = t.getText();
					isNumPlusMode = true;
				}
			} else {
				isNumPlusMode = true;
			}
		}
		if (isNumPlusMode) {
			String[] arr = lastTabName.split("tab");
			String ltabname = "";
			for (String s : arr) {
//				System.out.println(s); // number kittaan
				ltabname = s;
			}
			inum = Integer.parseInt(ltabname);
			inum = inum + 1;
		}
		WebView newBrowse = new WebView();
		WebEngine newEngine = newBrowse.getEngine();
		newEngine.load(url);
		webViewNewLink(newEngine);
		downloadOption(newEngine);
		Tab tab = new Tab("tab" + inum, newBrowse);
		tab.setTooltip(new Tooltip(url));
		tab.setContextMenu(contextMenuCreating(tab));
		tooltipLinkOperation(tab, newEngine);
		tabPaneBrowser.getTabs().add(tab);
		// tab focus to newly created
		tabPaneBrowser.getSelectionModel().select(tab);
		historySaveOperation(url);
	}

//	===============CONTEXT MENU FOR ALL TABS AND OPERATIONS==================

	private ContextMenu contextMenuCreating(Tab tab) {
		ContextMenu contextMenu = new ContextMenu();
//		contextMenu.setId(tab.getText());
		MenuItem menuClose = new MenuItem("Close");
		MenuItem menuCloseOthers = new MenuItem("Close Others");
		MenuItem menuCloseLeft = new MenuItem("Close Tabs to the Left");
		MenuItem menuCloseRight = new MenuItem("Close Tabs to the Right");
		MenuItem menuCloseAll = new MenuItem("Close All");

		menuClose.addEventHandler(EventType.ROOT, (Event event) -> {
			closeTabAction(tab);
		});
		menuCloseOthers.addEventHandler(EventType.ROOT, (Event event) -> {
			closeOthersTabAction(tab);
		});
		menuCloseLeft.addEventHandler(EventType.ROOT, (Event event) -> {
			closeTabsToLeftAction(tab);
		});
		menuCloseRight.addEventHandler(EventType.ROOT, (Event event) -> {
			closeTabsToRightAction(tab);
		});
		menuCloseAll.addEventHandler(EventType.ROOT, (Event event) -> {
			closeAllTabsAction(tab);
		});

		contextMenu.getItems().addAll(menuClose, menuCloseOthers, menuCloseLeft, menuCloseRight, menuCloseAll);

		contextMenu.setOnShown(e -> {
			tabList = tab.getTabPane().getTabs();
			sizeIndex = tabList.size() - 1;
			c = 0;
			for (Tab tb : tabList) {
				if (tab == tb) {
					break;
				}
				c++;
			}

			if (c == 0) {
				if (c == sizeIndex) { // oru tab maatram aavumbol
					menuCloseLeft.setVisible(false);
					menuCloseRight.setVisible(false);
					menuCloseOthers.setVisible(false);
					menuCloseAll.setVisible(false);
				} else { // select tab aadyatetum onnilathikam tabum undenkil
					menuCloseLeft.setVisible(false);
					menuCloseRight.setVisible(true);
					menuCloseOthers.setVisible(true);
					menuCloseAll.setVisible(true);
				}
			} else if (c == sizeIndex && c != 0) { // select tab last onnilathikam tabum undenkil
				menuCloseRight.setVisible(false);
				menuCloseLeft.setVisible(true);
				menuCloseOthers.setVisible(true);
				menuCloseAll.setVisible(true);
			} else {
				menuCloseLeft.setVisible(true);
				menuCloseRight.setVisible(true);
				menuCloseOthers.setVisible(true);
				menuCloseAll.setVisible(true);
			}
		});

		contextMenu.focusedProperty().addListener((obs, olVal, newVal) -> {
			if (newVal == true) {
				tab.setStyle("-fx-background-color: linear-gradient(skyblue,yellow);");
			} else {
				tab.setStyle("");
			}
		});

		return contextMenu;
	}

	private void closeTabAction(Tab tab) {
		tab.getTabPane().getTabs().remove(tab);
	}

	private void closeAllTabsAction(Tab tab) {
		tabPaneBrowser.getTabs().removeAll(tabPaneBrowser.getTabs());
	}

	private void closeOthersTabAction(Tab tab) {
//    	System.out.println(tab.getText());
		tabList = tab.getTabPane().getTabs();
		c = 0;
		for (Tab tb : tabList) {
			if (tab == tb) {
//    			System.out.println("HERE "+c); 
				break;
			}
			c++;
		}
//    	System.out.println("OUT "+c);
		sizeIndex = tabList.size() - 1; // ie tab 0 il ninnanu tughunnadhu
		///////// REVERSIL DELETE CHEYYANAM lastil ninnu first leku ellenkil error
		///////// kaanikkum.
		j = sizeIndex;
		while (j >= 0) {
			if (j != c) {
				tabPaneBrowser.getTabs().remove(j);
			}
			j--;
		}

	}

	private void closeTabsToLeftAction(Tab tab) {
		tabList = tab.getTabPane().getTabs();
		c = 0;
		for (Tab tb : tabList) {
			if (tab == tb) {
				break;
			}
			c++;
		}
		///////// REVERSIL DELETE CHEYYANAM selected tabinte mumbulla tabil ninnu first
		///////// leku ellenkil error kaanikkum.
		j = c - 1;
		while (j >= 0) {
			tabPaneBrowser.getTabs().remove(j);
			j--;
		}
	}

	private void closeTabsToRightAction(Tab tab) {
		tabList = tab.getTabPane().getTabs();
		c = 0;
		for (Tab tb : tabList) {
			if (tab == tb) {
				break;
			}
			c++;
		}
		sizeIndex = tabList.size() - 1; // ie tab 0 il ninnanu tughunnadhu
		///////// REVERSIL DELETE CHEYYANAM lastil ninnu venda tabil vare,avidannu break
		///////// cheyyukha, ellenkil error kaanikkum.
		j = sizeIndex;
		while (j >= 0) {
			if (j == c) {
				break;
			}
			tabPaneBrowser.getTabs().remove(j);
			j--;
		}
	}
//	===================================

}

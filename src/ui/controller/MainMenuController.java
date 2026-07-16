package ui.controller;

import exception.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.engine.MCTSAIBrain;
import logic.engine.SimpleAIBrain;
import logic.engine.GameEngine;
import logic.engine.Map;
import logic.enums.PlayerRole;
import logic.models.*;
import logic.save.SaveManager;
import logic.sound.SFXManager;
import logic.sound.SoundManager;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class MainMenuController {

    private final Image selectedAIImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/Icons/AISelected.png")));
    private final Image selectedHardAIImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/Icons/AIHardSelected.png")));
    private final Image unselectedAIImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/Icons/AIUnselected.png")));
    private final Image selectedHumanImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/Icons/HumanSelected.png")));
    private final Image unselectedHumanImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/Icons/HumanUnselected.png")));
    private int playerCount = 0;
    private static double gameSoundVolume = 1.0;
    List<Player> players = new ArrayList<>();
    GameBoardController controller;
    BorderPane root;
    @FXML
    private BorderPane borderPane;
    @FXML

    private final ObservableList<FileItem> saveList = FXCollections.observableArrayList();

    @FXML
    private Label Player1Label;

    @FXML
    private Label Player2Label;

    @FXML
    private Label Player3Label;

    @FXML
    private Label Player4Label;

    @FXML
    private TextField PlayerName1;

    @FXML
    private TextField PlayerName2;

    @FXML
    private TextField PlayerName3;

    @FXML
    private TextField PlayerName4;

    @FXML
    private Label PlayerRole1;

    @FXML
    private Label PlayerRole2;

    @FXML
    private Label PlayerRole3;

    @FXML
    private Label PlayerRole4;

    @FXML
    private HBox P1, P2, P3, P4;

    @FXML
    private VBox P2Hard, P3Hard, P4Hard;

    @FXML
    private CheckBox P2HardCheckBox, P3HardCheckBox, P4HardCheckBox;


    @FXML
    private ImageView TheHackerCEO;
    private boolean isTheHackerCEOSelected = false;
    @FXML
    private ImageView TheTechGuruCTO;
    private boolean isTheTechGuruCTOSelected = false;

    @FXML
    private ImageView TheVCFunded;
    private boolean isTheVCFundedSelected = false;

    @FXML
    private ImageView NoRole;


    @FXML
    private Group GameLobby;

    @FXML
    private Group MainMenu;

    @FXML
    private Group LoadMenu;

    @FXML
    private Group Settings;

    @FXML
    private Group AboutUs;


    @FXML
    private TableColumn<FileItem, String> saveDateCol;

    @FXML
    private TableColumn<FileItem, String> saveNameCol;

    @FXML
    private TableView<FileItem> saveTable;


    @FXML
    private ToggleButton P1AIToggle;

    @FXML
    private ToggleButton P1HumanToggle;

    @FXML
    private ToggleGroup P1ToggleGroup;

    @FXML
    private ToggleButton P2AIToggle;

    @FXML
    private ToggleButton P2HumanToggle;

    @FXML
    private ToggleGroup P2ToggleGroup;

    @FXML
    private ToggleButton P3AIToggle;

    @FXML
    private ToggleButton P3HumanToggle;

    @FXML
    private ToggleGroup P3ToggleGroup;

    @FXML
    private ToggleButton P4AIToggle;

    @FXML
    private ToggleButton P4HumanToggle;

    @FXML
    private ToggleGroup P4ToggleGroup;

    // ========================== Settings ==========================
    @FXML
    private Slider MenuVolumeSlider, GameVolumeSlider, SFXVolumeSlider;

    @FXML
    private Label MenuVolumeLabel, GameVolumeLabel, SFXVolumeLabel;

    private ImageView currentlySelectedImageView = null;
    private Set<ImageView> lockedRoles = new HashSet<>();

    @FXML
    public void initialize() {
        try {
            SFXManager.preload("MouseEnter.mp3", "Error 2.mp3");
        } catch (SFXNotFoundException e) {
            SFXManager.play("Error 2.mp3");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load files");
            alert.setContentText(e.getMessage());
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
            alert.showAndWait();
        }
        try {
            SoundManager.playBackgroundMusic("MainMenu.mp3");
        } catch (MusicFileNotFoundException e) {
            SFXManager.play("Error 2.mp3");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load files");
            alert.setContentText(e.getMessage());
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
            alert.showAndWait();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/GameBoard.fxml"));
            root = loader.load();

            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Player1Label.setTextFill(Color.web(GameEngine.PLAYER1COLOR));
        Player2Label.setTextFill(Color.web(GameEngine.PLAYER2COLOR));
        Player3Label.setTextFill(Color.web(GameEngine.PLAYER3COLOR));
        Player4Label.setTextFill(Color.web(GameEngine.PLAYER4COLOR));

        saveTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        saveNameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        saveDateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        saveNameCol.setReorderable(false);
        saveDateCol.setReorderable(false);
        saveTable.setItems(saveList);

        loadSaveFiles();

        P1ToggleGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (oldToggle != null) {
                if (isHumanToggle(((ToggleButton) oldToggle).getId())) {
                    setToggleImage((ToggleButton) oldToggle, unselectedHumanImage);
                } else if (!isHumanToggle(((ToggleButton) oldToggle).getId())) {
                    setToggleImage((ToggleButton) oldToggle, unselectedAIImage);
                }
            }

            if (newToggle != null) {
                if (isHumanToggle(((ToggleButton) newToggle).getId())) {
                    setToggleImage((ToggleButton) newToggle, selectedHumanImage);
                } else if (!isHumanToggle(((ToggleButton) newToggle).getId())) {
                    setToggleImage((ToggleButton) newToggle, selectedAIImage);
                }
            }
        });

        P2ToggleGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (oldToggle != null) {
                if (isHumanToggle(((ToggleButton) oldToggle).getId())) {
                    setToggleImage((ToggleButton) oldToggle, unselectedHumanImage);
                } else if (!isHumanToggle(((ToggleButton) oldToggle).getId())) {
                    setToggleImage((ToggleButton) oldToggle, unselectedAIImage);
                    P2Hard.setVisible(false);
                }
            }

            if (newToggle != null) {
                if (isHumanToggle(((ToggleButton) newToggle).getId())) {
                    setToggleImage((ToggleButton) newToggle, selectedHumanImage);
                } else if (!isHumanToggle(((ToggleButton) newToggle).getId())) {
                    setToggleImage((ToggleButton) newToggle, selectedAIImage);
                    P2Hard.setVisible(true);
                    P2HardCheckBox.setSelected(false);
                }
            }
        });

        P3ToggleGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (oldToggle != null) {
                if (isHumanToggle(((ToggleButton) oldToggle).getId())) {
                    setToggleImage((ToggleButton) oldToggle, unselectedHumanImage);
                } else if (!isHumanToggle(((ToggleButton) oldToggle).getId())) {
                    setToggleImage((ToggleButton) oldToggle, unselectedAIImage);
                    P3Hard.setVisible(false);
                }
            }

            if (newToggle != null) {
                if (isHumanToggle(((ToggleButton) newToggle).getId())) {
                    setToggleImage((ToggleButton) newToggle, selectedHumanImage);
                } else if (!isHumanToggle(((ToggleButton) newToggle).getId())) {
                    setToggleImage((ToggleButton) newToggle, selectedAIImage);
                    P3Hard.setVisible(true);
                    P3HardCheckBox.setSelected(false);
                }
            }
        });

        P4ToggleGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (oldToggle != null) {
                if (isHumanToggle(((ToggleButton) oldToggle).getId())) {
                    setToggleImage((ToggleButton) oldToggle, unselectedHumanImage);
                } else if (!isHumanToggle(((ToggleButton) oldToggle).getId())) {
                    setToggleImage((ToggleButton) oldToggle, unselectedAIImage);
                    P4Hard.setVisible(false);
                }
            }

            if (newToggle != null) {
                if (isHumanToggle(((ToggleButton) newToggle).getId())) {
                    setToggleImage((ToggleButton) newToggle, selectedHumanImage);
                } else if (!isHumanToggle(((ToggleButton) newToggle).getId())) {
                    setToggleImage((ToggleButton) newToggle, selectedAIImage);
                    P4Hard.setVisible(true);
                    P4HardCheckBox.setSelected(false);
                }
            }
        });

        P2HardCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                setToggleImage(P2AIToggle, selectedHardAIImage);
            } else {
                setToggleImage(P2AIToggle, selectedAIImage);
            }
        });

        P3HardCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                setToggleImage(P3AIToggle, selectedHardAIImage);
            } else {
                setToggleImage(P3AIToggle, selectedAIImage);
            }
        });

        P4HardCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                setToggleImage(P4AIToggle, selectedHardAIImage);
            } else {
                setToggleImage(P4AIToggle, selectedAIImage);
            }
        });

        MenuVolumeSlider.setValue(SoundManager.getVolume() * 100);
        GameVolumeSlider.setValue(gameSoundVolume * 100);
        SFXVolumeSlider.setValue(SFXManager.getVolume() * 100);

        MenuVolumeLabel.setText(String.valueOf((int) SoundManager.getVolume() * 100));
        GameVolumeLabel.setText(String.valueOf((int) gameSoundVolume * 100));
        SFXVolumeLabel.setText(String.valueOf((int) SFXManager.getVolume() * 100));

        MenuVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            MenuVolumeLabel.setText(String.valueOf((newVal.intValue())));
            if (oldVal.doubleValue() == 0.0 && newVal.doubleValue() > 0.0) {
                SoundManager.resumeMusic();
            }
            SoundManager.setVolume(newVal.doubleValue() / 100);
        });

        GameVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            GameVolumeLabel.setText(String.valueOf((newVal.intValue())));
            gameSoundVolume = newVal.doubleValue() / 100;
        });

        SFXVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            SFXVolumeLabel.setText(String.valueOf((newVal.intValue())));
            SFXManager.setVolume(newVal.doubleValue() / 100);
        });
    }

    private boolean isHumanToggle(String Id) {
        return Id.substring(2).equals("HumanToggle");
    }

    private void setToggleImage(ToggleButton button, Image image) {
        ImageView imageView = (ImageView) button.getGraphic();
        if (imageView != null) {
            imageView.setImage(image);
        }
    }

    public String getPlayerSelection(int N) {
        ToggleGroup[] toggleGroups = new ToggleGroup[]{P1ToggleGroup, P2ToggleGroup, P3ToggleGroup, P4ToggleGroup};
        try {
            ToggleButton selected = (ToggleButton) (toggleGroups[N]).getSelectedToggle();
            return selected.getId().substring(2);
        } catch (NullPointerException e) {
            throw new PlayerTypeNotSelectedException(N + 1);
        }
    }

    @FXML
    void LoadMenuButtonsMouseEnter(MouseEvent event) {
        SFXManager.play("MouseEnter.mp3");
        ((Button) event.getSource()).setStyle("-fx-background-color:  #1a1a1a; -fx-border-width: 3; -fx-border-color:  #d4af37");
    }

    @FXML
    void LoadMenuButtonsMouseExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle("-fx-background-color:  #1a1a1a");
    }

    @FXML
    void onBacktoMainMenuButton(ActionEvent event) {
        ResetAllPages();
        MainMenu.setVisible(true);
        MainMenu.setMouseTransparent(false);
    }

    @FXML
    void onLoadGame(ActionEvent event) throws IOException {
        GameEngine gameEngine;
        try {
            gameEngine = SaveManager.load(saveTable.getSelectionModel().getSelectedItem().getFile());
        } catch (IOException | ClassNotFoundException e) {
            SFXManager.play("Error 2.mp3");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to read file");
            Label messageLabel = new Label("The file might be corrupted, inaccessible, or in use by another program.");
            messageLabel.setWrapText(true);
            messageLabel.setPrefWidth(450);
            alert.getDialogPane().setContent(messageLabel);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
            dialogPane.setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
            return;
        } catch (NullPointerException e) {
            SFXManager.play("Error 2.mp3");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No File Selected");
            alert.setContentText("Please choose a file before loading.");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/GameBoard.fxml"));
        BorderPane root = loader.load();

        GameBoardController controller = loader.getController();

        controller.setGameEngine(gameEngine);
        SimpleAIBrain.setController(controller);
        MCTSAIBrain.setController(controller);
        controller.initialize(gameEngine);

        Stage gameBoardStage = new Stage();
        gameBoardStage.setScene(new Scene(root));
        gameBoardStage.setTitle("Silicon Valley: The Tech Cartel");
        gameBoardStage.setResizable(false);

        gameBoardStage.show();
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    private void loadSaveFiles() {
        saveList.clear();

        File saveFolder = new File("saves");

        if (!saveFolder.exists()) {
            saveFolder.mkdir();
        }

        File[] files = saveFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".sv")) {
                    saveList.add(new FileItem(file));
                }
            }
        }
    }

    @FXML
    void OnStartANewGame(ActionEvent event) {
        ResetAllPages();
        GameLobby.setVisible(true);
        GameLobby.setMouseTransparent(false);
        disableGroup();
    }

    @FXML
    void OnLoadAGame(ActionEvent event) {
        ResetAllPages();
        LoadMenu.setVisible(true);
        LoadMenu.setMouseTransparent(false);
    }


    @FXML
    void onSettings(ActionEvent event) {
        ResetAllPages();
        Settings.setVisible(true);
        Settings.setMouseTransparent(false);
    }

    @FXML
    void onAboutUs(ActionEvent event) {
        ResetAllPages();
        AboutUs.setVisible(true);
        AboutUs.setMouseTransparent(false);
    }

    void ResetAllPages() {
        GameLobby.setVisible(false);
        GameLobby.setMouseTransparent(true);
        MainMenu.setVisible(false);
        MainMenu.setMouseTransparent(true);
        LoadMenu.setVisible(false);
        LoadMenu.setMouseTransparent(true);
        AboutUs.setVisible(false);
        AboutUs.setMouseTransparent(true);
        Settings.setVisible(false);
        Settings.setMouseTransparent(true);
    }

    @FXML
    void ButtonsMouseEnter(MouseEvent event) {
        SFXManager.play("MouseEnter.mp3");
        ((Button) (event.getSource())).setStyle("-fx-background-color: #1a1a1a; -fx-border-color: #d4af37 ;-fx-border-width: 5;");
    }

    @FXML
    void ButtonsMouseExit(MouseEvent event) {
//        SFXManager.play("MouseExit.mp3");
        ((Button) (event.getSource())).setStyle("-fx-background-color: #1a1a1a;");
    }

    @FXML
    void onExitButton(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void RoleMouseEnter(MouseEvent event) {
        SFXManager.play("MouseEnter.mp3");
        if (lockedRoles.contains(event.getSource()) || (ImageView) event.getSource() == currentlySelectedImageView) {
            return;
        }
        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(2);
        lighting.setSpecularConstant(0.25);
        lighting.setSpecularExponent(0);
        lighting.setSurfaceScale(0);

        ((ImageView) event.getSource()).setEffect(lighting);
    }

    @FXML
    void ChangeRoleToChoose(MouseEvent event) {
        if (lockedRoles.contains((ImageView) event.getSource()) && (ImageView) event.getSource() != NoRole) {
            return;
        }

        if (currentlySelectedImageView != null && !lockedRoles.contains(currentlySelectedImageView)) {
            currentlySelectedImageView.setEffect(null);
        }
        currentlySelectedImageView = (ImageView) event.getSource();
        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(2);
        lighting.setSpecularConstant(0.4);
        lighting.setSpecularExponent(0);
        lighting.setSurfaceScale(0);

        ((ImageView) event.getSource()).setEffect(lighting);

        if (playerCount == 0) {
            selectRole((ImageView) event.getSource(), PlayerRole1);
        } else if (playerCount == 1) {
            selectRole((ImageView) event.getSource(), PlayerRole2);
        } else if (playerCount == 2) {
            selectRole((ImageView) event.getSource(), PlayerRole3);
        } else if (playerCount == 3) {
            selectRole((ImageView) event.getSource(), PlayerRole4);
        }
    }

    public void selectRole(ImageView role, Label playerRole) {
        if (role == TheHackerCEO && !isTheHackerCEOSelected) {
            playerRole.setText("The Hacker CEO");
        } else if (role == TheTechGuruCTO && !isTheTechGuruCTOSelected) {
            playerRole.setText("The Tech Guru (CTO)");
        } else if (role == TheVCFunded && !isTheVCFundedSelected) {
            playerRole.setText("The VC-Funded");
        } else if (role == NoRole) {
            playerRole.setText("No Role");
        }
    }

    @FXML
    void ResetRole(MouseEvent event) {
        if (lockedRoles.contains((ImageView) event.getSource()) || (ImageView) event.getSource() == currentlySelectedImageView) {
            return;
        }
        ((ImageView) event.getSource()).setEffect(null);
    }

    @FXML
    void NextPlayerButtonMouseEnter(MouseEvent event) {
        SFXManager.play("MouseEnter.mp3");
        String rgbColor = "rgb(0,0,225)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";" + "-fx-border-color: white;" + "-fx-border-width: 2");
    }

    @FXML
    void ResetAllButtonMouseEnter(MouseEvent event) {
        SFXManager.play("MouseEnter.mp3");
        String rgbColor = "rgb(220,0,0)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";" + "-fx-border-color: white;" + "-fx-border-width: 2");
    }

    @FXML
    void BackButtonMouseEnter(MouseEvent event) {
        SFXManager.play("MouseEnter.mp3");
        ((Button) (event.getSource())).setStyle("-fx-background-color:  #333; -fx-border-color: white; -fx-border-width: 2");
    }

    @FXML
    void StartButtonMouseEnter(MouseEvent event) {
        SFXManager.play("MouseEnter.mp3");
        String rgbColor = "rgb(0,110,0)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";" + "-fx-border-color: white;" + "-fx-border-width: 2");
    }

    @FXML
    void LobbyButtonsMouseExit(MouseEvent event) {
        ((Button) (event.getSource())).setStyle("-fx-background-color: black ;-fx-border-color: white ;-fx-border-width: 2");
    }

    @FXML
    void onStartGame(ActionEvent event) throws IOException {
        if (playerCount == 0) {
            SFXManager.play("Error 2.mp3");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Player Count");
            alert.setHeaderText("Not Enough Players");
            alert.setContentText("You need at least 2 players to start the game. Please add more players and try again.");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
            alert.showAndWait();
            return;
        }
        try {
            if (playerCount == 1) {
                if (!createPlayer(PlayerName2, PlayerRole2, getPlayerSelection(1).equals("AIToggle"))) {
                    return;
                }
            } else if (playerCount == 2) {
                if (!createPlayer(PlayerName3, PlayerRole3, getPlayerSelection(2).equals("AIToggle"))) {
                    return;
                }
            } else if (playerCount == 3) {
                if (!createPlayer(PlayerName4, PlayerRole4, getPlayerSelection(3).equals("AIToggle"))) {
                    return;
                }
            }
        } catch (PlayerTypeNotSelectedException e) {
            SFXManager.play("Error 2.mp3");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Player Configuration");
            alert.setHeaderText("Player Type Not Selected");
            alert.setContentText(e.getMessage());
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
            alert.showAndWait();
            return;
        } catch (PlayerRoleNotSelectedException e) {
            SFXManager.play("Error 2.mp3");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Player Configuration");
            alert.setHeaderText("Player Role Not Selected");
            alert.setContentText(e.getMessage());
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
            alert.showAndWait();
            return;
        } catch (InvalidPlayerNameException e) {
            SFXManager.play("Error 2.mp3");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Player Configuration");
            alert.setHeaderText("Invalid Player Name");
            alert.setContentText(e.getMessage());
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
            alert.showAndWait();
            return;
        }

        Map gameMap = new Map(5, 5);
        GameEngine gameEngine = new GameEngine(gameMap, players);


        controller.setGameEngine(gameEngine);
        controller.initialize(gameEngine);

        gameEngine.startSetupPhase();

        Stage gameBoardStage = new Stage();
        gameBoardStage.setScene(new Scene(root));
        gameBoardStage.setTitle("Silicon Valley: The Tech Cartel");
        gameBoardStage.setResizable(false);

        gameBoardStage.show();
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void onResetAll(MouseEvent event) {

        TheHackerCEO.setDisable(false);
        TheHackerCEO.setOpacity(1.0);
        TheHackerCEO.setEffect(null);

        TheTechGuruCTO.setDisable(false);
        TheTechGuruCTO.setOpacity(1.0);
        TheTechGuruCTO.setEffect(null);

        TheVCFunded.setDisable(false);
        TheVCFunded.setOpacity(1.0);
        TheVCFunded.setEffect(null);


        lockedRoles.clear();
        currentlySelectedImageView = null;

        players.clear();
        isTheHackerCEOSelected = false;
        isTheTechGuruCTOSelected = false;
        isTheVCFundedSelected = false;
        playerCount = 0;
        disableGroup();
        PlayerName1.setText(null);
        PlayerName2.setText(null);
        PlayerName3.setText(null);
        PlayerName4.setText(null);
        PlayerName1.setEditable(true);
        PlayerName2.setEditable(true);
        PlayerName3.setEditable(true);
        PlayerName4.setEditable(true);
        PlayerRole1.setText("NONE");
        PlayerRole2.setText("NONE");
        PlayerRole3.setText("NONE");
        PlayerRole4.setText("NONE");
    }

    @FXML
    void onNextPlayer(MouseEvent event) {
        int activeSlot = playerCount;
        boolean turnImageOff = true;
        try {
            if (playerCount == 0) {
                if (!createPlayer(PlayerName1, PlayerRole1, getPlayerSelection(0).equals("AIToggle"))) {
                    return;
                }
            } else if (playerCount == 1) {
                if (!createPlayer(PlayerName2, PlayerRole2, getPlayerSelection(1).equals("AIToggle"))) {
                    return;
                }
            } else if (playerCount == 2) {
                if (!createPlayer(PlayerName3, PlayerRole3, getPlayerSelection(2).equals("AIToggle"))) {
                    return;
                }
            }
        } catch (PlayerTypeNotSelectedException e) {
            SFXManager.play("Error 2.mp3");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Player Configuration");
            alert.setHeaderText("Player Type Not Selected");
            alert.setContentText(e.getMessage());
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
            alert.showAndWait();
            turnImageOff = false;
        } catch (PlayerRoleNotSelectedException e) {
            SFXManager.play("Error 2.mp3");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Player Configuration");
            alert.setHeaderText("Player Role Not Selected");
            alert.setContentText(e.getMessage());
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
            alert.showAndWait();
            turnImageOff = false;
        } catch (InvalidPlayerNameException e) {
            SFXManager.play("Error 2.mp3");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Player Configuration");
            alert.setHeaderText("Invalid Player Name");
            alert.setContentText(e.getMessage());
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
            alert.showAndWait();
            turnImageOff = false;
        }
        if (currentlySelectedImageView != null && turnImageOff && activeSlot < 3) {
            if (currentlySelectedImageView != NoRole) {
                lockedRoles.add(currentlySelectedImageView);
                currentlySelectedImageView.setDisable(true);
                currentlySelectedImageView.setOpacity(0.5);
            } else {
                NoRole.setEffect(null);
            }
            currentlySelectedImageView = null;
        }
    }

    public boolean createPlayer(TextField playerNameTF, Label playerRole, boolean isAI) {
        PlayerRole playerR = role(playerRole);
        if (playerR == null) {
            throw new PlayerRoleNotSelectedException(players.size() + 1);
        }
        String playerName;
        if (!playerNameTF.getText().isEmpty()) {
            playerName = playerNameTF.getText();
            if (equals(playerName) || !isNameValid(playerName)) {
                throw new InvalidPlayerNameException(players.size() + 1, playerName);
            }
            Player player = null;
            if (!isAI) {
                player = switch (playerR) {
                    case THE_HACKER_CEO -> new HackerCEOPlayer(playerName, new ArrayList<>());
                    case THE_TECH_GURU_CTO -> new TechGuruPlayer(playerName, new ArrayList<>());
                    case THE_VC_FUNDED -> new VCFundedPlayer(playerName, new ArrayList<>());
                    case NONE -> new Player(playerName, new ArrayList<>());
                };
            } else if(playerCount==1){
                player = switch (playerR) {
                    case THE_HACKER_CEO ->new AIHackerCEOPlayer(playerName, controller,P2HardCheckBox.isSelected());
                    case THE_TECH_GURU_CTO -> new AITechGuruPlayer(playerName, controller,P2HardCheckBox.isSelected());
                    case THE_VC_FUNDED -> new AIVCFundedPlayer(playerName, controller,P2HardCheckBox.isSelected());
                    case NONE -> new AIPlayer(playerName, controller,P2HardCheckBox.isSelected());
                };
            }else if(playerCount==2){
                player = switch (playerR) {
                    case THE_HACKER_CEO ->new AIHackerCEOPlayer(playerName, controller,P3HardCheckBox.isSelected());
                    case THE_TECH_GURU_CTO -> new AITechGuruPlayer(playerName, controller,P3HardCheckBox.isSelected());
                    case THE_VC_FUNDED -> new AIVCFundedPlayer(playerName, controller,P3HardCheckBox.isSelected());
                    case NONE -> new AIPlayer(playerName, controller,P3HardCheckBox.isSelected());
                };
            }else if(playerCount==3){
                player = switch (playerR) {
                    case THE_HACKER_CEO ->new AIHackerCEOPlayer(playerName, controller,P4HardCheckBox.isSelected());
                    case THE_TECH_GURU_CTO -> new AITechGuruPlayer(playerName, controller,P4HardCheckBox.isSelected());
                    case THE_VC_FUNDED -> new AIVCFundedPlayer(playerName, controller,P4HardCheckBox.isSelected());
                    case NONE -> new AIPlayer(playerName, controller,P4HardCheckBox.isSelected());
                };
            }

            player.setRole(playerR);
            playerNameTF.setEditable(false);
            players.add(player);
            playerCount++;
            if (playerR == PlayerRole.THE_HACKER_CEO) {
                isTheHackerCEOSelected = true;
            } else if (playerR == PlayerRole.THE_TECH_GURU_CTO) {
                isTheTechGuruCTOSelected = true;
            } else if (playerR == PlayerRole.THE_VC_FUNDED) {
                isTheVCFundedSelected = true;
            }
            disableGroup();
            return true;
        } else {
            throw new InvalidPlayerNameException(players.size() + 1, "");
        }
    }

    private boolean equals(String playerName) {
        for (Player player : players) {
            if (player.getPlayerName().equals(playerName)) {
                return true;
            }
        }
        return false;
    }

    private boolean isNameValid(String playerName) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+(?:\\s[a-zA-Z0-9]+)*$");
        return pattern.matcher(playerName).matches();
    }

    public PlayerRole role(Label playerRole) {
        if (playerRole.getText().equals("The Hacker CEO") && !isTheHackerCEOSelected) {
            return PlayerRole.THE_HACKER_CEO;
        } else if (playerRole.getText().equals("The Tech Guru (CTO)") && !isTheTechGuruCTOSelected) {
            return PlayerRole.THE_TECH_GURU_CTO;
        } else if (playerRole.getText().equals("The VC-Funded") && !isTheVCFundedSelected) {
            return PlayerRole.THE_VC_FUNDED;
        } else if (playerRole.getText().equals("No Role")) {
            return PlayerRole.NONE;
        }
        return null;
    }

    public void disableGroup() {
        if (playerCount == 0) {
            P1.setDisable(false);
            P2.setDisable(true);
            P3.setDisable(true);
            P4.setDisable(true);
        } else if (playerCount == 1) {
            P1.setDisable(true);
            P2.setDisable(false);
            P3.setDisable(true);
            P4.setDisable(true);
        } else if (playerCount == 2) {
            P1.setDisable(true);
            P2.setDisable(true);
            P3.setDisable(false);
            P4.setDisable(true);
        } else if (playerCount == 3) {
            P1.setDisable(true);
            P2.setDisable(true);
            P3.setDisable(true);
            P4.setDisable(false);
        }
    }

    @FXML
    void HardAICheckboxesMouseEnter(MouseEvent event) {
        SFXManager.play("MouseEnter.mp3");
    }

    public static double getGameSoundVolume() {
        return gameSoundVolume;
    }

    public static void setGameSoundVolume(double Volume) {
        gameSoundVolume = Volume;
    }
}

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class InventoryController {

    @FXML
    private TableView<Album> tableView;
    @FXML
    private TableColumn<Album, String> columnAlbumName;
    @FXML
    private TableColumn<Album, String> columnArtist;
    @FXML
    private TableColumn<Album, Integer> columnTotal;
    @FXML
    private TableColumn<Album, Integer> columnAvailable;

    @FXML
    private TextField albumNameField;
    @FXML
    private TextField artistField;
    @FXML
    private TextField totalField;
    @FXML
    private TextField rentedField;

    private ObservableList<Album> albumList;

    @FXML
    public void initialize() {
        // Set up table columns
        columnAlbumName.setCellValueFactory(new PropertyValueFactory<>("albumName"));
        columnArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        columnTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        columnAvailable.setCellValueFactory(new PropertyValueFactory<>("available"));

        // Initialize album list
        albumList = FXCollections.observableArrayList();
        tableView.setItems(albumList);
    }

    @FXML
    public void addAlbum() {
        try {
            String albumName = albumNameField.getText();
            String artist = artistField.getText();
            int total = Integer.parseInt(totalField.getText());
            int rented = Integer.parseInt(rentedField.getText());
            int available = total - rented;

            if (available < 0) {
                showError("Available copies cannot be negative.");
                return;
            }

            albumList.add(new Album(albumName, artist, total, available, rented));
            clearFields();
        } catch (NumberFormatException e) {
            showError("Invalid input. Please enter valid data.");
        }
    }

    @FXML
    public void deleteAlbum() {
        Album selectedAlbum = tableView.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            albumList.remove(selectedAlbum);
        } else {
            showError("No album selected.");
        }
    }

    @FXML
    public void updateAlbum() {
        Album selectedAlbum = tableView.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            try {
                String albumName = albumNameField.getText();
                String artist = artistField.getText();
                int total = Integer.parseInt(totalField.getText());
                int rented = Integer.parseInt(rentedField.getText());
                int available = total - rented;

                if (available < 0) {
                    showError("Available copies cannot be negative.");
                    return;
                }

                selectedAlbum.setAlbumName(albumName);
                selectedAlbum.setArtist(artist);
                selectedAlbum.setTotal(total);
                selectedAlbum.setAvailable(available);
                selectedAlbum.setRented(rented);

                tableView.refresh();
                clearFields();
            } catch (NumberFormatException e) {
                showError("Invalid input. Please enter valid data.");
            }
        } else {
            showError("No album selected.");
        }
    }

    @FXML
    public void rentAlbum() {
        Album selectedAlbum = tableView.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            if (selectedAlbum.getAvailable() > 0) {
                selectedAlbum.setAvailable(selectedAlbum.getAvailable() - 1);
                selectedAlbum.setRented(selectedAlbum.getRented() + 1);
                tableView.refresh();
            } else {
                showError("No available copies left.");
            }
        } else {
            showError("No album selected.");
        }
    }

    private void clearFields() {
        albumNameField.clear();
        artistField.clear();
        totalField.clear();
        rentedField.clear();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Inner class for Album
    public static class Album {
        private final StringProperty albumName;
        private final StringProperty artist;
        private final IntegerProperty total;
        private final IntegerProperty available;
        private final IntegerProperty rented;

        public Album(String albumName, String artist, int total, int available, int rented) {
            this.albumName = new SimpleStringProperty(albumName);
            this.artist = new SimpleStringProperty(artist);
            this.total = new SimpleIntegerProperty(total);
            this.available = new SimpleIntegerProperty(available);
            this.rented = new SimpleIntegerProperty(rented);
        }

        public StringProperty albumNameProperty() {
            return albumName;
        }

        public String getAlbumName() {
            return albumName.get();
        }

        public void setAlbumName(String albumName) {
            this.albumName.set(albumName);
        }

        public StringProperty artistProperty() {
            return artist;
        }

        public String getArtist() {
            return artist.get();
        }

        public void setArtist(String artist) {
            this.artist.set(artist);
        }

        public IntegerProperty totalProperty() {
            return total;
        }

        public int getTotal() {
            return total.get();
        }

        public void setTotal(int total) {
            this.total.set(total);
        }

        public IntegerProperty availableProperty() {
            return available;
        }

        public int getAvailable() {
            return available.get();
        }

        public void setAvailable(int available) {
            this.available.set(available);
        }

        public IntegerProperty rentedProperty() {
            return rented;
        }

        public int getRented() {
            return rented.get();
        }

        public void setRented(int rented) {
            this.rented.set(rented);
        }
    }
}

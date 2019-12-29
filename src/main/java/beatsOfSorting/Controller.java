package beatsOfSorting;

import beatsOfSorting.algorithms.*;
import beatsOfSorting.sound.SoundPlayer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.util.StringConverter;

import javax.sound.sampled.LineUnavailableException;
import java.text.DecimalFormat;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class Controller {
    @FXML
    private SortArray array;
    @FXML
    private Button btnSound;
    @FXML
    private Button btnRun;
    @FXML
    private Button btnReset;
    @FXML
    private Button btnRandom;
    @FXML
    private Label sortNameLabel;
    @FXML
    private Label comparisionsLabel;
    @FXML
    private Label arrayAccessLabel;
    @FXML
    private Label swapsLabel;
    @FXML
    private Label delayLabel;
    @FXML
    private Spinner<Integer> arraySizeSpinner;
    @FXML
    private Slider delaySlider;
    @FXML
    public ResizableCanvas canvas;
    @FXML
    public ComboBox<String> sortArrayTypeCombo;
    @FXML
    public ListView<Sort> sortAlgoListView;
    @FXML
    private Slider soundSustainSlider;
    @FXML
    private Labeled soundSustainLabel;
    @FXML
    private Pane canvasPane;

    private Sort sort;
    private SoundPlayer soundPlayer;
    private SortArray origArray;
    private Timeline timeline;
    private int arraySize;
    private double delay;
    private boolean sortFinished;
    private String arrayType;

    /**
     * Initializes the main controller class.
     */
    @FXML
    public void initialize() throws LineUnavailableException {
        delay = 2.0;
        sortFinished = false;

        soundPlayer = new SoundPlayer();

        // initialize controller and set text shown in the UI
        array.initArray();
        arraySize = array.getSize();

        array.shuffle();
        origArray = new SortArray(array);
        canvas.widthProperty().bind(canvasPane.widthProperty());
        canvas.heightProperty().bind(canvasPane.heightProperty());

        soundPlayer.soundSchedulerService(array.getSize());

        sortArrayTypeCombo.getItems().addAll("Random", "Ascending", "Descending");
        sortArrayTypeCombo.getSelectionModel().selectFirst();
        arrayType = sortArrayTypeCombo.getValue();
        initSortAlgorithmsList();

        initArraySizeSpinner();

        initDelaySlider();

        initSoundSustainSlider();

        //Disable when running sort
        btnReset.disableProperty().bind(btnRun.textProperty().isEqualTo("Pause"));
        sortAlgoListView.disableProperty().bind(btnRun.textProperty().isEqualTo("Pause"));
        sortArrayTypeCombo.disableProperty().bind(btnRun.textProperty().isEqualTo("Pause"));
        arraySizeSpinner.disableProperty().bind(btnRun.textProperty().isEqualTo("Pause"));
        btnRandom.disableProperty().bind(btnRun.textProperty().isEqualTo("Pause"));
        draw();
    }

    /**
     * Reset the canvas given array type and size
     */
    private void resetCanvas(String type) {
        sortFinished = false;

        if (array.getSize() != arraySize) {
            array = new SortArray(arraySize);
            array.initArray();
            selectArrayType(type);
        } else {
            array = new SortArray(origArray);
            array.resetBarColors();
            if (!arrayType.equals(type)) {
                selectArrayType(sortArrayTypeCombo.getValue());
            }
        }

        sort.setArray(array);
        canvas.setArray(array);
    }

    /**
     * Select the array type
     */
    private void selectArrayType(String type) {
        switch (type) {
            case "Random":
                array.shuffle();
                break;
            case "Ascending":
                array.initArray();
                break;
            case "Descending":
                array.setDescendingArray();
                break;
        }
        origArray = new SortArray(array);
        origArray.resetBarColors();
        arrayType = type;
    }

    private void initSortAlgorithmsList() {
        ObservableList<Sort> sortAlgorithms = FXCollections.observableArrayList(new InsertionSort(array),
                new SelectionSort(array), new BubbleSort(array), new GnomeSort(array), new CombSort(array),
                new ShellSort(array), new StoogeSort(array), new MergeSort(array), new QuickSortLomuto(array),
                new QuickSortHoare(array), new HeapSort(array), new RadixSortLSD(array), new RadixSortMSD(array),
                new CocktailShakerSort(array), new CycleSort(array));

        sortAlgoListView.setItems(sortAlgorithms);

        sortAlgoListView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            sort = newValue;

            if (sort != null) {
                sortFinished = false;

                sort.cancel();
                sort = sort.createNewSort();
            }
            resetCanvas(sortArrayTypeCombo.getValue());
        });

        sortAlgoListView.getSelectionModel().select(0);
    }

    private void initArraySizeSpinner() {
        SpinnerValueFactory<Integer> factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2000, 0);
        arraySizeSpinner.setValueFactory(factory);

        arraySizeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> arraySize = newValue);

        arraySizeSpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                arraySize = Integer.parseInt(newValue);
            }
        });

        Pattern validEditingState = Pattern.compile("[0-9]*");

        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) {
                return c;
            } else if (text.isEmpty() || text.isBlank()) {
                c.setText("0");
                return c;
            } else {
                return null;
            }
        };

        StringConverter<Integer> converter = new StringConverter<>() {

            @Override
            public String toString(Integer value) {
                return value.toString();
            }

            @Override
            public Integer fromString(String string) {
                if (string.isEmpty()) {
                    return 0;
                } else {
                    return Integer.valueOf(string);
                }
            }
        };

        TextFormatter<Integer> textFormatter = new TextFormatter<>(converter, 334, filter);

        factory.setConverter(converter);
        arraySizeSpinner.getEditor().setTextFormatter(textFormatter);
        factory.valueProperty().bindBidirectional(textFormatter.valueProperty());
    }

    private void initDelaySlider() {
        delaySlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            double base = 4;

            delay = Math.pow(base, newValue.intValue() / 2000.0 * Math.log(2 * 1000.0) / Math.log(base));
            DecimalFormat df;

            if (newValue.intValue() == 0)
                delay = 0;

            if (delay > 10) {
                df = new DecimalFormat("#");
                delayLabel.setText("Delay: " + df.format(delay) + " ms");
            } else if (delay > 1) {
                df = new DecimalFormat("#.#");
                delayLabel.setText("Delay: " + df.format(delay) + " ms");
            } else {
                df = new DecimalFormat("#.##");
                delayLabel.setText("Delay: " + df.format(delay) + " ms");
            }

            sort.setDelay(delay);
            soundPlayer.delayProperty().setValue(delay);
        });
    }

    private void initSoundSustainSlider() {
        soundSustainSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            double base = 4;

            double soundSustain = Math.pow(base, newValue.intValue() / 2000.0 * Math.log(50) / Math.log(base));
            DecimalFormat df = new DecimalFormat("#.#");

            soundSustainLabel.setText("Sound Sustain: " + df.format(soundSustain));
            soundPlayer.soundSustainProperty().setValue(soundSustain);
        });
    }

    /**
     * Button listener to start/pause the sort
     *
     * @param event
     */
    @FXML
    public void buttonRun(ActionEvent event) {
        if (!sortFinished) {
            if (sort.isPaused()) {
                sort.resume();
                btnRun.setText("Pause");
                return;
            }

            if (sort.isRunning()) {
                sort.pause();
                btnRun.setText("Run");
                return;
            }

            sort.setOnSucceeded(e -> {
                sortFinished = true;
                btnRun.setText("Run");
                System.out.println(array);
            });

            btnRun.setText("Pause");

            // Run the task in a background thread
            Thread backgroundThread = new Thread(sort);
            // Terminate the running thread if the application exits
            backgroundThread.setDaemon(true);
            // Start the thread
            backgroundThread.start();
        }
    }

    /**
     * Button listener to reset the screen
     *
     * @param event
     */
    @FXML
    public void buttonReset(ActionEvent event) {
        sortFinished = false;
        sort = sortAlgoListView.getSelectionModel().getSelectedItem();
        sort.cancel();
        sort = sort.createNewSort();
        sort.setDelay(delay);
        origArray.resetBarColors();
        array = new SortArray(origArray);
        sort.setArray(array);
        canvas.setArray(array);
        SoundPlayer.resetSound();
    }

    /**
     * Button listener random
     *
     * @param event
     */
    @FXML
    public void buttonRandom(ActionEvent event) {
        sortFinished = false;
        sort = sortAlgoListView.getSelectionModel().getSelectedItem();
        sort.cancel();
        sort = sort.createNewSort();
        sort.setDelay(delay);
        array = new SortArray(arraySize);
        array.initArray();
        selectArrayType("Random");
        sort.setArray(array);
        canvas.setArray(array);
    }

    /**
     * Button listener to control sound on/off
     *
     * @param event
     */
    @FXML
    public void buttonSound(ActionEvent event) {
        soundPlayer.soundOn = !soundPlayer.soundOn;
        if (soundPlayer.soundOn)
            btnSound.setText("Sound Off");
        else
            btnSound.setText("Sound On");
    }

    /**
     * Array type combo box
     *
     * @param event
     */
    @FXML
    public void comboSortArrayType(ActionEvent event) {
        sort = sortAlgoListView.getSelectionModel().getSelectedItem();
        sort.cancel();
        sort = sort.createNewSort();
        sort.setDelay(delay);
        resetCanvas(sortArrayTypeCombo.getValue());
    }

    /**
     * Method to call the draw method within a timeline
     */
    private void draw() {
        timeline = new Timeline(new KeyFrame(Duration.millis(16.66), event -> {
            sortNameLabel.setText("Name: " + sort);
            comparisionsLabel.setText("Comparisons: " + array.comp);
            arrayAccessLabel.setText("Array Access: " + array.arrayAccess);
            swapsLabel.setText("Swaps: " + array.swap);

            canvas.draw();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
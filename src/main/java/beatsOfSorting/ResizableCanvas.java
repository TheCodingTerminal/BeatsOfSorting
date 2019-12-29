package beatsOfSorting;

import javafx.beans.NamedArg;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.IOException;

public class ResizableCanvas extends Canvas {

    SortArray array;

    public ResizableCanvas(@NamedArg("array") SortArray array) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ResizableCanvas.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.array = array;
        /*
         * Make sure the canvas draws its content again when its size changes.
         */
        widthProperty().addListener(it -> draw());
        heightProperty().addListener(it -> draw());
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }

    private double getY(int key, int arraySize) {
        return (key * (getHeight() / arraySize)) * 0.9;
    }

    public void setArray(SortArray array) {
        this.array = array;
    }

    /*
     * Draw a chart based on the data provided by the model.
     */
    public void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        int arraySize = array.getSize();

        double barWidth = getWidth() / arraySize;

        if (barWidth < 1.0) {
            barWidth = 1.0;
        }

        double barGap = (getWidth() - arraySize) / arraySize;

        if (barGap > 1.0) {
            barGap = 1.0;
        }

        if (getWidth() <= arraySize) {
            barGap = 0.0;
        }

        for (int i = 0; i < arraySize; i++) {

            int value = array.getValueDirect(i);

            gc.setFill(array.getBarColor(i));

            gc.fillRect(i * barWidth, getHeight() - getY(value, arraySize),
                    Math.max((barWidth - barGap), 1.0), getY(value, arraySize));

        }
    }
}
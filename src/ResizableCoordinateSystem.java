import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.security.InvalidParameterException;
import java.util.*;

class ResizableCoordinateSystem extends Canvas {
    private double originX;
    private double originY;
    private GraphicsContext gc;
    private double deltaX, deltaY, xStart, xEnd, yStart, yEnd;
    public double xMousePress, yMousePress;
    private ArrayList<LinkedHashMap<Double, Double>> functions;
    private double plotSpacing;

    public ResizableCoordinateSystem(double deltaX, double deltaY, double xStart, double xEnd, double yStart, double yEnd, double plotSpacing) {
        if (xStart > xEnd || yStart > yEnd) {
            throw new InvalidParameterException("Invalid parameters for coordinate axis");
        }

        functions = new ArrayList<>();
        gc = getGraphicsContext2D();

        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.xStart = xStart;
        this.xEnd = xEnd;
        this.yStart = yStart;
        this.yEnd = yEnd;
        this.plotSpacing = plotSpacing;

        // Redraw canvas when size changes.
        //widthProperty().addListener(evt -> drawCoordinateSystem());
        //heightProperty().addListener(evt -> drawCoordinateSystem());

        widthProperty().setValue(500);
        heightProperty().setValue(400);
        drawCoordinateSystem();

    }

    public void rescaleSystem(double deltaX, double deltaY, double xStart, double xEnd, double yStart, double yEnd, double plotSpacing) {


        if (xStart > xEnd || yStart > yEnd) {
            throw new InvalidParameterException("Invalid parameters for coordinate axis");
        }
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.xStart = xStart;
        this.xEnd = xEnd;
        this.yStart = yStart;
        this.yEnd = yEnd;
        this.plotSpacing = plotSpacing;
        drawCoordinateSystem();
    }

    public void drawCoordinateSystem() {
        gc.clearRect(0, 0, getWidth(), getHeight());

        if (xStart >= 0) {
            originX = 0;
        } else if (xEnd < 0) {
            originX = getWidth();
        } else {
            originX = getWidth() * Math.abs(xStart) / (xEnd - xStart); //Just normal left to right scaling
        }

        if (yStart >= 0) {
            originY = getHeight();
        } else if (yEnd <= 0) {
            originY = 0;
        } else {
            originY = getHeight() * Math.abs(yEnd) / (yEnd - yStart); //Because coordinate system goes from "down to up" and canvas starts with height = 0 at top.
        }


        gc.setFont(new Font(7));
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, getWidth(), getHeight());
        gc.setLineWidth(1);

        generateCoordinateAxis();
    }

    private void generateCoordinateAxis() {
        gc.strokeLine(originX, 0, originX, getHeight());
        gc.strokeLine(0, originY, getWidth(), originY);

        int halfLineLength = 3;
        double pixelsPerX = getWidth() / ((xEnd - xStart) / deltaX);
        double pixelsPerY = getHeight() / ((yEnd - yStart) / deltaY);


        for (double x = originX + pixelsPerX; x <= getWidth(); x += pixelsPerX) {
            String gradeValue = String.valueOf(round(deltaX * (x - originX) / pixelsPerX, Double.toString(deltaX).length() - 1));
            gc.strokeLine(x, originY + halfLineLength, x, originY - halfLineLength);
            gc.strokeText(gradeValue, x, originY + 2 * halfLineLength);
        }

        for (double x = originX - pixelsPerX; x >= 0; x -= pixelsPerX) {
            String gradeValue = String.valueOf(round(deltaX * (x - originX) / pixelsPerX, Double.toString(deltaX).length() - 1));
            gc.strokeLine(x, originY + halfLineLength, x, originY - halfLineLength);
            gc.strokeText(gradeValue, x, originY + 2 * halfLineLength);
        }

        for (double y = originY - pixelsPerY; y >= 0; y -= pixelsPerY) {
            String gradeValue = String.valueOf(round(-deltaY * (y - originY) / pixelsPerY, Double.toString(deltaY).length() - 1));
            gc.strokeLine(originX - halfLineLength, y, originX + halfLineLength, y);
            gc.strokeText(gradeValue, originX + 2 * halfLineLength, y);
        }

        for (double y = originY + pixelsPerY; y <= getHeight(); y += pixelsPerY) {
            String gradeValue = String.valueOf(round(-deltaY * (y - originY) / pixelsPerY, Double.toString(deltaY).length() - 1));
            gc.strokeLine(originX - halfLineLength, y, originX + halfLineLength, y);
            gc.strokeText(gradeValue, originX + 2 * halfLineLength, y);
        }
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public double round(double value) {
        String number = String.valueOf(value);
        char[] arrayOfDigits = number.toCharArray();
        int startIndex = 0;
        if (arrayOfDigits[0] == '-' && arrayOfDigits[1] == '0') {
            startIndex = 3;
        } else if (arrayOfDigits[0] == '0') {
            startIndex = 2;
        }
        if (startIndex != 0) {
            for (int i = startIndex; i < arrayOfDigits.length; i++) {
                if (arrayOfDigits[i] != '0') {
                    return round(value, i + 2);
                }
            }
        }
        return value;
    }

    public void drawFunctions() {
        for (LinkedHashMap<Double, Double> function : functions) {
            plot(function);
        }
    }

    private void plot(LinkedHashMap<Double, Double> function) {
        ArrayList<Double> xValues = new ArrayList<>(function.keySet());
        for (int i = 0; i < xValues.size() - 1; i++) {
            double x1 = xValues.get(i);
            double y1 = function.get(x1);
            double x2 = xValues.get(i + 1);
            double y2 = function.get(x2);
            gc.strokeLine(xCoordToPixel(x1), yCoordToPixel(y1), xCoordToPixel(x2), yCoordToPixel(y2));
        }
    }

    public void appendGraph(String expression) {
        LinkedHashMap<Double, Double> xyValuePairs = new LinkedHashMap<>();
        Calculator calculator = new Calculator(expression);
        for (double x = xStart; x <= xEnd; x += plotSpacing) {
            double y;
            System.out.println("(" + String.valueOf(x) + ", " + String.valueOf(y = calculator.evaluateExprAtX(x)) + ")");
            xyValuePairs.put(x, y);
        }

        functions.add(xyValuePairs);
    }

    public void clearFunctions() {
        functions = new ArrayList<>();
        drawCoordinateSystem();
    }

    private double xCoordToPixel(double coord) {
        return (coord * getWidth() / (xEnd - xStart)) + originX;
    }

    private double yCoordToPixel(double coord) {
        return -(coord * getHeight() / (yEnd - yStart)) + originY;
    }

    public double xPixelToCoord(double pixel) {
        return (pixel - originX) * (xEnd - xStart) / getWidth();
    }

    public double yPixelToCoord(double pixel) {
        return -(pixel - originY) * (yEnd - yStart) / getHeight();
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public double getxStart() {
        return xStart;
    }

    public double getyStart() {
        return yStart;
    }

    public double getxEnd() {
        return xEnd;
    }

    public double getyEnd() {
        return yEnd;
    }

    public double getPlotSpacing() {
        return plotSpacing;
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
}
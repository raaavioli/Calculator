import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GUI extends Application {
    TextField lowerX, upperX, lowerY, upperY, deltaX, deltaY, plotSpacing;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Graphical Calculator");

        //Creating GridPane
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, 700, 500);

        VBox textFields = new VBox();
        TextField expressionField = new TextField();
        expressionField.setPromptText("Enter expression here");
        lowerX = new TextField();
        lowerX.setPromptText("Lower X-boundary");
        upperX = new TextField();
        upperX.setPromptText("Upper X-boundary");
        lowerY = new TextField();
        lowerY.setPromptText("Lower Y-boundary");
        upperY = new TextField();
        upperY.setPromptText("Upper Y-boundary");
        deltaX = new TextField();
        deltaX.setPromptText("deltaX");
        deltaY = new TextField();
        deltaY.setPromptText("deltaY");
        plotSpacing = new TextField();
        plotSpacing.setPromptText("plot spacing");
        textFields.getChildren().addAll(expressionField, lowerX, upperX, lowerY, upperY, deltaX, deltaY, plotSpacing);
        borderPane.setRight(textFields);

        Pane wrapperPane = new Pane();
        ResizableCoordinateSystem canvas = new ResizableCoordinateSystem(1, 1, -10, 10, -10, 10, 1);
        wrapperPane.getChildren().add(canvas);

        setTextFieldTexts(canvas);

        HBox topBox = new HBox();
        Button evaluateButton = new Button("Evaluate");
        topBox.getChildren().add(evaluateButton);
        Button plotAll = new Button("Plot All");
        topBox.getChildren().add(plotAll);
        Button clearAll = new Button("Clear functions");
        topBox.getChildren().add(clearAll);
        Button rescaleButton = new Button("Rescale");
        topBox.getChildren().add(rescaleButton);
        Button zoomOut = new Button("Zoom out");
        topBox.getChildren().add(zoomOut);
        CheckBox zoomIn = new CheckBox("Zoom in");
        topBox.getChildren().add(zoomIn);

        evaluateButton.setOnAction(e -> {
            rescaleButton.fire();
            canvas.appendGraph(expressionField.getText());
        });
        plotAll.setOnAction(e -> {
            canvas.drawCoordinateSystem();
            canvas.drawFunctions();
        });
        clearAll.setOnAction(e -> canvas.clearFunctions());
        rescaleButton.setOnAction(e -> {
            canvas.rescaleSystem(Double.parseDouble(deltaX.getText()), Double.parseDouble(deltaY.getText()), Double.parseDouble(lowerX.getText()), Double.parseDouble(upperX.getText()), Double.parseDouble(lowerY.getText()), Double.parseDouble(upperY.getText()), Double.parseDouble(plotSpacing.getText()));
            canvas.drawFunctions();
        });
        zoomOut.setOnAction(e -> {
            double zoom = Math.max(Math.abs(canvas.getxEnd() - canvas.getxStart()), Math.abs(canvas.getyEnd() - canvas.getyStart())) * 0.5;
            canvas.rescaleSystem(canvas.getDeltaX() * 2, canvas.getDeltaY() * 2, canvas.getxStart() - zoom, canvas.getxEnd() + zoom, canvas.getyStart() - zoom, canvas.getyEnd() + zoom, canvas.getPlotSpacing());
            setTextFieldTexts(canvas);
            canvas.drawFunctions();
        });

        EventHandler<MouseEvent> canvasZoomHandler = event -> {
            if (zoomIn.isSelected()) {
                String eventType = event.getEventType().toString();
                switch (eventType) {
                    case "MOUSE_PRESSED":
                        if (event.getButton() == MouseButton.PRIMARY) {
                            canvas.xMousePress = event.getX();
                            canvas.yMousePress = event.getY();
                            break;
                        }
                    case "MOUSE_RELEASED":
                        if (event.getButton() == MouseButton.PRIMARY) {
                            if (canvas.xMousePress != 0 && canvas.yMousePress != 0) {
                                double xStart = canvas.round(canvas.xPixelToCoord(Math.min(canvas.xMousePress, event.getX())));
                                double xEnd = canvas.round(canvas.xPixelToCoord(Math.max(canvas.xMousePress, event.getX())));
                                double yStart = canvas.round(canvas.yPixelToCoord(Math.max(canvas.yMousePress, event.getY())));
                                double yEnd = canvas.round(canvas.yPixelToCoord(Math.min(canvas.yMousePress, event.getY())));
                                double xGrade = canvas.round(Math.abs((xEnd - xStart) / 10));
                                double yGrade = canvas.round(Math.abs((yEnd - yStart) / 10));

                                if (xStart != 0 && xEnd != 0 && yStart != 0 && yEnd != 0 && xGrade != 0 && yGrade != 0) {
                                    lowerX.setText(String.valueOf(xStart));
                                    upperX.setText(String.valueOf(xEnd));
                                    lowerY.setText(String.valueOf(yStart));
                                    upperY.setText(String.valueOf(yEnd));
                                    deltaX.setText(String.valueOf(xGrade));
                                    deltaY.setText(String.valueOf(yGrade));
                                    plotSpacing.setText(String.valueOf(canvas.getPlotSpacing()));

                                    canvas.rescaleSystem(xGrade, yGrade, xStart, xEnd, yStart, yEnd, canvas.getPlotSpacing());

                                    canvas.drawFunctions();
                                }
                            }
                        }
                    case "MOUSE_EXITED":
                        System.out.println("Exited");
                        canvas.xMousePress = 0;
                        canvas.yMousePress = 0;
                        break;
                }
            }
        };

        canvas.setOnMousePressed(canvasZoomHandler);
        canvas.setOnMouseReleased(canvasZoomHandler);
        canvas.setOnMouseExited(canvasZoomHandler);


        borderPane.setCenter(wrapperPane);
        borderPane.setTop(topBox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setTextFieldTexts(ResizableCoordinateSystem canvas) {
        lowerX.setText(String.valueOf(canvas.getxStart()));
        upperX.setText(String.valueOf(canvas.getxEnd()));
        lowerY.setText(String.valueOf(canvas.getyStart()));
        upperY.setText(String.valueOf(canvas.getyEnd()));
        deltaX.setText(String.valueOf(canvas.getDeltaX()));
        deltaY.setText(String.valueOf(canvas.getDeltaY()));
        plotSpacing.setText(String.valueOf(canvas.getPlotSpacing()));
    }

}

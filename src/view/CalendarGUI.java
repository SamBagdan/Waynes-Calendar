package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Calendar;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sam on 5/28/2017.
 * @author Sam Bagdan, sambagdan@gmail.com
 * Desc: Handles the UI portion of the calendar
 */
public class CalendarGUI extends Application implements Observer {
    Calendar model = new Calendar();
    private static final Font FONT = new Font("Helvetica", 28);
    TextField yearString = new TextField("Year: ");
    TextField monthString = new TextField("Month: ");

    /**
     * Initializes the starting pane and boxes which comprise the UI
     * @param stage - the stage
     */
    public void start(Stage stage) throws Exception {
        model.addObserver(this);

        HBox selection = new HBox();
        Scene scene = new Scene(selection);

        //setup for year selection box
        VBox yearView = new VBox();
        ScrollPane years = new ScrollPane(yearView);
        TitledPane titleYears = new TitledPane("YEAR SELECTION:", years);
        years.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        yearView.prefWidthProperty().bind(years.widthProperty());
        years.prefWidthProperty().bind(scene.widthProperty());

        //setup for month selection
        VBox monthView = new VBox();
        ScrollPane months = new ScrollPane(monthView);
        TitledPane titleMonths = new TitledPane("MONTH SELECTION:", months);
        months.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        monthView.prefWidthProperty().bind(months.widthProperty());
        months.prefWidthProperty().bind(scene.widthProperty());

        //setup for month confirmation box
        VBox GoView = new VBox();
        yearString.prefWidthProperty().bind(GoView.widthProperty());
        yearString.setEditable(false);
        monthString.prefWidthProperty().bind(GoView.widthProperty());
        monthString.setEditable(false);
        Button go = makeGoButton(scene);
        GoView.getChildren().addAll(yearString, monthString, go);

        //populates the year selection with year boxes
        for (int i = 0; i < 50; i++) {
            yearView.getChildren().add(makeYearButton(2017 + i, yearView));
        }

        //populates the month selection with month boxes
        for (int i = 1; i < 13; i++) {
            monthView.getChildren().add(makeMonthButton(i, monthView));
        }

        //set and display the stage
        selection.getChildren().addAll(titleYears, titleMonths, GoView);
        stage.setScene(scene);
        stage.setHeight(500);
        stage.setWidth(800);
        stage.setTitle("Waynes Work Calendar");
        stage.show();
    }

    /**
     * Implementation of the resulting calendar
     */
    public class WorkMonth extends Stage implements Observer{
        private int dayHeight = 25;
        private int dayWidth = 180;
        private String dayOnColor = "grey";
        private String dayOffColor = "blue";
        private String topDayColor = "lightgrey";

        /**
         * builds the resulting calendar from the model
         * @param schedule - a boolean schedule of on/off days
         * @param year - entered year
         * @param month - entered month
         */
        public WorkMonth(Boolean[] schedule, int year, int month) {
            GridPane grid = new GridPane();
            StackPane sun = generateText("Sunday\r", dayWidth, dayHeight, topDayColor);
            StackPane mon = generateText("Monday\r", dayWidth, dayHeight, topDayColor);
            StackPane tue = generateText("Tuesday\r", dayWidth, dayHeight, topDayColor);
            StackPane wed = generateText("Wednesday\r", dayWidth, dayHeight, topDayColor);
            StackPane thu = generateText("Thursday\r", dayWidth, dayHeight, topDayColor);
            StackPane fri = generateText("Friday\r", dayWidth, dayHeight, topDayColor);
            StackPane sat = generateText("Saturday\r", dayWidth, dayHeight, topDayColor);

            //arrange the days boxes correctly according to the month
            grid.addRow(0, sun, mon, tue, wed, thu, fri, sat);
            int row = 0;
            int start = model.calculateFirstDay();
            for (int i = 0; i < model.getCalendar().length + start; i++) {
                if ((i) % 7 == 0) {
                    row++;
                }
                if (i < start) {
                    grid.add(generateText("\r", 180, 25, "white"), i, row);
                } else {
                    if(!model.getCalendar()[i - start]){
                        grid.add(generateText(Integer.toString(i - start + 1) + "\r", dayWidth, dayHeight, dayOnColor), (i % 7), row);
                    }else{
                        grid.add(generateText(Integer.toString(i - start + 1) + " Free!\r", dayWidth, dayHeight, dayOffColor), (i % 7), row);
                    }
                }
            }

            //set and show the scene
            Scene scene = new Scene(grid);
            this.setScene(scene);
            this.setTitle(getMonth(month) + " Work Schedule for " + year);
            this.show();
        }

        /**
         * generates formatted panes with text in them representing a calendar day
         * @param text - either "free" or not
         * @param width - width of pane
         * @param height - height of pane
         * @param color - color of pane
         * @return StackPane the day pane
         */
        private StackPane generateText(String text, int width, int height, String color) {
            Text box = new Text(text);
            box.setWrappingWidth(width);
            box.setLineSpacing(height);
            box.setFont(FONT);
            StackPane border = new StackPane();
            border.setBackground(new Background(new BackgroundFill(Paint.valueOf(color), CornerRadii.EMPTY, Insets.EMPTY)));
            border.getChildren().add(box);
            border.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            return border;
        }
            //unused
            public void update(Observable o, Object arg) {
        }

    }

    /**
     * make a "go button"
     * @param scene - the scene the the button resides in
     * @return Button - the go button
     */
    private Button makeGoButton(Scene scene) {
        Button butt = new Button("GET SCHEDULE");
        butt.prefWidthProperty().bind(scene.widthProperty());
        butt.setFont(new Font("Helvetica", 30));
        butt.setAlignment(Pos.CENTER);
        butt.setOnAction(event -> {
            model.calculateMonth(model.getYear(), model.getMonth());
            WorkMonth calendar = new WorkMonth(model.getCalendar(), model.getYear(), model.getMonth());
        });
        return butt;
    }

    /**
     * make a "year button"
     * @param label - year number
     * @param view - box the year resides in
     * @return Button - the year button
     */
    private Button makeYearButton(int label, VBox view) {
        String year = Integer.toString(label);
        Button butt = new Button(year);
        butt.setFont(FONT);
        butt.prefWidthProperty().bind(view.widthProperty());
        butt.prefWidthProperty().bind(view.widthProperty());
        butt.setAlignment(Pos.CENTER);
        butt.setOnAction(event -> {
            model.setYear(label);
            update(model, true);
        });
        return butt;
    }

    /**
     * make a "month button"
     * @param label - month number
     * @param view - box the month resides in
     * @return Button - the month button
     */
    private Button makeMonthButton(int label, VBox view) {
        String month = getMonth(label);
        Button butt = new Button(month);
        butt.setFont(FONT);
        butt.prefWidthProperty().bind(view.widthProperty());
        butt.setAlignment(Pos.CENTER);
        butt.setOnAction(event -> {
            model.setMonth(label);
            update(model, false);
        });
        return butt;
    }

    /**
     * update the month or year of the display
     * @param o - unused
     * @param arg - boolean deciding whether or not updating a month or year
     */
    @Override
    public void update(Observable o, Object arg) {
        if ((Boolean) arg){
            yearString.setText("Year: " + model.getYear());
        } else{
            monthString.setText("Month: " + getMonth(model.getMonth()));
        }

    }

    /**
     * get month from number
     * @param num - number of month
     * @return - the month name as a string
     */
    private String getMonth(int num){
        String month;
        switch (num) {
            case 1:
                month = "January";
                break;
            case 2:
                month = "February";
                break;
            case 3:
                month = "March";
                break;
            case 4:
                month = "April";
                break;
            case 5:
                month = "May";
                break;
            case 6:
                month = "June";
                break;
            case 7:
                month = "July";
                break;
            case 8:
                month = "August";
                break;
            case 9:
                month = "September";
                break;
            case 10:
                month = "October";
                break;
            case 11:
                month = "November";
                break;
            default:
                month = "December";
                break;
        }
        return month;
    }
}
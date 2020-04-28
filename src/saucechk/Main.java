package saucechk;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/*
emilia > rem
i like both
but what sealed the deal was that emilia wont stab you with a flail if she gets anger
also i like ice cubes
- lewis, 2020 after watching rezero s1
season 2 hype :pog:
 */


/*
Made by Lewis "l1npengtul" Rho 2020
Licensed under the GPLv3
The terms of the GPLv3 can be found at: https://www.gnu.org/licenses/gpl-3.0.en.html
*/

public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception, IOException{

        //Row 1
        Label label = new Label("Sauce:");
        TextField textField = new TextField();
        Button button = new Button("Verify");


        //Row 2
        Text validateStatus = new Text("");
        validateStatus.setFill(Color.CRIMSON);
        validateStatus.setStrokeWidth(0.5);
        validateStatus.setStroke(Color.BLACK);

        //Empty Row 3


        Label emptyRow = new Label(" ");

        //Title Row 4

        Label titlelabel = new Label("Title: ");
        Label title = new Label(" ");

        //Tags Row 5

        Label tagLabel = new Label("Tags: ");
        Label tags = new Label(" ");


        //Author Row 6

        Label authLabel = new Label("Artist: ");
        Label auth = new Label(" ");

        //Group Row 7

        Label groupLabel = new Label("Group: ");
        Label group = new Label(" ");

        //Language Row 8

        Label langLabel = new Label("Language: ");
        Label lang = new Label(" ");


        GridPane grid = new GridPane();
        grid.addRow(0,label,textField,button);
        grid.addRow(1, validateStatus);

        grid.addRow(2, emptyRow);


        grid.addRow(4, titlelabel,title);
        grid.addRow(5, tagLabel,tags);
        grid.addRow(6, authLabel,auth);
        grid.addRow(7, groupLabel,group);
        grid.addRow(8, langLabel,lang);


        grid.setVgap(20);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER_LEFT);

        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    title.setText("");
                    tags.setText("");
                    auth.setText("");
                    group.setText("");
                    lang.setText("");
                    validateStatus.setText("");
                    validateStatus.setFill(Color.GREENYELLOW);
                    validateStatus.setText("Validating...");
                    //System.out.print("\nEnter SixDigit Sauce (q to quit):");
                    String sauceread = textField.getText();
                    int sauceCode = 0;
                    if (sauceread.length() == 6) {
                        if (isStrInt(sauceread)) {
                            sauceCode = Integer.parseInt(sauceread);
                            String baseURL = "https://ka.guya.moe/g/";
                            try {
                                Comic comic = getSauce(sauceCode, baseURL);

                                try{
                                    button.setDisable(true);
                                    title.setText(comic.title);
                                    tags.setText(String.join(", ", comic.tags));
                                    auth.setText(comic.artist);
                                    group.setText(comic.group);
                                    lang.setText(comic.lang);
                                    validateStatus.setText("Real Shit!");
                                    button.setDisable(false);
                                }catch (NullPointerException e){
                                    validateStatus.setFill(Color.CRIMSON);
                                    validateStatus.setText("Fake Shit!");
                                    button.setDisable(false);

                                }


                            } catch (IOException ex) {
                                validateStatus.setFill(Color.CRIMSON);
                                validateStatus.setText("Error!");
                            }
                        }
                    } else if (sauceread.equalsIgnoreCase("q")) {
                        System.exit(0);
                    } else {
                        validateStatus.setFill(Color.CRIMSON);
                        validateStatus.setText("6 Digit Numbers!");
                    }
                } catch (NumberFormatException e) {
                    validateStatus.setFill(Color.CRIMSON);
                    validateStatus.setText("6 Digit Numbers Only!");
                }

            }
        };
        button.addEventFilter(MouseEvent.MOUSE_CLICKED,eventHandler);

        Scene scene = new Scene(grid, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static boolean isStrInt(String x){
        try{
            int foo  = Integer.parseInt(x);
            return true;
        }
        catch (NumberFormatException e){
            return false;
        }
    }

    public static Comic getSauce(int x, String baseURL) throws IOException {
        URL sauce = new URL(baseURL + String.valueOf(x));
        URLConnection yc = sauce.openConnection();
        Comic rComic = new Comic();
        rComic.artist = "";
        rComic.title = "";
        rComic.group = "";
        rComic.lang = "";
        rComic.tags = null;
        try{
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            yc.getInputStream()));
            String inputLine;

            //set the flags to print artist, group, translation/lang
            //String for artist, group , language, and title
            String artist,group,lang,title;
            title = artist = group = lang = "";
            //taglist
            List<String> tags = new ArrayList<String >();
            int counter = 0;
            while ((inputLine = in.readLine()) != null){
                //System.out.println(inputLine);

                //See if flag conditions are met


                if(inputLine.contains("<td class=\"text-muted text-sm\">")){
                    String[] line = inputLine.split(">");
                    String[] line2 = new String[0];
                    if(counter == 0){
                        for (String s : line) {
                            line2 = s.split("<");
                        }
                        for (String l : line2) {
                            if (!(l.equals("/td"))){
                                artist = l;
                                counter++;
                            }
                        }
                    }
                    else if(counter == 1){
                        for (String s : line) {
                            line2 = s.split("<");
                        }
                        for (String l : line2) {
                            if (!(l.equals("/td"))){
                                group = l;
                                counter++;
                            }
                        }
                    }
                    else if(counter == 2){
                        for (String s : line) {
                            line2 = s.split("<");
                        }
                        for (String l : line2) {
                            if (!(l.equals("/td"))){
                                lang = l;
                                counter = -1;
                            }
                        }
                    }

                }


                if(inputLine.contains("<h1>")){
                    String[] line = inputLine.split(">");
                    String[] line2 = new String[0];
                    for (String s : line) {
                        line2 = s.split("<");
                    }

                    for (String l : line2) {
                        if (!(l.equals("/h1"))){
                            //System.out.println(l);
                            title = l;

                        }
                    }
                }

                //Tags

                if(inputLine.contains("<div class=\"tag\">")){
                    String[] line = inputLine.split(">");
                    String[] line2 = new String[0];
                    for (String s : line) {
                        line2 = s.split("<");
                    }
                    for (String l : line2) {
                        if (!(l.equals("/div"))){
                            //System.out.println(l);
                            tags.add(l);
                        }
                    }
                }
            }

            rComic.artist = artist;
            rComic.title = title;
            rComic.group = group;
            rComic.lang = lang;
            rComic.tags = tags;

            /*
            System.out.println("Title: " + title);
            System.out.println("Artist: " + artist);
            System.out.println("Group: " + group);
            System.out.println("Language: " + lang);
            System.out.println("Tags: " + tags);
            */
            in.close();
            return rComic;
        }catch (IOException e){
            return rComic;
        }
    }
}

class Comic{
    String title,artist,group,lang;
    List<String> tags = new ArrayList<String>();
}
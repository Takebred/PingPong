import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.Random;


public class PingPong  extends Application {

    private static final int COTE_FENETRE = 1000 ;
    private static final int LONGUEUR_JOUEUR = 200;
    private static final int EPAISSEUR_JOUEUR = 10;
    private static final double joueur1X = 0;
    private static final double joueur2X = COTE_FENETRE - EPAISSEUR_JOUEUR;
    private static final int RAYON_BALLE = 20;
    private static int vitesseBalleX = 1;
    private static double vitesseBalleY = 1;

    private double joueur1Y = COTE_FENETRE/2 - LONGUEUR_JOUEUR/2;
    private double joueur2Y = COTE_FENETRE/2 - LONGUEUR_JOUEUR/2;

    private double balleX = COTE_FENETRE/2;
    private double balleY = COTE_FENETRE/2;

    private int score1 = 0;
    private int score2 = 0;

    private boolean partieEnCours;
    Font policeCommencer = Font.loadFont("file:///D:/Projets/Games/PingPong/fonts/Roamer.ttf",70);
    Font policeScore = Font.loadFont("file:///D:/Projets/Games/PingPong/fonts/Roamer.ttf",40);
    
    private static final Media mediaTheme = new Media("file:///D:/Projets/Games/PingPong/sounds/PingPongTheme.mp3");
    public static final  MediaPlayer mediaPlayerTheme = new MediaPlayer(mediaTheme);

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Ping Pong by Takebred");
        mediaPlayerTheme.play();
        Canvas canvas = new Canvas(COTE_FENETRE, COTE_FENETRE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), event -> lancer(gc)));
        tl.setCycleCount(Timeline.INDEFINITE);
        
        canvas.setOnMouseMoved(event -> joueur1Y = partieEnCours ? (event.getY() <= COTE_FENETRE - LONGUEUR_JOUEUR ?
                event.getY() : COTE_FENETRE - LONGUEUR_JOUEUR ) : COTE_FENETRE/2 - LONGUEUR_JOUEUR/2);
        canvas.setOnMouseClicked(event -> partieEnCours = true);
        primaryStage.setScene(new Scene(new StackPane(canvas)));
        primaryStage.show();
        tl.play();
    }

    private void lancer(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0, COTE_FENETRE, COTE_FENETRE);

        if(partieEnCours){

            gc.setFill(Color.WHITE);
            gc.fillRect(0,0, COTE_FENETRE, COTE_FENETRE);
            gc.setFill(Color.GREEN);
            gc.fillRect(10,10, COTE_FENETRE-20, COTE_FENETRE/2 - 15);
            gc.setFill(Color.GREEN);
            gc.fillRect(10,COTE_FENETRE/2 + 5 , COTE_FENETRE - 20, COTE_FENETRE/2 - 15);

            balleX+=vitesseBalleX;
            balleY+=vitesseBalleY;

            //Comportement de l'ordinateur
            if(balleY<COTE_FENETRE-LONGUEUR_JOUEUR/2 && balleY>LONGUEUR_JOUEUR/2){
                if(balleX<=COTE_FENETRE/2){
                    joueur2Y = balleY - LONGUEUR_JOUEUR/2;
                }else{
                    joueur2Y = balleY > joueur2Y + LONGUEUR_JOUEUR/2 ? joueur2Y+10 : joueur2Y-10;
                }
            }

            gc.setFill(Color.RED);
            gc.fillOval(balleX, balleY, RAYON_BALLE, RAYON_BALLE);

        }else {
            gc.setFill(Color.WHITE);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFont(policeCommencer);
            gc.fillText("Commencer une partie !", COTE_FENETRE/2, COTE_FENETRE/2);

            //Comportement de la balle à chaque début de partie
            balleX = COTE_FENETRE/2 - RAYON_BALLE/2;
            balleY = COTE_FENETRE/2 - RAYON_BALLE/2;
            vitesseBalleX = new Random().nextInt(2)==0 ? 3: -3;
            vitesseBalleY = new Random().nextInt(2)==0 ? 3: -3;
        }

        //Comportement de la balle quand elle approche l'un des bords de la fenêtre
        if((balleX == EPAISSEUR_JOUEUR && balleY <= joueur1Y + LONGUEUR_JOUEUR && balleY >= joueur1Y) ||
                (balleX == COTE_FENETRE - EPAISSEUR_JOUEUR - RAYON_BALLE && balleY <= joueur2Y + LONGUEUR_JOUEUR && balleY >= joueur2Y)){
            vitesseBalleX *= -1.10;
        }
        if(balleY >= COTE_FENETRE-RAYON_BALLE || balleY <= 0) {
            vitesseBalleY *= -1.05;
        }

        if(balleX<joueur1X){
            score2++;
            partieEnCours = false;
        }

        if(balleX>joueur2X+EPAISSEUR_JOUEUR){
            score1++;
            partieEnCours = false;
        }

        gc.setFill(Color.WHITE);
        gc.setFont(policeScore);
        gc.fillText("Joueur 1 : " + score1 + "                                                          Joueur 2 : " + score2, COTE_FENETRE/2, 100);

        gc.setFill(Color.BLACK);
        gc.fillRect(joueur1X,joueur1Y,EPAISSEUR_JOUEUR,LONGUEUR_JOUEUR);
        gc.fillRect(joueur2X,joueur2Y,EPAISSEUR_JOUEUR,LONGUEUR_JOUEUR);


    }
}
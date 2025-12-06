package upc.view.console;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.jeu.Team;
import upc.model.jeu.GameManager;
import upc.model.jeu.Terrain;

public class LanternaView {
    //attribut permettant la réprésentation du terminal et de le styliser
    private Screen screen;

    //attribut permettant l'écriture du texte, des caractères avec coordonnées et couleur
    private TextGraphics graphics;

    //Constructeur
    public LanternaView() {
        //Gestion des exceptions
        try {
            //Crée le terminal
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();

            //Crée le screen en lien avec le terminal terminalFactory
            this.screen = terminalFactory.createScreen();

            //Ouvre l'écran et passe en mode Lanterna
            screen.startScreen();

            //Crée le graphics en lien avec le terminal(screen) pour dessiner des chaînes ou caractères
            this.graphics = this.screen.newTextGraphics();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    //Méthode permettant de vider l'écran (avant de redessiner)
    public void clear() { this.screen.clear(); }

    //Méthode permettant d'afficher tout ce qui a été dessiné
    public void refresh() {

        //Gestion des exceptions car Java oblige à gérer toutes les exceptions checked en lien avec refresh()
        try {

            this.screen.refresh();

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    //Méthode permettant d'écrire un texte à une position donnée
    public void drawString(int x, int y, String string) {

        //Gestion des exceptions
        try {
            //Permet de dessiner à une position donnée
            graphics.putString(x, y, string);

        } catch (Exception e) {

            throw new RuntimeException(e);

        }
    }
}

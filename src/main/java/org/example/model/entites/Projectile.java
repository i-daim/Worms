package org.example.model.entites;

public abstract class Projectile {

    private final Position posDebut;
    private final Position posFin;
    private Position posNow;
    private int vitesse;
    private boolean etat;

    public Projectile(Position position, double angle, int vitesse, double distance) {
        if (position == null) {
            System.out.println("Position est nulle");
        }
        this.posDebut = position;
        this.posNow = position;
        this.posFin = calculPosFin(angle, distance);
        this.vitesse = vitesse;
        this.etat = true;
    }

    public Position calculPosFin(double angle, double distance) {
        // convertir angle degrees->radians car fonctions utilisent radians.
        double angleRad = Math.toRadians(angle);
        // calculer delacement horizontal calculX = Distance*cos(angle)
        double calculX = distance * Math.cos(angleRad);
        // calculer deplacement vertical calculY = Distance*sin(angle)
        double calculY = distance * Math.sin(angleRad);
        // Maintenant calculer la position finale
        int xFin = posDebut.getX() + (int) calculX;
        int yFin = posFin.getY() + (int) calculY;
        return new Position(xFin, yFin);
    }

    public void updatePosNow() {
        if (!this.etat) {
            // pas mettre à jour si entite n'est plus active (arrivée ou explosé)
            return;
        }
        // calculer vecteur de direction (vecteurX, vecteurY) et distance totale
        double vecteurX = this.posFin.getX() - this.posDebut.getX();
        double vecteurY = this.posFin.getY() - this.posDebut.getY();
        double totalDistance = Math.sqrt(vecteurX * vecteurX + vecteurY * vecteurY);
        // particulier : Si le départ et arrivée sont pareil
        if (totalDistance == 0 && vitesse > 0) {
            this.etat = false;
            return;
        }
        // normaliser vecteur de direction
        // (permet de contrôler direction / vitesse de déplacement avec précision)
        double directionX = vecteurX / totalDistance;
        double directionY = vecteurY / totalDistance;
        // determiner deplacement pour cette itération (utilisant la vitesse)
        int pasDeplacement = this.vitesse;
        // calculer nouvelle position
        double nouveauX = this.posNow.getX() + directionX * pasDeplacement;
        double nouveauY = this.posNow.getY() + directionY * pasDeplacement;
        // verifier si on a dépassé posFin
        double xManquant = this.posFin.getX() - this.posNow.getX();
        double yManquant = this.posFin.getY() - this.posNow.getY();
        double distanceRestante = Math.sqrt(xManquant * xManquant + yManquant * yManquant);
        if (pasDeplacement >= distanceRestante) {
            // Si deplacement amene au-dela de posFin on stop à posFin
            this.posNow = this.posFin;
            this.etat = false;
            // ici ajouter fonction degat ou explosion si nécessaire
        } else {
            // Sinon mettre à jour la position
            this.posNow = new Position((int) Math.round(nouveauX), (int) Math.round(nouveauY));
        }
    }

    public void asTouched() {
        // TO DO (lorsque les collisions seront ajoutées)
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }
    public Position getPosDebut() {
        return posDebut;
    }
    public Position getPosNow() {
        return posNow;
    }
    public Position getPosFin() {
        return posFin;
    }
    public int getVitesse() {
        return vitesse;
    }
    public boolean getEtat() {
        return etat;
    }


}
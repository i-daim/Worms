    package upc.controller;

    import upc.model.entites.Alien;
    import upc.model.jeu.GameManager;
    import upc.view.console.ConsoleView;
    import upc.view.console.LanternaView;

    public class ControllerConsole {
        private final GameManager gameManager;
        private final ConsoleView view; // <-- Ici

        private boolean enCours;

        public ControllerConsole() {
            this.gameManager = new GameManager();
            this.view = new ConsoleView(); // <-- Ici
            this.enCours = true;
        }


        public void start() {
            view.printTerrain(gameManager);

            // Afficher l'état après la chute
            view.printStateAfterFall(gameManager);

            while (enCours && !gameManager.isOver()) {
                view.printGameState(gameManager); // 1. Afficher
                String command = view.readKey();  // 2. Lire
                traiterCommande(command);         // 3. Agir
            }


            if (gameManager.isOver()) {
                view.printGameOver(gameManager);
            }

            view.close();
        }

        private void traiterCommande(String command, GameManager gameManager) {
            switch (command) {
                case "gauche":
                    gameManager.getActiveAlien().moveLeft();
                    view.printMovementResult(true, "gauche", gameManager);
                    gameManager.skipTurn();
                    break;

                case "droite":
                    gameManager.getActiveAlien().moveRight();
                    view.printMovementResult(true, "droite", gameManager);
                    gameManager.skipTurn();
                    break;

                case "sauter":
                    gameManager.getActiveAlien().jump();
                    view.printJumpResult(true, gameManager);
                    skipTurn();
                    break;


                case "attaquer":
                    int degats = view.chooseDamage();
                    int x = view.chooseX();
                    int y = view.chooseY();
                    Alien cible = gameManager.attackPosition(x, y, degats);
                    view.printAttackResult(cible, x, y, gameManager.getActiveTeam());
                    skipTurn();
                    break;


                case "passer":
                    gameManager.skipTurn();
                    view.printTurnSkipped();
                    break;

                case "info" :
                    view.printWelcome();
                    break;

                case "aliens":
                    view.printAliensStats(gameManager);
                    break;

                case "map":
                    view.printTerrain(gameManager);
                    view.printGameState(gameManager);
                    break;

                case "exit":
                    enCours = false;
                    view.printGameAbandoned();
                    break;

                case null:
                    view.printUnknownCommand();
                    break;

                default:
                    break;
            }
        }

        private void skipTurn() {
            gameManager.skipTurn();
            view.printTurnSkipped();
            wait(1000);
        }

        private void wait (int ms) {
            try { Thread.sleep(ms); } catch (InterruptedException e) {}
        }

    }

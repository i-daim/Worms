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

                    //System.out.println("⏳ Le worm saute...");
                    //for (int i = 0; i < 120; i++) {
                    //    gameManager.update(1.0 / 60.0);
                    //}

                    view.printJumpResult(true, gameManager);
                    gameManager.skipTurn();
                    break;

                case "attaquer":
                    int damage = view.chooseDamage();
                    int x = view.chooseX();
                    int y = view.chooseY();

                    Alien target = gameManager.attackPosition(x, y, damage);
                    view.printAttackResult(target, x, y, gameManager.getActiveTeam());
                    gameManager.skipTurn();
                    view.printTurnSkipped();

                    if (target != null) {
                        gameManager.skipTurn();
                    }
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
        private void wait (int ms) {
            try { Thread.sleep(ms); } catch (InterruptedException e) {}
        }

    }

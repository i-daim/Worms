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
            vue.printTerrain(gameManager);

            // Faire tomber les worms
            System.out.println("\n⏳ Les worms tombent...\n");

            //for (int i = 0; i < 180; i++) {
                //gameManager.update(1.0 / 60.0);
            //}

            // Afficher l'état après la chute
            vue.printStateAfterFall(gameManager);

            while (enCours && !gameManager.isOver()) {
                vue.printGameState(gameManager);
                String command = vue.readKey();
                traiterCommande(command, gameManager);
            }

            if (gameManager.isOver()) {
                vue.printGameOver(gameManager);
            }

            vue.close();
        }

        private void traiterCommande(String command, GameManager gameManager) {
            switch (command) {
                case "gauche":
                    gameManager.getActiveAlien().moveLeft();
                    vue.printMovementResult(true, "gauche", gameManager);
                    gameManager.skipTurn();
                    break;

                case "droite":
                    gameManager.getActiveAlien().moveRight();
                    vue.printMovementResult(true, "droite", gameManager);
                    gameManager.skipTurn();
                    break;

                case "sauter":
                    gameManager.getActiveAlien().jump();

                    //System.out.println("⏳ Le worm saute...");
                    //for (int i = 0; i < 120; i++) {
                    //    gameManager.update(1.0 / 60.0);
                    //}

                    vue.printJumpResult(true, gameManager);
                    gameManager.skipTurn();
                    break;

                case "attaquer":
                    int damage = vue.chooseDamage();
                    int x = vue.chooseX();
                    int y = vue.chooseY();

                    Alien target = gameManager.attackPosition(x, y, damage);
                    vue.printAttackResult(target, x, y, gameManager.getActiveTeam());
                    gameManager.skipTurn();
                    vue.printTurnSkipped();

                    if (target != null) {
                        gameManager.skipTurn();
                    }
                    break;

                case "passer":
                    gameManager.skipTurn();
                    vue.printTurnSkipped();
                    break;

                case "info" :
                    vue.printWelcome();
                    break;

                case "aliens":
                    vue.printAliensStats(gameManager);
                    break;

                case "map":
                    vue.printTerrain(gameManager);
                    vue.printGameState(gameManager);
                    break;

                case "exit":
                    enCours = false;
                    vue.printGameAbandoned();
                    break;

                case null:
                    vue.printUnknownCommand();
                    break;

                default:
                    break;
            }
        }
        private void wait (int ms) {
            try { Thread.sleep(ms); } catch (InterruptedException e) {}
        }

    }

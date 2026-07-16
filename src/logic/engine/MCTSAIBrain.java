package logic.engine;

import exception.InsufficientResourcesException;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import logic.enums.ResourceType;
import logic.models.Edge;
import logic.models.Player;
import logic.models.Sector;
import logic.models.Vertex;
//import logic.models.Map;
import ui.controller.GameBoardController;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MCTSAIBrain implements AIBrain, Serializable {
    private static final long serialVersionUID = 1L;
    private static GameBoardController controller;
    // فلگ برای جلوگیری از فراخوانی همزمان و تداخل نوبت‌ها
    private transient boolean isThinking = false;
    // محدودیت‌ها برای جلوگیری از کرش و OOM
    private static final int SIMULATION_TIME_MS = 3000;
    private static final int MAX_MCTS_ITERATIONS = 50000; // محدودیت کپی‌برداری برای جلوگیری از پر شدن رم
    private static final ExecutorService aiExecutor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true); // جهت بسته شدن خودکار ترد در صورت خروج از برنامه
        return thread;
    });
    public MCTSAIBrain(GameBoardController controller) {
        this.controller = controller;
    }

    @Override
    public void executeTurn(Player aiPlayer, GameEngine mainEngine, Runnable onTurnComplete) {
        if (isThinking) {
            System.out.println("AI is already thinking! Ignoring duplicate turn trigger.");
            return;
        }
        isThinking = true;
        if (controller != null) {
            Platform.runLater(() -> controller.setHumanControlsDisabled(true));
        }

        if (mainEngine.isSetupPhaseActive()) {
            executeSetupPhaseUI(aiPlayer, mainEngine);
            return;
        }

        // شروع چرخه فکر کردن هوش مصنوعی (شروع از گام 0)
        processNextMove(mainEngine, aiPlayer, onTurnComplete, 0);
    }

    /**
     * این متد جایگزین حلقه while شده است تا با رابط کاربری هماهنگ باشد.
     * یک حرکت محاسبه می‌شود، روی گرافیک اعمال می‌شود، و پس از اتمام گرافیک، دوباره این متد صدا زده می‌شود.
     */
    private void processNextMove(GameEngine mainEngine, Player aiPlayer, Runnable onTurnComplete, int stepCount) {
        Player winner = mainEngine.winnerPlayer();
        if (winner != null) {
            System.out.println("Game Over! Winner is: " + winner);
            isThinking = false;
            Platform.runLater(() -> {
                // در اینجا باید متدی از controller را صدا بزنید که صفحه پایان بازی را نشان دهد
                controller.showGameOverScreen(winner);
                refreshUI(mainEngine);
            });
            return; // خروج کامل از حلقه فکر کردن هوش مصنوعی
        }
        // جلوگیری از گیر کردن در لوپ بی‌نهایت
        if (stepCount > 15) {
            endTurnSafely(mainEngine, onTurnComplete);
            return;
        }

        aiExecutor.submit(() -> {
            try {
                // مکث ۱.۵ ثانیه‌ای پیش از هر حرکت تا حس فکر کردن به پلیر منتقل شود
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("MCTS AI is computing move... (Step: " + stepCount + ")");
            MCTSMove bestMove = calculateBestMoveWithMCTS(mainEngine);

            if (bestMove != null) {

                // 1. اگر حرکت تاس ریختن است (باید منتظر انیمیشن تاس بمانیم)
                if (bestMove.isRollDiceMove()) {
                    Platform.runLater(() -> {
                        ArrayList<Integer> dice = mainEngine.rollDiceForCurrentTurn();


                        controller.showDiceResultsUI(dice, () -> {
                            // بررسی آمدن عدد 7 برای حرکت بازرس (Auditor)
                            if (dice.get(0) + dice.get(1) == 7) {
                                aiPlayer.setCanPlaceAuditor(true);
                                tryPlaceAuditorByAI(aiPlayer, mainEngine);
                            }
                            refreshUI(mainEngine);
                            PauseTransition delay = new PauseTransition(Duration.seconds(1.0));
                            delay.setOnFinished(e -> processNextMove(mainEngine, aiPlayer, onTurnComplete, stepCount + 1));
                            delay.play();
                            // بعد از اتمام انیمیشن تاس، برو برای حرکت بعدی
//                            processNextMove(mainEngine, aiPlayer, onTurnComplete, stepCount + 1);
                        });
                    });
                }
                // 2. پایان نوبت
                else if (bestMove.isEndTurn()) {
                    Platform.runLater(() -> endTurnSafely(mainEngine, onTurnComplete));
                }
                // 3. سایر حرکات (ساخت و ساز)
                else {
                    Platform.runLater(() -> {
                        bestMove.execute(mainEngine, aiPlayer, true);
                        refreshUI(mainEngine);

                        // وقفه 1.5 ثانیه‌ای برای دیده شدن حرکت توسط کاربر، سپس فکر برای حرکت بعدی
                        PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
                        delay.setOnFinished(e -> processNextMove(mainEngine, aiPlayer, onTurnComplete, stepCount + 1));
                        delay.play();
                    });
                }
            } else {
                Platform.runLater(() -> endTurnSafely(mainEngine, onTurnComplete));
            }
        });
    }

    private void endTurnSafely(GameEngine mainEngine, Runnable onTurnComplete) {
        System.out.println("MCTS AI ended its turn.");
        isThinking = false;
        mainEngine.endCurrentTurn();
//        refreshUI(mainEngine);
//        if (onTurnComplete != null) onTurnComplete.run();
        Platform.runLater(() -> {
            if (controller != null) {
                // باز کردن قفل دکمه‌ها برای زمانی که نوبت به پلیر انسانی می‌رسد
                controller.setHumanControlsDisabled(false);
            }
            refreshUI(mainEngine);

            // این خط باید فعال باشد تا حلقه نوبت‌های بازی در کنترلر ادامه پیدا کند
//            if (onTurnComplete != null) {
//                onTurnComplete.run();
//            }
        });
    }

    private MCTSMove calculateBestMoveWithMCTS(GameEngine mainEngine) {
        GameEngine simEngine = mainEngine.deepCopy();
        if (simEngine == null) {
            System.err.println("CRITICAL ERROR: mainEngine.deepCopy() returned null!");
            System.err.println("هوش مصنوعی نمی‌تواند شبیه‌سازی کند و نوبتش را می‌سوزاند.");
            return null;
        }
        MCTSNode root = new MCTSNode(null, null, simEngine);
        long endTime = System.currentTimeMillis() + SIMULATION_TIME_MS;
        int iterations = 0;

        // محدودیت زمانی + محدودیت تعداد دفعات (جلوگیری قطعی از Out of Memory)
        while (System.currentTimeMillis() < endTime && iterations < MAX_MCTS_ITERATIONS) {
            System.out.println(iterations);
            MCTSNode promisingNode = selectPromisingNode(root);

            if (!promisingNode.isTerminal()) {
                expandNode(promisingNode);
            }

            MCTSNode nodeToExplore = promisingNode;
            if (!promisingNode.children.isEmpty()) {
                nodeToExplore = promisingNode.children.get(new Random().nextInt(promisingNode.children.size()));
            }

            double playoutResult = simulateRandomPlayout(nodeToExplore, mainEngine.getCurrentPlayerIndex());
            backPropagate(nodeToExplore, playoutResult);

            iterations++;

        }

        MCTSNode bestChild = getBestChild(root, 0);
        return bestChild != null ? bestChild.moveThatGotUsHere : null;
    }

    // =========================================================================
    // بخش محاسباتی درخت MCTS
    // =========================================================================

    private MCTSNode selectPromisingNode(MCTSNode root) {
        MCTSNode node = root;
        while (!node.children.isEmpty()) {
            node = getBestChild(node, 1.41);
        }
        return node;
    }

    private void expandNode(MCTSNode node) {
        Player currentPlayerInState = node.state.getCurrentPlayer();
        List<MCTSMove> possibleMoves = getPossibleMoves(node.state, currentPlayerInState);

        for (MCTSMove move : possibleMoves) {
            GameEngine childState = node.state.deepCopy();
            if (childState == null) continue;

            move.execute(childState, childState.getCurrentPlayer(), false);
            MCTSNode child = new MCTSNode(node, move, childState);
            node.children.add(child);
        }
        node.state = null;
    }

    private double simulateRandomPlayout(MCTSNode node, int aiPlayerIndex) {
        GameEngine playoutEngine = node.state.deepCopy();
        if (playoutEngine == null) return 0.0;

        int depth = 0;
        int maxDepth = 40;

        while (!isGameOver(playoutEngine) && depth < maxDepth) {
            Player currentPlayer = playoutEngine.getCurrentPlayer();
            List<MCTSMove> possibleMoves = getPossibleMoves(playoutEngine, currentPlayer);

            if (possibleMoves.isEmpty()) {
                if (!playoutEngine.isSetupPhase()) {
                    playoutEngine.endCurrentTurn();
                    break;
                } else {
                    break;
                }
            } else {
                MCTSMove randomMove = possibleMoves.get(new Random().nextInt(possibleMoves.size()));
                randomMove.execute(playoutEngine, currentPlayer, false);
            }
            depth++;
        }

        return evaluatePlayout(playoutEngine, aiPlayerIndex);
    }

    private boolean isGameOver(GameEngine engine) {
        for (Player p : engine.getPlayers()) {
            if (p.calculateVictoryPoints() >= 10) return true;
        }
        return false;
    }

    private double evaluatePlayout(GameEngine engine, int aiPlayerIndex) {
        Player aiInPlayout = engine.getPlayers().get(aiPlayerIndex);
        if (aiInPlayout.calculateVictoryPoints() >= 10) return 1.0;


        double score = aiInPlayout.calculateVictoryPoints() * 10.0;
        int totalResources = aiInPlayout.getResourceCount().values().stream().mapToInt(Integer::intValue).sum();
        score += (totalResources * 0.1);
        score += (aiInPlayout.getCompanies().size() * 2.0);
        for (int i = 0; i < engine.getPlayers().size(); i++) {
            if (i != aiPlayerIndex && engine.getPlayers().get(i).calculateVictoryPoints() >= 10) return 0.0;
        }
        return Math.min(score / 150.0, 0.99);
//        return (double) aiInPlayout.calculateVictoryPoints() / 10.0;
    }

    private void backPropagate(MCTSNode node, double result) {
        MCTSNode tempNode = node;
        while (tempNode != null) {
            tempNode.visitCount++;
            tempNode.winScore += result;
            tempNode = tempNode.parent;
        }
    }

    private MCTSNode getBestChild(MCTSNode node, double explorationParameter) {
        MCTSNode bestChild = null;
        double bestUCT = Double.NEGATIVE_INFINITY;

        for (MCTSNode child : node.children) {
            if (child.visitCount == 0) {
                return child;
            }
            double uctValue = (child.winScore / (double) child.visitCount)
                    + explorationParameter * Math.sqrt(Math.log(node.visitCount) / (double) child.visitCount);
            if (uctValue > bestUCT) {
                bestUCT = uctValue;
                bestChild = child;
            }
        }
        return bestChild != null ? bestChild : (node.children.isEmpty() ? null : node.children.get(0));
    }

    // =========================================================================
    // بازگرداندن حرکات مجاز بر اساس مختصات مستقل (ایمن در برابر کپی)
    // =========================================================================

    private List<MCTSMove> getPossibleMoves(GameEngine engine, Player player) {
        List<MCTSMove> moves = new ArrayList<>();

        if (engine.isSetupPhase()) {
            for (int r = 0; r < engine.getMap().getRows(); r++) {
                for (int c = 0; c < engine.getMap().getCols(); c++) {
                    if (engine.canBuildMVP(r, c)) {
                        final int finalR = r;
                        final int finalC = c;
                        moves.add(new MCTSMove() {
                            @Override
                            public void execute(GameEngine e, Player p, boolean isReal) {
                                try {
                                    Vertex v = e.getMap().getVertices()[finalR][finalC];
                                    e.buildMVP(v, p);
                                    if (isReal) buildMVP_UI(e, v);
                                } catch (Exception ex) {
                                }
                            }
                        });
                    }
                }
            }

            List<Edge> allEdges = engine.getMap().getEdges();
            for (int i = 0; i < allEdges.size(); i++) {
                final int edgeIndex = i;
                if (engine.canBuildPartnership(player, allEdges.get(i))) {
                    moves.add(new MCTSMove() {
                        @Override
                        public void execute(GameEngine e, Player p, boolean isReal) {
                            try {
                                Edge simEdge = e.getMap().getEdges().get(edgeIndex);
                                e.buildPartnership(p, simEdge);
                                if (isReal) buildPartnership_UI(e, simEdge);
                            } catch (Exception ex) {
                            }
                        }
                    });
                }
            }

            return moves;
        }

        // ۱. ریختن تاس
        if (!engine.getIsDiceRolled()) {
            moves.add(new MCTSMove() {
                @Override
                public void execute(GameEngine e, Player p, boolean isReal) {
                    try {
                        if (!isReal) e.rollDiceForCurrentTurn(); // در شبیه‌سازی
                    } catch (Exception ex) {
                        e.setIsDiceRolled(true);
                    }
                }

                @Override
                public boolean isRollDiceMove() {
                    return true;
                }
            });
            return moves;
        }
        // ۳. ارتقا به Unicorn
        if (player.hasResourcesForUnicornUpgrade()) {
            for (int r = 0; r < engine.getMap().getRows(); r++) {
                for (int c = 0; c < engine.getMap().getCols(); c++) {
                    if (engine.canUpgradeToUnicorn(r, c)) {
                        final int finalR = r;
                        final int finalC = c;
                        moves.add(new MCTSMove() {
                            @Override
                            public void execute(GameEngine e, Player p, boolean isReal) {
                                try {
                                    Vertex v = e.getMap().getVertices()[finalR][finalC];
                                    e.upgradeToUnicorn(v, p);
                                    if (isReal) upgradeUnicorn_UI(e, v);
                                } catch (Exception ex) {
                                }
                            }
                        });
                    }
                }
            }
        }

        // ۲. ساخت MVP
        if (player.hasResourcesForMVP()) {
            for (int r = 0; r < engine.getMap().getRows(); r++) {
                for (int c = 0; c < engine.getMap().getCols(); c++) {
                    if (engine.canBuildMVP(r, c)) {
                        final int finalR = r;
                        final int finalC = c;
                        moves.add(new MCTSMove() {
                            @Override
                            public void execute(GameEngine e, Player p, boolean isReal) {
                                try {
                                    Vertex v = e.getMap().getVertices()[finalR][finalC];
                                    e.buildMVP(v, p);
                                    if (isReal) buildMVP_UI(e, v);
                                } catch (Exception ex) {
                                }
                            }
                        });
                    }
                }
            }
        }


// ۵. ساخت Partnership (مسیر/جاده)
        // فرض بر این است که متدی مشابه hasResourcesForMVP برای مسیر هم در کلاس Player دارید
        if (player.hasResourcesForPartnership()) {
            List<Edge> allEdges = engine.getMap().getEdges();
            for (int i = 0; i < allEdges.size(); i++) {
                final int edgeIndex = i;
                if (engine.canBuildPartnership(player, allEdges.get(i))) {
                    moves.add(new MCTSMove() {
                        @Override
                        public void execute(GameEngine e, Player p, boolean isReal) {
                            try {
                                // دریافت Edge معادل در موتور شبیه‌سازی شده
                                Edge simEdge = e.getMap().getEdges().get(edgeIndex);
                                e.buildPartnership(p, simEdge);

                                // اگر حرکت واقعی است، گرافیک را آپدیت کن
                                if (isReal) {
                                    buildPartnership_UI(e, simEdge);
                                }
                            } catch (Exception ex) {
                                // مدیریت خطاهای احتمالی در شبیه‌سازی
                            }
                        }
                    });
                }
            }
        }
        for (logic.enums.ResourceType targetResource : logic.enums.ResourceType.values()) {
            if (targetResource == logic.enums.ResourceType.CAPITAL) {
                continue;
            }

            if (canAndShouldBuyResource(engine, player, targetResource)) {
                moves.add(new MCTSMove() {
                    @Override
                    public void execute(GameEngine e, Player p, boolean isReal) {
                        if (isReal) {
                            try {
                                // در بازی واقعی (زمانی که MCTS تصمیم نهایی را گرفت):
                                // از متد اصلی بازار شما استفاده می‌کنیم تا قیمت‌ها جابجا شود و خطاها هندل شوند
                                e.getMarket().buyFromMarket(e, p, targetResource, 1);


                                javafx.application.Platform.runLater(() -> {
                                    controller.refreshPlayersResourcesUI();
                                    controller.resetMarketPricesUI();

                                    // اگر نیاز به رفرش پنل بازار هم هست، متدش را اینجا صدا بزنید
                                });
                            } catch (exception.InsufficientResourcesException |
                                     exception.InvalidMarketTransactionException ex) {
                                ex.printStackTrace(); // در صورت بروز باگ احتمالی
                            }
                        } else {
                            // در دنیای شبیه‌سازی ذهن MCTS (Simulation):
                            // کارهای گرافیکی و استثنائات پیچیده را نادیده می‌گیریم تا سرعت شبیه‌سازی پایین نیاید
                            int price = e.getMarket().getPrice(targetResource);
                            int finalPrice = p.calculateMarketPrice(targetResource, price);
                            p.deductResource(logic.enums.ResourceType.CAPITAL, finalPrice);
                            p.addResource(targetResource, 1);

                            // نکته: اگر در بازی شما با هر خرید، قیمت بالا می‌رود، متد افزایش قیمت
                            // e.getMarket() را باید در اینجا برای شبیه‌ساز هم اعمال کنید.
                        }
                    }

                });

            }


        }


        // ۴. پایان نوبت
        moves.add(new MCTSMove() {
            @Override
            public void execute(GameEngine e, Player p, boolean isReal) {
                e.endCurrentTurn();
            }

            @Override
            public boolean isEndTurn() {
                return true;
            }
        });

        return moves;
    }

    // =========================================================================
    // کلاس‌های واسط
    // =========================================================================

    private interface MCTSMove {
        void execute(GameEngine engine, Player player, boolean isRealMove);

        default boolean isEndTurn() {
            return false;
        }

        default boolean isRollDiceMove() {
            return false;
        }
    }

    private class MCTSNode {
        MCTSNode parent;
        List<MCTSNode> children = new ArrayList<>();
        int visitCount = 0;
        double winScore = 0;
        MCTSMove moveThatGotUsHere;
        GameEngine state;

        public MCTSNode(MCTSNode parent, MCTSMove move, GameEngine state) {
            this.parent = parent;
            this.moveThatGotUsHere = move;
            this.state = state;
        }

        public boolean isTerminal() {
            // ---> این شرط باید اضافه شود <---
            if (this.state == null) return false;
            return MCTSAIBrain.this.isGameOver(this.state);
        }
    }

    // =========================================================================
    // منطق بازرس (Auditor) الهام گرفته از کدهای هوش مصنوعی قبلی شما
    // =========================================================================
    private void tryPlaceAuditorByAI(Player aiPlayer, GameEngine engine) {

        int[] targetCoords = findBestSectorCoordsForAuditor(engine, aiPlayer);

        if (targetCoords != null) {
            int targetRow = targetCoords[0];
            int targetCol = targetCoords[1];

            Platform.runLater(() -> {
                controller.performAIAuditorMove(targetRow, targetCol);
            });
        }
    }

    private int[] findBestSectorCoordsForAuditor(GameEngine engine, Player aiPlayer) {
        Sector[][] sectors = engine.getMap().getSectors();
        int bestScore = -1;
        int[] bestCoords = null;

        for (int r = 0; r < engine.getMap().getRows(); r++) {
            for (int c = 0; c < engine.getMap().getCols(); c++) {
                Sector sector = sectors[r][c];
                if (sector != null && engine.canPlaceAuditor(sector)) {
                    int currentScore = calculateEnemyPresence(sector, aiPlayer);
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestCoords = new int[]{r, c};
                    }
                }
            }
        }

        return bestCoords;
    }

    private int calculateEnemyPresence(logic.models.Sector sector, Player aiPlayer) {
        int score = 0;
        java.util.Map<logic.enums.CornerDirection, Vertex> corners = sector.getCorners();
        for (logic.enums.CornerDirection direction : logic.enums.CornerDirection.values()) {
            Vertex vertex = corners.get(direction);
            logic.models.CompanyStructure structure = vertex.getCompanyStructure();
            if (structure != null && !structure.getOwner().equals(aiPlayer)) {
                if (structure instanceof logic.models.Unicorn) score += 2;
                else if (structure instanceof logic.models.MVP) score += 1;
            }
        }
        return score;
    }

    // =========================================================================
    // مدیریت رابط کاربری
    // =========================================================================

    private void executeSetupPhaseUI(Player aiPlayer, GameEngine engine) {
        Color color = controller.getPlayerColor();
        PauseTransition mvpDelay = new PauseTransition(Duration.seconds(1.5));
        mvpDelay.setOnFinished(e -> {

//            if (!engine.isSetupPlacedMVP()) {
//                placeSetupMVP_UI(aiPlayer, engine, color);
//            }
            if (!engine.isSetupPlacedMVP()) {
                // استفاده از هوش مصنوعی برای پیدا کردن بهترین نقطه
                Vertex bestMVP = findBestSetupMVP(engine);
                if (bestMVP != null) {
                    engine.buildMVP(bestMVP, aiPlayer);
                    Platform.runLater(() -> {
                        controller.updateVertexUI(bestMVP, color);
                        controller.refreshPlayersResourcesUI();
                        controller.updatePlayersPoints();
                    });
                    engine.notifyMVPPlaced();
                }
            }


            PauseTransition partnershipDelay = new PauseTransition(Duration.seconds(1.5));
            partnershipDelay.setOnFinished(e2 -> {
                isThinking = false;


//                if (!engine.isSetupPlacedPartnership()) placeSetupPartnership_UI(aiPlayer, engine, color);
////                engine.endCurrentTurn();
                if (!engine.isSetupPlacedPartnership()) {
                    // استفاده از هوش مصنوعی برای پیدا کردن بهترین مسیر
                    Edge bestEdge = findBestSetupPartnership(engine, aiPlayer);
                    if (bestEdge != null) {
                        engine.buildPartnership(aiPlayer, bestEdge);
                        Platform.runLater(() -> {
                            controller.updateEdgeUI(bestEdge, color);
                            controller.refreshPlayersResourcesUI();
                            controller.updatePlayersPoints();
                        });
                        engine.notifyPartnershipPlaced();
                    }
                }


                refreshUI(engine);
            });
            partnershipDelay.play();
        });
        mvpDelay.play();
    }

    private void buildMVP_UI(GameEngine engine, Vertex targetVertex) {
        Platform.runLater(() -> {
            controller.updateVertexUI(targetVertex, controller.getPlayerColor());
            controller.refreshPlayersResourcesUI();
            controller.updatePlayersPoints();
        });
    }

    private void buildPartnership_UI(GameEngine engine, Edge targetEdge) {
        Platform.runLater(() -> {
            controller.updateEdgeUI(targetEdge, controller.getPlayerColor());
            controller.refreshPlayersResourcesUI();
            controller.updatePlayersPoints();
        });
    }

    private void upgradeUnicorn_UI(GameEngine engine, Vertex targetVertex) {
        Platform.runLater(() -> {
            controller.updateUnicornUI(targetVertex, controller.getPlayerColor());
            controller.refreshPlayersResourcesUI();
            controller.updatePlayersPoints();
        });
    }

    public static void setController(GameBoardController controller) {
        MCTSAIBrain.controller = controller;
    }

    private void refreshUI(GameEngine engine) {
        if (controller == null) return;
        Platform.runLater(() -> {
            if (engine.getCurrentPlayerIndex() == 0) controller.resetMarketPricesUI();
            controller.changePlayerTextColor();
            controller.refreshPlayersResourcesUI();
            controller.updateTurnControls();
        });
    }
    // =========================================================================
    // منطق مدیریت بحران (آمدن عدد 7)
    // =========================================================================


    public java.util.Map<logic.enums.ResourceType, Integer> calculateResourcesToDiscard(Player player) {
        java.util.Map<logic.enums.ResourceType, Integer> currentResources = player.getResourceCount();

        // محاسبه کل منابعی که بازیکن دارد
        int totalResources = currentResources.values().stream().mapToInt(Integer::intValue).sum();
        if (totalResources <= player.getCrisisModifierThreshold()) {
            return new java.util.HashMap<>();
        }
        // محاسبه تعداد کارت‌هایی که باید دور ریخته شوند (نصف منابع)
        int targetDiscardAmount = totalResources / 2;


        java.util.Map<logic.enums.ResourceType, Integer> discardMap = new java.util.HashMap<>();
        java.util.Map<logic.enums.ResourceType, Integer> tempResources = new java.util.HashMap<>(currentResources);

        // ۱. محاسبه تمام منابعی که بازیکن برای اهداف آتی به آن‌ها نیاز دارد
        java.util.Map<logic.enums.ResourceType, Integer> neededResources = calculateNeededResourcesForPlayer(player);

        // ۲. انتخاب هوشمندانه کارت‌ها به تعداد targetDiscardAmount
        for (int i = 0; i < targetDiscardAmount; i++) {
            logic.enums.ResourceType bestCandidateToDiscard = null;
            double maxDiscardScore = -Double.MAX_VALUE;

            for (java.util.Map.Entry<logic.enums.ResourceType, Integer> entry : tempResources.entrySet()) {
                logic.enums.ResourceType resourceType = entry.getKey();
                int currentCount = entry.getValue();

                if (currentCount <= 0) {
                    continue;
                }

                int neededCount = neededResources.getOrDefault(resourceType, 0);
                int excessCount = currentCount - neededCount;

                // محاسبه امتیاز دور ریختن (هرچه امتیاز بیشتر باشد، این منبع گزینه بهتری برای دور ریختن است)
                double discardScore;

                if (excessCount > 0) {
                    // کارت مازاد بر نیاز است: بالاترین اولویت برای دور ریختن
                    // منابعی که مازاد بیشتری دارند یا تعدادشان در دست زیاد است، سریع‌تر دور ریخته می‌شوند
                    discardScore = 1000.0 + (excessCount * 10.0) + currentCount;
                } else {
                    // کارت مازاد نیست و برای ساخت‌وسازهای بعدی نیاز است:
                    // کارت‌هایی که نیاز کمتری به آن‌ها داریم یا تعدادشان نسبتاً بیشتر است، در اولویت دور ریختن قرار می‌گیرند
                    discardScore = 100.0 - (neededCount * 5.0) + currentCount;
                }

                if (discardScore > maxDiscardScore) {
                    maxDiscardScore = discardScore;
                    bestCandidateToDiscard = resourceType;
                }
            }

            // ثبت منبع انتخاب شده برای دور ریختن و کاهش آن از لیست موقت
            if (bestCandidateToDiscard != null) {
                discardMap.put(bestCandidateToDiscard, discardMap.getOrDefault(bestCandidateToDiscard, 0) + 1);
                tempResources.put(bestCandidateToDiscard, tempResources.get(bestCandidateToDiscard) - 1);
            }
        }

        return discardMap;
    }

    /**
     * متد کمکی برای استخراج تمام منابع مورد نیاز بازیکن بر اساس وضعیت فعلی (Unicorn, MVP, Partnership)
     */
    private java.util.Map<logic.enums.ResourceType, Integer> calculateNeededResourcesForPlayer(Player player) {
        java.util.Map<logic.enums.ResourceType, Integer> needed = new java.util.HashMap<>();

        // ۱. اگر بازیکن MVP دارد، نیازهای ارتقا به Unicorn در اولویت قرار می‌گیرد
        if (player.hasMVP()) {
            mergeRequiredCosts(needed, logic.models.Unicorn.UPGRADE_COST);
        }

        // ۲. نیازهای ساخت MVP
        mergeRequiredCosts(needed, logic.models.MVP.CONSTRUCTION_COST);

        // ۳. نیازهای ساخت Partnership (جاده)
        mergeRequiredCosts(needed, logic.models.Partnership.CONSTRUCTION_COST);

        return needed;
    }

    /**
     * ادغام هزینه‌ها با حفظ حداکثر تعداد مورد نیاز برای هر منبع
     */
    private void mergeRequiredCosts(java.util.Map<logic.enums.ResourceType, Integer> targetMap,
                                    java.util.Map<logic.enums.ResourceType, Integer> costMap) {
        if (costMap == null) return;
        for (java.util.Map.Entry<logic.enums.ResourceType, Integer> entry : costMap.entrySet()) {
            int currentMax = targetMap.getOrDefault(entry.getKey(), 0);
            targetMap.put(entry.getKey(), Math.max(currentMax, entry.getValue()));
        }
    }

    // =========================================================================
    // هوش مصنوعی فاز چیدمان (Setup Phase)
    // =========================================================================

    private Vertex findBestSetupMVP(GameEngine engine) {
        Map map = engine.getMap();
        Vertex[][] vertices = map.getVertices();
        Sector[][] sectors = map.getSectors(); // دریافت تمام سکتورهای نقشه
        Vertex bestVertex = null;
        double bestScore = -1;

        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                if (engine.canBuildMVP(r, c)) {
                    Vertex v = vertices[r][c];
                    // حالا سکتورها را هم به تابع ارزیابی پاس می‌دهیم
                    double score = evaluateVertexForSetup(v, sectors);

                    // کمی چاشنی تصادفی برای تنوع در بازی‌های مختلف
                    score += Math.random() * 0.5;

                    if (score > bestScore) {
                        bestScore = score;
                        bestVertex = v;
                    }
                }
            }
        }
        return bestVertex;
    }

    private double evaluateVertexForSetup(Vertex vertex, Sector[][] allSectors) {
        double score = 0;
        List<logic.enums.ResourceType> uniqueResources = new ArrayList<>();

        // پیدا کردن سکتورهایی که این تقاطع (Vertex) یکی از گوشه‌های آن‌هاست
        for (int r = 0; r < allSectors.length; r++) {
            for (int c = 0; c < allSectors[r].length; c++) {
                Sector sector = allSectors[r][c];

                if (sector != null && isVertexInSector(vertex, sector)) {
                    if (sector.getResourceType() != ResourceType.REGULATORY) {
                        // استفاده از متد شما با نام دقیق getactivationNumber
                        int number = sector.getactivationNumber();
                        score += getDiceProbability(number);

                        // بررسی تنوع منابع
                        if (!uniqueResources.contains(sector.getResourceType())) {
                            uniqueResources.add(sector.getResourceType());
                        }
                    }
                }
            }
        }

        // جایزه برای تنوع منابع: هرچه منابع متنوع‌تر، شروع بازی قوی‌تر
        score += (uniqueResources.size() * 2.0);

        return score;
    }

    // متد کمکی برای بررسی اینکه آیا تقاطع مورد نظر، جزو گوشه‌های این سکتور هست یا نه
    private boolean isVertexInSector(Vertex vertex, Sector sector) {
        for (logic.enums.CornerDirection dir : logic.enums.CornerDirection.values()) {
            if (vertex.equals(sector.getCorner(dir))) {
                return true;
            }
        }
        return false;
    }

    // متد کمکی برای محاسبه شانس آمدن هر عدد با دو تاس 6 وجهی
    private int getDiceProbability(int diceNumber) {
        switch (diceNumber) {
            case 2:
            case 12:
                return 1;
            case 3:
            case 11:
                return 2;
            case 4:
            case 10:
                return 3;
            case 5:
            case 9:
                return 4;
            case 6:
            case 8:
                return 5;
            default:
                return 0; // برای کویر یا اعداد نامعتبر
        }
    }

    private Edge findBestSetupPartnership(GameEngine engine, Player aiPlayer) {
        List<Edge> validEdges = new ArrayList<>();
        Sector[][] sectors = engine.getMap().getSectors();

        // پیدا کردن تمام مسیرهای مجازی که بازیکن می‌تواند بسازد
        for (Edge edge : engine.getMap().getEdges()) {
            if (engine.canBuildPartnership(aiPlayer, edge)) {
                validEdges.add(edge);
            }
        }

        // --- پیدا کردن تقاطع (Vertex) آخرین MVP ساخته شده توسط بازیکن ---
        Vertex lastBuiltMVPVertex = null;
        List<logic.models.CompanyStructure> companies = aiPlayer.getCompanies();

        if (companies != null && !companies.isEmpty()) {
            // آخرین ساختمانی که به لیست اضافه شده (آخرین MVP)
            logic.models.CompanyStructure lastCompany = companies.get(companies.size() - 1);

            // جستجو در نقشه برای پیدا کردن مختصات این ساختمان
            outerLoop:
            for (int r = 0; r < engine.getMap().getRows(); r++) {
                for (int c = 0; c < engine.getMap().getCols(); c++) {
                    Vertex v = engine.getMap().getVertices()[r][c];
                    if (v != null && lastCompany.equals(v.getCompanyStructure())) {
                        lastBuiltMVPVertex = v;
                        break outerLoop; // به محض پیدا کردن، از هر دو حلقه خارج می‌شویم
                    }
                }
            }
        }
        // -------------------------------------------------------------

        Edge bestEdge = null;
        double bestEdgeScore = -1;

        for (Edge edge : validEdges) {
            // قانون مهم چیدمان: مسیر باید حتماً به آخرین MVP ساخته شده متصل باشد
            if (lastBuiltMVPVertex != null) {
                if (!edge.getStart().equals(lastBuiltMVPVertex) && !edge.getEnd().equals(lastBuiltMVPVertex)) {
                    continue; // این مسیر متصل به MVP ما نیست، پس رد می‌شویم
                }
            }

            // پیدا کردن سرِ دیگر جاده (تقاطعی که می‌خواهیم به سمت آن حرکت کنیم)
            Vertex targetVertex = edge.getOppositeVertex(lastBuiltMVPVertex);

            // ارزیابی می‌کنیم که این تقاطع جدید چقدر با ارزش است
            double score = (targetVertex != null) ? evaluateVertexForSetup(targetVertex, sectors) : 0;

            if (score > bestEdgeScore) {
                bestEdgeScore = score;
                bestEdge = edge;
            }
        }

        // اگر به هر دلیلی (مثل پر بودن خانه‌های اطراف) مسیر استراتژیکی پیدا نشد
        // یک مسیر مجاز متصل به آخرین ساختمان را به صورت تصادفی انتخاب می‌کنیم
        if (bestEdge == null && !validEdges.isEmpty()) {
            List<Edge> connectedEdges = new ArrayList<>();
            for (Edge e : validEdges) {
                if (lastBuiltMVPVertex == null || e.getStart().equals(lastBuiltMVPVertex) || e.getEnd().equals(lastBuiltMVPVertex)) {
                    connectedEdges.add(e);
                }
            }
            if (!connectedEdges.isEmpty()) {
                return connectedEdges.get(new java.util.Random().nextInt(connectedEdges.size()));
            }
            return validEdges.get(new java.util.Random().nextInt(validEdges.size()));
        }

        return bestEdge;
    }

    /**
     * بررسی می‌کند که آیا بازیکن هم به این منبع نیاز دارد و هم بعد از خرید،
     * سرمایه (Capital) کافی برای خود ساخت‌وساز برایش باقی می‌ماند یا خیر.
     */
    private boolean canAndShouldBuyResource(GameEngine engine, Player player, logic.enums.ResourceType targetResource) {
        java.util.Map<logic.enums.ResourceType, Integer> resources = player.getResourceCount();
        int currentCapital = resources.getOrDefault(logic.enums.ResourceType.CAPITAL, 0);

        // دریافت قیمت بازار دقیقاً با متد خودتان
        int marketPrice = engine.getMarket().getPrice(targetResource);
        int finalCost = player.calculateMarketPrice(targetResource, marketPrice);

        // اگر حتی پول خرید خود منبع را هم نداریم، فالس برگردان
        if (currentCapital < finalCost) return false;

        // ۱. بررسی ارتقا به Unicorn (اگر MVP دارد)
        if (player.hasMVP() && !player.hasResourcesForUnicornUpgrade()) {
            int reservedCapital = logic.models.Unicorn.UPGRADE_COST.getOrDefault(logic.enums.ResourceType.CAPITAL, 0);
            if (currentCapital - finalCost >= reservedCapital) { // چک کردن سرمایه رزرو
                if (targetResource == logic.enums.ResourceType.CLOUD) {
                    int missingCloud = Math.max(0, (logic.models.Unicorn.UPGRADE_COST.get(logic.enums.ResourceType.CLOUD) - player.getUpgradeCloudDiscount()) - resources.getOrDefault(logic.enums.ResourceType.CLOUD, 0));
                    if (missingCloud > 0) return true;
                }
                if (targetResource == logic.enums.ResourceType.DATA) {
                    int missingData = Math.max(0, logic.models.Unicorn.UPGRADE_COST.get(logic.enums.ResourceType.DATA) - resources.getOrDefault(logic.enums.ResourceType.DATA, 0));
                    if (missingData > 0) return true;
                }
            }
        }

        // ۲. بررسی ساخت MVP
        if (!player.hasResourcesForMVP()) {
            int reservedCapital = logic.models.MVP.CONSTRUCTION_COST.getOrDefault(logic.enums.ResourceType.CAPITAL, 0);
            if (currentCapital - finalCost >= reservedCapital) {
                if (targetResource == logic.enums.ResourceType.TALENT) {
                    int missingTalent = Math.max(0, logic.models.MVP.CONSTRUCTION_COST.get(logic.enums.ResourceType.TALENT) - resources.getOrDefault(logic.enums.ResourceType.TALENT, 0));
                    if (missingTalent > 0) return true;
                }
                if (targetResource == logic.enums.ResourceType.CLOUD) {
                    int missingCloud = Math.max(0, logic.models.MVP.CONSTRUCTION_COST.get(logic.enums.ResourceType.CLOUD) - resources.getOrDefault(logic.enums.ResourceType.CLOUD, 0));
                    if (missingCloud > 0) return true;
                }
                if (targetResource == logic.enums.ResourceType.DATA) {
                    int missingData = Math.max(0, logic.models.MVP.CONSTRUCTION_COST.get(logic.enums.ResourceType.DATA) - resources.getOrDefault(logic.enums.ResourceType.DATA, 0));
                    if (missingData > 0) return true;
                }
            }
        }

        // ۳. بررسی ساخت Partnership
        if (!player.hasResourcesForPartnership()) {
            int reservedCapital = logic.models.Partnership.CONSTRUCTION_COST.getOrDefault(logic.enums.ResourceType.CAPITAL, 0);
            if (currentCapital - finalCost >= reservedCapital) {
                if (targetResource == logic.enums.ResourceType.PATENT) {
                    int missingPatent = Math.max(0, logic.models.Partnership.CONSTRUCTION_COST.get(logic.enums.ResourceType.PATENT) - resources.getOrDefault(logic.enums.ResourceType.PATENT, 0));
                    if (missingPatent > 0) return true;
                }
            }
        }
        return false;
    }
}
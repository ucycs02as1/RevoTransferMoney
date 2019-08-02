package org.StavrosAndreou.RevoMoneyTransfer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.LinkedHashMap;
import java.util.Map;

import org.StavrosAndreou.RevoMoneyTransfer.model.Account;
import org.StavrosAndreou.RevoMoneyTransfer.model.Move;


//main class which manages the transfers between Accounts and registers Moves
public class RevolutMoneyTransfer extends AbstractVerticle {
	//creating list of accounts and moves
    private final Map<Integer, Account> accounts = new LinkedHashMap<>();
    private final Map<Integer, Move> moves = new LinkedHashMap<>();


    public static void main(final String[] args) {
        Launcher.executeCommand("run", RevolutMoneyTransfer.class.getName());
    }

    @Override
    public void start(Future<Void> fut) {
    	//creating tmp data for testing
        TempDataCreation();
        //A router receives request from an HttpServer and routes it to the first matching Route that it contains. 
        //A router can contain many routes.
        //Routers are also used for routing failures.
        Router router = Router.router(vertx);

        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("content-type", "text/html")
                    .end("<h1>RevolutMoneyTransfer</h1>");
        });
        //establishing the paths we will be using for the RESTful API calls
        router.route("/assets/*").handler(StaticHandler.create("assets"));

        router.route().handler(BodyHandler.create());
        //GET-PUT-POST=DELETE
        router.get("/api/accounts").handler(this::getAllAccounts);
        router.get("/api/accounts/:id").handler(this::getAccount);
        router.post("/api/accounts").handler(this::addAccount);
        router.put("/api/accounts/:id").handler(this::updateAccount);
        router.delete("/api/accounts/:id").handler(this::deleteAccount);

        router.get("/api/moves").handler(this::getAllMoves);
        router.get("/api/moves/:id").handler(this::getMove);
        router.post("/api/moves").handler(this::addMove);
        router.put("/api/moves/:id").handler(this::updateMove);

        vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(
                        8080,
                        result -> {
                            if (result.succeeded()) {
                                fut.complete();
                            } else {
                                fut.fail(result.cause());
                            }
                        }
                );
    }
    //method to get all accounts currently in our list
    private void getAllAccounts(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(accounts.values()));
    }
    //method to get a specific account
    private void getAccount(RoutingContext routingContext) {
        final String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            final Integer idAsInteger = Integer.valueOf(id);
            Account account = accounts.get(idAsInteger);
            if (account == null) {
                routingContext.response().setStatusCode(404).end();
            } else {
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(account));
            }
        }
    }
    //method to add account
    private void addAccount(RoutingContext routingContext) {
        try {
            final Account account = Json.decodeValue(routingContext.getBodyAsString(),
                    Account.class);
            accounts.put(account.getAccID(), account);
            routingContext.response()
                    .setStatusCode(201)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(account));
        } catch (Exception e) {
            routingContext.response().setStatusCode(400).end();
        }
    }
    //method to update an account
    private void updateAccount(RoutingContext routingContext) {
        final String id = routingContext.request().getParam("id");
        JsonObject json = routingContext.getBodyAsJson();//gets the information from the matched account
        if (id == null || json == null) { //if json or id is null then return 400 failed
            routingContext.response().setStatusCode(400).end();
        } else {
            final Integer idAsInteger = Integer.valueOf(id);
            Account account = accounts.get(idAsInteger);
            if (account == null) { //if account is not found the return not found 404
                routingContext.response().setStatusCode(404).end();
            } else {
                boolean updated = false;
                if (json.getString("name") != null && !json.getString("name").isEmpty()) {
                    account.setOwnerName(json.getString("name"));
                    updated = true;
                }
                if (json.getString("balance") != null && !json.getString("balance").isEmpty() && (new BigDecimal(json.getString("balance"))).compareTo(BigDecimal.ZERO) >= 0) {
                    account.setAccBalance(new BigDecimal(json.getString("balance")));
                    updated = true;
                }
                if (json.getString("currency") != null && !json.getString("currency").isEmpty()) {
                    try {
                        account.setAccCurr(Currency.getInstance(json.getString("currency")));
                        updated = true;
                    } catch (Exception e) {
                        updated = false;
                    }
                }
                if (!updated) {
                    routingContext.response().setStatusCode(400).end();
                } else {
                    routingContext.response()
                            .putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(account));
                }
            }
        }
    }
    //method to delete account
    private void deleteAccount(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else if (accounts.get(Integer.valueOf(id)) == null) {
            routingContext.response().setStatusCode(404).end();
        } else {
            Integer idAsInteger = Integer.valueOf(id);
            accounts.remove(idAsInteger);
            routingContext.response().setStatusCode(204).end();
        }
    }
    //method to get all the moves between accounts
    private void getAllMoves(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(moves.values()));
    }
    //method to get specific move
    private void getMove(RoutingContext routingContext) {
        final String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            final Integer idAsInteger = Integer.valueOf(id);
            Move move = moves.get(idAsInteger);
            if (move == null) {
                routingContext.response().setStatusCode(404).end();
            } else {
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(move));
            }
        }
    }
    //method to add moves
    private void addMove(RoutingContext routingContext) {
        try {
            final Move move = Json.decodeValue(routingContext.getBodyAsString(),
                    Move.class);
            moves.put(move.getId(), move);
            routingContext.response()
                    .setStatusCode(201)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(move));
        } catch (Exception e) {
            routingContext.response().setStatusCode(400).end();
        }
    }
    //method to update move
    private void updateMove(RoutingContext routingContext) {
        final String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            final Integer idAsInteger = Integer.valueOf(id);
            Move move = moves.get(idAsInteger);
            if (move == null) {
                routingContext.response().setStatusCode(404).end();
            } else {
                if (move.getStatus() != Move.StatusMove.EXECUTED && move.getStatus() != Move.StatusMove.FAILED && move.getMoveAmount().compareTo(BigDecimal.ZERO) > 0 && accounts.get(move.getMoveSourceAccID()) != null && accounts.get(move.getMoveDestinationAccID()) != null && accounts.get(move.getMoveSourceAccID()).getAccBalance().compareTo(move.getMoveAmount()) >= 0 && accounts.get(move.getMoveSourceAccID()).getAccCurr().equals(accounts.get(move.getMoveDestinationAccID()).getAccCurr()) && accounts.get(move.getMoveSourceAccID()).getAccCurr().equals(move.getMoveCurr()) && accounts.get(move.getMoveDestinationAccID()).getAccCurr().equals(move.getMoveCurr())) {
                    accounts.get(move.getMoveSourceAccID()).AccWithdrawal(move.getMoveAmount());
                    accounts.get(move.getMoveDestinationAccID()).AccDeposit(move.getMoveAmount());
                    move.setStatus(Move.StatusMove.EXECUTED);
                } else {
                    move.setStatus(Move.StatusMove.FAILED);
                }
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(move));
            }
        }
    }
    //method void to create starting data for the project
    private void TempDataCreation() {
        Account AccountTmp1 = new Account("Stavros Andreou", new BigDecimal("130000"), Currency.getInstance("EUR"));
        accounts.put(AccountTmp1.getAccID(), AccountTmp1);
        Account AccountTmp2 = new Account("Andreas Andreou", new BigDecimal("30000"), Currency.getInstance("EUR"));
        accounts.put(AccountTmp2.getAccID(), AccountTmp2);
        Account AccountTmp3 = new Account("Elena Andreou", new BigDecimal("10000"), Currency.getInstance("GBP"));
        accounts.put(AccountTmp3.getAccID(), AccountTmp3);
        Move TransferTmp1 = new Move(0, 1, new BigDecimal("500"), Currency.getInstance("EUR"), "IKEA");
        moves.put(TransferTmp1.getId(), TransferTmp1);
        Move TransferTmp2 = new Move(1, 2, new BigDecimal("200"), Currency.getInstance("USD"), "Tickets");
        moves.put(TransferTmp2.getId(), TransferTmp2);
        Move TransferTmp3 = new Move(1, 0, new BigDecimal("100"), Currency.getInstance("EUR"), "Nothing");
        moves.put(TransferTmp3.getId(), TransferTmp3);
    }

}

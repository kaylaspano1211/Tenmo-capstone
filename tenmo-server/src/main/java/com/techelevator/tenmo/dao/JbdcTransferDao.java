package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JbdcTransferDao implements TransferDao{

    private final JdbcTemplate jdbcTemplate;

    public JbdcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Transfer addTransfer(Transfer transfer){


        //1. look up account id for the sender and receiver
        //2. insert into transfer table
        //3. update sender and receiver account
        int userFromId = 0;
        int userToId = 0;
        String sqlFrom = "SELECT account.account_id FROM account " +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "WHERE account.user_id = ?;";
        SqlRowSet resultsFromAccount = jdbcTemplate.queryForRowSet(sqlFrom, transfer.getUserFromId());
        if (resultsFromAccount.next()) {
            userFromId = resultsFromAccount.getInt("account_id");
        }

        String sqlTo = "SELECT account.account_id FROM account " +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "WHERE account.user_id = ?;";
        SqlRowSet resultsToAccount = jdbcTemplate.queryForRowSet(sqlTo, transfer.getUserToId());
        if (resultsToAccount.next()) {
            userToId = resultsToAccount.getInt("account_id");
        }

        String sqlInsert = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (2, 2, ?, ?, ?) RETURNING transfer.transfer_id;";
        Integer newId = jdbcTemplate.queryForObject(sqlInsert, Integer.class,
                userFromId, userToId, transfer.getAmount());
        transfer.setTransferId(newId);

        String updateFrom = "UPDATE account SET balance = balance - ? WHERE user_id = ?;";
        jdbcTemplate.update(updateFrom, transfer.getAmount(), userFromId);

        String updateTo = "UPDATE account SET balance = balance + ? WHERE user_id = ?;";
        jdbcTemplate.update(updateTo, transfer.getAmount(), userToId);

        return transfer;
    }


    public List<Transfer> transferList (int userId){
        List<Transfer> transferList = new ArrayList<>();

        String sqlFrom = "SELECT transfer.transfer_id, transfer.amount, transfer.account_from, transfer.account_to, transfer.transfer_status_id, transfer.transfer_type_id " +
                "FROM transfer " +
                "JOIN account ON transfer.account_from = account.account_id " +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "WHERE tenmo_user.user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFrom, userId);

        while(results.next()) {
            transferList.add(mapRowToTransfer(results));
        }

        String sqlTo = "SELECT  transfer.transfer_id, transfer.amount, transfer.account_from, transfer.account_to, transfer.transfer_status_id, transfer.transfer_type_id " +
                "FROM transfer " +
                "JOIN account ON transfer.account_to = account.account_id " +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "WHERE tenmo_user.user_id = ?;";

        SqlRowSet resultsTo = jdbcTemplate.queryForRowSet(sqlTo, userId);

        while(resultsTo.next()) {
            transferList.add(mapRowToTransfer(resultsTo));
        }
        return transferList;
    }


    private Transfer mapRowToTransfer(SqlRowSet result) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(result.getInt("transfer_id"));
        transfer.setUserFromId(result.getInt("account_from"));
        transfer.setUserToId(result.getInt("account_to"));
        transfer.setTransferTypeId(result.getInt("transfer_type_id"));
        transfer.setAmount(result.getDouble("amount"));
        transfer.setTransferStatusId(result.getInt("transfer_status_id"));
        return transfer;
        }

}

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
        int accountFrom = 0;
        int accountTo = 0;
        String sqlFrom = "SELECT account.account_id FROM account " +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "WHERE account.user_id = ?;";
        SqlRowSet resultsFromAccount = jdbcTemplate.queryForRowSet(sqlFrom, transfer.getUserFromId());
        if (resultsFromAccount.next()) {
            accountFrom = resultsFromAccount.getInt("account_id");
        }

        String sqlTo = "SELECT account.account_id FROM account " +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "WHERE account.user_id = ?;";
        SqlRowSet resultsToAccount = jdbcTemplate.queryForRowSet(sqlTo, transfer.getUserToId());
        if (resultsToAccount.next()) {
            accountTo = resultsToAccount.getInt("account_id");
        }

        String sqlInsert = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (2, 2, ?, ?, ?) RETURNING transfer.transfer_id;";
        Integer newId = jdbcTemplate.queryForObject(sqlInsert, Integer.class,
                accountFrom, accountTo, transfer.getAmount());
        transfer.setTransferId(newId);


        String updateFrom = "UPDATE account SET balance = balance - ? WHERE user_id = ?;";
        jdbcTemplate.update(updateFrom, transfer.getAmount(), transfer.getUserFromId());

        String updateTo = "UPDATE account SET balance = balance + ? WHERE user_id = ?;";
        jdbcTemplate.update(updateTo, transfer.getAmount(), transfer.getUserToId());


        return transfer;
    }


    public List<Transfer> transferList (int userId){
        List<Transfer> transferList = new ArrayList<>();

        String sql = "SELECT transfer.transfer_id, userFrom.user_id AS user_id_from, userFrom.username AS user_from, " +
                "userTo.user_id AS user_id_to, userTo.username AS user_to, transfer.amount " +
        "FROM transfer " +
        "JOIN account AS acctFrom ON transfer.account_from = acctFrom.account_id " +
        "JOIN tenmo_user AS userFrom ON acctFrom.user_id = userFrom.user_id " +
        "JOIN account AS acctTo ON transfer.account_to = acctTo.account_id " +
        "JOIN tenmo_user AS userTo ON acctTo.user_id = userTo.user_id " +
        "WHERE userFrom.user_id = ? OR userTo.user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);

        while(results.next()) {
            transferList.add(mapRowToTransfer(results));
        }
        return transferList;
    }

    public Transfer retrieveTransferById(int transferId, int userId){
        Transfer transfer = null;

        String sql = "SELECT transfer.transfer_id, userFrom.user_id AS user_id_from, userFrom.username AS user_from, " +
                "userTo.user_id AS user_id_to, userTo.username AS user_to, transfer.amount " +
                "FROM transfer " +
                "JOIN account AS acctFrom ON transfer.account_from = acctFrom.account_id " +
                "JOIN tenmo_user AS userFrom ON acctFrom.user_id = userFrom.user_id " +
                "JOIN account AS acctTo ON transfer.account_to = acctTo.account_id " +
                "JOIN tenmo_user AS userTo ON acctTo.user_id = userTo.user_id " +
                "WHERE (userFrom.user_id = ? OR userTo.user_id = ?)" +
                "AND transfer_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId, transferId);

        if (results.next()){
            transfer = mapRowToTransfer(results);
        }
        return transfer;

    }



    private Transfer mapRowToTransfer(SqlRowSet result) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(result.getInt("transfer_id"));
        transfer.setAmount(result.getDouble("amount"));
        transfer.setUserFromId(result.getInt("user_id_from"));
        transfer.setUserToId(result.getInt("user_id_to"));
        transfer.setUsernameFrom(result.getString("user_from"));
        transfer.setUsernameTo(result.getString("user_to"));
        return transfer;
        }

}

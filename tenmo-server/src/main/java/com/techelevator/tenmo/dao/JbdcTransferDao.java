package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JbdcTransferDao implements TransferDao{

    private final JdbcTemplate jdbcTemplate;

    public JbdcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public Transfer addTransfer(Transfer transfer){

        Transfer addedTransfer = null;
        //1. look up account id for the sender and receiver
        //2. insert into transfer table
        //3. update sender and receiver account

        String sqlFrom = "SELECT account_id FROM account " +
                "JOIN transfer ON transfer.account_from = account.account_id " +
                "WHERE transfer.transfer_id = ?;";
        SqlRowSet resultsFromAccount = jdbcTemplate.queryForRowSet(sqlFrom, transfer);
        if (resultsFromAccount.next()) {
            addedTransfer = mapRowToTransfer(resultsFromAccount);
        }

        String sqlTo = "SELECT account_id FROM account " +
                "JOIN transfer ON transfer.account_to = account.account_id " +
                "WHERE transfer.transfer_id = ?;";
        SqlRowSet resultsToAccount = jdbcTemplate.queryForRowSet(sqlTo, transfer);
        if (resultsToAccount.next()) {
            addedTransfer = mapRowToTransfer(resultsToAccount);
        }

        String sqlInsert = "INSERT INTO transfer (account_to, account_from, amount) " +
                "VALUES (?, ?, ?) RETURNING transfer.transfer_id;";
        Integer newId = jdbcTemplate.queryForObject(sqlInsert, Integer.class, addedTransfer.getAccountTo(),
                addedTransfer.getAccountFrom(), addedTransfer.getAmount());
        addedTransfer.setTransferId(newId);

        String updateFrom = "UPDATE account SET balance = balance - (SELECT amount FROM transfer WHERE transfer_id = ?) WHERE account_id = ?;";
        jdbcTemplate.update(updateFrom, addedTransfer.getAmount());

        String updateTo = "UPDATE account SET balance = balance + (SELECT amount FROM transfer WHERE transfer_id = ?) WHERE account_id = ?;";
        jdbcTemplate.update(updateTo, addedTransfer.getAmount());

        return addedTransfer;
    }

    private Transfer mapRowToTransfer(SqlRowSet result) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(result.getInt("transfer_id"));
        transfer.setAccountFrom(result.getInt("account_from"));
        transfer.setAccountTo(result.getInt("account_to"));
        transfer.setTransferTypeId(result.getInt("transfer_type_id"));
        transfer.setAmount(result.getDouble("amount"));
        transfer.setTransferStatusId(result.getInt("transfer_status_id"));
        return transfer;
        }

}

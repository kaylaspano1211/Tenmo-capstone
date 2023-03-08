package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account retrieveBalance(int id) {
        Account accountBalance = null;

        String sql = "SELECT balance FROM account " +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "WHERE tenmo_user.user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()){
            accountBalance = mapRowToAccount(results);
        }
        return accountBalance;
    }


    public Transfer addTransfer(Transfer transfer){


        //1. look up account id for the sender and receiver
        //2. insert into transfer table
        //3. update sender and receiver account

        String sqlFrom = "SELECT username, amount, transfer.transfer_id FROM tenmo_user " +
                "JOIN account ON acount.user_id = tenmo_user.user_id " +
                "JOIN transfer ON transfer.acount_from = account.account_id " +
                "WHERE transfer.transfer_id = ?;";
        String sqlTo = "SELECT username, amount, transfer.transfer_id FROM tenmo_user " +
                "JOIN account ON acount.user_id = tenmo_user.user_id " +
                "JOIN transfer ON transfer.acount_to = account.account_id " +
                "WHERE transfer.transfer_id = ?;";
        String sqlInsert = "INSERT INTO account (balance) " +
                           "VALUES (SELECT amount FROM transfer WHERE transfer_id = ?;)";

        String updateFrom = "UPDATE account SET balance ";
        String updateTo = "";

    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}

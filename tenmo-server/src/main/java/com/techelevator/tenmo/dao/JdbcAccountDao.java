package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;

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
                "WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()){
            accountBalance = mapRowToAccount(results);
        }

        return accountBalance;
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.getBalance(rs.getBigDecimal("balance"));
        account.getAccountId(rs.getInt("account_id"));
        return account;
    }
}

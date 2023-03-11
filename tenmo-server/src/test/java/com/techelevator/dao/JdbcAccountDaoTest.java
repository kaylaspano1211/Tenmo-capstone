package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTest extends BaseDaoTests{


    private JdbcAccountDao dao;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcAccountDao(jdbcTemplate);
    }


    @Test
    public void retrieve_balance_returns_correct_balance(){
        BigDecimal balance = dao.retrieveBalance(1001).getBalance();
        Assert.assertEquals(new BigDecimal("1000.00"), balance);
    }


}

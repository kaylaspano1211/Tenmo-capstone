package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private AccountDao accountDao;

    @RequestMapping(path = "/tenmo/accounts", method = RequestMethod.GET)
    public Account retrieveBalance(Principal principal){
        int id = userDao.findIdByUsername(principal.getName());
        Account account = accountDao.retrieveBalance(id);

        return account;
    }

}

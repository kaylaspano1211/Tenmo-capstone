package com.techelevator.tenmo.dao;

import org.springframework.stereotype.Component;

@Component
public class JbdcTransferDao implements TransferDao{

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



}

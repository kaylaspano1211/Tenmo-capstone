package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer addTransfer(Transfer transfer);

    List<Transfer> transferList (int userId);
}

package com.revature.reimbursement.services;

import com.revature.reimbursement.daos.ReimbursementsDAO;
import com.revature.reimbursement.dtos.requests.NewReimbursementRequest;
import com.revature.reimbursement.dtos.requests.ReimbursementUpdateRequest;
import com.revature.reimbursement.dtos.requests.StatusChangeRequest;
import com.revature.reimbursement.models.Reimbursements;
import com.revature.reimbursement.util.customException.InvalidSQLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.internal.matchers.Null;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class ReimbursementsServiceTest {
    @InjectMocks
    private ReimbursementsService reimbursementsService;

    //<editor-fold desc="@Spy">
    @Spy
    private ReimbursementsDAO reimbursementsDAO;
    @Spy
    Reimbursements remMock = new Reimbursements();
    @Spy
    NewReimbursementRequest newReimbursementRequestMock =  new NewReimbursementRequest();
    @Spy
    StatusChangeRequest statusChangeRequestMock =  new StatusChangeRequest();
    @Spy
    ReimbursementUpdateRequest reimbursementUpdateRequestMock = new ReimbursementUpdateRequest();
    //</editor-fold>



    @Test
    void saveReimbursement() {

        // Null value in not null column (amount)
        newReimbursementRequestMock.setAmount(null);
        newReimbursementRequestMock.setSubmitted(Timestamp.from(Instant.now()));
        newReimbursementRequestMock.setDescription("");
        newReimbursementRequestMock.setAuthor_id("0");
        newReimbursementRequestMock.setStatus_id("0");
        newReimbursementRequestMock.setType("LODGING");
        assertThrows(NullPointerException.class, ()->reimbursementsService.saveReimbursement(newReimbursementRequestMock));

        // Invalid Desc:
        newReimbursementRequestMock.setStatus_id("NotARealID");
        assertThrows(NullPointerException.class, ()->reimbursementsService.saveReimbursement(newReimbursementRequestMock));

        // Valid Reimbursement:
        newReimbursementRequestMock.setAmount(0.00);
        doNothing().when(reimbursementsDAO).save(any());
        assertEquals(newReimbursementRequestMock.extractReimbursement().getId(), reimbursementsService.saveReimbursement(newReimbursementRequestMock).getId());
        assertEquals(newReimbursementRequestMock.extractReimbursement().getAmount(), reimbursementsService.saveReimbursement(newReimbursementRequestMock).getAmount());
    }


    @Test
    void updateReimbursement() {
        newReimbursementRequestMock.setId("0");
        newReimbursementRequestMock.setAmount(10.0);
        newReimbursementRequestMock.setSubmitted(Timestamp.from(Instant.now()));
        newReimbursementRequestMock.setDescription("");
        newReimbursementRequestMock.setAuthor_id("49e00c39-e18a-4be1-acaf-ada498240788");
        newReimbursementRequestMock.setStatus_id("0");
        newReimbursementRequestMock.setType("0");
        assertEquals(newReimbursementRequestMock.getAmount(), reimbursementsService.updateReimbursement(newReimbursementRequestMock).getAmount());
        //assertThrows(NullPointerException.class, ()->reimbursementsService.updateReimbursement(newReimbursementRequestMock));



    }

    @Test
    void updateReimbursementStatus() {
        statusChangeRequestMock.setStatus("APPROVED");
        statusChangeRequestMock.setStatus_id("0");
        statusChangeRequestMock.setRem_id("2");
        //reimbursementsService.updateReimbursementStatus(statusChangeRequestMock);
        //System.out.println(reimbursementsService.getById("0").getAmount());
        assertEquals(statusChangeRequestMock.getStatus_id(), reimbursementsService.updateReimbursementStatus(statusChangeRequestMock).getStatus_id());

    }

    @Test
    void deleteReimbursement() {
    }

    @Test
    void getById() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getAllByAuthorID() {
    }

    @Test
    void getRowByColumnValue() {
    }

    @Test
    void getAllRowsByColumnValue() {
    }
}
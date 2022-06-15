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
        //<editor-fold desc="@Mock Data">
        newReimbursementRequestMock.setId("TestID");
        newReimbursementRequestMock.setSubmitted(Timestamp.from(Instant.now()));
        newReimbursementRequestMock.setDescription("");
        newReimbursementRequestMock.setAuthor_id("fb1f34b7-ae8e-48d7-ac92-c82ee720acd4");
        newReimbursementRequestMock.setType("LODGING");
        newReimbursementRequestMock.setType_id("0");
        //</editor-fold>

        // Null value in not null column (amount)
        newReimbursementRequestMock.setAmount(null);
        assertThrows(NullPointerException.class, ()->reimbursementsService.saveReimbursement(newReimbursementRequestMock));

        // Invalid Desc:
        newReimbursementRequestMock.setStatus_id("NotARealID");
        assertThrows(NullPointerException.class, ()->reimbursementsService.saveReimbursement(newReimbursementRequestMock));

        // Valid Reimbursement:
        newReimbursementRequestMock.setAmount(10.00);
        newReimbursementRequestMock.setStatus_id("0");
        doNothing().when(reimbursementsDAO).save(any());
        assertEquals(newReimbursementRequestMock.extractReimbursement().getId(), reimbursementsService.saveReimbursement(newReimbursementRequestMock).getId());




        reimbursementsService.deleteReimbursement("TestID");
        reimbursementsService.deleteReimbursement("TestIDResolved");
    }


    @Test
    void updateReimbursement() {
        //<editor-fold desc="@Mock Data">
        newReimbursementRequestMock.setId("TestIDResolved");
        newReimbursementRequestMock.setAmount(15.00);
        newReimbursementRequestMock.setSubmitted(Timestamp.from(Instant.now()));
        newReimbursementRequestMock.setDescription("");
        newReimbursementRequestMock.setAuthor_id("fb1f34b7-ae8e-48d7-ac92-c82ee720acd4");
        newReimbursementRequestMock.setStatus_id("0");
        newReimbursementRequestMock.setType("LODGING");
        newReimbursementRequestMock.setType_id("0");
        reimbursementsService.saveReimbursement(newReimbursementRequestMock);

        statusChangeRequestMock.setStatus("APPROVED");
        statusChangeRequestMock.setStatus_id("0");
        statusChangeRequestMock.setRem_id("TestIDResolved");
        statusChangeRequestMock.setResolved(Timestamp.from(Instant.now()));
        statusChangeRequestMock.setResolver_id("49e00c39-e18a-4be1-acaf-ada498240788");
        reimbursementsService.updateReimbursementStatus(statusChangeRequestMock).getStatus_id();

        newReimbursementRequestMock.setId("TestID");
        newReimbursementRequestMock.setAmount(10.00);
        newReimbursementRequestMock.setSubmitted(Timestamp.from(Instant.now()));
        newReimbursementRequestMock.setDescription("");
        newReimbursementRequestMock.setAuthor_id("fb1f34b7-ae8e-48d7-ac92-c82ee720acd4");
        newReimbursementRequestMock.setStatus_id("0");
        newReimbursementRequestMock.setType("LODGING");
        newReimbursementRequestMock.setType_id("0");
        reimbursementsService.saveReimbursement(newReimbursementRequestMock);
        //</editor-fold>

        newReimbursementRequestMock.setId("TestID");
        newReimbursementRequestMock.setAmount(20.0);
        newReimbursementRequestMock.setSubmitted(Timestamp.from(Instant.now()));
        newReimbursementRequestMock.setDescription("");
        newReimbursementRequestMock.setType("0");
        newReimbursementRequestMock.setType_id("0");
        assertEquals(newReimbursementRequestMock.getAmount(), reimbursementsService.updateReimbursement(newReimbursementRequestMock).getAmount());

        //Null ID
        newReimbursementRequestMock.setId("Not an ID");
        assertThrows(InvalidSQLException.class, ()->reimbursementsService.updateReimbursement(newReimbursementRequestMock));

        //ID is Already Resolved
        newReimbursementRequestMock.setId("TestIDResolved");
        assertThrows(InvalidSQLException.class, ()->reimbursementsService.updateReimbursement(newReimbursementRequestMock));

        reimbursementsService.deleteReimbursement("TestID");
        reimbursementsService.deleteReimbursement("TestIDResolved");
    }

    @Test
    void updateReimbursementStatus() {
        //<editor-fold desc="@Mock Data">
        newReimbursementRequestMock.setId("TestIDResolved");
        newReimbursementRequestMock.setAmount(15.00);
        newReimbursementRequestMock.setSubmitted(Timestamp.from(Instant.now()));
        newReimbursementRequestMock.setDescription("");
        newReimbursementRequestMock.setAuthor_id("fb1f34b7-ae8e-48d7-ac92-c82ee720acd4");
        newReimbursementRequestMock.setStatus_id("0");
        newReimbursementRequestMock.setType("LODGING");
        newReimbursementRequestMock.setType_id("0");
        reimbursementsService.saveReimbursement(newReimbursementRequestMock);

        statusChangeRequestMock.setStatus("APPROVED");
        statusChangeRequestMock.setStatus_id("0");
        statusChangeRequestMock.setRem_id("TestIDResolved");
        statusChangeRequestMock.setResolved(Timestamp.from(Instant.now()));
        statusChangeRequestMock.setResolver_id("49e00c39-e18a-4be1-acaf-ada498240788");
        reimbursementsService.updateReimbursementStatus(statusChangeRequestMock).getStatus_id();

        newReimbursementRequestMock.setId("TestID");
        newReimbursementRequestMock.setAmount(10.00);
        newReimbursementRequestMock.setSubmitted(Timestamp.from(Instant.now()));
        newReimbursementRequestMock.setDescription("");
        newReimbursementRequestMock.setAuthor_id("fb1f34b7-ae8e-48d7-ac92-c82ee720acd4");
        newReimbursementRequestMock.setStatus_id("0");
        newReimbursementRequestMock.setType("LODGING");
        newReimbursementRequestMock.setType_id("0");
        reimbursementsService.saveReimbursement(newReimbursementRequestMock);
        //</editor-fold>

        statusChangeRequestMock.setStatus("APPROVED");
        statusChangeRequestMock.setStatus_id("0");
        statusChangeRequestMock.setRem_id("TestID");
        statusChangeRequestMock.setResolved(Timestamp.from(Instant.now()));
        statusChangeRequestMock.setResolver_id("49e00c39-e18a-4be1-acaf-ada498240788");
        assertEquals(statusChangeRequestMock.getStatus_id(), reimbursementsService.updateReimbursementStatus(statusChangeRequestMock).getStatus_id());

        //Null ID
        newReimbursementRequestMock.setId("Not");
        assertThrows(InvalidSQLException.class, ()->reimbursementsService.updateReimbursementStatus(statusChangeRequestMock));

        //ID is Already Resolved
        newReimbursementRequestMock.setId("TestIDResolved");
        assertThrows(InvalidSQLException.class, ()->reimbursementsService.updateReimbursementStatus(statusChangeRequestMock));

        reimbursementsService.deleteReimbursement("TestID");
        reimbursementsService.deleteReimbursement("TestIDResolved");
    }

}
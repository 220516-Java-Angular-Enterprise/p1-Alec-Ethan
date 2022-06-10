package com.revature.reimbursement.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.reimbursement.dtos.requests.NewReimbursementRequest;
import com.revature.reimbursement.dtos.requests.NewUserRequest;
import com.revature.reimbursement.dtos.responses.Principal;
import com.revature.reimbursement.models.Reimbursements;
import com.revature.reimbursement.models.Users;
import com.revature.reimbursement.services.ReimbursementsService;
import com.revature.reimbursement.services.TokenService;
import com.revature.reimbursement.services.UserRolesService;
import com.revature.reimbursement.services.UsersService;
import com.revature.reimbursement.util.annotations.Inject;
import com.revature.reimbursement.util.customException.InvalidRequestException;
import com.revature.reimbursement.util.customException.ResourceConflictException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class ReimbursementServlet extends HttpServlet {

    @Inject
    private final ObjectMapper mapper;
    private final UsersService usersService;
    private final UserRolesService userRolesService;
    private final TokenService tokenService;
    private final ReimbursementsService reimbursementsService;

    @Inject
    public ReimbursementServlet(ObjectMapper mapper, UsersService usersService, UserRolesService userRolesService, TokenService tokenService, ReimbursementsService reimbursementsService) {
        this.mapper = mapper;
        this.usersService = usersService;
        this.userRolesService = userRolesService;
        this.tokenService = tokenService;
        this.reimbursementsService = reimbursementsService;
    }

    //I want to create a doGet and this will show different things depending on the user role
    //EMPLOYEE: Display all reimbursements THEY CREATED
    //FINANCE MANAGER: Display all reimbursements
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));
            if (requester == null) {
                resp.setStatus(401); // UNAUTHORIZED
                return;
            }
            String role = userRolesService.getById(requester.getRole()).getRole();
            if (role.equals("FINANCE MANAGER")) {
                //Display all Reimbursements
                List<Reimbursements> rems = reimbursementsService.getAll();
                resp.setContentType("application/json");
                resp.getWriter().write(mapper.writeValueAsString(rems));
            }
            else if (role.equals("EMPLOYEE")) {
                //Display all Reimbursements by Author ID
                List<Reimbursements> rems = reimbursementsService.getAllByAuthorID(requester.getId());
                resp.setContentType("application/json");
                resp.getWriter().write(mapper.writeValueAsString(rems));
            }

        } catch (InvalidRequestException e) {
            resp.setStatus(404); // BAD REQUEST
            e.printStackTrace();
        } catch (ResourceConflictException e) {
            resp.setStatus(409); // RESOURCE CONFLICT
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }

    //EMPLOYEE: Create a new Reimbursement to the Database
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            //Only Employees should be able to create a new reimbursement
            NewReimbursementRequest reimbursementRequest = mapper.readValue(req.getInputStream(), NewReimbursementRequest.class);
            Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));
            String role = userRolesService.getById(requester.getRole()).getRole();
            reimbursementRequest.setSubmitted(Timestamp.from(Instant.now()));
            reimbursementRequest.setAuthor_id(requester.getId());
            reimbursementRequest.setStatus_id("0");
            String[] uris = req.getRequestURI().split("/");

            //Checks the URI to see what page they are on
            //Modify the Reimbursement:
            //Employees can update if their reimbursement is still PENDING.
            //FINANCE MANAGERS can change the reimbursement status


            //Filter the Reimbursement: (FINANCE MANAGER)


            //Create a Reimbursement: (EMPLOYEE)
            if (role.equals("EMPLOYEE")) {
                Reimbursements createdRem = reimbursementsService.saveReimbursement(reimbursementRequest);
                resp.setStatus(201); // CREATED
                resp.setContentType("application/json");
                resp.getWriter().write(mapper.writeValueAsString(createdRem.getId()));
            }
        } catch (InvalidRequestException e) {
            resp.setStatus(404); // BAD REQUEST
            e.printStackTrace();
        } catch (ResourceConflictException e) {
            resp.setStatus(409); // RESOURCE CONFLICT
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }
}

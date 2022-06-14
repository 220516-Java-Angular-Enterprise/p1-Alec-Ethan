package com.revature.reimbursement.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.reimbursement.daos.*;
import com.revature.reimbursement.services.*;
import com.revature.reimbursement.servlets.LoginServlet;
import com.revature.reimbursement.servlets.ReimbursementServlet;
import com.revature.reimbursement.servlets.UsersServlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

/* Need this ContextLoaderListener for our dependency injection upon deployment. */
public class ContextLoaderListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("\nInitializing reimbursement web application");

        /* ObjectMapper provides functionality for reading and writing JSON, either to and from basic POJOs (Plain Old Java Objects) */
        ObjectMapper mapper = new ObjectMapper();

        /* Dependency injection. */
        UsersServlet userServlet = new UsersServlet(mapper, new UsersService(new UsersDAO()), new UserRolesService(new UserRolesDAO()), new TokenService(new JwtConfig()));
        LoginServlet loginServlet = new LoginServlet(mapper, new UsersService(new UsersDAO()), new TokenService(new JwtConfig()));
        ReimbursementServlet reimbursementServlet = new ReimbursementServlet(mapper, new UsersService(new UsersDAO()), new UserRolesService(new UserRolesDAO()), new TokenService(new JwtConfig()), new ReimbursementsService(new ReimbursementsDAO()), new ReimbursementStatusService(new ReimbursementStatusDAO()), new ReimbursementTypesService(new ReimbursementTypesDAO()));

        /* Need ServletContext class to map whatever servlet to url path. */
        ServletContext context = sce.getServletContext();

        //User Related:
        context.addServlet("UsersServlet", userServlet).addMapping("/users/*");
        context.addServlet("LoginServlet", loginServlet).addMapping("/login/*");
        context.addServlet("ReimbursementServlet", reimbursementServlet).addMapping("/reimbursements/*");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("\nShutting down reimbursement web application");
    }
}

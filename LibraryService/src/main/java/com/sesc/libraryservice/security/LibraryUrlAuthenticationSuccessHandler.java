package com.sesc.libraryservice.security;

import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.repository.StudentRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Optional;

public class LibraryUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    protected Log logger = LogFactory.getLog(this.getClass());

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private final StudentRepository studentRepository;

    public LibraryUrlAuthenticationSuccessHandler(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * This method is called whenever the user is successfully authenticated
     *
     * @param request        the request object
     * @param response       the response object
     * @param chain          the filter chain
     * @param authentication the authentication object
     * @throws IOException if the response cannot be redirected
     */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authentication) throws IOException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    /**
     * This method is called whenever the user is successfully authenticated
     *
     * @param request        the request object
     * @param response       the response object
     * @param authentication the authentication object
     * @throws IOException if the response cannot be redirected
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    /**
     * Helper function to handle the redirection of the user after successful login
     *
     * @param request        the request object
     * @param response       the response object
     * @param authentication the authentication object
     * @throws IOException if the response cannot be redirected
     */
    protected void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        // Setting the Target URL based on the Authentication
        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            logger.debug(
                    "Response has already been committed. Unable to redirect to "
                            + targetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    /**
     * Helper function to determine the target URL based on the authentication
     *
     * @param authentication the authentication object
     * @return the target URL
     */
    protected String determineTargetUrl(final Authentication authentication) {
        // Getting the Student ID from the Authentication
        String studentId = authentication.getName();

        // Fetching the Student from the Database
        Optional<Student> student = studentRepository.findStudentByStudentId(studentId);
        // Applying the logic to check if it is the Student first login
        if (student.isPresent()) {
            if (student.get().isFirstLogin()) {
                return "/changepassword";
            }
            return "/home";
        }
        throw new IllegalStateException();
    }

    /**
     * Helper function to clear the authentication attributes
     *
     * @param request the request object
     */
    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}

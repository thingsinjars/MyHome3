package com.myhome.security;

import com.myhome.domain.User;
import com.myhome.services.CommunityService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO
 */
public class CommunityAuthorizationFilter extends BasicAuthenticationFilter {
    private final CommunityService communityService;
    private final String uuidPattern = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
    private final Pattern addAdminRequestPattern = Pattern.compile("/communities/" + uuidPattern + "/admins");


    public CommunityAuthorizationFilter(AuthenticationManager authenticationManager,
                                        CommunityService communityService) {
        super(authenticationManager);
        this.communityService = communityService;
    }

    /**
     * filters incoming HTTP requests based on a pattern and user authentication. If the
     * URL matches the pattern and the user is not an admin, it sets the response status
     * to SC_UNAUTHORIZED and returns. Otherwise, it delegates to the super method for
     * further filtering.
     * 
     * @param request HTTP request received by the filter.
     * 
     * 	- `getRequestURI()` returns the request URI, which is the path of the requested
     * resource.
     * 	- `matcher` refers to an instance of `Matcher`, which is used to match the URL
     * pattern of the request against a regular expression.
     * 	- `find()` method of the `Matcher` object returns `true` if the URL matches the
     * pattern, otherwise it returns `false`.
     * 	- `isUserCommunityAdmin(request)` is a method that checks whether the current
     * user is an administrator of a user community. If the method returns `false`, the
     * response status code is set to `HttpServletResponse.SC_UNAUTHORIZED`.
     * 	- `super.doFilterInternal(request, response, chain)` calls the superclass's
     * implementation of the `doFilterInternal` method, which handles the actual filtering
     * of the request.
     * 
     * @param response HttpServletResponse object that contains information about the
     * HTTP request and is used to send the response back to the client.
     * 
     * 	- `response`: A reference to the HttpServletResponse object that represents the
     * response sent to the client.
     * 	- `request`: A reference to the HttpServletRequest object that represents the
     * request received from the client.
     * 	- `chain`: A reference to the FilterChain object that represents the chain of
     * filters that have been applied to the request.
     * 
     * The function performs an internal filter operation on the request and response
     * objects, and then passes the request to the next filter in the chain for further
     * processing.
     * 
     * @param chain FilterChain that needs to be processed by the overridden doFilterInternal
     * method.
     * 
     * 	- `request`: The incoming HTTP request object.
     * 	- `response`: The HTTP response object.
     * 	- `chain`: The FilterChain object representing the chain of filters to be executed.
     * 	- `isUserCommunityAdmin`: A boolean value indicating whether the user is an admin
     * of a community or not.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        Matcher urlMatcher = addAdminRequestPattern.matcher(request.getRequestURI());

        if (urlMatcher.find() && !isUserCommunityAdmin(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        super.doFilterInternal(request, response, chain);
    }

    /**
     * determines if a user is a community admin based on their user ID and the community
     * ID in the request URL. It retrieves the list of community admins from the service,
     * filters out non-matching users, and returns whether the user is a community admin
     * or not.
     * 
     * @param request HTTP request that triggered the function execution and provides the
     * community ID from the request URI.
     * 
     * 	- `request`: An instance of `HttpServletRequest`, representing an HTTP request
     * made to the server.
     * 	- `getRequestURI()`: Returns the string representation of the request URI, which
     * contains the path and query parameters of the request.
     * 	- `split()`: Splits the request URI into a array of strings using the specified
     * separator.
     * 	- `[]`: Extracts the second element of the array, which represents the community
     * ID in the function's context.
     * 
     * @returns a boolean value indicating whether the current user is an administrator
     * of a specific community.
     */
    private boolean isUserCommunityAdmin(HttpServletRequest request) {
        String userId = (String) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        String communityId = request
                .getRequestURI().split("/")[2];
        Optional<List<User>> optional = communityService
                .findCommunityAdminsById(communityId, null);

        if (optional.isPresent()) {
            List<User> communityAdmins = optional.get();
            User admin = communityAdmins
                    .stream()
                    .filter(communityAdmin -> communityAdmin.getUserId().equals(userId))
                    .findFirst()
                    .orElse(null);

            return admin != null;
        }

        return false;
    }
}
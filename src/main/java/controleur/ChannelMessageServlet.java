package controleur;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.ChannelDAO;
import dao.DAOFactory;
import dao.MessageDAO;
import dao.UserDAO;
import dto.Channel;
import dto.Message;
import dto.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.JwtUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.*;

@WebServlet("/channels/*")
public class ChannelMessageServlet extends HttpServlet {
    ChannelDAO channelDAO = DAOFactory.getCanalDAO();
    MessageDAO messageDAO = DAOFactory.getMessageDAO();
    UserDAO userDAO = DAOFactory.getUtilisateurDAO();
    static ObjectMapper om = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(req.getMethod())) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        List<Integer> position = getPosition(req.getPathInfo()); // [id_canal, id_message] from pathinfo

        if (position.get(0) == null) { // case: no specified channel => GET all channels
            ArrayList<Channel> channels = channelDAO.findAll();
            ArrayList<Channel> accessible = new ArrayList<>();

            for (Channel channel : channels) {
                if (channel.is_public() || hasUserAccess(req, resp, channel) != null){
                    accessible.add(channel);
                }
            }

            if (accessible.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            }
            out.println(om.writeValueAsString(accessible));
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // case: specified channel
        try {
            Channel channel = channelDAO.findById(position.get(0));

            // verify access to channel
            if ((!channel.is_public()) && (hasUserAccess(req, resp, channel) == null)) return;

            if (position.get(1) == null){ // case: no specified message => GET all messages from specified channel
                ArrayList<Message> messages = messageDAO.findAllInCanal(channel);
                if (messages == null || messages.isEmpty()) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    return;
                }
                out.println(om.writeValueAsString(messages));
                resp.setStatus(HttpServletResponse.SC_OK);

            } else { // case: specified message => GET specified message
                Message message = messageDAO.findById(position.get(1));
                out.println(om.writeValueAsString(message));
                resp.setStatus(HttpServletResponse.SC_OK);
            }

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        resp.setContentType("application/json");
        List<Integer> position = getPosition(req.getPathInfo()); // [id_canal, id_message] from pathinfo
        if (position.get(0) == null) { // case: "site_url/channels/" => not allowed
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
         // case: "site_url/channels/i"
        Channel channel = channelDAO.findById(position.get(0));

        // verify access to channel
        User user = hasUserAccess(req, resp, channel);
        if(user == null) return;

        if(position.get(1) == null){
            Map<String,Object> map = getJsonFromRequest(req);

            // check values validity:
            Object text = map.get("text");
            if(text == null){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            Message message = new Message(-1, channel.getId(), user.getUser(), (String)text, timestamp, timestamp);

            // save new message:
            try {
                message = messageDAO.save(message);
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().println(om.writeValueAsString(message));
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        resp.setContentType("application/json");
        List<Integer> position = getPosition(req.getPathInfo()); // [id_canal, id_message] from pathinfo

        if (position.get(1) == null) { // case: "site_url/channels/", "site_url/channels/1/" or "site_url/channels/1/messages/"
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        // case: "site_url/channels/1/messages/2"
        Channel channel = channelDAO.findById(position.get(0));
        Message message = messageDAO.findById(position.get(1));

        // verify access to channel
        User user = hasUserAccess(req, resp, channel);
        if (user == null) return;
        if (!message.getIdAuteur().equals(user.getUser())){
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Map<String,Object> map = getJsonFromRequest(req);

        // check values validity:
        Object text = map.get("text");
        if(text == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        message.setText((String) text);
        message.setLastModification(timestamp);

        // update message:
        try {
            messageDAO.update(message);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_CONFLICT);
            return;
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        List<Integer> position = getPosition(req.getPathInfo()); // [id_canal, id_message] from pathinfo

        if (position.get(1) == null) { // case: "site_url/channels/", "site_url/channels/1/" or "site_url/channels/1/messages/"
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        // case: "site_url/channels/1/messages/2"
        Channel channel = channelDAO.findById(position.get(0));
        Message message = messageDAO.findById(position.get(1));

        if (message == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // check access rights:
        User user = hasUserAccess(req, resp, channel);
        if (user == null) return;
        if (!(message.getIdAuteur().equals(user.getUser()) || channel.getId_createur().equals(user.getUser()))) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // delete message:
       if (!messageDAO.delete(message)){
           resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
           resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private Map<String, Object> getJsonFromRequest(HttpServletRequest req) throws IOException {
        BufferedReader reader = req.getReader();
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        String json = builder.toString();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Map.class);
    }

    private List<Integer> getPosition(String pathInfo){
        // return the position of the path as (id_canal, id_message). Values are null when undefined in the path.
        List<Integer> positions = new ArrayList<>();
        positions.add(null);
        positions.add(null);
        if (pathInfo == null || pathInfo.equals("/")) {
            // case: "channels" or "channels/"
            // -> return (null, null)
            return positions;
        }
        String[] parsedPath = pathInfo.split("/");
        positions.set(0, Integer.valueOf(parsedPath[1]));
        if (parsedPath.length < 4 || parsedPath[3].isEmpty()) {
            // case: "channels/i", "channels/i/" or "channels/i/messages/"
            // -> return (i, null)
            return positions;
        }
        // case: "channels/i/messages/j" or "channels/i/messages/j/"
        // -> return (i, j)
        positions.set(1, Integer.valueOf(parsedPath[3]));
        return positions;
    }

    private User hasUserAccess(HttpServletRequest req, HttpServletResponse resp, Channel channel) {
        String username = JwtUtil.getUser(req);
        if (username == null){ // case: user not logged in / user didn't use their token
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        User user = userDAO.findByUser(username);
        if (!channelDAO.findAllJoined(user).contains(channel)) { // case : user logged in but not allowed in channel
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        return user; // case: user logged in and allowed in channel
    }

}



package dao;

public class DAOFactory {
    protected static final DbConnectionManager dbManager = DbConnectionManager.getInstance();

    public static ChannelDAO getCanalDAO(){
        return new ChannelDAOBD(dbManager);
    }

    public static MessageDAO getMessageDAO(){
        return new MessageDAOBD(dbManager);
    }

    public static UserDAO getUtilisateurDAO(){
        return new UserDAOBD(dbManager);
    }

}

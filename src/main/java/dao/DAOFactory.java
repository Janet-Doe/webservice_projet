package dao;

public class DAOFactory {
    protected static final DbConnectionManager dbManager = DbConnectionManager.getInstance();

    public static CanalDAO getCanalDAO(){
        return new CanalDAOBD(dbManager);
    }

    public static MessageDAO getMessageDAO(){
        return new MessageDAOBD(dbManager);
    }

    public static UtilisateurDAO getUtilisateurDAO(){
        return new UtilisateurDAOBD(dbManager);
    }

}

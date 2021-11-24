package dao;

import dao.impl.DepartamentoDaoJDBC;
import dao.impl.PessoaDaoJDBC;
import db.DB;

public class DaoFactory {
    
    public static PessoaDao createPessoaDao(){
        
    	return new PessoaDaoJDBC(DB.getConnection());
    }
    
    public static DepartamentoDao createDepartamentoDao() {
    	
    	return new DepartamentoDaoJDBC(DB.getConnection());
    }
}

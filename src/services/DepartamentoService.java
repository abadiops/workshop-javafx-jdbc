package services;

import java.util.List;

import dao.DaoFactory;
import dao.DepartamentoDao;
import model.Departamento;

public class DepartamentoService {
	
	private DepartamentoDao dao = DaoFactory.createDepartamentoDao();

	public List<Departamento> listarTodos(){
		
		return dao.localizarTodos();
	}
	
	public void saveOrUpdate(Departamento obj) {
		
		if (obj.getId() == null) {
			
			dao.inserir(obj);
		}
		else {
			dao.atualiziar(obj);
		}
	}
	
	public void remove(Departamento obj) {
		dao.deletarById(obj.getId());
	}
}

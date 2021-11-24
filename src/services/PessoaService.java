package services;

import java.util.List;

import dao.DaoFactory;
import dao.PessoaDao;
import model.Pessoa;

public class PessoaService {
	
	private PessoaDao dao = DaoFactory.createPessoaDao();

	public List<Pessoa> listarTodos(){
		
		return dao.localizarTodos();
	}
	
	public void saveOrUpdate(Pessoa obj) {
		
		if (obj.getId() == null) {
			
			dao.inserir(obj);
		}
		else {
			dao.atualiziar(obj);
		}
	}
	
	public void remove(Pessoa obj) {
		dao.deletarById(obj.getId());
	}
}

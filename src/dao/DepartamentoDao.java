package dao;

import java.util.List;
import model.Departamento;

public interface DepartamentoDao {
    
    void inserir(Departamento departamento);
    
    void atualiziar(Departamento departamento);
    
    void deletarById(Integer id);
    
    Departamento localizarById(Integer id);
    
    List<Departamento> localizarTodos();
    
}

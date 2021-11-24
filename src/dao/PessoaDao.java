package dao;

import java.util.List;

import model.Departamento;
import model.Pessoa;

public interface PessoaDao {

    void inserir(Pessoa pessoa);

    void atualiziar(Pessoa pessoa);

    void deletarById(Integer id);

    Pessoa localizarById(Integer id);

    List<Pessoa> localizarTodos();
    
    List<Pessoa> localizarByDepartamento(Departamento departamento);
}

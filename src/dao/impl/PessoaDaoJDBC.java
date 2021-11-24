package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.PessoaDao;
import db.DB;
import db.DbException;
import model.Departamento;
import model.Pessoa;

import java.sql.Statement;

public class PessoaDaoJDBC implements PessoaDao {

	private Connection conn;

	public PessoaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void inserir(Pessoa pessoa) {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("INSERT INTO pessoa " + "(Name, Email, BirthDate, BaseSalary, DepartamentoId) "
					+ "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, pessoa.getFuncionario());
			st.setString(2, pessoa.getEmail());
			st.setDate(3, new java.sql.Date(pessoa.getDataNascimento().getTime()));
			st.setDouble(4, pessoa.getSalarioBase());
			st.setInt(5, pessoa.getDepartamento().getId());

			int linhasAlteradas = st.executeUpdate();

			if (linhasAlteradas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					pessoa.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void atualiziar(Pessoa pessoa) {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(

					"UPDATE pessoa " + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartamentoId = ? "
							+ "WHERE Id = ?");

			st.setString(1, pessoa.getFuncionario());
			st.setString(2, pessoa.getEmail());
			st.setDate(3, new java.sql.Date(pessoa.getDataNascimento().getTime()));
			st.setDouble(4, pessoa.getSalarioBase());
			st.setInt(5, pessoa.getDepartamento().getId());
			st.setInt(6, pessoa.getId());

			st.execute();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deletarById(Integer id) {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("DELETE FROM pessoa WHERE Id = ?", Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, id);

			int linhaAlterada = st.executeUpdate();

			if (linhaAlterada > 0) {
				throw new DbException("Registro Apagado com Sucesso com Id = " + id);
			} else {
				throw new DbException("Nenhum Registro apagado - Id " + id + " não encontrado!!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Pessoa localizarById(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT pessoa.*,department.Name as DepName " + "FROM pessoa INNER JOIN department "
							+ "ON pessoa.DepartamentoId = department.Id " + "WHERE pessoa.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {
				Departamento dep = instantiateDepartamento(rs);
				Pessoa obj = instantiatePessoa(rs, dep);
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Pessoa> localizarByDepartamento(Departamento department) {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT pessoa.*,department.Name as DepName "
					+ "FROM pessoa INNER JOIN department " + "ON pessoa.DepartamentoId = department.Id "
					+ "WHERE DepartamentoId = ? " + "ORDER BY Name");

			st.setInt(1, department.getId());

			rs = st.executeQuery();

			List<Pessoa> list = new ArrayList<>();
			Map<Integer, Departamento> map = new HashMap<>();

			while (rs.next()) {

				Departamento dep = map.get(rs.getInt("DepartamentoId"));

				if (dep == null) {
					dep = instantiateDepartamento(rs);
					map.put(rs.getInt("DepartamentoId"), dep);
				}

				Pessoa obj = instantiatePessoa(rs, dep);
				list.add(obj);
			}
			return list;
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Pessoa> localizarTodos() {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT pessoa.*,departamento.departamento as DepName " + "FROM pessoa INNER JOIN departamento "
							+ "ON pessoa.DepartmentId = departamento.Id " + "ORDER BY Id");

			rs = st.executeQuery();

			List<Pessoa> list = new ArrayList<>();
			Map<Integer, Departamento> map = new HashMap<>();

			while (rs.next()) {

				Departamento dep = map.get(rs.getInt("DepartmentId"));

				if (dep == null) {
					dep = instantiateDepartamento(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}

				Pessoa obj = instantiatePessoa(rs, dep);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Pessoa instantiatePessoa(ResultSet rs, Departamento dep) throws SQLException {
		
		Pessoa obj = new Pessoa();
		obj.setId(rs.getInt("Id"));
		obj.setFuncionario(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setSalarioBase(rs.getDouble("BaseSalary"));
		obj.setDataNascimento(rs.getDate("BirthDate"));
		obj.setDepartamento(dep);
		
		return obj;
	}

	private Departamento instantiateDepartamento(ResultSet rs) throws SQLException {
		
		Departamento dep = new Departamento();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setDepartamento(rs.getString("DepName"));
		
		return dep;
	}

}

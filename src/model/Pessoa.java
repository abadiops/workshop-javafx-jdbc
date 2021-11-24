package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Pessoa implements Serializable {

    private Integer id;
    private String funcionario;
    private String email;
    private Date dataNascimento;
    private Double salarioBase;
    
    private Departamento departamento;
    
    public Pessoa(){        
    }

    public Pessoa(Integer id, String funcionario, String email, Date dataNascimento, Double slarioBase, Departamento departamento) {
        this.id = id;
        this.funcionario = funcionario;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.salarioBase = slarioBase;
        this.departamento = departamento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Double getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(Double salarioBase) {
        this.salarioBase = salarioBase;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pessoa other = (Pessoa) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Seller{" + "id=" + id + ", "
                + "funcionario=" + funcionario
                + ", email=" + email + ", dataNascimento=" + dataNascimento
                + ", salarioBase=" + salarioBase 
                + ", departamento=" + departamento + '}';
    }
    
   
    
    

}

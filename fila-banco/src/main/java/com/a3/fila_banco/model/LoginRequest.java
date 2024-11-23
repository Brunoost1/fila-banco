package com.a3.fila_banco.model;

public class LoginRequest {
       private String email;
    private String senha;  // Mudado de password para senha

    // Getters e Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {  
        return senha;
    }

    public void setSenha(String senha) {  
        this.senha = senha;
    }
}
package com.lubricentro.mantenimiento.security;

import org.springframework.security.core.AuthenticationException;

public class UsuarioInactivoException extends AuthenticationException {
    public UsuarioInactivoException(String mensaje) {
        super(mensaje);
    }
}

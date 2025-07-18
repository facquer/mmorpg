package com.facquer.mmorpg.utils;

import org.hibernate.PropertyValueException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UtilsApi {

    private UtilsApi() {
    }

    public static List<String> getProcessExceptionMessage(Exception ex, int size) {
        List<String> listMessage = new ArrayList<>();
        try {
            String msg = "";
            String msgError = "";
            if (Objects.isNull(ex)) {
                return listMessage;
            } else {
                if (!Objects.isNull(ex.getCause())) {
                    if (!Objects.isNull(ex.getCause().getCause())) {
                        String text = ex.getCause().getCause().getMessage();
                        if (text.contains("Detail")) {
                            text = text.substring(text.indexOf("Detail"), text.length() - 2);
                        }
                        msg += text + "\n";
                    } else if (!Objects.isNull(ex.getCause())) {
                        PropertyValueException pp = (PropertyValueException) ex.getCause();
                        String text = "";
                        //controlar mensajes de not null
                        if (pp.getMessage().contains("not-null")) {
                            text = pp.getMessage().substring(0, pp.getMessage().indexOf("com.")) + " " + pp.getPropertyName();
                        }
                        msg += text + "\n";
                    }
                } else if (!isNullEmpty(ex.getMessage())) {
                    msg = ex.getMessage();
                } else if (isNotNullEmpty(ex.getLocalizedMessage())) {
                    msg += "\n" + ex.getLocalizedMessage();
                }
                try {
                    if (!Objects.isNull(ex.getCause())) {
                        msgError += getCauseException(ex);
                    } else {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        ex.printStackTrace(pw);
                        String sStackTrace = sw.toString(); // stack trace as a string
                        msgError += sStackTrace + "\n";
                    }
                } catch (Exception e) {
                    Logger.getLogger(UtilsApi.class.getName()).log(Level.SEVERE,
                            "Error al generar msg de getprocessExceptionMessage", e);
                }
            }
            if (isNullEmpty(msg)) {
                msg = "ERROR NO IDENTIFICADO";
            }
            listMessage.add(msg.substring(0, msg.length() < size ? msg.length() : size));
            listMessage.add(msgError.substring(0, msgError.length() < size ? msgError.length() : size));
        } catch (Exception e) {
            listMessage.add("ERROR UNICO DE PARSEO");
            listMessage.add("Error no identificado");
        }

        return listMessage;
    }

    public static boolean isNullEmpty(Object object) {
        if (Objects.isNull(object))
            return true;
        else if (object.toString().isEmpty())
            return true;
        else
            return false;

    }

    public static boolean isNotNullEmpty(Object object) {
        return !isNullEmpty(object);
    }

    public static String getCauseException(Throwable ex) {
        String msg = (ex.getCause() == null ? "" : ex.getCause().getMessage());
        if (ex.getCause().getCause() != null) {
            msg += (isNullEmpty(ex.getCause().getCause().getMessage()) ? "" : ex.getCause().getCause().getLocalizedMessage()) + "\n";
        }
        return msg;
    }

    public static <T> GenericResponse<T> buildErrorResponse(Exception ex) {
        GenericResponse<T> response = new GenericResponse<>();
        response.setObject(null);
        response.saveMessage(getProcessExceptionMessage(ex, 500));

        // Lógica para determinar el estado según la excepción
        if (ex instanceof org.springframework.security.access.AccessDeniedException) {
            response.setState(StateEnum.UNAUTHORIZED);
        } else if (ex instanceof IllegalArgumentException || ex instanceof IllegalStateException) {
            response.setState(StateEnum.WARN);
        } else {
            response.setState(StateEnum.ERROR);
        }

        return response;
    }

    public static <T> GenericResponse<T> buildResponse(T object, Exception ex) {
        if (ex == null) {
            GenericResponse<T> response = new GenericResponse<>();
            response.setState(StateEnum.OK);
            response.setObject(object);
            response.setMessage("Proceso exitoso");
            return response;
        } else {
            return buildErrorResponse(ex);
        }
    }
}

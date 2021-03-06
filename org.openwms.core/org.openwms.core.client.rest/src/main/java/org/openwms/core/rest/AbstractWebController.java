/*
 * openwms.org, the Open Warehouse Management System.
 * Copyright (C) 2014 Heiko Scherrer
 *
 * This file is part of openwms.org.
 *
 * openwms.org is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * openwms.org is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software. If not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.openwms.core.rest;

import javax.validation.ValidationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * A AbstractWebController.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version $Revision: $
 * @since 0.1
 */
public abstract class AbstractWebController {

    @Autowired
    @Qualifier("messageSourceRest")
    private MessageSource messageSource;
    @Autowired
    @Qualifier("coreRestValidator")
    private Validator validator;

    /**
     * All general exceptions thrown by services are caught here and translated
     * into http conform responses with a status code
     * {@code 500 Internal Server Error}.
     * 
     * @param ex
     *            The exception occurred
     * @return A response object wraps the server result
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseVO> handleException(Exception ex) {
        if (ex.getClass().equals(HttpBusinessException.class)) {
            HttpBusinessException e = (HttpBusinessException) ex;
            return new ResponseEntity<>(new ResponseVO(ex.getMessage(), e.getHttpStatus()), e.getHttpStatus());
        }
        if (ex.getClass().equals(ValidationException.class)) {
            return new ResponseEntity<>(new ResponseVO(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),
              HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseVO(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Transforming {@link MethodArgumentNotValidException}
     * {@link ValidationException} into server error
     * {@code 500 Internal Server Error} responses with according validation
     * error message text.
     * 
     * @return A response object wraps the server result
     */
    @ExceptionHandler({ MethodArgumentNotValidException.class, ValidationException.class })
    public ResponseEntity<ResponseVO> handleValidationException() {
        return new ResponseEntity<>(new ResponseVO(translate(ExceptionCodes.VALIDATION_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Get the messageSource.
     * 
     * @param key
     *            The error code to search message text for
     * @param objects
     *            Any arguments that are passed into the message text
     * @return the messageSource.
     */
    protected String translate(String key, Object... objects) {
        return messageSource.getMessage(key, objects, null);
    }
}

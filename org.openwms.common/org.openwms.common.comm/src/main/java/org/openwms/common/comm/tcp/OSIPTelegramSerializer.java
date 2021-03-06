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
package org.openwms.common.comm.tcp;

import java.io.*;
import java.nio.charset.Charset;

import org.openwms.common.comm.api.CommonMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.serializer.Serializer;
import org.springframework.integration.ip.tcp.serializer.SoftEndOfStreamException;

/**
 * An OSIPTelegramSerializer is able to read OSIP telegram structures from an InputStream (deserialization) and can also serialize Object
 * structures into OSIP telegrams.
 * 
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version $Revision: $
 * @since 0.2
 */
public class OSIPTelegramSerializer implements Serializer<CommonMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OSIPTelegramSerializer.class);
    private static final byte[] CRLF = "\r\n".getBytes();

    /**
     * FIXME [scherrer] Comment this
     * 
     * @param bite
     * @throws IOException
     */
    protected void checkClosure(int bite) throws IOException {
        if (bite < 0) {
            LOGGER.debug("Socket closed during message assembly");
            throw new IOException("Socket closed during message assembly");
        }
    }

    /**
     * Writes the source object to an output stream using Java Serialization. The source object must implement {@link Serializable}.
     */
    @Override
    public void serialize(CommonMessage object, OutputStream outputStream) throws IOException {
        if (!(object instanceof Serializable)) {
            throw new IllegalArgumentException(getClass().getSimpleName() + " requires a Serializable payload "
                    + "but received an object of type [" + object.getClass().getName() + "]");
        }
        BufferedOutputStream os = new BufferedOutputStream(outputStream);
        os.write(object.toString().getBytes(Charset.defaultCharset()));
        os.write(CRLF);
        os.flush();
    }
}

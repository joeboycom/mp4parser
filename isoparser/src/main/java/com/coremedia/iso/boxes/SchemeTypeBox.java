/*  
 * Copyright 2008 CoreMedia AG, Hamburg
 *
 * Licensed under the Apache License, Version 2.0 (the License); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an AS IS BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */

package com.coremedia.iso.boxes;

import com.coremedia.iso.*;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * The Scheme Type Box identifies the protection scheme. Resides in  a Protection Scheme Information Box or
 * an SRTP Process Box.
 *
 * @see com.coremedia.iso.boxes.SchemeInformationBox
 */
public class SchemeTypeBox extends AbstractFullBox {
    public static final String TYPE = "schm";
    String schemeType = "    ";
    long schemeVersion;
    String schemeUri = null;

    public SchemeTypeBox() {
        super(IsoFile.fourCCtoBytes(TYPE));
    }

    public String getSchemeType() {
        return schemeType;
    }

    public long getSchemeVersion() {
        return schemeVersion;
    }

    public String getSchemeUri() {
        return schemeUri;
    }

    public void setSchemeType(String schemeType) {
        assert schemeType != null && schemeType.length() == 4 : "SchemeType may not be null or not 4 bytes long";
        this.schemeType = schemeType;
    }

    public void setSchemeVersion(int schemeVersion) {
        this.schemeVersion = schemeVersion;
    }

    public void setSchemeUri(String schemeUri) {
        this.schemeUri = schemeUri;
    }

    protected long getContentSize() {
        return 8 + (((getFlags() & 1) == 1) ? Utf8.utf8StringLengthInBytes(schemeUri) + 1 : 0);
    }

    @Override
    public void _parseDetails() {
        parseVersionAndFlags();
        schemeType = IsoTypeReader.read4cc(content);
        schemeVersion = IsoTypeReader.readUInt32(content);
        if ((getFlags() & 1) == 1) {
            schemeUri = IsoTypeReader.readString(content);
        }
    }

    @Override
    protected void getContent(ByteBuffer bb) throws IOException {
        writeVersionAndFlags(bb);
        bb.put(IsoFile.fourCCtoBytes(schemeType));
        IsoTypeWriter.writeUInt32(bb, schemeVersion);
        if ((getFlags() & 1) == 1) {
            bb.put(Utf8.convert(schemeUri));
        }
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Schema Type Box[");
        buffer.append("schemeUri=").append(schemeUri).append("; ");
        buffer.append("schemeType=").append(schemeType).append("; ");
        buffer.append("schemeVersion=").append(schemeUri).append("; ");
        buffer.append("]");
        return buffer.toString();
    }
}

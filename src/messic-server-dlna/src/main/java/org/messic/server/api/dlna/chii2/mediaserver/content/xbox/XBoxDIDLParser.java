package org.messic.server.api.dlna.chii2.mediaserver.content.xbox;

import org.fourthline.cling.model.XMLUtil;
import org.fourthline.cling.support.contentdirectory.DIDLParser;
import org.fourthline.cling.support.model.Res;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * DIDL Parser for XBox360
 */
public class XBoxDIDLParser extends DIDLParser {

    @Override
    protected void generateResource(Res resource, Document descriptor, Element parent) {
        // Call super
        if (resource.getValue() == null) {
            throw new RuntimeException("Missing resource URI value" + resource);
        }
        if (resource.getProtocolInfo() == null) {
            throw new RuntimeException("Missing resource protocol info: " + resource);
        }

        Element resourceElement = XMLUtil.appendNewElement( descriptor, parent, "res", resource.getValue());
        resourceElement.setAttribute("protocolInfo", resource.getProtocolInfo().toString());
        if (resource.getImportUri() != null)
            resourceElement.setAttribute("importUri", resource.getImportUri().toString());
        if (resource.getSize() != null)
            resourceElement.setAttribute("size", resource.getSize().toString());
        if (resource.getDuration() != null)
            resourceElement.setAttribute("duration", resource.getDuration());
        if (resource.getBitrate() != null)
            resourceElement.setAttribute("bitrate", resource.getBitrate().toString());
        if (resource.getSampleFrequency() != null)
            resourceElement.setAttribute("sampleFrequency", resource.getSampleFrequency().toString());
        if (resource.getBitsPerSample() != null)
            resourceElement.setAttribute("bitsPerSample", resource.getBitsPerSample().toString());
        if (resource.getNrAudioChannels() != null)
            resourceElement.setAttribute("nrAudioChannels", resource.getNrAudioChannels().toString());
        if (resource.getColorDepth() != null)
            resourceElement.setAttribute("colorDepth", resource.getColorDepth().toString());
        if (resource.getProtection() != null)
            resourceElement.setAttribute("protection", resource.getProtection());
        if (resource.getResolution() != null)
            resourceElement.setAttribute("resolution", resource.getResolution());

        // XBox Res
        if (resource instanceof XBoxRes) {
            XBoxRes xboxResource = (XBoxRes) resource;
            if (xboxResource.getMicrosoftCodec() != null) {
                resourceElement.setAttribute("microsoft:codec", xboxResource.getMicrosoftCodec());
                resourceElement.setAttribute("xmlns:microsoft", "urn:schemas-microsoft-com:WMPNSS-1-0/");
            }
        }
    }
}

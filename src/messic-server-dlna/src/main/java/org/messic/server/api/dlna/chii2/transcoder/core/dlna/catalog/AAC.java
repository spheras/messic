package org.messic.server.api.dlna.chii2.transcoder.core.dlna.catalog;

import org.fourthline.cling.support.model.dlna.DLNAProfiles;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.codec.AudioCodec;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.codec.Container;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.restriction.AudioRestriction;

/**
 * AAC
 */
public class AAC extends AudioCatalog {
    public AAC() {
        // AAC_ADTS_320
        profileMap.put(DLNAProfiles.AAC_ADTS_320, new AudioRestriction[]{
                new ContainerRestriction(Container.ADTS),
                new AudioCodecRestriction(AudioCodec.AAC_LC),
                new SampleBitRateRangeRestriction(8000, 48000),
                new AudioBitRateRangeRestriction(0, 320000),
                new ChannelRestriction(1, 2)
        });
        // AAC_ADTS
        profileMap.put(DLNAProfiles.AAC_ADTS, new AudioRestriction[]{
                new ContainerRestriction(Container.ADTS),
                new AudioCodecRestriction(AudioCodec.AAC_LC),
                new SampleBitRateRangeRestriction(8000, 48000),
                new AudioBitRateRangeRestriction(0, 576000),
                new ChannelRestriction(1, 2)
        });
        // AAC_MULT5_ADTS
        profileMap.put(DLNAProfiles.AAC_MULT5_ADTS, new AudioRestriction[]{
                new ContainerRestriction(Container.ADTS),
                new AudioCodecRestriction(AudioCodec.AAC_LC),
                new SampleBitRateRangeRestriction(8000, 48000),
                new AudioBitRateRangeRestriction(0, 1440000),
                new ChannelRestriction(1, 6)
        });
        // AAC_320
        profileMap.put(DLNAProfiles.AAC_ISO_320, new AudioRestriction[]{
                new ContainerRestriction(Container.MPEG4),
                new AudioCodecRestriction(AudioCodec.AAC_LC),
                new SampleBitRateRangeRestriction(8000, 48000),
                new AudioBitRateRangeRestriction(0, 320000),
                new ChannelRestriction(1, 2)
        });
        // AAC
        profileMap.put(DLNAProfiles.AAC_ISO, new AudioRestriction[]{
                new ContainerRestriction(Container.MPEG4),
                new AudioCodecRestriction(AudioCodec.AAC_LC),
                new SampleBitRateRangeRestriction(8000, 48000),
                new AudioBitRateRangeRestriction(0, 576000),
                new ChannelRestriction(1, 2)
        });
        // AAC_MULT5
        profileMap.put(DLNAProfiles.AAC_MULT5_ISO, new AudioRestriction[]{
                new ContainerRestriction(Container.MPEG4),
                new AudioCodecRestriction(AudioCodec.AAC_LC),
                new SampleBitRateRangeRestriction(8000, 48000),
                new AudioBitRateRangeRestriction(0, 1440000),
                new ChannelRestriction(1, 6)
        });
        // AAC_LTP
        profileMap.put(DLNAProfiles.AAC_LTP_ISO, new AudioRestriction[]{
                new ContainerRestriction(Container.MPEG4),
                new AudioCodecRestriction(AudioCodec.AAC_LTP),
                new SampleBitRateRangeRestriction(8000, 48000),
                new AudioBitRateRangeRestriction(0, 576000),
                new ChannelRestriction(1, 2)
        });
        // AAC_LTP_MULT5
        profileMap.put(DLNAProfiles.AAC_LTP_MULT5_ISO, new AudioRestriction[]{
                new ContainerRestriction(Container.MPEG4),
                new AudioCodecRestriction(AudioCodec.AAC_LTP),
                new SampleBitRateRangeRestriction(8000, 96000),
                new AudioBitRateRangeRestriction(0, 2880000),
                new ChannelRestriction(1, 6)
        });
        // AAC_LTP_MULT7
        profileMap.put(DLNAProfiles.AAC_LTP_MULT7_ISO, new AudioRestriction[]{
                new ContainerRestriction(Container.MPEG4),
                new AudioCodecRestriction(AudioCodec.AAC_LTP),
                new SampleBitRateRangeRestriction(8000, 96000),
                new AudioBitRateRangeRestriction(0, 4032000),
                new ChannelRestriction(1, 8)
        });
        // HEAAC_L2_ADTS_320
        profileMap.put(DLNAProfiles.HEAAC_L2_ADTS_320, new AudioRestriction[]{
                new ContainerRestriction(Container.ADTS),
                new AudioCodecRestriction(AudioCodec.HEAAC),
                new SampleBitRateRangeRestriction(8000, 24000),
                new AudioBitRateRangeRestriction(0, 320000),
                new ChannelRestriction(1, 2)
        });
        // HEAAC_L2_ADTS
        profileMap.put(DLNAProfiles.HEAAC_L2_ADTS, new AudioRestriction[]{
                new ContainerRestriction(Container.ADTS),
                new AudioCodecRestriction(AudioCodec.HEAAC),
                new SampleBitRateRangeRestriction(8000, 24000),
                new AudioBitRateRangeRestriction(0, 576000),
                new ChannelRestriction(1, 2)
        });
        // HEAAC_L2_320
        profileMap.put(DLNAProfiles.HEAAC_L2_ISO_320, new AudioRestriction[]{
                new ContainerRestriction(Container.MPEG4),
                new AudioCodecRestriction(AudioCodec.HEAAC),
                new SampleBitRateRangeRestriction(8000, 24000),
                new AudioBitRateRangeRestriction(0, 320000),
                new ChannelRestriction(1, 2)
        });
        // HEAAC_L2
        profileMap.put(DLNAProfiles.HEAAC_L2_ISO, new AudioRestriction[]{
                new ContainerRestriction(Container.MPEG4),
                new AudioCodecRestriction(AudioCodec.HEAAC),
                new SampleBitRateRangeRestriction(8000, 24000),
                new AudioBitRateRangeRestriction(0, 576000),
                new ChannelRestriction(1, 2)
        });
        // HEAAC_L3_ADTS
        profileMap.put(DLNAProfiles.HEAAC_L3_ADTS, new AudioRestriction[]{
                new ContainerRestriction(Container.ADTS),
                new AudioCodecRestriction(AudioCodec.HEAAC),
                new SampleBitRateRangeRestriction(8000, 48000),
                new AudioBitRateRangeRestriction(0, 576000),
                new ChannelRestriction(1, 2)
        });
        // HEAAC_MULT5_ADTS
        profileMap.put(DLNAProfiles.HEAAC_MULT5_ADTS, new AudioRestriction[]{
                new ContainerRestriction(Container.ADTS),
                new AudioCodecRestriction(AudioCodec.HEAAC),
                new SampleBitRateRangeRestriction(8000, 48000),
                new AudioBitRateRangeRestriction(0, 1440000),
                new ChannelRestriction(1, 6)
        });
        // HEAAC_L3
        profileMap.put(DLNAProfiles.HEAAC_L3_ISO, new AudioRestriction[]{
                new ContainerRestriction(Container.MPEG4),
                new AudioCodecRestriction(AudioCodec.HEAAC),
                new SampleBitRateRangeRestriction(8000, 48000),
                new AudioBitRateRangeRestriction(0, 576000),
                new ChannelRestriction(1, 2)
        });
        // HEAAC_MULT5
        profileMap.put(DLNAProfiles.HEAAC_MULT5_ISO, new AudioRestriction[]{
                new ContainerRestriction(Container.MPEG4),
                new AudioCodecRestriction(AudioCodec.HEAAC),
                new SampleBitRateRangeRestriction(8000, 48000),
                new AudioBitRateRangeRestriction(0, 1440000),
                new ChannelRestriction(1, 6)
        });
        // HEAACv2_L2_ADTS_320
        profileMap.put(DLNAProfiles.HEAACv2_L2_320_ADTS, new AudioRestriction[]{
                new ContainerRestriction(Container.ADTS),
                new AudioCodecRestriction(AudioCodec.HEAAC_V2),
                new SampleBitRateRangeRestriction(8000, 24000),
                new AudioBitRateRangeRestriction(0, 320000),
                new ChannelRestriction(1, 2)
        });
        // HEAACv2_L2_ADTS
        profileMap.put(DLNAProfiles.HEAACv2_L2_ADTS, new AudioRestriction[]{
                new ContainerRestriction(Container.ADTS),
                new AudioCodecRestriction(AudioCodec.HEAAC_V2),
                new SampleBitRateRangeRestriction(8000, 24000),
                new AudioBitRateRangeRestriction(0, 576000),
                new ChannelRestriction(1, 2)
        });
        // HEAACv2_L2_320
        profileMap.put(DLNAProfiles.HEAACv2_L2_320, new AudioRestriction[]{
                new ContainerRestriction(Container.MPEG4),
                new AudioCodecRestriction(AudioCodec.HEAAC_V2),
                new SampleBitRateRangeRestriction(8000, 24000),
                new AudioBitRateRangeRestriction(0, 320000),
                new ChannelRestriction(1, 2)
        });
        // HEAACv2_L2
        profileMap.put(DLNAProfiles.HEAACv2_L2, new AudioRestriction[]{
                new ContainerRestriction(Container.MPEG4),
                new AudioCodecRestriction(AudioCodec.HEAAC_V2),
                new SampleBitRateRangeRestriction(8000, 24000),
                new AudioBitRateRangeRestriction(0, 576000),
                new ChannelRestriction(1, 2)
        });
        // HEAACv2_L3_ADTS
        profileMap.put(DLNAProfiles.HEAACv2_L3_ADTS, new AudioRestriction[]{
                new ContainerRestriction(Container.ADTS),
                new AudioCodecRestriction(AudioCodec.HEAAC_V2),
                new SampleBitRateRangeRestriction(8000, 48000),
                new AudioBitRateRangeRestriction(0, 576000),
                new ChannelRestriction(1, 2)
        });
        // HEAACv2_MULT5_ADTS
        profileMap.put(DLNAProfiles.HEAACv2_MULT5_ADTS, new AudioRestriction[]{
                new ContainerRestriction(Container.ADTS),
                new AudioCodecRestriction(AudioCodec.HEAAC_V2),
                new SampleBitRateRangeRestriction(8000, 48000),
                new AudioBitRateRangeRestriction(0, 2880000),
                new ChannelRestriction(1, 6)
        });
        // HEAACv2_L3
        profileMap.put(DLNAProfiles.HEAACv2_L3, new AudioRestriction[]{
                new ContainerRestriction(Container.MPEG4),
                new AudioCodecRestriction(AudioCodec.HEAAC_V2),
                new SampleBitRateRangeRestriction(8000, 48000),
                new AudioBitRateRangeRestriction(0, 576000),
                new ChannelRestriction(1, 2)
        });
        // HEAACv2_MULT5
        profileMap.put(DLNAProfiles.HEAACv2_MULT5, new AudioRestriction[]{
                new ContainerRestriction(Container.MPEG4),
                new AudioCodecRestriction(AudioCodec.HEAAC_V2),
                new SampleBitRateRangeRestriction(8000, 48000),
                new AudioBitRateRangeRestriction(0, 2880000),
                new ChannelRestriction(1, 6)
        });
        // BSAC
        profileMap.put(DLNAProfiles.BSAC_ISO, new AudioRestriction[]{
                new ContainerRestriction(Container.MPEG4),
                new AudioCodecRestriction(AudioCodec.BSAC),
                new SampleBitRateRangeRestriction(16000, 48000),
                new AudioBitRateRangeRestriction(0, 128000),
                new ChannelRestriction(1, 2)
        });
        // BSAC_MULT5
        profileMap.put(DLNAProfiles.BSAC_MULT5_ISO, new AudioRestriction[]{
                new ContainerRestriction(Container.MPEG4),
                new AudioCodecRestriction(AudioCodec.BSAC),
                new SampleBitRateRangeRestriction(16000, 48000),
                new AudioBitRateRangeRestriction(0, 128000),
                new ChannelRestriction(1, 6)
        });
    }
}

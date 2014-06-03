package org.messic.server.api.dlna.chii2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Hashtable;

/* Copyright 2002 Erik Peterson 
   Code and program free for non-commercial use.
   Contact erik@mandarintools.com for fees and
   licenses for commercial use.
*/

public class Zhcode extends Encoding {
    // Simplfied/Traditional character equivalence hashes
    protected Hashtable<String,String> s2thash, t2shash;
    public boolean autodetect;
    private int unsupportedStrategy;

    public final static int DELETE = 0;
    public final static int HTMLESC = 1;
    public final static int UNIESC = 2;
    public final static int QUESTIONMARK = 3;
    public final static int TOTAL = 4;

    // Logger
    //protected Logger logger = LoggerFactory.getLogger("org.chii2.util");

    // Constructor
    public Zhcode() {
        super();
        unsupportedStrategy = UNIESC;
        String dataline;
        autodetect = false;

        // Initialize and load in the simplified/traditional character hashses
        s2thash = new Hashtable<String, String>();
        t2shash = new Hashtable<String, String>();

        BufferedReader reader = null;
        try {
            InputStream inputStream = getClass().getResourceAsStream("hcutf8.txt");
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"));
            while ((dataline = reader.readLine()) != null) {
                // Skip empty and commented lines
                if (dataline.length() == 0 || dataline.charAt(0) == '#') {
                    continue;
                }

                // Simplified to Traditional, (one to many, but pick only one)
                s2thash.put(dataline.substring(0, 1), dataline.substring(1, 2));

                // Traditional to Simplified, (many to one)
                for (int i = 1; i < dataline.length(); i++) {
                    t2shash.put(dataline.substring(i, i + 1), dataline.substring(0, 1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //logger.error("Read Chinese encoding mapping file with error: {}.", e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignore) {
                }
            }
        }

    }

    public void setUnsupportedStrategy(int strategy) {
        if (strategy >= 0 && strategy < TOTAL) {
            unsupportedStrategy = strategy;
        }
    }

    public int getUnsupportedStrategy() {
        return unsupportedStrategy;
    }

    public String convertString(String inline, int source_encoding, int target_encoding) {
        StringBuffer outline = new StringBuffer(inline);
        convertStringBuffer(outline, source_encoding, target_encoding);
        return outline.toString();
    }


    public void convertStringBuffer(StringBuffer dataline, int source_encoding, int target_encoding) {
        int lineindex;
        String currchar;
        char charvalue;

        if (source_encoding == HZ) {
            hz2gbStringBuffer(dataline);
        }

        for (lineindex = 0; lineindex < dataline.length(); lineindex++) {
            charvalue = dataline.charAt(lineindex);
            currchar = "" + charvalue;
            if (((int) charvalue == 0xfeff || (int) charvalue == 0xfffe) &&
                    (target_encoding != UNICODE && target_encoding != UNICODES && target_encoding != UNICODET &&
                            target_encoding != UTF8 && target_encoding != UTF8S && target_encoding != UTF8T)) {
                dataline.deleteCharAt(lineindex);
                continue;
            }

            if ((source_encoding == GB2312 || source_encoding == GBK || source_encoding == ISO2022CN_GB ||
                    source_encoding == HZ || source_encoding == GB18030 ||
                    source_encoding == UNICODE || source_encoding == UNICODES || source_encoding == UTF8 ||
                    source_encoding == UTF8S)
                    &&
                    (target_encoding == BIG5 || target_encoding == CNS11643 || target_encoding == UNICODET ||
                            target_encoding == UTF8T ||
                            target_encoding == ISO2022CN_CNS)) {
                if (s2thash.containsKey(currchar)) {
                    dataline.replace(lineindex, lineindex + 1, (String) s2thash.get(currchar));
                }
            } else if ((source_encoding == BIG5 || source_encoding == CNS11643 ||
                    source_encoding == UNICODET ||
                    source_encoding == UTF8 || source_encoding == UTF8T ||
                    source_encoding == ISO2022CN_CNS || source_encoding == GBK ||
                    source_encoding == GB18030 || source_encoding == UNICODE)
                    &&
                    (target_encoding == GB2312 || target_encoding == UNICODES ||
                            target_encoding == ISO2022CN_GB ||
                            target_encoding == UTF8S || target_encoding == HZ)) {
                if (t2shash.containsKey(currchar)) {
                    dataline.replace(lineindex, lineindex + 1, (String) t2shash.get(currchar));
                }
            }
        }

        if (target_encoding == HZ) {
            // Convert to look like HZ
            gb2hzStringBuffer(dataline);
        }

        Charset charset = Charset.forName(javaname[target_encoding]);
        CharsetEncoder encoder = charset.newEncoder();

        for (int i = 0; i < dataline.length(); i++) {
            if (!encoder.canEncode(dataline.subSequence(i, i + 1))) {
                // Replace or delete
                // Delete
                if (unsupportedStrategy == DELETE) {
                    dataline.deleteCharAt(i);
                    i--;
                } else if (unsupportedStrategy == HTMLESC) {
                    // HTML Escape &#xNNNN;
                    dataline.replace(i, i + 1, "&#x" + Integer.toHexString((int) dataline.charAt(i)) + ";");
                } else if (unsupportedStrategy == UNIESC) {
                    // Unicode Escape \\uNNNN
                    dataline.replace(i, i + 1, "\\u" + Integer.toHexString((int) dataline.charAt(i)));
                } else if (unsupportedStrategy == QUESTIONMARK) {
                    // Unicode Escape \\uNNNN
                    dataline.replace(i, i + 1, "?");
                }
            }
        }

    }


    public String hz2gb(String hzstring) {
        StringBuffer gbstring = new StringBuffer(hzstring);
        hz2gbStringBuffer(gbstring);
        return gbstring.toString();
    }


    public void hz2gbStringBuffer(StringBuffer hzstring) {
        byte[] hzbytes; // = new byte[2];
        byte[] gbchar = new byte[2];
        int i = 0;
        StringBuffer gbstring = new StringBuffer("");

        // Convert to look like equivalent Unicode of GB
        for (i = 0; i < hzstring.length(); i++) {
            if (hzstring.charAt(i) == '~') {
                if (hzstring.charAt(i + 1) == '{') {
                    hzstring.delete(i, i + 2);
                    while (i < hzstring.length()) {
                        if (hzstring.charAt(i) == '~' && hzstring.charAt(i + 1) == '}') {
                            hzstring.delete(i, i + 2);
                            i--;
                            break;
                        } else if (hzstring.charAt(i) == '\r' || hzstring.charAt(i) == '\n') {
                            break;
                        }
                        gbchar[0] = (byte) (hzstring.charAt(i) + 0x80);
                        gbchar[1] = (byte) (hzstring.charAt(i + 1) + 0x80);
                        try {
                            hzstring.replace(i, i + 2, new String(gbchar, "GB2312"));
                        } catch (Exception usee) {
                            System.err.println(usee.toString());
                        }
                        i++;
                    }
                } else if (hzstring.charAt(i + 1) == '~') { // ~~ becomes ~
                    hzstring.replace(i, i + 2, "~");
                }
            }
        }

    }


    public String gb2hz(String gbstring) {
        StringBuffer hzbuffer = new StringBuffer(gbstring);
        gb2hzStringBuffer(hzbuffer);
        return hzbuffer.toString();
    }


    public void gb2hzStringBuffer(StringBuffer gbstring) {
        byte[] gbbytes = new byte[2];
        int i;
        boolean terminated = false;

        for (i = 0; i < gbstring.length(); i++) {
            if ((int) gbstring.charAt(i) > 0x7f) {
                gbstring.insert(i, "~{");
                terminated = false;
                while (i < gbstring.length()) {
                    if (gbstring.charAt(i) == '\r' || gbstring.charAt(i) == '\n') {
                        gbstring.insert(i, "~}");
                        i += 2;
                        terminated = true;
                        break;
                    } else if ((int) gbstring.charAt(i) <= 0x7f) {
                        gbstring.insert(i, "~}");
                        i += 2;
                        terminated = true;
                        break;
                    }
                    try {
                        gbbytes = gbstring.substring(i, i + 1).getBytes("GB2312");
                    } catch (UnsupportedEncodingException uee) {
                        System.out.println(uee);
                    }
                    gbstring.delete(i, i + 1);
                    gbstring.insert(i, (char) (gbbytes[0] + 256 - 0x80));
                    gbstring.insert(i + 1, (char) (gbbytes[1] + 256 - 0x80));
                    i += 2;
                }
                if (!terminated) {
                    gbstring.insert(i, "~}");
                    i += 2;
                }
            } else {
                if (gbstring.charAt(i) == '~') {
                    gbstring.replace(i, i + 1, "~~");
                    i++;
                }
            }
        }

    }
}

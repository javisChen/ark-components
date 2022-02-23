
package com.kt.component.oss.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class FileContentTypeUtil {
    private static final Map<String, String> contentTypes = new HashMap<>(485);

    public FileContentTypeUtil() {
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src != null && src.length > 0) {
            for(int i = 0; i < src.length; ++i) {
                int v = src[i] & 255;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }

                stringBuilder.append(hv);
            }

            return stringBuilder.toString();
        } else {
            return null;
        }
    }

    private static String getFileContent(InputStream inputStream) throws IOException {
        byte[] b = new byte[28];
        inputStream.read(b, 0, 28);
        return bytesToHexString(b);
    }

    public static String getType(InputStream inputStream) throws IOException {
        String fileHead = getFileContent(inputStream);
        if (fileHead != null && fileHead.length() != 0) {
            fileHead = fileHead.toUpperCase();
            FileContentType[] fileTypes = FileContentType.values();
            for (FileContentType fileContentType : fileTypes) {
                if (fileHead.startsWith(fileContentType.getValue())) {
                    return fileContentType.getContentType();
                }
            }
            return null;
        } else {
            return null;
        }
    }

    public static String getType(String fileName) {
        int index = fileName.lastIndexOf(46);
        if (index == -1) {
            return null;
        } else {
            String extension = fileName.substring(index + 1);
            return contentTypes.get(extension);
        }
    }

    static {
        contentTypes.put("323", "text/h323");
        contentTypes.put("3gp", "video/3gpp");
        contentTypes.put("aab", "application/x-authoware-bin");
        contentTypes.put("aam", "application/x-authoware-map");
        contentTypes.put("aas", "application/x-authoware-seg");
        contentTypes.put("acx", "application/internet-property-stream");
        contentTypes.put("ai", "application/postscript");
        contentTypes.put("aif", "audio/x-aiff");
        contentTypes.put("aifc", "audio/x-aiff");
        contentTypes.put("aiff", "audio/x-aiff");
        contentTypes.put("als", "audio/X-Alpha5");
        contentTypes.put("amc", "application/x-mpeg");
        contentTypes.put("ani", "application/octet-stream");
        contentTypes.put("apk", "application/vnd.android.package-archive");
        contentTypes.put("asc", "text/plain");
        contentTypes.put("asd", "application/astound");
        contentTypes.put("asf", "video/x-ms-asf");
        contentTypes.put("asn", "application/astound");
        contentTypes.put("asp", "application/x-asap");
        contentTypes.put("asr", "video/x-ms-asf");
        contentTypes.put("asx", "video/x-ms-asf");
        contentTypes.put("au", "audio/basic");
        contentTypes.put("avb", "application/octet-stream");
        contentTypes.put("avi", "video/x-msvideo");
        contentTypes.put("awb", "audio/amr-wb");
        contentTypes.put("axs", "application/olescript");
        contentTypes.put("bas", "text/plain");
        contentTypes.put("bcpio", "application/x-bcpio");
        contentTypes.put("bin ", "application/octet-stream");
        contentTypes.put("bld", "application/bld");
        contentTypes.put("bld2", "application/bld2");
        contentTypes.put("bmp", "image/bmp");
        contentTypes.put("bpk", "application/octet-stream");
        contentTypes.put("bz2", "application/x-bzip2");
        contentTypes.put("c", "text/plain");
        contentTypes.put("cal", "image/x-cals");
        contentTypes.put("cat", "application/vnd.ms-pkiseccat");
        contentTypes.put("ccn", "application/x-cnc");
        contentTypes.put("cco", "application/x-cocoa");
        contentTypes.put("cdf", "application/x-cdf");
        contentTypes.put("cer", "application/x-x509-ca-cert");
        contentTypes.put("cgi", "magnus-internal/cgi");
        contentTypes.put("chat", "application/x-chat");
        contentTypes.put("class", "application/octet-stream");
        contentTypes.put("clp", "application/x-msclip");
        contentTypes.put("cmx", "image/x-cmx");
        contentTypes.put("co", "application/x-cult3d-object");
        contentTypes.put("cod", "image/cis-cod");
        contentTypes.put("conf", "text/plain");
        contentTypes.put("cpio", "application/x-cpio");
        contentTypes.put("cpp", "text/plain");
        contentTypes.put("cpt", "application/mac-compactpro");
        contentTypes.put("crd", "application/x-mscardfile");
        contentTypes.put("crl", "application/pkix-crl");
        contentTypes.put("crt", "application/x-x509-ca-cert");
        contentTypes.put("csh", "application/x-csh");
        contentTypes.put("csm", "chemical/x-csml");
        contentTypes.put("csml", "chemical/x-csml");
        contentTypes.put("css", "text/css");
        contentTypes.put("cur", "application/octet-stream");
        contentTypes.put("dcm", "x-lml/x-evm");
        contentTypes.put("dcr", "application/x-director");
        contentTypes.put("dcx", "image/x-dcx");
        contentTypes.put("der", "application/x-x509-ca-cert");
        contentTypes.put("dhtml", "text/html");
        contentTypes.put("dir", "application/x-director");
        contentTypes.put("dll", "application/x-msdownload");
        contentTypes.put("dmg", "application/octet-stream");
        contentTypes.put("dms", "application/octet-stream");
        contentTypes.put("doc", "application/msword");
        contentTypes.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        contentTypes.put("dot", "application/msword");
        contentTypes.put("dvi", "application/x-dvi");
        contentTypes.put("dwf", "drawing/x-dwf");
        contentTypes.put("dwg", "application/x-autocad");
        contentTypes.put("dxf", "application/x-autocad");
        contentTypes.put("dxr", "application/x-director");
        contentTypes.put("ebk", "application/x-expandedbook");
        contentTypes.put("emb", "chemical/x-embl-dl-nucleotide");
        contentTypes.put("embl", "chemical/x-embl-dl-nucleotide");
        contentTypes.put("eps", "application/postscript");
        contentTypes.put("epub", "application/epub+zip");
        contentTypes.put("eri", "image/x-eri");
        contentTypes.put("es", "audio/echospeech");
        contentTypes.put("esl", "audio/echospeech");
        contentTypes.put("etc", "application/x-earthtime");
        contentTypes.put("etx", "text/x-setext");
        contentTypes.put("evm", "x-lml/x-evm");
        contentTypes.put("evy", "application/envoy");
        contentTypes.put("exe", "application/octet-stream");
        contentTypes.put("fh4", "image/x-freehand");
        contentTypes.put("fh5", "image/x-freehand");
        contentTypes.put("fhc", "image/x-freehand");
        contentTypes.put("fif", "application/fractals");
        contentTypes.put("flr", "x-world/x-vrml");
        contentTypes.put("flv", "flv-application/octet-stream");
        contentTypes.put("fm", "application/x-maker");
        contentTypes.put("fpx", "image/x-fpx");
        contentTypes.put("fvi", "video/isivideo");
        contentTypes.put("gau", "chemical/x-gaussian-input");
        contentTypes.put("gca", "application/x-gca-compressed");
        contentTypes.put("gdb", "x-lml/x-gdb");
        contentTypes.put("gif", "image/gif");
        contentTypes.put("gps", "application/x-gps");
        contentTypes.put("gtar", "application/x-gtar");
        contentTypes.put("gz", "application/x-gzip");
        contentTypes.put("h", "text/plain");
        contentTypes.put("hdf", "application/x-hdf");
        contentTypes.put("hdm", "text/x-hdml");
        contentTypes.put("hdml", "text/x-hdml");
        contentTypes.put("hlp", "application/winhlp");
        contentTypes.put("hqx", "application/mac-binhex40");
        contentTypes.put("hta", "application/hta");
        contentTypes.put("htc", "text/x-component");
        contentTypes.put("htm", "text/html");
        contentTypes.put("html", "text/html");
        contentTypes.put("hts", "text/html");
        contentTypes.put("htt", "text/webviewhtml");
        contentTypes.put("ice", "x-conference/x-cooltalk");
        contentTypes.put("ico", "image/x-icon");
        contentTypes.put("ief", "image/ief");
        contentTypes.put("ifm", "image/gif");
        contentTypes.put("ifs", "image/ifs");
        contentTypes.put("iii", "application/x-iphone");
        contentTypes.put("imy", "audio/melody");
        contentTypes.put("ins", "application/x-internet-signup");
        contentTypes.put("ips", "application/x-ipscript");
        contentTypes.put("ipx", "application/x-ipix");
        contentTypes.put("isp", "application/x-internet-signup");
        contentTypes.put("it", "audio/x-mod");
        contentTypes.put("itz", "audio/x-mod");
        contentTypes.put("ivr", "i-world/i-vrml");
        contentTypes.put("j2k", "image/j2k");
        contentTypes.put("jad", "text/vnd.sun.j2me.app-descriptor");
        contentTypes.put("jam", "application/x-jam");
        contentTypes.put("jar", "application/java-archive");
        contentTypes.put("java", "text/plain");
        contentTypes.put("jfif", "image/pipeg");
        contentTypes.put("jnlp", "application/x-java-jnlp-file");
        contentTypes.put("jpe", "image/jpeg");
        contentTypes.put("jpeg", "image/jpeg");
        contentTypes.put("jpg", "image/jpeg");
        contentTypes.put("jpz", "image/jpeg");
        contentTypes.put("js", "application/x-javascript");
        contentTypes.put("jwc", "application/jwc");
        contentTypes.put("kjx", "application/x-kjx");
        contentTypes.put("lak", "x-lml/x-lak");
        contentTypes.put("latex", "application/x-latex");
        contentTypes.put("lcc", "application/fastman");
        contentTypes.put("lcl", "application/x-digitalloca");
        contentTypes.put("lcr", "application/x-digitalloca");
        contentTypes.put("lgh", "application/lgh");
        contentTypes.put("lha", "application/octet-stream");
        contentTypes.put("lml", "x-lml/x-lml");
        contentTypes.put("lmlpack", "x-lml/x-lmlpack");
        contentTypes.put("log", "text/plain");
        contentTypes.put("lsf", "video/x-la-asf");
        contentTypes.put("lsx", "video/x-la-asf");
        contentTypes.put("lzh", "application/octet-stream");
        contentTypes.put("m13", "application/x-msmediaview");
        contentTypes.put("m14", "application/x-msmediaview");
        contentTypes.put("m15", "audio/x-mod");
        contentTypes.put("m3u", "audio/x-mpegurl");
        contentTypes.put("m3url", "audio/x-mpegurl");
        contentTypes.put("m4a", "audio/mp4a-latm");
        contentTypes.put("m4b", "audio/mp4a-latm");
        contentTypes.put("m4p", "audio/mp4a-latm");
        contentTypes.put("m4u", "video/vnd.mpegurl");
        contentTypes.put("m4v", "video/x-m4v");
        contentTypes.put("ma1", "audio/ma1");
        contentTypes.put("ma2", "audio/ma2");
        contentTypes.put("ma3", "audio/ma3");
        contentTypes.put("ma5", "audio/ma5");
        contentTypes.put("man", "application/x-troff-man");
        contentTypes.put("map", "magnus-internal/imagemap");
        contentTypes.put("mbd", "application/mbedlet");
        contentTypes.put("mct", "application/x-mascot");
        contentTypes.put("mdb", "application/x-msaccess");
        contentTypes.put("mdz", "audio/x-mod");
        contentTypes.put("me", "application/x-troff-me");
        contentTypes.put("mel", "text/x-vmel");
        contentTypes.put("mht", "message/rfc822");
        contentTypes.put("mhtml", "message/rfc822");
        contentTypes.put("mi", "application/x-mif");
        contentTypes.put("mid", "audio/mid");
        contentTypes.put("midi", "audio/midi");
        contentTypes.put("mif", "application/x-mif");
        contentTypes.put("mil", "image/x-cals");
        contentTypes.put("mio", "audio/x-mio");
        contentTypes.put("mmf", "application/x-skt-lbs");
        contentTypes.put("mng", "video/x-mng");
        contentTypes.put("mny", "application/x-msmoney");
        contentTypes.put("moc", "application/x-mocha");
        contentTypes.put("mocha", "application/x-mocha");
        contentTypes.put("mod", "audio/x-mod");
        contentTypes.put("mof", "application/x-yumekara");
        contentTypes.put("mol", "chemical/x-mdl-molfile");
        contentTypes.put("mop", "chemical/x-mopac-input");
        contentTypes.put("mov", "video/quicktime");
        contentTypes.put("movie", "video/x-sgi-movie");
        contentTypes.put("mp2", "video/mpeg");
        contentTypes.put("mp3", "audio/mpeg");
        contentTypes.put("mp4", "video/mp4");
        contentTypes.put("mpa", "video/mpeg");
        contentTypes.put("mpc", "application/vnd.mpohun.certificate");
        contentTypes.put("mpe", "video/mpeg");
        contentTypes.put("mpeg", "video/mpeg");
        contentTypes.put("mpg", "video/mpeg");
        contentTypes.put("mpg4", "video/mp4");
        contentTypes.put("mpga", "audio/mpeg");
        contentTypes.put("mpn", "application/vnd.mophun.application");
        contentTypes.put("mpp", "application/vnd.ms-project");
        contentTypes.put("mps", "application/x-mapserver");
        contentTypes.put("mpv2", "video/mpeg");
        contentTypes.put("mrl", "text/x-mrml");
        contentTypes.put("mrm", "application/x-mrm");
        contentTypes.put("ms", "application/x-troff-ms");
        contentTypes.put("msg", "application/vnd.ms-outlook");
        contentTypes.put("mts", "application/metastream");
        contentTypes.put("mtx", "application/metastream");
        contentTypes.put("mtz", "application/metastream");
        contentTypes.put("mvb", "application/x-msmediaview");
        contentTypes.put("mzv", "application/metastream");
        contentTypes.put("nar", "application/zip");
        contentTypes.put("nbmp", "image/nbmp");
        contentTypes.put("nc", "application/x-netcdf");
        contentTypes.put("ndb", "x-lml/x-ndb");
        contentTypes.put("ndwn", "application/ndwn");
        contentTypes.put("nif", "application/x-nif");
        contentTypes.put("nmz", "application/x-scream");
        contentTypes.put("nokia-op-logo", "image/vnd.nok-oplogo-color");
        contentTypes.put("npx", "application/x-netfpx");
        contentTypes.put("nsnd", "audio/nsnd");
        contentTypes.put("nva", "application/x-neva1");
        contentTypes.put("nws", "message/rfc822");
        contentTypes.put("oda", "application/oda");
        contentTypes.put("ogg", "audio/ogg");
        contentTypes.put("oom", "application/x-AtlasMate-Plugin");
        contentTypes.put("p10", "application/pkcs10");
        contentTypes.put("p12", "application/x-pkcs12");
        contentTypes.put("p7b", "application/x-pkcs7-certificates");
        contentTypes.put("p7c", "application/x-pkcs7-mime");
        contentTypes.put("p7m", "application/x-pkcs7-mime");
        contentTypes.put("p7r", "application/x-pkcs7-certreqresp");
        contentTypes.put("p7s", "application/x-pkcs7-signature");
        contentTypes.put("pac", "audio/x-pac");
        contentTypes.put("pae", "audio/x-epac");
        contentTypes.put("pan", "application/x-pan");
        contentTypes.put("pbm", "image/x-portable-bitmap");
        contentTypes.put("pcx", "image/x-pcx");
        contentTypes.put("pda", "image/x-pda");
        contentTypes.put("pdb", "chemical/x-pdb");
        contentTypes.put("pdf", "application/pdf");
        contentTypes.put("pfr", "application/font-tdpfr");
        contentTypes.put("pfx", "application/x-pkcs12");
        contentTypes.put("pgm", "image/x-portable-graymap");
        contentTypes.put("pict", "image/x-pict");
        contentTypes.put("pko", "application/ynd.ms-pkipko");
        contentTypes.put("pm", "application/x-perl");
        contentTypes.put("pma", "application/x-perfmon");
        contentTypes.put("pmc", "application/x-perfmon");
        contentTypes.put("pmd", "application/x-pmd");
        contentTypes.put("pml", "application/x-perfmon");
        contentTypes.put("pmr", "application/x-perfmon");
        contentTypes.put("pmw", "application/x-perfmon");
        contentTypes.put("png", "image/png");
        contentTypes.put("pnm", "image/x-portable-anymap");
        contentTypes.put("pnz", "image/png");
        contentTypes.put("pot);", "application/vnd.ms-powerpoint");
        contentTypes.put("ppm", "image/x-portable-pixmap");
        contentTypes.put("pps", "application/vnd.ms-powerpoint");
        contentTypes.put("ppt", "application/vnd.ms-powerpoint");
        contentTypes.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        contentTypes.put("pqf", "application/x-cprplayer");
        contentTypes.put("pqi", "application/cprplayer");
        contentTypes.put("prc", "application/x-prc");
        contentTypes.put("prf", "application/pics-rules");
        contentTypes.put("prop", "text/plain");
        contentTypes.put("proxy", "application/x-ns-proxy-autoconfig");
        contentTypes.put("ps", "application/postscript");
        contentTypes.put("ptlk", "application/listenup");
        contentTypes.put("pub", "application/x-mspublisher");
        contentTypes.put("pvx", "video/x-pv-pvx");
        contentTypes.put("qcp", "audio/vnd.qcelp");
        contentTypes.put("qt", "video/quicktime");
        contentTypes.put("qti", "image/x-quicktime");
        contentTypes.put("qtif", "image/x-quicktime");
        contentTypes.put("r3t", "text/vnd.rn-realtext3d");
        contentTypes.put("ra", "audio/x-pn-realaudio");
        contentTypes.put("ram", "audio/x-pn-realaudio");
        contentTypes.put("rar", "application/octet-stream");
        contentTypes.put("ras", "image/x-cmu-raster");
        contentTypes.put("rc", "text/plain");
        contentTypes.put("rdf", "application/rdf+xml");
        contentTypes.put("rf", "image/vnd.rn-realflash");
        contentTypes.put("rgb", "image/x-rgb");
        contentTypes.put("rlf", "application/x-richlink");
        contentTypes.put("rm", "audio/x-pn-realaudio");
        contentTypes.put("rmf", "audio/x-rmf");
        contentTypes.put("rmi", "audio/mid");
        contentTypes.put("rmm", "audio/x-pn-realaudio");
        contentTypes.put("rmvb", "audio/x-pn-realaudio");
        contentTypes.put("rnx", "application/vnd.rn-realplayer");
        contentTypes.put("roff", "application/x-troff");
        contentTypes.put("rp", "image/vnd.rn-realpix");
        contentTypes.put("rpm", "audio/x-pn-realaudio-plugin");
        contentTypes.put("rt", "text/vnd.rn-realtext");
        contentTypes.put("rte", "x-lml/x-gps");
        contentTypes.put("rtf", "application/rtf");
        contentTypes.put("rtg", "application/metastream");
        contentTypes.put("rtx", "text/richtext");
        contentTypes.put("rv", "video/vnd.rn-realvideo");
        contentTypes.put("rwc", "application/x-rogerwilco");
        contentTypes.put("s3m", "audio/x-mod");
        contentTypes.put("s3z", "audio/x-mod");
        contentTypes.put("sca", "application/x-supercard");
        contentTypes.put("scd", "application/x-msschedule");
        contentTypes.put("sct", "text/scriptlet");
        contentTypes.put("sdf", "application/e-score");
        contentTypes.put("sea", "application/x-stuffit");
        contentTypes.put("setpay", "application/set-payment-initiation");
        contentTypes.put("setreg", "application/set-registration-initiation");
        contentTypes.put("sgm", "text/x-sgml");
        contentTypes.put("sgml", "text/x-sgml");
        contentTypes.put("sh", "application/x-sh");
        contentTypes.put("shar", "application/x-shar");
        contentTypes.put("shtml", "magnus-internal/parsed-html");
        contentTypes.put("shw", "application/presentations");
        contentTypes.put("si6", "image/si6");
        contentTypes.put("si7", "image/vnd.stiwap.sis");
        contentTypes.put("si9", "image/vnd.lgtwap.sis");
        contentTypes.put("sis", "application/vnd.symbian.install");
        contentTypes.put("sit", "application/x-stuffit");
        contentTypes.put("skd", "application/x-Koan");
        contentTypes.put("skm", "application/x-Koan");
        contentTypes.put("skp", "application/x-Koan");
        contentTypes.put("skt", "application/x-Koan");
        contentTypes.put("slc", "application/x-salsa");
        contentTypes.put("smd", "audio/x-smd");
        contentTypes.put("smi", "application/smil");
        contentTypes.put("smil", "application/smil");
        contentTypes.put("smp", "application/studiom");
        contentTypes.put("smz", "audio/x-smd");
        contentTypes.put("snd", "audio/basic");
        contentTypes.put("spc", "application/x-pkcs7-certificates");
        contentTypes.put("spl", "application/futuresplash");
        contentTypes.put("spr", "application/x-sprite");
        contentTypes.put("sprite", "application/x-sprite");
        contentTypes.put("sdp", "application/sdp");
        contentTypes.put("spt", "application/x-spt");
        contentTypes.put("src", "application/x-wais-source");
        contentTypes.put("sst", "application/vnd.ms-pkicertstore");
        contentTypes.put("stk", "application/hyperstudio");
        contentTypes.put("stl", "application/vnd.ms-pkistl");
        contentTypes.put("stm", "text/html");
        contentTypes.put("svg", "image/svg+xml");
        contentTypes.put("sv4cpio", "application/x-sv4cpio");
        contentTypes.put("sv4crc", "application/x-sv4crc");
        contentTypes.put("svf", "image/vnd");
        contentTypes.put("svg", "image/svg+xml");
        contentTypes.put("svh", "image/svh");
        contentTypes.put("svr", "x-world/x-svr");
        contentTypes.put("swf", "application/x-shockwave-flash");
        contentTypes.put("swfl", "application/x-shockwave-flash");
        contentTypes.put("t", "application/x-troff");
        contentTypes.put("tad", "application/octet-stream");
        contentTypes.put("talk", "text/x-speech");
        contentTypes.put("tar", "application/x-tar");
        contentTypes.put("taz", "application/x-tar");
        contentTypes.put("tbp", "application/x-timbuktu");
        contentTypes.put("tbt", "application/x-timbuktu");
        contentTypes.put("tcl", "application/x-tcl");
        contentTypes.put("tex", "application/x-tex");
        contentTypes.put("texi", "application/x-texinfo");
        contentTypes.put("texinfo", "application/x-texinfo");
        contentTypes.put("tgz", "application/x-compressed");
        contentTypes.put("thm", "application/vnd.eri.thm");
        contentTypes.put("tif", "image/tiff");
        contentTypes.put("tiff", "image/tiff");
        contentTypes.put("tki", "application/x-tkined");
        contentTypes.put("tkined", "application/x-tkined");
        contentTypes.put("toc", "application/toc");
        contentTypes.put("toy", "image/toy");
        contentTypes.put("tr", "application/x-troff");
        contentTypes.put("trk", "x-lml/x-gps");
        contentTypes.put("trm", "application/x-msterminal");
        contentTypes.put("tsi", "audio/tsplayer");
        contentTypes.put("tsp", "application/dsptype");
        contentTypes.put("tsv", "text/tab-separated-values");
        contentTypes.put("ttf", "application/octet-stream");
        contentTypes.put("ttz", "application/t-time");
        contentTypes.put("txt", "text/plain");
        contentTypes.put("uls", "text/iuls");
        contentTypes.put("ult", "audio/x-mod");
        contentTypes.put("ustar", "application/x-ustar");
        contentTypes.put("uu", "application/x-uuencode");
        contentTypes.put("uue", "application/x-uuencode");
        contentTypes.put("vcd", "application/x-cdlink");
        contentTypes.put("vcf", "text/x-vcard");
        contentTypes.put("vdo", "video/vdo");
        contentTypes.put("vib", "audio/vib");
        contentTypes.put("viv", "video/vivo");
        contentTypes.put("vivo", "video/vivo");
        contentTypes.put("vmd", "application/vocaltec-media-desc");
        contentTypes.put("vmf", "application/vocaltec-media-file");
        contentTypes.put("vmi", "application/x-dreamcast-vms-info");
        contentTypes.put("vms", "application/x-dreamcast-vms");
        contentTypes.put("vox", "audio/voxware");
        contentTypes.put("vqe", "audio/x-twinvq-plugin");
        contentTypes.put("vqf", "audio/x-twinvq");
        contentTypes.put("vql", "audio/x-twinvq");
        contentTypes.put("vre", "x-world/x-vream");
        contentTypes.put("vrml", "x-world/x-vrml");
        contentTypes.put("vrt", "x-world/x-vrt");
        contentTypes.put("vrw", "x-world/x-vream");
        contentTypes.put("vts", "workbook/formulaone");
        contentTypes.put("wav", "audio/x-wav");
        contentTypes.put("wax", "audio/x-ms-wax");
        contentTypes.put("wbmp", "image/vnd.wap.wbmp");
        contentTypes.put("wcm", "application/vnd.ms-works");
        contentTypes.put("wdb", "application/vnd.ms-works");
        contentTypes.put("web", "application/vnd.xara");
        contentTypes.put("wi", "image/wavelet");
        contentTypes.put("wis", "application/x-InstallShield");
        contentTypes.put("wks", "application/vnd.ms-works");
        contentTypes.put("wm", "video/x-ms-wm");
        contentTypes.put("wma", "audio/x-ms-wma");
        contentTypes.put("wmd", "application/x-ms-wmd");
        contentTypes.put("wmf", "application/x-msmetafile");
        contentTypes.put("wml", "text/vnd.wap.wml");
        contentTypes.put("wmlc", "application/vnd.wap.wmlc");
        contentTypes.put("wmls", "text/vnd.wap.wmlscript");
        contentTypes.put("wmlsc", "application/vnd.wap.wmlscriptc");
        contentTypes.put("wmlscript", "text/vnd.wap.wmlscript");
        contentTypes.put("wmv", "audio/x-ms-wmv");
        contentTypes.put("wmx", "video/x-ms-wmx");
        contentTypes.put("wmz", "application/x-ms-wmz");
        contentTypes.put("wpng", "image/x-up-wpng");
        contentTypes.put("wps", "application/vnd.ms-works");
        contentTypes.put("wpt", "x-lml/x-gps");
        contentTypes.put("wri", "application/x-mswrite");
        contentTypes.put("wrl", "x-world/x-vrml");
        contentTypes.put("wrz", "x-world/x-vrml");
        contentTypes.put("ws", "text/vnd.wap.wmlscript");
        contentTypes.put("wsc", "application/vnd.wap.wmlscriptc");
        contentTypes.put("wv", "video/wavelet");
        contentTypes.put("wvx", "video/x-ms-wvx");
        contentTypes.put("wxl", "application/x-wxl");
        contentTypes.put("x-gzip", "application/x-gzip");
        contentTypes.put("xaf", "x-world/x-vrml");
        contentTypes.put("xar", "application/vnd.xara");
        contentTypes.put("xbm", "image/x-xbitmap");
        contentTypes.put("xdm", "application/x-xdma");
        contentTypes.put("xdma", "application/x-xdma");
        contentTypes.put("xdw", "application/vnd.fujixerox.docuworks");
        contentTypes.put("xht", "application/xhtml+xml");
        contentTypes.put("xhtm", "application/xhtml+xml");
        contentTypes.put("xhtml", "application/xhtml+xml");
        contentTypes.put("xla", "application/vnd.ms-excel");
        contentTypes.put("xlc", "application/vnd.ms-excel");
        contentTypes.put("xll", "application/x-excel");
        contentTypes.put("xlm", "application/vnd.ms-excel");
        contentTypes.put("xls", "application/vnd.ms-excel");
        contentTypes.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        contentTypes.put("xlt", "application/vnd.ms-excel");
        contentTypes.put("xlw", "application/vnd.ms-excel");
        contentTypes.put("xm", "audio/x-mod");
        contentTypes.put("xml", "text/plain");
        contentTypes.put("xml", "application/xml");
        contentTypes.put("xmz", "audio/x-mod");
        contentTypes.put("xof", "x-world/x-vrml");
        contentTypes.put("xpi", "application/x-xpinstall");
        contentTypes.put("xpm", "image/x-xpixmap");
        contentTypes.put("xsit", "text/xml");
        contentTypes.put("xsl", "text/xml");
        contentTypes.put("xul", "text/xul");
        contentTypes.put("xwd", "image/x-xwindowdump");
        contentTypes.put("xyz", "chemical/x-pdb");
        contentTypes.put("yz1", "application/x-yz1");
        contentTypes.put("z", "application/x-compress");
        contentTypes.put("zac", "application/x-zaurus-zac");
        contentTypes.put("zip", "application/zip");
        contentTypes.put("json", "application/json");
    }
}

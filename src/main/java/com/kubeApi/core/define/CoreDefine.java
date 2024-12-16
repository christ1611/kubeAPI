package com.kubeApi.core.define;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CoreDefine {

    public static LocalDate     SYSTEM_DATE = LocalDate.now();
    public static LocalDateTime SYSTEM_DATE_TIME = LocalDateTime.now();
    public static String        SYSTEM_TIME = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

    public enum YesNoDvCd {

        YES("Y"),          // YES
        NO("N");          // NO

        private String vlu;

        YesNoDvCd(String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (YesNoDvCd eItm : YesNoDvCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static YesNoDvCd isKindOf(String vlu) {
            for (YesNoDvCd eItm : YesNoDvCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }
    }

    public enum HttpMethCd {

        POST("POST"),
        GET("GET"),
        DELETE("DELETE"),
        PUT("PUT"),
        PATCH("PATCH");

        private String vlu;

        HttpMethCd(String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (HttpMethCd eItm : HttpMethCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static HttpMethCd isKindOf(String vlu) {
            for (HttpMethCd eItm : HttpMethCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }
    }

    public enum NogtKindCd {

        APPLICATION_SEQUENCE("APP_SEQ"),
        EXECUTION_SEQUENCE("EXEC_SEQ"),
        VERSION_NAME("VER_NM");

        private String vlu;

        NogtKindCd(String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (NogtKindCd eItm : NogtKindCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static NogtKindCd isKindOf(String vlu) {
            for (NogtKindCd eItm : NogtKindCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }
    }

    public enum ExecStCd {
        SERVER_CALL_EXECUTE("SERVER_CALL_EXECUTE"), // Batman,Daemon Server Call request
        SERVER_CALL_FAIL("SERVER_CALL_FAIL"),       // Batman,Daemon Server Call request fail
        API_CALL_EXECUTE("API_CALL_EXECUTE"),       // K8S, PODMAN API Call request
        API_CALL_SUCCESS("API_CALL_SUCCESS"),       // K8S, PODMAN API Call success
        API_POD_CREATE("API_POD_CREATE"),           // K8S, PODMAN Pod Create success
        API_POD_FAIL("API_POD_FAIL"),               // K8S, PODMAN Pod Create fail
        API_CALL_FAIL("API_CALL_FAIL"),             // K8S, PODMAN API Call fail
        BATCH_EXECUTE("BATCH_EXECUTE"),             // Batch Execute request
        IMAGE_PULL_FAIL("IMG_PULL_FAIL"),           // PODMAN fail to pull image
        BATCH_SUCCESS("BATCH_SUCCESS"),             // Batch Execute request success
        BATCH_FAIL("BATCH_FAIL");                   // Batch Execute request fail

        private String vlu;

        ExecStCd(String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (ExecStCd eItm : ExecStCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static ExecStCd isKindOf(String vlu) {
            for (ExecStCd eItm : ExecStCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }
    }

    public enum Category   {
        COMMON("Common"),    // Common
        ACCOUNT("Account"),  // Account
        DEPOSIT("Deposit"),  // Deposit
        LOAN("Loan"),        // Loan
        CHANNEL("Channel");  // Channel

        private String vlu;

        Category(String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (Category eItm : Category.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static Category isKindOf(String vlu) {
            for (Category eItm : Category.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }
    }

    public enum BatchType {
        SHORTTERM("ShortTerm"),    // Short Term
        LONGTERM("LongTerm");      // Long Term

        private String vlu;

        BatchType(String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (BatchType eItm : BatchType.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static BatchType isKindOf(String vlu) {
            for (BatchType eItm : BatchType.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }
    }

    public enum AppDvCd {
        INDEPENDENT_APP("INDEPENDENT_APP"),    // Independent Application
        DAEMON_SERVER("DAEMON_SERVER");      // Daemon Server

        private String vlu;

        AppDvCd(String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (AppDvCd eItm : AppDvCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static AppDvCd isKindOf(String vlu) {
            for (AppDvCd eItm : AppDvCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }
    }

    public enum ActStCd {
        ACTIVE("ACTIVE"),    // Independent Application
        INACTIVE("INACTIVE");      // Daemon Server

        private String vlu;

        ActStCd(String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (ActStCd eItm : ActStCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static ActStCd isKindOf(String vlu) {
            for (ActStCd eItm : ActStCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }
    }

}

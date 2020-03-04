package cn.lingyuncraft.builddreamrecode.utils;

import com.sun.istack.internal.NotNull;



public class PublicID {

    public static int checkPublicID(@NotNull String publicID) {
        if (Storage.getDreamList().contains(publicID)) {
            return 1;//publicID被占用
        } else if (publicID.contains("/")
                || publicID.contains("\\")
                || publicID.contains(":")
                || publicID.contains("*")
                || publicID.contains("?")
                || publicID.contains("\"")
                || publicID.contains("<")
                || publicID.contains(">")
                || publicID.contains("|")
                || publicID.contains(".")) {
            return 2;//含有非法字符
        } else {
            return 0;
        }
    }

    public static boolean hasPublicID(@NotNull String publicID) {
        return Storage.getSchematicFile(publicID).exists();
    }
}

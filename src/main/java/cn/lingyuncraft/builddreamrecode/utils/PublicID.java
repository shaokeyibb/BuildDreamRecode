package cn.lingyuncraft.builddreamrecode.utils;

import lombok.NonNull;

public class PublicID {

    public static int checkPublicID(@NonNull String publicID) {
        if (Storage.get().get(publicID) != null) {
            return 1; // publicID 被占用
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
            return 2; // 含有非法字符
        } else {
            return 0;
        }
    }

    public static boolean hasPublicID(@NonNull String publicID) {
        return Storage.get().getSchematicFile(publicID).exists();
    }
}
